package us.mn.state.health.model.util.search;

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
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.io.IOUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.lucene.LuceneUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource;
import us.mn.state.health.persistence.HibernateUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RequestLineItemIndex extends EntityIndex {
    private static Log log = LogFactory.getLog(RequestLineItemIndex.class);
    public static final String CONCATENATED_CONTENT = "concatenatedContent";
    public static final String REQUEST_LINE_ITEM_ID = "requestLineItemId";
    public static final String RLI_STATUS_ID = "rliStatusId";
    public static final String RLI_STATUS_NAME = "rliStatusName";
    public static final String RLI_STATUS_CODE = "rliStatusCode";
    public static final String FUNDING_SRC_ORG_BDGT_CODES = "orgBdgtCodes";
    public static final String FUNDING_SRC_ORG_BDGT_NAMES = "orgBdgtNames";
    public static final String ITEM_DESCRIPTION = "itemDescription_";
    public static final String ITEM_MODEL = "itemModel";
    public static final String ITEM_CATEGORY_CODE = "itemCategoryCode";
    public static final String ITEM_CATEGORY_NAME = "itemCategoryName";
    public static final String VENDOR_NAMES = "vendorNames"; //this field holds the name of the vendors
    public static final String VENDOR_IDS = "vendorIDs"; // this field holds the id of the vendors (if there is one)
    public static final String ITEM_ICNBR = "itemIcnbr";
    public static final String ITEM_ID = "itemId";
    public static final String SWIFT_ITEM_ID = "swiftId";
    public static final String REQUEST_ID = "requestId";
    public static final String REQUESTOR_ID = "requestorId";
    public static final String PURCHASER_ID = "purchaserId";
    public static final String REQUEST_TRACKING_NUMBER = "requestTrackingNumber";
    public static final String NEED_BY_DATE = "needByDate";
    public static final String DATE_REQUESTED = "dateRequested";
    public static final String PRIORITY_CODE = "priorityCode";
    public static final String INDEX_NAME = "RequestIndex";

    protected static final Analyzer analyzer = new StandardAnalyzer();
    protected static IndexWriter rliIndexWriter;
    protected final static Object rliMutex = new Object();
    protected final static Object rliMutex2 = new Object();
    protected static DAOFactory daoFactory = new HibernateDAOFactory(); //default to Hibernate
    protected static boolean indexExists = true;


    private static File indexDirectory;
    private static File tempIndexDirectory;
    private static int optimizeCount = 0;
    private static Boolean isIndexing=false;

    static {
        try {
//            tempIndexDirectory = new File(Config.getConfig(Constants.MATERIALS_MANAGMENT_CODE, Constants.TEMP_REQUEST_LINE_ITEM_INDEX_PATH_CODE));
//            indexDirectory = new File(Config.getConfig(Constants.MATERIALS_MANAGMENT_CODE, Constants.REQUEST_LINE_ITEM_INDEX_PATH_CODE));
            String idxName = "requestLineItemIndex";
            String envDir = System.getProperty("PRISM_LUCENE") == null ? System.getenv("PRISM_LUCENE") : System.getProperty("PRISM_LUCENE");
            indexDirectory = new File(envDir+"/" + idxName);
            tempIndexDirectory = new File(envDir+"/temp/"+idxName);
        }
        catch (Exception e) {
//        catch (InterruptedException e) {
            log.error("Error in RequestLineItemIndex in the static block ", e);
        }
    }

    public static void createIndex(boolean newIndex) {
        if (isIndexing) {
            return;    //An indexing operation is currently in progress
        }

        synchronized (rliMutex) {
            File index = getCurrentIndexDirectory();
            if (newIndex) {
                boolean delete = IOUtils.deleteDir(index);
                log.debug("Deleted RequestLineItemIndex: " + delete);
            }
            create(index);
        }
    }

    private static void create(File index) {
//        Uncomment this when used with searchable 04/24/2007
        try {
            Boolean indexWasInterrupted = false;
            if (index.exists()) {
            File [] files = index.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (file.getName().equalsIgnoreCase("INDEXING")) {
                        indexWasInterrupted = true;
                        break;
                    }
                }
            }

            if (!RequestIndex.indexExists ||  indexWasInterrupted || !index.exists() || (index.listFiles().length <= 2)) {       //directory does not exist, is empty or only has initial 2 files
                log.warn("RequestLineItemIndex NOT found, Initializing directory, Index will be re-created shortly.");
                indexExists = false;
                index.mkdirs();
                synchronized (rliMutex) {
                    rliIndexWriter = new IndexWriter(index, analyzer, true);
                    rliIndexWriter.close();
                }
            }
        }
        catch (IOException e) {
            log.error("Error in RequestLineItemIndex.create()", e);
        }
//        catch (InfrastructureException e) {
//            log.error("Error in RequestLineItemIndex.create()", e);
//        }
    }


    public static void createIndexAtRuntime() throws InfrastructureException { //throws InfrastructureException {
        synchronized (rliMutex2) {
            if (isIndexing) {
                log.warn("A " + INDEX_NAME + " index operation is already running.");
                return;    //An indexing operation is currently in progress
            }
            isIndexing = true;
        }

        try {
            File f = new File(indexDirectory,"INDEXING");
            Boolean abortIndex = false;
            f.createNewFile();

            List rliIds = (List) new HibernateDAO().
                    executeQuery("select r.requestLineItemId from RequestLineItem r order by r.requestLineItemId desc");
            List<List> paginatedList = CollectionUtils.paginateList(rliIds, 1000);
            int i = 0;
            Long maxSecondsUsed = new Long(0);
            Long secondsUsed ;

            for (List list : paginatedList) {
                Long beginTime = System.currentTimeMillis();
                log.info("    RequestLineItemIndex: Processing next batch: " + ++i + ", size: " + list.size() + ", beginning at " + new Date());
                indexRLI(list,i);
                Long endTime = System.currentTimeMillis();
                secondsUsed = (endTime - beginTime)/1000;
                if (secondsUsed > maxSecondsUsed) {
                    maxSecondsUsed = secondsUsed;
                }
                log.info("    RequestLineItemIndex: Processing batch: " + i + ", ended at " + new Date() + ", Processing Time: " + secondsUsed + " seconds");
                log.info("");
                File f2 = new File(indexDirectory,"ABORT");
                if (f2.exists()) {
                    abortIndex = true;
                    f2.delete();
                    log.error("     Aborting " + INDEX_NAME + " Indexing Operation.");
                    break;
                }
            }
            log.info("   RequestLineItemIndex Maximum Seconds Used for any batch was: " + maxSecondsUsed);
            log.info("");
            HibernateUtil.commitTransaction();
            HibernateUtil.closeSession();
            if (!abortIndex) {
              indexExists = true;
              f.delete();
            }
        } catch (Exception e) {
            log.error("   RequestLineItemIndex threw Exception, Indexing Aborted: " + new Date());
            e.printStackTrace();
        }
        finally{
            synchronized (rliMutex) {
                    isIndexing = false;
            }
        }
    }

    private static void indexRLI(Collection identifiers, int i) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        HibernateDAO hibernateDAO = new HibernateDAO();
        hibernateDAO.addQueryParam("rliIds", identifiers);
        Collection reqLineItems =
                hibernateDAO.executeQuery("select r from RequestLineItem r " +
                        " left join fetch r.status s " +
                        " left join fetch r.request req " +
                        " left join fetch req.priority p " +
                        " left join fetch r.purchaser purch " +
                        " left join fetch r.fundingSources fs " +
                        " left join fetch fs.orgBudget ob " +
                        " left join fetch req.requestor requestor " +
                        "where r.requestLineItemId in (:rliIds)");
