package us.mn.state.health.common.lucene;

import java.text.DecimalFormat;

public class NumberUtils {
    private static final DecimalFormat formatter =
            new DecimalFormat("0000000000");
    public static final String TWO_DECIMALS_PATTERN = "#,##0.00";

    public static String pad(int n) {
        return formatter.format(n);
    }

    public static String formatNumber(double number, String pattern){
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);
    }

    public static String formatNumber(float number, String pattern){
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);
    }
}