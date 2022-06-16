package us.mn.state.health.model.purchasing;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.WrapperUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.ModelMember;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.receiving.ReceivingDetail;
import us.mn.state.health.model.util.search.EntityIndex;
import us.mn.state.health.model.util.search.OrderLineItemIndex;

import javax.persistence.Transient;

//public class OrderLineItem extends ModelMember implements Comparable, Searchable {
public class OrderLineItem extends ModelMember implements Comparable {
    private static Log log = LogFactory.getLog(OrderLineItem.class);
    private Long orderLineItemId;
    private Integer lineItemNumber;         //the sequential number given to this OLI, within the order. i.e., 1, 2, 3, 4, 5...)
    private Item item;
    private String itemDescription;
    private Double discountPercent;
    private Unit buyUnit;
    private Double buyUnitCost;
    private Integer dispenseUnitsPerBuyUnit;
    private Integer quantity;           //the total number of buyUnits being purchased
    private Collection receivingDetails = new TreeSet();
    private Collection requestLineItems = new HashSet();
    private Status status;
    private String commodityCode;
    private int version;
    private Order order;
    private String assetsType;
    private String assetNumber;
    private String internalNotes;
    private String vendorCatalogNbr;
    private Date assetsEmailSent;

    public static final String FIXED_ASSET_TYPE = "FIXED";
    public static final String SENSITIVE_ASSET_TYPE = "SENSITIVE";
    public static final String ORDER_ITEM_TYPE_STK = "Stock Item";
    public static final String ORDER_ITEM_TYPE_PUR = "Purchase Item";
    public static final String ORDER_ITEM_TYPE_NONCAT = "Non Catalog Item";
    public static final List<AssetType> assetTypes = AssetType.getAssetTypes();

    @Transient
    public Double getBuyUnitTotalCost() {
        try {
            double cost = buyUnitCost * quantity.doubleValue();
            if (discountPercent != null) {
                double discount = WrapperUtils.percentToNumber(discountPercent);
                cost = cost * (1d - discount);
            }
            return cost;
        }
        catch (Exception e) {
            log.error("getBuyUnitTotalCost(): " + e);
        }
        return 0.0;
    }

