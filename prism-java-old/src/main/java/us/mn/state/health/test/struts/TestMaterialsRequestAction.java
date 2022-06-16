package us.mn.state.health.test.struts;

import java.util.Collection;
import java.util.Iterator;
import javax.servlet.ServletContext;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import servletunit.struts.MockStrutsTestCase;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.view.common.DeliveryAddressForm;
import us.mn.state.health.view.inventory.Command;
import us.mn.state.health.view.materialsrequest.RequestForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;

public class TestMaterialsRequestAction extends MockStrutsTestCase  {
    private static Log log = LogFactory.getLog(TestMaterialsRequestAction.class);
    DAOFactory daoFactory;
    
    public TestMaterialsRequestAction(String testName) {
        super(testName);
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.DEFAULT);
    }
    
    // main method so class can run from command line.
	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static TestSuite suite() {
		return new TestSuite(TestMaterialsRequestAction.class);
	} 
    
    public void setUp() throws Exception {
        super.setUp();
        setConfigFile("/WEB-INF/requests/struts-config-requests.xml");
        System.setProperty(Constants.ENV_KEY, Constants.DEVDB);
    }
    
    public void tearDown() throws Exception {
        super.tearDown();
    }
    
    /**
     * This method tests the viewAddCatalogItemToCart() method of MaterialsRequestAction
     * Things to test:
     * 1) What happens when there is no itemId request parameter? There should be an error message
     * and the action should forward to the ERROR forward
     * 2) What happens when the itemId request parameter is present, but there is no item with 
     * that ID in the database?  There should be an error message and the action should forward to
     * the ERROR forward.
     * 3) What happens when the itemId doesn't represent a valid Long? (i.e., I3DSDEQ#$) ?? There 
     * should be an error message and the action should forward to the  ERROR forward.
     * 4) What happens when the itemId is a valid ID, representing a real item in the database?
     * There should be no error messages, and the forward should be SUCCESS.
     */
    public void testViewAddCatalogItemToCart() throws Exception {
        // Set the action path 
		setRequestPathInfo("/viewAddCatalogItemToCart.do");
        
        //test #1: run the action with no itemId request parameter
		this.actionPerform();
        verifyForward("failure");
        
        //test #2: itemId with no matching items.
        this.addRequestParameter("itemId", "12345678987654321");
        this.actionPerform();
        verifyForward("failure");
        
        //test #3: non-numeric itemId .
        this.addRequestParameter("itemId", "I3DSDEQ");
        this.actionPerform();
        verifyForward("failure");
        
        //test #4: valid itemId provided
        this.addRequestParameter("itemId", getItem().getItemId().toString());
        this.actionPerform();
        verifyForward("success");        
    }   
    
    /**
     * This method tests the viewAddNonCatalogItemToCart() method of MaterialsRequestAction
     * Things to test:
     * There is no user input that goes into this action, so we really just need to test 
     * post-conditions:
     * 1) RequestLineItemForm.item should be null.
     * 2) the categories collection should not be null or empty
     * 3) the units collection should not be null or empty
     * 4) the forward should be SUCCESS
     * 5) the fundingSourceForms collection should not be null or empty
     * @throws java.lang.Exception
     */
    public void testViewAddNonCatalogItemToCart() throws Exception {
        // Set the action path 
		setRequestPathInfo("/viewAddNonCatalogItemToCart.do");
        
        //run the action
        this.actionPerform();
        RequestLineItemForm form = (RequestLineItemForm)this.getActionForm();
        
        //test #1
        this.assertNull(form.getItem());
        
        //test #2
        this.assertNotNull(form.getCategories());
        this.assertFalse(form.getCategories().isEmpty());
        
        //test #3
        this.assertNotNull(form.getUnits());
        this.assertFalse(form.getUnits().isEmpty());
        
        //test #4
        verifyForward("success"); 
        
        //test #5
        this.assertNotNull(form.getFundingSourceForms());
        this.assertFalse(form.getFundingSourceForms().isEmpty());
    }
    
    /**
     * Test what happens when you add an item to the cart a second time (i.e., 
     * add an item to the cart that is already in the cart. Instead of adding a 
     * new RLI, the addToCart action should just increment the QTY for the 
     * existing line item.
     * Things to test:
     * 1) Make sure the forward is set correctly
     * 2) Make sure there is only 1 RequestLineItemForm.
     * 3) make sure the quantity is incremented appropriately on the existing line item.
     * 4) Make sure there is a shopping cart (RequestForm object) on the session
     * 5) Make sure the quantity property is not null on the RequestLineItemForm
     * 
     * @throws java.lang.Exception
     */
    public void testAddSameCatalogItemToCart() throws Exception {
        this.setRequestPathInfo("/addCatalogItemToCart.do");
        getSession().setAttribute(Globals.TRANSACTION_TOKEN_KEY, "test_token");
        addRequestParameter(org.apache.struts.taglib.html.Constants.TOKEN_KEY, "test_token");
        RequestForm shoppingCart = new RequestForm();
        
        //set up an 'existing' RLI 
        RequestLineItemForm existingRliForm = new RequestLineItemForm();
        existingRliForm.setItem(getItem());
        existingRliForm.setQuantity("100");
        shoppingCart.addRequestLineItemForm(existingRliForm);
             
        //now set up a 'new' RLI for the same item
        RequestLineItemForm newRliForm = new RequestLineItemForm();
        newRliForm.setItem(getItem());
        addRequestParameter("quantity", "100");
        
        //add the shopping cart to the session
        this.getSession().setAttribute("shoppingCart", shoppingCart);
        
        //set the form for the addCatalogItemToCart action
        this.setActionForm(newRliForm);            
        this.actionPerform();
        
        // test #1
        verifyForward(Command.ADD_TO_CART); 
        
        //test # 4
        RequestForm updatedCart = (RequestForm)getSession().getAttribute("shoppingCart");
        this.assertNotNull(updatedCart);
        
        //test # 5
        this.assertTrue(updatedCart.getRequestLineItemForms().size() == 1);
        
        //test # 6
        RequestLineItemForm rliForm = (RequestLineItemForm)updatedCart.getRequestLineItemForms().iterator().next();
        this.assertFalse(StringUtils.nullOrBlank(rliForm.getQuantity()));
        
        //test # 3
        int qty = Integer.parseInt(rliForm.getQuantity());
        this.assertTrue(qty == 200);
    }
    
    
    private Item getItem() throws Exception {
        Collection items = daoFactory.getItemDAO().findAll(1,1);
        Item item = null;
        if(!items.isEmpty()) {
            item = (Item)items.iterator().next();
        }
        return item;
    }
}