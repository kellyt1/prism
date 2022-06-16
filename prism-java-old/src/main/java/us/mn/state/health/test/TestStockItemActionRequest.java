package us.mn.state.health.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.dao.HibernatePersonDAO;
import us.mn.state.health.dao.HibernateStatusDAO;
import us.mn.state.health.dao.HibernateUserDAO;
import us.mn.state.health.dao.PersonDAO;
import us.mn.state.health.dao.StatusDAO;
import us.mn.state.health.dao.UserDAO;
import us.mn.state.health.dao.inventory.ActionRequestTypeDAO;
import us.mn.state.health.dao.inventory.HibernateActionRequestTypeDAO;
import us.mn.state.health.dao.inventory.HibernateStockItemActionRequestDAO;
import us.mn.state.health.dao.inventory.HibernateStockItemDAO;
import us.mn.state.health.dao.inventory.StockItemActionRequestDAO;
import us.mn.state.health.dao.inventory.StockItemDAO;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.inventory.ActionRequestType;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.StockItemActionRequest;
import us.mn.state.health.persistence.HibernateUtil;

public class TestStockItemActionRequest extends TestCase {

    private StockItemActionRequestDAO stockItemActionRequestDAO;
    private StockItemDAO stockItemDAO;
    private StockItemActionRequest stockItemActionRequest;
    private ActionRequestTypeDAO actionRequestTypeDAO;
    private PersonDAO personDAO;
    private Person person;
    private StatusDAO statusDAO;
    private UserDAO userDAO;
    
    public static Test suite() {
        TestSuite suite = new TestSuite();  
        
        /*
        suite.addTest(new TestStockItemActionRequest("testNewStockItemRequest"));  
        suite.addTest(new TestStockItemActionRequest("testApproveNewStockItemRequest"));
        suite.addTest(new TestStockItemActionRequest("testPutStockItemOnHold"));
        suite.addTest(new TestStockItemActionRequest("testApproveStockItemHoldRequest"));
        suite.addTest(new TestStockItemActionRequest("testInactivateStockItem"));        
        suite.addTest(new TestStockItemActionRequest("testReactivateStockItem"));
        suite.addTest(new TestStockItemActionRequest("testApproveReactivationRequest"));
        suite.addTest(new TestStockItemActionRequest("testTrivialStockItemModification"));
        suite.addTest(new TestStockItemActionRequest("testDenyRequest"));
        */
        suite.addTest(new TestStockItemActionRequest("testStockItemReflection"));
        
        return suite;
    }

    protected void setUp() throws Exception {
        stockItemActionRequestDAO = new HibernateStockItemActionRequestDAO();
        stockItemDAO = new HibernateStockItemDAO();
        actionRequestTypeDAO = new HibernateActionRequestTypeDAO();
        stockItemActionRequest = new StockItemActionRequest();
        personDAO = new HibernatePersonDAO();
        userDAO = new HibernateUserDAO();
        person = (Person) userDAO.findUserByUsername("flahas1");
        statusDAO = new HibernateStatusDAO();
    }

    protected void tearDown() throws Exception {
        HibernateUtil.commitTransaction();  
        HibernateUtil.closeSession();
        stockItemActionRequestDAO = null;
        stockItemDAO = null;
        actionRequestTypeDAO = null;
        stockItemActionRequest = null;
        personDAO = null;
    }
    
    public void testStockItemReflection() throws Exception {
        StockItem si = stockItemDAO.getStockItemById(new Long(163397), false);
        StockItem si2 = new StockItem();
        si2.copyProperties(si);
        si2.setDescription("piss up a rope");
        
        System.out.println("si1 desc: " + si.getDescription());
        System.out.println("si2 desc: " + si2.getDescription());
        
        Field[] theFields = StockItem.class.getDeclaredFields();
        Field[] superFields = StockItem.class.getSuperclass().getDeclaredFields();
        ArrayList list = new ArrayList();
        list.addAll(Arrays.asList(theFields));
        list.addAll(Arrays.asList(superFields));
        
        for (int i = 0; i < list.size(); i++) {
             Field field = (Field)list.get(i);
             String fieldName = field.getName();
             //System.out.println("Name: " + fieldName); 
             Object value1 = PropertyUtils.invokeReadMethodWithPropertyName(si, fieldName);
             Object value2 = PropertyUtils.invokeReadMethodWithPropertyName(si2, fieldName);
             if(value1 != null && !value1.equals(value2)) {
                System.out.println(fieldName + " property was NOT equal: " + PropertyUtils.invokeReadMethodWithPropertyName(si, fieldName).toString());
                System.out.println(value1);
                System.out.println(value2);
            }                      
        }
    }

