package us.mn.state.health.matmgmt.action

import groovy.util.logging.Log4j
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.builder.purchasing.OrderBuilder;
import us.mn.state.health.builder.purchasing.OrderFormBuilder;
import us.mn.state.health.builder.purchasing.SearchOrdersFormBuilder;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.report.JasperReportWriter;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.StatusDAO;
import us.mn.state.health.dao.materialsrequest.RequestLineItemDAO;
import us.mn.state.health.matmgmt.director.OrderDirector;
import us.mn.state.health.matmgmt.director.OrderFormDirector;
import us.mn.state.health.matmgmt.director.SearchOrdersFormDirector;
import us.mn.state.health.matmgmt.util.Form;
import us.mn.state.health.matmgmt.util.Report;
import us.mn.state.health.model.common.*;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.model.util.HibernateModelDetacher;
import us.mn.state.health.model.util.ModelDetacher;
import us.mn.state.health.security.ApplicationResources;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemSearchForm;
import us.mn.state.health.view.purchasing.OrderForm;
import us.mn.state.health.view.purchasing.OrderNoteForm;
import us.mn.state.health.view.purchasing.OrdersForm;
import us.mn.state.health.view.purchasing.SearchOrdersForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Struts Controller that processes Order actions
 */
@Log4j
public class OrderAction extends MappingDispatchAction {
    public ActionForward addPurchaseOrderNote(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in addPurchaseOrderNote()");
        OrderForm orderForm = (OrderForm) actionForm;
        User noteAuthor = (User) request.getSession().getAttribute(ApplicationResources.USER);
        OrderFormBuilder builder = new OrderFormBuilder(orderForm, noteAuthor);
        builder.buildNewNoteForm();

        return mapping.findForward("success");
    }

    public ActionForward removePurchaseOrderNote(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in removePurchaseOrderNote()");
        OrderForm orderForm = (OrderForm) actionForm;
        String noteFormKey = orderForm.getNoteFormKey();
        OrderNoteForm noteForm = (OrderNoteForm) CollectionUtils.getObjectFromCollectionById(orderForm.getOrderNoteForms(), noteFormKey, "key");
        noteForm.setRemoved(true);

        return mapping.findForward("success");
    }

