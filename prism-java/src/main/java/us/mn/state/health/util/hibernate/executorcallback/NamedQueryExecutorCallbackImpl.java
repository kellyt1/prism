package us.mn.state.health.util.hibernate.executorcallback;

import java.util.Iterator;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import us.mn.state.health.util.Pagination;
import us.mn.state.health.util.hibernate.queryparameters.BaseQueryParameters;
import us.mn.state.health.util.hibernate.queryparameters.NamedQueryParametersImpl;

public class NamedQueryExecutorCallbackImpl extends AbstractQueryExecutorCallback {

    public NamedQueryExecutorCallbackImpl(Pagination pagination,
                                          NamedQueryParametersImpl queryParameters) {
        super(pagination, queryParameters);
    }

    public boolean equals(Object x) {
        if (!(x instanceof NamedQueryExecutorCallbackImpl))
            return false;
        if (x == null)
            return false;
        NamedQueryExecutorCallbackImpl other = (NamedQueryExecutorCallbackImpl) x;
        return queryParameters.equals(other.queryParameters);
    }

    public int hashCode() {
        return queryParameters.hashCode();
    }

    public Object doInHibernate(Session session)
            throws HibernateException {
        Query query = session.getNamedQuery(((NamedQueryParametersImpl) queryParameters).getQueryName());
        query.setCacheable(true);
        for (Iterator it = queryParameters.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            String name = (String) entry.getKey();
            Object value = entry.getValue();
            query.setParameter(name, value);
        }
        if (queryParameters.getHibernateBaseQueryParameters() != null) {
            BaseQueryParameters parametersBase = queryParameters.getHibernateBaseQueryParameters();
            if (parametersBase.getLockAlias() != null) {
                query.setLockMode(parametersBase.getLockAlias(), parametersBase.getLockMode());
            }
            if (parametersBase.isJustOne())
                query.setMaxResults(1);
        }
        return query.list();
    }

    Query getQuery(Session session) {
        return null;
    }

    Query getTotalQuery(Session session) {
        return null;
    }
}