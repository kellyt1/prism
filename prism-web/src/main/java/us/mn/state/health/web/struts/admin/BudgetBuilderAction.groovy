package us.mn.state.health.web.struts.admin

import groovy.util.logging.Log4j
import org.apache.struts.action.ActionForm
import org.apache.struts.action.ActionForward
import org.apache.struts.action.ActionMapping
import org.apache.struts.actions.MappingDispatchAction
import org.hibernate.HibernateException
import org.hibernate.Session
import org.hibernate.SessionFactory
import us.mn.state.health.common.exceptions.InfrastructureException
import us.mn.state.health.dao.configuration.HibernateConfigurationItemDAO
import us.mn.state.health.matmgmt.util.Constants
import us.mn.state.health.model.common.Group
import us.mn.state.health.model.common.User
import us.mn.state.health.model.util.configuration.ConfigurationItem
import us.mn.state.health.model.util.search.struts.IndexForm
import us.mn.state.health.persistence.HibernateUtil
import us.mn.state.health.security.ApplicationResources

import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Log4j
public final class BudgetBuilderAction extends MappingDispatchAction {

    public ActionForward changeBudgetBuilderLink(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws Exception {

        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (!user.isInGroup(Group.PRISM_DEVELOPERS))   return mapping.findForward("notAuthorized");

        BudgetBuilderForm bbf = (BudgetBuilderForm) form;

        HibernateConfigurationItemDAO hcidao = new HibernateConfigurationItemDAO();
        ConfigurationItem ciLink;

        try {
            ciLink = hcidao.getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.BUDGET_BUILDER_LINK);
            bbf.setBudgetBuilderLink(ciLink.getValue());
        } catch (InfrastructureException ignore) {
        }

        return mapping.findForward("success");
    }

    public ActionForward changeBudgetBuilderLinkSave(ActionMapping mapping,
                                                      ActionForm form,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) throws Exception {

        BudgetBuilderForm bbf = (BudgetBuilderForm) form;

        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (!(user.isInGroup(Group.PRISM_DEVELOPERS)))   return mapping.findForward("notAuthorized");

        HibernateConfigurationItemDAO hcidao = new HibernateConfigurationItemDAO();
        ConfigurationItem ciLink = null;

        try {
            ciLink = hcidao.getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.BUDGET_BUILDER_LINK);
            ciLink.setValue(bbf.getBudgetBuilderLink());
        } catch (InfrastructureException ignore) {
        }

        try {
            Session session = HibernateUtil.getSession();
            session.saveOrUpdate(ciLink);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        catch(Exception e){
            e.printStackTrace();
            throw new InfrastructureException(e);
        }
        catch(Throwable throwable){
            throwable.printStackTrace();
        }
        return mapping.findForward("success");
    }
}