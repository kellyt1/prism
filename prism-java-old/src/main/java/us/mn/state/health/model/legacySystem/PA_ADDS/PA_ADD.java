package us.mn.state.health.model.legacySystem.PA_ADDS;

import java.sql.Timestamp;

/**
 * A bean class for the PA_ADDS records
 */
public class PA_ADD {
    public static final String STATUS_COLUMN = "STATUS";
    public static final String MNID_COLUMN = "MNID";
    public static final String BNBR_COLUMN = "BNBR";
    public static final String DATEIN_COLUMN = "DATEIN";
    public static final String VIDNBR_COLUMN = "VIDNBR";
    public static final String OBJ_COLUMN = "OBJ";
    public static final String QTY1_COLUMN = "QTY1";
    public static final String DESC1_COLUMN = "DESC1";
    public static final String CATNBR1_COLUMN = "CATNBR1";
    public static final String UNIT1_COLUMN = "UNIT1";
    public static final String UNITCOST1_COLUMN = "UNITCOST1";
    public static final String MEMO_COLUMN = "MEMO";
    public static final String MEMO2_COLUMN = "MEMO2";
    public static final String ORG_COLUMN = "ORG";
    public static final String PRIORITY_COLUMN = "PRIORITY";
    public static final String DISCOUNT_COLUMN = "DISC1";
    public static final String TOTAL_COST_COLUMN = "TOTCOST1";
    private String status;
    private String MNID;
    private String BNBR;
    private java.sql.Timestamp dateIn;
    private String VIDNBR;
    private String OBJ;
    private String quantity;
    private String description;
    private String vendorCatalogId;
    private String unit;
    private String unitCost;
    private String memo;
    private String memo2;
    private String orgBudgetCode;
    private String priority;
    private String discount;
    private String totalCost;

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getBNBR() {
        return BNBR;
    }

    public void setBNBR(String BNBR) {
        this.BNBR = BNBR;
    }

    public Timestamp getDateIn() {
        return dateIn;
    }

    public void setDateIn(Timestamp dateIn) {
        this.dateIn = dateIn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMemo2() {
        return memo2;
    }

    public void setMemo2(String memo2) {
        this.memo2 = memo2;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMNID() {
        return MNID;
    }

    public void setMNID(String MNID) {
        this.MNID = MNID;
    }

    public String getOBJ() {
        return OBJ;
    }

    public void setOBJ(String OBJ) {
        this.OBJ = OBJ;
    }

    public String getOrgBudgetCode() {
        return orgBudgetCode;
    }

    public void setOrgBudgetCode(String orgBudgetCode) {
        this.orgBudgetCode = orgBudgetCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(String unitCost) {
        this.unitCost = unitCost;
    }

    public String getVendorCatalogId() {
        return vendorCatalogId;
    }

    public void setVendorCatalogId(String vendorCatalogId) {
        this.vendorCatalogId = vendorCatalogId;
    }

    public String getVIDNBR() {
        return VIDNBR;
    }

    public void setVIDNBR(String VIDNBR) {
        this.VIDNBR = VIDNBR;
    }


    public String toString() {
        return "PA_ADD{" +
                "BNBR='" + BNBR + "'" +
                ", status='" + status + "'" +
                ", MNID='" + MNID + "'" +
                ", dateIn=" + dateIn +
                ", VIDNBR='" + VIDNBR + "'" +
                ", OBJ='" + OBJ + "'" +
                ", quantity='" + quantity + "'" +
                ", description='" + description + "'" +
                ", vendorCatalogId='" + vendorCatalogId + "'" +
                ", unit='" + unit + "'" +
                ", unitCost='" + unitCost + "'" +
                ", memo='" + memo + "'" +
                ", memo2='" + memo2 + "'" +
                ", orgBudgetCode='" + orgBudgetCode + "'" +
                "}" + "\n";
    }
}
