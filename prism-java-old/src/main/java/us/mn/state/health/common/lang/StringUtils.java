package us.mn.state.health.common.lang;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.collections.CollectionUtils;

/**
 * String utilities class
 *
 * @author Jason Stull, Shawn Flahave, Lucian Ochian
 */
public class StringUtils {

    ///////////////////////////////////////
    // operations

    /**
     * Strips a Character from the given String
     *
     * @param string String to have character stripped from
     * @param character   character to be stripped
     * @return a String without the character
     */
    public static String stripChar(String string, String character) {
        String newString = "";
        StringTokenizer st = new StringTokenizer(string, character);
        while (st.hasMoreTokens()) {
            newString += st.nextToken();
        }
        if (newString.length() == 0)
            newString = string;
        return newString;
    } // end stripChar


    /**
     * Determines whether a String is null or ""
     *
     * @param string String to be checked
     * @return indicates null or blank
     */
    public static boolean nullOrBlank(String string) {
        return (string == null || string.trim().equals(""));
    }

    /**
     * Trim a String safely, no worries of NullPointerExceptions
     * @return blank String if param is null, trimmed String otherwise
     * @param string
     * @author Shawn Flahave
     * @date 12/05/2005
     */
    public static String trim(String string) {
        if(string == null) {
            return "";
        }
        else {
            return string.trim();
        }
    }

    /*
    public static String[] split(String string, String delim, String escapeChar) {
        
        String[] split = new String[string.length()];
        int splitIndex = 0;
        string = string.trim();
        
        while(true) {
            int escapeIndex = 0;
            int delimIndex = 0;
            
            escapeIndex = string.indexOf(escapeChar);
            delimIndex = string.indexOf(delim);
            
            if(escapeIndex == 0) {
                int endEscapeIndex = string.indexOf(escapeChar, escapeIndex+1);
                split[splitIndex] = string.substring(escapeIndex + 1, endEscapeIndex);
                if(endEscapeIndex+2 <= string.length()) {
                    string = string.substring(endEscapeIndex+2, string.length());
                    ++splitIndex;
                }
                else {
                    return split;
                }
            }
            else {
                if(delimIndex < 0) {
                    split[splitIndex] = string.trim();
                    return split;
                }
                else {
                    split[splitIndex] = string.substring(0, delimIndex);
                    string = string.substring(delimIndex + 1, string.length());
                    string = string.trim();
                    ++splitIndex;
                }
            }     
        }
     
    }*/

    public static String[] split(String string, String delim) {

        String[] split = new String[string.length()];
        int splitIndex = 0;
        string = string.trim();

        while (true) {
            int delimIndex = 0;
            delimIndex = string.indexOf(delim);
            if (delimIndex < 0) {
                split[splitIndex] = string.trim();

                String[] cleanSplit = new String[splitIndex + 1];
                for (int i = 0; i < cleanSplit.length; i++) {
                    cleanSplit[i] = split[i];
                }

                return cleanSplit;
            }
            else {
                split[splitIndex] = string.substring(0, delimIndex);
                string = string.substring(delimIndex + 1, string.length());
                string = string.trim();
                ++splitIndex;
            }
        }
    }

    /**
     * This method is for generating a code from a String
     * replacing the spaces with '_' and the deleting the all the vowels starting with the 2nd one
     *
     * @param name
     * @return the generated code using the name parameter
     */
    public static String generateCodeFromName(String name) {
        final String VOWELS = "aeiouAEIOU";  //and sometimes y and w, but not here

        String _name = name.replace(' ', '_').toUpperCase();
        StringBuffer result = new StringBuffer();
        int flag = 0;

        for (int i = 0; i < _name.length(); i++) {
            int position = VOWELS.indexOf(_name.charAt(i));

            result.append(_name.charAt(i));
            // If the character is a vowle then count it.

            if (position != -1) {
                flag++;
                if (flag > 1) {
                    result.deleteCharAt(result.length() - 1);
                }
            }
        }
        return result.toString();
    }

    /**
     * Construct a java.util.Date object using a String with this format: MM/dd/YYYY
     *
     * @param date
     * @return a java.util.Date object if the parsing was successful, otherwise null
     */

