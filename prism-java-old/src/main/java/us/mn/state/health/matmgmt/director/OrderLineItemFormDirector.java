package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.purchasing.OrderLineItemFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class OrderLineItemFormDirector  {

    private OrderLineItemFormBuilder builder;
    
    public OrderLineItemFormDirector(OrderLineItemFormBuilder builder) {
        this.builder = builder;
    }
    
    public void constructNewPurchasing() throws InfrastructureException {
        builder.buildDirty();
        builder.buildAddReqLineItemForms();
        builder.buildItemFromReqLnItms();
        builder.buildQuantityFromReqLnItms();
        builder.buildDescriptionFromReqLnItms();
        builder.buildBuyUnitFromReqLnItms();
        builder.buildBuyUnitCostFromReqLnItms();
        builder.buildVendorCatalogNbrFromReqLnItms();
        //builder.buildAddresses();
        builder.buildUnits();
        builder.buildPurchasingStatuses();
        builder.buildAssetTypes();
    }
    
    public void constructEditPurchasing() throws InfrastructureException {
        builder.buildDirty();
        builder.buildItem();
        builder.buildReqLineItemFormsFromOrderLineItemRequests();
        builder.buildSimpleProperties();
        //builder.buildAddresses();
        builder.buildUnits();
        builder.buildPurchasingStatuses();
        builder.buildAssetTypes();
    }
    
    public void constructAddRequestLineItems() throws InfrastructureException {
        builder.buildDirty();
        builder.buildReqLineItemFormsFromOrderLineItemRequests();
        builder.buildAddReqLineItemForms();
        builder.buildItemFromReqLnItms();
        builder.buildQuantityFromReqLnItms();
        builder.buildDescriptionFromReqLnItms();
        builder.buildBuyUnitFromReqLnItms();
        builder.buildBuyUnitCostFromReqLnItms();
        builder.buildVendorCatalogNbrFromReqLnItms();
        //builder.buildAddresses();
        builder.buildUnits();
        builder.buildPurchasingStatuses();
        builder.buildAssetTypes();
    }    
}