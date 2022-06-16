package us.mn.state.health.domain.repository.materialsrequest.hibernate;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.domain.repository.materialsrequest.RequestSearchCriteria;
import us.mn.state.health.domain.repository.util.SQLBuilder;
import us.mn.state.health.util.NotYetImplementedException;
import us.mn.state.health.util.SQLUtils;

public class FindStockRequestsSLQBuilder extends SQLBuilder {
    StringBuilder baseSqlQuery =
            new StringBuilder(" from MATERIALS_REQUEST_TBL r " +
                    "inner join MATERIALS_REQ_LINE_ITEM_TBL rli on rli.request_id = r.request_id " +
                    "inner join PRIORITY_TBL p on p.priority_id = r.priority_id " +
                    "inner join PERSON_TBL req on req.person_id = r.requestor_person_id " +
                    "inner join STOCK_ITEM_TBL si on si.stock_item_id = rli.item_id " +
                    "inner join ITEM_TBL item on item.item_id = si.stock_item_id " +
                    "inner join CATEGORY_TBL cat on cat.category_id = item.category_id " +
                    "left outer join STOCK_ITEM_LOCATION_TBL sil on sil.STOCK_ITEM_ID = si.stock_item_id " +
                    "where 1=1 ");
    private RequestSearchCriteria searchCriteria;

