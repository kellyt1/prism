package us.mn.state.health.domain.repository.materialsrequest.hibernate;

import us.mn.state.health.domain.repository.materialsrequest.RequestSearchCriteria;
import us.mn.state.health.domain.repository.util.HQLBuilder;

class FindRequestsHQLBuilder implements HQLBuilder {
    RequestSearchCriteria searchCriteria;
    private final static String FROM_CLASS = " from Request r ";
    private final static String JOIN_RLI = "join r.requestLineItems rli ";
    private final static String JOIN_ITEM = " join rli.item item ";
    private final static String LEFT_JOIN_ITEM = "left join rli.item item ";
    private final static String RLI_STATUS_CLAUSE = " rli.status.statusCode in (:statusIds)";

    public FindRequestsHQLBuilder(RequestSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public StringBuilder buildCommonHQL() {
        StringBuilder commonHql = new StringBuilder();
        commonHql.append(FROM_CLASS);
        if (searchCriteria.isTrackingNumberSpecified()) {
            buildTrackingNumberClause(commonHql);
        }

        return commonHql;
    }

    public void buildHql() {
        StringBuilder countHql = new StringBuilder("select count(distinct r.requestId)");

    }

    public void buildCountHql() {
        StringBuilder hql = new StringBuilder("select distinct r");
    }

    private void buildTrackingNumberClause(StringBuilder commonHql) {

    }

    private void buildDescriptionClause(StringBuilder query) {

    }

    private void buildRequestedDateFromClause(StringBuilder query) {

    }

    private void buildRequestedDateToClause(StringBuilder query) {

    }

    private void buildNeedByDateFromClause(StringBuilder query) {

    }

    private void buildNeedByDateToClause(StringBuilder query) {

    }

    private void buildPriorityClause(StringBuilder query) {

    }

    private void buildRequestorClause(StringBuilder query) {

    }

    private void buildCategoryClause(StringBuilder query) {

    }

    private void buildRliStatusClause(StringBuilder query) {

    }

    private void buildFacilityAndIcnbrClause(StringBuilder query) {

    }
}
