package us.mn.state.health.model.materialsrequest;

import org.apache.struts.upload.FormFile;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.Unit;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;

public class MaterialsLineItem implements Serializable {
    protected Collection<RequestLineItemFundingSource> fundingSources = new HashSet<RequestLineItemFundingSource>();          //TreeSet elements must implement Comparable
    protected Boolean amountInDollars = Boolean.TRUE;            //true means dollars, false means percent   
    protected Item item;
    protected String itemDescription;                             //for non-catalog item requests
    protected Category itemCategory;                              //for non-catalog item requests
    protected Integer quantity;
    protected Double itemCost;                                    //for non-catalog item requests
    protected Boolean itemHazardous;                              //for non-catalog item requests
    protected String itemJustification;                              //for non-catalog item requests
    protected Unit unit;                                          //for non-catalog item requests
    protected Collection<FormFile> purchasingInfoFiles;
    protected String suggestedVendorURL;
    protected String suggestedVendorCatalogNumber;
    protected String suggestedVendorName;
    protected String swiftItemId;
    protected int version;
    protected DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
    
    /**
     * a helper method to return either the Unit or item.unit, so that 3rd party 
     * tools that use reflection won't have to do the testing. 
     * @return either item.dispenseUnit or this.unit if item is null
     */
    public Unit getUnitResolved() {
        return (item == null) ? unit : item.getDispenseUnit();
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void setSwiftItemId(String swiftItemId) {
        this.swiftItemId = swiftItemId;
    }

    public String getSwiftItemId() {
        return swiftItemId;
    }

    public void setFundingSources(Collection<RequestLineItemFundingSource> fundingSources) {
        this.fundingSources = fundingSources;
    }

    public Collection<RequestLineItemFundingSource> getFundingSources() {
        return fundingSources;
    }

    public void setAmountInDollars(Boolean amountInDollars) {
        this.amountInDollars = (amountInDollars == null) ? true : amountInDollars;
    }

    public Boolean getAmountInDollars() {
        return (amountInDollars == null) ? true : amountInDollars;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    /**
     * return the user-provided description if item is non-catalog.  
     * @return item description
     */
    public String getItemDescription() {
       return itemDescription;    
    }

    public void setItemCategory(Category itemCategory) {
        this.itemCategory = itemCategory;
    }

    /**
     * return user-provided category (for non-catalog item requests)  
     * @return item category
     */
    public Category getItemCategory() {
        return itemCategory;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setItemCost(Double itemCost) {
        this.itemCost = itemCost;
    }
    public String getItemCostRND() {
        return (itemCost == null) ? "0" : new DecimalFormat("##,####.00").format(itemCost);
    }
    public Double getItemCost() {
        return (itemCost == null) ? 0.0 : itemCost;
    }

    public void setItemHazardous(Boolean itemHazardous) {
        this.itemHazardous = itemHazardous;
    }

    public Boolean getItemHazardous() {
        return itemHazardous;
    }

    public void setPurchasingInfoFiles(Collection<FormFile> purchasingInfoFiles) {
        this.purchasingInfoFiles = purchasingInfoFiles;
    }

    public Collection<FormFile> getPurchasingInfoFiles() {
        return purchasingInfoFiles;
    }

    public void setSuggestedVendorURL(String suggestedVendorURL) {
        this.suggestedVendorURL = suggestedVendorURL;
    }

    public String getSuggestedVendorURL() {
        return suggestedVendorURL;
    }

    public void setSuggestedVendorCatalogNumber(String suggestedVendorCatalogNumber) {
        this.suggestedVendorCatalogNumber = suggestedVendorCatalogNumber;
    }

    public String getSuggestedVendorCatalogNumber() {
        return suggestedVendorCatalogNumber;
    }

    public void setSuggestedVendorName(String suggestedVendorName) {
        this.suggestedVendorName = suggestedVendorName;
    }

    public String getSuggestedVendorName() {
        return suggestedVendorName;
    }

    public String getItemJustification() {
        return itemJustification;
    }

    public void setItemJustification(String itemJustification) {
        this.itemJustification = itemJustification;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setDaoFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public DAOFactory getDaoFactory() {
        return daoFactory;
    }
}