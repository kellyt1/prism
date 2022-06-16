package us.mn.state.health.view.materialsrequest;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.model.common.User;

public class ShoppingListForm extends ValidatorForm {    
    private String shoppingListId;
    private String comment;
    private String name;
    private User owner;
    private String username;
    private Boolean selectAllCatItems = Boolean.FALSE;
    private Boolean selectAllNonCatItems = Boolean.FALSE;
    private Collection shoppingListCatLineItemForms = new ArrayList();
    private Collection shoppingListNonCatLineItemForms = new ArrayList();

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }


    public void setShoppingListId(String shoppingListId) {
        this.shoppingListId = shoppingListId;
    }


    public String getShoppingListId() {
        return shoppingListId;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getComment() {
        return comment;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setOwner(User owner) {
        this.owner = owner;
        username = owner.getFirstAndLastName();
    }

    public String getUsername() {
        return username;
    }


    public User getOwner() {
        return owner;
    }


    public void setSelectAllCatItems(Boolean selectAllCatItems) {
        this.selectAllCatItems = selectAllCatItems;
    }


    public Boolean getSelectAllCatItems() {
        return selectAllCatItems;
    }


    public void setSelectAllNonCatItems(Boolean selectAllNonCatItems) {
        this.selectAllNonCatItems = selectAllNonCatItems;
    }


    public Boolean getSelectAllNonCatItems() {
        return selectAllNonCatItems;
    }


    public void setShoppingListCatLineItemForms(Collection shoppingListCatLineItemForms) {
        this.shoppingListCatLineItemForms = shoppingListCatLineItemForms;
    }


    public Collection getShoppingListCatLineItemForms() {
        return shoppingListCatLineItemForms;
    }


    public void setShoppingListNonCatLineItemForms(Collection shoppingListNonCatLineItemForms) {
        this.shoppingListNonCatLineItemForms = shoppingListNonCatLineItemForms;
    }


    public Collection getShoppingListNonCatLineItemForms() {
        return shoppingListNonCatLineItemForms;
    }

    public Collection getShoppingListLineItemForms() {
        Collection shoppingListLineItemForms = new ArrayList();
        shoppingListLineItemForms.addAll(shoppingListCatLineItemForms);
        shoppingListLineItemForms.addAll(shoppingListNonCatLineItemForms);
        return shoppingListLineItemForms;
    }

    
}