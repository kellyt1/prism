package us.mn.state.health.web.struts.editstockitem;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import us.mn.state.health.facade.inventory.editStockItem.EditStockItemDropDownListsDTO;
import us.mn.state.health.facade.inventory.editStockItem.EditStockItemFacade;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.EmailBusinessDelegate;
import us.mn.state.health.security.ApplicationResources;
import us.mn.state.health.util.NotificationException;
import us.mn.state.health.util.struts.NotificationAwareAction;
import us.mn.state.health.builder.email.StockItemHitReorderPointEmailBuilder;
import us.mn.state.health.builder.email.EmailBuilder;
import us.mn.state.health.builder.email.StockItemHitSafetyStockEmailBuilder;
import us.mn.state.health.matmgmt.director.email.EmailDirector;

public class EditStockItemAction extends NotificationAwareAction {
    public static final String LISTS_DTO = "listsDTO";
    public static final String form_name = "editStockItemForm";
    private EditStockItemFacade editStockItemFacade;

    public void setEditStockItemFacade(EditStockItemFacade editStockItemFacade) {
        this.editStockItemFacade = editStockItemFacade;
    }

    public ActionForward editStockItem(ActionMapping mapping,
                                       ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        EditStockItemForm editStockItemForm = (EditStockItemForm) form;
        User user = setUpForm(request, editStockItemForm);

        String cmd = request.getParameter("cmd");
        editStockItemForm.setHistoryComments(editStockItemForm.concatHistoryComments());
        ActionForward validationForward = handleValidation(cmd, request, editStockItemForm, mapping);
        if (validationForward != null)
            return validationForward;
        if (cmd.equals(EditStockItemForm.LOAD_ITEM)) {
            editStockItemForm.setStockItem(editStockItemFacade.detachStockItem(Long.valueOf(editStockItemForm.getStockItemId())));
//        editStockItemForm.setHistoryComments(editStockItemForm.concatHistoryComments());
        }

        if (cmd.equals(EditStockItemForm.ADD_STOCK_ITEM_LOCATION)) {
            editStockItemFacade.addStockItemLocation(editStockItemForm.getStockItem(), editStockItemForm.getFacilityId(),
                    editStockItemForm.getLocationCode(), user.getUsername());
            resetLocationsUpdate(editStockItemForm);
        }
        if (cmd.equals(EditStockItemForm.REMOVE_STOCK_ITEM_LOCATION)) {
            editStockItemFacade.removeStockItemLocation(editStockItemForm.getStockItem(), editStockItemForm.getFacilityId(),
                    editStockItemForm.getLocationCode(), user.getUsername());
            resetLocationsUpdate(editStockItemForm);
        }

        if (cmd.equals(EditStockItemForm.ADD_STOCK_ITEM_LOT)) {
            try {
                editStockItemFacade.addStockItemLot(editStockItemForm.getStockItem(), editStockItemForm.getLotCode(), editStockItemForm.getLotExpDate(), user.getUsername());
                resetLotUpdate(editStockItemForm);
            } catch (NotificationException e) {
                printErrors(e, request);
            }
        }

        if (cmd.equals(EditStockItemForm.REMOVE_STOCK_ITEM_LOT)) {
            editStockItemFacade.removeStockItemLot(editStockItemForm.getStockItem(), editStockItemForm.getLotCode(), editStockItemForm.getLotExpDate(), user.getUsername());
            resetLotUpdate(editStockItemForm);
        }

        if (cmd.equalsIgnoreCase(EditStockItemForm.ADD_VENDOR)) {
            try {
                editStockItemFacade.addNewVendor(editStockItemForm.getStockItem(),
                        editStockItemForm.getNewVendorId(), editStockItemForm.getNewVendorCatalogNbr(),
                        editStockItemForm.getNewVendorBuyUnitId(), editStockItemForm.getNewVendorBuyUnitCost(),
                        editStockItemForm.getNewVendorDispenseUnitPerBuyUnit(), editStockItemForm.getNewVendorDiscount(),
                        user.getUsername());
                resetVendorUpdate(editStockItemForm);
            } catch (NotificationException e) {
                printErrors(e, request);
            }
        }

        if (cmd.equalsIgnoreCase(EditStockItemForm.REMOVE_VENDOR)) {
            editStockItemFacade.removeVendor(editStockItemForm.getStockItem(), request.getParameter("vendorKey"), user.getUsername());
        }

        if (cmd.equalsIgnoreCase(EditStockItemForm.PRE_SUBMIT)) {
            return mapping.findForward(EditStockItemForm.PRE_SUBMIT);
        }

        if (cmd.equalsIgnoreCase(EditStockItemForm.SUBMIT)) {
            try {
                editStockItemFacade.attachStockItem(editStockItemForm.getStockItem(), user.getUsername(), editStockItemForm.getNewHistoryComment(), editStockItemForm.getAdjustmentHistory());
//                editStockItemFacade.attachStockItem(editStockItemForm.getStockItem(), user.getUsername(), editStockItemForm.getHistoryComments(), editStockItemForm.getAdjustmentHistory());
                notifyStockItemHitReorderPoint(editStockItemForm.getStockItem()); //int oldQtyOnHand)
                notifyStockItemHitSafetyStock(editStockItemForm.getStockItem());  //int oldQtyOnHand)
                request.getSession().removeAttribute(form_name);
                request.setAttribute("icnbr", editStockItemForm.getStockItem().getFullIcnbr());
                return mapping.findForward("confirmation");
            } catch (HibernateOptimisticLockingFailureException e) {
                request.getSession().removeAttribute(form_name);
                return mapping.findForward("optimisticLockingFailureException");
            }
        }

        if (editStockItemForm.getPage() != editStockItemForm.getNextPage()) {
            editStockItemForm.setPage(editStockItemForm.getNextPage());
        }
        return mapping.findForward("tab" + editStockItemForm.getPage());
    }

