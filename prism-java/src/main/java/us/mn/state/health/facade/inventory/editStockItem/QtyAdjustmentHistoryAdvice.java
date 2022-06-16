package us.mn.state.health.facade.inventory.editStockItem;

import java.lang.reflect.Method;
import java.util.Date;

import org.springframework.aop.MethodBeforeAdvice;
import us.mn.state.health.domain.repository.common.UserRepository;
import us.mn.state.health.domain.repository.inventory.StockItemRepository;
import us.mn.state.health.model.inventory.StockQtyAdjustmentHistory;

public class QtyAdjustmentHistoryAdvice implements MethodBeforeAdvice {
    private StockItemRepository stockItemRepository;
    private UserRepository userRepository;


    public QtyAdjustmentHistoryAdvice(StockItemRepository stockItemRepository, UserRepository userRepository) {
        this.stockItemRepository = stockItemRepository;
        this.userRepository = userRepository;
    }

    public void before(Method method, Object[] args, Object target) throws Throwable {
        String username = (String) args[1];
        StockQtyAdjustmentHistory adjustmentHistory = (StockQtyAdjustmentHistory) args[3];
        if (adjustmentHistory.getChangeReason() != null) {
            adjustmentHistory.setChangeDate(new Date());
            adjustmentHistory.setChangedBy(userRepository.getUser(username));
            stockItemRepository.makePersistent(adjustmentHistory);
        }
    }
}
