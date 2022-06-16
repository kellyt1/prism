package us.mn.state.health.common.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.ListUtils;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;

public class CollectionUtils {

    public static void synchronizeItems(Collection selected, Collection available, String compareField) throws ReflectivePropertyException {
        List selList = (List) selected;
        List availList = (List) available;

        for (int i = 0; i < availList.size(); i++) {
            Object availItem = availList.get(i);
            Object compareFieldVal =
                    PropertyUtils.invokeReadMethodWithPropertyName(availItem, compareField);
            for (int j = 0; j < selList.size(); j++) {
                Object selItem = selList.get(j);
                Object compareFieldVal2 = PropertyUtils.invokeReadMethodWithPropertyName(selItem, compareField);

                if (compareFieldVal.toString().equals(compareFieldVal2.toString())) {
                    availList.remove(i);
                    --i;
                }
            }
        }
    }

    public static void moveItem(Collection from, Collection to, String keyField, Object keyValue) throws ReflectivePropertyException {
        if (isSet(from)) {
            Set fromSet = (Set) from;
            for (Iterator iterator = fromSet.iterator(); iterator.hasNext();) {
                Object fromItem = iterator.next();
                Object fromItemKeyFieldValue = PropertyUtils.invokeReadMethodWithPropertyName(fromItem, keyField);
                if (fromItemKeyFieldValue.toString().equals(keyValue.toString())) {
                    to.add(fromItem);
                    fromSet.remove(fromItem);
                }
            }
        } else {
            List fromList = (List) from;
            for (int i = 0; i < fromList.size(); i++) {
                Object fromItem = fromList.get(i);
                Object fromItemKeyFieldValue = PropertyUtils.invokeReadMethodWithPropertyName(fromItem, keyField);
                if (fromItemKeyFieldValue.toString().equals(keyValue.toString())) {
                    to.add(fromItem);
                    fromList.remove(i);
                }
            }
        }
    }

    public static void sort(Collection collection, String compareFields, boolean ascending) throws ReflectivePropertyException {
        List list = (List) collection;
        int left = 0;
        int right = list.size() - 1;
        quickSort((List) collection, compareFields, left, right, "STRING");
    }

    public static void sort(Collection collection, String compareFields, boolean ascending, String inType) throws ReflectivePropertyException {
        List list = (List) collection;
        int left = 0;
        int right = list.size() - 1;
        quickSort((List) collection, compareFields, left, right, inType);
    }

    private static void quickSort(List list, String compareFields, int left, int right, String inType) throws ReflectivePropertyException {
        if (right > left) {
            Object o1 = list.get(right);
            int i = left - 1;
            int j = right;
            while (true) {
                while (lessThan(list.get(++i), o1, compareFields, inType)) {
                }
                while (j > 0) {
                    if (lessThanOrEqual(list.get(--j), o1, compareFields, inType)) {
                        break;
                    }
                }
                if (i >= j) {
                    break;
                }
                swap(list, i, j);
            }
            swap(list, i, right);
            quickSort(list, compareFields, left, i - 1, inType);
            quickSort(list, compareFields, i + 1, right, inType);
        }

    }

    private static boolean lessThan(Object obj1, Object obj2, String compareFields, String inType) throws ReflectivePropertyException {

        boolean lessThan = (compare(obj1, obj2, compareFields, inType) < 0);
        return lessThan;
    }

    private static boolean lessThanOrEqual(Object obj1, Object obj2, String compareFields, String inType) throws ReflectivePropertyException {
        boolean lessThanOrEqual = (compare(obj1, obj2, compareFields, inType) < 0);
        return lessThanOrEqual;
    }

