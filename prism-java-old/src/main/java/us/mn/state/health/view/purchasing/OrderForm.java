package us.mn.state.health.view.purchasing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.model.common.ExtOrgDetailMailingAddress;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.common.VendorContract;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.view.inventory.Command;

public class OrderForm extends ValidatorForm {
    private String orderId;
    private String vendorInstructions;
    private String billToAddressId;
    private Collection billToAddresses = new ArrayList();
    private String otherBillToAddress;
    private String remitToAddress;
    private String vendorId;
    private Collection vendors = new ArrayList();
    private Collection requestLineItemForms = new ArrayList();
    private Collection<OrderLineItemForm> orderLineItemForms = new ArrayList();
    private String orderLineItemKey;
    private String vendorContractId;
    private Collection vendorContracts = new ArrayList();
    private String vendorAccountId;
    private Collection vendorAccounts = new ArrayList();
    private String vendorAddressId;
    private Collection vendorAddresses = new ArrayList();
    private Collection vendorPhones = new ArrayList();
    private Collection vendorEmails = new ArrayList();
    private Collection vendorFaxes = new ArrayList();
    private Collection vendorReps = new ArrayList();

    private boolean odd = true;
    private String purchaseOrderNumber;
    private String purchaseOrderNumberType;
    private Order order;
    private Date insertionDate;
    private String oliFormIndex;
    private String cmd;
    private Collection facilities = new ArrayList();    //for drop-down of facilities when receiving
    private String receivingFacilityId;                 //the id of the facility the items where received at
    private Collection orderNoteForms = new ArrayList();
    private String noteFormKey = "";
    private String suspenseDate;
    private String shipToAddressId;
    private Collection shipToAddresses = new ArrayList();
    private String otherShipToAddress;
    private Boolean showNotes = Boolean.FALSE;   
    private String vendorNameFirstCharStart = "0";      //the first letter in the grouping of vendors by name
    private String vendorNameFirstCharEnd = "Z";        //the last letter in the goruping of vendors by name

