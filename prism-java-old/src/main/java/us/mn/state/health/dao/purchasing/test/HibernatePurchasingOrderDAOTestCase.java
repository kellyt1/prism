package us.mn.state.health.dao.purchasing.test;
import junit.framework.TestCase;
import us.mn.state.health.dao.purchasing.HibernatePurchasingOrderDAO;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernatePurchasingOrderDAOTestCase extends TestCase  {
    HibernatePurchasingOrderDAO dao = new HibernatePurchasingOrderDAO();
    public HibernatePurchasingOrderDAOTestCase(String sTestName) {
        super(sTestName);
        try{HibernateUtil.commitTransaction();}
        catch(Exception e) {e.printStackTrace();}
    }

    /**
     * void makeTransient(Object)
     */
    public void testmakeTransient() throws Exception {
        Object order1 = dao.loadById(new Long(147837), Order.class, true);
        dao.makeTransient(order1);
        Object order2 = dao.loadById(new Long(147865), Order.class, true);
        dao.makeTransient(order2);
    }

    /**
     * void makePersistent(Object)
     */
    public void testmakePersistent() {
    }

    /**
     * Object getById(Long, Class, boolean)
     */
    public void testgetById() {
    }

    /**
     * Collection findAll(Class)
     */
    public void testfindAll() {
    }

    /**
     * Collection executeSqlQuery(String)
     */
    public void testexecuteSqlQuery() {
    }

    /**
     * Collection executeNamedQuery(String)
     */
    public void testexecuteNamedQuery() {
    }

    /**
     * Collection executeCriteriaQuery(Class)
     */
    public void testexecuteCriteriaQuery() {
    }

    /**
     * void addCriterion(Criterion)
     */
    public void testaddCriterion() {
    }

    /**
     * void addQueryParam(String, Object)
     */
    public void testaddQueryParam() {
    }
}