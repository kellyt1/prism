package us.mn.state.health.dao.inventory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.util.search.PurchaseItemIndex;
import us.mn.state.health.model.util.search.StockItemIndex;

import java.util.ArrayList;
import java.util.Collection;

public class LuceneItemDAO implements ItemDAO {
    private static Log log = LogFactory.getLog(LuceneItemDAO.class);

    public LuceneItemDAO() {
    }

    public Item getItemById(Long itemId, boolean lock) throws InfrastructureException {
        return null;
    }

    public Collection findItemsUsingIds(Collection ids) throws InfrastructureException {
        return null;
    }

    public Collection findAll() throws InfrastructureException {
        //TODO refactor this method - its just a stub right now.
        Collection items;
        PurchaseItemIndex itemIndex = new PurchaseItemIndex();
        try {
            items = itemIndex.search("");
        }
        catch (Exception ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }

    public Collection findAll(int firstResult, int maxResults) throws InfrastructureException {
        return null;
    }

    public Collection findAllActive(int firstResult, int maxResults) throws InfrastructureException {
        return null;
    }

    public Collection findByExample(Item item) throws InfrastructureException {
        return null;
    }

    public Collection findByCategoryCode(String categoryCode) throws InfrastructureException {
        Collection items = null;
        PurchaseItemIndex itemIndex = new PurchaseItemIndex();
        //TODO refactor this method
        try {
            items = itemIndex.search(us.mn.state.health.model.util.search.PurchaseItemIndex.CATEGORY_CODE + ":" + categoryCode);
        }
        catch (Exception ex) {
            throw new InfrastructureException(ex);
        }
        return items;
    }

    public Collection findByKeywords(String queryString) throws InfrastructureException {
        PurchaseItemIndex purchaseItemIndex = new PurchaseItemIndex();
        StockItemIndex stockItemIndex = new StockItemIndex();
        Collection items = new ArrayList();

        try {
            items = purchaseItemIndex.search(queryString);
            items.addAll(stockItemIndex.search(queryString));
//            CollectionUtils.sort(items, "description", true);
        }
        catch (Exception e) {
            log.error(e);
            throw new InfrastructureException(e);
        }

        return items;
    }

    public Collection findByKeywords(String queryString, int firstResult, int maxResults, String orderBy) throws InfrastructureException {
        Collection items = new ArrayList();
        Collection results = new ArrayList();

        try {
            items = findByKeywords(queryString);
            CollectionUtils.sort(items, "description", true);
            Object[] itemsArray = items.toArray();
            for (int i = firstResult; i < (firstResult + maxResults) && i < items.size(); i++) {
                results.add(itemsArray[i]);
            }
        }
        catch (Exception e) {
            log.error(e);
            throw new InfrastructureException(e);
        }

        return results;
    }

    public Collection findByCategory(Category cat, int firstResult, int maxResults, String orderBy) throws InfrastructureException {
        return null;
    }

    public Collection findContractsWhereWithVendor(Long vendorId) throws InfrastructureException {
        return null;
    }

    public void makePersistent(Item item) throws InfrastructureException {
//        PurchaseItemIndex itemIndex = new PurchaseItemIndex();
//        try {
//            itemIndex.add(item);
//        }
//        catch (Exception ex) {
//            throw new InfrastructureException(ex);
//        }
    }

    public void makeTransient(Item item) throws InfrastructureException {
//        PurchaseItemIndex itemIndex = new PurchaseItemIndex();
//        try {
//            itemIndex.drop(item);
//        }
//        catch (Exception ex) {
//            throw new InfrastructureException(ex);
//        }
    }

    public Item getItemById(Long itemId) throws InfrastructureException {
        return null;
    }
}
