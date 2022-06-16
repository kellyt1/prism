
package us.mn.state.health.dao;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.materialsrequest.DeliveryDetail;

import java.util.Collection;

public interface DeliveryDetailDAO {
    public DeliveryDetail getDeliveryDetailById(Long id) throws InfrastructureException;
}
