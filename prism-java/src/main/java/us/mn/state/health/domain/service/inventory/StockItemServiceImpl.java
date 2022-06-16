package us.mn.state.health.domain.service.inventory;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.domain.repository.common.CategoryRepository;
import us.mn.state.health.domain.repository.common.StatusRepository;
import us.mn.state.health.domain.repository.common.UnitRepository;
import us.mn.state.health.domain.repository.common.VendorRepository;
import us.mn.state.health.domain.repository.inventory.StockItemFacilityRepository;
import us.mn.state.health.domain.repository.inventory.StockItemRepository;
import us.mn.state.health.domain.repository.inventory.StockItemSearchCriteria;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.inventory.AttachedFile;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.StockItemLocation;
import us.mn.state.health.model.inventory.StockItemLot;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.util.Notification;
import us.mn.state.health.util.NotificationException;

public class StockItemServiceImpl implements StockItemService {
    private StatusRepository statusRepository;
    private StockItemRepository stockItemRepository;
    private CategoryRepository categoryRepository;
    private StockItemFacilityRepository stockItemFacilityRepository;
    private UnitRepository unitRepository;
    private VendorRepository vendorRepository;


    public StockItemServiceImpl(StatusRepository statusRepository,
                                StockItemRepository stockItemRepository,
                                CategoryRepository categoryRepository,
                                StockItemFacilityRepository stockItemFacilityRepository,
                                UnitRepository unitRepository,
                                VendorRepository vendorRepository) {
        this.statusRepository = statusRepository;
        this.stockItemRepository = stockItemRepository;
        this.categoryRepository = categoryRepository;
        this.stockItemFacilityRepository = stockItemFacilityRepository;
        this.unitRepository = unitRepository;
        this.vendorRepository = vendorRepository;
    }

    public StockItemServiceResult activateOnHoldStockItems() {

        StockItemSearchCriteria stockItemSearchCriteria = new StockItemSearchCriteria();
        stockItemSearchCriteria.setStatusId(onHoldStatus().getStatusId().toString());
        stockItemSearchCriteria.setHoldUntilDate(new Date());

        List<StockItem> stockItems = stockItems(stockItemSearchCriteria);
        for (StockItem stockItem : stockItems) {
            stockItem.setStatus(activeStatus());
            stockItem.setLastUpdatedBy("prism");
            stockItem.setLastUpdatedDate(new Date());
        }
        return buildResult(stockItems);
    }

    public void setCategory(StockItem stockItem, Long categoryId) {
        Category cat = categoryRepository.getCategoryById(categoryId);
        stockItem.setCategory(cat);
    }

    public void setPrimaryLocation(StockItem stockItem, String stockItemLocationKey, String user) {
        for (StockItemLocation stockItemLocation : stockItem.getLocations()) {
            if (stockItemLocationKey.equals(stockItemLocation.getFullBinLocation())) {
                if (!StringUtils.equals(stockItemLocationKey, stockItemLocation.getFullBinLocation())) {
                    stockItemLocation.setUpdatedBy(user);
                    stockItemLocation.setUpdateDate(new Date());
                }
                stockItemLocation.setIsPrimary(Boolean.TRUE);
                continue;
            }
            if (!StringUtils.equals(stockItemLocation.getIsPrimary().toString(), Boolean.FALSE.toString())) {
                stockItemLocation.setUpdatedBy(user);
                stockItemLocation.setUpdateDate(new Date());
            }
            stockItemLocation.setIsPrimary(Boolean.FALSE);
        }
    }

    public void removeStockItemLocation(StockItem stockItem, String facilityId, String locationCode, String user) {
        Set<StockItemLocation> itemLocations = stockItem.getLocations();
        for (StockItemLocation location : itemLocations) {
            if (location.getFacility().getFacilityId().toString().equals(facilityId) && ((locationCode.trim().equals("") && location.getLocationCode() == null) || (StringUtils.equals(locationCode, location.getLocationCode())))) {
                //we delete 1 milisecond because the deletion and the comparition might occur in the same milisecond 
                location.setEndDate(new Date(new Date().getTime() - 1));
                location.setEndedBy(user);
            }
        }
    }

    public void addStockItemLocation(StockItem stockItem, String facilityId, String locationCode, String user) {
        StockItemLocation location = new StockItemLocation();
        location.setLocationCode(locationCode);
        location.setInsertedBy(user);
        location.setInsertionDate(new Date());
        location.setFacility(stockItemFacilityRepository.getStockItemFacilityById(new Long(facilityId)));
        location.setIsPrimary(Boolean.FALSE);
        stockItem.addLocation(location);
    }

