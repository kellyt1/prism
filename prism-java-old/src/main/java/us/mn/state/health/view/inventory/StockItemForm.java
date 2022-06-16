package us.mn.state.health.view.inventory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.StockItemFacility;

public class StockItemForm extends ItemForm {
    private String icnbr;
    private String qtyOnHand;
    private String reorderPoint;
    private String reorderQty;
    private String cycleCountPriority;
    private Boolean stockOut;
    private String reorderDate;
    private String holdUntilDate;
    private String packQty;
    private Boolean staggeredDelivery;
    private Boolean seasonal;
    private String printFileURL;
    private String statusId;
    private String primaryContactName;  
    private String primaryContactId;    
    private String secondaryContactName;  
    private String secondaryContactId;
    private String orgBudgetCodeAndName;
    private String orgBudgetId;    
    private Boolean onOrder;
    private FormFile printSpecFile;
    private String asstDivDirId;    
    private String facilityId;    
    private String locationCode;
    private Boolean isPrimaryLocation = Boolean.TRUE;
    private String instructions;
    private Boolean selected = Boolean.FALSE;
    
    private Collection contacts = new TreeSet();
    private Collection statuses = new TreeSet();
    private Collection asstDivDirectors = new TreeSet();
    private Collection orgBudgets = new TreeSet();         
    private Collection allOrgBudgetsList = new HashSet();
    private Collection ownerOrgBudgets = new HashSet();
    private Collection stockItemFacilities = new TreeSet();
    private Collection myStockItemLocations = new HashSet();    //collection of StockItemLocation objects

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.setHazardous(Boolean.FALSE);
        super.setCmd("");
        locationCode = "";
        facilityId = "";

    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    public StockItemFacility getFacility() {
        return (StockItemFacility)CollectionUtils.getObjectFromCollectionById(stockItemFacilities, facilityId, "facilityId");
    }

    public Person getAsstDivDirector() {
        return (Person)CollectionUtils.getObjectFromCollectionById(asstDivDirectors, asstDivDirId, "personId");
    }

    public String getAsstDivDirId() {
        return asstDivDirId;
    }

    public void setAsstDivDirId(String asstDivDirId) {
        this.asstDivDirId = asstDivDirId;
    }

    public Collection getAsstDivDirectors() {
        return asstDivDirectors;
    }

    public void setAsstDivDirectors(Collection asstDivDirectors) {
        this.asstDivDirectors = asstDivDirectors;
    }

    public Person getAssistantDivisionDirectorForm() {
        Iterator i = asstDivDirectors.iterator();
        while(i.hasNext()) {
            Person person = (Person) i.next();
            if(person.getPersonId().toString().equals(asstDivDirId)) {
                return person;
            }
        }
        return null;
    }

    public Status getStatus() {
        Status status = (Status)CollectionUtils.getObjectFromCollectionById(statuses, statusId, "statusId");
        return status;
    }

    public Boolean getIsOnHold() {
        return null;
    }

    public void setIsOnHold() {

    }

    public Person getPrimaryContactForm() {
        Iterator i = contacts.iterator();
        while(i.hasNext()) {
            Person person = (Person)i.next();
            if(person.getPersonId().toString().equals(primaryContactId)) {
                return person;
            }
        }
        return null;
    }

    public Person getSecondaryContactForm() {
        Iterator i = contacts.iterator();
        while(i.hasNext()) {
            Person person = (Person) i.next();
            if(person.getPersonId().toString().equals(secondaryContactId)) {
                return person;
            }                
        }
        return null;
    }

    public OrgBudget getOrgBudgetForm() {
        Iterator i = orgBudgets.iterator();
        while(i.hasNext()) {
            OrgBudget orgBudget = (OrgBudget)i.next();
            if(orgBudget.getOrgBudgetId().toString().equals(orgBudgetId)) {
                return orgBudget;
            }
        }
        return null;
    }

    /**
     * Retrieve a representation of the file the user has uploaded
     */
    public FormFile getPrintSpecFile() {
        return printSpecFile;
    }

    /**
     * Set a representation of the file the user has uploaded
     */
    public void setPrintSpecFile(FormFile printSpecFile) {
        this.printSpecFile = printSpecFile;
    }

    public void setOnOrder(Boolean onOrder) {
        this.onOrder = onOrder;
    }

    public Boolean getOnOrder() {
        return onOrder;
    }

    public void setIcnbr(String icnbr) {
        this.icnbr = StringUtils.trim(icnbr);
    }


    public String getIcnbr() {
        return icnbr;
    }


    public void setQtyOnHand(String qtyOnHand) {
        this.qtyOnHand = StringUtils.trim(qtyOnHand);
    }


    public String getQtyOnHand() {
        return qtyOnHand;
    }

    public void setReorderPoint(String reorderPoint) {
        this.reorderPoint = StringUtils.trim(reorderPoint);
    }

    public String getReorderPoint() {
        return reorderPoint;
    }


    public void setReorderQty(String reorderQty) {
        this.reorderQty = StringUtils.trim(reorderQty);
    }

    public String getReorderQty() {
        return reorderQty;
    }

