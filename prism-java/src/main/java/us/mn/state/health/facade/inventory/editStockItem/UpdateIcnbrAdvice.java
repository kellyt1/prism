package us.mn.state.health.facade.inventory.editStockItem;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;
import us.mn.state.health.domain.repository.inventory.StockItemRepository;
import us.mn.state.health.model.inventory.StockItem;

/**
 * This advice is changing the ICBNR if the category is different
 * It is applied to EditStockItemFacade.attachStockItem(StockItem stockItem)
 */
public class UpdateIcnbrAdvice implements MethodBeforeAdvice {
    private EditStockItemFacade facade;
    private StockItemRepository repository;


    public UpdateIcnbrAdvice(EditStockItemFacade facade, StockItemRepository repository) {
        this.facade = facade;
        this.repository = repository;
    }

    public void before(Method method, Object[] args, Object target) throws Throwable {
        StockItem stockItem = (StockItem) args[0];
        StockItem previousStockItem = facade.detachStockItem(stockItem.getItemId());
        if (!stockItem.getCategory().equals(previousStockItem.getCategory())) {
            stockItem.setIcnbr(repository.findNextICNBR());
        }
    }
}
