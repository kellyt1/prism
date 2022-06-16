package us.mn.state.health.view.materialsrequest.reports;

import java.util.Date;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.model.common.Facility;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.materialsrequest.DeliveryDetail;

public class StockItemOutTransaction implements Comparable{

    private Long stockItemId;

    private String trackingNumber;
    private String itemDescription;
    private String dispenseUnit;
    private Double dispUnitCost;
    private Date dateRequested;
    private Integer qtyRequested;
    private Integer qtyFilled;
    private String requestorFirstName;
    private String requestorLastName;
    private Long requestLineItemId;
    private Double amountPaid;
    private String orgCode;
    private String orgCodeDisplay;
    private DeliveryDetail deliveryDetail;

    /**
     * We use item to populate the ICNBR field (after we cast it to a StockItem)
     * @param trackingNumber
     * @param itemId
     * @param itemDescription
     * @param dispenseUnit
     * @param dispUnitCost
     * @param dateRequested
     * @param qtyRequested
     * @param qtyFilled
     * @param requestorFirstName
     * @param requestorLastName
     */
    public StockItemOutTransaction(String trackingNumber
            , Long itemId
            , String itemDescription
            , String dispenseUnit
            , Double dispUnitCost
            , Date dateRequested
            , Integer qtyRequested
            , Integer qtyFilled
            , String requestorFirstName
            , String requestorLastName
            , Long requestLineItemId
            , Double amountPaid
            , String orgCode
              ,DeliveryDetail deliveryDetail
              ,ExternalOrgDetail externalOrgDetail
              ,Person recipient
              ,MailingAddress mailingAddress
              ,Facility facility) {

        this.trackingNumber = trackingNumber;
        this.stockItemId =  itemId;
        this.itemDescription = itemDescription;
        this.dispenseUnit = dispenseUnit;
        this.dispUnitCost = dispUnitCost;
        this.dateRequested = dateRequested;
        this.qtyRequested = qtyRequested;
        this.qtyFilled = qtyFilled;
        this.requestorFirstName = requestorFirstName;
        this.requestorLastName = requestorLastName;
        this.requestLineItemId = requestLineItemId;
        this.amountPaid = amountPaid;
        this.orgCode = orgCode;
        this.deliveryDetail = deliveryDetail;

    }
    public StockItemOutTransaction(String trackingNumber
            , Long itemId
            , String itemDescription
            , String dispenseUnit
            , Double dispUnitCost
            , Date dateRequested
            , Integer qtyRequested
            , Integer qtyFilled
            , String requestorFirstName
            , String requestorLastName
            , Long requestLineItemId
            , Double amountPaid
            , String orgCode
            , String orgCodeDisplay
              ,DeliveryDetail deliveryDetail
              ,ExternalOrgDetail externalOrgDetail
              ,Person recipient
              ,MailingAddress mailingAddress
              ,Facility facility) {
        this.trackingNumber = trackingNumber;
        this.stockItemId =  itemId;
        this.itemDescription = itemDescription;
        this.dispenseUnit = dispenseUnit;
        this.dispUnitCost = dispUnitCost;
        this.dateRequested = dateRequested;
        this.qtyRequested = qtyRequested;
        this.qtyFilled = qtyFilled;
        this.requestorFirstName = requestorFirstName;
        this.requestorLastName = requestorLastName;
        this.requestLineItemId = requestLineItemId;
        this.amountPaid = amountPaid;
        this.orgCode = orgCode;
        this.orgCodeDisplay = orgCodeDisplay;
        this.deliveryDetail = deliveryDetail;
    }

    public StockItemOutTransaction(String trackingNumber
            , Long itemId
            , String itemDescription
            , String dispenseUnit
            , Double dispUnitCost
            , Date dateRequested
            , Integer qtyRequested
            , Integer qtyFilled
            , String requestorFirstName
            , String requestorLastName
            , Long requestLineItemId
            , DeliveryDetail deliveryDetail
            , ExternalOrgDetail externalOrgDetail
            , Person recipient
            , MailingAddress mailingAddress
            , Facility facility) {
        this.trackingNumber = trackingNumber;
        this.stockItemId = itemId;
        this.itemDescription = itemDescription;
        this.dispenseUnit = dispenseUnit;
        this.dispUnitCost = dispUnitCost;
        this.dateRequested = dateRequested;
        this.qtyRequested = qtyRequested;
        this.qtyFilled = qtyFilled;
        this.requestorFirstName = requestorFirstName;
        this.requestorLastName = requestorLastName;
        this.requestLineItemId = requestLineItemId;
        this.amountPaid = new Double(0);
        this.orgCode = "N/A";
        this.orgCodeDisplay = "N/A";
        this.deliveryDetail = deliveryDetail;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getDispenseUnit() {
        return dispenseUnit;
    }

    public Double getDispUnitCost() {
        return dispUnitCost;
    }

    public Date getDateRequested() {
        return dateRequested;
    }

    public Integer getQtyRequested() {
        return qtyRequested;
    }

    public Integer getQtyFilled() {
        return qtyFilled;
    }

    public String getRequestorFirstName() {
        return requestorFirstName;
    }

    public String getRequestorLastName() {
        return requestorLastName;
    }

    public String getRequestorLastFirstName(){
        return requestorLastName + " " + requestorFirstName;
    }

    public Double getAmountPaid(){
        return amountPaid;
    }

    public Double getTotalAmount(){
        double qtyReq = qtyRequested.doubleValue();
        double duCost = dispUnitCost.doubleValue();
        double value = (Math.round(qtyReq * 100 * duCost)+0.00d) / 100;
        return new Double(value);
    }

    public Long getRequestLineItemId() {
        return requestLineItemId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public String getOrgCodeDisplay() {
        return orgCodeDisplay;
    }

    public String getFullIcnbr(){
        StockItem stockItemById;
        try {
            stockItemById = DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getStockItemDAO().getStockItemById(stockItemId, false);
        } catch (InfrastructureException e) {
            return "N/A";
        }
        return stockItemById.getFullIcnbr();
    }

    public int compareTo(Object o) {
        return (-1)*requestLineItemId.compareTo(((StockItemOutTransaction)o).requestLineItemId);
    }

    public String getRecipientName() throws InfrastructureException {
        return deliveryDetail.getRecipientName();
    }

    public String getOrgName() throws InfrastructureException {
        ExternalOrgDetail organization = deliveryDetail.getOrganization();
        if(organization != null) {
            return organization.getOrgName();
        }
        else {
            return "MDH";
        }
    }

    public String getAddress() throws InfrastructureException {
        MailingAddress mailingAddress = deliveryDetail.getMailingAddress();
        if(mailingAddress != null){
            return mailingAddress.getShortSummaryInOneLine();
        }
        return "N/A";
    }


    public String toString() {
        return "StockItemOutTransaction{" +
                "stockItemId=" + stockItemId +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", dispenseUnit='" + dispenseUnit + '\'' +
                ", dispUnitCost=" + dispUnitCost +
                ", dateRequested=" + dateRequested +
                ", qtyRequested=" + qtyRequested +
                ", qtyFilled=" + qtyFilled +
                ", requestorFirstName='" + requestorFirstName + '\'' +
                ", requestorLastName='" + requestorLastName + '\'' +
                ", requestLineItemId=" + requestLineItemId +
                ", amountPaid=" + amountPaid +
                ", orgCode='" + orgCode + '\'' +
                ", orgCodeDisplay='" + orgCodeDisplay + '\'' +
                '}';
    }
}
