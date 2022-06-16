package us.mn.state.health.matmgmt.action;

import java.util.Collection;
import java.util.Iterator;
import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.builder.VendorBuilder;
import us.mn.state.health.builder.VendorFormBuilder;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.matmgmt.director.VendorDirector;
import us.mn.state.health.matmgmt.director.VendorFormDirector;
import us.mn.state.health.matmgmt.util.Form;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.security.ApplicationResources;
import us.mn.state.health.view.common.ExternalOrgDetailForm;
import us.mn.state.health.view.common.MailingAddressForm;
import us.mn.state.health.view.common.VendorAccountForm;
import us.mn.state.health.view.common.VendorContractForm;
import us.mn.state.health.view.common.VendorForm;
import us.mn.state.health.view.common.VendorsForm;

public class VendorAction extends MappingDispatchAction {


    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewMaintainVendors(ActionMapping mapping,
                                             ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;
        vendorsForm.setVendorId(null);
        vendorsForm.setVendorForm(null);
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
//        Collection vendors = daoFactory.getVendorDAO().findAll(false);
        Collection vendors = daoFactory.getVendorDAO().findAllAsDTO();
        vendorsForm.setVendors(vendors);

        return mapping.findForward("success");
    }

    public ActionForward viewCreateNewVendor(ActionMapping mapping,
                                             ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;
        vendorsForm.setCurrentMailingAddressForm(null);
        vendorsForm.setVendorId(null);
        ExternalOrgDetailForm externalOrgDetailForm = new ExternalOrgDetailForm();
        VendorForm vendorForm = new VendorForm();
        vendorForm.setExternalOrgDetailForm(externalOrgDetailForm);
        vendorsForm.setVendorForm(vendorForm);

        return mapping.findForward("success");
    }

    public ActionForward viewEditVendor(ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;
        VendorForm vendorForm = new VendorForm();

        vendorsForm.setCurrentMailingAddressForm(null);
        vendorsForm.setCurrentVendorContractForm(null);

        String cmd = vendorsForm.getCmd();

        //if we click cancel when we add a mailing address or a vendor contract,
        //  we don't want to lose the state of the vendorsForm
        if (!Form.BACK_TO_EDIT_VENDOR.equals(cmd)) {
            vendorsForm.setVendorForm(vendorForm);
            String vendorId = vendorsForm.getVendorId();
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);

            Vendor vendor = daoFactory.getVendorDAO().getVendorById(new Long(vendorId), false);
            VendorFormBuilder builder = new VendorFormBuilder(vendor, vendorsForm.getVendorForm());
            VendorFormDirector director = new VendorFormDirector(builder);
            director.construct();
        }
        vendorsForm.setCmd(null);
        return mapping.findForward("success");
    }

    public ActionForward editMailingAddress(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;
        String mailingAddressId = vendorsForm.getMailingAddressId();
        MailingAddressForm addressForm =
                (MailingAddressForm) CollectionUtils.getObjectFromCollectionById(vendorsForm.getVendorForm()
                        .getExternalOrgDetailForm()
                        .getMailingAddresses(),
                        mailingAddressId,
                        "key");

        vendorsForm.setCurrentMailingAddressForm(addressForm);


        if (Form.VENDOR_MAILING_ADDRESS_EDIT.equals(vendorsForm.getCmd())) {
            vendorsForm.setCmd(null);
            return mapping.findForward("success");
        } else {
            vendorsForm.setCurrentMailingAddressForm(null);
            vendorsForm.setCmd(null);
            return mapping.findForward("return");
        }
    }

    public ActionForward viewAddMailingAddress(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;

        MailingAddressForm addressForm = new MailingAddressForm();
        vendorsForm.setCurrentMailingAddressForm(addressForm);
        vendorsForm.setCmd(null);
        vendorsForm.setMailingAddressId(null);
        return mapping.findForward("success");

    }

    public ActionForward addMailingAddress(ActionMapping mapping,
                                           ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;
        MailingAddressForm mailingAddressForm = vendorsForm.getCurrentMailingAddressForm();
        vendorsForm.getVendorForm().getExternalOrgDetailForm().getMailingAddresses().add(mailingAddressForm);
        vendorsForm.setCurrentMailingAddressForm(null);
        vendorsForm.setCmd("");
        return mapping.findForward("return");
    }

    public ActionForward deleteMailingAddress(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;
        String mailingAddressId = vendorsForm.getMailingAddressId();//get the key of currentMailingAddressForm
        Collection mailingAddressForms = vendorsForm.getVendorForm().getExternalOrgDetailForm().getMailingAddresses();
        for (Iterator iterator = mailingAddressForms.iterator(); iterator.hasNext();) {
            MailingAddressForm addressForm = (MailingAddressForm) iterator.next();
            if (addressForm.getKey().equals(mailingAddressId)) {
                mailingAddressForms.remove(addressForm);
                break;
            }
        }
        return mapping.findForward("return");
    }

    public ActionForward saveVendor(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;
        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        DAOFactory daoFactory = new HibernateDAOFactory();
        String vendorId = vendorsForm.getVendorId();
        Vendor v = null;
        VendorBuilder builder;
        VendorDirector director;
        if (vendorId == null || "".equals(vendorId.trim())) {
//            The case of  create a new vendor
            v = new Vendor();
            builder = new VendorBuilder(v, vendorsForm.getVendorForm(), user.getUsername());
            director = new VendorDirector(builder);
            director.constructNewVendor();
        } else {
            v = daoFactory.getVendorDAO().getVendorById(new Long(vendorId), true);
            builder = new VendorBuilder(v, vendorsForm.getVendorForm(), user.getUsername());
            director = new VendorDirector(builder);
            director.construct();
        }

        daoFactory.getVendorDAO().makePersistent(v);
        viewMaintainVendors(mapping, form, request, response);
        return mapping.findForward("success");
    }

