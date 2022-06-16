package us.mn.state.health.builder.purchasing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.*;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.model.purchasing.OrderNote;
import us.mn.state.health.model.util.HibernateModelDetacher;
import us.mn.state.health.model.util.ModelDetacher;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.purchasing.OrderForm;
import us.mn.state.health.view.purchasing.OrderLineItemForm;
import us.mn.state.health.view.purchasing.OrderNoteForm;

import java.util.*;

public class OrderFormBuilder {
    private OrderForm orderForm;
    private Order order;
    private Collection requestLineItemForms;
    private DAOFactory daoFactory;
    private Person actor;
    private static Log log = LogFactory.getLog(OrderFormBuilder.class);

    //full constructor
    public OrderFormBuilder(OrderForm orderForm,
                            Order order,
                            Collection requestLineItemForms,
                            DAOFactory daoFactory,
                            Person actor) {
        this.orderForm = orderForm;
        this.order = order;
        this.requestLineItemForms = requestLineItemForms;
        this.daoFactory = daoFactory;
        this.actor = actor;
    }


    public OrderFormBuilder(OrderForm orderForm, Person actor) {
        this.orderForm = orderForm;
        this.actor = actor;
    }

    public OrderFormBuilder(OrderForm orderForm, Order order, Person actor, DAOFactory daoFactory) {
        this(orderForm, order, daoFactory);
        this.actor = actor;
    }

    public OrderFormBuilder(OrderForm orderForm, DAOFactory daoFactory) {
        this.orderForm = orderForm;
        this.daoFactory = daoFactory;
    }

    public OrderFormBuilder(OrderForm orderForm, Order order, DAOFactory daoFactory) {
        this(orderForm, daoFactory);
        this.order = order;
    }

    public OrderFormBuilder(OrderForm orderForm,
                            Order order,
                            Collection requestLineItemForms,
                            DAOFactory daoFactory) {
        this(orderForm, requestLineItemForms, daoFactory);
        this.order = order;
    }

    public OrderFormBuilder(OrderForm orderForm, Collection requestLineItemForms, DAOFactory daoFactory) {
        this(orderForm, daoFactory);
        this.requestLineItemForms = requestLineItemForms;
    }

    /* 
     * Build default orderForm properties 
     */
    public void buildDefaultProperties() {
        Date suspenseDate = DateUtils.addDaysToDate(new Date(), 21);
        orderForm.setSuspenseDate(DateUtils.toString(suspenseDate));
        orderForm.setPurchaseOrderNumberType(Order.PO_NBR_TYPE_MAPS);
    }

    /**
     * This is the method that Jason wrote, which is currently being used in the Purchasing area.
     * There is a difference in approach between this and other method which accepts a String argument.
     * With this method's approach, the user would click an 'Add Note' button and then would be presented
     * with a blank text field to enter their note.  With the other method's approach, the user is already presented
     * with a blank text field to enter their note.  They click 'Add Note' to effective save the note.
     * TODO:  we should discuss these approaches and standardize one.  The application must be consistent throughout.
     */
    public void buildNewNoteForm() {
        OrderNoteForm noteForm = new OrderNoteForm();
        OrderNoteFormBuilder noteFormBuilder = new OrderNoteFormBuilder(noteForm, actor.getFirstAndLastName());
        noteFormBuilder.buildDefaultProperties();
        List orderNoteForms = (List) orderForm.getOrderNoteForms();
        orderNoteForms.add(0, noteForm);
    }

