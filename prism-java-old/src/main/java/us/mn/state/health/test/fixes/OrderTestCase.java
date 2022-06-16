package us.mn.state.health.test.fixes;

import java.util.Collection;

import junit.framework.TestCase;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class OrderTestCase extends TestCase {
    private String user;
    private HibernateDAO hibernateDAO;
    private DAOFactory daoFactory;
    private String environment;

    protected void setUp() {
        environment = Constants.DEVDB;
//        environment = Constants.TEST;
//            environment = Constants.PROD;
        System.setProperty(Constants.ENV_KEY, environment);

        user = "ochial1";
        hibernateDAO = new HibernateDAO();
        daoFactory = new HibernateDAOFactory();

    }

    public void testEagerLoadOrder() throws InfrastructureException {
        Long orderId = new Long(276742);
        String hql = "from Order o " +
                "left join fetch o.vendor v " +
                "left join fetch v.externalOrgDetail e " +
                "left join fetch o.billToAddress b " +
                "left join fetch o.vendorAddress va " +
                "left join fetch o.shipToAddress sa " +
                "left join fetch o.purchaser p " +
                "where o.orderId = "+ orderId;
        Collection result = hibernateDAO.executeQuery(hql);
        result.size();
    }

    protected void tearDown() {

    }
}