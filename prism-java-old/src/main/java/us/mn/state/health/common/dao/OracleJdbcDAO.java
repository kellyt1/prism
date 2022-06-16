package us.mn.state.health.common.dao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.persistence.StatementUtils;

public abstract class OracleJdbcDAO  {

    private DataSource dataSource;
    
    public OracleJdbcDAO() {
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public DataSource getDataSource() {
        return dataSource;
    }
    
    public Long getNextSequenceVal(Connection con, String seqName) 
        throws InfrastructureException {
        
        String SQL = "select " + seqName + ".nextval from dual";
        Statement s = null;
        try {
            s = con.createStatement();
            ResultSet rs = s.executeQuery(SQL);
            if(rs.next()) {
                return new Long(rs.getLong("NEXTVAL"));
            }
            return null;
        }
        catch(SQLException sqle) {
            StatementUtils.close(s, con);
            throw new InfrastructureException(sqle);
        }
    }
}