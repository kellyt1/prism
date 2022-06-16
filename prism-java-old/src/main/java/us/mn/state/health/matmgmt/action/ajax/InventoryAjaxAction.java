package us.mn.state.health.matmgmt.action.ajax;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.actions.MappingDispatchAction;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class InventoryAjaxAction extends MappingDispatchAction {
    /**
     * This action returns all the requests that make a stock item to be on order
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward getStockItemIsOnOrderInformation(ActionMapping mapping,
                                                          ActionForm actionForm,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) throws Exception {
        String stockItemId = request.getParameter("stockItemId");
        Long id = null;
        try {
            id = new Long(stockItemId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        List openRequests = DAOFactory.getDAOFactory(DAOFactory.DEFAULT)
                .getRequestDAO()
                .findIsOnOrderRequestsForStockItem(id);
        String icnbr = request.getParameter("icnbr");
        request.setAttribute("icnbr", icnbr);
        request.setAttribute("openRequests", openRequests);
        return mapping.findForward("success");
    }

    public ActionForward getRequestLineItems(ActionMapping mapping,
                                             ActionForm actionForm,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws InfrastructureException {
        String requestid = request.getParameter("requestId");
        String requestTrackingNumber = request.getParameter("requestTrackingNumber");
        Long id = null;
        try {
            id = new Long(requestid);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        List openRequestLineItems = new ArrayList(DAOFactory.getDAOFactory(DAOFactory.DEFAULT)
                .getRequestDAO()
                .getRequestById(id, false).getRequestLineItems());
        request.setAttribute("requestTrackingNumber", requestTrackingNumber);
        request.setAttribute("openRequestLineItems", openRequestLineItems);
        return mapping.findForward("success");
    }

}
