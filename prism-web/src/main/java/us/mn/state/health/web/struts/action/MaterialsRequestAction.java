package us.mn.state.health.web.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.facade.materialsrequest.viewMaterialsRequests.FindRequestsFacade;
import us.mn.state.health.facade.materialsrequest.viewMaterialsRequests.FindRequestsFacadeResult;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.web.struts.view.materialsrequest.SearchMaterialRequestsForm;

public class MaterialsRequestAction extends MappingDispatchAction {

    private FindRequestsFacade findRequestsFacade;


    public void setFindRequestsFacade(FindRequestsFacade findRequestsFacade) {
        this.findRequestsFacade = findRequestsFacade;
    }

    public ActionForward advancedSearchMaterialsRequests(ActionMapping mapping,
                                                         ActionForm form,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) throws Exception {
        SearchMaterialRequestsForm searchMaterialRequestsForm = (SearchMaterialRequestsForm) form;
        FindRequestsFacadeResult requests = findRequestsFacade.findAllRequests(
                searchMaterialRequestsForm.getRequestSearchCriteria());
        FindRequestsFacadeResult users = findRequestsFacade.findUsersList();
        FindRequestsFacadeResult statuses = findRequestsFacade.findStatusesList(StatusType.MATERIALS_REQUEST);
        FindRequestsFacadeResult categories = findRequestsFacade.findCategoryList();
        request.setAttribute("users", users);
        request.setAttribute("statuses", statuses);
        request.setAttribute("categories", categories);
        request.setAttribute("requests", requests.getQueryResult().getPageList());

        return mapping.findForward("success");
    }

}
