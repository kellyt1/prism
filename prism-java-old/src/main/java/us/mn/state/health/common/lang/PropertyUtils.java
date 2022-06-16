package us.mn.state.health.common.lang;

import java.lang.reflect.Method;
import java.util.Date;

import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.util.DateUtils;

/**
 * Contains utilites used to ease the use of reflection and object properties
 *
 * @author Jason Stull
 */
public class PropertyUtils {

    ///////////////////////////////////////
    // operations

    /**
     * Copies properties from one object to another.
     * The 'from' methods must start with "get" and they must return a java Wrapper type or a
     * String or a java.util.Date or java.sql.Date.
     *
     * @param to   object to copy properties to
     * @param from object to copy properties from
     */
    public static void copyProperties(Object to, Object from) throws ReflectivePropertyException {
        Method[] fromMethods = from.getClass().getMethods();

        try {
            for (int i = 0; i < fromMethods.length; i++) {
                Method fromMethod = fromMethods[i];
                if (isReadMethod(fromMethod)) {
                    if (isCopyable(fromMethod.getReturnType())) {
                        String fromFieldName = getMethodPropertyName(fromMethod.getName());
                        Object value = invokeReadMethodWithPropertyName(from, fromFieldName);
                        invokeWriteMethodWithPropertyName(to, fromFieldName, value);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ReflectivePropertyException(e);
        }
    } // end copyProperties

    /**
     * Creates Read Method or "Getter" name from a given property name
     *
     * @param propertyName
     * @return read method name
     */
    public static String createReadMethodName(String propertyName) {
        String cleanString = StringUtils.stripChar(propertyName, " ");
        String prefix = "get";
        String suffix = cleanString.substring(0, 1).toUpperCase() +
                cleanString.substring(1, cleanString.length());
        return prefix + suffix;
    }

    /**
     * Creates Write Method or "Setter" name from a given property name
     *
     * @param propertyName
     * @return write method name
     */
    public static String createWriteMethodName(String propertyName) {
        String cleanString = StringUtils.stripChar(propertyName, " ");
        String prefix = "set";
        String suffix = cleanString.substring(0, 1).toUpperCase() +
                cleanString.substring(1, cleanString.length());
        return prefix + suffix;
    }

    public static Object invokeReadMethodWithPropertyName(Object obj, String propertyName) throws ReflectivePropertyException {
        try {
            Object returnValue = null;
            String methodName = createReadMethodName(propertyName);
            Method m = getMethod(obj, methodName);
            if (m != null) {
                returnValue = m.invoke(obj, new Object[]{});
            }
            return returnValue;
        }
        catch (Exception e) {
            throw new ReflectivePropertyException("Failed invoking read method on property: " + propertyName, e);
        }
    }

    /*
    * @todo fix method lookup by name
    */
    public static void invokeWriteMethodWithPropertyName(Object obj,
                                                         String propertyName,
                                                         Object paramValue) throws ReflectivePropertyException {
        try {
            String methodName = createWriteMethodName(propertyName);
            Method m = null;
            m = getMethod(obj, methodName);
            if (m != null && isWriteMethod(m)) {
                paramValue = convert(m.getParameterTypes()[0], paramValue);
                Object[] params = {paramValue};
                m.invoke(obj, params);
            }
        }
        catch (Exception e) {
            throw new ReflectivePropertyException("Failed invoking write method on property: " + propertyName, e);
        }
    }

    public static Method getMethod(Object obj, String methodName) {
        Method[] methods = obj.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(methodName))
                return methods[i];
        }
        return null;
    }

    private static Object convert(Class toType, Object paramValue) throws ReflectivePropertyException {
        //Null value. No conversion necessary
        if (paramValue != null) {
            //From type and to type are the same. No conversion necessary
            if (toType.getName().equals(paramValue.getClass().getName())) {
                return paramValue;
            } else if (toType.isPrimitive()) {
                return paramValue;
            }
            //We need to do a conversion
            else {
                if (paramValue.getClass().getName().equals("java.lang.String")
                        && ((String) paramValue).trim().equals("")) {
                    return null;
                } else if (stringToWrapperConversion(toType, paramValue)) {
                    return convertToWrapper(toType, paramValue);
                } else if (wrapperToStringConversion(toType, paramValue)) {
                    return paramValue.toString();
                } else if (stringToDateConversion(toType, paramValue)) { //Date conversion
                    return convertStringToDate(paramValue);
                } else if (dateToStringConversion(toType, paramValue)) {
                    return convertDateToString(paramValue);
                } else {
                    if (dateToDateConversion(toType, paramValue)) {
                        return paramValue;
                    }
                }
            }
        }
        return null;
    }

    private static boolean dateToDateConversion(Class toType, Object paramValue) {
        return (paramValue instanceof Date || paramValue instanceof java.sql.Date);
    }

    private static boolean dateToStringConversion(Class toType, Object paramValue) {
        return (toType.getName().endsWith("java.lang.String") &&
                (paramValue.getClass().getName().equals("java.util.Date")
                        || paramValue.getClass().getName().equals("java.sql.Date")));
    }

    private static boolean stringToDateConversion(Class toType, Object paramValue) {
        return (paramValue.getClass().getName().endsWith("java.lang.String") &&
                (toType.getName().equals("java.util.Date")
                        || toType.getName().equals("java.sql.Date")));
    }

    private static boolean wrapperToStringConversion(Class toType, Object paramValue) {

        return (toType.getName().equals("java.lang.String") &&
                isWrapperType(paramValue.getClass()));
    }

    private static boolean stringToWrapperConversion(Class toType, Object paramValue) {
        return (paramValue.getClass().getName().equals("java.lang.String") &&
                isWrapperType(toType));
    }

    private static Object convertDateToString(Object paramValue)
            throws ReflectivePropertyException {

        Date date = (Date) paramValue;
        try {
            return DateUtils.toString(date);
        }
        catch (Exception e) {
            throw new ReflectivePropertyException(e);
        }
    }

    private static Object convertStringToDate(Object paramValue)
            throws ReflectivePropertyException {

        String dateStr = (String) paramValue;
        Date date = null;
        try {
            date = DateUtils.createDate(dateStr);
            return date;
        }
        catch (Exception e) {
            throw new ReflectivePropertyException(e);
        }
    }

    private static Object convertToWrapper(Class toType,
                                           Object paramValue) throws ReflectivePropertyException {

        try {
            Class fromType = paramValue.getClass();
            paramValue = paramValue.toString().replaceAll(",", "");
            Object[] inputParams = {paramValue};
            Class[] inputParamTypes = {fromType};

            return toType.getConstructor(inputParamTypes).newInstance(inputParams);
        }
        catch (Exception e) {
            throw new ReflectivePropertyException(e);
        }
    }

    public static boolean isCollectionType(Class type) {
        Class[] interfaces = type.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            if (interfaces[i].getName().equals("java.util.Collection")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPrimitiveType(Class type) {
        return type.isPrimitive();
    }

    public static boolean isWrapperType(Class type) {
        if (type.getName().equals("java.lang.Integer"))
            return true;
        else if (type.getName().equals("java.lang.Long"))
            return true;
        else if (type.getName().equals("java.lang.Double"))
            return true;
        else if (type.getName().equals("java.lang.Float"))
            return true;
        else if (type.getName().equals("java.lang.Boolean"))
            return true;
        else if (type.getName().equals("java.lang.Byte"))
            return true;
        else if (type.getName().equals("java.lang.Character"))
            return true;
        else
            return false;
    }

    private static boolean isCopyable(Class type) {
        if (isWrapperType(type))
            return true;
        if (type.getName().equals("java.lang.String"))
            return true;
        if (type.getName().equals("java.util.Date") || type.getName().equals("java.sql.Date"))
            return true;
        return false;
    }

    private static String getMethodPropertyName(String methodName) {
        String propertyName = methodName.substring(3, methodName.length());
        String firstChar = propertyName.substring(0, 1).toLowerCase();
        return firstChar + propertyName.substring(1, propertyName.length());
    }

    private static boolean isReadMethod(Method method) {
        if (method.getParameterTypes().length > 0)
            return false;
        if (!method.getName().substring(0, 3).equals("get"))
            return false;
        return true;
    }

    private static boolean isWriteMethod(Method method) {
        if (method.getParameterTypes().length == 0)
            return false;
        if (!method.getName().substring(0, 3).equals("set"))
            return false;
        return true;
    }

    public static Object getProperty(Object bean, String property) {
        try {
            return org.apache.commons.beanutils.PropertyUtils.getNestedProperty(bean, property);
        }
        catch (Exception e) {

        }
        return null;
    }

    public static String getPropertyAsString(Object form, String property) {
        Object value = getProperty(form, property);
        if (value != null) {
            if (!StringUtils.nullOrBlank(value.toString())) {
                return value.toString();
            }
        }
        return null;
    }
} // end PropertyUtils



