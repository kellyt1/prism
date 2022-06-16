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
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.io.IOUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.lucene.LuceneUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.dao.inventory.ItemDAO;
import us.mn.state.health.dao.materialsrequest.PurchaseItemDAO;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.model.common.Manufacturer;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.PurchaseItem;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.persistence.HibernateUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class PurchaseItemIndex extends EntityIndex {
    private static Log log = LogFactory.getLog(PurchaseItemIndex.class);
    public static String CONCATENATED_CONTENT = "concatenatedContent";
    public static String DESCRIPTION = "description";
    public static String CATEGORY_CODE = "categoryCode";
    public static String CATEGORY_NAME = "categoryName";
    public static String ID = "id";
    public static String CLASSNAME = "classname";
    public static String AVAILABILITY = "availability";
    public static String HAZARDOUS = "hazardous";
    public static String EOQ = "economicOrderQty";
    public static String DISPENSE_UNIT = "dispenseUnit";
    public static String ANNUAL_USAGE = "annualUsage";
    public static String MODEL = "model";
    public static String MANUFACTURER = "manufacturer";
    public static String MANUFACTURER_ID = "manufacturerId";
    public static String VENDOR = "vendor";
    public static String VENDOR_ID = "vendorId";

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
            String idxName = "purchaseItemIndex";
            String envDir = System.getProperty("PRISM_LUCENE") == null ? System.getenv("PRISM_LUCENE") : System.getProperty("PRISM_LUCENE");
            indexDirectory = new File(envDir + "/" + idxName);
            log.info("PurchaseItemIndexDirectory set as ${PRISM_LUCENE}/purchaseItemIndex.  Server resolved index as: " + indexDirectory.getAbsolutePath());
            tempIndexDirectory = new File(envDir+"/temp/"+idxName);
        }
        catch (Exception e) {
            log.error("Error in PurchaseItemIndex in the static block ", e);
        }
    }

    public static void createIndex(boolean newIndex) {
        synchronized (mutex) {
            File index = getCurrentIndexDirectory();
            if (newIndex) {
                boolean delete = IOUtils.deleteDir(index);
                log.debug("Deleted PurchaseItemIndex: " + delete);
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
            if (indexWasInterrupted || !index.exists() || (index.listFiles().length <= 2)) {       //directory does not exist, is empty or only has initial 2 files
//                System.out.println("PurchaseItemIndex NOT found, Initializing ..., This will take a few minutes and then PRISM will be available.");
                log.info("PurchaseItemIndex NOT found, Initializing ..., This will take a few minutes and then PRISM will be available.");
                indexExists = false;
                log.info("Making PurchaseItemIndex directory at: " + indexDirectory.getAbsolutePath());
                index.mkdirs();
                synchronized (mutex) {
                    writer = new IndexWriter(index, analyzer, true);
                    writer.close();
                    log.info("PurchaseItemIndex directory created.");
                }
            }
        }
        catch (Exception e) {
            log.error("Error in PurchaseItemIndex.create()", e);
        }
    }

    public static void createIndex2(boolean newIndex) {
        synchronized (mutex) {
            File index = getCurrentIndexDirectory();
            if (newIndex) {
                boolean delete = IOUtils.deleteDir(index);
                log.debug("Deleted PurchaseItemIndex: " + delete);
            }
            create2(index);
        }
    }

    private static void create2(File index) {
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
            if (indexWasInterrupted || !index.exists() || (index.listFiles().length <= 2)) {       //directory does not exist, is empty or only has initial 2 files
                log.info("PurchaseItemIndex NOT found, Initializing ..., This will take a few minutes and then PRISM will be available.");
                indexExists = false;
                index.mkdirs();
                synchronized (mutex) {
                    writer = new IndexWriter(index, analyzer, true);
                    writer.close();
                }
                File f = new File(indexDirectory,"INDEXING");
                f.createNewFile();

                ItemDAO itemDAO = daoFactory.getPurchaseItemDAO();
                Collection items = itemDAO.findAll();
                PurchaseItemIndex indexer = new PurchaseItemIndex();
                for (Object item1 : items) {
                    PurchaseItem item = (PurchaseItem) item1;
                    try {
                        indexer.add(item);
                    } catch (InfrastructureException e) {
                        log.error("Error in PurchaseItemIndex.create() for the Item:" + item.getItemId(), e);
                    }
                }
                indexExists = true;
                f.delete();
            }
        }
        catch (Exception e) {
            log.error("Error in PurchaseItemIndex.create()", e);
        }
    }


    public static void createIndexAtRuntime() throws ParseException, IOException, InfrastructureException {
        ArrayList ids = new ArrayList();
        PurchaseItemIndex index = new PurchaseItemIndex();
        File f = new File(indexDirectory,"INDEXING");
        f.createNewFile();

//        1. get database ids
        Collection items = daoFactory.getPurchaseItemDAO().findAll();
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            PurchaseItem purchaseItem = (PurchaseItem) iterator.next();
            ids.add(purchaseItem.getItemId());
        }

