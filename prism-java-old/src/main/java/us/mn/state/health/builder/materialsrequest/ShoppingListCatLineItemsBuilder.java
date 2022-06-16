package us.mn.state.health.builder.materialsrequest;

import java.util.Collection;
import java.util.Iterator;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.model.materialsrequest.ShoppingList;
import us.mn.state.health.model.materialsrequest.ShoppingListCatalogLineItem;
import us.mn.state.health.view.materialsrequest.ShoppingListCatLineItemForm;

public class ShoppingListCatLineItemsBuilder  {
    private Collection shoppingListCatLineItems;
    private Collection shoppingListCatLineItemForms;
    private ShoppingList shoppingList;
    
    public ShoppingListCatLineItemsBuilder(Collection shoppingListCatLineItems,
                                         Collection shoppingListCatLineItemForms,
                                         ShoppingList shoppingList) {                                         
        this.shoppingListCatLineItems = shoppingListCatLineItems;
        this.shoppingListCatLineItemForms = shoppingListCatLineItemForms;
        this.shoppingList = shoppingList;
    }
    
    public void buildShoppingListCatLineItems() throws InfrastructureException {
        ShoppingListCatLineItemForm form = null;
        ShoppingListCatalogLineItem shoppingListCatLineItem = null;
        ShoppingListCatLineItemBuilder builder = null;
        for(Iterator i = shoppingListCatLineItemForms.iterator(); i.hasNext();) {
            form = (ShoppingListCatLineItemForm)i.next();
             
            //This is a new Line Item
            if(StringUtils.nullOrBlank(form.getShoppingListLineItemId())) {
                shoppingListCatLineItem = new ShoppingListCatalogLineItem();
                builder = new ShoppingListCatLineItemBuilder(shoppingListCatLineItem,
                                                             form, 
                                                             shoppingList);
                builder.buildSimpleProperties();
                builder.buildItem();
                builder.buildShoppingList();
                shoppingListCatLineItems.add(shoppingListCatLineItem);
            }
            else { //This is an update to an existing Line Item
                shoppingListCatLineItem = 
                    (ShoppingListCatalogLineItem)CollectionUtils.getObjectFromCollectionById(shoppingListCatLineItems,
                                                                                             form.getShoppingListLineItemId(),
                                                                                             "shoppingListLineItemId");
                builder = new ShoppingListCatLineItemBuilder(shoppingListCatLineItem, form);
                builder.buildSimpleProperties();
            }
        }
    }
}