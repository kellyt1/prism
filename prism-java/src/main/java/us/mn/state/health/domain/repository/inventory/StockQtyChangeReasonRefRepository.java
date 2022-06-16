package us.mn.state.health.domain.repository.inventory;

import java.util.List;

import us.mn.state.health.domain.repository.util.DomainRepository;
import us.mn.state.health.model.inventory.StockQtyChangeReasonRef;

public interface StockQtyChangeReasonRefRepository extends DomainRepository {
    List<StockQtyChangeReasonRef> findAll();

    StockQtyChangeReasonRef getById(Long reasonId);
}