    public void setCycleCountPriority(String cycleCountPriority) {
        this.cycleCountPriority = StringUtils.trim(cycleCountPriority);
    }

    public String getCycleCountPriority() {
        return cycleCountPriority;
    }


    public void setStockOut(Boolean stockOut) {
        this.stockOut = stockOut;
    }


    public Boolean getStockOut() {
        return stockOut;
    }


    public void setReorderDate(String reorderDate) {
        this.reorderDate = StringUtils.trim(reorderDate);
    }


    public String getReorderDate() {
        return reorderDate;
    }


    public void setHoldUntilDate(String holdUntilDate) {
        this.holdUntilDate = StringUtils.trim(holdUntilDate);
    }


    public String getHoldUntilDate() {
        return holdUntilDate;
    }


    public void setPackQty(String packQty) {
        this.packQty = packQty;
    }


    public String getPackQty() {
        return packQty;
    }


    public void setStaggeredDelivery(Boolean staggeredDelivery) {
        this.staggeredDelivery = staggeredDelivery;
    }


    public Boolean getStaggeredDelivery() {
        return staggeredDelivery;
    }


    public void setSeasonal(Boolean seasonal) {
        this.seasonal = seasonal;
    }


    public Boolean getSeasonal() {
        return seasonal;
    }


    public void setPrintFileURL(String printFileURL) {
        this.printFileURL = printFileURL;
    }


    public String getPrintFileURL() {
        return printFileURL;
    }

    public void setPrimaryContactId(String primaryContactId) {
        this.primaryContactId = primaryContactId;
    }


    public String getPrimaryContactId() {
        return primaryContactId;
    }


    public void setSecondaryContactId(String secondaryContactId) {
        this.secondaryContactId = secondaryContactId;
    }


    public String getSecondaryContactId() {
        return secondaryContactId;
    }


    public void setOrgBudgetId(String orgBudgetId) {
        this.orgBudgetId = orgBudgetId;
    }


    public String getOrgBudgetId() {
        return orgBudgetId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }


    public String getStatusId() {
        return statusId;
    }

    public void setStatuses(Collection statuses) {
        this.statuses = statuses;
    }


    public Collection getStatuses() {
        return statuses;
    }


    public void setOrgBudgets(Collection orgBudgets) {
        this.orgBudgets = orgBudgets;
    }


    public Collection getOrgBudgets() {
        return orgBudgets;
    }


    public void setContacts(Collection contacts) {
        this.contacts = contacts;
    }


    public Collection getContacts() {
        return contacts;
    }


    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }


    public String getFacilityId() {
        return facilityId;
    }


    public void setStockItemFacilities(Collection facilities) {
        this.stockItemFacilities = facilities;
    }


    public Collection getStockItemFacilities() {
        return stockItemFacilities;
    }
    
    public StockItem getStockItem() {
        Item item = getItem();
        if(item != null && item instanceof StockItem) {
            return (StockItem)item;
        }
        else {
            return null;
        }
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * This is just a helper method that returns the
     * ICNBR in this format: CategoryCode-ICNBR
     * It's used to display the ICNBR in the jsp's
     * @return the full icnbr (including the category code)
     */
    public String getFullIcnbr(){
        String result = "";
        
        if(!StringUtils.nullOrBlank(icnbr)) {
            NumberFormat numberFormat = new DecimalFormat("0000");
            String icnbrFormat = numberFormat.format(new Integer(icnbr));
            Category category = (Category)CollectionUtils.getObjectFromCollectionById(getCategories(),
                                                                                      getCategoryId(),
                                                                                      "categoryId");
            if(category==null){
                category = (Category)CollectionUtils.getObjectFromCollectionById(getCategories(),
                                                                                      getCategoryCode(),
                                                                                      "categoryCode");
            }
            result+=category.getCategoryCode();
            result+="-";
            result+=icnbrFormat;
        }
        
        return result;
    }


    public void setMyStockItemLocations(Collection locations) {
        this.myStockItemLocations = locations;
    }


    public Collection getMyStockItemLocations() {
        return myStockItemLocations;
    }


    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }


    public String getLocationCode() {
        return locationCode;
    }


    public void setSelected(Boolean selected) {
        this.selected = selected;
    }


    public Boolean getSelected() {
        return selected;
    }


    public void setPrimaryContactName(String primaryContactName) {
        this.primaryContactName = primaryContactName;
    }


    public String getPrimaryContactName() {
        return primaryContactName;
    }


    public void setSecondaryContactName(String secondaryContactName) {
        this.secondaryContactName = secondaryContactName;
    }


    public String getSecondaryContactName() {
        return secondaryContactName;
    }


    public void setOrgBudgetCodeAndName(String orgBudgetCodeAndName) {
        this.orgBudgetCodeAndName = orgBudgetCodeAndName;
    }


    public String getOrgBudgetCodeAndName() {
        return orgBudgetCodeAndName;
    }


    public void setIsPrimaryLocation(Boolean isPrimaryLocation) {
        this.isPrimaryLocation = isPrimaryLocation;
    }


    public Boolean getIsPrimaryLocation() {
        return isPrimaryLocation;
    }


}