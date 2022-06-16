package us.mn.state.health.dao.receiving;

import java.util.Collection;
import java.util.Date;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAO;
import us.mn.state.health.model.receiving.DeliveryTicket;

public interface DeliveryTicketDAO extends DAO {

    public DeliveryTicket getDeliveryTicketById(Long deliveryTicketId, boolean lock) throws InfrastructureException;
    
    public Collection findAllDeliveryTickets() throws InfrastructureException;
    
    public Collection searchByDateRange(Date from, Date to) throws InfrastructureException;
    
}