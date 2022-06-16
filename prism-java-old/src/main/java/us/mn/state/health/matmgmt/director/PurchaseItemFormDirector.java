package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.inventory.PurchaseItemFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class PurchaseItemFormDirector  {
    
    private PurchaseItemFormBuilder builder;
    
    public PurchaseItemFormDirector(PurchaseItemFormBuilder builder) {
        this.builder = builder;
    }
    
    private void construct() throws InfrastructureException {
        builder.buildAvailableVendors();
        builder.buildCategories();
        builder.buildManufacturers();
        builder.buildUnits();
        builder.buildObjectCodesList();
        builder.buildAttachedFiles();
    }
    
    private void constructForITPurchases() throws InfrastructureException {
        builder.buildAvailableVendors();
        builder.buildCategoriesForITPurchases();
        builder.buildManufacturers();
        builder.buildUnits();
        builder.buildObjectCodesList();
        builder.buildAttachedFiles();
    }

    public void constructNew() throws InfrastructureException {
        construct();
        builder.buildDefaultProperties();
    }

    public void constructNewForITPurchases() throws InfrastructureException {
        constructForITPurchases();
        builder.buildDefaultProperties();
    }

    public void constructEdit() throws InfrastructureException {
        construct();
        builder.buildItemVendors();
        builder.buildSimpleProperties();
        builder.buildItemVendorContractDetail();
        builder.buildDeliveryDetail();
    }

    public void constructEditForITPurchases() throws InfrastructureException {
        constructEdit();
        builder.buildCategoriesForITPurchases();
    }
}