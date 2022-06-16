package us.mn.state.health.model.util.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.io.IOUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.lucene.LuceneUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.dao.purchasing.PurchasingOrderDAO;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.model.util.configuration.Config;
import us.mn.state.health.persistence.HibernateUtil;

public class OrderIndex extends EntityIndex {
    private static Log log = LogFactory.getLog(OrderIndex.class);
    public static final String CONCATENATED_CONTENT = "concatenatedContent";
    public static final String ORDER_ID = "orderId";
    public static final String ORDER_LINE_ITEM_IDs = "orderLineItemIds";
    public static final String REQUEST_LINE_ITEM_IDs = "requestLineItemIds";
    public static final String PURCHASER_PERSON_ID = "purchaserPersonId";
    public static final String SHIPPING_ADDRESS_ID = "shippingLocationId";
    public static final String VENDOR_ID = "vendorId";
    public static final String VENDOR_NAME = "vendorName";
    public static final String VENDOR_INSTRUCTIONS = "vendorInstructions";
    public static final String VENDOR_CONTRACT_ID = "vendorContractId";
    public static final String PO_NUMBER = "purchaseOrderNumber";
    public static final String PO_NUMBER_TYPE = "purchaseOrderNumberType";
    public static final String DATE_CREATED = "dateCreated";
    public static final String SUSPENSE_DATE = "suspenseDate";

    protected static final Analyzer analyzer = new StandardAnalyzer();
    protected static IndexWriter orderIndexWriter;
    protected final static Object orderMutex = new Object();
    protected static DAOFactory daoFactory = new HibernateDAOFactory(); //default to Hibernate

    private static File indexDirectory;
    private static File tempIndexDirectory;
    private static int optimizeCount = 0;

    static {
        try {
            tempIndexDirectory = new File(Config.getConfig(Constants.MATERIALS_MANAGMENT_CODE, Constants.TEMP_ORDER_INDEX_PATH_CODE));
            indexDirectory = new File(Config.getConfig(Constants.MATERIALS_MANAGMENT_CODE, Constants.ORDER_INDEX_PATH_CODE));
        }
        catch (InterruptedException e) {
            log.error("Error in OrderIndex in the static block ", e);
        }
    }

    public static void createIndex(boolean newIndex) {
        synchronized (orderMutex) {
            File index = getCurrentIndexDirectory();
            if (newIndex) {
                boolean delete = IOUtils.deleteDir(index);
                log.debug("Deleted OrderIndex: " + delete);
            }
            create(index);
        }
    }

    private static void create(File index) {
        try {
            if (!index.exists()) {
                index.mkdirs();
                orderIndexWriter = new IndexWriter(index, analyzer, true);
                orderIndexWriter.close();

                PurchasingOrderDAO purchasingOrderDAO = daoFactory.getPurchasingOrderDAO();
                Collection orders = purchasingOrderDAO.findAllOrders();
                OrderIndex orderIndexer = new OrderIndex();
                for (Object order1 : orders) {
                    Order order = (Order) order1;
//                    try {
//                        orderIndexer.add(order);
//                    } catch (InfrastructureException e) {
//                        log.error("Error in OrderIndex.create() for the Order:" + order.getOrderId(), e);
//                    }
                }
            }
        }
        catch (IOException e) {
            log.error("Error in OrderIndex.create()", e);
        }
        catch (InfrastructureException e) {
            log.error("Error in OrderIndex.create()", e);
        }
    }

