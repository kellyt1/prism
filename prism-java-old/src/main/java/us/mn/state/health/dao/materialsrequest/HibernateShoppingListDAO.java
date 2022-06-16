package us.mn.state.health.dao.materialsrequest;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.materialsrequest.ShoppingList;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateShoppingListDAO implements ShoppingListDAO {
    public HibernateShoppingListDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch (InfrastructureException e) {
        }
    }
    public Collection findShoppingListsAll() throws InfrastructureException {
              try {
                      Criteria criteria = HibernateUtil.getSession().createCriteria(ShoppingList.class);
                    criteria.addOrder(Order.asc("name"));
                    criteria.setCacheable(true);
                    return criteria.list();
              }
              catch (Exception e) {
                  throw new InfrastructureException(e);
              }

          }

    public Collection findParitShoppingListsAll() throws InfrastructureException {
              try {
                      Criteria criteria = HibernateUtil.getSession().createCriteria(ShoppingList.class);
                    criteria.addOrder(Order.asc("name"));
                    criteria.add(Restrictions.like("name","PARIT%"));
                    criteria.setCacheable(true);
                    return criteria.list();
              }
              catch (Exception e) {
                  throw new InfrastructureException(e);
              }
          }

    public Collection findShoppingListsWithUserId(Long userId) throws InfrastructureException {
        try {
            Query query = HibernateUtil.getSession().getNamedQuery("findShoppingListsWithUserId");
            query.setLong("userId", userId.longValue());

            return query.list();
        }
        catch (HibernateException e) {
            throw new InfrastructureException(e);
        }
    }

    public ShoppingList getShoppingListById(Long shoppingListId) throws InfrastructureException {
        try {
            return (ShoppingList) HibernateUtil.getSession().load(ShoppingList.class, shoppingListId);
        }
        catch (HibernateException e) {
            throw new InfrastructureException(e);
        }
    }

    public ShoppingList getShoppingListByIdEagerLoadCatItems(Long shoppingListId) throws InfrastructureException {
        HibernateDAO hibernateDAO = new HibernateDAO();
        hibernateDAO.addQueryParam("id", shoppingListId);
        ShoppingList shoppingList = null;

        Integer catItemsNo = new Integer(hibernateDAO.executeNamedQuery("countCatItemsForShoppingListId").iterator().next().toString());
        if (catItemsNo.intValue() > 0) {
            hibernateDAO.addQueryParam("id", shoppingListId);
            shoppingList = (ShoppingList) hibernateDAO.executeNamedQuery("findShoppingListByIdEagerLoadItem").iterator().next();
        } else {
            shoppingList = getShoppingListById(shoppingListId);
        }
        return shoppingList;
    }

    public void makePersistent(ShoppingList shoppingList) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(shoppingList);
        }
        catch (HibernateException e) {
            throw new InfrastructureException(e);
        }
    }

    public void makeTransient(ShoppingList shoppingList) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(shoppingList);
        }
        catch (HibernateException e) {
            throw new InfrastructureException(e);
        }
    }
}