package us.mn.state.health.web.struts.admin


import groovy.util.logging.Log4j;
import us.mn.state.health.persistence.HibernateUtil;
import us.mn.state.health.model.util.search.struts.IndexForm;
import us.mn.state.health.model.util.configuration.ConfigurationItem;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.security.ApplicationResources;
import us.mn.state.health.dao.configuration.HibernateConfigurationItemDAO;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.matmgmt.util.Constants;
import org.hibernate.*;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.actions.MappingDispatchAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.util.*;


@Log4j
public final class EvictCacheAction extends MappingDispatchAction {

    public ActionForward admin(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {

        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);

        return  mapping.findForward(user.isInGroup(Group.PRISM_DEVELOPERS) ? "success" : "notAuthorized")
    }


    public ActionForward selectCacheToEvict(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {

        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (user.isInGroup(Group.PRISM_DEVELOPERS)) {
            CachedObjectsForm cof = (CachedObjectsForm) form;
            cof.indexForms.clear()
            cof.indexForms.add(new IndexForm("us.mn.state.health.model.materialsrequest.Request", false));
            cof.indexForms.add(new IndexForm("us.mn.state.health.model.materialsrequest.RequestLineItem", false));
            cof.indexForms.add(new IndexForm("us.mn.state.health.model.materialsrequest.DeliveryDetail", false));
            cof.indexForms.add(new IndexForm("us.mn.state.health.model.common.Category", false));
            cof.indexForms.add(new IndexForm("Queries", false));

            return mapping.findForward("success");
        } else {
            return mapping.findForward("notAuthorized");
        }
    }


    public ActionForward evictCache(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (user.isInGroup(Group.PRISM_DEVELOPERS)) {
            CachedObjectsForm cof = (CachedObjectsForm) form;
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

            response.setContentType("text/html");
            ServletOutputStream out = response.getOutputStream();

            out.println("<head><title>Evicting Cache</title></head>");
            out.println("<h2>Evicting/Clearing Objects from Cache</h2><br/>");
            out.println("<p class='bg-success'>");

            cof.indexForms.each {
                if (it.isSelected()) {
                    if (it.getIndex().toString().equals("Queries")) {
                        sessionFactory.evictQueries();
                        out.println("All <b>Cached Queries</b> Evicted <br>");
                        log.info("All Cached Queries Evicted " + new Date());
                    }
                    else {
                        sessionFactory.evict(Class.forName(it.getIndex().toString()));
                        out.println("<b>" + it.getIndex().toString() + "</b> Class Evicted <br/>");
                        log.info(it.getIndex().toString() + " Class Evicted at " + new Date());
                    }
                }
            }

            String classToEvict = request.getParameter("CLASSTOEVICT");

            if (classToEvict) {
                try {
                    sessionFactory.evict(Class.forName(classToEvict));
                    out.println("<b>" + classToEvict + "</b> Class Evicted <br/>");
                    log.info(classToEvict + " Class Evicted at " + new Date());
                }
                catch (Exception ignore) {
                    out.println("ERROR: <b>" + classToEvict + "</b> Class was NOT Evicted <br/>");
                    log.error("ERROR: " + classToEvict + " Class was NOT Evicted " + new Date());
                }
                finally {
                    out.println("</p>");
                    out.flush();
                    out.close();
                }
            }
            return null;
        }
        else {
            return mapping.findForward("notAuthorized");
        }
    }


    public ActionForward changePrismNotifications(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (!user.isInGroup(Group.PRISM_DEVELOPERS))   return mapping.findForward("notAuthorized");

        CachedObjectsForm cof = (CachedObjectsForm) form;

        HibernateConfigurationItemDAO hcidao = new HibernateConfigurationItemDAO();
        ConfigurationItem ciLogin;

        try {
            ciLogin = hcidao.getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.MESSAGE_NOTIFY_LOGIN);
            cof.setPrismLoginNotification(ciLogin.getValue());
        } catch (InfrastructureException ignore) {
        }

        ConfigurationItem ciEveryPage;
        try {
            ciEveryPage = hcidao.getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.MESSAGE_NOTIFY_MAINMENU);
            cof.setPrismEveryPageNotification(ciEveryPage.getValue());
        } catch (InfrastructureException ignore) {
        }

        return mapping.findForward("success");
    }


    public ActionForward changePrismNotificationsSave(ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {

        CachedObjectsForm cof = (CachedObjectsForm) form;

        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (!(user.isInGroup(Group.PRISM_DEVELOPERS)))   return mapping.findForward("notAuthorized");

        HibernateConfigurationItemDAO hcidao = new HibernateConfigurationItemDAO();
        ConfigurationItem ciLogin = null;

        try {
            ciLogin = hcidao.getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.MESSAGE_NOTIFY_LOGIN);
            ciLogin.setValue(cof.getPrismLoginNotification());
        } catch (InfrastructureException ignore) {
        }

        ConfigurationItem ciEveryPage = null;
        try {
            ciEveryPage = hcidao.getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.MESSAGE_NOTIFY_MAINMENU);
            ciEveryPage.setValue(cof.getPrismEveryPageNotification());
        } catch (InfrastructureException ignore) {
        }

        try {
            Session session = HibernateUtil.getSession();
            session.saveOrUpdate(ciLogin);
            session.saveOrUpdate(ciEveryPage);
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