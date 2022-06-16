package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.inventory.StockItemFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class StockItemFormDirector  {
    
    private StockItemFormBuilder builder;
    
    public StockItemFormDirector(StockItemFormBuilder builder) {
        this.builder = builder;
    }
    
    
    /**
     * Build only those properties we need for the purpose of re-ordering a Stock Item.
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void constructForReorder() throws InfrastructureException {
        builder.buildItem();
    }
}