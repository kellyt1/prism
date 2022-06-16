package us.mn.state.health.model.inventory;

import us.mn.state.health.model.common.ModelMember;
import us.mn.state.health.model.common.VendorContract;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.common.User;
import us.mn.state.health.dao.UserDAO;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ItemVendorLinkHistory extends ModelMember implements java.io.Serializable {
    /**
     *
     */
   // private static final long serialVersionUID = -2781057171798110759L;
    private Long itemVendorLinkHistoryId;
    //private ItemVendor itemVendor;
    
    private VendorContract vendorContract;
    private Vendor vendor;
    private StockItem stockItem;
//    private int version;
    //    private Date insertionDate;
    private Unit buyUnit;
    private Date insertionDate;
    private String insertedBy;
    private String vendorCatalogNbr = "";
    private Double buyUnitCost;
    private Boolean primaryVendor;
    private Double discount; //discount is in percents 0-100
    private Integer dispenseUnitsPerBuyUnit;
    private Date createDate;
    public static Log log = LogFactory.getLog(ItemVendorLinkHistory.class);
    /**
     * No-arg constructor for JavaBean tools.
     */
    public ItemVendorLinkHistory() {
    }

    /** minimal constructor */
    public ItemVendorLinkHistory(Long itemVendorLinkHistoryId) {
        this.itemVendorLinkHistoryId = itemVendorLinkHistoryId;
    }

    /** full constructor */
    public ItemVendorLinkHistory(Long itemVendorLinkHistoryId, Unit buyUnit,
            StockItem stockItem,
            VendorContract vendorContract,
            Vendor vendor,
            Date insertionDate,
            String insertedBy, String vendorCatalogNbr, Double buyUnitCost,
            Boolean primaryVendor, Double discount, String lastUpdatedBy,
            Date lastUpdatedDate, int dispenseUnitsPerBuyUnit, Date createDate) {
        this.itemVendorLinkHistoryId = itemVendorLinkHistoryId;
        this.buyUnit = buyUnit;
//        this.itemVendor = itemVendor;
        this.stockItem = stockItem;
        this.vendorContract = vendorContract;
        this.vendor = vendor;
        this.insertionDate = insertionDate;
        this.insertedBy = insertedBy;
        this.vendorCatalogNbr = vendorCatalogNbr;
        this.buyUnitCost = buyUnitCost;
        this.primaryVendor = primaryVendor;
        this.discount = discount;
        this.updatedBy = lastUpdatedBy;
        this.updateDate = lastUpdatedDate;
        this.dispenseUnitsPerBuyUnit = dispenseUnitsPerBuyUnit;
        this.createDate = createDate;
    }

    public Long getItemVendorLinkHistoryId() {
        return itemVendorLinkHistoryId;
    }

    public void setItemVendorLinkHistoryId(Long itemVendorLinkHistoryId) {
        this.itemVendorLinkHistoryId = itemVendorLinkHistoryId;
    }

//    public ItemVendor getItemVendor() {
//        return itemVendor;
//    }

//    public void setItemVendor(ItemVendor itemVendor) {
//        this.itemVendor = itemVendor;
//    }

    public VendorContract getVendorContract() {
        return vendorContract;
    }

    public void setVendorContract(VendorContract vendorContract) {
        this.vendorContract = vendorContract;
    }

    public Vendor getVendor() {
        return vendor;
    }

    protected void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    protected void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    public Unit getBuyUnit() {
        return buyUnit;
    }

    public void setBuyUnit(Unit buyUnit) {
        this.buyUnit = buyUnit;
    }

    public Date getcreateDate() {
        return createDate;
    }

    public void setcreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getDispenseUnitsPerBuyUnit() {
        return dispenseUnitsPerBuyUnit;
    }

    public void setDispenseUnitsPerBuyUnit(Integer dispenseUnitsPerBuyUnit) {
        this.dispenseUnitsPerBuyUnit = dispenseUnitsPerBuyUnit;
    }

    public void setVendorCatalogNbr(String vendorCatalogNbr) {
        this.vendorCatalogNbr = vendorCatalogNbr;
    }

    public String getVendorCatalogNbr() {
        return vendorCatalogNbr;
    }

    public Double getBuyUnitCost() {
        return buyUnitCost;
    }

    public void setBuyUnitCost(Double buyUnitCost) {
        this.buyUnitCost = buyUnitCost;
    }

    public Boolean getPrimaryVendor() {
        return primaryVendor;
    }

    public void setPrimaryVendor(Boolean primaryVendor) {
        this.primaryVendor = primaryVendor;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    /**
     * @return
     * @deprecated
     */
    public String getLastUpdatedBy() {
        return getUpdatedBy();
    }

    /**
     * @param lastUpdatedBy
     * @deprecated
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        setUpdatedBy(lastUpdatedBy);
    }

    /**
     * @return
     * @deprecated
     */
    public Date getLastUpdatedDate() {
        return getUpdateDate();
    }

    /**
     * @param lastUpdatedDate
     * @deprecated
     */
    public void setLastUpdatedDate(Date lastUpdatedDate) {
        setUpdateDate(lastUpdatedDate);
    }

    public String getLastUpdatedByUser() {
        try {
            UserDAO userDAO = DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getUserDAO();
            User userByUsername = userDAO.findUserByUsername(updatedBy);
            if (userByUsername != null) {
                return userByUsername.getFirstAndLastName();
            } else {
                return "N/A";
            }
        } catch (InfrastructureException e) {
            log.error(e);
        }
        return "N/A";
    }

    public String getDispenseUnitsPerBuyUnitHistoryAsString() {
        if (dispenseUnitsPerBuyUnit == null) {
            return  "";
        }
        else if (dispenseUnitsPerBuyUnit < 1) {
            return "Empty";
    }
        else {
            return dispenseUnitsPerBuyUnit.toString();
        }
    }

    public String getBuyUnitCostHistoryAsString() {
        if (buyUnitCost == null) {
            return  "";
        }
        else if (buyUnitCost < 1) {
            return "Empty";
        }
        else {
            return buyUnitCost.toString();
        }
    }

    public String getDiscountHistoryAsString() {
        if (discount == null) {
            return  "";
        }
        else if (discount < 1) {
            return "Empty";
        }
        else {
            return discount.toString();
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemVendorLinkHistory that = (ItemVendorLinkHistory) o;

        if (buyUnit != null ? !buyUnit.equals(that.buyUnit) : that.buyUnit != null) return false;
        if (buyUnitCost != null ? !buyUnitCost.equals(that.buyUnitCost) : that.buyUnitCost != null) return false;
        if (!createDate.equals(that.createDate)) return false;
        if (discount != null ? !discount.equals(that.discount) : that.discount != null) return false;
        if (dispenseUnitsPerBuyUnit != null ? !dispenseUnitsPerBuyUnit.equals(that.dispenseUnitsPerBuyUnit) : that.dispenseUnitsPerBuyUnit != null)
            return false;
        if (insertedBy != null ? !insertedBy.equals(that.insertedBy) : that.insertedBy != null) return false;
        if (insertionDate != null ? !insertionDate.equals(that.insertionDate) : that.insertionDate != null)
            return false;
        if (!itemVendorLinkHistoryId.equals(that.itemVendorLinkHistoryId)) return false;
        if (primaryVendor != null ? !primaryVendor.equals(that.primaryVendor) : that.primaryVendor != null)
            return false;
        if (!stockItem.equals(that.stockItem)) return false;
        if (vendor != null ? !vendor.equals(that.vendor) : that.vendor != null) return false;
        if (vendorCatalogNbr != null ? !vendorCatalogNbr.equals(that.vendorCatalogNbr) : that.vendorCatalogNbr != null)
            return false;
        if (vendorContract != null ? !vendorContract.equals(that.vendorContract) : that.vendorContract != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = itemVendorLinkHistoryId.hashCode();
        result = 31 * result + (vendorContract != null ? vendorContract.hashCode() : 0);
        result = 31 * result + (vendor != null ? vendor.hashCode() : 0);
        result = 31 * result + stockItem.hashCode();
        result = 31 * result + (buyUnit != null ? buyUnit.hashCode() : 0);
        result = 31 * result + (insertionDate != null ? insertionDate.hashCode() : 0);
        result = 31 * result + (insertedBy != null ? insertedBy.hashCode() : 0);
        result = 31 * result + (vendorCatalogNbr != null ? vendorCatalogNbr.hashCode() : 0);
        result = 31 * result + (buyUnitCost != null ? buyUnitCost.hashCode() : 0);
        result = 31 * result + (primaryVendor != null ? primaryVendor.hashCode() : 0);
        result = 31 * result + (discount != null ? discount.hashCode() : 0);
        result = 31 * result + (dispenseUnitsPerBuyUnit != null ? dispenseUnitsPerBuyUnit.hashCode() : 0);
        result = 31 * result + createDate.hashCode();
        return result;
    }
}
