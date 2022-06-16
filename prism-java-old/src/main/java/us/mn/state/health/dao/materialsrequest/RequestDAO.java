package us.mn.state.health.dao.materialsrequest;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.materialsrequest.Request;

public interface RequestDAO {
    public Collection findAll() throws InfrastructureException;

    public Collection findByEvaluatorAndStatusCode(Person evaluator, String statusCode) throws InfrastructureException;

    public Collection findByStatusCode(String statusCode, Map aliases, Integer firstResult, Integer maxResults) throws InfrastructureException;

    public String findNextTrackingNumber() throws InfrastructureException;

    public Request getRequestById(Long requestId, boolean lock) throws InfrastructureException;

    public void makePersistent(Request request) throws InfrastructureException;

    public void makeTransient(Request request) throws InfrastructureException;

    public Collection findMyRequests(User user) throws InfrastructureException;

    public Collection findMyRequests(User user, int firstResult, int maxResults) throws InfrastructureException;

    public Collection findRequestsUsingRliId(Collection rliIds) throws InfrastructureException;

    public Collection findByStatusCode(String statusCode, Map aliases) throws InfrastructureException;

    public Collection findByStatusCode(String statusCode) throws InfrastructureException;

    public Collection findByStatusCode(String statusCode, Integer firstResult, Integer maxResults) throws InfrastructureException;

    public Collection findRequestsUsingRliId(Collection rliIds, int firstResult, int maxResults) throws InfrastructureException;

    public Collection findRequestsUsingRliId(Collection rliIds, Map aliases) throws InfrastructureException;

    public Collection findRequestsUsingRliId(Collection rliIds, Map aliases, int firstResult, int maxResults) throws InfrastructureException;

    public Collection findRequestsUsingIds(Collection ids) throws InfrastructureException;

    public Collection findRequestsUsingIds(Collection ids, int firstResult, int maxResults) throws InfrastructureException;

    public Integer countRequestsByStatusCode(String statusCode) throws InfrastructureException;

    public int countRequestsUsingRliId(Collection rliIds) throws InfrastructureException;

    public List findIsOnOrderRequestsForStockItem(Long stockItemid) throws InfrastructureException;
}