//    public ActionForward editVendorContract(ActionMapping mapping,
//                                            ActionForm form,
//                                            HttpServletRequest request,
//                                            HttpServletResponse response) throws Exception {
//        VendorsForm vendorsForm = (VendorsForm) form;
//        String vendorContractId = vendorsForm.getVendorContractId();
//        VendorContractForm vendorContractForm =
//                (VendorContractForm) CollectionUtils
//                .getObjectFromCollectionById(vendorsForm.getVendorForm().getVendorContracts()
//                        , vendorContractId
//                        , "vendorContractId");
//        vendorsForm.setCurrentVendorContractForm(vendorContractForm);
//
//        if (Form.VENDOR_CONTRACT_EDIT.equals(vendorsForm.getCmd())) {
//            vendorsForm.setCmd(null);
//            return mapping.findForward("success");
//        }
//        else {
//            vendorsForm.setCurrentVendorContractForm(null);
//            return mapping.findForward("return");
//        }
//    }

    public ActionForward editVendorContract(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;

        String vendorContractId = vendorsForm.getVendorContractId();
        VendorContractForm vendorContractForm = (VendorContractForm) CollectionUtils
                .getObjectFromCollectionById(vendorsForm.getVendorForm().getVendorContracts()
                        , vendorContractId
                        , "key");
        vendorsForm.setCurrentVendorContractForm(vendorContractForm);

        if (Form.VENDOR_CONTRACT_EDIT.equals(vendorsForm.getCmd())) {
            vendorsForm.setCmd(null);
            return mapping.findForward("success");
        } else {
            vendorsForm.setCurrentVendorContractForm(null);
            vendorsForm.setCmd(null);
            return mapping.findForward("return");
        }
    }

    public ActionForward editVendorAccount(ActionMapping mapping,
                                           ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;

        String vendorAccountId = vendorsForm.getVendorAccountId();
        VendorAccountForm vendorAccountForm = (VendorAccountForm) CollectionUtils
                .getObjectFromCollectionById(vendorsForm.getVendorForm().getVendorAccounts()
                        , vendorAccountId
                        , "vendorAccountId");
        vendorsForm.setCurrentVendorAccountForm(vendorAccountForm);

        if (Form.VENDOR_ACCOUNT_EDIT.equals(vendorsForm.getCmd())) {
            vendorsForm.setCmd(null);
            return mapping.findForward("success");
        } else {
            vendorsForm.setCurrentVendorAccountForm(null);
            vendorsForm.setCmd(null);
            return mapping.findForward("return");
        }
    }

