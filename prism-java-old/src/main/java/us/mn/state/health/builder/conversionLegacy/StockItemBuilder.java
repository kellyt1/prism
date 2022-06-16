package us.mn.state.health.builder.conversionLegacy;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.CategoryDAO;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.dao.PersonDAO;
import us.mn.state.health.dao.StatusDAO;
import us.mn.state.health.dao.VendorDAO;
import us.mn.state.health.dao.inventory.UnitDAO;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Manufacturer;
import us.mn.state.health.model.common.ObjectCode;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.common.VendorContract;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.StockItemFacility;
import us.mn.state.health.model.inventory.StockItemLocation;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.model.legacySystem.inventory.Inventory;
import us.mn.state.health.model.legacySystem.inventory.Purchase;
import us.mn.state.health.model.legacySystem.inventory.StkItemLocation;
import us.mn.state.health.model.legacySystem.inventory.StockInv;
import us.mn.state.health.model.legacySystem.inventory.Vendr;

public class StockItemBuilder {
    private static Log log = LogFactory.getLog(StockItemBuilder.class);
    private Purchase legacyStockItem;
    private StockItem stockItem;
    private StockInv stockInv;
    private DAOFactory daoFactory;
    private String user;
    public static Map missingCategories = new TreeMap();//Added this to see what categories are missing

    public StockItemBuilder(Purchase legacyStockItem,
                            StockItem stockItem,
                            StockInv stockInv,
                            DAOFactory daoFactory,
                            String user) {
        this.legacyStockItem = legacyStockItem;
        this.stockItem = stockItem;
        this.stockInv = stockInv;
        this.daoFactory = daoFactory;
        this.user = user;
    }

    /**
     * 1
     */
    public void buildCategory() throws InfrastructureException {
        CategoryDAO categoryDAO = daoFactory.getCategoryDAO();
        String categoryCode;
        categoryCode = legacyStockItem.getIcnbr().substring(0, 3);
        Category category = categoryDAO.findByCategoryCode(categoryCode);
        if (category == null) {
            category = categoryDAO.findByCategoryCode("140");
        }
        stockItem.setCategory(category);
    }

    /**
     * 2
     * Brad said that we have the manufacturer only for fixed assets
     */
    public void buildManufacturer() throws InfrastructureException {
        Manufacturer manufacturer = daoFactory.getManufacturerDAO().findManufacturerByCode(Manufacturer.CODE_UNKNOWN);
        stockItem.setManufacturer(manufacturer);
    }

    /**
     * 3
     */
    public void buildDescription() {
        String description = legacyStockItem.getItemDescription();
        stockItem.setDescription(description);
    }

    /**
     * 4
     */
    public void buildHazardous() {
        String hazardous = legacyStockItem.getHazardous();
        Boolean hzd = new Boolean(false);
        if (hazardous.toUpperCase().equals(Inventory.YES)) {
            hzd = new Boolean(true);
        }
        stockItem.setHazardous(hzd);
    }

    /**
     * 5
     */
    public void buildEOQ() {
        String legacyEOQ = legacyStockItem.getEoq();
        if (legacyEOQ == null) {
            legacyEOQ = "0";
        }
        Integer eoq = new Integer(legacyEOQ);
        stockItem.setEconomicOrderQty(eoq);
    }

    /**
     * 6
     * This method build the itemVendors links and the vendorContracts
     */
    public void buildItemVendors() throws InfrastructureException {
        String vendorId1 = legacyStockItem.getVendorId1();
        String vendorId2 = legacyStockItem.getVendorId2();
        String vendorId3 = legacyStockItem.getVendorId3();
        Vendor vendor1 = null;
        Vendor vendor2 = null;
        Vendor vendor3 = null;
        VendorDAO vendorDAO = daoFactory.getVendorDAO();
        //get the vendors
        vendor1 = vendorDAO.getVendorByLegacyId(vendorId1);
        if (vendor1 == null) {
            vendor1 = vendorDAO.getVendorByLegacyId(Vendr.DEFAULT_VENDR_ID);
        }
        vendor2 = vendorDAO.getVendorByLegacyId(vendorId2);
        vendor3 = vendorDAO.getVendorByLegacyId(vendorId3);

        //build the vendorContract for the 1st vendor ONLY
        VendorContract vendorContract1 = null;
        if (legacyStockItem.getCont() != null) {
            vendorContract1 = new VendorContract();
            vendorContract1.setContractNumber(legacyStockItem.getCont());
            Date expDate = null;
            String legacyExpDate = legacyStockItem.getExpDate();
            if (legacyExpDate != null) {
                try {
                    expDate = DateUtils.createDate(legacyExpDate, "MM/dd/yy");
                } catch (ParseException e) {
                }
            }
            if (expDate != null) {
                vendorContract1.setEndDate(expDate);
            }
            vendorContract1.setDeliveryTerms(legacyStockItem.getDelivery());
            vendorContract1.setVendor(vendor1);
            new HibernateDAO().makePersistent(vendorContract1);
        }

        //create the itemVendor
        ItemVendor itemVendor1 = ItemVendor.createItemVendor(vendor1, stockItem, vendorContract1, user);
        Unit buyUnit = daoFactory.getUnitDAO().findUnitByCode(stockInv.getUnit().toUpperCase());
        if (buyUnit == null) {
            buyUnit = daoFactory.getUnitDAO().findUnitByCode(Unit.CODE_UNKNOWN);
        }
        itemVendor1.setBuyUnit(buyUnit);
        ItemVendor itemVendor2;
        itemVendor2 = null;
        ItemVendor itemVendor3;
        itemVendor3 = null;

        if (vendor2 != null && !(vendor2.equals(vendor1))) {
            itemVendor2 = ItemVendor.createItemVendor(vendor2, stockItem, null, user);
        }

        if (vendor3 != null && !(vendor3.equals(vendor1)) && !(vendor3.equals(vendor2))) {
            itemVendor3 = ItemVendor.createItemVendor(vendor3, stockItem, null, user);
        }
    }

