package us.mn.state.health.dao.inventory;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.inventory.StockItemFacility;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateStockItemFacilityDAO implements StockItemFacilityDAO {

    public HibernateStockItemFacilityDAO() {
        try {
            HibernateUtil.beginTransaction();
        } 
        catch(InfrastructureException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This method uses a named query to find all stock item facilities of the given type. 
     * It filters out the facilities that have 'expired' (i.e., where endDate is not null).
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     * @return Collection of StockItemFacility objects
     * @param type
     */
    public Collection findStockItemFacilitiesByType(String type) throws InfrastructureException {
        try {       
            Query query = HibernateUtil.getSession().getNamedQuery("findStockItemFacilitiesByType");
            query.setString("facilityType", type);
            return query.list();
        } 
        catch(HibernateException e) {
            throw new InfrastructureException(e);
        }
    }
    
    public StockItemFacility getStockItemFacilityById(Long facilityId, boolean lock) throws InfrastructureException {
        StockItemFacility facility = null;
        try {
            if(lock) {
                facility = (StockItemFacility) HibernateUtil.getSession().load(StockItemFacility.class, facilityId, LockMode.UPGRADE);
            } 
            else {
                facility = (StockItemFacility) HibernateUtil.getSession().load(StockItemFacility.class, facilityId);
            }
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return facility;
    }

    public Collection findAll() throws InfrastructureException {
        Collection stockItemFacilities;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(StockItemFacility.class);
            criteria.addOrder(Order.asc("facilityCode"));
            criteria.setCacheable(true);
            stockItemFacilities = criteria.list();
        }
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return stockItemFacilities;
    }

    public void makePersistent(StockItemFacility facility) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(facility);
        }
        catch(HibernateException e) {
            throw new InfrastructureException(e);
        }
    }

    public void makeTransient(StockItemFacility facility) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(facility);
        } 
        catch(HibernateException e) {
            throw new InfrastructureException(e);
        }
    }

    /**
     * This method returns the stock item facility with the given code. 
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     * @return 
     * @param facilityCode
     */
    public StockItemFacility findByFacilityCode(String facilityCode) throws InfrastructureException {
        return (StockItemFacility)HibernateUtil.getSession()
                                               .createCriteria(StockItemFacility.class)
                                               .add(Expression.eq("facilityCode",facilityCode))
                                               .uniqueResult();
    }
}
