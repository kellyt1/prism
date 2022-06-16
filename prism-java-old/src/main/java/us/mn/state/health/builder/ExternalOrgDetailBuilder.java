package us.mn.state.health.builder;

import us.mn.state.health.common.exceptions.BusinessException;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.common.*;
import us.mn.state.health.view.common.EmailAddressForm;
import us.mn.state.health.view.common.ExternalOrgDetailForm;
import us.mn.state.health.view.common.MailingAddressForm;
import us.mn.state.health.view.common.PhoneForm;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Class that builds a ExternalOrgDetail bean from a ExternalOrgDetail form
 *
 * @author $Author: ochial1 $
 */
public class ExternalOrgDetailBuilder {

    /**
     * ExternalOrgDetail to be built
     */
    private ExternalOrgDetail extOrgDtl;

    /**
     * ExternalOrgDetailForm used to build ExternalOrgDetail
     */
    private ExternalOrgDetailForm extOrgDtlForm;

    /**
     * Logged on User, used to build insert and update meta properties
     */
    private String username;


    /**
     * ExternalOrgDetailBuilder Constructor
     *
     * @param extOrgDtl     ExternalOrgDetail to be built
     * @param extOrgDtlForm ExternalOrgDetailForm from which ExternalOrgDetail is built
     */
    public ExternalOrgDetailBuilder(ExternalOrgDetail extOrgDtl,
                                    ExternalOrgDetailForm extOrgDtlForm,
                                    String username) {
        this.extOrgDtl = extOrgDtl;
        this.extOrgDtlForm = extOrgDtlForm;
        this.username = username;
    }

    /**
     * Build default ExternalOrgDetail properties
     */
    public void buildDefaultProperties() {

    }

    /*
     * Build Primary ExternalOrgDetail Representative
     *
    public void buildPrimaryRep() throws InfrastructureException {
        Person primaryRep = extOrgDtl.getPrimaryRep();
        PersonBuilder persBuilder = new PersonBuilder(primaryRep, addBkExtSrcForm, username);
        persBuilder.buildSimpleProperties();

    }
    */

    /**
     * Build Primary ExternalOrgDetail Representative  using ExternalOrgDetailForm
     */
    public void buildPrimaryRepUsingEODForm() throws InfrastructureException {
        //TODO refactor that to a HQL query????
        Collection reps = extOrgDtl.getReps();
        HibernateDAO hibernateDAO = new HibernateDAO();
        ExternalOrgDetailRep orgRep =
                (ExternalOrgDetailRep) CollectionUtils.getObjectFromCollectionById(reps, new Integer(1), "rank");
        Person rep;
        //if the first or last name is null we delete the externalOrgDetailRep and return
        if (extOrgDtlForm.getPersonForm().getLastName() == null ||
                "".equals(extOrgDtlForm.getPersonForm().getLastName().trim()) ||
                extOrgDtlForm.getPersonForm().getFirstName() == null ||
                "".equals(extOrgDtlForm.getPersonForm().getFirstName().trim())) {
            if (orgRep != null) {
                rep = orgRep.getRep();
                hibernateDAO.makeTransient(orgRep);
                reps.remove(orgRep);
                hibernateDAO.makeTransient(rep);
                return;
            }
        } else {
            if (orgRep != null) {
                //            reps.remove(orgRep);
                //            hibernateDAO.makeTransient(orgRep);
                rep = orgRep.getRep();
                rep.setFirstName(extOrgDtlForm.getPersonForm().getFirstName());
                rep.setMiddleName(extOrgDtlForm.getPersonForm().getMiddleName());
                rep.setLastName(extOrgDtlForm.getPersonForm().getLastName());
                hibernateDAO.makePersistent(rep);
            } else {
                rep = new Person();
                rep.setFirstName(extOrgDtlForm.getPersonForm().getFirstName());
                rep.setMiddleName(extOrgDtlForm.getPersonForm().getMiddleName());
                rep.setLastName(extOrgDtlForm.getPersonForm().getLastName());
                hibernateDAO.makePersistent(rep);
                ExternalOrgDetailRep newOrgRep = ExternalOrgDetailRep.create(extOrgDtl, rep);
                newOrgRep.setRank(new Integer(1));
                hibernateDAO.makePersistent(newOrgRep);
            }
        }
//        Person newRep = daoFactory.getPersonDAO()
//                .getPersonById(new Long(extOrgDtlForm.getPersonForm().getPersonId()), false);
//        ExternalOrgDetailRep newOrgRep = ExternalOrgDetailRep.create(extOrgDtl, newRep);
//        newOrgRep.setRank(new Integer(1));
//        hibernateDAO.makePersistent(newOrgRep);
    }