    /* Infrastructure Methods */
    public int compareTo(Object object) {
        OrderLineItem that = (OrderLineItem) object;
        if (that.getOrderLineItemId() != null && this.getOrderLineItemId() != null) {
            return this.getOrderLineItemId().compareTo(that.getOrderLineItemId());
        }
        return this.toString().compareTo(that.toString());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderLineItem)) return false;
        final OrderLineItem that = (OrderLineItem) o;

        if (this.getOrderLineItemId() == null) {
            if (that.getOrderLineItemId() == null) {
                //dig deeper, using comparison by value
                if (commodityCode != null ? !commodityCode.equals(that.getCommodityCode()) : that.getCommodityCode() != null) {
                    return false;
                }
                if (item == null ^ that.getItem() == null) {
                    return false;
                }
                if (item != null && that.getItem() != null) {
                    if (!item.getItemId().equals(that.getItem().getItemId())) {
                        return false;
                    }
                }
                if (itemDescription != null ? !itemDescription.equals(that.getItemDescription()) : that.getItemDescription() != null) {
                    return false;
                }
                if (order != null ? !order.equals(that.getOrder()) : that.getOrder() != null) {
                    return false;
                }
                if (status != null ? !status.equals(that.getStatus()) : that.getStatus() != null) {
                    return false;
                }
                //if all the above tests pass, return true
                return true;
            } else {
                //if one ID is null, and the other is not null, they can't be the same
                return false;
            }
        } else {  //we know we can't get a NullPointerException now...
            return this.getOrderLineItemId().equals(that.getOrderLineItemId());
        }
    }

    /**
     * Never use the ID's in hashCode()!
     *
     * @return hashCode
     */
    public int hashCode() {
        int result = 13;
        result = 29 * result + (order != null ? order.hashCode() : 0);
        return result;
    }

    /* Accessor Methods */

    public void setOrderLineItemId(Long orderLineItemId) {
        this.orderLineItemId = orderLineItemId;
    }

    public Long getOrderLineItemId() {
        return orderLineItemId;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setBuyUnit(Unit buyUnit) {
        this.buyUnit = buyUnit;
    }

    public Unit getBuyUnit() {
        return buyUnit;
    }

    public void setBuyUnitCost(Double buyUnitCost) {
        this.buyUnitCost = buyUnitCost;
    }

    public Double getBuyUnitCost() {
        return buyUnitCost;
    }

    public void setDispenseUnitsPerBuyUnit(Integer dispenseUnitsPerBuyUnit) {
        this.dispenseUnitsPerBuyUnit = dispenseUnitsPerBuyUnit;
    }

    public Integer getDispenseUnitsPerBuyUnit() {
        return dispenseUnitsPerBuyUnit;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setRequestLineItems(Collection requestLineItems) {
        this.requestLineItems = requestLineItems;
    }

    public Collection getRequestLineItems() {
        return requestLineItems;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setAssetsType(String assetsType) {
        this.assetsType = assetsType;
    }

    public String getAssetsType() {
        return assetsType;
    }

    public void setReceivingDetails(Collection receivingDetails) {
        this.receivingDetails = receivingDetails;
    }

    public Collection getReceivingDetails() {
        return receivingDetails;
    }

    /**
     * get total quantity received; a calculated field.  The sum
     * of all ReceivingDetail.qtyReceived
     *
     * @return totalQtyReceived
     */
    @Transient
    public Integer getTotalQtyReceived() {
        int sum = 0;
        for(Object o : getReceivingDetails()){
            ReceivingDetail rd = (ReceivingDetail) o;
            sum += rd.getQuantityReceived();
        }

        return sum;
    }

    /**
     * Returns list of all funding sources for all Request Line Items
     * associated with this Order Line Item. List is sorted by Org Budget code
     *
     * @return allReqLnItmFundingSources
     */
    public Collection getAllReqLnItmFundingSources() {
        Collection reqLnItmFundingSources = new ArrayList();
        for (Iterator i = requestLineItems.iterator(); i.hasNext();) {
            RequestLineItem reqLnItm = (RequestLineItem) i.next();
            reqLnItmFundingSources.addAll(reqLnItm.getFundingSources());
        }
        try {
            CollectionUtils.sort(reqLnItmFundingSources, "orgBudget.orgBudgetCode", true);
        }
        catch (Exception ignore) {
        }

        return reqLnItmFundingSources;
    }

    public EntityIndex getEntityIndex() throws InfrastructureException {
        return new OrderLineItemIndex();
    }

    @Transient
    public String getItemType() {
        return (getItem() != null) ? (getItem() instanceof StockItem) ? ORDER_ITEM_TYPE_STK : ORDER_ITEM_TYPE_PUR : ORDER_ITEM_TYPE_NONCAT;
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

    public String toString() {
        return "OrderLineItem{" + "orderLineItemId=" + orderLineItemId + ", itemDescription='" + itemDescription + '\'' + '}';
    }

    @Transient
    public Boolean getReceived() {
        return this.getStatus().getStatusCode().equals(Status.RECEIVED) || this.getStatus().getStatusCode().equals(Status.RECEIVED_PARTIAL);
    }

    public RequestLineItem getRequestLineItem() {
        return (requestLineItems.size() > 0) ? (RequestLineItem) requestLineItems.iterator().next() : null;
    }

    @Transient
    public boolean getItemIsStockItem() {
        return this.getItem() != null && this.getItem().getItemType().equals(Item.STOCK_ITEM);
    }

    @Transient
    public String getFullIcnbr() {
        Item item = this.getItem();
        try {
            if (item != null && item.getItemType().equals(Item.STOCK_ITEM)) {
                return (DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getStockItemDAO().getStockItemById(item.getItemId(), false)).getFullIcnbr() + "\n";
            }
        } catch (InfrastructureException e) {
            log.error(e);
        }
        return "";
    }

    public Date getAssetsEmailSent() {
        return assetsEmailSent;
    }

    public void setAssetsEmailSent(Date assetsEmailSent) {
        this.assetsEmailSent = assetsEmailSent;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }
}