package us.mn.state.health.dao

import us.mn.state.health.common.exceptions.InfrastructureException
import us.mn.state.health.model.common.Person

/**
 * Created by demita1 on 6/21/2016.
 */
class PersonDAOStub implements PersonDAO {
    @Override
    Collection findAll() throws InfrastructureException {
        return null
    }

    @Override
    Collection findByExample(Person person) throws InfrastructureException {
        return null
    }

    @Override
    Person getPersonById(Long personId, boolean lock) throws InfrastructureException {
        return null
    }

    @Override
    Collection findAllMDHEmployees() throws InfrastructureException {
        return null
    }

    @Override
    Collection findAllNonMDHEmployees() throws InfrastructureException {
        return null
    }

    @Override
    void makePersistent(Person person) throws InfrastructureException {

    }

    @Override
    Collection findPersonsByPositionClassCode(String classCode) throws InfrastructureException {
        return null
    }

    @Override
    Collection findPersonsByGroupId(Long groupId) throws InfrastructureException {
        return null
    }

    @Override
    List findPersonsByGroupCode(String code) throws InfrastructureException {
        return null
    }

    @Override
    Collection findBudgetManagersByOrgBdgtCode(String orgBdgtCode) throws InfrastructureException {
        return null
    }

    @Override
    Collection findAllNonMDHEmployeesAsDTO() throws InfrastructureException {
        return null
    }

    @Override
    Collection findAllMDHEmployeesAsDTO() throws InfrastructureException {
        return null
    }
}
