package us.mn.state.health.domain.repository.inventory;

import java.util.Collection;
import java.util.List;

import us.mn.state.health.model.inventory.StockItemFacility;

public interface StockItemFacilityRepository {
    Collection findStockItemFacilitiesByType(String type) ;


    StockItemFacility getStockItemFacilityById(Long facilityId) ;

    List findAll();

    void makePersistent(StockItemFacility facility) ;

    void makeTransient(StockItemFacility facility);

    StockItemFacility findByFacilityCode(String facilityCode);
}
