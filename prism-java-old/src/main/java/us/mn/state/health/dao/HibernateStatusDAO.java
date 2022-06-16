package us.mn.state.health.dao;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.persistence.HibernateUtil;

import java.util.Collection;

public class HibernateStatusDAO implements StatusDAO {

    public HibernateStatusDAO() {
        try {
            HibernateUtil.beginTransaction();
        } 
        catch (InfrastructureException e) {
        }
    }
    
    public Status getStatusById(Long id, boolean lock) throws InfrastructureException {
		Session session = HibernateUtil.getSession();
		Status status = null;
		try {
			if(lock) {
				status = (Status)session.load(Status.class, id, LockMode.UPGRADE);
			} 
            else {
				status = (Status)session.load(Status.class, id);
			}
		}  
        catch(HibernateException ex) {
			throw new InfrastructureException(ex);
		}
		return status;
	}
    
    public Collection findAllByStatusTypeAndStatusCodes(String statusTypeCode, String[] statusCodes) throws InfrastructureException {        
        Session session = HibernateUtil.getSession();
        try {
            String hql = "select status from Status as status where " +
                         "status.statusType.code = :statusTypeCode and status.statusCode in (";
                            
            for(int i = 0; i < statusCodes.length; i++) {
                hql += "'" + statusCodes[i] + "'";
                if(i < (statusCodes.length - 1)) {
                    hql += ", ";
                }
            }
            hql += ") order by status.name";
            Query query = session.createQuery(hql);
            query.setString("statusTypeCode", statusTypeCode);
            query.setCacheRegion("findAllByStatusTypeAndStatusCodes");
            query.setCacheable(true);
            
            return query.list();
        } 
        catch (HibernateException e) {
            throw new InfrastructureException(e);
        }
    }

    public Collection findAllByStatusTypeCode(String statusTypeCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection results;
        try {
            Query query = session.getNamedQuery("findAllByStatusTypeCode");
            query.setString("statusTypeCode", statusTypeCode);
            query.setCacheable(true);
            results = query.list();
        } 
        catch (HibernateException e) {
            throw new InfrastructureException(e);
        }
        return results;
    }

    public Status findByStatusTypeCodeAndStatusCode(String statusTypeCode, String statusCode) throws InfrastructureException {        
        Session session = HibernateUtil.getSession();
        Status result;
        try {
            Query query = session.getNamedQuery("findByStatusTypeCodeAndStatusCode");
            query.setString("statusTypeCode", statusTypeCode);
            query.setString("statusCode", statusCode);
            result = (Status)query.list().iterator().next();
        } 
        catch (HibernateException e) {
            throw new InfrastructureException(e);
        }
        return result;
    }

    // ??
    public Status getStatusByStatusCode(String statusCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Status result;
        try {
            Query query = session.getNamedQuery("getStatusByStatusCode");
            query.setString("statusCode", statusCode);
            result = (Status)query.list().iterator().next();
        }
        catch (HibernateException e) {
            throw new InfrastructureException(e);
        }
        return result;
    }

    public void makePersistent(Status status) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(status);
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(Status status) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(status);
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
}