    public void testNewStockItemRequest() throws Exception {
        stockItemActionRequest = constructNewStockItemActionRequest();
       
        stockItemActionRequest.executeBusinessRules();

        assertTrue(stockItemActionRequest.getStatus().getStatusCode()
                    .equalsIgnoreCase(Status.WAITING_FOR_APPROVAL));
        assertTrue(stockItemActionRequest.getPotentialStockItem().getStatus().getStatusCode()
                    .equalsIgnoreCase(Status.INACTIVE));
        assertTrue(stockItemActionRequest.getPotentialStockItem().getPotentialIndicator().booleanValue());
        assertTrue(stockItemActionRequest.getStockItem() == null);
        assertTrue(stockItemActionRequest.getPotentialStockItem().getIcnbr() == null);
        
        HibernateUtil.commitTransaction();  
        HibernateUtil.closeSession();
    }

    public void testPutStockItemOnHold() throws Exception {
        stockItemActionRequest = constructPutOnHoldActionRequest();
        
        StockItem original = stockItemActionRequest.getStockItem();
        StockItem potential = stockItemActionRequest.getPotentialStockItem();
        
        //everything should match between the potential stock item and the original stock item EXCEPT the status and potentialInd should be Y on the potential stock item
        assertTrue(original.getAnnualUsage().intValue() == potential.getAnnualUsage().intValue());
        assertEquals(original.getDispenseUnitCost(), potential.getDispenseUnitCost());
        assertEquals(original.getCategory().getName(), potential.getCategory().getName());
        assertTrue(original.getDescription().equalsIgnoreCase(potential.getDescription()));
        assertEquals(original.getIcnbr(), potential.getIcnbr());
        assertEquals(original.getOrgBudget(), potential.getOrgBudget());
        assertFalse(original.getPotentialIndicator().booleanValue());
        assertTrue(potential.getPotentialIndicator().booleanValue());
        assertTrue(original.getPrimaryContact().equals(potential.getPrimaryContact()));
        assertEquals(original.getQtyOnHand(), potential.getQtyOnHand());
        assertFalse(original.getStatus().getStatusCode().equalsIgnoreCase(potential.getStatus().getStatusCode()));
        assertTrue(potential.getStatus().getStatusCode().equalsIgnoreCase(Status.ONHOLD));
        assertEquals(original.getSeasonal(), potential.getSeasonal());
        assertEquals(original.getSecondaryContact(), potential.getSecondaryContact());
        
        //everything OK, so go ahead and submit it.
        stockItemActionRequest.executeBusinessRules();
        
        
        assertEquals(stockItemActionRequest.getStatus().getStatusCode(), 
                    Status.WAITING_FOR_APPROVAL);  
        assertEquals(stockItemActionRequest.getPotentialStockItem().getStatus().getStatusCode(),
                     Status.ONHOLD);
        assertEquals(stockItemActionRequest.getStockItem().getStatus().getStatusCode(),
                     Status.ACTIVE);
        assertTrue(stockItemActionRequest.getPotentialStockItem().getPotentialIndicator().booleanValue());
        
        HibernateUtil.commitTransaction();  
        HibernateUtil.closeSession();
    }
    
