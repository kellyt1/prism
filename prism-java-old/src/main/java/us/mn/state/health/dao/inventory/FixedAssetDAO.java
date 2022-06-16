package us.mn.state.health.dao.inventory;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAO;
import us.mn.state.health.model.inventory.FixedAsset;

public interface FixedAssetDAO extends DAO {
    public Collection findAllByItemId(Long itemId) throws InfrastructureException;
    public FixedAsset findFixedAssetByAssetNumber(String assetNumber) throws InfrastructureException;
}