package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.inventory.FixedAssetBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class FixedAssetDirector {
    FixedAssetBuilder builder;
    
    public FixedAssetDirector(FixedAssetBuilder builder) {
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
        builder.buildOrderLineItem();
        builder.buildStatus();
    }
}