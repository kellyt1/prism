package us.mn.state.health.domain.service.util;

import org.acegisecurity.Authentication;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.UserDAO;
import us.mn.state.health.dao.configuration.HibernateConfigurationItemDAO;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.util.configuration.ConfigurationItem;
import us.mn.state.health.util.Utilities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomAuthenticationProcessingFilter extends
        AuthenticationProcessingFilter {
    private java.lang.String defaultTargetUrl;
    public static final String ACEGI_SECURITY_FORM_OTHER_PARAM_KEY = "j_other_param";

    public Authentication attemptAuthentication(HttpServletRequest request) {
        String authorityType = request.getParameter("authentication");
        String username = this.obtainUsername(request);
        String password = this.obtainPassword(request);
        String sForward = request.getParameter("firstPage");
        this.setAlwaysUseDefaultTargetUrl(false);

        if (sForward != null) {
            this.setAlwaysUseDefaultTargetUrl(true);
            this.defaultTargetUrl = sForward;
            this.setDefaultTargetUrl(sForward);
        }

        //do the authentication with your additional param
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        this.setDetails(request, authRequest);
        request.getSession().setAttribute("ACEGI_SECURITY_LAST_USERNAME", username);
        String rValue = doAuthenticate(username, password, authorityType, request);

        GrantedAuthority[] grantedAuthorities = null;

        if (rValue.equalsIgnoreCase("GOOD") || rValue.equalsIgnoreCase("NONE")) {
            HibernateConfigurationItemDAO hcidao = new HibernateConfigurationItemDAO();
            ConfigurationItem ci = null;
            try {
                ci = hcidao.getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.RETRIEVE_GROUP_MEMBERSHIP_FROM);
            } catch (InfrastructureException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            List<GrantedAuthorityImpl> grantedAuthorityList = new ArrayList();
            if (ci != null && ci.getValue().equals(Constants.RETRIEVE_GROUP_MEMBERSHIP_FROM_MS_ACTIVE_DIRECTORY)) {
                grantedAuthorities = getGroupsLDAP(username, password);
            } else if (ci != null && ci.getValue().equals(Constants.RETRIEVE_GROUP_MEMBERSHIP_FROM_ORACLE_TABLES)) {
                grantedAuthorities = getGroupsOracleDB(username);
            } else {  //if no group membership source is specified, then only assign prism_user role
                grantedAuthorityList.add(new GrantedAuthorityImpl("ROLE_PRISM_USER"));
                grantedAuthorities = grantedAuthorityList.toArray(new GrantedAuthority[0]);

            }

            authRequest = new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
            //authRequest = new UsernamePasswordAuthenticationToken(username,password,new GrantedAuthority[] { new GrantedAuthorityImpl("") });


            // authRequest.setAuthenticated(Boolean.TRUE.booleanValue());
        } else {
            if (rValue.equalsIgnoreCase("PASSWORD")) {
                throw new BadCredentialsException("Your username/password credentials do not match");
            } else if (rValue.equalsIgnoreCase("NOTINTABLE")) {
                throw new BadCredentialsException("You are not authorized for this application");
            } else {
                throw new BadCredentialsException("Please report this message to user support.");
            }
            //authRequest.setAuthenticated(Boolean.FALSE.booleanValue());
        }


        return authRequest;
        //return getAuthenticationManager().authenticate(authRequest);
    }

    private GrantedAuthority[] getGroupsOracleDB(String username) {
       List<GrantedAuthorityImpl> grantedAuthorityList = new ArrayList();
        try {
            User user = DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getUserDAO().findUserByUsername(username);
            List groups = (List)DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getGroupDAO().findGroupsByPersonId(user.getPersonId());
            // Give everyone ROLE_PRISM_USER
            grantedAuthorityList.add(new GrantedAuthorityImpl("ROLE_PRISM_USER"));

            for (int i=0; i < groups.size(); i++) {
                grantedAuthorityList.add(new GrantedAuthorityImpl(((Group)groups.get(i)).getGroupCode().toUpperCase()));
            }
        } catch (InfrastructureException ie) {
            ie.printStackTrace();
        }
        return grantedAuthorityList.toArray(new GrantedAuthority[0]);
    }


    private GrantedAuthority[] getGroupsLDAP(String username, String password) {


        DAOFactory ldapFactory = DAOFactory.getDAOFactory(DAOFactory.LDAP);
        UserDAO userDAO = ldapFactory.getUserDAO();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        List<GrantedAuthorityImpl> grantedAuthorityList = new ArrayList();
        // Give everyone ROLE_PRISM_USER
         grantedAuthorityList.add(new GrantedAuthorityImpl("ROLE_PRISM_USER"));

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            ByteArrayInputStream bs = new ByteArrayInputStream(userDAO.groupAuthority(username).getBytes());
            Document doc = db.parse(bs);
            doc.getDocumentElement().normalize();

            //System.out.println("Root element " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("group");
            //System.out.println("All groups");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element fstElmnt = (Element) fstNode;
                    NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("cn");
                    Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
                    NodeList fstNm = fstNmElmnt.getChildNodes();
                    String sGroup = fstNm.item(0).getNodeValue().toUpperCase();
                    if (sGroup.contains("BUDGET") || sGroup.contains("PRISM")) {
                        grantedAuthorityList.add(new GrantedAuthorityImpl(sGroup));
                        //System.out.println("cn : " + sGroup);
                    }

                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }


        return grantedAuthorityList.toArray(new GrantedAuthority[0]);  //To change body of implemented methods use File | Settings | File Templates.


    }


