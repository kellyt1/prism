package us.mn.state.health.domain.repository.common;

import us.mn.state.health.model.common.Group;

public interface GroupRepository {
    Group findGroupByCode(String groupCode);
}
