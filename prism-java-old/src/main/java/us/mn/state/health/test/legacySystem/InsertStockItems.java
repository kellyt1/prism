package us.mn.state.health.test.legacySystem;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import junit.framework.TestCase;
import us.mn.state.health.builder.conversionLegacy.StockItemBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.dao.VendorDAO;
import us.mn.state.health.dao.inventory.HibernateStockItemDAO;
import us.mn.state.health.dao.inventory.StockItemDAO;
import us.mn.state.health.dao.legacySystem.inventory.HibernateInventoryDAO;
import us.mn.state.health.dao.legacySystem.inventory.HibernateStockInvDAO;
import us.mn.state.health.matmgmt.director.conversionLegacy.StockItemDirector;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.model.legacySystem.inventory.Purchase;
import us.mn.state.health.model.legacySystem.inventory.StockInv;
import us.mn.state.health.model.legacySystem.inventory.Vendr;
import us.mn.state.health.persistence.HibernateUtil;

public class InsertStockItems extends TestCase {
    private DAOFactory daoFactory;
    private HibernateDAO hibernateDAO;
    private String user;
    private HibernateInventoryDAO inventoryDAO;
    private HibernateStockInvDAO stockInvDAO;
    private StockItemDAO stockItemDAO;
    private String icnbr;
    //first result starting from 0
    private int firstResult;
    private int maxResults;
    private String environment;

    protected void setUp() throws Exception {
        super.setUp();
        //here is where we select the environment
        environment = Constants.DEVDB;
//        environment = Constants.TEST;
//        environment = Constants.PROD;
        System.setProperty(Constants.ENV_KEY, environment);

        daoFactory = new HibernateDAOFactory();
        user = "ochial1";
        inventoryDAO = new HibernateInventoryDAO();
        stockInvDAO = new HibernateStockInvDAO();
        stockItemDAO = new HibernateStockItemDAO();
        hibernateDAO = new HibernateDAO();

//        icnbr ="141-0742";

        firstResult = 0;
        maxResults = 5000;
    }

    /**
     * This method sets the discount, buyUnitCost, primaryVendor,
     * 06/06/2006
     *
     * @throws InfrastructureException
     */

