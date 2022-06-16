package us.mn.state.health.test;

import javax.servlet.ServletContext;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.ModuleUtils;
import org.apache.struts.util.RequestUtils;
import servletunit.struts.MockStrutsTestCase;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.persistence.HibernateUtil;

public class TestStockRequestAction extends MockStrutsTestCase {
    private String environment;
    private DAOFactory daoFactory;
    
    public TestStockRequestAction(String testName) {
        super(testName);
    }
    
    public void setUp() throws Exception {
        /* always call the superclass setUp method! */
        super.setUp();
        
        /* get the ServletContext from the ActionServlet */
        ServletContext context = getActionServlet().getServletContext();
        
        /* grab the moduleConfig from the ServletContext */
        ModuleConfig moduleConfig = ModuleUtils.getInstance().getModuleConfig(this.getRequest(), context);
        setConfigFile("/WEB-INF/struts-config,/WEB-INF/inventory/struts-config-inventory.xml");
         
        /* Create a data source */
        environment = Constants.DEVDB;
        System.setProperty(Constants.ENV_KEY, environment);
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
    }
    
    public void testViewOpenStockRequests() {
        // Set the action path 
		setRequestPathInfo("/viewOpenStockRequests");
        addRequestParameter("orderBy","nothing");
        // Run the action through the ActionServlet 
        actionPerform();
    }
    
    /*
    public void testGeneratePickList() {
        // Set the action path 
		setRequestPathInfo("/generatePickList");
        
        // Run the action through the ActionServlet 
        actionPerform();
    }
    
    public void testFillStockRequests() {
        // Set the action path 
		setRequestPathInfo("/fillStockRequests");
        
        // Run the action through the ActionServlet
        actionPerform();
    }
    */

    protected void tearDown() throws Exception {
        super.tearDown();
        HibernateUtil.commitTransaction();  //is this necessary when you're only reading?
        HibernateUtil.closeSession();
    }
}
