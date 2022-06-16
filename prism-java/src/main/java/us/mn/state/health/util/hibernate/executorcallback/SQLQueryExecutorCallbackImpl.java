package us.mn.state.health.util.hibernate.executorcallback;

import org.hibernate.Query;
import org.hibernate.Session;
import us.mn.state.health.util.Pagination;
import us.mn.state.health.util.hibernate.queryparameters.SQLQueryParametersImpl;

public class SQLQueryExecutorCallbackImpl extends AbstractSQLQueryExecutorCallback {

    public SQLQueryExecutorCallbackImpl(Pagination pagination, SQLQueryParametersImpl sqlQueryParameters) {
        super(pagination, sqlQueryParameters);
    }

    Query getQuery(Session session) {
        return session.createSQLQuery(((SQLQueryParametersImpl) queryParameters).getQuery());
    }

    Query getTotalQuery(Session session) {
        return session.createSQLQuery(((SQLQueryParametersImpl) queryParameters).getTotalQuery());
    }


}
