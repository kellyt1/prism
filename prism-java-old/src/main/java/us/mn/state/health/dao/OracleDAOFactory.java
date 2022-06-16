package us.mn.state.health.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OracleDAOFactory extends BaseDAOFactory {
    private static Log log = LogFactory.getLog(OracleDAOFactory.class);
    private static String host;
    private static int port;
    private static String userID;
    private static String password;

    /**
     * This method returns a pooled connection to an oracle database.  The connection must be defined
     * in under the JNDI name "jdbc/pool/OracleConnectionPoolDS"
     *
     * @return A JDBC pooled Connection to the oracle database
     * @throws java.lang.Exception
     */
    public static Connection createConnection() throws Exception {
        Connection connection = null;
        try {
            // Establish network connection to database
            InitialContext context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("jdbc/pool/OracleConnectionPoolDS");
            connection = ds.getConnection();
        } 
        catch(SQLException e) {
            log.error(e);
            throw new SQLException(e.getMessage());
        } 
        catch(Exception ex) {
            log.error(ex);
            throw ex;
        }
        return connection;
    }

    /**
     * This method returns a pooled connection to an oracle database.  The connection must be defined
     * in under the JNDI name passed in
     *
     * @return A JDBC pooled Connection to the oracle database
     * @throws java.lang.Exception
     */
    public static Connection createConnection(String jndiName) throws Exception {
        Connection connection = null;
        try {
            // Establish network connection to database
            InitialContext context = new InitialContext();
            DataSource ds = (DataSource) context.lookup(jndiName);
            connection = ds.getConnection();
        } 
        catch(SQLException e) {
            log.error(e);
            throw new SQLException(e.getMessage());
        } 
        catch(Exception ex) {
            log.error(ex);
            throw ex;
        }
        return connection;
    }

    public static void closeConnection(Connection conn) {
        try {
            conn.clearWarnings();
            conn.close();
        } 
        catch(SQLException e) {
            log.error(e);
        }
    }

    public void setHost(String host) {
        OracleDAOFactory.host = host;
    }

    public String getHost() {
        return OracleDAOFactory.host;
    }

    public void setPort(String port) {
        OracleDAOFactory.port = (int) Integer.parseInt(port);
    }

    public void setPort(int port) {
        OracleDAOFactory.port = port;
    }

    public int getPort() {
        return OracleDAOFactory.port;
    }

    public void setUserID(String userID) {
        OracleDAOFactory.userID = userID;
    }

    public String getUserID() {
        return OracleDAOFactory.userID;
    }

    public void setPassword(String password) {
        OracleDAOFactory.password = password;
    }

    public String getPassword() {
        return password;
    }

    /*
     * The methods to return specific DAOs
     */

    public PersonDAO getPersonDAO() {
        return new OraclePersonDAO();
    }

    public UserDAO getUserDAO() {
        return new OracleUserDAO();
    }

    private static void printMessage(String message) {
        log.debug(message);
    }
}