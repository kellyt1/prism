package us.mn.state.health.builder.materialsrequest;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import us.mn.state.health.builder.LineItemBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.director.DeliveryDetailDirector;
import us.mn.state.health.matmgmt.director.RequestLineItemDirector;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.materialsrequest.DeliveryDetail;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.util.HibernateModelDetacher;
import us.mn.state.health.model.util.ModelDetacher;
import us.mn.state.health.view.materialsrequest.RequestForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.materialsrequest.ShoppingListCatLineItemForm;
import us.mn.state.health.view.materialsrequest.ShoppingListForm;
import us.mn.state.health.view.materialsrequest.ShoppingListNonCatLineItemForm;

public class RequestBuilder extends LineItemBuilder {
    private Request request;
    private RequestForm requestForm;
    private ShoppingListForm shoppingListForm;
    private DAOFactory daoFactory;
    private User user;

    public RequestBuilder(Request request, RequestForm requestForm, DAOFactory daoFactory) {
        this.request = request;
        this.requestForm = requestForm;
        this.daoFactory = daoFactory;
    }

    public RequestBuilder(Request request, RequestForm requestForm, DAOFactory daoFactory, User user) {
        this(request, requestForm, daoFactory);
        this.user = user;
    }

    public RequestBuilder(Request request, ShoppingListForm shoppingListForm, DAOFactory daoFactory, User user) {
        this.request = request;
        this.shoppingListForm = shoppingListForm;
        this.daoFactory = daoFactory;
        this.user = user;
    }

    public void buildRequestLineItemsFromForm() throws InfrastructureException {
        Iterator iter = requestForm.getRequestLineItemForms().iterator();
        while (iter.hasNext()) {
            RequestLineItemForm rliForm = (RequestLineItemForm) iter.next();
            RequestLineItem rli = new RequestLineItem();
            RequestLineItemBuilder rliBuilder = new RequestLineItemBuilder(rli, rliForm, daoFactory, user);
            RequestLineItemDirector rliDirector = new RequestLineItemDirector(rliBuilder);
            rliDirector.createNewLineItem("");
            request.addRequestLineItem(rli);
        }
    }

    public void buildRequestLineItemsFromShoppingList() throws InfrastructureException {
        if (shoppingListForm != null) {
            buildRequestLineItemsFromShoppingListForm();
        }
    }

    public void buildRequestLineItemsForPurchasing() throws InfrastructureException {
        Collection reqLnItmForms = requestForm.getRequestLineItemForms();
        for (Iterator i = reqLnItmForms.iterator(); i.hasNext();) {
            RequestLineItemForm rliForm = (RequestLineItemForm) i.next();
            RequestLineItem reqLnItm = null;

            if (!StringUtils.nullOrBlank(rliForm.getRequestLineItemId())) {
                //Existing Request Line Item. Load it.
                Long reqLnItmId = new Long(rliForm.getRequestLineItemId());
                reqLnItm = daoFactory.getRequestLineItemDAO().getRequestLineItemById(reqLnItmId, true);
                ModelDetacher detacher = new HibernateModelDetacher();
                detacher.detachRequestLineItem(reqLnItm);
            } else {//New Request Line Item
                reqLnItm = new RequestLineItem();
            }
            
            RequestLineItemBuilder builder = new RequestLineItemBuilder(reqLnItm, rliForm, daoFactory, user);
            builder.buildMetaProperties();
            //Build Non-catalog properties
            if (rliForm.getItem() == null) {
                builder.buildItemCategory();
                //builder.buildSuggestedVendor();
            } else {
                builder.buildItem();
            }
            builder.buildFundingSources();
            try {
                builder.buildNotes();
            }
            catch (Exception e) {
                throw new InfrastructureException(e);
            }
            builder.buildSimpleProperties();
            builder.buildStatusFromForm();
            builder.buildUnit();

            if (reqLnItm.getRequestLineItemId() == null) {
                request.addRequestLineItem(reqLnItm);
            }
        }
    }