    /*
     * Build new ExternalOrgDetail Representative (Person) properties
     *
    public void buildNewRep() throws InfrastructureException {
        //Build Person
        Person primaryRep = primaryRep = new Person();
        PersonBuilder persBuilder = new PersonBuilder(primaryRep, addBkExtSrcForm, username);
        persBuilder.buildSimpleProperties();
        
        //Build ExtOrgRep
        ExternalOrgDetailRep extOrgDtlRep = ExternalOrgDetailRep.create(extOrgDtl, primaryRep);
    }
    */

    /*
     * Build Primary Mailing Address
     *
    public void buildPrimaryMailingAddress() throws InfrastructureException {
        MailingAddress mlngAddr = extOrgDtl.getPrimaryMailingAddress();
        MailingAddressBuilder mlngAddrBuiler = new MailingAddressBuilder(mlngAddr, addBkExtSrcForm);
        mlngAddrBuiler.buildSimpleProperties();
    }
    */

    /*
     * Build and add new Mailing Address
     *
    public void buildNewPrimaryMailingAddress() throws InfrastructureException {
        MailingAddress mlngAddr = new MailingAddress();
        MailingAddressBuilder mlngAddrBuiler = new MailingAddressBuilder(mlngAddr, addBkExtSrcForm);
        mlngAddrBuiler.buildSimpleProperties();
        ExtOrgDetailMailingAddress.create(extOrgDtl, mlngAddr, username);
    }
    */

    public void buildPrimaryEmailAddressUsingEODForm() throws InfrastructureException {
        Collection emailAddresses = extOrgDtl.getEmailAddresses();
        HibernateDAO hibernateDAO = new HibernateDAO();
        ExtOrgDetailEmailAddress extOrgDetailEmailAddress =
                (ExtOrgDetailEmailAddress) CollectionUtils.getObjectFromCollectionById(emailAddresses, new Integer(1), "rank");
        EmailAddress emailAddress;
        EmailAddressForm emailAddressForm = extOrgDtlForm.getEmailAddressForm();
        String eAddr = emailAddressForm.getEmailAddress().trim();
        if (eAddr == null || "".equals(eAddr)) {
            if (extOrgDetailEmailAddress != null) {
                emailAddress = extOrgDetailEmailAddress.getEmailAddress();
                hibernateDAO.makeTransient(extOrgDetailEmailAddress);
                emailAddresses.remove(emailAddress);
                hibernateDAO.makePersistent(emailAddress);
            }
            return;
        }

        if (extOrgDetailEmailAddress != null) {
            emailAddress = extOrgDetailEmailAddress.getEmailAddress();
            EmailAddressBuilder builder = new EmailAddressBuilder(emailAddress, emailAddressForm);
            builder.buildSimpleProperties();
            hibernateDAO.makePersistent(emailAddress);
        } else {
            emailAddress = new EmailAddress();
            EmailAddressBuilder builder = new EmailAddressBuilder(emailAddress, emailAddressForm);
            builder.buildSimpleProperties();
            hibernateDAO.makePersistent(emailAddress);
            ExtOrgDetailEmailAddress newEmailAddress =
                    ExtOrgDetailEmailAddress.create(extOrgDtl, emailAddress, username);
            newEmailAddress.setRank(new Integer(1));
            hibernateDAO.makePersistent(newEmailAddress);
        }
    }

