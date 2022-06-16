package us.mn.state.health.matmgmt.action;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.builder.materialsrequest.RequestLineItemNoteFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.report.JasperReportWriter;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.util.Report;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.materialsrequest.RequestLineItemNote;
import us.mn.state.health.security.ApplicationResources;
import us.mn.state.health.view.inventory.Command;
import us.mn.state.health.view.materialsrequest.RequestForm;
import us.mn.state.health.view.materialsrequest.RequestFormCollection;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemNoteForm;

public class StockRequestAction extends MappingDispatchAction {
    public static Log log = LogFactory.getLog(StockRequestAction.class);

    public ActionForward generatePickList(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        RequestFormCollection requestFormCollection = (RequestFormCollection) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        String forward = handleDetailsAndNotesForRequestFormCollection(requestFormCollection, request, daoFactory);
        if (forward != null) {
            return mapping.findForward(forward);
        } else {  //they clicked 'Generate Pick List'
            Map cart = requestFormCollection.getCart();
            //process the last page before going to the generate pick list
            Collection requestForms = requestFormCollection.getRequestForms();
            for (Object requestFormObject : requestForms) {
                RequestForm requestForm = (RequestForm) requestFormObject;
                String requestId = requestForm.getRequestId();
                if (requestForm.getSelected()) {
                    cart.put(requestId, requestForm);
                }
                // remove it from the cart
                else {
                    cart.remove(requestId);
                }
            }
            saveToken(request);
            return mapping.findForward("success");
        }
    }