    private void buildRequestLineItemsFromShoppingListForm() throws InfrastructureException {
        Collection reqLnItms = request.getRequestLineItems();
        RequestLineItemBuilder builder = null;

        //Build Catalog Line Items
        Collection shppngLstCatLnItmFrms =
                CollectionUtils.getMatchingItems(shoppingListForm.getShoppingListCatLineItemForms(),
                        Boolean.TRUE,
                        "selected");
        for (Iterator i = shppngLstCatLnItmFrms.iterator(); i.hasNext();) {
            ShoppingListCatLineItemForm shppngLstCatLnItmFrm = (ShoppingListCatLineItemForm) i.next();
            Item item = shppngLstCatLnItmFrm.getItem();
            //Don't add discontinued Stock Items to cart
            if (Item.STOCK_ITEM.equals(item.getItemType()) && ((StockItem) item).getDiscontinued().booleanValue()) {
                continue;
            }
            //See if Request Line Item already exists for this Item
            Long itemId = shppngLstCatLnItmFrm.getItem().getItemId();
            RequestLineItem reqLnItm = (RequestLineItem) CollectionUtils.getObjectFromCollectionById(reqLnItms, itemId, "item.itemId");
            if (reqLnItm == null) { //Doesn't exist, so build it and add it
                reqLnItm = new RequestLineItem();
                builder = new RequestLineItemBuilder(reqLnItm, shppngLstCatLnItmFrm, daoFactory, user);
                builder.buildSimpleProperties();
                builder.buildItem();
                reqLnItms.add(reqLnItm);
            }
            //else skip it
        }

        //Build Non Catalog Line Items
        Collection shppngLstNonCatLnItmFrms = CollectionUtils.getMatchingItems(shoppingListForm.getShoppingListNonCatLineItemForms(),
                Boolean.TRUE,
                "selected");
        for (Iterator i = shppngLstNonCatLnItmFrms.iterator(); i.hasNext();) {
            ShoppingListNonCatLineItemForm shppngLstNonCatLnItmFrm = (ShoppingListNonCatLineItemForm) i.next();
            RequestLineItem reqLnItm = new RequestLineItem();
            builder = new RequestLineItemBuilder(reqLnItm, shppngLstNonCatLnItmFrm, daoFactory, user);
            builder.buildSimpleProperties();
            //builder.buildSuggestedVendor();
            builder.buildItemCategory();
            reqLnItms.add(reqLnItm);
        }
    }

    /**
     * This method sets all the 'primitive' types of the stock item from the form...
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(request, requestForm);
            request.setDateRequested(new Date());
        }
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building Request: ", rpe);
        }
    }

    public void buildPriority() throws InfrastructureException {
        request.setPriority(requestForm.getPriority());
    }

    /**
     * This version, as opposed to buildDeliveryDetailFromForm(),  expects the RequestForm
     * to have an initialized DeliveryDetail object in it, so
     * all we have to do is take that and set it in the Request object.
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildDeliveryDetail() throws InfrastructureException {
        request.setDeliveryDetail(requestForm.getDeliveryDetail());
    }

    /**
     * This version, as opposed to buildDeliveryDetail(), expects the RequestForm
     * to have an initialized DeliveryDetailForm object in it, so we need to take that
     * object, create a 'real' DeliveryDetail object out of it, and then set it as the Request's
     * DeliveryDetail object.
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildDeliveryDetailFromForm() throws InfrastructureException {
        if (requestForm != null && requestForm.getDeliveryDetailForm() != null) {
            DeliveryDetail deliveryDetail = new DeliveryDetail();
            DeliveryDetailBuilder builder = new DeliveryDetailBuilder(requestForm.getDeliveryDetailForm(), deliveryDetail, daoFactory);
            DeliveryDetailDirector director = new DeliveryDetailDirector(builder);
            director.construct();
            request.setDeliveryDetail(deliveryDetail);
        }
    }

    public void buildSelectedRequestor() {
        request.setRequestor(requestForm.getSelectedRequestor());
    }

    public void buildRequestor() {
        if (requestForm != null) {
            request.setRequestor(requestForm.getRequestor());
        } else {
            request.setRequestor(user);
        }
    }


    public void buildTrackingNumber() throws InfrastructureException {
        String trackingNumber = daoFactory.getRequestDAO().findNextTrackingNumber();
        request.setTrackingNumber(trackingNumber);
    }
}