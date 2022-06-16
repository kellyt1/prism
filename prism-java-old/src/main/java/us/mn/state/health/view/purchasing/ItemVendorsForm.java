package us.mn.state.health.view.purchasing;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.action.ActionMapping;
import us.mn.state.health.model.inventory.Item;

public class ItemVendorsForm extends ValidatorForm {
    private Long itemId;
    private Item item;
    private Collection itemVendorForms = new ArrayList();
    private Collection units = new ArrayList();
    private String primaryKey; //the key of the primary Vendor
    private boolean reset = false;

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Collection getItemVendorForms() {
        return itemVendorForms;
    }

    public void setItemVendorForms(ArrayList itemVendorForms) {
        this.itemVendorForms = itemVendorForms;
    }

    public Collection getUnits() {
        return units;
    }

    public void setUnits(Collection units) {
        this.units = units;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (reset) {
            reset = false;
            itemVendorForms = new ArrayList();
            item = null;
            itemId = null;
            primaryKey = null;
        }
    }
}