//    public ActionForward addVendorContract(ActionMapping mapping,
//                                           ActionForm form,
//                                           HttpServletRequest request,
//                                           HttpServletResponse response) throws Exception {
//        VendorsForm vendorsForm = (VendorsForm) form;
//        if (Form.VENDOR_CONTRACT_ADD.equals(vendorsForm.getCmd())) {
//            VendorContractForm vendorContractForm = new VendorContractForm();
//            vendorsForm.setCurrentVendorContractForm(vendorContractForm);
//            vendorsForm.setCmd(null);
//            vendorsForm.setVendorContractId(null);
//            return mapping.findForward("success");
//        }
//        else {
//            VendorContract vendorContract = new VendorContract();
//            PropertyUtils.copyProperties(vendorContract, vendorsForm.getCurrentVendorContractForm());
//            vendorContract.setInsertionDate(new Date());
//            User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
//            vendorContract.setInsertedBy(user.getUsername());
//            DAOFactory factory = new HibernateDAOFactory();
//            Vendor vendor = factory.getVendorDAO().getVendorById(new Long(vendorsForm.getVendorId()), true);
//            vendorContract.setVendor(vendor);
//            HibernateDAO dao = new HibernateDAO();
//            dao.makePersistent(vendorContract);
//            vendor.getVendorContracts().add(vendorContract);
//            factory.getVendorDAO().makePersistent(vendor);
//
//            viewEditVendor(mapping, form, request, response);
//            return mapping.findForward("return");
//        }
//    }

    public ActionForward viewAddVendorContract(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;

        VendorContractForm vendorContractForm = new VendorContractForm();
        vendorsForm.setCurrentVendorContractForm(vendorContractForm);
        vendorsForm.setCmd(null);
        vendorsForm.setVendorContractId(null);
        return mapping.findForward("success");
    }

    public ActionForward viewAddVendorAccount(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;

        VendorAccountForm vendorAccountForm = new VendorAccountForm();
        vendorsForm.setCurrentVendorAccountForm(vendorAccountForm);
        vendorsForm.setCmd(null);
        vendorsForm.setVendorAccountId(null);
        return mapping.findForward("success");
    }

    public ActionForward addVendorContract(ActionMapping mapping,
                                           ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;

        VendorContractForm vendorContractForm = vendorsForm.getCurrentVendorContractForm();
        vendorsForm.getVendorForm().getVendorContracts().add(vendorContractForm);
        vendorsForm.setCurrentVendorContractForm(null);
        vendorsForm.setCmd("");
        return mapping.findForward("return");
    }

    public ActionForward addVendorAccount(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;

        VendorAccountForm vendorAccountForm = vendorsForm.getCurrentVendorAccountForm();
        vendorsForm.getVendorForm().getVendorAccounts().add(vendorAccountForm);
        vendorsForm.setCurrentVendorAccountForm(null);
        vendorsForm.setCmd("");
        return mapping.findForward("return");
    }

    public ActionForward deleteVendorContract(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;
        String vendorContractId = vendorsForm.getVendorContractId();//get the key of currentVendorContractForm
        Collection vendorContractForms = vendorsForm.getVendorForm().getVendorContracts();
        for (Iterator iterator = vendorContractForms.iterator(); iterator.hasNext();) {
            VendorContractForm vendorContractForm = (VendorContractForm) iterator.next();
            if (vendorContractForm.getKey().equals(vendorContractId)) {
                vendorContractForms.remove(vendorContractForm);
                break;
            }
        }
        return mapping.findForward("return");
    }


    public ActionForward deleteVendorAccount(ActionMapping mapping,
                                             ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws Exception {
        VendorsForm vendorsForm = (VendorsForm) form;
        String vendorAccountId = vendorsForm.getVendorAccountId();//get the key of currentVendorAccountForm
        Collection vendorAccountForms = vendorsForm.getVendorForm().getVendorAccounts();
        for (Iterator iterator = vendorAccountForms.iterator(); iterator.hasNext();) {
            VendorAccountForm vendorAccountForm = (VendorAccountForm) iterator.next();
            if (vendorAccountForm.getVendorAccountId().toString().equals(vendorAccountId)) {
                vendorAccountForms.remove(vendorAccountForm);
                break;
            }
        }
        return mapping.findForward("return");
    }
    public ActionForward getVendorInstructions(
            ActionMapping mapping,
                                       ActionForm actionForm,
                                       HttpServletRequest request,
                                       HttpServletResponse response ) {
        Long lValue = new Long(0);
        String tValue = request.getParameter("shipToID");
        if (tValue != null) {
            lValue = Long.valueOf(tValue);
            try {    
                MailingAddress mm = findShippingAddressesById(lValue);
//                String outValue = "<textarea name=\"vendorInstructions\" cols=\"30\" rows=\"2\">" + mm.getInsertedBy()+ "</textarea>";
                String outValue = "";
                if (mm.getAddressNotes() != null) outValue = mm.getAddressNotes();
                response.setContentType("text/html");
                PrintWriter out = null;

                    out = response.getWriter();
                out.println(outValue);
                    out.flush();
            } catch (Exception e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
            return null;
        }
        public MailingAddress findShippingAddressesById(Long inID) throws InfrastructureException {
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
            MailingAddress mm = daoFactory.getMailingAddressDAO().getMailingAddressById(inID,true);
//            MailingAddress mm = (MailingAddress)super.getHibernateTemplate().get(MailingAddress.class, inID);
            return mm;
        }


}
