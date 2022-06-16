package us.mn.state.health.view.materialsrequest;

import us.mn.state.health.model.inventory.Item;

public class ShoppingListCatLineItemForm extends ShoppingListLineItemForm {
  
    private Item item;


    public void setItem(Item item) {
        this.item = item;
    }


    public Item getItem() {
        return item;
    }

     
}