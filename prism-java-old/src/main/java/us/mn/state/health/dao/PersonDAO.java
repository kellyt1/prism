package us.mn.state.health.dao;

import java.util.Collection;
import java.util.List;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Person;

public interface PersonDAO {
    public Collection findAll() throws InfrastructureException;

    public Collection findByExample(Person person) throws InfrastructureException;

    public Person getPersonById(Long personId, boolean lock) throws InfrastructureException;

    public Collection findAllMDHEmployees() throws InfrastructureException;

    public Collection findAllNonMDHEmployees() throws InfrastructureException;

    public void makePersistent(Person person) throws InfrastructureException;

    public Collection findPersonsByPositionClassCode(String classCode) throws InfrastructureException;

    public Collection findPersonsByGroupId(Long groupId) throws InfrastructureException;

    public List findPersonsByGroupCode(String code) throws InfrastructureException;

    public Collection findBudgetManagersByOrgBdgtCode(String orgBdgtCode) throws InfrastructureException;

    Collection findAllNonMDHEmployeesAsDTO() throws InfrastructureException;

    Collection findAllMDHEmployeesAsDTO() throws InfrastructureException;
}
