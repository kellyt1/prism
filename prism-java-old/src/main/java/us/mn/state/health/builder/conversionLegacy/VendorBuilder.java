package us.mn.state.health.builder.conversionLegacy;

import java.util.Date;

import us.mn.state.health.common.exceptions.BusinessException;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.ExtOrgDetailMailingAddress;
import us.mn.state.health.model.common.ExtOrgDetailPhone;
import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.Phone;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.legacySystem.inventory.Vendr;

public class VendorBuilder {
    private Vendr vendr;
    private Vendor vendor;
    private String user;
    private DAOFactory daoFactory;

    public VendorBuilder(Vendr vendr, Vendor vendor, String user, DAOFactory daoFactory) {
        this.vendr = vendr;
        this.vendor = vendor;
        this.user = user;
        this.daoFactory = daoFactory;
    }

    public void buildExternalOrgDetail() throws BusinessException, InfrastructureException {
        ExternalOrgDetail externalOrgDetail = new ExternalOrgDetail();
        externalOrgDetail.setOrgName(vendr.getName());
        externalOrgDetail.setOrgCode(StringUtils.generateCodeFromName(vendr.getName()));
        externalOrgDetail.setInsertedBy(user);
        externalOrgDetail.setInsertionDate(new Date());

        MailingAddress mailingAddress = new MailingAddress();

        System.out.println("Vendor ID:" + vendr.getIdNbr());
        System.out.println("Vendor ZIP:" + vendr.getZip());
        if (vendr.getState() == null) {
            mailingAddress.setState("MN");
        }
        else {
            mailingAddress.setState(vendr.getState());
        }
        if (vendr.getZip() == null || "".equals(vendr.getZip())) {
            mailingAddress.setZip("00000");
        }
        else {

            mailingAddress.setZip(vendr.getZip());
        }
        if (vendr.getCity() == null) {
            mailingAddress.setCity("UNKNOWN");
        }
        else {
            mailingAddress.setCity(vendr.getCity());
        }
        if (vendr.getAddr() == null) {
            mailingAddress.setAddress1("UNKNOWN");
        }
        else {
            mailingAddress.setAddress1(vendr.getAddr());
        }
        mailingAddress.setInsertedBy(user);
        mailingAddress.setInsertionDate(new Date());
        ExtOrgDetailMailingAddress extOrgDetailMailingAddress =
                ExtOrgDetailMailingAddress.create(externalOrgDetail, mailingAddress, user);
//        Collection mailingAddresses = new TreeSet();
//        mailingAddresses.add(extOrgDetailMailingAddress);

        Phone phone = new Phone();
        String legacyPhone = vendr.getPhone();
        boolean legacyVendorPhoneExists = (legacyPhone != null);
        if (legacyVendorPhoneExists) {
            // check to see if exists a phone with that phone number
            //if it doesn't exists we create a new phone, if it exists we are using the existing one
            Phone existingPhone = daoFactory.getPhoneDAO().getPhoneByNumber(legacyPhone);
            if (existingPhone != null) {
                phone = existingPhone;
            }
            else {
                phone.setNumber(legacyPhone);
                phone.setInsertionDate(new Date());
                phone.setInsertedBy(user);
            }
            ExtOrgDetailPhone extOrgDetailPhone = ExtOrgDetailPhone.create(externalOrgDetail, phone, user, Phone.LAND_PHONE);
        }
        vendor.setExternalOrgDetail(externalOrgDetail);
    }

    public void buildSimpleProperties() {
        vendor.setLegacyId(vendr.getIdNbr());
        vendor.setInsertionDate(new Date());
        vendor.setInsertedBy(user);
    }
}
