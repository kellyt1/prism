package us.mn.state.health.view.materialsrequest.reports;

import java.util.Date;

public class PurchaseItemTransaction {

    private Long requestLineItemId;
    private String itemDescription;
    private String trackingNumber;
    private String categoryName;
    private Double estAmountPaid;
    private String orgCode;
    private String orgCodeDisplay;
    private Long orderId;

    private Date dateRequested;
    private Date dateOrdered;

    private Integer qtyRequested;
    private Integer qtyOrdered;

    private String buyUnit;
    private Double buyUnitCost;


    private String vendorName;
    private String requestorFirstName;
    private String requestorLastName;

    private String statusName;

    public PurchaseItemTransaction(Long requestLineItemId
            , String itemDescription
            , String trackingNumber
            , String categoryName
            , Double estAmountPaid
            , String orgCode
            , Long orderId
            , Date dateRequested
            , Date dateOrdered
            , Integer qtyRequested
            , Integer qtyOrdered
            , String buyUnit
            , Double buyUnitCost
            , String vendorName
            , String requestorFirstName
            , String requestorLastName
            , String statusName) {
        this.requestLineItemId = requestLineItemId;
        this.itemDescription = itemDescription;
        this.trackingNumber = trackingNumber;
        this.categoryName = categoryName;
        this.estAmountPaid = estAmountPaid;
        this.orgCode = orgCode;
        this.orderId = orderId;
        this.dateRequested = dateRequested;
        this.dateOrdered = dateOrdered;
        this.qtyRequested = qtyRequested;
        this.qtyOrdered = qtyOrdered;
        this.buyUnit = buyUnit;
        this.buyUnitCost = buyUnitCost;
        this.vendorName = vendorName;
        this.requestorFirstName = requestorFirstName;
        this.requestorLastName = requestorLastName;
        this.statusName = statusName;
    }
    public PurchaseItemTransaction(Long requestLineItemId
            , String itemDescription
            , String trackingNumber
            , String categoryName
            , Double estAmountPaid
            , String orgCode
            , String orgCodeDisplay
            , Long orderId
            , Date dateRequested
            , Date dateOrdered
            , Integer qtyRequested
            , Integer qtyOrdered
            , String buyUnit
            , Double buyUnitCost
            , String vendorName
            , String requestorFirstName
            , String requestorLastName
            , String statusName) {
        this.requestLineItemId = requestLineItemId;
        this.itemDescription = itemDescription;
        this.trackingNumber = trackingNumber;
        this.categoryName = categoryName;
        this.estAmountPaid = estAmountPaid;
        this.orgCode = orgCode;
        this.orgCodeDisplay = orgCodeDisplay;
        this.orderId = orderId;
        this.dateRequested = dateRequested;
        this.dateOrdered = dateOrdered;
        this.qtyRequested = qtyRequested;
        this.qtyOrdered = qtyOrdered;
        this.buyUnit = buyUnit;
        this.buyUnitCost = buyUnitCost;
        this.vendorName = vendorName;
        this.requestorFirstName = requestorFirstName;
        this.requestorLastName = requestorLastName;
        this.statusName = statusName;
    }

    public Long getRequestLineItemId() {
        return requestLineItemId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Double getEstAmountPaid() {
        return estAmountPaid;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public String getOrgCodeDisplay() {
        return orgCodeDisplay;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Date getDateRequested() {
        return dateRequested;
    }

    public Date getDateOrdered() {
        return dateOrdered;
    }

    public Integer getQtyRequested() {
        return qtyRequested;
    }

    public Integer getQtyOrdered() {
        return qtyOrdered;
    }

    public String getBuyUnit() {
        return buyUnit;
    }

    public Double getBuyUnitCost() {
        return buyUnitCost;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getRequestorFirstName() {
        return requestorFirstName;
    }

    public String getRequestorLastName() {
        return requestorLastName;
    }

    public String getStatusName() {
        return statusName;
    }

    public Double getTotalAmountPaid(){
        double qtyReq = qtyRequested.doubleValue();
        double duCost = buyUnitCost.doubleValue();
        double value = (Math.round(qtyReq * 100 * duCost)+0.00d) / 100;
        return new Double(value);
    }

    public String getRequestorLastFirstName(){
        return requestorLastName + " " + requestorFirstName;
    }

}
