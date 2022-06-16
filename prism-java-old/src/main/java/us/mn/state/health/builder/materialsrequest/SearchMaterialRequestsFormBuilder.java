package us.mn.state.health.builder.materialsrequest;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.materialsrequest.RequestDAO;
import us.mn.state.health.matmgmt.director.RequestFormDirector;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.util.search.LuceneQueryBuilder;
import us.mn.state.health.model.util.search.RequestIndex;
import us.mn.state.health.model.util.search.RequestLineItemIndex;
import us.mn.state.health.view.materialsrequest.RequestForm;
import us.mn.state.health.view.materialsrequest.SearchMaterialRequestsForm;

import java.util.*;

public class SearchMaterialRequestsFormBuilder {
    private DAOFactory daoFactory;
    private SearchMaterialRequestsForm materialRequestsForm;
    private User user;

    public SearchMaterialRequestsFormBuilder(SearchMaterialRequestsForm materialRequestsForm, User user, DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.materialRequestsForm = materialRequestsForm;
        this.user = user;
    }

    public void buildOrgBudgets() throws InfrastructureException {
        Collection orgBudgets = daoFactory.getOrgBudgetDAO().findAllPurchaseOrgBudgets();
        materialRequestsForm.setOrgBudgets(orgBudgets);
    }

    public void buildStatuses() throws InfrastructureException {
        Collection statuses = daoFactory.getStatusDAO().findAllByStatusTypeCode(StatusType.MATERIALS_REQUEST);
        materialRequestsForm.setStatuses(statuses);
    }

    public void buildMyRequests(int firstResult, int maxResults) throws InfrastructureException {
        List<Request> requests = new ArrayList();
        requests.addAll(daoFactory.getRequestDAO().findMyRequests(user, firstResult, maxResults));

        List<RequestForm> requestForms = new ArrayList();

        for (Request request : requests) {
            RequestForm requestForm = new RequestForm();
            RequestFormBuilder builder = new RequestFormBuilder(requestForm, daoFactory, request);
            RequestFormDirector director = new RequestFormDirector(builder);
            director.constructMyRequests();
            requestForms.add(requestForm);
        }

        materialRequestsForm.setRequestForms(requestForms);
    }

    public void buildSearchResults(int firstResult, int maxResults) throws InfrastructureException {
        //only the id's of my requests
        Collection myRequestIds = new ArrayList();
        //the id's of my requests that contain our searching data in the concatenated field of the RequestIndex
        Collection myRequestIds1 = new ArrayList();
        //the id's of my requests that have rli's that contain in the concatenated String our searching data,
        // are asociated with one of my requests
        // and have the status different from FulFilled
        Collection myRequestIds2 = new ArrayList();
        //the final result of the search
        Set resultIds = new LinkedHashSet();
        String queryString = materialRequestsForm.getQuery();
        if (StringUtils.nullOrBlank(queryString)) {
            return;
        }
        RequestIndex requestIndex = new RequestIndex();
        BooleanQuery currentSearchingRequests = new BooleanQuery();
        Query myRequestQuery = new TermQuery(new Term(RequestIndex.REQUESTOR_ID, user.getPersonId().toString()));
        currentSearchingRequests.add(myRequestQuery, BooleanClause.Occur.MUST);
        myRequestIds = requestIndex.searchIds(myRequestQuery);

        currentSearchingRequests = new BooleanQuery();
        Query myRequestQueryConcatenated = new TermQuery(new Term(RequestIndex.CONCATENATED_CONTENT,
                queryString));
        currentSearchingRequests.add(myRequestQuery, BooleanClause.Occur.MUST);
        currentSearchingRequests.add(myRequestQueryConcatenated, BooleanClause.Occur.MUST);
        myRequestIds1 = requestIndex.searchIds(currentSearchingRequests);

        BooleanQuery currentSearchingRLIs;
        Query requestIdQuery;
        Query concatenatedContentQuery;
        Query statusQuery;
        RequestLineItemIndex requestLineItemIndex = new RequestLineItemIndex();

        for (Iterator iterator = myRequestIds.iterator(); iterator.hasNext();) {
            Long requestId = (Long) iterator.next();
            requestIdQuery = new TermQuery(new Term(RequestLineItemIndex.REQUEST_ID, requestId.toString()));
            concatenatedContentQuery = new TermQuery(new Term(RequestLineItemIndex.CONCATENATED_CONTENT, queryString));
            statusQuery = new TermQuery(new Term(RequestLineItemIndex.RLI_STATUS_CODE, Status.FULFILLED));
            currentSearchingRLIs = new BooleanQuery();
            currentSearchingRLIs.add(requestIdQuery, BooleanClause.Occur.MUST);
            currentSearchingRLIs.add(concatenatedContentQuery, BooleanClause.Occur.MUST);
            currentSearchingRLIs.add(statusQuery, BooleanClause.Occur.MUST_NOT);

            Collection rliIds = requestLineItemIndex.searchIds(currentSearchingRLIs);
            if (rliIds.size() > 0) {
                myRequestIds2.add(requestId);
            }
        }

        resultIds.addAll(myRequestIds2);
        resultIds.addAll(myRequestIds1);

        //converting the results in forms
        Collection requestForms = new ArrayList();
        if (resultIds.size() == 0) {
            materialRequestsForm.setRequestForms(requestForms);
            return;
        }
        Iterator iter = daoFactory.getRequestDAO().findRequestsUsingIds(resultIds, firstResult, maxResults).iterator();
        while (iter.hasNext()) {
            Request request = (Request) iter.next();
            RequestForm rForm = new RequestForm();
            RequestFormBuilder builder = new RequestFormBuilder(rForm, daoFactory, request, null);
            RequestFormDirector director = new RequestFormDirector(builder);
            director.constructMyRequests();
            requestForms.add(rForm);
        }

        materialRequestsForm.setRequestForms(requestForms);

    }

