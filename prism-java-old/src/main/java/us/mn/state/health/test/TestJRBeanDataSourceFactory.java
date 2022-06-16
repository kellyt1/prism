package us.mn.state.health.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import us.mn.state.health.builder.materialsrequest.RequestFormBuilder;
import us.mn.state.health.common.report.JRBeanDataSourceFactory;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.dao.materialsrequest.RequestDAO;
import us.mn.state.health.matmgmt.director.RequestFormDirector;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.view.materialsrequest.RequestForm;

public class TestJRBeanDataSourceFactory  extends JRBeanDataSourceFactory {
     public TestJRBeanDataSourceFactory() {
        try {
            JRBeanDataSourceFactory factory  = new JRBeanDataSourceFactory();
            DAOFactory daoFactory = new HibernateDAOFactory();
            RequestDAO requestDAO = daoFactory.getRequestDAO();
            Collection requests = requestDAO.findByStatusCode(Status.WAITING_FOR_DISPERSAL, null);
            Collection requestForms = new ArrayList();
            Collection rliFormsAll = new ArrayList();
            Iterator iter = requests.iterator();
            while(iter.hasNext()) {
               Request request = (Request)iter.next();
               RequestForm form = new RequestForm();
               RequestFormBuilder builder = new RequestFormBuilder(form, daoFactory, request);
               RequestFormDirector director = new RequestFormDirector(builder);
               director.constructForStockClerk();
               requestForms.add(form);
               rliFormsAll.addAll(form.getRequestLineItemForms());
            }
            //super.setBeans(requestForms);
            super.setBeans(rliFormsAll);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        TestJRBeanDataSourceFactory factory = new TestJRBeanDataSourceFactory();  
        System.out.println(factory.getBeans().size());
    }
}