    /**
     * Action that prepares Order search view
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewSearchOrders(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in viewSearchOrders()");
        SearchOrdersForm searchOrdersForm = (SearchOrdersForm) actionForm;

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        SearchOrdersFormBuilder builder = new SearchOrdersFormBuilder(searchOrdersForm, daoFactory);
        SearchOrdersFormDirector director = new SearchOrdersFormDirector(builder);
        director.construct();

        searchOrdersForm.setReset(true);
        searchOrdersForm.reset(mapping, request);

        return mapping.findForward("success");
    }

    /**
     * Action that searches for Orders
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward searchOrders(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in searchOrders()");
        SearchOrdersForm searchOrdersForm = (SearchOrdersForm) actionForm;

        String paginationDirection = request.getParameter(Form.PAGINATION_DIRECTION);
        int firstResult = searchOrdersForm.getFirstResult();
        int maxResults = searchOrdersForm.getMaxResults();
        String pageNo = searchOrdersForm.getPageNo();
        if (pageNo != null) {
            if (StringUtils.nullOrBlank(paginationDirection)) {
                firstResult = Integer.parseInt(pageNo) * searchOrdersForm.getMaxResults();
                searchOrdersForm.setFirstResult(firstResult);
            } else {
                firstResult = handlePaginationForSearchOrdersForm(paginationDirection, maxResults, firstResult, searchOrdersForm);
                searchOrdersForm.setFirstResult(firstResult);
            }
        } else {
            firstResult = handlePaginationForSearchOrdersForm(paginationDirection, maxResults, firstResult, searchOrdersForm);
        }

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        SearchOrdersFormBuilder builder = new SearchOrdersFormBuilder(searchOrdersForm, daoFactory);
        SearchOrdersFormDirector director = new SearchOrdersFormDirector(builder);
        builder.buildSearchResults(firstResult, maxResults);

        searchOrdersForm.setDisplayNextLink(searchOrdersForm.getSearchResults().size() > maxResults);

        return mapping.findForward("success");
    }

    private static int handlePaginationForSearchOrdersForm(
            String paginationDirection,
            int maxResults,
            int firstResult,
            SearchOrdersForm searchOrdersForm) {
        if (Form.NEXT.equalsIgnoreCase(paginationDirection)) {
            firstResult += maxResults;
        } else if (Form.PREVIOUS.equalsIgnoreCase(paginationDirection)) {
            firstResult -= maxResults;
        } else {
            firstResult = 0;
            searchOrdersForm.setDisplayNextLink(true);
        }
        searchOrdersForm.setFirstResult(firstResult);
        searchOrdersForm.setPageNo("" + firstResult / maxResults);
        return firstResult;
    }


    public ActionForward addReqLnItmsToExistingOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in addReqLnItmsToExistingOrder()");
        RequestLineItemSearchForm reqLnItmSrchForm = (RequestLineItemSearchForm) actionForm;
        String input = request.getParameter("input");
        OrdersForm ordersForm = (OrdersForm) request.getSession().getAttribute(Form.ORDERS);
        if (ordersForm == null) {
            ordersForm = new OrdersForm();
            request.getSession().setAttribute(Form.ORDERS, ordersForm);
        }
        ordersForm.setInput(input);

        return mapping.findForward("success");
    }

    public ActionForward chooseOrderVendor(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in chooseOrderVendor()");
        OrderForm orderForm = (OrderForm) actionForm;
        String vendorId = orderForm.getVendorId();
        Vendor vendor = null;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        if (!StringUtils.nullOrBlank(vendorId)) {
            vendor = daoFactory.getVendorDAO().getVendorById(new Long(vendorId), false);
        }
        if (vendor != null) {
            orderForm.setVendorContracts(vendor.getVendorContracts());
            orderForm.setVendorAccounts(vendor.getVendorAccounts());
            ExternalOrgDetail extOrgDetail = vendor.getExternalOrgDetail();
            // Initialize forms to nothing
            orderForm.setVendorPhones(null);
            orderForm.setVendorEmails(null);
            orderForm.setVendorFaxes(null);
            orderForm.setVendorReps(null);
            if (extOrgDetail != null) {
                if (extOrgDetail.getPhones().size() > 0) orderForm.setVendorPhones(extOrgDetail.getPhones());
                if (extOrgDetail.getEmailAddresses().size() > 0) orderForm.setVendorEmails(extOrgDetail.getEmailAddresses());
                if (extOrgDetail.getFaxes().size() > 0) orderForm.setVendorFaxes(extOrgDetail.getFaxes());
                if (extOrgDetail.getReps().size() > 0) orderForm.setVendorReps(extOrgDetail.getReps());

                Collection vendorAddresses = extOrgDetail.getMailingAddresses();
                orderForm.setVendorAddresses(vendorAddresses);
            }

            orderForm.setVendorContractId(null);
            orderForm.setVendorAccountId(null);
            orderForm.setVendorAddressId(null);
        }
        return mapping.findForward("success");
    }


    /**
     * Action that saves a Order
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward saveOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in saveOrder()");
        if (isTokenValid(request)) {
            def (boolean newOrder, OrderDirector director, OrderForm orderForm, OrderBuilder builder, DAOFactory daoFactory, Order order) = saveOrderCommon(actionForm, request)
            if (newOrder) {
                director.constructNewForPurchasing();
            } else {
                director.constructEditForPurchasing();
            }
            //release any un-ordered RLI's if the PO number has been entered
            if (!StringUtils.nullOrBlank(orderForm.getPurchaseOrderNumber())) {
                builder.releaseUnorderedRLIs();
            }
            daoFactory.getPurchasingOrderDAO().makePersistent(order);
            orderForm.setOrderId(order.getOrderId().toString());
            resetToken(request);
        } else {
            log.debug("in saveOrder()... token was NOT valid, so saving a new token");
        }

        return mapping.findForward("success");
    }

    private List saveOrderCommon(ActionForm actionForm, HttpServletRequest request) {
        log.debug("in saveOrder()... token was valid, so saving the order");
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        OrderForm orderForm = (OrderForm) actionForm;
        Order order;
        boolean newOrder = false;
        if (!StringUtils.nullOrBlank(orderForm.getOrderId())) { //Existing Order
            Long orderId = new Long(orderForm.getOrderId());
            order = daoFactory.getPurchasingOrderDAO().getOrderById(orderId, true);
            getDetacher().detachOrder(order);
        } else { //New Order
            order = new Order();
            newOrder = true;
        }

        User purchaser = (User) request.getSession().getAttribute(ApplicationResources.USER);
        OrderBuilder builder = new OrderBuilder(order, orderForm, purchaser, daoFactory);
        OrderDirector director = new OrderDirector(builder);
        [newOrder, director, orderForm, builder, daoFactory, order]
    }

    public ActionForward saveOrderComputer(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (isTokenValid(request)) {
            def (boolean newOrder, OrderDirector director, OrderForm orderForm, OrderBuilder builder, DAOFactory daoFactory, Order order) = saveOrderCommon(actionForm, request)
            if (newOrder) {
                director.constructNewForPurchasingComputer();
            } else {
                director.constructEditForPurchasingComputer();
            }
            //release any un-ordered RLI's if the PO number has been entered
//            if (!StringUtils.nullOrBlank(orderForm.getPurchaseOrderNumber())) {
//                builder.releaseUnorderedRLIs();
//            }
            daoFactory.getPurchasingOrderDAO().makePersistent(order);
            orderForm.setOrderId(order.getOrderId().toString());
            resetToken(request);
        } else {
            log.debug("in saveOrder()... token was NOT valid, so saving a new token");
        }

        return mapping.findForward("success");

    }
    /**
     * Action that saves a Order for Editor Role
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward saveOrder2(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in saveOrder()");
        if (isTokenValid(request)) {
            log.debug("in saveOrder()... token was valid, so saving the order");
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
            OrderForm orderForm = (OrderForm) actionForm;
            Order order;
            boolean newOrder = false;
            if (!StringUtils.nullOrBlank(orderForm.getOrderId())) { //Existing Order
                Long orderId = new Long(orderForm.getOrderId());
                order = daoFactory.getPurchasingOrderDAO().getOrderById(orderId, true);
                getDetacher().detachOrder(order);
            } else { //New Order
                order = new Order();
                newOrder = true;
            }

            User purchaser = (User) request.getSession().getAttribute(ApplicationResources.USER);
            OrderBuilder builder = new OrderBuilder(order, orderForm, purchaser, daoFactory);
            OrderDirector director = new OrderDirector(builder);
            if (newOrder) {
                director.constructNewForPurchasing();
            } else {
                director.constructEditForPurchasing();
            }
            //release any un-ordered RLI's if the PO number has been entered
            if (!StringUtils.nullOrBlank(orderForm.getPurchaseOrderNumber())) {
                builder.releaseUnorderedRLIs();
            }
            daoFactory.getPurchasingOrderDAO().makePersistent(order);
            orderForm.setOrderId(order.getOrderId().toString());
            resetToken(request);
        } else {
            log.debug("in saveOrder()... token was NOT valid, so saving a new token");
        }

        return mapping.findForward("success");
    }

    /**
     * Action that handles Printing of Purchase Order
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward printPurchaseOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in printPurchaseOrder()");
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        Long orderId = new Long(request.getParameter("orderId"));
        Order order = daoFactory.getPurchasingOrderDAO().getOrderById(orderId, false);
        getDetacher().detachOrder(order);
        return generateReport(request,response, order);
    }

    public ActionForward printPurchaseOrder2(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in printPurchaseOrder2()");
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        Long orderId = new Long(request.getParameter("orderId"));
        Order order = daoFactory.getPurchasingOrderDAO().getOrderById(orderId, false);
        getDetacher().detachOrder(order);
        return generateReport(request,response, order);
    }

    /**
     * Action that prepares create view of a Order
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewCreateOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in viewCreateOrder()");
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        RequestLineItemSearchForm form = (RequestLineItemSearchForm) actionForm;
        Collection selectedRLIForms = CollectionUtils.removeMatchingItems(form.getRliForms(),true,"selected");
        OrderForm orderForm = new OrderForm();
        OrderFormBuilder builder = new OrderFormBuilder(orderForm, selectedRLIForms, daoFactory);
        OrderFormDirector director = new OrderFormDirector(builder);
        director.constructNewForPurchasing();

        //put OrderForm(s) in session scope
        putOrderFormsInSessionScope(request.getSession(), orderForm);
        log.debug("in viewCreateOrder()... saving new token");
        saveToken(request);
        return mapping.findForward("success");
    }

    /**
     * Action that creates SWIFT order.
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward changeStatusSwiftItems(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        RequestLineItemDAO requestLineItemDAO = daoFactory.getRequestLineItemDAO();
        StatusDAO statusDAO = daoFactory.getStatusDAO();
        Status orderedSwift = statusDAO.getStatusByStatusCode(Status.SWIFT_ORDERED);
        RequestLineItemSearchForm form = (RequestLineItemSearchForm) actionForm;
        Collection selectedRLIForms = CollectionUtils.removeMatchingItems(form.getRliForms(),true,"selected");
        Set<RequestLineItemForm> swiftForms = new HashSet<RequestLineItemForm>();

        for(Object o : selectedRLIForms) {
            RequestLineItemForm requestLineItemForm = (RequestLineItemForm) o;
            RequestLineItem rli = requestLineItemDAO.getRequestLineItemById(Long.valueOf(requestLineItemForm.getRequestLineItemId()));
            if (rli?.getSwiftItemId()) {
                rli.setStatus(orderedSwift);
                requestLineItemDAO.makePersistent(rli);
            }
        }

        saveToken(request);
        return mapping.findForward("success");
    }

    public ActionForward viewSelectedRLIsDollarAmounts(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in viewSelectedRLIsDollarAmounts()");
        RequestLineItemSearchForm form = (RequestLineItemSearchForm) actionForm;
        Collection<RequestLineItemForm> selectedRLIForms = CollectionUtils.getMatchingItems(form.getRliForms(),true,"selected");

        generateReport(request, response, selectedRLIForms);
        return null;
    }

    /**
     * Action that prepares edit view of a Order
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward reloadVendorList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in reloadVendorList()");
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        OrderForm orderForm = (OrderForm) actionForm;
        String startChar = request.getParameter("start");
        String endChar = request.getParameter("end");
        if (!StringUtils.nullOrBlank(startChar) && !StringUtils.nullOrBlank(endChar)) {
            orderForm.setVendorNameFirstCharStart(startChar);
            orderForm.setVendorNameFirstCharEnd(endChar);
        }
        OrderFormBuilder builder = new OrderFormBuilder(orderForm, daoFactory);
        builder.buildVendors();

        return mapping.findForward("success");
    }

    /**
     * Action that prepares edit view of a Order for Editor Role
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward reloadVendorList2(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in reloadVendorList2()");
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        OrderForm orderForm = (OrderForm) actionForm;
        String startChar = request.getParameter("start");
        String endChar = request.getParameter("end");
        if (!StringUtils.nullOrBlank(startChar) && !StringUtils.nullOrBlank(endChar)) {
            orderForm.setVendorNameFirstCharStart(startChar);
            orderForm.setVendorNameFirstCharEnd(endChar);
        }
        OrderFormBuilder builder = new OrderFormBuilder(orderForm, daoFactory);
        builder.buildVendors();

        return mapping.findForward("success");
    }

    /**
     * Action that prepares edit view of a Order
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewEditOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in viewEditOrder()");
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        OrderForm orderForm = (OrderForm) actionForm;

        OrdersForm ordersForm = (OrdersForm) request.getSession().getAttribute(Form.ORDERS);

        //Look up Order
        String orderIdStr = request.getParameter("orderId") ?: orderForm.getOrderId()
        Order order = daoFactory.getPurchasingOrderDAO().getOrderById(new Long(orderIdStr));
        getDetacher().detachOrder(order);
        Collection newRLIForms = null;
        if (ordersForm != null) {
            if (ordersForm.getInput().equals("purchasingRequestLineItems")) {
                RequestLineItemSearchForm rliSrchForm = (RequestLineItemSearchForm) request.getSession().getAttribute(Form.REQ_LN_ITM_SRCH);
                newRLIForms = CollectionUtils.removeMatchingItems(rliSrchForm.getRliForms(), true, "selected");
                ordersForm.setInput("");
            }
        }

        //Build Order Form
        OrderFormBuilder builder = new OrderFormBuilder(orderForm, order, newRLIForms, daoFactory);
        OrderFormDirector director = new OrderFormDirector(builder);
        director.constructEditForPurchasing();

        putOrderFormsInSessionScope(request.getSession(), orderForm);
        log.debug("in viewEditOrder()... saving new token");
        saveToken(request);
        return mapping.findForward("success");
    }

    /**
     * Action that prepares edit view of a Order  ;  This is for Editor Role
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewEditOrder2(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("in viewEditOrder2()");
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        OrderForm orderForm = (OrderForm) actionForm;

        OrdersForm ordersForm = (OrdersForm) request.getSession().getAttribute(Form.ORDERS);

        //Look up Order
        String orderIdStr = request.getParameter("orderId") ?: orderForm.getOrderId()
        Order order = daoFactory.getPurchasingOrderDAO().getOrderById(new Long(orderIdStr));
        getDetacher().detachOrder(order);
        Collection newRLIForms = null;
        if (ordersForm != null) {
            if (ordersForm.getInput().equals("purchasingRequestLineItems")) {
                RequestLineItemSearchForm rliSrchForm = (RequestLineItemSearchForm) request.getSession().getAttribute(Form.REQ_LN_ITM_SRCH);
                newRLIForms = CollectionUtils.removeMatchingItems(rliSrchForm.getRliForms(), true, "selected");
                ordersForm.setInput("");
            }
        }

        //Build Order Form
        OrderFormBuilder builder = new OrderFormBuilder(orderForm, order, newRLIForms, daoFactory);
        OrderFormDirector director = new OrderFormDirector(builder);
        director.constructEditForPurchasing();

        putOrderFormsInSessionScope(request.getSession(), orderForm);
        log.debug("in viewEditOrder()... saving new token");
        saveToken(request);
        return mapping.findForward("success");
    }

    /**
     * Action that prepares read-only view of a Order (no building vendor lists, etc...)
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewOrderReadOnly(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        OrderForm orderForm = (OrderForm) actionForm;

        //Look up Order
        String orderIdStr = request.getParameter("orderId") ?: orderForm.getOrderId()
        Order order = daoFactory.getPurchasingOrderDAO().getOrderById(new Long(orderIdStr), false);
        getDetacher().detachOrder(order);

        //Build Order Form
        OrderFormBuilder builder = new OrderFormBuilder(orderForm, order, daoFactory);
        OrderFormDirector director = new OrderFormDirector(builder);
        director.constructReadOnly();

        return mapping.findForward("success");
    }

    /**
     * Action that marks a RequestLineItem for removal from an Order
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward removeRequestLineItemFromOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OrderForm orderForm = (OrderForm) actionForm;
        Long reqLnItmId = new Long(request.getParameter("requestLineItemId"));
        RequestLineItemForm reqLnItmForm =
                (RequestLineItemForm) CollectionUtils.getObjectFromCollectionById(orderForm.getRequestLineItemForms(), reqLnItmId, "requestLineItem.requestLineItemId");
        reqLnItmForm.setRemovedFromOrder(true);

        return mapping.findForward("success");
    }

    /* Helper Methods */