    public void buildAdvancedSearchResultsForMyRequests(int firstResult, int maxResults) throws InfrastructureException {
        BooleanQuery booleanQuery = new BooleanQuery();
        LuceneQueryBuilder queryBuilder = new LuceneQueryBuilder(materialRequestsForm, booleanQuery);
        queryBuilder.addAndAll("itemDescription", RequestLineItemIndex.ITEM_DESCRIPTION);
        queryBuilder.addAndAll("vendorName", RequestLineItemIndex.VENDOR_NAMES);
        queryBuilder.addAndAll("itemModel", RequestLineItemIndex.ITEM_MODEL);
        queryBuilder.addAndMatchPhrase("statusCode", RequestLineItemIndex.RLI_STATUS_CODE);
        queryBuilder.addAndDateRangeInclusive("dateRequestedForm", "dateRequestedTo", RequestLineItemIndex.DATE_REQUESTED);
        queryBuilder.addAndDateRangeInclusive("neededByFrom", "neededByTo", RequestLineItemIndex.NEED_BY_DATE);
        queryBuilder.addAndMatchPhrase("categoryCode", RequestLineItemIndex.ITEM_CATEGORY_CODE);
        queryBuilder.addAndAny("orgBudgetCode", RequestLineItemIndex.FUNDING_SRC_ORG_BDGT_CODES);
        queryBuilder.addAndAll("icnbr", RequestLineItemIndex.ITEM_ICNBR);
        //Search only my RLI
        queryBuilder = new LuceneQueryBuilder(user, booleanQuery);
        queryBuilder.addAndMatchPhrase("personId", RequestLineItemIndex.REQUESTOR_ID);

        String requestTrackingNumber = materialRequestsForm.getRequestTrackingNumber();
        String s = StringUtils.escapeSpecialCharactersInLucene(requestTrackingNumber);
        if (s != null && s.length() > 0) {
            TermQuery mrqQuery = new TermQuery(new Term(RequestLineItemIndex.REQUEST_TRACKING_NUMBER
                    , "MRQ-" + s));
            booleanQuery.add(mrqQuery, BooleanClause.Occur.MUST);
        }

        RequestLineItemIndex index = new RequestLineItemIndex();
        Collection rliIds = index.searchIds(booleanQuery);

        RequestDAO requestDAO = daoFactory.getRequestDAO();
        Collection requests = new ArrayList();
        requests = requestDAO.findRequestsUsingRliId(rliIds, firstResult, maxResults);

        Collection requestForms = new ArrayList();
        for (Object request1 : requests) {
            Request request = (Request) request1;
            RequestForm rForm = new RequestForm();
            RequestFormBuilder builder = new RequestFormBuilder(rForm, daoFactory, request);
            RequestFormDirector director = new RequestFormDirector(builder);
            director.constructMyRequests();
            requestForms.add(rForm);
        }
        materialRequestsForm.setRequestForms(requestForms);
    }

