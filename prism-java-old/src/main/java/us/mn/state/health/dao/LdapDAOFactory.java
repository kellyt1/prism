package us.mn.state.health.dao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Calendar;
import java.util.Hashtable;

import javax.naming.*;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.StartTlsRequest;
import javax.naming.ldap.StartTlsResponse;
//import com.novell.ldap.LDAPConnection;
//import com.novell.ldap.LDAPException;
//import com.novell.ldap.LDAPJSSESecureSocketFactory;
//import com.novell.ldap.LDAPSocketFactory;
import com.sun.net.ssl.internal.ssl.Provider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class LdapDAOFactory extends BaseDAOFactory {
    private static Log log = LogFactory.getLog(LdapDAOFactory.class);
//    private static int LDAPVERSION = LDAPConnection.LDAP_V3;
//    private static int SCOPE = LDAPConnection.SCOPE_SUB;

    private static String host;
    private static String hostBackup;
    private static final String domain = ".health.state.mn.us";
    private static int port;
    private static String userID;
    private static String password;
    private static String errorType = "";
    private static String errorMSG = "";

    private static String BASEDN = "o=MDH";

    public LdapDAOFactory() {
    }

    public void setHost(String host) {
        LdapDAOFactory.host = host;
    }

    public String getHost() {
        return LdapDAOFactory.host;
    }

    public static String getHostBackup() {
        return hostBackup;
    }

    public static void setHostBackup(String hostBackup) {
        LdapDAOFactory.hostBackup = hostBackup;
    }

    public void setPort(String port) {
        LdapDAOFactory.port = (int) Integer.parseInt(port);
    }

    public void setPort(int port) {
        LdapDAOFactory.port = port;
    }

    public int getPort() {
        return LdapDAOFactory.port;
    }

    public void setUserID(String userID) {
        LdapDAOFactory.userID = userID;
    }

    public String getUserID() {
        return LdapDAOFactory.userID;
    }

    public void setPassword(String password) {
        LdapDAOFactory.password = password;
    }

    public String getPassword() {
        return LdapDAOFactory.password;
    }


    public static String getErrorType() {
        return errorType;
    }

    public static void setErrorType(String errorType) {
        LdapDAOFactory.errorType = errorType;
    }


    public static String getErrorMSG() {
        return errorMSG;
    }

    public static void setErrorMSG(String errorMSG) {
        LdapDAOFactory.errorMSG = errorMSG;
    }


    public boolean createConnectionToActiveDirectory(String inHost) {
        boolean returnValue = Boolean.FALSE.booleanValue();
        Hashtable env = new Hashtable(11);
        Hashtable dx = new Hashtable(1);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

        // Must use the name of the server that is found in its certificate
        //String surl =  "ldap://" + host +":" + port + "/ou=Organizational CA/o=MDH_TREE";
        String surl = "ldap://" + inHost + ":" + port + "/" ;//+ BASEDN;
        env.put(Context.PROVIDER_URL, surl);
        env.put(Context.SECURITY_AUTHENTICATION,"DIGEST-MD5");
        env.put(Context.SECURITY_PRINCIPAL,userID.toLowerCase());
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put("javax.security.sasl.qop","auth-conf");
        // Create initial context
        InitialDirContext ctx = null;
        // Open an LDAP association
        try {
            ctx = new InitialDirContext(env);
            returnValue = (ctx != null) ;
            errorType = "";
            errorMSG = "";
        } catch (CommunicationException e) {
            returnValue = Boolean.FALSE.booleanValue();
            errorType = "RETRY";
            errorMSG = "";
        } catch (AuthenticationException e) {
                returnValue = Boolean.FALSE.booleanValue();
                errorType = "PASSWORD";
                 errorMSG = "";
        } catch (Exception e) {
              returnValue = Boolean.FALSE.booleanValue();
               errorType = "UNKNOWN";
                errorMSG = e.toString();
            log.error("Error in LdapDAOFactory: 2", e);
        }

        return returnValue;
}


    public boolean createConnectionTLS() {
        boolean returnValue = false;
        try {
//                if(true) return true;
            //              DirContext cty = new InitialDirContext();
            //Supported Sasl mechanisms EXTERNAL, DIGEST-MD5, NMAS_LOGIN
// Read supportedSASLMechanisms from root DSE
            //             Attributes attrs = cty.getAttributes("ldap://mdh-nds1.health.state.mn.us:389", new String[]{"supportedSASLMechanisms"});

// Set up environment for creating initial context
            Hashtable env = new Hashtable(11);
            Hashtable dx = new Hashtable(1);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

// Must use the name of the server that is found in its certificate
            //String surl =  "ldap://" + host +":" + port + "/ou=Organizational CA/o=MDH_TREE";
            String surl = "ldap://" + host + ":" + port + "/" + BASEDN;
            env.put(Context.PROVIDER_URL, surl);

            // Create initial context
            StartTlsResponse tls = null;
            LdapContext ctx = null;
            // Open an LDAP association
            try {
                ctx = new InitialLdapContext(env, null);
                // Perform a StartTLS extended operation

                tls = (StartTlsResponse) ctx.extendedOperation(new StartTlsRequest());
                tls.negotiate();
            } catch (Exception e) {
                // Open a TLS connection (over the existing LDAP association) and get details
                // of the negotiated TLS session: cipher suite, peer certificate, ...

                // If you cannot negotiate primary try the secondary.
                log.info("Security Manager fail over to " + hostBackup);
                surl = "ldap://" + hostBackup + ":" + port + "/" + BASEDN;
                env.put(Context.PROVIDER_URL, surl);
                // Open an LDAP association
                try {
                    ctx = new InitialLdapContext(env, null);
                    tls = (StartTlsResponse) ctx.extendedOperation(new StartTlsRequest());

                    tls.negotiate();
                } catch (Exception e2) {
                    // Try it with the domain affixed

                    log.info("Security Manager fail over to " + host.replaceAll(domain, ""));
                    surl = "ldap://" + host.replaceAll(domain, "") + ":" + port + "/" + BASEDN;
                    env.put(Context.PROVIDER_URL, surl);
                    // Open an LDAP association
                    ctx = new InitialLdapContext(env, null);
                    tls = (StartTlsResponse) ctx.extendedOperation(new StartTlsRequest());
                    tls.negotiate();
                }
            }

            String[] ATTR_LIST = {"mdhChallengeQuestion", "mdhChallengeAnswer",
                    "fullName", "givenName", "sn", "mdhContactID",
                    "mdhChangePasswordInd", "mail", "mdhActiveInd", "loginDisabled"
                    , "passwordExpirationTime","loginGraceRemaining"};
//               String[] ATTR_LIST = {"cn"};

            SearchControls sc = new SearchControls();
            sc.setReturningAttributes(ATTR_LIST);
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String filter = "(&(cn=" + userID + "))";
            NamingEnumeration answer = ctx.search("", filter, sc);
            String DN = "";
            String sExpiryDate = "";
            String sGraceRemaining="";
            if (answer != null && answer.hasMore()) {

                boolean activeFound = false;
                while (answer.hasMoreElements() && !activeFound) {
                    SearchResult sr = (SearchResult) answer.next();
                    Attributes aAttributes = sr.getAttributes();
//System.out.println("Attributes " + aAttributes);
                    String lValue = "";
                    if (aAttributes.get("loginDisabled") != null) lValue = aAttributes.get("loginDisabled").toString();
                    DN = "" + sr.getName() + "," + BASEDN;
                    if (lValue.equals("") || lValue.toUpperCase().indexOf("TRUE") < 0) {
                        if (aAttributes.get("passwordExpirationTime") != null) sExpiryDate = aAttributes.get("passwordExpirationTime").toString();
                        if(aAttributes.get("loginGraceRemaining") != null) sGraceRemaining = aAttributes.get("loginGraceRemaining").toString();

                        activeFound = true;
                    }
                }
            } else {
                answer = null;
            }

            // Now that you have a base DN,  authenticate the password.
            ctx.addToEnvironment(Context.SECURITY_AUTHENTICATION, "simple");
            ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, DN);
            ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);

            // Force authentication
            DirContext xp = new InitialDirContext(ctx.getEnvironment());

            // If you are authenticated go force them to change passwords if password has expired
            // Get current date in format YYYYMMDD
            try {
                if (!sExpiryDate.equals("")) {
                    sExpiryDate = sExpiryDate.replaceAll("passwordExpirationTime:", "").trim();
                }
                if (!sExpiryDate.equals("") && sExpiryDate.length() >= 8) {

                    String testValue = sExpiryDate.substring(0, 8);
                    String DATE_FORMAT = "yyyyMMdd";
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
                    Calendar c1 = Calendar.getInstance(); // today
                    String cValue = sdf.format(c1.getTime());
                    int xpTime = Integer.valueOf(testValue).intValue();
                    int serverTime = Integer.valueOf(cValue).intValue();
                    if (xpTime > 19999999 && xpTime <= 20990101 && serverTime > 19999999 && serverTime <= 20990101 && (xpTime < serverTime)) {

                        if (!sGraceRemaining.equals("")) sGraceRemaining = sGraceRemaining.toUpperCase().replaceAll("LOGINGRACEREMAINING:","").trim();
                        log.info("Password Change forced User " + userID + " LDAP EXP: " + testValue + " Server COMP " + cValue);
                        log.info("Full LDAP Entry " + sExpiryDate);
                        //errorMSG = "Your password has expired.  Please change your password immediately";
                        String expirationDate = sExpiryDate;
                        if (sExpiryDate.length() >= 8) {
                            expirationDate = sExpiryDate.substring(4,6) + "/" + sExpiryDate.substring(6,8) + "/" + sExpiryDate.substring(0,4) ;
                        }
                        errorMSG = "Your password expired " + expirationDate + ".  You have " + sGraceRemaining + " remaining grace logins";
                        errorType = "CHANGEPASSWORD";
                        try {
                            ctx.close();
                            // Close the TLS connection (revert back to the underlying LDAP association)
                            tls.close();
                        } catch (Exception e) {
                        }
// Override anyway
          //              errorMSG = "";
          //              errorType = "";
                        return true;
                    }
                          else if (!sGraceRemaining.equals("")) {
                          sGraceRemaining = sGraceRemaining.toUpperCase().replaceAll("LOGINGRACEREMAINING:","").trim();
                          //errorMSG = "Password Expiration date " + sExpiryDate + " and you have " + sGraceRemaining + " remaining grace logins";
                          //errorType = "CHANGEPASSWORD";
                            errorType = "";
                            errorMSG = "";
//                        String expirationDate = sExpiryDate;
//                        if (sExpiryDate.length() >= 8) {
//                            expirationDate = sExpiryDate.substring(4,6) + "/" + sExpiryDate.substring(6,8) + "/" + sExpiryDate.substring(0,4) ;
//                        }

//                          errorMSG = "Your password will expire on " + expirationDate;
                          return true;
                     } else {
                         errorMSG = "";
                         errorType = "";
                     }
                }
            } catch (Exception e) {

            }


            ctx.close();
            // Close the TLS connection (revert back to the underlying LDAP association)
            tls.close();
            // ... use ctx to perform unprotected LDAP operations
            // If you made it here then you were validated
            if (answer != null) {
            returnValue = true;
            } else {
            returnValue = false;
            }
        }
        catch (NamingException e) {
            errorMSG = e + "";
            errorType = "PASSWORD";
            returnValue = false;
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            log.error("Error in LdapDAOFactory - very possible because the username/passwd were not good");
        } catch (IOException e) {
            errorType = "OTHER";
            errorMSG = e + "";
            returnValue = false;
            log.error("Error in LdapDAOFactory:", e);
        }
        return returnValue;
    }

    public int changePasswordTLS(String newPassword) {
        int returnValue = -1;
        try {

// Set up environment for creating initial context
            Hashtable env = new Hashtable(11);
            Hashtable dx = new Hashtable(1);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

// Must use the name of the server that is found in its certificate
            //String surl =  "ldap://" + host +":" + port + "/ou=Organizational CA/o=MDH_TREE";
            String surl = "ldap://" + host + ":" + port + "/" + BASEDN;
            env.put(Context.PROVIDER_URL, surl);

            // Create initial context
            // Open an LDAP association
            LdapContext ctx = new InitialLdapContext(env, null);

            // Perform a StartTLS extended operation
            StartTlsResponse tls =
                    (StartTlsResponse) ctx.extendedOperation(new StartTlsRequest());

            // Open a TLS connection (over the existing LDAP association) and get details
            // of the negotiated TLS session: cipher suite, peer certificate, ...

            try {
                tls.negotiate();
            } catch (Exception e) {
                // If you cannot negotiate primary try the secondary.
                log.info("Security Manager fail over to " + hostBackup);
                surl = "ldap://" + hostBackup + ":" + port + "/" + BASEDN;
                env.put(Context.PROVIDER_URL, surl);
                // Open an LDAP association
                try {
                    ctx = new InitialLdapContext(env, null);
                    tls = (StartTlsResponse) ctx.extendedOperation(new StartTlsRequest());

                    tls.negotiate();
                } catch (Exception e2) {
                    // Try it with the domain affixed

                    log.info("Security Manager fail over to " + host.replaceAll(domain, ""));
                    surl = "ldap://" + host.replaceAll(domain, "") + ":" + port + "/" + BASEDN;
                    env.put(Context.PROVIDER_URL, surl);
                    // Open an LDAP association
                    ctx = new InitialLdapContext(env, null);
                    tls = (StartTlsResponse) ctx.extendedOperation(new StartTlsRequest());
                    tls.negotiate();
                }
            }


            String[] ATTR_LIST = {"mdhChallengeQuestion", "mdhChallengeAnswer",
                    "fullName", "givenName", "sn", "mdhContactID",
                    "mdhChangePasswordInd", "mail", "mdhActiveInd", "loginDisabled"};

            SearchControls sc = new SearchControls();
            sc.setReturningAttributes(ATTR_LIST);
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String filter = "(&(cn=" + userID + "))";
            NamingEnumeration answer = ctx.search("", filter, sc);
            String DN = "";
            String TESTDN = "";
            if (answer != null && answer.hasMore()) {

                boolean activeFound = false;
                while (answer.hasMoreElements() && !activeFound) {
                    SearchResult sr = (SearchResult) answer.next();
                    Attributes aAttributes = sr.getAttributes();
                    String lValue = "";
                    if (aAttributes.get("loginDisabled") != null) lValue = aAttributes.get("loginDisabled").toString();
                    TESTDN = sr.getName();
                    DN = "" + sr.getName() + "," + BASEDN;
                    if (lValue.equals("") || lValue.toUpperCase().indexOf("TRUE") < 0) {
                        activeFound = true;
                    }
                }
            }

            // Now that you have a base DN,  authenticate the password.
            ctx.addToEnvironment(Context.SECURITY_AUTHENTICATION, "simple");
            ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, DN);
            ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);

            // Force authentication
            // DirContext xp = new InitialDirContext(ctx.getEnvironment());
            LdapContext xp = new InitialLdapContext(ctx.getEnvironment(), null);

            StartTlsResponse tls2 = (StartTlsResponse) xp.extendedOperation(new StartTlsRequest());
            tls2.negotiate();

            //set password is a ldap modfy operation
            ModificationItem[] mods = new ModificationItem[2];

            //Replace the "unicdodePwd" attribute with a new value
            //Password must be both Unicode and a quoted string
            String newQuotedPassword = "\"" + newPassword + "\"";
            byte[] newUnicodePassword = newQuotedPassword.getBytes("UTF-16LE");


            mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("userPassword", password));
            mods[1] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("userPassword", newPassword));
