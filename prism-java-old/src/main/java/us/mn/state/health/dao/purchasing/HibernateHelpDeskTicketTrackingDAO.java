package us.mn.state.health.dao.purchasing;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.purchasing.HelpDeskTicketTracking;
import us.mn.state.health.persistence.HibernateUtil;

import java.util.Collection;

public class HibernateHelpDeskTicketTrackingDAO extends HibernateDAO implements HelpDeskTicketTrackingDAO {



    public Long getTicketIdbyTrackingNumber(String trackingNumber) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Long ticketId = 0L;
        Collection requests;
        // Also need to add 24 hour to the query.
        try {
            requests = session.getNamedQuery("findTicketUsingTrackingNumber")
                    .setString("trackingNumber", trackingNumber)
                    .list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }

        if (requests.size() > 0) {
            HelpDeskTicketTracking ht = new HelpDeskTicketTracking();
            ht = (HelpDeskTicketTracking) requests.iterator().next();
            ticketId = ht.getTrackingId();
        }

        return ticketId;

    }

    public Boolean findTicketUsingTrackingNumber(String trackingNumber) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Boolean found = false;
        Collection requests;
        // Also need to add 24 hour to the query.
        try {
            requests = session.getNamedQuery("findTicketUsingTrackingNumber")
                    .setString("trackingNumber", trackingNumber)
                    .list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }

        if (requests.size() > 0)
            found = true;

        return found;
    }

    // public HelpDeskTicketTracking findExistingTicketUsingTrackingNumber(String trackingNumber) throws InfrastructureException;
    public HelpDeskTicketTracking findExistingTicketUsingTrackingNumber(String trackingNumber) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Boolean found = false;
        Collection requests;
        HelpDeskTicketTracking ticket = null;
        // Also need to add 24 hour to the query.
        try {
            requests = session.getNamedQuery("findExistingTicketUsingTrackingNumber")
                    .setString("trackingNumber", trackingNumber)
                    .list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }

        if (requests.iterator().hasNext())
            ticket = (HelpDeskTicketTracking) requests.iterator().next();

        return ticket;
    }

}
