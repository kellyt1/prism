package us.mn.state.health.web.struts.editstockitem;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.upload.FormFile;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.facade.inventory.editStockItem.EditStockItemFacade;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.StockQtyAdjustmentHistory;
import us.mn.state.health.model.inventory.StockItemHistory;
import us.mn.state.health.util.Notification;
import us.mn.state.health.util.NotificationException;
import us.mn.state.health.util.struts.ValidationUtils;

public class EditStockItemForm extends ValidatorForm {
    private static final String ERRORS_CUSTOM = "errors.custom";
    public static final String LOAD_ITEM = "load";
    public static final String UPDATE_ITEM = "update";
    public static final String ADD_STOCK_ITEM_LOCATION = "add_location";
    public static final String REMOVE_STOCK_ITEM_LOCATION = "remove_location";
    public static final String ADD_STOCK_ITEM_LOT = "add_lot";
    public static final String REMOVE_STOCK_ITEM_LOT = "remove_lot";
    public static final String ADD_VENDOR = "add_vendor";
    public static final String REMOVE_VENDOR = "remove_vendor";
    public static final String PRE_SUBMIT = "pre_submit";
    public static final String SUBMIT = "submit";
    public static final String[] commands = {LOAD_ITEM, UPDATE_ITEM,
            ADD_STOCK_ITEM_LOCATION, REMOVE_STOCK_ITEM_LOCATION,
            ADD_STOCK_ITEM_LOT, REMOVE_STOCK_ITEM_LOT,
            ADD_VENDOR, REMOVE_VENDOR,
            PRE_SUBMIT, SUBMIT
    };

    private String stockItemId;
    private String cmd;
    private String tab;
    private StockItem stockItem;

    private String facilityId;
    private String locationCode;

    private String lotCode;
    private String lotExpDate;

    private StockQtyAdjustmentHistory adjustmentHistory;

    private String newVendorId;
    private String newVendorCatalogNbr;
    private String newVendorBuyUnitId;
    private String newVendorBuyUnitCost;
    private String newVendorDispenseUnitPerBuyUnit;
    private String newVendorDiscount = "0.0";

    private String historyComments;
    private String newHistoryComment;

    private ActionErrors messages = new ActionErrors();
    private int nextPage = 0;

    //this is used to set the list values just like the category
    private EditStockItemFacade editStockItemFacade;
    private String username;
    private User user;
    private FormFile printSpecFile;

    /**
     * Set a representation of the file the user has uploaded
     */
    public void setPrintSpecFile(FormFile printSpecFile) throws InfrastructureException {
        this.printSpecFile = printSpecFile;
//        editStockItemFacade.updatePrintSpecFileInfo(getStockItem(), printSpecFile);
        if (printSpecFile.getFileName() != null &&  !printSpecFile.getFileName().trim().equals("")) {
            editStockItemFacade.storeAttachFileInfo(getStockItem(), this.printSpecFile);
        }
    }

    /**
     * Retrieve a representation of the file the user has uploaded
     */
    public FormFile getPrintSpecFile() {
        return printSpecFile;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        facilityId = null;
        newVendorId = null;
        locationCode = null;
        page = nextPage = 0;
        cmd = UPDATE_ITEM;
        historyComments = null;
        newHistoryComment = null;
    }

    public String concatHistoryComments() {
        String comments = "";
        List hist = this.editStockItemFacade.getStockItemHistory(this.getStockItemId(),10);
        for (int i = 0; i < hist.size(); i++) {
            StockItemHistory sihist  =  (StockItemHistory) hist.get(i);
            String historyComments = sihist.getHistoryComments();
//            if ((sihist.getHistoryComments() != null) && !(sihist.getHistoryComments().equalsIgnoreCase("") )) {
            if ((historyComments == null) || (historyComments.trim().equalsIgnoreCase(""))) {
                historyComments = "No Comments were entered for this change.";
            }
            comments += Integer.toString(i + 1) + ") " + sihist.getInsertedBy() + " " + sihist.getInsertionDate().toString() + " " + historyComments +"\n";
//            comments += new Integer(i+1).toString() + ") " + sihist.getInsertedBy() + " " + sihist.getInsertionDate().toString() + " " + sihist.getHistoryComments() +"\n==============\n";
        }
       return comments;
    }

