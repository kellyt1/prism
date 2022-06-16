package us.mn.state.health.matmgmt.action;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.search.BooleanQuery;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.builder.email.EmailBuilder;
import us.mn.state.health.builder.email.EvaluateSIAREmailBuilder;
import us.mn.state.health.builder.email.NewSIAREmailBuilder;
import us.mn.state.health.builder.inventory.*;
import us.mn.state.health.builder.materialsrequest.RequestBuilder;
import us.mn.state.health.builder.materialsrequest.RequestFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.inventory.StockItemDAO;
import us.mn.state.health.matmgmt.director.*;
import us.mn.state.health.matmgmt.director.email.EmailDirector;
import us.mn.state.health.matmgmt.util.Bean;
import us.mn.state.health.matmgmt.util.Form;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.ActionRequestType;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.StockItemActionRequest;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.EmailBusinessDelegate;
import us.mn.state.health.model.util.search.LuceneQueryBuilder;
import us.mn.state.health.model.util.search.StockItemIndex;
import us.mn.state.health.security.ApplicationResources;
import us.mn.state.health.view.inventory.*;
import us.mn.state.health.view.materialsrequest.RequestForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.materialsrequest.SearchCatalogForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * Controller class for Stock Item Actions
 *
 * @author Lucian Ochian, Shawn Flahave, Jason Stull
 */

public final class StockItemAction extends MappingDispatchAction {
    public static Log log = LogFactory.getLog(StockItemAction.class);

    /**
     * Action that handles Evaluate Stock Item Action Request commands
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */

