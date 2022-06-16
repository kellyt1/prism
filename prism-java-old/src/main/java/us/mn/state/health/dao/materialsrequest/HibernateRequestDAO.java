package us.mn.state.health.dao.materialsrequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.util.HibernateCriteriaBuilder;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateRequestDAO implements RequestDAO {

    public HibernateRequestDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch (InfrastructureException e) {
            e.printStackTrace();
        }
    }

    public Collection findAll() throws InfrastructureException {
        Collection requests;
        try {
            requests = HibernateUtil.getSession().createCriteria(Request.class).list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return requests;
    }

    public Collection findByEvaluatorAndStatusCode(Person evaluator, String statusCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection requests;
        try {
            requests = session.getNamedQuery("findByEvaluatorAndStatusCode")
                    .setString("statusCode", statusCode)
                    .setEntity("evaluator", evaluator)
                    .setCacheable(true)
                    .list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return requests;
    }

    /**
     * @param statusCode
     * @return the requests that have the request line items with the status.statusCode=statusCode
     * @throws InfrastructureException
     */
    public Collection findByStatusCode(String statusCode) throws InfrastructureException {
        return findByStatusCode(statusCode, null);
    }

    /**
     * Count the number of query results without actually returning them.
     *
     * @param statusCode
     * @return
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Integer countRequestsByStatusCode(String statusCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Object uniqueResult = session.getNamedQuery("countRequestsByStatusCode")
                .setString("statusCode", statusCode)
                .uniqueResult();
        return (uniqueResult == null ? null : (new Integer(uniqueResult.toString())));
    }

    /**
     * @param statusCode
     * @param firstResult
     * @param maxResults
     * @return the requests that have the request line items with the status.statusCode=statusCode
     * @throws InfrastructureException
     */
    public Collection findByStatusCode(String statusCode, Integer firstResult, Integer maxResults) throws InfrastructureException {
        return findByStatusCode(statusCode, null, firstResult, maxResults);
    }

    /**
     * @param statusCode
     * @param aliases    - a map that contains pairs alias(key)-association path(value) for building the aliases in order
     *                   to have a join and eagerly fetch certain properties that are mapped as associations
     * @return the requests that have the request line items with the status.statusCode=statusCode
     * @throws InfrastructureException
     */

    public Collection findByStatusCode(String statusCode, Map aliases) throws InfrastructureException {
        return findByStatusCode(statusCode, aliases, null, null);
    }


    /**
     * @param statusCode
     * @param aliases     - a map that contains pairs alias(key)-association path(value) for building the aliases in order
     *                    to have a join and eagerly fetch certain properties that are mapped as associations
     * @param firstResult
     * @param maxResults
     * @return the requests that have the request line items with the status.statusCode=statusCode
     * @throws InfrastructureException
     */
    public Collection findByStatusCode(String statusCode, Map aliases, Integer firstResult, Integer maxResults) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection requestIds;
        Collection results = new ArrayList();

        try {
            //first, grab all the requests with the appropriate status
            if (firstResult == null || maxResults == null) {
                requestIds = session.getNamedQuery("findRequestIdsByStatusCodeAsc")
                        .setString("statusCode", statusCode).setFirstResult(0).setMaxResults(500)
                        .list();
            } else {
                requestIds = session.getNamedQuery("findRequestIdsByStatusCodeAsc")
                        .setString("statusCode", statusCode)
                        .setFirstResult(firstResult.intValue() - 1)
                        .setMaxResults(maxResults.intValue())
                        .list();
            }
            results = session.getNamedQuery("findRequestByIdsEagerRLIEagerItemAsc")
                    .setParameterList("ids", requestIds)
                    .list();
            results = new ArrayList(new LinkedHashSet(results));
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        catch (Exception ex) {
            throw new InfrastructureException(ex);
        }

        return results;
    }

    public Request getRequestById(Long requestId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Request request = null;
        try {
            if (lock) {
                request = (Request) session.load(Request.class, requestId, LockMode.UPGRADE);
            } else {
                request = (Request) session.load(Request.class, requestId);
            }
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return request;
    }

    /**
     * This class queries the REQUEST_TRACKING_NBR_SEQ directly, using a strategy I found
     * at http://forum.hibernate.org/viewtopic.php?t=939398
     * It works, but requires (minimal) use of JDBC.
     * I added the "MRQ" the beginning of the tracking number, for no reason - just 'becuz'.  MRQ is
     * code for "Materials Request".
     *
     * @return the next value from REQUEST_TRACKING_NBR_SEQ sequence
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public String findNextTrackingNumber() throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        String query = null;
        String result = null;
        try {
            Number nextval = (Number) new HibernateDAO().executeSqlQuery("select request_tracking_nbr_seq.nextval from  dual").iterator().next();
            long o = nextval.longValue();
//            Dialect dialect = Dialect.getDialect(HibernateUtil.getConfiguration().getProperties());
//            query = dialect.getSequenceNextValString("request_tracking_nbr_seq");
//            PreparedStatement ps = session.connection().prepareStatement(query);
//            ResultSet rs = ps.executeQuery();
//            rs.next();
            result = "MRQ-" + o;
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        if (result == null) {
            throw new InfrastructureException(new Exception("Unable to get a request tracking number."));
        }
        return result;
    }

    public Collection findMyRequests(User user) throws InfrastructureException {
        return findMyRequests(user, -1, -1);
    }

    public Collection findMyRequests(User user, int firstResult, int maxResults) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection results = new ArrayList();
        Query query = session.getNamedQuery("findMyRequests")
                .setEntity("requestor", user);
        HibernateDAO.handleQueryPagination(query, firstResult, maxResults);
        results = query.list();
        return results;
    }

    /**
     * @param rliIds
     * @return the requests that have the request line items with the requestLineItemId in the rliIds
     * @throws InfrastructureException
     */
    public Collection findRequestsUsingRliId(Collection rliIds) throws InfrastructureException {
//        results = session
//                .getNamedQuery("findRequestsUsingRliId")
//                .setParameterList("rliIds",rliIds)
//                .list();
        return findRequestsUsingRliId(rliIds, null, -1, -1);
    }

    /**
     * @param rliIds
     * @param firstResult
     * @param maxResults
     * @return the requests that have the request line items with the requestLineItemId in the rliIds
     * @throws InfrastructureException
     */
    public Collection findRequestsUsingRliId(Collection rliIds, int firstResult, int maxResults) throws InfrastructureException {
        return findRequestsUsingRliId(rliIds, null, firstResult, maxResults);
    }

    /**
     * @param rliIds
     * @param aliases - a map that contains pairs alias(key)-association path(value) for building the aliases in order
     *                to have a join and eagerly fetch certain properties that are mapped as associations
     * @return the requests that have the request line items with the requestLineItemId in the rliIds
     * @throws InfrastructureException
     */
    public Collection findRequestsUsingRliId(Collection rliIds, Map aliases) throws InfrastructureException {
        return findRequestsUsingRliId(rliIds, aliases, -1, -1);
    }

    /**
     * @param rliIds
     * @param aliases     - a map that contains pairs alias(key)-association path(value) for building the aliases in order
     *                    to have a join and eagerly fetch certain properties that are mapped as associations
     * @param firstResult
     * @param maxResults
     * @return the requests that have the request line items with the requestLineItemId in the rliIds
     * @throws InfrastructureException
     */
    public Collection findRequestsUsingRliId(Collection rliIds, Map aliases, int firstResult, int maxResults) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection results = new ArrayList();
        if (rliIds.size() <= 0) {
            return results;
        }

        Collection requestIds = new ArrayList();
        requestIds = session.getNamedQuery("findRequestIdsUsingRliId")
                .setParameterList("rliIds", rliIds)
                .list();

        Criteria criteria = session.createCriteria(Request.class)
                .createAlias("requestLineItems", "rli");
        if (firstResult >= 0 && maxResults >= 0) {
            int fromIndex = Math.max(firstResult, 0);
            int toIndex = Math.min(firstResult + maxResults, requestIds.size());
            if (fromIndex >= toIndex) {
                return results;
            }
            criteria.add(Expression.in("requestId", ((List) requestIds).subList(fromIndex, toIndex)));
        }
        if (aliases != null) {
            HibernateCriteriaBuilder builder = new HibernateCriteriaBuilder(criteria);
            builder.buildAliases(aliases);
        }
        results = new ArrayList(new LinkedHashSet(criteria.list()));
        return results;
    }

    public int countRequestsUsingRliId(Collection rliIds) throws InfrastructureException {
        if (rliIds.size() <= 0) {
            return 0;
        }
        Session session = HibernateUtil.getSession();
        return session.getNamedQuery("findRequestIdsUsingRliId")
                .setParameterList("rliIds", rliIds)
                .list().size();
    }


    public Collection findRequestsUsingIds(Collection ids) throws InfrastructureException {
        return findRequestsUsingIds(ids, -1, -1);
    }

    public Collection findRequestsUsingIds(Collection ids, int firstResult, int maxResults) throws InfrastructureException {
        Collection results = new ArrayList();
        if (ids.size() == 0) {
            return results;
        }
        Session session = HibernateUtil.getSession();
        Criteria criteria = session.createCriteria(Request.class)
                .createAlias("requestLineItems", "rli");

        if (firstResult >= 0 && maxResults >= 0) {
            int fromIndex = Math.max(firstResult, 0);
            int toIndex = Math.min(firstResult + maxResults, ids.size());
            if (fromIndex >= toIndex) {
                return results;
            }
            List subList = new LinkedList(ids).subList(fromIndex, toIndex);
            criteria.add(Expression.in("requestId", subList));
        } else {
            criteria.add(Expression.in("requestId", ids));
        }
        criteria.addOrder(Order.desc("requestId"));
        results = new LinkedHashSet(criteria.list());
        return results;
    }

    public List findIsOnOrderRequestsForStockItem(Long stockItemid) throws InfrastructureException {
        List results = new ArrayList();
        if (stockItemid == null) {
            return results;
        }
        results = (List) HibernateUtil.getSession().
                getNamedQuery("findIsOnOrderRequestsForStockItem").
                setParameter("stockItemId", stockItemid).list();
        return results;
    }

    public void makePersistent(Request request) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(request);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(Request request) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(request);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
}
