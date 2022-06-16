package us.mn.state.health.dao;

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Person;

public class LdapPersonDAO implements PersonDAO {
    private static Log log = LogFactory.getLog(LdapPersonDAO.class);

    private static final String BASE_DN = "ou=people,o=HEALTH-STATE-MN-US";

    public Collection findAll() throws InfrastructureException {
        return null;
    }

    public Collection findByExample(Person person) throws InfrastructureException {
        return null;
    }

    public Person getPersonById(Long personId, boolean lock) throws InfrastructureException {
        return null;
    }

    public Collection findAllMDHEmployees() throws InfrastructureException {
        return null;
    }

    public Collection findAllNonMDHEmployees() throws InfrastructureException {
        return null;
    }

    public void makePersistent(Person person) throws InfrastructureException {

    }

    public Collection findPersonsByPositionClassCode(String classCode) throws InfrastructureException {
        return null;
    }

    public Collection findPersonsByGroupId(Long groupId) throws InfrastructureException {
        return null;
    }

    public List findPersonsByGroupCode(String code) throws InfrastructureException {
        return null;
    }

    public Collection findBudgetManagersByOrgBdgtCode(String orgBdgtCode) throws InfrastructureException {
        return null;
    }

    public Collection findAllNonMDHEmployeesAsDTO() throws InfrastructureException {
        return null;
    }

    public Collection findAllMDHEmployeesAsDTO() throws InfrastructureException {
        return null;
    }

    private void printMessage(String message) {
        log.debug(message);
    }
}