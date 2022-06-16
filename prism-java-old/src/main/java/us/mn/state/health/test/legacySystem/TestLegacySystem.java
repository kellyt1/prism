package us.mn.state.health.test.legacySystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Date;

import junit.framework.TestCase;
import us.mn.state.health.builder.conversionLegacy.VendorBuilder;
import us.mn.state.health.common.exceptions.BusinessException;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.dao.HibernateVendorDAO;
import us.mn.state.health.dao.legacySystem.inventory.HibernateInventoryDAO;
import us.mn.state.health.dao.legacySystem.inventory.HibernateVendrDAO;
import us.mn.state.health.matmgmt.director.conversionLegacy.VendorDirector;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.ExtOrgDetailPhone;
import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.model.common.Phone;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.legacySystem.inventory.Purchase;
import us.mn.state.health.model.legacySystem.inventory.Vendr;
import us.mn.state.health.persistence.HibernateUtil;

public class TestLegacySystem extends TestCase {
    private HibernateInventoryDAO inventoryDAO;
    private HibernateVendrDAO vendrDAO;
    private HibernateVendorDAO vendorDAO;
    private DAOFactory daoFactory;
    private String user;
    private String environment;

    private String vendrId;


    protected void setUp() throws InfrastructureException {
        //here is where we select the environment
        environment = Constants.DEVDB;
//        environment = Constants.TEST;
//        environment = Constants.PROD;
        System.setProperty(Constants.ENV_KEY, environment);
        inventoryDAO = new HibernateInventoryDAO();
        vendorDAO = new HibernateVendorDAO();
        vendrDAO = new HibernateVendrDAO();
        daoFactory = new HibernateDAOFactory();
        vendrId = "0000260-009";
        user = "ochial1";
    }

    public void test1() throws InfrastructureException {
        List result = (List) inventoryDAO.findAll(-1,-1);
        Set errors = new HashSet();
        int i = 0;
        for (Iterator iterator = result.iterator(); iterator.hasNext();) {
            Purchase p = (Purchase) iterator.next();
            try {
                System.out.println("1:" + p.getVendorId1() + " 2:" + p.getVendorId2() + " 3:" + p.getVendorId3());
                i++;
            } catch (Exception e) {
                String err = e.toString();
                int x = err.indexOf("#");
                errors.add(err.substring(x + 1, x + 12));
            }
        }
        List r = new ArrayList(errors);
        Collections.sort(r);
        System.out.println(r);
        System.out.println("Result size = " + result.size());
        System.out.println("Result good = " + i);
    }

    public void testConvertOneVendor() throws InfrastructureException, BusinessException {
        Vendor v = new Vendor();
        Vendr vendr;
        vendr = vendrDAO.getVendrById(vendrId);
        System.out.println(vendr);

        VendorBuilder builder = new VendorBuilder(vendr, v, user, daoFactory);
        VendorDirector director = new VendorDirector(builder);
        director.construct();

        System.out.println("----------------------------------");
        vendorDAO.makePersistent(v);
        System.out.println(v);
    }

    public void convertAllVendors() throws InfrastructureException, BusinessException {
        int i = 0;
        Collection vendrs = vendrDAO.findAll();
        for (Iterator iterator = vendrs.iterator(); iterator.hasNext();) {
            Vendr vendr = (Vendr) iterator.next();
            Vendor v = new Vendor();

            VendorBuilder builder = new VendorBuilder(vendr, v, user, daoFactory);
            VendorDirector director = new VendorDirector(builder);
            director.construct();

            System.out.println("----------------------------------");
            vendorDAO.makePersistent(v);
            i++;
            System.out.println("i=" + i);
        }
        System.out.println("i=" + i);
    }

    /**
     * Method that takes the phone nbr. from the VENDOR_TBL and creates a Phone record in the TELEPHONE table
     *
     * @throws InfrastructureException Note: it was used only for some modifications that were not made the 1st time when we converted the vendors.
     *                                 Now its functionality is included in convertAllVendors()
     */

    public void addPhonesToVendors() throws InfrastructureException, BusinessException {
//        int i = 0;
////        Collection vendrs = vendorDAO.findAll();
//        Collection vendrs = HibernateUtil.getSession().createQuery("from Vendor v where v.legacyId is not null").list();
//        for (Iterator iterator = vendrs.iterator(); iterator.hasNext();) {
//            Vendor vendor = (Vendor) iterator.next();
//            String vendorPhoneNbr = vendor.getTelephone();
//
//            if (vendorPhoneNbr != null && vendor.getLegacyId() != null) {
//                Phone phone = new Phone();
//                phone.setInsertedBy(user);
//                phone.setNumber(vendorPhoneNbr);
//                phone.setInsertionDate(new Date());
//                ExternalOrgDetail externalOrgDetail = vendor.getExternalOrgDetail();
//                ExtOrgDetailPhone extOrgDetailPhone = ExtOrgDetailPhone.create(externalOrgDetail, phone, user, Phone.LAND_PHONE);
//                new HibernateDAO().makePersistent(extOrgDetailPhone);
//                vendor.setTelephone(null);
//                System.out.println(vendor);
//                i++;
//            }
//            vendorDAO.makePersistent(vendor);
//            System.out.println(vendor.getExternalOrgDetail().getEmailAddresses());
//
//        }
//        System.out.println("i=" + i);
    }

