package us.mn.state.health.builder.materialsrequest;
import java.util.Collection;

import us.mn.state.health.view.materialsrequest.ShoppingListsForm;

public class ShoppingListsFormBuilder  {

    private ShoppingListsForm shoppingListsForm;
    private Collection shoppingLists;
    
    public ShoppingListsFormBuilder(ShoppingListsForm shoppingListsForm,
                                    Collection shoppingLists) {
                                    
        this.shoppingListsForm = shoppingListsForm;
        this.shoppingLists = shoppingLists;
    }
    
    public void buildDefaultProperties() {
        shoppingListsForm.setShoppingListId(null);
        shoppingListsForm.setShoppingListForm(null);
    }
    
    public void buildShoppingLists() {
        shoppingListsForm.setShoppingLists(shoppingLists);
    }
}