package us.mn.state.health.domain.service;

import java.util.Collections;
import java.util.List;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import us.mn.state.health.domain.repository.materialsrequest.RequestRepository;
import us.mn.state.health.domain.repository.materialsrequest.RequestSearchCriteria;
import us.mn.state.health.domain.service.materialsrequest.FindRequestsService;
import us.mn.state.health.domain.service.materialsrequest.FindRequestsServiceImpl;
import us.mn.state.health.domain.service.materialsrequest.viewRequests.RequestDetacher;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.util.PagedQueryResultEnhanced;
import us.mn.state.health.util.Pagination;

public class ViewRequestsServiceImplTests extends MockObjectTestCase {
    private Mock mockOrderDetacher;

    private Mock mockOrderRepository;

    private RequestDetacher requestDetacher;

    private RequestRepository requestRepository;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockOrderDetacher = new Mock(RequestDetacher.class);
        mockOrderRepository = new Mock(RequestRepository.class);
        requestDetacher = (RequestDetacher) mockOrderDetacher.proxy();
        requestRepository = (RequestRepository) mockOrderRepository.proxy();
    }

    public void testViewOrdersService() {
        FindRequestsService service = new FindRequestsServiceImpl(
                requestRepository, requestDetacher);
        RequestSearchCriteria criteria = new RequestSearchCriteria();

        List requests = Collections.singletonList(new Request());
        List detachedRequests = Collections.singletonList(new Request());

        PagedQueryResultEnhanced pqr = new PagedQueryResultEnhanced(requests, new Pagination(0, 10), 25);

        mockOrderRepository.expects(once()).method("findAllRequests").with(eq(criteria)).will(returnValue(pqr));
        mockOrderDetacher.expects(once()).method("detachRequests").with(
                eq(requests)).will(returnValue(detachedRequests));

        PagedQueryResultEnhanced result = service.findAllRequests(criteria);

        assertSame(detachedRequests, result.getPageList());
        assertTrue(result.getPageCount() - 1 > result.getPageNo());
    }
}
