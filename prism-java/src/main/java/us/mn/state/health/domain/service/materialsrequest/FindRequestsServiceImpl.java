package us.mn.state.health.domain.service.materialsrequest;

import java.util.List;

import us.mn.state.health.domain.repository.materialsrequest.RequestRepository;
import us.mn.state.health.domain.repository.materialsrequest.RequestSearchCriteria;
import us.mn.state.health.domain.service.materialsrequest.viewRequests.RequestDetacher;
import us.mn.state.health.util.PagedQueryResultEnhanced;
import us.mn.state.health.util.Pagination;

public class FindRequestsServiceImpl implements FindRequestsService {
    private RequestRepository requestRepository;
    private RequestDetacher requestDetacher;


    public FindRequestsServiceImpl(RequestRepository requestRepository, RequestDetacher requestDetacher) {
        this.requestRepository = requestRepository;
        this.requestDetacher = requestDetacher;
    }

    public PagedQueryResultEnhanced findAllRequests(RequestSearchCriteria criteria) {
        PagedQueryResultEnhanced result = requestRepository.findAllRequests(criteria);
        //TODO this could go in facade
        List detachedOrders = requestDetacher.detachRequests(result.getPageList());
        return new PagedQueryResultEnhanced(detachedOrders, new Pagination(result.getPageNo(), result.getPageSize()), result.getNrOfElements());
    }
}