//        2. commit transaction and clear session
        try {
            HibernateUtil.getSession().clear();
            HibernateUtil.commitTransaction();
        } catch (InfrastructureException e) {
            e.printStackTrace();
        }

        PurchaseItemDAO purchaseItemDAO = daoFactory.getPurchaseItemDAO();
//        3. iterate and update the indexes
        updateIndex(ids, index, purchaseItemDAO);

//        4. get the index ids
        final IndexReader reader = IndexReader.open(indexDirectory);
        final IndexSearcher searcher = new IndexSearcher(reader);

        String queryString = CLASSNAME + ":us*";
        Query query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(queryString);

        query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(queryString);
        final Hits hits = searcher.search(query);
        for (int i = 0; i < hits.length(); i++) {
            final Document doc = hits.doc(i);
            Long id = new Long(doc.get(ID));
            ids.add(id);
        }
        searcher.close();
        reader.close();

        //delete the unexisting records
        deleteFromIndex(ids, index, purchaseItemDAO);
        indexExists = true;
        f.delete();
    }

    private static void updateIndex(ArrayList iDs, PurchaseItemIndex index, PurchaseItemDAO purchaseItemDAO) {
        for (Iterator iterator = iDs.iterator(); iterator.hasNext();) {
            Long id = (Long) iterator.next();
            PurchaseItem purchaseItem = null;
            try {
                purchaseItem = (PurchaseItem) purchaseItemDAO.getItemById(id, false);
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
            try {
                if (purchaseItem != null) {
                    index.dropAndAdd(purchaseItem);
                } else {
                    index.drop(purchaseItem);
                }
                HibernateUtil.getSession().clear();
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
        }
    }

    private static void deleteFromIndex(ArrayList iDs, PurchaseItemIndex index, PurchaseItemDAO purchaseItemDAO) {
        for (Iterator iterator = iDs.iterator(); iterator.hasNext();) {
            Long id = (Long) iterator.next();
            PurchaseItem purchaseItem = null;
            try {
                purchaseItem = purchaseItemDAO.findPurchaseItemById(id, false);
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
            try {
                if (purchaseItem == null) {
                    purchaseItem = new PurchaseItem();
                    purchaseItem.setItemId(id);
                    index.drop(purchaseItem);
                }
                HibernateUtil.getSession().clear();
            } catch (InfrastructureException e) {
                log.error("PurchaseItem:" + id + " was not found on the DB");
            }
        }
    }


    public PurchaseItemIndex() {
    }

    public void dropAndAdd(Searchable entity) throws InfrastructureException {
        synchronized (mutex) {
            drop(entity);
            add(entity);
        }
    }

    public void add(Searchable entity) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        if ( !file.exists()) {
            create(file);
        }
        final Document document = new Document();
        StringBuffer concatenatedFields = new StringBuffer();
        PurchaseItem item = (PurchaseItem) entity;
        document.add((Fieldable) new Field(ID, item.getItemId().toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        document.add((Fieldable) new Field(CLASSNAME, item.getClass().getName(), Field.Store.YES, Field.Index.TOKENIZED));
        Field description = new Field(DESCRIPTION, item.getDescription(), Field.Store.YES, Field.Index.TOKENIZED);
        description.setBoost(2.3f);
        document.add((Fieldable) description);
        concatenatedFields.append(item.getDescription()).append(" ");
        if (item.getCategory() != null) {
            document.add((Fieldable) new Field(CATEGORY_CODE, item.getCategory().getCategoryCode(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(CATEGORY_NAME, item.getCategory().getName(), Field.Store.YES, Field.Index.TOKENIZED));
            concatenatedFields.append(item.getCategory().getName()).append(" ");
        }
        if (item.getModel() != null) {
            document.add((Fieldable) new Field(MODEL, item.getModel(), Field.Store.YES, Field.Index.TOKENIZED));
        }
        if (item.getManufacturer() != null) {
            Manufacturer mftr = daoFactory.getManufacturerDAO().getManufacturerById(item.getManufacturer().getManufacturerId(), false);
            document.add((Fieldable) new Field(MANUFACTURER, mftr.getExternalOrgDetail().getOrgName(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(MANUFACTURER_ID, mftr.getManufacturerId().toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
            concatenatedFields.append(mftr.getExternalOrgDetail().getOrgName()).append(" ");
        }
        if (item.getHazardous() != null) {
            document.add((Fieldable) new Field(HAZARDOUS, item.getHazardous().toString(), Field.Store.YES, Field.Index.TOKENIZED));
        }
        if (item.getEconomicOrderQty() != null) {
            document.add((Fieldable) new Field(EOQ, item.getEconomicOrderQty().toString(), Field.Store.YES, Field.Index.NO));
        }
        if (item.getDispenseUnit() != null) {
            document.add((Fieldable) new Field(DISPENSE_UNIT, item.getDispenseUnit().getName(), Field.Store.YES, Field.Index.TOKENIZED));
        }
        if (item.getAnnualUsage() != null) {
            document.add((Fieldable) new Field(ANNUAL_USAGE, item.getAnnualUsage().toString(), Field.Store.YES, Field.Index.NO));
        } else {
            document.add((Fieldable) new Field(ANNUAL_USAGE, "0", Field.Store.YES, Field.Index.NO));
        }
        if (item.getItemVendors() != null) {
            Iterator iter = item.getItemVendors().iterator();
            StringBuffer vendorBuffer = new StringBuffer();
            StringBuffer vendorBufferIds = new StringBuffer();
            while (iter.hasNext()) {
                ItemVendor itemVendor = (ItemVendor) iter.next();
                Vendor vendor = itemVendor.getVendor();
                vendorBuffer.append(vendor.getExternalOrgDetail().getOrgName()).append(", ");
                vendorBufferIds.append(vendor.getVendorId()).append(" ");
            }
            document.add((Fieldable) new Field(VENDOR, vendorBuffer.toString(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(VENDOR_ID, vendorBufferIds.toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
            concatenatedFields.append(vendorBuffer.toString());
        }

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
            }
        }
        catch (IOException e) {
            log.error("Error in PurchaseItemIndex.add(), purchaseItem#:" + item.getItemId(), e);
            throw new InfrastructureException(e.getMessage(), e);
        }
    }

    public void drop(Searchable entity) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        if ( !file.exists()) {
            create(file);
        }
        PurchaseItem item = (PurchaseItem) entity;
        try {
            synchronized (mutex) {
                dropHandler(file, PurchaseItemIndex.ID, item.getItemId().toString(), log);
            }
        }
        catch (IOException e) {
            log.error("Error in PurchaseItemIndex.drop(), purchaseItem#:" + item.getItemId(), e);
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
     * @return a Collection of Items (will return only the Purchase Items)
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Collection search(Query query) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        //create(file);
        final ArrayList items = new ArrayList();
        try {
            final IndexReader reader = IndexReader.open(file);
            final IndexSearcher searcher = new IndexSearcher(reader);
            String qs = query.toString();
            log.debug("Search PurchaseItemIndex: " + qs);
            query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(qs);
            final Hits hits = searcher.search(query);
            for (int i = 0; i < hits.length(); i++) {
                final Document doc = hits.doc(i);
                Item item = new PurchaseItem();
                item.setItemId(new Long(doc.get(ID)));
                item.setAnnualUsage(new Integer(doc.get(ANNUAL_USAGE)));
                Category cat = new Category();
                cat.setName(doc.get(CATEGORY_NAME));
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
                item.setEconomicOrderQty(new Integer(doc.get(EOQ)));
                item.setHazardous(new Boolean(doc.get(HAZARDOUS)));
                items.add(item);
            }
            searcher.close();
            reader.close();
        } catch (IOException e) {
            log.error("Error in PurchaseItemIndex.search(Query query)", e);
            throw new InfrastructureException("PurchaseItemIndex.search(): " + e.getMessage(), e);
        } catch (ParseException e) {
            log.error("Error in PurchaseItemIndex.search2(Query query)", e);
            throw new InfrastructureException("StockItemIndex.search(): " + e.getMessage(), e);
        }

        return items;
    }

    public Collection<Long> searchIds(Query query) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        //create(file);
        final ArrayList<Long> itemIds = new ArrayList<Long>();
        try {
            final IndexReader reader = IndexReader.open(file);
            final IndexSearcher searcher = new IndexSearcher(reader);
            String qs = query.toString();
            log.debug("Search PurchaseItemIndex: " + qs);
            query = new QueryParser(CONCATENATED_CONTENT, analyzer).parse(qs);
            final Hits hits = searcher.search(query);
            for (int i = 0; i < hits.length(); i++) {
                final Document doc = hits.doc(i);
                Long stockItemId = new Long(doc.get(ID));
                itemIds.add(stockItemId);
            }
            searcher.close();
            reader.close();
        } catch (IOException e) {
            log.error("Error in PurchaseItemIndex.searchIds(Query query)", e);
            throw new InfrastructureException("PurchaseItemIndex.searchIds(Query query): " + e.getMessage(), e);
        } catch (ParseException e) {
            log.error("Error in PurchaseItemIndex.searchIds2(Query query)", e);
            throw new InfrastructureException("StockItemIndex.search(): " + e.getMessage(), e);
        }

        return itemIds;
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