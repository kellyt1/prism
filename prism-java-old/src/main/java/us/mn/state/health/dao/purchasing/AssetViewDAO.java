package us.mn.state.health.dao.purchasing;

import us.mn.state.health.dao.DAO;
import us.mn.state.health.model.purchasing.AssetView;

import java.util.Date;
import java.util.List;

/**
 * User: kiminn1
 * Date: 11/8/2017
 * Time: 4:56 PM
 */

public interface AssetViewDAO {

    public List<AssetView> findAssetsSinceOrderDate(Date orderDate);
}
