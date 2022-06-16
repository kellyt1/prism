package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.inventory.PurchaseItemBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class PurchaseItemDirector  {
    
    private PurchaseItemBuilder builder;
    
    public PurchaseItemDirector(PurchaseItemBuilder builder) {
        this.builder = builder;
    }
    
    public void construct() throws InfrastructureException {
        builder.buildCategory();
        builder.buildDispenseUnit();
        builder.buildManufacturer();
        builder.buildMetaProperties();
        builder.buildSimpleProperties();
        builder.buildVendorContract();
        builder.buildObjectCode();
        builder.buildDefaultDeliveryDetail();
    }
}