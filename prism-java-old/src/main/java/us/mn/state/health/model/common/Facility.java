package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public class Facility implements Serializable, Comparable {
    protected Long facilityId;
    protected String facilityType;
    protected String facilityName;
    protected Facility parent;
    protected Collection children;
    protected String facilityCode;
    protected MailingAddress mailingAddress;
    private Date endDate;
    
    public static final String TYPE_BLOCK = "BLOCK";
    public static final String TYPE_BUILDING = "BUILDING";
    public static final String TYPE_CAMPUS = "CAMPUS";
    public static final String TYPE_ROOM = "ROOM";
    public static final String TYPE_SPECIAL_USE_AREA = "SPECIAL_USE_AREA";
    public static final String TYPE_SUITE_WING = "SUITE/WING";
    public static final String TYPE_WORKSTATION = "WORKSTATION";

    public Long getFacilityId(){
        return facilityId;
    }

    public void setFacilityId(Long facilityId){
        this.facilityId = facilityId;
    }

    public String getFacilityType(){
        return facilityType;
    }

    public void setFacilityType(String facilityType){
        this.facilityType = facilityType;
    }

    public String getFacilityName(){
        return facilityName;
    }

    public void setFacilityName(String facilityName){
        this.facilityName = facilityName;
    }

    public Facility getParent(){
        return parent;
    }

    public void setParent(Facility parent){
        this.parent = parent;
    }

    public Collection getChildren(){
        return children;
    }

    public void setChildren(Collection children){
        this.children = children;
    }

    public String getFacilityCode(){
        return facilityCode;
    }

    public void setFacilityCode(String facilityCode){
        this.facilityCode= facilityCode;
    }
    
    public boolean equals(Object o) {        
        if(!(o instanceof Facility)) return false;
        
        if(this == o) return true;

        final Facility facility = (Facility)o;
        if(facilityId.longValue() == facility.getFacilityId().longValue()) {
            return true;
        }
        else {
            return false;
        }       
    }

    public int hashCode() {
        return 29 * facilityCode.hashCode() + facilityName.hashCode();
    }

    public String toString() {
        return "Facility{" +
                "facilityId=" + facilityId +
                ", facilityType='" + facilityType + "'" +
                ", facilityName='" + facilityName + "'" +
                ", facilityCode='" + facilityCode + "'" +
                "}";
    }
    
    public int compareTo(Object o) {
        if (o instanceof Facility) {
            return this.getFacilityName().compareTo(((Facility) o).getFacilityName());
        }
        return 0;
    }


    public void setMailingAddress(MailingAddress mailingAddress) {
        this.mailingAddress = mailingAddress;
    }
    
    public MailingAddress getMailingAddress() {
        return this.mailingAddress;
    }

    /**
     * This method uses recursion to get this facility's building mailing address. 
     * Apparently, only facilities whose type is BUILDING have an address. 
     * @return this object's MailingAddress or this object's Parent's MailingAddress, recursively until 
     *          the BUILDING is found.  
     */
    public MailingAddress getBuildingMailingAddress() {
        if(Facility.TYPE_BUILDING.equals(this.getFacilityType())) {  
            return mailingAddress;
        }
        else {
            return getParent().getBuildingMailingAddress();
        }
    }


    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public Date getEndDate() {
        return endDate;
    }
}
