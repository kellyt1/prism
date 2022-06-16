package us.mn.state.health.model.inventory;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.RequestEvaluation;

/**
 * This class is the 'many' end of a one-to-many relationship between StockItemActionRequest & this class.  An
 * instance of this class may belong to only one StockItemActionRequest.  A StockItemActionRequest may have a collection of
 * StockItemActionRequestRequestEvaluations because some StockItemActionRequest may require approval from many parties. So this class represents
 * a single 'approver' and their decision, as it relates to the StockItemActionRequest.
 * A business rules engine (or something similar) should set the evaluatorRole group once a StockItemActionRequest is submitted. 
 * Then, any user who is a member of the group may be the 'evaluator'.  
 */
public class StockItemActionRequestEvaluation extends RequestEvaluation {
   
    private StockItemActionRequest stockItemActionRequest;           

    public void save() throws InfrastructureException {
        this.getDaoFactory().getRequestEvaluationDAO().makePersistent(this);        
    }
    
    public void delete() throws InfrastructureException {
        if(this.getRequestEvaluationId() != null) {
            this.getDaoFactory().getRequestEvaluationDAO().makeTransient(this);
        }        
    }

    public void setStockItemActionRequest(StockItemActionRequest stockItemActionRequest) {
        this.stockItemActionRequest = stockItemActionRequest;
    }


    public StockItemActionRequest getStockItemActionRequest() {
        return stockItemActionRequest;
    }
}
