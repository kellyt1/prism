package us.mn.state.health.test;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.FacilityDAO;
import us.mn.state.health.dao.HibernateFacilityDAO;
import us.mn.state.health.dao.inventory.HibernateStockItemFacilityDAO;
import us.mn.state.health.model.common.Facility;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.inventory.StockItemFacility;
import us.mn.state.health.persistence.HibernateUtil;

public class TestFacility extends TestCase {
    private FacilityDAO facilityDAO;
    private HibernateStockItemFacilityDAO stockItemFacilityDAO;

    protected void setUp() throws Exception  {
        facilityDAO = new HibernateFacilityDAO();
        stockItemFacilityDAO = new HibernateStockItemFacilityDAO();
    }

    protected void tearDown() throws Exception {
        HibernateUtil.commitTransaction();  //is this necessary when you're only reading?
        HibernateUtil.closeSession();

        facilityDAO = null;
        stockItemFacilityDAO = null;
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        /*
        suite.addTest(new TestFacility("testFindAll"));
        */
        suite.addTest(new TestFacility("testGetMailingAddress"));

        return suite;
    }

    public void testFindAll() throws Exception {
        Collection facilities = facilityDAO.findAllFacilities();
        assertFalse(facilities.isEmpty());
    }
    
    public void testGetMailingAddress() throws Exception {
        Collection facilities = facilityDAO.findFacilitiesByType(Facility.TYPE_SUITE_WING);
        assertFalse(facilities.isEmpty());
        Iterator iter = facilities.iterator();
        while(iter.hasNext()) {
            Facility facility = (Facility)iter.next();
            assertNotNull(facility);
            MailingAddress mailingAddr = facility.getMailingAddress();
            if(mailingAddr != null) {
                System.out.println("facility " + facility.getFacilityId() + "\n" + 
                                   facility.getFacilityName() + "\n" + 
                                   mailingAddr.getShortSummary() + "\n");
            }
            else {
                System.out.println("facility " + facility.getFacilityId() + " " +
                                   facility.getFacilityName() + " has no address\n");
            }
        }
    }

    public void findStockItemFacilities() throws InfrastructureException{
        StockItemFacility facility = stockItemFacilityDAO.findByFacilityCode("LAB");
        Collection stockItemFacilities =
                stockItemFacilityDAO.findAll();
        assertFalse(facility==null);
        assertFalse(stockItemFacilities.isEmpty());
        System.out.println(facility);
        System.out.println(stockItemFacilities);
    }

    public TestFacility(String x) {
        super(x);
    }

    public static void main(String[] args) throws Exception {
        TestRunner.run(suite());
        //junit.swingui.TestRunner.run(us.mn.state.health.test.TestFacility.class);
    }
}
