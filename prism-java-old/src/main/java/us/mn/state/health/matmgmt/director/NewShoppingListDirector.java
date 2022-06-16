package us.mn.state.health.matmgmt.director;
import us.mn.state.health.builder.materialsrequest.ShoppingListBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class NewShoppingListDirector  {
    
    private ShoppingListBuilder builder;
    
    public NewShoppingListDirector(ShoppingListBuilder builder) {
        this.builder = builder;
    }
    
    public void construct() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildOwner();
    }
}