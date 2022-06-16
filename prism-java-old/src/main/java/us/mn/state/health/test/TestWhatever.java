package us.mn.state.health.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import us.mn.state.health.builder.purchasing.OrderFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.dao.inventory.FixedAssetDAO;
import us.mn.state.health.dao.inventory.SensitiveAssetDAO;
import us.mn.state.health.matmgmt.director.OrderFormDirector;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.Facility;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.inventory.FixedAsset;
import us.mn.state.health.model.inventory.SensitiveAsset;
import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.model.util.search.AssetIndex;
import us.mn.state.health.view.purchasing.OrderForm;

public class TestWhatever extends TestCase {
    private SensitiveAssetDAO saDAO;
    private FixedAssetDAO faDAO;
    private DAOFactory daoFactory;
    private String environment;

    protected void setUp() throws Exception {
        //here is where we select the environment
        environment = Constants.DEVDB;
//        environment = Constants.TEST;
//        environment = Constants.PROD;
        System.setProperty(Constants.ENV_KEY, environment);
        daoFactory = new HibernateDAOFactory();
        //saDAO = new HibernateSensitiveAssetDAO();
        //faDAO = new HibernateFixedAssetDAO();
    }

    protected void tearDown() throws Exception {
        //HibernateUtil.commitTransaction();  
        //HibernateUtil.closeSession();
        saDAO = null;
        faDAO = null;
    }

    public TestWhatever(String x) {
        super(x);
    }

    public static void main(String[] args) throws Exception {
        TestRunner.run(suite());
        //junit.swingui.TestRunner.run(us.mn.state.health.test.TestStatus.class);
    }

     public static Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTest(new TestWhatever("testWhatever"));

        return suite;
    }

    public void testWhatever() {
        System.out.println("java.io.tmpdir: " + System.getProperty("java.io.tmpdir"));
        System.out.println("user.home: " + System.getProperty("user.home"));
        System.out.println("user.dir: " + System.getProperty("user.dir"));
    }

    public void testDate() {
        try {
            // Some examples
            DateFormat formatter = new SimpleDateFormat("yy");
            Date date = new Date();
            String s = formatter.format(date);
            System.out.println(" FY" + s);
        }
        catch(Exception e) {
        }
    }

    public void testAssetIndex() throws Exception {
        HashSet assetsSet = new HashSet();                              //useing a Set to avoid adding duplicates
        assetsSet.addAll(faDAO.findAll(FixedAsset.class));
        assetsSet.addAll(saDAO.findAll(SensitiveAsset.class));
        AssetIndex indexer = new AssetIndex();
        Iterator iter = assetsSet.iterator();
        ArrayList fixedAssetsList = new ArrayList();
        ArrayList sensitiveAssetsList = new ArrayList();
        while(iter.hasNext()) {
            Object asset = iter.next();
            if(asset instanceof FixedAsset) {
                FixedAsset fixedAsset = (FixedAsset)asset;
                fixedAssetsList.add(fixedAsset);
            }
            else if(asset instanceof SensitiveAsset) {
                SensitiveAsset sensitiveAsset = (SensitiveAsset)asset;
                sensitiveAssetsList.add(sensitiveAsset);
            }
        }
        System.out.println("Number of Fixed Assets: " + fixedAssetsList.size());
        Iterator faListIter = fixedAssetsList.iterator();
        while(faListIter.hasNext()){
            FixedAsset fa = (FixedAsset)faListIter.next();
            System.out.println(fa.getSensitiveAssetId());
        }
        System.out.println("Number of Sensitive Assets: " + sensitiveAssetsList.size());
        Iterator saListIter = sensitiveAssetsList.iterator();
        while(saListIter.hasNext()){
            SensitiveAsset sa = (SensitiveAsset)saListIter.next();
            System.out.println(sa.getSensitiveAssetId());
        }
    }

    public void testBuildOrderForm() throws InfrastructureException {
        String orderIdStr;
        orderIdStr = "226973";
//        orderIdStr = "304539";
        OrderForm orderForm = new OrderForm();
        Collection newRLIForms = null;
        Long orderId = new Long(orderIdStr);
        Order order = daoFactory.getPurchasingOrderDAO().getOrderById(orderId, false);
        OrderFormBuilder builder = new OrderFormBuilder(orderForm, order, newRLIForms, daoFactory);
        OrderFormDirector director = new OrderFormDirector(builder);
        director.constructEditForPurchasing();
    }

    public void testGetPrimaryFacilityForPerson() throws InfrastructureException {
        String personIdStr = "7185";
        Long personId = new Long(personIdStr);
        Person recipient = daoFactory.getPersonDAO().getPersonById(personId, false);
        Facility facility = recipient.getPrimaryFacility();
        facility.getBuildingMailingAddress();
        System.out.println(facility);
    }

}