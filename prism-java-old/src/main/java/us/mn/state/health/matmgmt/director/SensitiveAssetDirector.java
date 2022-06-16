package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.inventory.SensitiveAssetBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class SensitiveAssetDirector {
    SensitiveAssetBuilder builder;
    
    public SensitiveAssetDirector(SensitiveAssetBuilder builder) {
        this.builder = builder;
    }
    
    public void construct() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildMetaProperties();
        builder.buildItem();
        builder.buildContactPerson();
        builder.buildClassCode();
        builder.buildFacility();
        builder.buildOwnerOrgBudgets();
        builder.buildVendor();
        builder.buildStatus();
        builder.buildOrderLineItem();
    }
}