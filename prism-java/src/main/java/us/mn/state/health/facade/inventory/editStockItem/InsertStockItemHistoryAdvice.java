package us.mn.state.health.facade.inventory.editStockItem;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;
import us.mn.state.health.domain.repository.inventory.StockItemRepository;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.StockItemHistory;

/**
 * This advice is inserting the history of the stock item
 * It is applied to EditStockItemFacade.attachStockItem(StockItem stockItem)
 */
//public class InsertStockItemHistoryAdvice implements AfterReturningAdvice {
public class InsertStockItemHistoryAdvice implements MethodBeforeAdvice {
    private EditStockItemFacade facade;
    private StockItemRepository repository;

    public InsertStockItemHistoryAdvice(EditStockItemFacade facade, StockItemRepository repository) {
        this.facade = facade;
        this.repository = repository;
    }

    public void before(Method method, Object[] args, Object target) throws Throwable {
        StockItem stockItem = (StockItem) args[0];
        String username = (String) args[1];
        String historyComments = (String) args[2];
        StockItem previousStockItem = facade.detachStockItem(stockItem.getItemId());
        StockItemHistory stockItemHistory =
                StockItemHistory.createStockItemHistory(previousStockItem, stockItem, historyComments, username);
        if (stockItemHistory != null) {
            // this runs in the same tx as the method that is advised
            repository.makePersistent(stockItemHistory);
        }
    }
}
