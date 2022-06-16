package us.mn.state.health.test;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.materialsrequest.HibernateRequestLineItemDAO;
import us.mn.state.health.dao.materialsrequest.RequestLineItemDAO;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.util.search.RequestLineItemIndex;
import us.mn.state.health.persistence.HibernateUtil;

public class TestRequestLineItemIndex extends TestCase {
    private RequestLineItemDAO rliDAO;

    protected void setUp() {
        rliDAO = new HibernateRequestLineItemDAO();
    }

    protected void tearDown() throws InfrastructureException {
        HibernateUtil.commitTransaction();  //is this necessary when you're only reading?
        HibernateUtil.closeSession();
        rliDAO = null;
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        /*
        suite.addTest(new TestRequestLineItemIndex("testCreateIndexFromScratch"));
        */
        suite.addTest(new TestRequestLineItemIndex("testSearch"));

        return suite;
    }
    
    public TestRequestLineItemIndex(String x) {
        super(x);
    }

    public static void main(String[] args) throws Exception {
        TestRunner.run(suite());
        //junit.swingui.TestRunner.run(us.mn.state.health.test.TestRequestLineItemIndex.class);
    }
     
    public void testCreateIndexFromScratch() throws InfrastructureException {
        RequestLineItemIndex rliIndex = new RequestLineItemIndex();
    }
    
    public void testSearch() throws InfrastructureException {
        RequestLineItemIndex rliIndex = new RequestLineItemIndex();
        Collection results = rliIndex.search("Approval");
        TestCase.assertNotNull(results);
        System.out.println("SearchResults:");
        Iterator iter = results.iterator();
        while(iter.hasNext()){
            RequestLineItem rli = (RequestLineItem)iter.next();
            
            System.out.println(rli.toString());
        }
    }
}