    public void setEditStockItemFacade(EditStockItemFacade editStockItemFacade) {
        this.editStockItemFacade = editStockItemFacade;
    }


    public String getHistoryComments() {
        return historyComments;
    }

    public void setHistoryComments(String historyComments) {
        this.historyComments = historyComments;
    }

    public String getNewHistoryComment() {
        return newHistoryComment;
    }

    public void setNewHistoryComment(String newHistoryComment) {
        this.newHistoryComment = newHistoryComment;
    }   

    public String getNewVendorId() {
        return newVendorId;
    }

    public void setNewVendorId(String newVendorId) {
        this.newVendorId = newVendorId;
    }

    public String getNewVendorCatalogNbr() {
        return newVendorCatalogNbr;
    }

    public void setNewVendorCatalogNbr(String newVendorCatalogNbr) {
        this.newVendorCatalogNbr = newVendorCatalogNbr;
    }

    public String getNewVendorBuyUnitId() {
        return newVendorBuyUnitId;
    }

    public void setNewVendorBuyUnitId(String newVendorBuyUnitId) {
        this.newVendorBuyUnitId = newVendorBuyUnitId;
    }

    public String getNewVendorBuyUnitCost() {
        return newVendorBuyUnitCost;
    }

    public void setNewVendorBuyUnitCost(String newVendorBuyUnitCost) {
        this.newVendorBuyUnitCost = newVendorBuyUnitCost;
    }

    public String getNewVendorDispenseUnitPerBuyUnit() {
        return newVendorDispenseUnitPerBuyUnit;
    }

    public void setNewVendorDispenseUnitPerBuyUnit(String newVendorDispenseUnitPerBuyUnit) {
        this.newVendorDispenseUnitPerBuyUnit = newVendorDispenseUnitPerBuyUnit;
    }

    public String getNewVendorDiscount() {
        return newVendorDiscount;
    }

    public void setNewVendorDiscount(String newVendorDiscount) {
        this.newVendorDiscount = newVendorDiscount;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public String getStockItemId() {
        return stockItemId;
    }

    public void setStockItemId(String stockItemId) {
        this.stockItemId = stockItemId;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
        resetQtyAdjustmentHistory();
    }

    void resetQtyAdjustmentHistory() {
        this.adjustmentHistory = StockQtyAdjustmentHistory.createStockQtyAdjustmentHistory(getStockItem(), user);
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }


    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }


    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = resetErrorsAfterValidation();
        if (StringUtils.equals(cmd, LOAD_ITEM)) return null;