    public ActionForward generateReport(HttpServletRequest request,HttpServletResponse response, Order order) throws Exception {
        String reportFilePath;
        String outFilePath = Report.PURCHASING_OUTPUT_PATH;
        String reportType = request.getParameter("reportType");
        String subReportPath;
        HashMap hashmap = new HashMap();
        hashmap.put("BaseDir", this.getServlet().getServletContext().getRealPath(Report.IMAGES_DIR_PATH_NAME));

        User loggedInUser = (User) request.getSession().getAttribute(ApplicationResources.USER);

        Collection reportData = new ArrayList();
        if (reportType.equals("internalPO")) { //Print internal PO
            Boolean swift = false;
            subReportPath = "";

            String fundingSrcSubRptPath;

//            Collection<PurchaseOrderData> plcollection = new HashSet();
            Collection<OrderLineItem> oli = order.getOrderLineItems();
            for (Iterator<OrderLineItem> orderLineItemIterator = oli.iterator(); orderLineItemIterator.hasNext();) {
                PurchaseOrderData pld = new PurchaseOrderData();
                OrderLineItem orderLineItem = orderLineItemIterator.next();
                pld.addOrder(order);
                if (orderLineItem != null) swift = pld.addOrderLineItemFields(orderLineItem);
                reportData.add(pld);
            }
            if (swift) {   //fiscal year > 2011
                reportFilePath = Report.PURCHASE_ORDER_INTERNAL_SWIFT;
                fundingSrcSubRptPath = Report.REPORTS_DIR_PATH_NAME + "FundingSourcesSWIFT.jasper";
            } else {
                reportFilePath = Report.PURCHASE_ORDER_INTERNAL;
                fundingSrcSubRptPath = Report.REPORTS_DIR_PATH_NAME + "FundingSources.jasper";
            }
            hashmap.put("fundingSrcSubRpt", fundingSrcSubRptPath);
        } else if (reportType.equals("accountsPayablePO")) {
            reportFilePath = Report.ACCOUNTS_PAYABLE_PURCHASE_ORDER;

            subReportPath = Report.REPORTS_DIR_PATH_NAME + "PurchaseOrderLineItemInternalWithReceivingHistory.jasper";

            hashmap.put("fundingSrcSubRpt", Report.REPORTS_DIR_PATH_NAME + "FundingSources.jasper");
            hashmap.put("oliReceivingHistorySubRpt", Report.REPORTS_DIR_PATH_NAME + "oliReceivingHistory.jasper");
            reportData.add(order);
        } else {//print Vendor PO
            reportFilePath = Report.PURCHASE_ORDER;

            subReportPath = Report.REPORTS_DIR_PATH_NAME + "PurchaseOrderLineItem.jasper";

            //Collection pldc = new HashSet();
            PurchaseOrderData pld = new PurchaseOrderData();
            pld.addOrder(order);
            //use the purchaser's information, NOT the current logged in user.
            Person purchaser = order.getPurchaser();

            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
            User user = daoFactory.getUserDAO().findUserByUsername(purchaser.getNdsUserId());
            //if the person printing the PO is not the purchaser, then leave the Signature BLANK??

            File aFile = null;
            if (user != null) {
                String sigFile = Report.REPORTS_DIR_PATH_NAME  + "signatures/" + user.getNdsUserId().toUpperCase() + ".jpg";
                if (loggedInUser.getNdsUserId().equalsIgnoreCase(user.getNdsUserId())) {
                    try {
                        aFile = new File(sigFile);
                    } catch (Exception e) {
                        aFile = null;
                    }
                }
            }
            hashmap.put("signature",aFile );

            if (user != null) {
                if (pld.getPurchaser_emailAddress() == null) {
                    pld.setPurchaser_emailAddress(user.getEmailAddress());
                }
                if (pld.getPurchaser_workLandPhone() == null) {
                    pld.setPurchaser_workLandPhone(user.getWorkPhone());
                }
            }
            else {
                pld.setPurchaser_emailAddress("");
                pld.setPurchaser_workLandPhone("");
            }
            reportData.add(pld);
        }

        if (!subReportPath.equals("")) {
            hashmap.put("oliSubReport", subReportPath);
        }
        JasperReportWriter reportWriter = new JasperReportWriter(this.getServlet(), response, reportFilePath, outFilePath, reportData, hashmap);

        outFilePath = reportWriter.write();

        return null;
    }


