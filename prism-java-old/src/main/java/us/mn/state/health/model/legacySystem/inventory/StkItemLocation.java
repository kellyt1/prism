package us.mn.state.health.model.legacySystem.inventory;

import java.io.Serializable;

public class StkItemLocation implements Serializable{
    private StkItemLocationId stkItemLocationId;

    public StkItemLocationId getStockItemLocationId() {
        return stkItemLocationId;
    }

    public void setStockItemLocationId(StkItemLocationId stkItemLocationId) {
        this.stkItemLocationId = stkItemLocationId;
    }


    public static class StkItemLocationId implements Serializable {
        private String icnbr;
        private String locationCode;

        public StkItemLocationId() {
        }

        public StkItemLocationId(String icnbr, String locationCode) {
            this.icnbr = icnbr;
            this.locationCode = locationCode;
        }

        public String getIcnbr() {
            return icnbr;
        }

        public String getLocationCode() {
            return locationCode;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final StkItemLocationId that = (StkItemLocationId) o;

            if (!icnbr.equals(that.icnbr)) return false;
            if (!locationCode.equals(that.locationCode)) return false;

            return true;
        }

        public int hashCode() {
            int result;
            result = icnbr.hashCode();
            result = 29 * result + locationCode.hashCode();
            return result;
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final StkItemLocation that = (StkItemLocation) o;

        if (!stkItemLocationId.equals(that.stkItemLocationId)) return false;

        return true;
    }

    public int hashCode() {
        return stkItemLocationId.hashCode();
    }

    public void setStkItemLocationId(StkItemLocationId stkItemLocationId) {
        this.stkItemLocationId = stkItemLocationId;
    }

    public StkItemLocationId getStkItemLocationId() {
        return stkItemLocationId;
    }
}
