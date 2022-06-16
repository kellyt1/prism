package us.mn.state.health.dao;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.User;

import java.util.Collection;
import java.util.List;

public interface OrgBudgetDAO  {    
    public OrgBudget getOrgBudgetById(Long orgBudgetId, boolean lock) throws InfrastructureException;
    public Collection findAll() throws InfrastructureException;
    public Collection findAllPurchaseOrgBudgets() throws InfrastructureException;
    public Collection findAllByBudgetManager(User budgetManager) throws InfrastructureException;
    public Collection findRptCategoriesByOrg(String orgBdgtCode) throws InfrastructureException;
    public Collection findRptCategoriesByAppropriationCode(String appropriationCode, String orgCode) throws InfrastructureException;
    public OrgBudget findByOrgBudgetCode(String orgBdgtCode) throws InfrastructureException;
    public void makePersistent(OrgBudget orgBudget) throws InfrastructureException;
    public void makeTransient(OrgBudget orgBudget) throws InfrastructureException;
    public OrgBudget findByOrgBudgetCodeAndLastFiscalYear(String orgBudgetCode) throws InfrastructureException;

    public List<OrgBudget> findMissingOrgBudgets() throws InfrastructureException;
}