        validateAdjustmentHistory(errors);
        validateStockItemDescriptionLength(errors);
        validateStockItemInstructionsLength(errors);
        validateNewHistoryCommentLength(errors);
        return errors;
    }

    private void validateAdjustmentHistory(ActionErrors errors) {
        if (!adjustmentHistory.getPreviousQtyOnHand().equals(adjustmentHistory.getNewQtyOnHand())) {
            if (getAdjustmentReasonId() == null) {
                errors.add("adjustmentReasonId", new ActionMessage("errors.custom", "Please enter a Qty On Hand Change Reason!"));
            }
//            if (getAdjustmentOrgBudgetId() == null) {
//                errors.add("adjustmentOrgBudgetId", new ActionMessage("errors.custom", "Please enter an Org Budget for Qty On Hand Change Credit/Charge!"));
//            }
        }
//        if (getAdjustmentReasonId() != null && getAdjustmentOrgBudgetId() == null && !errors.get("adjustmentOrgBudgetId").hasNext()) {
//            errors.add("adjustmentOrgBudgetId", new ActionMessage("errors.custom", "Please enter an Org Budget for Qty On Hand Change Credit/Charge!"));
//        }
        if (getAdjustmentOrgBudgetId() != null && getAdjustmentReasonId() == null && !errors.get("adjustmentReasonId").hasNext()) {
            errors.add("adjustmentReasonId", new ActionMessage("errors.custom", "Please enter a Qty On Hand Change Reason!"));
        }
    }
    private void validateStockItemDescriptionLength(ActionErrors errors) {
        if (stockItem.getDescription() != null && !(stockItem.getDescription().length() <= 500) ) {
                errors.add("stockItem.description", new ActionMessage("errors.custom", "Item Description can not be longer than 500 characters.(Current Length = "+stockItem.getDescription().length()+")"));
            }
    }

    private void validateStockItemInstructionsLength(ActionErrors errors) {
        if (stockItem.getInstructions() != null && !(stockItem.getInstructions().length() <= 1000) ) {
                errors.add("stockItem.instructions", new ActionMessage("errors.custom", "Handling Instructions can not be longer than 1000 characters.(Current Length = "+stockItem.getInstructions().length()+")"));
            }
    }

    private void validateNewHistoryCommentLength(ActionErrors errors) {
        if (getNewHistoryComment() != null && !(getNewHistoryComment().length() <= 500) ) {
                errors.add("newHistoryComment", new ActionMessage("errors.custom", "History Comments can not be longer than 500 characters.(Current Length = "+ getHistoryComments().length()+")"));
            }
    }

    private ActionErrors resetErrorsAfterValidation() {
        ActionErrors errors = messages;
        messages = new ActionErrors();
        return errors;
    }

    //Adapter methods

    public String getItemDescription() {
        return stockItem.getDescription();
    }

    public void setItemDescription(String description) {
        stockItem.setDescription(description);
    }

    public String getRop() {
        return stockItem.getReorderPoint() != null ? stockItem.getReorderPoint().toString() : null;
    }

    public void setRop(String rop) {
        Integer ropoint;
        try {
            ropoint = ValidationUtils.validateIntegerGT(true, rop, 0, messages, ERRORS_CUSTOM, new Object[]{"The Reorder Point must be a number > 0 "}, "rop");
        }
        catch (RuntimeException re) {
            return;
        }
        stockItem.setReorderPoint(ropoint);
    }

    public String getRoq() {
        return stockItem.getReorderQty() != null ? stockItem.getReorderQty().toString() : null;
    }

    public void setRoq(String roq) {
        Integer roqty;
        try {
            roqty = ValidationUtils.validateIntegerGT(true, roq, 0, messages, ERRORS_CUSTOM, new Object[]{"The Reorder Quantity must be a number > 0 "}, "roq");
        }
        catch (RuntimeException re) {
            return;
        }
        stockItem.setReorderQty(roqty);
    }

    // not required
    public void setCategoryId(String categoryId) {
        editStockItemFacade.setCategory(stockItem, categoryId);
    }

    /**
     * required
     *
     * @return
     */
    public String getCategoryId() {
        return stockItem.getCategory().getCategoryId().toString();
    }

    // not required
    public void setCycleCountPriorityId(String cycleCountPriorityId) {
        editStockItemFacade.setCycleCountPriority(stockItem, cycleCountPriorityId);
    }

    /**
     * not required
     *
     * @return
     */
    public String getCycleCountPriorityId() {
        if (stockItem.getCycleCountPriorityObject() == null) return null;
        return stockItem.getCycleCountPriorityObject().getCycleCountPriorityId().toString();
    }

    // not required
    public String getEstimatedAnnualUsage() {
        return stockItem.getEstimatedAnnualUsage() != null ? stockItem.getEstimatedAnnualUsage().toString() : null;
    }

    /**
     * not required
     *
     * @param eau   estimated annual usage
     */
    public void setEstimatedAnnualUsage(String eau) {
        Integer estAnnualUsage;
        try {
            estAnnualUsage = ValidationUtils.validateIntegerGT(false, eau, 0, messages, ERRORS_CUSTOM, new Object[]{"The Estimated Annual Usage must be a number > 0"}, "eau");
        }
        catch (RuntimeException e) {
            return;
        }
        stockItem.setEstimatedAnnualUsage(estAnnualUsage);
    }

    /* required */
    public String getDispenseUnitId() {
        return stockItem.getDispenseUnit().getUnitId().toString();
    }

    /* required */
    public void setDispenseUnitId(String duId) {
        editStockItemFacade.setDispenseUnit(stockItem, duId);
    }

    // not required
    public String getReviewDate() {
        return stockItem.getReviewDate() != null ? DateUtils.toString(stockItem.getReviewDate(), DateUtils.DEFAULT_DATE_FORMAT) : null;
    }

    // not required
    public void setReviewDate(String date) {
        Date reviewDate;
        try {
            reviewDate = ValidationUtils.validateDate(false, date, messages, ERRORS_CUSTOM, new Object[]{"Review Date is not a valid date"}, "reviewDate");
        } catch (Exception e) {
            return;
        }
        stockItem.setReviewDate(reviewDate);
    }

    public Integer getAnnualUsage() {
        return stockItem.getAnnualUsage();
//        return new Integer(12);
    }

    // not required
    public String getSafetyStock() {
        return stockItem.getSafetyStock() != null ? stockItem.getSafetyStock().toString() : null;
    }

    // not required
    public void setSafetyStock(String safetyStock) {
        Integer ss;
        try {
            ss = ValidationUtils.validateIntegerGT(false, safetyStock, 0, messages, ERRORS_CUSTOM, new Object[]{"The Safety Stock must be a number > 0"}, "safetyStock");
        } catch (Exception e) {
            return;
        }
        stockItem.setSafetyStock(ss);
    }

    /* not required */
    public String getObjectCodeId() {
        return stockItem.getObjectCode() != null ? stockItem.getObjectCode().getObjectCodeId().toString() : null;
    }

    /* not required */
    public void setObjectCodeId(String objectCodeId) {
        editStockItemFacade.setObjectCode(stockItem, objectCodeId);
    }

    /* not required */
    public String getHazardousId() {
        return stockItem.getHazardousObject() != null ? stockItem.getHazardousObject().getHazardousId().toString() : null;
    }

    /* not required */
    public void setHazardousId(String hazardousId) {
        editStockItemFacade.setHazardous(stockItem, hazardousId);
    }

    public String getOrgBudgetId() {
        return stockItem.getOrgBudget().getOrgBudgetId().toString();
    }

    public void setOrgBudgetId(String orgBudgetId) {
        editStockItemFacade.setOrgBudgetCode(stockItem, orgBudgetId);
    }

    //required
    public String getPrimaryContactId() {
        return stockItem.getPrimaryContact().getPersonId().toString();
    }

    public void setStatusId(String statusId) {
        editStockItemFacade.setStatus(stockItem, statusId);
    }

    public String getStatusId() {
        return getStockItem().getStatus().getStatusId().toString();
    }

    //required
    public void setPrimaryContactId(String primaryContactId) {
        try {
            ValidationUtils.validateIntegerGT(true, primaryContactId, 0, messages, ERRORS_CUSTOM, new Object[]{"Please select a person as a primary contact!"}, "primaryContactId");
        } catch (Exception e) {
            return;
        }
        editStockItemFacade.setPrimaryContact(stockItem, primaryContactId);
    }

    //required
    public String getSecondaryContactId() {
        return stockItem.getSecondaryContact().getPersonId().toString();
    }

    //required
    public void setSecondaryContactId(String secondaryContactId) {
        try {
            ValidationUtils.validateIntegerGT(true, secondaryContactId, 0, messages, ERRORS_CUSTOM, new Object[]{"Please select a person as a secondary contact!"}, "secondaryId");
        } catch (Exception e) {
            return;
        }
        editStockItemFacade.setSecondaryContact(stockItem, secondaryContactId);
    }

    public String getHoldUntilDate() {
        return stockItem.getHoldUntilDate() != null ? DateUtils.toString(stockItem.getHoldUntilDate(), DateUtils.DEFAULT_DATE_FORMAT) : null;
    }

    public void setHoldUntilDate(String date) {
        Date holdUntilDate;
        try {
            holdUntilDate = ValidationUtils.validateDate(stockItem.getStatus().getStatusCode().equals(Status.ONHOLD), date, messages, ERRORS_CUSTOM, new Object[]{"Hold Until Date is required and is not a valid date"}, "reviewDate");
        } catch (Exception e) {
            return;
        }
        stockItem.setHoldUntilDate(holdUntilDate);
    }

    public String getFillUntilDepleted() {
        return stockItem.getFillUntilDepleted() != null ? stockItem.getFillUntilDepleted().toString() : "false";
    }

    public void setFillUntilDepleted(String booleanValue) {
        stockItem.setFillUntilDepleted(Boolean.valueOf(booleanValue));
    }

    public String getPrimaryLocationKey() {
        return stockItem.getPrimaryStockItemLocation() != null ? stockItem.getPrimaryStockItemLocation().getFullBinLocation() : null;
    }

    public void setPrimaryLocationKey(String key) {
        editStockItemFacade.setPrimaryStockItemLocation(stockItem, key, username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public String getLotExpDate() {
        return lotExpDate;
    }

    public void setLotExpDate(String lotExpDate) {
        this.lotExpDate = lotExpDate;
        try {
            ValidationUtils.validateDate(false, lotExpDate, messages, ERRORS_CUSTOM, new Object[]{"Lot Expiration Date is not a valid date"}, "lotExpirationDate");
        } catch (Exception ignore) {
        }
    }

    public void setNewQtyOnHand(String newQty) {
        Integer qty;
        try {
            qty = ValidationUtils.validateIntegerGT(true, newQty, 0, messages, ERRORS_CUSTOM, new Object[]{"The Qty On Hand must be a number >= 0 "}, "qtyOnHand");
        }
        catch (RuntimeException re) {
            return;
        }
        stockItem.setQtyOnHand(qty);
        adjustmentHistory.setNewQtyOnHand(qty);
    }

    public String getNewQtyOnHand() {
        return adjustmentHistory.getNewQtyOnHand() != null ?
                adjustmentHistory.getNewQtyOnHand().toString() : stockItem.getQtyOnHand().toString();

    }

    public String getAdjustmentReasonId() {
        return adjustmentHistory.getChangeReason() != null ? adjustmentHistory.getChangeReason().getId().toString() : null;
    }

    public void setAdjustmentReasonId(String reasonId) {
        if (StringUtils.isEmpty(reasonId)) {
            adjustmentHistory.setChangeReason(null);
        } else {
            editStockItemFacade.setChangeReason(adjustmentHistory, reasonId);
        }
    }

    public String getAdjustmentOrgBudgetId() {
        return adjustmentHistory.getOrgBudget() != null ? adjustmentHistory.getOrgBudget().getOrgBudgetId().toString() : null;
    }

    public void setAdjustmentOrgBudgetId(String orgBdgtId) {
        //don't set the orgBudget if the reason is not set (make sure that we set the reason first)
        if (!StringUtils.isEmpty(orgBdgtId) && StringUtils.isNotEmpty(getAdjustmentReasonId())) {
            editStockItemFacade.setOrgBudget(adjustmentHistory, orgBdgtId);
        }
    }

    public String getVendorName(int index) {
        return itemVendorAtIndex(index).getVendor().getExternalOrgDetail().getOrgName();
    }

    // not required
    public String getVendorCatalogNbr(int index) {
        return itemVendorAtIndex(index).getVendorCatalogNbr();
    }

    //not required
    public void setVendorCatalogNbr(int index, String value) {
        editStockItemFacade.setVendorCatalogNbr(itemVendorAtIndex(index), value, username);
    }

    //required
    public String getBuyUnitId(int index) {
        return itemVendorAtIndex(index).getBuyUnit().getUnitId().toString();
    }

    //required
    public void setBuyUnitId(int index, String unitId) {
        try {
            editStockItemFacade.setVendorBuyUnit(itemVendorAtIndex(index), unitId, username);
        } catch (NotificationException e) {
            printExceptions(e);
        }
    }

    //not required
    public String getBuyUnitCost(int index) {
        return itemVendorAtIndex(index).getBuyUnitCost() != null ? itemVendorAtIndex(index).getBuyUnitCost().toString() : null;
    }

    //not required
    public void setBuyUnitCost(int index, String cost) {
        try {
            editStockItemFacade.setVendorBuyUnitCost(itemVendorAtIndex(index), cost, username);
        } catch (NotificationException e) {
            printExceptions(e);
        }
    }

    private void printExceptions(NotificationException e) {
        for (Notification.Error error : e.getNotification().getErrors()) {
            messages.add(error.getProperty(), new ActionMessage("errors.custom", error.getMessage()));
        }
    }

    //not required
    public String getDiscount(int index) {
        return itemVendorAtIndex(index).getDiscount() != null ?
                itemVendorAtIndex(index).getDiscount().toString() : null;
    }

    //not required
    public void setDiscount(int index, String discount) {
        try {
            editStockItemFacade.setVendorDiscount(itemVendorAtIndex(index), discount, username);
        } catch (NotificationException e) {
            printExceptions(e);
        }
    }

    //required
    public void setDispenseUnitsPerBuyUnits(int index, String dispUnitsPerBuyUnit) {
        try {
            editStockItemFacade.setVendorDispenseUnitPerBuyUnit(itemVendorAtIndex(index), dispUnitsPerBuyUnit, username);
        } catch (NotificationException e) {
            printExceptions(e);
        }
    }

    public String getDispenseUnitsPerBuyUnits(int index) {
        return itemVendorAtIndex(index).getDispenseUnitsPerBuyUnit() != null ?
                itemVendorAtIndex(index).getDispenseUnitsPerBuyUnit().toString() : null;
    }

    public String getPrimaryVendorKey() {
        for (int i = 0; i < stockItem.getItemVendorsAsArray().length; i++) {
            ItemVendor itemVendor = itemVendorAtIndex(i);
            if (itemVendor.getPrimaryVendor()) return i + "";
        }
        return null;
    }


    /**
     * @param key - the index in the itemVendorAsArray
     */
    public void setPrimaryVendorKey(String key) {
        try {
            editStockItemFacade.setPrimaryVendor(stockItem, itemVendorAtIndex(Integer.parseInt(key)), username);
        } catch (NotificationException e) {
            printExceptions(e);
        }
    }

    public String getUpdateInfo(int index) {
        ItemVendor itemVendor = itemVendorAtIndex(index);
        if (StringUtils.isEmpty(itemVendor.getUpdatedBy())) return "N/A";
        User user1 = editStockItemFacade.getUser(itemVendor.getUpdatedBy());
        //todo - when an employee leaves MDH, their nds_user_id in admin_db.person table is removed so the previous getUser() returns null
        // The person record still exists in the person table so if the person_id was stored instead of the login-id, then the first and
        // last names could still be retrieved so this would be a potential change/enhancement.
        // This is causing a 500 error to be thrown on the Maintain Inventory - Vendor Info page, so will return the stored login-id if
        // it has been removed from the person table
        String nameToDisplay = user1 != null ? user1.getFirstAndLastName() : itemVendor.getUpdatedBy();
        return nameToDisplay + " " + (itemVendor.getUpdateDate() != null ? DateUtils.toString(itemVendor.getUpdateDate()) : "");
    }

    private ItemVendor itemVendorAtIndex(int index) {
        return ((ItemVendor) stockItem.getItemVendorsAsArray()[index]);
    }


    public StockQtyAdjustmentHistory getAdjustmentHistory() {
        return adjustmentHistory;
    }


    void setAdjustmentHistory(StockQtyAdjustmentHistory adjustmentHistory) {
        this.adjustmentHistory = adjustmentHistory;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
