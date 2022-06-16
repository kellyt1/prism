package us.mn.state.health.dao;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Priority;

public interface PriorityDAO  {    
    public Priority getPriorityById(Long priorityId, boolean lock) throws InfrastructureException;
    public Collection findAll() throws InfrastructureException;
    public Priority findByPriorityCode(String priorityCode) throws InfrastructureException;
    public Priority findByPriorityName(String priorityName) throws InfrastructureException;
    public void makePersistent(Priority priority) throws InfrastructureException;
    public void makeTransient(Priority priority) throws InfrastructureException;
}