package us.mn.state.health.dao.materialsrequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateRequestLineItemDAO implements RequestLineItemDAO {

    public HibernateRequestLineItemDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch (InfrastructureException ie) {
            ie.printStackTrace();
        }
    }

    public Collection findAll() throws InfrastructureException {
        Collection requestLineItems;
        try {
            requestLineItems = HibernateUtil.getSession().createCriteria(RequestLineItem.class).list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return requestLineItems;
    }

    public Collection findAllRequestLineItemsWithStatusCode(String statusCode) throws InfrastructureException {
        try {
            Query query = HibernateUtil.getSession().getNamedQuery("findAllRequestLineItemsWithStatusCode");
            query.setString("statusCode", statusCode);
            return query.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    /**
     * TODO: 10/05/2005 I put in the  String code = requestLineItem.getStatus().getStatusCode(); to try to make hibernate eager load
     * and throw an exception if the object isn't found for now, because of problems with purchasing module - indexes
     * out of synch with databasefa... Remove later.
     *
     * @param lock
     * @param requestLineItemId
     * @return
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public RequestLineItem getRequestLineItemById(Long requestLineItemId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        RequestLineItem requestLineItem = null;
        try {
            if (lock) {
                requestLineItem = (RequestLineItem) session.load(RequestLineItem.class, requestLineItemId, LockMode.UPGRADE);
            } else {
                requestLineItem = (RequestLineItem) session.load(RequestLineItem.class, requestLineItemId);
            }
            String code = requestLineItem.getStatus().getStatusCode();  //just a band-aid... see above
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return requestLineItem;
    }

    public RequestLineItem getRequestLineItemById(Long requestLineItemId) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Object rli = session.get(RequestLineItem.class, requestLineItemId);
        return rli == null ? null : (RequestLineItem) rli;
    }


    /**
     * This method returns all the request line items that were filled
     *
     * @return a list of us.mn.state.health.view.materialsrequest.reports.StockItemOutTransaction's
     */
    public Collection findStockItemOutTransactionsWithFS(String orgBdgtCode, String[] fiscalYear, Date startDate, Date endDate, String categoryId, String icnbr) throws InfrastructureException {
//        Calendar calendar1 = new GregorianCalendar(new Integer(fiscalYear).intValue(),0,1);
//        Calendar calendar2 = new GregorianCalendar(new Integer(fiscalYear).intValue(),11,31);
//        Date beginFY = new Date(calendar1.getTimeInMillis());
//        Date endFY = new Date(calendar2.getTimeInMillis());
        Date[] beginFY = new Date[10];
        Date[] endFY = new Date[10];
        Calendar lowcalendar = new GregorianCalendar(new Integer(1900).intValue(), 11, 31);
        Date lowdate = new Date(lowcalendar.getTimeInMillis());

        for (int i = 0; i < fiscalYear.length; i++) {
            Calendar calendar1 = new GregorianCalendar(new Integer(fiscalYear[i]).intValue(), 0, 1);
            Calendar calendar2 = new GregorianCalendar(new Integer(fiscalYear[i]).intValue(), 11, 31);
            beginFY[i] = new Date(calendar1.getTimeInMillis());
            endFY[i] = new Date(calendar2.getTimeInMillis());

        }

        Session session = HibernateUtil.getSession();
        Query namedQuery = session.getNamedQuery("findStockItemOutTransactionsWithFS");
        String catId = StringUtils.nullOrBlank(categoryId) ? "%%" : categoryId;
        String catCode = "%%";
        String _icnbr = "%%";
        if (!StringUtils.nullOrBlank(icnbr)) {
            String[] array = icnbr.split("-");
            if (array.length > 1) {
                catCode = array[0].trim();
                try {
                    _icnbr = Integer.valueOf(array[1].trim()).toString();
                } catch (Exception e) {
                }
            } else {
                return new ArrayList();
            }
        }

        namedQuery.setParameter("orgBdgt", orgBdgtCode + "%")
//                .setParameter("beginFY",beginFY)
//                .setParameter("endFY",endFY)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("categoryId", catId)
                .setParameter("categoryCode", catCode)
                .setParameter("icnbr", _icnbr);
        for (int i = 0; i < 10; i++) {

            if (beginFY[i] != null) namedQuery.setParameter("beginFY" + (i + 1), beginFY[i]);
            else namedQuery.setParameter("beginFY" + (i + 1), lowdate);
            if (endFY[i] != null) namedQuery.setParameter("endFY" + (i + 1), endFY[i]);
            else namedQuery.setParameter("endFY" + (i + 1), lowdate);
        }

        Collection results = new ArrayList();
        results.addAll(namedQuery.list());
        return results;
    }

    public Collection findStockItemOutTransactionsWithoutFS(Date startDate, Date endDate, String categoryId, String icnbr) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Query namedQuery = session.getNamedQuery("findStockItemOutTransactionsWithoutFS");
        String catId = StringUtils.nullOrBlank(categoryId) ? "%%" : categoryId;
        String catCode = "%%";
        String _icnbr = "%%";
        if (!StringUtils.nullOrBlank(icnbr)) {
            String[] array = icnbr.split("-");
            if (array.length > 1) {
                catCode = array[0].trim();
                try {
                    _icnbr = Integer.valueOf(array[1].trim()).toString();
                } catch (Exception e) {
                }
            } else {
                return new ArrayList();
            }
        }

        namedQuery.setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("categoryId", catId)
                .setParameter("categoryCode", catCode)
                .setParameter("icnbr", _icnbr);
        Collection results = new ArrayList();
        results.addAll(namedQuery.list());
        return results;
    }


    public Collection findStockItemInTransactions(String orgBdgtCode, Date startDate, Date endDate, String categoryId, String icnbr) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Query namedQuery = session.getNamedQuery("findStockItemInTransactions");
        String catId = StringUtils.nullOrBlank(categoryId) ? "%%" : categoryId;
        String catCode = "%%";
        String _icnbr = "%%";
        if (!StringUtils.nullOrBlank(icnbr)) {
            String[] array = icnbr.split("-");
            if (array.length > 1) {
                catCode = array[0].trim();
                try {
                    _icnbr = Integer.valueOf(array[1].trim()).toString();
                } catch (Exception e) {
                }
            } else {
                return new ArrayList();
            }
        }

        namedQuery.setParameter("orgBdgt", orgBdgtCode + "%")
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("categoryId", catId)
                .setParameter("categoryCode", catCode)
                .setParameter("icnbr", _icnbr);
        Collection results = new ArrayList();
        results.addAll(namedQuery.list());
        return results;
    }

    public Collection findOrderedPurchaseItemTransactions(String orgBdgtCode, String[] fiscalYear, Date startDate, Date endDate, String categoryId) throws InfrastructureException {

//        String outFYSELECT = makeFiscalYearSelect(fiscalYear);
//       System.out.println("Fiscal Year " +  outFYSELECT);
        Date[] beginFY = new Date[10];
        Date[] endFY = new Date[10];
        Calendar lowcalendar = new GregorianCalendar(new Integer(1900).intValue(), 11, 31);
        Date lowdate = new Date(lowcalendar.getTimeInMillis());

        for (int i = 0; i < fiscalYear.length; i++) {
            Calendar calendar1 = new GregorianCalendar(new Integer(fiscalYear[i]).intValue(), 0, 1);
            Calendar calendar2 = new GregorianCalendar(new Integer(fiscalYear[i]).intValue(), 11, 31);
            beginFY[i] = new Date(calendar1.getTimeInMillis());
            endFY[i] = new Date(calendar2.getTimeInMillis());

        }

        Session session = HibernateUtil.getSession();
        Query namedQuery = session.getNamedQuery("findOrderedPurchaseItemTransactions");
        String catId = StringUtils.nullOrBlank(categoryId) ? "%%" : categoryId;


        namedQuery.setParameter("orgBdgt", orgBdgtCode + "%")
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("categoryId", catId);
        for (int i = 0; i < 10; i++) {

            if (beginFY[i] != null) namedQuery.setParameter("beginFY" + (i + 1), beginFY[i]);
            else namedQuery.setParameter("beginFY" + (i + 1), lowdate);
            if (endFY[i] != null) namedQuery.setParameter("endFY" + (i + 1), endFY[i]);
            else namedQuery.setParameter("endFY" + (i + 1), lowdate);
        }

        Collection results = new ArrayList();

        results.addAll(namedQuery.list());
        return results;
    }
//    private String makeFiscalYearSelect(String[] fiscalYear)  {
//        String outString = "";
//        String sep = "";
//        String scFiscalYear = "";
//        int x = 0;
//
//        for (int i=0;i<fiscalYear.length;i++) {
//            scFiscalYear = fiscalYear[i];
//
//            String outPhrase = sep + " fs.orgBudget.endDate > '01-Jan-" + scFiscalYear + "'";
//                   outPhrase +=  " and fs.orgBudget.endDate < '31-Dec-" + scFiscalYear + "'";
//            //fs.orgBudget.endDate > :beginFY and fs.orgBudget.endDate < :endFY
//            sep = " or ";
//            if (outString.equals("")) outString = "(";
//            outString += outPhrase;
//         }
//        if (outString.length() > 0) outString += ")";
//
//        return outString;
//    }

    public void makePersistent(RequestLineItem requestLineItem) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(requestLineItem);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(RequestLineItem requestLineItem) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(requestLineItem);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
}