package us.mn.state.health.matmgmt.director.conversionLegacy;

import us.mn.state.health.builder.conversionLegacy.VendorBuilder;
import us.mn.state.health.common.exceptions.BusinessException;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class VendorDirector {
    private VendorBuilder builder;

    public VendorDirector(VendorBuilder builder) {
        this.builder = builder;
    }

    public void construct() throws BusinessException, InfrastructureException {
        builder.buildExternalOrgDetail();
        builder.buildSimpleProperties();
    }
}
