package us.mn.state.health.matmgmt.action;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import us.mn.state.health.builder.inventory.ItemsAdvancedSearchFormBuilder;
import us.mn.state.health.builder.inventory.PurchaseItemBuilder;
import us.mn.state.health.builder.inventory.PurchaseItemFormBuilder;
import us.mn.state.health.builder.materialsrequest.DeliveryDetailBuilder;
import us.mn.state.health.builder.materialsrequest.DeliveryDetailFormBuilder;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.director.*;
import us.mn.state.health.matmgmt.util.Form;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.AttachedFile;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.PurchaseItem;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.model.materialsrequest.DeliveryDetail;
import us.mn.state.health.persistence.HibernateUtil;
import us.mn.state.health.security.ApplicationResources;
import us.mn.state.health.view.inventory.Command;
import us.mn.state.health.view.inventory.ItemForm;
import us.mn.state.health.view.inventory.ItemsAdvancedSearchForm;
import us.mn.state.health.view.materialsrequest.DeliveryDetailForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;


public class PurchaseItemAction extends MappingDispatchAction {    
    private static Log log = LogFactory.getLog(PurchaseItemAction.class);
    
    public ActionForward viewAdvancedSearchItems(ActionMapping mapping, 
                                                 ActionForm actionForm, 
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception {      
        log.debug("in viewAdvancedSearchItems()");
        ItemsAdvancedSearchForm iasForm = new ItemsAdvancedSearchForm();
        String input = request.getParameter("input");
        if(input == null) {
            input = "advancedSearchItems";
        }
        iasForm.setInput(input);
        iasForm.setCategoryCode("");
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        ItemsAdvancedSearchFormBuilder iasFormBuilder = new ItemsAdvancedSearchFormBuilder(iasForm, factory);
        ItemsAdvancedSearchFormDirector director = new ItemsAdvancedSearchFormDirector(iasFormBuilder);
        director.constructAdvancedSearchCatalogForm();
        request.getSession().setAttribute(Form.ADVANCED_SEARCH_CTLG, iasForm);
        
        return mapping.findForward("success");                                                  
    }

    /**
     * Action that performs item advanced search
     * @throws java.lang.Exception
     * @return 
     * @param response
     * @param request
     * @param actionForm
     * @param mapping
     */
    public ActionForward advancedSearchItems(ActionMapping mapping, 
                                             ActionForm actionForm,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws Exception { 
        log.debug("in advancedSearchItems()");
        ItemsAdvancedSearchForm iasForm = (ItemsAdvancedSearchForm)actionForm;
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        ItemsAdvancedSearchFormBuilder builder = new ItemsAdvancedSearchFormBuilder(iasForm, factory);
        ItemsAdvancedSearchFormDirector director = new ItemsAdvancedSearchFormDirector(builder);
        director.searchInCatalogAndConstruct();
        
        return mapping.findForward("success");                                       
    }
    
    public ActionForward viewCreatePurchaseItem(ActionMapping mapping, 
                                                ActionForm actionForm,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        log.debug("in viewCreatePurchaseItem()");
        ItemForm itemForm = new ItemForm();
        String input = request.getParameter("input");
        itemForm.setInput(input);
        request.getSession().setAttribute(Form.PURCHASE_ITEM, itemForm);
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        
        PurchaseItemFormBuilder builder = new PurchaseItemFormBuilder(itemForm, daoFactory);
        PurchaseItemFormDirector director = new PurchaseItemFormDirector(builder);

        if (request.getSession().getAttribute("skin").toString().equalsIgnoreCase("PRISM2")) {
            director.constructNewForITPurchases();
        }
        else {
            director.constructNew();
        }

        //Used to help with validation for different item form actions
        setItemFormsInSession(itemForm, request.getSession());
        log.debug("in viewCreatePurchaseItem()... saving new Token");
        saveToken(request);
        return mapping.findForward("success");                                   
    }
    
    public ActionForward viewEditPurchaseItem(ActionMapping mapping, 
                                              ActionForm actionForm,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {  
        log.debug("in viewEditPurchaseItem()");
        ItemForm itemForm = new ItemForm();
        String input = request.getParameter("input");
        itemForm.setInput(input);
        request.getSession().setAttribute(Form.PURCHASE_ITEM, itemForm);
        
        //Load Item
        Long itemId = new Long(request.getParameter("itemId"));
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        Item item = daoFactory.getItemDAO().getItemById(itemId, true);

        //todo - this should maybe be moved out of the action t.r.
        for (Object o : item.getAttachedFiles()) {
            Hibernate.initialize((AttachedFile) o);
        }

        
        PurchaseItemFormBuilder builder = new PurchaseItemFormBuilder(itemForm, item, daoFactory);
        PurchaseItemFormDirector director = new PurchaseItemFormDirector(builder);

        if (request.getSession().getAttribute("skin").toString().equalsIgnoreCase("PRISM2")) {
            director.constructEditForITPurchases();
        }
        else {
            director.constructEdit();
        }

        //Used to help with validation for different item form actions
        setItemFormsInSession(itemForm, request.getSession());
        log.debug("in viewEditPurchaseItem()... saving new Token");
        saveToken(request);
        return mapping.findForward("success");                                   
    }
    
    public ActionForward addPurchaseItemVendor(ActionMapping mapping, 
                                               ActionForm actionForm,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {  
        log.debug("in addPurchaseItemVendor()");
        
        ItemForm itemForm = (ItemForm)actionForm;
        Item item = null;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        User actor = (User)request.getSession().getAttribute(ApplicationResources.USER);
        
        if(!StringUtils.nullOrBlank(itemForm.getItemId())) {//Load item
            Long itemId = new Long(itemForm.getItemId());
            item = daoFactory.getItemDAO().getItemById(itemId, true);
        }
        else { //Create a new purchase item
            item = new PurchaseItem();
        }
        
        itemForm.setItemVendorId(null);
        itemForm.setContractType("");
        
        //Build Item
        PurchaseItemBuilder piBuilder = new PurchaseItemBuilder(item, itemForm, actor, daoFactory);
        
        PurchaseItemDirector director = new PurchaseItemDirector(piBuilder);
        director.construct();
        
        //Save the item
        daoFactory.getItemDAO().makePersistent(item);
        
        //Now build Item Vendor
        piBuilder.buildAddItemVendor();
        
        //Rebuild view
        PurchaseItemFormBuilder pifBuilder = new PurchaseItemFormBuilder(itemForm, item, daoFactory);
        PurchaseItemFormDirector pifDirector = new PurchaseItemFormDirector(pifBuilder);

        if (request.getSession().getAttribute("skin").toString().equalsIgnoreCase("PRISM2")) {
            pifDirector.constructEditForITPurchases();
        }
        else {
            pifDirector.constructEdit();
        }

        return mapping.findForward("success");                                        
    }
    
    public ActionForward removePurchaseItemVendor(ActionMapping mapping, 
                                                  ActionForm actionForm,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws Exception {          
        log.debug("in removePurchaseItemVendor()");
        ItemForm itemForm = (ItemForm)actionForm;
        Item item;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        User actor = (User)request.getSession().getAttribute(ApplicationResources.USER);
        
        if(!StringUtils.nullOrBlank(itemForm.getItemId())) {//Load item
            Long itemId = new Long(itemForm.getItemId());
            item = daoFactory.getItemDAO().getItemById(itemId, true);
        }
        else { //Create a new purchase item
            item = new PurchaseItem();
        }
        
        //Build Item
        PurchaseItemBuilder piBuilder = new PurchaseItemBuilder(item, itemForm, actor, daoFactory);
        
        PurchaseItemDirector director = new PurchaseItemDirector(piBuilder);
        director.construct();
        
        //Now Remove Item Vendor
        piBuilder.buildRemoveItemVendor();
        
        //Rebuild view
        PurchaseItemFormBuilder pifBuilder = new PurchaseItemFormBuilder(itemForm, item, daoFactory);
        pifBuilder.buildAvailableVendors();
        pifBuilder.buildItemVendors();

        itemForm.setItemVendorId(null);
        itemForm.setContractType("");
        
        return mapping.findForward("success");                                           
    }
    
    public ActionForward choosePurchaseItemContractType(ActionMapping mapping, 
                                                        ActionForm actionForm,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) throws Exception {                                                        
        ItemForm itemForm = (ItemForm)actionForm;
        return mapping.findForward("success");
    }
    
    public ActionForward viewPurchaseItemVendorDetail(ActionMapping mapping, 
                                                      ActionForm actionForm,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) throws Exception { 
        log.debug("in viewPurchaseItemVendorDetail()");
        ItemForm itemForm = (ItemForm)actionForm;
        if(itemForm.getItemVendor() == null) {
            return mapping.findForward("success");
        }
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        Long itemId = new Long(itemForm.getItemId());
        Item item = daoFactory.getItemDAO().getItemById(itemId, true);
        User actor = (User)request.getSession().getAttribute(ApplicationResources.USER);
        itemForm.resetVendorContractDetails();
        //Save Current State
        PurchaseItemBuilder piBuilder = new PurchaseItemBuilder(item, itemForm, actor, daoFactory);
        piBuilder.buildVendorContract();
        
        //Build New Vendor Contract Detail View
        PurchaseItemFormBuilder pifBuilder = new PurchaseItemFormBuilder(itemForm, item, daoFactory);
        pifBuilder.buildItemVendorContractDetail();
        
        return mapping.findForward("success");                                          
    }
    
    public ActionForward savePurchaseItem(ActionMapping mapping, 
                                          ActionForm actionForm,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {     
        log.debug("in savePurchaseItem()");
        ItemForm itemForm = (ItemForm)actionForm;
        String cmd = itemForm.getCmd();
        if (cmd.equals(Command.CHANGE_DELIVERY_INFO)) {
            return mapping.findForward(Command.CHANGE_DELIVERY_INFO);
        }
        if (cmd.equals(Command.REMOVE_DELIVERY_INFO)) {
            return mapping.findForward(Command.REMOVE_DELIVERY_INFO);
        }
        if(isTokenValid(request)) {
            resetToken(request);
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
            User actor = (User)request.getSession().getAttribute(ApplicationResources.USER);
            Item item = null;
            Long itemId;

            if(!StringUtils.nullOrBlank(itemForm.getItemId())) { //Existing Item, so load it
                itemId = new Long(itemForm.getItemId());
                item = daoFactory.getItemDAO().getItemById(itemId, true);

                if (itemForm.getEndDatePurchaseItem()) {
                   PurchaseItem pItem = (PurchaseItem) daoFactory.getPurchaseItemDAO().getItemById(itemId);
                   pItem.setEndDate(new Date());
                   daoFactory.getPurchaseItemDAO().makePersistent(pItem);
                }
            }
            else { //new item
                item = new PurchaseItem();
            }
            PurchaseItemBuilder builder = new PurchaseItemBuilder(item, itemForm, actor, daoFactory);
            PurchaseItemDirector director = new PurchaseItemDirector(builder);
            director.construct();

            AttachedFile attachedFile = new AttachedFile();
            if(itemForm.getPrintSpecFile().getFileName() != null) {
//                attachedFile.setItem(stockItem);        //TODO - TODD- check/verify this
                    attachedFile.setFileName(itemForm.getPrintSpecFile().getFileName());
                    attachedFile.setFileSize(new Long(itemForm.getPrintSpecFile().getFileSize()));
                    attachedFile.setFileContents(itemForm.getPrintSpecFile().getFileData());
                    item.addAttachedFile(attachedFile);
            }
            daoFactory.getItemDAO().makePersistent(item);
        }
        return mapping.findForward("success");
    }


    public ActionForward loadPurchaseItems(ActionMapping mapping,
                                          ActionForm actionForm,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        log.info("in loadPurchaseItems()");
            ItemForm itemForm = (ItemForm)actionForm;
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
            User actor = (User)request.getSession().getAttribute(ApplicationResources.USER);
            PurchaseItem pItem = null;
            Long itemId;

            try {
                String envDir = System.getProperty("PRISM_DATA") == null ? System.getenv("PRISM_DATA") : System.getProperty("PRISM_DATA");
                 CSVReader reader = new CSVReader(new FileReader(envDir+"/PARITitems.csv"));

                 String [] nextLine;
                 String category;
                 while ((nextLine = reader.readNext()) != null) {
                     // nextLine[] is an array of values from the line
                     log.info(nextLine[0] + "  |  " + nextLine[1]  + "  |  " + nextLine[2] + "  |  " + nextLine[3]+ " |  etc...");

                     if (nextLine[0].compareToIgnoreCase("Description") != 0) {  //skip header line if present

                         pItem = new PurchaseItem();
                         pItem.setDescription(nextLine[0]);
                         category = nextLine[1];
                         if (category.equalsIgnoreCase("Miscellaneous")) {
                             category = "Miscellaneous IT";
                         }
                         pItem.setCategory(daoFactory.getCategoryDAO().findByCategoryName(category));
                         pItem.setModel(nextLine[2]);

                         //nextLine[3] is Dispense Unit - all are EACH at this time
                         pItem.setDispenseUnit(daoFactory.getUnitDAO().findUnitByCode(Unit.CODE_EACH));

                         pItem.setDispenseUnitCost(new Double(nextLine[4]));

                         pItem.setInsertedBy(actor.getNdsUserId());
                         pItem.setInsertionDate(new Date());

                         //VENDOR nextLine[5]
    //                     pItem.setDescription(nextLine[0]);

                         //Spec file (PDF) nextLine[6]

                         //initialize required (not nullable) columns
                         pItem.setEconomicOrderQty(0);
                         pItem.setHazardous(false);
                         pItem.setManufacturer(daoFactory.getManufacturerDAO().findManufacturerByCode("UNKWN"));

                         //File specFile = new File(nextLine[6].replace('\\','/'));
                         File specFile = new File(nextLine[6]);
                         AttachedFile attachedFile = new AttachedFile();

                         if (specFile.exists()) {
                             attachedFile.setFileName(specFile.getName());
                             attachedFile.setFileSize(specFile.length());
                             InputStream creader = new FileInputStream(specFile);
                             attachedFile.setFileContentsHibernate(Hibernate.createBlob(creader));
                             String fName = specFile.getName();
                             attachedFile.setFileType(fName.substring(fName.lastIndexOf('.')+1));
                             attachedFile.setFileExtension(fName.substring(fName.lastIndexOf('.')+1));
                             attachedFile.setFileDate(new java.sql.Date(specFile.lastModified()));

                             pItem.addAttachedFile(attachedFile);
                         }

                         daoFactory.getPurchaseItemDAO().makePersistent(pItem);
                         HibernateUtil.getSession().saveOrUpdate(attachedFile);
                     }
                     System.out.println(nextLine[0] + "  |  " + nextLine[1]  + "  |  " + nextLine[2] + "  |  " + nextLine[3]+ "etc...");
                 }
             } catch (FileNotFoundException e)   {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             }
        return null;
    }




    public ActionForward updatePurchaseItems(ActionMapping mapping,
                                          ActionForm actionForm,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        log.info("in updatePurchaseItems()");
            ItemForm itemForm = (ItemForm)actionForm;
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
            User actor = (User)request.getSession().getAttribute(ApplicationResources.USER);
            PurchaseItem pItem = null;
            Long itemId;

            try {
                String envDir = System.getProperty("PRISM_DATA") == null ? System.getenv("PRISM_DATA") : System.getProperty("PRISM_DATA");
                CSVReader reader = new CSVReader(new FileReader(envDir+"/PARITitems.csv"));
                
                 String [] nextLine;
                 String category;
                 while ((nextLine = reader.readNext()) != null) {
                     log.info(nextLine[0] + "  |  " + nextLine[1]  + "  |  " + nextLine[2] + "  |  " + nextLine[3]+ " |  etc...");
                     
                     // nextLine[] is an array of values from the line
                     if (nextLine[0].compareToIgnoreCase("Description") != 0) {  //skip header line if present

                         Criterion modelEq = Restrictions.eq("model",nextLine[2]);
                         Criterion descriptionEq = Restrictions.eq("description",nextLine[0]);
                         Criterion endDateIsNull = Restrictions.isNull("endDate");
//                         Criterion itemIdEq = Restrictions.eq("itemId",10710269l);
//                         Criterion categoryEq = Restrictions.ge("category.getName()",nextLine[1]);
//                         Criterion categoryIdGe = Restrictions.ge("category.id",600000);
//                         Criterion categoryIdLe = Restrictions.le("category_id",600100);
//                         Session session = HibernateUtil.getSession();

                         Criteria crit =  HibernateUtil.getSession().createCriteria(PurchaseItem.class);
                         crit.add(modelEq);
                         crit.add(descriptionEq);
                         crit.add(endDateIsNull);
//                         crit.add(itemIdEq);
//                         crit.add(categoryIdGe);
//                         crit.add(categoryIdLe);
//                         crit.add(categoryEq);

                         pItem = (PurchaseItem) crit.uniqueResult();
                         if (pItem == null) {
                             pItem = new PurchaseItem();   
                             pItem.setInsertedBy(actor.getNdsUserId());
                             pItem.setInsertionDate(new Date());
                         }

                         pItem.setDescription(nextLine[0]);
                         category = nextLine[1];
                         if (category.equalsIgnoreCase("Miscellaneous")) {
                             category = "Miscellaneous IT";
                         }
                         pItem.setCategory(daoFactory.getCategoryDAO().findByCategoryName(category));
                         pItem.setModel(nextLine[2]);

                         //nextLine[3] is Dispense Unit - all are EACH at this time
                         pItem.setDispenseUnit(daoFactory.getUnitDAO().findUnitByCode(Unit.CODE_EACH));

                         pItem.setDispenseUnitCost(new Double(nextLine[4]));

                         pItem.setLastUpdatedBy(actor.getNdsUserId());
                         pItem.setLastUpdatedDate(new Date());

                         //VENDOR nextLine[5]
    //                     pItem.setDescription(nextLine[0]);

                         //Spec file (PDF) nextLine[6]

                         //initialize required (not nullable) columns
                         pItem.setEconomicOrderQty(0);
                         pItem.setHazardous(false);
                         pItem.setManufacturer(daoFactory.getManufacturerDAO().findManufacturerByCode("UNKWN"));
//                         String filePathAndName = nextLine[6];
//                         filePathAndName.replace('\\','/');
                         File specFile = new File(nextLine[6]);
                         AttachedFile attachedFile = new AttachedFile();

                         if (specFile.exists()) {


//                attachedFile.setItem(stockItem);        //TODO - TODD- check/verify this
                             attachedFile.setFileName(specFile.getName());
                             attachedFile.setFileSize(specFile.length());
                             InputStream creader = new FileInputStream(specFile);
                             attachedFile.setFileContentsHibernate(Hibernate.createBlob(creader));
                             String fName = specFile.getName();
                             attachedFile.setFileType(fName.substring(fName.lastIndexOf('.')+1));
                             attachedFile.setFileExtension(fName.substring(fName.lastIndexOf('.')+1));
                             attachedFile.setFileDate(new java.sql.Date(specFile.lastModified()));
                             pItem.addAttachedFile(attachedFile);
                         }

                         daoFactory.getPurchaseItemDAO().makePersistent(pItem);
                         HibernateUtil.getSession().saveOrUpdate(attachedFile);
                     }
                 }
             } catch (FileNotFoundException e)   {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             }

        return null;
    }


    private void setItemFormsInSession(ItemForm itemForm, HttpSession session) {
        session.setAttribute(Form.PURCHASE_ITEM_ADD_VENDOR, itemForm);
        session.setAttribute(Form.PURCHASE_ITEM_REMOVE_VENDOR, itemForm);
    }

    /**
     * Action that prepares view of the Change Delivery Detail page
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward if operation failed
     * @throws Exception
     */
    public ActionForward viewChangeItemDefaultDeliveryDetails(ActionMapping mapping,
                                                   ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws Exception {
        ItemForm itemForm = (ItemForm) form;
        DeliveryDetailForm deliveryDetailForm = new DeliveryDetailForm();
        DeliveryDetail deliveryDetail = itemForm.getDeliveryDetail();
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
        User requestor = (User)request.getSession().getAttribute(ApplicationResources.USER);

        //Build the DeliveryDetailForm
        DeliveryDetailFormBuilder builder = new DeliveryDetailFormBuilder(deliveryDetailForm, deliveryDetail, requestor, daoFactory);
        DeliveryDetailFormDirector director = new DeliveryDetailFormDirector(builder);
        if(DeliveryDetailForm.DELIVER_TO_CITIZEN.equals(deliveryDetailForm.getDeliverToType())) {
            //user wants to deliver to private citizen
            director.constructForPrivateCitizenOption();
        }
        else if(DeliveryDetailForm.DELIVER_TO_MDH.equals(deliveryDetailForm.getDeliverToType())){
            //user wants to deliver to an MDH employee
            director.constructForMdhEmployeeOption();
        }
        else {
            //user wants to deliver to an external org
            director.constructForExternalOrgOption();
        }
        itemForm.setDeliveryDetailForm(deliveryDetailForm);
        return mapping.findForward("success");
    }

    /**
     * Action sets the Request.deliveryDetail property
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return a successful forward if operation completed or a failure forward
     *         if operation failed
     * @throws Exception
     */
    public ActionForward changeItemDefaultDeliveryDetails(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        ItemForm itemForm = (ItemForm) form;
        DeliveryDetailForm deliveryDetailForm = itemForm.getDeliveryDetailForm();
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);

        String cmd = itemForm.getCmd();

        if(cmd.equals(Command.RELOAD)) {
            //Build the DeliveryDetailForm
            DeliveryDetailFormBuilder builder = new DeliveryDetailFormBuilder(deliveryDetailForm, daoFactory);
            DeliveryDetailFormDirector director = new DeliveryDetailFormDirector(builder);

            if(DeliveryDetailForm.DELIVER_TO_CITIZEN.equals(deliveryDetailForm.getDeliverToType())) {
                //user wants to deliver to private citizen
                deliveryDetailForm.setOrganizationId("");
                director.constructForPrivateCitizenOption();
            }
            else if(DeliveryDetailForm.DELIVER_TO_MDH.equals(deliveryDetailForm.getDeliverToType())){
                //user wants to deliver to an MDH employee
                deliveryDetailForm.setOrganizationId("");
                director.constructForMdhEmployeeOption();
                if(request.getSession().getAttribute("requestType").equals("SWIFT")){
                    deliveryDetailForm.setRecipientId(null);
                    deliveryDetailForm.setRecipients(null);
                }
            }
            else {
                //user wants to deliver to an external org
                deliveryDetailForm.setRecipientId("");
                director.constructForExternalOrgOption();
            }
            return mapping.findForward("reload");
        }
        else {
            DeliveryDetail deliveryDetail = new DeliveryDetail();
            DeliveryDetailBuilder builder = new DeliveryDetailBuilder(deliveryDetailForm, deliveryDetail, daoFactory);
            DeliveryDetailDirector director = new DeliveryDetailDirector(builder);
            director.construct();
            itemForm.setDeliveryDetail(deliveryDetail);
            deliveryDetailForm.reset(mapping, request);
            return mapping.findForward("success");
        }
    }

    public ActionForward removeItemDefaultDeliveryDetails(ActionMapping mapping,
                                                          ActionForm form,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) throws Exception {
        ItemForm itemForm = (ItemForm) form;
        itemForm.setDeliveryDetailForm(null);
        itemForm.setDeliveryDetail(null);
        return mapping.findForward("success");
    }

}