    public ActionForward generateReport(HttpServletRequest request,HttpServletResponse response, Collection<RequestLineItemForm> rliForms) throws Exception {
        String outFilePath = Report.PURCHASING_OUTPUT_PATH;
        HashMap hashmap = new HashMap();
        String rptName="CostSelectedRLI";
        String reportFilePath = Report.REPORTS_DIR_PATH_NAME + rptName;
        Collection reportData = new ArrayList();
        String fundingSrcSubRptPath = Report.REPORTS_DIR_PATH_NAME + "FundingSources.jasper";

        hashmap.put("fundingSrcSubRpt", fundingSrcSubRptPath);
        for (Iterator rliFormIterator = rliForms.iterator(); rliFormIterator.hasNext();) {
            RequestLineItem rli = ((RequestLineItemForm)rliFormIterator.next()).getRequestLineItem();
            RequestLineItemData rliData = new RequestLineItemData(rli);
            reportData.add(rliData);
        }

        JasperReportWriter reportWriter = new JasperReportWriter(this.getServlet(), response, reportFilePath, outFilePath, reportData, hashmap);

        outFilePath = reportWriter.write();

        return new ActionForward(outFilePath, true);
    }


    private void putOrderFormsInSessionScope(HttpSession session, OrderForm orderForm) {
        session.setAttribute(Form.ORDER, orderForm);
        session.setAttribute(Form.VIEW_ADD_TO_OLI, orderForm);
    }

    private ModelDetacher getDetacher() {
        ModelDetacher detacher = new HibernateModelDetacher();
        return detacher;
    }
}