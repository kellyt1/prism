package us.mn.state.health.builder.inventory;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.view.inventory.StockItemForm;

public class ItemVendorBuilder {

    private StockItem stockItem;
    private StockItemForm stockItemForm;
    private DAOFactory daoFactory;
    private boolean addItemVendor;
    private String user;

    public ItemVendorBuilder(StockItem stockItem, StockItemForm stockItemForm,
                             DAOFactory daoFactory, boolean addItemVendor, String user) {

        this.stockItem = stockItem;
        this.stockItemForm = stockItemForm;
        this.daoFactory = daoFactory;
        this.addItemVendor = addItemVendor;
        this.user = user;

    }

    public void buildItemVendors() throws InfrastructureException {
        //Set existing form values to null to free up Hibernate managed entities
        stockItemForm.setItemVendors(null);
        stockItemForm.setAvailableVendors(null);
        
        //Reload Vendors and Item Vendors from persistence store
        List availableVendors =
                (List) daoFactory.getVendorDAO().findVendorWhereNotWithItemId(stockItem.getItemId(), false);
        availableVendors.size(); //Eager load Collection
        Collection itemVendors = stockItem.getItemVendors();
        itemVendors.size(); //Eager load Collection

        if (addItemVendor) { //Add an item vendor
            for (int i = 0; i < availableVendors.size(); i++) {
                Vendor vendor = (Vendor) availableVendors.get(i);
                if (vendor.getExternalOrgDetail().getOrgId().toString().equals(stockItemForm.getAvailableVendorId())) {
                    Unit buyUnit = daoFactory.getUnitDAO().findUnitByCode(Unit.CODE_UNKNOWN);
                    ItemVendor itemVendor = stockItem.addVendor(vendor, user);
                    itemVendor.setBuyUnit(buyUnit);
//                    availableVendors.remove(i);
                    break;
                }
            }
        } else { //Remove an Item Vendor
            for (Iterator iterator = itemVendors.iterator(); iterator.hasNext();) {
                ItemVendor itemVendor = (ItemVendor) iterator.next();
                if (itemVendor.getVendor().getVendorId().toString().equals(stockItemForm.getItemVendorId())) {
                    availableVendors.add(itemVendor.getVendor());
                    iterator.remove();
                    new HibernateDAO().makeTransient(itemVendor);
                    stockItemForm.setBuyUnitId(null);
                    stockItemForm.setItemVendorId(null);
                    break;
                }
            }
        }

        stockItemForm.setItemVendors(itemVendors);
        availableVendors = (List) daoFactory.getVendorDAO().findVendorWhereNotWithItemId(stockItem.getItemId(), false);
        stockItemForm.setAvailableVendors(availableVendors);

    }
}