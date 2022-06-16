package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.inventory.StockItemFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;


public class NewStockItemFormDirector {

    protected StockItemFormBuilder builder;

    public NewStockItemFormDirector(StockItemFormBuilder stockItemFormBuilder) {
        this.builder = stockItemFormBuilder;
    }

    public void construct() throws InfrastructureException {
        builder.buildAsstDivDirectorsList();
        builder.buildCategoriesList();
        builder.buildContactsList();
        builder.buildDefaultProperties();
        builder.buildOrgBudgetsList();
        builder.buildUnitsList();
        builder.buildManufacturersList();
    }
}