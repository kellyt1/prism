package us.mn.state.health.dao.inventory;

import org.hibernate.*;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HibernateStockItemDAO extends HibernateItemDAO implements StockItemDAO {

    public HibernateStockItemDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch (InfrastructureException ie) {
            ie.printStackTrace();
        }
    }

    public StockItem getStockItemById(Long stockItemId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        StockItem stockItem = null;
        try {
            if (lock) {
                stockItem = (StockItem) session.load(StockItem.class, stockItemId, LockMode.UPGRADE);
            } else {
                stockItem = (StockItem) session.load(StockItem.class, stockItemId);
            }
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return stockItem;
    }

    public Collection findAll() throws InfrastructureException {
        Collection stockItems;
        try {
            stockItems = HibernateUtil.getSession()
                    .createCriteria(StockItem.class)
                    .add(Restrictions.ne("potentialIndicator", Boolean.TRUE))
                    .list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return stockItems;
    }

    public Collection findAll(int firstResult, int maxResults) throws InfrastructureException {
        Collection stockItems;
        try {
            Session session = HibernateUtil.getSession();

            //We had to do this trick here because the stock items have some property that executes a lot of queries
            // (currentDemand)

            String hql = "select si.itemId from StockItem si where si.potentialIndicator='N' order by si.description asc";
            String hql1 = "select si from StockItem si where si.itemId in (:ids) order by si.description asc";
            List itemIds = new ArrayList();
            Query query = session.createQuery(hql);
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            itemIds = query.list();

            if (itemIds.size() == 0) {
                return new ArrayList();
            }

            Query query1 = session.createQuery(hql1);
            query1.setParameterList("ids", itemIds);
            stockItems = query1.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return stockItems;
    }

    public Collection findAllActive(int firstResult, int maxResults) throws InfrastructureException {
        Collection stockItems;
        try {
            Session session = HibernateUtil.getSession();

            //We had to do this trick here because the stock items have some property that executes a lot of queries
            // (currentDemand)

            String hql = "select si.itemId from StockItem si where si.potentialIndicator='N' " +
                    "and si.status.statusCode='ACT' order by si.description asc";
            String hql1 = "select si from StockItem si where si.itemId in (:ids) order by si.description asc";
            List itemIds = new ArrayList();
            Query query = session.createQuery(hql);
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            itemIds = query.list();

            if (itemIds.size() == 0) {
                return new ArrayList();
            }

            Query query1 = session.createQuery(hql1);
            query1.setParameterList("ids", itemIds);
            stockItems = query1.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return stockItems;
    }

    public Collection findByStatusCode(String statusCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection stockItems;
        try {
            stockItems = session.getNamedQuery("findByStatusCode")
                    .setString("statusCode", statusCode)
                    .list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return stockItems;
    }

    public Collection findByExample(StockItem stockItem) throws InfrastructureException {
        Collection stockItems;
        try {
            Criteria crit = HibernateUtil.getSession().createCriteria(StockItem.class);
            stockItems = crit.add(Example.create(stockItem)).list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return stockItems;
    }

    public void makePersistent(StockItem stockItem) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(stockItem);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(StockItem stockItem) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(stockItem);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    /**
     * This method finds the next available ICNBR by searching for the
     * max number and adding 1 to it.
     *
     * @return next available ICNBR
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Integer findNextICNBR() throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Integer nextNbr = null;
        try {
            Object o = session.getNamedQuery("findNextICNBR")
                    .uniqueResult();
            if (o != null) {
                nextNbr = new Integer(o.toString());
            }
            if (nextNbr == null) {
                nextNbr = new Integer(1);   // default to 1
            }
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return nextNbr;
    }

    public Collection findByContactPerson(Person person) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection items = null;
        try {
            items = session.getNamedQuery("findStockItemsByContactPerson")
                    .setLong("personId", person.getPersonId().longValue())
                    .list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }


    public Collection findActiveByContactPerson(Person person) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection items = null;
        try {
            items = session.getNamedQuery("findActiveStockItemsByContactPerson")
                    .setLong("personId", person.getPersonId().longValue())
                    .list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }

    public Collection findByContactPerson(Person person, int firstResult, int maxResults) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection items = null;
        try {
            Query query = session.getNamedQuery("findStockItemsByContactPerson");
            query.setLong("personId", person.getPersonId().longValue());
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            items = query.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }

    public StockItem findStockItemByCategoryCodeAndItemCode(String categoryCode, String icnbr) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection items = null;
        items = session.getNamedQuery("findStockItemByCategoryCodeAndItemCode")
                .setString("categoryCode", categoryCode)
                .setInteger("icnbr", Integer.valueOf(icnbr).intValue()).list();
        if (items == null || items.size() == 0) {
            return null;
        } else {
            return (StockItem) items.iterator().next();
        }
    }

    public Collection findByCategoryCode(String categoryCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection items = null;
        try {
            items = session.getNamedQuery("findStockItemByCategoryCode")
                    .setString("categoryCode", categoryCode)
                    .list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }

    public Collection findByCategory(Category category, int firstResult, int maxResults, String orderBy) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection items = null;
        try {
            Criteria crit = session.createCriteria(StockItem.class);
            crit.add(Expression.eq("category", category));
            crit.add(Restrictions.ne("potentialIndicator", Boolean.TRUE));
            if ("desc".equals(orderBy)) {
                crit.addOrder(Order.desc("description"));
            } else {
                crit.addOrder(Order.asc("description"));
            }
            crit.setMaxResults(maxResults);
            crit.setFirstResult(firstResult);
            items = crit.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }

    public Collection findItemsUsingIds(Collection ids) throws InfrastructureException {
        List results = new ArrayList();
        if (ids.size() == 0) {
            return results;
        }
        Session session = HibernateUtil.getSession();
        Criteria criteria = session.createCriteria(StockItem.class);
        criteria.add(Expression.in("itemId", ids));
        results = criteria.list();
        return results;
    }
}
