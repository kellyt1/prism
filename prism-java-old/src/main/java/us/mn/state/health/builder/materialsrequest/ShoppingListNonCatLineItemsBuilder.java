package us.mn.state.health.builder.materialsrequest;
import java.util.Collection;
import java.util.Iterator;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.model.materialsrequest.ShoppingList;
import us.mn.state.health.model.materialsrequest.ShoppingListNonCatalogLineItem;
import us.mn.state.health.view.materialsrequest.ShoppingListNonCatLineItemForm;

public class ShoppingListNonCatLineItemsBuilder  {

    private Collection shoppingListNonCatLineItems;
    private Collection shoppingListNonCatLineItemForms;
    private ShoppingList shoppingList;
    
    public ShoppingListNonCatLineItemsBuilder(Collection shoppingListNonCatLineItems,
                                         Collection shoppingListNonCatLineItemForms,
                                         ShoppingList shoppingList) {
                                         
        this.shoppingListNonCatLineItems = shoppingListNonCatLineItems;
        this.shoppingListNonCatLineItemForms = shoppingListNonCatLineItemForms;
        this.shoppingList = shoppingList;
    }
    
    public void buildShoppingListNonCatLineItems() throws InfrastructureException {
        ShoppingListNonCatLineItemForm form = null;
        ShoppingListNonCatalogLineItem shoppingListNonCatLineItem = null;
        ShoppingListNonCatLineItemBuilder builder = null;
        for(Iterator i = shoppingListNonCatLineItemForms.iterator(); i.hasNext();) {
            form = (ShoppingListNonCatLineItemForm)i.next();
             
            //This is a new Line Item
            if(StringUtils.nullOrBlank(form.getShoppingListLineItemId())) {
                shoppingListNonCatLineItem = new ShoppingListNonCatalogLineItem();
                builder = 
                    new ShoppingListNonCatLineItemBuilder(shoppingListNonCatLineItem,
                                                          form, shoppingList);
                builder.buildSimpleProperties();
                builder.buildShoppingList();
                builder.buildCategory();
                builder.buildSuggestedVendor();

                shoppingListNonCatLineItems.add(shoppingListNonCatLineItem);
            }
            else { //This is an update to an existing Line Item
                shoppingListNonCatLineItem = 
                    (ShoppingListNonCatalogLineItem)CollectionUtils.getObjectFromCollectionById(shoppingListNonCatLineItems,
                                                                                             form.getShoppingListLineItemId(),
                                                                                             "shoppingListLineItemId");
                builder = 
                    new ShoppingListNonCatLineItemBuilder(shoppingListNonCatLineItem,
                                                       form);
                builder.buildSimpleProperties();
                builder.buildCategory();
                builder.buildSuggestedVendor();
            }
        }
    }

}