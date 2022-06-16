package us.mn.state.health.facade.inventory;

import java.util.List;

import us.mn.state.health.model.inventory.StockItem;

public class StockItemFacadeResult {
    public static final String OK_STATUS="OK";
    public List<StockItem> stockItems;
    public String status;

    public List<StockItem> getStockItems() {
        return stockItems;
    }

    public void setStockItems(List<StockItem> stockItems) {
        this.stockItems = stockItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
