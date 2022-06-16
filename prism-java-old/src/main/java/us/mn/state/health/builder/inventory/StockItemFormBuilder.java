package us.mn.state.health.builder.inventory;

import java.util.Collection;
import java.util.Iterator;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.ObjectCode;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Position;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.view.inventory.StockItemForm;
import us.mn.state.health.view.inventory.ItemVendorForm;

public class StockItemFormBuilder {
    private DAOFactory daoFactory;
    private StockItem stockItem;
    private StockItemForm stockItemForm;

    public StockItemFormBuilder(StockItemForm stockItemForm, DAOFactory daoFactory) {
        this.stockItemForm = stockItemForm;
        this.daoFactory = daoFactory;
    }

    public StockItemFormBuilder(StockItemForm stockItemForm,
                                StockItem stockItem,
                                DAOFactory daoFactory) {
        this(stockItemForm, daoFactory);
        this.stockItem = stockItem;
    }

    public void buildAsstDivDirectorsList() throws InfrastructureException {
        Collection asstDivDirectors =
                daoFactory.getPersonDAO().findPersonsByPositionClassCode(Position.ASST_DIV_DIRECTOR_CLASS_CODE);
        stockItemForm.setAsstDivDirectors(asstDivDirectors);
    }

    public void buildAvailableVendorsList() throws InfrastructureException {
        Collection availableVendors;
        if(stockItem != null) {
            availableVendors = daoFactory.getVendorDAO().findVendorWhereNotWithItemId(stockItem.getItemId(), false);
        }
        else {
//            availableVendors = daoFactory.getVendorDAO().findAll(false);   
            availableVendors = daoFactory.getVendorDAO().findAll();
        }
        stockItemForm.setAvailableVendors(availableVendors);
    }

    public void buildItemVendors() throws InfrastructureException {
        if(stockItem != null) {
            stockItemForm.setItemVendors(stockItem.getItemVendors());
        }
    }

    public void buildCategoriesList() throws InfrastructureException {
        Collection categories =
                daoFactory.getCategoryDAO().findDescendantCategoriesByParentCode(Category.MATERIALS, false);
        stockItemForm.setCategories(categories);
    }

    public void buildCategory() throws InfrastructureException {
        stockItemForm.setCategoryCode("");
        if(stockItem != null && stockItem.getCategory() != null) {
            stockItemForm.setCategoryId(stockItem.getCategory().getCategoryId().toString());
        }
        else {
            stockItemForm.setCategoryId("");
        }
    }

    public void buildCategoryUsingCategoryCode() {
        if(stockItem != null && stockItem.getCategory() != null) {
            stockItemForm.setCategoryCode(stockItem.getCategory().getCategoryCode());
//            stockItemForm.setCategoryId(stockItem.getCategory().getCategoryId().toString());
        }
        else {
            stockItemForm.setCategoryCode("");
            stockItemForm.setCategoryId("");
        }
    }

    public void buildItem() throws InfrastructureException {
//        stockItemForm.setItem(stockItem);
        if(stockItem != null) {
            stockItemForm.setItem(daoFactory.getStockItemDAO().getStockItemById(stockItem.getItemId(), false));
        }
    }

    public void buildContactsList() throws InfrastructureException {
        Collection employees = daoFactory.getPersonDAO().findAllMDHEmployees();
        stockItemForm.setContacts(employees);
    }

    /**
     * Set the primary contact property from the EXISTING information in the stock item.
     * This is the info that is already associated with the stock item; pre-edit.
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildPrimaryContact() throws InfrastructureException {
        if(stockItemForm != null && !StringUtils.nullOrBlank(stockItemForm.getPrimaryContactId())) {
            //take the info from the form if present
            Long primContactId = new Long(stockItemForm.getPrimaryContactId());
            Person person = daoFactory.getPersonDAO().getPersonById(primContactId, false);
            stockItemForm.setPrimaryContactName(person.getFirstAndLastName());
        }
        else if(stockItem != null && stockItem.getPrimaryContact() != null) {
            //otherwise take the existing info.
            stockItemForm.setPrimaryContactId(stockItem.getPrimaryContact().getPersonId().toString());
            stockItemForm.setPrimaryContactName(stockItem.getPrimaryContact().getFirstAndLastName());
        }
    }

    /**
     * Set the secondary contact property from the EXISTING information in the stock item.
     * This is the info that is already associated with the stock item; pre-edit.
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildSecondaryContact() throws InfrastructureException {
        if(stockItemForm != null && !StringUtils.nullOrBlank(stockItemForm.getSecondaryContactId())) {
            //take the info from the form if present
            Long secondaryContactId = new Long(stockItemForm.getSecondaryContactId());
            Person person = daoFactory.getPersonDAO().getPersonById(secondaryContactId, false);
            stockItemForm.setSecondaryContactName(person.getFirstAndLastName());
        }
        else if(stockItem != null && stockItem.getSecondaryContact() != null) {
            //otherwise take the existing info
            stockItemForm.setSecondaryContactId(stockItem.getSecondaryContact().getPersonId().toString());
            stockItemForm.setSecondaryContactName(stockItem.getSecondaryContact().getFirstAndLastName());
        }
    }

    public void buildAsstDivDirector() throws InfrastructureException {
         if(stockItem != null && stockItem.getAsstDivDirector() != null) {
            stockItemForm.setAsstDivDirId(stockItem.getAsstDivDirector().getPersonId().toString());
         }
         else {
             stockItemForm.setAsstDivDirId("");
         }
    }

    /**
     * This method is used to set defaults for properties when a user wishes to request a NEW stock item
     */
    public void buildDefaultProperties() {
        stockItemForm.setHazardous(Boolean.FALSE);
        stockItemForm.setOnOrder(Boolean.FALSE);
        stockItemForm.setSeasonal(Boolean.FALSE);
    }

