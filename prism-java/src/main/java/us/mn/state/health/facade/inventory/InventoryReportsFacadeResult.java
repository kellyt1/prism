package us.mn.state.health.facade.inventory;

import java.util.List;

public class InventoryReportsFacadeResult {
    private List stockItems;
    private List categories;
    private List stockItemFacilities;
    private int status;


    public InventoryReportsFacadeResult() {
    }

    public List getCategories() {
        return categories;
    }

    public void setCategories(List categories) {
        this.categories = categories;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public void setStockItems(List stockItems) {
        this.stockItems = stockItems;
    }

    public List getStockItems() {
        return stockItems;
    }

    public List getStockItemFacilities() {
        return stockItemFacilities;
    }

    public void setStockItemFacilities(List stockItemFacilities) {
        this.stockItemFacilities = stockItemFacilities;
    }
}
