package us.mn.state.health.dao.materialsrequest;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.RequestEvaluation;

public interface RequestEvaluationDAO {
    public Collection findAllByRequestLineItemId(Long requestLineItemId) throws InfrastructureException;
    public RequestEvaluation getRequestEvaluationById(Long requestEvaluationId, boolean lock) throws InfrastructureException;
    public void makePersistent(RequestEvaluation requestEvaluation) throws InfrastructureException;
    public void makeTransient(RequestEvaluation requestEvaluation) throws InfrastructureException;
}
