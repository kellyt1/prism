package us.mn.state.health.matmgmt.director;

import java.util.Collection;

import us.mn.state.health.builder.materialsrequest.BrowseCatalogFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class BrowseCatalogFormDirector {
    private BrowseCatalogFormBuilder builder;

    public BrowseCatalogFormDirector(BrowseCatalogFormBuilder browseCatalogFormBuilder) {
        this.builder = browseCatalogFormBuilder;
    }

    public void construct() throws InfrastructureException {
        builder.buildCategoriesPath();
        builder.buildCurrentSubCategories();
        builder.buildCurrentItems();
    }
    
    public void refreshItemsList() throws InfrastructureException {
        builder.buildCurrentItems();
    }

    public void refreshItemListUsingItemIds(Collection itemIds) throws InfrastructureException {
        builder.buildCurrentItemsUsingIds(itemIds);
    }
}