    public void buildOrgBudgetsList() throws InfrastructureException {
        Collection orgBudgets = daoFactory.getOrgBudgetDAO().findAllPurchaseOrgBudgets();
        stockItemForm.setOrgBudgets(orgBudgets);
    }

    /**
     * TODO - StockItems can have multiple org budgets, so we have to refactor to allow
     * for more than one.
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildOrgBudget() throws InfrastructureException {
        if(stockItem != null && stockItem.getOrgBudget() != null) {
            stockItemForm.setOrgBudgetId(stockItem.getOrgBudget().getOrgBudgetId().toString());
        }
        else {
            stockItemForm.setOrgBudgetId("");
        }
    }

    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(stockItemForm, stockItem);
        } 
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building StockItemForm: ", rpe);
        }
    }

    public void buildStatusesList() throws InfrastructureException {
        Collection statuses = daoFactory.getStatusDAO().findAllByStatusTypeCode(StatusType.STOCK_ITEM);
        stockItemForm.setStatuses(statuses);
    }

    public void buildStatus() throws InfrastructureException {
        if(stockItem != null && stockItem.getStatus() != null) {
            stockItemForm.setStatusId(stockItem.getStatus().getStatusId().toString());
        }
        else {
            stockItemForm.setStatusId("");
        }
    }

    public void buildStockItemFacilitiesList() throws InfrastructureException {
        Collection stockItemFacilities =
                daoFactory.getStockItemFacilityDAO().findAll();
        stockItemForm.setStockItemFacilities(stockItemFacilities);
    }

    public void buildMyStockItemLocations() throws InfrastructureException {
        if(stockItem != null) {
            stockItemForm.setMyStockItemLocations(stockItem.getLocations());
        }
    }

    public void buildUnitsList() throws InfrastructureException {
        Collection units = daoFactory.getUnitDAO().findAll(false);
        stockItemForm.setUnits(units);
    }

    public void buildObjectCodesList() throws InfrastructureException {
        Collection objectCodes = daoFactory.getObjectCodeDAO().findAll(ObjectCode.class);
        stockItemForm.setObjectCodes(objectCodes);
    }

    public void buildObjectCode() throws InfrastructureException {
        if(stockItem != null && stockItem.getObjectCode() != null) {
            stockItemForm.setObjectCodeId(stockItem.getObjectCode().getObjectCodeId().toString());
        }
        else {
            stockItemForm.setObjectCodeId("");
        }
    }

    public void buildManufacturersList() throws InfrastructureException {
        Collection manufacturers = daoFactory.getManufacturerDAO().findAll(false);
        stockItemForm.setManufacturers(manufacturers);
    }

    public void buildManufacturer() throws InfrastructureException {
        if(stockItem != null && stockItem.getManufacturer() != null) {
            stockItemForm.setManufacturerId(stockItem.getManufacturer().getManufacturerId().toString());
        }
        else {
            stockItemForm.setManufacturerId("");
        }
    }

    public void buildDispenseUnit() throws InfrastructureException {
        if(stockItem != null && stockItem.getDispenseUnit() != null) {
            stockItemForm.setDispenseUnitId(stockItem.getDispenseUnit().getUnitId().toString());
        }
        else {
            stockItemForm.setDispenseUnitId("");
        }
    }

    public void buildItemVendorForms() throws ReflectivePropertyException {
        for (Iterator iterator = stockItem.getItemVendors().iterator(); iterator.hasNext();) {
            ItemVendor itemVendor = (ItemVendor) iterator.next();
            ItemVendorForm itemVendorForm = new ItemVendorForm();
            itemVendorForm.setItemVendorId(itemVendor.getItemVendorId());
            PropertyUtils.copyProperties(itemVendorForm,itemVendor);
            Unit buyUnit = itemVendor.getBuyUnit();
            if (buyUnit!=null) {
                itemVendorForm.setBuyUnitId(buyUnit.getUnitId());
            }
            itemVendorForm.setVendorName(itemVendor.getVendor().getExternalOrgDetail().getOrgName());
            stockItemForm.getItemVendorForms().add(itemVendorForm);
            if(itemVendor.getPrimaryVendor().booleanValue()){
                stockItemForm.setPrimaryVendorKey(""+itemVendor.getItemVendorId().hashCode());
            }
        }
    }
}