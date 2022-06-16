package us.mn.state.health.builder.purchasing

import groovy.util.logging.Log4j;

import us.mn.state.health.builder.email.SensitiveAssetNewEmailBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.common.VendorAccount;
import us.mn.state.health.model.common.VendorContract;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.model.purchasing.OrderNote;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.EmailBusinessDelegate;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.purchasing.OrderForm;
import us.mn.state.health.view.purchasing.OrderLineItemForm;
import us.mn.state.health.view.purchasing.OrderNoteForm;
import us.mn.state.health.builder.email.EmailBuilder;
import us.mn.state.health.builder.email.FixedAssetNewEmailBuilder;
import us.mn.state.health.matmgmt.director.email.EmailDirector;

@Log4j
public class OrderBuilder {
    private Order order;
    private OrderForm orderForm;
    private User actor;
    private DAOFactory daoFactory;
    private RequestLineItemForm reqLnItmForm;

    public OrderBuilder(Order order, OrderForm orderForm, User actor, DAOFactory daoFactory) {
        this.order = order;
        this.orderForm = orderForm;
        this.actor = actor;
        this.daoFactory = daoFactory;
    }
    
    public OrderBuilder(Order order, OrderForm orderForm, RequestLineItemForm reqLnItmForm, DAOFactory daoFactory) {
        this.order = order;
        this.orderForm = orderForm;
        this.reqLnItmForm = reqLnItmForm;
        this.daoFactory = daoFactory;
    }

    /* Build meta order properties */

    public void buildMetaProperties() {
        order.buildInsertMetaProperties(actor.getUsername());
    }
    
    public void buildBillToAddress() throws InfrastructureException {
        MailingAddress address = orderForm.getBillToAddress();
        order.setBillToAddress(address);
    }
    
    public void buildNotes() throws InfrastructureException {
        Collection orderNotes = order.getOrderNotes();
        Collection orderNoteForms = orderForm.getOrderNoteForms();
        for(Iterator i = orderNoteForms.iterator(); i.hasNext();) {
            OrderNoteForm noteForm = (OrderNoteForm)i.next();
            Long noteId = null;
            if(!StringUtils.nullOrBlank(noteForm.getOrderNoteId())) { //Persistent note
                noteId = new Long(noteForm.getOrderNoteId());
            }
            if(noteForm.getRemoved()) {
                if(noteId) {
                    CollectionUtils.removeMatchingItem(orderNotes, noteId, "orderNoteId");
                }
                i.remove();
            }
            else {
                if(noteForm?.getNote()) {
                    OrderNote note = null;
                    if(noteId) { //Persistent note
                        note = (OrderNote)CollectionUtils.getObjectFromCollectionById(orderNotes, noteId, "orderNoteId");
                    }
                    else { //New note
                        note = new OrderNote();
                        note.setOrder(order);
                    }
                    OrderNoteBuilder builder = new OrderNoteBuilder(note, noteForm);
                    builder.buildSimpleProperties();
                    if(!noteId) { //Add new note to Order
                        order.getOrderNotes().add(note);
                    }
                }
            }
        }
    }
    
