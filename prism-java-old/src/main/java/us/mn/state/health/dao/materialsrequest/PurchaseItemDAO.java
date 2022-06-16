package us.mn.state.health.dao.materialsrequest;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.inventory.ItemDAO;
import us.mn.state.health.model.inventory.PurchaseItem;

public interface PurchaseItemDAO extends ItemDAO {
    public Collection findByExample(PurchaseItem purchaseItem) throws InfrastructureException;
    public PurchaseItem findPurchaseItemById(Long purchaseItemId, boolean lock) throws InfrastructureException;
    public void makePersistent(PurchaseItem purchaseItem) throws InfrastructureException;
    public void makeTransient(PurchaseItem purchaseItem) throws InfrastructureException;
}
