package us.mn.state.health.domain.service.inventory;

import java.util.List;

public interface InventoryReportsService {

    InventoryReportsServiceResult findStockItemsBelowROP(List categoryIds, List facilityIds, boolean includeStockItemsWithoutFacilities);
}
