package us.mn.state.health.common.report.test;

import junit.framework.TestCase;
import us.mn.state.health.common.report.HibernateJRBeanDataSourceFactory;

public class HibernateJRBeanDataSourceFactoryTestCase extends TestCase  {
    public HibernateJRBeanDataSourceFactoryTestCase(String sTestName) {
        super(sTestName);
    }
    
    public void testGetFieldValue() throws Exception {
        String hql = "select o from Order as o";
        HibernateJRBeanDataSourceFactory factory = 
            new HibernateJRBeanDataSourceFactory(hql);
        String[] propNames1 = {"orderId",
                               "purchaser.firstAndLastName"};
                              
        String[] propNames2 = {"orderLineItems.orderLineItemId",
                              "orderLineItems.buyUnit.name",
                              "orderLineItems.quantity"};
        
        while(factory.next()) {
            for(int i=0; i < propNames1.length; i++) {
                System.out.println(propNames1[i] + " field value = " + factory.getFieldValue(propNames1[i]));
            }
            for(int i=0; i < propNames2.length; i++) {
                System.out.println(propNames2[i] + " field value = " + factory.getFieldValue(propNames2[i]));
            }
        }
    }
}