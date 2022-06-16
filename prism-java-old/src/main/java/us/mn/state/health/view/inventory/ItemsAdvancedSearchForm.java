package us.mn.state.health.view.inventory;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.matmgmt.util.Form;

public class ItemsAdvancedSearchForm extends ValidatorForm {
    //Common properties
    private String description = "";
    private String model = "";
    private String dispenseUnit = "";
    private String categoryCode = "";
    private String hazardous = "";
    private String manufacturer = "";
    private String vendor = "";

    //Stock Item properties
    private String icnbr = "";
    private String status = "";
    private String orgBudget = "";
    private String location = "";
    private String seasonal = "";
    private String fillUntilDepleted = "";
    private String contact = "";
    private String qtyOnHandFrom = "";
    private String qtyOnHandTo = "";
    private String holdUntilDateFrom = "";
    private String holdUntilDateTo = "";

    private Collection categories;
    private Collection results;
    private Collection units;
    private Collection manufacturers;
    private Collection vendors = new ArrayList();
    private Collection locations;
    private Collection orgBudgets;
    private Collection statuses;
    private Collection contacts;

    private String descriptionSearchOption = Form.ANY_WORD;
    private String modelSearchOption = Form.ANY_WORD;
    private String categorySearchOption = Form.DESCENDANT_CATEGORIES;
    private String itemTypeSearchOption = Form.ANY_ITEM;
    private String input = "";
    private String forward = "";

    public Collection getContacts() {
        return contacts;
    }

    public void setContacts(Collection contacts) {
        this.contacts = contacts;
    }

    public String getQtyOnHandFrom() {
        return qtyOnHandFrom;
    }

    public void setQtyOnHandFrom(String qtyOnHandFrom) {
        this.qtyOnHandFrom = qtyOnHandFrom;
    }

    public String getQtyOnHandTo() {
        return qtyOnHandTo;
    }

    public void setQtyOnHandTo(String qtyOnHandTo) {
        this.qtyOnHandTo = qtyOnHandTo;
    }

    public String getHoldUntilDateFrom() {
        return holdUntilDateFrom;
    }

    public void setHoldUntilDateFrom(String holdUntilDateFrom) {
        this.holdUntilDateFrom = holdUntilDateFrom;
    }

    public String getHoldUntilDateTo() {
        return holdUntilDateTo;
    }

    public void setHoldUntilDateTo(String holdUntilDateTo) {
        this.holdUntilDateTo = holdUntilDateTo;
    }

    public String getSeasonal() {
        return seasonal;
    }

    public void setSeasonal(String seasonal) {
        this.seasonal = seasonal;
    }

    public String getFillUntilDepleted() {
        return fillUntilDepleted;
    }

    public void setFillUntilDepleted(String fillUntilDepleted) {
        this.fillUntilDepleted = fillUntilDepleted;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCategorySearchOption() {
        return categorySearchOption;
    }

    public void setCategorySearchOption(String categorySearchOption) {
        this.categorySearchOption = categorySearchOption;
    }

    public String getItemTypeSearchOption() {
        return itemTypeSearchOption;
    }

    public void setItemTypeSearchOption(String itemTypeSearchOption) {
        this.itemTypeSearchOption = itemTypeSearchOption;
    }

    public Collection getStatuses() {
        return statuses;
    }

    public void setStatuses(Collection statuses) {
        this.statuses = statuses;
    }

    public Collection getUnits() {
        return units;
    }

    public void setUnits(Collection units) {
        this.units = units;
    }

    public Collection getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(Collection manufacturers) {
        this.manufacturers = manufacturers;
    }

    public Collection getVendors() {
        return vendors;
    }

    public void setVendors(Collection vendors) {
        this.vendors = vendors;
    }

    public Collection getLocations() {
        return locations;
    }

    public void setLocations(Collection locations) {
        this.locations = locations;
    }

    public Collection getOrgBudgets() {
        return orgBudgets;
    }

    public void setOrgBudgets(Collection orgBudgets) {
        this.orgBudgets = orgBudgets;
    }

    public String getDispenseUnit() {
        return dispenseUnit;
    }

    public void setDispenseUnit(String dispenseUnit) {
        this.dispenseUnit = dispenseUnit;
    }

    public String getHazardous() {
        return hazardous;
    }

    public void setHazardous(String hazardous) {
        this.hazardous = hazardous;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrgBudget() {
        return orgBudget;
    }

    public void setOrgBudget(String orgBudget) {
        this.orgBudget = orgBudget;
    }

    public String getDescriptionSearchOption() {
        return descriptionSearchOption;
    }

    public void setDescriptionSearchOption(String descriptionOption) {
        this.descriptionSearchOption = descriptionOption;
    }
    public Collection getResults() {
        return results;
    }

    public void setResults(Collection results) {
        this.results = results;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryId) {
        this.categoryCode = categoryId;
    }

    public Collection getCategories() {
        return categories;
    }

    public void setCategories(Collection categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcnbr() {
        return icnbr;
    }

    public void setIcnbr(String icnbr) {
        this.icnbr = icnbr;
    }


    public ItemsAdvancedSearchForm() {
    }
    
    public int getResultsNumber(){
       if(results != null){
            return results.size();	
       } 
       else {
           return 0;
       }
    }
    
    public String getInput() {
        return input;
    }
    
    public void setInput(String input) {
        this.input = input;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        results = new ArrayList();
        input = "";
        description = "";
        model = "";
    }


    public void setForward(String forward) {
        this.forward = forward;
    }


    public String getForward() {
        return forward;
    }


    public void setModel(String model) {
        this.model = model;
    }


    public String getModel() {
        return model;
    }


    public void setModelSearchOption(String modelSearchOption) {
        this.modelSearchOption = modelSearchOption;
    }


    public String getModelSearchOption() {
        return modelSearchOption;
    }
}
