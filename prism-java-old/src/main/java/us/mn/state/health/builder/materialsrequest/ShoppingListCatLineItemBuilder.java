package us.mn.state.health.builder.materialsrequest;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.materialsrequest.ShoppingList;
import us.mn.state.health.model.materialsrequest.ShoppingListCatalogLineItem;
import us.mn.state.health.view.materialsrequest.ShoppingListCatLineItemForm;

public class ShoppingListCatLineItemBuilder {
    private ShoppingListCatalogLineItem shoppingListCatLineItem;
    private ShoppingListCatLineItemForm shoppingListCatLineItemForm;
    private ShoppingList shoppingList;
    
    public ShoppingListCatLineItemBuilder(ShoppingListCatalogLineItem shoppingListCatLineItem,
                                          ShoppingListCatLineItemForm shoppingListCatLineItemForm) {                                         
        this.shoppingListCatLineItem = shoppingListCatLineItem;
        this.shoppingListCatLineItemForm = shoppingListCatLineItemForm;
    }
    
    public ShoppingListCatLineItemBuilder(ShoppingListCatalogLineItem shoppingListCatLineItem,
                                          ShoppingListCatLineItemForm shoppingListCatLineItemForm,
                                          ShoppingList shoppingList) {                                         
        this(shoppingListCatLineItem, shoppingListCatLineItemForm);
        this.shoppingList = shoppingList;
    }
    
    public void buildShoppingList() {
        shoppingListCatLineItem.setShoppingList(shoppingList);
    }
    
    public void buildItem() {
        shoppingListCatLineItem.setItem(shoppingListCatLineItemForm.getItem());
    }
    
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(shoppingListCatLineItem, shoppingListCatLineItemForm);
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
}