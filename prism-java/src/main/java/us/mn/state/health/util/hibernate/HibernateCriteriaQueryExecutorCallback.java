package us.mn.state.health.util.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import us.mn.state.health.util.PagedQueryResult;
import us.mn.state.health.util.hibernate.queryparameters.BaseQueryParameters;

final class HibernateCriteriaQueryExecutorCallback implements
        HibernateCallback {
    public final static int NO_PAGINATION = -1;
    private final Class queryClass;

    private final CriteriaBuilder builder;

    private int startingIndex = NO_PAGINATION;

    private int pageSize = NO_PAGINATION;

    private BaseQueryParameters baseQueryParameters;

    HibernateCriteriaQueryExecutorCallback(Class queryClass,
                                           CriteriaBuilder builder, int startingIndex, int pageSize) {
        super();
        this.queryClass = queryClass;
        this.builder = builder;
        this.startingIndex = startingIndex;
        this.pageSize = pageSize;
    }

    HibernateCriteriaQueryExecutorCallback(Class queryClass,
                                           CriteriaBuilder builder) {
        super();
        this.queryClass = queryClass;
        this.builder = builder;
    }

    HibernateCriteriaQueryExecutorCallback(Class queryClass,
                                           CriteriaBuilder builder, BaseQueryParameters baseQueryParameters) {
        super();
        this.queryClass = queryClass;
        this.builder = builder;
        this.baseQueryParameters = baseQueryParameters;
    }

    public Object doInHibernate(Session session)
            throws HibernateException, SQLException {
//        session.createSQLQuery("").
        Criteria criteria = session.createCriteria(queryClass);
        builder.addCriteria(criteria);
        if (startingIndex != NO_PAGINATION) {
            criteria.setFirstResult(startingIndex);
        }
        if (pageSize != NO_PAGINATION) {
            //Add 1 just to see if we have a next page
            criteria.setMaxResults(pageSize + 1);
        }
        if (baseQueryParameters != null) {
            if (baseQueryParameters.getLockMode() != null) {
                if (baseQueryParameters.getLockAlias() == null) {
                    criteria.setLockMode(baseQueryParameters.getLockMode());
                } else {
                    criteria.setLockMode(baseQueryParameters.getLockAlias(), baseQueryParameters.getLockMode());
                }
            }
            if (baseQueryParameters.isJustOne())
                criteria.setMaxResults(1);

        }

        List criteriaResult = criteria.list();
        boolean more = false;
        if (startingIndex != NO_PAGINATION && pageSize != NO_PAGINATION) {
            more = criteriaResult.size() > pageSize;
            if (more) {
                //substracts the extra result that was used to see if there is more than 1 page
                criteriaResult.remove(pageSize);
            }
        }
        if (pageSize == NO_PAGINATION || startingIndex == NO_PAGINATION) {
            //we do that because we want to return a simple list for nonpaginated queries
            return criteriaResult;
        }
        return new PagedQueryResult(criteriaResult, more);
    }

}