    public void testReactivateStockItem() throws Exception {
        stockItemActionRequest = constructReactivateActionRequest();
         
        StockItem original = stockItemActionRequest.getStockItem();
        StockItem potential = stockItemActionRequest.getPotentialStockItem();
        
        //everything should match between the potential stock item and the original stock item EXCEPT the status and potentialInd should be Y on the potential stock item
        assertTrue(original.getAnnualUsage().intValue() == potential.getAnnualUsage().intValue());
        assertEquals(original.getDispenseUnitCost(), potential.getDispenseUnitCost());
        assertEquals(original.getCategory().getName(), potential.getCategory().getName());
        assertTrue(original.getDescription().equalsIgnoreCase(potential.getDescription()));
        assertEquals(original.getIcnbr(), potential.getIcnbr());
        assertEquals(original.getOrgBudget(), potential.getOrgBudget());
        assertFalse(original.getPotentialIndicator().booleanValue());
        assertTrue(potential.getPotentialIndicator().booleanValue());
        
        
        assertTrue(original.getPrimaryContact().equals(potential.getPrimaryContact()));
        assertEquals(original.getQtyOnHand(), potential.getQtyOnHand());
        assertFalse(original.getStatus().getStatusCode().equalsIgnoreCase(potential.getStatus().getStatusCode()));
        assertTrue(potential.getStatus().getStatusCode().equalsIgnoreCase(Status.ACTIVE));
        assertEquals(original.getSeasonal(), potential.getSeasonal());
        assertEquals(original.getSecondaryContact(), potential.getSecondaryContact());
        
        //everything OK, so go ahead and submit it.
        stockItemActionRequest.executeBusinessRules();
        
        assertEquals(stockItemActionRequest.getStatus().getStatusCode(), 
                    Status.WAITING_FOR_APPROVAL);  
        assertEquals(stockItemActionRequest.getPotentialStockItem().getStatus().getStatusCode(),
                     Status.ACTIVE);
        assertEquals(stockItemActionRequest.getStockItem().getStatus().getStatusCode(),
                     Status.INACTIVE);
        assertTrue(stockItemActionRequest.getPotentialStockItem().getPotentialIndicator().booleanValue());
        
        HibernateUtil.commitTransaction();  
        HibernateUtil.closeSession();
    }
    
    public void testInactivateStockItem() throws Exception {
        stockItemActionRequest = constructInactivateActionRequest();
        
        //make sure the 2 stock items have different status's and that potential's status is INACTIVE
        assertFalse(stockItemActionRequest.getStockItem().getStatus().getStatusCode()
                    .equalsIgnoreCase(stockItemActionRequest.getPotentialStockItem().getStatus().getStatusCode()));
        assertTrue(stockItemActionRequest.getPotentialStockItem().getStatus().getStatusCode()
                    .equalsIgnoreCase(Status.INACTIVE));
                    
        stockItemActionRequest.executeBusinessRules();
        
        assertEquals(stockItemActionRequest.getStatus().getStatusCode(), 
                    Status.APPROVED);
        assertEquals(stockItemActionRequest.getStockItem().getStatus().getStatusCode(), 
                    Status.INACTIVE);
        assertNull(stockItemActionRequest.getPotentialStockItem());        
        assertFalse(stockItemActionRequest.getStockItem().getPotentialIndicator().booleanValue());
        
        HibernateUtil.commitTransaction();  
        HibernateUtil.closeSession();
    }
    

    public void testTrivialStockItemModification() throws Exception {
        ActionRequestType modifyActionRequestType =
                (ActionRequestType)actionRequestTypeDAO.findByActionRequestTypeCode(ActionRequestType.STOCK_ITEM_MODIFICATIONS);
        
        stockItemActionRequest = new StockItemActionRequest();
        
        //StockItem originalStockItem = 
        //        (StockItem)stockItemDAO.findByStatusCode(Status.ACTIVE).iterator().next();
        StockItem originalStockItem =(StockItem)stockItemDAO.getStockItemById(new Long(47597), false);
        long originalID = originalStockItem.getItemId().longValue();
        
        String newCycleCount = "B";
        String newDesctiption = "Hey i think its working";
        int newAnnualUsage = 512;
        double newDiscount = 0.5678;
        
        StockItem potentialStockItem = new StockItem();  //make a copy for our test's sake - in real world, we'd create a brand new StockItem
        potentialStockItem.copyProperties(originalStockItem);
        potentialStockItem.setAnnualUsage(new Integer(newAnnualUsage));
        potentialStockItem.setDescription(newDesctiption);
        potentialStockItem.setCycleCountPriority(newCycleCount);
        potentialStockItem.setEconomicOrderQty(new Integer(125));
        potentialStockItem.setFillUntilDepleted(Boolean.TRUE);
        potentialStockItem.setHazardous(Boolean.TRUE);
        potentialStockItem.setHoldUntilDate(new Date());
        //potentialStockItem.setIcnbr(new Integer(12345));
        potentialStockItem.setLastUpdatedBy("flahas1");
        potentialStockItem.setLastUpdatedDate(new Date());
        potentialStockItem.setPotentialIndicator(Boolean.TRUE);
        potentialStockItem.setPrintFileURL("http://lucianopoverati.com");  
        potentialStockItem.setPrimaryContact(person);
        
        //set up only the properties that would be set before hitting the rules engine
        stockItemActionRequest.setActionRequestType(modifyActionRequestType);
        stockItemActionRequest.setRequestedDate(new Date());
        stockItemActionRequest.setRequestReason("Modify a stock item #" + originalID);
        stockItemActionRequest.setSpecialInstructions("Modify a stock item #" + originalID);
        stockItemActionRequest.setRequestor(person);
        stockItemActionRequest.setStockItem(originalStockItem);
        stockItemActionRequest.setPotentialStockItem(potentialStockItem);
        
        assertEquals(originalStockItem.getStatus().getStatusCode(),
                     potentialStockItem.getStatus().getStatusCode());
                  
        stockItemActionRequest.executeBusinessRules();
        //HibernateUtil.getSession().flush();
        HibernateUtil.commitTransaction(); 
        HibernateUtil.closeSession();
        
        StockItem modifiedOriginal = (StockItem) stockItemDAO.getStockItemById(new Long(originalID), false);
        
        assertEquals(modifiedOriginal.getAnnualUsage().intValue(), newAnnualUsage);
        assertEquals(modifiedOriginal.getCycleCountPriority(), newCycleCount);
        assertEquals(modifiedOriginal.getDescription(), newDesctiption);
        
        HibernateUtil.commitTransaction();  
        HibernateUtil.closeSession();
    }
  

