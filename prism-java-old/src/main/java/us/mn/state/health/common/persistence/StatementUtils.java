package us.mn.state.health.common.persistence;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class StatementUtils  {

    public static void setOptionalParameter(PreparedStatement ps, int index, 
        Object paramValue) throws SQLException {
        if(paramValue != null && !paramValue.toString().equals("0")) {
            if(paramValue.getClass().getName().equals("java.lang.Integer"))
                ps.setInt(index, ((Integer)paramValue).intValue());
            else if(paramValue.getClass().getName().equals("java.lang.Long"))
                ps.setLong(index, ((Long)paramValue).longValue());
            else if(paramValue.getClass().getName().equals("java.lang.Double"))
                ps.setDouble(index, ((Double)paramValue).doubleValue());
            else if(paramValue.getClass().getName().equals("java.lang.Float"))
                ps.setFloat(index, ((Float)paramValue).floatValue());
        }
        else { 
            ps.setNull(index, Types.NUMERIC);
        }
    }
    
    public static void close(Statement s, Connection conn) {
        try {
            if(s != null){
                s.close();			
            }
        }
        catch(Exception e) {
          //do nothing
        }
        
        try {
            if(conn != null){
                conn.close();
            }		
        }
        catch(Exception e) {
          //do nothing
        }
    }
    /*
    public static Object getOptionalValue(ResultSet rs, 
            String column, Class type, String nullValue) throws SQLException {
        
        try {
            String value = rs.getString(column);
            if(value.equals(nullValue))
                return null;
            else {
                
            }
        }
        catch(Exception e) {
            throw new SQLException(e.getMessage());
        }
    }*/
}