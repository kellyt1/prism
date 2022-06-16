package us.mn.state.health.view.purchasing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.matmgmt.util.Form;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.util.search.OrderLineItemIndex;

public class SearchOrdersForm extends ValidatorForm {
    private String orderId = "";        // the "OPR" number
    private String purchaseOrderNumber = "";
    private String purchaseOrderNumberType = "";
    private String itemDescription = "";
    private String vendorCatalogNbr= "";
    private String itemModel = "";
    private String vendorId = "";
    private String vendorName = "";
    private String statusId = "";
    private String orgBudgetId = "";
    private String orgBudgetCode = "";
    private String purchaserId = "";
    private String requestorId = "";
    private String requestId = "";
    private String mailingAddressId = "";
    private String orderedFrom = "";
    private String orderedTo = "";
    private String vendorContractId = "";
    private String selectedOrderId;
    private String suspenseDateFrom = "";
    private String suspenseDateTo = "";
    
    private Collection vendors;
    private Collection orgBudgets;
    private Collection statuses;
    private Collection persons;
    private Collection buyers;
    private Collection facilities;

    private String descriptionSearchOption = Form.ALL_WORDS;
    private String input = "";
    private String forward = "";
    private Collection searchResults;

    private String paginationDirection;
    private int firstResult = 0;  //for pagination
    private int maxResults = 10;   //for pagination
    private String pageNo;
    private int resultsNo;  // the total number of results
    private boolean displayNextLink=true;
    private boolean reset = false;

    public SearchOrdersForm() {
    }

    /** Validates Order form values */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
     //return super.validate(mapping, request);
     return null;
    }
    
    /** Resets Order form values */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (reset) {
            reset = false;
            orderId = null;
            itemDescription = null;
            this.vendorCatalogNbr= null;
            vendorId = null;
            vendorName = null;
            purchaseOrderNumber = null;
            statusId = null;
            purchaserId = null;
            requestId = null;
            requestorId = null;
            selectedOrderId = null;
            searchResults = null;
            orderedTo = DateUtils.toString(new Date());
            orderedFrom = "";
            suspenseDateFrom = null;
            suspenseDateTo = null;
            pageNo=null;
            orgBudgetCode = null;
        }
        pageNo=null;
    }


    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public String getOrderId() {
        return this.orderId;
    }
    
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemDescription() {
        return this.itemDescription;
    }



    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public String getPurchaseOrderNumber() {
        return this.purchaseOrderNumber;
    }
    
    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusId() {
        return this.statusId;
    }
    
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorId() {
        return this.vendorId;
    }

    public void setOrgBudgetId(String orgBudgetId) {
        this.orgBudgetId = orgBudgetId;
    }

    public String getOrgBudgetId() {
        return orgBudgetId;
    }
    public String getOrgBudgetCode() {
        return orgBudgetCode;
    }

    public void setOrgBudgetCode(String orgBudgetCode) {
        this.orgBudgetCode = orgBudgetCode;
    }


    public void setOrderedTo(String orderedTo) {
        this.orderedTo = orderedTo;
    }

    public String getOrderedTo() {
        return orderedTo;
    }

    public void setVendorContractId(String vendorContractId) {
        this.vendorContractId = vendorContractId;
    }


    public String getVendorContractId() {
        return vendorContractId;
    }

    public void setSelectedOrderId(String selectedOrderId) {
        this.selectedOrderId = selectedOrderId;
    }

    public String getSelectedOrderId() {
        return selectedOrderId;
    }

    public void setVendors(Collection vendors) {
        this.vendors = vendors;
    }

    public Collection getVendors() {
        return vendors;
    }

    public void setOrgBudgets(Collection orgBudgets) {
        this.orgBudgets = orgBudgets;
    }

    public Collection getOrgBudgets() {
        return orgBudgets;
    }

    public void setStatuses(Collection statuses) {
        this.statuses = statuses;
    }

    public Collection getStatuses() {
        return statuses;
    }

    public void setPersons(Collection persons) {
        this.persons = persons;
    }

    public Collection getPersons() {
        return persons;
    }

    public void setDescriptionSearchOption(String descriptionSearchOption) {
        this.descriptionSearchOption = descriptionSearchOption;
    }

    public String getDescriptionSearchOption() {
        return descriptionSearchOption;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public String getForward() {
        return forward;
    }

    public void setSearchResults(Collection searchResults) {
        this.searchResults = searchResults;
    }

    public Collection getSearchResults() {
        return searchResults;
    }

    public void setPurchaserId(String purchaserId) {
        this.purchaserId = purchaserId;
    }

    public String getPurchaserId() {
        return purchaserId;
    }

    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

    public String getRequestorId() {
        return requestorId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setOrderedFrom(String orderedFrom) {
        this.orderedFrom = orderedFrom;
    }

    public String getOrderedFrom() {
        return orderedFrom;
    }    


    public void setItemModel(String itemModel) {
        this.itemModel = itemModel;
    }


    public String getItemModel() {
        return itemModel;
    }


    public void setSuspenseDateFrom(String suspenseDateFrom) {
        this.suspenseDateFrom = suspenseDateFrom;
    }


    public String getSuspenseDateFrom() {
        return suspenseDateFrom;
    }


    public void setSuspenseDateTo(String suspenseDateTo) {
        this.suspenseDateTo = suspenseDateTo;
    }


    public String getSuspenseDateTo() {
        return suspenseDateTo;
    }

    public Status getStatus(){
        return (Status) CollectionUtils.getObjectFromCollectionById(statuses, statusId,"statusId");
    }


    public void setBuyers(Collection buyers) {
        this.buyers = buyers;
    }


    public Collection getBuyers() {
        return buyers;
    }


    public void setPurchaseOrderNumberType(String purchaseOrderNumberType) {
        this.purchaseOrderNumberType = purchaseOrderNumberType;
    }


    public String getPurchaseOrderNumberType() {
        return purchaseOrderNumberType;
    }


    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }


    public String getVendorName() {
        return vendorName;
    }

    public String getPaginationDirection() {
        return paginationDirection;
    }

    public void setPaginationDirection(String paginationDirection) {
        this.paginationDirection = paginationDirection;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public int getResultsNo() {
        return resultsNo;
    }

    public void setResultsNo(int resultsNo) {
        this.resultsNo = resultsNo;
    }

    public boolean isDisplayNextLink() {
        return displayNextLink;
    }

    public void setDisplayNextLink(boolean displayNextLink) {
        this.displayNextLink = displayNextLink;
    }

    public List getPages(){
        int a = resultsNo/10;
        int b = resultsNo%10;
        if(b!=0){
            a++;
        }
        List pages = new ArrayList();
        for (int i=1; i <= a; i++){
            pages.add(new Integer(i));
        }
        return pages;
    }


    public String getVendorCatalogNbr() {
        return vendorCatalogNbr;
    }

    public void setVendorCatalogNbr(String vendorCatalogNbr) {
        this.vendorCatalogNbr = vendorCatalogNbr;
    }

    public Collection getFacilities() {
        return facilities;
    }

    public void setFacilities(Collection facilities) {
        this.facilities = facilities;
    }

    public String getMailingAddressId() {
        return mailingAddressId;
    }

    public void setMailingAddressId(String mailingAddressId) {
        this.mailingAddressId = mailingAddressId;
    }

}
