package us.mn.state.health.util;

import java.text.ParseException;
import java.util.Arrays;

import us.mn.state.health.common.util.DateUtils;

public class SQLUtils {
    public static final String[] formats = {DateUtils.DEFAULT_DATE_FORMAT};

    /**
     * @param alias    of the property like: t.description
     * @param keywords
     * @return
     */
    public static String buildWhereClauseUsingAnd(String alias, String keywords, boolean startsWithToken) {
        StringBuilder clause = new StringBuilder();
        String likeToken = startsWithToken ? "%" : "";
        String[] tokens = keywords.trim().split("\\s");
        for (String token : tokens) {
            clause.append(" and ");
            clause.append("lower(");
            clause.append(alias);
            clause.append(") like '");
            clause.append(likeToken);
            clause.append(token.toLowerCase());
            clause.append(likeToken);
            clause.append("'");
        }
        return clause.toString();
    }

    public static String buildWhereClauseUsingOr(String alias, String keywords, boolean startsWithToken) {
        StringBuilder clause = new StringBuilder();
        String[] tokens = keywords.trim().split("\\s");
        String likeToken = startsWithToken ? "%" : "";
        for (String token : tokens) {
            clause.append(" or ");
            clause.append("lower(");
            clause.append(alias);
            clause.append(") like '");
            clause.append(likeToken);
            clause.append(token.toLowerCase());
            clause.append(likeToken);
            clause.append("'");
        }
        return clause.toString();
    }

    public static String buildWhereClauseGreaterOrEqualWithDate(String alias, String date, String format) {
        if (Arrays.asList(formats).contains(format)) {
            try {
                DateUtils.createDate(date, format);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date: " + date);
            }
            return " and " + alias + ">= to_date('" + date + "','" + format.toLowerCase() + "')";
        }
        throw new IllegalArgumentException("Invalid format!");
    }

    public static String buildWhereClauseLessOrEqualWithDate(String alias, String date, String format) {
        if (Arrays.asList(formats).contains(format)) {
            try {
                DateUtils.createDate(date, format);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date: " + date);
            }
            return " and " + alias + "<= to_date('" + date + "','" + format.toLowerCase() + "')";
        }
        throw new IllegalArgumentException("Invalid format!");
    }
}
