package us.mn.state.health.matmgmt.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.dao.inventory.OrderFormulaDAO;
import us.mn.state.health.view.common.CategoryForm;
import us.mn.state.health.view.inventory.InventoryMaintenanceForm;
import us.mn.state.health.view.inventory.Command;
import us.mn.state.health.view.inventory.ItemsAdvancedSearchForm;
import us.mn.state.health.view.inventory.FixedAssetForm;
import us.mn.state.health.view.inventory.AssetSearchForm;
import us.mn.state.health.view.inventory.SensitiveAssetForm;
import us.mn.state.health.view.purchasing.OrderFormulaMaintainanceForm;
import us.mn.state.health.view.purchasing.OrderFormulaForm;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.StockItemFacility;
import us.mn.state.health.model.inventory.OrderFormula;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.FixedAsset;
import us.mn.state.health.model.inventory.SensitiveAsset;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.builder.CategoryBuilder;
import us.mn.state.health.builder.PersistentCategoryFormBuilder;
import us.mn.state.health.builder.NewCategoryFormBuilder;
import us.mn.state.health.builder.purchasing.OrderFormulaMaintainanceFormBuilder;
import us.mn.state.health.builder.purchasing.OrderFormulaFormBuilder;
import us.mn.state.health.builder.purchasing.OrderFormulaBuilder;
import us.mn.state.health.builder.inventory.InventoryMaintenanceFormBuilder;
import us.mn.state.health.builder.inventory.ItemsAdvancedSearchFormBuilder;
import us.mn.state.health.builder.inventory.FixedAssetFormBuilder;
import us.mn.state.health.builder.inventory.FixedAssetBuilder;
import us.mn.state.health.builder.inventory.AssetSearchFormBuilder;
import us.mn.state.health.builder.inventory.SensitiveAssetFormBuilder;
import us.mn.state.health.builder.inventory.SensitiveAssetBuilder;
import us.mn.state.health.matmgmt.director.OrderFormulaMaintainanceFormDirector;
import us.mn.state.health.matmgmt.director.OrderFormulaFormDirector;
import us.mn.state.health.matmgmt.director.OrderFormulaDirector;
import us.mn.state.health.matmgmt.director.ItemsAdvancedSearchFormDirector;
import us.mn.state.health.matmgmt.director.FixedAssetFormDirector;
import us.mn.state.health.matmgmt.director.FixedAssetDirector;
import us.mn.state.health.matmgmt.director.AssetSearchFormDirector;
import us.mn.state.health.matmgmt.director.SensitiveAssetFormDirector;
import us.mn.state.health.matmgmt.director.SensitiveAssetDirector;
import us.mn.state.health.matmgmt.util.Form;
import us.mn.state.health.security.ApplicationResources;

public class InventoryMaintenanceAction extends MappingDispatchAction {
    public static Log log = LogFactory.getLog(InventoryMaintenanceAction.class);