    public ActionForward evaluateStockItemActionRequest(ActionMapping mapping,
                                                        ActionForm form,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) throws Exception {
        StockItemActionRequestForm siarForm = (StockItemActionRequestForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        String forwardName = "success";
        User evaluator = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (evaluator == null) {
            return mapping.findForward("failure");
        }
        //Load StockItemActionRequest to be updated
        Long siarId = new Long(siarForm.getStockItemActionRequestId());
        StockItemActionRequest siar = daoFactory.getStockItemActionRequestDAO()
                .findStockItemActionRequestById(siarId, false);

        //Build Potential Stock Item
        StockItemBuilder siBuilder = new StockItemBuilder(siar.getPotentialStockItem(),
                siarForm.getPotentialStockItemForm(),
                evaluator,
                daoFactory);
        StockItemDirector siDirector = new StockItemDirector(siBuilder);
        siDirector.construct();

        //Build Stock Item Action Request
        StockItemActionRequestBuilder siarBuilder =
                new StockItemActionRequestBuilder(evaluator, siar, siar.getPotentialStockItem(), siarForm, daoFactory);
        siarBuilder.buildRequestEvaluations();

        try {
            //Important: when the status is set, the siar is saved,
            // but the tx is not commited
            siarBuilder.buildStatus();
        } catch (Exception e) {
            siarForm.setReset(true);
            throw new RuntimeException("Error");
        }

        /*EmailBean emailBean = new EmailBean();
        EmailBuilder emailBuilder = new EvaluateSIAREmailBuilder(siarForm, evaluator, emailBean, siar);
        EmailDirector emailDirector = new EmailDirector(emailBuilder);
        emailDirector.construct();

        EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
        emailBusinessDelegate.sendEmail(emailBean);*/

        siarForm.setReset(true); //Reset command

        return mapping.findForward(forwardName);
    }

    /**
     * Action method that triggers StockItemActionRequest
     * to be generated in order for a new StockItem to
     * be approved
     *
     * @param mapping  Stuts ActionMapping configuration
     * @param form     StockItem view
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward
     *         if operation failed
     * @throws java.lang.Exception
     */
    public ActionForward requestNewStockItem(ActionMapping mapping,
                                             ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws Exception {
        StockItemActionRequestForm stockItemActionRequestForm = (StockItemActionRequestForm) form;
        StockItemForm stockItemForm = stockItemActionRequestForm.getPotentialStockItemForm();
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);

        //Build potential StockItem
        StockItem potentialStockItem = new StockItem();
        StockItemBuilder stockItemBuilder = new StockItemBuilder(potentialStockItem, stockItemForm, user, daoFactory);
        StockItemDirector stockItemDirector = new StockItemDirector(stockItemBuilder);
        stockItemDirector.construct();

        //Build StockItemActionRequest
        StockItemActionRequest stockItemActionRequest = new StockItemActionRequest();
        StockItemActionRequestBuilder siarBuilder = new StockItemActionRequestBuilder(user,
                stockItemActionRequest,
                potentialStockItem,
                stockItemActionRequestForm,
                ActionRequestType.NEW_STOCK_ITEM,
                daoFactory);
        NewStockItemActionRequestDirector stockItemActionRequestDirector = new NewStockItemActionRequestDirector(siarBuilder);
        stockItemActionRequestDirector.construct();

        //Execute StockItemActionRequest Business Rules
        stockItemActionRequest.executeBusinessRules();
        //todo - the following 4 lines should be able to be enabled and the above line
        // commented out to remove the need for the rules4j for a New Stock Item Request
        // tr - 5/27/2010
//        stockItemActionRequest.setApprovalRequired(true);
//        stockItemActionRequest.addEvaluatorGroup(Group.STOCK_CONTROLLER_CODE);
//        stockItemActionRequest.addEvaluatorGroup(Group.BUYER_CODE);
//        stockItemActionRequest.prepareForEvaluation();

        //set stock item and siar in session scope for confirmation page
        request.getSession().setAttribute(Bean.STK_ITM_ACTN_RQ, stockItemActionRequest);
        request.getSession().setAttribute(Bean.STK_ITM, stockItemActionRequest.getPotentialStockItem());

        //Send the email notification to buyers
        /*EmailBean emailBean = new EmailBean();
        EmailBuilder emailBuilder = new NewSIAREmailBuilder(stockItemActionRequest, user, emailBean);
        EmailDirector emailDirector = new EmailDirector(emailBuilder);
        emailDirector.construct();
        EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
        emailBusinessDelegate.sendEmail(emailBean);*/

        return mapping.findForward("success");
    }

    /**
     * Action that prepares view of StockItemActionRequest based on its id
     *
     * @param mapping
     * @param form     view StockItemActionRequest
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward
     *         if operation failed
     * @throws Exception
     */
    public ActionForward viewEvaluateStockItemActionRequest(ActionMapping mapping,
                                                            ActionForm form,
                                                            HttpServletRequest request,
                                                            HttpServletResponse response) throws Exception {
        StockItemActionRequestForm siarForm = (StockItemActionRequestForm) form;
        StockItemForm potentialStockItemForm = new StockItemForm();

        Long siarId = new Long(request.getParameter("siarId"));

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        StockItemActionRequest siar =
                daoFactory.getStockItemActionRequestDAO().findStockItemActionRequestById(siarId, false); //lock it ??  seems to hang when you do

        User evaluator = (User) request.getSession().getAttribute(ApplicationResources.USER);

        //Build potential stock item form
        StockItemFormBuilder siBuilder = new StockItemFormBuilder(potentialStockItemForm,
                siar.getPotentialStockItem(),
                daoFactory);
        EditStockItemFormDirector siDirector = new EditStockItemFormDirector(siBuilder);
        siDirector.construct();

        //Build stock item action request form
        StockItemActionRequestFormBuilder siarBuilder = new StockItemActionRequestFormBuilder(siarForm,
                potentialStockItemForm,
                daoFactory,
                siar,
                evaluator);
        EvaluateStockItemActionRequestFormDirector siarDirector =
                new EvaluateStockItemActionRequestFormDirector(siarBuilder);
        siarDirector.construct();

        return mapping.findForward("success");
    }

    /**
     * Action that perform a Lucene query only for Stock Items using the CONCATENATED_CONTENT field for query
     *
     * @param mapping
     * @param form     -  SearchCatalogForm object
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward searchStockItems(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        SearchCatalogForm searchCatalogForm = (SearchCatalogForm) form;

        String q = searchCatalogForm.getQuery();
        StockItemIndex index = new StockItemIndex();
        Collection results = new ArrayList();
        BooleanQuery luceneQuery = new BooleanQuery();
        LuceneQueryBuilder luceneQueryBuilder = new LuceneQueryBuilder(searchCatalogForm, luceneQuery);
        luceneQueryBuilder.addAndAny("query", StockItemIndex.CONCATENATED_CONTENT);

        if (searchCatalogForm.getCkShowInactive() == null || !searchCatalogForm.getCkShowInactive().equals("true")) {
            luceneQueryBuilder.addAndMatchPhrase("ckShowInactive", "status");
        }

        try {
//            results = index.search(q);
            results = index.search(luceneQuery);

        }
        catch (InfrastructureException e) {
            log.error("error in StockItemAction.searchStockItems: ", e);
        }
        searchCatalogForm.setResults(results);
        return mapping.findForward("success");
    }

    /**
     * Action that prepares the page for advanced search for Stock Items
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewAdvancedSearchStockItems(ActionMapping mapping,
                                                      ActionForm form,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) throws Exception {
        ItemsAdvancedSearchForm iasForm = (ItemsAdvancedSearchForm) form;
        iasForm.setCategoryCode("");
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        ItemsAdvancedSearchFormBuilder iasFormBuilder = new ItemsAdvancedSearchFormBuilder(iasForm, factory);
        ItemsAdvancedSearchFormDirector director = new ItemsAdvancedSearchFormDirector(iasFormBuilder);
        director.constructAdvancedSearchStockItemsForm();
        return mapping.findForward("success");
    }

    /**
     * Action that performs the advanced search for the stock items
     *
     * @param mapping
     * @param form     ItemsAdvancedSearchForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward advancedSearchStockItems(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws Exception {
        ItemsAdvancedSearchForm iasForm = (ItemsAdvancedSearchForm) form;
        iasForm.setCategoryCode("");
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        ItemsAdvancedSearchFormBuilder iasFormBuilder = new ItemsAdvancedSearchFormBuilder(iasForm, factory);
        ItemsAdvancedSearchFormDirector director = new ItemsAdvancedSearchFormDirector(iasFormBuilder);
        director.searchStockItemsAndConstruct();
        return mapping.findForward("success");
    }


    /**
     * Action method that prepares viewable list of StockItems for a
     * particular Owner
     *
     * @param mapping  Struts ActionMapping configuration
     * @param form     StockItem view
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward
     *         if operation failed
     * @throws java.lang.Exception
     */
    public ActionForward viewMyStockItems(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        StockItemsForm stockItemsForm = (StockItemsForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.LUCENE);

        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        String orderby = request.getParameter("orderBy");
        String viewAll = request.getParameter("viewAll");

        //Build StockItemsForm
        StockItemsFormBuilder builder = new StockItemsFormBuilder(stockItemsForm, daoFactory, user);
        if (StringUtils.isNotEmpty(viewAll) && viewAll.equalsIgnoreCase("true")) {
            builder.buildMyStockItems(orderby, false);
        } else {
            builder.buildMyStockItems(orderby, true);
        }

        return mapping.findForward("success");
    }

    public ActionForward viewStockItems(ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        request.setAttribute("searchCatalogForm", new SearchCatalogForm());
        return mapping.findForward("success");
    }

    /**
     * Prepares for the view of the Re-Order Stock Items page.  Brings back all the stock
     * items if the user is in the STOCK_CONTROLLER group, otherwise it just displays the stock
     * items which the user is a Primary or Secondary contact.
     *
     * @param response
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward to viewReorderStockItems.jsp if succesful
     * @throws Exception
     */
    public ActionForward viewReorderStockItems(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        StockItemsForm stockItemsForm = (StockItemsForm) form;
        stockItemsForm.getStockItemForms().clear();
        stockItemsForm.getStockItems().clear();
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        stockItemsForm.setContactId(user.getPersonId());
        Collection results;
        String paginationDirection = (String) request.getParameter(Form.PAGINATION_DIRECTION);
        int firstResult = stockItemsForm.getFirstResult().intValue();
        int maxResults = stockItemsForm.getMaxResults().intValue();
        if (Form.NEXT.equalsIgnoreCase(paginationDirection)) {
            firstResult += maxResults;
        } else if (Form.PREVIOUS.equalsIgnoreCase(paginationDirection)) {
            firstResult -= maxResults;
        }
        //default to only view active stock items(viewAll=false
        // - have not implemented a way yet to set viewAll to true
        String viewAll = request.getParameter("viewAll");
        if (StringUtils.isNotEmpty(viewAll) && viewAll.equalsIgnoreCase("true")) {
            stockItemsForm.setViewAll(Boolean.TRUE);
        }

        stockItemsForm.setFirstResult(new Integer(Math.max(firstResult, 0)));

        if (user.isInGroup(Group.STOCK_CONTROLLER_CODE)) {
            if (stockItemsForm.getViewAll()) {
                results = daoFactory.getStockItemDAO().findAll(firstResult, maxResults);
            } else {
                results = daoFactory.getStockItemDAO().findAllActive(firstResult, maxResults);
            }
        } else {
            results = daoFactory.getStockItemDAO().findByContactPerson(user, firstResult, maxResults);
        }

        if (results != null) {
            for (Iterator iter = results.iterator(); iter.hasNext();) {
                StockItem si = (StockItem) iter.next();
                StockItemForm siForm = new StockItemForm();
                StockItemFormBuilder builder = new StockItemFormBuilder(siForm, si, daoFactory);
                StockItemFormDirector director = new StockItemFormDirector(builder);
                director.constructForReorder();
                stockItemsForm.getStockItemForms().add(siForm);
            }
        }
        stockItemsForm.setDisplayNextLink(Boolean.TRUE);
        return mapping.findForward("success");
    }

    /**
     * Cancel a re-order request, BEFORE ITS BEEN SUBMITTED.  I.e., this just all removes items from
     * a re-order list.
     *
     * @param response
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward to viewReorderStockItems.jsp if succesful
     * @throws Exception
     */
    public ActionForward cancelReorderStockItems(ActionMapping mapping,
                                                 ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception {
        request.getSession().removeAttribute("stockItemReorderRequestForm");

        return mapping.findForward("success");
    }

    /**
     * This is a temporary method for searching stock items for the re-order stock items use case.
     * We really should re-evaluate the design of this system - we shouldn't have multiple frameworks
     * for doing the same thing - searching items.  In other words, we need to refactor certain parts of
     * this system, and this is one of them...
     * Right now, this only considers keyword search, and does not handle paging.
     *
     * @param response
     * @param request
     * @param form
     * @param mapping
     * @return
     * @throws Exception
     */
    public ActionForward searchStockItemsForReorder(ActionMapping mapping,
                                                    ActionForm form,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws Exception {
        StockItemsForm stockItemsForm = (StockItemsForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        stockItemsForm.getStockItemForms().clear();
        stockItemsForm.getStockItems().clear();
        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        stockItemsForm.setContactId(user.getPersonId());
        Collection results;
        StockItemIndex index = new StockItemIndex();
        String paginationDirection = (String) request.getParameter("paginationDirection");
        int firstResult = stockItemsForm.getFirstResult().intValue();
        int maxResults = stockItemsForm.getMaxResults().intValue();
        if ("next".equalsIgnoreCase(paginationDirection)) {
            firstResult += maxResults;
        } else if ("prev".equalsIgnoreCase(paginationDirection)) {
            firstResult -= maxResults;
        }
        stockItemsForm.setFirstResult(new Integer(Math.max(firstResult, 0)));

        BooleanQuery luceneQuery = new BooleanQuery();
        LuceneQueryBuilder luceneQueryBuilder = new LuceneQueryBuilder(stockItemsForm, luceneQuery);
        luceneQueryBuilder.addAndAny("query", StockItemIndex.CONCATENATED_CONTENT);
        if (!stockItemsForm.getCkShowInactive().equals("true")){
            luceneQueryBuilder.addAndMatchPhrase("statusActive", StockItemIndex.STATUS);
        }
        results = index.search(luceneQuery);

        if (!user.isInGroup(Group.STOCK_CONTROLLER_CODE)) {
            //filter out the items for which this user is not a contact
            StockItemDAO siDAO = DAOFactory.getDAOFactory(DAOFactory.LUCENE).getStockItemDAO();
            if (!stockItemsForm.getCkShowInactive().equals("true")){
                results.retainAll(siDAO.findByContactPerson(user));
            } else {
                results.retainAll(siDAO.findActiveByContactPerson(user));
            }
        }

        if (results != null) {   //the results hold StockItem objects, we need to make StockItemForm objects...
            for (Iterator iter = results.iterator(); iter.hasNext();) {
                StockItem si = (StockItem) iter.next();
                StockItemForm siForm = new StockItemForm();
                StockItemFormBuilder builder = new StockItemFormBuilder(siForm, si, daoFactory);
                StockItemFormDirector director = new StockItemFormDirector(builder);
                director.constructForReorder();
                stockItemsForm.getStockItemForms().add(siForm);
            }
        }
        stockItemsForm.setDisplayNextLink(Boolean.FALSE);
        stockItemsForm.setCkShowInactive("false");
        return mapping.findForward("success");
    }


    /**
     * Adds stock items to the 're-order list'
     *
     * @param response
     * @param request
     * @param form
     * @param mapping
     * @return
     * @throws Exception
     */
    public ActionForward addToReorderList(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (user == null) { //first, if the user isn't logged in, don't proceed
            return mapping.findForward("loginRequired");
        }

        StockItemsForm stockItemsForm = (StockItemsForm) form;
        RequestForm reqForm = (RequestForm) request.getSession().getAttribute("stockItemReorderRequestForm");
        if (reqForm == null) {    //cannot proceed without stock items to add
            reqForm = new RequestForm();
        }

        //build an RLI Form for each selected stock item or, if the stock item is already in the list, 
        //just increment the quantity by 1
        for (Iterator iter = stockItemsForm.getSelectedStockItemForms().iterator(); iter.hasNext();) {
            StockItemForm siForm = (StockItemForm) iter.next();
            boolean itemAlreadyInList = false;
            //iterate through all the rli forms and see if the current stock item is already in the list.  if it is
            //just increment the quantity by one. We don't want multiple rli's for the same item.
            for (Iterator regFormIter = reqForm.getRequestLineItemForms().iterator(); regFormIter.hasNext();) {
                RequestLineItemForm existingRliForm = (RequestLineItemForm) regFormIter.next();
                if (existingRliForm.getItem().equals(siForm.getStockItem())) {
                    int qty = Integer.parseInt(existingRliForm.getQuantity());
                    qty += 1;
                    existingRliForm.setQuantity(qty + "");
                    itemAlreadyInList = true;
                    break;
                }
            }
            //if the item is not already in the list, create a new RLI for it.
            if (!itemAlreadyInList) {
                RequestLineItemForm rliForm = new RequestLineItemForm();
                rliForm.setItem(siForm.getStockItem());
                rliForm.setIsForStockReorder(Boolean.TRUE);
                rliForm.setQuantity("1");

                reqForm.addRequestLineItemForm(rliForm);
            }
        }
        request.getSession().setAttribute("stockItemReorderRequestForm", reqForm);
        String msg = "The " + stockItemsForm.getSelectedStockItemForms().size() + " selected items have been added to your reorder list.<br />" +
                "The list now has a total of " + reqForm.getRequestLineItemForms().size() + " items.<br />" +
                "You can continue adding items or click the 'Proceed To Checkout' button to finish the process";
        request.setAttribute("message", msg);

        return mapping.findForward("success");
    }

    /**
     * Prepares view of the Checkout page for stock item re-orders.
     *
     * @param response
     * @param request
     * @param form
     * @param mapping
     * @return
     * @throws Exception
     */
    public ActionForward viewStockReorderCheckout(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (user == null) { //first, if the user isn't logged in, don't proceed
            return mapping.findForward("loginRequired");
        }
        RequestForm reqForm = (RequestForm) form;
     
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        RequestFormBuilder builder = new RequestFormBuilder(reqForm, daoFactory, null, user);
        builder.buildDeliveryDetailFormForStockReorder();
        builder.buildPriorities();
        builder.buildRequestor();
        return mapping.findForward("success");
    }

    public ActionForward removeRLIFormFromReorderList(ActionMapping mapping,
                                                      ActionForm form,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) throws Exception {
        RequestForm reqForm = (RequestForm) form;
        String rliIndexStr = (String) request.getParameter("rliIndex");
        if (rliIndexStr != null && !("".equals(rliIndexStr))) {
            int rliIndexPos = Integer.parseInt(rliIndexStr);
            Iterator iter = reqForm.getRequestLineItemForms().iterator();
            for (int i = 0; iter.hasNext(); i++) {
                RequestLineItemForm rliForm = (RequestLineItemForm) iter.next();
                if (i == rliIndexPos) {
                    reqForm.getRequestLineItemForms().remove(rliForm);
                    break;
                }
            }
        }
        return mapping.findForward("success");
    }

    /**
     * Action method that prepares view of new StockItem
     *
     * @param mapping  Struts ActionMapping configuration
     * @param form     StockItem view
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward
     *         if operation failed
     * @throws java.lang.Exception
     */
    public ActionForward viewRequestNewStockItem(ActionMapping mapping,
                                                 ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception {
        StockItemActionRequestForm stockItemActionRequestForm = (StockItemActionRequestForm) form;

        StockItemForm potentialStockItemForm = new StockItemForm(); //potential stock item form

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        //Build Potential Stock Item Form
        StockItemFormBuilder stockItemFormbuilder = new StockItemFormBuilder(potentialStockItemForm, daoFactory);
        NewStockItemFormDirector director = new NewStockItemFormDirector(stockItemFormbuilder);
        director.construct();

        //Build Stock Item Action Request Form
        StockItemActionRequestFormBuilder siarBuilder = new StockItemActionRequestFormBuilder(stockItemActionRequestForm,
                potentialStockItemForm,
                daoFactory);

        NewStockItemActionRequestFormDirector siarDirector = new NewStockItemActionRequestFormDirector(siarBuilder);
        siarDirector.constructNew();

        return mapping.findForward("success");
    }

    /**
     * Action method that prepares view of StockItemActionRequests for an evaluator.  The evaluator
     * will only see the open SIARs that he may evaluate.
     *
     * @param mapping
     * @param form     Collection of StockItemActionRequest's
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward
     *         if operation failed
     * @throws Exception
     */
    public ActionForward viewStockItemActionRequests(ActionMapping mapping,
                                                     ActionForm form,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        User evaluator = (User) request.getSession().getAttribute(ApplicationResources.USER);
        StockItemActionRequestsForm siarsForm = (StockItemActionRequestsForm) form;
        String[] statusCodes = {Status.WAITING_FOR_APPROVAL};
        String orderby = request.getParameter("orderBy");
        StockItemActionRequestsFormBuilder builder =
                new StockItemActionRequestsFormBuilder(siarsForm, daoFactory, statusCodes, evaluator);
        builder.buildStockItemActionRequests(orderby);

        return mapping.findForward("success");
    }

    /**
     * Action that submits the Request to re-order stock items.  Its the same model objects as a regular
     * end-consumer Request but the line items are all flagged as being for a stock reorder, so they aren't
     * subject to the rules regarding consumption and they'll go to purchasing rather than to the stock pickers
     * for fulfillment.  stock re-order checkout
     *
     * @param mapping
     * @param form
     * @param httpRequest
     * @param httpResponse
     * @return a successful forward if operation completed or a failure forward
     *         if operation failed
     * @throws Exception
     */
    public ActionForward reorderStockItem(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest httpRequest,
                                          HttpServletResponse httpResponse) throws Exception {
        RequestForm requestForm = (RequestForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        User requestor = (User) httpRequest.getSession().getAttribute(ApplicationResources.USER);
        if (requestor == null) {
            return mapping.findForward("loginRequired");
        }

        Request reorderRequest = new Request();
        requestForm.setRequestor(requestor);
        RequestBuilder builder = new RequestBuilder(reorderRequest, requestForm, daoFactory, requestor);
        RequestDirector director = new RequestDirector(builder);
        director.constructForStockReorderRequest();
        reorderRequest.executeBusinessRules(true);
        reorderRequest.assignTrackingNumber();
        reorderRequest.save();

        requestForm.setTrackingNumber(reorderRequest.getTrackingNumber());

        //send of an email to anybody that needs to approve this request.
        //MaterialsRequestAction.sendEmailToEvaluators(reorderRequest);

        //clear some stuff.... 
        requestForm.getRequestLineItemForms().clear();
        requestForm.setAdditionalInstructions("");
        requestForm.setNeedByDate("");
        return mapping.findForward("success");
    }

    public ActionForward viewInventoryReports(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest httpRequest,
                                              HttpServletResponse httpResponse) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        //PrismExternalReports preports = new preports
        Collection externalReports = daoFactory.getExternalReportDAO().findAll();
        httpRequest.setAttribute("ExternalReports", externalReports);

        return mapping.findForward("success");
    }
}