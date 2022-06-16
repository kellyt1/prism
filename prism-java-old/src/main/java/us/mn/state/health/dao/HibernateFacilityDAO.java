package us.mn.state.health.dao;
import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Facility;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateFacilityDAO extends HibernateDAO implements FacilityDAO {

    public HibernateFacilityDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch(InfrastructureException ie) {
        }
    }
    
    public Facility getFacilityById(Long facilityId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Facility facility = null;
        try {
            if(lock) {
                facility = (Facility) session.load(Facility.class, facilityId, LockMode.UPGRADE);
            } 
            else {
                facility = (Facility) session.load(Facility.class, facilityId);
            }
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return facility;
    }
    
    public Collection findAllFacilities() throws InfrastructureException {
        Collection facilities;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(Facility.class);
            criteria.addOrder(Order.asc("facilityName"));
            criteria.setCacheable(true);
            facilities = criteria.list();
        }
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return facilities;
    }

    /**
     * This method uses a named query which filters out facilities whose end date is not null 
     * (i.e., it filters out facilities that have 'expired').
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     * @return 
     * @param facilityType
     */
    public Collection findFacilitiesByType(String facilityType) throws InfrastructureException {    
        Collection facilities;
        try {
            Query query = HibernateUtil.getSession().getNamedQuery("findFacilitiesByType");
            query.setString("facilityType", facilityType);
            query.setCacheable(true);
            facilities = query.list();            
        }
        catch(HibernateException he) {
            throw new InfrastructureException(he);
        }
        return facilities;
    }
}