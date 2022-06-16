package us.mn.state.health.matmgmt.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.builder.email.EmailBuilder;
import us.mn.state.health.builder.email.RequestLineItemEmailBuilder;
import us.mn.state.health.builder.materialsrequest.*;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.director.RequestDirector;
import us.mn.state.health.matmgmt.director.RequestFormDirector;
import us.mn.state.health.matmgmt.director.RequestLineItemFormDirector;
import us.mn.state.health.matmgmt.director.RequestLineItemSearchFormDirector;
import us.mn.state.health.matmgmt.director.email.EmailDirector;
import us.mn.state.health.matmgmt.util.Form;
import us.mn.state.health.matmgmt.util.Forward;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource;
import us.mn.state.health.model.util.HibernateModelDetacher;
import us.mn.state.health.model.util.ModelDetacher;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.EmailBusinessDelegate;
import us.mn.state.health.security.ApplicationResources;
import us.mn.state.health.view.materialsrequest.*;
import us.mn.state.health.view.purchasing.OrderForm;
import us.mn.state.health.view.purchasing.OrderLineItemForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * This class contians all the Struts actions for allowing buyers to create a Request wihtout
 * going through the usual 'browse catalog' use case.  This feature was intended as a temporary solution
 * to help us transition from the old system to PRISM.  At first, only the buyers were going to
 * have access to the new system, so they'd have to take the requests from ASAP and re-enter them into PRISM.
 * However, the buyers since decided that it'd be too much work for them, so these actions are probably not
 * so useful anymore and should be considered deprecated. - Shawn Flahave
 *
 * @author Jason Stull
 */
public class PurchasingMaterialsRequestAction extends MappingDispatchAction {

