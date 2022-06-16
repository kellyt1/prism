package us.mn.state.health.model.materialsrequest;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

import us.mn.state.health.model.common.User;

public class ShoppingList implements Serializable {

    private Long shoppingListId;
    private String comment;
    private String name;
    private Collection shoppingListCatLineItems = new LinkedHashSet();
    private Collection shoppingListNonCatLineItems = new LinkedHashSet();
    private User owner;

    public Long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(Long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setShoppingListCatLineItems(Collection shoppingListCatLineItems) {
        this.shoppingListCatLineItems = shoppingListCatLineItems;
    }

    public Collection getShoppingListCatLineItems() {
        return shoppingListCatLineItems;
    }

    public void setShoppingListNonCatLineItems(Collection shoppingListNonCatLineItems) {
        this.shoppingListNonCatLineItems = shoppingListNonCatLineItems;
    }

    public Collection getShoppingListNonCatLineItems() {
        return shoppingListNonCatLineItems;
    }
}