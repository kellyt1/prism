package us.mn.state.health.matmgmt.action

import groovy.util.logging.Log4j
import org.apache.struts.action.ActionForm
import org.apache.struts.action.ActionForward
import org.apache.struts.action.ActionMapping
import org.apache.struts.actions.MappingDispatchAction
import us.mn.state.health.builder.email.*
import us.mn.state.health.builder.inventory.ItemsAdvancedSearchFormBuilder
import us.mn.state.health.builder.materialsrequest.*
import us.mn.state.health.common.exceptions.InfrastructureException
import us.mn.state.health.common.lang.StringUtils
import us.mn.state.health.common.util.CollectionUtils
import us.mn.state.health.common.util.DateUtils
import us.mn.state.health.common.util.ForwardParameters
import us.mn.state.health.dao.DAOFactory
import us.mn.state.health.dao.HibernateDAOFactory
import us.mn.state.health.dao.NotificationEmailAddressDAO
import us.mn.state.health.dao.PersonDAO
import us.mn.state.health.dao.configuration.HibernateConfigurationItemDAO
import us.mn.state.health.matmgmt.director.*
import us.mn.state.health.matmgmt.director.email.EmailDirector
import us.mn.state.health.matmgmt.director.email.EmailDirectorBook
import us.mn.state.health.matmgmt.util.Constants
import us.mn.state.health.matmgmt.util.Form
import us.mn.state.health.model.common.*
import us.mn.state.health.model.inventory.Item
import us.mn.state.health.model.inventory.ItemVendor
import us.mn.state.health.model.materialsrequest.*
import us.mn.state.health.model.purchasing.Order
import us.mn.state.health.model.purchasing.OrderLineItem
import us.mn.state.health.model.util.configuration.ConfigurationItem
import us.mn.state.health.model.util.email.EmailBean
import us.mn.state.health.model.util.email.EmailBusinessDelegate
import us.mn.state.health.security.ApplicationResources
import us.mn.state.health.view.inventory.Command
import us.mn.state.health.view.inventory.ItemsAdvancedSearchForm
import us.mn.state.health.view.materialsrequest.*
import us.mn.state.health.view.materialsrequest.reports.PurchaseItemTransactionsForm
import us.mn.state.health.view.materialsrequest.reports.StockItemTransactionsForm

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

/**
 * Controller class for Actions related to requests for goods & services
 *
 * @author Lucian Ochian, Shawn Flahave, Jason Stull
 */
