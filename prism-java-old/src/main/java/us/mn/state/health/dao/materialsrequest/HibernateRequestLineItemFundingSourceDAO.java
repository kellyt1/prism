package us.mn.state.health.dao.materialsrequest;

import java.util.Collection;
import java.util.HashSet;

import org.hibernate.HibernateException;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateRequestLineItemFundingSourceDAO extends HibernateDAO implements RequestLineItemFundingSourceDAO {
    
    public Collection findAll() throws InfrastructureException {
        return new HashSet();
    }
    
    public Collection findAllByRequestLineItemId(Long requestLineItemId) throws InfrastructureException {
        return null;
    }

    public RequestLineItemFundingSource getRequestLineItemFundingSourceById(Long rliFundingSourceId, boolean lock) throws InfrastructureException  {
        return (RequestLineItemFundingSource)super.loadById(rliFundingSourceId, RequestLineItemFundingSource.class, lock);
    }
    
    public void makePersistent(RequestLineItemFundingSource rliFundingSource) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(rliFundingSource);
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
    
    public void makeTransient(RequestLineItemFundingSource rliFundingSource) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(rliFundingSource);
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
}
