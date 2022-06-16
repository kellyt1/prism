package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.inventory.StockItemBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class StockItemDirector {
    private StockItemBuilder builder;

    public StockItemDirector(StockItemBuilder builder) {
        this.builder = builder;
    }

    /**
     * this method calls builder methods to set the complex properties of the StockItem.
     * The call to builder.buildStockItem() sets all the simple properties.
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void construct() throws InfrastructureException {
        builder.buildSimpleProperties(); //set the simple (primitive) properties
        builder.buildDefaultProperties();
        builder.buildCategory();
        builder.buildDispenseUnit();
        builder.buildOrgBudget();
        builder.buildManufacturer();
        builder.buildMetaProperties();
//        builder.buildPrintSpecFile();
        builder.buildPrimaryContact();
        builder.buildSecondaryContact();
        builder.buildStatus();
        builder.buildAsstDivDirector();   
        builder.buildLocations();
        builder.buildObjectCode();
    }
}