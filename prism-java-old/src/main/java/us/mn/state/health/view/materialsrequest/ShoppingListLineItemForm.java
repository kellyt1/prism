package us.mn.state.health.view.materialsrequest;

import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.lang.StringUtils;

public abstract class ShoppingListLineItemForm extends ValidatorForm {

    private String shoppingListLineItemId;
    private Boolean selected = Boolean.FALSE;
    private String quantity;

    public void setShoppingListLineItemId(String shoppingListLineItemId) {
        this.shoppingListLineItemId = shoppingListLineItemId;
    }


    public String getShoppingListLineItemId() {
        return shoppingListLineItemId;
    }


    public void setSelected(Boolean selected) {
        this.selected = selected;
    }


    public Boolean getSelected() {
        return selected;
    }


    public void setQuantity(String qty) {
        this.quantity = StringUtils.trim(qty);
    }


    public String getQuantity() {
        return quantity;
    }
     
}