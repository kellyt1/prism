package us.mn.state.health.domain.repository.common;

import java.util.List;

import us.mn.state.health.model.common.User;

public interface UserRepository {

    List<User> findAllUsers();

    User getUserById(Long userId);

    User getUser(String username);

    List<User> getUsersByGroupCode(String groupCode);
}
