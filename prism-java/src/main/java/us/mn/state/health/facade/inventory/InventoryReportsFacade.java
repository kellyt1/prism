package us.mn.state.health.facade.inventory;

public interface InventoryReportsFacade {
    InventoryReportsFacadeResult findStockItemsBelowROP(Long[] categoryIds, Long[] facilityIds, boolean includeStockItemsWithoutFacilities);

    InventoryReportsFacadeResult loadSearchCriteriaLists();
}
