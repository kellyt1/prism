package us.mn.state.health.facade.inventory.editStockItem;

import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.upload.FormFile;
import org.hibernate.Hibernate;
import us.mn.state.health.domain.repository.common.CategoryRepository;
import us.mn.state.health.domain.repository.common.HazardousRepository;
import us.mn.state.health.domain.repository.common.ManufacturerRepository;
import us.mn.state.health.domain.repository.common.OrgBudgetRepository;
import us.mn.state.health.domain.repository.common.StatusRepository;
import us.mn.state.health.domain.repository.common.UnitRepository;
import us.mn.state.health.domain.repository.common.UserRepository;
import us.mn.state.health.domain.repository.common.VendorRepository;
import us.mn.state.health.domain.repository.inventory.CycleCountPriorityRepository;
import us.mn.state.health.domain.repository.inventory.ObjectCodeRepository;
import us.mn.state.health.domain.repository.inventory.StockItemFacilityRepository;
import us.mn.state.health.domain.repository.inventory.StockItemRepository;
import us.mn.state.health.domain.repository.inventory.StockQtyChangeReasonRefRepository;
import us.mn.state.health.domain.service.inventory.StockItemService;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Hazardous;
import us.mn.state.health.model.common.Manufacturer;
import us.mn.state.health.model.common.ObjectCode;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.inventory.AttachedFile;
import us.mn.state.health.model.inventory.CycleCountPriority;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.ItemVendorLinkHistory;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.StockItemFacility;
import us.mn.state.health.model.inventory.StockItemHistory;
import us.mn.state.health.model.inventory.StockItemLocationHistory;
import us.mn.state.health.model.inventory.StockQtyAdjustmentHistory;
import us.mn.state.health.model.inventory.StockQtyChangeReasonRef;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.util.NotificationException;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class EditStockItemFacadeImpl implements EditStockItemFacade {
    private StockItemRepository stockItemRepository;
    private UserRepository userRepository;
    private UnitRepository unitRepository;
    private OrgBudgetRepository orgBudgetRepository;
    private StockItemFacilityRepository stockItemFacilityRepository;
    private CategoryRepository categoryRepository;
    private ObjectCodeRepository objectCodeRepository;
    private VendorRepository vendorRepository;
    private StatusRepository statusRepository;
    private StockQtyChangeReasonRefRepository stockQtyChangeReasonRefRepository;
    private ManufacturerRepository manufacturerRepository;
    private CycleCountPriorityRepository cycleCountPriorityRepository;
    private HazardousRepository hazardousRepository;

    private StockItemService stockItemService;


    public EditStockItemFacadeImpl(StockItemRepository stockItemRepository,
                                   UserRepository userRepository,
                                   UnitRepository unitRepository,
                                   OrgBudgetRepository orgBudgetRepository,
                                   StockItemFacilityRepository stockItemFacilityRepository,
                                   CategoryRepository categoryRepository,
                                   ObjectCodeRepository objectCodeRepository,
                                   VendorRepository vendorRepository,
                                   StatusRepository statusRepository,
                                   StockQtyChangeReasonRefRepository stockQtyChangeReasonRefRepository,
                                   ManufacturerRepository manufacturerRepository,
                                   CycleCountPriorityRepository cycleCountPriorityRepository,
                                   HazardousRepository hazardousRepository,
                                   StockItemService stockItemService) {
        this.stockItemRepository = stockItemRepository;
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.orgBudgetRepository = orgBudgetRepository;
        this.stockItemFacilityRepository = stockItemFacilityRepository;
        this.categoryRepository = categoryRepository;
        this.objectCodeRepository = objectCodeRepository;
        this.vendorRepository = vendorRepository;
        this.statusRepository = statusRepository;
        this.stockQtyChangeReasonRefRepository = stockQtyChangeReasonRefRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.cycleCountPriorityRepository = cycleCountPriorityRepository;
        this.hazardousRepository = hazardousRepository;
        this.stockItemService = stockItemService;
    }

    public StockItem detachStockItem(Long stockItemId) {
        //get the item and initialize the associations
        StockItem item;
//        item = (StockItem) stockItemRepository.load(StockItem.class, stockItemId);
        item = (StockItem) stockItemRepository.get(StockItem.class, stockItemId);

        item.getFullIcnbr();
        for (Object o : item.getItemVendors()) {
            ((ItemVendor) o).getInsertionDate();
            ((ItemVendor) o).getVendor().getExternalOrgDetail().getOrgName();
        }
        Hibernate.initialize(item.getStatus());
        Hibernate.initialize(item.getOrgBudget());
        Hibernate.initialize(item.getObjectCode());
        Hibernate.initialize(item.getPrimaryContact());
        Hibernate.initialize(item.getSecondaryContact());
        Hibernate.initialize(item.getHazardousObject());
        Hibernate.initialize(item.getCycleCountPriorityObject());

        for (Object o : item.getAttachedFiles()) {
//            ((AttachedFile) o).getAttachedFileId();
//            ((AttachedFile) o).getFileName();
            Hibernate.initialize((AttachedFile) o);
        }

        return item;
    }

    public void attachStockItem(StockItem stockItem, String user, String historyComments, StockQtyAdjustmentHistory qtyAdjustmentHistory) {
        if (stockItem == null) throw new IllegalArgumentException("Invalid stock item!");
        if (StringUtils.isBlank(user)) throw new IllegalArgumentException("The Username is blank!");
        stockItem.getIcnbr();
        stockItemRepository.makePersistent(stockItem);
    }

    public EditStockItemDropDownListsDTO getDropDownLists() {
        List<User> users = userRepository.findAllUsers();
        List<Unit> units = unitRepository.findAll(false, new String[]{UnitRepository.ORDER_BY_CODE, UnitRepository.ORDER_BY_NAME});
        List<OrgBudget> orgBudgets = orgBudgetRepository.findAllPurchaseOrgBudgets();
        List<StockItemFacility> facilities = stockItemFacilityRepository.findAll();
        List<Category> categories = categoryRepository.findAll(false);
        List<ObjectCode> objectCodes = objectCodeRepository.findAll();
        List<Vendor> vendors = vendorRepository.findAll();
        List<Status> statuses = statusRepository.findAllByStatusTypeCode(StatusType.STOCK_ITEM);
        List<StockQtyChangeReasonRef> stockQtyChangeReasonRefs = stockQtyChangeReasonRefRepository.findAll();
        List<Manufacturer> manufacturers = manufacturerRepository.findAll(false);
        List<CycleCountPriority> cycleCountPriorities = cycleCountPriorityRepository.findAll();
        List<Hazardous> hazardouses = hazardousRepository.findAll();

        return new EditStockItemDropDownListsDTO(units, orgBudgets, facilities, users, categories, objectCodes, vendors, statuses, stockQtyChangeReasonRefs, manufacturers, cycleCountPriorities, hazardouses);
    }

    //TODO move these setters in the service layer
    public void setCategory(StockItem stockItem, String categoryId) {
        if (stockItem.getCategory().getCategoryId().equals(Long.parseLong(categoryId))) return;
        stockItemService.setCategory(stockItem, Long.parseLong(categoryId));
        stockItem.getCategory().getCategoryCode();
    }

    public void setCycleCountPriority(StockItem stockItem, String cycleCountPriorityId) {
        if (StringUtils.isEmpty(cycleCountPriorityId)) {
            stockItem.setCycleCountPriorityObject(null);
            return;
        }

        if (stockItem.getCycleCountPriorityObject() != null &&
                stockItem.getCycleCountPriorityObject().getCycleCountPriorityId().equals(Long.parseLong(cycleCountPriorityId)))
            return;

        CycleCountPriority priority =
                (CycleCountPriority) cycleCountPriorityRepository.getById(Long.parseLong(cycleCountPriorityId));

        stockItem.setCycleCountPriorityObject(priority);
    }

    public void setObjectCode(StockItem stockItem, String objectCodeId) {
        if (StringUtils.isEmpty(objectCodeId)) {
            stockItem.setObjectCode(null);
            return;
        }

        if (stockItem.getObjectCode() != null && stockItem.getObjectCode().getObjectCodeId().equals(Long.parseLong(objectCodeId)))
            return;

        ObjectCode objectCode = (ObjectCode) objectCodeRepository.load(ObjectCode.class, Long.parseLong(objectCodeId));
        Hibernate.initialize(objectCode);
        stockItem.setObjectCode(objectCode);
    }

    public void setHazardous(StockItem stockItem, String hazardousId) {
        if (StringUtils.isEmpty(hazardousId)) {
            stockItem.setHazardousObject(null);
            return;
        }

        if (stockItem.getHazardousObject() != null && stockItem.getHazardousObject().getHazardousId().equals(Long.parseLong(hazardousId)))
            return;
        Hazardous hazardous = (Hazardous) hazardousRepository.loadById(Long.parseLong(hazardousId));
        stockItem.setHazardousObject(hazardous);
    }

    public void setDispenseUnit(StockItem stockItem, String dispenseUnitId) {
        try {
            Long.parseLong(dispenseUnitId);
        } catch (Exception e) {
            throw new IllegalArgumentException("The dispenseUnitId parameter must be a valid not null or empty number!");
        }
        if (stockItem.getDispenseUnit().getUnitId().equals(Long.parseLong(dispenseUnitId))) {
            return;
        }
        Unit du = (Unit) unitRepository.loadUnitById(Long.parseLong(dispenseUnitId));
        stockItem.setDispenseUnit(du);
    }

    public void setOrgBudgetCode(StockItem stockItem, String orgBudgetId) {
        try {
            Long.parseLong(orgBudgetId);
        } catch (Exception e) {
            throw new IllegalArgumentException("The orgBudgetId parameter must be a valid not null or empty number!");
        }
        if (stockItem.getOrgBudget().getOrgBudgetId().equals(Long.parseLong(orgBudgetId))) {
            return;
        }
        OrgBudget ob = (OrgBudget) orgBudgetRepository.getOrgBudgetById(Long.parseLong(orgBudgetId), false);
        stockItem.setOrgBudget(ob);
    }

    public void setPrimaryContact(StockItem stockItem, String primaryContactId) {
        try {
            Long.parseLong(primaryContactId);
        } catch (Exception e) {
            throw new IllegalArgumentException("The primaryContactId parameter must be a valid not null or empty number!");
        }
        if (stockItem.getPrimaryContact().getPersonId().equals(Long.parseLong(primaryContactId))) {
            return;
        }
        Person p = (Person) userRepository.getUserById(Long.parseLong(primaryContactId));
        stockItem.setPrimaryContact(p);
    }

    public void setSecondaryContact(StockItem stockItem, String secondaryContactId) {
        try {
            Long.parseLong(secondaryContactId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The primaryContactId parameter must be a valid not null or empty number!");
        }
        if (stockItem.getSecondaryContact().getPersonId().equals(Long.parseLong(secondaryContactId))) {
            return;
        }
        Person p = (Person) userRepository.getUserById(Long.parseLong(secondaryContactId));
        stockItem.setSecondaryContact(p);
    }

    public List getRequestLineItemsOnOrder(Long stockItemId) {
        return stockItemRepository.findOnOrderRequestLineItems(stockItemId);
    }

    public void setStatus(StockItem stockItem, String statusId) {
        if (stockItem.getStatus().getStatusId().equals(Long.parseLong(statusId))) return;
        Status status = statusRepository.loadById(Long.parseLong(statusId));
        status.getStatusCode();
        stockItem.setStatus(status);
    }

    public void setPrimaryStockItemLocation(StockItem stockItem, String primaryStockItemLocationKey, String user) {
        stockItemService.setPrimaryLocation(stockItem, primaryStockItemLocationKey, user);
    }

    public void addStockItemLocation(StockItem stockItem, String facilityId, String locationCode, String user) {
        stockItemService.addStockItemLocation(stockItem, facilityId, locationCode, user);
    }

    public void removeStockItemLocation(StockItem stockItem, String facilityId, String locationCode, String user) {
        stockItemService.removeStockItemLocation(stockItem, facilityId, locationCode, user);
    }

    public void addStockItemLot(StockItem stockItem, String lotCode, String lotExpDate, String user) throws NotificationException {
        stockItemService.addStockItemLot(stockItem, lotCode, lotExpDate, user);
    }

    public void removeStockItemLot(StockItem stockItem, String lotCode, String lotExpDate, String username) {
        stockItemService.removeStockItemLot(stockItem, lotCode, lotExpDate, username);
    }

    public void setChangeReason(StockQtyAdjustmentHistory adjustmentHistory, String reasonId) {
        if (adjustmentHistory != null
                && adjustmentHistory.getChangeReason() != null
                && adjustmentHistory.getChangeReason().getId().equals(Long.parseLong(reasonId))) {
            return;
        }
        adjustmentHistory.setChangeReason(stockQtyChangeReasonRefRepository.getById(Long.parseLong(reasonId)));
    }

    public void setOrgBudget(StockQtyAdjustmentHistory adjustmentHistory, String orgBdgtId) {
        if (adjustmentHistory != null
                && adjustmentHistory.getOrgBudget() != null
                && adjustmentHistory.getOrgBudget().getOrgBudgetId().equals(Long.parseLong(orgBdgtId))) {
            return;
        }
        adjustmentHistory.setOrgBudget(orgBudgetRepository.getOrgBudgetById(Long.parseLong(orgBdgtId), false));
    }

    public User getUser(String username) {
        return userRepository.getUser(username);
    }

    public void setVendorCatalogNbr(ItemVendor itemVendor, String vendorCatalogNbr, String username) {
        stockItemService.setVendorCatalogNbr(itemVendor, vendorCatalogNbr, username);
    }

    public void setVendorBuyUnit(ItemVendor itemVendor, String buyUnitId, String username) throws NotificationException {
        stockItemService.setVendorBuyUnit(itemVendor, buyUnitId, username);
    }

    public void setVendorBuyUnitCost(ItemVendor itemVendor, String buyUnitCost, String username) throws NotificationException {
        stockItemService.setVendorBuyUnitCost(itemVendor, buyUnitCost, username);
    }

    public void setVendorDispenseUnitPerBuyUnit(ItemVendor itemVendor, String dispenseUnitPerBuyUnit, String username) throws NotificationException {
        stockItemService.setVendorDispenseUnitPerBuyUnit(itemVendor, dispenseUnitPerBuyUnit, username);
    }

    public void setVendorDiscount(ItemVendor itemVendor, String discount, String username) throws NotificationException {
        stockItemService.setVendorDiscount(itemVendor, discount, username);
    }

    public void setPrimaryVendor(StockItem stockItem, ItemVendor itemVendor, String username) throws NotificationException {
        stockItemService.setPrimaryVendor(stockItem, itemVendor, username);
    }

    public void addNewVendor(Item item, String newVendorId, String newVendorCatalogNbr, String newVendorBuyUnitId, String newVendorBuyUnitCost, String newVendorDispenseUnitPerBuyUnit, String newVendorDiscount, String user) throws NotificationException {
        stockItemService.addNewVendor(item, newVendorId, newVendorCatalogNbr, newVendorBuyUnitId, newVendorBuyUnitCost, newVendorDispenseUnitPerBuyUnit, newVendorDiscount, user);
    }

    public void removeVendor(StockItem stockItem, String vendorKey, String username) {
        stockItemService.removeVendor(stockItem, vendorKey, username);
    }

    public List<StockItemHistory> getStockItemHistory(String stockItemId, int noOfRecords) {
        List<StockItemHistory> list = stockItemRepository.
                getStockItemHistory((StockItem) stockItemRepository.load(StockItem.class, new Long(stockItemId)),
                        noOfRecords);
        for (StockItemHistory itemHistory : list) {
            Hibernate.initialize(itemHistory.getStockItem());
            Hibernate.initialize(itemHistory.getStockItem().getCategory());
            Hibernate.initialize(itemHistory.getStockItem().getPrimaryContact());
            Hibernate.initialize(itemHistory.getPrimaryContact());
            Hibernate.initialize(itemHistory.getStockItem().getSecondaryContact());
            Hibernate.initialize(itemHistory.getSecondaryContact());
            Hibernate.initialize(itemHistory.getStockItem().getStatus());
        }
        return list;
    }

    public List getQtyAdjustmentHistory(String stockItemId, int noOfRecords) {
        List<StockQtyAdjustmentHistory> adjusments = stockItemRepository.getStockQtyAdjustmentHistory((StockItem) stockItemRepository.load(StockItem.class, new Long(stockItemId)), noOfRecords);
        for (StockQtyAdjustmentHistory adjusment : adjusments) {
            Hibernate.initialize(adjusment.getChangeReason());
            Hibernate.initialize(adjusment.getOrgBudget());
            Hibernate.initialize(adjusment.getChangedBy());
        }
        return adjusments;
    }

    public List<StockItemLocationHistory>  getStockItemLocationHistory(String stockItemId, int noOfRecords) {
        List<StockItemLocationHistory> list = stockItemRepository.getStockItemLocationHistory((StockItem) stockItemRepository.load(StockItem.class, new Long(stockItemId)), noOfRecords);
        for (StockItemLocationHistory itemLocationHistory : list) {
            Hibernate.initialize(itemLocationHistory.getStockItem());
            Hibernate.initialize(itemLocationHistory.getStockItem().getCategory());
            Hibernate.initialize(itemLocationHistory.getStockItem().getPrimaryContact());
            Hibernate.initialize(itemLocationHistory.getLocationCode());
            Hibernate.initialize(itemLocationHistory.getStockItem().getSecondaryContact());
            Hibernate.initialize(itemLocationHistory.getFacility());
            Hibernate.initialize(itemLocationHistory.getStockItem().getStatus());
        }
        return list;
    }

    public List<ItemVendorLinkHistory> getItemVendorLinkHistory(String stockItemId, int noOfRecords) {
        List<ItemVendorLinkHistory> list = stockItemRepository.getItemVendorLinkHistory((StockItem) stockItemRepository.load(StockItem.class, new Long(stockItemId)), noOfRecords);
        for (ItemVendorLinkHistory itemVendorLinkHistory : list) {
            Hibernate.initialize(itemVendorLinkHistory.getStockItem());
            Hibernate.initialize(itemVendorLinkHistory.getStockItem().getCategory());
            Hibernate.initialize(itemVendorLinkHistory.getStockItem().getPrimaryContact());
            Hibernate.initialize(itemVendorLinkHistory.getStockItem().getSecondaryContact());
            Hibernate.initialize(itemVendorLinkHistory.getStockItem().getStatus());
            Hibernate.initialize(itemVendorLinkHistory.getVendor().getVendorId());
            Hibernate.initialize(itemVendorLinkHistory.getVendor().getExternalOrgDetail());
            //Hibernate.initialize(itemVendorLinkHistory.getBuyUnit().getName());
            Hibernate.initialize(itemVendorLinkHistory.getVendorContract());
        }
        return list;
    }

    public void storeAttachFileInfo(StockItem stockItem, FormFile printSpecFile) throws InfrastructureException {
    try {
//        stockItemService.attachFileInfo(stockItem,  printSpecFile.getFileName(), printSpecFile.getFileSize(), printSpecFile.getFileData());
        stockItemService.storeAttachFileInfo(stockItem,  printSpecFile.getFileName(), printSpecFile.getFileData(), new Long(printSpecFile.getFileSize()));
    }
    catch(FileNotFoundException fnfe) {
                throw new InfrastructureException("Failed Building Print Spec File: ", fnfe);
        }
    catch(IOException ioe) {
                throw new InfrastructureException("Failed Building Print Spec File: ", ioe);
        }
    }
}
