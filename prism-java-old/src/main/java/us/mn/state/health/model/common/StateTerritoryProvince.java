package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public class StateTerritoryProvince implements Serializable {
    protected Long stateId;
    protected String stateCode;
    protected String stateName;
    protected String stateType;
    private Date endDate;


    public String toString() {
        return "";
        /*"Facility{" +
                "facilityId=" + facilityId +
                ", facilityType='" + facilityType + "'" +
                ", facilityName='" + facilityName + "'" +
                ", facilityCode='" + facilityCode + "'" +
                "}";*/
    }

    public static StateTerritoryProvince createStateTerritoryProvince(String inName, String inCode) {
            StateTerritoryProvince stp = new StateTerritoryProvince();
            stp.stateCode = inCode;
            stp.stateName = inName;
         return  stp;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateType() {
        return stateType;
    }

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