    /** Resets Order form values */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        orderLineItemKey = null;
        odd = true;
    }

    /** Validates Order form values */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors(); //resetErrorsAfterValidation();
        if (!Command.SHOW_NOTES.equals(cmd) && !Command.HIDE_NOTES.equals(cmd) && !Command.ADD_NOTE.equals(cmd)) {
            errors = super.validate(mapping, request);
        }

        int i = 0;
        for (Iterator<OrderNoteForm> iterator = orderNoteForms.iterator(); iterator.hasNext();) {
            OrderNoteForm o =  iterator.next();
            if (!o.getRemoved())   validateNoteLength(errors,  o.getNote(), ++i);
        }
        
        validateVendorInstructionsLength(errors);

        if (Command.RECEIVE_ORDER.equals(cmd)) {
            i = 1;
            for(Iterator<OrderLineItemForm> iterator = orderLineItemForms.iterator(); iterator.hasNext();)
            {
                OrderLineItemForm olif = iterator.next();
                if (olif.getUnitMismatch()) {
                    validateBuyUnitDisplenseUnit(errors,olif.getQuantityReceived(),olif.getQuantityReceivedInDispenseUnits(),i);
                }
                ++i;
            }
        }
        return errors;
    }


    private void validateNoteLength(ActionErrors errors, String note, int i) {
        if (note != null && !(note.length() <= 500) ) {
                errors.add("OrderNoteForm.Note", new ActionMessage("errors.custom", "Note #" + i + ", can not be longer than 500 characters.(Current Length = "+ note.length()+")"));
        }
    }

    private void validateVendorInstructionsLength(ActionErrors errors) {
        if (vendorInstructions != null && !(vendorInstructions.length() <= 1000) ) {
                errors.add("vendorInstructions", new ActionMessage("errors.custom", "Vendor Instructions can not be longer than 1000 characters.(Current Length = "+ vendorInstructions.length()+")"));
        }
    }

    private void validateBuyUnitDisplenseUnit(ActionErrors errors, String quanRec, String quanRecInDispenseUnits, int i) {
        Boolean OK =false;
        Boolean isANumber = true;
//check for non-numeric entry
        if ((quanRec == null || quanRec.equals("")) && (quanRecInDispenseUnits == null || quanRecInDispenseUnits.equals(""))) {
            OK = true;
        }
        else if (!(quanRec == null || quanRec.equals("")) && !(quanRecInDispenseUnits == null || quanRecInDispenseUnits.equals(""))) {
            Number l = 0;
            try {
                l = Long.parseLong(quanRec);
            }
            catch (NumberFormatException nfe) {
                errors.add("quantityReceivedInDispenseUnits", new ActionMessage("errors.custom", "Line Item #: " + i + ", Buy Units Rec'd Now must be a number."));
                isANumber = false;
            }
            try {
                l = Long.parseLong(quanRecInDispenseUnits);
            }
            catch (NumberFormatException nfe) {
                errors.add("quantityReceivedInDispenseUnits", new ActionMessage("errors.custom", "Line Item #: " + i + ", Dispense Units Rec'd Now must be a number."));                
                nfe.printStackTrace();
                isANumber = false;
            }

            if (isANumber) {
                if ( new Long(quanRec) != 0 && new Long(quanRecInDispenseUnits) != 0) {
                    OK = true;
                }
                else {
                            errors.add("quantityReceivedInDispenseUnits", new ActionMessage("errors.custom", "Line Item #: " + i + ", Buy Units Rec'd Now AND Dispense Units Rec'd Now can not = 0 if either is specified."));
                }
            }
        }

        if (!OK) {
            errors.add("quantityReceivedInDispenseUnits", new ActionMessage("errors.custom", "Line Item #: " + i + ", Buy Unit does NOT match Dispense Unit, So, Both Buy Units Rec'd Now AND Dispense Units Rec'd Now must be entered and can not = 0 if either is specified."));
        }
    }


    public MailingAddress getShipToAddress() {
        return (MailingAddress)CollectionUtils.getObjectFromCollectionById(shipToAddresses, shipToAddressId, "mailingAddressId");
    }
    
    public MailingAddress getVendorAddress() {
        ExtOrgDetailMailingAddress extOrgDtlMlngAddr = 
            (ExtOrgDetailMailingAddress)CollectionUtils.getObjectFromCollectionById(vendorAddresses, vendorAddressId, "mailingAddress.mailingAddressId");
        if(extOrgDtlMlngAddr != null) {
            return extOrgDtlMlngAddr.getMailingAddress();
        }
        return null;
    }
    
    
    public VendorContract getVendorContract() {
        return (VendorContract)CollectionUtils.getObjectFromCollectionById(vendorContracts, vendorContractId, "vendorContractId");
    }
    
    public Double getBuyUnitsTotalCost() {
        double total = 0d;
        for(Iterator i= orderLineItemForms.iterator(); i.hasNext();) {
            OrderLineItemForm oli = (OrderLineItemForm)i.next();
            if(!oli.getRemoved().booleanValue()) {
                double lineCost = oli.getOrderLineItem().getBuyUnitTotalCost().doubleValue();
                total += lineCost;
            }
        }
        return new Double(total);
    }

    /** Accessor Methods */
    
    public MailingAddress getBillToAddress() {
        return (MailingAddress)CollectionUtils.getObjectFromCollectionById(billToAddresses, billToAddressId, "mailingAddressId");
    }
    
    
    public Vendor getVendor() {
        return (Vendor)CollectionUtils.getObjectFromCollectionById(vendors, vendorId, "vendorId");
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public String getOrderId() {
        return orderId;
    }


    public void setVendorInstructions(String vendorInstructions) {
        this.vendorInstructions = vendorInstructions;
    }


    public String getVendorInstructions() {
        return vendorInstructions;
    }


    public void setBillToAddressId(String billToAddressId) {
        this.billToAddressId = billToAddressId;
    }


    public String getBillToAddressId() {
        return billToAddressId;
    }

    public void setOtherBillToAddress(String otherBillToAddress) {
        this.otherBillToAddress = otherBillToAddress;
    }


    public String getOtherBillToAddress() {
        return otherBillToAddress;
    }


    public void setRemitToAddress(String remitToAddress) {
        this.remitToAddress = remitToAddress;
    }


    public String getRemitToAddress() {
        return remitToAddress;
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

    public void setOrderLineItemForms(Collection orderLineItemForms) {
        this.orderLineItemForms = orderLineItemForms;
    }


    public Collection getOrderLineItemForms() {
        return orderLineItemForms;
    }


    public void setRequestLineItemForms(Collection requestLineItemForms) {
        this.requestLineItemForms = requestLineItemForms;
    }


    public Collection getRequestLineItemForms() {
        return requestLineItemForms;
    }


    public void setBillToAddresses(Collection billToAddresses) {
        this.billToAddresses = billToAddresses;
    }


    public Collection getBillToAddresses() {
        return billToAddresses;
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

    public void setOrderLineItemKey(String orderLineItemKey) {
        this.orderLineItemKey = orderLineItemKey;
    }


    public String getOrderLineItemKey() {
        return orderLineItemKey;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }


    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }


    public void setVendorContractId(String vendorContractId) {
        this.vendorContractId = vendorContractId;
    }


    public String getVendorContractId() {
        return vendorContractId;
    }

    public int getOrderLnItemFormCount() {
        return CollectionUtils.getMatchingItems(orderLineItemForms, Boolean.FALSE, "removed").size();
    }
    
    public int getReqLnItemFormCount() {
        return CollectionUtils.getMatchingItems(requestLineItemForms, Boolean.FALSE, "removedFromOrder").size();
    }


    public void setOrder(Order order) {
        this.order = order;
    }


    public Order getOrder() {
        return order;
    }


    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }


    public Date getInsertionDate() {
        return insertionDate;
    }


    public void setVendorContracts(Collection vendorContracts) {
        this.vendorContracts = vendorContracts;
    }


    public Collection getVendorContracts() {
        return vendorContracts;
    }


    public void setVendorAddressId(String vendorAddressId) {
        this.vendorAddressId = vendorAddressId;
    }


    public String getVendorAddressId() {
        return vendorAddressId;
    }


    public void setVendorAddresses(Collection vendorAddresses) {
        this.vendorAddresses = vendorAddresses;
    }


    public Collection getVendorAddresses() {
        return vendorAddresses;
    }


    public void setOliFormIndex(String oliFormIndex) {
        this.oliFormIndex = oliFormIndex;
    }


    public String getOliFormIndex() {
        return oliFormIndex;
    }


    public void setCmd(String cmd) {
        this.cmd = cmd;
    }


    public String getCmd() {
        return cmd;
    }
    
    public boolean getValidBillToAddress() {
        return getBillToAddress() != null || !StringUtils.nullOrBlank(otherBillToAddress);
    }
    
    public boolean getOrderLineItemsEmpty() {
        return orderLineItemForms.isEmpty();
    }
    
    public boolean getSelectedReqLnItemFormsEmpty() {
        return (CollectionUtils.getMatchingItems(requestLineItemForms, Boolean.TRUE, "selected").size() == 0);
    }


    public void setFacilities(Collection facilities) {
        this.facilities = facilities;
    }


    public Collection getFacilities() {
        return facilities;
    }


    public void setReceivingFacilityId(String receivingFacilityId) {
        this.receivingFacilityId = receivingFacilityId;
    }


    public String getReceivingFacilityId() {
        return receivingFacilityId;
    }


    public void setOrderNoteForms(Collection orderNoteForms) {
        this.orderNoteForms = orderNoteForms;
    }


    public Collection getOrderNoteForms() {
        return orderNoteForms;
    }


    public void setNoteFormKey(String noteFormKey) {
        this.noteFormKey = noteFormKey;
    }


    public String getNoteFormKey() {
        return noteFormKey;
    }


    public void setSuspenseDate(String suspenseDate) {
        this.suspenseDate = suspenseDate;
    }


    public String getSuspenseDate() {
        return suspenseDate;
    }


    public void setShipToAddressId(String shipToAddressId) {
        this.shipToAddressId = shipToAddressId;
    }


    public String getShipToAddressId() {
        return shipToAddressId;
    }


    public void setShipToAddresses(Collection shipToAddresses) {
        this.shipToAddresses = shipToAddresses;
    }


    public Collection getShipToAddresses() {
        return shipToAddresses;
    }


    public void setOtherShipToAddress(String otherShipToAddress) {
        this.otherShipToAddress = otherShipToAddress;
    }


    public String getOtherShipToAddress() {
        return otherShipToAddress;
    }


    public void setShowNotes(Boolean showNotes) {
        this.showNotes = showNotes;
    }


    public Boolean getShowNotes() {
        return showNotes;
    }


    public void setPurchaseOrderNumberType(String purchaseOrderNumberType) {
        this.purchaseOrderNumberType = purchaseOrderNumberType;
    }


    public String getPurchaseOrderNumberType() {
        return purchaseOrderNumberType;
    }


    public void setVendorNameFirstCharStart(String vendorNameFirstCharStart) {
        this.vendorNameFirstCharStart = vendorNameFirstCharStart;
    }


    public String getVendorNameFirstCharStart() {
        return vendorNameFirstCharStart;
    }


    public void setVendorNameFirstCharEnd(String vendorNameFirstCharEnd) {
        this.vendorNameFirstCharEnd = vendorNameFirstCharEnd;
    }


    public String getVendorNameFirstCharEnd() {
        return vendorNameFirstCharEnd;
    }


    public void setVendorAccountId(String vendorAccountId) {
        this.vendorAccountId = vendorAccountId;
    }


    public String getVendorAccountId() {
        return vendorAccountId;
    }


    public void setVendorAccounts(Collection vendorAccounts) {
        this.vendorAccounts = vendorAccounts;
    }


    public Collection getVendorAccounts() {
        return vendorAccounts;
    }

    
    public Collection getVendorPhones() {
        return vendorPhones;
    }

    public void setVendorPhones(Collection vendorPhones) {
        this.vendorPhones = vendorPhones;
    }

    public Collection getVendorEmails() {
        return vendorEmails;
    }

    public void setVendorEmails(Collection vendorEmails) {
        this.vendorEmails = vendorEmails;
    }

    public Collection getVendorFaxes() {
        return vendorFaxes;
    }

    public void setVendorFaxes(Collection vendorFaxes) {
        this.vendorFaxes = vendorFaxes;
    }

    public Collection getVendorReps() {
        return vendorReps;
    }

    public void setVendorReps(Collection vendorReps) {
        this.vendorReps = vendorReps;
    }

    //Order Line Items are no longer being renumbered sequentially starting with 1 so
    // the new order line item is assigned the next number greater than the current max used.
    // Return the maximum existing order line item number for this order.
    public int maxOrderLineItemNumber() {
        int maxOliNbr = 0;
        for (Iterator olif = orderLineItemForms.iterator(); olif.hasNext();) {
            OrderLineItemForm oliForm =  (OrderLineItemForm) olif.next();
            if (oliForm.getLineItemNumber() > maxOliNbr) {
                maxOliNbr =oliForm.getLineItemNumber();
            }
        }
        return maxOliNbr;
    }

}
