package us.mn.state.health.matmgmt.director.conversionLegacy.fixedassets;

import us.mn.state.health.builder.conversionLegacy.fixedAssets.FixedAssetBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class FixedAssetDirector {
    private FixedAssetBuilder builder;

    public FixedAssetDirector(FixedAssetBuilder builder) {
        this.builder = builder;
    }

    public void construct() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildNotes();
        builder.buildVendor();
        builder.buildClassCode();
        builder.buildPurchaseItem();
    }
}