    /**
     * Used on oct 19th 2006 - imported the fax numbers for vendors
     * @throws InfrastructureException
     * @throws BusinessException
     */
    public void addFaxesToVendors() throws InfrastructureException, BusinessException {
        int i = 0;
//        Collection vendrs = vendorDAO.findAll();
        Collection vendrs = HibernateUtil.getSession().createQuery("from Vendor v where v.legacyId is not null").list();
        for (Iterator iterator = vendrs.iterator(); iterator.hasNext();) {
            Vendor vendor = (Vendor) iterator.next();

            List vendrsLegacy = (List) new HibernateDAO().executeQuery("from Vendr v where v.idNbr = '"+vendor.getLegacyId()+"' and v.fax is not null");
            if (vendrsLegacy.size()>0) {
                Vendr vendr = (Vendr) vendrsLegacy.iterator().next();
                String fax = vendr.getFax();
                Phone faxPhone = null;
                Phone existingPhone = new HibernateDAOFactory().getPhoneDAO().getPhoneByNumber(fax);
                if (existingPhone==null) {
                    faxPhone = new Phone();
                    faxPhone.setInsertedBy(user);
                    faxPhone.setInsertionDate(new Date());
                    faxPhone.setMdhOwned(Phone.DEFAULT_MDHOWNED);
                    faxPhone.setNumber(fax);
                } else {
                    faxPhone = existingPhone;
                }

                ExtOrgDetailPhone extOrgDetailFax
                        = ExtOrgDetailPhone.create(vendor.getExternalOrgDetail(), faxPhone, user, Phone.FAX);

                i++;
                System.out.println("i="+i);
                new HibernateDAO().makePersistent(vendor);
            }



//            if (vendorPhoneNbr != null && vendor.getLegacyId() != null) {
//                Phone phone = new Phone();
//                phone.setInsertedBy(user);
//                phone.setNumber(vendorPhoneNbr);
//                phone.setInsertionDate(new Date());
//                ExternalOrgDetail externalOrgDetail = vendor.getExternalOrgDetail();
//                ExtOrgDetailPhone extOrgDetailPhone = ExtOrgDetailPhone.create(externalOrgDetail, phone, user, Phone.LAND_PHONE);
//                new HibernateDAO().makePersistent(extOrgDetailPhone);
//                vendor.setTelephone(null);
//                System.out.println(vendor);
//                i++;
//            }
//            vendorDAO.makePersistent(vendor);
//            System.out.println(vendor.getExternalOrgDetail().getEmailAddresses());

        }
        System.out.println("i=" + i);
    }

    /**
     * This method makes the deviceType for the imported vendors "LAND_PHONE"
     * Note: it was used only for some modifications that were not made the 1st time when we converted the vendors.
     * Now its functionality is included in convertAllVendors()
     *
     * @throws InfrastructureException
     */
    public void setLegacyPhoneDeviceType() throws InfrastructureException {
        int i = 0;
        Collection vendrs = HibernateUtil.getSession().createQuery("from Vendor v where v.legacyId is not null").list();
        for (Iterator iterator = vendrs.iterator(); iterator.hasNext();) {
            Vendor vendor = (Vendor) iterator.next();
            if (vendor.getLegacyId() != null) {
                ExternalOrgDetail externalOrgDetail = vendor.getExternalOrgDetail();
                Collection extOrgDetailPhones = externalOrgDetail.getPhones();
                System.out.println("Phones size:" + extOrgDetailPhones.size());
                for (Iterator iterator1 = extOrgDetailPhones.iterator(); iterator1.hasNext();) {
                    ExtOrgDetailPhone extOrgDetailPhone = (ExtOrgDetailPhone) iterator1.next();
                    extOrgDetailPhone.setDeviceType(Phone.LAND_PHONE);
                    new HibernateDAO().makePersistent(extOrgDetailPhone);
                }
                i++;
            }
            vendorDAO.makePersistent(vendor);
        }
        System.out.println("i=" + i);
    }

    /**
     * Method used to delete the vendors inserted from the legacy system with the method convertAllVendors()
     */
    public void deleteVendors() {
        int i = 0;
        HibernateDAO dao = new HibernateDAO();
        try {
//            Collection vendrs = HibernateUtil.getSession().createQuery("from Vendor v where v.legacyId is not null").setMaxResults(1000).list();
//            Collection vendrs = HibernateUtil.getSession().createQuery("from Vendor v where v.vendorId='188915'").setMaxResults(1000).list();
//            Collection vendrs = HibernateUtil.getSession().createQuery("from Vendor v where v.vendorId=191165").list();
            Collection vendrs = HibernateUtil.getSession().createQuery("from Vendor v where v.vendorId=192354").list();
            for (Iterator iterator = vendrs.iterator(); iterator.hasNext();) {
                i++;
                Vendor vendor = (Vendor) iterator.next();
                ExternalOrgDetail externalOrgDetail = vendor.getExternalOrgDetail();

//                Collection vendorContracts = vendor.getVendorContracts();
//
//                for (Iterator iterator1 = vendorContracts.iterator(); iterator1.hasNext();) {
//                    VendorContract vendorContract = (VendorContract) iterator1.next();
//                    dao.makeTransient(vendorContract);
//                }
                dao.makeTransient(vendor);
                dao.makeTransient(externalOrgDetail);
                System.out.println("i=" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void tearDown() throws InfrastructureException {
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();
    }
}