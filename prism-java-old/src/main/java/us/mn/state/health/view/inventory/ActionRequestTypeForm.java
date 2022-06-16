package us.mn.state.health.view.inventory;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class ActionRequestTypeForm extends ValidatorForm implements Comparable {    
    private String name;
    private String code;
    private String actionRequestTypeId;

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getCode() {
        return code;
    }


    public void setActionRequestTypeId(String actionRequestTypeId) {
        this.actionRequestTypeId = actionRequestTypeId;
    }


    public String getActionRequestTypeId() {
        return actionRequestTypeId;
    }
    
    public int compareTo(Object obj) {
        return this.actionRequestTypeId.compareTo(((ActionRequestTypeForm)obj).getActionRequestTypeId());
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }
}