    /**
     * This method iterates the collection of MailingAddressForm's and updates the DB
     *
     * @throws InfrastructureException
     */

//    public void buildMailingAddresses() throws InfrastructureException{
//        Collection mailingAddresses = extOrgDtl.getMailingAddresses();
//        HibernateDAO hibernateDAO = new HibernateDAO();
//        Collection mailingAddressForms = extOrgDtlForm.getMailingAddresses();
//
//        for (Iterator iterator = mailingAddressForms.iterator(); iterator.hasNext();) {
//            MailingAddressForm mailingAddressForm = (MailingAddressForm) iterator.next();
//            ExtOrgDetailMailingAddress extOrgDetailMailingAddress =
//                    (ExtOrgDetailMailingAddress) CollectionUtils
//                    .getObjectFromCollectionById(mailingAddresses
//                            ,mailingAddressForm.getMailingAddressId()
//                            ,"mailingAddress.mailingAddressId");
//            MailingAddress address = extOrgDetailMailingAddress.getMailingAddress();
//            MailingAddressBuilder builder =
//                    new MailingAddressBuilder(address, mailingAddressForm);
//            builder.buildSimpleProperties();
//            extOrgDetailMailingAddress.setUpdateDate(new Date());
//            extOrgDetailMailingAddress.setUpdatedBy(username);
//            hibernateDAO.makePersistent(extOrgDetailMailingAddress);
//
//        }
//    }
    public void buildMailingAddresses() throws InfrastructureException, ReflectivePropertyException {
        /*TODO update the existing mailing addresses (eventualy delete the deleted ones)
        * create the new mailing addresses
        */
        Collection mailingAddresses = extOrgDtl.getMailingAddresses();  //collection of ExtOrgDetailMailingAddress
        HibernateDAO hibernateDAO = new HibernateDAO();

        Collection mailingAddressForms = extOrgDtlForm.getMailingAddresses();
        //update and deletion
        for (Iterator iterator = mailingAddresses.iterator(); iterator.hasNext();) {
            ExtOrgDetailMailingAddress extOrgDetailMailingAddress = (ExtOrgDetailMailingAddress) iterator.next();
            String mailingAddressId = extOrgDetailMailingAddress.getMailingAddress().getMailingAddressId().toString();
            MailingAddressForm mailingAddressForm =
                    (MailingAddressForm) CollectionUtils.getObjectFromCollectionById(mailingAddressForms, mailingAddressId, "mailingAddressId");
            MailingAddress mailingAddress = extOrgDetailMailingAddress.getMailingAddress();
            if (mailingAddressForm == null) {
                iterator.remove();
                hibernateDAO.makeTransient(extOrgDetailMailingAddress);
                hibernateDAO.makeTransient(mailingAddress);
            } else {
                PropertyUtils.copyProperties(mailingAddress, mailingAddressForm);
                hibernateDAO.makePersistent(mailingAddress);
            }
        }

        //creation of new mailing addresses

        for (Iterator iterator = mailingAddressForms.iterator(); iterator.hasNext();) {
            MailingAddressForm mailingAddressForm = (MailingAddressForm) iterator.next();
            Long mailingAddressId = mailingAddressForm.getMailingAddressId();
            if (mailingAddressId == null) {
                MailingAddress mailingAddress = new MailingAddress();
                PropertyUtils.copyProperties(mailingAddress, mailingAddressForm);
                ExtOrgDetailMailingAddress.create(extOrgDtl, mailingAddress, username);
            }
        }
    }

    public void buildPrimaryPhoneAddressUsingEODForm() throws InfrastructureException, BusinessException, ParseException {
        Collection phones = extOrgDtl.getPhones();
        HibernateDAO hibernateDAO = new HibernateDAO();
        ExtOrgDetailPhone extOrgDetailPhone = (ExtOrgDetailPhone) CollectionUtils.getObjectFromCollectionById(phones, new Integer(1), "rank");
        Phone phone;
        PhoneForm phoneForm = extOrgDtlForm.getPhoneForm();
        String phoneNo = phoneForm.getNumber().trim();
        if (StringUtils.nullOrBlank(phoneNo)) {
            //delete
            if (extOrgDetailPhone != null) {
                phone = extOrgDetailPhone.getPhone();

                // new logic: phone # is coming from TELEPHONE_TBL and ID & number can not be null but END_DATE needs to be set as past date.  Note that null date is active & good.
                Date pastDate = DateUtils.createDate("01-01-2013", "dd-MM-yyyy");
                phone.setEndDate(pastDate);
                hibernateDAO.makePersistent(phone);

// Old logic, did not update.
//                hibernateDAO.makeTransient(extOrgDetailPhone);
//                phones.remove(extOrgDetailPhone);
                //don't delete the phone
            }
            return;
        }

        // for update & new cases when there is a phone #.
        Phone existingPhone = DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getPhoneDAO().getPhoneByNumber(phoneNo);

        if (extOrgDetailPhone != null && !phoneNo.equals(extOrgDetailPhone.getPhone().getNumber().trim())) {

            //update

            if (existingPhone != null) {
                phone = existingPhone;
            } else {
                phone = new Phone();
                PhoneBuilder builder = new PhoneBuilder(phone, phoneForm);
                builder.buildSimpleProperties();
                phone.setPhoneId(null);
                builder.buildDefaultProperties();
                hibernateDAO.makePersistent(phone);
            }
            extOrgDetailPhone.setPhone(phone);

        }
        if (extOrgDetailPhone==null) {
            {
                //new
                phone = new Phone();
                if (existingPhone != null) {
                    phone = existingPhone;
                    phone.setEndDate(null); // null end date is active & good.
                    hibernateDAO.makePersistent(phone);
                } else {
                    PhoneBuilder builder = new PhoneBuilder(phone, phoneForm);
                    builder.buildSimpleProperties();
                    builder.buildDefaultProperties();
                    hibernateDAO.makePersistent(phone);
                }
                ExtOrgDetailPhone newPhone = ExtOrgDetailPhone.create(extOrgDtl, phone, username, Phone.LAND_PHONE);
                newPhone.setRank(new Integer(1));
            }
        }
    }

