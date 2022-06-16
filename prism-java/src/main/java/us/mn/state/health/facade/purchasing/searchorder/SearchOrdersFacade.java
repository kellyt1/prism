package us.mn.state.health.facade.purchasing.searchorder;

import us.mn.state.health.domain.repository.purchasing.OrderSearchCriteria;
import us.mn.state.health.util.NotificationException;
import us.mn.state.health.util.PagedQueryResultEnhanced;


public interface SearchOrdersFacade {
    SearchOrdersDropDownListsDTO getDropDownLists();

    PagedQueryResultEnhanced searchOrders(OrderSearchCriteria criteria) throws NotificationException;
}
