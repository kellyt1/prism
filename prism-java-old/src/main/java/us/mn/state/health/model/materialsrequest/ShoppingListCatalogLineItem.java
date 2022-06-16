package us.mn.state.health.model.materialsrequest;
import us.mn.state.health.model.inventory.Item;

public class ShoppingListCatalogLineItem extends ShoppingListLineItem {

    private Item item;
    
    //Infrastructure Methods
    
    public boolean equals(Object o) {
        if(!(o instanceof ShoppingListCatalogLineItem)) {
            return false;
        }
        ShoppingListCatalogLineItem that = (ShoppingListCatalogLineItem) o;

        if(this.getShoppingListLineItemId() == null || that.getShoppingListLineItemId() == null) {
            return false;
        }

        if(this.getShoppingListLineItemId().longValue() == that.getShoppingListLineItemId().longValue()) {
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        return 29 * (item.hashCode());
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}