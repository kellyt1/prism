package us.mn.state.health.view.materialsrequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.lang.StringUtils;

public class RequestFormCollection extends ValidatorForm {
    private Collection priorities;
    private Collection requestors;
    private Collection categories;
    private Collection statuses;
    private String requestTrackingNumber;
    private String deliverTo;
    private String instructions;
    private String icnbr;
    private String itemDescription;
    private String priorityCode;
    private String unitCode;
    private String neededByFrom;
    private String neededByTo;
    private String itemModel;
    private String requestorId;
    private String dateRequestedForm;
    private String dateRequestedTo;
    private String categoryCode;
    private String statusCode;

    private String paginationDirection;
    private int firstResult = 0;  //for pagination
    private int maxResults = 10;   //for pagination
    private int totalRequests;     //for pagination - this represents the total # of request in the DB so we know how many page options to give
    private boolean displayNextLink = true;
    private String pageNbr = "1";
    private Integer pageLinksToDisplay = new Integer(20);   //the max number of page links to display at one time

    private String query;
    private Collection requestForms = new ArrayList();     //the type of collection has to be accessible via index (i.e., requests[0])
    private Map cart = new HashMap(); //the place where we keep the selected request forms during the pagination; key-requestId, value-requestForm
    private String requestFormIndex;
    private String cmd = "";
    private boolean reset = false;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (reset) {
            reset = false;
            firstResult = 0;
            pageNbr = "1";
            cmd = "";
            cart = new HashMap();
            requestForms = new ArrayList();
            totalRequests = 0;
            displayNextLink = false;
        }
        for (Iterator iterator = requestForms.iterator(); iterator.hasNext();) {
            RequestForm requestForm = (RequestForm) iterator.next();
            requestForm.setSelected(Boolean.FALSE);
        }
        pageNbr = "1";
    }

    public String getPaginationDirection() {
        return paginationDirection;
    }

    public void setPaginationDirection(String paginationDirection) {
        this.paginationDirection = paginationDirection;
    }

    public Map getCart() {
        return cart;
    }

    public void setCart(Map cart) {
        this.cart = cart;
    }

    public boolean isDisplayNextLink() {
        return displayNextLink;
    }

    public void setDisplayNextLink(boolean displayNextLink) {
        this.displayNextLink = displayNextLink;
    }

    public int getFirstResult() {
        int page = 1;
        if (!StringUtils.nullOrBlank(pageNbr)) {
            page = Integer.parseInt(pageNbr);
        }
        return ((page - 1) * maxResults) + 1;
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

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }

    public Collection getCategories() {
        return categories;
    }

    public void setCategories(Collection categories) {
        this.categories = categories;
    }

    public Collection getStatuses() {
        return statuses;
    }

    public void setStatuses(Collection statuses) {
        this.statuses = statuses;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    public String getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

    public Collection getRequestors() {
        return requestors;
    }

    public void setRequestors(Collection requestors) {
        this.requestors = requestors;
    }

    public String getItemModel() {
        return itemModel;
    }

    public void setItemModel(String itemModel) {
        this.itemModel = itemModel;
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

    public String getRequestTrackingNumber() {
        return requestTrackingNumber;
    }

    public void setRequestTrackingNumber(String requestTrackingNumber) {
        this.requestTrackingNumber = requestTrackingNumber;
    }

    public String getDeliverTo() {
        return deliverTo;
    }

    public void setDeliverTo(String deliverTo) {
        this.deliverTo = deliverTo;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getIcnbr() {
        return icnbr;
    }

    public void setIcnbr(String icnbr) {
        this.icnbr = icnbr;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Collection getPriorities() {
        return priorities;
    }

    public void setPriorities(Collection priorities) {
        this.priorities = priorities;
    }

    public void setRequestForms(Collection requestForms) {
        this.requestForms = requestForms;
    }

    public Collection getRequestForms() {
        return requestForms;
    }

    public Collection getSelectedRequestForms() {
        Iterator iter = requestForms.iterator();
        ArrayList selectedForms = new ArrayList();
        while (iter.hasNext()) {
            RequestForm reqForm = (RequestForm) iter.next();
            if (reqForm.getSelected().booleanValue()) {
                selectedForms.add(reqForm);
            }
        }
        return selectedForms;
    }

    public Collection getCartRequestForms() {
        Collection requestFormsInCart = getCart().values();
        return new ArrayList(requestFormsInCart);
    }

    public void setRequestFormIndex(String requestFormIndex) {
        this.requestFormIndex = requestFormIndex;
    }


    public String getRequestFormIndex() {
        return requestFormIndex;
    }


    public void setCmd(String cmd) {
        this.cmd = cmd;
    }


    public String getCmd() {
        return cmd;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }


    public void setTotalRequests(int totalRequests) {
        this.totalRequests = totalRequests;
    }


    public int getTotalRequests() {
        return totalRequests;
    }

    public List getPages() {
        int a = totalRequests / maxResults;
        int b = totalRequests % maxResults;
        if (b != 0) {
            a++;
        }
        List pages = new ArrayList();
        for (int i = 1; i <= a; i++) {
            pages.add(new Integer(i));
        }
        return pages;
    }


    public void setPageNbr(String pageNbr) {
        this.pageNbr = pageNbr;
    }


    public String getPageNbr() {
        return pageNbr;
    }

    public String getNextPageNbr() {
        return Integer.parseInt(pageNbr) + 1 + "";
    }

    public String getPreviousPageNbr() {
        return Integer.parseInt(pageNbr) - 1 + "";
    }

    public Integer getPageOffset() {
        if (StringUtils.nullOrBlank(pageNbr)) {
            pageNbr = "1";
        }
        int page = Integer.parseInt(pageNbr);
        int result = Math.max(0, page - (pageLinksToDisplay.intValue() / 2));

        return new Integer(result);
    }


    public void setReset(boolean reset) {
        this.reset = reset;
    }


    public boolean isReset() {
        return reset;
    }
}