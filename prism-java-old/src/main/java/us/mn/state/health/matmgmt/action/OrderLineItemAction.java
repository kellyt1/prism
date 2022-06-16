package us.mn.state.health.matmgmt.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.builder.inventory.PurchaseItemBuilder;
import us.mn.state.health.builder.materialsrequest.RequestLineItemBuilder;
import us.mn.state.health.builder.purchasing.ItemVendorsBuilder;
import us.mn.state.health.builder.purchasing.ItemVendorsFormBuilder;
import us.mn.state.health.builder.purchasing.OrderLineItemBuilder;
import us.mn.state.health.builder.purchasing.OrderLineItemFormBuilder;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.matmgmt.director.OrderLineItemDirector;
import us.mn.state.health.matmgmt.director.OrderLineItemFormDirector;
import us.mn.state.health.matmgmt.director.PurchaseItemDirector;
import us.mn.state.health.matmgmt.util.Form;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.PurchaseItem;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.model.util.HibernateModelDetacher;
import us.mn.state.health.model.util.ModelDetacher;
import us.mn.state.health.security.ApplicationResources;
import us.mn.state.health.view.inventory.ItemForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.purchasing.ItemVendorsForm;
import us.mn.state.health.view.purchasing.OrderForm;
import us.mn.state.health.view.purchasing.OrderLineItemForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class OrderLineItemAction extends MappingDispatchAction {
    private static Log log = LogFactory.getLog(OrderLineItemAction.class);

    public ActionForward saveOrderLineItem(ActionMapping mapping,
                                           ActionForm actionForm,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        log.debug("in saveOrderLineItem()");
        if (isTokenValid(request)) {
            OrderLineItemForm oliForm = (OrderLineItemForm) actionForm;
            OrderForm orderForm = (OrderForm) request.getSession().getAttribute(Form.ORDER);
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
            Object existingOrderLnItmForm = CollectionUtils.getObjectFromCollectionById(orderForm.getOrderLineItemForms(),
                    oliForm.getKey(),
                    "key");
            OrderLineItem oli = null;
            if (existingOrderLnItmForm == null) { //This is a new Order Line item form
//                int oliNbr = orderForm.getOrderLineItemForms().size() + 1;
//                oliForm.setLineItemNumber(new Integer(oliNbr));
                //Order Line Items are no longer being renumbered sequentially starting with 1 so
                // the new order line item is assigned the next number greater than the current max used.
                oliForm.setLineItemNumber(new Integer(orderForm.maxOrderLineItemNumber()+1));                
                orderForm.getOrderLineItemForms().add(oliForm);
                oli = new OrderLineItem();
                oliForm.setOrderLineItem(oli);
            } else {
                oli = ((OrderLineItemForm) existingOrderLnItmForm).getOrderLineItem();
            }
            OrderLineItemBuilder builder = new OrderLineItemBuilder(oli, oliForm, orderForm, daoFactory);
            OrderLineItemDirector director = new OrderLineItemDirector(builder);
            director.construct();
        } else {
            log.debug("invalid token in saveOrderLineItem()");
            saveToken(request);
        }
        return mapping.findForward("success");
    }

    /**
     * Action that removes a Request Line Item from an Order Line Item
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward removeRequestLineItemFromOrderLineItem(ActionMapping mapping,
                                                                ActionForm actionForm,
                                                                HttpServletRequest request,
                                                                HttpServletResponse response) throws Exception {
        log.debug("in removeRequestLineItemFromOrderLineItem()");
        Long reqLnItemId = new Long(request.getParameter("requestLineItemId"));
        OrderLineItemForm orderLnItmForm = (OrderLineItemForm) actionForm;
        OrderForm orderForm = (OrderForm) request.getSession().getAttribute(Form.ORDER);
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        //Get Req Ln Item Form
        Collection reqLnItmForms = orderLnItmForm.getRequestLineItemForms();
        RequestLineItemForm reqLnItmForm = (RequestLineItemForm) CollectionUtils.getObjectFromCollectionById(reqLnItmForms, reqLnItemId, "requestLineItem.requestLineItemId");
        reqLnItmForm.setRemovedFromOrderLineItem(Boolean.TRUE);

        //Rebuild Order Line Item Quantity
        OrderLineItemFormBuilder builder = new OrderLineItemFormBuilder(orderLnItmForm, daoFactory);
        builder.buildQuantityFromReqLnItms();

        //Add the RLI back to Order view
        reqLnItmForm.setSelected(Boolean.FALSE);
        reqLnItmForm.setRemovedFromOrder(Boolean.FALSE);
        boolean inList = CollectionUtils.inList(orderForm.getRequestLineItemForms(),
                "requestLineItem.requestLineItemId",
                reqLnItemId);
        if (!inList) {
            orderForm.getRequestLineItemForms().add(reqLnItmForm);
        }

        return mapping.findForward("success");
    }

    /**
     * Action that prepares create view of a OrderLineItem
     */
    public ActionForward viewCreateOrderLineItem(ActionMapping mapping,
                                                 ActionForm actionForm,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception {
        log.debug("in viewCreateOrderLineItem()");
        OrderForm orderForm = (OrderForm) actionForm;
        OrderLineItemForm oliForm = new OrderLineItemForm();

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);

        //Get selected Req Ln Itm Forms
        Collection rliForms = orderForm.getRequestLineItemForms();
        Collection selectedRLIForms = CollectionUtils.getMatchingItems(rliForms, Boolean.TRUE, "selected");
        CollectionUtils.assignValueToAll(selectedRLIForms, Boolean.TRUE, "removedFromOrder");

        //Build Order Line Item Form
        OrderLineItemFormBuilder builder = new OrderLineItemFormBuilder(oliForm, selectedRLIForms, daoFactory);
        OrderLineItemFormDirector director = new OrderLineItemFormDirector(builder);
        director.constructNewPurchasing();

        //put form in session scope
        request.getSession().setAttribute(Form.ORDER_LINE_ITEM, oliForm);
        log.debug("saving token in viewCreateOrderLineItem()");
        saveToken(request);
        return mapping.findForward("success");
    }

    /**
     * Action that prepares add to view of a OrderLineItem
     */
    public ActionForward viewAddToOrderLineItem(ActionMapping mapping,
                                                ActionForm actionForm,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        log.debug("in viewAddToOrderLineItem()");
        OrderForm orderForm = (OrderForm) actionForm;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        //Get selected Req Ln Itm Forms
        Collection reqLnItmForms = orderForm.getRequestLineItemForms();
        Collection selectedReqLnItmForms = CollectionUtils.getMatchingItems(reqLnItmForms, Boolean.TRUE, "selected");
        CollectionUtils.assignValueToAll(selectedReqLnItmForms, Boolean.TRUE, "removedFromOrder");

        //Get selected Order Line Item form
        Collection orderLnItmForms = orderForm.getOrderLineItemForms();
        String orderLineItemKey = orderForm.getOrderLineItemKey();
        orderForm.setCmd(null);

        OrderLineItemForm orderLnItmForm = (OrderLineItemForm) CollectionUtils.getObjectFromCollectionById(orderLnItmForms,
                orderLineItemKey,
                "key");
        OrderLineItem orderLnItm = null;
        if (!StringUtils.nullOrBlank(orderLnItmForm.getOrderLineItemId())) {
            Long orderLnItmId = new Long(orderLnItmForm.getOrderLineItemId());
            orderLnItm = daoFactory.getPurchasingOrderLineItemDAO().getOrderLineItemById(orderLnItmId);
            getDetacher().detachOrderLineItem(orderLnItm);
        }

        //Build OLI form
        orderLnItmForm.setDirty(Boolean.TRUE);
        OrderLineItemFormBuilder builder = new OrderLineItemFormBuilder(orderLnItmForm, orderLnItm, selectedReqLnItmForms, daoFactory);
        OrderLineItemFormDirector director = new OrderLineItemFormDirector(builder);
        director.constructAddRequestLineItems();

        //put form in session scope
        request.getSession().setAttribute(Form.ORDER_LINE_ITEM, orderLnItmForm);
        log.debug("saving token in viewAddToOrderLineItem()");
        saveToken(request);
        return mapping.findForward("success");
    }

    /**
     * Action that prepares edit view of OrderLineItem
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewEditOrderLineItem(ActionMapping mapping,
                                               ActionForm actionForm,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        log.debug("in viewEditOrderLineItem()");
        OrderForm form = (OrderForm) actionForm;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        String key = request.getParameter("key");
        Collection orderLnItmForms = form.getOrderLineItemForms();
        OrderLineItemForm orderLnItmForm = (OrderLineItemForm) CollectionUtils.getObjectFromCollectionById(orderLnItmForms, key, "key");

        //If OLI is persistent, then load it.
//        if (!StringUtils.nullOrBlank(orderLnItmForm.getOrderLineItemId())) {
        if ((orderLnItmForm != null) && !(StringUtils.nullOrBlank(orderLnItmForm.getOrderLineItemId()))) {       
            orderLnItmForm.setDirty(Boolean.TRUE);
            Long orderLnItmId = new Long(orderLnItmForm.getOrderLineItemId());
            OrderLineItem oli = daoFactory.getPurchasingOrderLineItemDAO().getOrderLineItemById(orderLnItmId);
            getDetacher().detachOrderLineItem(oli);
            OrderLineItemFormBuilder builder = new OrderLineItemFormBuilder(orderLnItmForm, oli, daoFactory);
            OrderLineItemFormDirector director = new OrderLineItemFormDirector(builder);
            director.constructEditPurchasing();
        }

        request.getSession().setAttribute(Form.ORDER_LINE_ITEM, orderLnItmForm);
        log.debug("saving token in viewEditOrderLineItem()");
        saveToken(request);
        return mapping.findForward("success");
    }

    /**
     * Action that prepares edit view of OrderLineItem    ;  This is for Editor Role
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewEditOrderLineItem2(ActionMapping mapping,
                                               ActionForm actionForm,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        log.debug("in viewEditOrderLineItem2()");
        OrderForm form = (OrderForm) actionForm;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        String key = request.getParameter("key");
        Collection orderLnItmForms = form.getOrderLineItemForms();
        OrderLineItemForm orderLnItmForm = (OrderLineItemForm) CollectionUtils.getObjectFromCollectionById(orderLnItmForms, key, "key");

        //If OLI is persistent, then load it.
//        if (!StringUtils.nullOrBlank(orderLnItmForm.getOrderLineItemId())) {
        if ((orderLnItmForm != null) && !(StringUtils.nullOrBlank(orderLnItmForm.getOrderLineItemId()))) {
            orderLnItmForm.setDirty(Boolean.TRUE);
            Long orderLnItmId = new Long(orderLnItmForm.getOrderLineItemId());
            OrderLineItem oli = daoFactory.getPurchasingOrderLineItemDAO().getOrderLineItemById(orderLnItmId);
            getDetacher().detachOrderLineItem(oli);
            OrderLineItemFormBuilder builder = new OrderLineItemFormBuilder(orderLnItmForm, oli, daoFactory);
            OrderLineItemFormDirector director = new OrderLineItemFormDirector(builder);
            director.constructEditPurchasing();
        }

        request.getSession().setAttribute(Form.ORDER_LINE_ITEM, orderLnItmForm);
        log.debug("saving token in viewEditOrderLineItem()");
        saveToken(request);
        return mapping.findForward("success");
    }

    /**
     * Action that removes an OrderLineItem
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward removeOrderLineItem(ActionMapping mapping,
                                             ActionForm actionForm,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws Exception {
        log.debug("in removeOrderLineItem()");
        OrderForm orderForm = (OrderForm) actionForm;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        String key = request.getParameter("key");
        Collection oliForms = orderForm.getOrderLineItemForms();
        OrderLineItemForm oliForm = (OrderLineItemForm) CollectionUtils.getObjectFromCollectionById(oliForms, key, "key");

        //remove the oliForm from the orderForm's collection
        oliForms.remove(oliForm);

        //remove the 'real' oli from the model Order object, if present, but 
        //make sure to reset the status of all its RLI objects first 
        if (!StringUtils.nullOrBlank(oliForm.getOrderLineItemId())) {
            Long id = new Long(oliForm.getOrderLineItemId());
            OrderLineItem oli;
            oli = daoFactory.getPurchasingOrderLineItemDAO()
                    .getOrderLineItemById(id);
            getDetacher().detachOrderLineItem(oli);

            //set all the RLI's status's back to Waiting For Purchase
            Status wfp = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST,
                    Status.WAITING_FOR_PURCHASE);
            for (Iterator iter = oli.getRequestLineItems().iterator(); iter.hasNext();) {
                RequestLineItem rli = (RequestLineItem) iter.next();
                rli.setStatus(wfp);
                /**
                 * break the association with the orderLineItem
                 */
                rli.setOrderLineItem(null);
            }

            //now we have to prepare the forms again; ie, refresh the view
            oliForm.setRemoved(Boolean.TRUE);
            oliForm.setDirty(Boolean.TRUE);
            OrderLineItemFormBuilder builder = new OrderLineItemFormBuilder(oliForm, oli, daoFactory);
            builder.buildReqLineItemFormsFromOrderLineItemRequests();

            CollectionUtils.assignValueToAll(oliForm.getRequestLineItemForms(), Boolean.FALSE, "removedFromOrder");
            CollectionUtils.assignValueToAll(oliForm.getRequestLineItemForms(), Boolean.TRUE, "removedFromOrderLineItem");
            CollectionUtils.assignValueToAll(oliForm.getRequestLineItemForms(), Boolean.FALSE, "selected");
            CollectionUtils.addWhereItemNotPresent(oliForm.getRequestLineItemForms(), orderForm.getRequestLineItemForms(), "requestLineItem.requestLineItemId");

            //delete the OLI from the order.  this should cascade to the Order and RLI's
            /*
            VERY IMPORTANT - This is the only workaround that I could figure to make the deletion of an oli and
            the rerank of the remaining oli's to work. There might be a better solution. - Lucian Ochian
            */
            /*
            2008-07-18 Todd R.
               When an Order Line Item is removed, we do not want to renumber remaining Order Line Items because this
                causes confusion because the Line numbers are referenced in the Order's notes sometimes such as received
                Line 3.  Then if Line 1 or 2 are subsequently removed, Line 3 was shifting to Line 2 and the Note was
                incorrect.
            */
            Order order = oli.getOrder();
