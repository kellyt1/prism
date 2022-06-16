package us.mn.state.health.dao;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Phone;
import us.mn.state.health.model.common.PrismExternalReports;

import java.util.Collection;

public interface ExternalReportDAO {
    public Collection findAll() throws InfrastructureException;
    public Collection findByExample(PrismExternalReports preports) throws InfrastructureException;
    public void makePersistent(PrismExternalReports preports) throws InfrastructureException;
    public void makeTransient(PrismExternalReports preports) throws InfrastructureException;

}
