package us.mn.state.health.dao.inventory;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.inventory.Item;

import java.util.Collection;

public interface ItemDAO {
    public Collection findAll() throws InfrastructureException;

    public Collection findAll(int firstResult, int maxResults) throws InfrastructureException;

    public Collection findAllActive(int firstResult, int maxResults) throws InfrastructureException;

    public Collection findByKeywords(String queryString) throws InfrastructureException;

    public Collection findByKeywords(String queryString, int firstResult, int maxResults, String orderBy) throws InfrastructureException;

    public Collection findByExample(Item item) throws InfrastructureException;

    public Collection findByCategoryCode(String categoryCode) throws InfrastructureException;

    public Collection findByCategory(Category category, int firstResult, int maxResults, String orderBy) throws InfrastructureException;

    public Collection findContractsWhereWithVendor(Long vendorId) throws InfrastructureException;

    public Item getItemById(Long id, boolean lock) throws InfrastructureException;

    public Collection findItemsUsingIds(Collection ids) throws InfrastructureException;

    public void makePersistent(Item item) throws InfrastructureException;

    public void makeTransient(Item item) throws InfrastructureException;

    Item getItemById(Long itemId) throws InfrastructureException;
}
