package us.mn.state.health.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernatePersonDAO;
import us.mn.state.health.dao.ManufacturerDAO;
import us.mn.state.health.dao.PersonDAO;
import us.mn.state.health.dao.VendorDAO;
import us.mn.state.health.dao.inventory.OrderFormulaDAO;
import us.mn.state.health.dao.inventory.StockItemDAO;
import us.mn.state.health.model.common.Facility;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.PartyResource;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Position;
import us.mn.state.health.persistence.HibernateUtil;

public class TestPerson extends TestCase {
    private PersonDAO personDAO;
    private StockItemDAO stockItemDAO;
    private OrderFormulaDAO orderFormulaDAO;
    private VendorDAO vendorDAO;
    private ManufacturerDAO manufacturerDAO;

    protected void setUp() {
        personDAO = new HibernatePersonDAO();
    }

    protected void tearDown() throws InfrastructureException {
        HibernateUtil.commitTransaction();  //is this necessary when you're only reading?
        HibernateUtil.closeSession();

        personDAO = null;
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        /*
        suite.addTest(new TestPerson("testFindPersonsByGroupId"));
        suite.addTest(new TestPerson("testGetPrimaryFacility"));
        suite.addTest(new TestPerson("testFindAllMDHEmployees"));
        suite.addTest(new TestPerson("testGetPrimaryMailingAddress"));
        */
        //suite.addTest(new TestPerson("testGetPrimaryFacility"));
        suite.addTest(new TestPerson("testGetPrimaryMailingAddress"));
        //suite.addTest(new TestPerson("testGetPositions"));

        return suite;
    }
    
    public void testGetPrimaryMailingAddress() throws InfrastructureException {
        Long personId = new Long("17113");
        Person person = personDAO.getPersonById(personId, false);
        Facility primaryFacility = person.getPrimaryFacility();
        MailingAddress primaryFacilityMailingAddr = null;
        String mailingAddr = "null";
        if(primaryFacility != null) {
            primaryFacilityMailingAddr = person.getPrimaryFacility().getBuildingMailingAddress(); 
        }
        if(primaryFacilityMailingAddr != null) {
            mailingAddr = primaryFacilityMailingAddr.getShortSummary(); 
        }
         
        System.out.println("Primary Mailing Address: \n" + mailingAddr);
    }
    
    public void testGetPositions() throws InfrastructureException {
        Long personId = new Long("17113");
        Person person = personDAO.getPersonById(personId, false);
        Collection positionLinks = person.getPersonPositionLinks();
        Iterator iter = positionLinks.iterator();
        StringBuffer buffer = new StringBuffer();
        buffer.append("Positions for personID " + personId.toString() + ": \n");
        int numPositions = 0;
        while(iter.hasNext()) {
            PartyResource partyResource = (PartyResource)iter.next();
            Position position = partyResource.getPosition();
            buffer.append(position.getPositionId().toString() + " " + position.getWorkingTitle());
            buffer.append("\n");
            numPositions++;
        }
        if(numPositions == 0) {
            buffer.append("no positions for personId " + personId.toString());
        }
        System.out.println(buffer.toString());
    }
    
    
    public void testFindAllMDHEmployees() throws InfrastructureException {
        Collection persons = personDAO.findAllMDHEmployees();
        assertFalse(persons.isEmpty());

        Iterator iter = persons.iterator();
        while (iter.hasNext()) {
            Person emp = (Person) iter.next();
            System.out.println(emp.getPersonId() + " " + emp.getEmployeeId()  + " " +  emp.getFirstAndLastName());
        }
    }
    
    public void testGetPrimaryFacility() throws InfrastructureException {
        Long personId = new Long("17113");
        Person person = personDAO.getPersonById(personId, false);
        Facility facility = person.getPrimaryFacility();
        System.out.println("Primary Facility:\n " + facility);
    }

    public void testFindPersonsByGroupId() throws InfrastructureException {
        Long groupId = new Long("62859");

        Collection groupMembers = personDAO.findPersonsByGroupId(groupId);
        assertFalse(groupMembers.isEmpty());

        Iterator iter = groupMembers.iterator();
        while (iter.hasNext()) {
            Person bm = (Person) iter.next();
            System.out.println(bm.toString());
        }
    }

    public void testFindPersonsByGroupCode() throws InfrastructureException {

        Collection groupMembers = personDAO.findPersonsByGroupCode(Group.STOCK_CONTROLLER_CODE);
        assertFalse(groupMembers.isEmpty());

        Iterator iter = groupMembers.iterator();
        Collection members = new ArrayList();
        while (iter.hasNext()) {
            Person bm = (Person) iter.next();
            members.add(bm);
            System.out.println("=============================================================");
            System.out.println(bm.getFirstAndLastName());
        }


    }

    public void testFindUsersByGroupCode() throws InfrastructureException {
        Collection users = personDAO.findPersonsByGroupCode(Group.BUYER_CODE);
        assertNotNull(users);
    }

    public TestPerson(String x) {
        super(x);
    }

    public static void main(String[] args) throws Exception {
        TestRunner.run(suite());
        //junit.swingui.TestRunner.run(us.mn.state.health.test.TestPerson.class);
    }

}