    /**
     * Action that adds note form to Request Line Item
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward addPurchasingRequestLineItemNote(ActionMapping mapping,
                                                          ActionForm actionForm,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) throws Exception {
        User author = (User) request.getSession().getAttribute(ApplicationResources.USER);
        RequestForm requestForm = (RequestForm) actionForm;
        RequestLineItemForm reqLnItmForm = requestForm.getRequestLineItemForm();
        RequestLineItemNoteForm noteForm = new RequestLineItemNoteForm();
        RequestLineItemNoteFormBuilder builder = new RequestLineItemNoteFormBuilder(noteForm, author);
        builder.buildAuthorName();
        builder.buildDate();
        List noteForms = (List) reqLnItmForm.getNoteForms();
        noteForms.add(0, noteForm);

        return mapping.findForward("success");
    }


    /**
     * Action that prepares view of Request search page
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward cancelEditPurchasingRequest(ActionMapping mapping,
                                                     ActionForm actionForm,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) throws Exception {
        RequestForm reqForm = (RequestForm) actionForm;
        return mapping.findForward(reqForm.getInput());//go back to where we started
    }

    /**
     * Action that removes a Purchasing Request Line Item
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward removePurchasingRequestLineItem(ActionMapping mapping,
                                                         ActionForm actionForm,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm) actionForm;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        RequestLineItemForm reqLnItmForm =
                (RequestLineItemForm) CollectionUtils.removeMatchingItem(requestForm.getRequestLineItemForms(),
                        requestForm.getRequestLineItemKey(),
                        "key");
        if (!StringUtils.nullOrBlank(reqLnItmForm.getRequestLineItemId())) { //Remove persistent Req Ln Item
            Long reqLnItmId = new Long(reqLnItmForm.getRequestLineItemId());
            RequestLineItem reqLnItm = daoFactory.getRequestLineItemDAO().getRequestLineItemById(reqLnItmId, true);
            daoFactory.getRequestLineItemDAO().makeTransient(reqLnItm);
        }
        //Clean up view
        if (requestForm.getRequestLineItemForm() != null) {
            if (requestForm.getRequestLineItemForm().getKey().equals(reqLnItmForm.getKey())) {
                requestForm.setRequestLineItemForm(null);
            }
        }
        return mapping.findForward("success");
    }

    /**
     * Action that removes a Request Line Item Funding Source
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward removePurchasingRequestLineItemFundingSource(ActionMapping mapping,
                                                                      ActionForm actionForm,
                                                                      HttpServletRequest request,
                                                                      HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm) actionForm;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        RequestLineItemForm reqLnItmForm = requestForm.getRequestLineItemForm();

        if (reqLnItmForm.getFundingSourceForms().size() > 1) {
            //THERE MUST BE AT LEAST 1 FUNDING SOURCE, SO ONLY DO THE FOLLOWING
            //IF THERE IS MORE THAN ONE FUNDING SOURCE...
            RequestLineItemFundingSourceForm fundingSrcForm =
                    (RequestLineItemFundingSourceForm) CollectionUtils.removeMatchingItem(reqLnItmForm.getFundingSourceForms(), reqLnItmForm.getFundingSourceKey(), "key");
            if (fundingSrcForm.getRequestLineItemFundingSourceId() != null) { //Remove persistent Funding Source
                RequestLineItemFundingSource fundingSrc =
                        daoFactory.getRequestLineItemFundingSourceDAO().getRequestLineItemFundingSourceById(fundingSrcForm.getRequestLineItemFundingSourceId(), true);
                RequestLineItem rli = fundingSrc.getRequestLineItem();
                rli.removeFundingSource(fundingSrc);
                daoFactory.getRequestLineItemDAO().makePersistent(rli);
            }
            //Cleanup view
            if (reqLnItmForm.getFundingSourceForm() != null) {
                if (reqLnItmForm.getFundingSourceForm().getKey().equals(fundingSrcForm.getKey())) {
                    reqLnItmForm.setFundingSourceForm(null);
                }
            }
        }

        return mapping.findForward("success");
    }

    /**
     * Action that removes note form from Request Line Item Form
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward removePurchasingRequestLineItemNote(ActionMapping mapping,
                                                             ActionForm actionForm,
                                                             HttpServletRequest request,
                                                             HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm) actionForm;
        RequestLineItemForm reqLnItmForm = requestForm.getRequestLineItemForm();
        String key = request.getParameter("key");
        Collection rliNoteForms = reqLnItmForm.getNoteForms();
        RequestLineItemNoteForm noteForm =
                (RequestLineItemNoteForm) CollectionUtils.getObjectFromCollectionById(rliNoteForms, key, "key");
        noteForm.setRemoved(Boolean.TRUE);
        return mapping.findForward("success");
    }

    /**
     * Action that searches for Purchasing Request Line Items
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward searchPurchasingRequestLineItems(ActionMapping mapping,
                                                          ActionForm actionForm,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) throws Exception {
        RequestLineItemSearchForm rliSearchForm = (RequestLineItemSearchForm) actionForm;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        RequestLineItemSearchFormBuilder builder = new RequestLineItemSearchFormBuilder(rliSearchForm, daoFactory);
        RequestLineItemSearchFormDirector director = new RequestLineItemSearchFormDirector(builder);
        director.constructPurchasingAdvancedSearch();

        return mapping.findForward("success");
    }

    /**
     * Saves Purchasing Request
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward savePurchasingRequest(ActionMapping mapping,
                                               ActionForm actionForm,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm) actionForm;

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        Request req = null;
        User purchaser = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (!StringUtils.nullOrBlank(requestForm.getRequestId())) { //Existing Request
            Long reqId = new Long(requestForm.getRequestId());
            req = daoFactory.getRequestDAO().getRequestById(reqId, true);
            getDetacher().detachRequest(req);
        } else { //New Request
            req = new Request();
        }
        HashMap<Long,String> rqListSave = new HashMap<Long, String>();

        Collection c = req.getRequestLineItems();
        for (Object o : c) {
            RequestLineItem rq = (RequestLineItem)o;
            rqListSave.put(rq.getRequestLineItemId(),rq.getStatus().getStatusCode());
        }


        //Handle RLI form in view that hasn't been added to list yet
        RequestLineItemForm reqLnItmForm = requestForm.getRequestLineItemForm();
        if (reqLnItmForm != null) {
            boolean inList = CollectionUtils.inList(requestForm.getRequestLineItemForms(), "key", reqLnItmForm.getKey());
            if (!inList) {
                requestForm.getRequestLineItemForms().add(reqLnItmForm);
            }

            RequestLineItemFundingSourceForm fndngSrcForm = reqLnItmForm.getFundingSourceForm();
            if (fndngSrcForm != null) {
                inList = CollectionUtils.inList(reqLnItmForm.getFundingSourceForms(), "key", fndngSrcForm.getKey());
                if (!inList) {
                    reqLnItmForm.getFundingSourceForms().add(fndngSrcForm);
                }
                if (requestForm.getRequestLineItemForm().getFundingSourceFormsSize() < 2) {
                    fndngSrcForm.setAmount(requestForm.getRequestLineItemForm().getLineTotal());
                }
            }
        }

        //Build Request
        RequestBuilder builder = new RequestBuilder(req, requestForm, daoFactory, purchaser);
        RequestDirector director = new RequestDirector(builder);
        if (req.getRequestId() == null) {
            director.constructNewForPurchasing();
        } else {
            director.constructEditForPurchasing();
        }
        daoFactory.getRequestDAO().makePersistent(req);
//        daoFactory.commitTransaction(true);

        //update forward-to view
        Collection reqLnItmForms = null;
        if (requestForm.getInput().equals(Forward.ORDR)) {
            OrderForm orderForm = (OrderForm) request.getSession().getAttribute(Form.ORDER);
            reqLnItmForms = orderForm.getRequestLineItemForms();
        } else if (requestForm.getInput().equals(Forward.ORDR_LN_ITM)) {
            OrderLineItemForm orderLnItmForm =
                    (OrderLineItemForm) request.getSession().getAttribute(Form.ORDER_LINE_ITEM);
            reqLnItmForms = orderLnItmForm.getRequestLineItemForms();
        } else {
            if (requestForm.getInput().equals(Forward.PRCHSNG_RQ_LN_ITMS)) {
                RequestLineItemSearchForm rliSearchForm =
                        (RequestLineItemSearchForm) request.getSession().getAttribute(Form.REQ_LN_ITM_SRCH);
                reqLnItmForms = rliSearchForm.getRliForms();
            }
        }
        if (reqLnItmForms != null) {
            reqLnItmForm = (RequestLineItemForm) requestForm.getRequestLineItemForms().iterator().next();
            Long reqLnItmId = new Long(reqLnItmForm.getRequestLineItemId());
            RequestLineItem reqLnItm =
                    (RequestLineItem) CollectionUtils.getObjectFromCollectionById(req.getRequestLineItems(), reqLnItmId, "requestLineItemId");
            reqLnItmForm = (RequestLineItemForm) CollectionUtils.getObjectFromCollectionById(reqLnItmForms, reqLnItmId, "requestLineItem.requestLineItemId");
            reqLnItmForm.setRequestLineItem(reqLnItm); // if Non CPO, keep this

            //PRIS-159    when order status changed to CPO from Non cpo, send email to Purchasing
            if (reqLnItm.getStatus().getStatusCode().equals(Status.COMPUTER_PURCHASE_ORDER)) {
                String oldValue = rqListSave.get(reqLnItm.getRequestLineItemId());

                if (!oldValue.equals(Status.COMPUTER_PURCHASE_ORDER)) {
                    //Search for the given Request Line Item and see if it really has changed or just a save
                    reqLnItm.getItemCategory().setCategoryCode("COMP");
                    reqLnItm.setItPurchase(true);

                    req.executeBusinessRules(true);
                    req.setTrackingNumber(requestForm.getTrackingNumber());
                    req.save();

                    reqLnItmForm.setRequestLineItem(reqLnItm);  // if CPO status, make sure reqLnItmForm has the last values.
                    reqLnItmForm.setItPurchase(true);
                    reqLnItmForm.setCategoryId(reqLnItm.getItemCategory().getCategoryId().toString());

                    Iterator requestIterator = req.getRequestLineItems().iterator();
                    Collection emails = new ArrayList();
                    while (requestIterator.hasNext()) {
                        RequestLineItem rli = (RequestLineItem) requestIterator.next();
                        if (rli.getStatus().getStatusCode().equals(Status.WAITING_FOR_APPROVAL)) {
                            EmailBean emailBean = new EmailBean();
                            EmailBuilder emailBuilder = new RequestLineItemEmailBuilder(rli, emailBean, daoFactory);
                            EmailDirector emailDirector = new EmailDirector(emailBuilder);
                            emailDirector.construct();
                            emails.add(emailBean);
                        }
                    }
                    EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
                    emailBusinessDelegate.sendEmails(emails);
                }
            }

        }

        return mapping.findForward(requestForm.getInput());
    }

    public ActionForward savePurchasingRequestLineItem(ActionMapping mapping,
                                                       ActionForm actionForm,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm) actionForm;
        RequestLineItemForm reqLnItmForm = requestForm.getRequestLineItemForm();
        boolean inList = CollectionUtils.inList(requestForm.getRequestLineItemForms(), "key", reqLnItmForm.getKey());

        if (!inList) {
            requestForm.getRequestLineItemForms().add(reqLnItmForm);
        }
        RequestLineItemFundingSourceForm fndngSrcForm = reqLnItmForm.getFundingSourceForm();
        if (fndngSrcForm != null) {
            inList = CollectionUtils.inList(reqLnItmForm.getFundingSourceForms(), "key", fndngSrcForm.getKey());
            if (!inList) {
                reqLnItmForm.getFundingSourceForms().add(fndngSrcForm);
            }
            if (requestForm.getRequestLineItemForm().getFundingSourceFormsSize() < 2) {
                fndngSrcForm.setAmount(requestForm.getRequestLineItemForm().getLineTotal());
            }
        }
//        requestForm.setRequestLineItemForm(null);
        return mapping.findForward("success");
    }

    public ActionForward savePurchasingRequestLineItemFundingSource(ActionMapping mapping,
                                                                    ActionForm actionForm,
                                                                    HttpServletRequest request,
                                                                    HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm) actionForm;
        RequestLineItemFundingSourceForm fundingSrcForm = requestForm.getRequestLineItemForm().getFundingSourceForm();
        boolean inList = CollectionUtils.inList(requestForm.getRequestLineItemForm().getFundingSourceForms(), "key", fundingSrcForm.getKey());
        if (!inList) {
            requestForm.getRequestLineItemForm().getFundingSourceForms().add(fundingSrcForm);
        }
        if (requestForm.getRequestLineItemForm().getFundingSourceFormsSize() < 2) {
            fundingSrcForm.setAmount(requestForm.getRequestLineItemForm().getLineTotal());
        }
        requestForm.getRequestLineItemForm().setFundingSourceForm(null);
        return mapping.findForward("success");
    }

    public ActionForward selectDeliverToPerson(ActionMapping mapping,
                                               ActionForm actionForm,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm) actionForm;
        /*
        if(requestForm.getAddBkPerson() != null) {
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
            Long personId = requestForm.getAddBkPerson().getPerson().getPersonId();
            Person deliverToPerson = daoFactory.getPersonDAO().getPersonById(personId, false);
            if(deliverToPerson.getPrimaryFacility() != null) {
                requestForm.setFacilityId(deliverToPerson.getPrimaryFacility().getFacilityId().toString());
            }
            else {
                requestForm.setFacilityId(null);
            }
        }*/

