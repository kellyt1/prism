package us.mn.state.health.view.inventory;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.matmgmt.util.Form;

public class AssetSearchForm extends ValidatorForm {
    private String description = "";
    private String categoryCode = "";
    private String manufacturer = "";
    private String vendor = "";
    private String statusCode = "";
    private String orgBudget = "";
    private String facility = "";
    private String contact = "";
    private String serialNumber = "";
    private String assetNumber = "";
    private String classCode = "";
    private String cost = "";
    private String dateReceivedFrom = "";
    private String dateReceivedTo = "";
    private String maintAgreementExpirationDate = "";
    private String maintAgreementPONumber = "";
    private String warrantyExpirationDate = "";
    private String model = "";

    private Collection classCodes;
    private Collection categories;
    private Collection results;
    private Collection manufacturers;
    private Collection facilities;
    private Collection orgBudgets;
    private Collection statuses;
    private Collection contacts;

    private String descriptionSearchOption = Form.ALL_WORDS;
    private String categorySearchOption = Form.DESCENDANT_CATEGORIES;
    private String itemTypeSearchOption = Form.ANY_ITEM;
    private String input = "";
    private String forward = "";
    
    public AssetSearchForm() { }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        results = new ArrayList();
        input = "";
        description = "";
    }

    public Collection getContacts() {
        return contacts;
    }

    public void setContacts(Collection contacts) {
        this.contacts = contacts;
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

    public Collection getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(Collection manufacturers) {
        this.manufacturers = manufacturers;
    }

    public Collection getFacilities() {
        return facilities;
    }

    public void setFacilities(Collection facilities) {
        this.facilities = facilities;
    }

    public Collection getOrgBudgets() {
        return orgBudgets;
    }

    public void setOrgBudgets(Collection orgBudgets) {
        this.orgBudgets = orgBudgets;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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
    
    public int getResultsNumber(){
       if(results!=null){
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

    public void setForward(String forward) {
        this.forward = forward;
    }


    public String getForward() {
        return forward;
    }


    public void setFacility(String facility) {
        this.facility = facility;
    }


    public String getFacility() {
        return facility;
    }


    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public String getSerialNumber() {
        return serialNumber;
    }


    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }


    public String getClassCode() {
        return classCode;
    }


    public void setCost(String cost) {
        this.cost = cost;
    }


    public String getCost() {
        return cost;
    }


    public void setDateReceivedFrom(String dateReceivedFrom) {
        this.dateReceivedFrom = dateReceivedFrom;
    }


    public String getDateReceivedFrom() {
        return dateReceivedFrom;
    }


    public void setDateReceivedTo(String dateReceivedTo) {
        this.dateReceivedTo = dateReceivedTo;
    }


    public String getDateReceivedTo() {
        return dateReceivedTo;
    }


    public void setMaintAgreementExpirationDate(String maintAgreementExpirationDate) {
        this.maintAgreementExpirationDate = maintAgreementExpirationDate;
    }


    public String getMaintAgreementExpirationDate() {
        return maintAgreementExpirationDate;
    }


    public void setMaintAgreementPONumber(String maintAgreementPONumber) {
        this.maintAgreementPONumber = maintAgreementPONumber;
    }


    public String getMaintAgreementPONumber() {
        return maintAgreementPONumber;
    }


    public void setWarrantyExpirationDate(String warrantyExpirationDate) {
        this.warrantyExpirationDate = warrantyExpirationDate;
    }


    public String getWarrantyExpirationDate() {
        return warrantyExpirationDate;
    }


    public void setModel(String model) {
        this.model = model;
    }


    public String getModel() {
        return model;
    }


    public void setClassCodes(Collection classCodes) {
        this.classCodes = classCodes;
    }


    public Collection getClassCodes() {
        return classCodes;
    }


    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }


    public String getAssetNumber() {
        return assetNumber;
    }
}
