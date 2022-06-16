package us.mn.state.health.domain.repository.purchasing.hibernate;

import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.domain.repository.purchasing.OrderRepository;
import us.mn.state.health.domain.repository.purchasing.OrderSearchCriteria;
import us.mn.state.health.domain.repository.util.HibernateDomainRepositoryImpl;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.util.NotificationException;
import us.mn.state.health.util.PagedQueryResultEnhanced;
import us.mn.state.health.util.hibernate.HibernateQueryExecutor;
import us.mn.state.health.util.hibernate.queryparameters.BaseSQLQueryParameters;
import us.mn.state.health.util.hibernate.queryparameters.SQLQueryParametersImpl;

public class OrderRepositoryImpl extends HibernateDomainRepositoryImpl implements OrderRepository {
    private HibernateQueryExecutor queryExecutor;

    public OrderRepositoryImpl(HibernateTemplate template, HibernateQueryExecutor queryExecutor) {
        super(template);
        this.queryExecutor = queryExecutor;
    }

    public PagedQueryResultEnhanced findOrders(OrderSearchCriteria criteria) throws NotificationException {
        FindOrdersSLQBuilder findOrdersSLQBuilder = new FindOrdersSLQBuilder(criteria);
        SQLQueryParametersImpl queryParameters = new SQLQueryParametersImpl(
                findOrdersSLQBuilder.buildSqlQuery(),
                findOrdersSLQBuilder.buildCountSqlQuery(),
                new BaseSQLQueryParameters("order_", Order.class));
        return queryExecutor.executeSQLQuery(criteria.getPagination(), queryParameters);
    }
}
