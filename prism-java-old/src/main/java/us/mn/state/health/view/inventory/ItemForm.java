package us.mn.state.health.view.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;

import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.upload.FormFile;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.inventory.HibernateUnitDAO;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Manufacturer;
import us.mn.state.health.model.common.ObjectCode;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.common.VendorContract;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.model.inventory.AttachedFile;
import us.mn.state.health.model.materialsrequest.DeliveryDetail;
import us.mn.state.health.view.materialsrequest.DeliveryDetailForm;

public class ItemForm extends ValidatorForm {
    private String availabilityId;
    private String description;
    private String descriptionForUser;
    private Boolean hazardous;
    private String economicOrderQty;
    private Collection units = new ArrayList(); //Units
    private String dispenseUnitId;
    private String dispenseUnitCost = "0";
    private String itemId;
    private String itemVendorId;
    private String availableVendorId;
    private DeliveryDetail deliveryDetail;
    private DeliveryDetailForm deliveryDetailForm;
    /**
     * @deprecated use itemVendorForms instead
     */
    private Collection itemVendors = new ArrayList(); //Item Vendor beans

    private Collection itemVendorForms = new ArrayList(); //colection of ItemVendorForm's
    private ItemVendorForm itemVendorForm = new ItemVendorForm();
    private String primaryVendorKey;
    private Collection availableVendors = new ArrayList(); //Vendor beans
    private String categoryId;
    private String categoryCode;
    private Collection categories = new ArrayList();
    private String manufacturerId;
    private Collection manufacturers = new ArrayList();
    private String estimatedAnnualUsage;
    private String buyUnitId;
    private String cmd = "";
    private Item item;
    private Boolean selected = Boolean.FALSE;
    private String annualUsage;
    private String objectCodeId;
    private Collection objectCodes = new ArrayList();
    private String model;

    //Vendor Contract Details
    private String currentItemVendorKey;
    private String contractNumber;
    private String contractDeliveryTerms;
    private String contractStartDate;
    private String contractEndDate;
    private String input = "";
    private String contractType = "";

    public static final String CONTRACT_TYPE_EXISTING = "EXISTING";
    public static final String CONTRACT_TYPE_NEW = "NEW";
    public static final String CONTRACT_TYPE_NONE = "NONE";
    public String lastContractId;
    public String contractId;
    public Collection contracts = new ArrayList();
    private Boolean endDatePurchaseItem = false;


    public void resetVendorContractDetails() {
        contractNumber = null;
        contractDeliveryTerms = null;
        contractStartDate = null;
        contractEndDate = null;
        contractType = "";
        contractId = null;
    }

    private FormFile printSpecFile;
    private Collection<AttachedFile> attachedFiles = new HashSet<AttachedFile>();

