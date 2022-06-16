package us.mn.state.health.model.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.PurchaseItem;
import us.mn.state.health.model.inventory.SensitiveAsset;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.model.purchasing.OrderLineItem;

public class HibernateModelDetacher implements ModelDetacher {
    private static Log log = LogFactory.getLog(HibernateModelDetacher.class);
    public void detachRequest(Request request) {
        if (request == null) return;
        Hibernate.initialize(request);
        Hibernate.initialize(request.getPriority());
        Hibernate.initialize(request.getRequestor());
        Hibernate.initialize(request.getRequestLineItems());
        for (Object o : request.getRequestLineItems()) {
            RequestLineItem rli = (RequestLineItem) o;
            detachRequestLineItem(rli);
        }
    }

    public void detachRequestLineItem(RequestLineItem requestLineItem) {
        if (requestLineItem == null) return;
        Hibernate.initialize(requestLineItem);
        Hibernate.initialize(requestLineItem.getRequest());
        Hibernate.initialize(requestLineItem.getStatus());
        Hibernate.initialize(requestLineItem.getFundingSources());
        for (Object fs : requestLineItem.getFundingSources()) {
            Hibernate.initialize(fs);
            Hibernate.initialize(((RequestLineItemFundingSource) fs).getOrgBudget());
            try {
            for (Object reportCategory : ((RequestLineItemFundingSource) fs).getOrgBudget().getRptCategories()) {
                Hibernate.initialize(reportCategory);
            }
            }catch(Exception e) {
                log.error("Error " + e);
            }
        }
        Hibernate.initialize(requestLineItem.getPurchaser());
        Hibernate.initialize(requestLineItem.getItemCategory());
        Hibernate.initialize(requestLineItem.getItem());

        if (requestLineItem.getItem() != null) {
            Hibernate.initialize(requestLineItem.getItem().getCategory());
            detachItemVendors(requestLineItem.getItem());
        }
    }

    private static void detachItemVendors(Item item) {
        Hibernate.initialize(item.getItemVendors());
        for (Object iv : item.getItemVendors()) {
            ItemVendor itemVendor = (ItemVendor) iv;
            Hibernate.initialize(itemVendor.getVendor());
        }
    }

    public void detachStockItem(StockItem stockItem) {
        if (stockItem == null) return;
        detachItem(stockItem);
        Hibernate.initialize(stockItem.getPrimaryContact());
        Hibernate.initialize(stockItem.getSecondaryContact());
        Hibernate.initialize(stockItem.getOrgBudget());
        Hibernate.initialize(stockItem.getAsstDivDirector());
        Hibernate.initialize(stockItem.getStatus());
        Hibernate.initialize(stockItem.getDiscontinued());
    }

    public void detachPurchaseItem(PurchaseItem purchaseItem) {
        if (purchaseItem == null) return;
        detachItem(purchaseItem);
        detachItemVendors(purchaseItem);
    }

    public void detachItem(Item item) {
        if (item == null) return;
        Hibernate.initialize(item);
        Hibernate.initialize(item.getCategory());
        Hibernate.initialize(item.getManufacturer());
        Hibernate.initialize(item.getDispenseUnit());
        detachItemVendors(item);
    }

    public void detachAsset(SensitiveAsset sensitiveAsset) {
        if (sensitiveAsset == null) return;
        Hibernate.initialize(sensitiveAsset);
        Hibernate.initialize(sensitiveAsset.getItem());
        Hibernate.initialize(sensitiveAsset.getItem().getCategory());
        Hibernate.initialize(sensitiveAsset.getItem().getManufacturer());
        Hibernate.initialize(sensitiveAsset.getVendor());
        Hibernate.initialize(sensitiveAsset.getFacility());
        Hibernate.initialize(sensitiveAsset.getStatus());
        Hibernate.initialize(sensitiveAsset.getContactPerson());
        Hibernate.initialize(sensitiveAsset.getOwnerOrgBudgets());
        for (Object o : sensitiveAsset.getOwnerOrgBudgets()) {
            Hibernate.initialize(o);
        }
    }

    public void detachOrder(Order order) {
        if (order == null) return;
        Hibernate.initialize(order);
        Hibernate.initialize(order.getPurchaser());
        Hibernate.initialize(order.getShipToAddress());
        Hibernate.initialize(order.getVendor());
        Hibernate.initialize(order.getVendorContract());
        Hibernate.initialize(order.getRequestLineItems());
        Hibernate.initialize(order.getOrderLineItems());
        for (Object o : order.getOrderLineItems()) {
            detachOrderLineItem((OrderLineItem) o);
        }
        for (Object o : order.getRequestLineItems()) {
            detachRequestLineItem((RequestLineItem) o);
        }
    }

    public void detachOrderLineItem(OrderLineItem orderLineItem) {
        if (orderLineItem == null) return;
        Hibernate.initialize(orderLineItem);
        Hibernate.initialize(orderLineItem.getBuyUnit());
        Hibernate.initialize(orderLineItem.getStatus());
        Hibernate.initialize(orderLineItem.getItem());
//        Hibernate.initialize(orderLineItem.getRequestLineItem());
//        Hibernate.initialize(orderLineItem.getRequestLineItem().getRequest());
//        Hibernate.initialize(orderLineItem.getRequestLineItem().getFundingSources());
//        for (Object o : orderLineItem.getRequestLineItem().getFundingSources()) {
//            Hibernate.initialize(((RequestLineItemFundingSource) o).getOrgBudget());
//        }
        detachRequestLineItem(orderLineItem.getRequestLineItem());
    }
}
