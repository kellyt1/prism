package us.mn.state.health.facade.inventory.editStockItem;

import java.util.List;

import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Hazardous;
import us.mn.state.health.model.common.Manufacturer;
import us.mn.state.health.model.common.ObjectCode;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.inventory.CycleCountPriority;
import us.mn.state.health.model.inventory.StockItemFacility;
import us.mn.state.health.model.inventory.StockQtyChangeReasonRef;
import us.mn.state.health.model.inventory.Unit;

public class EditStockItemDropDownListsDTO {
    private List<Unit> units;
    private List<OrgBudget> orgBudgets;
    private List<StockItemFacility> stockItemFacilities;
    private List<User> contacts;
    private List<Category> categories;
    private List<ObjectCode> objectCodes;
    private List<Vendor> vendors;
    private List<Status> statuses;
    private List<StockQtyChangeReasonRef> qtyChangeReasonRefs;
    private List<Manufacturer> manufacturers;
    private List<CycleCountPriority> cycleCountPriorities;
    private List<Hazardous> hazardouses;


    public EditStockItemDropDownListsDTO(List<Unit> units,
                                         List<OrgBudget> orgBudgets,
                                         List<StockItemFacility> stockItemFacilities,
                                         List<User> contacts,
                                         List<Category> categories,
                                         List<ObjectCode> objectCodes,
                                         List<Vendor> vendors,
                                         List<Status> statuses,
                                         List<StockQtyChangeReasonRef> qtyChangeReasonRefs,
                                         List<Manufacturer> manufacturers,
                                         List<CycleCountPriority> cycleCountPriorities,
                                         List<Hazardous> hazardouses) {
        this.units = units;
        this.orgBudgets = orgBudgets;
        this.stockItemFacilities = stockItemFacilities;
        this.contacts = contacts;
        this.categories = categories;
        this.objectCodes = objectCodes;
        this.vendors = vendors;
        this.statuses = statuses;
        this.qtyChangeReasonRefs = qtyChangeReasonRefs;
        this.manufacturers = manufacturers;
        this.cycleCountPriorities = cycleCountPriorities;
        this.hazardouses = hazardouses;
    }


    public List<Unit> getUnits() {
        return units;
    }

    public List<OrgBudget> getOrgBudgets() {
        return orgBudgets;
    }

    public List<StockItemFacility> getStockItemFacilities() {
        return stockItemFacilities;
    }

    public List<User> getContacts() {
        return contacts;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<ObjectCode> getObjectCodes() {
        return objectCodes;
    }

    public List<Vendor> getVendors() {
        return vendors;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public List<StockQtyChangeReasonRef> getQtyChangeReasonRefs() {
        return qtyChangeReasonRefs;
    }

    public List<Manufacturer> getManufacturers() {
        return manufacturers;
    }

    public List<CycleCountPriority> getCycleCountPriorities() {
        return cycleCountPriorities;
    }


    public List<Hazardous> getHazardouses() {
        return hazardouses;
    }

    public void setHazardouses(List<Hazardous> hazardouses) {
        this.hazardouses = hazardouses;
    }
}
