package us.mn.state.health.dao.materialsrequest;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource;

public interface RequestLineItemFundingSourceDAO {
    public Collection findAll() throws InfrastructureException;
    public Collection findAllByRequestLineItemId(Long requestLineItemId) throws InfrastructureException;
    public RequestLineItemFundingSource getRequestLineItemFundingSourceById(Long rliFundingSourceId, boolean lock) throws InfrastructureException;
    public void makePersistent(RequestLineItemFundingSource rliFundingSource) throws InfrastructureException;
    public void makeTransient(RequestLineItemFundingSource rliFundingSource) throws InfrastructureException;
}
