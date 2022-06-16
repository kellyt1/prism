package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.inventory.StockItemActionRequestBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class NewStockItemActionRequestDirector {

    private StockItemActionRequestBuilder builder;

    public NewStockItemActionRequestDirector(StockItemActionRequestBuilder builder) {
        this.builder = builder;
    }

    public void construct() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildActionRequestType();
        builder.buildMetaProperties();
        builder.buildPotentialStockItem();
        builder.buildRequestor();
        builder.buildSuggestedVendor();
        builder.buildRequestedDate();
        builder.buildStockItem();
        builder.buildStockQtyChangeReasonRef();
        builder.buildStockQtyChangeOrgBdgt();
    }
}