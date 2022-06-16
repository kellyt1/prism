package us.mn.state.health.model.util.email;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.email.ServiceLocatorException;

public class EmailServiceLocator {
    private static Log log = LogFactory.getLog(EmailServiceLocator.class);

    private InitialContext initialContext;
    private Map cache;

    private static EmailServiceLocator _instance;

    static {
        try {
            _instance = new EmailServiceLocator();
        } catch (ServiceLocatorException se) {
            log.error(se);
            se.printStackTrace(System.err);
        }
    }

    private EmailServiceLocator() throws ServiceLocatorException {
        try {
            initialContext = new InitialContext();
            cache = Collections.
                    synchronizedMap(new HashMap());
        } catch (NamingException ne) {
            throw new ServiceLocatorException(ne);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
    }

    static public EmailServiceLocator getInstance() {
        return _instance;
    }

    // implement lookup methods here
    public Session getMailSession(String jndiMailSessionName) {
        Session session = null;
        try {
            if (cache.containsKey(jndiMailSessionName)) {
                session = (Session) cache.get(jndiMailSessionName);
            }
            else {
                Context envCtx = (Context) initialContext.lookup("java:comp/env");
                session = (Session) envCtx.lookup(jndiMailSessionName);
                cache.put(jndiMailSessionName, session);
            }
        } catch (NamingException e) {
            throw new ServiceLocatorException("Couldn't find the Mail Session", e);
        }
        return session;
    }

    public Session getMailSessionWithOutJNDI(String smtpHost) {
        // Get system properties
        Properties props = System.getProperties();

        // Setup mail server
        props.put("mail.smtp.host", smtpHost);

        // Get session
        Session session = Session.getDefaultInstance(props, null);
        return session;
    }

    public Object getResource(String jndiName) {
        Object resource = null;
        try {
            if (cache.containsKey(jndiName)) {
                resource = cache.get(jndiName);
            }
            else {
                Context envCtx = (Context) initialContext.lookup("java:comp/env");
                resource = envCtx.lookup(jndiName);
                cache.put(jndiName, resource);
            }
        } catch (NamingException e) {
            throw new ServiceLocatorException("Couldn't find the Resource", e);
        }
        return resource;
    }
}