    public void testApproveNewStockItemRequest() throws Exception {
       Iterator iter = 
            (Iterator)stockItemActionRequestDAO
                                    .findSIARByActionRequestTypeCodeAndStatusCode(ActionRequestType.NEW_STOCK_ITEM,
                                                                                  Status.WAITING_FOR_APPROVAL)
                                    .iterator();
        
        stockItemActionRequest = (StockItemActionRequest)iter.next();
       
        assertNotNull(stockItemActionRequest);
        
        assertTrue(stockItemActionRequest.getActionRequestType().getCode().equalsIgnoreCase(ActionRequestType.NEW_STOCK_ITEM));
        assertNull(stockItemActionRequest.getStockItem());                      //a NEW SIAR should only have a potentialStockItem
        assertNotNull(stockItemActionRequest.getPotentialStockItem());
        assertNull(stockItemActionRequest.getPotentialStockItem().getIcnbr());  //potential should not have an icnbr yet
        assertTrue(stockItemActionRequest.getPotentialStockItem().getPotentialIndicator().booleanValue());
        
        stockItemActionRequest.approve();
        
        assertTrue(stockItemActionRequest.getStatus().getStatusCode().equalsIgnoreCase(Status.APPROVED));
        assertNotNull(stockItemActionRequest.getPotentialStockItem().getIcnbr());   //approving it should've assigned an ICNBR 
        assertFalse(stockItemActionRequest.getPotentialStockItem().getPotentialIndicator().booleanValue()); //make sure the potential indicator is set to FALSE
    
        HibernateUtil.commitTransaction();  
        HibernateUtil.closeSession();
    }

    public void testApproveStockItemHoldRequest() throws Exception {
        Iterator iter = 
            (Iterator)stockItemActionRequestDAO
                                    .findSIARByActionRequestTypeCodeAndStatusCode(ActionRequestType.STOCK_ITEM_MODIFICATIONS,
                                                                                  Status.WAITING_FOR_APPROVAL)
                                    .iterator();
        
        //make sure we get an SIAR for putting an item On Hold
        while(iter.hasNext()){
            stockItemActionRequest = (StockItemActionRequest)iter.next(); 
            boolean potentialIsOnHold = stockItemActionRequest.getPotentialStockItem().getStatus().getStatusCode()
                                                .equalsIgnoreCase(Status.ONHOLD);
            boolean originalIsOnHold = stockItemActionRequest.getStockItem().getStatus().getStatusCode()
                                                .equalsIgnoreCase(Status.ONHOLD);
                                                
            if(potentialIsOnHold && !originalIsOnHold){
                break;
            }
            stockItemActionRequest = null;
        }
        
        assertNotNull(stockItemActionRequest);
        assertNotNull(stockItemActionRequest.getStockItem());
        assertNotNull(stockItemActionRequest.getPotentialStockItem());
        assertTrue(stockItemActionRequest.getActionRequestType()
                                         .getCode()
                                         .equalsIgnoreCase(ActionRequestType.STOCK_ITEM_MODIFICATIONS));        

        Status approvedStatus = 
            (Status) statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM_ACTION_REQUEST,
                                                                 Status.APPROVED);

