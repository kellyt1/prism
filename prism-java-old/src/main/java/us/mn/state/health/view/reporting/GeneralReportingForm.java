package us.mn.state.health.view.reporting;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.lang.StringUtils;

public class GeneralReportingForm extends ValidatorForm {
    private String facilityId;
    private Collection facilities = new ArrayList();
    private String startDate;
    private String endDate;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = StringUtils.trim(endDate);
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = StringUtils.trim(startDate);
    }


    public void setFacilities(Collection facilities) {
        this.facilities = facilities;
    }


    public Collection getFacilities() {
        return facilities;
    }


    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }


    public String getFacilityId() {
        return facilityId;
    }
}
