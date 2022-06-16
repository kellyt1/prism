package us.mn.state.health.domain.service.materialsrequest.viewRequests;

import java.util.List;

/**
 * Encapsulates the persistence framework API for detaching a list of orders
 * @author Lucian Ochian
 *
 */
public interface RequestDetacher {
    List detachRequests(List requests);
}
