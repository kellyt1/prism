package us.mn.state.health.util;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class Notification implements Serializable {
    private Set<Error> errors = new LinkedHashSet<Error>();

    public Set<Error> getErrors() {
        return errors;
    }

    public void addError(Error error) {
        errors.add(error);
    }

    public void addError(String property, String message) {
        addError(new Error(property, message));
    }

    public boolean hasErrors() {
        return 0 != errors.size();
    }

    public static class Error {
        private String property;
        private String message;


        public Error(String property, String message) {
            this.property = property;
            this.message = message;
        }

        public String getMessage() {
            return StringEscapeUtils.escapeHtml(message);
        }

        public String getProperty() {
            return property;
        }


        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Error error = (Error) o;

            if (message != null ? !message.equals(error.message) : error.message != null) return false;
            if (property != null ? !property.equals(error.property) : error.property != null) return false;

            return true;
        }

        public int hashCode() {
            int result;
            result = (property != null ? property.hashCode() : 0);
            result = 31 * result + (message != null ? message.hashCode() : 0);
            return result;
        }
    }
}