package us.mn.state.health.view.materialsrequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.Vendor;

public class RequestLineItemSearchForm extends ValidatorForm {
    private String searchCriteria;
    private String requestLineItemId;
    private String categoryId;
    private Collection categories = new ArrayList();
    private String itemDescription;
    private String purchaserId;
    private Collection purchasers = new ArrayList();
    private String orgBudgetCode;
    private String neededByFrom;
    private String neededByTo;
    private String requestId;
    private String searchRequestLineItemId;
    private String requestorId;
    private Collection requestors = new ArrayList();
    private String statusId;
    private String statusCode;
    private String defaultStatusCodes;
    private Collection statuses = new ArrayList();
    private String requestTrackingNumber;
    private String vendorId;                        //obsolete - not doing vendor drop-down anymore
    private Collection vendors = new ArrayList();   //obsolete - not doing vendor drop-down anymore
    private String vendorName;
   
    private Boolean showAdvancedSearch = Boolean.TRUE;
    private Collection rliForms = new ArrayList();
    private boolean odd = true;
    private Boolean selectAll = Boolean.FALSE;

    /*
     * Resets RequestLineItems form values
     * @param mapping Mapping for this action
     * @param request request associated with this action
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        selectAll = Boolean.FALSE;
        odd = true;
        super.reset(mapping, request);
        if(!rliForms.isEmpty()){
            for(Iterator<RequestLineItemForm> iterator = rliForms.iterator(); iterator.hasNext();){
                RequestLineItemForm requestLineItemForm = (RequestLineItemForm)iterator.next();
                requestLineItemForm.setSelected(false);
            }
        }

    }

    /*
     * Validates RequestLineItems form values
     * @param mapping Mapping for this action
     * @param request request associated with this action
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }
    
    public Category getCategory() {
        return (Category)CollectionUtils.getObjectFromCollectionById(categories, categoryId, "categoryId");
    }
    
    public Status getStatus() {
        return (Status)CollectionUtils.getObjectFromCollectionById(statuses, statusCode, "statusCode");
    }
    
    public Person getRequestor() {
        return (Person)CollectionUtils.getObjectFromCollectionById(requestors, requestorId, "personId");
    }
    
    public Person getPurchaser() {
        return (Person)CollectionUtils.getObjectFromCollectionById(purchasers, purchaserId, "personId");
    }

    public Vendor getVendor(){
        return (Vendor)CollectionUtils.getObjectFromCollectionById(vendors, vendorId, "vendorId");
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public Collection getRliForms() {
        return rliForms;
    }

    public void setRliForms(Collection rliForms) {
        this.rliForms = rliForms;
    }

    public boolean getOdd() {
        return odd;
    }
    
    public String getFlip() {
        if(odd) {
            odd = false;
        }            
        else {
            odd = true;
        }
        return "";
    }
    
    public Boolean getSelectAll() {
        return selectAll;
    }
    
    public void setSelectAll(Boolean selectAll) {
        this.selectAll = selectAll;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


    public String getCategoryId() {
        return categoryId;
    }


    public void setCategories(Collection categories) {
        this.categories = categories;
    }


    public Collection getCategories() {
        return categories;
    }


    public void setPurchaserId(String purchaserId) {
        this.purchaserId = purchaserId;
    }


    public String getPurchaserId() {
        return purchaserId;
    }


    public void setPurchasers(Collection purchasers) {
        this.purchasers = purchasers;
    }


    public Collection getPurchasers() {
        return purchasers;
    }


    public void setOrgBudgetCode(String orgBudgetCode) {
        this.orgBudgetCode = orgBudgetCode;
    }


    public String getOrgBudgetCode() {
        return orgBudgetCode;
    }


    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    public String getRequestId() {
        return requestId;
    }


    public void setRequestLineItemId(String requestLineItemId) {
        this.requestLineItemId = requestLineItemId;
    }


    public String getRequestLineItemId() {
        return requestLineItemId;
    }


    public void setShowAdvancedSearch(Boolean showAdvancedSearch) {
        this.showAdvancedSearch = showAdvancedSearch;
    }


    public Boolean getShowAdvancedSearch() {
        return showAdvancedSearch;
    }


    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }


    public String getRequestorId() {
        return requestorId;
    }


    public void setRequestors(Collection requestors) {
        this.requestors = requestors;
    }


    public Collection getRequestors() {
        return requestors;
    }


    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }


    public String getStatusId() {
        return statusId;
    }


    public void setStatuses(Collection statuses) {
        this.statuses = statuses;
    }


    public Collection getStatuses() {
        return statuses;
    }


    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }


    public String getItemDescription() {
        return itemDescription;
    }


    public void setNeededByFrom(String neededByFrom) {
        this.neededByFrom = neededByFrom;
    }


    public String getNeededByFrom() {
        return neededByFrom;
    }


    public void setNeededByTo(String neededByTo) {
        this.neededByTo = neededByTo;
    }


    public String getNeededByTo() {
        return neededByTo;
    }


    public void setSearchRequestLineItemId(String searchRequestLineItemId) {
        this.searchRequestLineItemId = searchRequestLineItemId;
    }


    public String getSearchRequestLineItemId() {
        return searchRequestLineItemId;
    }


    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }


    public String getStatusCode() {
        return statusCode;
    }


    public void setDefaultStatusCodes(String defaultStatusCodes) {
        this.defaultStatusCodes = defaultStatusCodes;
    }


    public String getDefaultStatusCodes() {
        return defaultStatusCodes;
    }
    
    public boolean getSelectedReqLnItemFormsEmpty() {
        return CollectionUtils.getMatchingItems(rliForms, Boolean.TRUE, "selected")
                              .isEmpty();
    }


    public void setRequestTrackingNumber(String requestTrackingNumber) {
        this.requestTrackingNumber = requestTrackingNumber;
    }


    public String getRequestTrackingNumber() {
        return requestTrackingNumber;
    }


    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }


    public String getVendorId() {
        return vendorId;
    }


    public void setVendors(Collection vendors) {
        this.vendors = vendors;
    }


    public Collection getVendors() {
        return vendors;
    }
}
