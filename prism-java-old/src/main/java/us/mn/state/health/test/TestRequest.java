package us.mn.state.health.test;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import junit.framework.TestCase;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.StockItemLocation;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.persistence.HibernateUtil;

public class TestRequest extends TestCase {
    private String environment;

    public static void main(String[] args) throws InfrastructureException {
        TestRequest testRequest = new TestRequest();
        testRequest.setUp();
        testRequest.findMissingRequests1();
        testRequest.tearDown();
    }

    protected void setUp() {
        environment = Constants.DEVDB;
//        environment = Constants.TEST;
//        environment = Constants.PROD;
        System.setProperty(Constants.ENV_KEY, environment);
    }

    protected void findMissingRequests() throws InfrastructureException {
        String mrqQuery = "select distinct r.trackingNumber from Request r";
        String minMrqQuery = "select r.trackingNumber from Request r order by r.requestId asc";
        String maxMrqQuery = "select r.trackingNumber from Request r order by r.requestId desc";
        Collection mrqNumbers = new TreeSet();

        Collection fullMrqs = new HibernateDAO().executeQuery(mrqQuery);

        for (Iterator iterator = fullMrqs.iterator(); iterator.hasNext();) {
            String mrq = (String) iterator.next();
            String requestNumber = mrq.split("[\\p{Punct}]")[1];
            mrqNumbers.add(new Integer(requestNumber));
        }
        String min = ((String) new HibernateDAO().executeQuery(minMrqQuery).iterator().next()).split("[\\p{Punct}]")[1];
        String max = ((String) new HibernateDAO().executeQuery(maxMrqQuery).iterator().next()).split("[\\p{Punct}]")[1];
        int missingRequests = 0;
        for (int i = Integer.parseInt(min); i <= Integer.parseInt(max); i++) {
            Integer value = new Integer(i);
            if (!mrqNumbers.contains(value)) {
                missingRequests++;
                System.out.println("MRQ-" + value);
            }
        }

        System.out.println("");
        System.out.println("");
        System.out.println("We have " + missingRequests + " missing requests");
    }

    protected void findMissingRequests1() throws InfrastructureException {
        String mrqQuery = "select distinct r.trackingNumber from Request r";
        String minMrqQuery = "select r.trackingNumber from Request r order by r.requestId asc";
        String maxMrqQuery = "select r.trackingNumber from Request r order by r.requestId desc";
        Collection mrqNumbers = new TreeSet();

        Collection fullMrqs = new HibernateDAO().executeQuery(mrqQuery);

        Map missingTrackingNumbersPerDay = new TreeMap();

        for (Iterator iterator = fullMrqs.iterator(); iterator.hasNext();) {
            String mrq = (String) iterator.next();
            String requestNumber = mrq.split("[\\p{Punct}]")[1];
            mrqNumbers.add(new Integer(requestNumber));
        }
        String min = ((String) new HibernateDAO().executeQuery(minMrqQuery).iterator().next()).split("[\\p{Punct}]")[1];
        String max = ((String) new HibernateDAO().executeQuery(maxMrqQuery).iterator().next()).split("[\\p{Punct}]")[1];
        int missingRequests = 0;
        for (int i = Integer.parseInt(min); i <= Integer.parseInt(max); i++) {
            Integer value = new Integer(i);
            String fullValue = "MRQ-" + value;
            if (!mrqNumbers.contains(value)) {
                missingRequests++;
//                System.out.println(fullValue);
                String prevGoodTrackingNo = findPreviousRequestTrackingNumber(value, fullMrqs);
                Date date = findRequestDateUsingMRQ(prevGoodTrackingNo);
                Set missingRequestsPerDay = new TreeSet();
                Object setOfTheDay = missingTrackingNumbersPerDay.get(date);
                if (setOfTheDay != null) {
                    ((Set) setOfTheDay).add(fullValue);
                } else {
                    missingRequestsPerDay.add(fullValue);
                    missingTrackingNumbersPerDay.put(date, missingRequestsPerDay);
                }
            }
        }

        System.out.println("");
        System.out.println("");
        System.out.println("We have " + missingRequests + " missing requests");
        System.out.println("==========================================================================");
        for (Iterator iterator = missingTrackingNumbersPerDay.keySet().iterator(); iterator.hasNext();) {
            Date date = (Date) iterator.next();
            Set missingRequestsPerDay = (Set) missingTrackingNumbersPerDay.get(date);
            System.out.println(date + " : " + missingRequestsPerDay.size() + "  : " + missingRequestsPerDay);
        }

        System.out.println("==========================================================================");
        for (Iterator iterator = missingTrackingNumbersPerDay.keySet().iterator(); iterator.hasNext();) {
            Date date = (Date) iterator.next();
            Set missingRequestsPerDay = (Set) missingTrackingNumbersPerDay.get(date);
            System.out.println(date + ": This day has " + missingRequestsPerDay.size() + " missing requests ");
        }

        System.out.println("==========================================================================");
        for (Iterator iterator = missingTrackingNumbersPerDay.keySet().iterator(); iterator.hasNext();) {
            Date date = (Date) iterator.next();
            Set missingRequestsPerDay = (Set) missingTrackingNumbersPerDay.get(date);
            System.out.println(date + " \t " + missingRequestsPerDay.size());
        }

        System.out.println("**************************************************************************");
        System.out.println("**************************************************************************");
        System.out.println("**************************************************************************");

        Map trackingNumbersWithoutRequestLineItemsPerDay = new TreeMap();

        int emptyRequests = 0;
        Collection collection = new HibernateDAO().executeQuery("from Request r where r.requestLineItems.size=0");
        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            Request emptyRequest = (Request) iterator.next();
            if (emptyRequest.getRequestLineItems().size() == 0) {
                Date date = emptyRequest.getDateRequested();
                Object setOfTheDay = trackingNumbersWithoutRequestLineItemsPerDay.get(date);
                Set missingRequestsPerDay = new TreeSet();
                String trackingNumber = emptyRequest.getTrackingNumber();
                emptyRequests++;
                if (setOfTheDay != null) {
                    ((Set) setOfTheDay).add(trackingNumber);
                } else {
                    missingRequestsPerDay.add(trackingNumber);
                    trackingNumbersWithoutRequestLineItemsPerDay.put(date, missingRequestsPerDay);
                }
            }
        }
        System.out.println("We have " + emptyRequests + " empty requests");
        System.out.println("==========================================================================");
        for (Iterator iterator = trackingNumbersWithoutRequestLineItemsPerDay.keySet().iterator(); iterator.hasNext();)
        {
            Date date = (Date) iterator.next();
            Set missingRequestsPerDay = (Set) trackingNumbersWithoutRequestLineItemsPerDay.get(date);
            System.out.println(date + " : " + missingRequestsPerDay.size() + "  : " + missingRequestsPerDay);
        }

