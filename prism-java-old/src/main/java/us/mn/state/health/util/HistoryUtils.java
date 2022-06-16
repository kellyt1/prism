package us.mn.state.health.util;

import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.lang.StringUtils;
import us.mn.state.health.common.util.DateUtils;

public class HistoryUtils {
    public static String getStringValue(Object bean, String path) {
        String s = null;
        try {
            s = BeanUtils.getProperty(bean, path);
        }
        catch (NestedNullException e) {
            e.getMessage();
        }
        catch (Exception e) {
            throw new RuntimeException("Exception in HistoryUtils.getStringValue(Object bean, String path)", e);
        }
        return StringUtils.isBlank(s) ? "was blank" : s;
    }

    public static String getDateValue(Date value) {
        return value == null ? "was blank" : DateUtils.toString(value);
    }
}
