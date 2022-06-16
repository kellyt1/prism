package us.mn.state.health.dao.inventory;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.inventory.FixedAsset;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateFixedAssetDAO extends HibernateDAO implements FixedAssetDAO {

    public Collection findAllByItemId(Long itemId) throws InfrastructureException {
        return null;
    }

    public FixedAsset findFixedAssetByAssetNumber(String assetNumber) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection list = new ArrayList();
        Criteria crit = session.createCriteria(FixedAsset.class);
        crit.add(Expression.eq("fixedAssetNumber", assetNumber));
        crit.setMaxResults(1);
        list = crit.list();
        if(list == null || list.size()==0){
            return null;
        }
        else {
            return (FixedAsset) list.iterator().next();
        }
    }

}