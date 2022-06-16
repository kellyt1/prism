package us.mn.state.health.view.materialsrequest.reports;

import java.util.Date;

import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.Item;

public class StockItemInTransaction {
    private StockItem stockItem;
    private String trackingNumber;
    private String itemDescription;
    private String dispenseUnit;
    private Double dispUnitCost;
    private Date dateRequested;
    private Integer qtyRequested;
    private String requestorFirstName;
    private String requestorLastName;
    private Long requestLineItemId;
    private String orgCode;
    private String orgCodeDisplay;
    private String statusName;
    private Boolean isOnOrder;

    public StockItemInTransaction(Item stockItem
            , String trackingNumber
            , String itemDescription
            , String dispenseUnit
            , Double dispUnitCost
            , Date dateRequested
            , Integer qtyRequested
            , String requestorFirstName
            , String requestorLastName
            , Long requestLineItemId
            , String statusName
            , Boolean isOnOrder) {
        this.stockItem = (StockItem) stockItem;
        this.trackingNumber = trackingNumber;
        this.itemDescription = itemDescription;
        this.dispenseUnit = dispenseUnit;
        this.dispUnitCost = dispUnitCost;
        this.dateRequested = dateRequested;
        this.qtyRequested = qtyRequested;
        this.requestorFirstName = requestorFirstName;
        this.requestorLastName = requestorLastName;
        this.requestLineItemId = requestLineItemId;
        this.orgCode = ((StockItem) stockItem).getOrgBudget().getOrgBudgetCode();
        this.orgCodeDisplay = ((StockItem) stockItem).getOrgBudget().getOrgBudgetCodeDisplay();

        this.statusName = statusName;
        this.isOnOrder = isOnOrder;
    }

    public Boolean getOnOrder() {
        return isOnOrder;
    }

    public void setOnOrder(Boolean onOrder) {
        isOnOrder = onOrder;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getDispenseUnit() {
        return dispenseUnit;
    }

    public void setDispenseUnit(String dispenseUnit) {
        this.dispenseUnit = dispenseUnit;
    }

    public Double getDispUnitCost() {
        return dispUnitCost;
    }

    public void setDispUnitCost(Double dispUnitCost) {
        this.dispUnitCost = dispUnitCost;
    }

    public Date getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(Date dateRequested) {
        this.dateRequested = dateRequested;
    }

    public Integer getQtyRequested() {
        return qtyRequested;
    }

    public void setQtyRequested(Integer qtyRequested) {
        this.qtyRequested = qtyRequested;
    }

    public String getRequestorFirstName() {
        return requestorFirstName;
    }

    public void setRequestorFirstName(String requestorFirstName) {
        this.requestorFirstName = requestorFirstName;
    }

    public String getRequestorLastName() {
        return requestorLastName;
    }

    public void setRequestorLastName(String requestorLastName) {
        this.requestorLastName = requestorLastName;
    }

    public Long getRequestLineItemId() {
        return requestLineItemId;
    }

    public void setRequestLineItemId(Long requestLineItemId) {
        this.requestLineItemId = requestLineItemId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public String getOrgCodeDisplay() {
        return orgCodeDisplay;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getRequestorLastFirstName(){
        return requestorLastName + " " + requestorFirstName;
    }

    public Double getTotalAmount(){
        double qtyReq = qtyRequested.doubleValue();
        double duCost = dispUnitCost.doubleValue();
        double value = (Math.round(qtyReq * 100 * duCost)+0.00d) / 100;
        return new Double(value);
    }

    public String getFullIcnbr(){
        return stockItem.getFullIcnbr();
    }
}
