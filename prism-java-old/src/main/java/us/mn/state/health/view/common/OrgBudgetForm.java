package us.mn.state.health.view.common;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class OrgBudgetForm extends ValidatorForm implements Comparable {
    private String provisions;
    private String comments;
    private String effectiveDate;
    private String reportCategory;
    private String appropriationCode;
    private String orgBudgetCode;
    private String fundCode;
    private String name;
    private String orgBudgetId;


    public void setProvisions(String provisions) {
        this.provisions = provisions;
    }


    public String getProvisions() {
        return provisions;
    }


    public void setComments(String comments) {
        this.comments = comments;
    }


    public String getComments() {
        return comments;
    }


    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }


    public String getEffectiveDate() {
        return effectiveDate;
    }


    public void setReportCategory(String reportCategory) {
        this.reportCategory = reportCategory;
    }


    public String getReportCategory() {
        return reportCategory;
    }


    public void setAppropriationCode(String appropriationCode) {
        this.appropriationCode = appropriationCode;
    }


    public String getAppropriationCode() {
        return appropriationCode;
    }


    public void setOrgBudgetCode(String orgBudgetCode) {
        this.orgBudgetCode = orgBudgetCode;
    }


    public String getOrgBudgetCode() {
        return orgBudgetCode;
    }


    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }


    public String getFundCode() {
        return fundCode;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setOrgBudgetId(String orgBudgetId) {
        this.orgBudgetId = orgBudgetId;
    }


    public String getOrgBudgetId() {
        return orgBudgetId;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

    }

    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        return null;
    }

    public int compareTo(Object o) {
        if (o instanceof OrgBudgetForm)
            return this.getOrgBudgetCode().compareTo(((OrgBudgetForm) o).getOrgBudgetCode());
        return 0;
    }
}