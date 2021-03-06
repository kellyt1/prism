package us.mn.state.health.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.util.search.LuceneIndexCreatorServlet;

/**
 * Basic Hibernate helper class, handles SessionFactory, Session and Transaction.
 * 
 * Uses a static initializer for the initial SessionFactory creation
 * and holds Session and Transactions in thread local variables. All
 * exceptions are wrapped in an unchecked InfrastructureException.
 *
 * @author christian@hibernate.org
 */
public class HibernateUtil {

    private static Log log = LogFactory.getLog(HibernateUtil.class);

    private static Configuration configuration;
    private static SessionFactory sessionFactory;
    private static ApplicationContext applicationContext;
    private static final ThreadLocal threadSession = new ThreadLocal();
    private static final ThreadLocal threadTransaction = new ThreadLocal();
    private static final ThreadLocal threadInterceptor = new ThreadLocal();

    // Create the initial SessionFactory from the default configuration files

    /**
    static {
        String env_value = System.getProperty(Constants.ENV_KEY);

        try {
            configuration = new Configuration();
            if(Constants.PROD.equalsIgnoreCase(env_value)){
                log.info("************************ using hibernatePROD.cfg.xml **********************************");
                sessionFactory = configuration.configure("/hibernatePROD.cfg.xml").buildSessionFactory();
            }
            else if(Constants.STAGE.equalsIgnoreCase(env_value)){
                log.info("************************ using hibernateSTAGE.cfg.xml ***************************************");
                sessionFactory = configuration.configure("/hibernateSTAGE.cfg.xml").buildSessionFactory();
            }

            else if(Constants.TEST.equalsIgnoreCase(env_value)){
                log.info("************************ using hibernateTEST.cfg.xml ***************************************");
                sessionFactory = configuration.configure("/hibernateTEST.cfg.xml").buildSessionFactory();
            }
            else if(Constants.DEVDB.equalsIgnoreCase(env_value)){
                log.info("************************* using hibernateDEVDB.cfg.xml ***************************************");
                sessionFactory = configuration.configure("/hibernateDEVDB.cfg.xml").buildSessionFactory();
            }
            else {
                sessionFactory = configuration.configure().buildSessionFactory();
            }
            // We could also let Hibernate bind it to JNDI:
            // configuration.configure().buildSessionFactory()
        }
        catch (Throwable ex) {
            // We have to catch Throwable, otherwise we will miss
            // NoClassDefFoundError and other subclasses of Error
            log.error("Building SessionFactory failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

     */
    static {
        String env_value = System.getProperty(Constants.ENV_KEY);
        applicationContext = LuceneIndexCreatorServlet.getApplicationContext();
        if (applicationContext == null){
            //get the context for non-web environments
           String[] configLocations = new String[]{"applicationContext-hibernate.xml"};
            applicationContext = new ClassPathXmlApplicationContext(configLocations);
        }
        Object o = null;
        try {

            if(Constants.PROD.equalsIgnoreCase(env_value)){
                o = applicationContext.getBean(Constants.PRDI_SESSION_FACTORY);
            }
            else if(Constants.STAGE.equalsIgnoreCase(env_value)){
                o = applicationContext.getBean(Constants.STG_SESSION_FACTORY);
            }

            else if(Constants.TEST.equalsIgnoreCase(env_value)){
                o = applicationContext.getBean(Constants.TST_SESSION_FACTORY);
            }
            else if(Constants.DEVDB.equalsIgnoreCase(env_value)){
                o = applicationContext.getBean(Constants.DEV_SESSION_FACTORY);
            }
            else {
//                sessionFactory = configuration.configure().buildSessionFactory();
                o = applicationContext.getBean(Constants.DEPLOYMENT_SESSION_FACTORY);
            }
            // We could also let Hibernate bind it to JNDI:
            // configuration.configure().buildSessionFactory()
            sessionFactory = (SessionFactory) o;
            
        }
        catch (Throwable ex) {
            // We have to catch Throwable, otherwise we will miss
            // NoClassDefFoundError and other subclasses of Error
            log.error("Building SessionFactory failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Returns the SessionFactory used for this static class.
     *
     * @return SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        /* Instead of a static variable, use JNDI:
          SessionFactory sessions = null;
          try {
              Context ctx = new InitialContext();
              String jndiName = "java:hibernate/HibernateFactory";
              sessions = (SessionFactory)ctx.lookup(jndiName);
          } catch (NamingException ex) {
              throw new InfrastructureException(ex);
          }
          return sessions;
          */
        return sessionFactory;
    }

    /**
     * Returns the original Hibernate configuration.
     *
     * @return Configuration
     */
    public static Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Rebuild the SessionFactory with the static Configuration.
     *
     */
     public static void rebuildSessionFactory() throws InfrastructureException {
        synchronized(sessionFactory) {
            try {
                sessionFactory = getConfiguration().buildSessionFactory();
            }
            catch (Exception ex) {
                log.error("Error in HibernateUtil.rebuildSessionFactory()"+ex.getMessage());
                throw new InfrastructureException(ex);
            }
        }
     }

    /**
     * Rebuild the SessionFactory with the given Hibernate Configuration.
     *
     * @param cfg
     */
     public static void rebuildSessionFactory(Configuration cfg) throws InfrastructureException {
        synchronized(sessionFactory) {
            try {
                sessionFactory = cfg.buildSessionFactory();
                configuration = cfg;
            }
            catch (Exception ex) {
                log.error("Error in HibernateUtil.rebuildSessionFactory(Configuration cfg)"+ex.getMessage());
                throw new InfrastructureException(ex);
            }
        }
     }

	/**
	 * Retrieves the current Session local to the thread.
	 * <p/>
	 * If no Session is open, opens a new Session for the running thread.
	 *
	 * @return Session
	 */
	public static Session getSession() throws InfrastructureException {
		Session s = (Session)threadSession.get();
		try {
			if(s == null || !s.isOpen()) {
				log.debug("Opening new Session for this thread.");
//                if(getInterceptor() != null) {
//					log.debug("Using interceptor: " + getInterceptor().getClass());
//                    s = getSessionFactory().openSession(getInterceptor());
//				}
//                else {
//					s = getSessionFactory().openSession();
//                }
                s = getSessionFactory().openSession((Interceptor) applicationContext.getBean("luceneInterceptor"));
                threadSession.set(s);
			}
		}
        catch(HibernateException ex) {
			throw new InfrastructureException(ex);
		}
		return s;
	}

    public static void setSession(Session s) {
        threadSession.set(s);
    }

	/**
	 * Closes the Session local to the thread.
	 */
	public static void closeSession() throws InfrastructureException {
		try {
			Session s = (Session)threadSession.get();
			threadSession.set(null);
			if(s != null && s.isOpen()) {
				log.debug("Closing Session of this thread.");
				s.close();
			}
		}
        catch(HibernateException ex) {
            log.error("Error in HibernateUtil.closeSession()"+ex.getMessage());
            throw new InfrastructureException(ex);
        }
    }

	/**
	 * Start a new database transaction.
	 */
	public static void beginTransaction() throws InfrastructureException {
		Transaction tx = (Transaction)threadTransaction.get();
		try {
			if(tx == null) {
				log.debug("Starting new database transaction in this thread.");
                tx = getSession().beginTransaction();
				threadTransaction.set(tx);
			}
		}
        catch(HibernateException ex) {
            log.error("Error in HibernateUtil.beginTransaction()"+ex.getMessage());
			throw new InfrastructureException(ex);
		}
	}

	/**
	 * Commit the database transaction.
	 */
	public static void commitTransaction() throws InfrastructureException {
		Transaction tx = (Transaction)threadTransaction.get();
        Session s = (Session)threadSession.get();
		try {
			if(s != null && s.isOpen() && 
               tx != null && !tx.wasCommitted() && !tx.wasRolledBack() ) {
				log.debug("Committing database transaction of this thread.");
                tx.commit();
			}
			threadTransaction.set(null);
		}
        catch(HibernateException ex) {
            log.error("Error in HibernateUtil.commitTransaction() "+ex.getMessage());
            rollbackTransaction();
            throw new InfrastructureException(ex);
        }
    }

	/**
	 * Commit the database transaction.
	 */
	public static void rollbackTransaction() throws InfrastructureException {
		Transaction tx = (Transaction)threadTransaction.get();
		try {
			threadTransaction.set(null);
			if(tx != null && !tx.wasCommitted() && !tx.wasRolledBack() ) {
				log.debug("Tyring to rollback database transaction of this thread.");
                tx.rollback();
			}
		}
        catch(HibernateException ex) {
            log.error("Error in HibernateUtil.rollbackTransaction()"+ex.getMessage());
			throw new InfrastructureException(ex);
		}
        finally {
            closeSession();
        }
    }

	/**
	 * Reconnects a Hibernate Session to the current Thread.
	 *
	 * @param session The Hibernate Session to be reconnected.
	 */
	public static void reconnect(Session session) throws InfrastructureException {
		try {
			session.reconnect();
			threadSession.set(session);
		}
        catch(HibernateException ex) {
            log.error(ex.getMessage());
			throw new InfrastructureException(ex);
		}
	}

	/**
	 * Disconnect and return Session from current Thread.
	 *
	 * @return Session the disconnected Session
	 */
	public static Session disconnectSession() throws InfrastructureException {
		Session session = getSession();
		try {
			threadSession.set(null);
			if(session.isConnected() && session.isOpen()) {
                session.disconnect();
            }
		}
        catch(HibernateException ex) {
            log.error("Error in HibernateUtil.disconnectSession()"+ex.getMessage());
			throw new InfrastructureException(ex);
		}
		return session;
	}

    /**
     * Register a Hibernate interceptor with the current thread.
     * <p>
     * Every Session opened is opened with this interceptor after
     * registration. Has no effect if the current Session of the
     * thread is already open, effective on next close()/getSession().
     */
    public static void registerInterceptor(Interceptor interceptor) {
        threadInterceptor.set(interceptor);
    }

    /**
     * Reloads an object from the DB
     * @param o
     */

    public static void refresh(Object o) throws InfrastructureException {
        Session session = getSession();
        session.refresh(o);
    }

    /**
     * Reloads an object from the DB
     * @param o
     * @param lockMode
     * @throws InfrastructureException
     */
    public static void refresh(Object o, LockMode lockMode) throws InfrastructureException {
        Session session = getSession();
        session.refresh(o, lockMode);
    }

	private static Interceptor getInterceptor() {
		Interceptor interceptor = (Interceptor)threadInterceptor.get();
		return interceptor;
	}
}