        //make sure the status's of the 2 stock items are not the same, and that the potential one's status is ONHOLD
        assertFalse(stockItemActionRequest.getStockItem().getStatus().getStatusCode()
                    .equalsIgnoreCase(stockItemActionRequest.getPotentialStockItem().getStatus().getStatusCode()));
        assertTrue(stockItemActionRequest.getPotentialStockItem().getStatus().getStatusCode()
                    .equalsIgnoreCase(Status.ONHOLD));
        
        //approve the request, and therefore apply the changes to original stock item and SAVES everything
        stockItemActionRequest.approve();
        
        assertEquals(stockItemActionRequest.getStatus(), approvedStatus);
        assertNull(stockItemActionRequest.getPotentialStockItem());
        assertTrue(stockItemActionRequest.getStockItem().getStatus().getStatusCode()
                    .equalsIgnoreCase(Status.ONHOLD));
        assertFalse(stockItemActionRequest.getStockItem().getPotentialIndicator().booleanValue());
        
        HibernateUtil.commitTransaction();  
        HibernateUtil.closeSession();
    }
    
    public void testApproveReactivationRequest() throws Exception {
        Iterator iter = 
            (Iterator)stockItemActionRequestDAO
                                    .findSIARByActionRequestTypeCodeAndStatusCode(ActionRequestType.STOCK_ITEM_MODIFICATIONS,
                                                                                  Status.WAITING_FOR_APPROVAL)
                                    .iterator();
        
        //make sure we get an SIAR for reactivating an item
        while(iter.hasNext()){
            stockItemActionRequest = (StockItemActionRequest)iter.next(); 
            boolean potentialIsActive = stockItemActionRequest.getPotentialStockItem().getStatus().getStatusCode()
                                                .equalsIgnoreCase(Status.ACTIVE);
            boolean originalIsActive = stockItemActionRequest.getStockItem().getStatus().getStatusCode()
                                                .equalsIgnoreCase(Status.ACTIVE);
                                                
            if(potentialIsActive && !originalIsActive){
                break;
            }
            stockItemActionRequest = null;
        }
        
        assertNotNull(stockItemActionRequest);
        assertNotNull(stockItemActionRequest.getStockItem());
        assertNotNull(stockItemActionRequest.getPotentialStockItem());
        assertTrue(stockItemActionRequest.getActionRequestType()
                                         .getCode()
                                         .equalsIgnoreCase(ActionRequestType.STOCK_ITEM_MODIFICATIONS));        

        Status approvedStatus = 
            (Status) statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM_ACTION_REQUEST,
                                                                 Status.APPROVED);

        //make sure the status's of the 2 stock items are not the same, and that the potential one's status is ACTIVE
        assertFalse(stockItemActionRequest.getStockItem().getStatus().getStatusCode()
                    .equalsIgnoreCase(stockItemActionRequest.getPotentialStockItem().getStatus().getStatusCode()));
        assertTrue(stockItemActionRequest.getPotentialStockItem().getStatus().getStatusCode()
                    .equalsIgnoreCase(Status.ACTIVE));
        
        //approve the request, and therefore apply the changes to original stock item and SAVES everything
        stockItemActionRequest.approve();
        
        //make sure the SIAR's status is approved and the stock item's status is ACTIVE, and it's potentialInd is FALSE
        assertEquals(stockItemActionRequest.getStatus(), approvedStatus);
        assertNull(stockItemActionRequest.getPotentialStockItem());
        assertTrue(stockItemActionRequest.getStockItem().getStatus().getStatusCode()
                    .equalsIgnoreCase(Status.ACTIVE));
        assertFalse(stockItemActionRequest.getStockItem().getPotentialIndicator().booleanValue());  
        
        HibernateUtil.commitTransaction();  
        HibernateUtil.closeSession();
    }

    public void testDenyRequest() throws Exception {
        Iterator iter =  stockItemActionRequestDAO
                                    .findByStatusCodes(new String[] {Status.WAITING_FOR_APPROVAL})
                                    .iterator();
                                    
        if(iter == null || !iter.hasNext()) {
            stockItemActionRequest = (StockItemActionRequest)stockItemActionRequestDAO.findAll().iterator().next();
            Status wfa = (Status)statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM_ACTION_REQUEST,
                                                                             Status.WAITING_FOR_APPROVAL);
            stockItemActionRequest.setStatus(wfa);
            HibernateUtil.commitTransaction();  
            HibernateUtil.closeSession();
        }
        else {
            stockItemActionRequest = (StockItemActionRequest)iter.next();
        }
        
        assertNotNull(stockItemActionRequest);
        System.out.println("stockItemActionRequest ID: " + stockItemActionRequest.getStockItemActionRequestId());
        Status deniedStatus = 
            (Status) statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM_ACTION_REQUEST,
                                                                 Status.DENIED);
        
        //approve the request, and therefore apply the changes to original stock item and SAVES everything
        stockItemActionRequest.deny();
        
        //make sure the SIAR's status is approved and the stock item's status is ACTIVE, and it's potentialInd is FALSE
        assertEquals(stockItemActionRequest.getStatus(), deniedStatus);
        assertNotNull(stockItemActionRequest.getPotentialStockItem());
        assertTrue(stockItemActionRequest.getPotentialStockItem().getPotentialIndicator().booleanValue());
        HibernateUtil.commitTransaction();  
        HibernateUtil.closeSession();
    }

    private StockItemActionRequest constructNewStockItemActionRequest() throws Exception {
        ActionRequestType newSIactionRequestType =
                (ActionRequestType) actionRequestTypeDAO.findByActionRequestTypeCode(ActionRequestType.NEW_STOCK_ITEM);

        stockItemActionRequest.setDaoFactory(new HibernateDAOFactory());
        
        Status activeStockItemStatus = 
                    (Status) statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM, 
                                                                         Status.ACTIVE);
        
        StockItemActionRequest siar = new StockItemActionRequest();
        //set up only the properties that would be set before hitting the rules engine
        siar.setActionRequestType(newSIactionRequestType);
        siar.setRequestedDate(new Date());
        siar.setRequestReason("This is needed for Testing purposes.  TestStockItemActionRequest created this request...");
        siar.setSpecialInstructions("This is for a NEW stock item.");
        siar.setSuggestedVendorName("VWR");
        siar.setRequestor(person);

        StockItem potentialStockItem = new StockItem();
        potentialStockItem.copyProperties((StockItem)stockItemDAO.findAll().iterator().next());
        potentialStockItem.setStatus(activeStockItemStatus);
        potentialStockItem.setPotentialIndicator(Boolean.TRUE);
        potentialStockItem.setAnnualUsage(new Integer(1623));
        potentialStockItem.setCycleCountPriority("C");
        potentialStockItem.setDescription("A New Test Item created by the junit test class - TestStockItemActionRequest");
        potentialStockItem.setHazardous(Boolean.TRUE);
        potentialStockItem.setQtyOnHand(new Integer(0));
        potentialStockItem.setInsertionDate(new Date());
        potentialStockItem.setInsertedBy("flahas1");
        potentialStockItem.setIcnbr(null);

        siar.setPotentialStockItem(potentialStockItem);

        return siar;
    }

    private StockItemActionRequest constructPutOnHoldActionRequest() throws Exception {
        ActionRequestType modifyStockItemRequestType =
                (ActionRequestType) actionRequestTypeDAO.findByActionRequestTypeCode(ActionRequestType.STOCK_ITEM_MODIFICATIONS);

        Status onHoldStatus = (Status)statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM, 
                                                                                  Status.ONHOLD);
        Status activeStatus = (Status)statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM, 
                                                                                  Status.ACTIVE);
                                                                                  
        StockItem originalStockItem = (StockItem)stockItemDAO.findByStatusCode(activeStatus.getStatusCode()).iterator().next();
        assertNotNull(originalStockItem);
        
        originalStockItem.setStatus(activeStatus);       //always make sure its active before putting on hold, for rules engine
        
        StockItemActionRequest siar = new StockItemActionRequest();
        
        //set up only the properties that would be set before hitting the rules engine
        siar.setActionRequestType(modifyStockItemRequestType);
        siar.setRequestedDate(new Date());
        siar.setRequestReason("Put Item # " + originalStockItem.getItemId() + " on hold please.");
        siar.setSpecialInstructions("Put Item # " + originalStockItem.getItemId() + " on hold please.");
        siar.setRequestor(person);
        
        StockItem potentialStockItem = new StockItem();  //make a copy for our test's sake - in real world, we'd create a brand new StockItem  
        potentialStockItem.copyProperties(originalStockItem);
        potentialStockItem.setStatus(onHoldStatus);
        potentialStockItem.setFillUntilDepleted(Boolean.FALSE);
        potentialStockItem.setPotentialIndicator(Boolean.TRUE);

        siar.setStockItem(originalStockItem);
        siar.setPotentialStockItem(potentialStockItem);

        return siar;
    }

    private StockItemActionRequest constructReactivateActionRequest() throws Exception {
        ActionRequestType reactivateActionRequestType =
                (ActionRequestType)actionRequestTypeDAO.findByActionRequestTypeCode(ActionRequestType.STOCK_ITEM_MODIFICATIONS);

        Status activeStatus = 
            (Status)statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM,
                                                                Status.ACTIVE);
        Status inactiveStatus = 
            (Status)statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM,
                                                                Status.INACTIVE);
        
        StockItem originalStockItem = (StockItem)stockItemDAO.findByStatusCode(inactiveStatus.getStatusCode()).iterator().next();
        
        StockItem potentialStockItem = new StockItem();  //make a copy for our test's sake - in real world, we'd create a brand new StockItem 
        potentialStockItem.copyProperties(originalStockItem);
        potentialStockItem.setStatus(activeStatus);
        potentialStockItem.setPotentialIndicator(Boolean.TRUE);
        
        StockItemActionRequest siar = new StockItemActionRequest();
        
        //set up only the properties that would be set before hitting the rules engine
        siar.setActionRequestType(reactivateActionRequestType);
        siar.setRequestedDate(new Date());
        siar.setRequestReason("Reactivate stock item #" + originalStockItem.getItemId());
        siar.setSpecialInstructions("Reactivate stock item #" + originalStockItem.getItemId());
        siar.setRequestor(person);
        siar.setStockItem(originalStockItem);
        siar.setPotentialStockItem(potentialStockItem);

        return siar;
    }

    private StockItemActionRequest constructInactivateActionRequest() throws Exception {
        ActionRequestType modifyActionRequestType =
                (ActionRequestType)actionRequestTypeDAO.findByActionRequestTypeCode(ActionRequestType.STOCK_ITEM_MODIFICATIONS);

        Status inactiveStatus = 
            (Status)statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM,
                                                                Status.INACTIVE);
        Status activeStatus = 
            (Status)statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM,
                                                                Status.ACTIVE);
                                                                
        StockItem originalStockItem = 
            (StockItem)stockItemDAO.findByStatusCode(activeStatus.getStatusCode()).iterator().next();
        long itemID = originalStockItem.getItemId().longValue();
        
        StockItem potentialStockItem = new StockItem();  //make a copy for our test's sake - in real world, we'd create a brand new StockItem     
        potentialStockItem.copyProperties(originalStockItem);
        potentialStockItem.setStatus(inactiveStatus);
        
        StockItemActionRequest siar = new StockItemActionRequest();
        
        //set up only the properties that would be set before hitting the rules engine
        siar.setActionRequestType(modifyActionRequestType);
        siar.setRequestedDate(new Date());
        siar.setRequestReason("Inactivate a stock item #" + itemID );
        siar.setSpecialInstructions("Inactivate a stock item #" + itemID);
        siar.setRequestor(person);
        siar.setStockItem(originalStockItem);
        siar.setPotentialStockItem(potentialStockItem);

        return siar;
    }

    private StockItem constructStockItem() throws Exception {
        StockItem stockItem = new StockItem();

        return stockItem;
    }

    public TestStockItemActionRequest(String x) {
        super(x);
    }

    public static void main(String[] args) throws Exception {
        TestRunner.run(suite());
        //junit.swingui.TestRunner.run(us.mn.state.health.test.TestStockItemActionRequest.class);
    }
}