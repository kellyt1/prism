package us.mn.state.health.util.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.util.PagedQueryResult;
import us.mn.state.health.util.PagedQueryResultEnhanced;
import us.mn.state.health.util.Pagination;
import us.mn.state.health.util.hibernate.executorcallback.NamedQueryExecutorCallbackImpl;
import us.mn.state.health.util.hibernate.executorcallback.SQLQueryExecutorCallbackImpl;
import us.mn.state.health.util.hibernate.queryparameters.BaseQueryParameters;
import us.mn.state.health.util.hibernate.queryparameters.NamedQueryParametersImpl;
import us.mn.state.health.util.hibernate.queryparameters.SQLQueryParametersImpl;


public class HibernateQueryExecutor {

    HibernateTemplate hibernateTemplate;

    HibernateQueryExecutor() {
    }

    public HibernateQueryExecutor(
            HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public List executeNamedQuery(
            NamedQueryParametersImpl queryParams) {
        return hibernateTemplate
                .executeFind(new NamedQueryExecutorCallbackImpl(new Pagination(Pagination.UNPAGINATED, Pagination.UNPAGINATED), queryParams));
    }

    public PagedQueryResult executeCriteriaQuery(Class queryClass,
                                                 int startingIndex, int pageSize, CriteriaBuilder builder) {
        HibernateCriteriaQueryExecutorCallback callback = new HibernateCriteriaQueryExecutorCallback(
                queryClass, builder, startingIndex,
                pageSize);
        return (PagedQueryResult) hibernateTemplate
                .execute(callback);
    }

    public List executeCriteriaQuery(Class queryClass,
                                     CriteriaBuilder builder) {
        HibernateCriteriaQueryExecutorCallback callback = new HibernateCriteriaQueryExecutorCallback(
                queryClass, builder);
        return hibernateTemplate
                .executeFind(callback);
    }

    public List executeCriteriaQuery(Class queryClass,
                                     CriteriaBuilder builder, BaseQueryParameters baseQueryParameters) {
        HibernateCriteriaQueryExecutorCallback callback = new HibernateCriteriaQueryExecutorCallback(
                queryClass, builder, baseQueryParameters);
        return hibernateTemplate
                .executeFind(callback);
    }

    public PagedQueryResultEnhanced executeSQLQuery(Pagination pagination, SQLQueryParametersImpl queryParameters) {
        SQLQueryExecutorCallbackImpl callback = new SQLQueryExecutorCallbackImpl(pagination, queryParameters);
        return (PagedQueryResultEnhanced) hibernateTemplate.execute(callback);
    }

}