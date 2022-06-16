package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

import us.mn.state.health.common.util.CollectionUtils;

public class Position implements Serializable {
    private Long positionId;
    private String positionControlNbr;
    private String workingTitle;
    private String insertedBy;
    private Date insertionDate;
    private Date positionEndDate;
    private String classCode;
    private Float positionFtePrctRt;
    private Date effDt;
    private Date positionStartDate;
    private Date classStartDate;
    private String brgngUnitNumber;
    private String comments;
    private Collection personPositionLinks = new TreeSet();
    private Collection facilities = new TreeSet();

    public static final String ASST_DIV_DIRECTOR_CLASS_CODE = "001827";

    public Long getPositionId() {
        return positionId;
    }

    public Collection getPersonPositionLinks() {
        return personPositionLinks;
    }

    public void setPersonPositionLinks(Collection personPositionLinks) {
        this.personPositionLinks = personPositionLinks;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public String getPositionControlNbr() {
        return positionControlNbr;
    }

    public void setPositionControlNbr(String positionControlNbr) {
        this.positionControlNbr = positionControlNbr;
    }

    public String getWorkingTitle() {
        return workingTitle;
    }

    public void setWorkingTitle(String workingTitle) {
        this.workingTitle = workingTitle;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public Date getPositionEndDate() {
        return positionEndDate;
    }

    public void setPositionEndDate(Date positionEndDate) {
        this.positionEndDate = positionEndDate;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public Float getPositionFtePrctRt() {
        return positionFtePrctRt;
    }

    public void setPositionFtePrctRt(Float positionFtePrctRt) {
        this.positionFtePrctRt = positionFtePrctRt;
    }

    public Date getEffDt() {
        return effDt;
    }

    public void setEffDt(Date effDt) {
        this.effDt = effDt;
    }

    public Date getPositionStartDate() {
        return positionStartDate;
    }

    public void setPositionStartDate(Date positionStartDate) {
        this.positionStartDate = positionStartDate;
    }

    public Date getClassStartDate() {
        return classStartDate;
    }

    public void setClassStartDate(Date classStartDate) {
        this.classStartDate = classStartDate;
    }

    public String getBrgngUnitNumber() {
        return brgngUnitNumber;
    }

    public void setBrgngUnitNumber(String brgngUnitNumber) {
        this.brgngUnitNumber = brgngUnitNumber;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;

        final Position position = (Position) o;

        if (brgngUnitNumber != null ? !brgngUnitNumber.equals(position.brgngUnitNumber) : position.brgngUnitNumber != null)
            return false;
        if (classCode != null ? !classCode.equals(position.classCode) : position.classCode != null) return false;
        if (classStartDate != null ? !classStartDate.equals(position.classStartDate) : position.classStartDate != null)
            return false;
        if (comments != null ? !comments.equals(position.comments) : position.comments != null) return false;
        if (effDt != null ? !effDt.equals(position.effDt) : position.effDt != null) return false;
        if (insertedBy != null ? !insertedBy.equals(position.insertedBy) : position.insertedBy != null) return false;
        if (insertionDate != null ? !insertionDate.equals(position.insertionDate) : position.insertionDate != null)
            return false;
        if (personPositionLinks != null ? !personPositionLinks.equals(position.personPositionLinks) : position.personPositionLinks != null)
            return false;
        if (positionControlNbr != null ? !positionControlNbr.equals(position.positionControlNbr) : position.positionControlNbr != null)
            return false;
        if (positionEndDate != null ? !positionEndDate.equals(position.positionEndDate) : position.positionEndDate != null)
            return false;
        if (positionFtePrctRt != null ? !positionFtePrctRt.equals(position.positionFtePrctRt) : position.positionFtePrctRt != null)
            return false;
        if (!positionId.equals(position.positionId)) return false;
        if (positionStartDate != null ? !positionStartDate.equals(position.positionStartDate) : position.positionStartDate != null)
            return false;
        if (workingTitle != null ? !workingTitle.equals(position.workingTitle) : position.workingTitle != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = positionId.hashCode();
        result = 29 * result + (positionControlNbr != null ? positionControlNbr.hashCode() : 0);
        result = 29 * result + (workingTitle != null ? workingTitle.hashCode() : 0);
        result = 29 * result + (insertedBy != null ? insertedBy.hashCode() : 0);
        result = 29 * result + (insertionDate != null ? insertionDate.hashCode() : 0);
        result = 29 * result + (positionEndDate != null ? positionEndDate.hashCode() : 0);
        result = 29 * result + (classCode != null ? classCode.hashCode() : 0);
        result = 29 * result + (positionFtePrctRt != null ? positionFtePrctRt.hashCode() : 0);
        result = 29 * result + (effDt != null ? effDt.hashCode() : 0);
        result = 29 * result + (positionStartDate != null ? positionStartDate.hashCode() : 0);
        result = 29 * result + (classStartDate != null ? classStartDate.hashCode() : 0);
        result = 29 * result + (brgngUnitNumber != null ? brgngUnitNumber.hashCode() : 0);
        result = 29 * result + (comments != null ? comments.hashCode() : 0);
//        result = 29 * result + (personPositionLinks != null ? personPositionLinks.hashCode() : 0);
        return result;
    }


    public void setFacilities(Collection facilities) {
        this.facilities = facilities;
    }


    public Collection getFacilities() {
        return facilities;
    }

    public Facility getPrimaryFacility() {
        PositionFacility posFac = (PositionFacility)
                CollectionUtils.getObjectFromCollectionById(facilities, new Integer(1), "rank");
        if (posFac != null) {
            return posFac.getFacility();
        }
        return null;
    }


}
