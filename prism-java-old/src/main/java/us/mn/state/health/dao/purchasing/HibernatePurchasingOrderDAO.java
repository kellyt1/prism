package us.mn.state.health.dao.purchasing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernatePurchasingOrderDAO extends HibernateDAO implements PurchasingOrderDAO {

    public Order getOrderById(Long id, boolean lock) throws InfrastructureException {
        return (Order) super.loadById(id, Order.class, lock);
    }

    public Order getOrderById(Long id) throws InfrastructureException {
        Object o = super.getById(id, Order.class);
        return o == null ? null : (Order) o;
    }

    public Collection findAllOrders() throws InfrastructureException {
        return super.findAll(Order.class);
    }

    public Collection search(Order order) throws InfrastructureException {
        if (order.getOrderId() != null) {
            super.addCriterion(Expression.idEq(order.getOrderId()));
        }
        return super.executeCriteriaQuery(Order.class);
    }

    public Collection findOrdersUsingIds(Collection ids) throws InfrastructureException {
//        return findOrdersUsingIds(ids,-1,-1);
        //TODO this is a temporary solution until we implement the pagination for view Orders and receive Orders
        ArrayList results = new ArrayList();
        Set sortedIds = new TreeSet(ids);
        ArrayList descSortedIds = new ArrayList();
        for (Iterator iterator = sortedIds.iterator(); iterator.hasNext();) {
            Long id = (Long) iterator.next();
            descSortedIds.add(0, id);
        }

        for (int i = 0; i < descSortedIds.size(); i = i + 500) {
            results.addAll(findOrdersUsingIds(
                    descSortedIds.subList(i, Math.min(i + 500, descSortedIds.size())), -1, -1)
            );
        }

        return results;
    }

    public Collection findOrdersUsingIds(Collection ids, int firstResult, int maxResults) throws InfrastructureException {
        Collection results = new ArrayList();
        if (ids.size() == 0) {
            return results;
        }
        Session session = HibernateUtil.getSession();
        Criteria criteria = session.createCriteria(Order.class);
        if (firstResult >= 0 && maxResults >= 0) {
            int fromIndex = Math.max(firstResult, 0);
            int toIndex = Math.min(firstResult + maxResults, ids.size());
            if (fromIndex >= toIndex) {
                return results;
            }
            List subList = new LinkedList(ids).subList(fromIndex,
                    toIndex);
            criteria.add(Expression.in("orderId", subList));
        } else {
            criteria.add(Expression.in("orderId", ids));
        }
        criteria.addOrder(org.hibernate.criterion.Order.desc("orderId"));
        results = new LinkedHashSet(criteria.list());
        return results;
    }

    public Order findOrderByRequestLineItemId(Long requestLineItemId) throws InfrastructureException {
        if (requestLineItemId != null) {
            addQueryParam("rliId", requestLineItemId);
            Collection orders = executeNamedQuery("findOrderByRequestLineItemId");
            if (orders.size() > 0) {
                return (Order) orders.iterator().next();
            }
        }
        return null;
    }
}
