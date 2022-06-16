package us.mn.state.health.dao.inventory;
import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateDAO;

public class HibernateSensitiveAssetDAO extends HibernateDAO implements SensitiveAssetDAO {
    
    public Collection findAllByItemId(Long itemId) throws InfrastructureException {
        return null;
    }
    
}