    private static int compare(Object obj1, Object obj2, String compareFields, String inType) throws ReflectivePropertyException {
        try {
            if (obj1 == null)
                return -1;
            if (obj2 == null)
                return 1;
            int compare = 0;
            String[] fieldList = compareFields.split(",");
            String compareFieldVal1 = "";
            String compareFieldVal2 = "";

            //Get concatenated field 1 values
            for (int i = 0; i < fieldList.length; i++) {
                Object thisFieldValue = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(obj1, fieldList[i].trim());
                if (thisFieldValue == null) {
                    return -1;
                } else {
                    compareFieldVal1 += thisFieldValue.toString();
                }
            }

            //Get concatenated field 2 values
            for (int i = 0; i < fieldList.length; i++) {
                Object thisFieldValue = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(obj2, fieldList[i].trim());
                if (thisFieldValue == null) {
                    return 1;
                } else {
                    compareFieldVal2 += thisFieldValue.toString();
                }
            }

            if (inType.equalsIgnoreCase("INTEGER")) {
                Integer xValue = Integer.valueOf(compareFieldVal1);
                Integer xValue2 = Integer.valueOf(compareFieldVal2);
                compare = xValue.compareTo(xValue2);
            } else {
                compare = compareFieldVal1.compareToIgnoreCase(compareFieldVal2);
            }

            return compare;
        }
        catch (Exception e) {
            throw new ReflectivePropertyException(e);
        }
    }

    private static void swap(List list, int loc1, int loc2) {
        Object loc1Obj = list.get(loc1);
        Object loc2Obj = list.get(loc2);
        list.add(loc1, loc2Obj);
        list.remove(loc1 + 1);
        list.add(loc2, loc1Obj);
        list.remove(loc2 + 1);
    }

    public static Object getObjectFromCollectionById(Collection collection, Object id, String propertyName) {
        try {
            if (id != null) {
                for (Iterator i = collection.iterator(); i.hasNext();) {
                    Object obj = i.next();
                     if (obj != null) {
                        Object tempId = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(obj, propertyName);
                        if (tempId != null) {
                            if (tempId.toString().equals(id.toString())) {
                                return obj;
                            }
                        }
                     }
                }
            }
        }
        catch (Exception e) {
            //do nothing. Item may not be in list       
        }
        return null;
    }

    public static Object getItemBySerialVersion(Collection collection, String serialVersion) {
        try {
            for (Iterator i = collection.iterator(); i.hasNext();) {
                Object obj = i.next();
                if (obj.toString().equals(serialVersion)) {
                    return obj;
                }
            }
        }
        catch (Exception e) {
            //do nothing. Item may not be in list       
        }
        return null;
    }

    public static boolean isSet(Collection collection) {
        return collection.getClass().getInterfaces()[0].getName().equals("java.util.Set");
    }

    public static void assignValueToAll(Collection collection, Object value, String propertyName) {
        try {
            for (Iterator i = collection.iterator(); i.hasNext();) {
                Object obj = i.next();
                org.apache.commons.beanutils.PropertyUtils.setNestedProperty(obj, propertyName, value);
            }
        }
        catch (Exception e) {
            //do nothing for now.
        }
    }

    public static void removeMatchingItems(Collection valuesList,
                                           String indicatorProperty,
                                           Collection target,
                                           String propertyName) throws ReflectivePropertyException {
        for (Iterator i = valuesList.iterator(); i.hasNext();) {
            Object valuesListObj = i.next();
            Object valuesListIndicatorValue = PropertyUtils.invokeReadMethodWithPropertyName(valuesListObj, indicatorProperty);
            if (valuesListIndicatorValue.toString().equalsIgnoreCase("true")) {
                for (Iterator j = target.iterator(); j.hasNext();) {
                    Object targetObj = j.next();
                    Object targetObjId = PropertyUtils.invokeReadMethodWithPropertyName(targetObj, propertyName);
                    Object valuesListId = PropertyUtils.invokeReadMethodWithPropertyName(valuesListObj, propertyName);
                    if (valuesListId != null && targetObjId != null) {
                        if (valuesListId.toString().equals(targetObjId.toString())) {
                            j.remove();
                        }
                    }
                }
            }
        }
    }

