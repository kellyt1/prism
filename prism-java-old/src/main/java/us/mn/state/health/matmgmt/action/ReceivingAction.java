package us.mn.state.health.matmgmt.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import org.hibernate.Hibernate;
import us.mn.state.health.builder.email.AssetNumberNewEmailBuilder;
import us.mn.state.health.builder.email.ValidateHelpDeskTicketAddedEmailBuilder;
import us.mn.state.health.builder.inventory.FixedAssetBuilder;
import us.mn.state.health.builder.inventory.FixedAssetFormBuilder;
import us.mn.state.health.builder.inventory.SensitiveAssetBuilder;
import us.mn.state.health.builder.inventory.SensitiveAssetFormBuilder;
import us.mn.state.health.builder.purchasing.OrderBuilder;
import us.mn.state.health.builder.purchasing.OrderFormBuilder;
import us.mn.state.health.builder.purchasing.OrderLineItemBuilder;
import us.mn.state.health.builder.receiving.DeliveryTicketBuilder;
import us.mn.state.health.builder.receiving.DeliveryTicketFormBuilder;
import us.mn.state.health.common.report.JasperReportWriter;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.director.FixedAssetDirector;
import us.mn.state.health.matmgmt.director.OrderFormDirector;
import us.mn.state.health.matmgmt.director.OrderLineItemDirector;
import us.mn.state.health.matmgmt.director.SensitiveAssetDirector;
import us.mn.state.health.matmgmt.director.email.EmailDirector;
import us.mn.state.health.matmgmt.util.Report;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.FixedAsset;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.SensitiveAsset;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.purchasing.HelpDeskTicketTracking;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.model.receiving.DeliveryTicket;
import us.mn.state.health.model.receiving.ReceivingDetail;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.EmailBusinessDelegate;
import us.mn.state.health.security.ApplicationResources;
import us.mn.state.health.view.inventory.AssetFormCollection;
import us.mn.state.health.view.inventory.Command;
import us.mn.state.health.view.inventory.FixedAssetForm;
import us.mn.state.health.view.inventory.SensitiveAssetForm;
import us.mn.state.health.view.purchasing.OrderForm;
import us.mn.state.health.view.purchasing.OrderLineItemForm;
import us.mn.state.health.view.receiving.DeliveryTicketForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
* Struts Controller that processes Receiving actions
* @author Shawn Flahave
*/
public class ReceivingAction extends MappingDispatchAction {
    private static Log log = LogFactory.getLog(ReceivingAction.class);
    
    /**
     * Action that prepares view of an Order for Receiving
     */
    public ActionForward viewReceiveOrder(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        log.debug("in viewReceiveOrder()");
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        String selectedOrderId = request.getParameter("selectedOrderId");
        OrderForm orderForm = (OrderForm)form;
        orderForm.setShowNotes(Boolean.TRUE);

        Order order = daoFactory.getPurchasingOrderDAO()
                                .getOrderById(new Long(selectedOrderId), false);
        OrderFormBuilder builder = new OrderFormBuilder(orderForm, order, daoFactory);
        OrderFormDirector director = new OrderFormDirector(builder);
        director.constructForReceiving();
        log.debug("in viewReceiveOrder()... saving new token");
        saveToken(request);
        return mapping.findForward("success");
    }

    private void sendEmail(OrderForm orderForm, OrderLineItemForm oliForm){
        try {
            EmailBean emailBean = new EmailBean();
            AssetNumberNewEmailBuilder assetNumberNewEmailBuilder = new AssetNumberNewEmailBuilder(orderForm,oliForm, emailBean);
            EmailDirector emailDirector = new EmailDirector(assetNumberNewEmailBuilder);
            emailDirector.construct();
            EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
            emailBusinessDelegate.sendEmail(emailBean);
        } catch (Exception ignore) {
        }
    }
    
     /**
     * Action that handles receiving an order
     */
    public ActionForward receiveOrder(ActionMapping mapping,
                                      ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        log.debug("in receiveOrder()");
        
        resetToken(request);
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);    
        AssetFormCollection assetForms = new AssetFormCollection();
        OrderForm orderForm = (OrderForm)form;
        User user = (User)request.getSession().getAttribute(ApplicationResources.USER);
        if(user == null) {
            return mapping.findForward("notAuthenticated");
        }
        
