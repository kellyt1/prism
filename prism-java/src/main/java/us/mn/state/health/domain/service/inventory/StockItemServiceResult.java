package us.mn.state.health.domain.service.inventory;

import java.util.List;
import java.util.ArrayList;

import us.mn.state.health.model.inventory.StockItem;

public class StockItemServiceResult {
    private List<StockItem> stockItems = new ArrayList<StockItem>();

    public List<StockItem> getStockItems() {
        return stockItems;
    }

    public void setStockItems(List<StockItem> stockItems) {
        this.stockItems = stockItems;
    }
}
