package us.mn.state.health.builder.purchasing;

import java.util.Iterator;

import us.mn.state.health.view.purchasing.ItemVendorsForm;
import us.mn.state.health.view.inventory.ItemVendorForm;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;

public class ItemVendorsFormBuilder {
    private ItemVendorsForm itemVendorsForm;
    private Item item;
    private DAOFactory daoFactory;

    public ItemVendorsFormBuilder(ItemVendorsForm itemVendorsForm, Item item, DAOFactory daoFactory) {
        this.itemVendorsForm = itemVendorsForm;
        this.item = item;
        this.daoFactory = daoFactory;
    }

    public void buildItem(){
        itemVendorsForm.setItem(item);
    }

    public void buildItemVendorForms() throws ReflectivePropertyException {
        for (Iterator iterator = item.getItemVendors().iterator(); iterator.hasNext();) {
            ItemVendor itemVendor = (ItemVendor) iterator.next();
            ItemVendorForm itemVendorForm = new ItemVendorForm();
            itemVendorForm.setItemVendorId(itemVendor.getItemVendorId());
            PropertyUtils.copyProperties(itemVendorForm,itemVendor);
            itemVendorForm.setBuyUnitId(itemVendor.getBuyUnit().getUnitId());
            itemVendorForm.setVendorName(itemVendor.getVendor().getExternalOrgDetail().getOrgName());
            itemVendorsForm.getItemVendorForms().add(itemVendorForm);
            if(itemVendor.getPrimaryVendor().booleanValue()){
                itemVendorsForm.setPrimaryKey(""+itemVendor.getItemVendorId().hashCode());
            }
        }
    }

    public void buildUnits() throws InfrastructureException {
        itemVendorsForm.setUnits(daoFactory.getUnitDAO().findAll(false));
    }
}
