/* Generated by Together */

package us.mn.state.health.dao.inventory;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.inventory.StockItem;

import java.util.Collection;

public interface StockItemDAO extends ItemDAO { 
    public Collection findByExample(StockItem stockItem) throws InfrastructureException; 
    public Collection findByStatusCode(String statusCode) throws InfrastructureException; 
    public StockItem getStockItemById(Long stockItemId, boolean lock) throws InfrastructureException;
    public void makePersistent(StockItem stockItem) throws InfrastructureException;
    public void makeTransient(StockItem stockItem) throws InfrastructureException;   
    public Integer findNextICNBR() throws InfrastructureException;
    public Collection findByContactPerson(Person person) throws InfrastructureException;
    public Collection findActiveByContactPerson(Person person) throws InfrastructureException;
    public Collection findByContactPerson(Person person, int firstResult, int maxResults) throws InfrastructureException;
    public StockItem findStockItemByCategoryCodeAndItemCode(String categoryCode, String icnbr) throws InfrastructureException;
    public Collection<StockItem> findItemsUsingIds(Collection ids) throws InfrastructureException;
}
