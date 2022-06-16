package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.materialsrequest.ShoppingListFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class EditShoppingListFormDirector  {
    
    private ShoppingListFormBuilder builder;
    
    public EditShoppingListFormDirector(ShoppingListFormBuilder builder) {
        this.builder = builder;
    }
    
    public void construct() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildShoppingListCatLineItemForms();
        builder.buildShoppingListNonCatLineItemForms();
    }
}