    public static Collection removeMatchingItems(Collection valuesList,
                                                 String indicatorProperty,
                                                 String valuesListProperty,
                                                 Collection target,
                                                 String targetListProperty) {
        Collection matches = new ArrayList();

        for (Iterator i = valuesList.iterator(); i.hasNext();) {
            try {
                Object valuesListObj = i.next();
                Object valuesListIndicatorValue = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(valuesListObj, indicatorProperty);

                if (valuesListIndicatorValue.toString().equalsIgnoreCase("true")) {
                    Object valuesListPropertyValue = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(valuesListObj, valuesListProperty);
                    if (valuesListPropertyValue != null) {
                        Object targetListPropertyValue = removeMatchingItem(target, valuesListPropertyValue, targetListProperty);
                        if (targetListPropertyValue != null) {
                            matches.add(targetListPropertyValue);
                        }
                    }
                }
            }
            catch (Exception e) {
                //do nothing, maybe property doesn't exist. Should allow this for mixed lists
            }
        }
        return matches;
    }

    public static Collection removeMatchingItems(Collection target, Object value, String propertyName) {
        Collection matches = new ArrayList();

        for (Iterator i = target.iterator(); i.hasNext();) {
            Object targetObj = i.next();
            Object targetObjIdVal = null;
            try {
                targetObjIdVal =
                        org.apache.commons.beanutils.PropertyUtils.getNestedProperty(targetObj, propertyName);
            }
            catch (Exception e) {
                //do nothing, maybe property doesn't exist. Should allow this for mixed lists
            }
            if (targetObjIdVal != null) {
                if (value.toString().equals(targetObjIdVal.toString())) {
                    matches.add(targetObj);
                    i.remove();
                }
            }
        }
        return matches;
    }


    public static Object removeMatchingItem(Collection target, Object value, String propertyName) {
        Object match = null;

        for (Iterator i = target.iterator(); i.hasNext();) {
            Object targetObj = i.next();
            Object targetObjIdVal = null;
            try {
                targetObjIdVal = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(targetObj, propertyName);
            }
            catch (Exception e) {
                //do nothing, maybe property doesn't exist. Should allow this for mixed lists
            }
            if (targetObjIdVal != null) {
                if (value.toString().equals(targetObjIdVal.toString())) {
                    match = targetObj;
                    i.remove();
                }
            }
        }
        return match;
    }

    public static boolean inList(Collection target, String propertyName, Object value) {
        try {
            for (Iterator i = target.iterator(); i.hasNext();) {
                Object targetObj = i.next();
                Object targetObjIdVal = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(targetObj, propertyName);
                if (targetObjIdVal != null) {
                    if (value.toString().equals(targetObjIdVal.toString())) {
                        return true;
                    }
                }
            }
        }
        catch (Exception e) {
            //do nothing
        }
        return false;
    }

    public static Collection getMatchingItems(Collection target, Object value, String propertyName) {
        Collection matches = new ArrayList();

        for (Iterator i = target.iterator(); i.hasNext();) {
            Object targetObj = i.next();
            Object targetObjIdVal = null;
            try {
                targetObjIdVal = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(targetObj, propertyName);

                if (targetObjIdVal != null) {
                    if (value.toString().equals(targetObjIdVal.toString())) {
                        matches.add(targetObj);
                    }
                }
            }
            catch (Exception e) {
                //do nothing, maybe property doesn't exist. Should allow this for mixed lists
            }
        }
        return matches;
    }

    public static Object getLastItem(Collection beans) {
        for (Iterator i = beans.iterator(); i.hasNext();) {
            Object item = i.next();
            if (!i.hasNext()) {
                return item;
            }
        }
        return null;
    }

