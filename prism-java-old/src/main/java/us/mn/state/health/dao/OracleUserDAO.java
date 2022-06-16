package us.mn.state.health.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.acegisecurity.GrantedAuthority;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.User;
import us.mn.state.health.util.Utilities;

public class OracleUserDAO extends OraclePersonDAO implements UserDAO {
   private static Log log = LogFactory.getLog(OraclePersonDAO.class); 
   
   public Boolean authenticate(String username, String password, String authentication){
//      try{
//          Connection conn = OracleDAOFactory.createConnection();
//
//          String query = "SELECT * FROM user_password_tbl " +
//                         "WHERE lower(username) = ? " +
//                         "AND password = ? ";
//
//          PreparedStatement pStmt = conn.prepareStatement(query);
//          pStmt.setString(1, username.toLowerCase());
//          pStmt.setString(2, Utilities.encryptPassword(password.toLowerCase()));
//          ResultSet rs = pStmt.executeQuery();
//          boolean flag = false;
//          if(rs.next()){
//            flag = true;
//          }
//          else {
//            flag = false;
//          }
//          rs.close();
//          conn.close();
//          conn = null;
//          return new Boolean(flag);
//      }
//      catch(SQLException e){
//        log.error(e);
//        return Boolean.FALSE;
//      }
//      catch(Exception e){
//        log.error(e);
        return Boolean.FALSE;
   }
   
   public User findUserByUsername(String username) throws InfrastructureException {
      User user = new User();
      try{
        Connection conn = OracleDAOFactory.createConnection();
        String query = "SELECT p.emp_id AS employeeId, " +
                               "p.first_name AS firstName, " +
                               "p.last_name AS lastName, " +
                               "p.mid_name AS midName, " +
                               "p.org_code AS orgCode, " +
                               "p.salutation AS salutation, " +
                               "p.name_prefix AS namePrefix, " +
                               "p.gender AS gender " +
                "p.person_id as personId" +
                 "p.nds_user_id as ndsUserId" +
                       "FROM  PERSON_TBL p " +
                       "WHERE lower(a.username) = ? ";

        username = username.toLowerCase();
        PreparedStatement pStmt = conn.prepareStatement(query);
        pStmt.setString(1, username);
        ResultSet rs = pStmt.executeQuery();
        if(rs.next()){
          user.setUsername(username);
          user.setEmployeeId(rs.getString("employeeId"));
          user.setFirstName(rs.getString("firstName"));
          user.setLastName(rs.getString("lastName"));
          user.setMiddleName(rs.getString("midName"));
          user.setSalutation(rs.getString("salutation"));
          user.setGender(rs.getString("gender"));
          user.setNamePrefix(rs.getString("namePrefix"));
          user.setPersonGroupLinks(getGroups(username));
          user.setEmailAddress(getEmailAddress(username));
          user.setChangePasswordIndicator(Boolean.FALSE);
        }
        rs.close();
        conn.close();
        conn = null;
      }
      catch(SQLException e){
        log.error(e);
        throw new InfrastructureException(e);
      }
      catch(Exception e){
        log.error(e);
        throw new InfrastructureException(e);
      }
      return user;
   }

