package us.mn.state.health.matmgmt.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.view.materialsrequest.RequestForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: RodenT1a
 * Date: Oct 31, 2008
 * Time: 11:43:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class PickListData {
    private static Log log = LogFactory.getLog(PickListData.class);
    //StockItem
    private String deliverToInfoAsString;
    private String stockItem_locationCodesAsString;
    private String stockItem_fullIcnbr;
    private String stockItem_dispenseUnit_name;
    private String stockItem_description;
    private Integer stockItem_qtyOnHand;

    //RequestLineItemForm
    private String quantity;
    private String quantityFilled;
    private String quantityPicked;

    //Request
    private String request_trackingNumber;
    private Date request_dateRequested;
    private Date request_needByDate;
    private String request_additionalInstructions;

    //Calculated
    private String quantityOutStanding;

//reqForm.getRequestLineItemForms()


    public PickListData(RequestForm rf) {
        this.request_trackingNumber = rf.getTrackingNumber();
        this.request_dateRequested = rf.getDateRequested();
        this.request_needByDate = new Date(rf.getNeedByDate());
        this.request_additionalInstructions = rf.getAdditionalInstructions();
        this.deliverToInfoAsString = rf.getDeliverToInfoAsString();
    }

  public void addRequestLineItem(RequestLineItemForm rlif) {
      this.stockItem_locationCodesAsString = rlif.getStockItem().getLocationCodesAsString();
      this.stockItem_fullIcnbr = rlif.getStockItem().getFullIcnbr();
      this.stockItem_dispenseUnit_name = rlif.getStockItem().getDispenseUnitName();
      this.stockItem_description = rlif.getStockItem().getDescription();
      this.stockItem_qtyOnHand = rlif.getStockItem().getQtyOnHand();
      this.quantity = rlif.getQuantity();
      this.quantityFilled = rlif.getQuantityFilled();
      this.quantityPicked =  rlif.getQuantityPicked();
//      this.setQuantityOutStanding();
  }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
//        this.setQuantityOutStanding();
    }

    public String getQuantityFilled() {
        return quantityFilled;
    }

    public void setQuantityFilled(String quantityFilled) {
        this.quantityFilled = quantityFilled;
//        this.setQuantityOutStanding();
    }


    public String getQuantityPicked() {
        return quantityPicked;
    }

    public void setQuantityPicked(String quantityPicked) {
        this.quantityPicked = quantityPicked;
    }

    public String getRequest_additionalInstructions() {
        return request_additionalInstructions;
    }

    public void setRequest_additionalInstructions(String request_additionalInstructions) {
        this.request_additionalInstructions = request_additionalInstructions;
    }

    public Date getRequest_dateRequested() {
        return request_dateRequested;
    }

    public void setRequest_dateRequested(Date request_dateRequested) {
        this.request_dateRequested = request_dateRequested;
    }

    public Date getRequest_needByDate() {
        return request_needByDate;
    }

    public void setRequest_needByDate(Date request_needByDate) {
        this.request_needByDate = request_needByDate;
    }

    public String getRequest_trackingNumber() {
        return request_trackingNumber;
    }

    public void setRequest_trackingNumber(String request_trackingNumber) {
        this.request_trackingNumber = request_trackingNumber;
    }

    public String getStockItem_description() {
        return stockItem_description;
    }

    public void setStockItem_description(String stockItem_description) {
        this.stockItem_description = stockItem_description;
    }

    public String getStockItem_dispenseUnit_name() {
        return stockItem_dispenseUnit_name;
    }

    public void setStockItem_dispenseUnit_name(String stockItem_dispenseUnit_name) {
        this.stockItem_dispenseUnit_name = stockItem_dispenseUnit_name;
    }

    public String getStockItem_fullIcnbr() {
        return stockItem_fullIcnbr;
    }

    public void setStockItem_fullIcnbr(String stockItem_fullIcnbr) {
        this.stockItem_fullIcnbr = stockItem_fullIcnbr;
    }

    public String getStockItem_locationCodesAsString() {
        String outString = stockItem_locationCodesAsString;

        if ( outString != null && outString.length() > 75) outString = outString.substring(0,75);
        return outString;
    }

    public void setStockItem_locationCodesAsString(String stockItem_locationCodesAsString) {
        this.stockItem_locationCodesAsString = stockItem_locationCodesAsString;
    }

    public Integer getStockItem_qtyOnHand() {
        return stockItem_qtyOnHand;
    }

    public void setStockItem_qtyOnHand(Integer stockItem_qtyOnHand) {
        this.stockItem_qtyOnHand = stockItem_qtyOnHand;
    }

    public String getDeliverToInfoAsString() {
    return deliverToInfoAsString;
}

    public void setDeliverToInfoAsString(String deliverToInfoAsString) {
        this.deliverToInfoAsString = deliverToInfoAsString;
    }

    public String getQuantityOutStanding() {
        calcQuantityOutStanding();
        return quantityOutStanding;
    }

    public void calcQuantityOutStanding() {
        try {
            this.quantityOutStanding = ((Integer)(Integer.parseInt(getQuantity().trim()) - Integer.parseInt(getQuantityFilled().trim()))).toString();
        } catch (NumberFormatException nfe) {
            this.quantityOutStanding="";
            log.error("NumberFormatException: " + nfe.getMessage());
        } catch (Exception e) {
            this.quantityOutStanding="";
        }
    }
}
