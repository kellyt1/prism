package us.mn.state.health.dao;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateExternalOrgDetailDAO implements ExternalOrgDetailDAO {

    public HibernateExternalOrgDetailDAO() {
        try {
            HibernateUtil.beginTransaction();
        } 
        catch(InfrastructureException ie) { }
    }

    public ExternalOrgDetail getExternalOrgDetailById(Long externalOrgDetailId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        ExternalOrgDetail externalOrgDetail = null;
        try {
            if(lock) {
                externalOrgDetail = (ExternalOrgDetail) session.load(ExternalOrgDetail.class, externalOrgDetailId, LockMode.UPGRADE);
            } 
            else {
                externalOrgDetail = (ExternalOrgDetail) session.load(ExternalOrgDetail.class, externalOrgDetailId);
            }
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return externalOrgDetail;
    }

    public Collection findAll(boolean excludeVendors) throws InfrastructureException {
        Collection externalOrgDetails;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(ExternalOrgDetail.class);
            if(excludeVendors) {
                criteria.add(Restrictions.sqlRestriction("{alias}.org_id not in (select distinct v.org_id from VENDOR_TBL v)"));
            }

            criteria.addOrder(Order.asc("upperOrgName"));
            criteria.setCacheable(true);            
            externalOrgDetails = criteria.list();
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return externalOrgDetails;
    }
    
    public Collection findAll() throws InfrastructureException {
        return findAll(false);
    }
    
    public Collection findByNameFirstCharRange(char start, char end, boolean excludeVendors) throws InfrastructureException {
        Collection externalOrgs;
        try {
            Session session = HibernateUtil.getSession();
            Query query = null;
            
            String startStr = "" + start + "%";
            String endStr = "" + end + "%";  
            
            Criteria criteria = session.createCriteria(ExternalOrgDetail.class);
            Disjunction disjunction =  Restrictions.disjunction();
            disjunction.add(Expression.between("upperOrgName",startStr, endStr));        //between excludes endStr
            disjunction.add(Expression.like("upperOrgName", endStr));                    //so we have to add a like statement here
            criteria.add(disjunction);
            if(excludeVendors) {
                criteria.add(Restrictions.sqlRestriction("{alias}.org_id not in (select distinct v.org_id from VENDOR_TBL v)"));
            }
            criteria.addOrder(Order.asc("orgName"));
            externalOrgs = criteria.list();
        }
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return externalOrgs;
    }
    
    public Collection findByNameFirstCharRange(char start, char end) throws InfrastructureException {
        return findByNameFirstCharRange(start, end, false); //returns results with vendors
    }
    
    public ExternalOrgDetail findByOrgName(String name) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        ExternalOrgDetail externalOrgDetail = null;
        try {
            Query query = session.getNamedQuery("findByOrgName");
            query.setString("name", name.toLowerCase().trim());
            query.setMaxResults(1);
            externalOrgDetail = (ExternalOrgDetail)query.uniqueResult();
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return externalOrgDetail;
    }
    
    public Collection findByExample(ExternalOrgDetail externalOrgDetail) throws InfrastructureException {
        Collection externalOrgDetails;
        try {
            Criteria crit = HibernateUtil.getSession().createCriteria(ExternalOrgDetail.class);
            externalOrgDetails = crit.add(Example.create(externalOrgDetail)).list();
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return externalOrgDetails;
    }

    public void makePersistent(ExternalOrgDetail externalOrgDetail) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(externalOrgDetail);
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
    
   public void makeTransient(ExternalOrgDetail externalOrgDetail) throws InfrastructureException {
		try {
			HibernateUtil.getSession().delete(externalOrgDetail);
		} 
        catch(HibernateException ex) {
			throw new InfrastructureException(ex);
		}
	}
}
