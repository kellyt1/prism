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
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.io.IOUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.inventory.FixedAssetDAO;
import us.mn.state.health.dao.inventory.ItemDAO;
import us.mn.state.health.dao.inventory.SensitiveAssetDAO;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.*;
import us.mn.state.health.model.inventory.*;
import us.mn.state.health.persistence.HibernateUtil;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class AssetIndex extends EntityIndex {
    private static Log log = LogFactory.getLog(AssetIndex.class);
    public static String CONCATENATED_CONTENT = "concatenatedContent";
    public static String DESCRIPTION = "description";
    public static String CATEGORY_CODE = "categoryCode";
    public static String CATEGORY_NAME = "categoryName";
    public static String ID = "id";
    public static String CLASSNAME = "classname";
    public static String MODEL = "model";
    public static String MANUFACTURER_NAME = "manufacturerName";
    public static String MANUFACTURER_ID = "manufacturerId";
    public static String VENDOR_NAME = "vendorName";
    public static String VENDOR_ID = "vendorId";
    public static String STATUS_CODE = "statusCode";
    public static String ORG_BUDGET_IDS = "orgBudgetIds";
    public static String FACILITY_ID = "facilityId";
    public static String FACILITY_NAME = "facilityName";
    public static String CONTACT_PERSON_NAME = "contactPersonName";
    public static String CONTACT_PERSON_ID = "contactPersonId";
    public static String SERIAL_NUMBER = "serialNumber";
    public static String ASSET_NUMBER = "assetNumber";
    public static String CLASS_CODE = "classCode";
    public static String COST = "cost";
    public static String DATE_RECEIVED = "dateReceived";
    public static String MAINT_AGMT_EXP_DATE = "maintAgreementExpirationDate";
    public static String MAINT_AGMT_PO_NBR = "maintAgreementPONumber";
    public static String WARRANTY_EXP_DATE = "warrantyExpirationDate";
    public static String ORDER_NBR = "orderNbr";
    public static final String INDEX_NAME = "AssetIndex";

    protected static final Analyzer analyzer = new StandardAnalyzer();
    protected static IndexWriter writer;
    protected final static Object mutex = new Object();
    protected final static Object mutex2 = new Object();
   
    protected static DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE); //default to Hibernate
    protected static boolean indexExists = true;
    public static final DateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    private static File indexDirectory;
    private static File tempIndexDirectory;
    private static int optimizeCount = 0;
    private static Boolean isIndexing=false;

    static {
        try {
//            tempIndexDirectory = new File(Config.getConfig(Constants.MATERIALS_MANAGMENT_CODE, Constants.TEMP_ASSET_INDEX_PATH_CODE));
//            indexDirectory = new File(Config.getConfig(Constants.MATERIALS_MANAGMENT_CODE, Constants.ASSET_INDEX_PATH_CODE));
            String idxName = "assetIndex";
            String envDir = System.getProperty("PRISM_LUCENE") == null ? System.getenv("PRISM_LUCENE") : System.getProperty("PRISM_LUCENE");
            indexDirectory = new File(envDir + "/" + idxName);
            tempIndexDirectory = new File(envDir+"/temp/"+idxName);
        }
        catch (Exception e) {
//        catch (InterruptedException e) {
            log.error("Error in AssetIndex static block", e);
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
                log.debug("Deleted AssetIndex: " + delete);
            }
            create(index);
        }
    }

    public static void createIndexAtRuntime() throws InfrastructureException {
        synchronized (mutex2) {
            if (isIndexing) {
                log.warn("A " + INDEX_NAME + " index operation is already running.");
                return;    //An indexing operation is currently in progress
            }
            isIndexing = true;
            log.info(INDEX_NAME + " STARTED: " + new Date());
        }

        try {
            File f = new File(indexDirectory,"INDEXING");
            Boolean abortIndex = false;


//        ArrayList assetIds = new ArrayList();
//        AssetIndex index = new AssetIndex();
//        File f = new File(indexDirectory,"INDEXING");
//        f.createNewFile();
//        1. get database ids
//        Collection assetItems = daoFactory.getSensitiveAssetDAO().findAll(SensitiveAsset.class);

//            File f = new File(indexDirectory,"INDEXING");
            f.createNewFile();

            SensitiveAssetDAO saDAO = daoFactory.getSensitiveAssetDAO();
            FixedAssetDAO faDAO = daoFactory.getFixedAssetDAO();
            HashSet assetsSet = new HashSet();                              //using a Set to avoid adding duplicates
            assetsSet.addAll(faDAO.findAll(FixedAsset.class));
            assetsSet.addAll(saDAO.findAll(SensitiveAsset.class));
//                AssetIndex indexer = new AssetIndex();
            indexAssets(assetsSet);

            File f2 = new File(indexDirectory,"ABORT");
            if (f2.exists()) {
                abortIndex = true;
                f2.delete();
                log.error("     Aborting " + INDEX_NAME + " Indexing Operation.");
//                break;

            }
//        System.out.println("   "+INDEX_NAME+StockItemIndex Maximum Seconds Used for any batch was: " + maxSecondsUsed);
//        HibernateUtil.commitTransaction();
//        HibernateUtil.closeSession();
            if (!abortIndex) {
                indexExists = true;
                f.delete();
            }
            log.info(INDEX_NAME + " FINISHED: " + new Date());
    }
    catch (Exception e) {
        log.error("   " + INDEX_NAME + " threw Exception, Indexing Aborted: " + new Date());
        e.printStackTrace();
    }
    finally {
        synchronized (mutex) {
        isIndexing = false;
        }
    }



/*
        for (Iterator iterator = assetItems.iterator(); iterator.hasNext();) {
            SensitiveAsset asset = (SensitiveAsset) iterator.next();
            assetIds.add(asset.getSensitiveAssetId());
        }

//        2. commit transaction and clear session
        try {
            HibernateUtil.getSession().clear();
            HibernateUtil.commitTransaction();
        } catch (InfrastructureException e) {
            e.printStackTrace();
        }

//        3. iterate and update the indexes
        updateIndex(assetIds, index);

//        4. get the index ids
        final IndexReader reader = IndexReader.open(indexDirectory);
        final IndexSearcher searcher = new IndexSearcher(reader);

        String queryString = "classname:us*";
        QueryParser queryParser = new QueryParser(CONCATENATED_CONTENT, analyzer);
        Query query;
        query = queryParser.parse(queryString);

        final Hits hits = searcher.search(query);
        for (int i = 0; i < hits.length(); i++) {
            final Document doc = hits.doc(i);
            Long id = new Long(doc.get(ID));
            assetIds.add(id);
        }
        searcher.close();
        reader.close();

        //delete the unexisting records
        deleteFromIndex(assetIds, index);
*/
//        indexExists = true;
//        f.delete();
    }

    private static void updateIndex(ArrayList assetIDs, AssetIndex index) {
/*
        for (Iterator iterator = assetIDs.iterator(); iterator.hasNext();) {
            Long id = (Long) iterator.next();
            SensitiveAsset asset = null;
            try {
                asset = (SensitiveAsset) daoFactory.getSensitiveAssetDAO().getById(id, SensitiveAsset.class);
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
            try {
                if (asset != null) {
                    index.dropAndAdd(asset);
                } else {
                    index.drop(asset);
                }
                HibernateUtil.getSession().clear();
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
        }
*/
    }

    private static void deleteFromIndex(ArrayList assetIDs, AssetIndex index) {
/*
        for (Iterator iterator = assetIDs.iterator(); iterator.hasNext();) {
            Long id = (Long) iterator.next();
            SensitiveAsset sensitiveAsset = null;
            try {
                sensitiveAsset = (SensitiveAsset) daoFactory.getSensitiveAssetDAO().loadById(id, SensitiveAsset.class, false);
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
            try {
                if (sensitiveAsset == null) {
                    sensitiveAsset = new SensitiveAsset();
                    sensitiveAsset.setSensitiveAssetId(id);
                    index.drop(sensitiveAsset);
                }
                HibernateUtil.getSession().clear();
            } catch (InfrastructureException e) {
                log.error("Asset:" + id + " was not found on the DB");
            }
        }
*/
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
//            if (!index.exists() || (index.listFiles().length == 0)) {
//            if (!index.exists() || (index.listFiles().length <= 2)) {       //directory does not exist, is empty or only has initial 2 files
            if (indexWasInterrupted || !index.exists() || (index.listFiles().length <= 2)) {       //directory does not exist, is empty or only has initial 2 files
//                System.out.println("AssetIndex NOT found, Initializing ..., This will take a few minutes and then PRISM will be available.");
                log.warn(INDEX_NAME + " NOT found, Initializing directory, a batch reindex will be scheduled and will begin shortly.");
                indexExists = false;
                index.mkdirs();
                synchronized (mutex) {
                    writer = new IndexWriter(index, analyzer, true);
                    writer.close();
                }
/*
                File f = new File(indexDirectory,"INDEXING");
                f.createNewFile();

                SensitiveAssetDAO saDAO = daoFactory.getSensitiveAssetDAO();
                FixedAssetDAO faDAO = daoFactory.getFixedAssetDAO();
                HashSet assetsSet = new HashSet();                              //using a Set to avoid adding duplicates
                assetsSet.addAll(faDAO.findAll(FixedAsset.class));
                assetsSet.addAll(saDAO.findAll(SensitiveAsset.class));
//                AssetIndex indexer = new AssetIndex();
                indexAssets(assetsSet);

                indexExists = true;
                f.delete();
*/
            }
        }
        catch (Exception e) {
            log.error("Error in AssetIndex.create()", e);
        }
    }

    public AssetIndex() {
    }