@Log4j
public class MaterialsRequestAction extends MappingDispatchAction {
    public ActionForward viewAdvancedSearchMyRequests(ActionMapping mapping,
                                                      ActionForm form,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) throws Exception {
        log.debug("in viewAdvancedSearchMyRequests()");
        SearchMaterialRequestsForm materialRequestsForm = (SearchMaterialRequestsForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        materialRequestsForm.setRequestForms(new ArrayList());
        SearchMaterialRequestsFormBuilder builder = new SearchMaterialRequestsFormBuilder(materialRequestsForm, user, daoFactory);
        SearchMaterialRequestsFormDirector director = new SearchMaterialRequestsFormDirector(builder);
        director.constructForMyRequests();
        return mapping.findForward("success");
    }

    public ActionForward advancedSearchMyRequests(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws Exception {
        long t1 = System.currentTimeMillis();
        log.debug("in advancedSearchMyRequests()");
        SearchMaterialRequestsForm materialRequestsForm = (SearchMaterialRequestsForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        String cmd = materialRequestsForm.getCmd();
        String rliId = request.getParameter("rliId");
        String forward = handleDetailsAndNotesForSearchMaterialRequestsForm(cmd, materialRequestsForm, rliId, daoFactory, request);
        if (forward != null) {
            return mapping.findForward(forward);
        }
        String paginationDirection = (String) request.getParameter(Form.PAGINATION_DIRECTION);
        int firstResult = materialRequestsForm.getFirstResult();
        int maxResults = materialRequestsForm.getMaxResults();
        firstResult = handlePaginationForSearchMaterialRequestsForm(paginationDirection, maxResults, firstResult, materialRequestsForm);


        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        SearchMaterialRequestsFormBuilder builder = new SearchMaterialRequestsFormBuilder(materialRequestsForm, user, daoFactory);
        builder.buildAdvancedSearchResultsForMyRequests(firstResult, maxResults);

        if (materialRequestsForm.getRequestForms().size() < maxResults) {
            materialRequestsForm.setDisplayNextLink(false);
        } else {
            materialRequestsForm.setDisplayNextLink(true);
        }


        log.info("time="+(System.currentTimeMillis()-t1));
        return mapping.findForward("success");
    }

    public ActionForward viewAdvancedSearchRequests(ActionMapping mapping,
                                                    ActionForm form,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws Exception {
        log.debug("in viewAdvancedSearchRequests()");
        SearchMaterialRequestsForm materialRequestsForm = (SearchMaterialRequestsForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        materialRequestsForm.setRequestForms(new ArrayList());
        SearchMaterialRequestsFormBuilder builder = new SearchMaterialRequestsFormBuilder(materialRequestsForm, user, daoFactory);
        SearchMaterialRequestsFormDirector director = new SearchMaterialRequestsFormDirector(builder);
        director.constructForAllRequests();
        materialRequestsForm.setDateRequestedTo(DateUtils.toString(new Date()));

        // *********** Default From date to today - 90 *******************************
        materialRequestsForm.setDateRequestedForm(DateUtils.toString(DateUtils.addDaysToDate(new Date(), -90)));
        return mapping.findForward("success");
    }

    public ActionForward advancedSearchRequests(ActionMapping mapping,
                                                ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        log.debug("in advancedSearchRequests()");
        SearchMaterialRequestsForm materialRequestsForm = (SearchMaterialRequestsForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        String cmd = materialRequestsForm.getCmd();
        String rliId = request.getParameter("rliId");
        String forward = handleDetailsAndNotesForSearchMaterialRequestsForm(cmd, materialRequestsForm, rliId, daoFactory, request);
        if (forward) {
            return mapping.findForward(forward);
        }
        String paginationDirection = (String) request.getParameter(Form.PAGINATION_DIRECTION);
        int firstResult = materialRequestsForm.getFirstResult();
        int maxResults = materialRequestsForm.getMaxResults();
        String pageNo = materialRequestsForm.getPageNo();
        if(pageNo){
            firstResult = Integer.parseInt(pageNo)*materialRequestsForm.getMaxResults();
            materialRequestsForm.setFirstResult(firstResult);
        }
        else {
            firstResult = handlePaginationForSearchMaterialRequestsForm(paginationDirection, maxResults, firstResult, materialRequestsForm);
        }


        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        SearchMaterialRequestsFormBuilder builder = new SearchMaterialRequestsFormBuilder(materialRequestsForm, user, daoFactory);
        builder.buildAdvancedSearchResultsForAllRequests(firstResult, maxResults);

        if (materialRequestsForm.getRequestForms().size() < maxResults) {
            materialRequestsForm.setDisplayNextLink(false);
        } else {
            materialRequestsForm.setDisplayNextLink(true);
        }



        return mapping.findForward("success");
    }

    /**
     * @param response
     * @param request
     * @param form     SearchMaterialRequestsForm
     * @param mapping
     * @return
     * @throws Exception
     */
    public ActionForward searchMyRequests(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        long t1 = System.currentTimeMillis();
        SearchMaterialRequestsForm materialRequestsForm = (SearchMaterialRequestsForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        String cmd = materialRequestsForm.getCmd();
        String rliId = request.getParameter("rliId");
        String forward = handleDetailsAndNotesForSearchMaterialRequestsForm(cmd, materialRequestsForm, rliId, daoFactory, request);
        if (forward) {
            return mapping.findForward(forward);
        }
        else if (StringUtils.nullOrBlank(cmd)) {
            String paginationDirection = (String) request.getParameter(Form.PAGINATION_DIRECTION);
            int firstResult = materialRequestsForm.getFirstResult();
            int maxResults = materialRequestsForm.getMaxResults();
            firstResult = handlePaginationForSearchMaterialRequestsForm(paginationDirection, maxResults, firstResult, materialRequestsForm);
            User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
            SearchMaterialRequestsFormBuilder builder = new SearchMaterialRequestsFormBuilder(materialRequestsForm, user, daoFactory);
            builder.buildSearchResults(firstResult,maxResults);
            if (materialRequestsForm.getRequestForms().size() < maxResults) {
                materialRequestsForm.setDisplayNextLink(false);
            } else {
                materialRequestsForm.setDisplayNextLink(true);
            }
        }
        log.info("time="+(System.currentTimeMillis()-t1));
        return mapping.findForward("success");
    }

    public ActionForward viewMyRequests(ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        long t1 = System.currentTimeMillis();
        SearchMaterialRequestsForm materialRequestsForm = (SearchMaterialRequestsForm) form;
        String cmd = materialRequestsForm.getCmd();
        String rliId = request.getParameter("rliId");
        String forward = handleDetailsAndNotesForSearchMaterialRequestsForm(cmd, materialRequestsForm, rliId, daoFactory, request);
        if (forward) {
            return mapping.findForward(forward);
        }
        else if (StringUtils.nullOrBlank(cmd)) {
            String paginationDirection = (String) request.getParameter(Form.PAGINATION_DIRECTION);
            int firstResult = materialRequestsForm.getFirstResult();
            int maxResults = materialRequestsForm.getMaxResults();
            firstResult = handlePaginationForSearchMaterialRequestsForm(paginationDirection, maxResults, firstResult, materialRequestsForm);
            User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
            SearchMaterialRequestsFormBuilder builder = new SearchMaterialRequestsFormBuilder(materialRequestsForm, user, daoFactory);
            builder.buildMyRequests(firstResult,maxResults);
            if (materialRequestsForm.getRequestForms().size() < maxResults) {
                materialRequestsForm.setDisplayNextLink(false);
            } else {
                materialRequestsForm.setDisplayNextLink(true);
            }
            log.info("time="+(System.currentTimeMillis()-t1));
            return mapping.findForward("success");
        }
        return mapping.findForward("success");
    }

    private static int handlePaginationForSearchMaterialRequestsForm(
            String paginationDirection,
            int maxResults,
            int firstResult,
            SearchMaterialRequestsForm materialRequestsForm) {
        if (Form.NEXT.equalsIgnoreCase(paginationDirection)) {
            firstResult += maxResults;
        }
        else if (Form.PREVIOUS.equalsIgnoreCase(paginationDirection)) {
            firstResult -= maxResults;
        }
        else {
            firstResult = 0;
            materialRequestsForm.setDisplayNextLink(true);
        }
        materialRequestsForm.setFirstResult(firstResult);
        materialRequestsForm.setPageNo(""+firstResult/maxResults);
        return firstResult;
    }

    /**
     * @param cmd
     * @param materialRequestsForm
     * @param rliId
     * @return a string that is the mapped forward name
     * @param request
     * @param daoFactory
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    private String handleDetailsAndNotesForSearchMaterialRequestsForm(String cmd,
                                                                      SearchMaterialRequestsForm materialRequestsForm,
                                                                      String rliId,
                                                                      DAOFactory daoFactory,
                                                                      HttpServletRequest request) throws InfrastructureException {
        String result = null;
        if(Command.CLOSE_RLI.equals(cmd)){
            //handle Stock and Purchase requests(including reorders)
            RequestLineItem rli = daoFactory.getRequestLineItemDAO().getRequestLineItemById(new Long(rliId), true);
            Status status;

            if(rli.getStatus().getStatusCode().equals(Status.WAITING_FOR_APPROVAL)){
                status = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.CANCELED);
                rli.setStatus(status);
                ArrayList requestFormsList = (ArrayList) materialRequestsForm.getRequestForms();
                RequestForm reqForm = (RequestForm) requestFormsList.get(Integer.parseInt(materialRequestsForm.getRequestFormIndex()));
                Collection requestLineItemForms = reqForm.getRequestLineItemForms();
                RequestLineItemForm rliForm = (RequestLineItemForm) CollectionUtils.getObjectFromCollectionById(requestLineItemForms, rliId, "requestLineItemId");
                rliForm.setStatusName(Status.CANCELED_NAME);
                rliForm.setStatusCode(Status.CANCELED);

            }
            else if(rli.getStatus().getStatusCode().equals(Status.WAITING_FOR_DISPERSAL)) {
                status = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.FULFILLED);
                rli.setStatus(status);
                ArrayList requestFormsList = (ArrayList) materialRequestsForm.getRequestForms();
                RequestForm reqForm = (RequestForm) requestFormsList.get(Integer.parseInt(materialRequestsForm.getRequestFormIndex()));
                Collection requestLineItemForms = reqForm.getRequestLineItemForms();
                RequestLineItemForm rliForm = (RequestLineItemForm) CollectionUtils.getObjectFromCollectionById(requestLineItemForms, rliId, "requestLineItemId");
                rliForm.setStatusName(Status.CANCELED_NAME);
                rliForm.setStatusCode(Status.CANCELED);
            }
            else if(rli.getStatus().getStatusCode().equals(Status.WAITING_FOR_PURCHASE)) {
                status = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.CANCELED);
                rli.setStatus(status);
                ArrayList requestFormsList = (ArrayList) materialRequestsForm.getRequestForms();
                RequestForm reqForm = (RequestForm) requestFormsList.get(Integer.parseInt(materialRequestsForm.getRequestFormIndex()));
                Collection requestLineItemForms = reqForm.getRequestLineItemForms();
                RequestLineItemForm rliForm = (RequestLineItemForm) CollectionUtils.getObjectFromCollectionById(requestLineItemForms, rliId, "requestLineItemId");
                rliForm.setStatusName(Status.CANCELED_NAME);
                rliForm.setStatusCode(Status.CANCELED);
            }
            
            materialRequestsForm.setCmd(null);
            
            return result = "success";
        }
        else if (Command.SHOW_DETAIL.equalsIgnoreCase(cmd)) {
            materialRequestsForm.setCmd("");
            ArrayList requestFormsList = (ArrayList) materialRequestsForm.getRequestForms();
            RequestForm requestForm = (RequestForm) requestFormsList.get(Integer.parseInt(materialRequestsForm.getRequestFormIndex()));
            
            User user = (User) request.getSession().getAttribute(ApplicationResources.USER);

            //Build the RequestForm
            long requestId = Long.valueOf(requestForm.getRequestId()).longValue();
            Request materialsRequest = daoFactory.getRequestDAO().getRequestById(requestId, false);  //lock it
            // Simulate a search

            RequestFormBuilder builder = new RequestFormBuilder(requestForm, daoFactory, materialsRequest);
            builder.buildRequestor();
            RequestFormDirector director = new RequestFormDirector(builder);
            director.rebuildRequestLineItemForms();
            if (requestForm.getRequestor() != null && requestForm.getRequestor().getNdsUserId() != null &&  requestForm.getRequestor().getNdsUserId().equals(user.getNdsUserId())) {
                requestForm.setIsOwnerOfRequest(true);
            }
            requestForm.setTrackingNumber(materialsRequest.getTrackingNumber());
            requestForm.setShowDetail(true);
            
            result = "success";
        }
        else if (Command.HIDE_DETAIL.equalsIgnoreCase(cmd)) {
            materialRequestsForm.setCmd("");
            ArrayList requestFormsList = (ArrayList) materialRequestsForm.getRequestForms();
            RequestForm reqForm = (RequestForm) requestFormsList.get(Integer.parseInt(materialRequestsForm.getRequestFormIndex()));
            reqForm.setShowDetail(false);
            
            result = "success";
        }
        else if (Command.SHOW_NOTES.equalsIgnoreCase(cmd)) {
            materialRequestsForm.setCmd("");
            ArrayList requestFormsList = (ArrayList) materialRequestsForm.getRequestForms();
            RequestForm reqForm = (RequestForm) requestFormsList.get(Integer.parseInt(materialRequestsForm.getRequestFormIndex()));
            Iterator iter = reqForm.getRequestLineItemForms().iterator();
            while (iter.hasNext()) {
                RequestLineItemForm rliForm = (RequestLineItemForm) iter.next();
                if (rliForm.getRequestLineItemId().equals(rliId)) {
                    rliForm.setShowNotes(true);
                    result = "success";
                    return result;
                }
            }
            result = "success";
            return result;
        }
        else if (Command.HIDE_NOTES.equalsIgnoreCase(cmd)) {
            materialRequestsForm.setCmd("");
            ArrayList requestFormsList = (ArrayList) materialRequestsForm.getRequestForms();
            RequestForm reqForm = (RequestForm) requestFormsList.get(Integer.parseInt(materialRequestsForm.getRequestFormIndex()));
            Iterator iter = reqForm.getRequestLineItemForms().iterator();
            while (iter.hasNext()) {
                RequestLineItemForm rliForm = (RequestLineItemForm) iter.next();
                if (rliForm.getRequestLineItemId().equals(rliId)) {
                    rliForm.setShowNotes(false);
                    result = "success";
                    return result; //break out of the loop now
                }
            }
            result = "success";
            return result;
        }
        else if (Command.ADD_NOTE.equalsIgnoreCase(cmd)) {
            materialRequestsForm.setCmd("");
            ArrayList requestFormsList = (ArrayList) materialRequestsForm.getRequestForms();
            RequestForm reqForm = (RequestForm) requestFormsList.get(Integer.parseInt(materialRequestsForm.getRequestFormIndex()));

            RequestLineItemForm rliForm = (RequestLineItemForm) CollectionUtils.getObjectFromCollectionById(reqForm.getRequestLineItemForms(), rliId, "requestLineItemId");
            String text = rliForm.getTextNote();
            if (!StringUtils.nullOrBlank(text)) {
                RequestLineItem rli = daoFactory.getRequestLineItemDAO().getRequestLineItemById(new Long(rliId), false);
                RequestLineItemNote rliNote = new RequestLineItemNote();
                User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
                rliNote.setAuthorName(user.getFirstAndLastName());
                rliNote.setInsertionDate(new Date());
                rliNote.setNoteText(text);
                rli.addNote(rliNote);
                daoFactory.commitTransaction(true);

                ArrayList rliNoteForms = (ArrayList) rliForm.getNoteForms();
                RequestLineItemNoteForm rliNoteForm = new RequestLineItemNoteForm();
                RequestLineItemNoteFormBuilder builder = new RequestLineItemNoteFormBuilder(rliNoteForm, rliNote, daoFactory);
                builder.buildSimpleProperties();
                rliNoteForms.add(0, rliNoteForm);
                rliForm.setTextNote(null);
            }
            result = "success";
        }
        return result;
    }
         private void buildItemCostAndEstimate(RequestLineItemForm rliForm,DAOFactory daoFactory) throws InfrastructureException {
        // If it is a stock item,  show item cost from primary vendor
        if (rliForm.getItem()!=null) {
            Collection itemVendors = daoFactory.getVendorDAO().findItemVendorsById(rliForm.getItem().getItemId());
               for (Iterator<ItemVendor> iterator = itemVendors.iterator(); iterator.hasNext();) {
                    ItemVendor lVendor =  iterator.next();
                    if (lVendor.getPrimaryVendor()) {
                        rliForm.setItemCost(lVendor.getBuyUnitCost().toString());
                        if (rliForm.getQuantity() != null && !rliForm.getQuantity().equals("")&& lVendor.getBuyUnit().getCode().equalsIgnoreCase("EACH")) {
                                rliForm.setEstimatedCost(lVendor.getBuyUnitCost() * Double.valueOf(rliForm.getQuantity()));
                        } else rliForm.setEstimatedCost("0");
                    }
            }
        }

    }


    /**
     * Action that handles adding any type of item to the shopping cart
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward addItemToCart(ActionMapping mapping,
                                       ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        RequestLineItemForm rliForm = (RequestLineItemForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
             
        String skin = request.getSession().getAttribute("skin") != null?
                request.getSession().getAttribute("skin").toString() :
                "";

        if(rliForm.getCmd().equalsIgnoreCase(Command.ADD_FUNDING_SOURCE)) {
            if(isTokenValid(request)) {
                RequestLineItemFormBuilder rliFormBuilder = new RequestLineItemFormBuilder(rliForm, daoFactory);
                rliFormBuilder.addNewRequestLineItemFundingSourceForm(true);
            }
            return mapping.findForward(Command.ADD_FUNDING_SOURCE);
        }
        else if(rliForm.getCmd().equalsIgnoreCase(Command.CANCEL)) {
            return mapping.findForward(Command.CANCEL);
        }
//
//  Not used in this version
//    Used to allow attaching multiple files to a request line item
//
        else if(rliForm.getCmd().equalsIgnoreCase(Command.UPLOAD_FILE)) {
            if(isTokenValid(request)) {
                RequestLineItem requestLineItem = rliForm.getRequestLineItem();
                RequestLineItemBuilder builder = new RequestLineItemBuilder(requestLineItem, rliForm, daoFactory,(User)request.getSession().getAttribute(ApplicationResources.USER));
                RequestLineItemDirector director = new RequestLineItemDirector(builder);
                director.attachFile();                        
            }
            return mapping.findForward(Command.UPLOAD_FILE);
        }
        else if(rliForm.getCmd().endsWith(Command.ADD_TO_SHOPPING_LIST)) {
            return mapping.findForward(Command.ADD_TO_SHOPPING_LIST);
        }
        else {  //they either clicked 'Checkout' or 'Add To Cart'
            //this block of code can probably be refactored into a helper method.
            if(isTokenValid(request)) {
                resetToken(request);
                RequestLineItem requestLineItem = new RequestLineItem();
                RequestLineItemBuilder builder = new RequestLineItemBuilder(requestLineItem, rliForm, daoFactory,(User)request.getSession().getAttribute(ApplicationResources.USER));
                RequestLineItemDirector director = new RequestLineItemDirector(builder);

                director.createNewLineItem(skin);

                Request shoppingCart = (Request) request.getSession().getAttribute("shoppingCart");
                if(!shoppingCart) {
                    shoppingCart = new Request();
                }

                RequestLineItem existingRLI = null;
                //check if it's a catalog item or not
                Item item = requestLineItem.getItem();
                if(item != null) {
                    Long rliItemId = item.getItemId();
                    for(Iterator iterator = shoppingCart.getRequestLineItems().iterator(); iterator.hasNext();) {
                        RequestLineItem lineItem = (RequestLineItem) iterator.next();
                        Item rliItem = lineItem.getItem();
                        if(rliItem != null) {
                            Long itemId = rliItem.getItemId();
                            if(itemId.equals(rliItemId)) {
                                existingRLI = lineItem;
                                break;
                            }
                        }
                    }
                }

                //if it's a catalog item and it's already in the cart then update the qty
                if(existingRLI != null) {
                    //update here the funding sources if the item is not a SI
                    // - for the same FS update the amount
                    // - if the FS doesn't exist, add it
                    int previousQty = existingRLI.getQuantity().intValue();
                    int currentQty = requestLineItem.getQuantity().intValue();
                    int newQty = previousQty + currentQty;
                    existingRLI.setQuantity(new Integer(newQty));
                    Collection fss = requestLineItem.getFundingSources();
                    Collection existingFss = existingRLI.getFundingSources();
                    for(Iterator iterator = fss.iterator(); iterator.hasNext();) {
                        RequestLineItemFundingSource requestLineItemFundingSource = (RequestLineItemFundingSource) iterator.next();
                        OrgBudget orgBudget = requestLineItemFundingSource.getOrgBudget();
                        boolean fundingSourceExists = false;
                        for(Iterator iterator1 = existingFss.iterator(); iterator1.hasNext();) {
                            RequestLineItemFundingSource existingRequestLineItemFundingSource = (RequestLineItemFundingSource) iterator1.next();
                            OrgBudget existingOrgBudget = existingRequestLineItemFundingSource.getOrgBudget();
                            if(orgBudget.equals(existingOrgBudget)) {
                                Double existingAmount = existingRequestLineItemFundingSource.getAmount();
                                Double newAmount = requestLineItemFundingSource.getAmount();
                                Double cuurrentAmount = new Double(existingAmount.doubleValue() + newAmount.doubleValue());
                                existingRequestLineItemFundingSource.setAmount(cuurrentAmount);
                                fundingSourceExists = true;
                            }
                        }
                        if(!fundingSourceExists) {
                            existingRLI.getFundingSources().add(requestLineItemFundingSource);
                        }
                    }
                }
                else {
                    //else just add the new RLI to the cart
                    shoppingCart.addRequestLineItem(requestLineItem);
                    // Get the first funding source and save it as the last session used.

                    if (requestLineItem.getFundingSources() != null) {
                        Iterator it = requestLineItem.getFundingSources().iterator();
                        while (it.hasNext()) {
                           RequestLineItemFundingSource aa = (RequestLineItemFundingSource) it.next();
                           request.getSession().setAttribute("defaultOrg",aa.getOrgBudget().getOrgBudgetId().toString());

                        }
                    }

                }

                User requestor = (User) request.getSession().getAttribute(ApplicationResources.USER);
                shoppingCart.setRequestor(requestor);
                request.getSession().setAttribute("shoppingCart", shoppingCart);
            }
            return mapping.findForward(Command.ADD_TO_CART);
        }
    }

    public ActionForward viewAddNonCatalogItemToCart(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {

        RequestLineItemForm rliForm = (RequestLineItemForm) form;
        rliForm.reset();
        return prepareViewAddNonCatalogItemToCart(mapping,form,request,response);
    }

    public ActionForward viewAddStaffToCart(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {


        RequestLineItemForm rliForm = (RequestLineItemForm) form;
        rliForm.reset();
        rliForm.setSuggestedVendorCatalogNumber("STAFFAUG");
        rliForm.setBudgetBuilderLink(budgetBuilderLink());
        return prepareViewAddNonCatalogItemToCart(mapping,form,request,response);


    }
    public ActionForward viewAddContractToCart(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {

        RequestLineItemForm rliForm = (RequestLineItemForm) form;
        rliForm.reset();
        rliForm.setSuggestedVendorCatalogNumber("MNITCONTRACT");
        rliForm.setBudgetBuilderLink(budgetBuilderLink());
        return prepareViewAddNonCatalogItemToCart(mapping,form,request,response);



    }

    public ActionForward viewAddWanToCart(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {

        RequestLineItemForm rliForm = (RequestLineItemForm) form;
        rliForm.reset();
        rliForm.setSuggestedVendorCatalogNumber("WAN/Computing Services");
        rliForm.setBudgetBuilderLink(budgetBuilderLink());
        return prepareViewAddNonCatalogItemToCart(mapping,form,request,response);



    }



    private String budgetBuilderLink(){
        HibernateConfigurationItemDAO hcidao = new HibernateConfigurationItemDAO()
        ConfigurationItem ci = null

        try{
            ci = hcidao.getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.BUDGET_BUILDER_LINK)
        }catch (InfrastructureException ignore){
        }
        return ci?.getValue()?: ""
    }

    /**
     * Action that prepares view of Add Non-catalog Item to Cart
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward
     *         if operation failed
     * @throws Exception
     */
    public ActionForward prepareViewAddNonCatalogItemToCart(ActionMapping mapping,
                                                     ActionForm form,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) throws Exception {
        RequestLineItemForm rliForm = (RequestLineItemForm) form;

        //set the item to null in order to work ok after we add a catalog item to cart
        //we can't do this in addItemToCart because the
        // Synchronized Token doesn't work anymore(we get an error if we click back and submit again addCatalogItemToCart)
        rliForm.setItem(null);
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        //Build the RequestLineItemForm
        rliForm.setSwiftItemId("");
        RequestLineItemFormBuilder rliFormBuilder = new RequestLineItemFormBuilder(rliForm, daoFactory);

        RequestLineItemFormDirector rliFormDirector = new RequestLineItemFormDirector(rliFormBuilder);
        if (request.getSession().getAttribute("skin").toString().equalsIgnoreCase("PRISM2")) {
            rliFormDirector.constructAddRequestLineItemFormForITPurchases(null);
            String categoryID = request.getParameter("categoryId");
            if (categoryID) {
                rliForm.setCategoryId(categoryID);
            }
        }
        else {
            rliFormDirector.constructAddRequestLineItemForm(null);
        }

        // Default the org if it exists
        HttpSession ss = request.getSession(false);
        if (ss.getAttribute("defaultOrg") != null) {
                Collection aa = rliForm.getFundingSourceForms();
                Collection bb = new ArrayList();
                Iterator iterator = aa.iterator();

                while (iterator.hasNext()) {
                    RequestLineItemFundingSourceForm xx = (RequestLineItemFundingSourceForm)iterator.next();
                    if (xx.getOrgBudgetId() == null) {
                        String lOrg = (String)ss.getAttribute("defaultOrg");
                        xx.setOrgBudgetId(lOrg);
                    }
                    bb.add(xx);
                }
                rliForm.setFundingSourceForms(bb);
        }

        saveToken(request);

        return mapping.findForward("success");
    }


    /**
     * Action that prepares view of Add Catalog Item to Cart
     * <p/>
     * TODO: see if you can just add itemId as a property to RequestLineItemForm, and have it set at that level.
     * Then, you wouldn't have to pass in the itemId to the director.  The builder would be able to get it from the
     * rliForm.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward if operation failed
     * @throws Exception
     */
    public ActionForward viewAddCatalogItemToCart(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws Exception {
        RequestLineItemForm rliForm = (RequestLineItemForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);

        //Build the RequestLineItemForm
        RequestLineItemFormBuilder rliFormBuilder = new RequestLineItemFormBuilder(rliForm, daoFactory);
        RequestLineItemFormDirector rliFormDirector = new RequestLineItemFormDirector(rliFormBuilder);
        String idString = request.getParameter("itemId");
        Long itemId = null;
        if(idString) {
            itemId = new Long(idString);
        }

        if(itemId) {
            if (request.getSession().getAttribute("skin").toString().equalsIgnoreCase("PRISM2")) {
                rliFormDirector.constructAddRequestLineItemFormForITPurchases(itemId);
            }
            else {
                rliFormDirector.constructAddRequestLineItemForm(itemId);
            }
                    // Initialize an orgcode to the last one chosen if there is a last one chosen.
            HttpSession ss = request.getSession(false);
            if (ss.getAttribute("defaultOrg") != null) {
                if ((rliForm.getStockItem() == null) ||  //Purchase Item
                    ((!rliForm.getStockItem().getOrgBudget().getOrgBudgetCode().equalsIgnoreCase(OrgBudget.INDIRECT)
                    && rliForm.getItem().getDispenseUnitCost().doubleValue() > 0))) {
                        Collection aa = rliForm.getFundingSourceForms();
                        Collection bb = new ArrayList();
                        Iterator iterator = aa.iterator();

                        while (iterator.hasNext()) {
                            RequestLineItemFundingSourceForm xx = (RequestLineItemFundingSourceForm)iterator.next();
                            if (xx.getOrgBudgetId() == null) {
                                String lOrg = (String)ss.getAttribute("defaultOrg");
                                xx.setOrgBudgetId(lOrg);
                            }
                            bb.add(xx);
                        }

                        rliForm.setFundingSourceForms(bb);
                }
            }
            saveToken(request);
            return mapping.findForward("success");
        }
        else {
            return mapping.findForward("failure");
        }

    }

    /**
     * Action that handles the modification of Shopping Cart
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward if operation failed
     * @throws Exception
     */
    public ActionForward editShoppingCart(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        log.debug("in editShoppingCart()");
        RequestForm requestForm = (RequestForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        Request shoppingCart = (Request) request.getSession().getAttribute("shoppingCart");

        String skin = request?.getSession()?.getAttribute("skin")?.toString() ?: "";

        if(!shoppingCart) {
            shoppingCart = new Request();
        }

        String forward = "success";

        String cmd = requestForm.getCmd() ?: "";

        if(cmd.equalsIgnoreCase(Command.ADD_FUNDING_SOURCE)) {
            ArrayList al = (ArrayList) requestForm.getRequestLineItemForms();
            RequestLineItemForm rliForm = (RequestLineItemForm) al.get(Integer.parseInt(requestForm.getRliFormIndex()));
            RequestLineItemFormBuilder rliFormBuilder = new RequestLineItemFormBuilder(rliForm, daoFactory);
            rliFormBuilder.addNewRequestLineItemFundingSourceForm(true);
            forward = Command.ADD_FUNDING_SOURCE;
        }
        else if(cmd.equalsIgnoreCase(Command.ADD_TO_SHOPPING_LIST)) {
            forward = Command.ADD_TO_SHOPPING_LIST;
        }
        else if(cmd.equalsIgnoreCase(Command.VIEW_CHECKOUT)) {
            forward = Command.VIEW_CHECKOUT;
        }

        saveCart(shoppingCart, requestForm, daoFactory,(User) request.getSession().getAttribute(ApplicationResources.USER), skin);
        request.getSession().setAttribute("shoppingCart", shoppingCart);

        log.debug("in editShoppingCart() - forwarding to: " + forward);
        return mapping.findForward(forward);
    }

    /**
     * Action that prepares view of Shopping Cart
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward if operation failed
     * @throws Exception
     */
    public ActionForward viewShoppingCart(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm)form;
        Request shoppingCart = (Request)request.getSession().getAttribute("shoppingCart");
        User requestor = (User)request.getSession().getAttribute(ApplicationResources.USER);
        if(!shoppingCart) {
            shoppingCart = new Request();
        }
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);

        //Build the RequestForm
//        buildRequestForm(requestForm, daoFactory, shoppingCart, requestor);
        RequestFormBuilder requestFormBuilder = new RequestFormBuilder(requestForm, daoFactory, shoppingCart, requestor);
        RequestFormDirector requestFormDirector = new RequestFormDirector(requestFormBuilder);
        requestFormDirector.rebuildRequestLineItemForms();

        return mapping.findForward("success");
    }
    /**
     * Action that allows viewing of request Line Item
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward if operation failed
     * @throws Exception
     */
    public ActionForward viewRequestLineItemDetail(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm)form;
        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);

        //Build the RequestForm
        long requestId = Long.valueOf(requestForm.getRequestId()).longValue();
        Request materialsRequest = daoFactory.getRequestDAO().getRequestById(requestId, false);  //lock it
        // Simulate a search

        RequestFormBuilder builder = new RequestFormBuilder(requestForm, daoFactory, materialsRequest);
        builder.buildRequestor();
        RequestFormDirector director = new RequestFormDirector(builder);
        director.rebuildRequestLineItemForms();
        if (requestForm.getRequestor() != null && requestForm.getRequestor().getNdsUserId().equals(user.getNdsUserId())) {
            requestForm.setIsOwnerOfRequest(true);
        }
        requestForm.setTrackingNumber(materialsRequest.getTrackingNumber());

        return mapping.findForward("success");
    }

    /**
     * Helper method that builds the requestForm using the shoppingCart
     *
     * @param requestForm
     * @param daoFactory
     * @param shoppingCart
     * @throws InfrastructureException
     */
    private void buildRequestForm(RequestForm requestForm, DAOFactory daoFactory, Request shoppingCart, User user, String skin) throws InfrastructureException {
        RequestFormBuilder requestFormBuilder = new RequestFormBuilder(requestForm, daoFactory, shoppingCart, user);
        RequestFormDirector requestFormDirector = new RequestFormDirector(requestFormBuilder);
        if (!skin || !skin.equalsIgnoreCase("PRISM2")) {
            requestFormDirector.construct();
        }
        else {
            requestFormDirector.constructForITPurchases();
        }
    }

    /**
     * Action that prepares view of Checkout page
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward
     *         if operation failed
     * @throws Exception
     */
    public ActionForward viewCheckout(ActionMapping mapping,
                                      ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        log.debug("in viewCheckout()");
        RequestForm requestForm = (RequestForm) form;
        Request shoppingCart = (Request) request.getSession().getAttribute("shoppingCart");
        User requestor = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (!shoppingCart) {
            shoppingCart = new Request();
        }
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);

        //Build the RequestForm
        RequestFormBuilder requestFormBuilder = new RequestFormBuilder(requestForm, daoFactory, shoppingCart, requestor);
        RequestFormDirector requestFormDirector = new RequestFormDirector(requestFormBuilder);
        if (request.getSession().getAttribute("skin").toString().equalsIgnoreCase("PRISM2")) {
            requestFormDirector.constructForITPurchases();
        }
        else {
            requestFormDirector.construct();
        }

        saveToken(request);
        return mapping.findForward("success");
    }

    /**
     * Action that submits the Request
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward
     *         if operation failed
     * @throws Exception
     */
    public ActionForward checkout(ActionMapping mapping,
                                  ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        User requestor = (User) request.getSession().getAttribute(ApplicationResources.USER);
        String skin = request.getSession().getAttribute("skin") != null ?request.getSession().getAttribute("skin").toString() :"";

        Request shoppingCart = (Request) request.getSession().getAttribute("shoppingCart");
        if (!shoppingCart) {
            shoppingCart = new Request();
        }
        String cmd = requestForm.getCmd();

        if (cmd.equals(Command.CHANGE_DELIVERY_INFO)) {
            return mapping.findForward(Command.CHANGE_DELIVERY_INFO);
        }
        else if (cmd.equalsIgnoreCase(Command.ADD_FUNDING_SOURCE)) {
            ArrayList rliFoms = (ArrayList) requestForm.getRequestLineItemForms();
            RequestLineItemForm rliForm = (RequestLineItemForm) rliFoms.get(Integer.parseInt(requestForm.getRliFormIndex()));
            RequestLineItemFormBuilder rliFormBuilder = new RequestLineItemFormBuilder(rliForm, daoFactory);
            rliFormBuilder.addNewRequestLineItemFundingSourceForm(true);
            return mapping.findForward(Command.ADD_FUNDING_SOURCE);
        }
        else if (cmd.equalsIgnoreCase(Command.SAVE_CART)) {
            saveCart(shoppingCart, requestForm, daoFactory, requestor, skin);
            request.getSession().setAttribute("shoppingCart", shoppingCart);
            buildRequestForm(requestForm, daoFactory, shoppingCart, requestor,skin);
            return mapping.findForward(Command.SAVE_CART);
        }
        else {
            // user clicked Done / Submit
            DeliveryDetail originalDeliveryDetail= requestForm.getDeliveryDetail();
            if(isTokenValid(request)) {
                resetToken(request);

                saveCart(shoppingCart, requestForm, daoFactory, requestor, skin);

                requestForm.setRequestor(requestor);

                RequestBuilder builder = new RequestBuilder(shoppingCart, requestForm, daoFactory, requestor);
                RequestDirector director = new RequestDirector(builder);
                director.construct();

                try {
                    shoppingCart.assignTrackingNumber();
                    shoppingCart.executeBusinessRules();
                    daoFactory.getRequestDAO().makePersistent(shoppingCart);
                    daoFactory.commitTransaction(false);
                }
                catch(Exception e) {
                    log.error("Error in MaterialsRequestAction.checkout()",e);
                    log.error("Error in MaterialsRequestAction.checkout(): " + shoppingCart);
                    return mapping.findForward("error");
                }

                notifiyWithEmails(shoppingCart,skin);

                requestForm.setTrackingNumber(shoppingCart.getTrackingNumber());
                request.setAttribute("shoppingCart", null);
                // Replace the delivery detail back if PARIT
                if (request.getSession().getAttribute("skin").toString().equalsIgnoreCase("PRISM2")) {
                    requestForm.setDeliveryDetail(originalDeliveryDetail);
                }
                return mapping.findForward("success");
            }
            else {
                //token isn't valid...  maybe this isn't the best way to handle it, but reload the checkout page....
                return mapping.findForward(Command.SAVE_CART);
            }
        }
    }

    /**
     * Action that prepares view of the Change Delivery Detail page
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward if operation failed
     * @throws Exception
     */
    public ActionForward viewChangeDeliveryDetails(ActionMapping mapping,
                                                   ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm) form;
        DeliveryDetailForm deliveryDetailForm = new DeliveryDetailForm();
        DeliveryDetail deliveryDetail = requestForm.getDeliveryDetail();
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        User requestor = (User)request.getSession().getAttribute(ApplicationResources.USER);

        //Build the DeliveryDetailForm
        DeliveryDetailFormBuilder builder = new DeliveryDetailFormBuilder(deliveryDetailForm, deliveryDetail, requestor, daoFactory);
        DeliveryDetailFormDirector director = new DeliveryDetailFormDirector(builder);
        if(DeliveryDetailForm.DELIVER_TO_CITIZEN.equals(deliveryDetailForm.getDeliverToType())) {
            //user wants to deliver to private citizen
            director.constructForPrivateCitizenOption();
        }
        else if(DeliveryDetailForm.DELIVER_TO_MDH.equals(deliveryDetailForm.getDeliverToType())){
            //user wants to deliver to an MDH employee
            director.constructForMdhEmployeeOption();
        }
        else {
            //user wants to deliver to an external org
            director.constructForExternalOrgOption();
        }
        requestForm.setDeliveryDetailForm(deliveryDetailForm);
        return mapping.findForward("success");
    }

    /**
     * Action sets the Request.deliveryDetail property
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward
     *         if operation failed
     * @throws Exception
     */
    public ActionForward changeDeliveryDetails(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm) form;
        DeliveryDetailForm deliveryDetailForm = requestForm.getDeliveryDetailForm();
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);

        String cmd = requestForm.getCmd();

        if(cmd.equals(Command.RELOAD)) {
            //Build the DeliveryDetailForm
            DeliveryDetailFormBuilder builder = new DeliveryDetailFormBuilder(deliveryDetailForm, daoFactory);
            DeliveryDetailFormDirector director = new DeliveryDetailFormDirector(builder);

            if(DeliveryDetailForm.DELIVER_TO_CITIZEN.equals(deliveryDetailForm.getDeliverToType())) {
                //user wants to deliver to private citizen
                deliveryDetailForm.setOrganizationId("");
                director.constructForPrivateCitizenOption();
            }
            else if(DeliveryDetailForm.DELIVER_TO_MDH.equals(deliveryDetailForm.getDeliverToType())){
                //user wants to deliver to an MDH employee
                deliveryDetailForm.setOrganizationId("");
                director.constructForMdhEmployeeOption();
                if(request.getSession().getAttribute("requestType").equals("SWIFT")){
                    deliveryDetailForm.setRecipientId(null);
                    deliveryDetailForm.setRecipients(null);
                }
            }
            else {
                //user wants to deliver to an external org
                deliveryDetailForm.setRecipientId("");
                director.constructForExternalOrgOption();
            }
            return mapping.findForward("reload");
        }
        else {
            DeliveryDetail deliveryDetail = new DeliveryDetail();
            DeliveryDetailBuilder builder = new DeliveryDetailBuilder(deliveryDetailForm, deliveryDetail, daoFactory);
            DeliveryDetailDirector director = new DeliveryDetailDirector(builder);
            director.construct();
            requestForm.setDeliveryDetail(deliveryDetail);
            deliveryDetailForm.reset(mapping, request);
            return mapping.findForward("success");
        }
    }

    /**
     * @throws java.lang.Exception
     * @return
     * @param response
     * @param request
     * @param form
     * @param mapping
     */
    public ActionForward reloadExtOrgList(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        log.debug("in reloadExtOrgList()");
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        RequestForm requestForm = (RequestForm)form;
        DeliveryDetailForm ddForm = requestForm.getDeliveryDetailForm();
        ddForm.setOrganizationId("");       //have to call this here, cuz struts doesn't because RequestForm is the real 'form' for this action
        String startChar = request.getParameter("start");
        String endChar = request.getParameter("end");
        if(!StringUtils.nullOrBlank(startChar) && !StringUtils.nullOrBlank(endChar)) {
            ddForm.setOrgNameFirstCharStart(startChar);
            ddForm.setOrgNameFirstCharEnd(endChar);
        }
        DeliveryDetailFormBuilder builder = new DeliveryDetailFormBuilder(ddForm, daoFactory);
        DeliveryDetailFormDirector director = new DeliveryDetailFormDirector(builder);
        director.constructForExternalOrgOption();
        requestForm.setDeliveryDetailForm(ddForm);
        return mapping.findForward("success");
    }

    /**
     * This method sends emails in order to notify the evaluators that a materials action request has been made and each
     * request line item needs evaluation
     *
     * @param shoppingCart
     * @throws InfrastructureException
     */
    public static void sendEmailToEvaluators(Request shoppingCart) throws InfrastructureException {
        Collection emails = new ArrayList();
        Iterator requestIterator = shoppingCart.getRequestLineItems().iterator();
        while (requestIterator.hasNext()) {
            RequestLineItem rli = (RequestLineItem) requestIterator.next();
            if (rli.getStatus().getStatusCode().equals(Status.WAITING_FOR_APPROVAL)) {
                EmailBean emailBean = new EmailBean();
                DAOFactory daoFactory = new HibernateDAOFactory();
                EmailBuilder emailBuilder = new RequestLineItemEmailBuilder(rli, emailBean, daoFactory);
                EmailDirector emailDirector = new EmailDirector(emailBuilder);
                emailDirector.construct();
                emails.add(emailBean);
            }
            //Send notice to members of BOOK_NOTIFICATION group(library) that a book
            //has been requested in PRISM

            if (rli.getItemCategory() != null && rli.getItemCategory().getCategoryCode().equals("BOK")) {
                EmailBean emailBean = new EmailBean();
                DAOFactory daoFactory = new HibernateDAOFactory();
                EmailBuilderBook emailBuilder = new RequestLineItemEmailBuilder(rli, emailBean, daoFactory);
                EmailDirectorBook emailDirector = new EmailDirectorBook(emailBuilder);
                emailDirector.construct("PRISM-BOOK-NOTIFICATION");
                emails.add(emailBean);
            }
        }
        EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
        emailBusinessDelegate.sendEmails(emails);
    }

    /**
     * This method sends emails in order to notify the evaluators that a materials action request has been made and each
     * request line item needs evaluation
     *
     * @param request
     * @throws InfrastructureException
     */
    public static void sendEmailToSecondLevelEvaluators(Request request, String skin) throws InfrastructureException {
        Collection emails = new ArrayList();
        Iterator requestIterator = request.getRequestLineItems().iterator();
        while (requestIterator.hasNext()) {
            RequestLineItem rli = (RequestLineItem) requestIterator.next();
            if (rli.getStatus().getStatusCode().equals(Status.WAITING_FOR_APPROVAL)) {
                //Send notice to members of Conference Coordinators group that a conference/meeting or catering/food request
                //has been entered
                EmailBean emailBean = new EmailBean();
                DAOFactory daoFactory = new HibernateDAOFactory();
                EmailBuilderBook emailBuilder = new RequestLineItemEmailBuilder(rli, emailBean, daoFactory);
                EmailDirectorBook emailDirector = new EmailDirectorBook(emailBuilder);
                emailDirector.construct(Group.CONFERENCE_COORDINATORS);
                emails.add(emailBean);
            }
        }
        EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
        emailBusinessDelegate.sendEmails(emails);
    }

    /**
     * Sends an email for the request.
     * @param shoppingCart is the Request
     * @param skin
     */
    static void notifiyWithEmails(Request shoppingCart, String skin) {
        RequestEmailBuilder builder = new RequestEmailBuilder(personDao, notificationEmailAddressDAO, shoppingCart, skin)
        sendEmails(Collections.singleton(builder.build()))
    }

    static void sendEmails(Collection<EmailBean> emails) {
        new EmailBusinessDelegate().sendEmails(emails)
    }

    static PersonDAO getPersonDao() {
        new HibernateDAOFactory().personDAO
    }

    static NotificationEmailAddressDAO getNotificationEmailAddressDAO() {
        new HibernateDAOFactory().notificationEmailAddressDAO
    }

    /*

        Alter the deliver to location to the caretaker for the computer systems.

     */
    private void alterDeliverTo(Request shoppingCart, RequestForm requestForm, DAOFactory daoFactory, User user, String skin) throws Exception {
        HibernateConfigurationItemDAO hcidao = new HibernateConfigurationItemDAO();
        ConfigurationItem aconfig = hcidao.getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.PARIT_COMPUTER_SHIP_TO);



        if (aconfig) {
            Person person = daoFactory.getPersonDAO().getPersonById(Long.valueOf(aconfig.getValue()), false);
            Facility facility = person.getPrimaryFacility();
            MailingAddress mailingAddress = daoFactory.getFacilityDAO().getFacilityById(facility.getFacilityId(),false).getBuildingMailingAddress();
            /* IF you found a person and facility then change it */
            if (person && facility) {
                /** Take Current delivery to and put in notes */
                requestForm.setAdditionalInstructions((request.getAdditionalInstructions() ?: "").concat(" Original Deliver to: ").concat(" ").concat(requestForm.getDeliveryDetail().getRecipientName()));
                if (requestForm.getDeliveryDetail().getFacility()) {
                    requestForm.setAdditionalInstructions(requestForm.getAdditionalInstructions().concat(" ").concat(requestForm.getDeliveryDetail().getFacility().getFacilityName()));
                }
                if (requestForm.getDeliveryDetail().getMailingAddress())
                    requestForm.setAdditionalInstructions(requestForm.getAdditionalInstructions().concat(" ").concat(requestForm.getDeliveryDetail().getMailingAddress().getCityAndAddress()));


                DeliveryDetail deliveryDetail = new DeliveryDetail();
                deliveryDetail.setFacility(facility);
                deliveryDetail.setRecipient(person);
                deliveryDetail.setMailingAddress(mailingAddress);



                requestForm.setDeliveryDetail(deliveryDetail);
            }
        }
        saveCart(shoppingCart, requestForm, daoFactory, user, skin);

    }

