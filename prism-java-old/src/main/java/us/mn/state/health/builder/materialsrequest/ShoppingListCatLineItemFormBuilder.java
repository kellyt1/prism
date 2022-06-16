package us.mn.state.health.builder.materialsrequest;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.materialsrequest.ShoppingListCatalogLineItem;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.materialsrequest.ShoppingListCatLineItemForm;

public class ShoppingListCatLineItemFormBuilder  {

    private Item item;
    private ShoppingListCatLineItemForm shopLstLnItmForm;
    private ShoppingListCatalogLineItem shoppingListLineItem;
    private RequestLineItemForm reqLineItemForm;
    
    public ShoppingListCatLineItemFormBuilder(ShoppingListCatLineItemForm shopLstLnItmForm,
                                              Item item) {
                                           
        this.shopLstLnItmForm = shopLstLnItmForm;
        this.item = item;
    }
    
    public ShoppingListCatLineItemFormBuilder(ShoppingListCatLineItemForm shopLstLnItmForm,
                                              ShoppingListCatalogLineItem shoppingListLineItem) {
                                           
        this.shopLstLnItmForm = shopLstLnItmForm;
        this.shoppingListLineItem = shoppingListLineItem;
    }
    
    public ShoppingListCatLineItemFormBuilder(ShoppingListCatLineItemForm shopLstLnItmForm,
                                              RequestLineItemForm reqLineItemForm) {
                                           
        this.shopLstLnItmForm = shopLstLnItmForm;
        this.reqLineItemForm = reqLineItemForm;
    }
    
    public void buildDefaultProperties() {
        shopLstLnItmForm.setSelected(Boolean.FALSE);
    }
    
    public void buildItem() {
        if(item != null) {
            shopLstLnItmForm.setItem(item);
        }
        else if(reqLineItemForm != null) {
            shopLstLnItmForm.setItem(reqLineItemForm.getItem());
        }
        else {
            shopLstLnItmForm.setItem(shoppingListLineItem.getItem());
        }
    }
    
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            if(reqLineItemForm != null) {
                PropertyUtils.copyProperties(shopLstLnItmForm, reqLineItemForm);
            }
            else {
                PropertyUtils.copyProperties(shopLstLnItmForm, shoppingListLineItem);
            }
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
}