    public static void createIndexAtRuntime() throws ParseException, IOException, InfrastructureException {


        ArrayList orderIds = new ArrayList();
        OrderIndex index = new OrderIndex();
//        1. get database ids
        Collection orders = daoFactory.getPurchasingOrderDAO().findAllOrders();
        for (Iterator iterator = orders.iterator(); iterator.hasNext();) {
            Order order = (Order) iterator.next();
            orderIds.add(order.getOrderId());
        }

//        2. commit transaction and clear session
        try {
            HibernateUtil.getSession().clear();
            HibernateUtil.commitTransaction();
        } catch (InfrastructureException e) {
            e.printStackTrace();
        }

        PurchasingOrderDAO purchasingOrderDAO = daoFactory.getPurchasingOrderDAO();
//        3. iterate and update the indexes
        updateIndex(orderIds, index, purchasingOrderDAO);

//        4. get the index ids
        final IndexReader reader = IndexReader.open(indexDirectory);
        final IndexSearcher searcher = new IndexSearcher(reader);

        String queryString = DATE_CREATED + ":2*";
        Query query;
//        query = QueryParser.parse(queryString, CONCATENATED_CONTENT, analyzer);
        query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(queryString);

//        query = QueryParser.parse(queryString, CONCATENATED_CONTENT, analyzer);
        final Hits hits = searcher.search(query);
        for (int i = 0; i < hits.length(); i++) {
            final Document doc = hits.doc(i);
            Long id = new Long(doc.get(ORDER_ID));
            orderIds.add(id);
        }
        searcher.close();
        reader.close();

        //delete the unexisting records
        deleteFromIndex(orderIds, index, purchasingOrderDAO);
    }

