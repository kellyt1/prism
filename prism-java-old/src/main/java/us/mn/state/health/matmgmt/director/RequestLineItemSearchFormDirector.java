package us.mn.state.health.matmgmt.director;
import us.mn.state.health.builder.materialsrequest.RequestLineItemSearchFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class RequestLineItemSearchFormDirector  {

    private RequestLineItemSearchFormBuilder builder;
    
    public RequestLineItemSearchFormDirector(RequestLineItemSearchFormBuilder builder) {
        this.builder = builder;
    }
    
    public void constructPurchasingNew() throws InfrastructureException {
        builder.buildDefaultProperties();
        //builder.buildVendors();   SF - commented this out - we're not doing vendor drop-down for searching after all - too slow
        builder.buildCategories();
        builder.buildRequestors();
        builder.buildPurchasers();
        builder.buildPurchasingStatuses();
    }
    
    public void constructPurchasingAdvancedSearch() throws InfrastructureException {
        builder.buildAdvancedSearchResults();
    }
}