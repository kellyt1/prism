package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public abstract class ContactMechanism implements Serializable, Comparable {
    private Long contactMechanismId;
    private Date startDate;
    private Date endDate;
    private String insertedBy;
    private Date insertionDate;
    private String terminatedBy;
    private Date terminationDate;
    private String mdhOwned;
    private String location;
    private String deviceType;
    private String comments;
    private String changedBy;
    private Date changeDate;   
    private Collection personContactMechanismLinks;       //a collection of PersonContactMechanismLink objects

    public ContactMechanism() { }    

    // ********************** Common Methods ********************** //
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof ContactMechanism;
    }

    public int hashCode() {
        int result;
        result = (contactMechanismId != null ? contactMechanismId.hashCode() : 0);
        result = 29 * result + (contactMechanismId != null ? contactMechanismId.hashCode() : 0);
        return result;
    }

    public int compareTo(Object o) {
        if (o instanceof ContactMechanism) {
            return this.getContactMechanismId().compareTo(((ContactMechanism)o).getContactMechanismId());
        }
        return 0;
    }


    public void setContactMechanismId(Long contactMechanismId) {
        this.contactMechanismId = contactMechanismId;
    }


    public Long getContactMechanismId() {
        return contactMechanismId;
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public Date getStartDate() {
        return startDate;
    }


    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public Date getEndDate() {
        return endDate;
    }


    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }


    public String getInsertedBy() {
        return insertedBy;
    }


    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }


    public Date getInsertionDate() {
        return insertionDate;
    }


    public void setTerminatedBy(String terminatedBy) {
        this.terminatedBy = terminatedBy;
    }


    public String getTerminatedBy() {
        return terminatedBy;
    }


    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }


    public Date getTerminationDate() {
        return terminationDate;
    }


    public void setMdhOwned(String mdhOwned) {
        this.mdhOwned = mdhOwned;
    }


    public String getMdhOwned() {
        return mdhOwned;
    }


    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }


    public String getDeviceType() {
        return deviceType;
    }


    public void setComments(String comments) {
        this.comments = comments;
    }


    public String getComments() {
        return comments;
    }


    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }


    public String getChangedBy() {
        return changedBy;
    }


    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }


    public Date getChangeDate() {
        return changeDate;
    }


    public void setLocation(String location) {
        this.location = location;
    }


    public String getLocation() {
        return location;
    }


    public void setPersonContactMechanismLinks(Collection personContactMechanismLinks) {
        this.personContactMechanismLinks = personContactMechanismLinks;
    }


    public Collection getPersonContactMechanismLinks() {
        return personContactMechanismLinks;
    }
}