//            getDetacher().detachOrder(order);

            /*
            First we set the line item number for the deleted oli to a number that is different from the other
            lineItemNumbers for that order(we do that because for some reason Hibernate first updates the records
            and then makes the deletion and we have a constraint in the DB for the pair orderId and lineItemNumber
            to be unique)
            */
/*
            oli.setLineItemNumber(new Integer(order.getOrderLineItems().size() + 1));
            HibernateUtil.getSession().flush();
            Collection orderLineItems = order.getOrderLineItems();
            orderLineItems.remove(null);
            int i = 0;
*/
            /**
             * Here we reassign numbers for oli's
             * We skip the one that we want to delete
             */
/*
            for (Iterator iterator = orderLineItems.iterator(); iterator.hasNext();) {
                OrderLineItem orderLineItem = (OrderLineItem) iterator.next();
                if (orderLineItem != oli) {
                    orderLineItem.setLineItemNumber(new Integer(++i));
                }
            }
*/
            /*
            Here we delete the item
            */
            order.removeOrderLineItem(oli);
            daoFactory.getPurchasingOrderDAO().makePersistent(order);
        }

        return mapping.findForward("success");
    }

    public ActionForward associateItemWithOrderLineItem(ActionMapping mapping,
                                                        ActionForm actionForm,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) throws Exception {
        log.debug("in associateItemWithOrderLineItem()");
        String itemIdStr = request.getParameter("itemId");
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        Item item = null;
        if (itemIdStr != null) { //Load item
            Long itemId = new Long(itemIdStr);
            item = daoFactory.getItemDAO().getItemById(itemId);
            getDetacher().detachItem(item);
        } else { //Save new Item
            ItemForm itemForm = (ItemForm) actionForm;
            item = new PurchaseItem();
            User actor = (User) request.getSession().getAttribute(ApplicationResources.USER);
            PurchaseItemBuilder builder = new PurchaseItemBuilder(item, itemForm, actor, daoFactory);
            PurchaseItemDirector director = new PurchaseItemDirector(builder);
            director.construct();
        }

        //Set Item in OLI form
        OrderLineItemForm orderLnItmForm = (OrderLineItemForm) request.getSession().getAttribute(Form.ORDER_LINE_ITEM);
        orderLnItmForm.setItem(item);

        //Load RLIs and assign item to them
        Collection reqLnItmForms = orderLnItmForm.getRequestLineItemForms();
        RequestLineItemBuilder rliBuilder = null;
        for (Iterator i = reqLnItmForms.iterator(); i.hasNext();) {
            RequestLineItemForm rliForm = (RequestLineItemForm) i.next();
            orderLnItmForm.setItemDescription(item.getDescription());
            rliForm.setItem(item);
            RequestLineItem rli = daoFactory.getRequestLineItemDAO().getRequestLineItemById(rliForm.getRequestLineItem().getRequestLineItemId(), true);
            rliBuilder = new RequestLineItemBuilder(rli, rliForm, daoFactory,(User)request.getSession().getAttribute(ApplicationResources.USER));
            rliBuilder.buildItem();
            //Update view
            rliForm.setRequestLineItem(rli);
        }

        //Save OLI if persistent
        if (!StringUtils.nullOrBlank(orderLnItmForm.getOrderLineItemId())) {
            Long orderLnItmId = new Long(orderLnItmForm.getOrderLineItemId());
            orderLnItmForm.setDirty(Boolean.TRUE);
            OrderLineItem orderLnItm = daoFactory.getPurchasingOrderLineItemDAO().getOrderLineItemById(orderLnItmId, true);
            OrderLineItemBuilder oliBuilder = new OrderLineItemBuilder(orderLnItm, orderLnItmForm, null, daoFactory);
            OrderLineItemDirector director = new OrderLineItemDirector(oliBuilder);
            director.construct();
        }

        return mapping.findForward("success");
    }

    public ActionForward selectVendorPurchasingInfo(ActionMapping mapping,
                                                    ActionForm actionForm,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws Exception {
        OrderLineItemForm orderLineItemForm = (OrderLineItemForm) actionForm;
        Long itemId = orderLineItemForm.getItem().getItemId();
        DAOFactory daoFactory = new HibernateDAOFactory();
        Item item = daoFactory.getItemDAO().getItemById(itemId);
        getDetacher().detachItem(item);
        orderLineItemForm.setItem(item);
        return mapping.findForward("success");
    }

    public ActionForward viewEditItemVendorInfo(ActionMapping mapping,
                                                ActionForm actionForm,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        ItemVendorsForm itemVendorsForm = (ItemVendorsForm) actionForm;
        Long itemId = itemVendorsForm.getItemId();
        itemVendorsForm.setReset(true);
        itemVendorsForm.reset(mapping, request);
        DAOFactory daoFactory = new HibernateDAOFactory();
        Item item = daoFactory.getItemDAO().getItemById(itemId);
        getDetacher().detachItem(item);
        ItemVendorsFormBuilder builder = new ItemVendorsFormBuilder(itemVendorsForm, item, daoFactory);
        builder.buildItemVendorForms();
        builder.buildUnits();
        builder.buildItem();
        return mapping.findForward("success");
    }

    public ActionForward editItemVendorInfo(ActionMapping mapping,
                                            ActionForm actionForm,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {
        ItemVendorsForm itemVendorsForm = (ItemVendorsForm) actionForm;

        Long itemId = itemVendorsForm.getItem().getItemId();
        DAOFactory daoFactory = new HibernateDAOFactory();
        Item item = daoFactory.getItemDAO().getItemById(itemId, true);
        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);

        Collection itemVendorForms = itemVendorsForm.getItemVendorForms();
        ArrayList itemVendorFormsBkp = new ArrayList(itemVendorForms);
        ItemVendorsBuilder builder = new ItemVendorsBuilder(item, itemVendorForms, daoFactory, user, itemVendorsForm.getPrimaryKey());
        builder.buildItemVendors();
        daoFactory.getItemDAO().makePersistent(item);
        itemVendorsForm.setItemId(itemId);
        itemVendorsForm.setItemVendorForms(itemVendorFormsBkp);
        return mapping.findForward("success");
    }

    public ActionForward viewAddItemVendorInfo(ActionMapping mapping,
                                               ActionForm actionForm,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {

        return mapping.findForward("");
    }

    public ActionForward addItemVendorInfo(ActionMapping mapping,
                                           ActionForm actionForm,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {

        return mapping.findForward("");
    }

    private ModelDetacher getDetacher() {
        ModelDetacher detacher = new HibernateModelDetacher();
        return detacher;
    }
}