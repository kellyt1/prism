package us.mn.state.health.facade.inventory.editStockItem;

import java.util.List;

import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.StockItemHistory;
import us.mn.state.health.model.inventory.StockQtyAdjustmentHistory;
import us.mn.state.health.util.NotificationException;
import us.mn.state.health.common.exceptions.InfrastructureException;
import org.apache.struts.upload.FormFile;

public interface EditStockItemFacade {
    StockItem detachStockItem(Long stockItemId);

    void attachStockItem(StockItem stockItem, String user, String historyComments, StockQtyAdjustmentHistory qtyAdjustmentHistory);

    EditStockItemDropDownListsDTO getDropDownLists();

    void setCategory(StockItem stockItem, String categoryId);

    void setCycleCountPriority(StockItem stockItem, String cycleCountPriorityId);

    void setObjectCode(StockItem stockItem, String objectCodeId);

    void setHazardous(StockItem stockItem, String hazardousId);

    void setDispenseUnit(StockItem stockItem, String dispenseUnitId);

    void setOrgBudgetCode(StockItem stockItem, String orgBudgetId);

    void setPrimaryContact(StockItem stockItem, String primaryContactId);

    void setSecondaryContact(StockItem stockItem, String secondaryContactId);

    List getRequestLineItemsOnOrder(Long stockItemId);

    void setStatus(StockItem stockItem, String statusId);

    void setPrimaryStockItemLocation(StockItem stockItem, String primaryStockItemLocationKey, String user);

    void addStockItemLocation(StockItem stockItem, String facilityId, String locationCode, String user);

    void removeStockItemLocation(StockItem stockItem, String facilityId, String locationCode, String user);

    void addStockItemLot(StockItem stockItem, String lotCode, String lotExpDate, String user) throws NotificationException;

    void removeStockItemLot(StockItem stockItem, String lotCode, String lotExpDate, String username);

    void setChangeReason(StockQtyAdjustmentHistory adjustmentHistory, String reasonId);

    void setOrgBudget(StockQtyAdjustmentHistory adjustmentHistory, String orgBdgtId);

    User getUser(String username);

    void setVendorCatalogNbr(ItemVendor itemVendor, String vendorCatalogNbr, String username);

    void setVendorBuyUnit(ItemVendor itemVendor, String buyUnitId, String username) throws NotificationException;

    void setVendorBuyUnitCost(ItemVendor itemVendor, String buyUnitCost, String username) throws NotificationException;

    void setVendorDispenseUnitPerBuyUnit(ItemVendor itemVendor, String dispenseUnitPerBuyUnit, String username) throws NotificationException;

    void setVendorDiscount(ItemVendor itemVendor, String discount, String username) throws NotificationException;

    void setPrimaryVendor(StockItem stockItem, ItemVendor itemVendor, String username) throws NotificationException;

    void addNewVendor(Item item, String newVendorId, String newVendorCatalogNbr, String newVendorBuyUnitId, String newVendorBuyUnitCost, String newVendorDispenseUnitPerBuyUnit, String newVendorDiscount, String username) throws NotificationException;

    void removeVendor(StockItem stockItem, String vendorKey, String username);


    List<StockItemHistory> getStockItemHistory(String stockItemId, int noOfRecords);

    List getQtyAdjustmentHistory(String stockItemId, int noOfRecords);

    List getStockItemLocationHistory(String stockItemId, int noOfRecords);

    List getItemVendorLinkHistory(String stockItemId, int noOfRecords);

    void storeAttachFileInfo(StockItem stockItem, FormFile printSpecFile) throws InfrastructureException;

}
