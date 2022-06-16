package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.inventory.StockItemFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class EditStockItemFormDirector {
    private StockItemFormBuilder builder;

    public EditStockItemFormDirector(StockItemFormBuilder builder) {
        this.builder = builder;
    }

    public void construct() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildAsstDivDirectorsList();
        builder.buildAsstDivDirector();
        builder.buildCategoriesList();
        builder.buildCategory();
//        builder.buildContactsList();
        builder.buildPrimaryContact();
        builder.buildSecondaryContact();
        builder.buildOrgBudgetsList();
        builder.buildOrgBudget();
        builder.buildUnitsList();
        builder.buildDispenseUnit();
        builder.buildStatusesList();
        builder.buildStatus();
//        builder.buildAvailableVendorsList();
//        builder.buildItemVendors();
        builder.buildStockItemFacilitiesList();
        builder.buildMyStockItemLocations();
        builder.buildItem();
        builder.buildObjectCodesList();
        builder.buildObjectCode();
        builder.buildManufacturersList();
        builder.buildManufacturer();
    }

}