//    private static void indexSII(Collection identifiers,int i) throws InfrastructureException {
    private static void indexAssets(Collection assetsSet) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        FullTextSession fullTextSession = Search.createFullTextSession(session);
        Transaction tx = fullTextSession.beginTransaction();
        Iterator iter = assetsSet.iterator();

        while (iter.hasNext()) {
            Object asset = iter.next();
            if (asset instanceof FixedAsset) {
                FixedAsset fixedAsset = (FixedAsset) asset;
                fullTextSession.index(fixedAsset);
            } else if (asset instanceof SensitiveAsset) {
                SensitiveAsset sensitiveAsset = (SensitiveAsset) asset;
                fullTextSession.index(sensitiveAsset);
            }
        }

        tx.commit();
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();

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
        SensitiveAsset asset = (SensitiveAsset) entity;
        ItemDAO itemDAO = daoFactory.getItemDAO();
        Item item = itemDAO.getItemById(asset.getItem().getItemId(), false);  //open a new session if need be

        document.add((Fieldable) new Field(ID, asset.getSensitiveAssetId().toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        document.add((Fieldable) new Field(CLASSNAME, asset.getClass().getName(), Field.Store.YES, Field.Index.TOKENIZED));
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
            document.add((Fieldable) new Field(MANUFACTURER_NAME, item.getManufacturer().getExternalOrgDetail().getOrgName(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(MANUFACTURER_ID, item.getManufacturer().getManufacturerId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            concatenatedFields.append(item.getManufacturer().getExternalOrgDetail().getOrgName()).append(" ");
        }
        if (asset.getVendor() != null) {
            Vendor vendor = daoFactory.getVendorDAO().getVendorById(asset.getVendor().getVendorId(), false);
/*
            document.add((Fieldable) new Field(VENDOR_NAME, asset.getVendor().getExternalOrgDetail().getOrgName(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(VENDOR_ID, asset.getVendor().getVendorId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            concatenatedFields.append(asset.getVendor().getExternalOrgDetail().getOrgName()).append(" ");
*/
            document.add((Fieldable) new Field(VENDOR_NAME, vendor.getExternalOrgDetail().getOrgName(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(VENDOR_ID, vendor.getVendorId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            concatenatedFields.append(vendor.getExternalOrgDetail().getOrgName()).append(" ");
        }
        if (asset.getFacility() != null) {
            document.add((Fieldable) new Field(FACILITY_ID, asset.getFacility().getFacilityId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(FACILITY_NAME, asset.getFacility().getFacilityName(), Field.Store.YES, Field.Index.TOKENIZED));
            concatenatedFields.append(asset.getFacility().getFacilityName()).append(" ");
        }
        if (asset.getOrderLineItem() != null) {
            document.add((Fieldable) new Field(ORDER_NBR, asset.getOrderLineItem().getOrder().getOrderId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            concatenatedFields.append(asset.getOrderLineItem().getOrder().getOrderId().toString()).append(" ");
        }
        if (asset.getSerialNumber() != null) {
            document.add((Fieldable) new Field(SERIAL_NUMBER, asset.getSerialNumber(), Field.Store.YES, Field.Index.TOKENIZED));
            concatenatedFields.append(asset.getSerialNumber()).append(" ");
        }
        if (asset.getStatus() != null) {
            document.add((Fieldable) new Field(STATUS_CODE, asset.getStatus().getStatusCode(), Field.Store.YES, Field.Index.TOKENIZED));
            concatenatedFields.append(asset.getStatus().getName()).append(" ");
        }
        if (asset.getClassCode() != null) {
            document.add((Fieldable) new Field(CLASS_CODE, asset.getClassCode().getClassCodeValue(), Field.Store.YES, Field.Index.TOKENIZED));
            concatenatedFields.append(asset.getClassCode().getClassCodeValue()).append(" ");
        }
        if (asset.getContactPerson() != null) {
            document.add((Fieldable) new Field(CONTACT_PERSON_ID, asset.getContactPerson().getPersonId().toString(), Field.Store.YES, Field.Index.TOKENIZED));
            document.add((Fieldable) new Field(CONTACT_PERSON_NAME, asset.getContactPerson().getFirstAndLastName(), Field.Store.YES, Field.Index.TOKENIZED));
            concatenatedFields.append(asset.getContactPerson().getFirstAndLastName()).append(" ");
        }
        if (asset.getOwnerOrgBudgets() != null && asset.getOwnerOrgBudgets().size() > 0) {
            Iterator iter = asset.getOwnerOrgBudgets().iterator();
            StringBuffer ids = new StringBuffer();
            while (iter.hasNext()) {
                OrgBudget orgBdgt = (OrgBudget) iter.next();
                ids.append(orgBdgt.getOrgBudgetId());
            }
            document.add((Fieldable) new Field(ORG_BUDGET_IDS, ids.toString(), Field.Store.YES, Field.Index.TOKENIZED));
        }
        if (asset.getDateReceived() != null) {
            document.add((Fieldable) new Field(DATE_RECEIVED, formatter.format(asset.getDateReceived()), Field.Store.YES, Field.Index.TOKENIZED));
        }
        if (entity instanceof FixedAsset) {
            FixedAsset fixedAsset = (FixedAsset) entity;
            document.add((Fieldable) new Field(ASSET_NUMBER, fixedAsset.getFixedAssetNumber(), Field.Store.YES, Field.Index.TOKENIZED));
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
            log.error("Error in AssetIndex.add() , asset#:" + asset.getSensitiveAssetId(), e);
            throw new InfrastructureException(e.getMessage(), e);
        }
    }

    public void drop(Searchable entity) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        create(file);
        SensitiveAsset asset = (SensitiveAsset) entity;
        try {
            synchronized (mutex) {
                dropHandler(file, AssetIndex.ID, asset.getSensitiveAssetId().toString(), log);
//                final IndexReader reader = IndexReader.open(file);
//                try {
//                    Term term = new Term(AssetIndex.ID, asset.getSensitiveAssetId().toString());
//                    reader.deleteDocuments(term);
//                } finally {
//                    reader.close();
//                }
            }
        }
        catch (IOException e) {
            log.error("Error in AssetIndex.drop() , asset#:" + asset.getSensitiveAssetId(), e);
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
        File file = getCurrentIndexDirectory();
        //create(file);
        BooleanQuery booleanQuery = new BooleanQuery();
        Query tempQuery;
        String[] tokens = queryString.split("[\\s\"]");
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            token = StringUtils.escapeSpecialCharactersInLucene(token);
            if (token.length() > 0) {
                tempQuery = new PrefixQuery(new Term(AssetIndex.CONCATENATED_CONTENT, token));
                booleanQuery.add(tempQuery, BooleanClause.Occur.MUST);
            }
        }

        final ArrayList assets = new ArrayList();
        String value = booleanQuery.toString();
        if (StringUtils.alphaNumericOnly(value).length() == 0) {
            return assets;
        }
        QueryParser queryParser = new QueryParser(CONCATENATED_CONTENT, analyzer);
        Query query;
        try {
            query = queryParser.parse(value);
        }

        catch (ParseException e) {
            throw new InfrastructureException(e.getMessage(), e);
        }
        try {
            final IndexReader reader = IndexReader.open(file);
            final IndexSearcher searcher = new IndexSearcher(reader);
            final Hits hits = searcher.search(query);
            for (int i = 0; i < hits.length(); i++) {
                final Document doc = hits.doc(i);

                Long id = new Long(doc.get(ID));
                if (doc.get(CLASSNAME).equals("us.mn.state.health.model.inventory.FixedAsset")) {
                    FixedAsset fa = (FixedAsset) daoFactory.getFixedAssetDAO().getById(id, FixedAsset.class);
                    assets.add(fa);
                } else if (doc.get(CLASSNAME).equals("us.mn.state.health.model.inventory.SensitiveAsset")) {
                    SensitiveAsset sa = (SensitiveAsset) daoFactory.getSensitiveAssetDAO().getById(id, SensitiveAsset.class);
                    assets.add(sa);
                }
            }
            searcher.close();
            reader.close();
        }
        catch (IOException e) {
            log.error("Error in AssetIndex.search(String)", e);
            throw new InfrastructureException(e.getMessage(), e);
        }

        return assets;
    }

    public Collection searchIds(String queryString) throws InfrastructureException {
        return null;
    }

    /**
     * @param query
     * @return a Collection of Fixed and/or Sensitive assets
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Collection search(Query query) throws InfrastructureException {
        File file = getCurrentIndexDirectory();
        log.debug("The query is: " + query.toString());
        //create(file);
        final ArrayList results = new ArrayList();
        try {
            final IndexReader reader = IndexReader.open(file);
            final IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser queryParser = new QueryParser(ID, analyzer);
            Query q = null;
            try {
                q = queryParser.parse(query.toString());
            }
            catch (ParseException e) {
                throw new InfrastructureException(e);
            }
            final Hits hits = searcher.search(q);
            log.debug("number of hits: " + hits.length());
            for (int i = 0; i < hits.length(); i++) {
                final Document doc = hits.doc(i);
                String classname = doc.get(AssetIndex.CLASSNAME);
                log.debug("classname: " + classname);
                if (doc.get(AssetIndex.CLASSNAME).equals("us.mn.state.health.model.inventory.FixedAsset")) {
                    FixedAsset fixedAsset = new FixedAsset();
                    fixedAsset.setFixedAssetNumber(doc.get(AssetIndex.ASSET_NUMBER));
                    setupSensitiveAssetProperties(doc, fixedAsset);
                    results.add(fixedAsset);
                } else if (doc.get(AssetIndex.CLASSNAME).equals("us.mn.state.health.model.inventory.SensitiveAsset")) {
                    SensitiveAsset sensitiveAsset = new SensitiveAsset();
                    setupSensitiveAssetProperties(doc, sensitiveAsset);
                    results.add(sensitiveAsset);
                }
            }
            searcher.close();
            reader.close();
        }
        catch (IOException e) {
            log.error("Error in AssetIndex.search(Query)", e);
            throw new InfrastructureException("AssetIndex.search(): " + e.getMessage(), e);
        }
        catch (Exception e) {
            log.error("Error in AssetIndex.search(Query)", e);
            throw new InfrastructureException("AssetIndex.search(): " + e.getMessage(), e);
        }
        return results;
    }

    public Collection searchIds(Query query) throws InfrastructureException {
        return null;
    }

    private void setupSensitiveAssetProperties(Document doc, SensitiveAsset asset) throws java.text.ParseException {
        asset.setSensitiveAssetId(new Long(doc.get(AssetIndex.ID)));
        asset.setSerialNumber(doc.get(AssetIndex.SERIAL_NUMBER));
        ClassCode classCode = new ClassCode();
        classCode.setClassCodeValue(doc.get(AssetIndex.CLASS_CODE));
        asset.setClassCode(classCode);
        Person contactPerson = new Person();
        contactPerson.setFirstName("");
        contactPerson.setLastName("");
        String name = doc.get(AssetIndex.CONTACT_PERSON_NAME);
        if (name != null) {
            contactPerson.setFirstName(name.substring(0, name.indexOf(' ')));
            contactPerson.setLastName(name.substring(name.indexOf(' '), name.length()));
        }
        asset.setContactPerson(contactPerson);
        String dateReceived = doc.get(AssetIndex.DATE_RECEIVED);
        if (!StringUtils.nullOrBlank(dateReceived)) {
            asset.setDateReceived(formatter.parse(dateReceived));
        }
        //set up a junk item instance
        Item item = new PurchaseItem();
        Category cat = new Category();
        cat.setName(doc.get(CATEGORY_NAME));
        item.setCategory(cat);
        Manufacturer manufacturer = new Manufacturer();
        ExternalOrgDetail extOrgDetail = new ExternalOrgDetail();
        extOrgDetail.setOrgName(doc.get(MANUFACTURER_NAME));
        manufacturer.setExternalOrgDetail(extOrgDetail);
        item.setManufacturer(manufacturer);
        item.setModel(doc.get(AssetIndex.MODEL));
        item.setDescription(doc.get(AssetIndex.DESCRIPTION));
        asset.setItem(item);
        Facility fac = new Facility();
        fac.setFacilityName(doc.get(AssetIndex.FACILITY_NAME));
        asset.setFacility(fac);
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