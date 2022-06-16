package us.mn.state.health.builder.materialsrequest;
import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.materialsrequest.ShoppingList;
import us.mn.state.health.view.materialsrequest.ShoppingListForm;

public class ShoppingListFormBuilder  {

    private ShoppingListForm shoppingListForm;
    private ShoppingList shoppingList;
    private Collection shoppingListCatLineItemForms;
    private Collection shoppingListNonCatLineItemForms;
    
    public ShoppingListFormBuilder(ShoppingListForm shoppingListForm,
                                   ShoppingList shoppingList) {
                                   
        this.shoppingListForm = shoppingListForm;
        this.shoppingList = shoppingList;
    }
    
    public ShoppingListFormBuilder(ShoppingListForm shoppingListForm,
                                   ShoppingList shoppingList,
                                   Collection shoppingListCatLineItemForms,
                                   Collection shoppingListNonCatLineItemForms) {
                                   
        this(shoppingListForm, shoppingList);
        this.shoppingListCatLineItemForms = shoppingListCatLineItemForms;
        this.shoppingListNonCatLineItemForms = shoppingListNonCatLineItemForms;
    }
    
    public void buildSimpleProperties() throws InfrastructureException {
         try {
            PropertyUtils.copyProperties(shoppingListForm, shoppingList);
            shoppingListForm.setOwner(shoppingList.getOwner());
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
    
    public void buildShoppingListCatLineItemForms() {
        shoppingListForm.setShoppingListCatLineItemForms(shoppingListCatLineItemForms);
    }
    
    public void buildShoppingListNonCatLineItemForms() {
        shoppingListForm.setShoppingListNonCatLineItemForms(shoppingListNonCatLineItemForms);
    }
}