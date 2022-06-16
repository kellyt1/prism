package us.mn.state.health.builder.materialsrequest;
import java.util.Collection;
import java.util.Iterator;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.materialsrequest.ShoppingListNonCatLineItemForm;

public class ShoppingListRLINonCatLineItemFormsBuilder  {

    private Collection requestLineItemForms;
    private Collection shoppingListLineItemForms;
    private DAOFactory daoFactory;
    
    public ShoppingListRLINonCatLineItemFormsBuilder(Collection shoppingListLineItemForms,
                                            Collection requestLineItemForms,
                                            DAOFactory daoFactory) {
                                            
        this.shoppingListLineItemForms = shoppingListLineItemForms;
        this.requestLineItemForms = requestLineItemForms;
        this.daoFactory = daoFactory;
    }
    
    public void buildShoppingListNonCatLineItemForms() throws InfrastructureException {
    
        ShoppingListNonCatLineItemFormBuilder builder = null;
        
        for(Iterator i = requestLineItemForms.iterator(); i.hasNext();) {
            
            
            RequestLineItemForm reqLineItemForm = (RequestLineItemForm)i.next();
            if(reqLineItemForm.getItem() == null) { //This is a non catalog RLI
                ShoppingListNonCatLineItemForm form = new ShoppingListNonCatLineItemForm();
                builder = 
                    new ShoppingListNonCatLineItemFormBuilder(form, reqLineItemForm,
                                                              daoFactory);
                builder.buildDefaultProperties();
                builder.buildCategories();
//                builder.buildVendors();
                builder.buildSimpleProperties();
                shoppingListLineItemForms.add(form);
            }
        }
    }
}