//        hibernateDAO.executeQuery("select r from RequestLineItem r " +
//                        "where r.requestLineItemId in (:rliIds)");
        log.info("    RequestLineItemIndex: Finished hibernateDAO.executeQuery, beginning index of batch: " + i + " " +  new Date());
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        Transaction tx = fullTextSession.beginTransaction();
        for (Object reqLineItem : reqLineItems) {
            fullTextSession.index(reqLineItem);
        }
        tx.commit();
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();
    }

    public static void createIndexAtRuntime_() throws ParseException, IOException, InfrastructureException {


        ArrayList requestLineItemIDs = new ArrayList();
        RequestLineItemIndex index = new RequestLineItemIndex();
//        1. get database ids
        Collection requestLineItems = daoFactory.getRequestLineItemDAO().findAll();
        for (Iterator iterator = requestLineItems.iterator(); iterator.hasNext();) {
            RequestLineItem requestLineItem = (RequestLineItem) iterator.next();
            requestLineItemIDs.add(requestLineItem.getRequestLineItemId());
        }

//        2. commit transaction and clear session
        try {
            HibernateUtil.getSession().clear();
            HibernateUtil.commitTransaction();
        } catch (InfrastructureException e) {
            e.printStackTrace();
        }

//        3. iterate and update the indexes
        updateIndex(requestLineItemIDs, index);

//        4. get the index ids
        final IndexReader reader = IndexReader.open(indexDirectory);
        final IndexSearcher searcher = new IndexSearcher(reader);

        String queryString = "dateRequested:2*";
        Query query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(queryString);

        query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(queryString);
        final Hits hits = searcher.search(query);
        for (int i = 0; i < hits.length(); i++) {
            final Document doc = hits.doc(i);
            Long id = new Long(doc.get(REQUEST_LINE_ITEM_ID));
            requestLineItemIDs.add(id);
        }
        searcher.close();
        reader.close();

        //delete the unexisting records
        deleteFromIndex(requestLineItemIDs, index);
    }

    private static void updateIndex(ArrayList requestLineItemIDs, RequestLineItemIndex index) {
//        Uncomment this when used with searchable 04/24/2007
        /*
        for (Iterator iterator = requestLineItemIDs.iterator(); iterator.hasNext();) {
            Long rliId = (Long) iterator.next();
            RequestLineItem requestLineItem = null;
            try {
                requestLineItem = daoFactory.getRequestLineItemDAO().getRequestLineItemById(rliId, false);
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
            try {
                if (requestLineItem != null) {
                    index.dropAndAdd(requestLineItem);
                } else {
                    index.drop(requestLineItem);
                }
                HibernateUtil.getSession().clear();
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
        }

        */
    }

    private static void deleteFromIndex(ArrayList requestLineItemIDs, RequestLineItemIndex index) {
//        Uncomment this when used with searchable 04/24/2007
        /*
        for (Iterator iterator = requestLineItemIDs.iterator(); iterator.hasNext();) {
            Long rliId = (Long) iterator.next();
            RequestLineItem requestLineItem = null;
            try {
                requestLineItem = daoFactory.getRequestLineItemDAO().getRequestLineItemById(rliId, false);
            } catch (InfrastructureException e) {
                log.error("RLI:" + rliId + " was not found on the DB");
            }
            try {
                if (requestLineItem == null) {
                    requestLineItem = new RequestLineItem();
                    requestLineItem.setRequestLineItemId(rliId);
                    index.drop(requestLineItem);
                }
                HibernateUtil.getSession().clear();
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
        }
        */
    }

    public void dropAndAdd(Searchable entity) throws InfrastructureException {
        synchronized (rliMutex) {
            drop(entity);
            add(entity);
        }
    }

    /**
     * Add a RequestLineItem to the index only if it has been approved and is either waiting for purchase
     * (for purchase items) or is waiting to be picked (for stock items).
     *
     * @param entity
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void add(Searchable entity) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        create(file);
        final Document document = new Document();

        RequestLineItem rli = (RequestLineItem) entity;
        Request request = rli.getRequest();
        Status status = rli.getStatus();
        status = daoFactory.getStatusDAO().getStatusById(status.getStatusId(), false);
        String dateRequestedString = null;

        log.debug("RequestLineItemIndex.add(): rli ID: " + rli.getRequestLineItemId());
        try {
            StringBuffer concatenatedFields = new StringBuffer();
            StringBuffer icnbrContent = new StringBuffer();
            document.add((Fieldable) new Field(REQUEST_ID, request.getRequestId().toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
            String trackingNumber = request.getTrackingNumber();
            document.add((Fieldable) new Field(REQUEST_TRACKING_NUMBER, trackingNumber, Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(REQUEST_LINE_ITEM_ID, rli.getRequestLineItemId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(RLI_STATUS_ID, status.getStatusId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(RLI_STATUS_NAME, status.getName(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(RLI_STATUS_CODE, status.getStatusCode(), Field.Store.YES, Field.Index.TOKENIZED));

            concatenatedFields.append(status.getName()).append(" ");

            StringBuffer orgBdgtIds = new StringBuffer();
            StringBuffer orgBdgtCodes = new StringBuffer();
            StringBuffer orgBdgtNames = new StringBuffer();
            for (Object o : rli.getFundingSources()) {
                RequestLineItemFundingSource rliFS = (RequestLineItemFundingSource) o;
                OrgBudget orgBdgt = rliFS.getOrgBudget();

                orgBdgt = daoFactory.getOrgBudgetDAO().getOrgBudgetById(orgBdgt.getOrgBudgetId(), false);

                orgBdgtIds.append(orgBdgt.getOrgBudgetId().toString()).append(" ");
                //Grab the last four characters from the dept_id, as they should be the same as the
                orgBdgtCodes.append(orgBdgt.getDeptId().substring(4)).append(" ");
                orgBdgtNames.append(orgBdgt.getName()).append(" ");
            }
            document.add((Fieldable) new Field(FUNDING_SRC_ORG_BDGT_CODES, orgBdgtCodes.toString(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(FUNDING_SRC_ORG_BDGT_NAMES, orgBdgtNames.toString(), Field.Store.YES, Field.Index.TOKENIZED));
            concatenatedFields.append(orgBdgtNames.toString()).append(" ");
            concatenatedFields.append(orgBdgtCodes.toString()).append(" ");

            document.add((Fieldable) new Field(REQUESTOR_ID, request.getRequestor().getPersonId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            if (rli.getPurchaser() != null) {
                document.add((Fieldable) new Field(PURCHASER_ID, rli.getPurchaser().getPersonId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            } else {
                document.add((Fieldable) new Field(PURCHASER_ID, "0", Field.Store.YES, Field.Index.TOKENIZED));
            }
            String needByStr = null;

            if (request.getNeedByDate() != null) {
                needByStr = DateUtils.toString(request.getNeedByDate(), "yyyyMMdd");
                document.add((Fieldable) new Field(NEED_BY_DATE, needByStr, Field.Store.YES, Field.Index.TOKENIZED));
            } else {
                needByStr = DateUtils.toString(new Date(), "yyyyMMdd");
                document.add((Fieldable) new Field(NEED_BY_DATE, needByStr, Field.Store.YES, Field.Index.TOKENIZED));
            }

            if (request.getDateRequested() != null) {
                dateRequestedString = DateUtils.toString(request.getDateRequested(), "yyyyMMdd");
                document.add((Fieldable) new Field(DATE_REQUESTED, dateRequestedString, Field.Store.YES, Field.Index.TOKENIZED));
            }

            if (rli.getRequestItemType().equalsIgnoreCase(RequestLineItem.REQUEST_ITEM_TYPE_NONCAT)) {
                document.add((Fieldable) new Field(ITEM_DESCRIPTION, rli.getItemDescription(), Field.Store.YES, Field.Index.TOKENIZED));
                document.add((Fieldable) new Field(ITEM_CATEGORY_NAME, rli.getItemCategory().getName(), Field.Store.YES, Field.Index.TOKENIZED));
                document.add((Fieldable) new Field(ITEM_CATEGORY_CODE, rli.getItemCategory().getCategoryCode(), Field.Store.YES, Field.Index.TOKENIZED));
                document.add((Fieldable) new Field(ITEM_ID, "NULL", Field.Store.YES, Field.Index.TOKENIZED));
                document.add((Fieldable) new Field(SWIFT_ITEM_ID, rli.getSwiftItemId(), Field.Store.YES, Field.Index.TOKENIZED));

                String vendorName = "";
                if (!StringUtils.nullOrBlank(rli.getSuggestedVendorName())) {
                    vendorName = rli.getSuggestedVendorName();
                }
                document.add((Fieldable) new Field(VENDOR_NAMES, vendorName, Field.Store.YES, Field.Index.TOKENIZED));

                concatenatedFields.append(rli.getItemDescription()).append(" ");
                concatenatedFields.append(rli.getItemCategory().getName()).append(" ");
                concatenatedFields.append(rli.getItemCategory().getCategoryCode()).append(" ");
            } else {
                //added this in order to have a loaded item - no proxy
                Item item = rli.getItem();
                ArrayList<Long> list = new ArrayList<Long>();
                list.add(item.getItemId());
                item = (Item) daoFactory.getItemDAO().findItemsUsingIds(list).iterator().next();

                document.add((Fieldable) new Field(ITEM_DESCRIPTION, item.getDescription(), Field.Store.YES, Field.Index.TOKENIZED));
                document.add((Fieldable) new Field(ITEM_CATEGORY_NAME, item.getCategory().getName(), Field.Store.YES, Field.Index.TOKENIZED));
                document.add((Fieldable) new Field(ITEM_CATEGORY_CODE, item.getCategory().getCategoryCode(), Field.Store.YES, Field.Index.TOKENIZED));
                document.add((Fieldable) new Field(ITEM_ID, item.getItemId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
                if (item instanceof StockItem) {
                    StockItem si = (StockItem) item;
                    icnbrContent.append(si.getFullIcnbr());
                    document.add((Fieldable) new Field(ITEM_ICNBR, si.getFullIcnbr(), Field.Store.YES, Field.Index.TOKENIZED));
                }

                //add the vendor info
                Iterator iter = item.getItemVendors().iterator();
                StringBuffer vendorNamesBuffer = new StringBuffer();
                StringBuffer vendorIDsBuffer = new StringBuffer();
                while (iter.hasNext()) {
                    ItemVendor itemVendor = (ItemVendor) iter.next();
                    Vendor vendor = itemVendor.getVendor();
                    vendor = daoFactory.getVendorDAO().getVendorById(vendor.getVendorId(), false);
                    vendorNamesBuffer.append(vendor.getExternalOrgDetail().getOrgName()).append(" ");
                    vendorIDsBuffer.append(vendor.getVendorId().toString()).append(" ");
                }
                document.add((Fieldable) new Field(VENDOR_NAMES, vendorNamesBuffer.toString(), Field.Store.YES, Field.Index.TOKENIZED));
                document.add((Fieldable) new Field(VENDOR_IDS, vendorIDsBuffer.toString(), Field.Store.YES, Field.Index.TOKENIZED));

                String model = item.getModel();
                if (model != null) {
                    document.add((Fieldable) new Field(ITEM_MODEL, model, Field.Store.YES, Field.Index.TOKENIZED));
                    concatenatedFields.append(" ").append(model);
                } else {
                    document.add((Fieldable) new Field(ITEM_MODEL, "", Field.Store.YES, Field.Index.TOKENIZED));
                }
                document.add((Fieldable) new Field(PRIORITY_CODE, request.getPriority().getPriorityCode(), Field.Store.YES, Field.Index.TOKENIZED));
                concatenatedFields.append(vendorNamesBuffer.toString()).append(" ");
                concatenatedFields.append(item.getDescription()).append(" ");
                concatenatedFields.append(item.getCategory().getName()).append(" ");
                concatenatedFields.append(item.getCategory().getCategoryCode()).append(" ");
                if (!StringUtils.nullOrBlank(icnbrContent.toString())) {
                    concatenatedFields.append(icnbrContent).append(" ");
                }
            }
            concatenatedFields.append(" ").append(trackingNumber);
            document.add((Fieldable) new Field(CONCATENATED_CONTENT, concatenatedFields.toString(), Field.Store.YES, Field.Index.TOKENIZED));
        }
        catch (Exception e) {
            log.error("Error in RequestLineItemIndex.add() RLI#:" + rli.getRequestLineItemId(), e);
            throw new InfrastructureException("Exception in RequestLineItemIndex.add(): ", e);
        }

        try {
            synchronized (rliMutex) {
                int i = optimizeCount % optimizeValue;
                if (i != 0) {
                    addHandler(file, document, analyzer, log, false);
                    optimizeCount++;
                } else {
                    addHandler(file, document, analyzer, log, true);
                    optimizeCount = 1;
                }
            }
        }
        catch (IOException e) {
            log.error("Error in RequestLineItemIndex.add() RLI#:" + rli.getRequestLineItemId(), e);
            throw new InfrastructureException(e.getMessage(), e);
        }
    }

    public void drop(Searchable entity) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        create(file);
        RequestLineItem rli = (RequestLineItem) entity;
        String field = RequestLineItemIndex.REQUEST_LINE_ITEM_ID;
        String value = rli.getRequestLineItemId().toString();
        try {
            synchronized (rliMutex) {
                dropHandler(file, field, value, log);
            }
        }
        catch (IOException e) {
            log.error("Error in RequestLineItemIndex.drop() RLI#:" + rli.getRequestLineItemId(), e);
            throw new InfrastructureException(e.getMessage(), e);
        }
    }


    /**
     * @param query
     * @return a Collection of RequestLineItems
     * @throws InfrastructureException
     */

    public Collection search(Query query) throws InfrastructureException {
        final Collection results = new ArrayList();
        Collection rliIds = searchIds(query);
        for (Iterator iterator = rliIds.iterator(); iterator.hasNext();) {
            Long rliId = (Long) iterator.next();
            RequestLineItem reqLnItm;
            try {
                reqLnItm = daoFactory.getRequestLineItemDAO().getRequestLineItemById(rliId, false);
                results.add(reqLnItm);
            }
            catch (Exception e) {
                log.error("Error in RequestLineItemIndex.search(Query)", e);
            }

        }
        return results;
    }

    /**
     * @param query
     * @return a Collection of RequestLineItem Id's
     * @throws InfrastructureException
     */
    public Collection searchIds(Query query) throws InfrastructureException {
        final ArrayList requestLineItemIDs = new ArrayList();
        String queryString = query.toString();
        log.debug("Search RequestLineItemIndex: " + queryString);
        if (StringUtils.nullOrBlank(queryString)) {
            return requestLineItemIDs;
        }
        File file = getCurrentIndexDirectory();
        //create(file);
        try {
            final IndexReader reader = IndexReader.open(file);
            final IndexSearcher searcher = new IndexSearcher(reader);
            query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(queryString);
            final Hits hits = searcher.search(query);
            for (int i = 0; i < hits.length(); i++) {
                final Document doc = hits.doc(i);
                Long id = new Long(doc.get(REQUEST_LINE_ITEM_ID));
                requestLineItemIDs.add(id);
            }
            searcher.close();
            reader.close();
        }
        catch (IOException e) {
            log.debug(e);
        } catch (ParseException e) {
            log.error("Error in RequestLineItemIndex.searchIds(Query query)", e);
        } catch (Exception e) {
            log.error("Query=" + query.toString());
            log.error("Error in RequestLineItemIndex.searchIds(Query query)", e);
        }
        return requestLineItemIDs;
    }

    /**
     * @param query
     * @return a Collection of RequestLineItem Id's
     * @throws InfrastructureException
     */
    public Collection searchRequestIds(Query query) throws InfrastructureException {
        final ArrayList requestLineItemIDs = new ArrayList();
        String queryString = query.toString();
        log.debug("Search RequestLineItemIndex: " + queryString);
        if (StringUtils.nullOrBlank(queryString)) {
            return requestLineItemIDs;
        }
        File file = getCurrentIndexDirectory();
        //create(file);
        try {
            final IndexReader reader = IndexReader.open(file);
            final IndexSearcher searcher = new IndexSearcher(reader);
            query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(queryString);
            final Hits hits = searcher.search(query);
            for (int i = 0; i < hits.length(); i++) {
                final Document doc = hits.doc(i);
                Long id = new Long(doc.get(REQUEST_ID));
                requestLineItemIDs.add(id);
            }
            searcher.close();
            reader.close();
        }
        catch (IOException e) {
            log.debug(e);
//            throw new InfrastructureException("RequestLineItemIndex.searchIds(Query query): " + e.getMessage(), e);
        } catch (ParseException e) {
            log.error("Error in RequestLineItemIndex.searchIds(Query query)", e);
//            throw new InfrastructureException("StockItemIndex.search(): " + e.getMessage(), e);
        }
        return requestLineItemIDs;
    }

    /**
     * @param queryString
     * @return a Collection of RequestLineItems
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

    /**
     * @param queryString
     * @return a Collection of RequestLineItem Id's
     * @throws InfrastructureException
     */
    public Collection searchIds(String queryString) throws InfrastructureException {
        if (StringUtils.nullOrBlank(queryString)) {
            return new ArrayList();
        }
        Query query = LuceneUtils.createSearchAllTheWordsQuery(queryString, CONCATENATED_CONTENT, BooleanClause.Occur.MUST);
        return this.searchIds(query);
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