    /**
     * @param stockItem
     * @param lotCode
     * @param lotExpDate
     * @param user
     * @throws NotificationException
     */
    public void addStockItemLot(StockItem stockItem, String lotCode, String lotExpDate, String user) throws NotificationException {
        Notification notification = new Notification();
        Date date = null;
        try {
            date = DateUtils.createDate(lotExpDate);
        } catch (ParseException e) {
            notification.addError(INVALID_LOT_EXPIRATION_DATE);
        }
        if (StringUtils.isBlank(lotCode)) {
            notification.addError(INVALID_LOT_CODE);
        }

        if (notification.hasErrors()) throw new NotificationException(notification);
        stockItem.addLot(StockItemLot.createStockItemLot(stockItem, lotCode, date, user));
    }

    public void removeStockItemLot(StockItem stockItem, String lotCode, String lotExpDate, String username) {
        for (StockItemLot lot : stockItem.getLots()) {
            if (lot.getLotCode().equals(lotCode) &&
                    DateUtils.toString(lot.getExpirationDate()).equals(lotExpDate)) {
                lot.setEndDate(new Date(new Date().getTime() - 1));
                lot.setEndedBy(username);
            }
        }
    }


    public void setVendorCatalogNbr(ItemVendor itemVendor, String vendorCatalogNbr, String username) {
        if (!StringUtils.equals(itemVendor.getVendorCatalogNbr(), vendorCatalogNbr)) {
            itemVendor.setVendorCatalogNbr(vendorCatalogNbr);
            itemVendor.setUpdateMetaProperties(username);
        }
    }

    /**
     * @param itemVendor
     * @param buyUnitId
     * @param username
     * @throws NotificationException
     */
    public void setVendorBuyUnit(ItemVendor itemVendor, String buyUnitId, String username) throws NotificationException {
        try {
            if ((itemVendor.getBuyUnit() == null && !StringUtils.isEmpty(buyUnitId)) || (!itemVendor.getBuyUnit().getUnitId().equals(Long.parseLong(buyUnitId)))) {
                itemVendor.setBuyUnit(unitRepository.getUnitById(Long.parseLong(buyUnitId)));
                itemVendor.setUpdatedBy(username);
            }
        } catch (NumberFormatException e) {
            Notification notification = new Notification();
            Notification.Error error = new Notification.Error("buyUnit", "Invalid Buy Unit selected for the vendor "
                    + itemVendor.getVendor().getExternalOrgDetail().getOrgName());
            notification.addError(error);
            throw new NotificationException(notification);
        }
    }

    public void setVendorBuyUnitCost(ItemVendor itemVendor, String buyUnitCost, String username) throws NotificationException {
        if (StringUtils.isBlank(buyUnitCost) && itemVendor.getBuyUnitCost() == null) return;
        try {
            if (
                    itemVendor.getBuyUnitCost() == null && !StringUtils.isEmpty(buyUnitCost)
                            ||
                            StringUtils.isEmpty(buyUnitCost) && itemVendor.getBuyUnitCost() != null
                            ||
                            (!itemVendor.getBuyUnitCost().equals(Double.parseDouble(buyUnitCost))
                            )
                    ) {

                if (!StringUtils.isEmpty(buyUnitCost)) {
                    if (Double.parseDouble(buyUnitCost) < 0) {
                        Notification notification = new Notification();
                        notification.addError("buyUnitCost", "Invalid Buy Unit Cost for the vendor "
                                + itemVendor.getVendor().getExternalOrgDetail().getOrgName() + "(it must be a positive number)");
                        throw new NotificationException(notification);
                    }
                    itemVendor.setBuyUnitCost(Double.parseDouble(buyUnitCost));
                } else {
                    itemVendor.setBuyUnitCost(null);
                }
                itemVendor.setUpdateMetaProperties(username);
            }
        } catch (NumberFormatException e) {
            Notification notification = new Notification();
            notification.addError("buyUnitCost", "Invalid Buy Unit Cost for the vendor "
                    + itemVendor.getVendor().getExternalOrgDetail().getOrgName());
            throw new NotificationException(notification);
        }
    }

