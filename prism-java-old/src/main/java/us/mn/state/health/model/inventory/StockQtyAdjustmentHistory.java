package us.mn.state.health.model.inventory;

import java.io.Serializable;
import java.util.Date;

import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.Person;

public class StockQtyAdjustmentHistory implements Serializable {
    private Long id;
    private StockItem stockItem;
    private Integer previousQtyOnHand;
    private Integer newQtyOnHand;
    private Person changedBy;
    private Date changeDate;
    private StockQtyChangeReasonRef changeReason;
    private OrgBudget orgBudget;


    StockQtyAdjustmentHistory() {
    }

    public static StockQtyAdjustmentHistory createStockQtyAdjustmentHistory(
            StockItem stockItem, Person requestor) {
        StockQtyAdjustmentHistory adjustmentHistory = new StockQtyAdjustmentHistory();
        adjustmentHistory.setChangeDate(new Date());
        adjustmentHistory.setChangedBy(requestor);
        adjustmentHistory.setStockItem(stockItem);
        adjustmentHistory.setPreviousQtyOnHand(stockItem.getQtyOnHand());
        adjustmentHistory.setNewQtyOnHand(stockItem.getQtyOnHand());
        return adjustmentHistory;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }


    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }


    public StockItem getStockItem() {
        return stockItem;
    }


    public void setPreviousQtyOnHand(Integer previousQtyOnHand) {
        this.previousQtyOnHand = previousQtyOnHand;
    }


    public Integer getPreviousQtyOnHand() {
        return previousQtyOnHand;
    }


    public void setChangedBy(Person changedBy) {
        this.changedBy = changedBy;
    }


    public Person getChangedBy() {
        return changedBy;
    }


    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }


    public Date getChangeDate() {
        return changeDate;
    }


    public void setChangeReason(StockQtyChangeReasonRef changeReason) {
        this.changeReason = changeReason;
    }


    public StockQtyChangeReasonRef getChangeReason() {
        return changeReason;
    }


    public void setOrgBudget(OrgBudget orgBudget) {
        this.orgBudget = orgBudget;
    }


    public OrgBudget getOrgBudget() {
        return orgBudget;
    }


    public void setNewQtyOnHand(Integer newQtyOnHand) {
        this.newQtyOnHand = newQtyOnHand;
    }


    public Integer getNewQtyOnHand() {
        return newQtyOnHand;
    }
}