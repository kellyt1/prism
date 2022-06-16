package us.mn.state.health.domain.service.inventory;

import java.util.List;

public class InventoryReportsServiceResult {
    private List stockItems;


    public InventoryReportsServiceResult(List stockItems) {
        this.stockItems = stockItems;
    }

    public List getStockItems() {
        return stockItems;
    }
}
