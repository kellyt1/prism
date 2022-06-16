package us.mn.state.health.dao

import us.mn.state.health.common.exceptions.InfrastructureException
import us.mn.state.health.model.common.Priority

/**
 * Created by demita1 on 5/16/2016.
 */
class PriorityDAOStub implements PriorityDAO {

    @Override
    Priority getPriorityById(Long priorityId, boolean lock) throws InfrastructureException {
        return null
    }

    @Override
    Collection findAll() throws InfrastructureException {
        return null
    }

    @Override
    Priority findByPriorityCode(String priorityCode) throws InfrastructureException {
        return new Priority()
    }

    @Override
    Priority findByPriorityName(String priorityName) throws InfrastructureException {
        return null
    }

    @Override
    void makePersistent(Priority priority) throws InfrastructureException {

    }

    @Override
    void makeTransient(Priority priority) throws InfrastructureException {

    }
}
