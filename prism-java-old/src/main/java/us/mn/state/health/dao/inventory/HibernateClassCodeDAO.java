package us.mn.state.health.dao.inventory;

import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.inventory.ClassCode;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateClassCodeDAO implements ClassCodeDAO {

	public HibernateClassCodeDAO() {
        try{
            HibernateUtil.beginTransaction();
        }
        catch(InfrastructureException ie){}		
	}
    
     public ClassCode getClassCodeById(Long classCodeId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        ClassCode classCode = null;
        try {
            if(lock) {
                classCode = (ClassCode)session.load(ClassCode.class, classCodeId, LockMode.UPGRADE);
            } 
            else {
                classCode = (ClassCode)session.load(ClassCode.class, classCodeId);
            }
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return classCode;
    }

    public ClassCode getClassCodeByCode(String code) throws InfrastructureException{
        Session session = HibernateUtil.getSession();
        Collection list = new ArrayList();
        Criteria crit = session.createCriteria(ClassCode.class);
        crit.add(Expression.eq("classCodeValue",code));
        crit.setMaxResults(1);
        list = crit.list();
        if(list == null || list.size()==0){
            return null;
        }
        else {
            return (ClassCode) list.iterator().next();
        }
    }

    public Collection findAll() throws InfrastructureException {
		Collection classCodes;
		try {
			classCodes = HibernateUtil.getSession()
                                      .createCriteria(us.mn.state.health.model.inventory.ClassCode.class)
                                      .list();
		} 
        catch(HibernateException ex) {
			throw new InfrastructureException(ex);
		}
		return classCodes;
	}

    public void makePersistent(ClassCode classCode) throws InfrastructureException {
		try {
            HibernateUtil.getSession().saveOrUpdate(classCode);
		} 
        catch(HibernateException ex) {
			throw new InfrastructureException(ex);
		}
	}

	public void makeTransient(ClassCode classCode) throws InfrastructureException {
		try {
            HibernateUtil.getSession().delete(classCode);
		} 
        catch(HibernateException ex) {
			throw new InfrastructureException(ex);
		}
	}
}
