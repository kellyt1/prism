package us.mn.state.health.model.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.persistence.Id;
import org.hibernate.search.annotations.DocumentId;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.inventory.OrderFormulaDAO;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Hazardous;
import us.mn.state.health.model.common.Manufacturer;
import us.mn.state.health.model.common.ObjectCode;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.common.VendorContract;
import us.mn.state.health.model.materialsrequest.DeliveryDetail;
import us.mn.state.health.model.util.search.EntityIndex;
import us.mn.state.health.model.util.search.Searchable;

/**
 * This is the superclass for all types of items.  It is abstract, and so cannot be instantiated directly.
 */
//public abstract class Item implements Serializable, Comparable, Searchable {
public abstract class Item implements Serializable, Comparable {
    public static final String ID = "id";

    private Long itemId;
    private Category category;                                  //an item has only one category
    private ObjectCode objectCode;                              //an item has only one objectCode
    private Manufacturer manufacturer;
    private String model;                                       // aka 'product nbr'
    private String description;
    private String descriptionForUser;
    private DeliveryDetail deliveryDetail;
    /**
     * @deprecated
     */
    private Boolean hazardous = Boolean.FALSE;
    private Hazardous hazardousObject;
    private Integer economicOrderQty = 0;
    private Collection<ItemVendor> itemVendors = new LinkedHashSet<ItemVendor>();
    private Unit dispenseUnit;
    private Double dispenseUnitCost;
    private Integer estimatedAnnualUsage;                        //flakey value a user enters; its a guess usually on a new stock item SIAR
    private Integer annualUsage = 0;                //last FY annual usage, for system use only.  Not for display or entry by any user
                                                                 //Update - This is displayed on the Main Stock Items selection page and General Info tab
    private int version;
    private Date lastUpdatedDate;
    private String lastUpdatedBy;
    private Date terminationDate;
    private String terminatedBy;
    private Date insertionDate = new Date();
    private String insertedBy;
    private Integer currentDemand;                                  //calculated field.
    public static final String STOCK_ITEM = "Stock Item";
    public static final String PURCHASE_ITEM = "Purchase Item";
    protected DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
    private Collection<AttachedFile> attachedFiles = new HashSet<AttachedFile>();

    public Item() {
    }

    public Item(String description) {
        this.description = description;
    }

    // ********************** Common Methods to be implemented by sub-classes ********************** //
    public abstract boolean equals(Object o);

    public abstract int hashCode();

    public abstract int compareTo(Object o);

    public abstract EntityIndex getEntityIndex() throws InfrastructureException;

    public abstract void delete() throws InfrastructureException;

    public abstract void save() throws InfrastructureException;

    public ObjectCode getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(ObjectCode objectCode) {
        this.objectCode = objectCode;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }

    public String getTerminatedBy() {
        return terminatedBy;
    }

