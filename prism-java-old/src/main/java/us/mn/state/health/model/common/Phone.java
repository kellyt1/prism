package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Date;

public class Phone implements Serializable, Comparable {
    private Long phoneId;
    private String number;
    private Date insertionDate;
    private String insertedBy;
    private String location;
    private String mdhOwned;
    private Date endDate;
    public static final String LAND_PHONE = "LAND_PHONE";
    public static final String CELLPHONE = "CELLPHONE";
    public static final String PAGER = "PAGER";
    public static final String FAX = "FAX";
    public static final String DEFAULT_LOCATION = "WORK";
    public static final String DEFAULT_MDHOWNED = "non-MDH";

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMdhOwned() {
        return mdhOwned;
    }

    public void setMdhOwned(String mdhOwned) {
        this.mdhOwned = mdhOwned;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }


    public int compareTo(Object o) {
        if (o instanceof Phone) {
            return this.getNumber().compareTo(((Phone) o).getNumber());
        }
        return 0;
    }
}