    /**
     * Category Actions
     */

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward maintainCategory(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        DAOFactory daoFactory = new HibernateDAOFactory();
        CategoryForm categoryForm = (CategoryForm) form;
        Category category = null;
        boolean isTransient = false;
        if (StringUtils.nullOrBlank(categoryForm.getCategoryId())) {
            category = new Category();
            isTransient = true;
        } else {
            Long categoryId = new Long(categoryForm.getCategoryId());
            category = daoFactory.getCategoryDAO().getCategoryById(categoryId, true);
        }
        CategoryBuilder builder = new CategoryBuilder(category, categoryForm);
        builder.buildCategory();
        if (isTransient) {
            daoFactory.getCategoryDAO().makePersistent(category);
        }

        return mapping.findForward("success");
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward selectCategory(ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        InventoryMaintenanceForm imForm = (InventoryMaintenanceForm) form;
        Long parentCategoryId = new Long(request.getParameter("categoryId"));
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        Category category = daoFactory.getCategoryDAO().getCategoryById(parentCategoryId, false);
        imForm.setCategory(category);

        return mapping.findForward("success");
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewEditCategory(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        Long categoryId = new Long(request.getParameter("categoryId"));
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        CategoryForm categoryForm = (CategoryForm) form;

        Category category = daoFactory.getCategoryDAO().getCategoryById(categoryId, true);
        PersistentCategoryFormBuilder persistentCategoryFormBuilder =
                new PersistentCategoryFormBuilder(category, categoryForm);
        persistentCategoryFormBuilder.buildCategoryForm();

        return mapping.findForward("success");
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewNewCategory(ActionMapping mapping,
                                         ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {
        Long categoryId = new Long(request.getParameter("categoryId"));
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        CategoryForm categoryForm = (CategoryForm) form;
        Category category = daoFactory.getCategoryDAO().getCategoryById(categoryId, false);
        NewCategoryFormBuilder newCategoryFormBuilder = new NewCategoryFormBuilder(category, categoryForm);
        newCategoryFormBuilder.buildCategoryForm();

        return mapping.findForward("success");
    }

    /**
     * Maintenance Page Actions
     */

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward viewInventoryMaintenanceItems(ActionMapping mapping,
                                                       ActionForm form,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) throws Exception {
        InventoryMaintenanceForm imForm = (InventoryMaintenanceForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        InventoryMaintenanceFormBuilder builder = new InventoryMaintenanceFormBuilder(imForm, daoFactory);
        builder.buildCategories();
        builder.buildStockItemLocations();

        return mapping.findForward("success");
    }

    /**
     * Stock Item Location Action Methods
     */


    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward selectStockItemFacility(ActionMapping mapping,
                                                 ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception {
        InventoryMaintenanceForm imForm = (InventoryMaintenanceForm) form;
        Long facilityId = new Long(request.getParameter("facilityId"));
        DAOFactory daoFactory = new HibernateDAOFactory();
        StockItemFacility facility = daoFactory.getStockItemFacilityDAO().getStockItemFacilityById(facilityId, false);
        imForm.setFacility(facility);
        imForm.setFacilities(facility.getChildren());

        return mapping.findForward("success");
    }

    /**
     * Action that prepares the view for the page that edits the OrderFormula
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewMaintainOrderFormula(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws Exception {
        OrderFormulaMaintainanceForm orderFormulaMaintainanceForm = (OrderFormulaMaintainanceForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        OrderFormulaMaintainanceFormBuilder builder = new OrderFormulaMaintainanceFormBuilder(daoFactory, orderFormulaMaintainanceForm);
        OrderFormulaMaintainanceFormDirector director = new OrderFormulaMaintainanceFormDirector(builder);
        director.construct();
        OrderFormulaForm orderFormulaForm = new OrderFormulaForm();
        orderFormulaMaintainanceForm.setOrderFormulaForm(orderFormulaForm);
        return mapping.findForward("success");
    }

    public ActionForward maintainOrderFormula(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {
        String forward = "success";
        OrderFormulaMaintainanceForm orderFormulaMaintainanceForm = (OrderFormulaMaintainanceForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        Category category =
                (Category) CollectionUtils.getObjectFromCollectionById(orderFormulaMaintainanceForm.getCategories(),
                        orderFormulaMaintainanceForm.getOrderFormulaForm().getCategoryId(),
                        "categoryId");
        OrderFormulaDAO orderFormulaDAO = daoFactory.getOrderFormulaDAO();
        OrderFormula orderFormula = orderFormulaDAO.findByCategoryCode(category.getCategoryCode());

        if (orderFormulaMaintainanceForm.getCmd().equals(Command.SELECT_CATEGORY)) {
            orderFormulaMaintainanceForm.setCategory(category);
            orderFormulaMaintainanceForm.setCmd("");
            OrderFormulaForm orderFormulaForm = orderFormulaMaintainanceForm.getOrderFormulaForm();
            OrderFormulaFormBuilder builder = new OrderFormulaFormBuilder(daoFactory,
                    orderFormulaForm,
                    orderFormula);
            OrderFormulaFormDirector director = new OrderFormulaFormDirector(builder);
            director.construct();
        } else if (orderFormulaMaintainanceForm.getCmd().equals(Command.SAVE_ORDER_FORMULA) &&
                orderFormulaMaintainanceForm.getOrderFormulaForm().getCategoryId() != null) {
            if (orderFormula == null) {
                orderFormula = new OrderFormula();
            }
            OrderFormulaBuilder builder =
                    new OrderFormulaBuilder(orderFormulaMaintainanceForm.getOrderFormulaForm(),
                            orderFormula,
                            daoFactory);
            OrderFormulaDirector director = new OrderFormulaDirector(builder);
            director.construct();
            daoFactory.getOrderFormulaDAO().makePersistent(orderFormula);
            orderFormulaMaintainanceForm.setCmd("");
        }

        return mapping.findForward(forward);
    }

    public ActionForward viewSearchAllItems(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {
        ItemsAdvancedSearchForm iasForm = (ItemsAdvancedSearchForm) form;
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        ItemsAdvancedSearchFormBuilder iasFormBuilder = new ItemsAdvancedSearchFormBuilder(iasForm, factory);
        ItemsAdvancedSearchFormDirector director = new ItemsAdvancedSearchFormDirector(iasFormBuilder);
        director.constructAdvancedSearchCatalogForm();
        String forward = request.getParameter("forward");
        if (forward != null && forward.equalsIgnoreCase("fixedAsset")) {
            return mapping.findForward("fixedAsset");
        } else if (forward != null && forward.equalsIgnoreCase("sensitiveAsset")) {
            return mapping.findForward("sensitiveAsset");
        } else {
            return mapping.findForward("error");
        }

    }

    public ActionForward searchAllItems(ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        ItemsAdvancedSearchForm iasForm = (ItemsAdvancedSearchForm) form;
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        ItemsAdvancedSearchFormBuilder builder = new ItemsAdvancedSearchFormBuilder(iasForm, factory);
        ItemsAdvancedSearchFormDirector director = new ItemsAdvancedSearchFormDirector(builder);
        try {
            iasForm.setItemTypeSearchOption(Form.ANY_ITEM);
            director.searchInCatalogAndConstruct(); //this will set iasForm.results
        }
        catch (InfrastructureException e) {
            log.error("Error in InventoryMaintenanceAction.searchAllItems", e);
        }
        catch (Exception e) {
            log.error("Error in InventoryMaintenanceAction.searchAllItems", e);
        }

        String forward = request.getParameter("forward");
        if (forward != null && forward.equalsIgnoreCase("fixedAsset")) {
            return mapping.findForward("fixedAsset");
        } else if (forward != null && forward.equalsIgnoreCase("sensitiveAsset")) {
            return mapping.findForward("sensitiveAsset");
        } else {
            return mapping.findForward("error");
        }
    }

    public ActionForward viewAddNewFixedAsset(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {
        FixedAssetForm faForm = (FixedAssetForm) form;
        faForm.setReset(true);
        faForm.reset(mapping, request);
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        String itemId = request.getParameter("itemId");
        Item item = daoFactory.getItemDAO().getItemById(new Long(itemId), false);
        faForm.setItem(item);
        FixedAssetFormBuilder builder = new FixedAssetFormBuilder(null, faForm, daoFactory);
        FixedAssetFormDirector director = new FixedAssetFormDirector(builder);
        director.constructNew();
        return mapping.findForward("success");
    }

    public ActionForward addNewFixedAsset(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        FixedAssetForm faForm = (FixedAssetForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        if (faForm.getCmd().equalsIgnoreCase("addOwnerOrgBdgt")) {
            FixedAssetFormBuilder formBuilder = new FixedAssetFormBuilder(null, faForm, daoFactory);
            formBuilder.addOwnerOrgBudget();
            return mapping.findForward("reload");
        } else {
            User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
            FixedAsset fa = new FixedAsset();
            FixedAssetBuilder builder = new FixedAssetBuilder(fa, faForm, daoFactory, user);
            FixedAssetDirector director = new FixedAssetDirector(builder);
            director.construct();
            daoFactory.getFixedAssetDAO().makePersistent(fa);
            return mapping.findForward("success");
        }
    }

    public ActionForward viewSearchFixedAssets(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        AssetSearchForm searchForm = (AssetSearchForm) form;
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        AssetSearchFormBuilder builder = new AssetSearchFormBuilder(searchForm, factory);
        AssetSearchFormDirector director = new AssetSearchFormDirector(builder);
        director.construct();
        return mapping.findForward("success");
    }

    public ActionForward searchFixedAssets(ActionMapping mapping,
                                           ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        AssetSearchForm searchForm = (AssetSearchForm) form;
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        AssetSearchFormBuilder builder = new AssetSearchFormBuilder(searchForm, factory);
        AssetSearchFormDirector director = new AssetSearchFormDirector(builder);
        try {
            searchForm.setItemTypeSearchOption(Form.ONLY_FIXED_ASSETS);
            director.search(); //this will set searchForm.results
        }
        catch (InfrastructureException e) {
            log.error("Error in InventoryMaintenanceAction.searchFixedAssets()", e);
        }
        catch (Exception e) {
            log.error("Error in InventoryMaintenanceAction.searchFixedAssets()", e);
        }

        return mapping.findForward("success");
    }

    public ActionForward viewEditFixedAsset(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {
        FixedAssetForm faForm = (FixedAssetForm) form;
        faForm.setReset(true);
        faForm.reset(mapping, request);
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        String fixedAssetId = request.getParameter("fixedAssetId");
        FixedAsset fa = (FixedAsset) daoFactory.getFixedAssetDAO().getById(new Long(fixedAssetId), FixedAsset.class);
        FixedAssetFormBuilder builder = new FixedAssetFormBuilder(fa, faForm, daoFactory);
        FixedAssetFormDirector director = new FixedAssetFormDirector(builder);
        director.constructExisting();
        return mapping.findForward("success");
    }

    public ActionForward editFixedAsset(ActionMapping mapping,
                                        ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        FixedAssetForm faForm = (FixedAssetForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        if (faForm.getCmd().equalsIgnoreCase("addOwnerOrgBdgt")) {
            FixedAssetFormBuilder formBuilder = new FixedAssetFormBuilder(null, faForm, daoFactory);
            formBuilder.addOwnerOrgBudget();
            return mapping.findForward("reload");
        } else if (faForm.getCmd().equalsIgnoreCase("removeOwnerOrgBdgt")) {
            String orgBdgtId = request.getParameter("orgBdgtId");
            faForm.setOrgBudgetId(orgBdgtId);
            FixedAssetFormBuilder formBuilder = new FixedAssetFormBuilder(null, faForm, daoFactory);
            formBuilder.removeOwnerOrgBudget();
            return mapping.findForward("reload");
        } else {
            User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
            Long sensitiveAssetId = faForm.getFixedAsset().getSensitiveAssetId();
            FixedAsset fa = (FixedAsset) daoFactory.getFixedAssetDAO().loadById(sensitiveAssetId, FixedAsset.class, true);
            FixedAssetBuilder builder = new FixedAssetBuilder(fa, faForm, daoFactory, user);
            FixedAssetDirector director = new FixedAssetDirector(builder);
            director.construct();
            daoFactory.getFixedAssetDAO().makePersistent(fa);
            return mapping.findForward("success");
        }
    }

    //*************************** SENSITIVE ASSET ACTIONS ********************************************//
    public ActionForward viewAddNewSensitiveAsset(ActionMapping mapping,
                                                  ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws Exception {
        SensitiveAssetForm saForm = (SensitiveAssetForm) form;
        saForm.setReset(true);
        saForm.reset(mapping, request);
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        String itemId = request.getParameter("itemId");
        Item item = daoFactory.getItemDAO().getItemById(new Long(itemId), false);
        saForm.setItem(item);
        SensitiveAssetFormBuilder builder = new SensitiveAssetFormBuilder(null, saForm, daoFactory);
        SensitiveAssetFormDirector director = new SensitiveAssetFormDirector(builder);
        director.constructNew();
        return mapping.findForward("success");
    }

    public ActionForward addNewSensitiveAsset(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {
        SensitiveAssetForm saForm = (SensitiveAssetForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        if (saForm.getCmd().equalsIgnoreCase("addOwnerOrgBdgt")) {
            SensitiveAssetFormBuilder formBuilder = new SensitiveAssetFormBuilder(null, saForm, daoFactory);
            formBuilder.addOwnerOrgBudget();
            return mapping.findForward("reload");
        } else {
            User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
            SensitiveAsset sa = new SensitiveAsset();
            SensitiveAssetBuilder builder = new SensitiveAssetBuilder(sa, saForm, daoFactory, user);
            SensitiveAssetDirector director = new SensitiveAssetDirector(builder);
            director.construct();
            daoFactory.getSensitiveAssetDAO().makePersistent(sa);
            return mapping.findForward("success");
        }
    }

    public ActionForward viewSearchSensitiveAssets(ActionMapping mapping,
                                                   ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws Exception {
        AssetSearchForm searchForm = (AssetSearchForm) form;
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        AssetSearchFormBuilder builder = new AssetSearchFormBuilder(searchForm, factory);
        AssetSearchFormDirector director = new AssetSearchFormDirector(builder);
        director.construct();
        return mapping.findForward("success");
    }

    public ActionForward searchSensitiveAssets(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        AssetSearchForm searchForm = (AssetSearchForm) form;
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        AssetSearchFormBuilder builder = new AssetSearchFormBuilder(searchForm, factory);
        AssetSearchFormDirector director = new AssetSearchFormDirector(builder);
        try {
            searchForm.setItemTypeSearchOption(Form.ONLY_SENSITIVE_ASSETS);
            director.search(); //this will set searchForm.results
        }
        catch (InfrastructureException e) {
            log.error("Error in InventoryMaintenanceAction.searchSensitiveAssets()", e);
        }
        catch (Exception e) {
            log.error("Error in InventoryMaintenanceAction.searchSensitiveAssets()", e);
        }

        return mapping.findForward("success");
    }

    public ActionForward viewEditSensitiveAsset(ActionMapping mapping,
                                                ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        SensitiveAssetForm saForm = (SensitiveAssetForm) form;
        saForm.setReset(true);
        saForm.reset(mapping, request);
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        String sensitiveAssetId = request.getParameter("sensitiveAssetId");
        SensitiveAsset sa = (SensitiveAsset) daoFactory.getSensitiveAssetDAO().getById(new Long(sensitiveAssetId), SensitiveAsset.class);
        SensitiveAssetFormBuilder builder = new SensitiveAssetFormBuilder(sa, saForm, daoFactory);
        SensitiveAssetFormDirector director = new SensitiveAssetFormDirector(builder);
        director.constructExisting();
        return mapping.findForward("success");
    }

    public ActionForward editSensitiveAsset(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {
        SensitiveAssetForm saForm = (SensitiveAssetForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        if (saForm.getCmd().equalsIgnoreCase("addOwnerOrgBdgt")) {
            SensitiveAssetFormBuilder formBuilder = new SensitiveAssetFormBuilder(null, saForm, daoFactory);
            formBuilder.addOwnerOrgBudget();
            return mapping.findForward("reload");
        } else if (saForm.getCmd().equalsIgnoreCase("removeOwnerOrgBdgt")) {
            String orgBdgtId = request.getParameter("orgBdgtId");
            saForm.setOrgBudgetId(orgBdgtId);
            SensitiveAssetFormBuilder formBuilder = new SensitiveAssetFormBuilder(null, saForm, daoFactory);
            formBuilder.removeOwnerOrgBudget();
            return mapping.findForward("reload");
        } else {
            User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
            SensitiveAsset sa = saForm.getSensitiveAsset();
            SensitiveAssetBuilder builder = new SensitiveAssetBuilder(sa, saForm, daoFactory, user);
            SensitiveAssetDirector director = new SensitiveAssetDirector(builder);
            director.construct();
            daoFactory.getFixedAssetDAO().makePersistent(sa);
            return mapping.findForward("success");
        }
    }

}
