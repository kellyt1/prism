package us.mn.state.health.util.struts;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import us.mn.state.health.common.util.DateUtils;

public class ValidationUtils {
    public static Integer validateIntegerGT(boolean required, String sourceValue, int gtValue, ActionMessages messages, String messageKey, Object[] messageValues, String propertyName) {
        if (notRequiredAndBlank(required, sourceValue)) return null;
        Integer integer = null;
        try {
            integer = Integer.parseInt(sourceValue);
            if (integer < gtValue) {
                throw new IllegalArgumentException();
            }
        }
        catch (Exception e) {
            addErrorMesage(messageKey, messageValues, messages, propertyName);
        }

        return integer;
    }

    public static Double validateDoubleGT(boolean required, String sourceValue, int gtValue, ActionMessages messages, String messageKey, Object[] messageValues, String propertyName) {
        if (notRequiredAndBlank(required, sourceValue)) return null;
        Double double_ = null;
        try {
            double_ = Double.parseDouble(sourceValue);
            if (double_ < gtValue) {
                throw new IllegalArgumentException();
            }
        }
        catch (Exception e) {
            addErrorMesage(messageKey, messageValues, messages, propertyName);
        }

        return double_;
    }

    public static Date validateDate(boolean required, String sourceValue, ActionMessages messages, String messageKey, Object[] messageValues, String propertyName) {
        if (notRequiredAndBlank(required, sourceValue)) return null;
        Date date = null;
        try {
            date = DateUtils.createDate(sourceValue);
        }
        catch (ParseException e) {
            addErrorMesage(messageKey, messageValues, messages, propertyName);
        }
        return date;
    }

    /**
     * @param required
     * @param sourceValue
     * @param messages
     * @param messageKey
     * @param messageValues
     * @param propertyName
     * @return true if it passed the validation, or false otherwise
     */
    public static boolean validateRequired(boolean required, String sourceValue, ActionMessages messages, String messageKey, Object[] messageValues, String propertyName) {
        if (notRequiredAndBlank(required, sourceValue)) return true;
        if (requiredAndBlank(required, sourceValue)) {
            addErrorMesage(messageKey, messageValues, messages, propertyName);
            return false;
        }
        return true;
    }

    private static void addErrorMesage(String messageKey, Object[] messageValues, ActionMessages messages, String propertyName) {
        ActionMessage msg = new ActionMessage(messageKey, messageValues);
        messages.add(propertyName, msg);
        throw new RuntimeException();
    }

    private static boolean notRequiredAndBlank(boolean required, String sourceValue) {
        return !required && StringUtils.isBlank(sourceValue);
    }

    private static boolean requiredAndBlank(boolean required, String sourceValue) {
        return required && StringUtils.isBlank(sourceValue);
    }
}