    public void setVendorDiscount(ItemVendor itemVendor, String discount, String username) throws NotificationException {
        try {
            if (Double.parseDouble(discount) < 0 || Double.parseDouble(discount) > 100) {
                Notification notification = new Notification();
                notification.addError("discount", "Invalid Discount for the vendor "
                        + itemVendor.getVendor().getExternalOrgDetail().getOrgName() + "(it must be a number between 0 and 100)");
                throw new NotificationException(notification);
            }
            itemVendor.setDiscount(new Double(Math.round(Double.parseDouble(discount) * 100) / 100d));
            itemVendor.setUpdateMetaProperties(username);
        } catch (NumberFormatException e) {
            Notification notification = new Notification();
            notification.addError("buyUnitCost", "Invalid Discount for the vendor "
                    + itemVendor.getVendor().getExternalOrgDetail().getOrgName());
            throw new NotificationException(notification);
        }
    }

    public void setVendorDispenseUnitPerBuyUnit(ItemVendor itemVendor, String dispenseUnitPerBuyUnit, String username) throws NotificationException {
        try {
            if (StringUtils.isBlank(dispenseUnitPerBuyUnit) && itemVendor.getDispenseUnitsPerBuyUnit() != null) {
                itemVendor.setDispenseUnitsPerBuyUnit(null);
                itemVendor.setUpdateMetaProperties(username);
            }


            if ((StringUtils.isBlank(dispenseUnitPerBuyUnit) && itemVendor.getDispenseUnitsPerBuyUnit() == null)
                    ||
                    new Integer(dispenseUnitPerBuyUnit).equals(itemVendor.getDispenseUnitsPerBuyUnit())) return;

            if (Integer.parseInt(dispenseUnitPerBuyUnit) < 1) {
                Notification notification = new Notification();
                notification.addError("dupbu", "The Dispense Unit Per Buy Unit for " + itemVendor.getVendor().getExternalOrgDetail().getOrgName() + " must be a number >= 1 ");
                throw new NotificationException(notification);
            }
            itemVendor.setDispenseUnitsPerBuyUnit(new Integer(dispenseUnitPerBuyUnit));
            itemVendor.setUpdateMetaProperties(username);
        } catch (NumberFormatException e) {
            Notification notification = new Notification();
            notification.addError("dupbu", "The Dispense Unit Per Buy Unit for " + itemVendor.getVendor().getExternalOrgDetail().getOrgName() + " must be a number >= 1 ");
            throw new NotificationException(notification);
        }
    }

    public void setPrimaryVendor(StockItem stockItem, ItemVendor itemVendor, String username) throws NotificationException {
        if (itemVendor.getPrimaryVendor()) return;
        for (int i = 0; i < stockItem.getItemVendorsAsArray().length; i++) {
            ItemVendor iv = (ItemVendor) stockItem.getItemVendorsAsArray()[i];
            if (iv.getPrimaryVendor()) {
                iv.setPrimaryVendor(Boolean.FALSE);
                iv.setUpdateMetaProperties(username);
            }
        }
        itemVendor.setPrimaryVendor(Boolean.TRUE);
        itemVendor.setUpdateMetaProperties(username);
    }

    public void addNewVendor(Item item, String newVendorId, String newVendorCatalogNbr, String newVendorBuyUnitId, String newVendorBuyUnitCost, String newVendorDispenseUnitPerBuyUnit, String newVendorDiscount, String username) throws NotificationException {
        Notification notification = new Notification();
        Vendor vendor = validateVendor(newVendorId, notification);
        Unit buyUnit = validateBuyUnit(newVendorBuyUnitId, notification);
        Double buyUnitCost = validateBuyUnitCost(newVendorBuyUnitCost, notification);
        Integer duPerBu = validateDispenseUnitPerBuyUnit(newVendorDispenseUnitPerBuyUnit, notification);
        Double discount = validateDiscount(newVendorDiscount, notification);

        if (notification.hasErrors()) throw new NotificationException(notification);

        vendor.getExternalOrgDetail().getOrgName();

        ItemVendor itemVendor = ItemVendor.createItemVendor(vendor, item, username);
        itemVendor.setDiscount(discount);
        itemVendor.setBuyUnit(buyUnit);
        itemVendor.setBuyUnitCost(buyUnitCost);
        itemVendor.setVendorCatalogNbr(newVendorCatalogNbr);
        itemVendor.setDispenseUnitsPerBuyUnit(duPerBu);
        itemVendor.setUpdateMetaProperties(username);
    }

    public void removeVendor(StockItem stockItem, String vendorKey, String username) {
        if (!NumberUtils.isNumber(vendorKey)) return;
        for (ItemVendor itemVendor : stockItem.getItemVendors()) {
            if (itemVendor.getKey() == Integer.parseInt(vendorKey)) {
                itemVendor.setEndDate(new Date(new Date().getTime() - 1));
                itemVendor.setEndedBy(username);
                itemVendor.setUpdateMetaProperties(username);
                return;
            }
        }
    }

