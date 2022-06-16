package us.mn.state.health.model.common;

import java.io.Serializable;

import us.mn.state.health.common.lang.StringUtils;

/**
 * Value Object class that formats a name
 */
public class NameSummary implements Serializable {

    private String firstName;
    private String middleName;
    private String lastName;

    public NameSummary(String firstName, String lastName) {
        this(firstName, null, lastName);
    }

    public NameSummary(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String firstAndLast() {
        return firstName + " " + lastName;
    }

    public String firstMiddleAndLast() {
        if (StringUtils.nullOrBlank(middleName)) {
            return firstAndLast();
        }
        return firstName + " " + middleName + " " + lastName;
    }

    public String lastAndFirst() {
        return lastName + ", " + firstName;
    }

    public String lastFirstAndMiddle() {
        if (StringUtils.nullOrBlank(middleName)) {
            return lastAndFirst();
        }
        return lastName + ", " + firstName + " " + middleName;
    }
}