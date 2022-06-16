package us.mn.state.health.builder.materialsrequest;
import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.materialsrequest.ShoppingListNonCatalogLineItem;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.materialsrequest.ShoppingListNonCatLineItemForm;

public class ShoppingListNonCatLineItemFormBuilder  {

    private ShoppingListNonCatLineItemForm shopLstLnItmForm;
    private ShoppingListNonCatalogLineItem shoppingListLineItem;
    private RequestLineItemForm reqLineItemForm;
    private DAOFactory daoFactory;
    
    public ShoppingListNonCatLineItemFormBuilder(ShoppingListNonCatLineItemForm shopLstLnItmForm,
                                                 ShoppingListNonCatalogLineItem shoppingListLineItem,
                                                 DAOFactory daoFactory) {
                                           
        this.shopLstLnItmForm = shopLstLnItmForm;
        this.shoppingListLineItem = shoppingListLineItem;
        this.daoFactory = daoFactory;
    }
    
    public ShoppingListNonCatLineItemFormBuilder(ShoppingListNonCatLineItemForm shopLstLnItmForm,
                                                 RequestLineItemForm reqLineItemForm,
                                                 DAOFactory daoFactory) {
                                           
        this.shopLstLnItmForm = shopLstLnItmForm;
        this.reqLineItemForm = reqLineItemForm;
        this.daoFactory = daoFactory;
        
    }
    
    public void buildCategories() throws InfrastructureException {
        Collection categories =
            daoFactory.getCategoryDAO().findDescendantCategoriesByParentCode(Category.MATERIALS,false);
        shopLstLnItmForm.setCategories(categories);
    }
    
    public void buildDefaultProperties() {
        shopLstLnItmForm.setItemHazardous(Boolean.FALSE);
        shopLstLnItmForm.setSelected(Boolean.FALSE);
    }
    
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            if(shoppingListLineItem != null) {
                PropertyUtils.copyProperties(shopLstLnItmForm, shoppingListLineItem);
                if(shoppingListLineItem.getCategory() != null) {
                    shopLstLnItmForm.setCategoryId(shoppingListLineItem.getCategory().getCategoryId().toString());
                }
                if(shoppingListLineItem.getSuggestedVendor() != null) {
                    shopLstLnItmForm.setSuggestedVendorId(shoppingListLineItem.getSuggestedVendor().getVendorId().toString());
                }
            }
            else {
                PropertyUtils.copyProperties(shopLstLnItmForm, reqLineItemForm);
            }
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
    
    public void buildVendors() throws InfrastructureException {
//        Collection vendors = daoFactory.getVendorDAO().findAll(true);
        Collection vendors = daoFactory.getVendorDAO().findAll();
        shopLstLnItmForm.setVendors(vendors);
    }
}