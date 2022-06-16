package us.mn.state.health.domain.repository.common;

import java.util.List;

import us.mn.state.health.model.common.Priority;

public interface PriorityRepository {
    List<Priority> findAllPriorities();
}