    public static String handleDetailsAndNotesForRequestFormCollection(RequestFormCollection requestForms,
                                                                       HttpServletRequest request,
                                                                       DAOFactory daoFactory) throws InfrastructureException {
        String forwardName = null;
        if (Command.SHOW_DETAIL.equalsIgnoreCase(requestForms.getCmd())) {
            ArrayList requestFormsList = (ArrayList) requestForms.getRequestForms();
            RequestForm reqForm = (RequestForm) requestFormsList.get(Integer.parseInt(requestForms.getRequestFormIndex()));
            reqForm.setShowDetail(Boolean.TRUE);
            requestForms.setCmd(null);
            return forwardName = "reload";
        } else if (Command.HIDE_DETAIL.equalsIgnoreCase(requestForms.getCmd())) {
            ArrayList requestFormsList = (ArrayList) requestForms.getRequestForms();
            RequestForm reqForm = (RequestForm) requestFormsList.get(Integer.parseInt(requestForms.getRequestFormIndex()));
            reqForm.setShowDetail(Boolean.FALSE);
            requestForms.setCmd(null);
            return forwardName = "reload";
        } else if (Command.CLOSE_RLI.equalsIgnoreCase(requestForms.getCmd())) {
            //set rli status to FULFILLED w/o deducting inventory
            String rliId = request.getParameter("rliId");
            RequestLineItem rli = daoFactory.getRequestLineItemDAO().getRequestLineItemById(new Long(rliId), true);
            Status fulfilled = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST,
                    Status.FULFILLED);
            rli.setStatus(fulfilled);

            ArrayList requestFormsList = (ArrayList) requestForms.getRequestForms();
            RequestForm reqForm = (RequestForm) requestFormsList.get(Integer.parseInt(requestForms.getRequestFormIndex()));
            Collection requestLineItemForms = reqForm.getRequestLineItemForms();
            RequestLineItemForm rliForm =
                    (RequestLineItemForm) CollectionUtils.getObjectFromCollectionById(requestLineItemForms, rliId, "requestLineItemId");
            requestLineItemForms.remove(rliForm);

            requestForms.setCmd(null);
            return forwardName = "reload";

        } else if (Command.SHOW_NOTES.equalsIgnoreCase(requestForms.getCmd())) {
            String rliId = request.getParameter("rliId");
            ArrayList requestFormsList = (ArrayList) requestForms.getRequestForms();
            RequestForm reqForm = (RequestForm) requestFormsList.get(Integer.parseInt(requestForms.getRequestFormIndex()));
            for (Object o : reqForm.getRequestLineItemForms()) {
                RequestLineItemForm rliForm = (RequestLineItemForm) o;
                String requestLineItemId = rliForm.getRequestLineItemId();
                if (requestLineItemId.equals(rliId)) {
                    rliForm.setShowNotes(Boolean.TRUE);
                    requestForms.setCmd(null);
                    return forwardName = "reload";  //break out of the loop now
                }
            }
            return forwardName = "reload";
        } else if (Command.HIDE_NOTES.equalsIgnoreCase(requestForms.getCmd())) {
            String rliId = request.getParameter("rliId");
            ArrayList requestFormsList = (ArrayList) requestForms.getRequestForms();
            RequestForm reqForm = (RequestForm) requestFormsList.get(Integer.parseInt(requestForms.getRequestFormIndex()));
            for (Object o : reqForm.getRequestLineItemForms()) {
                RequestLineItemForm rliForm = (RequestLineItemForm) o;
                if (rliForm.getRequestLineItemId().equals(rliId)) {
                    rliForm.setShowNotes(Boolean.FALSE);
                    requestForms.setCmd(null);
                    return forwardName = "reload"; //break out of the loop now;
                }
            }
            return forwardName = "reload";
        } else if (Command.ADD_NOTE.equalsIgnoreCase(requestForms.getCmd())) {
            //in this case, we'll add the note to the model object (i.e., we'll persist the note and then reload the page.
            String rliId = request.getParameter("rliId");
            ArrayList requestFormsList = (ArrayList) requestForms.getRequestForms();
            RequestForm reqForm = (RequestForm) requestFormsList.get(Integer.parseInt(requestForms.getRequestFormIndex()));
            RequestLineItemForm rliForm =
                    (RequestLineItemForm) CollectionUtils.getObjectFromCollectionById(reqForm.getRequestLineItemForms(), rliId, "requestLineItemId");
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
                requestForms.setCmd(null);
                rliForm.setTextNote(null);
                return forwardName = "reload";
            }
            requestForms.setCmd(null);
            return forwardName = "reload";
        }
        requestForms.setCmd(null);
        return forwardName;
    }

    public ActionForward fillStockRequests(ActionMapping mapping,
                                           ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        RequestFormCollection requestFormCollection = (RequestFormCollection) form;
        ArrayList requestFormsList = (ArrayList) requestFormCollection.getCartRequestForms();
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        User actor = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (actor == null) {
            return mapping.findForward("failure");
        }

        if (requestFormCollection.getCmd().equalsIgnoreCase(Command.PRINT_MAILING_LABELS)) {
            JasperReportWriter reportWriter = new JasperReportWriter(this.getServlet(),response,
                    Report.MAILING_LABELS,
                    Report.INVENTORY_OUTPUT_PATH,
                    requestFormCollection.getCart().values(),
                    new HashMap());
            reportWriter.write();
            return null;
        } else if (requestFormCollection.getCmd().equalsIgnoreCase(Command.PRINT_PACKING_SLIPS)) {
            HashMap hashmap = new HashMap();
            String rliSubReportPath = this.getServlet().getServletContext().getRealPath(Report.RLI_SUB_REPORT + ".jasper");
            hashmap.put("rliSubReport", rliSubReportPath);
            Collection psdc = new HashSet();
            for (Iterator iterator = requestFormCollection.getCart().values().iterator(); iterator.hasNext();) {
                Object o =  iterator.next();
                for (Iterator iterator2 = ((RequestForm)o).getRequestLineItemForms().iterator(); iterator2.hasNext();) {
                    Object o2 =  iterator2.next();
                    PackingSlipData psd = new PackingSlipData((RequestForm) o);
                    psd.addRequestLineItem(((RequestLineItemForm) o2));
                    psdc.add(psd);
                }
        }
            JasperReportWriter reportWriter = new JasperReportWriter(this.getServlet(),response,
                    Report.PACKING_SLIP,
                    Report.INVENTORY_OUTPUT_PATH,
//                    requestFormCollection.getCart().values(),
                    psdc,
                    hashmap);
          //return new ActionForward(reportWriter.write(), true);
            reportWriter.write();
            return null;
//          return new ActionForward(reportWriter.write(response), true);
        } else if (requestFormCollection.getCmd().equalsIgnoreCase(Command.PRINT_PICK_LIST)) {
            //first create a collection containing all the RLI's from all the selected requests
//            Collection allRLIForms = new ArrayList();
            Collection pldc = new HashSet();
            for (Object o : requestFormCollection.getCart().values()) {
                for (Iterator iterator2 = ((RequestForm)o).getRequestLineItemForms().iterator(); iterator2.hasNext();) {
                    Object o2 =  iterator2.next();
//                    RequestForm reqForm = (RequestForm) o;
                    PickListData pld = new PickListData((RequestForm) o);
                    pld.addRequestLineItem((RequestLineItemForm) o2);
                    pldc.add(pld);
//                allRLIForms.addAll(reqForm.getRequestLineItemForms());
                }
            }
            //now generate the report
            JasperReportWriter reportWriter = new JasperReportWriter(this.getServlet(),response,
                    Report.PICK_LIST,
                    Report.INVENTORY_OUTPUT_PATH,
//                    allRLIForms,
                    pldc,
                    new HashMap());
            reportWriter.write();
            //return new ActionForward(reportWriter.write(), true);
            //return new ActionForward(reportWriter.write(), true);
            return null;
        } else {  //they clicked 'Deduct Inventory'
            if (isTokenValid(request)) {
                resetToken(request);
                for (Object reqForm : requestFormsList) {
                    RequestForm requestForm = (RequestForm) reqForm;
                    for (Object o : requestForm.getRequestLineItemForms()) {
                        RequestLineItemForm rliForm = (RequestLineItemForm) o;
                        RequestLineItem rli =
                                daoFactory.getRequestLineItemDAO().getRequestLineItemById(new Long(rliForm.getRequestLineItemId()), true);
                        int qtyPicked = Integer.parseInt(rliForm.getQuantityPicked());
                        fillStockRequestHelper(rli, qtyPicked);
                    }
                    //Email disabled for migration to SWIFT inventory
                    //notifyRequestor(requestForm);
                }
                ActionMessages messages = new ActionMessages();
                ActionMessage msg = new ActionMessage("message.success");
                messages.add("message1", msg);
                saveMessages(request, messages);
                requestFormCollection.setReset(true);
                return mapping.findForward("success");
            } else {
                saveToken(request);
                return mapping.findForward("success");
            }
        }
    }

    /**
     * A struts-independent helper method that handles the business logic of fulfilling a
     * stock request. I stripped this out of filStockRequests because that method is tightly coupled to
     * struts and is therefore too difficult to unit test without using StrutsTestCase.  This way, we can
     * use StrutsTestCase to test fillStockRequest, and just JUnit to test this method. Later we may
     * factor all the pure business logic out of the struts action methods and put them in more 'pure', easily
     * unit-testable logic classes.  I.e., we'll create another layer that consists of only business logic.
     *
     * @param qtyPicked
     * @param rli
     * @throws java.lang.Exception
     */
    public void fillStockRequestHelper(RequestLineItem rli, int qtyPicked) throws Exception {
        int totalQtyFilled = qtyPicked + rli.getQuantityFilled();
        rli.setQuantityFilled(totalQtyFilled);
        rli.setDateDelivered(Calendar.getInstance().getTime());
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);

        //deduct on-hand amt for the stock item
        StockItem si = daoFactory.getStockItemDAO().getStockItemById(rli.getItem().getItemId(), true);
        int oldQtyOnHand = si.getQtyOnHand();
        si.setQtyOnHand(Math.max(oldQtyOnHand - qtyPicked, 0));
        rli.setItem(si);  //is this necessary?

        //Disable emails for migration to SWIFT inventory
        //notifyStockItemHitReorderPoint(si, oldQtyOnHand);
        //notifyStockItemHitSafetyStock(si, oldQtyOnHand);

        //set the status of the rli to Fulfilled if it has been fulfilled, or if the
        //stock item is inactive AND the qtyOnHand is less than 1.
        if (totalQtyFilled >= rli.getQuantity() ||
                (si.getStatus().getStatusCode().equals(Status.INACTIVE) && si.getQtyOnHand() < 1)) {
            Status fulfilled = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST,
                    Status.FULFILLED);
            rli.setStatus(fulfilled);
        }
    }

    /**
     * helper method to send email notification to requestor
     *
     * @param requestForm
     */
