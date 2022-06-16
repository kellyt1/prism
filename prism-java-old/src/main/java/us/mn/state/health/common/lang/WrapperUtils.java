package us.mn.state.health.common.lang;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import us.mn.state.health.common.util.CollectionUtils;

public class WrapperUtils  {
  
  public static Byte[] wrap(byte[] bytes) {
    Byte[] wrappedBytes = new Byte[bytes.length];
    for(int i=0; i < bytes.length; i++) {
      wrappedBytes[i] = new Byte(bytes[i]);
    }
    return wrappedBytes;
  }
  
  public static byte[] unwrap(Byte[] bytes) {
    byte[] unwrappedBytes = new byte[bytes.length];
    for(int i=0; i < bytes.length; i++) {
      unwrappedBytes[i] = bytes[i].byteValue();
    }
    return unwrappedBytes;
  }
  
  public static String increment(String number) {
      try {
        if(!StringUtils.nullOrBlank(number)) {
          Long longNumber = new Long(number);
          long longPrim = longNumber.longValue();
          ++longPrim;
          return new Long(longPrim).toString();
        }
      }
      catch(Exception e) {
          //do nothing, can't increment
      }
      return number;
  }
  
    public static Integer increment(Integer integer) {
        return new Integer(integer.intValue() + 1);
    }
  
    public static String add(String orig, String addition) {
        try {
            Long longNumber = new Long(orig);
            long longPrim = longNumber.longValue();
            return new Long(longPrim + Long.parseLong(addition)).toString();
        }
        catch(Exception e) {
          //do nothing, can't increment
        }
        return orig;
    }
    
    public static Double percentToNumber(Double percent) {
        double number = percent.doubleValue();
        return new Double(number/100);
    }
  
    public static Long toLong(String longStr) {
        if(StringUtils.nullOrBlank(longStr)) {
            return null;
        }
        return new Long(longStr);
    }
    
    public static Integer getNextHighestValue(Collection beans, String propertyName) {
        try {
            List list = new ArrayList(beans);
            if(list.size() == 0) {
                return new Integer(1);
            }
            CollectionUtils.sort(list, propertyName, true);
            Object lastItem = CollectionUtils.getLastItem(list);
            Integer currHighest = (Integer)PropertyUtils.getNestedProperty(lastItem, propertyName);
            
            return increment(currHighest);
        }
        catch(Exception e) {
            e.printStackTrace(); //just print the error
        }
        return null;
    }
    
    public static String round(Double value, int decimalPlace) {
        if(value != null) {
            return round(value.toString(), decimalPlace);
        }
        return null;
    }
    
    public static String round(String value, int decimalPlace) {
        if(!StringUtils.nullOrBlank(value)) {
            int decimalIndex = value.indexOf(".");
            if(decimalIndex < 0) {
                value += ".";
                decimalIndex = value.length() - 1;
            }
            int lastIndex = value.length() - 1;
            int endOfNumberIndex = decimalIndex + decimalPlace;
            while(endOfNumberIndex > lastIndex) {
                value += "0";
                lastIndex = value.length() - 1;
            }
            int endOfNumberInt = Integer.parseInt(value.substring(endOfNumberIndex, endOfNumberIndex + 1));
            if(endOfNumberIndex < lastIndex) {
                int roundingIndicator = Integer.parseInt(value.substring(endOfNumberIndex + 1, endOfNumberIndex + 2));
                if(roundingIndicator >= 5) {
                    ++endOfNumberInt;
                }
            }
            return value.substring(0, decimalIndex) + value.substring(decimalIndex, endOfNumberIndex) +
                Integer.toString(endOfNumberInt);
        }
        return value;
    }
}