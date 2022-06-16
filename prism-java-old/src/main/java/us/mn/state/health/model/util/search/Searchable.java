package us.mn.state.health.model.util.search;

import us.mn.state.health.common.exceptions.InfrastructureException;

public interface Searchable {
    final Class[] IMPLEMENTATIONS = new Class[]{AssetIndex.class,
//            OrderIndex.class,
//            OrderLineItemIndex.class,
            RequestIndex.class,
            RequestLineItemIndex.class,
            PurchaseItemIndex.class,
            StockItemIndex.class
    };

    /**
     * Get Lucene Document describing our searchable content.  The
     * term keywords "id" and "classname" are reserved by our interceptor.
     */
    public EntityIndex getEntityIndex() throws InfrastructureException;
}