        System.out.println("==========================================================================");
        for (Iterator iterator = trackingNumbersWithoutRequestLineItemsPerDay.keySet().iterator(); iterator.hasNext();)
        {
            Date date = (Date) iterator.next();
            Set missingRequestsPerDay = (Set) trackingNumbersWithoutRequestLineItemsPerDay.get(date);
            System.out.println(date + ": This day has " + missingRequestsPerDay.size() + " empty requests ");
        }

        System.out.println("==========================================================================");
        for (Iterator iterator = trackingNumbersWithoutRequestLineItemsPerDay.keySet().iterator(); iterator.hasNext();)
        {
            Date date = (Date) iterator.next();
            Set missingRequestsPerDay = (Set) trackingNumbersWithoutRequestLineItemsPerDay.get(date);
            System.out.println(date + " \t " + missingRequestsPerDay.size());
        }


    }

    private String findPreviousRequestTrackingNumber(Integer missingRequestTrackingNumber,
                                                     Collection allRequestFullTrackingNumbers) {
        int tn = missingRequestTrackingNumber.intValue();
        String fullTrackingNumberPrefix = "MRQ-";
        String fullTrackingNumber = "";
        while (true) {
            fullTrackingNumber = fullTrackingNumberPrefix + tn;
            if (allRequestFullTrackingNumbers.contains(fullTrackingNumber)) {
                break;
            } else {
                tn--;
            }
        }
        return fullTrackingNumber;
    }

    private Date findRequestDateUsingMRQ(String mrq) throws InfrastructureException {
        String query = "select r.dateRequested from Request r where r.trackingNumber like '" + mrq + "'";
        Date result = (Date) new HibernateDAO().executeQuery(query).iterator().next();
        return result;
    }

    public void testCriteria() throws InfrastructureException {
        Session session = HibernateUtil.getSession();

        DetachedCriteria subQuery = DetachedCriteria.forClass(StockItem.class, "si");
        DetachedCriteria subQuery2 = DetachedCriteria.forClass(StockItemLocation.class, "loc");
//        subQuery2.
//                createAlias("sil.facility","f").
//                createAlias("sil.stockItem","stockItem").
//                add(Restrictions.eqProperty("stockItem.itemId","si.itemId")).
//                add(Restrictions.gt("f.facilityId",62025l)).setProjection(Property.forName("sil.locationId").count());
        subQuery2.add(
                Restrictions.sqlRestriction(
                        " {alias}.facility_id=62025"));

//        subQuery.
//                createCriteria("si.locations","loc").
//                createAlias("facility","fac").
//                add(Restrictions.eqProperty("si.itemId","item.itemId")).
//                add(Restrictions.gt("si.qtyOnHand", 0)).
//                add(Subqueries.gt(0,subQuery2)).
//                setProjection(Property.forName("si.itemId").count());
//
        subQuery.
                add(Restrictions.eqProperty("si.itemId", "item.itemId")).
                add(Restrictions.gt("si.qtyOnHand", 0)).
                add(Restrictions.sqlRestriction(
                        "(select count(distinct s.stock_item_id) from Stock_Item_Tbl s " +
                                "inner join Stock_Item_Location_Tbl location_ " +
                                "on s.stock_item_id = location_.stock_item_id " +
                                "where location_.facility_id = 62025 " +
                                "and s.stock_item_id={alias}.stock_item_id)>0"
                )).
                setProjection(Property.forName("si.itemId").count());

        Criteria criteria = session.createCriteria(Request.class).
                createAlias("requestLineItems", "rli", CriteriaSpecification.LEFT_JOIN).
                createAlias("rli.item", "item").
                createAlias("rli.status", "s").
                add(Restrictions.eq("s.statusCode", "WFD")).add(Subqueries.lt(0, subQuery))
                .setProjection(Projections.id());


        long i = System.currentTimeMillis();
        System.out.println("Total=" + new LinkedHashSet(criteria.list()).size());
        System.out.println("time=" + (System.currentTimeMillis() - i));
    }

    protected void tearDown() throws InfrastructureException {
//        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();
    }
}