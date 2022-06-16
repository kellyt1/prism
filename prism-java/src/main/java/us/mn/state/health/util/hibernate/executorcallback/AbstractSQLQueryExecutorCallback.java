package us.mn.state.health.util.hibernate.executorcallback;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import us.mn.state.health.util.PagedQueryResultEnhanced;
import us.mn.state.health.util.Pagination;
import us.mn.state.health.util.hibernate.queryparameters.BaseSQLQueryParameters;
import us.mn.state.health.util.hibernate.queryparameters.QueryParameters;
import us.mn.state.health.util.hibernate.queryparameters.SQLQueryParameters;

public abstract class AbstractSQLQueryExecutorCallback extends AbstractQueryExecutorCallback {
    protected AbstractSQLQueryExecutorCallback(Pagination pagination, QueryParameters queryParameters) {
        super(pagination, queryParameters);
    }

    void addClassEntity(SQLQuery query) {
        BaseSQLQueryParameters baseSqlQueryParameters = ((SQLQueryParameters) queryParameters).getBaseSqlQueryParameters();
        query.addEntity(baseSqlQueryParameters.getEntityAlias(), baseSqlQueryParameters.getEntityClass());
    }

    void addScalar(SQLQuery query) {
        BaseSQLQueryParameters baseSqlQueryParameters = ((SQLQueryParameters) queryParameters).getBaseSqlQueryParameters();
        if (baseSqlQueryParameters.getScalarProperties() != null) {
            for (String s : baseSqlQueryParameters.getScalarProperties()) {
                query.addScalar(s);
            }
        }
    }

    public Object doInHibernate(Session session) throws HibernateException, SQLException {
        SQLQuery query = (SQLQuery) getQuery(session);
        SQLQuery totalQuery = (SQLQuery) getTotalQuery(session);
        //if is paginated and not just 1 result, run a total query first before applying the pagination
        int totalResults = getTotalResults(totalQuery);

        applyPagination(query, totalResults);
        applyLock(query);
        applyJustOneResult(query);

        addClassEntity(query);
        addScalar(query);


        List results = query.list();
        if (!Pagination.NO_PAGINATION.equals(pagination)) {
            return new PagedQueryResultEnhanced(results, pagination, totalResults);
        } else return results;
    }

    private static int getTotalResults(SQLQuery query) {
        return Integer.parseInt(query.list().get(0).toString());
    }
}
