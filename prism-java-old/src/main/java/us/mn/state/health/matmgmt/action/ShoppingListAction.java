package us.mn.state.health.matmgmt.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import us.mn.state.health.builder.materialsrequest.RequestBuilder;
import us.mn.state.health.builder.materialsrequest.ShoppingListBuilder;
import us.mn.state.health.builder.materialsrequest.ShoppingListCatLineItemFormBuilder;
import us.mn.state.health.builder.materialsrequest.ShoppingListCatLineItemFormsBuilder;
import us.mn.state.health.builder.materialsrequest.ShoppingListCatLineItemsBuilder;
import us.mn.state.health.builder.materialsrequest.ShoppingListFormBuilder;
import us.mn.state.health.builder.materialsrequest.ShoppingListNonCatLineItemFormBuilder;
import us.mn.state.health.builder.materialsrequest.ShoppingListNonCatLineItemFormsBuilder;
import us.mn.state.health.builder.materialsrequest.ShoppingListNonCatLineItemsBuilder;
import us.mn.state.health.builder.materialsrequest.ShoppingListRLICatLineItemFormsBuilder;
import us.mn.state.health.builder.materialsrequest.ShoppingListRLINonCatLineItemFormsBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.lang.WrapperUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.matmgmt.director.EditShoppingListFormDirector;
import us.mn.state.health.matmgmt.director.ShoppingListDirector;
import us.mn.state.health.matmgmt.util.Bean;
import us.mn.state.health.matmgmt.util.Form;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.ShoppingList;
import us.mn.state.health.model.util.HibernateModelDetacher;
import us.mn.state.health.model.util.ModelDetacher;
import us.mn.state.health.security.ApplicationResources;
import us.mn.state.health.view.materialsrequest.RequestForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.materialsrequest.ShoppingListCatLineItemForm;
import us.mn.state.health.view.materialsrequest.ShoppingListForm;
import us.mn.state.health.view.materialsrequest.ShoppingListNonCatLineItemForm;
import us.mn.state.health.view.materialsrequest.ShoppingListsForm;

public class ShoppingListAction extends MappingDispatchAction {
    private static final String ADD_CART = "addCart";
    private static final String ADD_CAT_REQ_LN_ITM = "addCatReqLnItm";
    private static final String ADD_ITM = "addItm";
    private static final String ADD_NON_CAT_REQ_LN_ITM = "addNonCatReqLnItm";

