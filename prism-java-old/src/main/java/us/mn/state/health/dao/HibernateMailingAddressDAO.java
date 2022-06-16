package us.mn.state.health.dao;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateMailingAddressDAO extends HibernateDAO implements MailingAddressDAO {

    public HibernateMailingAddressDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch(InfrastructureException ie) {
        }
    }
    
    public MailingAddress getMailingAddressById(Long mailingAddressId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        MailingAddress mailingAddress = null;
        try {
            if(lock) {
                mailingAddress = (MailingAddress) session.load(MailingAddress.class, mailingAddressId, LockMode.UPGRADE);
            } 
            else {
                mailingAddress = (MailingAddress) session.load(MailingAddress.class, mailingAddressId);
            }
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return mailingAddress;
    }
    
    public Collection findAll() throws InfrastructureException {
        Collection mailingAddresses;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(MailingAddress.class);
            criteria.setCacheable(true);
            mailingAddresses = criteria.list();
        }
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return mailingAddresses;
    }
    
    public Collection findBillingAddresses() throws InfrastructureException {
        Collection results;
        Session session = HibernateUtil.getSession();
        try {
            results = session.getNamedQuery("findBillingAddresses")
                             .setCacheable(true)
                             .list();
        }
        catch(HibernateException e) {
            throw new InfrastructureException(e);
        }
        return results;
    }
    
    public Collection findShippingAddresses() throws InfrastructureException {
        Collection results;
        Session session = HibernateUtil.getSession();
        try {
            results = session.getNamedQuery("findShippingAddresses")
                             .setCacheable(true)
                             .list();
        }
        catch(HibernateException e) {
            throw new InfrastructureException(e);
        }
        return results;
    }
    
    public void makePersistent(MailingAddress mailingAddress) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(mailingAddress);
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
    
   public void makeTransient(MailingAddress mailingAddress) throws InfrastructureException {
		try {
			HibernateUtil.getSession().delete(mailingAddress);
		} 
        catch(HibernateException ex) {
			throw new InfrastructureException(ex);
		}
	}

}