    /**
     * This method accepts a string argument, duh.  Use it when you expect the user has already entered their note and they
     * want to effectively submit it. This is being used in the Receiving area.
     *
     * @param text
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildNewOrderNoteForm(String text) throws InfrastructureException {
        OrderNoteForm noteForm = new OrderNoteForm();
        OrderNoteFormBuilder noteFormBuilder = new OrderNoteFormBuilder(noteForm, actor.getFirstAndLastName());
        noteFormBuilder.buildDefaultProperties();
        noteForm.setNote(text);
        List orderNoteForms = (List) orderForm.getOrderNoteForms();
        orderNoteForms.add(0, noteForm);
    }

    public void buildNoteForms() throws InfrastructureException {
        Collection orderNoteForms = new ArrayList();
        for (Iterator i = order.getOrderNotes().iterator(); i.hasNext();) {
            OrderNoteForm noteForm = new OrderNoteForm();
            OrderNote note = (OrderNote) i.next();
            OrderNoteFormBuilder builder = new OrderNoteFormBuilder(noteForm, note);
            builder.buildSimpleProperties();
            orderNoteForms.add(noteForm);
        }
        orderForm.setOrderNoteForms(orderNoteForms);
    }

    public void buildOrderLineItemForms() throws InfrastructureException {
        Collection oliForms = new ArrayList();
        Collection orderLnItms = order.getOrderLineItems();
        OrderLineItemFormBuilder builder = null;
        OrderLineItemForm orderLnItmForm = null;
        OrderLineItem orderLnItm = null;

        for (Iterator i = orderLnItms.iterator(); i.hasNext();) {
            orderLnItm = (OrderLineItem) i.next();
            //Order Line Items are mapped using a Hibernate List so if Line Item Numbers are
            // not sequential, then Hibernate inserts null objects into the collection for the
            // missing Line Items
            if (orderLnItm != null) {
                orderLnItmForm = new OrderLineItemForm();
                builder = new OrderLineItemFormBuilder(orderLnItmForm, orderLnItm, daoFactory);
                builder.buildOrderLineItem();
                builder.buildItem();    // 3/2/2006 - SF: added this to try and get ICNBR's on receiving pages
                builder.buildSimpleProperties();
                builder.buildReqLineItemFormsFromOrderLineItemRequests();
                //builder.buildOrderLineItemNoteForms();
                oliForms.add(orderLnItmForm);
            }
        }

        try {
            CollectionUtils.sort(oliForms, "lineItemNumber", true, "INTEGER");
        }
        catch (ReflectivePropertyException e) {
            log.error("ReflectivePropertyException in buildOrderLineItemForms(): ", e);
            throw new InfrastructureException(e);
        }
        orderForm.setOrderLineItemForms(oliForms);
    }

    public void buildReqLineItemForms() throws InfrastructureException {
        if (requestLineItemForms != null) {
            CollectionUtils.assignValueToAll(requestLineItemForms, Boolean.FALSE, "selected");
            for (Iterator i = requestLineItemForms.iterator(); i.hasNext();) {
                RequestLineItemForm rliForm = (RequestLineItemForm) i.next();
                RequestLineItem rli = daoFactory.getRequestLineItemDAO()
                        .getRequestLineItemById(rliForm.getRequestLineItem().getRequestLineItemId());
//                long id = rli.getItem().getItemId();

//                Item item = rli.getItem();

                //todo - Todd R. Verify that commenting the next line does not bread anything 
//                Hibernate.initialize(rli.getItem());

//                String mdl = rli.getItem().getModel();
                ModelDetacher modelDetacher = new HibernateModelDetacher();
                modelDetacher.detachRequestLineItem(rli);
                rliForm.setRequestLineItem(rli);
            }
        } else {
            requestLineItemForms = new ArrayList();
        }
        if (order != null) { //Build RLIForms for RLI's already assigned to this order
            for (Iterator i = order.getRequestLineItems().iterator(); i.hasNext();) {
                RequestLineItemForm rliForm = new RequestLineItemForm();
                RequestLineItem rli = (RequestLineItem) i.next();
                rliForm.setRequestLineItem(rli);
                requestLineItemForms.add(rliForm);
            }
        }
        orderForm.setRequestLineItemForms(requestLineItemForms);
    }

    /* 
     * Build simple orderForm properties from the order 
     */
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(orderForm, order);
            if (order.getBillToAddress() != null) {
                orderForm.setBillToAddressId(order.getBillToAddress().getMailingAddressId().toString());
            }
            if (order.getVendor() != null) {
                orderForm.setVendorId(order.getVendor().getVendorId().toString());
            }
            if (order.getVendorContract() != null) {
                orderForm.setVendorContractId(order.getVendorContract().getVendorContractId().toString());
            }
            if (order.getVendorAddress() != null) {
                orderForm.setVendorAddressId(order.getVendorAddress().getMailingAddressId().toString());
            }
        }
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }

    public void buildBillToAddresses() throws InfrastructureException {
        Collection billToAddresses = daoFactory.getMailingAddressDAO().findBillingAddresses();
        orderForm.setBillToAddresses(billToAddresses);
        if (order != null && order.getBillToAddress() != null) {
            orderForm.setBillToAddressId(order.getBillToAddress().getMailingAddressId().toString());
        } else orderForm.setBillToAddressId(""); 
    }

    public void buildShipToAddresses() throws InfrastructureException {
        Collection shipToAddresses = daoFactory.getMailingAddressDAO().findShippingAddresses();
        orderForm.setShipToAddresses(shipToAddresses);
        if (order != null && order.getShipToAddress() != null) {
            orderForm.setShipToAddressId(order.getShipToAddress().getMailingAddressId().toString());
        } else orderForm.setShipToAddressId("");
    }

    /**
     * builds the vendor list.  It will try to limit the size of the list so that it
     * only contains the vendors whose name starts with a character in a given range.  The
     * range must be set in the form, otherwise all gazillion vendors will be loaded.
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildVendors() throws InfrastructureException {
        Collection vendors;
        if (!StringUtils.nullOrBlank(orderForm.getVendorNameFirstCharStart()) &&
                !StringUtils.nullOrBlank(orderForm.getVendorNameFirstCharEnd())) {
            char start = orderForm.getVendorNameFirstCharStart().toCharArray()[0];
            char end = orderForm.getVendorNameFirstCharEnd().toCharArray()[0];
            vendors = daoFactory.getVendorDAO().findByNameFirstCharRange(start, end);
        } else {
//            vendors = daoFactory.getVendorDAO().findAll(false);
            vendors = daoFactory.getVendorDAO().findAll();
        }

        if (order != null) {
            if (order.getVendor() != null) {
                Vendor vendor = daoFactory.getVendorDAO().getVendorById(order.getVendor().getVendorId(), false);
                if (!vendors.contains(vendor)) {
                    vendors.add(order.getVendor());
                }
                orderForm.setVendorId(order.getVendor().getVendorId().toString());
            }
        }

        orderForm.setVendors(vendors);
    }

    public void buildVendorPhones() {
        if (order != null && order.getVendor() != null) {
            ExternalOrgDetail extOrgDetail = order.getVendor().getExternalOrgDetail();
            orderForm.setVendorPhones(extOrgDetail.getPhones());
        }
    }

    public void buildVendorFaxes() {
        if (order != null && order.getVendor() != null) {
            ExternalOrgDetail extOrgDetail = order.getVendor().getExternalOrgDetail();
            orderForm.setVendorFaxes(extOrgDetail.getFaxes());
        }
    }

    public void buildVendorEmails() {
        if (order != null && order.getVendor() != null) {
            ExternalOrgDetail extOrgDetail = order.getVendor().getExternalOrgDetail();
            orderForm.setVendorEmails(extOrgDetail.getEmailAddresses());
        }
    }

    public void buildVendorReps() {
        if (order != null && order.getVendor() != null) {
            ExternalOrgDetail extOrgDetail = order.getVendor().getExternalOrgDetail();
            orderForm.setVendorReps(extOrgDetail.getReps());
        }
    }

    public void buildVendorAccounts() {
        if (order != null && order.getVendor() != null) {
            orderForm.setVendorAccounts(order.getVendor().getVendorAccounts());
            if (order.getVendorAccount() != null) {
                orderForm.setVendorAccountId(order.getVendorAccount().getVendorAccountId().toString());
            }
        }
    }

    public void buildVendorContracts() throws InfrastructureException {
        if (order != null && order.getVendor() != null) {
            Collection vendorContracts = order.getVendor().getVendorContracts();
            HashSet vcs = new HashSet ();

            for (Iterator iterator = vendorContracts.iterator(); iterator.hasNext();) {
                VendorContract vendorContract = (VendorContract) iterator.next();
                if (vendorContract.getEndDate() != null)   {
                    Date contractEndDate = vendorContract.getEndDate();
                    long endDate = contractEndDate.getTime();
                    Calendar c = Calendar.getInstance();
                    Date today = c.getTime();
                    long presentDay = today.getTime();
                    if (endDate < presentDay) {
                        // endDate expired
                    }
                    else
                        vcs.add(vendorContract);
                }
                else  // null endDate is good & active contract.
                    vcs.add(vendorContract);
            }

            //orderForm.setVendorContracts(order.getVendor().getVendorContracts());  // original code display all endDates
            orderForm.setVendorContracts(vcs); // just need to display contracts with null endDate or future enddate.

            if (order.getVendorContract() != null) {
                orderForm.setVendorContractId(order.getVendorContract().getVendorContractId().toString());
            }
        }
    }

    public void buildVendorAddresses() throws InfrastructureException {
        if (order != null && order.getVendor() != null) {
            Long vendorId = order.getVendor().getVendorId();
            Vendor vendor = daoFactory.getVendorDAO().getVendorById(vendorId, false);
            Collection vendorAddresses = vendor.getExternalOrgDetail().getMailingAddresses();
            orderForm.setVendorAddresses(vendorAddresses);
            if (order.getVendorAddress() != null) {
                orderForm.setVendorAddressId(order.getVendorAddress().getMailingAddressId().toString());
            }
        }
    }

    public void buildFacilities() throws InfrastructureException {
        Collection facilities = daoFactory.getFacilityDAO()
                .findFacilitiesByType(Facility.TYPE_BUILDING);
        orderForm.setFacilities(facilities);
    }

    public void buildOrder() throws InfrastructureException {
        orderForm.setOrder(order);

    }

    public Order getOrder() {
        return order;
    }

    public Collection getRequestLineItemForms() {
        return requestLineItemForms;
    }
}