    // the newVendorDiscount is not required
    private Double validateDiscount(String newVendorDiscount, Notification notification) {
        if (StringUtils.isBlank(newVendorDiscount)) return null;
        Double discount = null;
        try {
            discount = new Double(newVendorDiscount);
        } catch (NumberFormatException e) {
            notification.addError("newVendorDiscount", "The discount is not a valid number!");
        }

        if (discount != null && (discount.doubleValue() < 0 || discount.doubleValue() > 100)) {
            notification.addError("newVendorDiscount", "The discount must be between 0 and 100!");
        }
        return discount;
    }

    // the newVendorDispenseUnitPerBuyUnit is not required
    private Integer validateDispenseUnitPerBuyUnit(String newVendorDispenseUnitPerBuyUnit, Notification notification) {
        if (StringUtils.isBlank(newVendorDispenseUnitPerBuyUnit)) return null;
        Integer duPerBu = null;

        try {
            duPerBu = new Integer(newVendorDispenseUnitPerBuyUnit);
        } catch (NumberFormatException e) {
            notification.addError("newVendorDispenseUnitPerBuyUnit", "The Dispense Unit Per Buy Unit is not a valid number!");
        }
        if (duPerBu != null && (duPerBu.intValue() < 1))
            notification.addError("newVendorDispenseUnitPerBuyUnit", "The Dispense Unit Per Buy Unit must be a number > 0");
        return duPerBu;
    }

    // the buyUnitCost is not required
    private Double validateBuyUnitCost(String newVendorBuyUnitCost, Notification notification) {
        if (StringUtils.isBlank(newVendorBuyUnitCost)) return null;
        Double cost = new Double(0);
        try {
            cost = new Double(newVendorBuyUnitCost);
        }
        catch (NumberFormatException e) {
            invalidBuyUnitCost(notification);
        }
        if (cost < 0) invalidBuyUnitCost(notification);
        return cost;
    }

    private void invalidBuyUnitCost(Notification notification) {
        notification.addError("newVendorBuyUnitCost", "The Buy Unit Cost is not a valid number > 0");
    }

    private Unit validateBuyUnit(String newVendorBuyUnitId, Notification notification) {
        long unitId = 0;
        try {
            unitId = Long.parseLong(newVendorBuyUnitId);
        } catch (NumberFormatException e) {
            invalidBuyUnit(notification);

        }
        Unit buyUnit = unitRepository.getUnitById(unitId);
        if (buyUnit == null) {
            invalidBuyUnit(notification);
        }
        return buyUnit;
    }

    private void invalidBuyUnit(Notification notification) {
        notification.addError("newVendorBuyUnitId", "You have selected an invalid buy unit");
    }

    private Vendor validateVendor(String newVendorId, Notification notification) {
        Vendor vendor;
        long vendorId = 0;
        try {
            vendorId = Long.parseLong(newVendorId);
        } catch (NumberFormatException e) {
            invalidVendorError(notification);
        }
        vendor = vendorRepository.getById(vendorId);
        if (vendor == null) {
            invalidVendorError(notification);
        }
        return vendor;
    }

    private void invalidVendorError(Notification notification) {
        notification.addError("newVendorId", "You have selected an invalid vendor");
    }

    private StockItemServiceResult buildResult(List<StockItem> stockItems) {
        StockItemServiceResult stockItemServiceResult = new StockItemServiceResult();
        stockItemServiceResult.setStockItems(stockItems);
        return stockItemServiceResult;
    }

    private Status activeStatus() {
        return statusRepository.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM, Status.ACTIVE);
    }

    private Status onHoldStatus() {
        return statusRepository.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM, Status.ONHOLD);
    }

    private List<StockItem> stockItems(StockItemSearchCriteria stockItemSearchCriteria) {
        return stockItemRepository.findAllStockItems(stockItemSearchCriteria, true);
    }

    public void storeAttachFileInfo(StockItem stockItem, String fileName, byte[] fileContents, Long fileSize) {
        AttachedFile attachedFile = new AttachedFile();
        if(fileName != null) {
//                attachedFile.setItem(stockItem);        //TODO - TODD- check/verify this
                attachedFile.setFileName(fileName);
                attachedFile.setFileSize(fileSize);
                attachedFile.setFileContents(fileContents);
                stockItem.addAttachedFile(attachedFile);
         }
    }
}