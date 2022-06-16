package us.mn.state.health.matmgmt.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.builder.materialsrequest.RequestFormBuilder;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.director.RequestFormDirector;
import us.mn.state.health.view.materialsrequest.RequestForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemFundingSourceForm;

public class RequestPurchasingAction extends MappingDispatchAction {

    public ActionForward viewCreateRequestPurchasing(ActionMapping mapping,
                                                     ActionForm actionForm,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) throws Exception {
        RequestForm requestForm = (RequestForm)actionForm;
        requestForm.setRequestLineItemForm(new RequestLineItemForm());
        requestForm.getRequestLineItemForm().setFundingSourceForm(new RequestLineItemFundingSourceForm());
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        RequestFormBuilder builder = new RequestFormBuilder(requestForm, daoFactory);
        RequestFormDirector director = new RequestFormDirector(builder);
        director.constructNew();
        
        return mapping.findForward("success");
    }
    
    public ActionForward saveRequestPurchasing(ActionMapping mapping,
                                               ActionForm actionForm,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {        
        RequestForm requestForm = (RequestForm)actionForm;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        
        return mapping.findForward("success");
    }
}