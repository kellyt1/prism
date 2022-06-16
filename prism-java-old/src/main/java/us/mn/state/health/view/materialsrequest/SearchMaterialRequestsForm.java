package us.mn.state.health.view.materialsrequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.matmgmt.util.Form;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Form that is used for the searching of Materials Requests
 *
 * @author Lucian Ochian
 */
public class SearchMaterialRequestsForm extends ValidatorForm {

    private String requestId;
    private String itemDescription;
    private String itemModel;
    private String vendorId;
    private String vendorName;
    private String statusCode;
    private String orgBudgetId;
    private String icnbr;
    private String dateRequestedForm;
    private String dateRequestedTo;
    private String neededByFrom;
    private String neededByTo;
    private String location;
    private String additionalInstr;
    private String categoryCode;
    private String orgBudgetCode;
    private String requestTrackingNumber;
    private String requestorId;
    private String query;
    private String swiftItemId;

    private Collection vendors;
    private Collection orgBudgets;
    private Collection statuses;
    private Collection categories;
    private Collection requestors;

    private String descriptionSearchOption = Form.ALL_WORDS;
    private String input = "";
    private String forward = "";
    private Collection searchResults;

    private Collection requestForms = new ArrayList();     //the type of collection has to be accessible via index (i.e., requests[0])
    private String requestFormIndex;
    private String cmd = "";

    private String paginationDirection;
    private int firstResult = 0;  //for pagination
    private int maxResults = 10;   //for pagination
    private String pageNo;
    private int resultsNo;  // the total number of results
    private boolean displayNextLink=true;


    public SearchMaterialRequestsForm() {
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        pageNo = null;
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

    public String getRequestTrackingNumber() {
        return requestTrackingNumber;
    }

    public void setRequestTrackingNumber(String requestTrackingNumber) {
        this.requestTrackingNumber = requestTrackingNumber;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getOrgBudgetCode() {
        return orgBudgetCode;
    }

    public void setOrgBudgetCode(String orgBudgetCode) {
        this.orgBudgetCode = orgBudgetCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Collection getCategories() {
        return categories;
    }

    public void setCategories(Collection categories) {
        this.categories = categories;
    }

    public Collection getRequestForms() {
        return requestForms;
    }

    public void setRequestForms(Collection requestForms) {
        this.requestForms = requestForms;
    }

    public String getRequestFormIndex() {
        return requestFormIndex;
    }

    public void setRequestFormIndex(String requestFormIndex) {
        this.requestFormIndex = requestFormIndex;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemModel() {
        return itemModel;
    }

    public void setItemModel(String itemModel) {
        this.itemModel = itemModel;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getOrgBudgetId() {
        return orgBudgetId;
    }

    public void setOrgBudgetId(String orgBudgetId) {
        this.orgBudgetId = orgBudgetId;
    }

    public String getIcnbr() {
        return icnbr;
    }

    public void setIcnbr(String icnbr) {
        this.icnbr = icnbr;
    }

    public String getDateRequestedForm() {
        return dateRequestedForm;
    }

    public void setDateRequestedForm(String dateRequestedForm) {
        this.dateRequestedForm = dateRequestedForm;
    }

    public String getDateRequestedTo() {
        return dateRequestedTo;
    }

    public void setDateRequestedTo(String dateRequestedTo) {
        this.dateRequestedTo = dateRequestedTo;
    }

    public String getNeededByFrom() {
        return neededByFrom;
    }

    public void setNeededByFrom(String neededByFrom) {
        this.neededByFrom = neededByFrom;
    }

    public String getNeededByTo() {
        return neededByTo;
    }

    public void setNeededByTo(String neededByTo) {
        this.neededByTo = neededByTo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAdditionalInstr() {
        return additionalInstr;
    }

    public void setAdditionalInstr(String additionalInstr) {
        this.additionalInstr = additionalInstr;
    }

    public Collection getVendors() {
        return vendors;
    }

    public void setVendors(Collection vendors) {
        this.vendors = vendors;
    }

    public Collection getOrgBudgets() {
        return orgBudgets;
    }

    public void setOrgBudgets(Collection orgBudgets) {
        this.orgBudgets = orgBudgets;
    }

    public Collection getStatuses() {
        return statuses;
    }

    public void setStatuses(Collection statuses) {
        this.statuses = statuses;
    }

    public String getDescriptionSearchOption() {
        return descriptionSearchOption;
    }

    public void setDescriptionSearchOption(String descriptionSearchOption) {
        this.descriptionSearchOption = descriptionSearchOption;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public Collection getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(Collection searchResults) {
        this.searchResults = searchResults;
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

    public boolean isDisplayNextLink() {
        return displayNextLink;
    }

    public void setDisplayNextLink(boolean displayNextLink) {
        this.displayNextLink = displayNextLink;
    }

    public Collection getRequestors() {
        return requestors;
    }

    public void setRequestors(Collection requestors) {
        this.requestors = requestors;
    }

    public String getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

    public String getSwiftItemId() {
        return swiftItemId;
    }

    public void setSwiftItemId(String swiftItemId) {
        this.swiftItemId = swiftItemId;
    }

    public List getPages(){
        int a = resultsNo/10;
        int b = resultsNo%10;
        if(b!=0){
            a++;
        }
        List pages = new ArrayList();
        for (int i=1; i <= a; i++){
            pages.add(i);
        }
        return pages;
    }

}
