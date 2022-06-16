package us.mn.state.health.view.inventory;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.inventory.StockItemFacility;

public class InventoryMaintenanceForm extends ValidatorForm {
    private Category category = new Category(); //Category
    private Collection facilities = new ArrayList(); //Facilities    
    private StockItemFacility facility; //Current Facility    
    private Collection units; //Editable Unit    
    private String cmd = ""; //A maintenance related command    
    private String categoryId = "";    
    private String facilityId = "";    
    private String locationId = "";
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {        
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }


    public String getCmd() {
        return cmd;
    }


    public void setFacilities(Collection facilities) {
        this.facilities = facilities;
    }


    public Collection getFacilities() {
        return facilities;
    }


    public void setUnits(Collection units) {
        this.units = units;
    }


    public Collection getUnits() {
        return units;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public Category getCategory() {
        return category;
    }


    public void setFacility(StockItemFacility facility) {
        this.facility = facility;
    }


    public StockItemFacility getFacility() {
        return facility;
    }


    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


    public String getCategoryId() {
        return categoryId;
    }


    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }


    public String getFacilityId() {
        return facilityId;
    }


    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }


    public String getLocationId() {
        return locationId;
    }    
}