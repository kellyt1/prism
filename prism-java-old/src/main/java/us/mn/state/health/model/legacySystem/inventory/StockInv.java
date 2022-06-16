package us.mn.state.health.model.legacySystem.inventory;

import java.util.Date;

/**
 * This class maps the STOCK_INV_TBL table
 */
public class StockInv {

    private Long stockInvId;
    private String icnbr;
    private String itemDescription;
    private String unit;
    private Integer ROP;
    private Integer ROQ;
    private String BNBR;
    private Long primaryContactId;
    private Long secondaryContactId;
    private String insertedBy;
    private Date insertionDate;
    private Date endDate;
    private String comments;
    private Long aContactId;
    private Date holdDate;
    private String reponseType; //reponse or response???
    private Boolean hazardous;
    private Boolean onOrder;
    private Boolean seasonal;
    private Integer packQty;
    private String VendorName;
    private Integer annualUsage;

    public Long getStockInvId() {
        return stockInvId;
    }

    public void setStockInvId(Long stockInvId) {
        this.stockInvId = stockInvId;
    }

    public String getIcnbr() {
        return icnbr;
    }

    public void setIcnbr(String icnbr) {
        this.icnbr = icnbr;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getROP() {
        return ROP;
    }

    public void setROP(Integer ROP) {
        this.ROP = ROP;
    }

    public Integer getROQ() {
        return ROQ;
    }

    public void setROQ(Integer ROQ) {
        this.ROQ = ROQ;
    }

    public String getBNBR() {
        return BNBR;
    }

    public void setBNBR(String BNBR) {
        this.BNBR = BNBR;
    }

    public Long getPrimaryContactId() {
        return primaryContactId;
    }

    public void setPrimaryContactId(Long primaryContactId) {
        this.primaryContactId = primaryContactId;
    }

    public Long getSecondaryContactId() {
        return secondaryContactId;
    }

    public void setSecondaryContactId(Long secondaryContactId) {
        this.secondaryContactId = secondaryContactId;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getaContactId() {
        return aContactId;
    }

    public void setaContactId(Long aContactId) {
        this.aContactId = aContactId;
    }

    public Date getHoldDate() {
        return holdDate;
    }

    public void setHoldDate(Date holdDate) {
        this.holdDate = holdDate;
    }

    public String getReponseType() {
        return reponseType;
    }

    public void setReponseType(String reponseType) {
        this.reponseType = reponseType;
    }

    public Boolean getHazardous() {
        return hazardous;
    }

    public void setHazardous(Boolean hazardous) {
        this.hazardous = hazardous;
    }

    public Boolean getOnOrder() {
        return onOrder;
    }

    public void setOnOrder(Boolean onOrder) {
        this.onOrder = onOrder;
    }

    public Boolean getSeasonal() {
        return seasonal;
    }

    public void setSeasonal(Boolean seasonal) {
        this.seasonal = seasonal;
    }

    public Integer getPackQty() {
        return packQty;
    }

    public void setPackQty(Integer packQty) {
        this.packQty = packQty;
    }

    public String getVendorName() {
        return VendorName;
    }

    public void setVendorName(String vendorName) {
        VendorName = vendorName;
    }

    public Integer getAnnualUsage() {
        return annualUsage;
    }

    public void setAnnualUsage(Integer annualUsage) {
        this.annualUsage = annualUsage;
    }

}