    public void fixItemVendorInfo() throws InfrastructureException {
	
        Collection legacyStockItems = inventoryDAO.findAll();
        int i = 0;
        for (Iterator iterator = legacyStockItems.iterator(); iterator.hasNext();) {
            Purchase purchase = (Purchase) iterator.next();
            String _icnbr = purchase.getIcnbr().trim();
            String[] s = _icnbr.split("-");
            System.out.println("icnbr=" + s[0] + "-" + s[1]);
            StockItem stockItem = daoFactory.getStockItemDAO().findStockItemByCategoryCodeAndItemCode(s[0], s[1]);

            String discount = purchase.getDiscount();
            String primaryVendorId = purchase.getVendorId1();
            String buyUnitcost = purchase.getRetCost();


            if (stockItem != null) {
                Collection itemVendors = stockItem.getItemVendors();
                for (Iterator iterator1 = itemVendors.iterator(); iterator1.hasNext();) {
                    ItemVendor itemVendor = (ItemVendor) iterator1.next();
                    boolean primaryVendorIdIsNotNull = (!StringUtils.nullOrBlank(primaryVendorId));
                    boolean thisIsThePrimaryVendor = itemVendor.getVendor().getLegacyId().indexOf(primaryVendorId.trim()) != -1;
                    if (primaryVendorIdIsNotNull
                            && thisIsThePrimaryVendor) {
                        itemVendor.setPrimaryVendor(Boolean.TRUE);
                        itemVendor.setVendorCatalogNbr(purchase.getItemNbr());
                        try {
                            if (!StringUtils.nullOrBlank(discount)) {
                                Double disc = Double.valueOf(discount);
                                itemVendor.setDiscount(disc);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        try {
                            if (!StringUtils.nullOrBlank(buyUnitcost)) {
                                Double cost = Double.valueOf(buyUnitcost);
                                itemVendor.setBuyUnitCost(cost);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        String vendorId2 = purchase.getVendorId2();
                        String vendorId3 = purchase.getVendorId3();
                        boolean thisIsTheSecondaryVendor = ((!StringUtils.nullOrBlank(vendorId2))&&(itemVendor.getVendor().getLegacyId().indexOf(vendorId2.trim()) != -1));
                        boolean thisIsTheThirdVendor = ((!StringUtils.nullOrBlank(vendorId3))&&(itemVendor.getVendor().getLegacyId().indexOf(vendorId3.trim()) != -1));
                        if(thisIsTheSecondaryVendor){
                            itemVendor.setVendorCatalogNbr(purchase.getItemNbr2());
                        }

                        if(thisIsTheThirdVendor){
                            itemVendor.setVendorCatalogNbr(purchase.getItemNbr3());
                        }
                    }
                }
                hibernateDAO.makePersistent(stockItem);
                System.out.println("i=" + (++i));
            }
        }
    }

    public void fixItemVendorInfo1() throws InfrastructureException {
        Purchase purchase = inventoryDAO.getStockInvByICNBR("375-0025");
        String _icnbr = purchase.getIcnbr().trim();
        String[] s = _icnbr.split("-");
        System.out.println("icnbr=" + s[0] + "-" + s[1]);
        StockItem stockItem = daoFactory.getStockItemDAO().findStockItemByCategoryCodeAndItemCode(s[0], s[1]);

        String discount = purchase.getDiscount();
        String primaryVendorId = purchase.getVendorId1();
        String buyUnitcost = purchase.getRetCost();


        if (stockItem != null) {
            Collection itemVendors = stockItem.getItemVendors();
            for (Iterator iterator1 = itemVendors.iterator(); iterator1.hasNext();) {
                ItemVendor itemVendor = (ItemVendor) iterator1.next();
                boolean primaryVendorIdIsNotNull = (!StringUtils.nullOrBlank(primaryVendorId));
                boolean thisIsThePrimaryVendor = itemVendor.getVendor().getLegacyId().indexOf(primaryVendorId.trim()) != -1;
                if (primaryVendorIdIsNotNull
                        && thisIsThePrimaryVendor) {
                    itemVendor.setPrimaryVendor(Boolean.TRUE);
                    try {
                        Double disc = Double.valueOf(discount);
                        itemVendor.setDiscount(disc);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    try {
                        Double cost = Double.valueOf(buyUnitcost);
                        itemVendor.setBuyUnitCost(cost);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            hibernateDAO.makePersistent(stockItem);
        }
    }

    /**
     * this method does the conversion of one stock item
     *
     * @throws InfrastructureException
     */
    public void testInsertStockItem() throws InfrastructureException {
        StockItem stockItem = new StockItem();
        Purchase legacyStockItem = null;
        StockInv stockInv = null;
        legacyStockItem = inventoryDAO.getStockInvByICNBR(icnbr);
        buildStockItem(legacyStockItem, stockItem);
    }

    /**
     * this method does the conversion of all the stock items
     *
     * @throws InfrastructureException
     */
    public void testInsertStockItems() throws InfrastructureException {
        Collection legacyStockItems = inventoryDAO.findAll(firstResult, maxResults);
        int i = 0;
        for (Iterator iterator = legacyStockItems.iterator(); iterator.hasNext();) {
            ++i;
            Purchase purchase = (Purchase) iterator.next();
//            if (i >= firstResult && i <= maxResults) {
            StockItem stockItem = new StockItem();
            System.out.println("STOCK ITEM NO:" + (i + firstResult));
            System.out.println("ICNBR:" + purchase.getIcnbr());
            buildStockItem(purchase, stockItem);
            if (i % 25 == 0) {
                new HibernateDAOFactory().commitTransaction(true);
            }
//                continue;
//            }

//            if (i > maxResults) {
//                break;
//            }
        }
    }

    /**
     * Sets the dispense units for some SI that had the dispense unit code different from waht we have in UNIT_TBL
     *
     * @throws InfrastructureException
     */
    public void fixDispenseUnitForTransferredStockItems() throws InfrastructureException {
        Collection legacyStockItems = inventoryDAO.findAll(firstResult, maxResults);
        int i = 0;
        int j = 0;
        for (Iterator iterator = legacyStockItems.iterator(); iterator.hasNext();) {

            Purchase purchase = (Purchase) iterator.next();
            String unitCode = purchase.getDispenseUnit();
            String _icnbr = purchase.getIcnbr().trim();
            String[] s = _icnbr.split("-");
            System.out.println("icnbr=" + s[0] + "-" + s[1]);
            StockItem stockItem = daoFactory.getStockItemDAO().findStockItemByCategoryCodeAndItemCode(s[0], s[1]);
            if (stockItem != null && !stockItem.getDispenseUnit().getCode().equals(unitCode)) {
                Unit unit = daoFactory.getUnitDAO().findUnitByCode(unitCode);
                stockItem.setDispenseUnit(unit);
                daoFactory.getStockItemDAO().makePersistent(stockItem);
                ++i;
            }
            System.out.println("j=" + (++j));
        }
        System.out.println("i=" + i);
    }

    /**
     * fixBuyUnitForSIPrimaryVendors
     * run this first
     *
     * @throws InfrastructureException
     */
    public void fixBuyUnitForSIPrimaryVendors() throws InfrastructureException {
        Collection legacyStockItems = inventoryDAO.findAll(firstResult, maxResults);
        int i = 0;
        int j = 0;
        for (Iterator iterator = legacyStockItems.iterator(); iterator.hasNext();) {

            Purchase purchase = (Purchase) iterator.next();
            String unitCode = purchase.getBuyUnit();
            String _icnbr = purchase.getIcnbr().trim();
            String[] s = _icnbr.split("-");
            System.out.println("icnbr=" + s[0] + "-" + s[1]);
            StockItem stockItem = daoFactory.getStockItemDAO().findStockItemByCategoryCodeAndItemCode(s[0], s[1]);

            if (stockItem != null) {
                ItemVendor primaryItemVendor = (ItemVendor) stockItem.getItemVendors().iterator().next();
                Unit oldBuyUnit = primaryItemVendor.getBuyUnit();
                if (!oldBuyUnit.getCode().equals(unitCode)) {
                    System.out.println("old_bu=" + oldBuyUnit.getCode() + " new_bu=" + unitCode);
                }
                if (!oldBuyUnit.getCode().equals(unitCode)) {
                    Unit unit = daoFactory.getUnitDAO().findUnitByCode(unitCode);
                    primaryItemVendor.setBuyUnit(unit);
                    daoFactory.getStockItemDAO().makePersistent(stockItem);
                    ++i;
                }
            }
            if (j % 25 == 0) {
                new HibernateDAOFactory().commitTransaction(true);
            }

            System.out.println("j=" + (++j));
        }
        System.out.println("i=" + i);
    }

    /**
     * Run this second
     * addVendorCatalogNumbersForPrimaryVendors
     *
     * @throws InfrastructureException
     */
    public void addVendorCatalogNumbersForPrimaryVendors() throws InfrastructureException {
        Collection legacyStockItems = inventoryDAO.findAll(firstResult, maxResults);
        int i = 0;
        int j = 0;
        for (Iterator iterator = legacyStockItems.iterator(); iterator.hasNext();) {

            Purchase purchase = (Purchase) iterator.next();
            String _icnbr = purchase.getIcnbr().trim();
            String vendorCatalogNumForPrimaryVendor = purchase.getItemNbr();
            String[] s = _icnbr.split("-");
            System.out.println("icnbr=" + s[0] + "-" + s[1]);
            StockItem stockItem = daoFactory.getStockItemDAO().findStockItemByCategoryCodeAndItemCode(s[0], s[1]);

            if (stockItem != null) {
                ItemVendor primaryItemVendor = (ItemVendor) stockItem.getItemVendors().iterator().next();
                primaryItemVendor.setVendorCatalogNbr(vendorCatalogNumForPrimaryVendor);
                daoFactory.getStockItemDAO().makePersistent(stockItem);
                ++i;
            }
            if (j % 25 == 0) {
                new HibernateDAOFactory().commitTransaction(true);
            }

            System.out.println("j=" + (++j));
        }
        System.out.println("i=" + i);
    }

    /**
     * Run this third
     * Adds the itemVendor2,3
     */
    public void fixItemVendors() throws InfrastructureException {
        Collection legacyStockItems = inventoryDAO.findAll(firstResult, maxResults);
        int j = 0;

        VendorDAO vendorDAO = daoFactory.getVendorDAO();
        for (Iterator iterator = legacyStockItems.iterator(); iterator.hasNext();) {
            Purchase purchase = (Purchase) iterator.next();
            String _icnbr = purchase.getIcnbr().trim();
            //get the vendors
            Vendor vendor1 = null;
            Vendor vendor2 = null;
            Vendor vendor3 = null;
            String vendorId1 = purchase.getVendorId1();
            String vendorId2 = purchase.getVendorId2();
            String vendorId3 = purchase.getVendorId3();

            vendor1 = vendorDAO.getVendorByLegacyId(vendorId1);
            if (vendor1 == null) {
                vendor1 = vendorDAO.getVendorByLegacyId(Vendr.DEFAULT_VENDR_ID);
            }
            vendor2 = vendorDAO.getVendorByLegacyId(vendorId2);
            vendor3 = vendorDAO.getVendorByLegacyId(vendorId3);

            //get the stock item
            String[] s = _icnbr.split("-");
            System.out.println("icnbr=" + s[0] + "-" + s[1]);
            StockItem stockItem = daoFactory.getStockItemDAO().findStockItemByCategoryCodeAndItemCode(s[0], s[1]);
            if (stockItem == null) {
                continue;
            }

            Unit buyUnit = daoFactory.getUnitDAO().findUnitByCode(purchase.getBuyUnit().toUpperCase());
            if (buyUnit == null) {
                buyUnit = daoFactory.getUnitDAO().findUnitByCode(Unit.CODE_UNKNOWN);
            }
            ItemVendor itemVendor2 = null;
            ItemVendor itemVendor3 = null;

            if (vendor2 != null && !(vendorId2.equals(vendorId1))) {
                itemVendor2 = ItemVendor.createItemVendor(vendor2, stockItem, null, user);
                itemVendor2.setBuyUnit(buyUnit);
                itemVendor2.setVendorCatalogNbr(purchase.getItemNbr2());
            }

            if (vendor3 != null && !(vendorId3.equals(vendorId1)) && !(vendorId3.equals(vendorId2))) {
                itemVendor3 = ItemVendor.createItemVendor(vendor3, stockItem, null, user);
                itemVendor3.setBuyUnit(buyUnit);
                itemVendor3.setVendorCatalogNbr(purchase.getItemNbr3());
            }
            daoFactory.getStockItemDAO().makePersistent(stockItem);
            if (j % 25 == 0) {
                new HibernateDAOFactory().commitTransaction(true);
            }

            System.out.println("j=" + (++j));
        }
    }

    private void buildStockItem(Purchase legacyStockItem, StockItem stockItem) throws InfrastructureException {
        StockInv stockInv;
        String in;
        in = legacyStockItem.getIcnbr();
        stockInv = stockInvDAO.getStockInvByICNBR(in);
        if (stockInv == null) {
            return;
        }
        StockItemBuilder builder = new StockItemBuilder(legacyStockItem, stockItem, stockInv, daoFactory, user);
        StockItemDirector director = new StockItemDirector(builder);
        director.construct();
        stockItemDAO.makePersistent(stockItem);
        director.constructItemVendorsContracts();
        stockItemDAO.makePersistent(stockItem);
        System.out.println("stop");
    }

    /**
     * Method that deletes all the Stock Items
     */
    public void deleteAllTheStockItemsItems() {
        int i = 0;
        HibernateDAO dao = new HibernateDAO();
        try {
            Collection vendrs = HibernateUtil.getSession().createQuery("from StockItem si").setMaxResults(1000).list();
            for (Iterator iterator = vendrs.iterator(); iterator.hasNext();) {
                i++;
                StockItem stockItem = (StockItem) iterator.next();
                dao.makeTransient(stockItem);
                System.out.println("i=" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void testCheckOrgBudgets() throws InfrastructureException {
        HibernateDAO dao = new HibernateDAO();
        String hql;
        hql = "select distinct i.space from Inventory i where length(i.space)=4 and i.space not like \'MISC\'";
        hql = "select distinct i.space from Inventory i";
        Collection results = dao.executeQuery(hql);

        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            s = s.substring(0, 4);

            OrgBudget orgBudget = daoFactory.getOrgBudgetDAO().findByOrgBudgetCodeAndLastFiscalYear(s);
            if (orgBudget == null || orgBudget.getEndDate().before(new Date())) {
                System.out.println(s);
            }
        }
        try {
            System.out.println("Finished!");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testFixOrgBudgets() throws InfrastructureException {
        HibernateDAO dao = new HibernateDAO();
        Collection stockItems = daoFactory.getStockItemDAO().findAll();
        int i = 0;

        StockItem stockItem;
        OrgBudget orgBudget;
        String orgBudgetCode = null;
        for (Iterator iterator = stockItems.iterator(); iterator.hasNext();) {

            stockItem = (StockItem) iterator.next();
            String fullIcnbr = stockItem.getFullIcnbr();
            if (!fullIcnbr.toLowerCase().equals(fullIcnbr.toUpperCase())) {
                System.out.println(fullIcnbr);
            }
            Collection collection = dao.
                    executeQuery("select i.space from Inventory i where i.icnbr=\'" + fullIcnbr.trim() + "\'");
            if(collection.size()>0)
            orgBudgetCode = (String) collection.iterator().next();
            orgBudget = daoFactory.getOrgBudgetDAO().findByOrgBudgetCodeAndLastFiscalYear(orgBudgetCode.substring(0, 4));
            if (orgBudget.getEndDate().before(new Date())) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!" + orgBudget.getOrgBudgetCode());
                continue;
            }
            if (orgBudgetCode.length() > 4) {
                System.out.println(orgBudgetCode);
                String instructions = stockItem.getInstructions();
                instructions = instructions + "; OrgBudgets:" + orgBudgetCode;
                stockItem.setInstructions(instructions);
            }
            stockItem.setOrgBudget(orgBudget);
            dao.makePersistent(stockItem);
            System.out.println("i#=" + i);
            i++;
        }

        System.out.println("i=" + i);

    }

    protected void tearDown() throws Exception {
//        HibernateUtil.commitTransaction();
//        HibernateUtil.closeSession();
        super.tearDown();
    }
}