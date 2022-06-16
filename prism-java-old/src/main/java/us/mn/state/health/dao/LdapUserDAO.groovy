package us.mn.state.health.dao

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.UniformInterfaceException
import com.sun.jersey.api.client.WebResource
import com.sun.jersey.core.util.MultivaluedMapImpl
import groovy.json.JsonException
import groovy.json.JsonSlurper
import groovy.json.internal.JsonFastParser
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import us.mn.state.health.common.exceptions.InfrastructureException

//import com.novell.ldap.LDAPAttribute;
//import com.novell.ldap.LDAPConnection;
//import com.novell.ldap.LDAPEntry;
//import com.novell.ldap.LDAPException;
//import com.novell.ldap.LDAPModification;
//import com.novell.ldap.LDAPSearchResults;

import us.mn.state.health.dao.configuration.HibernateConfigurationItemDAO
import us.mn.state.health.matmgmt.util.Constants
import us.mn.state.health.model.common.User
import us.mn.state.health.model.util.configuration.ConfigurationItem

import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MultivaluedMap

public class LdapUserDAO extends LdapPersonDAO implements UserDAO {
   private static Log log = LogFactory.getLog(LdapUserDAO.class);

 //  private static final String BASE_DN = "ou=people,o=HEALTH-STATE-MN-US";
      private static final String BASE_DN = "o=MDH";
      private static final String LDAPSERVER = "mdh-nds1.health.state.mn.us";
      private static final String ADMAIN = "MDH-DC1.mdh-ad.health.state.mn.us";
      private static final String ADBACKUP = "MDH-DC3.mdh-ad.health.state.mn.us";
      //private static final String MASDTRACKINGAPIKEY =  "21BC59FF2170D71627044727FBD97791";
      private static final String APIKEY =  "21C32FCA2170D71627712C6221375CBB";
      //private static final String LDAPSERVER = "MDH-NDS3";
      private static final String LDAPSERVER_BACKUP = "MDH-NDS2";
//      private static final String AUTHSERVICE="https://fyi.health.state.mn.us/services/ldap/";
//      private static final String AUTHSERVICE="https://fyi-test.health.state.mn.us/services/ldap/";

      private static String errorMSG = "";
      private static String errorType = "";



    private static String getAuthenticationServer() {
        HibernateConfigurationItemDAO hcidao = new HibernateConfigurationItemDAO();
        ConfigurationItem ci = null;
        try {
            ci = hcidao.getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.AUTHENTICATION_SERVER);
        } catch ( InfrastructureException ie) {
            ie.printStackTrace();
        }

//        private static final String AUTHSERVICE = ci.getValue();
        return ci.getValue();
    }

    private static final String AUTHSERVICE = getAuthenticationServer();


    public Boolean authenticate(String loginID, String password, String authentication){

        String s = authenticationSERVICE(loginID, password);

        if (s.replaceAll("\\s+","").contains("\"access_token\"")) {
            return (checkAccessToken(s, loginID))
         } else {
             errorType = "PASSWORD";
             errorMSG = "Something is amiss with your username or password";
            return false;
        }
    }
    private String authenticationSERVICE(String loginID, String password) {
            Client client = Client.create();
            //for use in the webResource.post(...) we were getting an error when trying to access the CF service without the trailing "/"
            String url = Constants.KEYCLOAK_TOKEN_ENDPOINT

            WebResource webResource = client.resource(url);

            MultivaluedMap queryParams = new MultivaluedMapImpl();

            queryParams.add("grant_type", "password");
            queryParams.add("client_id", Constants.KEYCLOAK_CLIENT_ID);
            queryParams.add("password", password);
            queryParams.add("username", loginID);

        String s
        try{
           s = webResource.type("application/x-www-form-urlencoded").post(String.class, queryParams);
        } catch (UniformInterfaceException uif){
            log.error(loginID + "with provided credentials was not authenticated", uif)
            s = "Authentication Failed"
        }

        return s;   //must be in a valid MASDTracking group to access the application at this time
    }

