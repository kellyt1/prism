package us.mn.state.health.dao.receiving;

import java.util.Collection;
import java.util.Date;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.receiving.DeliveryTicket;

public class HibernateDeliveryTicketDAO extends HibernateDAO implements DeliveryTicketDAO  {

    public DeliveryTicket getDeliveryTicketById(Long id, boolean lock) throws InfrastructureException {
        return (DeliveryTicket)super.loadById(id, DeliveryTicket.class, lock);
    }

    public Collection findAllDeliveryTickets() throws InfrastructureException {
        return super.findAll(DeliveryTicket.class);
    }
    
    public Collection searchByDateRange(Date from, Date to) throws InfrastructureException {
        return null;
    }
}
