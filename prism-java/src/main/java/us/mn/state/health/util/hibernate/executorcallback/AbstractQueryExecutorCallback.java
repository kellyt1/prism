package us.mn.state.health.util.hibernate.executorcallback;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import us.mn.state.health.util.Pagination;
import us.mn.state.health.util.hibernate.queryparameters.QueryParameters;

abstract public class AbstractQueryExecutorCallback implements HibernateCallback {
    protected Pagination pagination;
    protected QueryParameters queryParameters;

    protected AbstractQueryExecutorCallback() {
    }

    protected AbstractQueryExecutorCallback(Pagination pagination) {
        this.pagination = pagination;
    }

    protected AbstractQueryExecutorCallback(QueryParameters queryParameters) {
        this.queryParameters = queryParameters;
    }

    /**
     * @param pagination
     * @param queryParameters
     * @throws IllegalArgumentException if pagination is null
     */
    protected AbstractQueryExecutorCallback(Pagination pagination, QueryParameters queryParameters) {
        if (pagination == null) {
            throw new IllegalArgumentException("Invalid pagination parameter! The pagination paramater must not be null!" +
                    " Use Pagination.NO_PAGINATION instead!");
        }
        this.pagination = pagination;
        this.queryParameters = queryParameters;
    }

    abstract Query getQuery(Session session);

    abstract Query getTotalQuery(Session session);

    public void applyPagination(Query query, int totalResults) {
        if (!Pagination.NO_PAGINATION.equals(pagination)) {
            query.setFirstResult(Math.min(pagination.getPageNumber() * pagination.getPageSize(), totalResults / pagination.getPageSize() * pagination.getPageSize()));
            query.setMaxResults(pagination.getPageSize());
        }
    }

    public void applyLock(Query query) {
        if (queryParameters != null
                && queryParameters.getHibernateBaseQueryParameters() != null
                && StringUtils.isNotEmpty(queryParameters.getHibernateBaseQueryParameters().getLockAlias())) {
            query.setLockMode(queryParameters.getHibernateBaseQueryParameters().getLockAlias(), queryParameters.getHibernateBaseQueryParameters().getLockMode());
        }
    }

    public void applyJustOneResult(Query query) {
        if (queryParameters != null && queryParameters.getHibernateBaseQueryParameters() != null && queryParameters.getHibernateBaseQueryParameters().isJustOne()) {
            query.setMaxResults(1);
        }
    }

}