    /**
     * 7
     */
    public void buildDispenseUnit() throws InfrastructureException {
        UnitDAO unitDAO = daoFactory.getUnitDAO();
        Unit unit = unitDAO.findUnitByCode(legacyStockItem.getDispenseUnit());
        if (unit == null) {
            unit = unitDAO.findUnitByCode(Unit.CODE_UNKNOWN);
        }
        stockItem.setDispenseUnit(unit);
    }

    /**
     * 8
     */
    public void buildEstimatedAnnualUsage() {

    }

    /**
     * 9
     */
    public void buildAnnualUsage() {
        String annualUsage = legacyStockItem.getUsage();
        if (annualUsage != null) {
            stockItem.setAnnualUsage(new Integer(annualUsage));
        } else {
            stockItem.setAnnualUsage(new Integer(0));
        }
    }

    /**
     * 10
     */
    public void buildLastUpdateDate() {
        stockItem.setLastUpdatedDate(new Date());
    }

    /**
     * 11
     */
    public void buildLastUpdatedBy() {
        stockItem.setLastUpdatedBy(user);

    }

    /**
     * 12
     */
    public void buildTerminationDate() {

    }

    /**
     * 13
     */
    public void buildterminatedBy() {

    }

    /**
     * 14
     */
    public void buildInsertionDate() {
        if (legacyStockItem.getCreated() != null) {
            stockItem.setInsertionDate(StringUtils.formatDateFromString(legacyStockItem.getCreated()));
        }
    }

    /**
     * 15
     */
    public void buildCurrentDemand() {

    }

    /**
     *
     */
    public void buildDispenseUnitCost() {
        stockItem.setDispenseUnitCost(new Double(legacyStockItem.getCost()));
    }

    /**
     * 16
     */
    public void buildICNBR() {
        String icnbr = legacyStockItem.getIcnbr().substring(4);
        stockItem.setIcnbr(new Integer(icnbr));
    }

    /**
     * 17
     */
    public void buildQtyOnHand() {
        stockItem.setQtyOnHand(new Integer(legacyStockItem.getOnhand()));
    }

    /**
     * 18
     */
    public void buildLocations() throws InfrastructureException {

        //TODO make this code to work for all the facilities
        StockItemFacility stockItemFacility = null;
//        stockItemFacility = daoFactory.getStockItemFacilityDAO().findByFacilityCode("SOPSR");
        StockItemLocation location;
        Set locations = stockItem.getLocations();

        HibernateDAO hibernateDAO = new HibernateDAO();
        Collection legacyStkItemLocations =
                hibernateDAO
                        .executeQuery("from StkItemLocation s where s.stkItemLocationId.icnbr like \'"
                                + legacyStockItem.getIcnbr().trim() + "\'");
        for (Iterator iterator = legacyStkItemLocations.iterator(); iterator.hasNext();) {
            StkItemLocation stkItemLocation = (StkItemLocation) iterator.next();
            String locCode = stkItemLocation.getStockItemLocationId().getLocationCode();
            location = new StockItemLocation();
            String fac;
            if (locCode.equalsIgnoreCase("LAB")) {
                fac = "LAB";
            } else {
                fac = "SOPSR";
            }
            stockItemFacility = daoFactory.getStockItemFacilityDAO().findByFacilityCode(fac);
            location.setFacility(stockItemFacility);
            location.setLocationCode(locCode);
            location.setStockItem(stockItem);
            location.setIsPrimary(new Boolean(true));
            locations.add(location);
        }
        stockItem.setLocations(locations);
    }

    /**
     * 19
     */
    public void buildROP_ROQ() {
        stockItem.setReorderPoint(new Integer(legacyStockItem.getRop()));
        stockItem.setReorderQty(new Integer(legacyStockItem.getRoq()));
    }

    /**
     * 20
     */
    public void buildCycleCountPriority() {
        String priority = legacyStockItem.getP();
        if (StringUtils.nullOrBlank(priority)) {
            priority = "C";
        }
        stockItem.setCycleCountPriority(priority);
    }

    /**
     * 21
     */
    public void buildFillUntilDepleted() {
        //         This should be done in buildStatus()
    }

    /**
     * 22
     */
    public void buildReorderDate() {

    }

