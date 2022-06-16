package us.mn.state.health.builder.materialsrequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.director.RequestFormDirector;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.view.materialsrequest.RequestForm;
import us.mn.state.health.view.materialsrequest.RequestFormCollection;

public class RequestFormCollectionBuilder {
    private DAOFactory daoFactory;
    private RequestFormCollection requestFormCollection;
    private User evaluator;

    public RequestFormCollectionBuilder(RequestFormCollection requestFormCollection, DAOFactory daoFactory) {
        this.requestFormCollection = requestFormCollection;
        this.daoFactory = daoFactory;
    }

    public RequestFormCollectionBuilder(RequestFormCollection requestFormCollection, DAOFactory daoFactory, User evaluator) {
        this(requestFormCollection, daoFactory);
        this.evaluator = evaluator;
    }

    /**
     * This method is responsible for creating a collection of RequestForm objects and storing the
     * collection in the RequestFormCollection object.  First it gets all the matching Request (model) objects
     * and then it builds a RequestForm for each of them.
     *
     * @param statusCodes
     * @param orderBy
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildRequestForms(String[] statusCodes, String orderBy) throws InfrastructureException {
        buildRequestForms(statusCodes, orderBy, 0, 0);
    }

    public void buildRequestForms(String[] statusCodes, String orderBy, int firstResult, int maxResults) throws InfrastructureException {
        Collection requests = new ArrayList();
        Collection requestForms = new ArrayList();
        int totalRequests = 0;
        for (int i = 0; i < statusCodes.length; i++) {
            String statusCode = statusCodes[i];
            if (evaluator != null) {  //this is for evaluating materials requests
                requests.addAll(daoFactory.getRequestDAO().findByEvaluatorAndStatusCode(evaluator, statusCode));
            } else {  //this is for Picking stock requests
                requests.addAll(daoFactory.getRequestDAO()
                        .findByStatusCode(statusCode, new Integer(firstResult), new Integer(maxResults)));
            }
            totalRequests += daoFactory.getRequestDAO().countRequestsByStatusCode(statusCode).intValue();
        }
        requestFormCollection.setTotalRequests(totalRequests);
        convertRequestsToRequestForms(requests, requestForms);
    }

    public void convertRequestsToRequestForms(Collection requests, Collection requestForms) throws InfrastructureException {
        Map cart = maintainTheCartForFillStockRequests();

        //reset the requestForms collection after processing the old(existing) request forms
        // and before populating the requestForms with the next requests
        requestFormCollection.setRequestForms(new ArrayList());
        // populating the requestForms with the next requests
        Iterator iter = requests.iterator();
        while (iter.hasNext()) {
            Request request = (Request) iter.next();
            RequestForm rForm = new RequestForm();
            RequestFormBuilder builder = new RequestFormBuilder(rForm, daoFactory, request, evaluator);
            RequestFormDirector director = new RequestFormDirector(builder);
            if (evaluator != null) {
                director.constructForEvaluation();
            } else {
                director.constructForStockClerk();
            }

            if (cart.containsKey(rForm.getRequestId())) {
                rForm.setSelected(Boolean.TRUE);
            }
            requestForms.add(rForm);
        }

        requestFormCollection.setRequestForms(requestForms);
    }

    public Map maintainTheCartForFillStockRequests() {
        Map cart = requestFormCollection.getCart();
        //process the old(existing) request forms - add them to the 'cart' if they were selected
        Collection oldRequests = requestFormCollection.getRequestForms();
        for (Iterator iterator = oldRequests.iterator(); iterator.hasNext();) {
            RequestForm requestForm = (RequestForm) iterator.next();
            String requestId = requestForm.getRequestId();
            if (requestForm.getSelected().booleanValue()) {
                cart.put(requestId, requestForm);
            }
            // remove it from the cart only during the navigation
            else { //(!StringUtils.nullOrBlank(requestFormCollection.getPaginationDirection())){
                cart.remove(requestId);
            }
        }
        return cart;
    }

    public void buildPriorities() throws InfrastructureException {
        requestFormCollection.setPriorities(daoFactory.getPriorityDAO().findAll());
    }

    public void buildRequestors() throws InfrastructureException {
        requestFormCollection.setRequestors(daoFactory.getPersonDAO().findAllMDHEmployees());
    }

    public void buildCategories() throws InfrastructureException {
        requestFormCollection.setCategories(
                daoFactory.getCategoryDAO().findDescendantCategoriesByParentCode(Category.MATERIALS, false));
    }

    public void buildStatuses() throws InfrastructureException {
        Collection statuses = daoFactory.getStatusDAO().findAllByStatusTypeAndStatusCodes(
                StatusType.MATERIALS_REQUEST, new String[]{Status.FULFILLED, Status.WAITING_FOR_DISPERSAL}
        );
        requestFormCollection.setStatuses(statuses);
        requestFormCollection.setStatusCode(Status.WAITING_FOR_DISPERSAL);
    }

    public void convertRequestsToRequestFormsForSearchFillStockRequests(Collection requests, Collection requestForms) throws InfrastructureException {
        Map cart = maintainTheCartForFillStockRequests();

        //reset the requestForms collection after processing the old(existing) request forms
        // and before populating the requestForms with the next requests
        requestFormCollection.setRequestForms(new ArrayList());
        // populating the requestForms with the next requests
        Iterator iter = requests.iterator();
        while (iter.hasNext()) {
            Request request = (Request) iter.next();
            RequestForm rForm = new RequestForm();
            RequestFormBuilder builder = new RequestFormBuilder(rForm, daoFactory, request, evaluator);
            RequestFormDirector director = new RequestFormDirector(builder);
            if (evaluator != null) {
                director.constructForEvaluation();
            } else {
                director.constructForSearchFillStockRequests();
            }

            if (cart.containsKey(rForm.getRequestId())) {
                rForm.setSelected(Boolean.TRUE);
            }
            requestForms.add(rForm);
        }

        requestFormCollection.setRequestForms(requestForms);

    }
}