    /**
     * Builds OrderLineItems
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildOrderLineItems(Boolean computerPurchase) throws InfrastructureException {
        String storedFixedAssetType = "";
        Collection oliForms = orderForm.getOrderLineItemForms();
        for(Iterator i = oliForms.iterator(); i.hasNext(); ) {
            OrderLineItemForm oliForm = (OrderLineItemForm)i.next();
            OrderLineItem oli;
            Long oliId = oliForm.getOrderLineItem().getOrderLineItemId();
                
            if(!oliId) {//New OLI
                oli = new OrderLineItem();
                oliForm.setOrderLineItem(oli);
            }
            else {//existing OLI
                oliId = new Long(oliForm.getOrderLineItemId());
                oli = (OrderLineItem)CollectionUtils.getObjectFromCollectionById(order.getOrderLineItems(), oliId, "orderLineItemId");
                storedFixedAssetType = oli.getAssetsType();
            }
            OrderLineItemBuilder oliBuilder = new OrderLineItemBuilder(oli, order, oliForm, orderForm, daoFactory);

            boolean removed = oliForm.getRemoved();
            if(removed) { //If removed from Order
                i.remove(); //Remove form
                if(oliId) { //Persistent OrderLineItem, so make transient
                    oliBuilder.buildRequestLineItems(computerPurchase);
                    //orderLnItm.setOrder(null);
                    oli = (OrderLineItem)CollectionUtils.removeMatchingItem(order.getOrderLineItems(), oliId, "orderLineItemId");
                }
            }
            else {
                if(oliForm.getDirty()) {
                    oliBuilder.buildSimpleProperties();
                    //oliBuilder.buildShipToAddress();
                    oliBuilder.buildBuyUnit();
                    oliBuilder.buildItem();
                }
                oliBuilder.buildStatusForPurchasing(computerPurchase);
                if(!oliId) {
                    oliBuilder.buildOrder();
                    order.getOrderLineItems().add(oli);
                }

                if ((oli?.getAssetsType()?.equals(OrderLineItem.FIXED_ASSET_TYPE) || oli?.getAssetsType()?.equals(OrderLineItem.SENSITIVE_ASSET_TYPE))  &&
                         !oli.getAssetsEmailSent() && oli.getStatus().getStatusCode().equalsIgnoreCase(Status.ORDERED)) {

                    try {
                        EmailBean emailBean = new EmailBean();
                        EmailBuilder emailBuilder;
                        if (oli.getAssetsType().equals(OrderLineItem.FIXED_ASSET_TYPE)) {
                            emailBuilder = new FixedAssetNewEmailBuilder(oli, emailBean, daoFactory);
                        } else {  //
                            emailBuilder = new SensitiveAssetNewEmailBuilder(oli, emailBean, daoFactory);
                        }
                        EmailDirector emailDirector = new EmailDirector(emailBuilder);
                        emailDirector.construct();
                        EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
                        emailBusinessDelegate.sendEmail(emailBean);

                        oli.setAssetsEmailSent(new Date());
                    } catch (Exception ignore) {
                    }
                }
                oliBuilder.buildRequestLineItems(computerPurchase);
            }
        }
    }
    
    public void buildPurchaser() {
        order.setPurchaser(actor);
    }
    
    public void buildReqLineItems() throws InfrastructureException {
        Collection requestLineItemForms = orderForm.getRequestLineItemForms();
        Collection reqLnItms = order.getRequestLineItems();
             
        Iterator i = requestLineItemForms.iterator();                                                              
        while(i.hasNext()) {
            RequestLineItemForm rliForm = (RequestLineItemForm)i.next();
            Long reqLnItmId = rliForm.getRequestLineItem().getRequestLineItemId();
            RequestLineItem rli
            if(rliForm.getRemovedFromOrder()) { //Remove if marked
                i.remove();
                rli = daoFactory.getRequestLineItemDAO().getRequestLineItemById(reqLnItmId, false);
                if(rli) {
                    if(rli.getStatus().getStatusCode().equals(Status.PENDING_BUILDING_ORDER)) {
                        Status wfp = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.WAITING_FOR_PURCHASE);
                        rli.setStatus(wfp);
                        rli.setPurchaser(null);
                    }
                    CollectionUtils.removeMatchingItem(reqLnItms, reqLnItmId, "requestLineItemId");
                }
            }
            else {
                boolean alreadyWithOrder = CollectionUtils.inList(reqLnItms, "requestLineItemId", reqLnItmId);
                if(!alreadyWithOrder) {
                    rli = daoFactory.getRequestLineItemDAO().getRequestLineItemById(reqLnItmId, false);
                    Status pbo = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.PENDING_BUILDING_ORDER);
                    rli.setStatus(pbo);
                    rli.setPurchaser(actor);
                    reqLnItms.add(rli);
                }
            }
        }
    }
    
    /**
     * Removes any Request Line Items associated with this order that do not 
     * have an Order Line Item, and sets their status to Waiting For Purchase. 
     * The purpose is to release any RLI's that have not been associated with an OLI
     * when the user enters a PO number.  
     */
    public void releaseUnorderedRLIs() throws InfrastructureException {
        Status wfp = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST,
                                                                                 Status.WAITING_FOR_PURCHASE);
        for(Iterator iter = order.getRequestLineItems().iterator(); iter.hasNext(); ) {
            RequestLineItem rli = (RequestLineItem)iter.next();            
            if(!rli?.getOrderLineItem()) {
                rli.setStatus(wfp);
                iter.remove();
            }
        }
    }
    
    /**
     * Assigns ship-to address to Order
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildShipToAddress() throws InfrastructureException {
        MailingAddress mailingAddress = orderForm.getShipToAddress();
        order.setShipToAddress(mailingAddress);
    }

    /**
     * Build simple order properties from the orderForm 
     **/
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(order, orderForm);
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
    
    public void buildVendor() throws InfrastructureException {
        if(orderForm?.getVendorId()) {
            Vendor vendor = daoFactory.getVendorDAO().getVendorById(new Long(orderForm.getVendorId()), false);
            order.setVendor(vendor);
        }
        else {
            order.setVendor(null);
        }        
    }
    public void buildDummyVendor() throws InfrastructureException {
        if(orderForm?.getVendorId()) {
            Vendor vendor = daoFactory.getVendorDAO().getVendorById(new Long(orderForm.getVendorId()), false);
            order.setVendor(vendor);
        }
        else {
            Vendor vendor = daoFactory.getVendorDAO().getVendorByOrgCode('MN-IT', false);
            order.setVendor(vendor);
        }
    }

    
    public void buildVendorContract() throws InfrastructureException {
       if(orderForm?.getVendorContractId()) {
            VendorContract vendorContract = daoFactory.getVendorContractDAO().getVendorContractById(new Long(orderForm.getVendorContractId()), false);
            order.setVendorContract(vendorContract);
       } else {
           order.setVendorContract(null);
       }
    }
    
    public void buildVendorAccount() throws InfrastructureException {
       if(orderForm?.getVendorAccountId()) {
           VendorAccount vendorAccount = daoFactory.getVendorAccountDAO().getVendorAccountById(new Long(orderForm.getVendorAccountId()), false);
           order.setVendorAccount(vendorAccount);
       } else {
            order.setVendorAccount(null);
       }
    }
    
    public void buildVendorAddress() throws InfrastructureException {
        if(orderForm?.getVendorAddressId()) {
            MailingAddress vendorAddress = daoFactory.getMailingAddressDAO().getMailingAddressById(new Long(orderForm.getVendorAddressId()), false);
            order.setVendorAddress(vendorAddress);
        } else {
            order.setVendorAddress(null);
        }
    }

    public Order getOrder() {
        return order;
    }
}