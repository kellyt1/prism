package us.mn.state.health.model.receiving;

import java.io.Serializable;
import java.util.Date;

import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.purchasing.OrderLineItem;

public class DeliveryTicket implements Serializable {
    private Long deliveryTicketId;
    private OrderLineItem orderLineItem;
    private RequestLineItem requestLineItem;
    private Date dateCreated;
    private Person createdBy;
    private Integer qtyDelivered;
    private Date expectedDeliveryDate;
    private Integer numberOfBoxes;

    public void setOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItem = orderLineItem;
    }


    public OrderLineItem getOrderLineItem() {
        return orderLineItem;
    }


    public void setRequestLineItem(RequestLineItem requestLineItem) {
        this.requestLineItem = requestLineItem;
    }


    public RequestLineItem getRequestLineItem() {
        return requestLineItem;
    }


    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }


    public Date getDateCreated() {
        return dateCreated;
    }


    public void setCreatedBy(Person createdBy) {
        this.createdBy = createdBy;
    }


    public Person getCreatedBy() {
        return createdBy;
    }


    public void setQtyDelivered(Integer qtyDelivered) {
        this.qtyDelivered = qtyDelivered;
    }


    public Integer getQtyDelivered() {
        return qtyDelivered;
    }


    public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }


    public Date getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }


    public void setNumberOfBoxes(Integer numberOfBoxes) {
        this.numberOfBoxes = numberOfBoxes;
    }


    public Integer getNumberOfBoxes() {
        return numberOfBoxes;
    }


    public void setDeliveryTicketId(Long deliveryTicketId) {
        this.deliveryTicketId = deliveryTicketId;
    }


    public Long getDeliveryTicketId() {
        return deliveryTicketId;
    }
}