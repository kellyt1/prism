package us.mn.state.health.model.inventory;

import java.util.Date;

import org.apache.commons.lang.ObjectUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.model.common.ModelMember;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.util.HistoryUtils;

public class StockItemHistory extends ModelMember {

    private Long stockItemHistoryId;

    private StockItem stockItem;
    private String description;
    private String category;
    private String icnbr;
    private String hazardous;
    private String reorderPoint;
    private String reorderQty;
    private String safetyStock;
    private String cycleCountPriority;
    private String estimatedAnnualUsage;
    private String reviewDate;
    private Boolean seasonal;
    private String dispenseUnit;
    private String orgBudget;
    private Person primaryContact;
    private Person secondaryContact;
    private String status;
    private String holdUntilDate;
    private Boolean fillUntilDepleted;
    private String instructions;
    private String historyComments;


    public static StockItemHistory createStockItemHistory(StockItem oldStockItem, StockItem newStockItem, String newHistoryComment, String user) {
        StockItemHistory itemHistory = new StockItemHistory();
        itemHistory.setInsertedBy(user);
        itemHistory.setInsertionDate(new Date());
        itemHistory.stockItem = newStockItem;

        boolean isChanged = false;

        //Want to be able to save a new History Comment record even if the new comment is the only
        //thing that has been added and no changes have been made to the stock item.
        if (!(newHistoryComment == null) && !(newHistoryComment.equalsIgnoreCase(""))) {
            isChanged = true;
            itemHistory.setHistoryComments(newHistoryComment);
        }

        if (!ObjectUtils.equals(oldStockItem.getDescription(), newStockItem.getDescription())) {
            isChanged = true;
            itemHistory.description = oldStockItem.getDescription();
        }

        if (!ObjectUtils.equals(oldStockItem.getCategory(), newStockItem.getCategory())) {
            isChanged = true;
            itemHistory.category = oldStockItem.getCategory().getCodeAndName();
        }

        if (!ObjectUtils.equals(oldStockItem.getIcnbr(), newStockItem.getIcnbr())) {
            isChanged = true;
            itemHistory.icnbr = oldStockItem.getFullIcnbr();
        }

        if (!ObjectUtils.equals(oldStockItem.getHazardousObject(), newStockItem.getHazardousObject())) {
            isChanged = true;
            itemHistory.hazardous = HistoryUtils.getStringValue(oldStockItem, "hazardousObject.description");
        }

        if (!ObjectUtils.equals(oldStockItem.getReorderPoint(), newStockItem.getReorderPoint())) {
            isChanged = true;
            itemHistory.reorderPoint = HistoryUtils.getStringValue(oldStockItem, "reorderPoint");
        }

        if (!ObjectUtils.equals(oldStockItem.getReorderQty(), newStockItem.getReorderQty())) {
            isChanged = true;
//            itemHistory.reorderQty = oldStockItem.getReorderQty();
            itemHistory.reorderQty = HistoryUtils.getStringValue(oldStockItem, "reorderQty");
        }

        if (!ObjectUtils.equals(oldStockItem.getSafetyStock(), newStockItem.getSafetyStock())) {
            isChanged = true;
            itemHistory.safetyStock = HistoryUtils.getStringValue(oldStockItem, "safetyStock");
        }

        if (!ObjectUtils.equals(oldStockItem.getCycleCountPriorityObject(), newStockItem.getCycleCountPriorityObject())) {
            isChanged = true;
            itemHistory.cycleCountPriority = HistoryUtils.getStringValue(oldStockItem, "cycleCountPriorityObject.code");
        }

        if (!ObjectUtils.equals(oldStockItem.getEstimatedAnnualUsage(), newStockItem.getEstimatedAnnualUsage())) {
            isChanged = true;
            itemHistory.estimatedAnnualUsage = HistoryUtils.getStringValue(oldStockItem, "estimatedAnnualUsage");
        }

        if (!ObjectUtils.equals(DateUtils.toString(oldStockItem.getReviewDate()), DateUtils.toString(newStockItem.getReviewDate()))) {
            isChanged = true;
            itemHistory.reviewDate = HistoryUtils.getDateValue(oldStockItem.getReviewDate());
        }

        if (!ObjectUtils.equals(oldStockItem.getSeasonal(), newStockItem.getSeasonal())) {
            isChanged = true;
            itemHistory.seasonal = oldStockItem.getSeasonal();
        }

        if (!ObjectUtils.equals(oldStockItem.getDispenseUnit(), newStockItem.getDispenseUnit())) {
            isChanged = true;
            itemHistory.dispenseUnit = oldStockItem.getDispenseUnit().getName();
        }

        if (!ObjectUtils.equals(oldStockItem.getOrgBudget(), newStockItem.getOrgBudget())) {
            isChanged = true;
            itemHistory.orgBudget = oldStockItem.getOrgBudget().getOrgBudgetCodeAndNameAndFY();
        }

        if (!ObjectUtils.equals(oldStockItem.getPrimaryContact().getPersonId(), newStockItem.getPrimaryContact().getPersonId())) {
            isChanged = true;
            itemHistory.primaryContact = oldStockItem.getPrimaryContact();
        }

        if (!ObjectUtils.equals(oldStockItem.getSecondaryContact().getPersonId(), newStockItem.getSecondaryContact().getPersonId())) {
            isChanged = true;
            itemHistory.secondaryContact = oldStockItem.getSecondaryContact();
        }

        if (!ObjectUtils.equals(oldStockItem.getStatus(), newStockItem.getStatus())) {
            isChanged = true;
            itemHistory.status = oldStockItem.getStatus().getName();
        }

        if (!ObjectUtils.equals(DateUtils.toString(oldStockItem.getHoldUntilDate()), DateUtils.toString(newStockItem.getHoldUntilDate()))) {
            isChanged = true;
            itemHistory.holdUntilDate = HistoryUtils.getDateValue(oldStockItem.getHoldUntilDate());
        }

        if (!ObjectUtils.equals(oldStockItem.getFillUntilDepleted(), newStockItem.getFillUntilDepleted())) {
            isChanged = true;
            itemHistory.fillUntilDepleted = oldStockItem.getFillUntilDepleted();
        }
        if (!ObjectUtils.equals(oldStockItem.getInstructions(), newStockItem.getInstructions())) {
            isChanged = true;
            itemHistory.instructions = oldStockItem.getInstructions();
        }

        return isChanged ? itemHistory : null;
    }


