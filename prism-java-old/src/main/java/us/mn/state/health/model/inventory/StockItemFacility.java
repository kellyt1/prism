package us.mn.state.health.model.inventory;

import java.util.Collection;
import java.util.HashSet;

import us.mn.state.health.model.common.Facility;

/**
 * Facility Entity class
 *
 * @author Jason Stull, Shawn Flahave, Lucian Ochian
 */
public class StockItemFacility extends Facility {
    private Collection stockItemLocations = new HashSet();

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockItemFacility)) return false;

        final StockItemFacility facility = (StockItemFacility)o;
        if (!facilityName.equals(facility.facilityName)) return false;
        if (!facilityCode.equals(facility.facilityCode)) return false;
        if (!facilityType.equals(facility.facilityType)) return false;
        return true;
    }

    public int hashCode() {
        return 29 * facilityCode.hashCode() + facilityName.hashCode();
    }


    public void setStockItemLocations(Collection stockItemLocations) {
        this.stockItemLocations = stockItemLocations;
    }


    public Collection getStockItemLocations() {
        return stockItemLocations;
    }


    public String toString() {
        return super.toString();
    }
}