    public void buildPrimaryFaxAddressUsingEODForm() throws InfrastructureException, BusinessException, ParseException {
        Collection faxes = extOrgDtl.getFaxes();
        HibernateDAO hibernateDAO = new HibernateDAO();
        ExtOrgDetailPhone extOrgDetailPhone = (ExtOrgDetailPhone) CollectionUtils.getObjectFromCollectionById(faxes, new Integer(1), "rank");
        Phone phone;
        PhoneForm phoneForm = extOrgDtlForm.getFaxForm();
        String phoneNo = phoneForm.getNumber().trim();
        if (StringUtils.nullOrBlank(phoneNo)) {
            //delete

            if (extOrgDetailPhone != null) {
                phone = extOrgDetailPhone.getPhone();

                // new logic: phone # is coming from TELEPHONE_TBL and ID & number can not be null but END_DATE needs to be set as past date.  Note that null date is active & good.
                Date pastDate = DateUtils.createDate("01-01-2013", "dd-MM-yyyy");
                phone.setEndDate(pastDate);
                hibernateDAO.makePersistent(phone);

// Old logic, did not update.
//                hibernateDAO.makeTransient(extOrgDetailPhone);
//                faxes.remove(extOrgDetailPhone);
                //don't delete the phone
            }
            return;
        }

        // for update & new when there is a phone #.
        Phone existingPhone = DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getPhoneDAO().getPhoneByNumber(phoneNo);

        if (extOrgDetailPhone != null && !phoneNo.equals(extOrgDetailPhone.getPhone().getNumber().trim())) {

            //update

            if (existingPhone != null) {
                phone = existingPhone;
            } else {
                phone = new Phone();
                PhoneBuilder builder = new PhoneBuilder(phone, phoneForm);
                builder.buildSimpleProperties();
                builder.buildDefaultProperties();
                phone.setPhoneId(null);
                hibernateDAO.makePersistent(phone);
            }
            extOrgDetailPhone.setPhone(phone);
        }
        if (extOrgDetailPhone==null) {
            {
                //new
                phone = new Phone();
                if (existingPhone != null) {
                    phone = existingPhone;
                    phone.setEndDate(null); // null end date is active & good.
                    hibernateDAO.makePersistent(phone);
                } else {
                    PhoneBuilder builder = new PhoneBuilder(phone, phoneForm);
                    builder.buildSimpleProperties();
                    builder.buildDefaultProperties();
                    hibernateDAO.makePersistent(phone);  //
                }
                ExtOrgDetailPhone newPhone = ExtOrgDetailPhone.create(extOrgDtl, phone, username, Phone.FAX);
                newPhone.setRank(new Integer(1));
            }
        }
    }

    /**
     * Build simple ExternalOrgDetail properties from the ExternalOrgDetailForm
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            if (extOrgDtlForm != null) {
                PropertyUtils.copyProperties(extOrgDtl, extOrgDtlForm);
                extOrgDtl.setOrgName(extOrgDtlForm.getOrgName().toUpperCase());
            }
            /*
            else {
                if(addBkExtSrcForm != null) {
                    PropertyUtils.copyProperties(extOrgDtl, addBkExtSrcForm);
                }
            }
            */
        }
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
}
