package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.purchasing.OrderLineItemBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class OrderLineItemDirector  {
    
    private OrderLineItemBuilder builder;
    
    public OrderLineItemDirector(OrderLineItemBuilder builder) {
        this.builder = builder;
    }
    
    public void construct() throws InfrastructureException {
        builder.buildBuyUnit();
        builder.buildItem();
        //builder.buildShipToAddress();
        builder.buildSimpleProperties();
        builder.buildStatusForPurchasing();
        builder.buildOrder();
    }
    
    public void constructReceivedOLI() throws InfrastructureException {
        builder.buildReceivingDetails();
        builder.buildStatusForReceiving();
    }
}