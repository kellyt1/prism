package us.mn.state.health.dao.legacySystem.fixedAssets;

import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.legacySystem.fixedAssets.FixedAssetOracle;
import us.mn.state.health.model.legacySystem.inventory.StockInv;
import us.mn.state.health.persistence.HibernateUtil;

public class FixedAssetOracleDAO extends HibernateDAO{
    public Collection findAll() throws InfrastructureException {
        return super.findAll(FixedAssetOracle.class);
    }

    public StockInv getFixedAssetOracleByAssetNumber(Integer id) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Object entity = null;
        try {
            /**
             * we use get cuz' we want to return null if we cann't find the requested  FixedAssetOracle
             */
            entity = session.get(FixedAssetOracle.class, id, LockMode.READ);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return (StockInv) entity;
    }

}
