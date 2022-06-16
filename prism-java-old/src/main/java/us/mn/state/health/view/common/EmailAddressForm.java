package us.mn.state.health.view.common;

import org.apache.struts.validator.ValidatorForm;

public class EmailAddressForm extends ValidatorForm {
    private String contactMechanismId;
    private String startDate;
    private String endDate;
    private String insertedBy;
    private String insertionDate;
    private String terminatedBy;
    private String terminationDate;
    private String mdhOwned;
    private String location;
    private String deviceType;
    private String comments;
    private String changedBy;
    private String changeDate;
    private String emailAddress;
    private String emailType;

    public String getContactMechanismId() {
        return contactMechanismId;
    }

    public void setContactMechanismId(String contactMechanismId) {
        this.contactMechanismId = contactMechanismId;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(String insertionDate) {
        this.insertionDate = insertionDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(String terminationDate) {
        this.terminationDate = terminationDate;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public String getTerminatedBy() {
        return terminatedBy;
    }

    public void setTerminatedBy(String terminatedBy) {
        this.terminatedBy = terminatedBy;
    }

    public String getMdhOwned() {
        return mdhOwned;
    }

    public void setMdhOwned(String mdhOwned) {
        this.mdhOwned = mdhOwned;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }
}
