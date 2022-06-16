package us.mn.state.health.dao.inventory;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HibernateItemDAO extends HibernateDAO implements ItemDAO {

    public Item getItemById(Long itemId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Item item = null;
        try {
            if (lock) {
                item = (Item) session.get(us.mn.state.health.model.inventory.Item.class, itemId, LockMode.UPGRADE);
            } else {
                item = (Item) session.get(us.mn.state.health.model.inventory.Item.class, itemId);
            }
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        if (item == null) {
            throw new InfrastructureException(new Exception("Object not found in the database"));
        }
        return item;
    }

    public Item getItemById(Long itemId) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Item item = null;
        item = (Item) session.get(us.mn.state.health.model.inventory.Item.class, itemId);
        return item;
    }

    public Collection findItemsUsingIds(Collection ids) throws InfrastructureException {
        List results = new ArrayList();
        if (ids.size() == 0) {
            return results;
        }
        Session session = HibernateUtil.getSession();
        Criteria criteria = session.createCriteria(Item.class);
        criteria.add(Expression.in("itemId", ids));
        criteria.addOrder(Order.asc("description"));
        results = criteria.list();
        return results;
    }

    public Collection findAll() throws InfrastructureException {
        Collection items;
        try {
            items = HibernateUtil.getSession().createCriteria(us.mn.state.health.model.inventory.Item.class).list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }

    public Collection findAll(int firstResult, int maxResults) throws InfrastructureException {
        Collection items;
        try {
            Criteria crit = HibernateUtil.getSession().createCriteria(Item.class);
            crit.addOrder(Order.asc("description"));
            crit.setFirstResult(firstResult);
            crit.setMaxResults(maxResults);
            items = crit.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }

    public Collection findAllActive(int firstResult, int maxResults) throws InfrastructureException {
        //Item does not have a status to be active|inactive so return all
        return findAll(firstResult, maxResults);
    }

    public Collection findByKeywords(String queryString) throws InfrastructureException {
        return null;
    }

    public Collection findByKeywords(String queryString, int firstResult, int maxResults, String orderBy) throws InfrastructureException {
        return null;
    }

    public Collection findByExample(Item item) throws InfrastructureException {
        Collection items;
        try {
            Criteria crit = HibernateUtil.getSession().createCriteria(us.mn.state.health.model.inventory.Item.class);
            items = crit.add(Example.create(item)).list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }

    public Collection findByCategoryCode(String categoryCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection items = null;
        try {
            items = session.getNamedQuery("findItemByCategoryCode")
                    .setString("categoryCode", categoryCode)
//                    .setString("orderBy", "asc")
                    .list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }

    public Collection findByCategory(Category category, int firstResult, int maxResults, String orderBy) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        String query = "select i.itemId from Item i where " +
                "(" +
                "(select count(*) from StockItem si where si.itemId=i.itemId and si.potentialIndicator='N')>0" +
                " or " +
                "(select count(*) from StockItem si where si.itemId=i.itemId)=0" +
                ")" +
                " and i.category.categoryId " +
                "= " + category.getCategoryId() + " order by i.description asc";
        Collection items = new ArrayList();
        Collection itemIds = session.createQuery(query).setFirstResult(firstResult).setMaxResults(maxResults).list();
        if (itemIds.size() == 0) return items;
        items = findItemsUsingIds(itemIds);
        return items;
    }

    public Collection findContractsWhereWithVendor(Long vendorId) throws InfrastructureException {
        super.addQueryParam("vendorId", vendorId);
        return super.executeNamedQuery("findContractsWhereWithVendor");
    }


    public void makePersistent(Item item) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(item);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(Item item) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(item);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
}
