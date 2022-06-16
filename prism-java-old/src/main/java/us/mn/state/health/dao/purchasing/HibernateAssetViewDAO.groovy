package us.mn.state.health.dao.purchasing

import org.hibernate.Session
import us.mn.state.health.dao.HibernateDAO
import us.mn.state.health.model.purchasing.AssetView
import us.mn.state.health.persistence.HibernateUtil

/**
 * User: kiminn1
 * Date: 11/8/2017
 * Time: 5:02 PM
 */

class HibernateAssetViewDAO implements AssetViewDAO {

    @Override
    List<AssetView> findAssetsSinceOrderDate(Date orderDate) {
        Session session = HibernateUtil.getSession()
        session.getNamedQuery("assetsSinceOrderDate")
            .setParameter("orderDate",orderDate)
            .list()
    }
}