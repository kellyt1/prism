package us.mn.state.health.builder.materialsrequest;
import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.materialsrequest.ShoppingList;
import us.mn.state.health.view.materialsrequest.ShoppingListForm;

public class ShoppingListBuilder  {

    private User owner;
    private ShoppingList shoppingList;
    private ShoppingListForm shoppingListForm;
    private Collection shoppingListCatLineItems;
    private Collection shoppingListNonCatLineItems;
    
    public ShoppingListBuilder(ShoppingList shoppingList,
                               ShoppingListForm shoppingListForm, User owner) {
                               
        this.shoppingList = shoppingList;
        this.shoppingListForm = shoppingListForm;
        this.owner = owner;
    }
    
    public ShoppingListBuilder(ShoppingList shoppingList,
                               ShoppingListForm shoppingListForm, 
                               User owner,
                               Collection shoppingListCatLineItems,
                               Collection shoppingListNonCatLineItems) {
                               
        this(shoppingList, shoppingListForm, owner);
        this.shoppingListCatLineItems = shoppingListCatLineItems;
        this.shoppingListNonCatLineItems = shoppingListNonCatLineItems;
    }
    
    public void buildOwner() {
        shoppingList.setOwner(owner);
    }
    
    public void buildShoppingListCatLineItems() {
        shoppingList.setShoppingListCatLineItems(shoppingListCatLineItems);
    }
    
    public void buildShoppingListNonCatLineItems() {
        shoppingList.setShoppingListNonCatLineItems(shoppingListNonCatLineItems);
    }
    
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(shoppingList, shoppingListForm);
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }


}