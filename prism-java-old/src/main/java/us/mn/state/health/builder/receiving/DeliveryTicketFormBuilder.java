package us.mn.state.health.builder.receiving;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.view.receiving.DeliveryTicketForm;

public class DeliveryTicketFormBuilder  {
    private DeliveryTicketForm deliveryTicketForm;
    private OrderLineItem orderLineItem;
    
    public DeliveryTicketFormBuilder(DeliveryTicketForm deliveryTicketForm,
                                     OrderLineItem orderLineItem) {
        this.deliveryTicketForm = deliveryTicketForm;
        this.orderLineItem = orderLineItem;
    }
    
    public void buildOrderLineItem() throws InfrastructureException {
        deliveryTicketForm.setOrderLineItem(this.orderLineItem);
    }
    
    public void buildDefaultProperties() throws InfrastructureException {
        //deliveryTicketForm.set(this.orderLineItem);
    } 
}