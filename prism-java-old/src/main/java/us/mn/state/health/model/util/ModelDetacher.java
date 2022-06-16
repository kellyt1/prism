package us.mn.state.health.model.util;

import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.PurchaseItem;
import us.mn.state.health.model.inventory.SensitiveAsset;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.model.purchasing.OrderLineItem;

public interface ModelDetacher {

    void detachRequest(Request request);

    void detachRequestLineItem(RequestLineItem requestLineItem);

    void detachStockItem(StockItem stockItem);

    void detachPurchaseItem(PurchaseItem purchaseItem);

    void detachAsset(SensitiveAsset sensitiveAsset);

    void detachOrder(Order order);

    void detachOrderLineItem(OrderLineItem orderLineItem);

    void detachItem(Item item);
}
