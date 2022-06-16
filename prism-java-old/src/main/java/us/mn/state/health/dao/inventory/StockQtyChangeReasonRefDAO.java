package us.mn.state.health.dao.inventory;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.inventory.StockQtyChangeReasonRef;

public interface StockQtyChangeReasonRefDAO {
    public Collection findAll() throws InfrastructureException;
    public StockQtyChangeReasonRef getStockQtyChangeReasonRefById(Long stockQtyChangeReasonRefId, boolean lock) throws InfrastructureException;
}