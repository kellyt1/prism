package us.mn.state.health.facade.materialsrequest.viewMaterialsRequests;

import us.mn.state.health.domain.repository.common.CategoryRepository;
import us.mn.state.health.domain.repository.common.StatusRepository;
import us.mn.state.health.domain.repository.common.UserRepository;
import us.mn.state.health.domain.repository.materialsrequest.RequestSearchCriteria;
import us.mn.state.health.domain.service.materialsrequest.FindRequestsService;
import us.mn.state.health.util.PagedQueryResultEnhanced;

public class FindRequestsFacadeImpl implements FindRequestsFacade {
    private FindRequestsService findRequestsService;
    private UserRepository userRepository;
    private StatusRepository statusRepository;
    private CategoryRepository categoryRepository;


    public FindRequestsFacadeImpl(FindRequestsService findRequestsService,
                                  UserRepository userRepository,
                                  StatusRepository statusRepository,
                                  CategoryRepository categoryRepository) {
        this.findRequestsService = findRequestsService;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
        this.categoryRepository = categoryRepository;
    }

    public FindRequestsFacadeResult findAllRequests(RequestSearchCriteria criteria) {
        PagedQueryResultEnhanced queryResult =
                findRequestsService.findAllRequests(criteria);
        FindRequestsFacadeResult facadeResult = new FindRequestsFacadeResult();
        facadeResult.setQueryResult(queryResult);
        return facadeResult;
    }

    public FindRequestsFacadeResult findUsersList() {
        FindRequestsFacadeResult facadeResult = new FindRequestsFacadeResult();
        facadeResult.setUsers(userRepository.findAllUsers());
        return facadeResult;
    }

    public FindRequestsFacadeResult findStatusesList(String statusType) {
        FindRequestsFacadeResult facadeResult = new FindRequestsFacadeResult();
        facadeResult.setStatuses(statusRepository.findAllByStatusTypeCode(statusType));
        return facadeResult;
    }

    public FindRequestsFacadeResult findCategoryList() {
        FindRequestsFacadeResult facadeResult = new FindRequestsFacadeResult();
        facadeResult.setCategories(categoryRepository.findAll(false));
        return facadeResult;
    }


}
