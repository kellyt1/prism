package us.mn.state.health.dao.legacySystem.inventory;

import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAO;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.legacySystem.inventory.Inventory;
import us.mn.state.health.model.legacySystem.inventory.Purchase;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateInventoryDAO extends HibernateDAO implements DAO {

    public HibernateInventoryDAO() throws InfrastructureException {
        HibernateUtil.beginTransaction();
    }

    public Collection findAll(int firstResult, int maxResults) throws InfrastructureException {
        return super.findAll(Inventory.class,firstResult, maxResults);
    }

    public Purchase getStockInvByICNBR(String id) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Object entity = null;
        try {
            /**
             * we use get cuz' we want to return null if we cann't find the requested Vendr
             */
            entity = session.get(Inventory.class, id, LockMode.READ);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return (Purchase) entity;
    }

    public Collection findAll() throws InfrastructureException {
        return findAll(-1,-1);
    }
}
