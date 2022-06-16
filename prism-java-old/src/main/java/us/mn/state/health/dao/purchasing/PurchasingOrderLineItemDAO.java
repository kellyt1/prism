package us.mn.state.health.dao.purchasing;

import java.util.Collection;
import java.util.Date;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAO;
import us.mn.state.health.model.purchasing.OrderLineItem;

public interface PurchasingOrderLineItemDAO extends DAO {
    public OrderLineItem getOrderLineItemById(Long id, boolean lock) throws InfrastructureException;

    public Collection findAllOrderLineItems() throws InfrastructureException;

    public Collection findStockItemsToBeStocked(String facilityId, Date startDate, Date endDate) throws InfrastructureException;

    public Collection findPurchaseItemsToBeDelivered(String facilityId, Date startDate, Date endDate) throws InfrastructureException;

    OrderLineItem getOrderLineItemById(Long orderLnItmId) throws InfrastructureException;
}


