package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Date;

import us.mn.state.health.util.ProxyUtils;

/**
 * This class represents the object code for Item class
 */

public class ObjectCode implements Serializable {
    private String objectCode;
    private String name;
    private Long objectCodeId;
    private Date insertionDate;
    private String insertedBy;

    public static final String UNKNOWN = "UNKWN";

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getObjectCodeId() {
        return objectCodeId;
    }

    public void setObjectCodeId(Long objectCodeId) {
        this.objectCodeId = objectCodeId;
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

    public String getCodeAndName() {
        return getObjectCode() + "-" + getName();
    }


    public String toString() {
        return "ObjectCode{" +
                "objectCode='" + objectCode + "'" +
                ", name='" + name + "'" +
                ", objectCodeId=" + objectCodeId +
                ", insertionDate=" + insertionDate +
                ", insertedBy='" + insertedBy + "'" +
                "}" + "\n";
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (!ProxyUtils.validateClassesForEquals(ObjectCode.class, this, o)) return false;
        ObjectCode that = (ObjectCode) o;

        if (!objectCode.equals(that.getObjectCode())) return false;
        if (!objectCodeId.equals(that.getObjectCodeId())) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = objectCode.hashCode();
        result = 31 * result + objectCodeId.hashCode();
        return result;
    }
}
