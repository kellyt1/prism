package us.mn.state.health.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateManufacturerDAO;
import us.mn.state.health.dao.HibernateStatusDAO;
import us.mn.state.health.dao.HibernateVendorDAO;
import us.mn.state.health.dao.ManufacturerDAO;
import us.mn.state.health.dao.StatusDAO;
import us.mn.state.health.dao.VendorDAO;
import us.mn.state.health.dao.inventory.HibernateItemDAO;
import us.mn.state.health.dao.inventory.HibernateOrderFormulaDAO;
import us.mn.state.health.dao.inventory.HibernateStockItemDAO;
import us.mn.state.health.dao.inventory.ItemDAO;
import us.mn.state.health.dao.inventory.OrderFormulaDAO;
import us.mn.state.health.dao.inventory.StockItemDAO;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Manufacturer;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.OrderFormula;
import us.mn.state.health.model.inventory.PurchaseItem;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.persistence.HibernateUtil;

public class TestItem extends TestCase {

    private ItemDAO itemDAO;
    private StockItemDAO stockItemDAO;
    private OrderFormulaDAO orderFormulaDAO;
    private VendorDAO vendorDAO;
    private ManufacturerDAO manufacturerDAO;
    private String username = "flahas1";
    private Long[] itemIds = new Long[]{new Long(210676),new Long(210322)};
    private String environment;

    protected void setUp() throws Exception {
        super.setUp();
        environment = Constants.DEVDB;
//        environment = Constants.TEST;
//        environment = Constants.PROD;
        System.setProperty(Constants.ENV_KEY, environment);
        itemDAO = new HibernateItemDAO();
        stockItemDAO = new HibernateStockItemDAO();
        orderFormulaDAO = new HibernateOrderFormulaDAO();
        vendorDAO = new HibernateVendorDAO();
        manufacturerDAO = new HibernateManufacturerDAO();
    }

    protected void tearDown() throws Exception {
        HibernateUtil.commitTransaction();  //is this necessary when you're only reading?
        HibernateUtil.closeSession();

        itemDAO = null;
        stockItemDAO = null;
        orderFormulaDAO = null;
        vendorDAO = null;
        manufacturerDAO = null;
        super.tearDown();
    }

    public void testFindStockItemByCategoryCodeAndItemCode() throws InfrastructureException {
        String categoryCode = "141";
        String icnbr = "0135";
        StockItem stockItem= stockItemDAO.findStockItemByCategoryCodeAndItemCode(categoryCode, icnbr);
        System.out.println(stockItem);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        /*
        suite.addTest(new TestItem("testAssignICNBR"));
        suite.addTest(new TestItem("testFindByStatusCode"));  
        suite.addTest(new TestItem("testFindByCategoryCode")); 
        suite.addTest(new TestItem("testCalculateSuggestedEOQ"));
        suite.addTest(new TestItem("testCalculateSuggestedROP")); 
        suite.addTest(new TestItem("testCalculateSuggestedROQ"));
        suite.addTest(new TestItem("testFindAll"));
        suite.addTest(new TestItem("testSetVendorAndManufacturer"));
        */
        suite.addTest(new TestItem("testGetItemInfo"));

        return suite;
    }

    public void testGetItemInfo() throws Exception {
        Item item = itemDAO.getItemById(new Long(63222), false);
        System.out.println("Manufacturer: " + item.getManufacturer().getExternalOrgDetail().getOrgName());
        System.out.println("Model: " + item.getModel());

    }

    public void testGetCurrentDemand() throws Exception {
        Item item = itemDAO.getItemById(new Long(63259), false);
        System.out.println("Current Demand: " + item.getCurrentDemand());
        assertEquals(item.getCurrentDemand().intValue(), 70);
    }

    public void testAssignICNBR() throws Exception {
        Integer nextICNBR = stockItemDAO.findNextICNBR();
        System.out.println("Next ICNBR: " + nextICNBR.intValue());
        assertTrue(nextICNBR.intValue() > 0);
    }

