package us.mn.state.health.builder.purchasing;

import us.mn.state.health.builder.LineItemBuilder;
import us.mn.state.health.builder.email.BudgetManagerEmailBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.lang.WrapperUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.director.email.EmailDirector;
import us.mn.state.health.model.common.Facility;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.model.receiving.ReceivingDetail;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.EmailBusinessDelegate;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.purchasing.OrderForm;
import us.mn.state.health.view.purchasing.OrderLineItemForm;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class OrderLineItemBuilder extends LineItemBuilder {
    private Order order;
    private OrderLineItem orderLineItem;
    private OrderLineItemForm orderLineItemForm;
    private OrderForm orderForm;
    private DAOFactory daoFactory;
    private Person user;            //a person taking action on the OLI

    /**
     * Parameterized Constructor
     *
     * @param daoFactory    Data access mechanism
     * @param orderLineItem OrderLineItem to be built
     */
    public OrderLineItemBuilder(OrderLineItem orderLineItem,
                                DAOFactory daoFactory) {
        this.orderLineItem = orderLineItem;
        this.daoFactory = daoFactory;
    }

    /**
     * Parameterized Constructor
     *
     * @param daoFactory        data access mechanism
     * @param orderLineItemForm used to build OrderLineItem
     * @param orderLineItem     OrderLineItem to be built
     */
    public OrderLineItemBuilder(OrderLineItem orderLineItem,
                                OrderLineItemForm orderLineItemForm,
                                OrderForm orderForm,
                                DAOFactory daoFactory) {
        this(orderLineItem, daoFactory);
        this.orderLineItemForm = orderLineItemForm;
        this.orderForm = orderForm;
    }

    /**
     * @param orderLineItem
     * @param orderLineItemForm
     * @param orderForm
     * @param user
     * @param daoFactory
     */
    public OrderLineItemBuilder(OrderLineItem orderLineItem,
                                OrderLineItemForm orderLineItemForm,
                                OrderForm orderForm,
                                Person user,
                                DAOFactory daoFactory) {
        this(orderLineItem, daoFactory);
        this.orderLineItemForm = orderLineItemForm;
        this.orderForm = orderForm;
        this.user = user;
    }

    /**
     * Parameterized Constructor
     *
     * @param daoFactory        data access mechanism
     * @param order             Order reference to be assigned to OrderLineItem
     * @param orderLineItemForm used to build OrderLineItem
     * @param orderLineItem     OrderLineItem to be built
     */
    public OrderLineItemBuilder(OrderLineItem orderLineItem,
                                Order order,
                                OrderLineItemForm orderLineItemForm,
                                OrderForm orderForm,
                                DAOFactory daoFactory) {
        this(orderLineItem, orderLineItemForm, orderForm, daoFactory);
        this.order = order;
    }

    /**
     * Assigns buy unit to OrderLineItem
     */
    public void buildBuyUnit() throws InfrastructureException {
        Long buyUnitId = new Long(orderLineItemForm.getBuyUnitId());
        Unit buyUnit = daoFactory.getUnitDAO().getUnitById(buyUnitId, false);
        orderLineItem.setBuyUnit(buyUnit);
    }

    /**
     * Assigns Item to OrderLineItem
     */
    public void buildItem() {
        orderLineItem.setItem(orderLineItemForm.getItem());
        if (orderLineItemForm.getItem() != null) { //Set dispense unit cost
            if (!StringUtils.nullOrBlank(orderLineItemForm.getDispenseUnitsPerBuyUnit())) {
                Item item = orderLineItemForm.getItem();
                double dispenseUnitsPerBuyUnit = Double.parseDouble(orderLineItemForm.getDispenseUnitsPerBuyUnit());
                double buyUnitCost = Double.parseDouble(orderLineItemForm.getBuyUnitCost().replaceAll(",", ""));
                double dispenseUnitCost = buyUnitCost / dispenseUnitsPerBuyUnit;
                dispenseUnitCost = Double.parseDouble(WrapperUtils.round(Double.toString(dispenseUnitCost), 2));
                item.setDispenseUnitCost(new Double(dispenseUnitCost));
            }
        }
    }

    /**
     * Builds meta data for OrderLineItem instance
     */
    public void buildMetaProperties() {
        orderLineItem.buildInsertMetaProperties("");
    }

    /**
     * Builds RequestLineItems assigned to OrderLineItem.
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildRequestLineItems(Boolean bComputerRequest) throws InfrastructureException {
        Collection reqLnItmForms = orderLineItemForm.getRequestLineItemForms();
        Collection oliReqLnItms = orderLineItem.getRequestLineItems();

        for (Iterator i = reqLnItmForms.iterator(); i.hasNext();) {
            RequestLineItemForm rliForm = (RequestLineItemForm) i.next();
            Long reqLnItmId = rliForm.getRequestLineItem().getRequestLineItemId();
            RequestLineItem rli = null;
            if (rliForm.getRemovedFromOrderLineItem().booleanValue()) { //check if marked for removal
                i.remove(); //remove form
                rli = daoFactory.getRequestLineItemDAO().getRequestLineItemById(reqLnItmId, false);
                if (!rliForm.getRemovedFromOrder().booleanValue()) {
                    //reset status of rli to pending order
                    Status pbo = daoFactory.getStatusDAO()
                            .findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.PENDING_BUILDING_ORDER);
                    rli.setStatus(pbo);
                }
                rli.setOrderLineItem(null); //Remove one-to-many association with OrderLineItem
                //Remove from OrderLineItem
                CollectionUtils.removeMatchingItem(orderLineItem.getRequestLineItems(), reqLnItmId, "requestLineItemId");
            } else { //not marked for removal
                //check if already assigned to OLI
                boolean alreadyInOrderLnItm = CollectionUtils.inList(oliReqLnItms, "requestLineItemId", reqLnItmId);
                rli = daoFactory.getRequestLineItemDAO().getRequestLineItemById(reqLnItmId, true);

                // If RLI status is not canceled,received, received partial, then set it to ordered. We want to leave canceled RLI's alone.
                String existingStatusCode = rli.getStatus().getStatusCode();
                if (!existingStatusCode.equals(Status.CANCELLED_ITEM_DISCONTINUED)
                        && !existingStatusCode.equals(Status.RECEIVED)
                        && !existingStatusCode.equals(Status.RECEIVED_PARTIAL) ) {
                    String statusCode = Status.ORDERED;
                    if (existingStatusCode.equals(Status.COMPUTER_PURCHASE_ORDER) || existingStatusCode.equals(Status.MNIT_ORDERED) || bComputerRequest) {
                        statusCode = Status.MNIT_ORDERED;
                        if ((rli.getSuggestedVendorCatalogNumber() == null) || (!rli.getSuggestedVendorCatalogNumber().equalsIgnoreCase("STAFFAUG") && !rli.getSuggestedVendorCatalogNumber().equalsIgnoreCase("MNITCONTRACT") && !rli.getSuggestedVendorCatalogNumber().equalsIgnoreCase("WAN/Computing Services"))) {
                            if (!StringUtils.nullOrBlank(orderForm.getPurchaseOrderNumber())) {
                                rli.getRequest().setHelpDeskticketId(rli.createHelpDeskTicket(rli.getRequest(), orderLineItem.getOrder().getPurchaseOrderNumber(), false));
                           }
                        }
                        else if ((rli.getSuggestedVendorCatalogNumber() != null) && (rli.getSuggestedVendorCatalogNumber().equalsIgnoreCase("STAFFAUG") || rli.getSuggestedVendorCatalogNumber().equalsIgnoreCase("MNITCONTRACT") || rli.getSuggestedVendorCatalogNumber().equalsIgnoreCase("WAN/Computing Services"))) {
                            // Send Email To Budget Manager indicating there is a new approved Contract or staffing change.  Include AC2 Code and PO and MRQ.
                                EmailBean emailBean = new EmailBean();
                                BudgetManagerEmailBuilder builder = new BudgetManagerEmailBuilder(rli, emailBean, daoFactory);
                                builder.setExtraMessage("");
                                EmailDirector director = new EmailDirector(builder);
                                director.construct();

                                EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
                                emailBusinessDelegate.sendEmail(emailBean);

                        }
                    }
                    if (StringUtils.nullOrBlank(orderForm.getPurchaseOrderNumber())) {
                        if (!statusCode.equals(Status.COMPUTER_PURCHASE_ORDER) && !statusCode.equals(Status.MNIT_ORDERED)) {
                            statusCode = Status.PENDING_BUILDING_ORDER;
                        }
                    }
                    Status rliStatus = daoFactory.getStatusDAO()
                            .findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, statusCode);
                    rli.setStatus(rliStatus);
                }
                if (!alreadyInOrderLnItm) { //not already assigned to OLI, so add it
                    rli.setOrderLineItem(orderLineItem);
                    oliReqLnItms.add(rli); //redo this - add a method to OrderLineItem to replace both these calls
                }
            }
        }
    }

    /* moved addresses to Order
     * Assigns ship-to address to OrderLineItem
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     
    public void buildShipToAddress() throws InfrastructureException {
        AddressBookExternalSource addBkExtSrc = orderLineItemForm.getShipToAddress();
        orderLineItem.setShipToAddress(addBkExtSrc);
    }
    */
    public void buildStatusForPurchasing() throws InfrastructureException {
        buildStatusForPurchasing(false);
    }

    public void buildStatusForPurchasing(Boolean computerPurchase) throws InfrastructureException {
        Status status = orderLineItemForm.getStatus();
        if (status == null) {
            status = orderLineItem.getStatus();
        }
        status = daoFactory.getStatusDAO().getStatusById(status.getStatusId(), false);  //why are we calling this?  Lazy-loading problem?

        /*if OrderLineItem status is pending incumbrence and purchaseOrderNunber has been assigned, then
    * set status to ordered*/
        if (status.getStatusCode().equals(Status.PENDING_INCUMBRANCE) && !StringUtils.nullOrBlank(orderForm.getPurchaseOrderNumber()))
        {
//            if (orderLineItem.getVendorCatalogNbr() != null && (orderLineItem.getVendorCatalogNbr().equalsIgnoreCase("STAFFAUG") || orderLineItem.getVendorCatalogNbr().equalsIgnoreCase("MNITCONTRACT"))) {
            if (computerPurchase) {
                status = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_ORDER, Status.MNIT_ENCUMBERED);
            }   else
                status = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_ORDER, Status.ORDERED);
        }
        orderLineItem.setStatus(status);
    }

    /**
     * Builds status of OrderLineItem and each of its RequestLineItems.
     * NOTE = IT IS IMPORTANT THAT buildReceivingDetails() IS CALLED FIRST,
     * AS THIS METHOD RELIES ON THE ORDER LINE ITEM'S RECEIVING DETAILS BEING
     * UP-TO-DATE IN ORDER TO SET THE STATUS.
     * <p/>
     * 03/07/2006: intialized otalQtyReceived to 0 instead of the user-provided
     * value.  See bug #: 1229 for more info - SF
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildStatusForReceiving() throws InfrastructureException {
        if (!StringUtils.nullOrBlank(orderLineItemForm.getQuantityReceived()) &&
                !(orderLineItemForm.getQuantityReceived().equals("0"))) {
            int totalQtyReceived = 0;
            Iterator detailsIter = orderLineItem.getReceivingDetails().iterator();
            while (detailsIter.hasNext()) {
                ReceivingDetail currentDetail = (ReceivingDetail) detailsIter.next();
                totalQtyReceived += currentDetail.getQuantityReceived().intValue();
            }

            //by default, status is Received_Partial
            Status oliStatus;
            Status rliStatus;

            //if its been completely received, status is RECEIVED
            if (totalQtyReceived >= orderLineItem.getQuantity().intValue()) {
                oliStatus = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_ORDER,
                        Status.RECEIVED);
                rliStatus = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST,
                        Status.RECEIVED);
            } else {
                oliStatus = daoFactory.getStatusDAO()
                        .findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_ORDER, Status.RECEIVED_PARTIAL);

                rliStatus = daoFactory.getStatusDAO()
                        .findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.RECEIVED_PARTIAL);

            }

            //now set the status on the OLI, and each of its RLI's.  
            orderLineItem.setStatus(oliStatus);
            for (Iterator iter = orderLineItem.getRequestLineItems().iterator(); iter.hasNext();) {
                RequestLineItem rli = (RequestLineItem) iter.next();
                rli.setStatus(rliStatus);
            }
        }
    }

    /**
     * Assigns Order to OrderLineItem
     */
    public void buildOrder() {
        orderLineItem.setOrder(order);
    }

    /* junk
    public void buildSensitiveAssets() throws InfrastructureException {
        if(orderLineItemForm.getAssetsType().equals(OrderLineItemForm.SENSITIVE_ASSETS)) {
            if(!StringUtils.nullOrBlank(orderLineItemForm.getOrderLineItemId())) {
                Long orderLnItmId = new Long(orderLineItemForm.getOrderLineItemId());
                Long buyQty = new Long(orderLineItemForm.getQuantity());
                Long existingAssetCount = daoFactory.getSensitiveAssetDAO().findSensitiveItemCountByOrderLineItemId(orderLnItmId);
                long sensitiveAssetsToAdd = buyQty.longValue() - existingAssetCount.longValue();
                for(int i=0; i <= sensitiveAssetsToAdd; i++) {
                    SensitiveAsset sa = new SensitiveAsset();
                    sa.setOrderLineItemId(orderLnItmId);
                    daoFactory.getSensitiveAssetDAO().makePersistent(sa);
                }
            }    
        }
    }
    */

    /**
     * Builds OrderLineItem simple properties from OrderLineItemForm
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(orderLineItem, orderLineItemForm);
        }
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }

    public void buildReceivingDetails() throws InfrastructureException {
        String qtyReceived = orderLineItemForm.getQuantityReceived();
        if (qtyReceived != null && !(qtyReceived.equals("") || qtyReceived.equals("0"))) {
            Integer quantity = new Integer(qtyReceived);
            String facilityId = orderForm.getReceivingFacilityId();
            Facility facility = daoFactory.getFacilityDAO().getFacilityById(new Long(facilityId), false);
            String assetNumber = orderLineItemForm.getAssetNumber();
            ReceivingDetail detail = new ReceivingDetail(orderLineItem, user, new Date(), quantity, facility, assetNumber);
            orderLineItem.getReceivingDetails().add(detail);
        }
    }
}