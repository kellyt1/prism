package us.mn.state.health.dao.purchasing;

import java.util.Collection;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernatePurchasingOrderLineItemDAO extends HibernateDAO implements PurchasingOrderLineItemDAO {

    public OrderLineItem getOrderLineItemById(Long id, boolean lock) throws InfrastructureException {
        return (OrderLineItem) super.loadById(id, OrderLineItem.class, lock);
    }

    public Collection findAllOrderLineItems() throws InfrastructureException {
        return super.findAll(OrderLineItem.class);
    }

    /**
     * returns a collection of ReceivingDetail objects.
     *
     * @param endDate
     * @param startDate
     * @param facilityId
     * @return
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Collection findStockItemsToBeStocked(String facilityId, Date startDate, Date endDate) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection receivingDetails;
        try {
            receivingDetails = session.getNamedQuery("findStockItemsToBeStocked")
                    .setDate("startDate", startDate)
                    .setDate("endDate", endDate)
                    .setString("facilityId", facilityId)
                    .list();

        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }

        return receivingDetails;
    }

    /**
     * Gets list of Request objects that have a RequestLineItem whose OrderLineItem has a null Item
     * or a reference to a PurchaseItem and has been received against at the given facility within
     * the given date range.
     *
     * @param endDate
     * @param startDate
     * @param facilityId
     * @return
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Collection findPurchaseItemsToBeDelivered(String facilityId, Date startDate, Date endDate) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection requests;
        try {
            requests = session.getNamedQuery("findPurchaseItemsToBeDelivered")
                    .setDate("startDate", startDate)
                    .setDate("endDate", endDate)
                    .setString("facilityId", facilityId)
                    .list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }

        return requests;
    }

    public OrderLineItem getOrderLineItemById(Long orderLnItmId) throws InfrastructureException {
        Object oli = super.getById(orderLnItmId, OrderLineItem.class);
        return oli == null ? null : (OrderLineItem) oli;
    }
}