    public Collection<AttachedFile> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(Collection<AttachedFile> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    /**
     * Set a representation of the file the user has uploaded
     */
    public void setPrintSpecFile(FormFile printSpecFile) throws InfrastructureException {
        this.printSpecFile = printSpecFile;
//        editStockItemFacade.updatePrintSpecFileInfo(getStockItem(), printSpecFile);
//        if (printSpecFile.getFileName() != null &&  !printSpecFile.getFileName().trim().equals("")) {
//            editStockItemFacade.storeAttachFileInfo(getStockItem(), this.printSpecFile);
//        }
    }

    /**
     * Retrieve a representation of the file the user has uploaded
     */
    public FormFile getPrintSpecFile() {
        return printSpecFile;
    }
    

    public ItemVendorForm getItemVendorForm() {
        return itemVendorForm;
    }

    public void setItemVendorForm(ItemVendorForm itemVendorForm) {
        this.itemVendorForm = itemVendorForm;
    }

    public Collection getItemVendorForms() {
        return itemVendorForms;
    }

    public void setItemVendorForms(Collection itemVendorForms) {
        this.itemVendorForms = itemVendorForms;
    }

    public String getPrimaryVendorKey() {
        return primaryVendorKey;
    }

    public void setPrimaryVendorKey(String primaryVendorKey) {
        this.primaryVendorKey = primaryVendorKey;
    }

    public VendorContract getContract() {
        return (VendorContract) CollectionUtils.getObjectFromCollectionById(contracts, contractId, "vendorContractId");
    }

    public Category getCategory() {
        return (Category) CollectionUtils.getObjectFromCollectionById(categories, categoryId, "categoryId");
    }

    public Vendor getAvailableVendor() {
        return (Vendor) CollectionUtils.getObjectFromCollectionById(availableVendors, availableVendorId, "vendorId");
    }

    public ItemVendor getItemVendor() {
        for (Iterator i = itemVendors.iterator(); i.hasNext();) {
            ItemVendor itemVendor = (ItemVendor) i.next();
            String vendorId = itemVendor.getVendor().getVendorId().toString();
            if (vendorId.equals(itemVendorId)) {
                return itemVendor;
            }
        }
        return null;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Unit getDispenseUnit() {
        return (Unit) CollectionUtils.getObjectFromCollectionById(units, dispenseUnitId, "unitId");
    }


    public Unit getBuyUnit() throws InfrastructureException {
        if (!buyUnitId.equals("")) {
            return (Unit) CollectionUtils.getObjectFromCollectionById(units, buyUnitId, "unitId");
        } else {
            return new HibernateUnitDAO().findUnitByCode("UNKWN");
        }
    }

    public String getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(String availabilityId) {
        this.availabilityId = availabilityId;
    }

    public Manufacturer getManufacturer() {
        return (Manufacturer) CollectionUtils.getObjectFromCollectionById(manufacturers, manufacturerId, "manufacturerId");
    }

    public void setEstimatedAnnualUsage(String estimatedAnnualUsage) {
        this.estimatedAnnualUsage = estimatedAnnualUsage;
    }

    public String getEstimatedAnnualUsage() {
        return estimatedAnnualUsage;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getDescription() {
        return description;
    }

    public String getDescriptionForUser() {
        return descriptionForUser;
    }

    public void setDescriptionForUser(String descriptionForUser) {
        this.descriptionForUser = descriptionForUser;
    }

    public void setHazardous(Boolean hazardous) {
        this.hazardous = hazardous;
    }


    public Boolean getHazardous() {
        return hazardous;
    }


    public void setEconomicOrderQty(String economicOrderQty) {
        this.economicOrderQty = economicOrderQty;
    }


    public String getEconomicOrderQty() {
        return economicOrderQty;
    }

    public void setDispenseUnitId(String dispenseUnitId) {
        this.dispenseUnitId = dispenseUnitId;
    }


    public String getDispenseUnitId() {
        return dispenseUnitId;
    }

//    public void setOurCost(String ourCost) {
//        this.ourCost = ourCost;
//    }
//
//
//    public String getOurCost() {
//        return ourCost;
//    }
//
//
//    public void setDiscount(String discount) {
//        this.discount = discount;
//    }
//
//
//    public String getDiscount() {
//        return discount;
//    }
//
//
//    public void setRetailCost(String retailCost) {
//        this.retailCost = retailCost;
//    }
//
//
//    public String getRetailCost() {
//        return retailCost;
//    }


    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


    public String getItemId() {
        return itemId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
    }


    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturers(Collection manufacturers) {
        this.manufacturers = manufacturers;
    }


    public Collection getManufacturers() {
        return manufacturers;
    }

    public void setCategories(Collection categories) {
        this.categories = categories;
    }


    public Collection getCategories() {
        return categories;
    }

    public void setUnits(Collection units) {
        this.units = units;
    }


    public Collection getUnits() {
        return units;
    }

    public void setAvailableVendors(Collection availableVendors) {
        this.availableVendors = availableVendors;
    }


    public Collection getAvailableVendors() {
        return availableVendors;
    }

    public void setAvailableVendorId(String availableVendorId) {
        this.availableVendorId = availableVendorId;
    }


    public String getAvailableVendorId() {
        return availableVendorId;
    }


    public void setItemVendors(Collection itemVendors) {
        this.itemVendors = itemVendors;
    }


    public Collection getItemVendors() {
        return itemVendors;
    }

    public void setItemVendorId(String itemVendorId) {
        this.itemVendorId = itemVendorId;
    }


    public String getItemVendorId() {
        return itemVendorId;
    }


    public void setCmd(String cmd) {
        this.cmd = cmd;
    }


    public String getCmd() {
        return cmd;
    }


    public void setBuyUnitId(String buyUnitId) {
        this.buyUnitId = buyUnitId;
    }


    public String getBuyUnitId() {
        return buyUnitId;
    }


    public void setItem(Item item) {
        this.item = item;
    }


    public Item getItem() {
        return item;
    }


    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }


    public String getContractNumber() {
        return contractNumber;
    }


    public void setInput(String input) {
        this.input = input;
    }


    public String getInput() {
        return input;
    }


    public void setContractDeliveryTerms(String contractDeliveryTerms) {
        this.contractDeliveryTerms = contractDeliveryTerms;
    }


    public String getContractDeliveryTerms() {
        return contractDeliveryTerms;
    }


    public void setContractStartDate(String contractStartDate) {
        this.contractStartDate = contractStartDate;
    }


    public String getContractStartDate() {
        return contractStartDate;
    }


    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate = StringUtils.trim(contractEndDate);
    }


    public String getContractEndDate() {
        return contractEndDate;
    }


    public void setAnnualUsage(String annualUsage) {
        this.annualUsage = StringUtils.trim(annualUsage);
    }


    public String getAnnualUsage() {
        return annualUsage;
    }


    public void setCurrentItemVendorKey(String currentItemVendorKey) {
        this.currentItemVendorKey = currentItemVendorKey;
    }


    public String getCurrentItemVendorKey() {
        return currentItemVendorKey;
    }


    public void setContractType(String contractType) {
        this.contractType = contractType;
    }


    public String getContractType() {
        return contractType;
    }


    public void setContractId(String contractId) {
        this.contractId = contractId;
    }


    public String getContractId() {
        return contractId;
    }


    public void setContracts(Collection contracts) {
        this.contracts = contracts;
    }


    public Collection getContracts() {
        return contracts;
    }


    public void setLastContractId(String lastContractId) {
        this.lastContractId = lastContractId;
    }


    public String getLastContractId() {
        return lastContractId;
    }


    public void setObjectCodeId(String objectCodeId) {
        this.objectCodeId = objectCodeId;
    }

    public ObjectCode getObjectCode() {
        return (ObjectCode) CollectionUtils.getObjectFromCollectionById(objectCodes, objectCodeId, "objectCodeId");
    }

    public String getObjectCodeId() {
        return objectCodeId;
    }


    public void setObjectCodes(Collection objectCodes) {
        this.objectCodes = objectCodes;
    }


    public Collection getObjectCodes() {
        return objectCodes;
    }


    public void setModel(String model) {
        this.model = model;
    }


    public String getModel() {
        return model;
    }


    public void setDispenseUnitCost(String dispenseUnitCost) {
        this.dispenseUnitCost = dispenseUnitCost;
    }

    public String getDispenseUnitCost() {
        return dispenseUnitCost;
    }

    public boolean getContractNotRequired() {
        if (CONTRACT_TYPE_NONE.equals(contractType) || StringUtils.nullOrBlank(contractType)) {
            return true;
        }
        return false;
    }

    public Boolean getEndDatePurchaseItem() {
        return endDatePurchaseItem;
    }

    public void setEndDatePurchaseItem(Boolean endDatePurchaseItem) {
        this.endDatePurchaseItem = endDatePurchaseItem;
    }

    public DeliveryDetail getDeliveryDetail() {
        return deliveryDetail;
    }

    public void setDeliveryDetail(DeliveryDetail deliveryDetail) {
        this.deliveryDetail = deliveryDetail;
    }

    public DeliveryDetailForm getDeliveryDetailForm() {
        return deliveryDetailForm;
    }

    public void setDeliveryDetailForm(DeliveryDetailForm deliveryDetailForm) {
        this.deliveryDetailForm = deliveryDetailForm;
    }
}