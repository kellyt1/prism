package us.mn.state.health.domain.repository.inventory;

import java.util.Date;
import java.util.List;

import us.mn.state.health.domain.repository.util.DomainRepository;
import us.mn.state.health.model.inventory.*;
import us.mn.state.health.model.materialsrequest.RequestLineItem;

public interface StockItemRepository extends DomainRepository {
    StockItem getById(Long stockItemId);

    List findStockItemsBelowROP(List categoryIds, List facilityIds, boolean includeStockItemsWithoutFacilities);

    List findAllStockItems(StockItemSearchCriteria stockItemSearchCriteria);

    List<StockItem> findAllStockItems(StockItemSearchCriteria stockItemSearchCriteria, boolean lockResults);

    List<RequestLineItem> findOnOrderRequestLineItems(Long stockItemId);

    Integer findNextICNBR();

    List<StockItemHistory> getStockItemHistory(StockItem stockItem, int noOfRecords);

    void makePersistent(StockItem stockItem);

    List<StockQtyAdjustmentHistory> getStockQtyAdjustmentHistory(StockItem stockItem, int noOfRecords);

    List<StockItem> getStockItemsHitReviewDateBetweenDates(Date startDate, Date endDate);

    List<StockItemLocationHistory> getStockItemLocationHistory(StockItem stockItem, int noOfRecords);

    List<ItemVendorLinkHistory> getItemVendorLinkHistory(StockItem stockItem, int noOfRecords);
}
