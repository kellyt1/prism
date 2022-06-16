package us.mn.state.health.model.common;

public class EmailAddress extends ContactMechanism {
    public static final String VENDOR_EMAIL_TYPE = "vendor";
    private String emailAddress;
    private String emailType;

    public EmailAddress() {
    }

    // ********************** Common Methods ********************** //
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailAddress)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        result = (emailAddress != null ? emailAddress.hashCode() : 0);
        result = 29 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "EmailAddress ('" + getContactMechanismId() + "'), " + "value: '" + getEmailAddress() + "'";
    }

    public int compareTo(Object o) {
        if (o instanceof EmailAddress) {
            return this.getContactMechanismId().compareTo(((EmailAddress) o).getContactMechanismId());
        }
        return 0;
    }

    // ************* END COMMON METHODS ****************** //


    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public String getEmailAddress() {
        return emailAddress;
    }


    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }


    public String getEmailType() {
        return emailType;
    }
}
