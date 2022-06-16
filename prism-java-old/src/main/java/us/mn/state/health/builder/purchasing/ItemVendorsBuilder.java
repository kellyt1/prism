package us.mn.state.health.builder.purchasing;

import java.util.Iterator;
import java.util.Collection;
import java.util.Date;

import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.model.common.User;
import us.mn.state.health.view.inventory.ItemVendorForm;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;

public class ItemVendorsBuilder {
    private Item item;
//    private ItemVendorsForm itemVendorsForm;
    private User user;
    private DAOFactory daoFactory;
    private Collection itemVendorForms;
    private String itemVendorPrimaryKey;

//    public ItemVendorsBuilder(Item item, ItemVendorsForm itemVendorsForm, DAOFactory daoFactory, User user) {
//        this.item = item;
//        this.itemVendorsForm = itemVendorsForm;
//        this.daoFactory = daoFactory;
//        this.user = user;
//    }

    public ItemVendorsBuilder(Item item, Collection itemVendorForms, DAOFactory daoFactory, User user, String itemVendorPrimaryKey) {
        this.itemVendorPrimaryKey = itemVendorPrimaryKey;
        this.item = item;
        this.itemVendorForms = itemVendorForms;
        this.daoFactory = daoFactory;
        this.user = user;
    }

    public void buildItemVendors() throws InfrastructureException, ReflectivePropertyException {
        for (Iterator iterator = item.getItemVendors().iterator(); iterator.hasNext();) {
            ItemVendor itemVendor = (ItemVendor) iterator.next();

            ItemVendorForm itemVendorForm = (ItemVendorForm) CollectionUtils.
                    getObjectFromCollectionById(itemVendorForms, itemVendor.getItemVendorId(), "itemVendorId");
            if (itemVendorForm == null) {
                //remove the removed item vendor info
                iterator.remove();
                continue;
            }
            buildItemVendor(itemVendor, itemVendorForms, itemVendorPrimaryKey, false, iterator);
        }

        //create the new item vendor links
        for (Iterator iterator = itemVendorForms.iterator(); iterator.hasNext();) {
            ItemVendorForm itemVendorForm = (ItemVendorForm) iterator.next();
            String vendorId = itemVendorForm.getVendorId();
            if (vendorId != null) {
                ItemVendor itemVendor = ItemVendor.createItemVendor(daoFactory.getVendorDAO().getVendorById(new Long(vendorId), false), item, null, user.getUsername());
                buildItemVendor(itemVendor, itemVendorForms, itemVendorPrimaryKey, true, iterator);
            }

        }
    }

    private void buildItemVendor(ItemVendor itemVendor,
                                 Collection itemVendorForms,
                                 String itemVendorPrimaryKey,
                                 boolean isNew, Iterator iterator) throws InfrastructureException, ReflectivePropertyException {
        ItemVendorForm itemVendorForm = (ItemVendorForm) CollectionUtils.getObjectFromCollectionById(itemVendorForms, itemVendor.getItemVendorId(), "itemVendorId");
        /*
        if (itemVendorForm == null) {
            //remove the removed item vendor info
            iterator.remove();
//            item.getItemVendors().remove(itemVendor);
            return;
//            daoFactory.getItemVendorDAO().makeTransient(itemVendor);
        } */
        if (itemVendorForm.getKey().equals(itemVendorPrimaryKey)) {
            itemVendorForm.setPrimaryVendor(Boolean.TRUE);
        } else {
            itemVendorForm.setPrimaryVendor(Boolean.FALSE);
        }
        boolean dirty = false;
        if (!isNew) {
            dirty = itemVendorIsDirty(itemVendor, itemVendorForm);
        }

        Long buyUnitId = itemVendorForm.getBuyUnitId();
        if (buyUnitId != null && buyUnitId.longValue() != 0) {
            itemVendor.setBuyUnit(daoFactory.getUnitDAO().getUnitById(buyUnitId, false));
        }
        PropertyUtils.copyProperties(itemVendor, itemVendorForm);
        if (itemVendorForm.getKey().equals(itemVendorPrimaryKey)) {
            itemVendor.setPrimaryVendor(Boolean.TRUE);
        } else {
            itemVendor.setPrimaryVendor(Boolean.FALSE);
        }
        if (dirty) {
            itemVendor.setLastUpdatedBy(user.getUsername());
            itemVendor.setLastUpdatedDate(new Date());
        }

        if (!isNew) {
            itemVendorForms.remove(itemVendorForm);
        }
    }

    private boolean itemVendorIsDirty(ItemVendor itemVendor, ItemVendorForm itemVendorForm) {
        //check the discount, unit, catalog#, unitCost, primary
        Double buyUnitCost = itemVendor.getBuyUnitCost();
        String formBuyUnitCost = itemVendorForm.getBuyUnitCost();
        boolean buyUnitCostIsDirty;
        if (buyUnitCost == null && formBuyUnitCost == null) {
            buyUnitCostIsDirty = false;
        } else {
            buyUnitCostIsDirty = (buyUnitCost == null && formBuyUnitCost != null) || (buyUnitCost != null && formBuyUnitCost == null) || !buyUnitCost.equals(Double.valueOf(formBuyUnitCost));
        }

        boolean discountIsDirty = !itemVendor.getDiscount().equals(Double.valueOf(itemVendorForm.getDiscount()));
        boolean primaryVendorIsDirty = !itemVendor.getPrimaryVendor().equals(itemVendorForm.getPrimaryVendor());


        boolean buyUnitIsDirty;
        Unit buyUnit = itemVendor.getBuyUnit();
        Long buyUnitId = itemVendorForm.getBuyUnitId();

        if (buyUnit == null) {
            if (buyUnitId != null || buyUnitId.longValue() != 0) {
                buyUnitIsDirty = true;
            } else {
                buyUnitIsDirty = false;
            }
        } else {
            buyUnitIsDirty = !buyUnit.getUnitId().equals(buyUnitId);
        }


        boolean catalogNoIsDirty = !StringUtils.trim(itemVendor.getVendorCatalogNbr()).equals(StringUtils.trim(itemVendorForm.getVendorCatalogNbr()));
        if (buyUnitCostIsDirty
                || discountIsDirty
                || primaryVendorIsDirty
                || buyUnitIsDirty
                || catalogNoIsDirty) {
            return true;
        }
        return false;
    }
}
