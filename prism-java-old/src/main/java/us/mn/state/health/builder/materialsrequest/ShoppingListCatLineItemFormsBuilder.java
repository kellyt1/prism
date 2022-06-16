package us.mn.state.health.builder.materialsrequest;
import java.util.Collection;
import java.util.Iterator;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.materialsrequest.ShoppingListCatalogLineItem;
import us.mn.state.health.view.materialsrequest.ShoppingListCatLineItemForm;

public class ShoppingListCatLineItemFormsBuilder  {

    private boolean catalog;
    private Collection shoppingListLineItems;
    private Collection shoppingListLineItemForms;
    
    public ShoppingListCatLineItemFormsBuilder(Collection shoppingListLineItemForms,
                                            Collection shoppingListLineItems) {
        this.shoppingListLineItemForms = shoppingListLineItemForms;
        this.shoppingListLineItems = shoppingListLineItems;
    }
    
    public void buildShoppingListCatLineItemForms() throws InfrastructureException {
        ShoppingListCatLineItemFormBuilder builder = null;
        for(Iterator i = shoppingListLineItems.iterator(); i.hasNext();) {
            ShoppingListCatLineItemForm form = new ShoppingListCatLineItemForm();
            ShoppingListCatalogLineItem shoppingListLineItem = 
                    (ShoppingListCatalogLineItem)i.next();
            builder = 
                new ShoppingListCatLineItemFormBuilder(form, shoppingListLineItem);
            builder.buildDefaultProperties();
            builder.buildItem();
            builder.buildSimpleProperties();
            shoppingListLineItemForms.add(form);
        }
    }
}