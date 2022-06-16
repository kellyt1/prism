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
import us.mn.state.health.dao.inventory.StockItemDAO;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.*;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.persistence.HibernateUtil;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class StockItemIndex extends EntityIndex {
    private static int optimizeCount = 0;
    private static Log log = LogFactory.getLog(StockItemIndex.class);
    public static final String ICNBR = "icnbr";
    public static final String FULL_ICNBR = "fullIcnbr";
    public static final String STATUS_NAME = "statusName";
    public static final String STATUS = "status";
    public static final String SEASONAL = "seasonal";
    public static final String PRIMARY_CONTACT = "primaryContact";
    public static final String PRIMARY_CONTACT_ID = "primaryContactId";
    public static final String SECONDARY_CONTACT = "secondaryContact";
    public static final String SECONDARY_CONTACT_ID = "secondaryContactID";
    public static final String ORG_BUDGET = "orgBudget";
    public static final String ORG_BUDGET_ENDDATE = "orgBudgetEndDate";
    public static final String ORG_BUDGET_ID = "orgBudgetId";
    public static final String CONCATENATED_CONTENT = "concatenatedContent";
    public static final String DESCRIPTION = "description";
    public static final String CATEGORY_CODE = "categoryCode";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String ID = "id";
    public static final String CLASSNAME = "classname";
    public static final String HAZARDOUS = "hazardous";
    public static final String DISPENSE_UNIT = "dispenseUnit";
    public static final String MODEL = "model";
    public static final String MANUFACTURER = "manufacturer";
    public static final String MANUFACTURER_ID = "manufacturerId";
    public static final String VENDOR = "vendor";
    public static final String VENDOR_ID = "vendorId";
    public static final String ANNUAL_USAGE = "annualUsage";
    public static final String INDEX_NAME = "StockItemIndex";

    protected static final Analyzer analyzer = new StandardAnalyzer();
    protected static IndexWriter writer;
    protected final static Object mutex = new Object();
    protected final static Object mutex2 = new Object();

    protected static DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);//default to Hibernate
    protected static boolean indexExists = true;

    public static final DateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    private static File indexDirectory;
    private static File tempIndexDirectory;
    private static Boolean isIndexing=false;

    static {
        try {
//            tempIndexDirectory = new File(Config.getConfig(Constants.MATERIALS_MANAGMENT_CODE, Constants.TEMP_STOCK_ITEM_INDEX_PATH_CODE));
//            indexDirectory = new File(Config.getConfig(Constants.MATERIALS_MANAGMENT_CODE, Constants.STOCK_ITEM_INDEX_PATH_CODE));
            String idxName = "stockItemIndex";
            String envDir = System.getProperty("PRISM_LUCENE") == null ? System.getenv("PRISM_LUCENE") : System.getProperty("PRISM_LUCENE");
            indexDirectory = new File(envDir+"/" + idxName);
            tempIndexDirectory = new File(envDir+"/temp/"+idxName);
        }
        catch (Exception e) {
//        catch (InterruptedException e) {
            log.error("Error in StockItemIndex static block", e);
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
                log.debug("Deleted StockItemIndex: " + delete);
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

//            if (!index.exists() || (index.listFiles((new FilenameFilter("INDEXING")).length > 0) || (index.listFiles().length <= 2)) {       //directory does not exist, is empty or only has initial 2 files
            if (indexWasInterrupted || !index.exists() || (index.listFiles().length <= 2)) {       //directory does not exist, is empty or only has initial 2 files
                log.warn(INDEX_NAME + " NOT found, Initializing directory, a batch reindex will be scheduled and will begin shortly.");
                indexExists = false;
                index.mkdirs();
                /*
                Recall that a thread cannot acquire a lock owned by another thread. But a thread can acquire
                 a lock that it already owns. Allowing a thread to acquire the same lock more than once enables
                 reentrant synchronization. This describes a situation where synchronized code, directly or
                 indirectly, invokes a method that also contains synchronized code, and both sets of code use
                 the same lock. Without reentrant synchronization, synchronized code would have to take many
                 additional precautions to avoid having a thread cause itself to block.
                 */
                synchronized (mutex) {
                    writer = new IndexWriter(index, analyzer, true);
                    writer.close();
                }
/*
                ItemDAO itemDAO = daoFactory.getStockItemDAO();
                Collection items = itemDAO.findAll();
                StockItemIndex indexer = new StockItemIndex();
                for (Object item1 : items) {
                    StockItem item = (StockItem) item1;
                    log.debug("StockItem: " + item.toString() + ", Class:" + item.getClass().toString());
                    try {
                        indexer.add(item);
                    } catch (InfrastructureException e) {
                        log.error("Error in StockItemIndex.create() for the SI:" + item.getItemId(), e);
                    }
                }
*/
//                createIndexAtRuntime();
            }
/*
            else if (index.listFiles().length == 0) {     //directory exists, but is empty
                synchronized (mutex) {
                    writer = new IndexWriter(index, analyzer, true);
                    writer.close();
                }
//                createIndexAtRuntime();
            }
*/
        }
        catch (Exception e) {
            log.error("Error in StockItemIndex.create()", e);
        }
   }

    public static void createIndexAtRuntime() throws InfrastructureException {
        synchronized (mutex2) {
            if (isIndexing) {
                log.info("A " + INDEX_NAME + " index operation is already running.");
                return;    //An indexing operation is currently in progress
            }
            isIndexing = true;
        }

        try {
            File f = new File(indexDirectory,"INDEXING");
            Boolean abortIndex = false;
            f.createNewFile();
            List siIds = (List) new HibernateDAO().
                    executeQuery("select s.itemId from StockItem s order by s.itemId desc");
            List<List> paginatedList = CollectionUtils.paginateList(siIds, 500);
            int i = 0;
            Long maxSecondsUsed = new Long(0);
            Long secondsUsed ;
            for (List list : paginatedList) {
                Long beginTime = System.currentTimeMillis();
                log.info("    " + INDEX_NAME + ": Processing next batch: " + ++i + ", size: " + list.size() + ", beginning at " + new Date());
                indexSII(list,i);
                Long endTime = System.currentTimeMillis();
                secondsUsed = (endTime - beginTime)/1000;
                if (secondsUsed > maxSecondsUsed) {
                    maxSecondsUsed = secondsUsed;
                }
                log.info("    " + INDEX_NAME + ": Processing batch: " + i + ", ended at " + new Date() + ", Processing Time: " + secondsUsed + " seconds");
                log.info("");
                //This file is created by ... to end / abort this method 
                File f2 = new File(indexDirectory,"ABORT");
                if (f2.exists()) {
                    abortIndex = true;
                    f2.delete();
                    log.warn("     Aborting " + INDEX_NAME + " Indexing Operation.");
                    break;

                }
            }
            log.info("    " + INDEX_NAME + " Maximum Seconds Used for any batch was: " + maxSecondsUsed);
            HibernateUtil.commitTransaction();
            HibernateUtil.closeSession();
            if (!abortIndex) {
              indexExists = true;
              f.delete();
            }
        }
        catch (Exception e) {
            log.error("    " + INDEX_NAME + " threw Exception, Indexing Aborted: " + new Date());
            e.printStackTrace();
        }
        finally {
//            log.error("   StockItemIndex threw Exception, Indexing Aborted: " + new Date());
            synchronized (mutex) {
                    isIndexing = false;
            }
        }
    }

    private static void indexSII(Collection identifiers,int i) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        HibernateDAO hibernateDAO = new HibernateDAO();
        hibernateDAO.addQueryParam("siIds", identifiers);
        Collection stockItems =
                hibernateDAO.executeQuery("select s from StockItem s " +
                        "where s.itemId in (:siIds)");
