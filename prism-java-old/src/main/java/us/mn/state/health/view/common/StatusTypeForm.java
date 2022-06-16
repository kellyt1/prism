package us.mn.state.health.view.common;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class StatusTypeForm  extends ValidatorForm {

    private String statusTypeId;
    private String name;

    public void setStatusTypeId(String statusTypeId) {
        this.statusTypeId = statusTypeId;
    }


    public String getStatusTypeId() {
        return statusTypeId;
    }


    public void setName(String name) {
        this.name = name;
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
    }
    
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        return null;
    }


    public String getName() {
        return name;
    }
}