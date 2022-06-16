package us.mn.state.health.dao.inventory;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateUnitDAO implements UnitDAO {

    public HibernateUnitDAO() {
        try {
            HibernateUtil.beginTransaction();
        } 
        catch (InfrastructureException e) {
        }
    }
    
    public Unit getUnitById(Long unitId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Unit unit = null;
        try {
            if(lock) {
                unit = (Unit)session.load(Unit.class, unitId, LockMode.UPGRADE);
            } 
            else {
                unit = (Unit)session.load(Unit.class, unitId);
            }
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return unit;
    }

    public Collection findAll(boolean withUnknown) throws InfrastructureException {
        Collection units;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(Unit.class);
            criteria.addOrder(Order.asc("name"));
            if (!withUnknown) {
                criteria.add(Expression.not(Expression.eq("code", Unit.CODE_UNKNOWN)));
            }
            criteria.setCacheable(true);
            units = criteria.list();
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return units;
    }

    public Unit findUnitByCode(String code) throws InfrastructureException {
        try {
            Query query = HibernateUtil.getSession().getNamedQuery("findUnitByCode");
            query.setString("code", code);
            Collection c = query.list();
            if (c.size() == 1) {
                return (Unit) c.iterator().next();
            }
            return null;
        } 
        catch(HibernateException e) {
            throw new InfrastructureException(e);
        }
    }

    public void makePersistent(Unit unit) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(unit);
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(Unit unit) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(unit);
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }


}