package us.mn.state.health.builder.purchasing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.util.search.LuceneQueryBuilder;
import us.mn.state.health.model.util.search.OrderIndex;
import us.mn.state.health.model.util.search.OrderLineItemIndex;
import us.mn.state.health.model.util.search.RequestLineItemIndex;
import us.mn.state.health.view.purchasing.SearchOrdersForm;

public class SearchOrdersFormBuilder {
    private DAOFactory factory;
    private SearchOrdersForm form;

    public SearchOrdersFormBuilder(SearchOrdersForm form, DAOFactory factory) {
        this.form = form;
        this.factory = factory;
    }

    /**
     * Don't use this unless you have to... there's a lot of vendors and it'll really slow down the app
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildVendors() throws InfrastructureException {
//        Collection vendors = factory.getVendorDAO().findAll(false);
        Collection vendors = factory.getVendorDAO().findAll();
        form.setVendors(vendors);
    }

    public void buildOrgBudgets() throws InfrastructureException {
        Collection orgBudgets = factory.getOrgBudgetDAO().findAllPurchaseOrgBudgets();
        form.setOrgBudgets(orgBudgets);
    }

    public void buildStatuses() throws InfrastructureException {
        Collection statuses = factory.getStatusDAO().findAllByStatusTypeCode(StatusType.MATERIALS_ORDER);
        form.setStatuses(statuses);
    }

    public void buildPersons() throws InfrastructureException {
        Collection persons = factory.getPersonDAO().findAllMDHEmployeesAsDTO();
        form.setPersons(persons);
    }

    public void buildBuyers() throws InfrastructureException {
        Collection buyers = factory.getPersonDAO().findPersonsByGroupCode(Group.BUYER_CODE);
        form.setBuyers(buyers);
    }

    public void buildFacilities() throws InfrastructureException {
        Collection locations = factory.getMailingAddressDAO().findShippingAddresses();
        //Collection locations = factory.getFacilityDAO().findFacilitiesByType(StockItemFacility.TYPE_BUILDING);
        form.setFacilities(locations);
    }

    public void buildSearchResults() throws InfrastructureException {
        BooleanQuery searchOrdersUsingOrderCriteria = new BooleanQuery();
        LuceneQueryBuilder orderCriteriaQueryBuilder = new LuceneQueryBuilder(form, searchOrdersUsingOrderCriteria);
        Collection results = new ArrayList();
        Collection orderIdResults = new LinkedHashSet();

        /**
         * Here we build a query using only Order search criteria (w/o RLI and OLI criteria)
         */
        orderCriteriaQueryBuilder.addAndAny("vendorId", OrderIndex.VENDOR_ID);
        orderCriteriaQueryBuilder.addAndAll("vendorName", OrderIndex.VENDOR_NAME);
        orderCriteriaQueryBuilder.addAndAny("purchaseOrderNumber", OrderIndex.PO_NUMBER);
        orderCriteriaQueryBuilder.addAndAny("orderId", OrderIndex.ORDER_ID);
        orderCriteriaQueryBuilder.addAndAny("purchaserId", OrderIndex.PURCHASER_PERSON_ID);
        orderCriteriaQueryBuilder.addAndAll("vendorContractId", OrderIndex.VENDOR_CONTRACT_ID);
        orderCriteriaQueryBuilder.addAndDateRangeInclusive("orderedFrom", "orderedTo", OrderIndex.DATE_CREATED);
        orderCriteriaQueryBuilder.addAndDateRangeInclusive("suspenseDateFrom", "suspenseDateTo", OrderIndex.SUSPENSE_DATE);