    public void buildAdvancedSearchResultsForAllRequests(int firstResult, int maxResults) throws InfrastructureException {
        BooleanQuery booleanQuery = new BooleanQuery();
        LuceneQueryBuilder queryBuilder = new LuceneQueryBuilder(materialRequestsForm, booleanQuery);
        queryBuilder.addAndAll("itemDescription", RequestLineItemIndex.ITEM_DESCRIPTION);
        queryBuilder.addAndAll("vendorName", RequestLineItemIndex.VENDOR_NAMES);
        queryBuilder.addAndAll("itemModel", RequestLineItemIndex.ITEM_MODEL);
        queryBuilder.addAndMatchPhrase("statusCode", RequestLineItemIndex.RLI_STATUS_CODE);
        queryBuilder.addAndDateRangeInclusive("dateRequestedForm", "dateRequestedTo", RequestLineItemIndex.DATE_REQUESTED);
        queryBuilder.addAndDateRangeInclusive("neededByFrom", "neededByTo", RequestLineItemIndex.NEED_BY_DATE);
        queryBuilder.addAndMatchPhrase("categoryCode", RequestLineItemIndex.ITEM_CATEGORY_CODE);
        queryBuilder.addAndAny("orgBudgetCode", RequestLineItemIndex.FUNDING_SRC_ORG_BDGT_CODES);
        queryBuilder.addAndAll("icnbr", RequestLineItemIndex.ITEM_ICNBR);
        queryBuilder.addAndAll("swiftItemId", RequestLineItemIndex.SWIFT_ITEM_ID);
        queryBuilder.addAndMatchPhrase("requestorId", RequestLineItemIndex.REQUESTOR_ID);
        //Search only my RLI
//        queryBuilder = new LuceneQueryBuilder(user, booleanQuery);
//        queryBuilder.addAndMatchPhrase("personId", RequestLineItemIndex.REQUESTOR_ID);

        String requestTrackingNumber = materialRequestsForm.getRequestTrackingNumber();
        String s = StringUtils.escapeSpecialCharactersInLucene(requestTrackingNumber);
        if (s != null && s.length() > 0) {
            TermQuery mrqQuery = new TermQuery(new Term(RequestLineItemIndex.REQUEST_TRACKING_NUMBER
                    , "MRQ-" + s));
            booleanQuery.add(mrqQuery, BooleanClause.Occur.MUST);
        }

        RequestLineItemIndex index = new RequestLineItemIndex();

        //find the requestIds using Lucene

        Set requestIds = new TreeSet();
        requestIds.addAll(index.searchRequestIds(booleanQuery));

        //sort the requestIds descending
        List reqIds = new ArrayList();
        for (Iterator iterator = requestIds.iterator(); iterator.hasNext();) {
            Long requestId = (Long) iterator.next();
            reqIds.add(0, requestId);
        }

        RequestDAO requestDAO = daoFactory.getRequestDAO();
        Collection requests = new ArrayList();
        requests = requestDAO.findRequestsUsingIds(reqIds, firstResult, maxResults);

        Collection requestForms = new ArrayList();
        Iterator iter = requests.iterator();
        while (iter.hasNext()) {
            Request request = (Request) iter.next();
            RequestForm rForm = new RequestForm();
            RequestFormBuilder builder = new RequestFormBuilder(rForm, daoFactory, request);
            RequestFormDirector director = new RequestFormDirector(builder);
            director.constructMyRequests();
            requestForms.add(rForm);
        }
        materialRequestsForm.setRequestForms(requestForms);
        materialRequestsForm.setResultsNo(reqIds.size());
    }

    public void buildCategories() throws InfrastructureException {
        Collection categories = daoFactory.getCategoryDAO().findDescendantCategoriesByParentCode("MAT", false);
        materialRequestsForm.setCategories(categories);

    }

    public void buildContacts() throws InfrastructureException {
        Collection requestors = daoFactory.getUserDAO().findAll();
        materialRequestsForm.setRequestors(requestors);
    }
}
