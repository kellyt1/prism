package us.mn.state.health.view.receiving;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.model.purchasing.OrderLineItem;

public class DeliveryTicketForm extends ValidatorForm {
    private OrderLineItem orderLineItem;
    private String requestLineItemId;
    private String dateCreated;
    private String qtyDelivered;
    private String expectedDeliveryDate;
    private String numberOfBoxes;
    private String deliveryTicketId;
    private String cmd = "";
    
    /** Resets DeliveryTicket form values */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        cmd = dateCreated = qtyDelivered = expectedDeliveryDate = 
        numberOfBoxes = deliveryTicketId = requestLineItemId = "";
    }

    /** Validates Order form values */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        //return super.validate(mapping, request);
        return null;
    }

    public void setOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItem = orderLineItem;
    }


    public OrderLineItem getOrderLineItem() {
        return orderLineItem;
    }


    public void setRequestLineItemId(String requestLineItemId) {
        this.requestLineItemId = requestLineItemId;
    }


    public String getRequestLineItemId() {
        return requestLineItemId;
    }


    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }


    public String getDateCreated() {
        return dateCreated;
    }


    public void setQtyDelivered(String qtyDelivered) {
        this.qtyDelivered = StringUtils.trim(qtyDelivered);
    }


    public String getQtyDelivered() {
        return qtyDelivered;
    }


    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }


    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }


    public void setNumberOfBoxes(String numberOfBoxes) {
        this.numberOfBoxes = StringUtils.trim(numberOfBoxes);
    }


    public String getNumberOfBoxes() {
        return numberOfBoxes;
    }


    public void setDeliveryTicketId(String deliveryTicketId) {
        this.deliveryTicketId = deliveryTicketId;
    }


    public String getDeliveryTicketId() {
        return deliveryTicketId;
    }
    
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }


    public String getCmd() {
        return cmd;
    }
}