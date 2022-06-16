package us.mn.state.health.web.struts.searchorders;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import us.mn.state.health.facade.purchasing.searchorder.SearchOrdersFacade;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.util.NotificationException;
import us.mn.state.health.util.PagedQueryResultEnhanced;
import us.mn.state.health.util.Pagination;
import us.mn.state.health.util.struts.NotificationAwareAction;

public class SearchOrdersAction extends NotificationAwareAction {
    private static final String ORDERS_FORM = "";
    private SearchOrdersFacade searchOrdersFacade;
    public static Log log = LogFactory.getLog(SearchOrdersAction.class);

    public void setSearchOrdersFacade(SearchOrdersFacade searchOrdersFacade) {
        this.searchOrdersFacade = searchOrdersFacade;
    }

    public ActionForward advancedSearchOrders(ActionMapping mapping,
                                                         ActionForm form,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response){
        PagedQueryResultEnhanced queryResult = new PagedQueryResultEnhanced(new ArrayList(), Pagination.DEFAULT_PAGINATION, 0);
        ActionErrors errors = new ActionErrors();
        long start = System.currentTimeMillis();
        try {
            if (request.getParameterMap().size() != 0) {
                if (request.getParameterMap().size() > 1 && request.getParameter("message") == null )  {
                   queryResult = searchOrdersFacade.searchOrders(((SearchOrdersForm) form).getSearchCriteria());
                }
            }
        } catch (RuntimeException e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.custom", e.getMessage()));
        } catch (NotificationException e) {
            printErrors(e, request);
        }
        List<Request> list = queryResult.getPageList();
        prepareLists(request);
        //log.info("Search Time for advancedSearchOrders = " + (System.currentTimeMillis() -  start));
        request.setAttribute("result", queryResult);
        return mapping.findForward("success");
    }

    private void prepareLists(HttpServletRequest request) {
        request.setAttribute("lists", searchOrdersFacade.getDropDownLists());
    }
}
