package us.mn.state.health.builder.materialsrequest;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.materialsrequest.ShoppingListCatLineItemForm;

public class ShoppingListRLICatLineItemFormBuilder  {

    private ShoppingListCatLineItemForm shopLstLnItmForm;
    private RequestLineItemForm reqLineItemForm;
    
    public ShoppingListRLICatLineItemFormBuilder(ShoppingListCatLineItemForm shopLstLnItmForm,
                                              RequestLineItemForm reqLineItemForm) {
                                           
        this.shopLstLnItmForm = shopLstLnItmForm;
        this.reqLineItemForm = reqLineItemForm;
        
    }
    
    public void buildDefaultProperties() {
        shopLstLnItmForm.setSelected(Boolean.FALSE);
    }
    
    public void buildItem() {
       shopLstLnItmForm.setItem(reqLineItemForm.getItem());
    }
    
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(shopLstLnItmForm, reqLineItemForm);
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
}