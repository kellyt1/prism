package us.mn.state.health.domain.repository.materialsrequest;

import java.io.Serializable;

import us.mn.state.health.util.hibernate.SearchCriteria;

public class RequestSearchCriteria extends SearchCriteria implements Serializable {
    public static final String IN_STOCK = "1";
    public static final String OUT_OF_STOCK = "0";
    public static final String[] STOCK_ITEM_ON_STOCK_STATUSES = {IN_STOCK, OUT_OF_STOCK};

    public static final int SORT_BY_REQUEST_ID = 0;
    public static final int SORT_BY_DATE_REQUESTED = 1;
    public static final int SORT_BY_REQUESTOR = 2;
    public static final int SORT_BY_NEED_BY_DATE = 3;
    public static final int SORT_BY_PRIORITY = 4;

    private int sortBy = SORT_BY_REQUEST_ID;

    private String dateRequestedFrom;
    private String dateRequestedTo;
    private String needByDateFrom;
    private String needByDateTo;
    private String trackingNumber;

    private String orgBdgt;
    private String statusId;
    private String vendorName;
    private String itemDescription;
    private String itemModel;
    private String categoryId;
    private String requestorId;
    private String fullIcnbr;
    private String priorityId;
    private String facilityId;
    private String onStock = IN_STOCK;


    public boolean isFullIcnbrSpecified() {
        return isSpecified(fullIcnbr);
    }

    public boolean isPrioritySpecified() {
        return isSpecified(priorityId);
    }

    public boolean isFacilitySpecified() {
        return isSpecified(facilityId);
    }

    public boolean isOrgBdgtSpecified() {
        return isSpecified(orgBdgt);
    }

    public boolean isStatusIdSpecified() {
        return isSpecified(statusId);
    }

    public boolean isVendorNameSpecified() {
        return isSpecified(vendorName);
    }

    public boolean isItemDescriptionSpecified() {
        return isSpecified(itemDescription);
    }

    public boolean isItemModelSpecified() {
        return isSpecified(itemModel);
    }

    public boolean isCategoryIdSpecified() {
        return isSpecified(categoryId);
    }

    public boolean isRequestorIdSpecified() {
        return isSpecified(requestorId);
    }

    public boolean isTrackingNumberSpecified() {
        return isSpecified(trackingNumber);
    }

    public boolean isDateRequestedFromSpecified() {
        return isSpecified(dateRequestedFrom);
    }

    public boolean isDateRequestedToSpecified() {
        return isSpecified(dateRequestedTo);
    }

    public boolean isNeedByDateFromSpecified() {
        return isSpecified(needByDateFrom);
    }

    public boolean isNeedByDateToSpecified() {
        return isSpecified(needByDateTo);
    }


    public String getOrgBdgt() {
        return orgBdgt;
    }

    public void setOrgBdgt(String orgBdgt) {
        this.orgBdgt = orgBdgt;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemModel() {
        return itemModel;
    }

    public void setItemModel(String itemModel) {
        this.itemModel = itemModel;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

    public String getNeedByDateFrom() {
        return needByDateFrom;
    }

    public void setNeedByDateFrom(String needByDateFrom) {
        this.needByDateFrom = needByDateFrom;
    }

    public String getNeedByDateTo() {
        return needByDateTo;
    }

    public void setNeedByDateTo(String needByDateTo) {
        this.needByDateTo = needByDateTo;
    }

    public String getDateRequestedTo() {
        return dateRequestedTo;
    }

    public void setDateRequestedTo(String dateRequestedTo) {
        this.dateRequestedTo = dateRequestedTo;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    public void setDateRequestedFrom(String date) {
        this.dateRequestedFrom = date;
    }


    public String getDateRequestedFrom() {
        return dateRequestedFrom;
    }

    public String getFullIcnbr() {
        return fullIcnbr;
    }

    public void setFullIcnbr(String fullIcnbr) {
        this.fullIcnbr = fullIcnbr;
    }

    public String getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(String priorityId) {
        this.priorityId = priorityId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }


    public String getOnStock() {
        return onStock;
    }

    public void setOnStock(String onStock) {
        this.onStock = onStock;
    }

}
