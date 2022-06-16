package us.mn.state.health.model.materialsrequest;

import java.util.Collection;
import java.util.HashSet;

import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Vendor;

public class ShoppingListNonCatalogLineItem extends ShoppingListLineItem {

    private Collection fundingSources = new HashSet();                             
    private String itemDescription;                             
    private Category itemCategory;                              
    private Double itemCost;                                    
    private Boolean itemHazardous;                              
    private Collection purchasingInfoFiles;
    private String suggestedVendorURL;
    private String suggestedVendorCatalogNumber;
    private String suggestedVendorName;
    private Vendor suggestedVendor;
    private Category category;
    
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if (!(o instanceof ShoppingListNonCatalogLineItem)) return false;

        final ShoppingListNonCatalogLineItem that = (ShoppingListNonCatalogLineItem)o;

        if(this.getShoppingListLineItemId() == null) {
            if(that.getShoppingListLineItemId() == null) {
                //dig deeper, using comparison by value
                if(this.getItemDescription() != null && !this.getItemDescription().equals(that.getItemDescription())) {
                    return false;
                }
                if(this.getItemCategory() != null && !this.getItemCategory().equals(that.getItemCategory())) {
                    return false;
                }
                if(this.getCategory() != null && !this.getCategory().equals(that.getCategory())) {
                    return false;
                }

                if(this.getItemCost() != null && !this.getItemCost().equals(that.getItemCost())) {
                    return false;
                }
                return true;
            }
            else {
                //if one ID is null, and the other is not null, they can't be the same
                return false;
            }
        }
        else {  //we know we can't get a NullPointerException now...
            return this.getShoppingListLineItemId().equals(that.getShoppingListLineItemId());
        }
    }

    public int hashCode() {
        int result = 13;
        result = 29 * result + (getItemDescription() != null ? getItemDescription().hashCode() : 0);
        return result;
    }

    public void setFundingSources(Collection fundingSources) {
        this.fundingSources = fundingSources;
    }

    public Collection getFundingSources() {
        return fundingSources;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemCategory(Category itemCategory) {
        this.itemCategory = itemCategory;
    }

    public Category getItemCategory() {
        return itemCategory;
    }

    public void setItemCost(Double itemCost) {
        this.itemCost = itemCost;
    }

    public Double getItemCost() {
        return itemCost;
    }

    public void setItemHazardous(Boolean itemHazardous) {
        this.itemHazardous = itemHazardous;
    }

    public Boolean getItemHazardous() {
        return itemHazardous;
    }

    public void setPurchasingInfoFiles(Collection purchasingInfoFiles) {
        this.purchasingInfoFiles = purchasingInfoFiles;
    }

    public Collection getPurchasingInfoFiles() {
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

    public void setSuggestedVendor(Vendor suggestedVendor) {
        this.suggestedVendor = suggestedVendor;
    }

    public Vendor getSuggestedVendor() {
        return suggestedVendor;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }
}