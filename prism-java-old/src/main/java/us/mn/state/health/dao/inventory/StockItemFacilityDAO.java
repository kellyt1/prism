package us.mn.state.health.dao.inventory;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.inventory.StockItemFacility;

public interface StockItemFacilityDAO {

    public Collection findStockItemFacilitiesByType(String type) throws InfrastructureException;
    
    public StockItemFacility getStockItemFacilityById(Long facilityId, boolean lock) throws InfrastructureException;

    public Collection findAll() throws InfrastructureException;

    public void makePersistent(StockItemFacility facility) throws InfrastructureException;

    public void makeTransient(StockItemFacility facility) throws InfrastructureException;

    StockItemFacility findByFacilityCode(String facilityCode) throws InfrastructureException;
}
