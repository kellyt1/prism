package us.mn.state.health.domain.service.inventory;

import java.util.List;

import us.mn.state.health.domain.repository.inventory.StockItemRepository;

public class InventoryReportsServiceImpl implements InventoryReportsService {
    private StockItemRepository stockItemRepository;


    public InventoryReportsServiceImpl(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    public InventoryReportsServiceResult findStockItemsBelowROP(List categoryIds, List facilityIds, boolean includeStockItemsWithoutFacilities) {
        List stockItems = stockItemRepository.findStockItemsBelowROP(categoryIds, facilityIds, includeStockItemsWithoutFacilities);
        InventoryReportsServiceResult serviceResult = new InventoryReportsServiceResult(stockItems);
        return serviceResult;
    }
}
