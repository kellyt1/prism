package us.mn.state.health.view.purchasing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.lang.WrapperUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.model.purchasing.AssetType;
import us.mn.state.health.model.purchasing.OrderLineItem;

public class OrderLineItemForm extends ValidatorForm {
    private String orderLineItemId;
    private Integer lineItemNumber;
    private Item item;
    private String itemDescription;
    private String discountPercent;
    private String buyUnitId;
    private Collection units = new ArrayList();
    private String buyUnitCost;
    private String dispenseUnitsPerBuyUnit;
    private String quantity; //the total number of buyUnits being purchased
    private String quantityReceived;                    //this will end up in a ReceivingDetail object
    private String quantityReceivedInDispenseUnits;     //qty received in terms of dispense Units
    private String assetNumber;                             //aka the 'bin' at the receiving facility
    private String statusId;
    private String statusCode;
    private Collection statuses = new ArrayList();
    //private Collection orderLineItemNoteForms = new ArrayList();  moved to Order
    //private String shipToAddressId;                               moved to Order
    //private Collection addresses = new ArrayList();               moved to Order
    //private String otherShipToAddress;                            moved to Order
    private String commodityCode;
    private Collection requestLineItemForms = new ArrayList();
    private Boolean selected = Boolean.FALSE;
    private OrderLineItem orderLineItem;
    private String assetsType = "";
    private Boolean removed = Boolean.FALSE;
    private Boolean dirty = Boolean.FALSE;
    private boolean odd = true;
    private Boolean showDetail = Boolean.FALSE;
    private Boolean unitMismatch = Boolean.FALSE;
    private String vendorCatalogNbr;
    private Collection assetTypes = new ArrayList();
    private AssetType assetTypeEnum;



