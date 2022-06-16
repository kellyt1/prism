package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.materialsrequest.RequestBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class RequestDirector  {
    RequestBuilder builder;
    
    public RequestDirector(RequestBuilder builder) {
        this.builder = builder;
    }
    
    public void construct() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildDeliveryDetail();
        builder.buildPriority();
        builder.buildRequestor();
    }
    
     public void constructForStockReorderRequest() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildRequestor();
        builder.buildPriority();
        builder.buildDeliveryDetailFromForm();
        builder.buildRequestLineItemsFromForm();
     }
    
    public void constructEditForPurchasing() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildPriority();
        builder.buildRequestLineItemsForPurchasing();
        builder.buildSelectedRequestor();
        //builder.buildDeliverToPerson();
    }
    
    public void constructNewForPurchasing() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildPriority();
        builder.buildRequestLineItemsForPurchasing();
        builder.buildSelectedRequestor();
        builder.buildTrackingNumber();
        //builder.buildDeliverToPerson();
    }
}