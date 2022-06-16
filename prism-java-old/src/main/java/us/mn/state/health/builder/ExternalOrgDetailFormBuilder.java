package us.mn.state.health.builder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.common.EmailAddress;
import us.mn.state.health.model.common.ExtOrgDetailMailingAddress;
import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Phone;
import us.mn.state.health.view.common.ExternalOrgDetailForm;
import us.mn.state.health.view.common.MailingAddressForm;

/**
 * Class that builds a ExternalOrgDetailForm from a ExternalOrgDetail
 */
public class ExternalOrgDetailFormBuilder {

    /** ExternalOrgDetailForm to be built */
    private ExternalOrgDetailForm extOrgDtlForm;

    /** ExternalOrgDetail used to build ExternalOrgDetailForm */
    private ExternalOrgDetail extOrgDtl;

    /**
     * ExternalOrgDetailFormBuilder Constructor
     * @param extOrgDtlForm the ExternalOrgDetailForm to be built
     * @param extOrgDtl the ExternalOrgDetail used to build ExternalOrgDetailForm
     */
    public ExternalOrgDetailFormBuilder(ExternalOrgDetailForm extOrgDtlForm, ExternalOrgDetail extOrgDtl) {
        this.extOrgDtlForm = extOrgDtlForm;
        this.extOrgDtl = extOrgDtl;
    }

    /** Build default ExternalOrgDetailForm properties */
    public void buildDefaultProperties() {

    }

    /**
     * Build simple extOrgDtlForm properties ExternalOrgDetailForm the ExternalOrgDetail
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(extOrgDtlForm, extOrgDtl);
            extOrgDtlForm.setOrgName(extOrgDtl.getOrgName().toUpperCase());
        }
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }

    public void buildPrimaryRepresentative() throws InfrastructureException{
        Person primaryRep = extOrgDtl.getPrimaryRep();
        if (primaryRep!=null) {
            extOrgDtlForm.getPersonForm()
                    .setPersonId(primaryRep.getPersonId().toString());
            extOrgDtlForm.getPersonForm().setFirstName(primaryRep.getFirstName());
            extOrgDtlForm.getPersonForm().setMiddleName(primaryRep.getMiddleName());
            extOrgDtlForm.getPersonForm().setLastName(primaryRep.getLastName());
        }
    }

    public void buildPrimaryEmailAddress() throws InfrastructureException{
        EmailAddress primaryEmailAddress = extOrgDtl.getPrimaryEmailAddress();
        if(primaryEmailAddress!=null){
            new EmailAddressFormBuilder(primaryEmailAddress, extOrgDtlForm.getEmailAddressForm())
                    .buildSimpleProperties();
        }

    }
    public void buildPrimaryPhone() throws InfrastructureException{
        Phone phone = extOrgDtl.getPrimaryPhone();
        if(phone!=null){
            new PhoneFormBuilder(phone, extOrgDtlForm.getPhoneForm())
                    .buildSimpleProperties();
        }
    }
    public void buildPrimaryFax() throws InfrastructureException{
        Phone fax = extOrgDtl.getPrimaryFax();
        if(fax!=null){
            new PhoneFormBuilder(fax,extOrgDtlForm.getFaxForm()).buildSimpleProperties();
        }
    }

    /**
     * Creates the collection of MailingAddressForm
     * @throws InfrastructureException
     */

    public void buildMailingAddresses() throws InfrastructureException {
        Collection extOrgDetailMailingAddresses = extOrgDtl.getMailingAddresses();
        Set addressForms = new HashSet();
        for (Iterator iterator = extOrgDetailMailingAddresses.iterator(); iterator.hasNext();) {
            ExtOrgDetailMailingAddress extOrgDetailMailingAddress = (ExtOrgDetailMailingAddress) iterator.next();
            MailingAddress address = extOrgDetailMailingAddress.getMailingAddress();
            MailingAddressForm addressForm = new MailingAddressForm();
            MailingAddressFormBuilder builder = new MailingAddressFormBuilder(addressForm, address);
            builder.buildSimpleProperties();
            addressForms.add(addressForm);
        }
        extOrgDtlForm.setMailingAddresses(addressForms);
    }

}
