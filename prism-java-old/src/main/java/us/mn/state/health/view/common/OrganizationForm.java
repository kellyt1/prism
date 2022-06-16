package us.mn.state.health.view.common;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class OrganizationForm  extends ValidatorForm {
    private String deptIdName;
    private String formalName;
    private String deptIdCode;
    private String endDate;
    private String effectiveDate;
    private String comments;
    private String shortName;
    private String name;
    private String type;
    private String code;
    private String orgID;


    public void setDeptIdName(String deptIdName) {
        this.deptIdName = deptIdName;
    }


    public String getDeptIdName() {
        return deptIdName;
    }


    public void setFormalName(String formalName) {
        this.formalName = formalName;
    }


    public String getFormalName() {
        return formalName;
    }


    public void setDeptIdCode(String deptIdCode) {
        this.deptIdCode = deptIdCode;
    }


    public String getDeptIdCode() {
        return deptIdCode;
    }


    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    public String getEndDate() {
        return endDate;
    }


    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }


    public String getEffectiveDate() {
        return effectiveDate;
    }


    public void setComments(String comments) {
        this.comments = comments;
    }


    public String getComments() {
        return comments;
    }


    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


    public String getShortName() {
        return shortName;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getType() {
        return type;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getCode() {
        return code;
    }


    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }


    public String getOrgID() {
        return orgID;
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }
}