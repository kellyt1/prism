package us.mn.state.health.builder.materialsrequest;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.materialsrequest.ShoppingList;
import us.mn.state.health.model.materialsrequest.ShoppingListNonCatalogLineItem;
import us.mn.state.health.view.materialsrequest.ShoppingListNonCatLineItemForm;

public class ShoppingListNonCatLineItemBuilder  {

    private ShoppingListNonCatalogLineItem shoppingListNonCatLineItem;
    private ShoppingListNonCatLineItemForm shoppingListNonCatLineItemForm;
    private ShoppingList shoppingList;
    
    public ShoppingListNonCatLineItemBuilder(ShoppingListNonCatalogLineItem shoppingListNonCatLineItem,
                                         ShoppingListNonCatLineItemForm shoppingListNonCatLineItemForm) {
                                         
        this.shoppingListNonCatLineItem = shoppingListNonCatLineItem;
        this.shoppingListNonCatLineItemForm = shoppingListNonCatLineItemForm;
    }
    
    public ShoppingListNonCatLineItemBuilder(ShoppingListNonCatalogLineItem shoppingListNonCatLineItem,
                                         ShoppingListNonCatLineItemForm shoppingListNonCatLineItemForm,
                                         ShoppingList shoppingList) {
                                         
        this(shoppingListNonCatLineItem, shoppingListNonCatLineItemForm);
        this.shoppingList = shoppingList;
    }
    
    public void buildCategory() {
        shoppingListNonCatLineItem.setCategory(shoppingListNonCatLineItemForm.getCategory());
    }
    
    public void buildShoppingList() {
        shoppingListNonCatLineItem.setShoppingList(shoppingList);
    }
    
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(shoppingListNonCatLineItem, 
                                         shoppingListNonCatLineItemForm);
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
    
    public void buildSuggestedVendor() {
        shoppingListNonCatLineItem.setSuggestedVendor(shoppingListNonCatLineItemForm.getVendor());
    }

}