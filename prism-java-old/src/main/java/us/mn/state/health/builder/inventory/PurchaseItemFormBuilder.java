package us.mn.state.health.builder.inventory;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.ObjectCode;
import us.mn.state.health.model.common.VendorContract;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.view.inventory.ItemForm;

public class PurchaseItemFormBuilder  {
    private ItemForm itemForm;
    private Item item;
    private DAOFactory daoFactory;
    
    public PurchaseItemFormBuilder(ItemForm itemForm, DAOFactory daoFactory) {
        this.itemForm = itemForm;
        this.daoFactory = daoFactory;
    }
    
    public PurchaseItemFormBuilder(ItemForm itemForm, Item item, DAOFactory daoFactory) {
        this(itemForm, daoFactory);
        this.item = item;
    }
    
    public void buildDefaultProperties() {
        itemForm.setEconomicOrderQty("0");
        itemForm.setAnnualUsage("0");
    }
    
    public void buildCategories() throws InfrastructureException {
        Collection categories = daoFactory.getCategoryDAO().findDescendantCategoriesByParentCode(Category.MATERIALS, false);
        itemForm.setCategories(categories);
    }
    
    public void buildCategoriesForITPurchases() throws InfrastructureException {
        Collection categories = daoFactory.getCategoryDAO().findDescendantCategoriesByParentCode(Category.MATERIALS_STANDARD_COMPUTERS, false);
//        categories.addAll(daoFactory.getCategoryDAO().findDescendantCategoriesByParentCode(Category.MATERIALS_STANDARD_COMPUTERS, false));
        //todo - check this IT purchase category part
        categories.addAll(daoFactory.getCategoryDAO().findDescendantCategoriesByParentCode(Category.MATERIALS_STANDARD_COMPUTER_ACCESSORY, false));
        categories.addAll(daoFactory.getCategoryDAO().findDescendantCategoriesByParentCode(Category.MATERIALS_STANDARD_SOFTWARE, true));
        itemForm.setCategories(categories);
    }

    public void buildUnits() throws InfrastructureException {
        Collection units = daoFactory.getUnitDAO().findAll(false);
        itemForm.setUnits(units);
    }
    
    public void buildManufacturers() throws InfrastructureException {
        Collection manufacturers = daoFactory.getManufacturerDAO().findAll(false);
        itemForm.setManufacturers(manufacturers);
    }

    public void buildAvailableVendors() throws InfrastructureException {
        Collection availableVendors = null;
        if(item != null) {
            availableVendors = daoFactory.getVendorDAO().findVendorWhereNotWithItemId(item.getItemId(), true);
        }
        else {
//            availableVendors = daoFactory.getVendorDAO().findAll(true);
            availableVendors = daoFactory.getVendorDAO().findAll();
        }
        itemForm.setAvailableVendors(availableVendors);
    }

    public void buildItemVendors() throws InfrastructureException {
        if(item != null) {
            itemForm.setItemVendors(item.getItemVendors());
        }
    }
    
    public void buildItemVendorContractDetail() throws InfrastructureException {    
        itemForm.setContractDeliveryTerms(null);
        itemForm.setContractEndDate(null);
        itemForm.setContractId(null);
        itemForm.setContractNumber(null);
        itemForm.setContractStartDate(null);
        
        if(itemForm.getItemVendor() != null) {
            Long vendorId = itemForm.getItemVendor().getVendor().getVendorId();
            ItemVendor itemVendor = (ItemVendor)CollectionUtils.getObjectFromCollectionById(item.getItemVendors(), vendorId, "vendor.vendorId");
            VendorContract vendorContract = itemVendor.getVendorContract();
            Collection contracts = daoFactory.getItemDAO().findContractsWhereWithVendor(itemVendor.getVendor().getVendorId());
            itemForm.setContracts(contracts);
            if(vendorContract != null) {
                itemForm.setContractId(vendorContract.getVendorContractId().toString());
                itemForm.setContractDeliveryTerms(vendorContract.getDeliveryTerms());
                try {
                    itemForm.setContractEndDate(DateUtils.toString(vendorContract.getEndDate()));
                    itemForm.setContractStartDate(DateUtils.toString(vendorContract.getStartDate()));
                }
                catch(Exception e) {
                }
                itemForm.setContractId(vendorContract.getVendorContractId().toString());
                itemForm.setContractNumber(vendorContract.getContractNumber());
                itemForm.setContractType(ItemForm.CONTRACT_TYPE_EXISTING);
            }
            else {
                itemForm.setContractType(ItemForm.CONTRACT_TYPE_NONE);
            }
        }

    }

    public void buildDeliveryDetail() throws InfrastructureException {
        if(item.getDeliveryDetail() != null){
            itemForm.setDeliveryDetail(item.getDeliveryDetail());
        }
    }
    
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(itemForm, item);
            if(item.getDispenseUnit() != null) {
                itemForm.setDispenseUnitId(item.getDispenseUnit().getUnitId().toString());
            }
            if(item.getManufacturer() != null) {
                itemForm.setManufacturerId(item.getManufacturer().getManufacturerId().toString());
            }
            if(item.getCategory() != null) {
                itemForm.setCategoryId(item.getCategory().getCategoryId().toString());
            }
            if(item.getObjectCode() != null) {
                itemForm.setObjectCodeId(item.getObjectCode().getObjectCodeId().toString());
            }
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building Item Form: ", rpe);
        }
    }
    

    public void buildAttachedFiles() {

            if(item != null && item.getAttachedFiles() != null) {
                itemForm.setAttachedFiles(item.getAttachedFiles());
            }
    }

    public void buildObjectCodesList() throws InfrastructureException {
        Collection objectCodes = daoFactory.getObjectCodeDAO().findAll(ObjectCode.class);
        itemForm.setObjectCodes(objectCodes);
    }
}