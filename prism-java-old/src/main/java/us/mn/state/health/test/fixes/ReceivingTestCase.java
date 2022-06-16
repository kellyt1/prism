package us.mn.state.health.test.fixes;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.purchasing.OrderLineItem;

public class ReceivingTestCase extends TestCase {
    private String user;
    private HibernateDAO hibernateDAO;
    private DAOFactory daoFactory;
    private String environment;

    protected void setUp() throws Exception {
        super.setUp();
//        environment = Constants.DEVDB;
        environment = Constants.TEST;
//        environment = Constants.PROD;
        System.setProperty(Constants.ENV_KEY, environment);

        user = "ochial1";
        hibernateDAO = new HibernateDAO();
        daoFactory = new HibernateDAOFactory();

    }

    public void testUpdateReceivedRequestLineItems() throws InfrastructureException {
        String hql = "select oli from OrderLineItem oli join oli.requestLineItems rli where rli.status.statusCode<>oli.status.statusCode and oli.status.statusCode in ('RCD','RCP')";
        Collection olis = hibernateDAO.executeQuery(hql);
        System.out.println("Size="+olis.size());
        Status oliRCD = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_ORDER,
                        Status.RECEIVED);
        Status oliRCP = daoFactory.getStatusDAO()
                        .findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_ORDER, Status.RECEIVED_PARTIAL);
        Status rliRCD = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST,
                        Status.RECEIVED);
        Status rliRCP = daoFactory.getStatusDAO()
                        .findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.RECEIVED_PARTIAL);

        for (Iterator iterator = olis.iterator(); iterator.hasNext();) {
            OrderLineItem orderLineItem = (OrderLineItem) iterator.next();
            if(orderLineItem.getStatus().getStatusCode().equals(oliRCD.getStatusCode())){
                for (Iterator iterator1 = orderLineItem.getRequestLineItems().iterator(); iterator1.hasNext();) {
                    RequestLineItem requestLineItem = (RequestLineItem) iterator1.next();
                    requestLineItem.setStatus(rliRCD);
                }
            }

            if(orderLineItem.getStatus().getStatusCode().equals(oliRCP.getStatusCode())){
                for (Iterator iterator1 = orderLineItem.getRequestLineItems().iterator(); iterator1.hasNext();) {
                    RequestLineItem requestLineItem = (RequestLineItem) iterator1.next();
                    requestLineItem.setStatus(rliRCP);
                }
            }
            daoFactory.getPurchasingOrderLineItemDAO().makePersistent(orderLineItem);
        }

    }

    protected void tearDown() throws InfrastructureException {
//        HibernateUtil.commitTransaction();
//        HibernateUtil.closeSession();
    }
}