//   private String authenticationGROUP(String loginID) {
//            //log.info("authenticating {0}", credentials.getUsername());
//
//            Client client = Client.create();
//       //for use in the webResource.post(...) we were getting an error when trying to access the CF service without the trailing "/"
//       String url = AUTHSERVICE + "People/" + loginID  + "/";
//
////query for groups
////        WebResource webResource = client.resource(url);
//            WebResource webResource = null;
//            webResource = client.resource(url);
//
//            MultivaluedMap queryParams = new MultivaluedMapImpl();
////            queryParams.add("password", password);
//            queryParams.add("apikey", APIKEY);
////        queryParams.add("action", "auth");
//
//
//            String s = webResource.type("application/x-www-form-urlencoded").post(String.class, queryParams);
////            String s = webResource.queryParams(queryParams).post(String.class, queryParams);
//
//            return s;   //must be in a valid MASDTracking group to access the application at this time
//            }

    public int changePassword(String loginID, String oldpassword, String newpassword){
       int result = 0;
       try{
           LdapDAOFactory lx = new LdapDAOFactory();
           lx.setHost(LDAPSERVER);
           lx.setHostBackup(LDAPSERVER_BACKUP);
           // Unsecure Port we are using TLS so it emulates the secure port
           lx.setPort(389);
           lx.setUserID(loginID);
           lx.setPassword(oldpassword);
           // If a DN comes back then it is authenticated

          result = lx.changePasswordTLS(newpassword);

          this.printMessage("Password changed: returning " + result);
          return result;
       }
       catch(Exception e){
          this.printMessage("LDAPException in authenticate(): " + e);
           //.getLDAPErrorMessage());
          return result;
       }
    }
    private Boolean checkAccessToken(String tokenResponse, String loginId) {

        Boolean outValue = false;
        if(isTextJson(tokenResponse)){
            try{
                Map<String, String> tokenMap = new JsonSlurper().parseText(tokenResponse)

                Client client = Client.create();

                //for use in the webResource.get(...)
                String url = Constants.KEYCLOAK_USERINFO_ENDPOINT

                WebResource webResource = client.resource(url);


                String userInfoResponse = webResource.type("application/x-www-form-urlencoded")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenMap.get("access_token"))
                        .get(String.class);

                Map<String, String> userInfoMap = new JsonSlurper().parseText(userInfoResponse)

                //The Auth service provided a token, now we're using the token to ensure the service can work back from the token and verify the user's info.
                outValue = loginId.toLowerCase() == userInfoMap.get("preferred_username").toLowerCase()
            }catch (JsonException e) {
                e.printStackTrace()  //To change body of catch statement use File | Settings | File Templates.
                return false
            }
        }else {
            false
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            try {
//                DocumentBuilder db = dbf.newDocumentBuilder();
//                ByteArrayInputStream bs = new ByteArrayInputStream(inString.getBytes());
//                Document doc = db.parse(bs);
//                doc.getDocumentElement().normalize();
//
//                NodeList nodeLst = doc.getElementsByTagName("response");
//
//
//                for (int s = 0; s < nodeLst.getLength(); s++) {
//
//                    Node fstNode = nodeLst.item(s);
//
//                    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//                        Element fstElmnt = (Element) fstNode;
//                        NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("access_token");
//                        Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
//                        NodeList fstNm = fstNmElmnt.getChildNodes();
//                        String sGroup = fstNm.item(0).getNodeValue().toUpperCase();
//                        if (sGroup.equalsIgnoreCase("TRUE")) outValue = true;
//                        //      System.out.println("cn : " + sGroup);
//                    }
//                }
//
//            } catch (ParserConfigurationException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                return false;
//            } catch (SAXException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                return false;
//            } catch (IOException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                return false;
//            }
        }

        return outValue;

    }

    public String findAllByGroup(String inGroup) {
        //log.info("authenticating {0}", credentials.getUsername());

        Client client = Client.create();
        //for use in the webResource.post(...) we were getting an error when trying to access the CF service without the trailing "/"
        String url = AUTHSERVICE  + "Groups/" + inGroup  + "/";

 //All groups that a given person is in.
//https://fyi.health.state.mn.us/services/ldap/People/deanj1/?apikey=21C32FCA2170D71627712C6221375CBB

//query for groups
//        WebResource webResource = client.resource(url);
        WebResource webResource = null;
        webResource = client.resource(url);

        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("apikey", APIKEY);
//        queryParams.add("action", "auth");


        String s = webResource.type("application/x-www-form-urlencoded").post(String.class, queryParams);
//        String s = webResource.queryParams(queryParams).post(String.class, queryParams);

        return s;

    }


    public Collection findAll() throws InfrastructureException { return null; }
    public Collection findByExample(User user) throws InfrastructureException { return null; }
    public User getUserById(Long personId, boolean lock) throws InfrastructureException  { return null; }
    public void makePersistent(User user) throws InfrastructureException {}

   public User findUserByUsername(String username){
      User user = new User();
//      try{
//         LDAPConnection conn = LdapDAOFactory.createConnection();
//         String[] ATTR_LIST = {"mdhChallengeQuestion", "mdhChallengeAnswer",
//                               "fullName", "givenName", "sn", "mdhContactID",
//                               "mdhChangePasswordInd", "mail", "mdhActiveInd"};
//         LDAPSearchResults res = conn.search(BASE_DN,
//                                             LDAPConnection.SCOPE_SUB,
//                                             "cn=" + username,
//                                             ATTR_LIST,
//                                             false);
//         if(res.hasMore()){
//               LDAPEntry entry = res.next();
//               LDAPAttribute attr = entry.getAttribute("mdhChallengeQuestion");
//               if(attr != null){
//                  user.setChallengeQuestion((attr.getStringValueArray())[0]);
//               }
//
//               attr = entry.getAttribute("mdhChallengeAnswer");
//               if(attr != null){
//                  user.setChallengeAnswer((attr.getStringValueArray())[0]);
//               }
//
//               attr = entry.getAttribute("givenName");
//               if(attr != null){
//                  user.setFirstName((attr.getStringValueArray())[0]);
//               }
//
//               attr = entry.getAttribute("sn");
//               if(attr != null){
//                  user.setLastName((attr.getStringValueArray())[0]);
//               }
//
//
//               attr = entry.getAttribute("mdhContactID");
//               if(attr != null){
//                  user.setPersonId(new Long(((attr.getStringValueArray())[0])));
//               }
//
//
//               attr = entry.getAttribute("mdhChangePasswordInd");
//               String changePWFlag = (attr.getStringValueArray())[0];
//               if(changePWFlag.equalsIgnoreCase("Y") || changePWFlag.equalsIgnoreCase("Yes")){
//                 this.printMessage("change pw flag is true");
//                 user.setChangePasswordIndicator(Boolean.TRUE);
//               }
//               else{
//                  this.printMessage("change pw flag is false");
//                  user.setChangePasswordIndicator(Boolean.FALSE);
//               }
//
//               attr = entry.getAttribute("mail");
//               if(attr != null){
//                  //user.setEmailAddress(attr.getStringValueArray()[0]);
//               }
//
//               attr = entry.getAttribute("mdhActiveInd");
//               String activeInd = "";
//               if(attr != null){
//                  activeInd = attr.getStringValueArray()[0];
//               }
//               if(activeInd.equalsIgnoreCase("Y") || activeInd.equalsIgnoreCase("Yes")){
//                     this.printMessage("activeInd is true");
//                     user.setActiveIndicator(Boolean.TRUE);
//               }
//               else{
//                  this.printMessage("activeInd is false");
//                  user.setActiveIndicator(Boolean.FALSE);
//               }
//         }
//         else{ //did not find an entry that matched the given login ID, so return null.
//           return null;
//         }
//         LdapDAOFactory.closeConnection(conn);
//      }
//      catch(LDAPException e){
//         this.printMessage("LDAPException in findUser(): " + e.getLDAPErrorMessage());
//      }
//      catch(Exception e){
//         this.printMessage("Exception in findUser(): " + e.getMessage());
//      }
      user.setUsername(username);
      user.setLoginAttempts(new Integer(0));

      return user;
   }

    public String groupAuthority(String username) {
        return authenticationGROUP(username);


    }


