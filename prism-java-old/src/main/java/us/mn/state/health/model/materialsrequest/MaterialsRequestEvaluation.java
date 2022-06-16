package us.mn.state.health.model.materialsrequest;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.RequestEvaluation;

/**
 * This class is the 'many' end of a one-to-many relationship between RequestLineItem & this class.  An
 * instance of this class may belong to only one RequestLineItem.  A RequestLineItem may have a collection of
 * RequestEvaluations because some RequestLineItems may require approval from many parties. So this class represents
 * a single 'approver' and their decision, as it relates to the RequestLineItem.
 * A business rules engine (or something similar) should set the evaluatorRole group once a RequestLineItem is submitted. 
 * Then, any user who is a member of the group may be the 'evaluator'.  
 */
public class MaterialsRequestEvaluation extends RequestEvaluation {
    private RequestLineItem requestLineItem;        //  a reference to the RLI it belongs to
  
    public void save() throws InfrastructureException {
        this.getDaoFactory().getRequestEvaluationDAO().makePersistent(this);
    }
    
    public void delete() throws InfrastructureException {
        if(this.getRequestEvaluationId() != null) {
          this.getDaoFactory().getRequestEvaluationDAO().makeTransient(this);
        }        
    }

    public void setRequestLineItem(RequestLineItem requestLineItem) {
        this.requestLineItem = requestLineItem;
    }

    public RequestLineItem getRequestLineItem() {
        return requestLineItem;
    }
}