    public void setStockItemHistoryId(Long stockItemHistoryId) {
        this.stockItemHistoryId = stockItemHistoryId;
    }

    public Long getStockItemHistoryId() {
        return stockItemHistoryId;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getIcnbr() {
        return icnbr;
    }

    public String getHazardous() {
        return hazardous;
    }

    public String getReorderPoint() {
        return reorderPoint;
    }

    public String getReorderQty() {
        return reorderQty;
    }

    public String getSafetyStock() {
        return safetyStock;
    }

    public String getCycleCountPriority() {
        return cycleCountPriority;
    }

    public String getEstimatedAnnualUsage() {
        return estimatedAnnualUsage;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public Boolean getSeasonal() {
        return seasonal;
    }

    public String getDispenseUnit() {
        return dispenseUnit;
    }

    public String getOrgBudget() {
        return orgBudget;
    }

    public Person getPrimaryContact() {
        return primaryContact;
    }

    public Person getSecondaryContact() {
        return secondaryContact;
    }

    public String getStatus() {
        return status;
    }

    public String getHoldUntilDate() {
        return holdUntilDate;
    }

    public Boolean getFillUntilDepleted() {
        return fillUntilDepleted;
    }


    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIcnbr(String icnbr) {
        this.icnbr = icnbr;
    }

    public void setHazardous(String hazardous) {
        this.hazardous = hazardous;
    }

    public void setReorderPoint(String reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public void setReorderQty(String reorderQty) {
        this.reorderQty = reorderQty;
    }

    public void setSafetyStock(String safetyStock) {
        this.safetyStock = safetyStock;
    }

    public void setCycleCountPriority(String cycleCountPriority) {
        this.cycleCountPriority = cycleCountPriority;
    }

    public void setEstimatedAnnualUsage(String estimatedAnnualUsage) {
        this.estimatedAnnualUsage = estimatedAnnualUsage;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setSeasonal(Boolean seasonal) {
        this.seasonal = seasonal;
    }

    public void setDispenseUnit(String dispenseUnit) {
        this.dispenseUnit = dispenseUnit;
    }

    public void setOrgBudget(String orgBudget) {
        this.orgBudget = orgBudget;
    }

    public void setPrimaryContact(Person primaryContact) {
        this.primaryContact = primaryContact;
    }

    public void setSecondaryContact(Person secondaryContact) {
        this.secondaryContact = secondaryContact;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHoldUntilDate(String holdUntilDate) {
        this.holdUntilDate = holdUntilDate;
    }

    public void setFillUntilDepleted(Boolean fillUntilDepleted) {
        this.fillUntilDepleted = fillUntilDepleted;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }


    public String getHistoryComments() {
        return historyComments;
    }

    public void setHistoryComments(String historyComments) {
        this.historyComments = historyComments;
    }
}