    /**
     * 23
     */
    public void buildHoldUntilDate() {
//         This should be done in buildStatus()
    }

    /**
     * 24
     */

    public void buildPackQty() {
        Integer packQty = stockInv.getPackQty();
        if (packQty != null) {
            stockItem.setPackQty(packQty);
        }
    }

    /**
     * 25
     */
    public void buildStaggedDelivery() {
        Boolean flag = new Boolean(false);
        if (legacyStockItem.getStag().toUpperCase().equals(Inventory.YES)) {
            flag = new Boolean(true);
        }
        stockItem.setStaggeredDelivery(flag);
    }

    /**
     * 26
     */
    public void buildSeasonal() {
        Boolean seasonal;
        seasonal = stockInv.getSeasonal();
        if (seasonal != null) {
            stockItem.setSeasonal(seasonal);
        } else {
            stockItem.setSeasonal(new Boolean(false));
        }
    }

    /**
     * 28
     * We convert only stock items with the status Active or On-Hold
     * Here we have to set the 'hold until date' and 'fill until depleted'
     */
    public void buildStatus() throws InfrastructureException {
        StatusDAO statusDAO = daoFactory.getStatusDAO();
        Status status = null;

        String onHold = legacyStockItem.getHold();
        Boolean onh = new Boolean(true);
        Boolean fillUntilDepleted = null;
        Date holdUntilDate = null;
        //the legacy ROPDate is the hold until date
        Date legacyDate = legacyStockItem.getRopDate();
        if (onHold.equals(Inventory.NO)) {
            onh = new Boolean(false);
        }

        if (!onh.booleanValue()) {
            status = statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM, Status.ACTIVE);
        } else {
            if (legacyDate == null) {
                status = statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM, Status.ONHOLD);
                fillUntilDepleted = new Boolean(true);
            } else {
                // the value 0 if the legacy Date is equal to current Date;
                // a value less than 0 if legacy Date is before the current Date;
                // and a value greater than 0 if legacy Date is after the current Date.
                int compareLegacyDateWithCurrentDate = legacyDate.compareTo(new Date());
                if (compareLegacyDateWithCurrentDate <= 0) {
                    //legacy date is expired and we make the SI active
                    status = statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM, Status.ACTIVE);
                } else {
                    status = statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM, Status.ONHOLD);
                    fillUntilDepleted = new Boolean(true);
                    holdUntilDate = legacyDate;
                }
            }
        }
        stockItem.setStatus(status);
        stockItem.setFillUntilDepleted(fillUntilDepleted);
        stockItem.setHoldUntilDate(holdUntilDate);
    }

    /**
     * 29
     * this is the method that sets the primary and secondary contact
     */
    public void buildContacts() throws InfrastructureException {
        PersonDAO personDAO = daoFactory.getPersonDAO();
        Long primaryContactId = stockInv.getPrimaryContactId();
        Long secondaryContactId = stockInv.getSecondaryContactId();
        stockItem.setPrimaryContact(personDAO.getPersonById(primaryContactId, true));
        stockItem.setSecondaryContact(personDAO.getPersonById(secondaryContactId, true));
    }

    /**
     * 30
     */

    public void buildOrgBudget() throws InfrastructureException {
        OrgBudget orgBudget = null;

        String orgBudgetCode = legacyStockItem.getSpace();
        orgBudget = daoFactory.getOrgBudgetDAO()
                .findByOrgBudgetCodeAndLastFiscalYear(orgBudgetCode.substring(0, 4));
        if (orgBudget.getEndDate().before(new Date())) {
            log.error("!!!!!!!!!!!!!!!!!!!!" + orgBudget.getOrgBudgetCode());

        }
        if (orgBudgetCode.length() > 4) {
            log.error(orgBudgetCode);
            String instructions = stockItem.getInstructions();
            instructions = instructions + "; OrgBudgets:" + orgBudgetCode;
            stockItem.setInstructions(instructions);
        }
        stockItem.setOrgBudget(orgBudget);
    }

    /**
     * 33
     */

    public void buildPotentialIndicator() {
        Boolean pi = new Boolean(false);
        stockItem.setPotentialIndicator(pi);
    }

    /**
     * 34
     */

    public void buildAssistanDivDirector() throws InfrastructureException {
        Long assistantDivisionDirectorId = stockInv.getaContactId();
        if (assistantDivisionDirectorId != null) {
            Person assistantDivisionDirector = daoFactory.getPersonDAO().getPersonById(assistantDivisionDirectorId, true);
            stockItem.setAsstDivDirector(assistantDivisionDirector);
        }
    }

    /**
     * 35
     */

    public void buildInstructions() {
        stockItem.setInstructions(legacyStockItem.getHistory());
    }

    /**
     * 36
     */

    public void buildObjectCode() throws InfrastructureException {
        String code = legacyStockItem.getCode();
        ObjectCode objectCode = daoFactory.getObjectCodeDAO().findByCode(code);
        if (objectCode == null) {
            objectCode = daoFactory.getObjectCodeDAO().findByCode(ObjectCode.UNKNOWN);
        }
        stockItem.setObjectCode(objectCode);
    }
}
