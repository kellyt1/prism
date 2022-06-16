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
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.dao.materialsrequest.RequestDAO;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.persistence.HibernateUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RequestIndex extends EntityIndex {
    private static Log log = LogFactory.getLog(RequestIndex.class);
    public static final String CONCATENATED_CONTENT = "concatenatedContent";
    public static final String REQUEST_ID = "requestId";
    public static final String REQUESTOR_NAME = "requestorName";
    public static final String REQUESTOR_ID = "requestorId";
    public static final String DATE_REQUESTED = "dateRequested";
    public static final String NEED_BY_DATE = "needByDate";
    public static final String PRIORITY_NAME = "priorityName";
    public static final String PRIORITY_ID = "priorityId";
    public static final String TRACKING_NUMBER = "trackingNumber";
    public static final String DELIVERY_DETAIL_ID = "deliveryDetailId";
    public static final String RLI_IDS = "requestLineItemsIds"; //field that holds the id's of the rli associated with this request
    public static final String INDEX_NAME = "RequestIndex";

    protected static final Analyzer analyzer = new StandardAnalyzer();
    protected static IndexWriter requestIndexWriter;
    protected final static Object mutex = new Object();
    protected final static Object mutex2 = new Object();
    protected static DAOFactory daoFactory = new HibernateDAOFactory(); //default to Hibernate
    protected static boolean indexExists = true;

    private static File indexDirectory;
    private static File tempIndexDirectory;
    private static int optimizeCount = 0;
    private static Boolean isIndexing=false;

    static {
        try {
//            tempIndexDirectory = new File(Config.getConfig(Constants.MATERIALS_MANAGMENT_CODE, Constants.TEMP_REQUEST_INDEX_PATH_CODE));
//            indexDirectory = new File(Config.getConfig(Constants.MATERIALS_MANAGMENT_CODE, Constants.REQUEST_INDEX_PATH_CODE));
            String idxName = "requestIndex";
            String envDir = System.getProperty("PRISM_LUCENE") == null ? System.getenv("PRISM_LUCENE") : System.getProperty("PRISM_LUCENE");
            indexDirectory = new File(envDir+"/" + idxName);
            tempIndexDirectory = new File(envDir+"/temp/"+idxName);
        }
        catch (Exception e) {
//        catch (InterruptedException e) {
            log.error("Error in RequestIndex in the static block ", e);
        }
    }

    public static void createIndex(boolean newIndex) {
        if (isIndexing) {
            return;    //An indexing operation is currently in progress
        }
        synchronized (mutex) {
            File index = getCurrentIndexDirectory();
            if (newIndex) {
                boolean delete = IOUtils.deleteDir(index);
                log.debug("Deleted RequestIndex: " + delete);
            }
            create(index);
        }
    }

    private static void create(File index) {
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
//            if (!index.exists() || index.listFiles().length == 0) {
//            if (!index.exists() || (index.listFiles().length <= 2)) {       //directory does not exist, is empty or only has initial 2 files
            if (indexWasInterrupted || !index.exists() || (index.listFiles().length <= 2)) {       //directory does not exist, is empty or only has initial 2 files
                log.warn("    " + INDEX_NAME + " NOT found, Initializing directory, a batch reindex will be scheduled and will begin shortly.");
//                System.out.println("RequestIndex NOT found, Initializing directory, but you must initiate index creation.");
                indexExists = false;
                index.mkdirs();
                synchronized (mutex) {
                    requestIndexWriter = new IndexWriter(index, analyzer, true);
                    requestIndexWriter.close();
                }

                //TODO TR refactor this to remove the coupling
//                RequestLineItemIndex.createIndex(true);

//                RequestDAO requestDAO = daoFactory.getRequestDAO();
//                Collection requests = requestDAO.findAll();
//                RequestIndex requestIndexer = new RequestIndex();
//                for (Object request1 : requests) {
//                    Request request = (Request) request1;
//                    Uncomment this when used with searchable
//                    try {
//                        requestIndexer.add(request);
//                    } catch (InfrastructureException e) {
//                        log.error("Error in RequestIndex.create() for the Request:" + request.getRequestId(), e);
//                    }
//                }
/*
            }
            else if (index.listFiles().length == 0) {     //directory exists, but is empty
                synchronized (mutex) {
                    requestIndexWriter = new IndexWriter(index, analyzer, true);
                    requestIndexWriter.close();
                }
*/
            }

        }
        catch (IOException e) {
            log.error("Error in RequestIndex.create()", e);
        }
//        catch (InfrastructureException e) {
//            log.error("Error in RequestIndex.create()", e);
//        }
    }

    public static void createIndexAtRuntime() throws InfrastructureException {
        synchronized (mutex2) {
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

            //TODO TR refactor this to remove the coupling
            File f2 = new File(RequestLineItemIndex.getIndexDirectory(),"INDEXING");
            List requestIds = (List) new HibernateDAO().
                    executeQuery("select r.requestId from Request r order by r.requestId desc");
            List<List> paginatedList = CollectionUtils.paginateList(requestIds, 1000);
//            List<List> paginatedList = CollectionUtils.paginateList(requestIds, 100);
            int i = 0;
            Long maxSecondsUsed = new Long(0);
            Long secondsUsed ;
            for (List list : paginatedList) {
                Long beginTime = System.currentTimeMillis();
                log.info("    " + INDEX_NAME + ": Processing next batch: " + ++i + ", size: " + list.size() + ", beginning at " + new Date());
                indexRequests(list,i);
                Long endTime = System.currentTimeMillis();
                secondsUsed = (endTime - beginTime)/1000;
                if (secondsUsed > maxSecondsUsed) {
                    maxSecondsUsed = secondsUsed;
                }
                log.info("    " + INDEX_NAME + ": Processing batch: " + i + ", ended at " + new Date() + ", Processing Time: " + secondsUsed + " seconds");
                log.info("");
                File f3 = new File(indexDirectory,"ABORT");
                if (f3.exists()) {
                    abortIndex = true;
                    f3.delete();
                    log.error("     Aborting " + INDEX_NAME + " Indexing Operation.");
                    break;
                 }
//                break;
            }
            log.info("    " + INDEX_NAME + " Maximum Seconds Used for any batch was: " + maxSecondsUsed);
            HibernateUtil.commitTransaction();
            HibernateUtil.closeSession();
            if (!abortIndex) {
                indexExists = true;
                f.delete();

                //TODO TR refactor this to remove the coupling
                RequestLineItemIndex.indexExists = true;
                f2.delete();
            }
        } catch (Exception e) {
            log.error("    " + INDEX_NAME + " threw Exception, Indexing Aborted: " + new Date());
            e.printStackTrace();
        } finally {
//            System.out.println("   RequestIndex threw Exception, Indexing Aborted: " + new Date());
            synchronized (mutex) {
                    isIndexing = false;
            }
        }
    }

    private static void indexRequests(Collection identifiers,int i) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        HibernateDAO hibernateDAO = new HibernateDAO();
        hibernateDAO.addQueryParam("requestIds", identifiers);
        Collection requests = hibernateDAO.executeQuery("select req from Request req" +
                " left join fetch req.requestLineItems r " +
                " left join fetch r.status s " +
                " left join fetch req.priority p " +
                " left join fetch r.purchaser purch " +
                " left join fetch req.requestor requestor " +
                "where req.requestId in (:requestIds)");
        log.info("    " + INDEX_NAME + ": Finished hibernateDAO.executeQuery, beginning index of batch: " + i + " at " + new Date());

        FullTextSession fullTextSession = Search.createFullTextSession(session);
        Transaction tx = fullTextSession.beginTransaction();
        for (Object request : requests) {
            fullTextSession.index(request);
        }
        tx.commit();
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();
    }

    public static void createIndexAtRuntime_() throws ParseException, IOException, InfrastructureException {


        ArrayList requestIds = new ArrayList();
        RequestIndex index = new RequestIndex();
//        1. get database ids
        Collection Requests = daoFactory.getRequestDAO().findAll();
        for (Iterator iterator = Requests.iterator(); iterator.hasNext();) {
            Request request = (Request) iterator.next();
            requestIds.add(request.getRequestId());
        }

//        2. commit transaction and clear session
        try {
            HibernateUtil.getSession().clear();
            HibernateUtil.commitTransaction();
        } catch (InfrastructureException e) {
            e.printStackTrace();
        }

        RequestDAO requestDAO = daoFactory.getRequestDAO();
//        3. iterate and update the indexes
        updateIndex(requestIds, index, requestDAO);

//        4. get the index ids
        final IndexReader reader = IndexReader.open(indexDirectory);
        final IndexSearcher searcher = new IndexSearcher(reader);

        String queryString = DATE_REQUESTED + ":2*";
        Query query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(queryString);

        query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(queryString);
        final Hits hits = searcher.search(query);
        for (int i = 0; i < hits.length(); i++) {
            final Document doc = hits.doc(i);
            Long id = new Long(doc.get(REQUEST_ID));
            requestIds.add(id);
        }
        searcher.close();
        reader.close();

        //delete the unexisting records
        deleteFromIndex(requestIds, index, requestDAO);
    }

    private static void updateIndex(ArrayList RequestIDs, RequestIndex index, RequestDAO requestDAO) {
        for (Iterator iterator = RequestIDs.iterator(); iterator.hasNext();) {
            Long id = (Long) iterator.next();
            Request request = null;
            try {
                request = (Request) requestDAO.getRequestById(id, false);
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
            try {
//                Uncomment this when used with searchable
                if (request != null) {
//                    index.dropAndAdd(request);
                } else {
//                    index.drop(request);
                }
                HibernateUtil.getSession().clear();
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
        }
    }

    private static void deleteFromIndex(ArrayList RequestIDs, RequestIndex index, RequestDAO requestDAO) {
        for (Iterator iterator = RequestIDs.iterator(); iterator.hasNext();) {
            Long id = (Long) iterator.next();
            Request request = null;
            try {
                request = requestDAO.getRequestById(id, false);
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
            try {
                if (request == null) {
                    request = new Request();
                    request.setRequestId(id);
//                    Uncomment this when used with searchable
//                    index.drop(request);
                }
                HibernateUtil.getSession().clear();
            } catch (InfrastructureException e) {
                log.error("Request:" + id + " was not found on the DB");
            }
        }
    }


    public void dropAndAdd(Searchable entity) throws InfrastructureException {
        synchronized (mutex) {
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
        Request request = (Request) entity;
        log.debug("Request.add(): request ID: " + request.getRequestId());
        try {
            StringBuffer concatenatedFields = new StringBuffer();

            document.add((Fieldable) new Field(REQUEST_ID, request.getRequestId().toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
            document.add((Fieldable) new Field(REQUESTOR_NAME, request.getRequestor().getLastAndFirstName(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(REQUESTOR_ID, request.getRequestor().getPersonId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(DATE_REQUESTED, request.getDateRequested().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(PRIORITY_NAME, request.getPriority().getName(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(PRIORITY_ID, request.getPriority().getPriorityId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(TRACKING_NUMBER, request.getTrackingNumber(), Field.Store.YES, Field.Index.TOKENIZED));

            if (request.getNeedByDate() != null) {
                document.add((Fieldable) new Field(NEED_BY_DATE, request.getNeedByDate().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            }
            if (request.getDeliveryDetail() != null) {
                document.add((Fieldable) new Field(DELIVERY_DETAIL_ID, request.getDeliveryDetail().getDeliveryDetailId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            }

            concatenatedFields.append(request.getRequestId().toString()).append(" ");
            concatenatedFields.append(request.getRequestor().getLastAndFirstName()).append(" ");
            concatenatedFields.append(request.getTrackingNumber()).append(" ");

            document.add((Fieldable) new Field(CONCATENATED_CONTENT, concatenatedFields.toString(), Field.Store.YES, Field.Index.TOKENIZED));

            StringBuffer rliIds = new StringBuffer();
            for (Iterator iterator = request.getRequestLineItems().iterator(); iterator.hasNext();) {
                RequestLineItem requestLineItem = (RequestLineItem) iterator.next();
                rliIds.append(requestLineItem.getRequestLineItemId()).append(" ");
            }
            document.add((Fieldable) new Field(RLI_IDS, rliIds.toString(), Field.Store.YES, Field.Index.TOKENIZED));
        }
        catch (Exception e) {
            log.error("Exception in RequestIndex.add(), Request#:" + request.getRequestId(), e);
            throw new InfrastructureException("Exception in RequestIndex.add(): ", e);
        }

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
//                requestIndexWriter = new IndexWriter(file, analyzer, false);
//                try {
//                    requestIndexWriter.addDocument(document);
//                    requestIndexWriter.optimize();
//                }
//                finally {
//                    requestIndexWriter.close();
//                }
            }
        }
        catch (IOException e) {
            log.error("Error in RequestIndex.add() for Request#:" + request.getRequestId(), e);
            throw new InfrastructureException(e.getMessage(), e);
        }
    }

    public void drop(Searchable entity) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        create(file);
        Request request = (Request) entity;
        try {
            synchronized (mutex) {
                dropHandler(file, REQUEST_ID, request.getRequestId().toString(), log);
//                final IndexReader reader = IndexReader.open(file);
//                try {
//                    Term term = new Term(RequestIndex.REQUEST_ID, request.getRequestId().toString());
//                    reader.deleteDocuments(term);
//                }
//                finally {
//                    reader.close();
//                }
            }
        }
        catch (IOException e) {
            log.error("Error in RequestIndex.drop() for Request#" + request.getRequestId(), e);
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

    public Collection search(Query query) throws InfrastructureException {
        Collection requests = new ArrayList();
        List requestIds = new ArrayList(searchIds(query));
        for (int i = 0; i < requestIds.size(); i++) {
            Long id = (Long) requestIds.get(i);
            Request request;
            try {
                //TODO maybe we should create a DAO method to get all the requests in one query
                request = daoFactory.getRequestDAO().getRequestById(id, false);
                requests.add(request);
            }
            catch (Exception e) {
                log.error("Error in RequestIndex.search(Query)", e);
            }
        }
        return requests;
    }

    public Collection searchIds(Query query) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        //create(file);
        final ArrayList requestIDs = new ArrayList();
        try {
            final IndexReader reader = IndexReader.open(file);
            final IndexSearcher searcher = new IndexSearcher(reader);
            String queryString = query.toString();
            log.debug("Search RequestIndex: " + queryString);
            query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(queryString);
            final Hits hits = searcher.search(query);
            for (int i = 0; i < hits.length(); i++) {
                final Document doc = hits.doc(i);
                Long id = new Long(doc.get(REQUEST_ID));
                requestIDs.add(id);
            }
            searcher.close();
            reader.close();
        }
        catch (IOException e) {
            log.error("Error in RequestIndex.searchIds(Query query)", e);
//            throw new InfrastructureException("RequestIndex.searchIds(Query query): " + e.getMessage(), e);
        } catch (ParseException e) {
            log.error("Error in RequestIndex.searchIds(Query query)", e);
//            throw new InfrastructureException("StockItemIndex.search(): " + e.getMessage(), e);
        }
        return requestIDs;
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