        if(orderForm.getCmd().equalsIgnoreCase(Command.ADD_NOTE)) {
            ArrayList oliFormsList = (ArrayList)orderForm.getOrderLineItemForms();
            OrderFormBuilder builder = new OrderFormBuilder(orderForm, null, user, daoFactory);
            String noteText = request.getParameter("note");
            builder.buildNewOrderNoteForm(noteText);
            return mapping.findForward("reload");
        }
        else if(orderForm.getCmd().equalsIgnoreCase(Command.SHOW_NOTES)) {
            orderForm.setShowNotes(Boolean.TRUE);
            return mapping.findForward("reload");
        }
        else if(orderForm.getCmd().equalsIgnoreCase(Command.HIDE_NOTES)) {
            orderForm.setShowNotes(Boolean.FALSE);
            return mapping.findForward("reload");
        }
        else {  //Otherwise, user clicked SUBMIT so handle accordingly.        
            //persist the notes
            Order order = daoFactory.getPurchasingOrderDAO().getOrderById(new Long(orderForm.getOrderId()), false);
            OrderBuilder orderBuilder = new OrderBuilder(order, orderForm, user, daoFactory);
            orderBuilder.buildNotes();
        
            Iterator iter = orderForm.getOrderLineItemForms().iterator();
            while(iter.hasNext()){
                OrderLineItemForm oliForm = (OrderLineItemForm)iter.next();
                OrderLineItem oli = daoFactory.getPurchasingOrderLineItemDAO()
                                              .getOrderLineItemById(new Long(oliForm.getOrderLineItemId()), false);
                OrderLineItemBuilder oliBuilder = new OrderLineItemBuilder(oli, 
                                                                           oliForm, 
                                                                           orderForm, 
                                                                           user, 
                                                                           daoFactory);
                OrderLineItemDirector director = new OrderLineItemDirector(oliBuilder);
                director.constructReceivedOLI();
                
                String qtyReceived = oliForm.getQuantityReceived();
                if(qtyReceived != null && !("".equals(qtyReceived) || "0".equals(qtyReceived)) ) {
                    Integer quantity = new Integer(qtyReceived);    
                    //get ready to record sensitive asset or fixed asset info, if necessary
                    if(OrderLineItem.FIXED_ASSET_TYPE.equals(oli.getAssetsType())) {
                        for(int i = 0; i < quantity.intValue(); i++) {
                            FixedAssetForm faForm = new FixedAssetForm();
                            FixedAsset fixedAsset = new FixedAsset();
                            FixedAssetFormBuilder faFormBuilder = new FixedAssetFormBuilder(oli, fixedAsset, faForm, daoFactory);
                            faFormBuilder.buildPropertiesFromOLI();
                            assetForms.getFixedAssetForms().add(faForm);
                        }

                }
                    if(OrderLineItem.SENSITIVE_ASSET_TYPE.equals(oli.getAssetsType())) {
                        for(int i = 0; i < quantity.intValue(); i++) {
                            SensitiveAssetForm saForm = new SensitiveAssetForm();
                            SensitiveAsset sensitiveAsset = new SensitiveAsset();
                            SensitiveAssetFormBuilder saFormBuilder = new SensitiveAssetFormBuilder(oli, sensitiveAsset, saForm, daoFactory);
                            saFormBuilder.buildPropertiesFromOLI();
                            assetForms.getSensitiveAssetForms().add(saForm);
                        }

                    }
                    
                    //increment qty on hand if the item is a stock item   
                    if(oli.getItem() != null) {
                        Item item = oli.getItem();
                        if(item.getItemType().equals(Item.STOCK_ITEM)) {
                            StockItem stockItem = daoFactory.getStockItemDAO().getStockItemById(item.getItemId(), false);
                            int currentQOH = stockItem.getQtyOnHand().intValue();
                            if(stockItem.getDispenseUnit().equals(oli.getBuyUnit())) {                        
                                stockItem.setQtyOnHand(new Integer(currentQOH + quantity.intValue()));      
                            }
                            else {
                                //unit mismatch.  user should enter qty in terms of dispense units.
                                Integer qtyReceivedInDispenseUnits = new Integer(oliForm.getQuantityReceivedInDispenseUnits());
                                //TODO - TODD implement validation for the input of getQuantityReceivedInDispenseUnits so
                                //       user does not get a 500 Internal Server Error
                                stockItem.setQtyOnHand(new Integer(currentQOH + qtyReceivedInDispenseUnits.intValue()));
                            }
                        }
                    }

                    if(oliForm.getAssetTypeEnum() != null) {
                        sendEmail(orderForm,oliForm);
                    }
                }
            }
            daoFactory.getPurchasingOrderDAO().makePersistent(order);
                
            //forward to the page to enter sensitive/fixed assets info if the assetForms collection isn't empty
            //  This functionality is not fully implemented, so the struts mapping viewEnterAssetInfo will do the same
            //  thing as success for now.
            //  Also, the person receiving a Fixed or Sensitive Asset probably does not have the Asset # or ready access to
            //  the serial #.  Currently Bruce Brokaw sends a form to the end-user to collect the information including serial #.
            if(!assetForms.getFixedAssetForms().isEmpty() || !assetForms.getSensitiveAssetForms().isEmpty()) {
               request.getSession().setAttribute("assetFormCollection", assetForms);
               log.debug("in receiveAction()... forwarding to viewEnterAssetInfo action");
               return mapping.findForward("viewEnterAssetInfo");
            }
        } 
        return mapping.findForward("success");
    }    
    
    /**
     * Action that prepares view of Receiving History for a particular Order line item
     * TODO - there ought to be a better way of getting the RequestDetails w/o having to go 
     * back to the DB again.  Shouldn't it already be available on the session?
     * Can't I just pass in the OrderForm and make the receiveOrder page specify the oli Index, so that the 
     * viewReceivingHistory page would just display the ReceivingDetail objects for that particular oli form?
     * Or maybe in the receive Order action you just take the OLI form and put it on the session and let the 
     * view history page pull it with that name?  
     */
    public ActionForward viewReceivingHistory(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        String oliId = request.getParameter("oliId");
        OrderLineItem oli = daoFactory.getPurchasingOrderLineItemDAO()
                                      .getOrderLineItemById(new Long(oliId), false);
        oli.getReceivingDetails();
        Iterator iter = oli.getReceivingDetails().iterator();
        while(iter.hasNext()){
            ReceivingDetail detail = (ReceivingDetail)iter.next();
        }
        request.getSession().setAttribute("oliReceivingHistoryBean", oli);
        return mapping.findForward("success");
    } 
    
    /**
     * Action that prepares view of Enter Asset Info
     */
    public ActionForward viewEnterAssetInfo(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {
        log.debug("in viewEnterAssetInfo()");
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        AssetFormCollection assetForms = (AssetFormCollection)form;
    
        log.debug("in viewEnterAssetInfo()... saving new token");
        saveToken(request);
        
        return mapping.findForward("success");
    } 
    
    /**
     * Action that records sensitive asset and fixed asset info
     */
    public ActionForward enterAssetInfo(ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        log.debug("in enterAssetInfo()");
        if(isTokenValid(request)) {
            resetToken(request);
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
            User user = (User)request.getSession().getAttribute(ApplicationResources.USER);
            AssetFormCollection assetForms = (AssetFormCollection)form;
            
            Iterator fixedAssetsIter = assetForms.getFixedAssetForms().iterator();
            while(fixedAssetsIter.hasNext()) {
                FixedAssetForm faForm = (FixedAssetForm)fixedAssetsIter.next();
                FixedAsset fa = new FixedAsset();
                FixedAssetBuilder faBuilder = new FixedAssetBuilder(fa, faForm, daoFactory, user);
                FixedAssetDirector director = new FixedAssetDirector(faBuilder);
                director.construct();
                daoFactory.getFixedAssetDAO().makePersistent(fa);            
            }
            
            Iterator sensitiveAssetsIter = assetForms.getSensitiveAssetForms().iterator();
            while(sensitiveAssetsIter.hasNext()) {
                SensitiveAssetForm saForm = (SensitiveAssetForm)sensitiveAssetsIter.next();
                SensitiveAsset sa = new SensitiveAsset();
                SensitiveAssetBuilder saBuilder = new SensitiveAssetBuilder(sa, saForm, daoFactory, user);
                SensitiveAssetDirector director = new SensitiveAssetDirector(saBuilder);
                director.construct();
                daoFactory.getSensitiveAssetDAO().makePersistent(sa);
            }
        }
        else {
            log.debug("in enterAssetInfo()... invalid token.  Saving new token.");
            saveToken(request);
        }
                
        return mapping.findForward("success");
    } 
    
    public ActionForward viewCreateDeliveryTicket(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        String oliId = request.getParameter("oliId");
//        OrderLineItem oli = daoFactory.getPurchasingOrderLineItemDAO()
//                                      .getOrderLineItemById(new Long(oliId), false);
        OrderLineItem oli = daoFactory.getPurchasingOrderLineItemDAO().getOrderLineItemById(new Long(oliId));
        Hibernate.initialize(oli.getOrder());
        Iterator iter = oli.getRequestLineItems().iterator();
        while(iter.hasNext()) {
            RequestLineItem rli = (RequestLineItem)iter.next();
        }
        DeliveryTicketForm deliveryTicketForm = (DeliveryTicketForm)form;
        DeliveryTicketFormBuilder builder = new DeliveryTicketFormBuilder(deliveryTicketForm, oli);
        builder.buildOrderLineItem();
        builder.buildDefaultProperties();
        
        return mapping.findForward("success");
    }
    
    /**
     * Action that records delivery ticket info
     */
    public ActionForward createDeliveryTicket(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        User user = (User)request.getSession().getAttribute(ApplicationResources.USER);
        DeliveryTicketForm deliveryTicketForm = (DeliveryTicketForm)form;
        DeliveryTicket deliveryTicket = new DeliveryTicket();
        DeliveryTicketBuilder builder = new DeliveryTicketBuilder(deliveryTicketForm, deliveryTicket, user, daoFactory);
        builder.buildRequestLineItem();
        builder.buildSimpleProperties();
        
        if(deliveryTicketForm.getCmd().equals(Command.PRINT_DELIVERY_TICKET)){
            ArrayList collection = new ArrayList();
            collection.add(deliveryTicket);
            JasperReportWriter reportWriter = new JasperReportWriter(this.getServlet(),
                                                                     Report.DELIVERY_TICKET,
                                                                     Report.RECEIVING_OUTPUT_PATH,
                                                                     collection,
                                                                     new HashMap());    
            return new ActionForward(reportWriter.write(), true);
        }
        else {
            daoFactory.getDeliveryTicketDAO().makePersistent(deliveryTicket);
                        
            return mapping.findForward("success");
        }
    }
}