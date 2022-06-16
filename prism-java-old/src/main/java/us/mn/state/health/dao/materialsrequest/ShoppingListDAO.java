package us.mn.state.health.dao.materialsrequest;
import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.materialsrequest.ShoppingList;

public interface ShoppingListDAO  {

    public Collection findShoppingListsWithUserId(Long userId) throws InfrastructureException;
    public Collection findShoppingListsAll() throws InfrastructureException;
    public Collection findParitShoppingListsAll() throws InfrastructureException;
    public ShoppingList getShoppingListById(Long shoppingListId) throws InfrastructureException;
    public ShoppingList getShoppingListByIdEagerLoadCatItems(Long shoppingListId) throws InfrastructureException;
    public void makePersistent(ShoppingList shoppingList) throws InfrastructureException;
    public void makeTransient(ShoppingList shoppingList) throws InfrastructureException;
    
}