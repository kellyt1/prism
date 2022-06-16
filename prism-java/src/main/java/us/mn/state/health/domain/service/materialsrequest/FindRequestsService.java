package us.mn.state.health.domain.service.materialsrequest;

import us.mn.state.health.domain.repository.materialsrequest.RequestSearchCriteria;
import us.mn.state.health.util.PagedQueryResultEnhanced;

public interface FindRequestsService {
    public PagedQueryResultEnhanced findAllRequests(RequestSearchCriteria criteria);
}