        return mapping.findForward("success");
    }

    public ActionForward viewCreatePurchasingRequest(ActionMapping mapping,
                                                     ActionForm actionForm,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm) actionForm;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        RequestFormBuilder builder = new RequestFormBuilder(requestForm, daoFactory);
        RequestFormDirector director = new RequestFormDirector(builder);
        director.constructNewForPurchasing();

        RequestLineItemForm reqLnItmForm = new RequestLineItemForm();
        RequestLineItemFormBuilder reqLnItmFormBuilder = new RequestLineItemFormBuilder(reqLnItmForm, daoFactory);
        RequestLineItemFormDirector reqLnItmFormDirector = new RequestLineItemFormDirector(reqLnItmFormBuilder);
        reqLnItmFormDirector.constructNewPurchasingRequestLineItemForm(null);
        requestForm.setRequestLineItemForm(reqLnItmForm);
        RequestLineItemFundingSourceForm fndngSrcForm = new RequestLineItemFundingSourceForm();
        RequestLineItemFundingSourceFormBuilder fndngSrcFormBuilder = new RequestLineItemFundingSourceFormBuilder(fndngSrcForm, daoFactory);
        fndngSrcFormBuilder.buildOrgBudgets();
        reqLnItmForm.setFundingSourceForm(fndngSrcForm);

        putRequestFormInScope(requestForm, request.getSession());

        return mapping.findForward("success");
    }

    /**
     * Prepares view for creating new Purchasing Request Line Item
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewCreatePurchasingRequestLineItem(ActionMapping mapping,
                                                             ActionForm actionForm,
                                                             HttpServletRequest request,
                                                             HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm) actionForm;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        RequestLineItemForm reqLnItmForm = new RequestLineItemForm();
        RequestLineItemFormBuilder builder = new RequestLineItemFormBuilder(reqLnItmForm, daoFactory);
        RequestLineItemFormDirector director = new RequestLineItemFormDirector(builder);
        String itemIdStr = request.getParameter("itemId");
        Long itemId = null;
        if (!StringUtils.nullOrBlank(itemIdStr)) {
            itemId = new Long(itemIdStr);
        }
        director.constructNewPurchasingRequestLineItemForm(itemId);
        requestForm.setRequestLineItemForm(reqLnItmForm);
        RequestLineItemFundingSourceForm fndngSrcForm = new RequestLineItemFundingSourceForm();
        RequestLineItemFundingSourceFormBuilder fndingSrcBuilder = new RequestLineItemFundingSourceFormBuilder(fndngSrcForm, daoFactory);
        fndingSrcBuilder.buildOrgBudgets();
        reqLnItmForm.setFundingSourceForm(fndngSrcForm);

        return mapping.findForward("success");
    }

    /**
     * Action that prepares view for creating Purchasing Request Line Item Funding Source
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewCreatePurchasingRequestLineItemFundingSource(ActionMapping mapping,
                                                                          ActionForm actionForm,
                                                                          HttpServletRequest request,
                                                                          HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm) actionForm;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        RequestLineItemForm reqLnItmForm = requestForm.getRequestLineItemForm();
        RequestLineItemFundingSourceForm fundingSrcForm = new RequestLineItemFundingSourceForm();
        RequestLineItemFundingSourceFormBuilder builder = new RequestLineItemFundingSourceFormBuilder(fundingSrcForm, daoFactory);
        builder.buildOrgBudgets();
        reqLnItmForm.setFundingSourceForm(fundingSrcForm);

        return mapping.findForward("success");
    }


    /**
     * Action that prepares edit of Request Line Item view
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewEditPurchasingRequest(ActionMapping mapping,
                                                   ActionForm actionForm,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws Exception {
        Long reqLnItmId = new Long(request.getParameter("requestLineItemId"));
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);

        //Look up Req Ln Itm
        RequestLineItem reqLnItm = daoFactory.getRequestLineItemDAO().getRequestLineItemById(reqLnItmId);
        getDetacher().detachRequest(reqLnItm.getRequest());

        //Build Request and Req Ln Itm Form
        Request req = reqLnItm.getRequest();
        RequestForm requestForm = new RequestForm();
        requestForm.setInput(request.getParameter("input")); //where to return to after editing request
        RequestFormBuilder requestBuilder = new RequestFormBuilder(requestForm, daoFactory, req, RequestFormBuilder.FOR_PURCHASING);
        RequestFormDirector requestDirector = new RequestFormDirector(requestBuilder);
        requestDirector.constructEditForPurchasing();

        RequestLineItemForm reqLnItmForm = new RequestLineItemForm();

        RequestLineItemFormBuilder reqLnItmFormBuilder = new RequestLineItemFormBuilder(reqLnItmForm, reqLnItm, daoFactory);
        RequestLineItemFormDirector reqLnItmFormDirector = new RequestLineItemFormDirector(reqLnItmFormBuilder);
        reqLnItmFormDirector.constructEditPurchasingRequestLineItemForm();

        requestForm.getRequestLineItemForms().add(reqLnItmForm);
        requestForm.setRequestLineItemForm(reqLnItmForm);

        putRequestFormInScope(requestForm, request.getSession());

        return mapping.findForward("success");
    }

    /**
     * Action that prepares view for editing Purchasing Request Line Item
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewEditPurchasingRequestLineItem(ActionMapping mapping,
                                                           ActionForm actionForm,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm) actionForm;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        RequestLineItemForm reqLnItmForm =
                (RequestLineItemForm) CollectionUtils.getObjectFromCollectionById(requestForm.getRequestLineItemForms(),
                        requestForm.getRequestLineItemKey(),
                        "key");
        RequestLineItem reqLnItm = null;
        if (!StringUtils.nullOrBlank(reqLnItmForm.getRequestLineItemId())) {
            Long reqLnItmId = new Long(reqLnItmForm.getRequestLineItemId());
            reqLnItm = daoFactory.getRequestLineItemDAO().getRequestLineItemById(reqLnItmId, true);
        }
        RequestLineItemFormBuilder builder = new RequestLineItemFormBuilder(reqLnItmForm, reqLnItm, daoFactory);
        builder.buildCategories();
        builder.buildPurchasingStatuses();
        builder.buildUnits();
        //builder.buildVendors();
        requestForm.setRequestLineItemForm(reqLnItmForm);

        return mapping.findForward("success");
    }

    /**
     * Action that prepares edit view of Purchasing Request Line Item Funding Source
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewEditPurchasingRequestLineItemFundingSource(ActionMapping mapping,
                                                                        ActionForm actionForm,
                                                                        HttpServletRequest request,
                                                                        HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm) actionForm;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        RequestLineItemForm reqLnItmForm = requestForm.getRequestLineItemForm();
        RequestLineItemFundingSourceForm reqLnItmFundingSrcForm =
                (RequestLineItemFundingSourceForm) CollectionUtils.getObjectFromCollectionById(reqLnItmForm.getFundingSourceForms(),
                        reqLnItmForm.getFundingSourceKey(),
                        "key");
        RequestLineItemFundingSourceFormBuilder builder =
                new RequestLineItemFundingSourceFormBuilder(reqLnItmFundingSrcForm, daoFactory);
        builder.buildOrgBudgets(null);
        reqLnItmForm.setFundingSourceForm(reqLnItmFundingSrcForm);

        return mapping.findForward("success");
    }

    /**
     * Action that prepares view of Request Line Item search page
     *
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewSearchPurchasingRequestLineItems(ActionMapping mapping,
                                                              ActionForm actionForm,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response) throws Exception {
        RequestLineItemSearchForm rliSearchForm = (RequestLineItemSearchForm) actionForm;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        RequestLineItemSearchFormBuilder builder = new RequestLineItemSearchFormBuilder(rliSearchForm, daoFactory);
        RequestLineItemSearchFormDirector director = new RequestLineItemSearchFormDirector(builder);
        director.constructPurchasingNew();

        request.getSession().setAttribute(Form.VIEW_ADD_TO_ORDER, rliSearchForm);


        return mapping.findForward("success");
    }

    private void putRequestFormInScope(RequestForm requestForm, HttpSession session) {
        session.setAttribute(Form.PRCHSNG_REQ_LN_ITM, requestForm);
        session.setAttribute(Form.PRCHSNG_REQ_LN_ITM_FNDNG_SRC, requestForm);
        session.setAttribute(Form.REQ_PRCHSNG, requestForm);
    }

    private ModelDetacher getDetacher() {
        ModelDetacher detacher = new HibernateModelDetacher();
        return detacher;
    }
}