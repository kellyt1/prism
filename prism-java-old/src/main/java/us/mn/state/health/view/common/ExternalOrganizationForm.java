package us.mn.state.health.view.common;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.lang.StringUtils;

public class ExternalOrganizationForm extends ValidatorForm  {
    private String orgDescription;
    private String comments;
    private String webAddress;
    private String orgShortName;
    private String orgName;
    private String orgId;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        orgName = "";
        orgShortName = "";
        webAddress = "";
        comments = "";
        orgDescription = "";
        orgId = "";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }

    public void setOrgDescription(String orgDescription) {
        this.orgDescription = orgDescription;
    }


    public String getOrgDescription() {
        return orgDescription;
    }


    public void setComments(String comments) {
        this.comments = comments;
    }


    public String getComments() {
        return comments;
    }


    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }


    public String getWebAddress() {
        return webAddress;
    }


    public void setOrgShortName(String orgShortName) {
        this.orgShortName = orgShortName.toUpperCase();
    }


    public String getOrgShortName() {
        return orgShortName;
    }


    public void setOrgName(String orgName) {
        this.orgName = orgName.toUpperCase();
    }


    public String getOrgName() {
        if(StringUtils.nullOrBlank(orgName)){
            return null;
        }
        return orgName.toUpperCase();
    }


    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }


    public String getOrgId() {
        return orgId;
    }
}