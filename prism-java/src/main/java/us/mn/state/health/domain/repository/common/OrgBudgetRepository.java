package us.mn.state.health.domain.repository.common;

import us.mn.state.health.domain.repository.util.DomainRepository;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.User;

import java.util.List;

public interface OrgBudgetRepository extends DomainRepository {
    public OrgBudget getOrgBudgetById(Long orgBudgetId, boolean lock);

    public OrgBudget loadOrgBudgetById(Long orgBudgetId, boolean lock);

    public List findAll();

    public List findAllPurchaseOrgBudgets();

    public List findAllByBudgetManager(User budgetManager);

    public OrgBudget findByOrgBudgetCode(String orgBdgtCode);

    public void makePersistent(OrgBudget orgBudget);

    public OrgBudget findByOrgBudgetCodeAndLastFiscalYear(String orgBudgetCode);

    public List<OrgBudget> findMissingOrgBudgets();
}
