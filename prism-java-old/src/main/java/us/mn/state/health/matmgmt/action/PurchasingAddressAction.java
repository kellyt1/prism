package us.mn.state.health.matmgmt.action;

import org.apache.struts.actions.MappingDispatchAction;

public class PurchasingAddressAction extends MappingDispatchAction {
    
//    /**
//     * Action that prepares view of purchasing Addresses
//     * @throws java.lang.Exception
//     * @return 
//     * @param response
//     * @param request
//     * @param form
//     * @param mapping
//     */
//    public ActionForward viewPurchasingAddresses(ActionMapping mapping, 
//                                                 ActionForm form,
//                                                 HttpServletRequest request,
//                                                 HttpServletResponse response) throws Exception {    
//        PurchasingAddressesForm purchAddressForm = (PurchasingAddressesForm)form;
//        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
//        PurchasingAddressesFormBuilder builder = new PurchasingAddressesFormBuilder(purchAddressForm, daoFactory);
//        PurchasingAddressesFormDirector director = new PurchasingAddressesFormDirector(builder);
//        director.constructViewPurchasingAddresses();
//        
//        return mapping.findForward("success");
//    }
    
//    /**
//     * Action that prepares view for creating purchasing address
//     * @throws java.lang.Exception
//     * @return 
//     * @param response
//     * @param request
//     * @param form
//     * @param mapping
//     */
//    public ActionForward viewCreatePurchasingAddress(ActionMapping mapping, 
//                                                     ActionForm form,
//                                                     HttpServletRequest request,
//                                                     HttpServletResponse response) throws Exception {                                                     
//        PurchasingAddressesForm purchAddressForm = (PurchasingAddressesForm)form;
//        purchAddressForm.setAddBkExtSrcForm(new AddressBookExternalSourceForm());
//        
//        return mapping.findForward("success");                                           
//    }
    
//    /**
//     * Action that prepares view for editing purchasing address
//     * @throws java.lang.Exception
//     * @return 
//     * @param response
//     * @param request
//     * @param form
//     * @param mapping
//     */
//    public ActionForward viewEditPurchasingAddress(ActionMapping mapping, 
//                                                   ActionForm form,
//                                                   HttpServletRequest request,
//                                                   HttpServletResponse response) throws Exception {                                                    
//        PurchasingAddressesForm purchAddressForm = (PurchasingAddressesForm)form;
//        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
//        
//        //Look Add Bk Ext Source
//        Long addBkExtSrcId = new Long(purchAddressForm.getAddressBookExternalSourceId());
//        AddressBookExternalSourceForm addBkExtSrcForm = new AddressBookExternalSourceForm();
//        AddressBookExternalSource addBkExtSource = daoFactory.getAddressBookDAO().getAddBkExtSourceById(addBkExtSrcId);
//            
//        //Build the form
//        AddressBookExternalSourceFormBuilder builder = new AddressBookExternalSourceFormBuilder(addBkExtSrcForm, addBkExtSource);
//        AddressBookExternalSourceFormDirector director = new AddressBookExternalSourceFormDirector(builder);
//        director.construct();
//        
//        purchAddressForm.setAddBkExtSrcForm(addBkExtSrcForm);
//        
//        return mapping.findForward("success");                                          
//    }
    
//    /**
//     * Action that saves a purchasing address
//     * @throws java.lang.Exception
//     * @return 
//     * @param response
//     * @param request
//     * @param form
//     * @param mapping
//     * @deprecated
//     */
//    public ActionForward savePurchasingAddress(ActionMapping mapping, 
//                                               ActionForm form,
//                                               HttpServletRequest request,
//                                               HttpServletResponse response) throws Exception {    
//        PurchasingAddressesForm purchAddressForm = (PurchasingAddressesForm)form;
////        
////        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
////        User user = (User)request.getSession().getAttribute(ApplicationResources.USER);        
////        
////        AddressBookExternalSource addBkExtSrc = null;
////        AddressBookExternalSourceForm addBkExtSrcForm = purchAddressForm.getAddBkExtSrcForm();
////        
////        //Existing Add Bk Ext Src
////        if(addBkExtSrcForm.getAddressBookExternalSourceId() != null) {
////            Long addBkExtSrcId = new Long(purchAddressForm.getAddressBookExternalSourceId());
////            addBkExtSrc = daoFactory.getAddressBookDAO().getAddBkExtSourceById(addBkExtSrcId);                                
////        }
////        else { //New Add Bk Ext Src
////            addBkExtSrc = new AddressBookExternalSource();
////        }
////        
////        //Build it
////        AddressBookExternalSourceBuilder builder = new AddressBookExternalSourceBuilder(addBkExtSrc, 
////                                                                                        addBkExtSrcForm,
////                                                                                        user.getUsername(),
////                                                                                        Group.BUYER_CODE,
////                                                                                        daoFactory);        
////        AddressBookExternalSourceDirector director = new AddressBookExternalSourceDirector(builder, addBkExtSrcForm);
////        director.construct();
////        
////        //Save it
////        daoFactory.getAddressBookDAO().makeAddrBookAddBkExtSourcePersistent(addBkExtSrc);
////       
//        return mapping.findForward("success");                                           
//    }
}