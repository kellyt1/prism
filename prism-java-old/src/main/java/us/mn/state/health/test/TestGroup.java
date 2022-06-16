package us.mn.state.health.test;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.GroupDAO;
import us.mn.state.health.dao.HibernateGroupDAO;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.PersonGroupLink;
import us.mn.state.health.persistence.HibernateUtil;

public class TestGroup extends TestCase {
    private GroupDAO groupDAO;
    private String environment;

    protected void setUp() {
                environment = Constants.DEVDB;
//        environment = Constants.TEST;
//        environment = Constants.STAGE;
//        environment = Constants.PROD;
        System.setProperty(Constants.ENV_KEY, environment);
        groupDAO = new HibernateGroupDAO();
    }

    protected void tearDown() throws InfrastructureException {
        HibernateUtil.commitTransaction();  //is this necessary when you're only reading?
        HibernateUtil.closeSession();

        groupDAO = null;
    }

    public void testInsertNewGroup() throws InfrastructureException {
        Group group = new Group();
        group.setGroupCode(Group.STOCK_CONTROLLER_CHEMICAL_GASSES_EMAIL_NOTIFICATION_CODE);
        group.setGroupName("PRISM Stock Controller for Chemicals and Gasses Email Notification");
        group.setGroupShortName("StkCtrCGNf");
        group.setGroupPurpose("PRISM A group representing Stock controllers for Chemicals and Gasses that should receive reorder stock item email notifications");
        group.setGroupType("");
        group.setInsertedBy("ochial1");
        group.setInsertionDate(new Date());
//        groupDAO.makePersistent(group);
    }

    public void testReorderSIEmailGroup() throws InfrastructureException {
        Group stkController = DAOFactory.getDAOFactory(DAOFactory.DEFAULT)
                                            .getGroupDAO()
                                            .findGroupByCode(Group.STOCK_CONTROLLER_EMAIL_NOTIFICATION_CODE);
        StringBuffer to = new StringBuffer();
        Collection groupLinks = stkController.getPersonGroupLinks();
        int i = groupLinks.size();
        for(Iterator iter = groupLinks.iterator(); iter.hasNext(); ) {
                PersonGroupLink pgl = (PersonGroupLink)iter.next();
                Person person = pgl.getPerson();
                to.append(person.getNdsUserId()+"@health.state.mn.us,");
            }
        System.out.println(to.toString());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        /*
        suite.addTest(new TestGroup("testFindAll"));
        suite.addTest(new TestGroup("testFindGroupByName"));
        suite.addTest(new TestGroup("testFindGroupByCode"));
        */
        suite.addTest(new TestGroup("testFindGroupsByPersonId"));

        return suite;
    }

    public void testFindAll() throws Exception {
        Collection groups = groupDAO.findAll();
        assertFalse(groups.isEmpty());
    }

    public void testFindGroupByName() throws Exception {
        Group group = groupDAO.findGroupByName("Computer Purchase Evaluators");
        assertNotNull(group);
    }

    public void testFindGroupByCode() throws Exception {
        Group group = groupDAO.findGroupByCode("COMP_PRCH_EVAL");
        assertNotNull(group);
    }

    public void testFindGroupsByPersonId() throws Exception {
        Collection groups = groupDAO.findGroupsByPersonId(new Long("17113"));
        assertFalse(groups.isEmpty());
    }

    public TestGroup(String x) {
        super(x);
    }

    public static void main(String[] args) throws Exception {
        TestRunner.run(suite());
        //junit.swingui.TestRunner.run(us.mn.state.health.test.TestGroup.class);
    }
}
