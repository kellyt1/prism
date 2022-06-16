package us.mn.state.health.test.struts;

import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.ModuleUtils;
import org.apache.struts.util.RequestUtils;
import servletunit.struts.MockStrutsTestCase;
import us.mn.state.health.view.common.DeliveryAddressForm;

public class DeliveryAddressActionTest extends MockStrutsTestCase  {
    private static Log log = LogFactory.getLog(DeliveryAddressActionTest.class);
    
    public DeliveryAddressActionTest(String testName) {
        super(testName);
    }
    
    public void setUp() throws Exception {
        super.setUp();
        ServletContext context = this.getActionServlet().getServletContext();
        ModuleConfig moduleConfig = ModuleUtils.getInstance().getModuleConfig(this.getRequest(), context);
        setConfigFile("/WEB-INF/struts-config.xml");
    }
    
    public void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testViewAddNewAddressStep1() {
        // Set the action path 
		setRequestPathInfo("/viewAddNewAddressStep1.do");
        
        // Run the action through the ActionServlet
		this.actionPerform();
        
        DeliveryAddressForm theForm = (DeliveryAddressForm)getActionForm();
        this.assertTrue(theForm.getExtOrgs().size() > 0); 
        verifyForward("success");
        verifyNoActionErrors();
    }
    /*
    public void testViewAddNewAddressStep2() {
        // Set the action path 
		setRequestPathInfo("/viewAddNewAddressStep2.do");
        addRequestParameter("selectedExtOrgId", "171530");
        // Run the action through the ActionServlet
		this.actionPerform();
        
        DeliveryAddressForm theForm = (DeliveryAddressForm)getActionForm();
        String extOrgId = theForm.getSelectedExtOrgId();
        log.debug("extOrgId: " + extOrgId);
        this.assertFalse((extOrgId == null || "".equals(extOrgId)));
        this.assertFalse(theForm.getExtOrgs().size() > 0); 
        this.assertTrue(theForm.getNonContactPersons().size() > 0); 
        
        //Make sure the contactPersons collection and the nonContactPersons collection
        //do not contain the same person
        Collection nonContacts = theForm.getNonContactPersons();
        for(Iterator iter = theForm.getContactPersons().iterator(); iter.hasNext(); ) {
            Person person = (Person)iter.next();
            assertFalse(nonContacts.contains(person));  //should not contain any of the same persons as contacts
            assertNull(person.getEmployeeId());         //should not contain employees
        }
        for(Iterator iter = nonContacts.iterator(); iter.hasNext(); ) {
            Person person = (Person)iter.next();
            assertNull(person.getEmployeeId());         //should not contain employees
        }
        
        verifyForward("success");
        verifyNoActionErrors();
    }
    
    public void testAddContacts() {
        // Set the action path 
		setRequestPathInfo("/addContacts.do");
        
        verifyForward("success");
        verifyNoActionErrors();
    }
    */
}