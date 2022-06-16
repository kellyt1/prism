package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.inventory.StockItemActionRequestFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class EvaluateStockItemActionRequestFormDirector {

    private StockItemActionRequestFormBuilder builder;

    public EvaluateStockItemActionRequestFormDirector(StockItemActionRequestFormBuilder builder) {
        this.builder = builder;
    }

    public void construct() throws InfrastructureException {
        builder.buildActionRequestType();
        builder.buildPotentialStockItemForm();
        builder.buildRequestor();
        builder.buildSimpleProperties();
        builder.buildStockItem();
//        builder.buildVendors();
    }
}