    public ActionForward viewAllShoppingLists(ActionMapping mapping,
                                                   ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws Exception {
        ShoppingListsForm slsForm = (ShoppingListsForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        User owner = (User) request.getSession().getAttribute(ApplicationResources.USER);

        Collection shoppingLists = null;// = daoFactory.getShoppingListDAO().findShoppingListsAll();

        String str = request.getParameter("skin");
        if (str != null && str.equals("PRISM2")) {
            shoppingLists = daoFactory.getShoppingListDAO().findParitShoppingListsAll();
        } else {
            shoppingLists = daoFactory.getShoppingListDAO().findShoppingListsAll();
        }

        //build ShoppingListsForm
        buildShoppingListsForm(slsForm, new ShoppingListForm(), shoppingLists, null, null, null, "", request);

        //Set form in session variables to help with Validation
        request.getSession().setAttribute(Form.SHOPPING_LIST_SAVE, slsForm);

        return mapping.findForward("success");
    }
    public ActionForward viewMaintainShoppingLists(ActionMapping mapping,
                                                   ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws Exception {
        ShoppingListsForm slsForm = (ShoppingListsForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        User owner = (User) request.getSession().getAttribute(ApplicationResources.USER);

        //Load Shopping Lists for this user (owner)
        Collection shoppingLists = daoFactory.getShoppingListDAO().findShoppingListsWithUserId(owner.getPersonId());
        //Collection shoppingLists = daoFactory.getShoppingListDAO().findShoppingListsAll();

        //build ShoppingListsForm
        buildShoppingListsForm(slsForm, new ShoppingListForm(), shoppingLists, null, null, null, "", request);

        //Set form in session variables to help with Validation
        request.getSession().setAttribute(Form.SHOPPING_LIST_SAVE, slsForm);

        return mapping.findForward("success");
    }

    public ActionForward viewEditShoppingList(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {
        ShoppingListsForm slsForm = (ShoppingListsForm) form;
        ShoppingListForm slForm = new ShoppingListForm();
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        //Load Shopping List
        Long shoppingListId = new Long(slsForm.getShoppingListId());
        ShoppingList shoppingList = daoFactory.getShoppingListDAO().getShoppingListByIdEagerLoadCatItems(shoppingListId);
        //Build Shopping List Form
        buildShoppingListForm(slForm, shoppingList);

        //Set Current ShoppingListForm in ShoppingListsForm
        slsForm.setShoppingListForm(slForm);

        return mapping.findForward("success");
    }

    public ActionForward createNewShoppingList(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        ShoppingListsForm slsForm = (ShoppingListsForm) form;

        //Set empty Shopping List Form
        slsForm.setShoppingListForm(new ShoppingListForm());

        return mapping.findForward("success");
    }

    public ActionForward removeShoppingList(ActionMapping mapping,
                                            ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {
        ShoppingListsForm slsForm = (ShoppingListsForm) form;
        ShoppingListForm slForm = slsForm.getShoppingListForm();
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        //Load Shopping List
        Long shoppingListId = new Long(slForm.getShoppingListId());
        ShoppingList shoppingList = daoFactory.getShoppingListDAO().getShoppingListById(shoppingListId);

        //Remove Shopping List
        daoFactory.getShoppingListDAO().makeTransient(shoppingList);

        return mapping.findForward("success");
    }

    public ActionForward saveShoppingList(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        ShoppingListsForm slsForm = (ShoppingListsForm) form;
        ShoppingListForm slForm = slsForm.getShoppingListForm();
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        saveShoppingListFormToDB(slForm, daoFactory, request);
        //Determine forward
        String forward = null;
        if (slsForm.getShoppingListAction().equals(ShoppingListAction.ADD_ITM)) {
            forward = "searchCatalog";
        } else if (slsForm.getShoppingListAction().equals(ShoppingListAction.ADD_NON_CAT_REQ_LN_ITM)) {
            forward = "addNonCatItemToCart";
        } else if (slsForm.getShoppingListAction().equals(ShoppingListAction.ADD_CAT_REQ_LN_ITM)) {
            forward = "addCatItemToCart";
        } else if (slsForm.getShoppingListAction().equals(ShoppingListAction.ADD_CART)) {
            forward = "viewCart";
        } else {
            forward = "success";
        }

        return mapping.findForward(forward);
    }

    public ActionForward viewChooseShoppingList(ActionMapping mapping,
                                                ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        ShoppingListsForm slsForm = (ShoppingListsForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        User owner = (User) request.getSession().getAttribute(ApplicationResources.USER);
        Long itemId = WrapperUtils.toLong(request.getParameter("itemId"));
        RequestLineItemForm reqLineItemForm = (RequestLineItemForm) request.getSession().getAttribute(Form.REQ_LN_ITM);
        RequestForm requestForm = (RequestForm) request.getSession().getAttribute(Form.REQUEST);

//    Changed the form to a multi-part form to accomodate file uploads/attachments so the
//    request.getParameter no longer works.  Added the property ShoppingListAction to the RequestLineItemForm.
        String shoppingListAction = request.getParameter("shoppingListAction");
        if (shoppingListAction == null) {
            shoppingListAction = reqLineItemForm.getShoppingListAction();
        }

        //Load Shopping Lists for this user (owner)
        Collection shoppingLists = daoFactory.getShoppingListDAO().findShoppingListsWithUserId(owner.getPersonId());

        //Build Shopping Lists Form 
        buildShoppingListsForm(slsForm, new ShoppingListForm(), shoppingLists,
                itemId, reqLineItemForm, requestForm,
                shoppingListAction, request);

        return mapping.findForward("success");
    }

    public ActionForward addToShoppingList(ActionMapping mapping,
                                           ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        ShoppingListsForm slsForm = (ShoppingListsForm) form;
        ShoppingList shoppingList = null;
        ShoppingListForm slForm = null;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        //addCatReqLnItm
        //addNonCatReqLnItm
        //addItm
        //addCart        
        if (slsForm.getExistingList().booleanValue()) { //This is an existing Shopping List
            //Load Shopping List
            Long shoppingListId = new Long(slsForm.getShoppingListId());
            shoppingList = daoFactory.getShoppingListDAO().getShoppingListByIdEagerLoadCatItems(shoppingListId);
            slForm = new ShoppingListForm();

            //Build Shopping List Form
            buildShoppingListForm(slForm, shoppingList);
//            slsForm.setShoppingListForm(slForm);  This line is repeated below

            //Set Current ShoppingListForm in ShoppingListsForm
            slsForm.setShoppingListForm(slForm);

        } else { //This is a new form with name, comments, etc. already entered (New Shopping List)
            slForm = slsForm.getShoppingListForm();
        }

        //Determine what type of thing we are adding to Shopping List        
        if (slsForm.getShoppingListAction().equals(ShoppingListAction.ADD_ITM)) { //Adding Item to Shopping List
            addItemToShoppingList(slsForm, slForm, request);
        } else
        if (slsForm.getShoppingListAction().equals(ShoppingListAction.ADD_CAT_REQ_LN_ITM)) { //Adding Non Catalog Item to Shopping List
            addCatalogItemToShoppingList(slsForm, slForm, request);
        } else
        if (slsForm.getShoppingListAction().equals(ShoppingListAction.ADD_NON_CAT_REQ_LN_ITM)) { //Adding Non Catalog Item to Shopping List
            addNonCatalogItemToShoppingList(slsForm, slForm, request);
        } else { //Adding Cart to Shopping List
            addCartToShoppingList(slsForm, slForm, request);
        }

        saveShoppingListFormToDB(slForm, daoFactory, request);

        return mapping.findForward("success");
    }

    /**
     * Method that builds the ShoppingList using the ShoppingListForm and then it saves it to the DB
     *
     * @param slForm
     * @param daoFactory
     * @param request
     * @throws InfrastructureException
     */

    private void saveShoppingListFormToDB(ShoppingListForm slForm, DAOFactory daoFactory, HttpServletRequest request) throws InfrastructureException {
        ShoppingList shoppingList;
        //This is a Existing Shopping List
        if (!StringUtils.nullOrBlank(slForm.getShoppingListId())) {
            //Load Shopping List
            Long shoppingListId = new Long(slForm.getShoppingListId());
            shoppingList = daoFactory.getShoppingListDAO().getShoppingListById(shoppingListId);

            //Build Shopping List
            buildShoppingList(shoppingList, slForm, request);
        } else { //This is a new Shopping List
            //Build Shopping List
            shoppingList = new ShoppingList();
            buildShoppingList(shoppingList, slForm, request);

            //Save Shopping List
            daoFactory.getShoppingListDAO().makePersistent(shoppingList);
        }
    }

    public ActionForward addShoppingListToCart(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        ShoppingListsForm slsForm = (ShoppingListsForm) form;
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        ShoppingListForm slForm = slsForm.getShoppingListForm();
        Request shoppingCart = (Request) request.getSession().getAttribute(Bean.SHOPPING_CART);
        User requestor = (User) request.getSession().getAttribute(ApplicationResources.USER);
        boolean newCart = false;

        if (shoppingCart == null) {
            newCart = true;
        }

        if (newCart) {
            shoppingCart = new Request();
            request.getSession().setAttribute(Bean.SHOPPING_CART, shoppingCart);
        }
        RequestBuilder builder = new RequestBuilder(shoppingCart,
                slForm,
                daoFactory,
                requestor);
        if (newCart) {
            builder.buildRequestor();
        }

        for(ShoppingListCatLineItemForm s : (List<ShoppingListCatLineItemForm>)slForm.getShoppingListCatLineItemForms()){
            s.setSelected(Boolean.TRUE);
        }

        builder.buildRequestLineItemsFromShoppingList();

        return mapping.findForward("success");

    }

    public ActionForward flipCatLineItemCheckBoxes(ActionMapping mapping,
                                                   ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) throws Exception {
        ShoppingListsForm slsForm = (ShoppingListsForm) form;
        Boolean selectAllValue = slsForm.getShoppingListForm().getSelectAllCatItems();
        CollectionUtils.assignValueToAll(slsForm.getShoppingListForm().getShoppingListCatLineItemForms(),
                selectAllValue,
                "selected");

        return mapping.findForward("success");
    }

    public ActionForward flipNonCatLineItemCheckBoxes(ActionMapping mapping,
                                                      ActionForm form,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) throws Exception {
        ShoppingListsForm slsForm = (ShoppingListsForm) form;
        Boolean selectAllValue = slsForm.getShoppingListForm().getSelectAllNonCatItems();
        CollectionUtils.assignValueToAll(slsForm.getShoppingListForm().getShoppingListNonCatLineItemForms(),
                selectAllValue,
                "selected");

        return mapping.findForward("success");
    }

    public ActionForward removeSelectedShoppingListItems(ActionMapping mapping,
                                                         ActionForm form,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        ShoppingListsForm slsForm = (ShoppingListsForm) form;
        ShoppingListForm slForm = slsForm.getShoppingListForm();

        Collection catLineItemForms = slForm.getShoppingListCatLineItemForms();
        Collection nonCatLineItemForms = slForm.getShoppingListNonCatLineItemForms();

        //Existing Shopping List
        if (!StringUtils.nullOrBlank(slForm.getShoppingListId())) {
            //Load ShoppingList
            Long shoppingListId = new Long(slForm.getShoppingListId());
            ShoppingList shoppingList = daoFactory.getShoppingListDAO().getShoppingListByIdEagerLoadCatItems(shoppingListId);

            //Remove Selected Catalog Line Items
            Collection catLineItems = shoppingList.getShoppingListCatLineItems();
            CollectionUtils.removeMatchingItems(catLineItemForms,
                    "selected",
                    catLineItems,
                    "shoppingListLineItemId");

            //Remove Selected Non Catalog Line Items                                  
            Collection nonCatLineItems = shoppingList.getShoppingListNonCatLineItems();
            CollectionUtils.removeMatchingItems(nonCatLineItemForms,
                    "selected",
                    nonCatLineItems,
                    "shoppingListLineItemId");

            //Rebuild Shopping List Forms
            buildShoppingListForm(slForm, shoppingList);
        } else {//This is a new Shopping List
            //Remove Selected Catalog Line Item Forms
            CollectionUtils.removeMatchingItems(catLineItemForms, Boolean.TRUE, "selected");

            //Remove Selected Non Catalog Line Item Forms
            CollectionUtils.removeMatchingItems(nonCatLineItemForms, Boolean.TRUE, "selected");
        }

        return mapping.findForward("success");
    }

    //Utility Methods **********************************************************

    public void addCartToShoppingList(ShoppingListsForm slsForm,
                                      ShoppingListForm slForm,
                                      HttpServletRequest request) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        RequestForm requestForm = slsForm.getRequestToAdd();
        Collection reqLineItemForms = requestForm.getRequestLineItemForms();

        //Build Catalog Line Item Forms
        Collection shpListCatLnItmForms = slForm.getShoppingListCatLineItemForms();

        ShoppingListRLICatLineItemFormsBuilder shLstRLICatLnItmFrmBuilder =
                new ShoppingListRLICatLineItemFormsBuilder(shpListCatLnItmForms,
                        reqLineItemForms);
        shLstRLICatLnItmFrmBuilder.buildShoppingListCatLineItemForms();

        //Build Non Catalog Line Items
        Collection shpLstNonCatLnItmForms = slForm.getShoppingListNonCatLineItemForms();
        ShoppingListRLINonCatLineItemFormsBuilder shLstRLINonCatLnItmFrmsBuilder =
                new ShoppingListRLINonCatLineItemFormsBuilder(shpLstNonCatLnItmForms,
                        reqLineItemForms,
                        daoFactory);
        shLstRLINonCatLnItmFrmsBuilder.buildShoppingListNonCatLineItemForms();
    }

    public void addCatalogItemToShoppingList(ShoppingListsForm slsForm,
                                             ShoppingListForm slForm,
                                             HttpServletRequest request) throws Exception {
        RequestLineItemForm reqLineItemForm = slsForm.getReqLineItemToAdd();
        List shoppingListCatLineItemForms = (List) slForm.getShoppingListCatLineItemForms();

        //Check if this Item already in Shopping List
        boolean inList = CollectionUtils.inList(shoppingListCatLineItemForms,
                "item.itemId",
                reqLineItemForm.getItem().getItemId());

        if (inList) {
            //Get Shopping List Form
            ShoppingListCatLineItemForm shLstCatLnItmForm =
                    (ShoppingListCatLineItemForm) CollectionUtils.getObjectFromCollectionById(shoppingListCatLineItemForms,
                            reqLineItemForm.getItem().getItemId(),
                            "item.itemId");
            //Set new Quantity
            if (!StringUtils.nullOrBlank(reqLineItemForm.getQuantity())) {
                shLstCatLnItmForm.setQuantity(reqLineItemForm.getQuantity());
            }
        } else {
            //Build Shopping List Line Item Form for this Non Catalog Request Line Item
            ShoppingListCatLineItemForm shopLstCatLnItemForm = new ShoppingListCatLineItemForm();
            ShoppingListCatLineItemFormBuilder shopLstNonCatLnItmBuilder =
                    new ShoppingListCatLineItemFormBuilder(shopLstCatLnItemForm, reqLineItemForm);
            shopLstNonCatLnItmBuilder.buildDefaultProperties();
            shopLstNonCatLnItmBuilder.buildSimpleProperties();
            shopLstNonCatLnItmBuilder.buildItem();

            //Add Shopping List Cat Line Item Form to Display List

            shoppingListCatLineItemForms.add(0, shopLstCatLnItemForm);
        }
    }

    public void addNonCatalogItemToShoppingList(ShoppingListsForm slsForm,
                                                ShoppingListForm slForm,
                                                HttpServletRequest request) throws Exception {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        RequestLineItemForm reqLineItemForm = slsForm.getReqLineItemToAdd();

        //Build Shopping List Line Item Form for this Non Catalog Request Line Item
        ShoppingListNonCatLineItemForm shopLstNonCatLnItemForm = new ShoppingListNonCatLineItemForm();
        ShoppingListNonCatLineItemFormBuilder shopLstNonCatLnItmBuilder =
                new ShoppingListNonCatLineItemFormBuilder(shopLstNonCatLnItemForm,
                        reqLineItemForm,
                        daoFactory);
        shopLstNonCatLnItmBuilder.buildDefaultProperties();
        shopLstNonCatLnItmBuilder.buildSimpleProperties();
        shopLstNonCatLnItmBuilder.buildCategories();
//        shopLstNonCatLnItmBuilder.buildVendors();

        //Add Shopping List Non Cat Line Item Form to Display List
        List shoppingListNonCatLineItemForms = (List) slForm.getShoppingListNonCatLineItemForms();
        shoppingListNonCatLineItemForms.add(0, shopLstNonCatLnItemForm);
    }

    public void addItemToShoppingList(ShoppingListsForm slsForm,
                                      ShoppingListForm slForm,
                                      HttpServletRequest request) throws Exception {
        List shoppingListCatLineItemForms = (List) slForm.getShoppingListCatLineItemForms();

        //Check if this Item already in Shopping List
        boolean inList = CollectionUtils.inList(shoppingListCatLineItemForms,
                "item.itemId",
                slsForm.getItemToAddId());

        if (inList) { //Item already in Shopping list
            //Get Shopping List Form
            ShoppingListCatLineItemForm shLstCatLnItmForm =
                    (ShoppingListCatLineItemForm) CollectionUtils.getObjectFromCollectionById(shoppingListCatLineItemForms,
                            slsForm.getItemToAddId(),
                            "item.itemId");
            //Increment Quantity
            shLstCatLnItmForm.setQuantity(WrapperUtils.increment(shLstCatLnItmForm.getQuantity()));
        } else { //Item not in Shopping List
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

            //Load Item
            HibernateDAO hibernateDAO = new HibernateDAO();
            hibernateDAO.addQueryParam("id", slsForm.getItemToAddId());
            Item item = (Item) hibernateDAO.executeNamedQuery("findItemByIdEagerLoadStatus").iterator().next();
            ModelDetacher detacher = new HibernateModelDetacher();
            detacher.detachItem(item);

            //Build Shopping List Line Item Form for this Item
            ShoppingListCatLineItemForm shopLstCatLineItmForm = new ShoppingListCatLineItemForm();
            ShoppingListCatLineItemFormBuilder shopLstLnItmFrmBuilder = new ShoppingListCatLineItemFormBuilder(shopLstCatLineItmForm, item);
            shopLstLnItmFrmBuilder.buildDefaultProperties();
            shopLstLnItmFrmBuilder.buildItem();
            shopLstCatLineItmForm.setQuantity("1");

            //Add Shopping List Cat Line Item Form to Display List
            shoppingListCatLineItemForms.add(0, shopLstCatLineItmForm);
        }
    }

    private void buildShoppingListsForm(ShoppingListsForm slsForm,
                                        ShoppingListForm slForm,
                                        Collection shoppingLists,
                                        Long itemIdToAdd,
                                        RequestLineItemForm reqLineItemForm,
                                        RequestForm requestForm,
                                        String shoppingListAction,
                                        HttpServletRequest request) {
        slsForm.setShoppingListForm(slForm);
        slsForm.setShoppingLists(shoppingLists);
        slsForm.setItemToAddId(itemIdToAdd);
        slsForm.setReqLineItemToAdd(reqLineItemForm);
        slsForm.setRequestToAdd(requestForm);
        slsForm.setShoppingListAction(shoppingListAction);

        if (shoppingLists.size() == 0) {
            slsForm.setExistingList(Boolean.FALSE);
        }

        //Set form in session variables to help with Validation
        request.getSession().setAttribute(Form.SHOPPING_LIST_ADD_TO, slsForm);
        request.getSession().setAttribute(Form.SHOPPING_LIST_SAVE, slsForm);
    }

    private void buildShoppingList(ShoppingList shoppingList,
                                   ShoppingListForm slForm,
                                   HttpServletRequest request) throws InfrastructureException {
        User owner = (User) request.getSession().getAttribute(ApplicationResources.USER);

        //Build Shopping List Catalog Line Items
        Collection shoppingListCatLineItemForms = slForm.getShoppingListCatLineItemForms();
        Collection shoppingListCatLineItems = shoppingList.getShoppingListCatLineItems();
        ShoppingListCatLineItemsBuilder catLineItemsBuilder =
                new ShoppingListCatLineItemsBuilder(shoppingListCatLineItems,
                        shoppingListCatLineItemForms,
                        shoppingList);
        catLineItemsBuilder.buildShoppingListCatLineItems();

        //Build Shopping List Non Catalog Line Items                                   
        Collection shoppingListNonCatLineItems = shoppingList.getShoppingListNonCatLineItems();
        Collection shoppingListNonCatLineItemForms = slForm.getShoppingListNonCatLineItemForms();
        ShoppingListNonCatLineItemsBuilder shLstNonCatLnItmsBuilder =
                new ShoppingListNonCatLineItemsBuilder(shoppingListNonCatLineItems,
                        shoppingListNonCatLineItemForms,
                        shoppingList);
        shLstNonCatLnItmsBuilder.buildShoppingListNonCatLineItems();

        //Build Shopping List
        ShoppingListBuilder shoppingListBuilder = new ShoppingListBuilder(shoppingList,
                slForm,
                owner,
                shoppingListCatLineItems,
                shoppingListNonCatLineItems);

        ShoppingListDirector shoppingListDirector = new ShoppingListDirector(shoppingListBuilder);
        shoppingListDirector.construct();
    }

    private void buildShoppingListForm(ShoppingListForm slForm,
                                       ShoppingList shoppingList)
            throws InfrastructureException {

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

        //Build Catalog Line Item Forms
        Collection shoppingListCatLineItems =
                shoppingList.getShoppingListCatLineItems();
        Collection shoppingListCatLineItemForms = new ArrayList();
        ShoppingListCatLineItemFormsBuilder catLineItemFormsBuilder =
                new ShoppingListCatLineItemFormsBuilder(shoppingListCatLineItemForms,
                        shoppingListCatLineItems);
        catLineItemFormsBuilder.buildShoppingListCatLineItemForms();

        //Build Non Catalog Line Item Forms
        Collection shoppingListNonCatLineItems =
                shoppingList.getShoppingListNonCatLineItems();
        Collection shoppingListNonCatLineItemForms = new ArrayList();
        ShoppingListNonCatLineItemFormsBuilder nonCatLineItemFormsBuilder =
                new ShoppingListNonCatLineItemFormsBuilder(shoppingListNonCatLineItemForms,
                        shoppingListNonCatLineItems,
                        daoFactory);
        nonCatLineItemFormsBuilder.buildShoppingListNonCatLineItemForms();

        //Build Shopping List Form
        ShoppingListFormBuilder slFormBuilder =
                new ShoppingListFormBuilder(slForm, shoppingList,
                        shoppingListCatLineItemForms,
                        shoppingListNonCatLineItemForms);
        EditShoppingListFormDirector eslFormDirector =
                new EditShoppingListFormDirector(slFormBuilder);
        eslFormDirector.construct();
    }
}