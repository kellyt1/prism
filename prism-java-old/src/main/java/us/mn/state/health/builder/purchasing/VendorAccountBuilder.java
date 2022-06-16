package us.mn.state.health.builder.purchasing;

import us.mn.state.health.model.common.VendorContract;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.common.VendorAccount;
import us.mn.state.health.view.common.VendorContractForm;
import us.mn.state.health.view.common.VendorAccountForm;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;

import java.util.Date;

public class VendorAccountBuilder {
    private VendorAccount vendorAccount;
    private VendorAccountForm vendorAccountForm;
    private Vendor vendor;
    private String user;

    public VendorAccountBuilder(VendorAccount vendorAccount, VendorAccountForm vendorAccountForm, Vendor vendor, String user) {
        this.vendorAccount = vendorAccount;
        this.vendorAccountForm = vendorAccountForm;
        this.vendor = vendor;
        this.user = user;
    }


    public void buildSimpleProperties() throws ReflectivePropertyException {
        PropertyUtils.copyProperties(vendorAccount, vendorAccountForm);
    }

    public void buildNewVendorAccountProperties() {
        vendorAccount.setVendor(vendor);
        vendor.getVendorAccounts().add(vendorAccount);
//        vendorAccount.setInsertedBy(user);
 //       vendorAccount.setInsertionDate(new Date());
    }
}
