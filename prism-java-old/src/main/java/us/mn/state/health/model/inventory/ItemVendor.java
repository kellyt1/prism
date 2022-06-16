package us.mn.state.health.model.inventory;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.UserDAO;
import us.mn.state.health.model.common.ModelMember;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.common.VendorContract;


public class ItemVendor extends ModelMember implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -2781057171798110759L;
    private VendorContract vendorContract;
    private Vendor vendor;
    private Item item;
    private ItemVendorId itemVendorId = new ItemVendorId();
    private int version;
    //    private Date insertionDate;
    private Unit buyUnit;
    private String insertedBy;
    private String vendorCatalogNbr = "";
    private Double buyUnitCost;
    private Boolean primaryVendor;
    private Double discount = new Double(0); //discount is in percents 0-100

    public static Log log = LogFactory.getLog(ItemVendor.class);
    private Integer dispenseUnitsPerBuyUnit;

    public static class ItemVendorId implements Serializable {
        private Long vendorId;
        private Long itemId;
        private Date insertionDate;

        ItemVendorId() {
        }

        public ItemVendorId(Long vendorId, Long itemId) {
            this.vendorId = vendorId;
            this.itemId = itemId;
            this.insertionDate = new Date();
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ItemVendorId that = (ItemVendorId) o;

            if (!insertionDate.equals(that.insertionDate)) return false;
            if (!itemId.equals(that.itemId)) return false;
            if (!vendorId.equals(that.vendorId)) return false;

            return true;
        }

        public int hashCode() {
            int result;
            result = vendorId.hashCode();
            result = 31 * result + itemId.hashCode();
            result = 31 * result + insertionDate.hashCode();
            return result;
        }

        public String toString() {
            return "ItemVendor is : vendor " + vendorId + " + item " + itemId + "\n";
        }

        public Date getInsertionDate() {
            return insertionDate;
        }

        public void setInsertionDate(Date insertionDate) {
            this.insertionDate = insertionDate;
        }
    }

    /**
     * No-arg constructor for JavaBean tools.
     */
    public ItemVendor() {
    }

    public static ItemVendor createItemVendor(Vendor vendor,
                                              Item item, String user) {
        return createItemVendor(vendor, item, null, user);
    }

    public static ItemVendor createItemVendor(Vendor vendor,
                                              Item item,
                                              VendorContract vendorContract, String user) {
        ItemVendor itemVendor = new ItemVendor();
        itemVendor.setInsertedBy(user);
        itemVendor.setVendor(vendor);
        itemVendor.setItem(item);
        itemVendor.setItemVendorId(new ItemVendorId(vendor.getVendorId(), item.getItemId()));
        //vendor.getItemVendors().add(itemVendor);
        item.getItemVendors().add(itemVendor);

        if (vendorContract != null) {
            vendorContract.getItemVendors().add(itemVendor);
            itemVendor.setVendorContract(vendorContract);
        }
        itemVendor.setDiscount(new Double(0));
        itemVendor.setPrimaryVendor(Boolean.FALSE);
        return itemVendor;
    }

    public ItemVendorId getItemVendorId() {
        return itemVendorId;
    }

    public void setItemVendorId(ItemVendorId itemVendorId) {
        this.itemVendorId = itemVendorId;
    }

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

    public Item getItem() {
        return item;
    }

    protected void setItem(Item item) {
        this.item = item;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public Date getInsertionDate() {
        return itemVendorId.getInsertionDate();
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public Unit getBuyUnit() {
        return buyUnit;
    }

    public void setBuyUnit(Unit buyUnit) {
        this.buyUnit = buyUnit;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    // ********************** Common Methods ********************** //
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemVendor)) return false;

        final ItemVendor itemVendor = (ItemVendor) o;

        if (insertedBy != null ? !insertedBy.equals(itemVendor.insertedBy) : itemVendor.insertedBy != null)
            return false;
//        if (insertionDate != null ? !insertionDate.equals(itemVendor.insertionDate) : itemVendor.insertionDate != null) return false;
        if (item != null ? !item.equals(itemVendor.item) : itemVendor.item != null) return false;
        if (!itemVendorId.equals(itemVendor.itemVendorId)) return false;
        if (vendor != null ? !vendor.equals(itemVendor.vendor) : itemVendor.vendor != null) return false;
        if (vendorContract != null ? !vendorContract.equals(itemVendor.vendorContract) : itemVendor.vendorContract != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (vendorContract != null ? vendorContract.hashCode() : 0);
        result = 29 * result + (vendor != null ? vendor.hashCode() : 0);
        result = 29 * result + (item != null ? item.hashCode() : 0);
        result = 29 * result + itemVendorId.hashCode();
        result = 29 * result + version;
//        result = 29 * result + (insertionDate != null ? insertionDate.hashCode() : 0);
        result = 29 * result + (insertedBy != null ? insertedBy.hashCode() : 0);
        return result;
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

    public Integer getDispenseUnitsPerBuyUnit() {
        return dispenseUnitsPerBuyUnit;
    }

    public void setDispenseUnitsPerBuyUnit(Integer dispenseUnitsPerBuyUnit) {
        this.dispenseUnitsPerBuyUnit = dispenseUnitsPerBuyUnit;
    }

    public int getKey() {
        return itemVendorId.hashCode();
    }
}
