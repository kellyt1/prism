package us.mn.state.health.domain.repository.util;

import org.apache.commons.lang.StringUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.util.Notification;
import us.mn.state.health.util.NotificationException;
import us.mn.state.health.util.SQLUtils;

public abstract class SQLBuilder {
    public abstract String buildSqlQuery() throws NotificationException;

    public abstract String buildCountSqlQuery() throws NotificationException;

    protected StringBuilder buildSuspenseDateWhereClause(Notification notification, DateWhereClauseInfoValueObject dateWhereClauseInfo) {
        String from = "";
        String to = "";
        from = buildFromClause(dateWhereClauseInfo, from, notification);
        to = buildToClause(dateWhereClauseInfo, to, notification);
        return new StringBuilder().append(from).append(to);
    }

    private String buildFromClause(DateWhereClauseInfoValueObject dateWhereClauseInfo, String from, Notification notification) {
        try {
            if (StringUtils.isNotBlank(dateWhereClauseInfo.getValueFrom())) {
                from = SQLUtils.buildWhereClauseGreaterOrEqualWithDate(
                        dateWhereClauseInfo.getAlias(),
                        dateWhereClauseInfo.getValueFrom(),
                        DateUtils.DEFAULT_DATE_FORMAT);
            }
        } catch (RuntimeException e) {
            notification.addError(new Notification.Error(
                    dateWhereClauseInfo.getPropertyNameFrom(),
                    dateWhereClauseInfo.getErrorMessageFrom()));
        }
        return from;
    }

    private String buildToClause(DateWhereClauseInfoValueObject dateWhereClauseInfo, String to, Notification notification) {
        try {
            if (StringUtils.isNotBlank(dateWhereClauseInfo.getValueTo())) {
                to = SQLUtils.buildWhereClauseLessOrEqualWithDate(
                        dateWhereClauseInfo.getAlias(),
                        dateWhereClauseInfo.getValueTo(),
                        DateUtils.DEFAULT_DATE_FORMAT);
            }
        } catch (RuntimeException e) {
            notification.addError(new Notification.Error(
                    dateWhereClauseInfo.getPropertyNameTo(),
                    dateWhereClauseInfo.getErrorMessageTo()));
        }
        return to;
    }

    /**
     * Data Value Object that encalpsulates the necessary data for building a where clause
     * of a date between a fromDate and a toDate
     */

    public static class DateWhereClauseInfoValueObject {
        private String alias;

        private String valueFrom;
        private String propertyNameFrom;
        private String errorMessageFrom;

        private String valueTo;
        private String propertyNameTo;
        private String errorMessageTo;

        public DateWhereClauseInfoValueObject(String alias, String valueFrom, String propertyNameFrom, String errorMessageFrom, String valueTo, String propertyNameTo, String errorMessageTo) {
            this.alias = alias;
            this.valueFrom = valueFrom;
            this.propertyNameFrom = propertyNameFrom;
            this.errorMessageFrom = errorMessageFrom;
            this.valueTo = valueTo;
            this.propertyNameTo = propertyNameTo;
            this.errorMessageTo = errorMessageTo;
        }


        public String getAlias() {
            return alias;
        }

        public String getValueFrom() {
            return valueFrom;
        }

        public String getPropertyNameFrom() {
            return propertyNameFrom;
        }

        public String getErrorMessageFrom() {
            return errorMessageFrom;
        }

        public String getValueTo() {
            return valueTo;
        }

        public String getPropertyNameTo() {
            return propertyNameTo;
        }

        public String getErrorMessageTo() {
            return errorMessageTo;
        }
    }
}
