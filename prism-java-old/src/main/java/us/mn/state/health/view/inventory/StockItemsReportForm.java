package us.mn.state.health.view.inventory;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.action.ActionMapping;

public class StockItemsReportForm extends ValidatorForm {
    private List categories = new ArrayList();
    private List stockItems = new ArrayList();
    private List stockItemFacilities = new ArrayList();
    private Long[] selectedCategories;
    private Long[] selectedFacilities;
    //include the stock items without a location in the search results
    private boolean woLocations = true;
    private boolean reset = false;

    public Long[] getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(Long[] selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public List getCategories() {
        return categories;
    }

    public void setCategories(List categories) {
        this.categories = categories;
    }

    public List getStockItems() {
        return stockItems;
    }

    public void setStockItems(List stockItems) {
        this.stockItems = stockItems;
    }

    public Object getStockItemFromIndex(int index) {
        return stockItems.get(index);
    }


    public List getStockItemFacilities() {
        return stockItemFacilities;
    }

    public void setStockItemFacilities(List stockItemFacilities) {
        this.stockItemFacilities = stockItemFacilities;
    }


    public Long[] getSelectedFacilities() {
        return selectedFacilities;
    }

    public void setSelectedFacilities(Long[] selectedFacilities) {
        this.selectedFacilities = selectedFacilities;
    }


    public boolean getWoLocations() {
        return woLocations;
    }

    public void setWoLocations(boolean woLocations) {
        this.woLocations = woLocations;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        if(isReset()){
            reset = false;
            selectedCategories = new Long[0];
            selectedFacilities = new Long[0];
            woLocations = true;
            stockItems = new ArrayList();
            categories = new ArrayList();
            stockItemFacilities = new ArrayList();
        }
        woLocations = false;
    }


    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }
}
