package us.mn.state.health.dao.legacySystem.inventory;

import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.legacySystem.inventory.Vendr;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateVendrDAO extends HibernateDAO{

    public Collection findAll() throws InfrastructureException {
        return super.findAll(Vendr.class);
    }

    public Vendr getVendrById(String id) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Object entity = null;
        try {
            /**
             * we use get cuz' we want to return null if we cann't find the requested Vendr
             */
            entity = session.get(Vendr.class, id, LockMode.READ);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return (Vendr) entity;
    }

    
}
