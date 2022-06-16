package us.mn.state.health.view.inventory;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class UnitForm extends ValidatorForm implements Comparable {
    private String name;
    private String code;
    private String unitId;
    
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


    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }


    public String getUnitId() {
        return unitId;
    }
    
    public int compareTo(Object o) {
        if(o instanceof UnitForm) {
            return this.getName().compareTo(((UnitForm)o).getName());
        }
        return 0;
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }
}