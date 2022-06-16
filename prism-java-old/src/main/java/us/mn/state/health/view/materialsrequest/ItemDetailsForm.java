package us.mn.state.health.view.materialsrequest;

import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.PurchaseItem;
import us.mn.state.health.model.inventory.StockItem;

public class ItemDetailsForm extends ValidatorForm {
    private Item item;

    public ItemDetailsForm() {
    }

    public ItemDetailsForm(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public boolean getIsPurchaseItem() {
        return item instanceof PurchaseItem;
    }

    public boolean getIsStockItem() {
        return item instanceof StockItem;
    }
}