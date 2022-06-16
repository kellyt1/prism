package us.mn.state.health.facade.common;

import us.mn.state.health.domain.repository.common.UserRepository;

public class UserFacadeImpl implements UserFacade {
    private UserRepository userRepository;


    public UserFacadeImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserFacadeResult findAllUsers() {
        UserFacadeResult result = new UserFacadeResult();
        result.setUsers(userRepository.findAllUsers());
        return result;
    }
}
