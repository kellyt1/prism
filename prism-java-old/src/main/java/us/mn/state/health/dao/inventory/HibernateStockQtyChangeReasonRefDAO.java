package us.mn.state.health.dao.inventory;

import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.inventory.StockQtyChangeReasonRef;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateStockQtyChangeReasonRefDAO implements StockQtyChangeReasonRefDAO {

	public HibernateStockQtyChangeReasonRefDAO() {
        try{
            HibernateUtil.beginTransaction();
        }
        catch(InfrastructureException ie){}		
	}
    
     public StockQtyChangeReasonRef getStockQtyChangeReasonRefById(Long stockQtyChangeReasonRefId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        StockQtyChangeReasonRef stockQtyChangeReasonRef = null;
        try {
            if(lock) {
                stockQtyChangeReasonRef = (StockQtyChangeReasonRef)session.load(StockQtyChangeReasonRef.class, stockQtyChangeReasonRefId, LockMode.UPGRADE);
            } 
            else {
                stockQtyChangeReasonRef = (StockQtyChangeReasonRef)session.load(StockQtyChangeReasonRef.class, stockQtyChangeReasonRefId);
            }
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return stockQtyChangeReasonRef;
    }

	public Collection findAll() throws InfrastructureException {
		Collection stockQtyChangeReasonRefs;
		try {
			stockQtyChangeReasonRefs = HibernateUtil.getSession()
                                                    .createCriteria(us.mn.state.health.model.inventory.StockQtyChangeReasonRef.class)
                                                    .list();
		} 
        catch(HibernateException ex) {
			throw new InfrastructureException(ex);
		}
		return stockQtyChangeReasonRefs;
	}

    public void makePersistent(StockQtyChangeReasonRef stockQtyChangeReasonRef) throws InfrastructureException {
		try {
            HibernateUtil.getSession().saveOrUpdate(stockQtyChangeReasonRef);
		} 
        catch(HibernateException ex) {
			throw new InfrastructureException(ex);
		}
	}

	public void makeTransient(StockQtyChangeReasonRef stockQtyChangeReasonRef) throws InfrastructureException {
		try {
            HibernateUtil.getSession().delete(stockQtyChangeReasonRef);
		} 
        catch(HibernateException ex) {
			throw new InfrastructureException(ex);
		}
	}
}