        orderIdResults.addAll(searchOrdersUsingRLIs(searchOrdersUsingOrderCriteria));
        orderIdResults.addAll(searchOrdersUsingOLIs(searchOrdersUsingOrderCriteria));
        results = factory.getPurchasingOrderDAO().findOrdersUsingIds(orderIdResults);
        form.setSearchResults(new ArrayList(results));
    }

    public void buildSearchResults(int firstResult, int maxResults) throws InfrastructureException {
        BooleanQuery searchOrdersUsingOrderCriteria = new BooleanQuery();
        LuceneQueryBuilder orderCriteriaQueryBuilder = new LuceneQueryBuilder(form, searchOrdersUsingOrderCriteria);
        Collection results = new ArrayList();
        Collection orderIdResults = new LinkedHashSet();

        /**
         * Here we build a query using only Order search criteria (w/o RLI and OLI criteria)
         */
        orderCriteriaQueryBuilder.addAndAny("vendorId", OrderIndex.VENDOR_ID);
        orderCriteriaQueryBuilder.addAndAll("vendorName", OrderIndex.VENDOR_NAME);
        orderCriteriaQueryBuilder.addAndAny("purchaseOrderNumber", OrderIndex.PO_NUMBER);
        orderCriteriaQueryBuilder.addAndAny("orderId", OrderIndex.ORDER_ID);
        orderCriteriaQueryBuilder.addAndAny("purchaserId", OrderIndex.PURCHASER_PERSON_ID);
        orderCriteriaQueryBuilder.addAndAny("mailingAddressId", OrderIndex.SHIPPING_ADDRESS_ID);
        orderCriteriaQueryBuilder.addAndAll("vendorContractId", OrderIndex.VENDOR_CONTRACT_ID);
        orderCriteriaQueryBuilder.addAndDateRangeInclusive("orderedFrom", "orderedTo", OrderIndex.DATE_CREATED);
        orderCriteriaQueryBuilder.addAndDateRangeInclusive("suspenseDateFrom", "suspenseDateTo", OrderIndex.SUSPENSE_DATE);

        orderIdResults.addAll(searchOrdersUsingRLIs(searchOrdersUsingOrderCriteria));
        orderIdResults.addAll(searchOrdersUsingOLIs(searchOrdersUsingOrderCriteria));
        // sort the orderId's descending
        Set ascOrderIds = new TreeSet(orderIdResults);
        ArrayList descOrderIds = new ArrayList();
        for (Iterator iterator = ascOrderIds.iterator(); iterator.hasNext();) {
            Long orderId = (Long) iterator.next();
            descOrderIds.add(0, orderId);
        }

//        results = factory.getPurchasingOrderDAO().findOrdersUsingIds(orderIdResults);
        results = factory.getPurchasingOrderDAO().findOrdersUsingIds(descOrderIds, firstResult, maxResults);

        form.setSearchResults(new ArrayList(results));
        form.setResultsNo(ascOrderIds.size());
    }


    /**
     * This method returns the Order ids found using a generated query that looks like that:
     * +orderQuery +(requestLineItemIds:{the RLI ids that were found using the RLI search criteria(requestTracking#, itemDescription,itemModel,requestorId)})
     * So in this method we search the order properties and the rli properties of the RLIs associated with a particular order
     *
     * @param orderQuery - a boolean query which was built
     *                   using only the Order search criteria (vendorId, vendorName, orderId, purchaserId, vendorContractId, orderDate, suspenseDate)
     * @return a collection of Order ids
     * @throws InfrastructureException
     */
    private Collection searchOrdersUsingRLIs(BooleanQuery orderQuery) throws InfrastructureException {
        Collection results = new ArrayList();

        boolean atLeastOnePureOLIsearchCriteria =
                (!StringUtils.nullOrBlank(PropertyUtils.getPropertyAsString(form, "statusId")));
        /**
         * if at least one search criteria that is available only for OLI is present then we return a empty collection
         * and we rely only on the searchOrdersUsingOLIs search
         */
        if (atLeastOnePureOLIsearchCriteria) {
            return results;
        }

        BooleanQuery searchOrdersUsingRLIs = new BooleanQuery();
        if (!StringUtils.nullOrBlank(orderQuery.toString())) {
            searchOrdersUsingRLIs.add(orderQuery, BooleanClause.Occur.MUST);
        }
        Collection rliIdResults2 = new ArrayList();

        rliIdResults2 = searchRequestLineItemIndex2();
        if (rliIdResults2.size() == 0
                &&
                (!StringUtils.nullOrBlank(PropertyUtils.getPropertyAsString(form, "requestId"))
                        || !StringUtils.nullOrBlank(PropertyUtils.getPropertyAsString(form, "itemDescription"))
                        || !StringUtils.nullOrBlank(PropertyUtils.getPropertyAsString(form, "itemModel"))
                        || !StringUtils.nullOrBlank(PropertyUtils.getPropertyAsString(form, "vendorCatalogNbr"))
                        || !StringUtils.nullOrBlank(PropertyUtils.getPropertyAsString(form, "orgBudgetCode"))
                        || !StringUtils.nullOrBlank(PropertyUtils.getPropertyAsString(form, "requestorId")))) {
            return results;
        }

        BooleanQuery rliIdClause = new BooleanQuery();
        for (Iterator iterator = rliIdResults2.iterator(); iterator.hasNext();) {
            Long requestLineItemId = (Long) iterator.next();
            Query q = new TermQuery(new Term(OrderIndex.REQUEST_LINE_ITEM_IDs, requestLineItemId.toString()));
            rliIdClause.add(q, BooleanClause.Occur.SHOULD);
        }
        if (!StringUtils.nullOrBlank(rliIdClause.toString())) {
            searchOrdersUsingRLIs.add(rliIdClause, BooleanClause.Occur.MUST);
        }
        if (!StringUtils.nullOrBlank(searchOrdersUsingRLIs.toString())) {
            results = new OrderIndex().searchIds(searchOrdersUsingRLIs);
        }
        return results;
    }

    /**
     * This method returns the Order ids found using a generated query that looks like that:
     * +orderQuery +(orderLineItemIds:{the OLI ids that were found using the OLI search criteria(itemDescription,itemModel,requestorId)})
     * Before using the OLI ids, if we have the requestTracking# in the search criteria, first we search the RLI ids
     * that are associated with the given Request and then we add those RLI ids to the query that returns the OLI ids
     * as a requested clause.
     * So in this method we search the order properties ,the oli properties of the OLIs associated with a particular order,
     * and eventually the request tracking # of the rlis that are associated with a OLI.
     *
     * @param orderQuery - a boolean query which was built
     *                   using only the Order search criteria (vendorId, vendorName, orderId, purchaserId, vendorContractId, orderDate, suspenseDate)
     * @return a collection of Order ids
     * @throws InfrastructureException
     */
    private Collection searchOrdersUsingOLIs(BooleanQuery orderQuery) throws InfrastructureException {
        Collection results = new ArrayList();

        BooleanQuery searchOrdersUsingOLIs = new BooleanQuery();
        searchOrdersUsingOLIs.add(orderQuery, BooleanClause.Occur.MUST);

        Collection rliIdResults1 = new ArrayList();
        Collection oliIdResults = new ArrayList();

        rliIdResults1 = searchRequestLineItemIndex1();
        if (rliIdResults1.size() == 0 && !StringUtils.nullOrBlank(PropertyUtils.getPropertyAsString(form, "requestId"))) {
            return results;
        }

        oliIdResults = searchOrderLineItemIndex(rliIdResults1);

        boolean oliIdsIsEmpty = (oliIdResults.size() == 0);
        boolean atLeastOneOLIsearchCriteria = (!StringUtils.nullOrBlank(PropertyUtils.getPropertyAsString(form, "itemDescription"))
                || !StringUtils.nullOrBlank(PropertyUtils.getPropertyAsString(form, "itemModel"))
                || !StringUtils.nullOrBlank(PropertyUtils.getPropertyAsString(form, "statusId"))
                || !StringUtils.nullOrBlank(PropertyUtils.getPropertyAsString(form, "vendorCatalogNbr"))
                || !StringUtils.nullOrBlank(PropertyUtils.getPropertyAsString(form, "orgBudgetCode"))
                || !StringUtils.nullOrBlank(PropertyUtils.getPropertyAsString(form, "requestorId")));
//        if((oliIdsIsEmpty && rliIdsIsNotEmpty && atLeastOneOLIsearchCriteria)||(oliIdsIsEmpty && !rliIdsIsNotEmpty) ){
        if ((oliIdsIsEmpty && atLeastOneOLIsearchCriteria) || oliIdsIsEmpty) {
            return results;
        }
        BooleanQuery oliIdClause = new BooleanQuery();
        for (Iterator iterator = oliIdResults.iterator(); iterator.hasNext();) {
            Long orderLineItemId = (Long) iterator.next();
            Query q = new TermQuery(new Term(OrderIndex.ORDER_LINE_ITEM_IDs, orderLineItemId.toString()));
            oliIdClause.add(q, BooleanClause.Occur.SHOULD);
        }
        if (!StringUtils.nullOrBlank(oliIdClause.toString())) {
            searchOrdersUsingOLIs.add(oliIdClause, BooleanClause.Occur.MUST);
        }
        return new OrderIndex().searchIds(searchOrdersUsingOLIs);
    }

    private Collection searchOrderLineItemIndex(Collection reqLineItemIds) throws InfrastructureException {
        Collection oliIds = new ArrayList();
        BooleanQuery query = new BooleanQuery();
        LuceneQueryBuilder builder = new LuceneQueryBuilder(form, query);
        builder.addAndAll("itemDescription", OrderLineItemIndex.ITEM_DESCRIPTION);
        builder.addAndAll("vendorCatalogNbr", OrderLineItemIndex.VENDOR_CATALOG_NBR);
        builder.addAndAll("itemModel", OrderLineItemIndex.ITEM_MODEL);
        String statusId = PropertyUtils.getPropertyAsString(form, "statusId");
//        builder.addAndMatchPhrase("statusId", OrderLineItemIndex.STATUS_CODE);
        if (!StringUtils.nullOrBlank(statusId)) {
            Status status = (Status) CollectionUtils.getObjectFromCollectionById(form.getStatuses(), statusId, "statusId");
            TermQuery statusQuery = new TermQuery(new Term(OrderLineItemIndex.STATUS_CODE, status.getStatusCode()));
            query.add(statusQuery, BooleanClause.Occur.MUST);
        }

        builder.addAndAny("orgBudgetCode", OrderLineItemIndex.ORG_BUDGET_CODES);
        builder.addAndMatchPhrase("requestorId", OrderLineItemIndex.REQUESTOR_IDs);

        //now we want to add a clause where we must have at least one RLI
        // that have been found in searchRequestLineItemIndex
        BooleanQuery rliIdClause = new BooleanQuery();
        for (Iterator iterator = reqLineItemIds.iterator(); iterator.hasNext();) {
            Long rliId = (Long) iterator.next();
            Query q = new TermQuery(new Term(OrderLineItemIndex.REQ_LINE_ITEM_IDs, rliId.toString()));
            rliIdClause.add(q, BooleanClause.Occur.SHOULD);
        }

        if (!StringUtils.nullOrBlank(rliIdClause.toString())) {
            query.add(rliIdClause, BooleanClause.Occur.MUST);
        }
        oliIds = new OrderLineItemIndex().searchIds(query);
        return oliIds;
    }

    /**
     * This method searches the RLI index using ONLY Request Tracking Numeber as a search criteria
     *
     * @return a collection of Request Line Item ids
     * @throws InfrastructureException
     */
    private Collection searchRequestLineItemIndex1() throws InfrastructureException {
        Collection rliIds = new ArrayList();
        BooleanQuery query = new BooleanQuery();
        LuceneQueryBuilder builder = new LuceneQueryBuilder(form, query);
        builder.addAndMatchPhrase("requestId", RequestLineItemIndex.REQUEST_TRACKING_NUMBER);
        rliIds = new RequestLineItemIndex().searchIds(query);
        return rliIds;
    }

    /**
     * This method searches the RLI index using the Request Tracking Numeber, item description, item model, requestor
     * as a search criteria
     *
     * @return a collection of Request Line Item ids
     * @throws InfrastructureException
     */

    private Collection searchRequestLineItemIndex2() throws InfrastructureException {
        Collection rliIds = new ArrayList();
        BooleanQuery query = new BooleanQuery();
        LuceneQueryBuilder builder = new LuceneQueryBuilder(form, query);
        builder.addAndMatchPhrase("requestId", RequestLineItemIndex.REQUEST_TRACKING_NUMBER);

        builder.addAndAll("itemDescription", RequestLineItemIndex.ITEM_DESCRIPTION);
        //builder.addAndAll("vendorCatalogNbr", RequestLineItemIndex.VENDOR_CATALOG_NBR);
        builder.addAndAll("itemModel", RequestLineItemIndex.ITEM_MODEL);
        builder.addAndMatchPhrase("requestorId", RequestLineItemIndex.REQUESTOR_ID);

        rliIds = new RequestLineItemIndex().searchIds(query);
        return rliIds;
    }

}
