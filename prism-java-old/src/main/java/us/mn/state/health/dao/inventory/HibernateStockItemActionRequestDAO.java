package us.mn.state.health.dao.inventory;

import java.util.Collection;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import org.hibernate.criterion.Example;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.inventory.ActionRequestType;
import us.mn.state.health.model.inventory.StockItemActionRequest;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateStockItemActionRequestDAO implements StockItemActionRequestDAO {
    private static Log log = LogFactory.getLog(HibernateStockItemActionRequestDAO.class);

    public HibernateStockItemActionRequestDAO() {
        try {
            //Interceptor interceptor = new LuceneInterceptor();
            //HibernateUtil.registerInterceptor(interceptor);
            HibernateUtil.beginTransaction();
        } 
        catch(InfrastructureException ie) {
            log.error(ie);
        }
    }

    public int countStockItemActionRequestWithStockItemIdByStatusCodes(Long stockItemId, String[] statusCodes) throws InfrastructureException {
        int result = 0;
        try {
            String queryString =
                    "select count(siar)  from StockItemActionRequest as siar where  siar.stockItem.itemId=" +
                    stockItemId +
                    "and (siar.status.statusCode = '";
            for(int i = 0; i < statusCodes.length; i++) {
                queryString += statusCodes[i] + "'";
                if (i < statusCodes.length - 1) {
                    queryString += " or siar.status.statusCode = '";
                }
            }
            queryString += ")";
            log.debug("findByStatusCodes Query: " + queryString);
            Query query = HibernateUtil.getSession().createQuery(queryString);
            result = new Integer(query.uniqueResult().toString()).intValue();
        } 
        catch(HibernateException ex) {
            log.error(ex);
            throw new InfrastructureException(ex);
        }
        return result;
    }

    public StockItemActionRequest findStockItemActionRequestById(Long stockItemActionRequestId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        StockItemActionRequest stockItemActionRequest = null;
        try {
            if(lock) {
                stockItemActionRequest = (StockItemActionRequest) session.load(StockItemActionRequest.class, stockItemActionRequestId, LockMode.UPGRADE);
            } 
            else {
                stockItemActionRequest = (StockItemActionRequest) session.load(StockItemActionRequest.class, stockItemActionRequestId);
            }
        }
        catch(HibernateException ex) {
            log.error(ex);
            throw new InfrastructureException(ex);
        }
        return stockItemActionRequest;
    }


    public Collection findByStatusCodes(String[] statusCodes) throws InfrastructureException {
        Collection stockItemActionRequests;
        try {
            String queryString = "from StockItemActionRequest as siar where siar.status.statusCode = '";
            for(int i = 0; i < statusCodes.length; i++) {
                queryString += statusCodes[i] + "'";
                if (i < statusCodes.length - 1) {
                    queryString += " or siar.status.statusCode = '";
                }
            }
            log.debug("findByStatusCodes Query: " + queryString);
            Query query = HibernateUtil.getSession().createQuery(queryString);
            stockItemActionRequests = query.list();
        } 
        catch(HibernateException ex) {
            log.error(ex);
            throw new InfrastructureException(ex);
        }
        return stockItemActionRequests;
    }

    public Collection findAll() throws InfrastructureException {
        Collection stockItemActionRequests;
        try {
            Query query = HibernateUtil.getSession().getNamedQuery("findAllStockItemActionRequests");
            stockItemActionRequests = query.list();
        } 
        catch(HibernateException ex) {
            log.error(ex);
            throw new InfrastructureException(ex);
        }
        return stockItemActionRequests;
    }

    public Collection findByExample(StockItemActionRequest stockItemActionRequest) throws InfrastructureException {
        Collection stockItemActionRequests;
        try {
            Criteria crit = HibernateUtil.getSession().createCriteria(StockItemActionRequest.class);
            stockItemActionRequests = crit.add(Example.create(stockItemActionRequest)).list();
        } 
        catch(HibernateException ex) {
            log.error(ex);
            throw new InfrastructureException(ex);
        }
        return stockItemActionRequests;
    }

    public Collection findSIARByActionRequestType(ActionRequestType actionRequestType) throws InfrastructureException {
        Collection stockItemActionRequests;
        try {
            stockItemActionRequests = HibernateUtil.getSession()
//                                                   .getNamedQuery("findSIARByActionRequestTypeCode")
                                                   .getNamedQuery("findSIARByActionRequestType")
                                                   .setParameter("typeId", actionRequestType.getActionRequestTypeId())
                                                   .list();
        } 
        catch(HibernateException ex) {
            log.error(ex);
            throw new InfrastructureException(ex);
        }
        return stockItemActionRequests;
    }

    public Collection findSIARByActionRequestTypeCodeAndStatusCode(String actionRequestTypeCode,
                                                                   String statusCode) throws InfrastructureException {
        Collection stockItemActionRequests;
        try {
            stockItemActionRequests = HibernateUtil.getSession()
                                                   .getNamedQuery("findSIARByActionRequestTypeCodeAndStatusCode")
                                                   .setString("actionRequestTypeCode", actionRequestTypeCode)
                                                   .setString("statusCode", statusCode)
                                                   .list();
        } 
        catch(HibernateException ex) {
            log.error(ex);
            throw new InfrastructureException(ex);
        }
        return stockItemActionRequests;
    }

    public Collection findSIARByActionRequestTypeCode(String actionRequestTypeCode) throws InfrastructureException {
        Collection stockItemActionRequests;
        try {
            stockItemActionRequests = HibernateUtil.getSession()
                                                   .getNamedQuery("findSIARByActionRequestTypeCode")
                                                   .setString("typeCode", actionRequestTypeCode)
                                                   .list();
        } 
        catch(HibernateException ex) {
            log.error(ex);
            throw new InfrastructureException(ex);
        }
        return stockItemActionRequests;
    }
    

    public Collection findSIARByPotentialItemId(long itemId) throws InfrastructureException {
        ArrayList stockItemActionRequests = new ArrayList();
        String sqlString =
 "(select {siar.*} from STOCK_ITEM_ACTION_REQUEST_TBL siar where potential_item_id = :itemId)";
        SQLQuery sqlQuery = HibernateUtil.getSession().createSQLQuery(sqlString);
        stockItemActionRequests = (ArrayList) sqlQuery.addEntity("siar", StockItemActionRequest.class).
        setLong("itemId",itemId).setCacheable(true).list();
        return stockItemActionRequests;
    }


    public Collection findByEvaluatorAndStatusCode(Person evaluator, String statusCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection stockItemActionRequests;
        try {
            stockItemActionRequests = session.getNamedQuery("findSIARByEvaluatorAndStatusCode")
                                             .setString("statusCode", statusCode)
                                             .setEntity("evaluator", evaluator)
                                             .list();
        } 
        catch(HibernateException ex) {
            log.error(ex);
            throw new InfrastructureException(ex);
        }
        return stockItemActionRequests;
    }

    public void makePersistent(StockItemActionRequest stockItemActionRequest) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(stockItemActionRequest);
        } 
        catch(HibernateException ex) {  
            log.error(ex);
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(StockItemActionRequest stockItemActionRequest) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(stockItemActionRequest);
        } 
        catch(HibernateException ex) {
            log.error(ex);
            throw new InfrastructureException(ex);
        }
    }
}
