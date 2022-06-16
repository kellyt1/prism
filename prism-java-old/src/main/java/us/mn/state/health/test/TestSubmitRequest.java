package us.mn.state.health.test;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.businesslogic.MaterialsRequestLogic;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Facility;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Priority;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.PurchaseItem;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.materialsrequest.DeliveryDetail;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource;
import us.mn.state.health.persistence.HibernateUtil;

public class TestSubmitRequest extends TestCase {
    private String environment;
    private DAOFactory daoFactory;
    
    public TestSubmitRequest(String testName) {
        super(testName);
    }
    
    public void setUp() throws Exception  {         
        /* Create a data source */
        environment = Constants.DEVDB;
        System.setProperty(Constants.ENV_KEY, environment);
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
    }
    
    protected void tearDown() throws Exception {
        HibernateUtil.commitTransaction();  //is this necessary when you're only reading?
        HibernateUtil.closeSession();
    }
    
    public static void main(String[] args) throws Exception {
        TestRunner.run(suite());
        //junit.swingui.TestRunner.run(us.mn.state.health.test.TestStatus.class);
    }

     public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSubmitRequest("testSubmitStockRequest"));
        suite.addTest(new TestSubmitRequest("testSubmitNonStockRequest"));
        return suite;
    }
    
    /**
     * This method should test submission of a materials request for a stock item.
     * (or several stock items).  Create a request with 1 or more lines for stock items.
     * 1) Make sure the status on each line is set appropriately (should be 
     *    WAITING FOR DISPERSAL). 
     * 2) Make sure there is a Delivery Detail associated with the request.
     * 3) After the request is submitted, make sure it has a tracking number 
     * 4) Make sure that the request was definitely saved - we should be able to
     *    read back the entire request (all RLI's should be present). 
     * 5) Make sure that the Stock Items' currentDemand property accurately reflects the 
     *    new request (compare what it was previous to submitting the request, and then make
     *    sure that it was incremented according to the qty requested). 
     * 6) Make sure there is a Requestor associated with the request
     * 7) Make sure there is a date requested value.  
     * 8) 
     * @throws java.lang.Exception
     */
    public void testSubmitStockRequest() throws Exception {
        Request request = buildStockRequest(2);
        MaterialsRequestLogic.submitMaterialsRequest(request);
        Long requestId = request.getRequestId();
        
        Request savedReq = daoFactory.getRequestDAO().getRequestById(requestId, false);
        //test for condition #4 above
        this.assertNotNull(savedReq);
        this.assertTrue(savedReq.getRequestLineItems().size() > 0);
        
        //test for condition #1 above
        for(Iterator iter = savedReq.getRequestLineItems().iterator(); iter.hasNext(); ) {
            RequestLineItem rli = (RequestLineItem)iter.next();
            this.assertEquals(rli.getStatus().getStatusCode(), Status.WAITING_FOR_DISPERSAL);
        }
        
        //test for condition #2 above
        this.assertNotNull(savedReq.getDeliveryDetail());
        
        //test for condition #3 above
        this.assertNotNull(savedReq.getTrackingNumber());
        
        //test for condition #6 above
        this.assertNotNull(savedReq.getRequestor());
        
        //test for condition #7 above
        this.assertNotNull(savedReq.getDateRequested());
    }
    
    /**
     * This method should test submission of a materials request for 
     * non-stock catalog items. Create a request with 1 or more non-stock
     * catalog items.
     * 1) Make sure the status on each line is set appropriately 
     * 2) Make sure there is a Delivery Detail associated with the request.
     * 3) After the request is submitted, make sure it has a tracking number 
     * 4) Make sure that the request was definitely saved - we should be able to
     *    read back the entire request (all RLI's should be present). 
     * 5) Make sure that the Stock Items' currentDemand property accurately reflects the 
     *    new request (compare what it was previous to submitting the request, and then make
     *    sure that it was incremented according to the qty requested). 
     * 6) Make sure there is a Requestor associated with the request
     * 7) Make sure there is a date requested value. 
     * @throws java.lang.Exception
     */
    public void testSubmitNonStockRequest() throws Exception {
        Request request = new Request();
        this.setUpRequest(request);
        int quantity = 5;
        
        //add a computer item RLI
        PurchaseItem computerItem = 
            (PurchaseItem)daoFactory.getPurchaseItemDAO().findByCategoryCode(Category.MATERIALS_COMPUTER_EQUIPMENT).iterator().next();
        RequestLineItem rli_1 = this.createRLI(computerItem, quantity);
        addFundingSource(rli_1, "1601", new Double(computerItem.getDispenseUnitCost().doubleValue() * quantity));
        request.addRequestLineItem(rli_1);
        
        //add a miscellaneous item RLI
        PurchaseItem miscItem = 
            (PurchaseItem)daoFactory.getPurchaseItemDAO().findByCategoryCode(Category.MATERIALS_MISCELLANEOUS).iterator().next();
        RequestLineItem rli_2 = this.createRLI(miscItem, quantity);
        addFundingSource(rli_2, "1602", new Double(miscItem.getDispenseUnitCost().doubleValue() * quantity));
        request.addRequestLineItem(rli_2);
        
        //run the Request through the business logic
        MaterialsRequestLogic.submitMaterialsRequest(request);
        
        //now for the testing!
        Long requestId = request.getRequestId();
        this.assertNotNull(requestId);
        
        Request savedReq = daoFactory.getRequestDAO().getRequestById(requestId, false);      
        this.assertNotNull(savedReq);
        this.assertTrue(savedReq.getRequestLineItems().size() > 0);
    }
    
    /**
     * This method should test submission of a materials request for 
     * non-catalog items. Create a request with 1 or more non-catalog
     * (write-in) items.
     * 1) 
     * @throws java.lang.Exception
     */
    public void testSubmitNonCatalogItemRequest() throws Exception {
        
    }   
    
    /**
     * This method should test submission of a materials request for all 3 types
     * of items: non-stock catalog items, stock items, and purchase items. 
     * 1) 
     * @throws java.lang.Exception
     */
    public void testSubmitMixedRequest() throws Exception {
        
    }
    
    
