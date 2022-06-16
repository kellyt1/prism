package us.mn.state.health.test;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.materialsrequest.HibernateRequestDAO;
import us.mn.state.health.dao.materialsrequest.RequestDAO;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.util.search.RequestIndex;
import us.mn.state.health.persistence.HibernateUtil;

public class TestRequestIndex extends TestCase {
    private RequestDAO requestDAO;

    protected void setUp() {
        requestDAO = new HibernateRequestDAO();
    }

    protected void tearDown() throws InfrastructureException {
        HibernateUtil.commitTransaction();  //is this necessary when you're only reading?
        HibernateUtil.closeSession();
        requestDAO = null;
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        /*
        suite.addTest(new TestRequestIndex("testCreateIndexFromScratch"));
        suite.addTest(new TestRequestIndex("testSearch"));
        */
        suite.addTest(new TestRequestIndex("testCreateIndexFromScratch"));
        suite.addTest(new TestRequestIndex("testSearch"));
        
        return suite;
    }
    
    public TestRequestIndex(String x) {
        super(x);
    }

    public static void main(String[] args) throws Exception {
        TestRunner.run(suite());
        //junit.swingui.TestRunner.run(us.mn.state.health.test.TestRequestIndex.class);
    }
     
    public void testCreateIndexFromScratch() throws InfrastructureException {
        RequestIndex reqIndex = new RequestIndex();
    }
    
    public void testSearch() throws InfrastructureException {
        RequestIndex reqIndex = new RequestIndex();
        Collection results = reqIndex.search("Stull");
        TestCase.assertNotNull(results);
        System.out.println("SearchResults:");
        Iterator iter = results.iterator();
        while(iter.hasNext()){
            Request req = (Request)iter.next();            
            System.out.println(req.toString());
        }
    }
}
