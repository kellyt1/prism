package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.materialsrequest.SearchMaterialRequestsFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class SearchMaterialRequestsFormDirector {
    private SearchMaterialRequestsFormBuilder builder;

    public SearchMaterialRequestsFormDirector(SearchMaterialRequestsFormBuilder builder) {
        this.builder = builder;
    }

    public void constructForMyRequests() throws InfrastructureException {
//        builder.buildOrgBudgets();
        builder.buildStatuses();
        builder.buildCategories();
    }

    public void constructForAllRequests() throws InfrastructureException {
//        builder.buildOrgBudgets();
        builder.buildStatuses();
        builder.buildCategories();
        builder.buildContacts();
    }

    public void search() throws InfrastructureException {
        builder.buildSearchResults(-1, -1);
    }

    public void advancedSearchMyRequests() throws InfrastructureException {
        builder.buildAdvancedSearchResultsForMyRequests(-1, -1);
    }
}
