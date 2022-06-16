package us.mn.state.health.builder.receiving;

import java.util.Date;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.receiving.DeliveryTicket;
import us.mn.state.health.view.receiving.DeliveryTicketForm;

public class DeliveryTicketBuilder  {
    private DeliveryTicketForm deliveryTicketForm;
    private DeliveryTicket deliveryTicket;
    private DAOFactory daoFactory;
    private Person actor;
    
    public DeliveryTicketBuilder(DeliveryTicketForm deliveryTicketForm,
                                 DeliveryTicket deliveryTicket,
                                 Person actor,
                                 DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.deliveryTicket = deliveryTicket;
        this.deliveryTicketForm = deliveryTicketForm;
        this.actor = actor;
    }    
    
    public void buildRequestLineItem() throws InfrastructureException {
        Long rliID;
        if(deliveryTicketForm.getRequestLineItemId() != null && !deliveryTicketForm.getRequestLineItemId().equals("")) {
            rliID = new Long(deliveryTicketForm.getRequestLineItemId());
            RequestLineItem rli = daoFactory.getRequestLineItemDAO()
                                            .getRequestLineItemById(rliID, false);  
            deliveryTicket.setRequestLineItem(rli);
        }
        else {
           deliveryTicket.setRequestLineItem(null);
        }
    }
    
     public void buildSimpleProperties() throws InfrastructureException {
        try {
            if(deliveryTicketForm != null) {
                PropertyUtils.copyProperties(deliveryTicket, deliveryTicketForm);
            }
            deliveryTicket.setDateCreated(new Date());
            deliveryTicket.setCreatedBy(actor);
            deliveryTicket.setOrderLineItem(deliveryTicketForm.getOrderLineItem());
        }
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building FixedAsset: ", rpe);
        }
    }
}