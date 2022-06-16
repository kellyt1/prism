package us.mn.state.health.facade.inventory;

public interface StockItemFacade {
    StockItemFacadeResult activateOnHoldStockItemsAndNotifyContacts(StockItemFacade stockItemFacade);

    StockItemFacadeResult activateOnHoldStockItems();

    void notifyStockItemHitReviewDate();

}