    /** Resets OrderLineItem form values */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        selected = Boolean.FALSE;
        odd = true;
    }

    /** Validates OrderLineItem form values */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }
    
    public boolean getOdd() {
        return odd;
    }
    
    public String getFlip() {
        if(odd) {
            odd = false;
        }
        else {
            odd = true;
        }
        return "";
    }
    
    public Double getBuyUnitTotalCost() {
        try {
            if(!StringUtils.nullOrBlank(buyUnitCost) && !StringUtils.nullOrBlank(quantity)) {
                double cost = Double.parseDouble(buyUnitCost)  * Double.parseDouble(quantity);
                if(!StringUtils.nullOrBlank(discountPercent)) {
                    double discount = new Double("." + discountPercent).doubleValue();
                    cost = cost * (1d - discount);
                }
                return new Double(cost);
            }
        }
        catch(Exception e) {
        }
        return new Double(0);
    }

   /** Accessor Methods */
   
    public Unit getBuyUnit() {
       return (Unit)CollectionUtils.getObjectFromCollectionById(units, buyUnitId, "unitId");
    }
   
    public Status getStatus() {
       return (Status)CollectionUtils.getObjectFromCollectionById(statuses, statusCode, "statusCode");
    }
   
   /* moved ship to address to Order
    public AddressBookExternalSource getShipToAddress() {
        return (AddressBookExternalSource)CollectionUtils.
            getObjectFromCollectionById(addresses, shipToAddressId, 
                "addressBookExternalSourceId");
    }
    */

    public void setOrderLineItemId(String orderLineItemId) {
        this.orderLineItemId = orderLineItemId;
    }


    public String getOrderLineItemId() {
        return orderLineItemId;
    }


    public void setItem(Item item) {
        this.item = item;
    }


    public Item getItem() {
        return item;
    }


    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = StringUtils.trim(discountPercent);
    }


    public String getDiscountPercent() {
        return discountPercent;
    }


    public void setBuyUnitId(String buyUnitId) {
        this.buyUnitId = buyUnitId;
    }


    public String getBuyUnitId() {
        return buyUnitId;
    }


    public void setUnits(Collection units) {
        this.units = units;
    }


    public Collection getUnits() {
        return units;
    }


    public void setBuyUnitCost(String buyUnitCost) {
        this.buyUnitCost = StringUtils.stripChar(buyUnitCost, ",");
    }


    public String getBuyUnitCost() {
//        return StringUtils.formatCurrency(buyUnitCost);
        return buyUnitCost;
    }


    public void setDispenseUnitsPerBuyUnit(String dispenseUnitsPerBuyUnit) {
        this.dispenseUnitsPerBuyUnit = StringUtils.trim(dispenseUnitsPerBuyUnit);
    }


    public String getDispenseUnitsPerBuyUnit() {
        return dispenseUnitsPerBuyUnit;
    }


    public void setQuantity(String qty) {
        this.quantity = StringUtils.trim(qty);
    }


    public String getQuantity() {
        return quantity;
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

    /* notes were noved to the Order level per stakeholders
    public void setOrderLineItemNoteForms(Collection orderLineItemNoteForms) {
        this.orderLineItemNoteForms = orderLineItemNoteForms;
    }

    public Collection getOrderLineItemNoteForms() {
        return orderLineItemNoteForms;
    }
    */

    /* moved to OrderForm
    public void setShipToAddressId(String shipToAddressId) {
        this.shipToAddressId = shipToAddressId;
    }


    public String getShipToAddressId() {
        return shipToAddressId;
    }
    */


    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }


    public String getCommodityCode() {
        return commodityCode;
    }


    public void setRequestLineItemForms(Collection requestLineItemForms) {
        this.requestLineItemForms = requestLineItemForms;
    }


    public Collection getRequestLineItemForms() {
        return requestLineItemForms;
    }


    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }


    public String getItemDescription() {
        return itemDescription;
    }

    /* moved addresses to Order form
    public void setAddresses(Collection addresses) {
        this.addresses = addresses;
    }


    public Collection getAddresses() {
        return addresses;
    }
    */
    
    public String getTotalCost() {
        try {
            if(!StringUtils.nullOrBlank(buyUnitCost) && !StringUtils.nullOrBlank(quantity)) {
                double cost = new Double(buyUnitCost).doubleValue() * new Double(quantity).doubleValue();
                if(!StringUtils.nullOrBlank(discountPercent)) { 
                    double discount = WrapperUtils.percentToNumber(new Double(discountPercent)).doubleValue();
                    cost = cost * (1d - discount);
                }
                return new Double(cost).toString();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Boolean getSelected() {
        return selected;
    }
    
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
    
    public String getKey() {
        return this.toString();
    }

    /* moved addresses to OrderForm
    public void setOtherShipToAddress(String otherShipToAddress) {
        this.otherShipToAddress = otherShipToAddress;
    }


    public String getOtherShipToAddress() {
        return otherShipToAddress;
    }
    */

    public void setOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItem = orderLineItem;
    }


    public OrderLineItem getOrderLineItem() {
        return orderLineItem;
    }

    public void setAssetsType(String assetsType) {
        this.assetsType = assetsType;
        if(assetsType != null){
            boolean isAssetTypeEnum = !assetsType.equalsIgnoreCase("") && !assetsType.equalsIgnoreCase("SENSITIVE") && !assetsType.equalsIgnoreCase("FIXED");
            if(isAssetTypeEnum){
                this.assetTypeEnum = AssetType.valueOf(assetsType);
            }
        }
    }


    public String getAssetsType() {
        String result = null;
        if(assetsType != null) {
            result = assetsType.equals(AssetType.NONASSET.name()) ? null : assetsType;
        }
        return result;
    }


    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }


    public String getStatusCode() {
        return statusCode;
    }


    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }


    public Boolean getRemoved() {
        return removed;
    }


    public void setDirty(Boolean dirty) {
        this.dirty = dirty;
    }


    public Boolean getDirty() {
        return dirty;
    }


    public void setQuantityReceived(String quantityReceived) {
        this.quantityReceived = StringUtils.trim(quantityReceived);
    }


    public String getQuantityReceived() {
        return quantityReceived;
    }


    public void setShowDetail(Boolean showDetail) {
        this.showDetail = showDetail;
    }


    public Boolean getShowDetail() {
        return showDetail;
    }
    
    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }
    public String getAssetNumber() {
        return this.assetNumber;
    }


    public void setQuantityReceivedInDispenseUnits(String quantityReceivedInDispenseUnits) {
        this.quantityReceivedInDispenseUnits = StringUtils.trim(quantityReceivedInDispenseUnits);
    }


    public String getQuantityReceivedInDispenseUnits() {
        return quantityReceivedInDispenseUnits;
    }

    public Boolean getUnitMismatch() {
        if(orderLineItem != null && orderLineItem.getItem() != null && orderLineItem.getBuyUnit() != null) {
            if(!orderLineItem.getItem().getDispenseUnit().getCode().equals(orderLineItem.getBuyUnit().getCode())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }


    public void setVendorCatalogNbr(String vendorCatalogNbr) {
        this.vendorCatalogNbr = vendorCatalogNbr;
    }


    public String getVendorCatalogNbr() {
        return vendorCatalogNbr;
    }


    public void setLineItemNumber(Integer lineItemNumber) {
        this.lineItemNumber = lineItemNumber;
    }


    public Integer getLineItemNumber() {
        return lineItemNumber;
    }

    /* moved addresses to OrderForm
    public boolean getValidShipToAddress() {
        return (getShipToAddress() != null || !StringUtils.nullOrBlank(otherShipToAddress));
    }
    */

    public Collection getAssetTypes() {
        return assetTypes;
    }

    public void setAssetTypes(Collection assetTypes) {
        this.assetTypes = assetTypes;
    }

    public AssetType getAssetTypeEnum() {
        return assetTypeEnum;
    }
}
