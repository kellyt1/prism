package us.mn.state.health.model.util.search.struts;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.util.search.Searchable;
import us.mn.state.health.persistence.HibernateUtil;



public class LuceneIndexCreatorAction extends MappingDispatchAction {
    public static Log log = LogFactory.getLog(LuceneIndexCreatorAction.class);

    public ActionForward selectIndexes(ActionMapping mapping,
                                       ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        LuceneIndexesForm indexesForm = (LuceneIndexesForm) form;
        indexesForm.setIndexForms(new ArrayList());
        for (Class implementation : Searchable.IMPLEMENTATIONS) {
            String name1 = implementation.getName();
            indexesForm.getIndexForms().add(new IndexForm(Class.forName(name1).newInstance(), false));
        }
        return mapping.findForward("success");
    }

    public ActionForward buildIndexes(ActionMapping mapping,
                                      ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        LuceneIndexesForm indexesForm = (LuceneIndexesForm) form;
        final String cmd = request.getParameter("cmd");
        List indexes = new ArrayList();
        for (Iterator iterator = indexesForm.getIndexForms().iterator(); iterator.hasNext();) {
            IndexForm indexForm = (IndexForm) iterator.next();
            if (indexForm.isSelected()) {
                indexes.add(indexForm.getIndex());
            }
        }

        final List idxs = indexes;

        int numberOfMillisecondsInTheFuture = 0;
        Date timeToRun = new Date(System.currentTimeMillis() + numberOfMillisecondsInTheFuture);
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run() {
                for (Object index : idxs) {
                    try {
//                        System.out.println(index.getClass().getName() + " STARTED at " + new Date());
                        log.info(index.getClass().getName() + " STARTED at " + new Date());
                        if (cmd.equalsIgnoreCase("createIndexes")) {
                            //createIndex just does the initialization part, it does not actuall
                            Method method = index.getClass().getMethod("createIndex", new Class[]{boolean.class});
                            method.invoke(index, new Object[]{true});
                        }
//                        if (cmd.equalsIgnoreCase("refreshIndexes")) {
                            Method method = index.getClass().getMethod("createIndexAtRuntime", new Class[]{});
                            method.invoke(index, new Object[]{});
//                        }
//                        System.out.println(index.getClass().getName() + " FINISHED at " + new Date());
                        log.info(index.getClass().getName() + " FINISHED at " + new Date());
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    HibernateUtil.commitTransaction();
                    HibernateUtil.closeSession();
                } catch (InfrastructureException e) {
                    e.printStackTrace();
                }

            }
        }, timeToRun);
        request.setAttribute("luceneIndexesForm", form);
        form = null;
        return mapping.findForward("success");
    }

}
