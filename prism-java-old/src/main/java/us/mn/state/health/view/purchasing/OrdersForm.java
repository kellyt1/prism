package us.mn.state.health.view.purchasing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.Vendor;

public class OrdersForm extends ValidatorForm {    
    private String orderId;
    private String itemDescription;    
    private String vendorId;    
    private Collection vendors = new ArrayList();    
    private String purchaseOrderNumber;    
    private String purchaseOrderNumberType;
    private String statusId;    
    private Collection statuses = new ArrayList();    
    private String purchaserId;    
    private Collection purchasers = new ArrayList();    
    private String requestId;    
    private String requestorId;    
    private Collection requestors = new ArrayList();    
    private String orderedFrom;
    private String orderedTo;    
    private String input = "";    
    private Collection orders = new ArrayList();    
    private String selectedOrderId;
    
    
    /** Validates Order form values */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }
    
    /** Resets Order form values */
    public void reset(ActionMapping mapping, HttpServletRequest request) {        
        orderId = null;
        itemDescription = null;
        vendorId = null;
        purchaseOrderNumber = null;
        statusId = null;
        purchaserId = null;
        requestId = null;
        requestorId = null;
        orderedFrom = null;
        orders = new ArrayList();
        try {
            orderedTo = DateUtils.toString(new Date());
        }
        catch(Exception e) {}        
    }
    
    public Status getStatus() {
        return (Status)CollectionUtils.getObjectFromCollectionById(statuses, statusId, "statusId");
    }
    
    public Vendor getVendor() {
        return (Vendor)CollectionUtils.getObjectFromCollectionById(vendors, vendorId, "vendorId");
    }
    
    public Person getPurchaser() {
        return (Person)CollectionUtils.getObjectFromCollectionById(purchasers, purchaserId, "personId");
    }
    
    public Person getRequestor() {
        return (Person)CollectionUtils.getObjectFromCollectionById(requestors, requestorId, "personId");
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public String getOrderId() {
        return orderId;
    }


    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }


    public String getItemDescription() {
        return itemDescription;
    }


    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }


    public String getVendorId() {
        return vendorId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }


    public String getStatusId() {
        return statusId;
    }


    public void setPurchaserId(String purchaserId) {
        this.purchaserId = purchaserId;
    }


    public String getPurchaserId() {
        return purchaserId;
    }


    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    public String getRequestId() {
        return requestId;
    }


    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }


    public String getRequestorId() {
        return requestorId;
    }


    public void setOrderedFrom(String orderedFrom) {
        this.orderedFrom = orderedFrom;
    }


    public String getOrderedFrom() {
        return orderedFrom;
    }


    public void setOrderedTo(String orderedTo) {
        this.orderedTo = orderedTo;
    }


    public String getOrderedTo() {
        return orderedTo;
    }


    public void setOrders(Collection orders) {
        this.orders = orders;
    }


    public Collection getOrders() {
        return orders;
    }


    public void setVendors(Collection vendors) {
        this.vendors = vendors;
    }


    public Collection getVendors() {
        return vendors;
    }


    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }


    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }


    public void setStatuses(Collection statuses) {
        this.statuses = statuses;
    }


    public Collection getStatuses() {
        return statuses;
    }


    public void setPurchasers(Collection purchasers) {
        this.purchasers = purchasers;
    }


    public Collection getPurchasers() {
        return purchasers;
    }


    public void setRequestors(Collection requestors) {
        this.requestors = requestors;
    }


    public Collection getRequestors() {
        return requestors;
    }


    public void setInput(String input) {
        this.input = input;
    }


    public String getInput() {
        return input;
    }


    public void setSelectedOrderId(String selectedOrderId) {
        this.selectedOrderId = selectedOrderId;
    }


    public String getSelectedOrderId() {
        return selectedOrderId;
    }


    public void setPurchaseOrderNumberType(String purchaseOrderNumberType) {
        this.purchaseOrderNumberType = purchaseOrderNumberType;
    }


    public String getPurchaseOrderNumberType() {
        return purchaseOrderNumberType;
    }
}