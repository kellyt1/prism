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
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.dao.purchasing.PurchasingOrderLineItemDAO;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.model.util.configuration.Config;
import us.mn.state.health.persistence.HibernateUtil;

public class OrderLineItemIndex extends EntityIndex {
    private static Log log = LogFactory.getLog(OrderLineItemIndex.class);
    public static final String CONCATENATED_CONTENT = "concatenatedContent";
    public static final String ORDER_LINE_ITEM_ID = "orderLineItemId";
    public static final String ITEM_DESCRIPTION = "itemDescription";
    public static final String ITEM_MODEL = "itemModel";
    public static final String REQUESTOR_IDs = "requestorIds";
    public static final String ORG_BUDGET_CODES = "orgBudgetCodes";
    public static final String BUY_UNIT_CODE = "buyUnitCode";
    public static final String BUY_UNIT_COST = "buyUnitCost";
    public static final String QUANTITY = "quantity";
    public static final String STATUS_CODE = "statusCode";
    public static final String VENDOR_CATALOG_NBR = "vendorCatalogNbr";
    public static final String COMODITY_CODE = "comodityCode";
    public static final String ORDER_ID = "orderId";
    public static final String REQ_LINE_ITEM_IDs = "reqLineItemIds";

    public static String CLASSNAME = "classname";

    protected static final Analyzer analyzer = new StandardAnalyzer();
    protected static IndexWriter writer;
    protected final static Object mutex = new Object();
    protected static DAOFactory daoFactory = new HibernateDAOFactory(); //default to Hibernate
    protected static boolean indexExists = true;

    private static File indexDirectory;
    private static File tempIndexDirectory;
    private static int optimizeCount = 0;

    static {
        try {
            tempIndexDirectory = new File(Config.getConfig(Constants.MATERIALS_MANAGMENT_CODE, Constants.TEMP_ORDER_LINE_ITEM_INDEX_PATH_CODE));
            indexDirectory = new File(Config.getConfig(Constants.MATERIALS_MANAGMENT_CODE, Constants.ORDER_LINE_ITEM_INDEX_PATH_CODE));
        }
        catch (InterruptedException e) {
            log.error("Error in OrderLineItemIndex in the static block ", e);
        }
    }

    public static void createIndex(boolean newIndex) {
        synchronized (mutex) {
            File index = getCurrentIndexDirectory();
            if (newIndex) {
                boolean delete = IOUtils.deleteDir(index);
                log.debug("Deleted OrderLineItemIndex: " + delete);
            }
            create(index);
        }
    }

    public static void createIndexAtRuntime() throws ParseException, IOException, InfrastructureException {


        ArrayList ids = new ArrayList();
        OrderLineItemIndex index = new OrderLineItemIndex();
//        1. get database ids
        Collection orderLineItems = daoFactory.getPurchasingOrderLineItemDAO().findAllOrderLineItems();
        for (Iterator iterator = orderLineItems.iterator(); iterator.hasNext();) {
            OrderLineItem orderLineItem = (OrderLineItem) iterator.next();
            ids.add(orderLineItem.getOrderLineItemId());
        }

//        2. commit transaction and clear session
        try {
            HibernateUtil.getSession().clear();
            HibernateUtil.commitTransaction();
        } catch (InfrastructureException e) {
            e.printStackTrace();
        }

        PurchasingOrderLineItemDAO purchasingOrderLineItemDAO = daoFactory.getPurchasingOrderLineItemDAO();
//        3. iterate and update the indexes
        updateIndex(ids, index, purchasingOrderLineItemDAO);

//        4. get the index ids
        final IndexReader reader = IndexReader.open(indexDirectory);
        final IndexSearcher searcher = new IndexSearcher(reader);

        String queryString = CLASSNAME + ":us*";
        Query query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(queryString);

        query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(queryString);
        final Hits hits = searcher.search(query);
        for (int i = 0; i < hits.length(); i++) {
            final Document doc = hits.doc(i);
            Long id = new Long(doc.get(ORDER_LINE_ITEM_ID));
            ids.add(id);
        }
        searcher.close();
        reader.close();

        //delete the unexisting records
        deleteFromIndex(ids, index, purchasingOrderLineItemDAO);
    }

