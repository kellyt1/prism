package us.mn.state.health.builder.materialsrequest;
import java.util.Collection;
import java.util.Iterator;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.materialsrequest.ShoppingListNonCatalogLineItem;
import us.mn.state.health.view.materialsrequest.ShoppingListNonCatLineItemForm;

public class ShoppingListNonCatLineItemFormsBuilder  {

    private Collection shoppingListLineItems;
    private Collection shoppingListLineItemForms;
    private DAOFactory daoFactory;
    
    public ShoppingListNonCatLineItemFormsBuilder(Collection shoppingListLineItemForms,
                                            Collection shoppingListLineItems,
                                            DAOFactory daoFactory) {
                                            
        this.shoppingListLineItemForms = shoppingListLineItemForms;
        this.shoppingListLineItems = shoppingListLineItems;
        this.daoFactory = daoFactory;
    }
    
    public void buildShoppingListNonCatLineItemForms() throws InfrastructureException {
        ShoppingListNonCatLineItemFormBuilder builder = null;
        for(Iterator i = shoppingListLineItems.iterator(); i.hasNext();) {
            ShoppingListNonCatLineItemForm form = new ShoppingListNonCatLineItemForm();
            ShoppingListNonCatalogLineItem shoppingListLineItem = 
                    (ShoppingListNonCatalogLineItem)i.next();
            builder = 
                new ShoppingListNonCatLineItemFormBuilder(form, shoppingListLineItem,
                                                          daoFactory);
            builder.buildDefaultProperties();
            builder.buildCategories();
//            builder.buildVendors();
            builder.buildSimpleProperties();
            shoppingListLineItemForms.add(form);
        }
    }
}