    private void saveCart(Request shoppingCart, RequestForm requestForm, DAOFactory daoFactory, User user, String skin) throws Exception {
        shoppingCart.getRequestLineItems().clear();

        ArrayList rliFormsList = (ArrayList) requestForm.getRequestLineItemForms();
        for (int i = 0; i < rliFormsList.size(); i++) {
            RequestLineItemForm rliForm = (RequestLineItemForm) rliFormsList.get(i);
            if (!rliForm.getRemove().booleanValue()) {
                RequestLineItem requestLineItem = new RequestLineItem();
                RequestLineItemBuilder builder = new RequestLineItemBuilder(requestLineItem, rliForm, daoFactory,user);
                RequestLineItemDirector director = new RequestLineItemDirector(builder);
                director.createNewLineItem(skin);
                shoppingCart.addRequestLineItem(requestLineItem);
            }
        }
        RequestBuilder builder = new RequestBuilder(shoppingCart, requestForm, daoFactory);
        builder.buildSimpleProperties();
    }

    /**
     * Action that prepares view of Checkout confirmation page
     * The call to requestForm.setDeliveryDetail(null) is there because without it, if you submit one request
     * and then another request afterwards with different delivery detail information, it would set the delivery information
     * on both requests (it'd set the first requests delivery info to the second requests.  No good.  We can rework later if needed.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward if operation failed
     * @throws Exception
     */
    public ActionForward viewCheckoutConfirmation(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws Exception {
        request.getSession().setAttribute("shoppingCart", null);    //remove the cart from the session
        
        return mapping.findForward("success");
    }

    /**
     * Action that prepares view of a page that lists materials Requests that require evaluation
     * 1) user logs in & clicks Request Goods & Services link
     * 2) System determines if user is member of MaterialsRequestEvaluator group ???? and, if so,
     * displays the 'Evaluate Requests' link in the header menu.
     * 3) User clicks 'Evaluate Requests' link
     * 4) System displays a summary list of all Requests that meet ALL the following criteria:
     * -Status of any RequestLineItem is WFA
     * -User is a member of an evaluatorGroup for any RequestLineItem in the Request
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward if operation failed
     * @throws Exception
     */
    public ActionForward viewMaterialsRequests(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        RequestFormCollection requestForms = (RequestFormCollection) form;

        User evaluator = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (!evaluator) {
            return mapping.findForward("failure");   //user MUST be logged in to see the page.
        }

        RequestFormCollectionBuilder builder = new RequestFormCollectionBuilder(requestForms, daoFactory, evaluator);
        String orderBy = request.getParameter("orderBy");
        String[] statusCodes = [Status.WAITING_FOR_APPROVAL];
        builder.buildRequestForms(statusCodes, orderBy);

        return mapping.findForward("success");
    }

    /**
     * Action that prepares view of a single materials request, for evaluation purposes...
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward if operation failed
     * @throws Exception
     */
    public ActionForward viewEvaluateMaterialsRequest(ActionMapping mapping,
                                                      ActionForm form,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        RequestForm requestForm = (RequestForm) form;

        User evaluator = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (!evaluator) {
            return mapping.findForward("failure");   //user MUST be logged in to see the page.
        }

        Long requestId = new Long(request.getParameter("requestId"));
        Request materialsRequest = daoFactory.getRequestDAO().getRequestById(requestId, false);  //lock it

        RequestFormBuilder builder = new RequestFormBuilder(requestForm, daoFactory, materialsRequest, evaluator);
        RequestFormDirector director = new RequestFormDirector(builder);
        director.constructForEvaluation();

        saveToken(request);

        if (requestForm.getEvaluatorsEvaluationStatus() == null) return new ForwardParameters().add("requestId",request.getParameter("requestId")).forward(mapping.findForward("nothingToApprove"));
        if (requestForm.getEvaluatorsEvaluationStatus().contains(Constants.TRIGGER_TEXT2_TO_DETERMINE_WHETHER_TO_DISPLAY_APPROVAL_STATUS_PAGE))  return mapping.findForward("showStatus");
        if (requestForm.getRequestLineItemForms().size() < 1) {
            return new ForwardParameters().add("requestId",request.getParameter("requestId")).forward(mapping.findForward("nothingToApprove"));
        }

        if (!requestForm.getEvaluatorsEvaluationStatus().contains(Constants.TRIGGER_TEXT1_TO_DETERMINE_WHETHER_TO_DISPLAY_APPROVAL_STATUS_PAGE))  return mapping.findForward("showStatus");

        return mapping.findForward("success");
    }

    /**
     * Action that submits the evaluation
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward if operation failed
     * @throws Exception
     */
    public ActionForward evaluateMaterialsRequest(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        RequestForm requestForm = (RequestForm) form;

        String skin = request.getSession().getAttribute("skin") != null ? request.getSession().getAttribute("skin").toString() : "";

        User evaluator = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (!evaluator) {
            return mapping.findForward("failure");
        }

        if (requestForm.getCmd().equalsIgnoreCase(Command.ADD_FUNDING_SOURCE)) {
            if (isTokenValid(request)) {
                ArrayList rliFormsList = (ArrayList) requestForm.getRequestLineItemForms();
                RequestLineItemForm rliForm = (RequestLineItemForm) rliFormsList.get(Integer.parseInt(requestForm.getRliFormIndex()));
                RequestLineItemFormBuilder rliFormBuilder = new RequestLineItemFormBuilder(rliForm, daoFactory);
                rliFormBuilder.addNewRequestLineItemFundingSourceForm(true, evaluator);
            }
            return mapping.findForward("reload");
        }
        else if (requestForm.getCmd().equalsIgnoreCase(Command.SHOW_DETAIL)) {
            ArrayList rliFormsList = (ArrayList) requestForm.getRequestLineItemForms();
            RequestLineItemForm rliForm = (RequestLineItemForm) rliFormsList.get(Integer.parseInt(requestForm.getRliFormIndex()));
            rliForm.setShowDetail(true);
            return mapping.findForward("reload");
        }
        else if (requestForm.getCmd().equalsIgnoreCase(Command.HIDE_DETAIL)) {
            ArrayList rliFormsList = (ArrayList) requestForm.getRequestLineItemForms();
            RequestLineItemForm rliForm = (RequestLineItemForm) rliFormsList.get(Integer.parseInt(requestForm.getRliFormIndex()));
            rliForm.setShowDetail(false);
            return mapping.findForward("reload");
        }
        else if (requestForm.getCmd().equalsIgnoreCase(Command.ADD_NOTE)) {
            if (isTokenValid(request)) {
                ArrayList rliFormsList = (ArrayList) requestForm.getRequestLineItemForms();
                RequestLineItemForm rliForm = (RequestLineItemForm) rliFormsList.get(Integer.parseInt(requestForm.getRliFormIndex()));
                RequestLineItemFormBuilder rliFormBuilder = new RequestLineItemFormBuilder(rliForm, daoFactory);
                String noteText = request.getParameterValues("rliNote")[Integer.parseInt(requestForm.getRliFormIndex())];
                rliFormBuilder.addNewRequestLineItemNoteForm(noteText, evaluator);
            }
            return mapping.findForward("reload");
        }
        else {  //submit was clicked
            if (isTokenValid(request)) {
                resetToken(request);
                Iterator iter = requestForm.getRequestLineItemForms().iterator();
                while (iter.hasNext()) {
                    RequestLineItemForm rliForm = (RequestLineItemForm) iter.next();
                    Long requestLineItemId = new Long(rliForm.getRequestLineItemId());
                    RequestLineItem rli = daoFactory.getRequestLineItemDAO().getRequestLineItemById(requestLineItemId, true);
                    RequestLineItemBuilder rliBuilder = new RequestLineItemBuilder(rli, rliForm,  daoFactory, evaluator);
                    RequestLineItemDirector director = new RequestLineItemDirector(rliBuilder);
                    // PRIS-180, for multiple items just create one ticket.
                    director.constructEvaluatedLineItem(rliForm.getApproved(),skin,rliForm.getEndRequestStatus());
                    sendEmailToRequestor(rli, daoFactory);
                    if (!rli.levelOneRequestEvaluationsStillNeedingApproval() && rliForm.isLevelTwoRequestEvaluationApprovalsPending()) {
                        sendEmailToSecondLevelEvaluators(rli.getRequest(), "reset");
                    }
                }
            }
            return mapping.findForward("success");
        }
    }

    /**
     * Action that submits the evaluation
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward if operation failed
     * @throws Exception
     */
    public ActionForward attachQuote(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        RequestForm requestForm = (RequestForm) form;

        String skin = request.getSession().getAttribute("skin") != null ? request.getSession().getAttribute("skin").toString() : "";

        User evaluator = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (!evaluator) {
            return mapping.findForward("failure");
        }

        if (requestForm.getCmd().equalsIgnoreCase(Command.ADD_QUOTE)) {
            if (isTokenValid(request)) {
                ArrayList rliFormsList = (ArrayList) requestForm.getRequestLineItemForms();
                RequestLineItemForm rliForm = (RequestLineItemForm) rliFormsList.get(Integer.parseInt(requestForm.getRliFormIndex()));

            }
            return mapping.findForward("success");
        }
        else return mapping.findForward("reload");
    }

        public static void sendEmailToRequestor(RequestLineItem rli, DAOFactory daoFactory) throws InfrastructureException {
        EmailBean emailBean = new EmailBean();
        EvaluateRequestLineItemEmailBuilder builder = new EvaluateRequestLineItemEmailBuilder(rli, emailBean, daoFactory);
        EmailDirector director = new EmailDirector(builder);
        director.construct();
        EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
        emailBusinessDelegate.sendEmail(emailBean);
    }

    /**
     * Action that performs a browse in the catalog (navigates in the categories tree)
     *
     * @param mapping
     * @param form     BrowseCatalogForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward browseCatalog(ActionMapping mapping,
                                       ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        BrowseCatalogForm browseCatalogForm = (BrowseCatalogForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        try {
            if("reset".equals(request.getParameter("cmd"))) {
                browseCatalogForm.setSelectedCategory("");
            }
            browseCatalogForm.resetStartIndex();
            browseCatalogForm.setSearchType(BrowseCatalogForm.CATEGORY_SEARCH_TYPE);
            BrowseCatalogFormBuilder builder = new BrowseCatalogFormBuilder(browseCatalogForm, daoFactory);
            BrowseCatalogFormDirector director = new BrowseCatalogFormDirector(builder);
            if(StringUtils.nullOrBlank(browseCatalogForm.getPaginationDirection())) {
                director.construct();
            }
            else {
                director.refreshItemsList();
            }
        }
        catch(Exception e) {
            log.error("error in MaterialsRequestAction.browseCatalog: ",e);
        }

        return mapping.findForward("success");
    }


    /**
     * Action that performs the Lucene query in the catalog
     *
     * @param mapping
     * @param form     SearchCatalogForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward searchCatalog(ActionMapping mapping,
                                       ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        BrowseCatalogForm browseCatalogForm = (BrowseCatalogForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.LUCENE);

        try {
            browseCatalogForm.resetStartIndex();
            browseCatalogForm.setSearchType(BrowseCatalogForm.KEYWORD_SEARCH_TYPE);
            BrowseCatalogFormBuilder builder = new BrowseCatalogFormBuilder(browseCatalogForm, daoFactory);
            BrowseCatalogFormDirector director = new BrowseCatalogFormDirector(builder);
            director.refreshItemsList();

            Collection ids = new ArrayList();
            for (Iterator iterator = browseCatalogForm.getCurrentItems().iterator(); iterator.hasNext();) {
                Item item = (Item) iterator.next();
                ids.add(item.getItemId());
            }

            daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
            builder = new BrowseCatalogFormBuilder(browseCatalogForm, daoFactory);
            director = new BrowseCatalogFormDirector(builder);
            director.refreshItemListUsingItemIds(ids);
        }
        catch(Exception e) {
            log.error("error in MaterialsRequestAction.searchCatalog: ", e);
        }

        return mapping.findForward("success");
    }

    /**
     * Action that prepares the page for advanced search
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewAdvancedSearchCatalog(ActionMapping mapping,
                                                   ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws Exception {
        ItemsAdvancedSearchForm iasForm = (ItemsAdvancedSearchForm) form;
        iasForm.setCategoryCode("");
        DAOFactory factory = new HibernateDAOFactory();
        ItemsAdvancedSearchFormBuilder iasFormBuilder = new ItemsAdvancedSearchFormBuilder(iasForm, factory);
        ItemsAdvancedSearchFormDirector director = new ItemsAdvancedSearchFormDirector(iasFormBuilder);
        director.constructAdvancedSearchCatalogForm();
        return mapping.findForward("success");
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward advancedSearchCatalog(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        ItemsAdvancedSearchForm iasForm = (ItemsAdvancedSearchForm) form;
        DAOFactory factory = new HibernateDAOFactory();

        ItemsAdvancedSearchFormBuilder builder = new ItemsAdvancedSearchFormBuilder(iasForm, factory);
        ItemsAdvancedSearchFormDirector director = new ItemsAdvancedSearchFormDirector(builder);
        try {
            director.searchInCatalogAndConstruct();
        }
        catch (InfrastructureException e) {
            log.error("Error in MaterialsRequestAction.advancedSearchCatalog()",e);
        }
        catch (Exception e) {
            log.error("Error in MaterialsRequestAction.advancedSearchCatalog()",e);
        }

        return mapping.findForward("success");
    }

    /**
     * Action that is used for displaying the details of an item when we browse the catalog
     * or when we search items in the catalog
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward showItemDetails(ActionMapping mapping,
                                         ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {
        ItemDetailsForm itemDetailsForm = (ItemDetailsForm) form;
        DAOFactory daoFactory = new HibernateDAOFactory();
        String itemId = request.getParameter("itemId");
        Long id = itemId ? new Long(itemId) : null;
        Item item = daoFactory.getItemDAO().getItemById(id, true);
        
        itemDetailsForm.setItem(item);
        
        return mapping.findForward("success");
    }

    public ActionForward showRequestLineItemDetails(ActionMapping mapping,
                                                    ActionForm form,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws Exception {
        RequestLineItemPurchaseDetailsForm reqLineItemPurchaseDetailsForm = (RequestLineItemPurchaseDetailsForm) form;
        String requestLineItemId = reqLineItemPurchaseDetailsForm.getRequestLineItemId();
        DAOFactory daoFactory = new HibernateDAOFactory();
        Long rliId = Long.decode(requestLineItemId);
        RequestLineItem requestLineItem = daoFactory.getRequestLineItemDAO().getRequestLineItemById(rliId,false);
        OrderLineItem oli = requestLineItem.getOrderLineItem();
        Order order;
        
        if(!oli){
            order = daoFactory.getPurchasingOrderDAO().findOrderByRequestLineItemId(rliId);
            reqLineItemPurchaseDetailsForm.setOrder(order);
        }
        else {
            reqLineItemPurchaseDetailsForm.setOrder(oli.getOrder());
        }
        reqLineItemPurchaseDetailsForm.setRequestLineItem(requestLineItem);
        return mapping.findForward("success");
    }

    public ActionForward displayStockItemOutTransactions(ActionMapping mapping,
                                                         ActionForm form,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) throws Exception {
        StockItemTransactionsForm transactionsForm = (StockItemTransactionsForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        OrgBdgtTransactionsFormBuilder builder = new OrgBdgtTransactionsFormBuilder(transactionsForm, daoFactory);
        builder.buildStockItemsOutWithFS();
        return mapping.findForward("success");
    }

    public ActionForward displayStockItemOutTransactionsWithoutFS(ActionMapping mapping,
                                                         ActionForm form,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) throws Exception {
        StockItemTransactionsForm transactionsForm = (StockItemTransactionsForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        OrgBdgtTransactionsFormBuilder builder = new OrgBdgtTransactionsFormBuilder(transactionsForm, daoFactory);
        builder.buildStockItemsOutWithoutFS();
        return mapping.findForward("success");
    }

    public ActionForward displayStockItemInTransactions(ActionMapping mapping,
                                                        ActionForm form,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) throws Exception {
        StockItemTransactionsForm transactionsForm = (StockItemTransactionsForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        OrgBdgtTransactionsFormBuilder builder = new OrgBdgtTransactionsFormBuilder(transactionsForm, daoFactory);
        builder.buildStockItemsIn();

        return mapping.findForward("success");
    }

    public ActionForward displayPurchaseItemTransactions(ActionMapping mapping,
                                                         ActionForm form,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) throws Exception {
        PurchaseItemTransactionsForm  purchaseItemTransactionsForm = (PurchaseItemTransactionsForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        PurchaseItemTransactionsFormBuilder builder = new PurchaseItemTransactionsFormBuilder(purchaseItemTransactionsForm, daoFactory);
        builder.buildOrderedPurchaseItemTransactions();
        return mapping.findForward("success");
    }
    public ActionForward populateRequestLineItemFromRequestForm(ActionMapping mapping,
                                                         ActionForm form,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) throws Exception {

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        RequestForm requestForm = (RequestForm) form;

        User evaluator = (User) request.getSession().getAttribute(ApplicationResources.USER);

        Long requestId = new Long(request.getParameter("requestId"));
        Request materialsRequest = daoFactory.getRequestDAO().getRequestById(requestId, false);  //lock it

        RequestFormBuilder builder = new RequestFormBuilder(requestForm, daoFactory, materialsRequest, evaluator);
        RequestFormDirector director = new RequestFormDirector(builder);
        director.constructForEvaluation();
        director.rebuildRequestLineItemForms();
        saveToken(request);


        return mapping.findForward("success");
    }

}