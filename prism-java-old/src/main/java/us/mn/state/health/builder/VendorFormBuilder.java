package us.mn.state.health.builder;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.common.VendorAccount;
import us.mn.state.health.model.common.VendorContract;
import us.mn.state.health.view.common.ExternalOrgDetailForm;
import us.mn.state.health.view.common.VendorAccountForm;
import us.mn.state.health.view.common.VendorContractForm;
import us.mn.state.health.view.common.VendorForm;

import java.util.*;


public class VendorFormBuilder {
    Vendor vendor;
    VendorForm vendorForm;

    public VendorFormBuilder(Vendor vendor, VendorForm vendorForm) {
        this.vendor = vendor;
        this.vendorForm = vendorForm;
    }

    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(vendorForm,vendor);
        } catch (ReflectivePropertyException e) {
            throw new InfrastructureException(e);
        }
    }

     public void buildVendorAccounts() throws InfrastructureException{
        VendorAccountForm vendorAccountForm ;
        Set vendorAccountForms = new HashSet();
        Collection vendorAccounts = vendor.getVendorAccounts();
        try {
            for (Iterator iterator = vendorAccounts.iterator(); iterator.hasNext();) {
                VendorAccount vendorAccount = (VendorAccount) iterator.next();
                vendorAccountForm = new VendorAccountForm();
                PropertyUtils.copyProperties(vendorAccountForm, vendorAccount);
                vendorAccountForms.add(vendorAccountForm);
            }
        } catch (ReflectivePropertyException e) {
            throw new InfrastructureException(e);
        }
        vendorForm.setVendorAccounts(vendorAccountForms);
    }

    public void buildVendorContracts() throws InfrastructureException{
        VendorContractForm vendorContractForm ;
        Set vendorContractForms = new HashSet();
        Collection vendorContracts = vendor.getVendorContracts();
        try {
            for (Iterator iterator = vendorContracts.iterator(); iterator.hasNext();) {
                VendorContract vendorContract = (VendorContract) iterator.next();
                vendorContractForm = new VendorContractForm();
                PropertyUtils.copyProperties(vendorContractForm, vendorContract);

                // throw expired endDate; null or blank ones are good and active contracts.
                if (vendorContractForm.getEndDate() != null)   {
                    Date contractEndDate = StringUtils.formatDateFromString(vendorContractForm.getEndDate());
                    long endDate = contractEndDate.getTime();
                    Calendar c = Calendar.getInstance();
                    Date today = c.getTime();
                    long presentDay = today.getTime();
                    if (endDate < presentDay) {
                        // endDate expired
                    }
                    else
                        vendorContractForms.add(vendorContractForm);
                }
                else{
                    // null or blank endDate are good and active contracts.
                    vendorContractForms.add(vendorContractForm);
                }
            }
        } catch (ReflectivePropertyException e) {
            throw new InfrastructureException(e);
        }
        vendorForm.setVendorContracts(vendorContractForms);
    }

    public void buildExternalOrgDetail() throws InfrastructureException {
        ExternalOrgDetailForm externalOrgDetailForm = vendorForm.getExternalOrgDetailForm();
        ExternalOrgDetail externalOrgDetail = vendor.getExternalOrgDetail();
        ExternalOrgDetailFormBuilder builder = new ExternalOrgDetailFormBuilder(externalOrgDetailForm,externalOrgDetail);
        builder.buildSimpleProperties();
        builder.buildPrimaryRepresentative();
        builder.buildPrimaryPhone();
        builder.buildPrimaryEmailAddress();
        builder.buildPrimaryFax();
        builder.buildMailingAddresses();
    }
}
