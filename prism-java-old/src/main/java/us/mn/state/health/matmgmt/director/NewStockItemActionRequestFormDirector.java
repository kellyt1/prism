package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.inventory.StockItemActionRequestFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class NewStockItemActionRequestFormDirector {

    private StockItemActionRequestFormBuilder builder;

    public NewStockItemActionRequestFormDirector(StockItemActionRequestFormBuilder builder) {
        this.builder = builder;
    }

    /**
     * This is used for a new SI
     *
     * @throws InfrastructureException
     */
    public void constructNew() throws InfrastructureException {
        builder.buildDefaultProperties();
        builder.buildPotentialStockItemForm();
        builder.buildStockItem();
        //builder.buildVendors();           //don't display a list of vendors for this - too much of a performance hit.
        builder.buildStockQtyChangeReasonRefs();
    }
}