/////////////////////////////////////////////////////////////////////////////////////////////////
//////// HELPER METHODS //////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
    private Request buildStockRequest(int numOfLineItems) throws Exception {
        Collection stockItems = daoFactory.getStockItemDAO().findAll();
        this.assertNotNull(stockItems);
        Iterator iter = stockItems.iterator();
        Request request = new Request();
        for(int i = 0; i < numOfLineItems; i++) {
            StockItem si = (StockItem)iter.next();
            RequestLineItem rli = createRLI(si, 5);
            request.addRequestLineItem(rli);
        }    
        
        setUpRequest(request);
        
        return request;
    }
    
    private void setUpRequest(Request request) throws Exception {
        request.setAdditionalInstructions("TestSubmitRequest jUnit test");
        request.setDateRequested(new Date());
        request.setNeedByDate(new Date());
        
        Priority normal = daoFactory.getPriorityDAO().findByPriorityCode(Priority.NORMAL);
        request.setPriority(normal);
        
        Person requestor = daoFactory.getPersonDAO().getPersonById(new Long(17113), false);
        request.setRequestor(requestor);
        
        DeliveryDetail deliveryDetail = new DeliveryDetail();
        MailingAddress ma = requestor.getPrimaryMailingAddress();
        Facility fac = requestor.getPrimaryFacility();
        deliveryDetail.setRecipient(requestor);
        deliveryDetail.setFacility(fac);
        deliveryDetail.setMailingAddress(fac.getBuildingMailingAddress());        
        request.setDeliveryDetail(deliveryDetail);
    }
    
    private RequestLineItem createRLI(Item item, int qty) {
        RequestLineItem rli = new RequestLineItem();
        rli.setItem(item);
        rli.setQuantity(new Integer(qty));
        return rli;
    }
 
    private void addFundingSource(RequestLineItem rli, String orgCode, Double amount) throws Exception {
        OrgBudget org = daoFactory.getOrgBudgetDAO().findByOrgBudgetCode(orgCode);
        RequestLineItemFundingSource fndSrc = new RequestLineItemFundingSource(org, amount, rli);
        rli.addFundingSource(fndSrc);
    }
}
