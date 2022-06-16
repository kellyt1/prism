package us.mn.state.health.dao.inventory;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.inventory.ActionRequestType;
import us.mn.state.health.model.inventory.StockItemActionRequest;

public interface StockItemActionRequestDAO {
    public Collection findAll() throws InfrastructureException;
    public Collection findByExample(StockItemActionRequest stockItemActionRequest) throws InfrastructureException;
    public Collection findSIARByActionRequestType(ActionRequestType actionRequestType) throws InfrastructureException;
    public Collection findSIARByActionRequestTypeCode(String actionRequestTypeCode) throws InfrastructureException;
    public Collection findSIARByActionRequestTypeCodeAndStatusCode(String actionRequestTypeCode, String statusCode) throws InfrastructureException;
    public StockItemActionRequest findStockItemActionRequestById(Long stockItemActionRequestId, boolean lock) throws InfrastructureException;
    public void makePersistent(StockItemActionRequest stockItemActionRequest) throws InfrastructureException;
    public void makeTransient(StockItemActionRequest stockItemActionRequest) throws InfrastructureException;
    public Collection findByStatusCodes(String[] statusCodes) throws InfrastructureException;
    public Collection findByEvaluatorAndStatusCode(Person evaluator, String statusCode) throws InfrastructureException;
    public int countStockItemActionRequestWithStockItemIdByStatusCodes(Long stockItemId, String[] statusCodes) throws InfrastructureException;
    public Collection findSIARByPotentialItemId(long itemId) throws InfrastructureException;
}
