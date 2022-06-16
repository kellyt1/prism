package us.mn.state.health.facade.inventory;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import us.mn.state.health.domain.repository.inventory.StockItemRepository;
import us.mn.state.health.domain.repository.inventory.StockItemFacilityRepository;
import us.mn.state.health.domain.repository.common.CategoryRepository;
import us.mn.state.health.domain.service.inventory.InventoryReportsService;
import us.mn.state.health.domain.service.inventory.InventoryReportsServiceResult;
import us.mn.state.health.common.util.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

public class InventoryReportsFacadeImpl implements InventoryReportsFacade {

    private StockItemFacilityRepository stockItemFacilityRepository;
    private StockItemRepository stockItemRepository;
    private CategoryRepository categoryRepository;
    private InventoryReportsService inventoryReportsService;


    public InventoryReportsFacadeImpl(StockItemRepository stockItemRepository,
                                      CategoryRepository categoryRepository,
                                      StockItemFacilityRepository stockItemFacilityRepository,
                                      InventoryReportsService inventoryReportsService) {
        this.categoryRepository = categoryRepository;
        this.stockItemRepository = stockItemRepository;
        this.stockItemFacilityRepository = stockItemFacilityRepository;
        this.inventoryReportsService = inventoryReportsService;
    }

    /**
     * @return a InventoryReportsFacadeResult that has the categories and the facilities lists
     */
    public InventoryReportsFacadeResult loadSearchCriteriaLists() {
        List categories = categoryRepository.findAll(false);
        List facilities = stockItemFacilityRepository.findAll();
        InventoryReportsFacadeResult facadeResult = new InventoryReportsFacadeResult();
        facadeResult.setCategories(categories);
        facadeResult.setStockItemFacilities(facilities);
        return facadeResult;
    }

    public InventoryReportsFacadeResult findStockItemsBelowROP(Long[] categoryIds, Long[] facilityIds, boolean includeStockItemsWithoutFacilities) {

        List catIds;
        List facIds;

        if (ArrayUtils.isEmpty(categoryIds)) {
            catIds = CollectionUtils.getNestedItems(categoryRepository.findAll(false), "categoryId");
        } else {
            catIds = Arrays.asList(categoryIds);
        }

        if (ArrayUtils.isEmpty(facilityIds)) {
            facIds = CollectionUtils.getNestedItems(stockItemFacilityRepository.findAll(), "facilityId");
        } else {
            facIds = Arrays.asList(facilityIds);
        }

        InventoryReportsServiceResult stockItemsBelowROP =
                inventoryReportsService.findStockItemsBelowROP(catIds, facIds, includeStockItemsWithoutFacilities);
        List result = stockItemsBelowROP.getStockItems();
        //remove duplicates
        result = new ArrayList(new LinkedHashSet(result));

        InventoryReportsFacadeResult facadeResult = new InventoryReportsFacadeResult();
        facadeResult.setStockItems(result);
        return facadeResult;
    }
}
