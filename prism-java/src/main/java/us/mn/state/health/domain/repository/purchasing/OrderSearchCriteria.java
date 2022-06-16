package us.mn.state.health.domain.repository.purchasing;

import java.io.Serializable;

import us.mn.state.health.util.hibernate.SearchCriteria;

public class OrderSearchCriteria extends SearchCriteria implements Serializable {

    //no orderLineItem or requestLineItem
    public static final String EMPTY_ORDERS = "0";
    public static final String NON_EMPTY_ORDERS = "1";

    public static final String[] ORDER_STATUSES = {EMPTY_ORDERS, NON_EMPTY_ORDERS};

    public static final int SORT_BY_ORDER_ID = 0;
    public static final int SORT_BY_PO_NO = 1;
    public static final int SORT_BY_VENDOR = 2;
    public static final int SORT_BY_BUYER = 3;
    public static final int SORT_BY_SUSPENSE_DATE = 4;

    private int sortBy = SORT_BY_ORDER_ID;

    private String oprNo;
    private String poNo;
    private String buyerId;
    private String itemDescription;
    private String vendorName;
    private String requestTrackingNo;
    private String orderedFrom;
    private String orderedTo;
    private String itemModel;
    private String statusId;
    private String requestorId;
    private String stockItemLocationId;
    private String orgCode;
    private String suspenseDateFrom;
    private String suspenseDateTo;
    private String icnbr;
    private String shippingAddressId;


    public String getIcnbr() {
        return icnbr;
    }

    public void setIcnbr(String icnbr) {
        this.icnbr = icnbr;
    }

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    public String getOprNo() {
        return oprNo;
    }

    public void setOprNo(String oprNo) {
        this.oprNo = oprNo;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getRequestTrackingNo() {
        return requestTrackingNo;
    }

    public void setRequestTrackingNo(String requestTrackingNo) {
        this.requestTrackingNo = requestTrackingNo;
    }

    public String getOrderedFrom() {
        return orderedFrom;
    }

    public void setOrderedFrom(String orderedFrom) {
        this.orderedFrom = orderedFrom;
    }

    public String getOrderedTo() {
        return orderedTo;
    }

    public void setOrderedTo(String orderedTo) {
        this.orderedTo = orderedTo;
    }

    public String getItemModel() {
        return itemModel;
    }

    public void setItemModel(String itemModel) {
        this.itemModel = itemModel;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

    public String getStockItemLocationId() {
        return stockItemLocationId;
    }

    public void setStockItemLocationId(String stockItemLocationId) {
        this.stockItemLocationId = stockItemLocationId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getSuspenseDateFrom() {
        return suspenseDateFrom;
    }

    public void setSuspenseDateFrom(String suspenseDateFrom) {
        this.suspenseDateFrom = suspenseDateFrom;
    }

    public String getSuspenseDateTo() {
        return suspenseDateTo;
    }

    public void setSuspenseDateTo(String suspenseDateTo) {
        this.suspenseDateTo = suspenseDateTo;
    }


    public String getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(String shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }
}