//        hibernateDAO.executeQuery("select r from RequestLineItem r " +
//                        "where r.requestLineItemId in (:rliIds)");
        log.info("    " + INDEX_NAME + ": Finished hibernateDAO.executeQuery, beginning index of batch: " + i + " at " +  new Date());
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        Transaction tx = fullTextSession.beginTransaction();
        for (Object stockItem : stockItems) {
            fullTextSession.index(stockItem);
        }
        tx.commit();
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();
    }


    public static void createIndexAtRuntime_() throws ParseException, IOException, InfrastructureException {


        ArrayList<Long> ids = new ArrayList<Long>();
        StockItemIndex index = new StockItemIndex();
//        1. get database ids
        Collection items = daoFactory.getStockItemDAO().findAll();
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            StockItem StockItem = (StockItem) iterator.next();
            ids.add(StockItem.getItemId());
        }

//        2. commit transaction and clear session
        try {
            HibernateUtil.getSession().clear();
            HibernateUtil.commitTransaction();
        } catch (InfrastructureException e) {
            e.printStackTrace();
        }

        StockItemDAO StockItemDAO = daoFactory.getStockItemDAO();
//        3. iterate and update the indexes
        updateIndex(ids, index, StockItemDAO);

//        4. get the index ids
        final IndexReader reader = IndexReader.open(indexDirectory);
        final IndexSearcher searcher = new IndexSearcher(reader);

        String queryString = CLASSNAME + ":us*";
        Query query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(queryString);
        final Hits hits = searcher.search(query);
        for (int i = 0; i < hits.length(); i++) {
            final Document doc = hits.doc(i);
            Long id = new Long(doc.get(ID));
            ids.add(id);
        }
        searcher.close();
        reader.close();

        //delete the unexisting records
        deleteFromIndex(ids, index, StockItemDAO);
    }

    public static void createIndexAtRuntime2() throws ParseException, IOException, InfrastructureException {


        ArrayList<Long> ids = new ArrayList<Long>();
        StockItemIndex index = new StockItemIndex();
        ids.add(new Long(210162));

        StockItemDAO StockItemDAO = daoFactory.getStockItemDAO();
//        3. iterate and update the indexes
        updateIndex(ids, index, StockItemDAO);
    }

    private static void updateIndex(ArrayList iDs, StockItemIndex index, StockItemDAO StockItemDAO) {
        /*
        for (Iterator iterator = iDs.iterator(); iterator.hasNext();) {
            Long id = (Long) iterator.next();
            StockItem StockItem = null;
            try {
                StockItem = (StockItem) StockItemDAO.getItemById(id, false);
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
            try {
                if (StockItem != null) {
                    index.dropAndAdd(StockItem);
                } else {
                    index.drop(StockItem);
                }
                HibernateUtil.getSession().clear();
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
        }
        */
    }

    private static void deleteFromIndex(ArrayList iDs, StockItemIndex index, StockItemDAO StockItemDAO) {
        /*
        for (Iterator iterator = iDs.iterator(); iterator.hasNext();) {
            Long id = (Long) iterator.next();
            StockItem stockItem = null;
            try {
                stockItem = StockItemDAO.getStockItemById(id, false);
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
            try {
                if (stockItem == null) {
                    stockItem = new StockItem();
                    stockItem.setItemId(id);
                    index.drop(stockItem);
                }
                HibernateUtil.getSession().clear();
            } catch (InfrastructureException e) {
                log.error("StockItem:" + id + " was not found on the DB");
            }
        }
        */
    }


    public StockItemIndex() {
    }

    public void dropAndAdd(Searchable entity) throws InfrastructureException {
        synchronized (mutex) {
            drop(entity);
            add(entity);
        }
    }

    public void add(Searchable entity) throws InfrastructureException {
        StockItem item = (StockItem) entity;
        if (item.getPotentialIndicator().booleanValue()) {
            return;
        }
        File file = getCurrentIndexDirectory();
        create(file);
        final Document document = new Document();
        log.debug("Entity: " + entity.toString() + ", Class:" + entity.getClass().toString());

        try {
            StringBuffer concatenatedFields = new StringBuffer();
            if (item.getIcnbr() != null && !item.getPotentialIndicator().booleanValue()) {
                String icnbrString = StringUtils.formatStringNumber(item.getIcnbr().toString(), 4);
                document.add((Fieldable) new Field(ICNBR, icnbrString, Field.Store.YES, Field.Index.TOKENIZED));
                String fullIcnbrString = item.getCategory().getCategoryCode() + "-" + icnbrString;
                String fullIcnbrStringWithoudDash = item.getCategory().getCategoryCode() + icnbrString;
                document.add((Fieldable) new Field(FULL_ICNBR, fullIcnbrString, Field.Store.YES, Field.Index.TOKENIZED));
                document.add((Fieldable) new Field(ID, item.getItemId().toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
                document.add((Fieldable) new Field(CLASSNAME, item.getClass().getName(), Field.Store.YES, Field.Index.TOKENIZED));

                if (item.getDescription() != null) {
                    Field description = new Field(DESCRIPTION, item.getDescription(), Field.Store.YES, Field.Index.TOKENIZED);
                    description.setBoost(2.3f);
                    document.add((Fieldable) description);
                    concatenatedFields.append(item.getDescription()).append(" ");
                }

                if (item.getCategory() != null) {
                    document.add((Fieldable) new Field(CATEGORY_CODE, item.getCategory().getCategoryCode(), Field.Store.YES, Field.Index.TOKENIZED));
                    document.add((Fieldable) new Field(CATEGORY_NAME, item.getCategory().getName(), Field.Store.YES, Field.Index.TOKENIZED));
                    concatenatedFields.append(item.getCategory().getName()).append(" ");
                }
                if (item.getManufacturer() != null) {
                    Manufacturer mftr = daoFactory.getManufacturerDAO().getManufacturerById(item.getManufacturer().getManufacturerId(), false);
                    document.add((Fieldable) new Field(MANUFACTURER, mftr.getExternalOrgDetail().getOrgName(), Field.Store.YES, Field.Index.TOKENIZED));
                    document.add((Fieldable) new Field(MANUFACTURER_ID, mftr.getManufacturerId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
                    concatenatedFields.append(mftr.getExternalOrgDetail().getOrgName()).append(" ");
                }
                if (item.getHazardous() != null) {
                    document.add((Fieldable) new Field(HAZARDOUS, item.getHazardous().toString(), Field.Store.YES, Field.Index.TOKENIZED));
                }

                if (item.getPrimaryContact() != null) {
                    Field primaryContact = new Field(PRIMARY_CONTACT, daoFactory.getPersonDAO().getPersonById(item.getPrimaryContact().getPersonId(), false).getFirstAndLastName(), Field.Store.YES, Field.Index.TOKENIZED);
                    document.add((Fieldable) primaryContact);
                    document.add((Fieldable) new Field(PRIMARY_CONTACT_ID, item.getPrimaryContact().getPersonId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
                }
                if (item.getSecondaryContact() != null) {
                    Field secondaryContact = new Field(SECONDARY_CONTACT, daoFactory.getPersonDAO().getPersonById(item.getSecondaryContact().getPersonId(), false).getFirstAndLastName(), Field.Store.YES, Field.Index.TOKENIZED);
                    document.add((Fieldable) secondaryContact);
                    document.add((Fieldable) new Field(SECONDARY_CONTACT_ID, item.getSecondaryContact().getPersonId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
                }
                if (item.getOrgBudget() != null) {
                    Long ordBdgtId = item.getOrgBudget().getOrgBudgetId();
                    OrgBudget orgBdgt = daoFactory.getOrgBudgetDAO().getOrgBudgetById(ordBdgtId, false);
                    document.add((Fieldable) new Field(ORG_BUDGET, orgBdgt.getOrgBudgetCodeAndName(), Field.Store.YES, Field.Index.NO));
                    document.add((Fieldable) new Field(ORG_BUDGET_ENDDATE, DateUtils.toString(orgBdgt.getEndDate(), "yyyyMMdd"), Field.Store.YES, Field.Index.NO));

                    document.add((Fieldable) new Field(ORG_BUDGET_ID, ordBdgtId.toString(), Field.Store.YES, Field.Index.TOKENIZED));
                    concatenatedFields.append(orgBdgt.getOrgBudgetCodeAndName()).append(" ");
                }
                if (item.getStatus() != null) {
                    Status status = item.getStatus();
                    status = daoFactory.getStatusDAO().getStatusById(status.getStatusId(), false);
                    document.add((Fieldable) new Field(STATUS, status.getStatusCode(), Field.Store.YES, Field.Index.TOKENIZED));
                    document.add((Fieldable) new Field(STATUS_NAME, status.getName(), Field.Store.YES, Field.Index.TOKENIZED));
                    concatenatedFields.append(status.getName()).append(" ");
                }
                if (item.getDispenseUnit() != null) {
                    document.add((Fieldable) new Field(DISPENSE_UNIT, item.getDispenseUnit().getName(), Field.Store.YES, Field.Index.TOKENIZED));
                }
                if (item.getItemVendors() != null) {
                    Iterator iter = item.getItemVendors().iterator();
                    StringBuffer vendorBuffer = new StringBuffer();
                    StringBuffer vendorBufferIds = new StringBuffer();
                    while (iter.hasNext()) {
                        ItemVendor itemVendor = (ItemVendor) iter.next();
                        Vendor vendor = itemVendor.getVendor();
                        vendor = daoFactory.getVendorDAO().getVendorById(vendor.getVendorId(), false);
                        vendorBuffer.append(vendor.getExternalOrgDetail().getOrgName()).append(", ");
                        vendorBufferIds.append(vendor.getVendorId()).append(" ");
                    }
                    document.add((Fieldable) new Field(VENDOR, vendorBuffer.toString(), Field.Store.YES, Field.Index.TOKENIZED));
                    document.add((Fieldable) new Field(VENDOR_ID, vendorBufferIds.toString(), Field.Store.YES, Field.Index.TOKENIZED));
                    concatenatedFields.append(vendorBuffer.toString());
                }
                if (item.getSeasonal() != null) {
                    document.add((Fieldable) new Field(SEASONAL, item.getSeasonal().toString(), Field.Store.YES, Field.Index.TOKENIZED));
                }
                if (icnbrString != null) {
                    concatenatedFields.append(icnbrString).append(" ");
                    concatenatedFields.append(fullIcnbrString).append(" ");
                    concatenatedFields.append(fullIcnbrStringWithoudDash).append(" ");
                }
                if (item.getPrimaryContact() != null) {
                    Person person = item.getPrimaryContact();
                    person = daoFactory.getPersonDAO().getPersonById(person.getPersonId(), false);
                    concatenatedFields.append(person.getFirstAndLastName()).append(" ");
                }
                if (item.getSecondaryContact() != null) {
                    Person person = item.getSecondaryContact();
                    person = daoFactory.getPersonDAO().getPersonById(person.getPersonId(), false);
                    concatenatedFields.append(person.getFirstAndLastName()).append(" ");
                }
                if (item.getAsstDivDirector() != null) {
                    Person asstDivDirector = item.getAsstDivDirector();
                    asstDivDirector = daoFactory.getPersonDAO().getPersonById(asstDivDirector.getPersonId(), false);
                    concatenatedFields.append(asstDivDirector.getFirstAndLastName()).append(" ");
                }
                document.add((Fieldable) new Field(CONCATENATED_CONTENT, concatenatedFields.toString(), Field.Store.YES, Field.Index.TOKENIZED));
            }
        }
        catch (Exception e) {
            log.error("Error in StockItemIndex.add() , SI#:" + item.getItemId(), e);
            throw new InfrastructureException("StockItemIndex.add(): ", e);
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
            log.error("Error in StockItemIndex.add2() , SI#:" + item.getItemId(), e);
            throw new InfrastructureException(e.getMessage(), e);
        }
    }

    public void drop(Searchable entity) throws InfrastructureException {
        StockItem item = (StockItem) entity;
        if (item.getPotentialIndicator().booleanValue()) {
            return;
        }
        File file = getCurrentIndexDirectory();
        create(file);
        try {
            synchronized (mutex) {
                dropHandler(file, StockItemIndex.ID, item.getItemId().toString(), log);
            }
//                final IndexReader reader = IndexReader.open(file);
//                try {
//                    Term term = new Term(StockItemIndex.ID, item.getItemId().toString());
//                    reader.deleteDocuments(term);
//                } finally {
//                    reader.close();
//                }
//            }
        }
        catch (IOException e) {
            log.error("Error in StockItemIndex.drop() , SI#:" + item.getItemId(), e);
            throw new InfrastructureException(e.getMessage(), e);
        }
    }

    /**
     * @param query
     * @return a Collection of Items
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Collection search(Query query) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        //create(file);
        final ArrayList<StockItem> items = new ArrayList<StockItem>();
        try {
            final IndexReader reader = IndexReader.open(file);
            final IndexSearcher searcher = new IndexSearcher(reader);
            String qs = query.toString();
            log.debug("Search StockItemIndex: " + qs);
            query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(qs);
            final Hits hits = searcher.search(query);
            for (int i = 0; i < hits.length(); i++) {
                final Document doc = hits.doc(i);
                if (doc.get(ICNBR) == null) {
                    continue;
                }
                StockItem item = new StockItem();
                item.setItemId(new Long(doc.get(ID)));
                item.setAnnualUsage(new Integer(doc.get(ANNUAL_USAGE)));
                Category cat = new Category();
                cat.setName(doc.get(CATEGORY_NAME));
                cat.setCategoryCode(doc.get(CATEGORY_CODE));
                item.setCategory(cat);
                item.setModel(doc.get(MODEL));
                Manufacturer manufacturer = new Manufacturer();
                ExternalOrgDetail extOrgDetail = new ExternalOrgDetail();
                extOrgDetail.setOrgName(doc.get(MANUFACTURER));
                manufacturer.setExternalOrgDetail(extOrgDetail);
                item.setManufacturer(manufacturer);
                item.setDescription(doc.get(DESCRIPTION));
                Unit dispenseUnit = new Unit();
                dispenseUnit.setName(doc.get(DISPENSE_UNIT));
                item.setDispenseUnit(dispenseUnit);
                item.setHazardous(new Boolean(doc.get(HAZARDOUS)));
                Person primaryContact = new Person();
                StringTokenizer tokenizer = new StringTokenizer(doc.get(PRIMARY_CONTACT));
                if (tokenizer.countTokens() == 2) {
                    primaryContact.setFirstName(tokenizer.nextToken());
                    primaryContact.setLastName(tokenizer.nextToken());
                } else {
                    primaryContact.setFirstName(tokenizer.nextToken());
                    primaryContact.setMiddleName(tokenizer.nextToken());
                    primaryContact.setLastName(tokenizer.nextToken());
                }
                item.setPrimaryContact(primaryContact);
                Person secondaryContact = new Person();
                tokenizer = new StringTokenizer(doc.get(SECONDARY_CONTACT));
                if (tokenizer.countTokens() == 2) {
                    secondaryContact.setFirstName(tokenizer.nextToken());
                    secondaryContact.setLastName(tokenizer.nextToken());
                } else {
                    secondaryContact.setFirstName(tokenizer.nextToken());
                    secondaryContact.setMiddleName(tokenizer.nextToken());
                    secondaryContact.setLastName(tokenizer.nextToken());
                }
                item.setSecondaryContact(secondaryContact);
                item.setIcnbr(new Integer(doc.get(ICNBR)));
                String orgBdgt = doc.get(ORG_BUDGET);
                OrgBudget orgBudget = new OrgBudget();
                orgBudget.setOrgBudgetCode(orgBdgt.substring(0, orgBdgt.indexOf(" ")));
                orgBudget.setName(orgBdgt.substring(orgBdgt.indexOf(" ")));
                String orgBudgetEndDate = doc.get(ORG_BUDGET_ENDDATE);
                Date endDate = DateUtils.createDate(orgBudgetEndDate, "yyyyMMdd");
                orgBudget.setEndDate(endDate);
                item.setOrgBudget(orgBudget);
                Status status = new Status();
                status.setName(doc.get(STATUS_NAME));
                status.setStatusCode(doc.get(STATUS));
                item.setStatus(status);

                items.add(item);
            }
            searcher.close();
            reader.close();
        }
        catch (IOException e) {
            log.error("1 Error in StockItemIndex.search(Query)", e);
            throw new InfrastructureException("StockItemIndex.search(): " + e.getMessage(), e);
        }
        catch (java.text.ParseException e) {
            log.error("2 Error in StockItemIndex.search(Query)", e);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return items;
    }

    /**
     * @param query
     * @return a collection of Long objects which are the ids of the result objects
     * @throws InfrastructureException
     */
    public Collection searchIds(Query query) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        //create(file);
        final ArrayList<Long> itemIds = new ArrayList<Long>();
        try {
            final IndexReader reader = IndexReader.open(file);
            final IndexSearcher searcher = new IndexSearcher(reader);
            String qs = query.toString();
            log.debug("Search StockItemIndex: " + qs);
            query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(qs);
            final Hits hits = searcher.search(query);
            for (int i = 0; i < hits.length(); i++) {
                final Document doc = hits.doc(i);
                if (doc.get(ICNBR) == null) {
                    continue;
                }
                Long itemId = new Long(doc.get(ID));
                itemIds.add(itemId);
            }
            searcher.close();
            reader.close();
        }
        catch (IOException e) {
            log.error("Error in StockItemIndex.searchIds(Query)", e);
            throw new InfrastructureException("StockItemIndex.search(): " + e.getMessage(), e);
        } catch (ParseException e) {
            log.error("Error in StockItemIndex.searchIds(Query)", e);
            throw new InfrastructureException("StockItemIndex.search(): " + e.getMessage(), e);
        }

        return itemIds;
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