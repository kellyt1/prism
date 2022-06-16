package us.mn.state.health.web.struts.fillstockrequests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.builder.materialsrequest.RequestFormCollectionBuilder;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.facade.inventory.fillstockrequests.FillStockRequestsFacade;
import us.mn.state.health.matmgmt.action.StockRequestAction;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.util.PagedQueryResultEnhanced;
import us.mn.state.health.util.Pagination;
import us.mn.state.health.view.materialsrequest.RequestFormCollection;

public class FillStockRequestsAction extends MappingDispatchAction {
    private static String CART_FORM = "requestFormCollection";
    FillStockRequestsFacade fillStockRequestsFacade;


    public void setFillStockRequestsFacade(FillStockRequestsFacade fillStockRequestsFacade) {
        this.fillStockRequestsFacade = fillStockRequestsFacade;
    }

    public ActionForward advancedSearchOpenStockRequests(ActionMapping mapping,
                                                         ActionForm form,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) throws Exception {
        //1. populate the lists

        PagedQueryResultEnhanced queryResult = new PagedQueryResultEnhanced(new ArrayList(), Pagination.DEFAULT_PAGINATION, 0);
        ActionErrors errors = new ActionErrors();
        try {
            if (request.getParameterMap().size() != 0) {
                queryResult = fillStockRequestsFacade.advancedSearchOpenRequests(
                        ((AdvancedSearchFillStockRequestsForm) form)
                                .getSearchCriteria());
            } else {
                request.getSession().setAttribute(CART_FORM, null);
            }
        } catch (RuntimeException e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.custom", e.getMessage()));
        }
        List<Request> list = queryResult.getPageList();
        request.setAttribute("lists", fillStockRequestsFacade.getDropDownLists());
        request.setAttribute("result", queryResult);

        //end of the pagination and sort
        //Retrive the cart

        Object reqFormCollection = request.getSession().getAttribute(CART_FORM);
        RequestFormCollection requestFormCollection;
        requestFormCollection = reqFormCollection == null ? new RequestFormCollection() : (RequestFormCollection) reqFormCollection;


        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        RequestFormCollectionBuilder builder = new RequestFormCollectionBuilder(requestFormCollection, daoFactory, null);
        List requestForms = new ArrayList();
        builder.convertRequestsToRequestFormsForSearchFillStockRequests(queryResult.getPageList(), requestForms);
        requestFormCollection.setRequestForms(requestForms);

        requestFormCollection.setTotalRequests(queryResult.getNrOfElements());

        request.getSession().setAttribute(CART_FORM, requestFormCollection);
        request.getSession().setAttribute("result", queryResult);
        request.getSession().setAttribute("advancedSearchFillStockRequestsForm", form);

        saveErrors(request, errors);
        return mapping.findForward("success");
    }

    public ActionForward generatePickListForSearchOpenStockRequestsTemp(ActionMapping mapping,
                                                                        ActionForm form,
                                                                        HttpServletRequest request,
                                                                        HttpServletResponse response) throws Exception {
        request.setAttribute("result", request.getSession().getAttribute("result"));
        request.setAttribute("advancedSearchFillStockRequestsForm", request.getSession().getAttribute("advancedSearchFillStockRequestsForm"));
        request.setAttribute("lists", fillStockRequestsFacade.getDropDownLists());
        RequestFormCollection requestFormCollection = (RequestFormCollection) form;

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        RequestFormCollectionBuilder builder = new RequestFormCollectionBuilder(requestFormCollection, daoFactory, null);
        builder.maintainTheCartForFillStockRequests();
        request.getSession().setAttribute(CART_FORM, requestFormCollection);

        String forward = StockRequestAction.handleDetailsAndNotesForRequestFormCollection(requestFormCollection, request, daoFactory);
        //forward will be 'reload' always

        String cmd = request.getParameter("cmd");
        if (StringUtils.isEmpty(cmd)) {
            //if the cmd is null (whi)
            forward = "search";
            cmd = null;
        } else {
            return mapping.findForward(forward);
        }

        String parameterList = buildQueryPath(request);

        response.sendRedirect(request.getContextPath() + mapping.findForward("search").getPath() + parameterList);
        return null;
    }

    private String buildQueryPath(HttpServletRequest request) {
        Map parameters = request.getParameterMap();
        String parameterList = "?";
        for (Iterator iterator = parameters.keySet().iterator(); iterator.hasNext();) {
            String parameterName = (String) iterator.next();
            String parameterValue = parameters.get(parameterName).toString();
            if ("cmd".equals(parameterName) || parameterValue == null) continue;
            if (parameterName.startsWith("searchCriteria"))
                parameterList += "&" + parameterName + "=" + ((String[]) parameters.get(parameterName))[0];
        }
        return parameterList;
    }

}
