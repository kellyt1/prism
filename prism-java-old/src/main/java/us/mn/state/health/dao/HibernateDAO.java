package us.mn.state.health.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateDAO {

    /**
     * Query parameters used for the current query
     */
    private Collection queryParams = new ArrayList();

    private Collection userCriteria = new ArrayList();

    /**
     * Constructor
     */
    public HibernateDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch (InfrastructureException ie) {
        }
    }

    /**
     * Adds a query parameter for the current query
     *
     * @param paramValue
     * @param paramName
     */
    public void addQueryParam(String paramName, Object paramValue) {
        queryParams.add(new QueryParam(paramName, paramValue));
    }

    public void addCriterion(Criterion criterion) {
        userCriteria.add(criterion);
    }

    /**
     * Executes a Criteria query
     *
     * @param criteriaClass
     * @return
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Collection executeCriteriaQuery(Class criteriaClass) throws InfrastructureException {
        Criteria criteria;
        Collection results = new ArrayList();
        try {
            criteria = HibernateUtil.getSession().createCriteria(criteriaClass);
            for (Iterator i = userCriteria.iterator(); i.hasNext();) {
                Criterion criterion = (Criterion) i.next();
                criteria.add(criterion);
            }
            results = criteria.list();
            userCriteria.clear();

            return results;
        }
        catch (HibernateException he) {
            throw new InfrastructureException(he);
        }
    }

    /**
     * Exectues a named query
     *
     * @param queryName
     * @return
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Collection executeNamedQuery(String queryName) throws InfrastructureException {
        try {
            Query query = HibernateUtil.getSession().getNamedQuery(queryName);
            for (Iterator i = queryParams.iterator(); i.hasNext();) {
                QueryParam param = (QueryParam) i.next();
                query.setParameter(param.paramName, param.paramValue);
            }
            if (queryParams.size() > 0) {
                queryParams.clear();
            }
            return query.list();
        }
        catch (HibernateException he) {
            throw new InfrastructureException(he);
        }
    }

    public Collection executeQuery(String queryString) throws InfrastructureException {
        return executeQuery(queryString, 0);
    }

    public Object uniqueResult(String queryString) throws InfrastructureException {
        try {
            Query query = processQuery(queryString, 0);
            return query.uniqueResult();
        }
        catch (HibernateException he) {
            throw new InfrastructureException(he);
        }
    }

    public Collection executeQuery(String queryString, int maxResults) throws InfrastructureException {
        try {
            Query query = processQuery(queryString, maxResults);
            return query.list();
        }
        catch (HibernateException he) {
            throw new InfrastructureException(he);
        }
    }

    private Query processQuery(String queryString, int maxResults) throws InfrastructureException {
        Query query = HibernateUtil.getSession().createQuery(queryString);
        for (Iterator i = queryParams.iterator(); i.hasNext();) {
            QueryParam param = (QueryParam) i.next();
            if (!(param.paramValue instanceof Collection)) {
                query.setParameter(param.paramName, param.paramValue);
            } else {
                query.setParameterList(param.paramName, (Collection) param.paramValue);
            }
        }
        if (queryParams.size() > 0) {
            queryParams.clear();
        }
        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }
        return query;
    }

    /**
     * Exectues a SQL query
     *
     * @param queryName
     * @return
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Collection executeSqlQuery(String queryName) throws InfrastructureException {
        try {
            SQLQuery query = HibernateUtil.getSession().createSQLQuery(queryName);

            return query.list();
        }
        catch (HibernateException he) {
            throw new InfrastructureException(he);
        }
    }

    /**
     * Executes find all query for a given mapped Class
     *
     * @param clazz
     * @return
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Collection findAll(Class clazz) throws InfrastructureException {
        try {
            return HibernateUtil.getSession().createCriteria(clazz).list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public Collection findAll(Class clazz, int firstResult, int maxResults) throws InfrastructureException {
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(clazz);
            HibernateDAO.handleCriteriaPagination(criteria, firstResult, maxResults);
            return criteria.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    /**
     * Loads a mapped Class
     *
     * @param lock
     * @param clazz
     * @param id
     * @return
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Object loadById(Long id, Class clazz, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Object entity = null;
        try {
            if (lock) {
                entity = session.load(clazz, id, LockMode.UPGRADE);
            } else {
                entity = session.load(clazz, id);
            }
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return entity;
    }

    public Object getById(Long id, Class clazz) throws InfrastructureException {
        return HibernateUtil.getSession().get(clazz, id);
    }

    /**
     * Persists a mapped class instance
     *
     * @param entity
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void makePersistent(Object entity) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(entity);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    /**
     * Removes a mapped class instance
     *
     * @param entity
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void makeTransient(Object entity) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(entity);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public Collection findByExample(Class clazz, Object example) throws InfrastructureException {
        Collection entities;
        try {
            Criteria crit = HibernateUtil.getSession().createCriteria(clazz);
            entities = crit.add(Example.create(example)).list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return entities;
    }

    /**
     * Inner class that represents a query parameter
     */
    private class QueryParam {

        private String paramName;
        private Object paramValue;

        private QueryParam(String paramName, Object paramValue) {
            this.paramName = paramName;
            this.paramValue = paramValue;
        }
    }

    public static void handleQueryPagination(Query query, int firstResult, int maxResults) {
        if (firstResult >= 0 && maxResults >= 0) {
            query.setFirstResult(firstResult).setMaxResults(maxResults);
        }
    }

    public static void handleCriteriaPagination(Criteria criteria, int firstResult, int maxResults) {
        if (firstResult >= 0 && maxResults >= 0) {
            criteria.setFirstResult(firstResult).setMaxResults(maxResults);
        }
    }
}