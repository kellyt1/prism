package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.inventory.StockItemActionRequestBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class EvaluateStockItemActionRequestDirector {
    private StockItemActionRequestBuilder builder;

    public EvaluateStockItemActionRequestDirector(StockItemActionRequestBuilder builder) {
        this.builder = builder;
    }

    public void construct() throws InfrastructureException {
        builder.buildActionRequestType();
        builder.buildSimpleProperties();
        builder.buildMetaProperties();
        builder.buildPotentialStockItem();
        builder.buildRequestEvaluations();
        builder.buildStatus();  //this call should always be last!
    }
}