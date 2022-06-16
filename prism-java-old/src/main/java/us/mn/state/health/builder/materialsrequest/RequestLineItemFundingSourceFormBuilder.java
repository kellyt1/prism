package us.mn.state.health.builder.materialsrequest;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource;
import us.mn.state.health.view.materialsrequest.RequestLineItemFundingSourceForm;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class RequestLineItemFundingSourceFormBuilder  {
    private DAOFactory daoFactory;
    private RequestLineItemFundingSource rliFundingSource;
    private RequestLineItemFundingSourceForm rliFundingSourceForm;
    
    public RequestLineItemFundingSourceFormBuilder(RequestLineItemFundingSourceForm rliFundingSourceForm, 
                                                   DAOFactory daoFactory) {
        this.rliFundingSourceForm = rliFundingSourceForm;
        this.daoFactory = daoFactory;
    }
    
     public RequestLineItemFundingSourceFormBuilder(RequestLineItemFundingSourceForm rliFundingSourceForm, 
                                                    RequestLineItemFundingSource rliFundingSource,
                                                    DAOFactory daoFactory) {
        this(rliFundingSourceForm, daoFactory);
        this.rliFundingSource = rliFundingSource;
    }
    
    /**
     * set up the budgets for the drop down list.  If the budgetManager argument is not null, 
     * it will find only the budgets that are managed by that person.  
     * @param budgetManager
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildOrgBudgets(User budgetManager) throws InfrastructureException {
        Collection orgBudgets = null;
        
        if(budgetManager == null) {
            orgBudgets = daoFactory.getOrgBudgetDAO().findAllPurchaseOrgBudgets();
            for (Iterator iterator = orgBudgets.iterator(); iterator.hasNext();) {
                OrgBudget orgBudget = (OrgBudget) iterator.next();
                orgBudget.getOrgBudgetCodeAndNameAndFY();
            }
        }
        else {
            orgBudgets = daoFactory.getOrgBudgetDAO().findAllByBudgetManager(budgetManager);
        }
        
        rliFundingSourceForm.setOrgBudgets(orgBudgets);
    }
    
    public void buildOrgBudgets() throws InfrastructureException {
        buildOrgBudgets(null);
    }

    // ??  begin
    public void buildOrgApproverMissing() throws InfrastructureException {
        Collection orgApproverMissings = null;

        orgApproverMissings = daoFactory.getOrgBudgetDAO().findMissingOrgBudgets();       // use native query.
        //orgApproverMissings = daoFactory.getOrgBudgetDAO().findAllPurchaseOrgBudgets();    // ?? testing


        for (Iterator iterator = orgApproverMissings.iterator(); iterator.hasNext();) {
            OrgBudget orgApproverMissing = (OrgBudget) iterator.next();
            orgApproverMissing.getOrgBudgetCodeAndNameAndFY();
        }

        rliFundingSourceForm.setOrgApproverMissing(orgApproverMissings);
    }
    // ??   end
    
    /**
     * Prepare the list for the funding source drop-down on the page.
     * If the selected org budget is not in the list of org budgets on the form (which might 
     * happen if the user is in as a budget manager and the selected org budget is not one the 
     * budget manager has control over), clear the list and add only the selected org bdget and 
     * flag the form as inactive so the user can't edit it.  
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildSelectedOrgBudget() throws InfrastructureException {
        if(rliFundingSource != null) {
            String selectedOrgBdgtId = rliFundingSource.getOrgBudget().getOrgBudgetId().toString();
            if(rliFundingSourceForm.getOrgBudgets().contains(rliFundingSource.getOrgBudget())) {
                rliFundingSourceForm.setOrgBudgetId(selectedOrgBdgtId);
            }
            else {  //add the org budget to the form, but disable it - the user does not have control over the budget
                rliFundingSourceForm.getOrgBudgets().clear();
                Collection singleOrgBdgtCollection = new TreeSet();
                singleOrgBdgtCollection.add(rliFundingSource.getOrgBudget());
                rliFundingSourceForm.setOrgBudgets(singleOrgBdgtCollection);
                rliFundingSourceForm.setOrgBudgetId(rliFundingSource.getOrgBudget().getOrgBudgetId().toString());
                rliFundingSourceForm.setEnabled(false);
            }
        }
    }
    
     public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(rliFundingSourceForm, rliFundingSource);
        } 
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building StockItemForm: ", rpe);
        }
    }
}