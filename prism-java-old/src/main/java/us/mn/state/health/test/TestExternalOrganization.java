package us.mn.state.health.test;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateManufacturerDAO;
import us.mn.state.health.dao.HibernateVendorDAO;
import us.mn.state.health.dao.ManufacturerDAO;
import us.mn.state.health.dao.VendorDAO;
import us.mn.state.health.dao.inventory.HibernateItemDAO;
import us.mn.state.health.dao.inventory.ItemDAO;
import us.mn.state.health.model.common.Manufacturer;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.persistence.HibernateUtil;

public class TestExternalOrganization extends TestCase {
    private VendorDAO vendorDAO;
    private ManufacturerDAO manufacturerDAO;
    private ItemDAO itemDAO;
    
    protected void setUp() {
		vendorDAO = new HibernateVendorDAO();
        itemDAO = new HibernateItemDAO();
        manufacturerDAO = new HibernateManufacturerDAO();
    }
    
    protected void tearDown()  throws InfrastructureException {
		vendorDAO = null;
        itemDAO = null;
        manufacturerDAO = null;
        
        HibernateUtil.commitTransaction();  
		HibernateUtil.closeSession();
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        //suite.addTest(new TestExternalOrganization("testManufacturerQuery"));
        suite.addTest(new TestExternalOrganization("testVendorFindAll"));
        
		return suite;
	}

	public TestExternalOrganization(String x) {
		super(x);
	}

	public static void main(String[] args) throws Exception {
		TestRunner.run( suite() );
	}
    
    
    public void testManufacturerQuery() throws Exception  {
        Manufacturer manufacturer = manufacturerDAO.getManufacturerById(new Long(47599), false);
        System.out.println(manufacturer.toString());
    }
    
    public void testVendorFindAll() throws Exception {
        Collection vendors = vendorDAO.findAll();
        Vendor vendor = vendorDAO.getVendorById(new Long(274155), false);
        this.assertNotNull(vendor);
        System.out.println(vendor.getExternalOrgDetail().getOrgName());
        this.assertTrue(vendors.contains(vendor));
    }
    
    public void testVendorQuery() throws Exception {
        char start = 'A';
        char end = 'F';
        Collection vendors = vendorDAO.findByNameFirstCharRange(start, end);
        this.assertNotNull(vendors);
        this.assertTrue(vendors.size() > 1);
        for(Iterator iter = vendors.iterator(); iter.hasNext(); ) {
            Vendor vendor =  (Vendor)iter.next();
            System.out.println(vendor.getExternalOrgDetail().getOrgName());
        }
        
        start = 'W';
        end = 'Z';
        vendors = vendorDAO.findByNameFirstCharRange(start, end);
        this.assertNotNull(vendors);
        this.assertTrue(vendors.size() > 1);
        for(Iterator iter = vendors.iterator(); iter.hasNext(); ) {
            Vendor vendor =  (Vendor)iter.next();
            System.out.println(vendor.getExternalOrgDetail().getOrgName());
        }
    }
}