/*    private void notifyRequestor(RequestForm requestForm) {
        try {
            EmailBean emailBean = new EmailBean();
            EmailBuilder emailBuilder = new FillStockRequestEmailBuilder(requestForm, emailBean);
            EmailDirector emailDirector = new EmailDirector(emailBuilder);
            emailDirector.construct();
            EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
            emailBusinessDelegate.sendEmail(emailBean);
        }
        catch (Exception e) {
            log.error("Error in StockRequestAction.notifyRequestor()", e);
        }
    }*/

    /**
     * Helper method to send an email to the appropriate parties if the
     * stock item has hit its re-order point.
     * It should only send the email once, the first time the Qty On Hand
     * moves from greater than Re-Order point to less than or equal to the Re-Order Point.
     *
     * @param si
     * @param oldQtyOnHand - the previous value for Qty On hand, before it was decremented.
     */
/*    private void notifyStockItemHitReorderPoint(StockItem si, int oldQtyOnHand) {
        int rop = si.getReorderPoint().intValue();
        int newQtyOnHand = si.getQtyOnHand().intValue();
        if (oldQtyOnHand > rop &&
                !si.getIsOnOrder().booleanValue() &&
                newQtyOnHand <= rop &&
                si.getStatus().getStatusCode().equalsIgnoreCase(Status.ACTIVE)) {
            //only send the email once, when the stock item's quantity on hand 
            //moves from being greater than the ROP to being less than or equal to the ROP.
            try {
                EmailBean emailBean = new EmailBean();
                EmailBuilder emailBuilder = new StockItemHitReorderPointEmailBuilder(si, emailBean);
                EmailDirector emailDirector = new EmailDirector(emailBuilder);
                emailDirector.construct();
                EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
                emailBusinessDelegate.sendEmail(emailBean);
            }
            catch (Exception e) {
                log.error("Error in StockRequestAction.notifyStockItemHitReorderPoint()", e);
            }
        }
    }*/

/*    private void notifyStockItemHitSafetyStock(StockItem si, int oldQtyOnHand) {
        int rop = 0;
        if (si.getSafetyStock() != null) rop = si.getSafetyStock().intValue();
        int newQtyOnHand = si.getQtyOnHand().intValue();
        OrderLineItem xx = null;

        if (rop > 0 &&
                !si.getIsOnOrder().booleanValue() &&
                newQtyOnHand <= rop &&
                si.getStatus().getStatusCode().equalsIgnoreCase(Status.ACTIVE)) {
            //only send the email once, when the stock item's quantity on hand
            //moves from being greater than the ROP to being less than or equal to the ROP.
            try {
                EmailBean emailBean = new EmailBean();
                EmailBuilder emailBuilder = new StockItemHitSafetyStockEmailBuilder(si, emailBean);

                EmailDirector emailDirector = new EmailDirector(emailBuilder);
                emailDirector.construct();
                EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
                emailBusinessDelegate.sendEmail(emailBean);
            }
            catch (Exception e) {
                log.error("Error in StockRequestAction.notifyStockItemHitSafetyStock()", e);
            }
        }
    }*/
}