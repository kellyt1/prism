package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.VendorFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class VendorFormDirector {
    VendorFormBuilder vendorFormBuilder;

    public VendorFormDirector(VendorFormBuilder vendorFormBuilder) {
        this.vendorFormBuilder = vendorFormBuilder;
    }

    public void construct() throws InfrastructureException {
        vendorFormBuilder.buildSimpleProperties();
        vendorFormBuilder.buildExternalOrgDetail();
        vendorFormBuilder.buildVendorContracts();
        vendorFormBuilder.buildVendorAccounts();
    }
}
