package us.mn.state.health.facade.inventory;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.domain.repository.common.GroupRepository;
import us.mn.state.health.domain.repository.common.UserRepository;
import us.mn.state.health.domain.repository.inventory.StockItemRepository;
import us.mn.state.health.domain.service.inventory.StockItemService;
import us.mn.state.health.domain.service.inventory.StockItemServiceResult;
import us.mn.state.health.domain.service.util.EmailCommand;
import us.mn.state.health.domain.service.util.MailService;
import us.mn.state.health.domain.service.util.StockItemHitReviewDateEmailCommand;
import us.mn.state.health.domain.service.util.StockItemReactivationEmailCommand;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.util.Email;

public class StockItemFacadeImpl implements StockItemFacade {
    public static Log log = LogFactory.getLog(StockItemFacadeImpl.class);
    private StockItemService stockItemService;
    private MailService mailService;
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private StockItemRepository stockItemRepository;


    public StockItemRepository getStockItemRepository() {
        return stockItemRepository;
    }

    public void setStockItemRepository(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    public void setStockItemService(StockItemService stockItemService) {
        this.stockItemService = stockItemService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }


    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public StockItemFacadeResult activateOnHoldStockItems() {

        StockItemServiceResult stockItemServiceResult;
        stockItemServiceResult = stockItemService.activateOnHoldStockItems();

        return buildResult(stockItemServiceResult);
    }

    public void notifyStockItemHitReviewDate() {
        List<StockItem> stockItems = stockItemRepository.getStockItemsHitReviewDateBetweenDates(new Date(), new Date());
        for (StockItem item : stockItems) {
            EmailCommand emailCommand = new StockItemHitReviewDateEmailCommand(userRepository, groupRepository, mailService, new Email(), item);
            emailCommand.executeSendEmail();
        }
    }

    //we have to pass a proxy reference to this method in order to be able to save the changes to the database
    public StockItemFacadeResult activateOnHoldStockItemsAndNotifyContacts(StockItemFacade stockItemFacade) {
        //1 - activate stock items
        List<StockItem> stockItems = stockItemFacade.activateOnHoldStockItems().getStockItems();
        //2 - notify the owners that the stock item has been reactivated
        notifyContactsAboutStockItemReactivation(stockItems);
        return buildResult(stockItems);
    }

    public void notifyContactsAboutStockItemReactivation(List<StockItem> stockItems) {
        for (StockItem stockItem : stockItems) {
            EmailCommand emailCommand = new StockItemReactivationEmailCommand(userRepository, groupRepository, mailService, new Email(), stockItem);
            emailCommand.executeSendEmail();
        }
    }

    private StockItemFacadeResult buildResult(StockItemServiceResult stockItemServiceResult) {
        StockItemFacadeResult facadeResult = new StockItemFacadeResult();
        facadeResult.setStockItems(stockItemServiceResult.getStockItems());
        facadeResult.setStatus(StockItemFacadeResult.OK_STATUS);
        return facadeResult;
    }

    private StockItemFacadeResult buildResult(List<StockItem> stockItems) {
        StockItemFacadeResult facadeResult = new StockItemFacadeResult();
        facadeResult.setStockItems(stockItems);
        facadeResult.setStatus(StockItemFacadeResult.OK_STATUS);
        return facadeResult;
    }
}
