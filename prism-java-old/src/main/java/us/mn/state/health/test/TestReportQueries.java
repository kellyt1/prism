package us.mn.state.health.test;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.receiving.ReceivingDetail;
import us.mn.state.health.persistence.HibernateUtil;

public class TestReportQueries extends TestCase {
    
    DAOFactory daoFactory;
    
    protected void setUp() {
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
    }

    protected void tearDown() throws InfrastructureException {
        HibernateUtil.commitTransaction();  //is this necessary when you're only reading?
        HibernateUtil.closeSession();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        /*
        suite.addTest(new TestReportQueries("testDeliveryReceipt"));
        */
        suite.addTest(new TestReportQueries("testToBeStockedReport"));

        return suite;
    }
    
    public void testToBeStockedReport() throws Exception {
        String startDate = "08/01/2005";
        String endDate = "10/31/2005";
        
        Collection results = daoFactory.getPurchasingOrderLineItemDAO()
                                       .findStockItemsToBeStocked("2037",
                                                                   DateUtils.createDate(startDate, DateUtils.DEFAULT_DATE_FORMAT), 
                                                                   DateUtils.createDate(endDate, DateUtils.DEFAULT_DATE_FORMAT));
        System.out.println("There are " + results.size() + " results");
        for(Iterator iter = results.iterator(); iter.hasNext(); ) {
            ReceivingDetail rd = (ReceivingDetail)iter.next();
            System.out.println("ReceivingDetailId: " + rd.getReceivingDetailId().longValue());
        }
    }

    public void testDeliveryReceipt() throws Exception {
        String startDate = "08/01/2005";
        String endDate = "10/31/2005";
        
        Collection results = daoFactory.getPurchasingOrderLineItemDAO()
                                       .findPurchaseItemsToBeDelivered("2037",
                                                                       DateUtils.createDate(startDate, DateUtils.DEFAULT_DATE_FORMAT), 
                                                                       DateUtils.createDate(endDate, DateUtils.DEFAULT_DATE_FORMAT));
        System.out.println("There are " + results.size() + " results");
        for(Iterator iter = results.iterator(); iter.hasNext(); ) {
            Request req = (Request)iter.next();
            System.out.println("RequestID: " + req.getRequestId().longValue());
        }
    }

    public TestReportQueries(String x) {
        super(x);
    }

    public static void main(String[] args) throws Exception {
        TestRunner.run(suite());
        //junit.swingui.TestRunner.run(us.mn.state.health.test.TestReportQueries.class);
    }
}
