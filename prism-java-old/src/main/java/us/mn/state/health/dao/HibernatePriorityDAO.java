package us.mn.state.health.dao;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Priority;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernatePriorityDAO implements PriorityDAO {

    public HibernatePriorityDAO() {
        try {
            HibernateUtil.beginTransaction();
        } 
        catch (InfrastructureException e) {
            e.printStackTrace();
        }
    }
    
    public Priority getPriorityById(Long priorityId, boolean lock) throws InfrastructureException {
		Session session = HibernateUtil.getSession();
		Priority priority = null;
		try {
			if (lock) {
				priority = (Priority) session.load(Priority.class, priorityId, LockMode.UPGRADE);
			} 
            else {
				priority = (Priority) session.load(Priority.class, priorityId);
			}
		}  
        catch (HibernateException ex) {
			throw new InfrastructureException(ex);
		}
		return priority;
	}

    public Collection findAll() throws InfrastructureException {
        Collection priorities;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(Priority.class);
            criteria.addOrder(Order.asc("name"));
            criteria.setCacheable(true);
            priorities = criteria.list();

            return priorities;
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
    
    public Priority findByPriorityCode(String priorityCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Priority priority = null;
        try {
            priority = (Priority)session.getNamedQuery("findPriorityByPriorityCode")
                                          .setString("priorityCode", priorityCode).setCacheable(true)
                                          .uniqueResult();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return priority;
    }
    
    public Priority findByPriorityName(String priorityName) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Priority priority = null;
        try {
            priority = (Priority)session.getNamedQuery("findPriorityByPriorityName")
                                        .setString("priorityName", priorityName)
                                        .uniqueResult();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return priority;
    }

    public void makePersistent(Priority priority) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(priority);
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(Priority priority) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(priority);
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
}