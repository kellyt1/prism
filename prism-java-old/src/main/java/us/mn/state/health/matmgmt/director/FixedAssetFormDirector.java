package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.inventory.FixedAssetFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class FixedAssetFormDirector {
    FixedAssetFormBuilder builder;
    
    public FixedAssetFormDirector(FixedAssetFormBuilder builder) {
        this.builder = builder;
    }
    
    public void constructNew() throws InfrastructureException {
        builder.buildContactPersons();
        builder.buildClassCodes();
        builder.buildFacilities();
        builder.buildAllOrgBudgetsList();
        builder.buildVendors();
        builder.buildStatuses();
        builder.buildOrderLineItem();
        builder.buildPropertiesFromOLI();
    }
    
    public void constructExisting() throws InfrastructureException {
        builder.buildFixedAsset();
        builder.buildSimpleProperties();
        builder.buildContactPersons();
        builder.buildContactPerson();
        builder.buildClassCodes();
        builder.buildClassCode();
        builder.buildFacilities();
        builder.buildFacility();
        builder.buildAllOrgBudgetsList();
        builder.buildOrgBudgets();
        builder.buildVendors();
        builder.buildVendor();
        builder.buildStatuses();
        builder.buildStatus();
        builder.buildOrderLineItem();
    }
}