package us.mn.state.health.dao;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.BusinessRulesORCL;

import java.util.Collection;
import java.util.List;

public interface BusinessRulesORCLDAO {
    Collection findAll() throws InfrastructureException;
    List<BusinessRulesORCL> findByExample(String criteria) throws InfrastructureException;
    void makePersistent(BusinessRulesORCL businessRules) throws InfrastructureException;
    void makeTransient(BusinessRulesORCL businessRules) throws InfrastructureException;
}