    private static void updateIndex(ArrayList iDs, OrderLineItemIndex index, PurchasingOrderLineItemDAO purchasingOrderLineItemDAO) {
        for (Iterator iterator = iDs.iterator(); iterator.hasNext();) {
            Long id = (Long) iterator.next();
            OrderLineItem orderLineItem = null;
            try {
                orderLineItem = (OrderLineItem) purchasingOrderLineItemDAO.getOrderLineItemById(id, false);
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
            try {
                if (orderLineItem != null) {
//                    index.dropAndAdd(orderLineItem);
                } else {
//                    index.drop(orderLineItem);
                }
                HibernateUtil.getSession().clear();
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
        }
    }

    private static void deleteFromIndex(ArrayList iDs, OrderLineItemIndex index, PurchasingOrderLineItemDAO purchasingOrderLineItemDAO) {
        for (Iterator iterator = iDs.iterator(); iterator.hasNext();) {
            Long id = (Long) iterator.next();
            OrderLineItem orderLineItem = null;
            try {
                orderLineItem = purchasingOrderLineItemDAO.getOrderLineItemById(id, false);
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
            try {
                if (orderLineItem == null) {
                    orderLineItem = new OrderLineItem();
                    orderLineItem.setOrderLineItemId(id);
//                    index.drop(orderLineItem);
                }
                HibernateUtil.getSession().clear();
            } catch (InfrastructureException e) {
                log.error("OLI:" + id + " was not found on the DB");
            }
        }
    }

    private static void create(File index) {
        try {
            if (!index.exists()) {
                indexExists = false;
                index.mkdirs();
                writer = new IndexWriter(index, analyzer, true);
                writer.close();
                PurchasingOrderLineItemDAO oliDAO = daoFactory.getPurchasingOrderLineItemDAO();
                Collection items = oliDAO.findAllOrderLineItems();
                OrderLineItemIndex indexer = new OrderLineItemIndex();
                for (Object item1 : items) {
                    OrderLineItem item = (OrderLineItem) item1;
//                    try {
//                        indexer.add(item);
//                    } catch (InfrastructureException e) {
//                        log.error("Error in OrderLineItemIndex.create() for the OLI:" + item.getOrderLineItemId(), e);
//                    }
                }
            }
        }
        catch (Exception e) {
            log.error("Error in OrderLineItemIndex.create()", e);
        }
    }

    public OrderLineItemIndex() {
    }

    public void add(Searchable entity) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        create(file);
        final Document document = new Document();
        OrderLineItem oli = (OrderLineItem) entity;

        StringBuffer concatenatedFields = new StringBuffer();
        document.add((Fieldable) new Field(CLASSNAME, oli.getClass().getName(), Field.Store.YES, Field.Index.TOKENIZED));
        Long orderLineItemId = oli.getOrderLineItemId();
        document.add((Fieldable) new Field(ORDER_LINE_ITEM_ID, orderLineItemId.toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        Unit buyUnit = oli.getBuyUnit();
        document.add((Fieldable) new Field(BUY_UNIT_CODE, buyUnit.getCode(), Field.Store.YES, Field.Index.TOKENIZED));
        Double buyUnitCost = oli.getBuyUnitCost();
        document.add((Fieldable) new Field(BUY_UNIT_COST, buyUnitCost.toString(), Field.Store.YES, Field.Index.TOKENIZED));
        Integer quantity = oli.getQuantity();
        document.add((Fieldable) new Field(QUANTITY, quantity.toString(), Field.Store.YES, Field.Index.TOKENIZED));
        Status status = oli.getStatus();
        document.add((Fieldable) new Field(STATUS_CODE, status.getStatusCode(), Field.Store.YES, Field.Index.TOKENIZED));
        String vendorCatalogNbr = oli.getVendorCatalogNbr();
        if (vendorCatalogNbr != null && !vendorCatalogNbr.equals("")) {
            document.add((Fieldable) new Field(VENDOR_CATALOG_NBR, vendorCatalogNbr, Field.Store.YES, Field.Index.TOKENIZED));
        }
        String commodityCode = oli.getCommodityCode();
        if (commodityCode != null) {
            document.add((Fieldable) new Field(COMODITY_CODE, commodityCode, Field.Store.YES, Field.Index.TOKENIZED));
        }
        Order order = oli.getOrder();
        document.add((Fieldable) new Field(ORDER_ID, order.getOrderId().toString(), Field.Store.YES, Field.Index.TOKENIZED));

        StringBuffer itemDescription = new StringBuffer();
        if (oli.getItemType().equals(OrderLineItem.ORDER_ITEM_TYPE_NONCAT)) {
            itemDescription.append(oli.getItemDescription());
        } else {
            itemDescription.append(oli.getItem().getDescription());
        }
        document.add((Fieldable) new Field(ITEM_DESCRIPTION, itemDescription.toString(), Field.Store.YES, Field.Index.TOKENIZED));


        if (!oli.getItemType().equals(OrderLineItem.ORDER_ITEM_TYPE_NONCAT)) {
            String itemModel = oli.getItem().getModel();
            if (itemModel != null) {
                document.add((Fieldable) new Field(ITEM_MODEL, itemModel, Field.Store.YES, Field.Index.TOKENIZED));
                concatenatedFields.append(itemModel).append(" ");
            }
        }
        concatenatedFields.append(oli.getStatus().getName()).append(" ");
        concatenatedFields.append(itemDescription).append(" ");
        concatenatedFields.append(commodityCode).append(" ");

        StringBuffer requestorIds = new StringBuffer();
        StringBuffer rliIds = new StringBuffer();
        StringBuffer orgBudgetCodes = new StringBuffer();
        Collection rlitems = oli.getRequestLineItems();
        for (Iterator iterator = rlitems.iterator(); iterator.hasNext();) {
            RequestLineItem requestLineItem = (RequestLineItem) iterator.next();
            String requestorId = requestLineItem.getRequest().getRequestor().getPersonId().toString();
            String rliIdString = requestLineItem.getRequestLineItemId().toString();
            String orgBudgetCodes2 = requestLineItem.getFundingSrcSummary().getOrgBudgetCodes();
            if (!StringUtils.checkIfStringContainsWord(requestorIds.toString(), requestorId)) {
                requestorIds.append(requestorId).append(" ");
            }
            if (!StringUtils.checkIfStringContainsWord(orgBudgetCodes.toString(), orgBudgetCodes2)) {
                orgBudgetCodes.append(orgBudgetCodes2).append(" ");
            }
            if (!StringUtils.checkIfStringContainsWord(rliIds.toString(), rliIdString)) {
                rliIds.append(rliIdString).append(" ");
            }
        }
        document.add((Fieldable) new Field(REQUESTOR_IDs, requestorIds.toString(), Field.Store.YES, Field.Index.TOKENIZED));
        document.add((Fieldable) new Field(ORG_BUDGET_CODES, orgBudgetCodes.toString(), Field.Store.YES, Field.Index.TOKENIZED));
        document.add((Fieldable) new Field(REQ_LINE_ITEM_IDs, rliIds.toString(), Field.Store.YES, Field.Index.TOKENIZED));
        document.add((Fieldable) new Field(CONCATENATED_CONTENT, concatenatedFields.toString(), Field.Store.YES, Field.Index.TOKENIZED));

        try {
            synchronized (mutex) {
                int i = optimizeCount % optimizeValue;
                if (i != 0) {
                    addHandler(file, document, analyzer, log, false);
                    optimizeCount++;
                } else {
                    addHandler(file, document, analyzer, log, true);
                    optimizeCount = 1;
                }
//                writer = new IndexWriter(file, analyzer, false);
//                try {
//                    writer.addDocument(document);
//                    writer.optimize();
//                } finally {
//                    writer.close();
//                }

            }
        }
        catch (IOException e) {
            log.error("Error in OrderLineItemIndex.add(), oli#:" + oli.getOrderLineItemId(), e);
            throw new InfrastructureException(e.getMessage(), e);
        }
    }

    public void drop(Searchable entity) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        create(file);
        OrderLineItem oli = (OrderLineItem) entity;
        try {
            synchronized (mutex) {
                dropHandler(file, ORDER_LINE_ITEM_ID, oli.getOrderLineItemId().toString(), log);
//                final IndexReader reader = IndexReader.open(file);
//                try {
//                    Term term = new Term(ORDER_LINE_ITEM_ID, oli.getOrderLineItemId().toString());
//                    reader.deleteDocuments(term);
//                } finally {
//                    reader.close();
//                }
            }
        }
        catch (IOException e) {
            log.error("Error in OrderLineItemIndex.drop(), oli#:" + oli.getOrderLineItemId(), e);
            throw new InfrastructureException(e.getMessage(), e);
        }
    }

    public void dropAndAdd(Searchable entity) throws InfrastructureException {
        synchronized (mutex) {
            drop(entity);
            add(entity);
        }
    }

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

    public Collection search(Query query) throws InfrastructureException {
        Collection orderLineItems = new ArrayList();
        List orderLineItemIds = new ArrayList(searchIds(query));
        for (int i = 0; i < orderLineItemIds.size(); i++) {
            Long id = (Long) orderLineItemIds.get(i);
            OrderLineItem orderLineItem;
            try {
                //TODO maybe we should create a DAO method to get all the requests in one query
                orderLineItem = daoFactory.getPurchasingOrderLineItemDAO().getOrderLineItemById(id, false);
                orderLineItems.add(orderLineItem);
            }
            catch (Exception e) {
                log.error("Error in OrderLineItemIndex.search(Query query)", e);
            }
        }
        return orderLineItems;
    }

    public Collection searchIds(Query query) throws InfrastructureException {
        final ArrayList orderLineItemIDs = new ArrayList();
        String queryString = query.toString();
        log.debug("Search OrderLineItemIndex: " + queryString);
        if (StringUtils.nullOrBlank(queryString)) {
            return orderLineItemIDs;
        }
        File file = getCurrentIndexDirectory();
        try {
            final IndexReader reader = IndexReader.open(file);
            final IndexSearcher searcher = new IndexSearcher(reader);
            query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(queryString);
            final Hits hits = searcher.search(query);
            for (int i = 0; i < hits.length(); i++) {
                final Document doc = hits.doc(i);
                Long id = new Long(doc.get(ORDER_LINE_ITEM_ID));
                orderLineItemIDs.add(id);
            }
            searcher.close();
            reader.close();
        }
        catch (IOException e) {
            log.error("Error in OrderLineItemIndex.searchIds(Query query)", e);
//            throw new InfrastructureException("OrderLineItemIndex.searchIds(Query query): " + e.getMessage(), e);
        } catch (ParseException e) {
            log.error("Error in OrderLineItemIndex.searchIds2(Query query)", e);
//            throw new InfrastructureException("OrderLineItemIndex.searchIds(Query query): " + e.getMessage(), e);
        }
        return orderLineItemIDs;
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
