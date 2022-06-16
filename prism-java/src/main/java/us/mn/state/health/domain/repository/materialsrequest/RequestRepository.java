package us.mn.state.health.domain.repository.materialsrequest;

import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.util.PagedQueryResultEnhanced;

import java.util.Date;
import java.util.List;

public interface RequestRepository {

    PagedQueryResultEnhanced findAllRequests(RequestSearchCriteria requestSearchCriteria);

    List<RequestLineItem> findRequestLineItemsWaitingFormApprovalSubmittedBeforeDate(Date requestedDate);

    List<RequestLineItem> findRequestLineItemsWaitingForLevelOneApprovalSubmittedBeforeDate(Date requestedDate);

    void makePersistent(Object o);
}
