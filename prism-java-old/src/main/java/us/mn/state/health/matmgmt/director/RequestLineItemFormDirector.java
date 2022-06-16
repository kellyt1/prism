package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.materialsrequest.RequestLineItemFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.User;

public class RequestLineItemFormDirector {
    private RequestLineItemFormBuilder builder;
    
    public RequestLineItemFormDirector(RequestLineItemFormBuilder builder) {
        this.builder = builder;
    }
    
    /**
     * Prepare a blank form for the creation of a new RequestLineItem
     * 
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
     public void constructAddRequestLineItemForm(Long itemId) throws InfrastructureException {
        if(itemId != null) { 
            builder.buildItem(itemId); 
        }
        //builder.buildVendors();
        builder.buildCategories();
        builder.buildUnits();
        builder.addNewRequestLineItemFundingSourceForm(false);
     }    
     
    public void constructAddRequestLineItemFormForITPurchases(Long itemId) throws InfrastructureException {
       if(itemId != null) {
           builder.buildItem(itemId);
       }
       //builder.buildVendors();
       builder.buildCategoriesForITPurchases();
       builder.buildUnits();
       builder.addNewRequestLineItemFundingSourceForm(false);
    }
     /**
     * Prepare a form for the view/edit of existing RequestLineItems
     * 
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
     public void constructEditRequestLineItemForm() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildItem();    //call this before buildVendors()
        //builder.buildVendors();
        builder.buildCategories();
        builder.buildRequestLineItemFundingSourceForms();
        builder.buildRequestLineItemNoteForms();        
        builder.buildRequestLineItem();
        builder.buildUnits();
     }
     
    public void constructEditRequestLineItemFormForITPurchases() throws InfrastructureException {
       builder.buildSimpleProperties();
       builder.buildItem();    //call this before buildVendors()
       //builder.buildVendors();
       builder.buildCategoriesForITPurchases();
       builder.buildRequestLineItemFundingSourceForms();
       builder.buildRequestLineItemNoteForms();
       builder.buildRequestLineItem();
       builder.buildUnits();
    }

     /**
     * Prepare a form for the view/edit of existing RequestLineItems exclusive to Purchasing
     * 
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
     public void constructEditPurchasingRequestLineItemForm() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildItem();    //call this before buildVendors()
        //builder.buildVendors();

        // Change for PRIS-181 effects PRIS-159; need to be rewritten just coming from constructEditPurchasingRequestLineItemForm()
        // builder.buildCategories();
        builder.buildCategoriesEdit();


        builder.buildRequestLineItemFundingSourceForms();
        builder.buildRequestLineItemNoteForms();        

        builder.buildPurchasingStatuses();
        builder.buildRequestLineItem();
        builder.buildUnits();
     }

    /**
     * Prepare a form for the view/edit of existing RequestLineItems exclusive to Purchasing
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void constructEditEditorRequestLineItemForm() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildItem();    //call this before buildVendors()
        //builder.buildVendors();
        builder.buildCategories();
        builder.buildRequestLineItemFundingSourceForms();
        builder.buildRequestLineItemNoteForms();
        //builder.buildPurchasingStatuses();
        builder.buildPurchasingStatusesEditor(); // limiting to certain statuses
        builder.buildRequestLineItem();
        builder.buildUnits();
    }
     
      public void constructNewPurchasingRequestLineItemForm(Long itemId) throws InfrastructureException {
        if(itemId != null) { 
            builder.buildItem(itemId); //call this before buildVendors()
        }
        //builder.buildVendors();
        builder.buildCategories();
        builder.buildUnits();
        builder.buildPurchasingStatuses();        
      }
     
     /**
     * Prepare a form for the evaluation of existing RequestLineItems.  This method accepts 
     * a User as an argument which represents an evaluator.  The buildRequestLineItemFundingSourceForms()
     * 
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
     public void constructEvaluateRequestLineItemForm(User evaluator) throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildItem();                    //call this before buildVendors()
        builder.buildRequestLineItemFundingSourceForms(evaluator);
        builder.buildRequestLineItemNoteForms();
        builder.buildRequestLineItem();
        builder.buildUnits();
        builder.buildRequestEvaluationApprovalsPending(evaluator);
     }
     
     /**
     * Prepare a form for stock room staff to pick 
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
     public void constructStockpickRequestLineItemForm() throws InfrastructureException {
         builder.buildSimpleProperties();
         builder.buildItem();               //call this before buildVendors()
         builder.buildDefaultQtyPicked();   //this must go after buildItem(), because it expects the item to be set.
         builder.buildRequestLineItemNoteForms();
         builder.buildRequestLineItem();
         
     }

    public void constructRequestLineItemFormForViewMyRequests() throws InfrastructureException{
         builder.buildSimpleProperties();
         builder.buildItem();    //call this before buildVendors()
         builder.buildRequestLineItemFundingSourceForms();
         builder.buildRequestLineItemNoteForms();
         builder.buildRequestLineItem();
         builder.buildStatusNameForViewMyRequests();
    }
}