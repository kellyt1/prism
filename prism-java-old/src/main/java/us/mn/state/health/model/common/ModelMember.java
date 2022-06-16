package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Date;

import us.mn.state.health.common.util.DateUtils;

public class ModelMember implements Serializable {
    protected String insertedBy;
    protected Date insertionDate = new Date();
    protected String updatedBy;
    protected Date updateDate;
    protected Date endDate;
    protected String endedBy;
    protected int version;
    protected String relationshipType;


    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEndedBy() {
        return endedBy;
    }

    public void setEndedBy(String endedBy) {
        this.endedBy = endedBy;
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

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void buildInsertMetaProperties(String username) {
        try {
            this.setInsertedBy(username);
            this.setInsertionDate(DateUtils.formatDate(new Date()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpdateMetaProperties(String username) {
        this.setUpdatedBy(username);
        this.setUpdateDate(new Date());
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getRelationshipType() {
        return relationshipType;
    }
}