    private static void updateIndex(ArrayList orderIDs, OrderIndex index, PurchasingOrderDAO purchasingOrderDAO) {
        for (Iterator iterator = orderIDs.iterator(); iterator.hasNext();) {
            Long id = (Long) iterator.next();
            Order order = null;
            try {
                order = (Order) purchasingOrderDAO.getOrderById(id, false);
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
            try {
                if (order != null) {
//                    index.dropAndAdd(order);
                } else {
//                    index.drop(order);
                }
                HibernateUtil.getSession().clear();
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
        }
    }

    private static void deleteFromIndex(ArrayList orderIDs, OrderIndex index, PurchasingOrderDAO purchasingOrderDAO) {
        for (Iterator iterator = orderIDs.iterator(); iterator.hasNext();) {
            Long id = (Long) iterator.next();
            Order order = null;
            try {
                order = purchasingOrderDAO.getOrderById(id, false);
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
            try {
                if (order == null) {
                    order = new Order();
                    order.setOrderId(id);
//                    index.drop(order);
                }
                HibernateUtil.getSession().clear();
            } catch (InfrastructureException e) {
                log.error("Order:" + id + " was not found on the DB");
            }
        }
    }

    public void dropAndAdd(Searchable entity) throws InfrastructureException {
        synchronized (orderMutex) {
            drop(entity);
            add(entity);
        }
    }

    /**
     * Add an Order to the index
     *
     * @param entity
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void add(Searchable entity) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        create(file);
        final Document document = new Document();
        Order order = (Order) entity;
        try {
            StringBuffer concatenatedFields = new StringBuffer();

            document.add((Fieldable) new Field(ORDER_ID, order.getOrderId().toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
            concatenatedFields.append(order.getOrderId().toString()).append(" ");

            //01/22/2007
            if (order.getPurchaser() != null) {
                Person purchaser = order.getPurchaser();
                purchaser = daoFactory.getPersonDAO().getPersonById(purchaser.getPersonId(), false);
                document.add((Fieldable) new Field(PURCHASER_PERSON_ID, purchaser.getPersonId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
                concatenatedFields.append(purchaser.getLastAndFirstName()).append(" ");
            }
            if (order.getShipToAddress() != null) {
                document.add((Fieldable) new Field(SHIPPING_ADDRESS_ID, order.getShipToAddress().getMailingAddressId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
                // concatenatedFields.append(order.getShipToAddress().getCityAndAddress()).append(" ");
            }
            if (order.getVendor() != null) {
                Vendor vendor = order.getVendor();
                vendor = daoFactory.getVendorDAO().getVendorById(vendor.getVendorId(), false);
                document.add((Fieldable) new Field(VENDOR_ID, vendor.getVendorId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
                document.add((Fieldable) new Field(VENDOR_NAME, vendor.getExternalOrgDetail().getOrgName(), Field.Store.YES, Field.Index.TOKENIZED));
                concatenatedFields.append(vendor.getExternalOrgDetail().getOrgName()).append(" ");
            }

            if (order.getVendorInstructions() != null) {
                document.add((Fieldable) new Field(VENDOR_INSTRUCTIONS, order.getVendorInstructions(), Field.Store.YES, Field.Index.TOKENIZED));
            }


            if (order.getVendorContract() != null) {
                document.add((Fieldable) new Field(VENDOR_CONTRACT_ID, order.getVendorContract().getVendorContractId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            }

            if (order.getPurchaseOrderNumber() != null) {
                document.add((Fieldable) new Field(PO_NUMBER, order.getPurchaseOrderNumber(), Field.Store.YES, Field.Index.TOKENIZED));
                concatenatedFields.append(order.getPurchaseOrderNumber()).append(" ");
            }

            if (order.getPurchaseOrderNumberType() != null) {
                document.add((Fieldable) new Field(PO_NUMBER_TYPE, order.getPurchaseOrderNumberType(), Field.Store.YES, Field.Index.TOKENIZED));
                concatenatedFields.append(order.getPurchaseOrderNumberType()).append(" ");
            }

            if (order.getInsertionDate() != null) {
                //document.add(Field.Text(DATE_CREATED, order.getInsertionDate().toString()));
                document.add((Fieldable) new Field(DATE_CREATED, DateUtils.toString(order.getInsertionDate(), DateUtils.LUCENE_DATE_FORMAT), Field.Store.YES, Field.Index.TOKENIZED));
            }

            if (order.getSuspenseDate() != null) {
                //document.add(Field.Text(SUSPENSE_DATE, order.getSuspenseDate().toString()));
                document.add((Fieldable) new Field(SUSPENSE_DATE, DateUtils.toString(order.getSuspenseDate(), DateUtils.LUCENE_DATE_FORMAT), Field.Store.YES, Field.Index.TOKENIZED));
            }

            StringBuffer oliIds = new StringBuffer();
            Collection olis = order.getOrderLineItems();
            for (Iterator iterator = olis.iterator(); iterator.hasNext();) {
                OrderLineItem orderLineItem = (OrderLineItem) iterator.next();
                String oliIdString = orderLineItem.getOrderLineItemId().toString();
                oliIds.append(oliIdString).append(" ");
            }
            document.add((Fieldable) new Field(ORDER_LINE_ITEM_IDs, oliIds.toString(), Field.Store.YES, Field.Index.TOKENIZED));

            StringBuffer rliIds = new StringBuffer();
            try {
                //TODO fix this-find where we don't initialize the request line items 03/30/2007

                Collection rlis = order.getRequestLineItems();
                for (Iterator iterator = rlis.iterator(); iterator.hasNext();) {
                    RequestLineItem requestLineItem = (RequestLineItem) iterator.next();
                    String rliIdString = requestLineItem.getRequestLineItemId().toString();
                    rliIds.append(rliIdString).append(" ");
                }
            } catch (Exception e) {
                log.error("Failed to add the RLI ID's to the order index", e);
            }
            document.add((Fieldable) new Field(REQUEST_LINE_ITEM_IDs, rliIds.toString(), Field.Store.YES, Field.Index.TOKENIZED));

            document.add((Fieldable) new Field(CONCATENATED_CONTENT, concatenatedFields.toString(), Field.Store.YES, Field.Index.TOKENIZED));
        }
        catch (Exception e) {
            log.error("Error in OrderIndex.add(), order#:" + order.getOrderId(), e);
            throw new InfrastructureException("Exception in OrderIndex.add(): ", e);
        }

        try {
            synchronized (orderMutex) {
                int i = optimizeCount % optimizeValue;
                if (i != 0) {
                    addHandler(file, document, analyzer, log, false);
                    optimizeCount++;
                } else {
                    addHandler(file, document, analyzer, log, true);
                    optimizeCount = 1;
                }
//                orderIndexWriter = new IndexWriter(file, analyzer, false);
//                try {
//                    orderIndexWriter.addDocument(document);
//                    orderIndexWriter.optimize();
//                } finally {
//                    orderIndexWriter.close();
//                }
            }
        }
        catch (IOException e) {
            log.error("Error in OrderIndex.add2(), order#:" + order.getOrderId(), e);
            throw new InfrastructureException(e.getMessage(), e);
        }
    }

    public void drop(Searchable entity) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        create(file);
        Order order = (Order) entity;
        try {
            synchronized (orderMutex) {
                dropHandler(file, ORDER_ID, order.getOrderId().toString(), log);
//                final IndexReader reader = IndexReader.open(file);
//                try {
//                    Term term = new Term(OrderIndex.ORDER_ID, order.getOrderId().toString());
//                    reader.deleteDocuments(term);
//                } finally {
//                    reader.close();
//                }
            }
        }
        catch (IOException e) {
            log.error("Error in OrderIndex.drop(), order#:" + order.getOrderId(), e);
            throw new InfrastructureException(e.getMessage(), e);
        }
    }

    /**
     * @param queryString
     * @return a Collection of Items
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Collection search(String queryString) throws InfrastructureException {
        if (StringUtils.nullOrBlank(queryString)) {
            return new ArrayList();
        }
        Query query = LuceneUtils.createSearchAllTheWordsQuery(queryString, CONCATENATED_CONTENT, BooleanClause.Occur.MUST);
        return this.search(query);
    }

    public Collection searchIds(String queryString) throws InfrastructureException {
        if (StringUtils.nullOrBlank(queryString)) {
            return new ArrayList();
        }
        Query query = LuceneUtils.createSearchAllTheWordsQuery(queryString, CONCATENATED_CONTENT, BooleanClause.Occur.MUST);
        return this.searchIds(query);
    }

    /**
     * @param query
     * @return a Collection of Orders
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Collection search(Query query) throws InfrastructureException {
        Collection orders = new ArrayList();
        List orderIds = new ArrayList(searchIds(query));
        orders = daoFactory.getPurchasingOrderDAO().findOrdersUsingIds(orderIds);
        return orders;
    }

    public Collection searchIds(Query query) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        //create(file);
        final ArrayList orderIDs = new ArrayList();
        try {
            final IndexReader reader = IndexReader.open(file);
            final IndexSearcher searcher = new IndexSearcher(reader);
            String queryString = query.toString();
            log.debug("Search OrderIndex: " + queryString);
//            query = QueryParser.parse(queryString,CONCATENATED_CONTENT, analyzer);
            query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(queryString);
            final Hits hits = searcher.search(query);
            for (int i = 0; i < hits.length(); i++) {
                final Document doc = hits.doc(i);
                Long id = new Long(doc.get(ORDER_ID));
                orderIDs.add(id);
            }
            searcher.close();
            reader.close();
        }
        catch (IOException e) {
            log.error("Error in OrderIndex.searchIds(Query)", e);
//            throw new InfrastructureException("OrderIndex.searchIds(Query query): " + e.getMessage(), e);
        } catch (ParseException e) {
            log.error("Error in OrderIndex.searchIds2(Query)", e);
//            throw new InfrastructureException("OrderIndex.searchIds(Query query): " + e.getMessage(), e);
        }
        return orderIDs;
    }

    public static File getCurrentIndexDirectory() {
        boolean isStandAloneApp = !StringUtils.nullOrBlank(System.getProperty(Constants.ENV_KEY));
        if (isStandAloneApp) {
            return tempIndexDirectory;
        } else {
            return indexDirectory;
        }
    }

    public static File getIndexDirectory() {
        return indexDirectory;
    }

    public static File getTempIndexDirectory() {
        return tempIndexDirectory;
    }
}