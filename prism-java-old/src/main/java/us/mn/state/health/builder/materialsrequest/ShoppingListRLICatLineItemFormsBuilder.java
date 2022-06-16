package us.mn.state.health.builder.materialsrequest;

import java.util.Collection;
import java.util.Iterator;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.materialsrequest.ShoppingListCatLineItemForm;

public class ShoppingListRLICatLineItemFormsBuilder  {

    private Collection reqLineItemForms;
    private Collection shoppingListLineItemForms;
    
    public ShoppingListRLICatLineItemFormsBuilder(Collection shoppingListLineItemForms,
                                                  Collection reqLineItemForms) {
                                                  
        this.shoppingListLineItemForms = shoppingListLineItemForms;
        this.reqLineItemForms = reqLineItemForms;
        
    }
    
    public void buildShoppingListCatLineItemForms() throws InfrastructureException {
    
        ShoppingListRLICatLineItemFormBuilder builder = null;
        ShoppingListCatLineItemForm shLstCatLnItmForm = null;
        RequestLineItemForm reqLineItemForm = null;
        for(Iterator i = reqLineItemForms.iterator(); i.hasNext();) {
        
            reqLineItemForm = (RequestLineItemForm)i.next();
            Item item = reqLineItemForm.getItem();
            
            if(item != null) { //Determines if this is a Catalog Line Item
                shLstCatLnItmForm = (ShoppingListCatLineItemForm)CollectionUtils.
                            getObjectFromCollectionById(shoppingListLineItemForms,
                                                        item.getItemId(), "item.itemId");
                if(shLstCatLnItmForm == null) { //No existing form for this item exists
                    shLstCatLnItmForm = new ShoppingListCatLineItemForm();
                    builder = 
                        new ShoppingListRLICatLineItemFormBuilder(shLstCatLnItmForm,
                                                                  reqLineItemForm);
                    builder.buildDefaultProperties();
                    builder.buildSimpleProperties();
                    builder.buildItem();
                    
                    shoppingListLineItemForms.add(shLstCatLnItmForm);
                }
                else { //Found existing line item for this Item, do update
                    builder = 
                        new ShoppingListRLICatLineItemFormBuilder(shLstCatLnItmForm,
                                                                  reqLineItemForm);
                    builder.buildDefaultProperties();
                    builder.buildSimpleProperties();
                }
            }
          
        }
        
    }
}