    public String findAllByGroup(String inGroup) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String groupAuthority(String username) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Collection findAll() throws InfrastructureException { return null; }
    public Collection findByExample(User user) throws InfrastructureException { return null; }
    public User getUserById(Long personId, boolean lock) throws InfrastructureException  { return null; }
        public Connection getConnection() {
        try {
            return OracleDAOFactory.createConnection();
        } catch (Exception e) {
            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }
    public void makePersistent(User user) throws InfrastructureException {
//        String activeInd = "Y";
//        if(user.getActiveIndicator() != null && !user.getActiveIndicator().booleanValue()){
//            activeInd = "N";
//        }
//
//        String changePasswordInd = "Y";
//        if(user.getChangePasswordIndicator() != null && !user.getChangePasswordIndicator().booleanValue()){
//            changePasswordInd = "N";
//        }
//
//        try{
//            Connection conn = OracleDAOFactory.createConnection();
//            conn.setAutoCommit(false);
//
//            String query = "UPDATE user_password_tbl " +
//                           "SET password = ? , " +
//                           "mdhchallengequestion = ? , " +
//                           "mdhchallengeanswer = ? , " +
//                           "active_ind = ? , " +
//                           "change_password_ind = ?, " +
//                           "changed_by = 'orcl_dao' , " +
//                           "change_date = sysdate  " +
//                           "WHERE person_id = ? ";
//            PreparedStatement pStmt = conn.prepareStatement(query);
//            pStmt.setString(1, user.getPassword());
//            pStmt.setString(2, user.getChallengeQuestion());
//            pStmt.setString(3, user.getChallengeAnswer());
//            pStmt.setString(4, activeInd);
//            pStmt.setString(5, changePasswordInd);
//            pStmt.setLong(6, user.getPersonId().longValue());
//
//            if(!(pStmt.executeUpdate() > 0)){
//                conn.rollback();
//                throw new Exception("unable to persist user with username: " + user.getUsername());
//            }
//
//            try{
//                super.makePersistent(user);
//            }
//            catch(Exception e) {
//                conn.rollback();
//                log.error(e);
//                throw new Exception("unable to persist user with username: " + user.getUsername());
//            }
//
//            conn.commit();
//            conn.close();
//            conn = null;
//        }
//        catch(SQLException e){
//            log.error(e);
//            throw new InfrastructureException(e);
//        }
//        catch(Exception e){
//            log.error(e);
//            throw new InfrastructureException(e);
//        }
    }

    public int changePassword(String username, String password, String oldpassword) throws InfrastructureException {
        return -1;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getErrorMSG() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getErrorType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getEmailAddress(String username){
      String emailAddress = "";
      try{
        Connection conn = OracleDAOFactory.createConnection();
        String query = "SELECT email_address " + 
                       "FROM EMAIL_TBL " +
                       "WHERE end_date is null AND email_type = 'mdh_work' " + 
                       "AND email_id in " + 
                       "(SELECT contact_mechanism_id FROM ENTITY_target_contact_link " +
                       "WHERE end_date is null AND entity_target_id IN " + 
                       "(SELECT DISTINCT nds_user_id " +
                       "FROM PERSON_TBL " +
                       "WHERE lower(username) = ? ))";
        username = username.toLowerCase();          
        PreparedStatement pStmt = conn.prepareStatement(query);
        pStmt.setString(1, username);
        ResultSet rs = pStmt.executeQuery();
        if(rs.next()){
          emailAddress = rs.getString("email_address");
        }
        rs.close();
        conn.close();
        conn = null;
      }
      catch(SQLException e){
        log.error(e);
        return null;
      }
      catch(Exception e){
        log.error(e);
        return null;
      }
      this.printMessage("getEmailAddress(): email address for username: " + username + "=" + emailAddress);
      return emailAddress;
   }
   
   public List getGroups(String username){
      Vector groups = new Vector();
      try{
        Connection conn = OracleDAOFactory.createConnection();
        String query = "SELECT group_id " + 
                       "FROM GROUP_TBL " +
                       "WHERE end_date is null " + 
                       "AND group_id IN " + 
                       "(SELECT group_id " + 
                       "FROM ENTITY_target_group_link " +
                       "WHERE end_date is null " + 
                       "AND entity_target_id IN " + 
                       "(SELECT DISTINCT nds_person_id " +
                       "FROM PERSON_TBL " +
                       "WHERE lower(username) = ? ))";
        username = username.toLowerCase();          
        PreparedStatement pStmt = conn.prepareStatement(query);
        pStmt.setString(1, username);
        ResultSet rs = pStmt.executeQuery();
        while(rs.next()){
          groups.add(rs.getString("group_id"));
        }
        rs.close();
        conn.close();
        conn = null;
      }
      catch(SQLException e){
        log.error(e);
        return null;
      }
      catch(Exception e){
        log.error(e);
        return null;
      }
      this.printMessage("getGroups(): groups.toString() for username: " + username + " = " + groups.toString());
      return groups;
   }

   public String getChallengeQuestion(String username){
        return null;
   }

   public String getChallengeAnswer(String username){
        return null;
   }
   
   public String getFullName(String username){
      String fullName = "";
      try{
        Connection conn = OracleDAOFactory.createConnection();
        String query = "SELECT p.first_name || ' ' || p.last_name AS fullName " +
                       "FROM PERSON_TBL p " +
                       "WHERE lower(p.nds_user_id) = ? ";

        username = username.toLowerCase();          
        PreparedStatement pStmt = conn.prepareStatement(query);
        pStmt.setString(1, username);
        ResultSet rs = pStmt.executeQuery();
        if(rs.next()){
          fullName = rs.getString("fullName");
        }
        rs.close();
        conn.close();
        conn = null;
      }
      catch(SQLException e){
        log.error(e);
        return null;
      }
      catch(Exception e){
        log.error(e);
        return null;
      }
      return fullName;
   }

   public boolean setUserPassword(String username, String newPassword){
    boolean returnFlag = false;
    return returnFlag;
   }

   public boolean setChallengeQuestionAndAnswer(String username, String challengeQuestion, String challengeAnswer){
    boolean returnFlag = false;
    return returnFlag;
   }
   
   private void printMessage(String message){
      log.debug(message);     
   }
}
