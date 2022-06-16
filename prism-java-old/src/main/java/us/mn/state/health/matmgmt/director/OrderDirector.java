package us.mn.state.health.matmgmt.director;
import us.mn.state.health.builder.purchasing.OrderBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class OrderDirector  {

    private OrderBuilder builder;
    
    public OrderDirector(OrderBuilder builder) {
        this.builder = builder;
    }
    
    public void constructForPurchasing() throws InfrastructureException {
        builder.buildBillToAddress();
        builder.buildShipToAddress();

        builder.buildVendor();
        builder.buildVendorContract();
        builder.buildVendorAccount();
        builder.buildVendorAddress();
        builder.buildReqLineItems();
        builder.buildSimpleProperties();
        builder.buildNotes();
    }
   private void constructForPurchasingComputer() throws InfrastructureException {

        builder.buildDummyVendor();
        builder.buildReqLineItems();
        builder.buildSimpleProperties();
        builder.buildNotes();
    }

    public void constructEditForPurchasing() throws InfrastructureException {
        constructForPurchasing();
        builder.buildOrderLineItems(Boolean.FALSE);

    }

    public void constructEditForPurchasingComputer() throws InfrastructureException {
        constructForPurchasingComputer();
        builder.buildOrderLineItems(Boolean.TRUE);
    }

    public void constructNewForPurchasing() throws InfrastructureException {
        constructForPurchasing();
        builder.buildPurchaser();
        builder.buildMetaProperties();
        builder.buildOrderLineItems(Boolean.FALSE);
    }
    public void constructNewForPurchasingComputer() throws InfrastructureException {
        constructForPurchasingComputer();
        builder.buildPurchaser();
        builder.buildMetaProperties();
        builder.buildOrderLineItems(Boolean.TRUE);
    }
}