package us.mn.state.health.matmgmt.director.conversionLegacy.convertStockItemMaterialRequests;

import us.mn.state.health.builder.conversionLegacy.convertStockItemMaterialRequests.StockItemMaterialRequestBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class StockItemMaterialRequestDirector {
    private StockItemMaterialRequestBuilder builder;

    public StockItemMaterialRequestDirector(StockItemMaterialRequestBuilder builder) {
        this.builder = builder;
    }

    public void construct() throws InfrastructureException {
        builder.buildDateRequested();
        builder.buildPriority();
        builder.buildTrackingNumber();
        builder.buildAdditionalInstructions();
        builder.buildRequestor();
        builder.buildRequestLineItems();
    }
}