    public void testCopyProperties() throws Exception {
        StockItem original = (StockItem)stockItemDAO.getStockItemById(new Long(47761), false);
        StockItem copy = new StockItem();
        copy.copyProperties(original);
        StatusDAO statusDAO = new HibernateStatusDAO();
        Status onHoldStatus = (Status) statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM,
                Status.ONHOLD);
        System.out.println("original's status before changing copy's status: " + original.getStatus().getName());
        System.out.println("copy's status before changing copy's status: " + copy.getStatus().getName());
        copy.setStatus(onHoldStatus);
        System.out.println("original's status after changing copy's status: " + original.getStatus().getName());
        System.out.println("copy's status after changing copy's status: " + copy.getStatus().getName());
        stockItemDAO.makePersistent(copy);
        System.out.println("original's status after committing: " + original.getStatus().getName());
        System.out.println("copy's status after committing: " + copy.getStatus().getName());
    }

    public void testFindByStatusCode() throws Exception {
        Collection stockItems = stockItemDAO.findByStatusCode(Status.ACTIVE);
        Iterator iter = stockItems.iterator();

        while (iter.hasNext()) {
            StockItem si = (StockItem)iter.next();
            assertEquals(Status.ACTIVE, si.getStatus().getStatusCode());
            assertFalse(si.getPotentialIndicator().booleanValue());
        }
    }

    public void testFindByCategoryCode() throws Exception {
        Collection items = itemDAO.findByCategoryCode(Category.MATERIALS_OFFICESUPPLIES);
        Iterator iter = items.iterator();

        while (iter.hasNext()) {
            Item si = (Item)iter.next();
            assertEquals(Category.MATERIALS_OFFICESUPPLIES, si.getCategory().getCategoryCode());
        }
    }

    public void testCalculateSuggestedROP() throws Exception {
        String category = Category.MATERIALS_PRINTEDMATERIALS;

        OrderFormula orderFormula = orderFormulaDAO.findByCategoryCode(category);
        Collection items = stockItemDAO.findByCategoryCode(category);

        int leadTimeDays = orderFormula.getLeadTimeDays().intValue();

        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            StockItem item = (StockItem)iter.next();
            assertEquals(category, item.getCategory().getCategoryCode());

            int usage = item.getAnnualUsage().intValue();
            Integer result = new Integer(leadTimeDays * usage);
            System.out.println("SuggestedROP for " + item.getDescription() + ": " + result);

            assertNotNull(result);
            assertEquals(result, item.calculateSuggestedROP());
            assertFalse(result.intValue() < 0);
            assertFalse(result.intValue() == usage);
        }
    }

    public void testCalculateSuggestedROQ() throws Exception {
        Collection stockItems = stockItemDAO.findAll();
        Iterator iter = stockItems.iterator();

        assertTrue(iter.hasNext());

        while (iter.hasNext()) {
            StockItem stockItem = (StockItem)iter.next();
            Integer suggestedROQ = stockItem.calculateSuggestedROQ();
            if (stockItem.calculateSuggestedEOQ().intValue() < stockItem.getAnnualUsage().intValue()) {
                assertEquals(suggestedROQ, stockItem.calculateSuggestedROQ());
            } else {
                assertEquals(stockItem.getAnnualUsage(), stockItem.calculateSuggestedROQ());
            }
        }
    }

    public void testCalculateSuggestedEOQ() throws Exception {
        String category = Category.MATERIALS_PRINTEDMATERIALS;

        OrderFormula orderFormula = orderFormulaDAO.findByCategoryCode(category);
        Collection items = stockItemDAO.findByCategoryCode(category);
        Iterator iter = items.iterator();

        double orderCost = orderFormula.getOrderCost().doubleValue();

        while (iter.hasNext()) {
            PurchaseItem item = (PurchaseItem)iter.next();
            assertEquals(category, item.getCategory().getCategoryCode());

            int usage = item.getAnnualUsage().intValue();

            Long calculation;
            Integer intCalculation = new Integer(0);

            if (item.getDispenseUnitCost() == null || item.getDispenseUnitCost().doubleValue() < 0.001) {
                if (item.getAnnualUsage() != null) {
                    intCalculation = item.getAnnualUsage();
                }
                assertEquals("item id: " + item.getItemId(), item.calculateSuggestedEOQ(), intCalculation);
            } else {
                calculation = new Long(Math.round(Math.sqrt(((24 * orderCost) * (usage / 12)) / (0.28 * item.getDispenseUnitCost().doubleValue()))));
                intCalculation = new Integer(calculation.intValue());

                assertNotNull(item.getDispenseUnitCost());
                assertFalse(item.getDispenseUnitCost().doubleValue() == 0);
                assertTrue(item.calculateSuggestedEOQ().intValue() >= 0);
                System.out.println(item.getDescription() + " suggestedEOQ = " + intCalculation.intValue());
                assertEquals("item id: " + item.getItemId(), item.calculateSuggestedEOQ(), intCalculation);
            }
        }
    }

    public void testFindAll() throws Exception {
        Collection items = itemDAO.findAll();
        Iterator iter = items.iterator();
        assertTrue(iter.hasNext());
        while (iter.hasNext()) {
            PurchaseItem item = (PurchaseItem)iter.next();
            assertNotNull(item.getManufacturer());
            assertNotNull(item.getManufacturer().getExternalOrgDetail().getOrgName());

        }
    }

    public void testStockItemDAOFindAll() throws Exception {
        Collection items = stockItemDAO.findAll();
        Iterator iter = items.iterator();
        assertTrue(iter.hasNext());
        while (iter.hasNext()) {
            StockItem item = (StockItem)iter.next();
            assertNotNull(item.getManufacturer());
            assertNotNull(item.getManufacturer().getExternalOrgDetail().getOrgName());
        }
    }

    public void testSetVendorAndManufacturer() throws Exception {
        Vendor vendor = vendorDAO.getVendorById(new Long(47599), false);
        Manufacturer manufacturer = manufacturerDAO.getManufacturerById(new Long(47599), false);

        Item item = itemDAO.getItemById(new Long(47852), false);
        item.addVendor(vendor, username );
        item.setManufacturer(manufacturer);
    }

    public void testLoadItems() throws InfrastructureException {
        for (int i = 0; i < itemIds.length; i++) {
            Long itemId = itemIds[i];
            System.out.println(itemId);
            Item itemById = itemDAO.getItemById(itemId, false);
            System.out.println(itemById.getItemId());
        }
    }

    public TestItem(String x) {
        super(x);
    }

//    public void testBlob() throws InfrastructureException, IOException {
//        StockItem stockItemById = stockItemDAO.getStockItemById(new Long(279701), false);
//        byte[] printSpecFileBinaryValue = stockItemById.getPrintSpecFileBinaryValue();
//        System.out.println("Size = " + printSpecFileBinaryValue.length);
//        assertNotNull(stockItemById);
//
//        File file  = new File("C:\\" + stockItemById.getPrintSpecFileName());
//        OutputStream out = new FileOutputStream(file);
//        out.write(printSpecFileBinaryValue);
//        out.close();
//    }

    public static void main(String[] args) throws Exception {
        TestRunner.run(suite());
        //junit.swingui.TestRunner.run(us.mn.state.health.test.TestItem.class);
    }
}
