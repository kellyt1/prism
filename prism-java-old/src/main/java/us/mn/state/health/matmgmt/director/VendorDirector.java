package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.VendorBuilder;
import us.mn.state.health.common.exceptions.BusinessException;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;

import java.text.ParseException;

public class VendorDirector {
    private VendorBuilder builder;

    public VendorDirector(VendorBuilder builder) {
        this.builder = builder;
    }

    public void construct() throws InfrastructureException, BusinessException, ReflectivePropertyException, ParseException {
        builder.buildSimpleProperties();
        builder.buildExternalOrgDetail();
        builder.buildVendorContracts();
        builder.buildVendorAccounts();
    }

    public void constructNewVendor() throws BusinessException, InfrastructureException, ReflectivePropertyException, ParseException {
        builder.buildSimpleProperties();
        builder.buildNewExternalOrgDetail();
        builder.buildVendorContracts();
        builder.buildVendorAccounts();
    }
}
