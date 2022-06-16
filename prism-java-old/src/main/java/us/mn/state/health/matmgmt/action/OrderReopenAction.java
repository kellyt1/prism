package us.mn.state.health.matmgmt.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.builder.email.ReopenRequestLineItemEmailBuilder;
import us.mn.state.health.builder.materialsrequest.RequestLineItemBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.director.RequestLineItemDirector;
import us.mn.state.health.matmgmt.director.email.EmailDirector;
import us.mn.state.health.matmgmt.util.Form;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.EmailBusinessDelegate;
import us.mn.state.health.security.ApplicationResources;
import us.mn.state.health.view.materialsrequest.RequestForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemSearchForm;
import us.mn.state.health.view.purchasing.OrderForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Struts Controller that processes Order actions
 */
public class OrderReopenAction extends MappingDispatchAction {
    private static Log log = LogFactory.getLog(OrderReopenAction.class);



    public ActionForward reopenMessagePage(ActionMapping mapping,
                                    ActionForm actionForm,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        log.debug("Reopen Request Line Item");
        StringBuffer outString = new StringBuffer();

        RequestLineItemSearchForm form = (RequestLineItemSearchForm) actionForm;
        Collection<RequestLineItemForm> selectedRLIForms = CollectionUtils.getMatchingItems(form.getRliForms(),
                Boolean.TRUE,
                "selected");

        OrderForm orderForm = new OrderForm();
        orderForm.getRequestLineItemForms().addAll(selectedRLIForms);
        request.getSession().setAttribute(Form.ORDER, orderForm);

        return mapping.findForward("success");
    }


    public ActionForward reopenRLIS(ActionMapping mapping,
                                    ActionForm actionForm,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        log.debug("Reopen Request Line Item");
        StringBuffer outString = new StringBuffer();

        OrderForm form = (OrderForm) actionForm;
        Collection<RequestLineItemForm> selectedRLIForms = form.getRequestLineItemForms();
        String sep = "";
        for (RequestLineItemForm selectedRLI : selectedRLIForms) {
            outString.append(selectedRLI.getRequestLineItem().getRequest().getTrackingNumber()).append(sep);
            sep = ", ";
            reopenRLI(selectedRLI.getRequestLineItem(), form.getNoteFormKey());
        }
        //request.setAttribute("message", "The selected request line item(s) have been sent back to the requestor " + outString.toString());
        response.getOutputStream().print("The selected request line item(s) have been sent back to the requestor " + outString.toString());
        response.getOutputStream().flush();
        response.getOutputStream().close();

        return null; // mapping.findForward("success");
    }
    public ActionForward reSubmitRLIS(ActionMapping mapping,
                                    ActionForm actionForm,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        log.debug("ReSubmit Request Line Item");
        StringBuffer outString = new StringBuffer();
        RequestForm form = (RequestForm) actionForm;
         User requestor = (User) request.getSession().getAttribute(ApplicationResources.USER);
        RequestLineItemForm requestLineItemForm = form.getRequestLineItemForm();

        // take the form parameters and update the model.
        reSubmitRLI(requestLineItemForm, requestor);






        return null;
    }
    private boolean reopenRLI(RequestLineItem rli, String additionalMessage) throws InfrastructureException {
        // Set the status to Pending Need more info
       DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        Status wfp = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.PENDING_NEED_MORE_INFO);
        RequestLineItem rliNew = daoFactory.getRequestLineItemDAO().getRequestLineItemById(rli.getRequestLineItemId());

        rliNew.setStatus(wfp);
        daoFactory.getRequestLineItemDAO().makePersistent(rliNew);


        // Email person and tell them we are waiting
        sendEmailToRequestor(rli, daoFactory, additionalMessage);

        return Boolean.TRUE;
    }

    private void sendEmailToRequestor(RequestLineItem rli, DAOFactory daoFactory, String additionalMessage) throws InfrastructureException {
        EmailBean emailBean = new EmailBean();
        ReopenRequestLineItemEmailBuilder builder = new ReopenRequestLineItemEmailBuilder(rli, emailBean, daoFactory);
        builder.setExtraMessage(additionalMessage);
        EmailDirector director = new EmailDirector(builder);
        director.construct();

        EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
        emailBusinessDelegate.sendEmail(emailBean);
    }

    private boolean reSubmitRLI(RequestLineItemForm requestLineItemForm, User requestor) throws InfrastructureException {
        boolean resubmitted = true;
         // Set the status to Pending Need more info
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        Status wfp = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.WAITING_FOR_APPROVAL);
        RequestLineItem rliNew = daoFactory.getRequestLineItemDAO().getRequestLineItemById(Long.valueOf(requestLineItemForm.getRequestLineItemId()));

        rliNew.setStatus(wfp);
        rliNew.getRequest().executeBusinessRules();

        RequestLineItemBuilder builder = new RequestLineItemBuilder(rliNew, requestLineItemForm, daoFactory,requestor);
        RequestLineItemDirector director = new RequestLineItemDirector(builder);
        try {
            director.constructEditLineItem();
            daoFactory.getRequestLineItemDAO().makePersistent(rliNew);
            daoFactory.commitTransaction(false);
        }
        catch(Exception e) {
            resubmitted = false;
        }
         return resubmitted;
     }


}