package us.mn.state.health.dao.materialsrequest;

import java.util.Collection;
import java.util.Date;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.materialsrequest.RequestLineItem;

public interface RequestLineItemDAO {
    public Collection findAll() throws InfrastructureException;

    public Collection findAllRequestLineItemsWithStatusCode(String statusCode) throws InfrastructureException;

    public RequestLineItem getRequestLineItemById(Long requestLineItemId, boolean lock) throws InfrastructureException;

    public void makePersistent(RequestLineItem requestLineItem) throws InfrastructureException;

    public void makeTransient(RequestLineItem requestLineItem) throws InfrastructureException;

    Collection findStockItemOutTransactionsWithFS(String orgBdgtCode, String[] fiscalYear, Date startDate, Date endDate, String categoryId, String icnbr) throws InfrastructureException;

    Collection findStockItemInTransactions(String orgBdgtCode, Date startDate, Date endDate, String categoryId, String icnbr) throws InfrastructureException;

    Collection findOrderedPurchaseItemTransactions(String orgBdgtCode, String[] fiscalYear, Date startDate, Date endDate, String categoryId) throws InfrastructureException;

    Collection findStockItemOutTransactionsWithoutFS(Date startDate, Date endDate, String categoryId, String icnbr) throws InfrastructureException;

    RequestLineItem getRequestLineItemById(Long requestLineItemId) throws InfrastructureException;
}
