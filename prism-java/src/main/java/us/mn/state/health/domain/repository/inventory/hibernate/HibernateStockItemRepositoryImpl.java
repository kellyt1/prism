package us.mn.state.health.domain.repository.inventory.hibernate;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.ge;
import static org.hibernate.criterion.Restrictions.le;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import us.mn.state.health.domain.repository.inventory.StockItemRepository;
import us.mn.state.health.domain.repository.inventory.StockItemSearchCriteria;
import us.mn.state.health.domain.repository.util.HibernateDomainRepositoryImpl;
import us.mn.state.health.model.inventory.*;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.util.hibernate.CriteriaBuilder;
import us.mn.state.health.util.hibernate.HibernateQueryExecutor;
import us.mn.state.health.util.hibernate.queryparameters.BaseQueryParameters;

public class HibernateStockItemRepositoryImpl extends HibernateDomainRepositoryImpl implements StockItemRepository {
    private HibernateQueryExecutor queryExecutor;

    public HibernateStockItemRepositoryImpl(HibernateTemplate template,
                                            HibernateQueryExecutor hibernateQueryExecutor) {
        setHibernateTemplate(template);
        this.queryExecutor = hibernateQueryExecutor;
    }

    public List findStockItemsBelowROP() {
        return null;
    }

    public StockItem getById(Long stockItemId) {
        Object o = get(StockItem.class, stockItemId);
        return o != null ? (StockItem) o : null;
    }

    public List findStockItemsBelowROP(List categoryIds, List facilityIds, boolean includeStockItemsWithoutFacilities) {
        List result;
        result = getHibernateTemplate()
                .findByNamedQueryAndNamedParam("findStockItemsBelowROP", new String[]{"categoryIds", "facilityIds", "includeStockItemsWithoutFacilities"}
                        , new Object[]{categoryIds, facilityIds, includeStockItemsWithoutFacilities ? new Integer(1) : new Integer(0)});
        return result;
    }

    public List findAllStockItems(StockItemSearchCriteria stockItemSearchCriteria) {
        return findAllStockItems(stockItemSearchCriteria, false);
    }

    public List<StockItem> findAllStockItems(StockItemSearchCriteria stockItemSearchCriteria, boolean lockResults) {
        FindStockItemsCriteriaBuilder builder = new FindStockItemsCriteriaBuilder(stockItemSearchCriteria);
        BaseQueryParameters baseQueryParameters;
        if (lockResults) {
            baseQueryParameters = new BaseQueryParameters(LockMode.UPGRADE);
        } else {
            baseQueryParameters = new BaseQueryParameters(LockMode.READ);
        }
        List<StockItem> queryResult;
        queryResult = queryExecutor.executeCriteriaQuery(StockItem.class, builder, baseQueryParameters);
        return queryResult;
    }

    public List<RequestLineItem> findOnOrderRequestLineItems(final Long stockItemId) {
        return getHibernateTemplate().findByNamedQueryAndNamedParam("findOnOrderRequestLineItems", "stockItemId", stockItemId);
    }

    public Integer findNextICNBR() {
        return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Integer nextNbr = null;

                Object o = session.getNamedQuery("findNextICNBR")
                        .uniqueResult();
                if (o != null) {
                    nextNbr = new Integer(o.toString());
                }
                if (nextNbr == null) {
                    nextNbr = new Integer(1);   // default to 1
                }
                return nextNbr;
            }
        });
    }

    public List<StockItemHistory> getStockItemHistory(final StockItem stockItem, final int noOfRecords) {
        if (stockItem == null) throw new IllegalArgumentException("The stock item is null!");
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(StockItemHistory.class).
                        addOrder(Order.desc("stockItemHistoryId")).
                        setMaxResults(noOfRecords).add(Expression.eq("stockItem", stockItem)).list();
            }
        });
    }


    public void makePersistent(StockItem stockItem) {
        getHibernateTemplate().saveOrUpdate(stockItem);
    }

    public List<StockQtyAdjustmentHistory> getStockQtyAdjustmentHistory(final StockItem stockItem, final int noOfRecords) {
        if (stockItem == null) throw new IllegalArgumentException("The stock item is null!");
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(StockQtyAdjustmentHistory.class).
                        addOrder(Order.desc("id")).
                        setMaxResults(noOfRecords).add(Expression.eq("stockItem", stockItem)).list();
            }
        });
    }

    public List<StockItem> getStockItemsHitReviewDateBetweenDates(final Date startDate, final Date endDate) {
        if (startDate == null || endDate == null)
            throw new IllegalArgumentException("The start date and the end date parameters can't be null!");
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(StockItem.class)
                        .add(ge("reviewDate", DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH)))
                        .add(le("reviewDate", DateUtils.truncate(endDate, Calendar.DAY_OF_MONTH))).list();
            }
        });
    }

    public List<StockItemLocationHistory> getStockItemLocationHistory(final StockItem stockItem, final int noOfRecords) {
        if (stockItem == null) throw new IllegalArgumentException("The stock item is null!");
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(StockItemLocationHistory.class).
                        addOrder(Order.desc("locationHistoryId")).
                        setMaxResults(noOfRecords).add(Expression.eq("stockItem", stockItem)).list();
            }
        });
    }

    public List<ItemVendorLinkHistory> getItemVendorLinkHistory(final StockItem stockItem, final int noOfRecords) {
        if (stockItem == null) throw new IllegalArgumentException("The stock item is null!");
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(ItemVendorLinkHistory.class).
                        addOrder(Order.desc("itemVendorLinkHistoryId")).
                        setMaxResults(noOfRecords).add(Expression.eq("stockItem", stockItem)).list();
            }
        });
    }


    class FindStockItemsCriteriaBuilder implements CriteriaBuilder {
        StockItemSearchCriteria searchCriteria;

        public FindStockItemsCriteriaBuilder(StockItemSearchCriteria stockItemSearchCriteria) {
            this.searchCriteria = stockItemSearchCriteria;
        }

        public void addCriteria(Criteria criteria) throws HibernateException {
            if (searchCriteria.isStatusIdSpecified()) {
                criteria.add(eq("status.statusId", new Long(searchCriteria.getStatusId())));
            }

            if (searchCriteria.isHoldUntilDateSpecified()) {
                criteria.add(le("holdUntilDate", searchCriteria.getHoldUntilDate()));
            }

            criteria.setFetchMode("primaryContact", FetchMode.JOIN);
            criteria.setFetchMode("secondaryContact", FetchMode.JOIN);

        }
    }
}