    public ActionForward editMyStockItem(ActionMapping mapping,
                                         ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {
        EditStockItemForm editStockItemForm = (EditStockItemForm) form;
        User user = setUpForm(request, editStockItemForm);

        String cmd = request.getParameter("cmd");
        ActionForward validationForward = handleValidation(cmd, request, editStockItemForm, mapping);
        if (validationForward != null)
            return validationForward;

        removeTheInactiveStatus(request);

        if (cmd.equals(EditStockItemForm.LOAD_ITEM)) {
            editStockItemForm.setStockItem(editStockItemFacade.detachStockItem(Long.valueOf(editStockItemForm.getStockItemId())));
            if (!editStockItemForm.getStockItem().getPrimaryContact().getUser().equals(user)
                    && !editStockItemForm.getStockItem().getSecondaryContact().getUser().equals(user)) {
                request.getSession().removeAttribute(form_name);
                return mapping.findForward("cancel");
            }

        }

        if (cmd.equalsIgnoreCase(EditStockItemForm.PRE_SUBMIT)) {
            return mapping.findForward(EditStockItemForm.PRE_SUBMIT);
        }

        if (cmd.equalsIgnoreCase(EditStockItemForm.SUBMIT)) {
            try {
                editStockItemFacade.attachStockItem(editStockItemForm.getStockItem(), user.getUsername(), editStockItemForm.getHistoryComments(), editStockItemForm.getAdjustmentHistory());
                request.getSession().removeAttribute(form_name);
                request.setAttribute("icnbr", editStockItemForm.getStockItem().getFullIcnbr());
                return mapping.findForward("confirmation");
            } catch (HibernateOptimisticLockingFailureException e) {
                request.getSession().removeAttribute(form_name);
                return mapping.findForward("optimisticLockingFailureException");
            }
        }

        if (editStockItemForm.getPage() != editStockItemForm.getNextPage()) {
            editStockItemForm.setPage(editStockItemForm.getNextPage());
        }
        return mapping.findForward("tab" + editStockItemForm.getPage());
    }

    private void removeTheInactiveStatus(HttpServletRequest request) {
        ((EditStockItemDropDownListsDTO) request.getAttribute(LISTS_DTO)).getStatuses().remove(CollectionUtils.find(((EditStockItemDropDownListsDTO) request.getAttribute(LISTS_DTO)).getStatuses(), new Predicate() {
            public boolean evaluate(Object input) {
                return ((Status) input).getStatusCode().equals(Status.INACTIVE);
            }
        }));
    }

    private User setUpForm(HttpServletRequest request, EditStockItemForm editStockItemForm) {
        User user;
        user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        editStockItemForm.setEditStockItemFacade(editStockItemFacade);
        editStockItemForm.setUsername(user.getUsername());
        editStockItemForm.setUser(user);
        populateLists(request);
        return user;
    }

    private ActionForward handleValidation(String cmd, HttpServletRequest request, EditStockItemForm editStockItemForm, ActionMapping mapping) {
        if (validateCommands(cmd, request, editStockItemForm))
            return mapping.findForward("cancel");
        ActionErrors errors = editStockItemForm.validate(mapping, request);
        if (isInputInvalid(errors, request))
            return mapping.findForward("tab" + editStockItemForm.getPage());
        return null;
    }

    private void resetVendorUpdate(EditStockItemForm editStockItemForm) {
        editStockItemForm.setNewVendorId(null);
        editStockItemForm.setNewVendorCatalogNbr(null);
        editStockItemForm.setNewVendorBuyUnitId(null);
        editStockItemForm.setNewVendorBuyUnitCost(null);
        editStockItemForm.setNewVendorDispenseUnitPerBuyUnit(null);
        editStockItemForm.setNewVendorDiscount("0.0");
    }

    private void resetLocationsUpdate(EditStockItemForm editStockItemForm) {
        editStockItemForm.setFacilityId(null);
        editStockItemForm.setLocationCode(null);
    }

    private void resetLotUpdate(EditStockItemForm editStockItemForm) {
        editStockItemForm.setLotCode(null);
        editStockItemForm.setLotExpDate(null);
    }

    public ActionForward getStockItemOnOrderInfo(ActionMapping mapping,
                                                 ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {
        String stockItemId = request.getParameter("stockItemId");
        Long id = null;
        try {
            id = new Long(stockItemId);
        } catch (NumberFormatException ignore) {
        }
        List requestLineItemsOnOrder = editStockItemFacade.getRequestLineItemsOnOrder(id);
        String icnbr = request.getParameter("icnbr");
        request.setAttribute("icnbr", icnbr);
        request.setAttribute("requestLineItemsOnOrder", requestLineItemsOnOrder);
        return mapping.findForward("success");
    }

    private void populateLists(HttpServletRequest request) {
        EditStockItemDropDownListsDTO listsDTO = editStockItemFacade.getDropDownLists();
        request.setAttribute(LISTS_DTO, listsDTO);
    }

    private boolean validateCommands(String cmd, HttpServletRequest request, EditStockItemForm editStockItemForm) {
        if ((!Arrays.asList(EditStockItemForm.commands).contains(cmd)) ||
                (!StringUtils.equals(cmd, EditStockItemForm.LOAD_ITEM) && editStockItemForm.getStockItem() == null)) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            request.getSession().setAttribute(form_name, null);
            return true;
        }
        return false;
    }

    private boolean isInputInvalid(ActionErrors errors, HttpServletRequest request) {
        if (errors != null && errors.size() > 0) {
            saveErrors(request, errors);
            return true;
        }
        return false;
    }


    public ActionForward getStockItemHistory(ActionMapping mapping,
                                             ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        String stockItemId = request.getParameter("stockItemId");
        List stockItemHistory = editStockItemFacade.getStockItemHistory(stockItemId, 10);
        String icnbr = request.getParameter("icnbr");
        request.setAttribute("icnbr", icnbr);
        request.setAttribute("stockItemHistory", stockItemHistory);
        return mapping.findForward("success");
    }

    public ActionForward getStockItemQtyAdjustmentHistory(ActionMapping mapping,
                                                          ActionForm form,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) {
        String stockItemId = request.getParameter("stockItemId");
        List qtyAdjustmentHistory = editStockItemFacade.getQtyAdjustmentHistory(stockItemId, 10);
        String icnbr = request.getParameter("icnbr");
        request.setAttribute("icnbr", icnbr);
        request.setAttribute("qtyAdjustmentHistory", qtyAdjustmentHistory);
        return mapping.findForward("success");
    }

    public ActionForward getStockItemLocationHistory(ActionMapping mapping,
                                                     ActionForm form,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {
        String stockItemId = request.getParameter("stockItemId");
        List stockItemLocationHistory = editStockItemFacade.getStockItemLocationHistory(stockItemId, 10);
        String icnbr = request.getParameter("icnbr");
        request.setAttribute("icnbr", icnbr);
        request.setAttribute("stockItemLocationHistory", stockItemLocationHistory);
        return mapping.findForward("success");
    }

    public ActionForward getItemVendorLinkHistory(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {
        String stockItemId = request.getParameter("stockItemId");
        List itemVendorLinkHistory = editStockItemFacade.getItemVendorLinkHistory(stockItemId, 10);
        String icnbr = request.getParameter("icnbr");
        request.setAttribute("icnbr", icnbr);
        request.setAttribute("itemVendorLinkHistory", itemVendorLinkHistory);
        return mapping.findForward("success");
    }


    /**
     * Helper method to send an email to the appropriate parties if the
     * stock item has hit its re-order point.
     * It should only send the email once, the first time the Qty On Hand
     * moves from greater than Re-Order point to less than or equal to the Re-Order Point.
     *
     * @param si
     *
     * Todd R. 1/24/2008 -- When Quantity On Hand is manually changed, and is now below the ROP, send an email
     * even if the Quantity On Hand was initially below the ROP and so an email has been
     * generated previously.
     */
    private void notifyStockItemHitReorderPoint(StockItem si) { //, int oldQtyOnHand) {
        int rop = si.getReorderPoint();
        int newQtyOnHand = si.getQtyOnHand();
        if (
//                oldQtyOnHand > rop &&  //generate an email regardless even if the previous QtyOnHand was <= ROP
                !si.getIsOnOrder() &&
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
//                log.error("Error in StockRequestAction.notifyStockItemHitReorderPoint()", e);
            }
        }
    }

    private void notifyStockItemHitSafetyStock(StockItem si) { //, int oldQtyOnHand) {
        int rop = 0;
        if (si.getSafetyStock() != null) rop = si.getSafetyStock();
        int newQtyOnHand = si.getQtyOnHand();
       // OrderLineItem xx = null;

        if (rop > 0 &&
                !si.getIsOnOrder() &&
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
//                log.error("Error in StockRequestAction.notifyStockItemHitSafetyStock()", e);
            }
        }
    }

}
