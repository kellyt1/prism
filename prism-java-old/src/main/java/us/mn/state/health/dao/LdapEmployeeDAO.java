package us.mn.state.health.dao;

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Employee;
import us.mn.state.health.model.common.EmployeeQuickTblId;

public class LdapEmployeeDAO implements EmployeeDAO {
    private static Log log = LogFactory.getLog(LdapEmployeeDAO.class);

    private static final String BASE_DN = "ou=people,o=HEALTH-STATE-MN-US";

    public Collection findAll() throws InfrastructureException {
        return null;
    }

    public Employee getEmployeeByPersonId(Long personId, boolean lock) throws InfrastructureException {
        return null;
    }

    public Employee getEmployeeById(EmployeeQuickTblId employeeQuickTblId, boolean lock) throws InfrastructureException {
        return null;
    }

    public Collection findAllMDHEmployees() throws InfrastructureException {
        return null;
    }

    public Collection findAllMDHEmployeesAsDTO() throws InfrastructureException {
        return null;
    }

    private void printMessage(String message) {
        log.debug(message);
    }
}