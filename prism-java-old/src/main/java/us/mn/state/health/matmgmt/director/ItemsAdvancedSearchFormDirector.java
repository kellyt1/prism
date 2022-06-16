package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.inventory.ItemsAdvancedSearchFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class ItemsAdvancedSearchFormDirector {
    private ItemsAdvancedSearchFormBuilder builder;

    public ItemsAdvancedSearchFormDirector(ItemsAdvancedSearchFormBuilder builder) {
        this.builder = builder;
    }

    public void constructAdvancedSearchCatalogForm() throws InfrastructureException {
        builder.buildCategories();
        builder.buildUnits();
        builder.buildOrgBudgets();
//        builder.buildVendors();
        builder.buildManufacturers();
        builder.buildStatuses();
    }

    public void constructAdvancedSearchStockItemsForm() throws InfrastructureException {
        builder.buildContacts();
        builder.buildCategories();
        builder.buildUnits();
        builder.buildOrgBudgets();
//        builder.buildVendors();
        builder.buildManufacturers();
        builder.buildStatuses();
    }

    /**
     * This method is used for doing the search in the Catalog and populating the form with the results
     *
     * @throws InfrastructureException
     */
    public void searchInCatalogAndConstruct() throws InfrastructureException {
        builder.buildCatalogResults();
    }

    /**
     * This method is used for doing the search only in Stock Items and populating the form with the results
     *
     * @throws InfrastructureException
     */

    public void searchStockItemsAndConstruct() throws InfrastructureException {
        builder.buildStockItemsResults();
    }
}
