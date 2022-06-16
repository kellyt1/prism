package us.mn.state.health.model.legacySystem.stockItemMaterialRequests;

import java.sql.Timestamp;

/**
 * THIS CLASS REPRESENTS THE REQUESTLINEITEM
 */
public class OrdItem {
    public static final String ORDNBR_COLUMN = "ORDNBR";
    public static final String LOCATION_COLUMN = "LOC";
    public static final String QUANTITY_COLUMN = "QTY";
    public static final String QUANTITYRECEIVED_COLUMN = "QTY_REC";
    public static final String UNIT_COLUMN = "UNIT";
    public static final String FILLED_COLUMN = "FILLED";
    public static final String DATEFILL_COLUMN = "DATEFILL";
    public static final String BACKORDER_COLUMN = "BO";
    public static final String CANCEL_COLUMN = "CANCEL";
    public static final String ICNBR_COLUMN = "ICNBR";

    private String ordNbr;
    private String location;
    private String quantity;
    private String quantityReceived;
    private String unit;
    private boolean filled;
    private Timestamp dateFill;
    private boolean backOrder;
    private boolean cancel;
    private String icnbr;

    public String getOrdNbr() {
        return ordNbr;
    }

    public void setOrdNbr(String ordNbr) {
        this.ordNbr = ordNbr;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityReceived() {
        return quantityReceived;
    }

    public void setQuantityReceived(String quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public Timestamp getDateFill() {
        return dateFill;
    }

    public void setDateFill(Timestamp dateFill) {
        this.dateFill = dateFill;
    }

    public boolean isBackOrder() {
        return backOrder;
    }

    public void setBackOrder(boolean backOrder) {
        this.backOrder = backOrder;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public String getIcnbr() {
        return icnbr;
    }

    public void setIcnbr(String icnbr) {
        this.icnbr = icnbr;
    }


    public String toString() {
        return "OrdItem{" +
                "ordNbr='" + ordNbr + "'" +
                ", filled=" + filled +
                ", dateFill=" + dateFill +
                ", backOrder=" + backOrder +
                ", cancel=" + cancel +
                ", icnbr='" + icnbr + "'" +
                ", unit='" + unit + "'" +
                ", quantity='" + quantity + "'" +
                ", quantityReceived='" + quantityReceived + "'" +
                "}";
    }
}
