package us.mn.state.health.dao;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.FiscalYears;

import java.util.Collection;

public interface FiscalYearsDAO {
    public Collection findAll() throws InfrastructureException;
    public FiscalYears getFiscalYearById(Long yearsago, boolean lock) throws InfrastructureException;
    public void makePersistent(FiscalYears fiscalYear) throws InfrastructureException;
    public void makeTransient(FiscalYears fiscalYear) throws InfrastructureException;
}