    public void setTerminatedBy(String terminatedBy) {
        this.terminatedBy = terminatedBy;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionForUser() {
        return descriptionForUser;
    }

    public void setDescriptionForUser(String descriptionForUser) {
        this.descriptionForUser = descriptionForUser;
    }

    public void setDeliveryDetail(DeliveryDetail deliveryDetail) {this.deliveryDetail = deliveryDetail;
    }

    public DeliveryDetail getDeliveryDetail() {
        return deliveryDetail;
    }

    public String getDeliverToInfoAsString() {
        return (getDeliveryDetail() != null) ? getDeliveryDetail().getLongSummary() : "";
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @deprecated
     */
    public Boolean getHazardous() {
        return hazardous;
    }

    /**
     * @deprecated
     */
    public void setHazardous(Boolean hazardous) {
        this.hazardous = hazardous;
    }


    public Hazardous getHazardousObject() {
        return hazardousObject;
    }

    public void setHazardousObject(Hazardous hazardousObject) {
        this.hazardousObject = hazardousObject;
    }

    public Integer getEconomicOrderQty() {
        return economicOrderQty;
    }

    public void setEconomicOrderQty(Integer economicOrderQty) {
        this.economicOrderQty = economicOrderQty;
    }

    public Collection<ItemVendor> getItemVendors() {
        return itemVendors;
    }

    public void setItemVendors(Collection<ItemVendor> itemVendors) {
        this.itemVendors = itemVendors;
    }

    public Unit getDispenseUnit() {
        return dispenseUnit;
    }

    public void setDispenseUnit(Unit dispenseUnit) {
        this.dispenseUnit = dispenseUnit;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    @Id
    @DocumentId (name = ID)
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getEstimatedAnnualUsage() {
        return estimatedAnnualUsage;
    }

    public void setEstimatedAnnualUsage(Integer estimatedAnnualUsage) {
        this.estimatedAnnualUsage = estimatedAnnualUsage;
    }

    public Integer getAnnualUsage() {
        return annualUsage;
    }

    public void setAnnualUsage(Integer annualUsage) {
        this.annualUsage = annualUsage;
    }

    /**
     * calculate a suggested Economic Order Quantity for this item based on the EOQ formula.
     * EOQ = SQRT(((24 * order cost for Item's category)*(annual usage / 12))/(0.28 * unit cost)).
     * If this method cannot calculate a suggested EOQ properly, it will return the annualUsag.
     * For example, if unit cost is null then we can't figure out the EOQ.
     * <p/>
     * TODO: Determine if this method should return a BusinessException instead of returning 0
     * if the unit cost is null or 0 or if the annualUsage is null
     *
     * @return an up-to-date suggested Economic Order Quantity based on the EOQ formula or annual usage
     *         if it can't calculate suggested EOQ properly.  Returns 0 if unit cost is either null or 0 and annualUsage is null
     */
    public Integer calculateSuggestedEOQ() throws InfrastructureException {
        OrderFormulaDAO orderFormulaDAO = daoFactory.getOrderFormulaDAO();
        OrderFormula orderFormula = orderFormulaDAO.findByCategoryCode(this.category.getCategoryCode());
        Integer intCalculation;
        try {
            double orderCost = orderFormula.getOrderCost();
            int usage = 0;

            if (this.getAnnualUsage() != null) {
                usage = getAnnualUsage();
            }

            if (getDispenseUnitCost() == null || getDispenseUnitCost() < 0.001) {
                if (this.annualUsage != null) {
                    return this.annualUsage;        //can't divide by zero, so default to annual usage
                } else {
                    return 0;
                }
            }

            Long calculation = Math.round(Math.sqrt(((24 * orderCost) * (usage / 12)) / (0.28 * getDispenseUnitCost())));


            intCalculation = calculation.intValue();
        } catch (Exception e) {
            intCalculation = -1;
        }

        return intCalculation;
    }

    public ItemVendor getItemVendor(Long vendorId) {
        for (Iterator<ItemVendor> i = itemVendors.iterator(); i.hasNext();) {
            ItemVendor itemVendor = i.next();
            if (itemVendor.getVendor().getVendorId().longValue() ==
                    vendorId.longValue()) {

                return itemVendor;
            }
        }
        return null;
    }

    public ItemVendor addVendor(Vendor vendor, String user) throws InfrastructureException {
        return this.addVendor(vendor, null, user);
    }

    public ItemVendor addVendor(Vendor vendor, VendorContract contract, String user) throws InfrastructureException {
        //the createItemVendor adds the ItemVendor to this class's (and Vendor's) itemVendors collection
        ItemVendor iv;
        iv = ItemVendor.createItemVendor(vendor, this, contract, user);
        return iv;
    }

    /**
     * @return a String to display in the pages the type of the Item
     */
    public abstract String getItemType();// {
//        if (this instanceof StockItem) return STOCK_ITEM;
//        if (this instanceof PurchaseItem) return PURCHASE_ITEM;
//        TODO maybe in the future we'll have Surplus items
//        return null;
//    }

    public void setCurrentDemand(Integer currentDemand) {
        this.currentDemand = currentDemand;
    }


    public Integer getCurrentDemand() {
        if (currentDemand == null) {
            return 0;
        }
        return currentDemand;
    }

    public Double getDispenseUnitCost() {
        return this.dispenseUnitCost;
    }


    /**
     * this property should get updated every time an order is placed for a particular item.
     */
    public void setDispenseUnitCost(Double dispenseUnitCost) {
        this.dispenseUnitCost = dispenseUnitCost;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return this.model;
    }

    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", description='" + description + "'" +
                ", insertionDate=" + insertionDate +
                ", lastUpdatedDate=" + lastUpdatedDate +
                ", lastUpdatedBy='" + lastUpdatedBy + "'" +
                "}";
    }

    public Object[] getItemVendorsAsArray() {
        List<ItemVendor> vendrs = new ArrayList<ItemVendor>();
        for (ItemVendor itemVendor : itemVendors) {
            if (itemVendor.getEndDate() == null || itemVendor.getEndDate().after(new Date()))
                vendrs.add(itemVendor);
        }
        return vendrs.toArray();
    }

    public Collection<AttachedFile> getAttachedFiles() {
            return this.attachedFiles;
        }

    public void setAttachedFiles(Collection<AttachedFile> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public void addAttachedFile(AttachedFile attachedFile){
//        attachedFile.setItem(this);
        attachedFiles.add(attachedFile);
    }

}
