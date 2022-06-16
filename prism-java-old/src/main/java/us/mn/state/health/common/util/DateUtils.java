package us.mn.state.health.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**
     * Default date Format, "MM/dd/yyyy"
     */
    public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
    public static final String DATE_FORMAT_2DIGITYEAR = "MM/dd/yy";
    public static final String LUCENE_DATE_FORMAT = "yyyyMMdd";

    /**
     * Creates a Java Date with the given pattern
     *
     * @param date    String to be parsed in to date
     * @param pattern
     * @return Java Date object
     * @throws java.text.ParseException
     */
    public static Date createDate(String date, String pattern) throws ParseException {
        DateFormat df = new SimpleDateFormat(pattern);
        df.setLenient(false);
        Date dateObj = df.parse(date);

        Date nextdayminus1milisec = new Date(dateObj.getTime() + 24 * 60 * 60 * 1000 - 1);
        return nextdayminus1milisec;
    }

    /**
     * Creates a Java Date with the default pattern of "MM/dd/yyyy"
     *
     * @param date String to be parsed in to date
     * @return Java Date object
     * @throws java.text.ParseException
     */
    public static Date createDate(String date) throws ParseException {
        return createDate(date, DEFAULT_DATE_FORMAT);
    }

    public static String toString(Date date) {
        return toString(date, DEFAULT_DATE_FORMAT);
    }

    public static String toString(Date date, String format) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat(format);
            df.setLenient(false);
            return df.format(date);
        }
        return null;
    }

    public static Date formatDate(Date in) throws ParseException {
        return formatDate(in, DEFAULT_DATE_FORMAT);
    }

    public static Date formatDate(Date in, String pattern) throws ParseException {
        String inDateStr = toString(in, pattern);
        return createDate(inDateStr, pattern);
    }
    
    public static Date addDaysToDate(Date timeIn, int daysToAdd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timeIn);                        // Set it in the Calendar object
        cal.add(Calendar.DATE, daysToAdd);          // Add days
        return cal.getTime();                   // Return the resulting date
    }    
}