package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.inventory.AssetSearchFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class AssetSearchFormDirector {
    private AssetSearchFormBuilder builder;

    public AssetSearchFormDirector(AssetSearchFormBuilder builder) {
        this.builder = builder;
    }

    public void construct() throws InfrastructureException {
        builder.buildCategories();
        builder.buildClassCodes();
        builder.buildOrgBudgets();
        builder.buildManufacturers();
        builder.buildStatuses();
        builder.buildContacts();
        builder.buildFacilities();
    }

    /**
     * This method is used for doing the search and populating the form with the results
     *
     * @throws InfrastructureException
     */
    public void search() throws InfrastructureException {
        builder.buildResults();
    }
}
