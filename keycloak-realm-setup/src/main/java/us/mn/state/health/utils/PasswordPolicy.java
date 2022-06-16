package us.mn.state.health.utils;

/**
 * User: ochial1
 * Date: 6/29/2016
 * Time: 11:33 AM
 */
public enum PasswordPolicy {

    PROD("length(10) and hashAlgorithm and digits(1) and upperCase(1) and lowerCase(1) and passwordHistory(3) and hashIterations(1000) and notUsername(1) and "),
    LOCAL(""),
    ;
    String value;

    PasswordPolicy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
