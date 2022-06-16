package us.mn.state.health.builder;
import us.mn.state.health.builder.purchasing.VendorAccountBuilder;
import us.mn.state.health.builder.purchasing.VendorContractBuilder;
import us.mn.state.health.common.exceptions.BusinessException;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.common.VendorAccount;
import us.mn.state.health.model.common.VendorContract;
import us.mn.state.health.view.common.ExternalOrgDetailForm;
import us.mn.state.health.view.common.VendorAccountForm;
import us.mn.state.health.view.common.VendorContractForm;
import us.mn.state.health.view.common.VendorForm;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class VendorBuilder  {

    private Vendor vendor;
    private VendorForm vendorForm;
    private String username;

    public VendorBuilder(Vendor vendor, VendorForm vendorForm, String username) {
        this.vendor = vendor;
        this.vendorForm = vendorForm;
        this.username = username;
    }

    public void buildSimpleProperties() throws BusinessException {
        try {
            PropertyUtils.copyProperties(vendor, vendorForm);
        }
        catch(ReflectivePropertyException rpe) {
            throw new BusinessException(rpe);
        }
    }

    public void buildExternalOrgDetail() throws BusinessException, InfrastructureException, ReflectivePropertyException, ParseException {
        ExternalOrgDetailForm externalOrgDetailForm = vendorForm.getExternalOrgDetailForm();
        ExternalOrgDetail externalOrgDetail = vendor.getExternalOrgDetail();
        ExternalOrgDetailBuilder builder = new ExternalOrgDetailBuilder(externalOrgDetail
                , externalOrgDetailForm
                , username);
        builder.buildSimpleProperties();
        builder.buildPrimaryRepUsingEODForm();
        builder.buildPrimaryEmailAddressUsingEODForm();
        builder.buildPrimaryPhoneAddressUsingEODForm();
        builder.buildPrimaryFaxAddressUsingEODForm();
        builder.buildMailingAddresses();
    }

    public void buildVendorContracts() throws InfrastructureException, ReflectivePropertyException {
        /*
        * update the existing vendor contracts (eventualy delete the deleted ones)
        */
        Collection vendorContracts = vendor.getVendorContracts();
        HibernateDAO hibernateDAO = new HibernateDAO();

        Collection vendorContractForms = vendorForm.getVendorContracts();
        VendorContractBuilder vendorContractBuilder = null;
        //update and deletion
        for (Iterator iterator = vendorContracts.iterator(); iterator.hasNext();) {
            VendorContract vendorContract = (VendorContract) iterator.next();
            String vendorContractId = vendorContract.getVendorContractId().toString();
            VendorContractForm vendorContractForm =
                    (VendorContractForm) CollectionUtils
                    .getObjectFromCollectionById(vendorContractForms, vendorContractId, "vendorContractId");

            if (vendorContractForm == null) {
                iterator.remove();
                hibernateDAO.makeTransient(vendorContract);
            }
            else {
                vendorContractBuilder = new VendorContractBuilder(vendorContract, vendorContractForm, vendor, username);
                vendorContractBuilder.buildSimpleProperties();
            }
        }

        //creation of new vendor contracts

        for (Iterator iterator = vendorContractForms.iterator(); iterator.hasNext();) {
            VendorContractForm vendorContarctForm = (VendorContractForm) iterator.next();
            String vendorContractId = vendorContarctForm.getVendorContractId();
            if (vendorContractId == null) {
                VendorContract vendorContract = new VendorContract();
                vendorContractBuilder = new VendorContractBuilder(vendorContract, vendorContarctForm, vendor, username);
                vendorContractBuilder.buildSimpleProperties();
                vendorContractBuilder.buildNewVendorContractProperties();
            }
        }
    }
    public void buildVendorAccounts() throws InfrastructureException, ReflectivePropertyException {
        /*
        * update the existing vendor accounts (eventualy delete the deleted ones)
        */
        Collection vendorAccounts = vendor.getVendorAccounts();
        HibernateDAO hibernateDAO = new HibernateDAO();

        Collection vendorAccountForms = vendorForm.getVendorAccounts();
        VendorAccountBuilder vendorAccountBuilder = null;
        //update and deletion
        for (Iterator iterator = vendorAccounts.iterator(); iterator.hasNext();) {
            VendorAccount vendorAccount = (VendorAccount) iterator.next();
            String vendorAccountId = vendorAccount.getVendorAccountId().toString();
            VendorAccountForm vendorAccountForm =
                    (VendorAccountForm) CollectionUtils
                    .getObjectFromCollectionById(vendorAccountForms, vendorAccountId, "vendorAccountId");

            if (vendorAccountForm == null) {
                iterator.remove();
                hibernateDAO.makeTransient(vendorAccount);
            }
            else {
                vendorAccountBuilder = new VendorAccountBuilder(vendorAccount, vendorAccountForm, vendor, username);
                vendorAccountBuilder.buildSimpleProperties();
            }
        }

        //creation of new vendor accounts

        for (Iterator iterator = vendorAccountForms.iterator(); iterator.hasNext();) {
            VendorAccountForm vendorAccountForm = (VendorAccountForm) iterator.next();
            Long vendorAccountId = vendorAccountForm.getVendorAccountId();
            if (vendorAccountId == null) {
                VendorAccount vendorAccount = new VendorAccount();
                vendorAccountBuilder = new VendorAccountBuilder(vendorAccount, vendorAccountForm, vendor, username);
                vendorAccountBuilder.buildSimpleProperties();
                vendorAccountBuilder.buildNewVendorAccountProperties();
            }
        }
    }

    public void buildNewExternalOrgDetail() throws InfrastructureException, BusinessException, ReflectivePropertyException, ParseException {
        ExternalOrgDetailForm externalOrgDetailForm = vendorForm.getExternalOrgDetailForm();
        ExternalOrgDetail externalOrgDetail = new ExternalOrgDetail();
        ExternalOrgDetailBuilder builder = new ExternalOrgDetailBuilder(externalOrgDetail, 
                                                                        externalOrgDetailForm, 
                                                                        username);
        builder.buildSimpleProperties();
        builder.buildPrimaryRepUsingEODForm();
        builder.buildPrimaryEmailAddressUsingEODForm();
        builder.buildPrimaryPhoneAddressUsingEODForm();
        builder.buildPrimaryFaxAddressUsingEODForm();
        builder.buildMailingAddresses();
        externalOrgDetail.setInsertionDate(new Date());
        externalOrgDetail.setInsertedBy(username);
        vendor.setExternalOrgDetail(externalOrgDetail);
    }
}