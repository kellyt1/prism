package us.mn.state.health.view.materialsrequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.lang.WrapperUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.model.common.OrgBudget;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.TreeSet;

public class RequestLineItemFundingSourceForm extends ValidatorForm {    
    private Long requestLineItemFundingSourceId;
    private String orgBudgetId;
    private String amount = ""; 
    private Collection orgBudgets = new TreeSet();  //for display - the drop-down
    private Collection orgApproverMissing = new TreeSet();  //for display - the drop-down whyIsMyBudgetMissing
    private boolean enabled = true;
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {        
    }

    public Collection getOrgApproverMissing() {
        return orgApproverMissing;
    }

    public void setOrgApproverMissing(Collection orgApproverMissing) {
        this.orgApproverMissing = orgApproverMissing;
    }

//    public void setOrgApproverMissing(OrgBudget orgApproverMissing) {
//        this.orgApproverMissing = orgApproverMissing;
//    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }
    
    public OrgBudget getOrgBudget() {
        return (OrgBudget)CollectionUtils.getObjectFromCollectionById(orgBudgets, orgBudgetId, "orgBudgetId");
    }
    public void setOrgBudget() {

    }


    public void setRequestLineItemFundingSourceId(Long requestLineItemFundingSourceId) {
        this.requestLineItemFundingSourceId = requestLineItemFundingSourceId;
    }

    public Long getRequestLineItemFundingSourceId() {
        return requestLineItemFundingSourceId;
    }
    
    public void setOrgBudgetId(String orgBudgetId) {
        this.orgBudgetId = orgBudgetId;
    }

    public String getOrgBudgetId() {
        return orgBudgetId;
    }

    public void setOrgBudgets(Collection orgBudgets) {
        this.orgBudgets = orgBudgets;
    }

    public Collection getOrgBudgets() {
        return orgBudgets;
    }

    public void setAmount(String amt) {
        this.amount = StringUtils.trim(amt);
    }

    public String getAmount() {
        return WrapperUtils.round(amount, 2);
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public boolean getEnabled() {
        return enabled;
    }
    
    public String getKey() {
        return this.toString();
    }
    
    public String getSummary() {
        String summary = null;
        if(getOrgBudget()!=null){
          summary = getOrgBudget().getOrgBudgetCodeAndNameAndFY();
        }

        if(getAmount() != null) {
            summary = (WrapperUtils.round(getAmount(), 2) + "-" + summary);
        }
        return summary;
    }
}