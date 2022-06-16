package us.mn.state.health.view.inventory;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class StockItemsForm extends ValidatorForm {
    private String query;                                           //for searching 
    private Collection stockItems = new ArrayList();                   //Collection of StockItem beans(from the model)
    private Collection stockItemForms = new ArrayList();               //Collection of StockItemForm beans(from the view - for use in the re-order feature)
    private Integer firstResult = new Integer(0);                   //for pagination
    private Integer maxResults = new Integer(10);                   //for pagination
    private Boolean displayNextLink = Boolean.FALSE;   
    private Long contactId;
    private Boolean viewAll=false;
    private String ckShowInactive="ACT";
    private String statusActive="ACT";

    public Collection getSelectedStockItemForms() {
        Iterator iter = stockItemForms.iterator();
        ArrayList selectedForms = new ArrayList();
        while(iter.hasNext()) {
            StockItemForm siForm = (StockItemForm)iter.next();
            if(siForm.getSelected().booleanValue()) {
                selectedForms.add(siForm);
            }            
        }
        return selectedForms;
    }

    public void setStockItems(Collection stockItems) {
        this.stockItems = stockItems;
    }

    public Collection getStockItems() {
        return stockItems;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }


    public void setStockItemForms(Collection stockItemForms) {
        this.stockItemForms = stockItemForms;
    }


    public Collection getStockItemForms() {
        return stockItemForms;
    }


    public void setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
    }


    public Integer getFirstResult() {
        return firstResult;
    }


    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }


    public Integer getMaxResults() {
        return maxResults;
    }


    public void setQuery(String query) {
        this.query = query;
    }


    public String getQuery() {
        return query;
    }


    public void setDisplayNextLink(Boolean displayNextLink) {
        this.displayNextLink = displayNextLink;
    }


    public Boolean getDisplayNextLink() {
        return displayNextLink;
    }


    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }


    public Long getContactId() {
        return contactId;
    }

    public Boolean getViewAll() {
        return viewAll;
    }

    public void setViewAll(Boolean viewAll) {
        this.viewAll = viewAll;
    }

    public String getCkShowInactive() {
        return ckShowInactive;
    }

    public void setCkShowInactive(String ckShowInactive) {
        this.ckShowInactive = ckShowInactive;
    }

    public String getStatusActive() {
        return statusActive;
    }

    public void setStatusActive(String statusActive) {
        this.statusActive = statusActive;
    }
}