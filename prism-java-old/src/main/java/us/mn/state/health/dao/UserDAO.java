package us.mn.state.health.dao;

import java.util.Collection;
import java.sql.Connection;

import org.acegisecurity.GrantedAuthority;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.User;

public interface UserDAO extends PersonDAO { 
    public Collection findAll() throws InfrastructureException; 
    public Collection findByExample(User user) throws InfrastructureException;
    public User findUserByUsername(String username) throws InfrastructureException;
    public String findAllByGroup(String inGroup);
    public String groupAuthority(String username);
    public Boolean authenticate(String username, String password,String authentication) throws InfrastructureException;
    public User getUserById(Long personId, boolean lock) throws InfrastructureException;
    public void makePersistent(User user) throws InfrastructureException;
    public int changePassword(String username, String password, String oldpassword)  throws InfrastructureException;
    public String getErrorMSG();
    public String getErrorType();
    public Connection getConnection();   
    //public void makeTransient(User user) throws InfrastructureException;
    
}
