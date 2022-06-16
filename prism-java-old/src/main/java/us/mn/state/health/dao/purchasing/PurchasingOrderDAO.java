package us.mn.state.health.dao.purchasing;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAO;
import us.mn.state.health.model.purchasing.Order;

public interface PurchasingOrderDAO extends DAO {

    public Order getOrderById(Long orderId, boolean lock) throws InfrastructureException;

    public Collection findAllOrders() throws InfrastructureException;

    public Collection search(Order order) throws InfrastructureException;

    public Collection findOrdersUsingIds(Collection ids) throws InfrastructureException;

    public Collection findOrdersUsingIds(Collection ids, int firstResult, int maxResults) throws InfrastructureException;

    Order findOrderByRequestLineItemId(Long requestLineItemId) throws InfrastructureException;

    Order getOrderById(Long id) throws InfrastructureException;
}