package us.mn.state.health.builder.inventory;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.view.inventory.StockItemsForm;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StockItemsFormBuilder {

    private StockItemsForm stockItemsForm;
    private DAOFactory daoFactory;
    private User user;

    public StockItemsFormBuilder(StockItemsForm stockItemsForm, DAOFactory daoFactory, User user) {

        this.stockItemsForm = stockItemsForm;
        this.daoFactory = daoFactory;
        this.user = user;
    }

    public void buildStockItems() throws InfrastructureException {
        Collection stockItems = daoFactory.getStockItemDAO().findAll();
        stockItemsForm.setStockItems(stockItems);
    }

    public void buildMyStockItems(final String orderBy, Boolean activeOnly) throws InfrastructureException {
        Collection<StockItem> myStockItems;
        if (activeOnly) {
            myStockItems = daoFactory.getStockItemDAO().findActiveByContactPerson(user);
        } else {
             myStockItems = daoFactory.getStockItemDAO().findByContactPerson(user);
        }
        Collections.sort((List) myStockItems, new Comparator() {
            public int compare(Object o1, Object o2) {
                StockItem si1 = (StockItem) o1;
                StockItem si2 = (StockItem) o2;
                if (StockItem.ICNBR.equals(orderBy)) return si1.getIcnbr().compareTo(si2.getIcnbr());
                if (StockItem.UNIT.equals(orderBy)) return si1.getDispenseUnit().getName().compareTo(si2.getDispenseUnit().getName());
                if (StockItem.DESCRIPTION.equals(orderBy)) return si1.getDescription().compareTo(si2.getDescription());
                if (StockItem.ORGANIZATION.equals(orderBy))
                    return si1.getOrgBudget().getOrgBudgetCodeAndName().
                            compareTo(si2.getOrgBudget().getOrgBudgetCodeAndName());
                if (StockItem.PRIMARY_CONTACT.equals(orderBy))
                    return si1.getPrimaryContact().
                            getFirstAndLastName().compareTo(si2.getPrimaryContact().getFirstAndLastName());
                if (StockItem.SECONDARY_CONTACT.equals(orderBy))
                    return si1.getSecondaryContact().
                            getFirstAndLastName().compareTo(si2.getSecondaryContact().getFirstAndLastName());
                if (StockItem.USAGE.equals(orderBy)) return si1.getAnnualUsage().compareTo(si2.getAnnualUsage());

                return 0;

            }
        });
        stockItemsForm.setStockItems(myStockItems);
    }
}