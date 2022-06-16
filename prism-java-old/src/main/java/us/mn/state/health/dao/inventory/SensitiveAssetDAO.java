package us.mn.state.health.dao.inventory;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAO;

public interface SensitiveAssetDAO extends DAO {    
    public Collection findAllByItemId(Long itemId) throws InfrastructureException;
}