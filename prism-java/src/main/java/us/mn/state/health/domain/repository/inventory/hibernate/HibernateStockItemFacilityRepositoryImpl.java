package us.mn.state.health.domain.repository.inventory.hibernate;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.domain.repository.inventory.StockItemFacilityRepository;
import us.mn.state.health.domain.repository.util.HibernateDomainRepositoryImpl;
import us.mn.state.health.model.inventory.StockItemFacility;

public class HibernateStockItemFacilityRepositoryImpl
        extends HibernateDomainRepositoryImpl implements StockItemFacilityRepository {


    public HibernateStockItemFacilityRepositoryImpl(HibernateTemplate hibernateTemplate) {
        super(hibernateTemplate);
    }

    public Collection findStockItemFacilitiesByType(String type) {
        return null;
    }

    public StockItemFacility getStockItemFacilityById(Long facilityId) {
        Object o = getHibernateTemplate().get(StockItemFacility.class, facilityId);
        return o != null ? (StockItemFacility) o : null;
    }

    public List findAll() {
        List stockItemFacilities;
        stockItemFacilities = (List) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(StockItemFacility.class);
                criteria.addOrder(Order.asc("facilityCode"));
                criteria.setCacheable(true);
                return criteria.list();
            }
        });
        return stockItemFacilities;
    }

    public void makePersistent(StockItemFacility facility) {

    }

    public void makeTransient(StockItemFacility facility) {

    }

    public StockItemFacility findByFacilityCode(String facilityCode) {
        return null;
    }
}
