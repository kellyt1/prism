package us.mn.state.health.facade.materialsrequest.viewMaterialsRequests;

import us.mn.state.health.domain.repository.materialsrequest.RequestSearchCriteria;

public interface FindRequestsFacade {
    public FindRequestsFacadeResult findAllRequests(RequestSearchCriteria criteria);

    FindRequestsFacadeResult findUsersList();

    FindRequestsFacadeResult findStatusesList(String statusType);

    FindRequestsFacadeResult findCategoryList();
}
