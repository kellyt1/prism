package us.mn.state.health.domain.repository.materialsrequest.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import us.mn.state.health.domain.repository.materialsrequest.RequestRepository;
import us.mn.state.health.domain.repository.materialsrequest.RequestSearchCriteria;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.util.PagedQueryResultEnhanced;
import us.mn.state.health.util.hibernate.HibernateQueryExecutor;
import us.mn.state.health.util.hibernate.queryparameters.BaseSQLQueryParameters;
import us.mn.state.health.util.hibernate.queryparameters.SQLQueryParametersImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HibernateRequestRepositoryImpl extends HibernateDaoSupport implements RequestRepository {
    private HibernateQueryExecutor queryExecutor;

    public HibernateRequestRepositoryImpl(HibernateTemplate template) {
        setHibernateTemplate(template);
    }

    public HibernateRequestRepositoryImpl(HibernateTemplate template,
                                          HibernateQueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
        setHibernateTemplate(template);
    }

    public PagedQueryResultEnhanced findAllRequests(RequestSearchCriteria criteria) {
        FindStockRequestsSLQBuilder findStockRequestsSLQBuilder = new FindStockRequestsSLQBuilder(criteria);
        SQLQueryParametersImpl queryParameters =
                new SQLQueryParametersImpl(findStockRequestsSLQBuilder.buildSqlQuery(), findStockRequestsSLQBuilder.buildCountSqlQuery(), new BaseSQLQueryParameters("r", Request.class));
        return queryExecutor.executeSQLQuery(criteria.getPagination(), queryParameters);
    }

    public List<RequestLineItem> findRequestLineItemsWaitingFormApprovalSubmittedBeforeDate(final Date requestedDate){
        List<RequestLineItem> requestLineItems = new ArrayList<RequestLineItem>();
        requestLineItems = getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String hql = "select distinct rli \n" +
                        "            from RequestLineItem as rli\n" +
                        "             join rli.requestEvaluations as reqEval\n" +
                        "            where rli.status.statusCode = 'WFA' \n" +
                        "             and reqEval.evaluationDecision.statusType.code = 'MRQ' \n" +
                        "             and reqEval.evaluationDecision.statusCode = 'WFA' " +
                        "and rli.request.dateRequested < :dateRequested";
                Query query = session.createQuery(hql);
                query.setParameter("dateRequested", requestedDate);
                return query.list();
            }
        });
        return requestLineItems;
    }

    public List<RequestLineItem> findRequestLineItemsWaitingForLevelOneApprovalSubmittedBeforeDate(final Date requestedDate){
        List<RequestLineItem> requestLineItems = new ArrayList<RequestLineItem>();
        requestLineItems = getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String hql = "select distinct rli \n" +
                        "            from RequestLineItem as rli\n" +
                        "             join rli.requestEvaluations as reqEval\n" +
                        "            where rli.status.statusCode = 'WFA' \n" +
                        "             and reqEval.evaluationDecision.statusType.code = 'MRQ' \n" +
                        "             and reqEval.evaluationDecision.statusCode = 'WFA' " +
                        "             and reqEval.approvalLevel = 1 " +
                        "and rli.request.dateRequested < :dateRequested";
                Query query = session.createQuery(hql);
                query.setParameter("dateRequested", requestedDate);
                return query.list();
            }
        });
        return requestLineItems;
    }

    public void makePersistent(Object o) {
        getHibernateTemplate().save(o);
    }


}
