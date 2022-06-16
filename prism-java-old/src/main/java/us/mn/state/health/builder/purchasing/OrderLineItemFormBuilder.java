package us.mn.state.health.builder.purchasing;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.lang.WrapperUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.purchasing.AssetType;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.purchasing.OrderLineItemForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class OrderLineItemFormBuilder {
    private OrderLineItemForm orderLineItemForm;
    private OrderLineItem orderLineItem;
    private Collection rliForms;
    private DAOFactory daoFactory;    
    
    /**
     * Parameterized Constructor
     * @param daoFactory data access mechanism
     * @param rliForms represents RequestLineItems to be associated with this OrderLineItem
     * @param orderLineItemForm form to be built
     */
    public OrderLineItemFormBuilder(OrderLineItemForm orderLineItemForm,
                                    Collection rliForms, 
                                    DAOFactory daoFactory) {
        this.orderLineItemForm = orderLineItemForm;
        this.daoFactory = daoFactory;
        this.rliForms = rliForms;
    }
    
    /**
     * Parameterized Constructor
     * @param daoFactory data access mechanism
     * @param rliForms represents RequestLineItems to be associated with this OrderLineItem
     * @param orderLineItemForm form to be built
     */
    public OrderLineItemFormBuilder(OrderLineItemForm orderLineItemForm,
                                    OrderLineItem orderLineItem,
                                    Collection rliForms, 
                                    DAOFactory daoFactory) {
        this(orderLineItemForm, orderLineItem, daoFactory);
        this.rliForms = rliForms;
    }
    
    /**
     * Parameterized Constructor
     * @param daoFactory data access mechanism
     * @param orderLineItemForm form to be built
     */
    public OrderLineItemFormBuilder(OrderLineItemForm orderLineItemForm, 
                                    DAOFactory daoFactory) {
        this.orderLineItemForm = orderLineItemForm;
        this.daoFactory = daoFactory;
    }
    
    /**
     * Parameterized Constructor
     * @param daoFactory data access mechanism
     * @param orderLineItem used to build OrderLineItemForm
     * @param orderLineItemForm form to be built
     */
    public OrderLineItemFormBuilder(OrderLineItemForm orderLineItemForm,
                                    OrderLineItem orderLineItem,
                                    DAOFactory daoFactory) {
        this(orderLineItemForm, daoFactory);
        this.orderLineItem = orderLineItem;
    }

    /**
     * Build default orderLineItemForm properties
     */
    public void buildDefaultProperties() {
    }
    
    /**
     * Builds dirty flag indicating that OrderLineItem is new or has been edited
     */
    public void buildDirty() {
        orderLineItemForm.setDirty(Boolean.TRUE);
    }
    
    /**
     * Builds dirty flag indicating that OrderLineItem is not new or has not been edited
     */
    public void buildNotDirty() {
        orderLineItemForm.setDirty(Boolean.FALSE);
    }
    
    /**
     * Builds default item from RequestLineItems that have been added to OrderLineItem
     */
    public void buildItem() throws InfrastructureException {
        if(orderLineItem != null) {           
            if(orderLineItem.getItem() != null) {
                Item item = daoFactory.getItemDAO().getItemById(orderLineItem.getItem().getItemId(), false);
                if(item.getItemType().equals(Item.STOCK_ITEM)) {
                    StockItem si = daoFactory.getStockItemDAO().getStockItemById(item.getItemId(), false);
                    orderLineItemForm.setItem(si);
                }
                else {
                    orderLineItemForm.setItem(item);
                }    
            }
            else {
                orderLineItemForm.setItem(null);
            }
        }
    }
    
    
    public void buildItemFromReqLnItms() {
        if(rliForms != null && rliForms.size() > 0) {
            //Take first RLI orderLineItemForm and use as a template
            RequestLineItemForm rliForm = (RequestLineItemForm)rliForms.iterator().next();
            orderLineItemForm.setItem(rliForm.getRequestLineItem().getItem()); 
        }
    }
    
    /**
     * Builds default description from RequestLineItems that have been added to OrderLineItem
     */
    public void buildDescriptionFromReqLnItms() {
        if(rliForms != null && rliForms.size() > 0) {
            if(StringUtils.nullOrBlank(orderLineItemForm.getItemDescription())) {
                //Take first Req Ln Itm orderLineItemForm and use as a template
                RequestLineItemForm reqLnItemForm = (RequestLineItemForm)rliForms.iterator().next();
                if(reqLnItemForm.getRequestLineItem().getItem() != null) {
                    orderLineItemForm.setItemDescription(reqLnItemForm.getRequestLineItem().getItem().getDescription());
                }
                else {
                    orderLineItemForm.setItemDescription(reqLnItemForm.getRequestLineItem().getItemDescription());
                }
            }
        }
    }
    
    /**
     * Builds default vendorCatalogNbr from RequestLineItems that have been added to OrderLineItem
     */
    public void buildVendorCatalogNbrFromReqLnItms() {
        if(rliForms != null && rliForms.size() > 0) {
            for(Iterator i = rliForms.iterator(); i.hasNext(); ) {
                RequestLineItemForm rliForm = (RequestLineItemForm)i.next();
                if(rliForm.getRequestLineItem() != null && 
                   !StringUtils.nullOrBlank(rliForm.getRequestLineItem().getSuggestedVendorCatalogNumber())) {
                       orderLineItemForm.setVendorCatalogNbr(rliForm.getRequestLineItem().getSuggestedVendorCatalogNumber());
                       break;
                   }
            }
        }
    }
    
    public void buildBuyUnitFromReqLnItms() {
        if(rliForms != null && rliForms.size() > 0) {
            Unit buyUnit = null;
            RequestLineItemForm reqLnItmForm = (RequestLineItemForm)rliForms.iterator().next();
            if(reqLnItmForm.getRequestLineItem().getItem() != null) {
                buyUnit = reqLnItmForm.getRequestLineItem().getItem().getDispenseUnit();
            }
            else {
                buyUnit = reqLnItmForm.getRequestLineItem().getUnit();
            }
            if(buyUnit != null) {
                orderLineItemForm.setBuyUnitId(buyUnit.getUnitId().toString());
            }
        }
    }
    
    public void buildBuyUnitCostFromReqLnItms() {
        if(rliForms != null && rliForms.size() > 0) {
            Double buyUnitCost = null;
            RequestLineItemForm reqLnItmForm = (RequestLineItemForm)rliForms.iterator().next();
            if(reqLnItmForm.getRequestLineItem().getItem() != null) {
                buyUnitCost = reqLnItmForm.getRequestLineItem().getItem().getDispenseUnitCost();
            }
            else {
                buyUnitCost = reqLnItmForm.getRequestLineItem().getItemCost();
            }
            if(buyUnitCost != null) {
                orderLineItemForm.setBuyUnitCost(buyUnitCost.toString());
            }
        }
    }
    
    /* moved addresses to Order
     * Builds addresses used for shipping and billing
     * @throws us.mn.state.health.common.exceptions.InfrastructureException     
    public void buildAddresses() throws InfrastructureException {    
        Collection addBkExtSources = 
            daoFactory.getAddressBookDAO().findAddBkExtSourcesWithGroupCode(Group.BUYER_CODE);
        addBkExtSources = 
            CollectionUtils.getMatchingItems(addBkExtSources, Boolean.TRUE, "externalOrgDetail.primaryMailingAddress.shipToType");
            
        orderLineItemForm.setAddresses(addBkExtSources);
    }
    */
    
    /**
     * Builds OrderLineItem used for display purposes
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildOrderLineItem() throws InfrastructureException {
        orderLineItemForm.setOrderLineItem(orderLineItem);
        if(orderLineItem != null) {
            orderLineItemForm.setOrderLineItemId(orderLineItem.getOrderLineItemId().toString());
        }
    }
    
    /**
     * Build default quantity from RequestLineItems that have been added to OrderLineItem
     */
    public void buildQuantityFromReqLnItms() {
        Collection allrliForms = orderLineItemForm.getRequestLineItemForms();
        String qty = "0";
        for(Iterator i = allrliForms.iterator(); i.hasNext();) {
            RequestLineItemForm reqLnItemForm = (RequestLineItemForm)i.next();
            //Ignore forms that have been removed from OrderLineItem
            if(!reqLnItemForm.getRemovedFromOrderLineItem().booleanValue()) {
                if(reqLnItemForm.getRequestLineItem().getQuantity() != null) {
                    qty = WrapperUtils.add(qty, reqLnItemForm.getRequestLineItem().getQuantity().toString());
                }
            }
        }
        orderLineItemForm.setQuantity(qty);
        //why are we doing this here? This is an OLI FORM builder, not an OLI model obj builder
        if(orderLineItem != null) {
            orderLineItem.setQuantity(new Integer(qty));      
        }
    }
    
    /**
     * Build RequestLineItemForms from RequestLineItems that have been added to OrderLineItem
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildAddReqLineItemForms() throws InfrastructureException {
        if(rliForms != null) {
            CollectionUtils.assignValueToAll(rliForms, Boolean.FALSE, "removedFromOrderLineItem");
            CollectionUtils.assignValueToAll(rliForms, Boolean.TRUE, "removedFromOrder");
            CollectionUtils.assignValueToAll(rliForms, Boolean.FALSE, "selected");
            CollectionUtils.addWhereItemNotPresent(rliForms, orderLineItemForm.getRequestLineItemForms(), "requestLineItem.requestLineItemId");
        }
    }
    
    /**
     * Build RLI forms from OrderLineItem RequestLineItems
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildReqLineItemFormsFromOrderLineItemRequests() throws InfrastructureException {
        if(orderLineItem != null) {
            Collection rlis = orderLineItem.getRequestLineItems();
            Collection reqLnItmForms = new ArrayList();
            for(Iterator i = rlis.iterator(); i.hasNext();) {
                RequestLineItemForm rliForm = new RequestLineItemForm();
                RequestLineItem rli = (RequestLineItem)i.next();
                rliForm.setRequestLineItem(rli);
                reqLnItmForms.add(rliForm);
            }
            orderLineItemForm.setRequestLineItemForms(reqLnItmForms);
        }
    }

    /** 
     * Build simple orderLineItemForm properties from the orderLineItem
     */

    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(orderLineItemForm, orderLineItem);
            if(orderLineItem.getBuyUnit() != null) {
                orderLineItemForm.setBuyUnitId(orderLineItem.getBuyUnit().getUnitId().toString());
            }
            /* moved addresses to Order
            if(orderLineItem.getShipToAddress() != null) {
                orderLineItemForm.setShipToAddressId(orderLineItem.getShipToAddress().getAddressBookExternalSourceId().toString());
            }
            */
            if(orderLineItem.getStatus() != null) {
                orderLineItemForm.setStatusCode(orderLineItem.getStatus().getStatusCode());
            }
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
    
    /**
     * Build OrderLineItemForm buy units
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildUnits() throws InfrastructureException {
        Collection units = daoFactory.getUnitDAO().findAll(false);
        orderLineItemForm.setUnits(units);
    }
    
    /**
     * Build purchasing OrderLineItem statuses
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildPurchasingStatuses() throws InfrastructureException {
        String[] statusCodes = null;
        String[] pendingStatusCodes = {Status.PENDING_INCUMBRANCE, Status.CANCELED};
        String[] orderedStatusCodes = {Status.MNIT_ENCUMBERED, Status.SWIFT_ORDERED,Status.ORDERED, Status.ORDERED_ON_BACK_ORDER, Status.CANCELED};
        String[] receivedStatusCodes = {Status.RECEIVED, Status.RECEIVED_PARTIAL, Status.CANCELED};
        
        Status status = orderLineItemForm.getStatus();
        if(status == null) { 
            if(orderLineItem != null) {
                status = orderLineItem.getStatus();
            }
        }
        
        if(status == null) {
            statusCodes = pendingStatusCodes;
            orderLineItemForm.setStatusCode(Status.PENDING_INCUMBRANCE);
        }
        else if(status.getStatusCode().equals(Status.PENDING_INCUMBRANCE)) {
            statusCodes = pendingStatusCodes;
        }
        else if(status.getStatusCode().equals(Status.CANCELED)) {
            statusCodes = pendingStatusCodes;
        }
        else if(status.getStatusCode().equals(Status.ORDERED) || orderLineItem.getStatus().getStatusCode().equals(Status.ORDERED_ON_BACK_ORDER) || status.getStatusCode().equals(Status.MNIT_ENCUMBERED) ) {
            statusCodes = orderedStatusCodes;
        }
        else {//received
            statusCodes = receivedStatusCodes;
        }
        Collection statuses = daoFactory.getStatusDAO().findAllByStatusTypeAndStatusCodes(StatusType.MATERIALS_ORDER,
                                                                                          statusCodes);
        orderLineItemForm.setStatuses(statuses);
    }

    public OrderLineItem getOrderLineItem() {
        return orderLineItem;
    }
    
    /* stakeholders decided to move the notes to the Order level
    public void addNewOrderLineItemNoteForm(String text, User author) throws InfrastructureException {
        OrderLineItemNoteForm noteForm = new OrderLineItemNoteForm();
        noteForm.setNote(text);
        noteForm.setAuthorName(author.getFirstAndLastName());
        noteForm.setInsertionDate(new Date());
        orderLineItemForm.getOrderLineItemNoteForms().add(noteForm);
    }
    
    public void buildOrderLineItemNoteForms() throws InfrastructureException {  
        if(orderLineItem != null){
            Collection oliNotes = orderLineItem.getOrderLineItemNotes();
            Iterator iter = oliNotes.iterator();
            Collection oliNoteForms = new ArrayList();
            while(iter.hasNext()){
                OrderLineItemNote oliNote = (OrderLineItemNote)iter.next();
                OrderLineItemNoteForm oliNoteForm = new OrderLineItemNoteForm();
                OrderLineItemNoteFormBuilder oliNoteFormBuilder = 
                            new OrderLineItemNoteFormBuilder(oliNoteForm, oliNote, daoFactory);
                oliNoteFormBuilder.buildSimpleProperties();
                oliNoteForms.add(oliNoteForm);
            }
            orderLineItemForm.setOrderLineItemNoteForms(oliNoteForms);
        }
    }
    */

    public void buildAssetTypes(){
        orderLineItemForm.setAssetTypes(AssetType.getAssetTypes());
    }
}