    public FindStockRequestsSLQBuilder(RequestSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    private String buildBaseSqlQuery() {
        return baseSqlQuery.toString()
                + buildDescriptionClause()
                + buildTrackingNumberWhereClause()
                + buildIcnbrWhereClause()
                + buildPriorityWhereClause()
                + buildStatusWhereClause()
                + buildCategoryWhereClause()
                + buildRequestorWhereClause()
                + buildFacilityWhereClause()
                + buildRequestedDateFromWhereClause()
                + buildRequestedDateToWhereClause()
                + buildNeedByDateFromWhereClause()
                + buildNeedByDateToWhereClause()
                + buildInStockWhereClause()
                + " and (sil.end_date is null or sil.end_date >= sysdate)";
    }

    private String buildInStockWhereClause() {
        if (StringUtils.isNotEmpty(this.searchCriteria.getOnStock())) {
            if (this.searchCriteria.getOnStock().equals(RequestSearchCriteria.IN_STOCK))
                return " and si.qty_on_hand>0 ";
            if (this.searchCriteria.getOnStock().equals(RequestSearchCriteria.OUT_OF_STOCK))
                return " and si.qty_on_hand=0 ";
        }
        return "";
    }

    public String buildSqlQuery() {
        return "select   distinct r.REQUEST_ID," +
                "  r.VERSION  ," +
                "  r.DATE_REQUESTED  ," +
                "  r.DATE_NEEDED  ," +
                "  r.ADDITIONAL_INSTRUCTIONS  ," +
                "  r.TRACKING_NUMBER  ," +
                "  r.TICKET_ID  ," +
                "  r.PRIORITY_ID  ," +
                "  r.REQUESTOR_PERSON_ID  ," +
                "  r.DELIVERY_DETAIL_ID ,  " +
                " p.priority_code_number "
                + buildBaseSqlQuery()
                + buildOrderByClause();
    }

    private String buildOrderByClause() {
        String orderByClause = " order by ";
        String sortMethod = searchCriteria.getSortMethod();
        if (!Arrays.asList(RequestSearchCriteria.SORT_METHODS).contains(sortMethod)) {
            sortMethod = RequestSearchCriteria.ASC;
        }
        switch (searchCriteria.getSortBy()) {
            case RequestSearchCriteria.SORT_BY_REQUEST_ID:
                orderByClause += " r.request_id " + sortMethod;
                break;
            case RequestSearchCriteria.SORT_BY_DATE_REQUESTED:
                orderByClause += " r.DATE_REQUESTED " + sortMethod;
                break;
            case RequestSearchCriteria.SORT_BY_REQUESTOR:
                orderByClause += " r.request_id " + sortMethod;
                break;
            case RequestSearchCriteria.SORT_BY_NEED_BY_DATE:
                orderByClause += " r.DATE_NEEDED " + sortMethod;
                break;
            case RequestSearchCriteria.SORT_BY_PRIORITY:
                orderByClause += " p.priority_code_number " + sortMethod;
                break;
            default:
                throw new NotYetImplementedException();
        }
        return orderByClause;
    }

    public String buildCountSqlQuery() {
        return "select count(*) from (select   distinct r.REQUEST_ID " +
                buildBaseSqlQuery() +
                ")";
    }

    private String buildDescriptionClause() {
        if (StringUtils.isNotEmpty(searchCriteria.getItemDescription())) {
            return SQLUtils.buildWhereClauseUsingAnd("item.DESCRIPTION", searchCriteria.getItemDescription(), true);
        }
        return "";
    }

    private String buildTrackingNumberWhereClause() {
        if (StringUtils.isNotEmpty(searchCriteria.getTrackingNumber())) {
            return SQLUtils.buildWhereClauseUsingAnd("r.TRACKING_NUMBER", "MRQ-" + searchCriteria.getTrackingNumber().trim(), false);
        }
        return "";
    }

    private String buildIcnbrWhereClause() {
        if (StringUtils.isNotEmpty(searchCriteria.getFullIcnbr())) {
            String[] tokens = searchCriteria.getFullIcnbr().trim().split("-");
            try {
                if (tokens.length == 1) {
                    return SQLUtils.buildWhereClauseUsingAnd("si.icnbr", new Integer(tokens[0]).toString(), false);
                } else if (tokens.length == 2) {
                    return SQLUtils.buildWhereClauseUsingAnd("si.icnbr", new Integer(tokens[1]).toString(), false) +
                            SQLUtils.buildWhereClauseUsingAnd("cat.CATEGORY_CODE", tokens[0], false);
                }
            } catch (NumberFormatException e) {
                new IllegalArgumentException("The ICNBR format is invalid!");
            }
            throw new IllegalArgumentException("The ICNBR format is invalid!");
        }
        return "";
    }

    private String buildPriorityWhereClause() {
        if (StringUtils.isNotEmpty(searchCriteria.getPriorityId())) {
            return SQLUtils.buildWhereClauseUsingAnd("r.priority_id", searchCriteria.getPriorityId(), false);
        }
        return "";
    }

    private String buildStatusWhereClause() {
        if (StringUtils.isNotEmpty(searchCriteria.getStatusId())) {
            return SQLUtils.buildWhereClauseUsingAnd("rli.status_id", searchCriteria.getStatusId(), false);
        }
        return "";
    }

    private String buildFacilityWhereClause() {
        if (StringUtils.isNotEmpty(searchCriteria.getFacilityId())) {
            return SQLUtils.buildWhereClauseUsingAnd("sil.FACILITY_ID", searchCriteria.getFacilityId(), false);
        }
        return "";
    }

    private String buildCategoryWhereClause() {
        if (StringUtils.isNotEmpty(searchCriteria.getCategoryId())) {
            return SQLUtils.buildWhereClauseUsingAnd("cat.CATEGORY_ID", searchCriteria.getCategoryId(), false);
        }
        return "";
    }

    private String buildRequestorWhereClause() {
        if (StringUtils.isNotEmpty(searchCriteria.getRequestorId())) {
            return SQLUtils.buildWhereClauseUsingAnd("r.REQUESTOR_PERSON_ID", searchCriteria.getRequestorId(), false);
        }
        return "";
    }

    private String buildRequestedDateFromWhereClause() {
        if (StringUtils.isNotEmpty(searchCriteria.getDateRequestedFrom())) {
            return SQLUtils.buildWhereClauseGreaterOrEqualWithDate("r.DATE_REQUESTED", searchCriteria.getDateRequestedFrom(), DateUtils.DEFAULT_DATE_FORMAT);
        }
        return "";
    }

    private String buildRequestedDateToWhereClause() {
        if (StringUtils.isNotEmpty(searchCriteria.getDateRequestedTo())) {
            return SQLUtils.buildWhereClauseLessOrEqualWithDate("r.DATE_REQUESTED", searchCriteria.getDateRequestedTo(), DateUtils.DEFAULT_DATE_FORMAT);
        }
        return "";
    }

    private String buildNeedByDateFromWhereClause() {
        if (StringUtils.isNotEmpty(searchCriteria.getNeedByDateFrom())) {
            return SQLUtils.buildWhereClauseGreaterOrEqualWithDate("r.DATE_NEEDED", searchCriteria.getNeedByDateFrom(), DateUtils.DEFAULT_DATE_FORMAT);
        }
        return "";
    }

    private String buildNeedByDateToWhereClause() {
        if (StringUtils.isNotEmpty(searchCriteria.getNeedByDateTo())) {
            return SQLUtils.buildWhereClauseLessOrEqualWithDate("r.DATE_NEEDED", searchCriteria.getNeedByDateTo(), DateUtils.DEFAULT_DATE_FORMAT);
        }
        return "";
    }


}
