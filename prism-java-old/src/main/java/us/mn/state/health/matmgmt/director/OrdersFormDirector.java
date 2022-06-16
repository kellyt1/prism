package us.mn.state.health.matmgmt.director;
import us.mn.state.health.builder.purchasing.OrdersFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class OrdersFormDirector  {

    private OrdersFormBuilder builder;
    
    public OrdersFormDirector(OrdersFormBuilder builder) {
        this.builder = builder;
    }
    
    public void construct() throws InfrastructureException {
        builder.buildDefaultProperties();
        builder.buildPurchasers();
        builder.buildRequestors();
        builder.buildStatuses();
        builder.buildVendors();
    }
}