//    private String getLoginDN(LDAPConnection conn, String loginID)
//                                             throws LDAPException {
//         String[] ATTR_LIST = {"cn"};
//         LDAPSearchResults res = conn.search(BASE_DN,
//                                             LDAPConnection.SCOPE_SUB,
//                                             "cn="+loginID,
//                                             ATTR_LIST,
//                                             false);
//         if(res != null && res.getCount() > 0 &&  res.hasMore()){
//               LDAPEntry entry = res.next();
//               return entry.getDN();
//         }
//         return "";
//   }//end of getLoginDN()
//
//   public String getChallengeQuestion(String loginID){
//      String challengeQuestion = "";
//      try{
//         LDAPConnection conn = LdapDAOFactory.createConnection();
//         String[] ATTR_LIST = {"mdhChallengeQuestion"};
//         LDAPSearchResults res = conn.search("ou=active,ou=people,o=HEALTH-STATE-MN-US",
//                                             LDAPConnection.SCOPE_SUB,
//                                             "cn="+loginID,
//                                             ATTR_LIST,
//                                             false);
//         if(res.hasMore()){
//               LDAPEntry entry = res.next();
//               LDAPAttribute attr = entry.getAttribute(ATTR_LIST[0]);
//               challengeQuestion = (attr.getStringValueArray())[0];
//         }
//
//         LdapDAOFactory.closeConnection(conn);
//      }
//      catch(LDAPException e){
//         this.printMessage("LDAPException in getChallengeQuestion(): " + e.getLDAPErrorMessage());
//      }
//      this.printMessage("challengeQuestion = " + challengeQuestion);
//      return challengeQuestion;
//   }
//
//   public String getChallengeAnswer(String loginID){
//      String challengeAnswer = "";
//      try{
//         LDAPConnection conn = LdapDAOFactory.createConnection();
//         String[] ATTR_LIST = {"mdhChallengeAnswer"};
//         LDAPSearchResults res = conn.search("ou=active,ou=people,o=HEALTH-STATE-MN-US",
//                                             LDAPConnection.SCOPE_SUB,
//                                             "cn="+loginID,
//                                             ATTR_LIST,
//                                             false);
//         if(res.hasMore()){
//               LDAPEntry entry = res.next();
//               LDAPAttribute attr = entry.getAttribute(ATTR_LIST[0]);
//               challengeAnswer = (attr.getStringValueArray())[0];
//         }
//         LdapDAOFactory.closeConnection(conn);
//      }
//      catch(LDAPException e){
//         this.printMessage("LDAPException in getChallengeAnswer(): " + e.getLDAPErrorMessage());
//      }
//      this.printMessage("challengeAnswer = " + challengeAnswer);
//      return challengeAnswer;
//   }
//
//   public String getFullName(String loginID){
//     String fullName = "";
//     try{
//         LDAPConnection conn = LdapDAOFactory.createConnection();
//         String[] ATTR_LIST = {"fullName", "givenName", "sn"};
//         LDAPSearchResults res = conn.search("ou=active,ou=people,o=HEALTH-STATE-MN-US",
//                                             LDAPConnection.SCOPE_SUB,
//                                             "cn="+loginID,
//                                             ATTR_LIST,
//                                             false);
//         if(res.hasMore()){
//               LDAPEntry entry = res.next();
//               LDAPAttribute attr = entry.getAttribute("fullName");
//               if(attr != null){
//                  fullName = (attr.getStringValueArray())[0];
//               }
//               else{
//                  StringBuffer buffer = new StringBuffer();
//                  attr = entry.getAttribute("givenName");
//                  buffer.append((attr.getStringValueArray())[0]);
//                  attr = entry.getAttribute("sn");
//                  buffer.append((attr.getStringValueArray())[0]);
//                  fullName = buffer.toString();
//               }
//         }
//         LdapDAOFactory.closeConnection(conn);
//      }
//      catch(LDAPException e){
//         this.printMessage("LDAPException in getFullName(): " + e.getLDAPErrorMessage());
//      }
//      this.printMessage("fullName = " + fullName);
//      return fullName;
//   }
//
//   public boolean setUserPassword(String loginID, String newPassword){
//      boolean result = true;
//      try{
//         LDAPConnection conn = LdapDAOFactory.createConnection();
//         LDAPAttribute passwordAttr = new LDAPAttribute("userPassword", newPassword);
//         LDAPAttribute changePWIndAttr = new LDAPAttribute("mdhChangePasswordInd", "N");
//
//         /* create an array list that specifies that we want
//          * to modify the password and the change password flag*/
//         ArrayList modList = new ArrayList();
//         modList.add(new LDAPModification(LDAPModification.REPLACE, passwordAttr));
//         modList.add(new LDAPModification(LDAPModification.REPLACE, changePWIndAttr));
//
//         /* perform the modification (change the password) */
//         LDAPModification[] mods = new LDAPModification[modList.size()];
//         mods = (LDAPModification[])modList.toArray(mods);
//         String loginDN = this.getLoginDN(conn,loginID);
//         conn.modify(loginDN, mods);
//         LdapDAOFactory.closeConnection(conn);
//      }
//      catch(LDAPException e){
//         result = false;
//         this.printMessage("LDAPException in setUserPassword(): " + e.getLDAPErrorMessage());
//      }
//      this.printMessage("setUserPassword - password has been changed");
//      return result;
//   }
//
//   public boolean setChallengeQuestionAndAnswer(String loginID,
//                                                String challengeQuestion,
//                                                String challengeAnswer){
//      boolean result = true;
//      try{
//         LDAPConnection conn = LdapDAOFactory.createConnection();
//         LDAPAttribute challengeQuestionAttr = new LDAPAttribute("mdhChallengeQuestion", challengeQuestion);
//         LDAPAttribute challengeAnswerAttr = new LDAPAttribute("mdhChallengeAnswer", challengeAnswer);
//
//         /* create an array list that specifies that we want
//          * to set the challenge question.  Using LDAPModification.REPLACE
//          * indicates that if the attribute is not already on the entry,
//          * then the attribute is created and the values added. */
//         ArrayList modList = new ArrayList();
//         modList.add(new LDAPModification(LDAPModification.REPLACE, challengeQuestionAttr));
//         modList.add(new LDAPModification(LDAPModification.REPLACE, challengeAnswerAttr));
//
//         /* perform the modification  */
//         LDAPModification[] mods = new LDAPModification[modList.size()];
//         mods = (LDAPModification[])modList.toArray(mods);
//         String loginDN = this.getLoginDN(conn,loginID);
//         conn.modify(loginDN, mods);
//         LdapDAOFactory.closeConnection(conn);
//      }
//      catch(LDAPException e){
//         result = false;
//         this.printMessage("LDAPException in setChallengeQuestionAndAnswer(): " + e.getLDAPErrorMessage());
//      }
//      this.printMessage("setChallengeQuestionAndAnswer - Q&A has been set!");
//      return result;
//   }
    public java.sql.Connection getConnection() {
        return null;
    }
    public  String getErrorMSG() {
        return errorMSG;
    }


    public String getErrorType() {
        return errorType;
    }


    private void printMessage(String message){
     log.debug(message);
   }

    private static boolean isTextJson(String test){
        try{
            new JsonFastParser().parse(test)
        }catch (JsonException e){
            return false;
        }
        return true;
    }
}