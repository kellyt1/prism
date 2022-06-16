package us.mn.state.health.model.inventory;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;

import us.mn.state.health.model.common.ModelMember;

public class StockItemLocation extends ModelMember implements Serializable {
    private Long locationId;
    private String locationCode;
    private StockItemFacility facility;
    private StockItem stockItem;
    private Boolean isPrimary = Boolean.TRUE; //set to true, make sure we have at least one primary location

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationCode() {
        Calendar c1 = Calendar.getInstance();

        if (endDate == null || c1.before(endDate)) {
            return locationCode;
        } else {
            return null;
        }
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public StockItemFacility getFacility() {
        return facility;
    }

    public void setFacility(StockItemFacility facility) {
        this.facility = facility;
    }

    public String getFullBinLocation() {
        return getFacility().getFacilityName() + " ( " + locationCode + " )";
    }


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockItemLocation)) {
            return false;
        }

        final StockItemLocation that = (StockItemLocation) o;

        if (this.getLocationId() == null) {
            if (that.getLocationId() == null) {
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
            return this.getLocationId().equals(that.getLocationId());
        }
    }

    public int hashCode() {
        return 29 * facility.hashCode() + stockItem.hashCode();
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

}
