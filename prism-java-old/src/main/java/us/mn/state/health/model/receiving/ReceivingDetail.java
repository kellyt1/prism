package us.mn.state.health.model.receiving;

import java.io.Serializable;
import java.util.Date;

import us.mn.state.health.model.common.Facility;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.purchasing.OrderLineItem;

public class ReceivingDetail implements Serializable {
    private Long receivingDetailId;
    private OrderLineItem orderLineItem;
    private Person receivedBy;
    private Date dateReceived;
    private Integer quantityReceived;
    private Facility facility;
    private String assetNumber;


    public ReceivingDetail() {
        this(null, null, null, null, null, null);
    }

    public ReceivingDetail(OrderLineItem oli,
                           Person receivedBy,
                           Date dateReceived,
                           Integer qtyReceived,
                           Facility facility,
                           String assetNumber) {
        this.orderLineItem = oli;
        this.receivedBy = receivedBy;
        this.dateReceived = dateReceived;
        this.quantityReceived = qtyReceived;
        this.facility = facility;
        this.assetNumber = assetNumber;


    }

    public void setReceivingDetailId(Long receivingDetailId) {
        this.receivingDetailId = receivingDetailId;
    }


    public Long getReceivingDetailId() {
        return receivingDetailId;
    }


    public void setOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItem = orderLineItem;
    }


    public OrderLineItem getOrderLineItem() {
        return orderLineItem;
    }


    public void setReceivedBy(Person receivedBy) {
        this.receivedBy = receivedBy;
    }


    public Person getReceivedBy() {
        return receivedBy;
    }


    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }


    public Date getDateReceived() {
        return dateReceived;
    }


    public void setQuantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
    }


    public Integer getQuantityReceived() {
        return quantityReceived;
    }


    public void setFacility(Facility facility) {
        this.facility = facility;
    }


    public Facility getFacility() {
        return facility;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

}