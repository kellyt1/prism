package us.mn.state.health.matmgmt.action;

import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource;
import us.mn.state.health.model.materialsrequest.RequestLineItem;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: RodenT1a
 * Date: Oct 31, 2008
 * Time: 11:43:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseOrderData {
    //StockItem
    private String vendorName;
    public String billToAddress;
    private String vendorContract;
    private String shipToAddress;
   private String vendorInstructions;
   private String otherBillToAddress;
   private String purchaseOrderNumber;
   private String otherShipToAddress;
   private String purchaser_firstAndLastName;
   private String purchaser_workLandPhone;
   private String purchaser_emailAddress;
   private Long orderId = new Long(0);
   private Double buyUnitsTotalCost= new Double(0);
   private String vendorAccount;
   private String vendorAddress;
   private String orderFiscalYear_2Digits;
   private String remitToAddress;
   private Collection<OrderLineItem>orderLineItems=new ArrayList();

    //Order Line Item
    private Long orderLineItem_orderLineItemId  = new Long(0); // OrderLineItemId();
            private Integer orderLineItem_quantity  = new Integer(0); // Quantity();
            private String orderLineItem_itemDescription; // ItemDescription();
            private String orderLineItem_buyUnit_name; // BuyUnit().getName();
            private Double orderLineItem_discountPercent  = new Double(0); // DiscountPercent();
            private String orderLineItem_commodityCode; // CommodityCode();
            private Double orderLineItem_buyUnitCost = new Double(0);  // BuyUnitCost();
            private Double orderLineItem_buyUnitTotalCost = new Double(0); // BuyUnitTotalCost();
            //private Collection<RequestLineItemFundingSource> orderLineItem_allReqLnItmFundingSources  = new ArrayList(); // AllReqLnItmFundingSources();
            private Collection<FundingSourceData> orderLineItem_allReqLnItmFundingSources  = new ArrayList(); // AllReqLnItmFundingSources();
            private Double orderLineItem_order_buyUnitsTotalCost  = new Double(0); // Order().getBuyUnitsTotalCost();
            private String orderLineItem_status_statusCode; // Status().getStatusCode();
            private String orderLineItem_vendorCatalogNbr; // VendorCatalogNbr();
            private Integer orderLineItem_lineItemNumber  = new Integer(0); // LineItemNumber();
          



    public void addOrder(Order order) {
        if (order.getBillToAddress() != null) {
        this.billToAddress = order.getBillToAddress().getShortSummary();
        }
        if (order.getVendorContract() != null) {
            this.vendorContract = order.getVendorContract().getContractNumber();
        }
        if (order.getShipToAddress() != null) {
          this.shipToAddress = order.getShipToAddress().getShortSummary();
        }
        this.vendorInstructions = order.getVendorInstructions();
        this.otherBillToAddress = order.getOtherBillToAddress();
        this.purchaseOrderNumber = order.getPurchaseOrderNumber();
        this.otherShipToAddress = order.getOtherShipToAddress();
        if (order.getPurchaser() != null) {
            this.purchaser_firstAndLastName = order.getPurchaser().getFirstAndLastName();
            if (order.getPurchaser().getWorkLandPhone() != null) {
                    this.purchaser_workLandPhone = order.getPurchaser().getWorkLandPhone().getNumber();
            }    
            this.purchaser_emailAddress = order.getPurchaser().getEmailAddress();
        }
        this.orderId = order.getOrderId();
        this.buyUnitsTotalCost =   order.getBuyUnitsTotalCost();
        if (order.getVendorAccount() != null) {
            this.vendorAccount = order.getVendorAccount().getAccountNbr();
        }
        if (order.getVendorAddress() != null) {
            this.vendorAddress = order.getVendorAddress().getShortSummary();
        }
        this.orderFiscalYear_2Digits = order.getOrderFiscalYear_2Digits();
        this.orderLineItems = order.getOrderLineItems();
        if (order.getVendor() != null && order.getVendor().getExternalOrgDetail() != null) {
            this.vendorName = order.getVendor().getExternalOrgDetail().getOrgName();
        }
        this.remitToAddress = order.getRemitToAddress();
    }
    public Boolean addOrderLineItemFields(OrderLineItem orderLineItem) {

        boolean swift = false;
        Double totalAmt = new Double(0);
        Double percent = new Double(0);
        Integer numFundingSources = new Integer(0);
        this.orderLineItem_orderLineItemId = orderLineItem.getOrderLineItemId();
        this.orderLineItem_quantity = orderLineItem.getQuantity();
        this.orderLineItem_itemDescription = orderLineItem.getItemDescription();
        if (orderLineItem.getBuyUnit() != null) {
            this.orderLineItem_buyUnit_name = orderLineItem.getBuyUnit().getName();
        }
        this.orderLineItem_discountPercent = orderLineItem.getDiscountPercent();
        this.orderLineItem_commodityCode = orderLineItem.getCommodityCode();
        this.orderLineItem_buyUnitCost = orderLineItem.getBuyUnitCost();
        this.orderLineItem_buyUnitTotalCost = orderLineItem.getBuyUnitTotalCost();
        Collection cFundingSource = orderLineItem.getAllReqLnItmFundingSources();
        for (Iterator iterator = cFundingSource.iterator(); iterator.hasNext();) {
            ++numFundingSources;
            RequestLineItemFundingSource o =  (RequestLineItemFundingSource)iterator.next();
            FundingSourceData cFundingSourceData = new FundingSourceData();
            cFundingSourceData.addFundingSourceData(o);
            totalAmt = totalAmt + cFundingSourceData.getAmount();
        }

        if (numFundingSources > 0) {

            for (Iterator iterator = cFundingSource.iterator(); iterator.hasNext();) {
                RequestLineItemFundingSource o =  (RequestLineItemFundingSource)iterator.next();
                FundingSourceData cFundingSourceData = new FundingSourceData();
                cFundingSourceData.addFundingSourceData(o);

                if(getFy(cFundingSourceData.getOrgBudget_fiscalYear())>=12){
                    swift = true;
                }

                BigDecimal bd = new BigDecimal(100.00 / numFundingSources);
                if (totalAmt > 0) {
                    bd = new BigDecimal(cFundingSourceData.getAmount()/totalAmt * 100);
                }
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                cFundingSourceData.setOrgBudget_percentage("" + bd + '%');
                orderLineItem_allReqLnItmFundingSources.add(cFundingSourceData);
            }
        }
        FundingSourceData fs = new FundingSourceData();
        //this.orderLineItem_allReqLnItmFundingSources = orderLineItem.getAllReqLnItmFundingSources();
        this.orderLineItem_order_buyUnitsTotalCost = orderLineItem.getOrder().getBuyUnitsTotalCost();
        if (orderLineItem.getStatus() != null) {
             this.orderLineItem_status_statusCode = orderLineItem.getStatus().getStatusCode();
         }
        this.orderLineItem_vendorCatalogNbr = orderLineItem.getVendorCatalogNbr();
        this.orderLineItem_lineItemNumber = orderLineItem.getLineItemNumber();
        return swift;
    }

    public String getBillToAddress() {
        return billToAddress;
    }

    public void setBillToAddress(String billToAddress) {
        this.billToAddress = billToAddress;
    }

    public String getVendorContract() {
        return vendorContract;
    }

    public void setVendorContract(String vendorContract) {
        this.vendorContract = vendorContract;
    }

    public String getShipToAddress() {
        return shipToAddress;
    }

    public void setShipToAddress(String shipToAddress) {
        this.shipToAddress = shipToAddress;
    }

    public String getVendorInstructions() {
        return vendorInstructions;
    }

    public void setVendorInstructions(String vendorInstructions) {
        this.vendorInstructions = vendorInstructions;
    }

    public String getOtherBillToAddress() {
        return otherBillToAddress;
    }

    public void setOtherBillToAddress(String otherBillToAddress) {
        this.otherBillToAddress = otherBillToAddress;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public String getOtherShipToAddress() {
        return otherShipToAddress;
    }

    public void setOtherShipToAddress(String otherShipToAddress) {
        this.otherShipToAddress = otherShipToAddress;
    }

    public String getPurchaser_firstAndLastName() {
        return purchaser_firstAndLastName;
    }

    public void setPurchaser_firstAndLastName(String purchaser_firstAndLastName) {
        this.purchaser_firstAndLastName = purchaser_firstAndLastName;
    }

    public String getPurchaser_workLandPhone() {
        return purchaser_workLandPhone;
    }

    public void setPurchaser_workLandPhone(String purchaser_workLandPhone) {
        this.purchaser_workLandPhone = purchaser_workLandPhone;
    }

    public String getPurchaser_emailAddress() {
        return purchaser_emailAddress;
    }

    public void setPurchaser_emailAddress(String purchaser_emailAddress) {
        this.purchaser_emailAddress = purchaser_emailAddress;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getBuyUnitsTotalCost() {
        return buyUnitsTotalCost;
    }

    public void setBuyUnitsTotalCost(Double buyUnitsTotalCost) {
        this.buyUnitsTotalCost = buyUnitsTotalCost;
    }

    public String getVendorAccount() {
        return vendorAccount;
    }

    public void setVendorAccount(String vendorAccount) {
        this.vendorAccount = vendorAccount;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public String getOrderFiscalYear_2Digits() {
        return orderFiscalYear_2Digits;
    }

    public void setOrderFiscalYear_2Digits(String orderFiscalYear_2Digits) {
        this.orderFiscalYear_2Digits = orderFiscalYear_2Digits;
    }

    public Collection<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(Collection<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getRemitToAddress() {
        return remitToAddress;
    }

    public void setRemitToAddress(String remitToAddress) {
        this.remitToAddress = remitToAddress;
    }
    // Order Line Item Fields

    public Long getOrderLineItem_orderLineItemId() {
        return orderLineItem_orderLineItemId;
    }

    public void setOrderLineItem_orderLineItemId(Long orderLineItem_orderLineItemId) {
        this.orderLineItem_orderLineItemId = orderLineItem_orderLineItemId;
    }

    public Integer getOrderLineItem_quantity() {
        return orderLineItem_quantity;
    }

    public void setOrderLineItem_quantity(Integer orderLineItem_quantity) {
        this.orderLineItem_quantity = orderLineItem_quantity;
    }

    public String getOrderLineItem_itemDescription() {
        return orderLineItem_itemDescription;
    }

    public void setOrderLineItem_itemDescription(String orderLineItem_itemDescription) {
        this.orderLineItem_itemDescription = orderLineItem_itemDescription;
    }

    public String getOrderLineItem_buyUnit_name() {
        return orderLineItem_buyUnit_name;
    }

    public void setOrderLineItem_buyUnit_name(String orderLineItem_buyUnit_name) {
        this.orderLineItem_buyUnit_name = orderLineItem_buyUnit_name;
    }

    public Double getOrderLineItem_discountPercent() {
        return orderLineItem_discountPercent;
    }

    public void setOrderLineItem_discountPercent(Double orderLineItem_discountPercent) {
        this.orderLineItem_discountPercent = orderLineItem_discountPercent;
    }

    public String getOrderLineItem_commodityCode() {
        return orderLineItem_commodityCode;
    }

    public void setOrderLineItem_commodityCode(String orderLineItem_commodityCode) {
        this.orderLineItem_commodityCode = orderLineItem_commodityCode;
    }

    public Double getOrderLineItem_buyUnitTotalCost() {
        return orderLineItem_buyUnitTotalCost;
    }

    public void setOrderLineItem_buyUnitTotalCost(Double orderLineItem_buyUnitTotalCost) {
        this.orderLineItem_buyUnitTotalCost = orderLineItem_buyUnitTotalCost;
    }


    public Double getOrderLineItem_order_buyUnitsTotalCost() {
        return orderLineItem_order_buyUnitsTotalCost;
    }

    public void setOrderLineItem_order_buyUnitsTotalCost(Double orderLineItem_order_buyUnitsTotalCost) {
        this.orderLineItem_order_buyUnitsTotalCost = orderLineItem_order_buyUnitsTotalCost;
    }

    public String getOrderLineItem_status_statusCode() {
        return orderLineItem_status_statusCode;
    }

    public void setOrderLineItem_status_statusCode(String orderLineItem_status_statusCode) {
        this.orderLineItem_status_statusCode = orderLineItem_status_statusCode;
    }

    public String getOrderLineItem_vendorCatalogNbr() {
        return orderLineItem_vendorCatalogNbr;
    }

    public void setOrderLineItem_vendorCatalogNbr(String orderLineItem_vendorCatalogNbr) {
        this.orderLineItem_vendorCatalogNbr = orderLineItem_vendorCatalogNbr;
    }

    public Integer getOrderLineItem_lineItemNumber() {
        return orderLineItem_lineItemNumber;
    }

    public void setOrderLineItem_lineItemNumber(Integer orderLineItem_lineItemNumber) {
        this.orderLineItem_lineItemNumber = orderLineItem_lineItemNumber;
    }

    public Double getOrderLineItem_buyUnitCost() {
        return orderLineItem_buyUnitCost;
    }

    public void setOrderLineItem_buyUnitCost(Double orderLineItem_buyUnitCost) {
        this.orderLineItem_buyUnitCost = orderLineItem_buyUnitCost;
    }

    public Collection<FundingSourceData> getOrderLineItem_allReqLnItmFundingSources() {
        return orderLineItem_allReqLnItmFundingSources;
    }

    public void setOrderLineItem_allReqLnItmFundingSources(Collection<FundingSourceData> orderLineItem_allReqLnItmFundingSources) {
        this.orderLineItem_allReqLnItmFundingSources = orderLineItem_allReqLnItmFundingSources;
    }

    private int getFy(String getOrgBudgetFiscalYear){
        Pattern pattern = Pattern.compile("(?i)(\\-FY)(.*?)(\\-)");
        Matcher matcher = pattern.matcher(getOrgBudgetFiscalYear);
        int  returnValue = 0;

        List<String> listMatches = new ArrayList<String>();

        try{
            returnValue = matcher.find()? Integer.valueOf( matcher.group(2)): 0;
        }catch(NumberFormatException ex){
            returnValue = -1;
        }

        return returnValue;
    }
}