//            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPassword", newUnicodePassword));

            // Perform the update
            xp.modifyAttributes(TESTDN, mods);

            log.info("Reset Password for: " + userID);
            tls2.close();
            tls.close();

            ctx.close();
            // Close the TLS connection (revert back to the underlying LDAP association)

            // ... use ctx to perform unprotected LDAP operations
            // If you made it here then you were validated
            returnValue = 0;
        }
        catch (NamingException e) {
            if (e.toString().toUpperCase().indexOf("DUPLICATE PASSWORD") > 1) {
                returnValue = -2;
            } else {
                returnValue = -1;
            }
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            returnValue = -1;
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return returnValue;
    }

//    public static LDAPConnection createConnection() {
//        try {
//            if (port == LDAPConnection.DEFAULT_SSL_PORT) { //set up an SSL connection
//                // Dynamically set JSSE as a security provider
//                Security.addProvider(new Provider());
//
//                // Dynamically set the property that JSSE uses to identify
//                // the keystore that holds trusted root certificates
//                String pathToKeystore = "ssl.keystore"; //its in same dir
//                System.setProperty("javax.net.ssl.TrustStore", pathToKeystore);
//
//                LDAPSocketFactory ssf = new LDAPJSSESecureSocketFactory();
//
//                // Set the socket factory as the default for all future connections
//                LDAPConnection.setSocketFactory(ssf);
//            }
//            LDAPConnection conn = new LDAPConnection();
//            conn.connect(host, port);
//            String sXX = "X";
//            if (conn.isConnected()) return conn;
//
//            //conn.bind(LDAPVERSION,"ou=NSP,o=MDH", sXX.getBytes("UTF8"));
//            //              conn.bind(LDAPVERSION,"cn=PughJ1,ou=NSP,o=MDH",password.getBytes("UTF8"));
//            //conn.bind(LDAPVERSION, userID, password.getBytes("UTF8"));
//
//
//        }
//        catch (LDAPException e) {
//            printMessage("LDAPException in createConnection(): " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
////        catch(UnsupportedEncodingException e){
////        printMessage("Unsupported EncodingE in createConnection(): " + e.getMessage());
////        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
////        return null;
////    }
//////        catch(UnsupportedEncodingException e) {
////            printMessage("UnsupportedEncodingException in createConnection():" + e.getMessage());
////            return null;
////        }
//        return null;
//    }
//
//    public static LDAPConnection createConnection(String host, int port,
//                                                  String bindDN, String password) {
//        try {
//            if (port == LDAPConnection.DEFAULT_SSL_PORT) { //set up an SSL connection
//                // Dynamically set JSSE as a security provider
//                Security.addProvider(new Provider());
//
//                // Dynamically set the property that JSSE uses to identify
//                // the keystore that holds trusted root certificates
//                String pathToKeystore = "ssl.keystore"; //its in same dir
//                System.setProperty("javax.net.ssl.TrustStore", pathToKeystore);
//
//                LDAPSocketFactory ssf = new LDAPJSSESecureSocketFactory();
//
//                // Set the socket factory as the default for all future connections
//                LDAPConnection.setSocketFactory(ssf);
//            }
//            LDAPConnection conn = new LDAPConnection();
//            conn.connect(host, port);
//            conn.bind(LDAPVERSION, bindDN, password.getBytes("UTF8"));
//            return conn;
//        }
//        catch (LDAPException e) {
//            printMessage("LDAPException in createConnection(String,int,String,String): " + e.getMessage());
//            return null;
//        }
//        catch (UnsupportedEncodingException e) {
//            printMessage("UnsupportedEncodingException in createConnection(String,int,String,String):" + e.getMessage());
//            return null;
//        }
//    }
//
//    public static void closeConnection(LDAPConnection conn) {
//        try {
//            conn.disconnect();
//        }
//        catch (LDAPException e) {
//            e.printStackTrace();
//        }
//    }

    public PersonDAO getPersonDAO() {
        return new LdapPersonDAO();
    }

    public EmployeeDAO getEmployeeDAO() {
        return new LdapEmployeeDAO();
    }

    public UserDAO getUserDAO() {
        return new LdapUserDAO();
    }

    private static void printMessage(String message) {
        log.debug(message);
    }
}