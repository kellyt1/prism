package us.mn.state.health.dao.materialsrequest;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.dao.inventory.HibernateItemDAO;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.PurchaseItem;
import us.mn.state.health.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class HibernatePurchaseItemDAO extends HibernateItemDAO implements PurchaseItemDAO {

    public HibernatePurchaseItemDAO() {
        try {
            //HibernateUtil.registerInterceptor(new LuceneInterceptor());
            HibernateUtil.beginTransaction();
        } 
        catch (InfrastructureException ie) {
        }
    }

    public Collection findAll() throws InfrastructureException {
        Collection purchaseItems;
        try {
            purchaseItems = HibernateUtil.getSession().createCriteria(PurchaseItem.class).list();
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return purchaseItems;
    }

    public Collection findAll(int firstResult, int maxResults) throws InfrastructureException {
        Collection items;
        try {
            Criteria crit = HibernateUtil.getSession().createCriteria(PurchaseItem.class);
            crit.addOrder(Order.asc("description"));
            crit.setFirstResult(firstResult);
            crit.setMaxResults(maxResults);
            items = crit.list();
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }
    public Collection findAllActive(int firstResult, int maxResults) throws InfrastructureException {
        //PurchaseItem does not have status so return all for allActive
        return findAll(firstResult, maxResults);
    }


    public Collection findByExample(Item item) throws InfrastructureException {
        return null;
    }

    public Collection findByCategoryCode(String categoryCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection items = null;
        try {
            items = session.getNamedQuery("findPurchaseItemByCategoryCode")
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
            Criteria crit = session.createCriteria(PurchaseItem.class);
            crit.add(Restrictions.eq("category", category));
//            crit.add(Restrictions.gt("endDate",new Date()));
//            Criterion endDateGtToday = Restrictions.gt("endDate",new Date());
//            Criterion endDateNull = Restrictions.eq("endDate",null);
//            crit.add(Restrictions.or(endDateGtToday,endDateNull));

            crit.add(Restrictions.or(Restrictions.gt("endDate",new Date()),Restrictions.isNull("endDate")));

            if("desc".equals(orderBy)){
              crit.addOrder(Order.desc("description"));
            }
            else {
                crit.addOrder(Order.asc("description"));
            }

            // The list is displayed like 0-20, 21-40, etc.  If list < 20, causes issue.
            if (crit.list().size() < 20)
                crit.setMaxResults(crit.list().size());
            else
                crit.setMaxResults(maxResults);

            if (firstResult < 0)
                crit.setFirstResult(0);
            else
                crit.setFirstResult(firstResult);

            items = crit.list();
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }

    public Collection findByExample(PurchaseItem purchaseItem) throws InfrastructureException {
        return new HibernateDAO().findByExample(PurchaseItem.class,purchaseItem);
    }

    public PurchaseItem findPurchaseItemById(Long purchaseItemId, boolean lock) throws InfrastructureException {
        return (PurchaseItem) new HibernateDAO().loadById(purchaseItemId, PurchaseItem.class, lock);
    }

    public Collection findItemsUsingIds(Collection ids) throws InfrastructureException {
        List results = new ArrayList();
        if(ids.size()==0){
            return results;
        }
        Session session = HibernateUtil.getSession();
        Criteria criteria = session.createCriteria(PurchaseItem.class);
        criteria.add(Expression.in("itemId",ids));
        results = criteria.list();
        return results;
    }

    public void makePersistent(PurchaseItem purchaseItem) throws InfrastructureException {
        try {
            //HibernateUtil.registerInterceptor(new LuceneInterceptor());
            HibernateUtil.getSession().saveOrUpdate(purchaseItem);
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(PurchaseItem purchaseItem) throws InfrastructureException {
        try {
            //HibernateUtil.registerInterceptor(new LuceneInterceptor());
            HibernateUtil.getSession().delete(purchaseItem);
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
}
