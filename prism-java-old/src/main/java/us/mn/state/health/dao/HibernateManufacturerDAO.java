package us.mn.state.health.dao;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Manufacturer;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateManufacturerDAO implements ManufacturerDAO {
    private static Log log = LogFactory.getLog(HibernateManufacturerDAO.class);

    public HibernateManufacturerDAO() {
        try {
            HibernateUtil.beginTransaction();
        } 
        catch(InfrastructureException e) {
            log.error("Begin Tx failed in HibernateManufacturerDAO()",e);
        }
    }

    public Manufacturer getManufacturerById(Long manufacturerId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Manufacturer manufacturer = null;
        try {
            if (lock) {
                manufacturer = (Manufacturer) session.load(Manufacturer.class, manufacturerId, LockMode.UPGRADE);
            } 
            else {
                manufacturer = (Manufacturer) session.load(Manufacturer.class, manufacturerId);
            }
        } 
        catch(HibernateException e) {
            log.error("Error in HibernateManufacturerDAO.getManufacturerById()",e);
            throw new InfrastructureException(e);
        }
        return manufacturer;
    }

    public Collection findAll(boolean withUnknown) throws InfrastructureException {
        Collection manufacturers = new ArrayList();

        Criteria criteria = HibernateUtil.getSession().createCriteria(Manufacturer.class);
        criteria.setCacheable(true);

        if(!withUnknown) {
            criteria.add(Expression.not(Expression.eq("manufacturerCode", Manufacturer.CODE_UNKNOWN)));
        }
        try {
            log.debug(criteria.toString());
            manufacturers = criteria.list();
        } 
        catch(HibernateException ex) {
            log.error(ex);
            throw new InfrastructureException(ex);
        }
        return manufacturers;
    }

    public Collection findByExample(Manufacturer manufacturer) throws InfrastructureException {
        Collection manufacturers;
        try {
            Criteria crit = HibernateUtil.getSession().createCriteria(Manufacturer.class);
            manufacturers = crit.add(Example.create(manufacturer)).list();
        } 
        catch(HibernateException ex) {
            log.error(ex);
            throw new InfrastructureException(ex);
        }
        return manufacturers;
    }

    public Manufacturer findManufacturerByCode(String code) throws InfrastructureException {
        try {
            Query query = HibernateUtil.getSession().getNamedQuery("findManufacturerByCode");
            query.setString("code", code);
            Collection c = query.list();
            if(c.size() == 1) {
                return (Manufacturer) c.iterator().next();
            }
            return null;
        } 
        catch(HibernateException ex) {
            log.error(ex);
            throw new InfrastructureException(ex);
        }
    }

    public void makePersistent(Manufacturer manufacturer) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(manufacturer);
        } 
        catch(HibernateException ex) {
            log.error(ex);
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(Manufacturer manufacturer) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(manufacturer);
        } 
        catch(HibernateException ex) {
            log.error(ex);
            throw new InfrastructureException(ex);
        }
    }
}