    public static Collection getItemsWherePropertyNotNull(Collection beans, String propertyName) {
        Collection matches = new ArrayList();
        for (Iterator i = beans.iterator(); i.hasNext();) {
            Object item = i.next();
            try {
                Object propertyValue = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(item, propertyName);
                if (propertyValue != null) {
                    matches.add(item);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return matches;
    }

    public static Collection getItemPropertiesWherePropertyIsNull(Collection beans, String itemPropertyName, String propertyName) {
        Collection matches = new ArrayList();
        for (Iterator i = beans.iterator(); i.hasNext();) {
            Object item = i.next();
            try {
                Object itemProperty = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(item, itemPropertyName);
                Object property = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(itemProperty, propertyName);
                if (property == null) {
                    matches.add(itemProperty);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return matches;
    }

    public static Collection getItemsWherePropertyIsNull(Collection beans, String propertyName) {
        Collection matches = new ArrayList();
        for (Iterator i = beans.iterator(); i.hasNext();) {
            Object item = i.next();
            try {
                Object property = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(item, propertyName);
                if (property == null || StringUtils.nullOrBlank(property.toString())) {
                    matches.add(item);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return matches;
    }

    public static List getNestedItems(Collection beans, String propertyName) {
        List matches = new ArrayList();
        for (Iterator i = beans.iterator(); i.hasNext();) {
            Object item = i.next();
            try {
                Object property = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(item, propertyName);
                matches.add(property);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return matches;
    }

    public static Collection getWhereItemNotPresent(Collection source, Collection target, String propertyName) {
        Collection matches = new ArrayList();
        for (Iterator i = source.iterator(); i.hasNext();) {
            Object sourceItem = i.next();
            try {
                Object sourceItemProperty = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(sourceItem, propertyName);
                if (!inList(target, propertyName, sourceItemProperty)) {
                    matches.add(sourceItem);
                }
            }
            catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return matches;
    }

    /**
     * Add objects from the source Collection to the target Collection if the objects are not already
     * present in the target collection.  Use the propertyName property for comparing the source objects to
     * the target objects.
     *
     * @param propertyName
     * @param target
     * @param source
     */
    public static void addWhereItemNotPresent(Collection source, Collection target, String propertyName) {
        for (Iterator i = source.iterator(); i.hasNext();) {
            Object sourceItem = i.next();
            try {
                Object sourceItemProperty = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(sourceItem, propertyName);
                if (!inList(target, propertyName, sourceItemProperty)) {
                    target.add(sourceItem);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Object replaceInList(Collection beans, Object newBean, Object id, String idProperty) {
        List list = (List) beans;
        for (int i = 0; i < list.size(); i++) {
            Object bean = list.get(i);
            try {
                Object targetId = org.apache.commons.beanutils.PropertyUtils.getNestedProperty(bean, idProperty);
                if (targetId != null) {
                    if (id.toString().equalsIgnoreCase(targetId.toString())) {
                        Object oldBean = list.remove(i);
                        list.add(i, newBean);
                        return oldBean;
                    }
                }
            }
            catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * This method re-ranks a collection of link objects like ExternalOrgDetailRep,ExtOrgDetailEmailAddress...
     *
     * @param linkObjects
     */
    public static void reRankCollection(Collection linkObjects) throws ReflectivePropertyException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ArrayList list = new ArrayList(linkObjects);
        CollectionUtils.sort(list, "rank", true);
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            org.apache.commons.beanutils.PropertyUtils.setSimpleProperty(o, "rank", new Integer(i + 1));
        }
    }

    /**
     * @param linkObjects
     * @param propertyName - the property that represents the rank
     * @throws ReflectivePropertyException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void reRankCollection(Collection linkObjects, String propertyName) throws ReflectivePropertyException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ArrayList list = new ArrayList(linkObjects);
        CollectionUtils.sort(list, propertyName, true);
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            org.apache.commons.beanutils.PropertyUtils.setSimpleProperty(o, propertyName, new Integer(i + 1));
        }
    }

    /**
     * @param sourceList
     * @param offset
     * @return a list containing the source list paginated using the given offset
     * @throws IllegalArgumentException if sourceList == null or offset < 1
     */
    public static List<List> paginateList(List sourceList, int offset) {
        if (sourceList == null) {
            throw new IllegalArgumentException("Invalid argument: the source List must not be null");
        }
        if (offset < 1) {
            throw new IllegalArgumentException("Invalid argument: the offset value must be > 0");
        }
        List<List> result = new ArrayList<List>();
        List subList = new ArrayList();
        while (sourceList.size() > offset) {
            subList = sourceList.subList(0, offset);
            sourceList = ListUtils.subtract(sourceList, subList);
            result.add(subList);
        }
        if (sourceList.size() > 0) {
            result.add(sourceList);
        }
        return result;
    }
}