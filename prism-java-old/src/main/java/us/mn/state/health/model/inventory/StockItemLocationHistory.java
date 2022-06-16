package us.mn.state.health.model.inventory;

import us.mn.state.health.model.common.ModelMember;

import java.io.Serializable;

public class StockItemLocationHistory extends ModelMember implements Serializable {
    //private Long locationId;
    private Long locationHistoryId;
    private StockItemLocation location;
    private String locationCode;
    private StockItemFacility facility;
    private StockItem stockItem;
    private Boolean isPrimary = Boolean.TRUE; //set to true, make sure we have at least one primary location

    public StockItemLocationHistory() {
    }

    public void setLocationHistoryId(Long locationHistoryId) {
        this.locationHistoryId = locationHistoryId;
    }

    public Long getLocationHistoryId() {
        return locationHistoryId;
    }

    public void setLocation(StockItemLocation location) {
        this.location = location;
    }

    public StockItemLocation getLocation() {
        return location;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setFacility(StockItemFacility facility) {
        this.facility = facility;
    }

    public StockItemFacility getFacility() {
        return facility;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public String getFullBinLocation() {
        return getFacility().getFacilityName() + " ( " + locationCode + " )";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockItemLocationHistory)) {
            return false;
        }

        final StockItemLocationHistory that = (StockItemLocationHistory) o;

        if (this.getLocationHistoryId() == null) {
            if (that.getLocationHistoryId() == null) {
                //dig deeper, using comparison by value
                if (this.getFacility() != null && !this.getFacility().equals(that.getFacility())) {
                    return false;
                }

                if (this.getStockItem() != null && !this.getStockItem().equals(that.getStockItem())) {
                    return false;
                }

                if (this.getLocationCode() != null && !this.getLocationCode().equals(that.getLocationCode())) {
                    return false;
                }
                return true;
            } else {
                //if one ID is null, and the other is not null, they can't be the same
                return false;
            }
        } else {  //we know we can't get a NullPointerException now...
            return this.getLocationHistoryId().equals(that.getLocationHistoryId());
        }
    }

    public int hashCode() {
        return 29 * facility.hashCode() + stockItem.hashCode();
    }


}
