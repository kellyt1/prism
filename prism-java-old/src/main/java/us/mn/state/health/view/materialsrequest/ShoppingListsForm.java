package us.mn.state.health.view.materialsrequest;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class ShoppingListsForm extends ValidatorForm {

    private Collection shoppingLists = new ArrayList();
    
    private ShoppingListForm shoppingListForm;
    
    private String shoppingListId;
    
    private Boolean existingList;
    
    private Long itemToAddId;
    
    private RequestLineItemForm reqLineItemToAdd;
    
    private RequestForm requestToAdd;
    
    private String shoppingListAction;
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        existingList = Boolean.TRUE;
        if(shoppingListForm != null) {
            shoppingListForm.setSelectAllCatItems(Boolean.FALSE);
            shoppingListForm.setSelectAllNonCatItems(Boolean.FALSE);
            
        }
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }


    public void setShoppingLists(Collection shoppingLists) {
        this.shoppingLists = shoppingLists;
    }


    public Collection getShoppingLists() {
        return shoppingLists;
    }


    public void setShoppingListForm(ShoppingListForm shoppingListForm) {
        this.shoppingListForm = shoppingListForm;
    }


    public ShoppingListForm getShoppingListForm() {
        return shoppingListForm;
    }

    public void setItemToAddId(Long itemToAddId) {
        this.itemToAddId = itemToAddId;
    }


    public Long getItemToAddId() {
        return itemToAddId;
    }


    public void setReqLineItemToAdd(RequestLineItemForm reqLineItemToAdd) {
        this.reqLineItemToAdd = reqLineItemToAdd;
    }


    public RequestLineItemForm getReqLineItemToAdd() {
        return reqLineItemToAdd;
    }


    public void setRequestToAdd(RequestForm requestToAdd) {
        this.requestToAdd = requestToAdd;
    }


    public RequestForm getRequestToAdd() {
        return requestToAdd;
    }


    public void setShoppingListAction(String shoppingListAction) {
        this.shoppingListAction = shoppingListAction;
    }


    public String getShoppingListAction() {
        return shoppingListAction;
    }


    public void setExistingList(Boolean existingList) {
        this.existingList = existingList;
    }


    public Boolean getExistingList() {
        return existingList;
    }


    public void setShoppingListId(String shoppingListId) {
        this.shoppingListId = shoppingListId;
    }


    public String getShoppingListId() {
        return shoppingListId;
    }
    
}