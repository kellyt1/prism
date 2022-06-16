package us.mn.state.health.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Person;

public class OraclePersonDAO implements PersonDAO {
    private static Log log = LogFactory.getLog(OraclePersonDAO.class);

    public Collection findAll() throws InfrastructureException {
        return null;
    }

    public Collection findByExample(Person person) throws InfrastructureException {
        return null;
    }

    public Collection findAllMDHEmployees() throws InfrastructureException {
        return null;
    }

    public Collection findAllNonMDHEmployees() throws InfrastructureException {
        return null;
    }

    public Person getPersonById(Long personId, boolean lock) throws InfrastructureException {
        return null;
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

    public void makePersistent(Person person) throws InfrastructureException {
        try {
            Connection conn = OracleDAOFactory.createConnection();
            conn.setAutoCommit(false);
            String query = "UPDATE PERSON_TBL SET " +
                    "emp_id = ?, " +
                    "first_name = ?, " +
                    "last_name = ?, " +
                    "mid_name = ?, " +
                    "salutation = ?, " +
                    "gender = ?, " +
                    "name_prefix = ?, " +
                    "changed_by = 'orcl_dao', " +
                    "change_date = sysdate " +
                    "where person_id = ? ";
            PreparedStatement pStmt = conn.prepareStatement(query);
            pStmt.setString(1, person.getEmployeeId());
            pStmt.setString(2, person.getFirstName());
            pStmt.setString(3, person.getLastName());
            pStmt.setString(4, person.getMiddleName());
            pStmt.setString(5, person.getSalutation());
            pStmt.setString(6, person.getGender());
            pStmt.setString(7, person.getNamePrefix());
            pStmt.setLong(8, person.getPersonId().longValue());

            if (!(pStmt.executeUpdate() > 0)) {
                conn.rollback();
                throw new Exception("unable to persist person with personId: " + person.getPersonId());
            }
            conn.commit();
            conn.close();
            conn = null;
        }
        catch (SQLException e) {
            log.error(e);
            throw new InfrastructureException(e);
        }
        catch (Exception e) {
            log.error(e);
            throw new InfrastructureException(e);
        }
    }

    private void printMessage(String message) {
        log.debug(message);
    }
}
