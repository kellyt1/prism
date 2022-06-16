package us.mn.state.health.dao.materialsrequest;

import java.util.Collection;

import org.hibernate.HibernateException;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.RequestEvaluation;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateRequestEvaluationDAO implements RequestEvaluationDAO {
   
    public Collection findAllByRequestLineItemId(Long requestLineItemId) throws InfrastructureException {
        return null;
    }

    public RequestEvaluation getRequestEvaluationById(Long requestEvaluationId, boolean lock) throws InfrastructureException  {
        return null;
    }
    
    public void makePersistent(RequestEvaluation requestEvaluation) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(requestEvaluation);
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
    
    public void makeTransient(RequestEvaluation requestEvaluation) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(requestEvaluation);
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
}
