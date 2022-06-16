package us.mn.state.health.model.materialsrequest;

import java.io.Serializable;

public abstract class ShoppingListLineItem implements Serializable {
    private Long shoppingListLineItemId;    
    private ShoppingList shoppingList;    
    private Integer quantity;    
    private int version;
    
    //Infrastructure Method Definitions
    public abstract boolean equals(Object o);
    public abstract int hashCode();


    public void setShoppingListLineItemId(Long shoppingListLineItemId) {
        this.shoppingListLineItemId = shoppingListLineItemId;
    }

    public Long getShoppingListLineItemId() {
        return shoppingListLineItemId;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }
}