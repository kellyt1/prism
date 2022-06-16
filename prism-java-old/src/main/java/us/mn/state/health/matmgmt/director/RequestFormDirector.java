package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.materialsrequest.RequestFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class RequestFormDirector {
    private RequestFormBuilder builder;

    public RequestFormDirector(RequestFormBuilder builder) {
        this.builder = builder;
    }

    public void constructNew() throws InfrastructureException {
        builder.buildPriorities();
        builder.buildDeliveryDetail();
        builder.buildAvailableRequestors();
    }

    public void rebuildRequestLineItemForms() throws InfrastructureException {
        builder.buildRequestLineItemForms(false);
    }

    /**
     * Prepare the Request/Shopping Cart form
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void construct() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildRequestor();
        builder.buildRequestLineItemForms(false);
        builder.buildPriorities();
        builder.buildDeliveryDetail();
    }

    public void constructForITPurchases() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildRequestor();
        builder.buildRequestLineItemFormsForITPurchases(false);
        builder.buildPriorities();
        builder.buildDeliveryDetail();
    }
    /**
     * Prepare the Request form for modification, probably by Requestor via the My Requests
     * use case.
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void constructMyRequests() throws InfrastructureException {
        long t1 = System.currentTimeMillis();
        builder.buildSimpleProperties();
        builder.buildRequestor();
        //builder.buildRequestLineItemFormsForViewMyRequests();
        builder.buildPriorities();
        builder.buildDeliveryDetail();
        //System.out.println("timeWhole="+(System.currentTimeMillis()-t1));
    }

    /**
     * Prepare the Request form for an evaluator
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void constructForEvaluation() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildRequestor();
        builder.buildRequestLineItemForms(true);
        builder.buildPriorities();
        builder.buildDeliveryDetail();
    }

    /**
     * Prepare the Request Form for stock clerk.  The request form will
     * only contain the approved RLI's that are for stock items.
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void constructForStockClerk() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildRequestor();
        builder.buildRequestLineItemFormsForStockItems();
        builder.buildPriorities();
        builder.buildDeliveryDetail();
    }

    public void constructForSearchFillStockRequests() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildRequestor();
        builder.buildRequestLineItemFormsForStockItemsWithAllStatuses();
        builder.buildPriorities();
        builder.buildDeliveryDetail();
    }

    /**
     * Prepare the Request Form for stock clerk.  The request form will
     * only contain the approved RLI's that are for stock items.
     * <p/>
     * Added this method for the advanced search of Stock Requests where the user
     * can select to see not only the 'Waiting For Dispersal' rli's
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void constructForStockClerk(String[] codes) throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildRequestor();
        builder.buildRequestLineItemFormsForStockItems(codes);
        builder.buildPriorities();
        builder.buildDeliveryDetail();
    }

    /**
     * Prepare edit Request Form for a purchaser
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void constructEditForPurchasing() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildAvailableRequestors();
        builder.buildDeliveryDetail();
        builder.buildPriorities();
    }

    /**
     * Prepare new Request Form for a purchaser
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void constructNewForPurchasing() throws InfrastructureException {
        builder.buildDefaultProperties();
        builder.buildAvailableRequestors();
        builder.buildPriorities();
    }
}