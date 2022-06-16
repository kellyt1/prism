package us.mn.state.health.domain.repository.purchasing;

import us.mn.state.health.util.NotificationException;
import us.mn.state.health.util.PagedQueryResultEnhanced;

public interface OrderRepository {
    PagedQueryResultEnhanced findOrders(OrderSearchCriteria criteria) throws NotificationException;
}
