package us.mn.state.health.dao.purchasing;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAO;
import us.mn.state.health.model.purchasing.HelpDeskTicketTracking;

public interface HelpDeskTicketTrackingDAO extends DAO {

    public Long getTicketIdbyTrackingNumber(String trackingNumber) throws InfrastructureException;
    public Boolean findTicketUsingTrackingNumber(String trackingNumber) throws InfrastructureException;
    public HelpDeskTicketTracking findExistingTicketUsingTrackingNumber(String trackingNumber) throws InfrastructureException;
}