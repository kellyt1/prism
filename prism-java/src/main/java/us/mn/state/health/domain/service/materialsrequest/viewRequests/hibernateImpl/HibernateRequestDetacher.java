package us.mn.state.health.domain.service.materialsrequest.viewRequests.hibernateImpl;

import java.util.List;

import us.mn.state.health.domain.service.materialsrequest.viewRequests.RequestDetacher;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.DeliveryDetail;
import org.hibernate.Hibernate;

/**
 * Encapsulates the persistence framework API for detaching a list of orders
 * @author Lucian Ochian
 *
 */
public class HibernateRequestDetacher implements RequestDetacher {

    public List detachRequests(List requests) {
        for (Object request : requests) {
            DeliveryDetail deliveryDetail = ((Request) request).getDeliveryDetail();
            Hibernate.initialize(deliveryDetail);
            Hibernate.initialize(((Request)request).getRequestor());
            Hibernate.initialize(((Request)request).getPriority());
            Hibernate.initialize(deliveryDetail.getOrganization());
            Hibernate.initialize(deliveryDetail.getRecipient());
            Hibernate.initialize(deliveryDetail.getFacility());
            Hibernate.initialize(deliveryDetail.getMailingAddress());
        }
        return requests;
    }
}
