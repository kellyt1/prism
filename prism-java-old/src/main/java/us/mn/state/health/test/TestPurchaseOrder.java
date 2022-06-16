package us.mn.state.health.test;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.persistence.HibernateUtil;
import us.mn.state.health.view.purchasing.OrderForm;

public class TestPurchaseOrder extends TestCase {
    private DAOFactory daoFactory;
    
    public TestPurchaseOrder(String x) {
        super(x);
    }

    public static void main(String[] args) throws Exception {
        TestRunner.run(suite());
        //junit.swingui.TestRunner.run(us.mn.state.health.test.TestPurchaseOrder.class);
    }

    protected void setUp() {
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
    }

    protected void tearDown() throws InfrastructureException {
        HibernateUtil.closeSession();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        /*
       
        */
        suite.addTest(new TestPurchaseOrder("testSavePurchaseOrder"));

        return suite;
    }
    
    public void testSavePurchaseOrder() throws Exception {
        Order order = new Order();
        initializeOrder(order);
        //OrderForm orderForm = createOrderForm();
        daoFactory.getPurchasingOrderDAO().makePersistent(order);
        HibernateUtil.commitTransaction();
        assertNotNull(order.getOrderId());
    }
    
    private void initializeOrder(Order order) throws Exception {
        MailingAddress ma = daoFactory.getMailingAddressDAO().getMailingAddressById(new Long(70616), false);
        order.setBillToAddress(ma);
        Person purchaser = daoFactory.getPersonDAO().getPersonById(new Long(17113), false);
        order.setPurchaser(purchaser);
        order.setShipToAddress(ma);
        order.setSuspenseDate(new Date());
        Vendor vendor = daoFactory.getVendorDAO().getVendorById(new Long(62043), false);
        order.setVendor(vendor);
        MailingAddress addr = vendor.getExternalOrgDetail().getPrimaryMailingAddress();
        order.setVendorAddress(addr);
        order.setInsertedBy("flahas1");
        order.setInsertionDate(new Date());
        order.setVendorInstructions("do not pay too much");
        order.setOrderLineItems(createOrderLineItems(order));
    }
    
    private Collection createOrderLineItems(Order order) throws Exception {
        int numberOfOlis = 2;
        Collection olis = new HashSet();
        Collection rlis = daoFactory.getRequestLineItemDAO().findAllRequestLineItemsWithStatusCode(Status.WAITING_FOR_PURCHASE);
        for(int i = 0; i < numberOfOlis; i++) {
            OrderLineItem oli = new OrderLineItem();
            RequestLineItem rli = (RequestLineItem)rlis.iterator().next();
            oli.setAssetsType(OrderLineItem.SENSITIVE_ASSET_TYPE);
            Unit buyUnit = daoFactory.getUnitDAO().findUnitByCode(Unit.CODE_EACH);
            oli.setBuyUnit(buyUnit);
            oli.setBuyUnitCost(new Double("69.99"));
            oli.setCommodityCode("6969696969");
            oli.setDiscountPercent(new Double("10"));
            oli.setDispenseUnitsPerBuyUnit(new Integer("15"));
            oli.setInsertedBy("flahas1");
            oli.setInsertionDate(new Date());
            oli.setItemDescription("Well I changed the RLI mapping - Test item #" + i + " from the junit test case");
            oli.setQuantity(new Integer(i * 2));
            oli.setStatus(daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_ORDER, 
                                                                                      Status.ORDERED));
            oli.setOrder(order);
            oli.getRequestLineItems().add(rli);
            rli.setOrderLineItem(oli);
            olis.add(oli);
        }
        return olis;        
    }
    
    private OrderForm createOrderForm() throws Exception {
        OrderForm orderForm = new OrderForm();
        orderForm.setOtherBillToAddress("8612 Smelly Street, Mobile AL, 55005");
        orderForm.setOtherShipToAddress("512 Jackson Lane, St. Paul, MN 55101");
        orderForm.setSuspenseDate("09/30/2005");
        orderForm.setVendorId("62043");
        return orderForm;
    }
}
