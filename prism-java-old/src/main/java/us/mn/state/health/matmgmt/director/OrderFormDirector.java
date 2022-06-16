package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.purchasing.OrderFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class OrderFormDirector  {    
    private OrderFormBuilder builder;
    
    public OrderFormDirector(OrderFormBuilder builder) {
        this.builder = builder;
    }
    
    private void constructPurchasing() throws InfrastructureException {
        builder.buildBillToAddresses();
        builder.buildShipToAddresses();
        builder.buildVendors();
        builder.buildReqLineItemForms();     
    }
    
    public void constructNewForPurchasing() throws InfrastructureException {
        constructPurchasing();
        builder.buildDefaultProperties();
    }
    
    public void constructEditForPurchasing() throws InfrastructureException {
        constructPurchasing();
        builder.buildSimpleProperties();
        builder.buildOrderLineItemForms();
        builder.buildVendorContracts();
        builder.buildVendorAccounts();
        builder.buildVendorAddresses();
        builder.buildNoteForms();
        builder.buildVendorFaxes();
        builder.buildVendorPhones();
        builder.buildVendorEmails();
        builder.buildVendorReps();
    }
    
    public void constructForReceiving() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildOrder();
        builder.buildFacilities();
        builder.buildOrderLineItemForms();
        builder.buildNoteForms();
    }
    
    public void constructReadOnly() throws InfrastructureException {
        //builder.buildSimpleProperties();
        builder.buildOrder();
    }
}