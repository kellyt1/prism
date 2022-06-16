package us.mn.state.health.view.materialsrequest;

import java.util.ArrayList;
import java.util.Collection;

import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Vendor;

public class ShoppingListNonCatLineItemForm extends ShoppingListLineItemForm {

    private String itemDescription;
    private String itemCost;
    private Boolean itemHazardous;
    private String suggestedVendorURL;
    private String suggestedVendorCatalogNumber;
    private String suggestedVendorName;
    private Collection vendors = new ArrayList();
    private String suggestedVendorId;
    private Collection categories = new ArrayList();
    private String categoryId;
    
    
    public Category getCategory() {
        return (Category)CollectionUtils.
            getObjectFromCollectionById(categories, categoryId, "categoryId");
    }
    
    public Vendor getVendor() {
        return (Vendor)CollectionUtils.
            getObjectFromCollectionById(vendors, suggestedVendorId, "vendorId");
    }


    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }


    public String getItemDescription() {
        return itemDescription;
    }


    public void setItemCost(String itemCost) {
        this.itemCost = itemCost;
    }


    public String getItemCost() {
        return itemCost;
    }


    public void setItemHazardous(Boolean itemHazardous) {
        this.itemHazardous = itemHazardous;
    }


    public Boolean getItemHazardous() {
        return itemHazardous;
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


    public void setVendors(Collection vendors) {
        this.vendors = vendors;
    }


    public Collection getVendors() {
        return vendors;
    }


    public void setSuggestedVendorId(String suggestedVendorId) {
        this.suggestedVendorId = suggestedVendorId;
    }


    public String getSuggestedVendorId() {
        return suggestedVendorId;
    }


    public void setCategories(Collection categories) {
        this.categories = categories;
    }


    public Collection getCategories() {
        return categories;
    }


    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


    public String getCategoryId() {
        return categoryId;
    }

     
}