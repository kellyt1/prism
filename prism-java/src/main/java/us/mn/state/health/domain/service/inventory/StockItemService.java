package us.mn.state.health.domain.service.inventory;

import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.util.Notification;
import us.mn.state.health.util.NotificationException;

public interface StockItemService {
    Notification.Error INVALID_LOT_EXPIRATION_DATE = new Notification.Error("lot.expirationDate", "Invalid Expiration Date!");
    Notification.Error INVALID_LOT_CODE = new Notification.Error("lot.expirationDate", "Invalid Lot Code!");

    StockItemServiceResult activateOnHoldStockItems();

    void setCategory(StockItem stockItem, Long categoryId);

    void setPrimaryLocation(StockItem stockItem, String stockItemLocationKey, String user);
//
//    void setCycleCountPriority(StockItem stockItem, String cycleCountPriorityId);

    void removeStockItemLocation(StockItem stockItem, String facilityId, String locationCode, String user);

    void addStockItemLocation(StockItem stockItem, String facilityId, String locationCode, String user);

    void addStockItemLot(StockItem stockItem, String lotCode, String lotExpDate, String user) throws NotificationException;

    void removeStockItemLot(StockItem stockItem, String lotCode, String lotExpDate, String username);

    void setVendorCatalogNbr(ItemVendor itemVendor, String vendorCatalogNbr, String username);

    void setVendorBuyUnit(ItemVendor itemVendor, String buyUnitId, String username) throws NotificationException;

    void setVendorBuyUnitCost(ItemVendor itemVendor, String buyUnitCost, String username) throws NotificationException;

    void setVendorDiscount(ItemVendor itemVendor, String discount, String username) throws NotificationException;

    void setVendorDispenseUnitPerBuyUnit(ItemVendor itemVendor, String dispenseUnitPerBuyUnit, String username) throws NotificationException;

    void setPrimaryVendor(StockItem stockItem, ItemVendor itemVendor, String username) throws NotificationException;

    void addNewVendor(Item item, String newVendorId, String newVendorCatalogNbr, String newVendorBuyUnitId, String newVendorBuyUnitCost, String newVendorDispenseUnitPerBuyUnit, String newVendorDiscount, String username) throws NotificationException;

    void removeVendor(StockItem stockItem, String vendorKey, String username);

    void storeAttachFileInfo(StockItem stockItem, String filename, byte[] fileContents, Long fileSize);
}
