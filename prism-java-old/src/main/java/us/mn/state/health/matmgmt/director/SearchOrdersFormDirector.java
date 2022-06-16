package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.purchasing.SearchOrdersFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class SearchOrdersFormDirector {
    private SearchOrdersFormBuilder builder;

    public SearchOrdersFormDirector(SearchOrdersFormBuilder builder) {
        this.builder = builder;
    }

    public void construct() throws InfrastructureException {
        //builder.buildVendors();   commented this out - where replacing vendor drop-down with a text box.
//        builder.buildOrgBudgets();
        builder.buildStatuses();
        builder.buildPersons();
        builder.buildBuyers();
        builder.buildFacilities();

    }


    /**
     * instruct the builder to build the search results
     *
     * @throws InfrastructureException
     */
    public void search() throws InfrastructureException {
        builder.buildSearchResults();
    }
}
