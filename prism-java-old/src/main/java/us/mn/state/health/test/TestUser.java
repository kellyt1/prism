package us.mn.state.health.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernatePersonDAO;
import us.mn.state.health.dao.HibernateUserDAO;
import us.mn.state.health.dao.PersonDAO;
import us.mn.state.health.dao.UserDAO;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.User;
import us.mn.state.health.persistence.HibernateUtil;

public class TestUser extends TestCase {
    private UserDAO userDAO;
    private PersonDAO personDAO;
    
    protected void setUp() {
		userDAO = new HibernateUserDAO();
        personDAO = new HibernatePersonDAO();
    }
    
    protected void tearDown()  throws InfrastructureException {
		userDAO = null;
        
        HibernateUtil.commitTransaction();  //is this necessary when you're only reading?
		HibernateUtil.closeSession();
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestUser("testGetPersonById"));
        suite.addTest(new TestUser("testFindUserByUsername"));
        suite.addTest(new TestUser("testAuthenticate"));
        
		return suite;
	}

	public TestUser(String x) {
		super(x);
	}

	public static void main(String[] args) throws Exception {
		TestRunner.run( suite() );
        //junit.swingui.TestRunner.run(us.mn.state.health.test.TestItem.class);
	}
    
    public void testAuthenticate() throws Exception {
        Boolean result = userDAO.authenticate("flahas1", "temp1234","AD");
        if(result.booleanValue()){
            System.out.println("Good username/password");
        }
        else {
            System.out.println("Bad username/password");
        }
    }
    
    public void testGetPersonById() throws Exception {
        Person user = userDAO.getPersonById(new Long(17113), false);
        assertNotNull(user);  
    }
    
    public void testFindUserByUsername() throws Exception {
        User user = userDAO.findUserByUsername("flahas1");   
        
        assertNotNull(user);
        
        System.out.println(user.toString());
    }

    public void testFindUserByUsername1() throws Exception {
        //enddate
        User user = userDAO.findUserByUsername("OKABUD1");

        assertNotNull(user);

        System.out.println(user.toString());
    }
}