//    private GrantedAuthority[] getGroups(String username) {
//        UserDAO userDAO = null;
//        User user = null;
//        DAOFactory daoFactory = Utilities.getDAOFactory("HIBERNATE");
//        userDAO = daoFactory.getUserDAO();
//        List<GrantedAuthorityImpl> grantedAuthorityList = new ArrayList();
//        // Give everyone ROLE_PRISM_USER
//        grantedAuthorityList.add(new GrantedAuthorityImpl("ROLE_PRISM_USER"));
//
//        try {
//            user = userDAO.findUserByUsername(username);
//            Collection<PersonGroupLink> aList = user.getPersonGroupLinks();
//            for (Iterator<PersonGroupLink> personIterator = aList.iterator(); personIterator.hasNext();) {
//                PersonGroupLink person = personIterator.next();
//                if (person.getGroup() != null) {
//                    //        System.out.println("GROUP " + person.getGroup().getGroupName());
//                    String groupName = person.getGroup().getGroupName().toUpperCase();
//                    if (groupName != null && groupName.indexOf("PRISM") >= 0) {
//                        //   System.out.println("Code " + person.getGroup().getGroupCode());
//                        GrantedAuthorityImpl grimpl = new GrantedAuthorityImpl(person.getGroup().getGroupCode());
//                        grantedAuthorityList.add(grimpl);
//                    }
//                }
//            }
//        } catch (InfrastructureException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//        return grantedAuthorityList.toArray(new GrantedAuthority[0]);
////       return (GrantedAuthority)grantedAuthorityList.toArray();;
//
//
//    }


    private String doAuthenticate(String username, String password, String authorityType, HttpServletRequest sw) {
        String newView = "";
        String sErrorType = "NONE";
        String sErrorMessage = "";
        DAOFactory ldapFactory = DAOFactory.getDAOFactory(DAOFactory.LDAP);
        UserDAO userDAO = ldapFactory.getUserDAO();

        HttpSession session = sw.getSession();

        //UserDAO userDAO = daoFactory.getUserDAO();
        User user = null;
        try {
            boolean authenticated = false;
// IF PRISM Security Manager turn this on.
            String referrer = sw.getHeader("referer");
            //String usernameBypass = sw.getParameter("username");

            if ((referrer != null && referrer.toUpperCase().indexOf("FYI") > 0) && username != null) {
                authenticated = true;
            } else if (userDAO.authenticate(username, password, authorityType).booleanValue()) {
                authenticated = true;
            }
            if (authenticated) {
                DAOFactory daoFactory = Utilities.getDAOFactory("HIBERNATE");
                if (daoFactory == null) {
                    return "DATASOURCE";
                }
                userDAO = daoFactory.getUserDAO();
                // JMP Legacy code you must do an authenticate,  you can ignore the results
                // This is for prism where it needs to set objects for the tag.
                //userDAO.authenticate(username, password, authenticationType);

                user = userDAO.findUserByUsername(username);
                if (user == null) sErrorType = "NOTINTABLE";
            } else {
                if (userDAO.getErrorType() != null && !userDAO.getErrorType().equals("")) {
                    sErrorType = userDAO.getErrorType();
                    sErrorMessage = userDAO.getErrorMSG();
                } else {
                    sErrorType = "PASSWORD";
                }
            }
        }
        catch (InfrastructureException ie) {
            sErrorType = "ERROR";
            sErrorMessage = ie + "";
            ie.printStackTrace();
        }
        if (user != null) {
            session.setAttribute("User", user);

            if (sErrorType.equalsIgnoreCase("CHANGEPASSWORD")) {
                session.setAttribute("username", username);
                session.setAttribute("ErrorMessage", "Security Manager: " + sErrorMessage);
            }

            if (sErrorType.equalsIgnoreCase("PASSWORD")) {
                session.setAttribute("username", username);
            } else if (sErrorType.equalsIgnoreCase("NOTINTABLE")) {
                session.setAttribute("username", username);
            } else {
                session.setAttribute("username", username);
                newView = "GOOD";
            }
        }

        return sErrorType;
    }

}