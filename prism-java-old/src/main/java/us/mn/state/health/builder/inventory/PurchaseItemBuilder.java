package us.mn.state.health.builder.inventory;

import java.util.Date;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Manufacturer;
import us.mn.state.health.model.common.ObjectCode;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.common.VendorContract;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.view.inventory.ItemForm;

public class PurchaseItemBuilder {    
    protected DAOFactory daoFactory;
    private Item item;
    private ItemForm itemForm;
    private User user;
    
    public PurchaseItemBuilder(Item item, 
                               ItemForm itemForm, 
                               User user,
                               DAOFactory daoFactory) {            
        this.item = item;
        this.itemForm = itemForm;
        this.user = user;
        this.daoFactory = daoFactory;        
    }
    
    public void buildCategory() {
        Category category = (Category) CollectionUtils.getObjectFromCollectionById(itemForm.getCategories(),
                                                                                   itemForm.getCategoryId(), 
                                                                                   "categoryId");
        if(category==null) {
            category = (Category) CollectionUtils.getObjectFromCollectionById(itemForm.getCategories(),
                                                                                   itemForm.getCategoryCode(),
                                                                                   "categoryCode");
        }
        item.setCategory(category);
    }

    public void buildDispenseUnit() {
        item.setDispenseUnit(itemForm.getDispenseUnit());
    }
    
    /**
     * This method sets all the 'primitive' types of the item from the form... 
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(item, itemForm);
        } 
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building Item: ", rpe);
        }
    }
    
    public void buildObjectCode() throws InfrastructureException {
        ObjectCode objectCode = null;
        if(!StringUtils.nullOrBlank(itemForm.getObjectCodeId())) {
            objectCode = itemForm.getObjectCode();
        } 
        item.setObjectCode(objectCode);
    }
    
    public void buildManufacturer() throws InfrastructureException {
        Manufacturer manufacturer = null;
        Manufacturer manufacturerUnkwn = daoFactory.getManufacturerDAO().findManufacturerByCode(Manufacturer.CODE_UNKNOWN);
        if (StringUtils.nullOrBlank(itemForm.getManufacturerId())
                || itemForm.getManufacturerId().equals(manufacturerUnkwn.getManufacturerId().toString())) {
            manufacturer = manufacturerUnkwn;
        } 
        else {
            manufacturer = itemForm.getManufacturer();
        }
        item.setManufacturer(manufacturer);
    }
    
    public void buildAddItemVendor() throws InfrastructureException {
        Vendor selectedVendor = itemForm.getAvailableVendor();
        if (selectedVendor!=null) {
            item.addVendor(selectedVendor, user.getUsername());
        }
    }
    
    public void buildRemoveItemVendor() throws InfrastructureException {
        if (!StringUtils.nullOrBlank(itemForm.getItemVendorId())) {
            CollectionUtils.removeMatchingItem(item.getItemVendors(),
                                               itemForm.getItemVendorId(),
                                               "vendor.vendorId");
        }
    }
    
    public void buildVendorContract() throws InfrastructureException {    
        if(itemForm.getItemVendor() != null) {
            Long vendorId = itemForm.getItemVendor().getVendor().getVendorId();
            ItemVendor itemVendor = 
                    (ItemVendor)CollectionUtils.getObjectFromCollectionById(item.getItemVendors(), vendorId, "vendor.vendorId");

            VendorContract vendorContract = itemForm.getContract();
            
            if(itemForm.getContractType().equals("")) {
                if(itemVendor.getVendorContract() != null) {
                    itemForm.setContractType(ItemForm.CONTRACT_TYPE_NEW);
                }
                else {
                    itemForm.setContractType(ItemForm.CONTRACT_TYPE_NONE);
                }
            }
            else {
                if(itemForm.getContractType().equals(ItemForm.CONTRACT_TYPE_NEW)) {
                    vendorContract = new VendorContract();
                    itemVendor.setVendorContract(vendorContract);
                }
                else if(itemForm.getContractType().equals(ItemForm.CONTRACT_TYPE_EXISTING)) {
                    if(vendorContract.getVendorContractId().longValue() == 
                        itemVendor.getVendorContract().getVendorContractId().longValue()) {
                        vendorContract = itemVendor.getVendorContract();
                    }
                    else {
                        itemVendor.setVendorContract(vendorContract);
                    }
                }
                else {
                    vendorContract = itemVendor.getVendorContract();
                    itemVendor.setVendorContract(null);
                    if(vendorContract!=null){
                        daoFactory.getVendorContractDAO().makeTransient(vendorContract);
                    }
                }
                
                if(itemForm.getContractType().equals(ItemForm.CONTRACT_TYPE_EXISTING) ||
                    itemForm.getContractType().equals(ItemForm.CONTRACT_TYPE_NEW)) {
                    
                    vendorContract.setVendor(itemVendor.getVendor());
                    vendorContract.setContractNumber(itemForm.getContractNumber());
                    vendorContract.setDeliveryTerms(itemForm.getContractDeliveryTerms());
                    try {
                        Date contractStartDate = null;
                        Date contractEndDate = null;
                        if(!StringUtils.nullOrBlank(itemForm.getContractStartDate())) {
                            contractStartDate = DateUtils.createDate(itemForm.getContractStartDate());
                        }
                        if(!StringUtils.nullOrBlank(itemForm.getContractEndDate())) {
                            contractEndDate = DateUtils.createDate(itemForm.getContractEndDate());
                        }
                        vendorContract.setStartDate(contractStartDate);
                        vendorContract.setEndDate(contractEndDate);
                    }
                    catch(Exception e) {
                        throw new InfrastructureException(e);
                    }    
                }
            }
        }
    }

    public void buildDefaultDeliveryDetail() throws InfrastructureException {
        item.setDeliveryDetail(itemForm.getDeliveryDetail());
    }

        public void buildMetaProperties() {
        if(item.getItemId() == null) {
            item.setInsertedBy(user.getUsername());
            item.setInsertionDate(new Date());
        }
        else {
            item.setLastUpdatedBy(user.getUsername());
            item.setLastUpdatedDate(new Date());
        }
    }    
}