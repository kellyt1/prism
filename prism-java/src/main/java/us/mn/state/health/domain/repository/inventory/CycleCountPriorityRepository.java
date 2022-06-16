package us.mn.state.health.domain.repository.inventory;

import java.util.List;

import us.mn.state.health.domain.repository.util.DomainRepository;
import us.mn.state.health.model.inventory.CycleCountPriority;

public interface CycleCountPriorityRepository extends DomainRepository {
    List<CycleCountPriority> findAll();

    CycleCountPriority getById(Long id);
}
