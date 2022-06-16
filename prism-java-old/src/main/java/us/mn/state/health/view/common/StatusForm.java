package us.mn.state.health.view.common;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class StatusForm extends ValidatorForm {
    
    private String name;
    private String statusId;
    private StatusTypeForm statusTypeForm;
    private Collection statusTypeForms = new ArrayList();

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }


    public String getStatusId() {
        return statusId;
    }
    
    public void setStatusTypeForm(StatusTypeForm statusTypeForm) {
        this.statusTypeForm = statusTypeForm;
    }


    public StatusTypeForm getStatusTypeForm() {
        return statusTypeForm;
    }


    public void setStatusTypeForms(Collection statusTypeForms) {
        this.statusTypeForms = statusTypeForms;
    }


    public Collection getStatusTypeForms() {
        return statusTypeForms;
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
    }
    
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        return null;
    }
}