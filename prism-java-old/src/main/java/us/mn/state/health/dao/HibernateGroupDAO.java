package us.mn.state.health.dao;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateGroupDAO implements GroupDAO {

    public HibernateGroupDAO() {
        try {
            HibernateUtil.beginTransaction();
        } 
        catch(InfrastructureException ie) { }
    }

    public Group getGroupById(Long groupId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Group group = null;
        try {
            if(lock) {
                group = (Group) session.load(Group.class, groupId, LockMode.UPGRADE);
            } 
            else {
                group = (Group) session.load(Group.class, groupId);
            }
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return group;
    }

    public Collection findAll() throws InfrastructureException {
        Collection groups;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(Group.class);
            criteria.addOrder(Order.asc("groupName"));
            criteria.setCacheable(true);
            groups = criteria.list();
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return groups;
    }
    
    public Collection findByExample(Group group) throws InfrastructureException {
        Collection groups;
        try {
            Criteria crit = HibernateUtil.getSession().createCriteria(Group.class);
            groups = crit.add(Example.create(group)).list();
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return groups;
    }

    public void makePersistent(Group group) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(group);
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
    
   public void makeTransient(Group group) throws InfrastructureException {
		try {
			HibernateUtil.getSession().delete(group);
		} 
        catch(HibernateException ex) {
			throw new InfrastructureException(ex);
		}
	}
    
    public Collection findGroupsByPersonId(Long personId) throws InfrastructureException {
        Collection groups;
        Session session = HibernateUtil.getSession();
        try {
            groups = session.getNamedQuery("findGroupsByPersonId")
                            .setLong("personId", personId.longValue())
                            .list();
        }
        catch(HibernateException e) {
            throw new InfrastructureException(e);
        }
        return groups;
    }
    
    public Group findGroupByName(String groupName) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
		Group group = null;
		try {
			group = (Group)session.getNamedQuery("findGroupByName")
                                  .setString("groupName", groupName)
                                  .uniqueResult();
		}  
        catch(HibernateException ex) {
			throw new InfrastructureException(ex);
		}
		return group;
    }
    
    public Group findGroupByCode(String groupCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
		Group group = null;
		try {
			group = (Group)session.getNamedQuery("findGroupByCode")
                                  .setString("groupCode", groupCode)
                                  .uniqueResult();
		}  
        catch(HibernateException ex) {
			throw new InfrastructureException(ex);
		}
		return group;
    }
}
