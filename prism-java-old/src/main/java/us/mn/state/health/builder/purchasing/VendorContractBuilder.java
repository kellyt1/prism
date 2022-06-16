package us.mn.state.health.builder.purchasing;

import java.util.Date;

import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.common.VendorContract;
import us.mn.state.health.view.common.VendorContractForm;

public class VendorContractBuilder {
    private VendorContract vendorContract;
    private VendorContractForm vendorContractForm;
    private Vendor vendor;
    private String user;

    public VendorContractBuilder(VendorContract vendorContract, VendorContractForm vendorContractForm, Vendor vendor, String user) {
        this.vendorContract = vendorContract;
        this.vendorContractForm = vendorContractForm;
        this.vendor = vendor;
        this.user = user;
    }


    public void buildSimpleProperties() throws ReflectivePropertyException {
        PropertyUtils.copyProperties(vendorContract, vendorContractForm);
    }

    public void buildNewVendorContractProperties() {
        vendorContract.setVendor(vendor);
        vendor.getVendorContracts().add(vendorContract);
        vendorContract.setInsertedBy(user);
        vendorContract.setInsertionDate(new Date());
    }
}
