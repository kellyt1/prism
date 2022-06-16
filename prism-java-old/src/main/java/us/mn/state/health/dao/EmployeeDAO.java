package us.mn.state.health.dao;

import java.util.Collection;
import java.util.List;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Employee;
import us.mn.state.health.model.common.EmployeeQuickTblId;

public interface EmployeeDAO {
    public Collection findAll() throws InfrastructureException;

    public Employee getEmployeeById(EmployeeQuickTblId employeeQuickTblId, boolean lock) throws InfrastructureException;

    public Employee getEmployeeByPersonId(Long personId, boolean lock) throws InfrastructureException;

    public Collection findAllMDHEmployees() throws InfrastructureException;

    Collection findAllMDHEmployeesAsDTO() throws InfrastructureException;
}