    public static Date formatDateFromString(String date) {
        try {
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date d = (Date) formatter.parse(date);
            return d;

        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * @param number
     * @param numberOfDigits
     * @return the number formatted as a string
     */
    public static String formatStringNumber(String number, int numberOfDigits) {
        StringBuffer stringFormatter = new StringBuffer();
        for (int i = 0; i < numberOfDigits; i++) {
            stringFormatter.append("0");
        }
        String result = new DecimalFormat(stringFormatter.toString()).format(Integer.parseInt(number));
        return result;
    }

    public static String formatCurrencySimple(String value) {
        try {
            if (!StringUtils.nullOrBlank(value)) {
                return new DecimalFormat("#,##0.00").format(new Double(value)).replaceAll(",", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String formatCurrency(String value) {
        try {
            if (!StringUtils.nullOrBlank(value)) {
                return new DecimalFormat("#,##0.00").format(new Double(value));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String toDelimitedString(String[] strings, String delim) {
        String string = null;
        if (strings != null) {
            string = "";
            for (int i = 0; i < strings.length; i++) {
                string += strings[i];
                if (i < (strings.length - 1)) {
                    string += delim;
                }
            }
        }
        return string;
    }

    public static String stripChars(String value, String[] chars) {
        for (int i = 0; value != null && i < chars.length; i++) {
            value = value.replaceAll(chars[i], "");
        }
        return value;
    }

    public static String alphaNumericOnly(String value) {
        if (value != null) {
            int newValueIndex = 0;
            char[] newValue = new char[value.length()];
            char[] chars = value.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if (Character.isLetter(c) || Character.isDigit(c) ||
                        Character.isWhitespace(c)) {
                    newValue[newValueIndex] = c;
                    ++newValueIndex;
                }
            }
            return new String(newValue, 0, newValueIndex);
        }
        return value;
    }
    
    /**
     * uh, I stole this from Apache Commons StringUtils. 
     * 
     * <p>Checks if the String contains only unicode digits.
     * A decimal point is not a unicode digit and returns false.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     * 
     * <pre>
     * StringUtils.isNumeric(null)   = false
     * StringUtils.isNumeric("")     = true
     * StringUtils.isNumeric("  ")   = false
     * StringUtils.isNumeric("123")  = true
     * StringUtils.isNumeric("12 3") = false
     * StringUtils.isNumeric("ab2c") = false
     * StringUtils.isNumeric("12-3") = false
     * StringUtils.isNumeric("12.3") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if only contains digits, and is non-null
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method that removes the nonAlphaNumeric leading characters for a string
     *
     * @param value
     * @return the value without leading nonAlphaNumeric characters
     */
    public static String removeLeadingNonAlphaNumericChars(String value) {
        StringBuffer buffer;
        if (value.length()>0) {
            buffer = new StringBuffer(value);
            char firstChar = buffer.charAt(0);
            if (!Character.isLetterOrDigit(firstChar)) {
                buffer = new StringBuffer(removeLeadingNonAlphaNumericChars(buffer.deleteCharAt(0).toString()));
            }
            return buffer.toString();
        }
        return value;
    }


    public static String escapeSpecialCharactersInLucene(String value) {
        String[] specialCharacters =
                new String[]{"+", "-", "&&", "||", "!", "(",")", "{", "}", "[", "]", "^", "\"", "~", "*", "?", ":", "\\"};
        StringBuffer buffer = new StringBuffer();
        Set sc = new HashSet();
        CollectionUtils.addAll(sc, specialCharacters);
        for(int i = 0; i  < value.length(); i++){
            char c = value.charAt(i);
            String o = String.valueOf(c);
            if(sc.contains(o)){
                buffer.append("\\").append(o);
            }
            else {
                buffer.append(o);
            }
        }
        return buffer.toString();
    }

    public static boolean checkIfStringContainsWord(String string, String word){
        String[] strings = string.split("[\\s\\p{Punct}]");
        Set set  = new HashSet();
        CollectionUtils.addAll(set,strings);
        if(set.contains(word)){
            return true;
        }
        return false;
    }
}


