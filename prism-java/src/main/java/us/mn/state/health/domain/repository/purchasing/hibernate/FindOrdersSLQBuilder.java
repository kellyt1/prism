package us.mn.state.health.domain.repository.purchasing.hibernate;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import us.mn.state.health.domain.repository.purchasing.OrderSearchCriteria;
import us.mn.state.health.domain.repository.util.SQLBuilder;
import us.mn.state.health.util.NotYetImplementedException;
import us.mn.state.health.util.Notification;
import us.mn.state.health.util.NotificationException;
import us.mn.state.health.util.SQLUtils;
import us.mn.state.health.util.hibernate.SearchCriteria;

public class FindOrdersSLQBuilder extends SQLBuilder {
    private static final String SUSPENSE_DATE_ALIAS = "order_.SUSPENSE_DATE";
    private static final String ORDER_DATE_ALIAS = "order_.INSERTION_DATE";

    //used for item description, catalog number and as a link for rli joins
    private static StringBuilder oliJoins = new StringBuilder("left outer join MATERIALS_ORDER_LINE_ITEM_TBL oli on order_.ORDER_ID=oli.ORDER_ID\n");

    //used for item description and as a link for org budget joins, request joins
    private static StringBuilder rliJoins = new StringBuilder(
            "left outer join MATERIALS_REQ_LINE_ITEM_TBL oliRli on oli.ORDER_LINE_ITEM_ID=oliRli.ORDER_LN_ITM_ID\n" +
                    "left outer join ITEM_TBL oliRliItem on oliRli.ITEM_ID=oliRliItem.ITEM_ID\n" +
                    "left outer join MATERIALS_REQ_LINE_ITEM_TBL rli on order_.ORDER_ID=rli.ORDER_ID\n" +
                    "left outer join ITEM_TBL rliItem on rli.ITEM_ID=rliItem.ITEM_ID\n");

    private static StringBuilder stockItemJoins = new StringBuilder(
            "left outer join CATEGORY_TBL rliItemCategory on rliItem.CATEGORY_ID=rliItemCategory.CATEGORY_ID\n" +
                    "left outer join CATEGORY_TBL oliRliItemCategory on oliRliItem.CATEGORY_ID=oliRliItemCategory.CATEGORY_ID\n" +
                    "left outer join STOCK_ITEM_TBL rliStockItem on rliItem.ITEM_ID=rliStockItem.STOCK_ITEM_ID\n" +
                    "left outer join STOCK_ITEM_TBL oliRliStockItem on oliRliItem.ITEM_ID=oliRliStockItem.STOCK_ITEM_ID \n");

    //used for orgBdgt joins
    private static StringBuilder orgBdgtJoins = new StringBuilder(
            "left outer join MATERIALS_RLI_FND_SRC_TBL oliRliFs on oliRli.REQUEST_LINE_ITEM_ID=oliRliFs.REQUEST_LINE_ITEM_ID\n" +
                    "left outer join ORG_BDGT_TBL_VIEW oliRliFsOrgBdgt on oliRliFs.ORG_BDGT_ID=oliRliFsOrgBdgt.ORG_BDGT_ID\n" +
                    "left outer join MATERIALS_RLI_FND_SRC_TBL rliFs on rli.REQUEST_LINE_ITEM_ID=rliFs.REQUEST_LINE_ITEM_ID\n" +
                    "left outer join ORG_BDGT_TBL_VIEW rliFsOrgBdgt on rliFs.ORG_BDGT_ID=rliFsOrgBdgt.ORG_BDGT_ID\n"
    );
    //used for request joins
    private static StringBuilder requestJoins = new StringBuilder(
            "left outer join MATERIALS_REQUEST_TBL oliRliRequest on oliRli.REQUEST_ID=oliRliRequest.REQUEST_ID\n" +
                    "left outer join MATERIALS_REQUEST_TBL rliRequest on rli.REQUEST_ID=rliRequest.REQUEST_ID\n"
    );

    private static StringBuilder vendorJoins = new StringBuilder(
            "left outer join VENDOR_TBL vendor on order_.VENDOR_ID=vendor.VENDOR_ID\n" +
                    "inner join EXTERNAL_ORG_TBL extOrg on vendor.ORG_ID=extOrg.ORG_ID\n");

    private static StringBuilder buyerJoins = new StringBuilder(
            "left outer join PERSON_TBL buyer on order_.PURCHASER_ID=buyer.PERSON_ID\n");

    private static StringBuilder whereClause = new StringBuilder(" where 1=1 ");

    private static StringBuilder baseSqlQuery =
            new StringBuilder(" from\n" +
                    "MATERIALS_ORDER_TBL order_\n").append(vendorJoins).append(buyerJoins);

    private OrderSearchCriteria searchCriteria;

    public FindOrdersSLQBuilder(OrderSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public String buildSqlQuery() throws NotificationException {
        return "select distinct order_.*, buyer.last_name, extOrg.org_name " + buildBaseQuery();
    }

    public String buildCountSqlQuery() throws NotificationException {
        return "select count(*) from (" + buildSqlQuery() + ")";
    }

    private String buildBaseQuery() throws NotificationException {
        Notification notification = new Notification();
        StringBuilder result = new StringBuilder()
                .append(baseSqlQuery)
                .append(buildJoins())
                .append(whereClause)
                .append(buildPoNmbrWhereClause())
                .append(buildOPRNmbrWhereClause())
                .append(buildBuyerWhereClause())
                .append(buildStatusWhereClause())
                .append(buildRequestorWhereClause())
                .append(buildTrackingNumberWhereClause())
                .append(buildOrderedBetweenWhereClause(notification))
                .append(buildSuspenseDateWhereClause(notification))
                .append(buildOrgBdgtWhereClause())
                .append(buildItemDescriptionWhereClause())
                .append(buildVendorWhereClause())
                .append(buildIcnbrWhereClause())
                .append(buildCatalogNbrWhereClause())
                .append(buildShippingAddressWhereClause())
                .append(buildOrderByClause());
        if (notification.hasErrors()) throw new NotificationException(notification);
        return result
                .toString();
    }

    private StringBuilder buildOrderByClause() {
        StringBuilder orderByClause = new StringBuilder(" order by ");
        String sortMethod = searchCriteria.getSortMethod();
        if (!Arrays.asList(SearchCriteria.SORT_METHODS).contains(sortMethod)) {
            sortMethod = SearchCriteria.ASC;
        }
        switch (searchCriteria.getSortBy()) {
            case OrderSearchCriteria.SORT_BY_ORDER_ID:
                orderByClause.append(" order_.order_id ").append(sortMethod);
                break;
            case OrderSearchCriteria.SORT_BY_PO_NO:
                orderByClause.append(" order_.PO_NUMBER ").append(sortMethod);
                break;
            case OrderSearchCriteria.SORT_BY_VENDOR:
                orderByClause.append(" extOrg.ORG_NAME ").append(sortMethod);
                break;
            case OrderSearchCriteria.SORT_BY_BUYER:
                orderByClause.append(" buyer.LAST_NAME ").append(sortMethod);
                break;
            case OrderSearchCriteria.SORT_BY_SUSPENSE_DATE:
                orderByClause.append(" order_.SUSPENSE_DATE ").append(sortMethod);
                break;
            default:
                throw new NotYetImplementedException();
        }
        return orderByClause;
    }

    private StringBuilder buildIcnbrWhereClause() {
        if (StringUtils.isNotBlank(searchCriteria.getIcnbr())) {
            return new StringBuilder(" and ( 0=1 ")
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingOr("rliItemCategory.category_code ||'-'|| LPAD(rliStockItem.icnbr,4,'0')", searchCriteria.getIcnbr(), true)))
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingOr("oliRliItemCategory.category_code||'-'|| LPAD(oliRliStockItem.icnbr,4,'0')", searchCriteria.getIcnbr(), true)))
                    .append(") ");
        }
        return new StringBuilder();
    }

    private StringBuilder buildVendorWhereClause() {
        if (StringUtils.isNotBlank(searchCriteria.getVendorName())) {
            return new StringBuilder(new StringBuilder(
                    SQLUtils.buildWhereClauseUsingAnd("extOrg.ORG_NAME", searchCriteria.getVendorName(), true))
            );
        }
        return new StringBuilder();
    }

    private StringBuilder buildItemDescriptionWhereClause() {
        if (StringUtils.isNotBlank(searchCriteria.getItemDescription())) {
            return new StringBuilder(" and ( 0=1 ")
                    .append(" or (1=1 ")
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingAnd("oli.DESCRPTN", searchCriteria.getItemDescription(), true)))
                    .append(") or (1=1 ")
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingAnd("rli.ITEM_DESCRIPTION", searchCriteria.getItemDescription(), true)))
                    .append(") or (1=1 ")
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingAnd("oliRli.ITEM_DESCRIPTION", searchCriteria.getItemDescription(), true)))
                    .append(") or (1=1 ")
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingAnd("rliItem.DESCRIPTION", searchCriteria.getItemDescription(), true)))
                    .append(") or (1=1 ")
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingAnd("oliRliItem.DESCRIPTION", searchCriteria.getItemDescription(), true)))
                    .append(")) ");
        }
        return new StringBuilder();
    }

    private StringBuilder buildOrgBdgtWhereClause() {
        if (StringUtils.isNotBlank(searchCriteria.getOrgCode())) {
            return new StringBuilder(" and ( 0=1 ")
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingOr("oliRliFsOrgBdgt.ORGDISPLAY", searchCriteria.getOrgCode(), true)))
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingOr("rliFsOrgBdgt.ORGDISPLAY", searchCriteria.getOrgCode(), true)))
                    .append(") ");
        }
        return new StringBuilder();
    }

    private StringBuilder buildCatalogNbrWhereClause() {
        return StringUtils.isNotBlank(searchCriteria.getItemModel()) ?
                new StringBuilder(SQLUtils.buildWhereClauseUsingAnd("oli.VENDOR_CATALOG_NBR", searchCriteria.getItemModel(), true))
                : new StringBuilder();
    }

    private StringBuilder buildJoins() {
        StringBuilder joins = new StringBuilder();
        if (StringUtils.isNotBlank(searchCriteria.getItemDescription())
                || StringUtils.isNotBlank(searchCriteria.getItemModel())
                || StringUtils.isNotBlank(searchCriteria.getIcnbr())
                || StringUtils.isNotBlank(searchCriteria.getOrgCode())
                || StringUtils.isNotBlank(searchCriteria.getRequestTrackingNo())
                || StringUtils.isNotBlank(searchCriteria.getStatusId())
                || StringUtils.isNotBlank(searchCriteria.getRequestorId())
                ) {
            joins.append(oliJoins);
        }
        if (StringUtils.isNotBlank(searchCriteria.getItemDescription())
                || StringUtils.isNotBlank(searchCriteria.getIcnbr())
                || StringUtils.isNotBlank(searchCriteria.getOrgCode())
                || StringUtils.isNotBlank(searchCriteria.getRequestTrackingNo())
                || StringUtils.isNotBlank(searchCriteria.getRequestorId())
                || StringUtils.isNotBlank(searchCriteria.getStatusId())
                ) {
            joins.append(rliJoins);
        }
        if (StringUtils.isNotBlank(searchCriteria.getRequestTrackingNo())
                || StringUtils.isNotBlank(searchCriteria.getRequestorId())
                ) {
            joins.append(requestJoins);
        }

        if (StringUtils.isNotBlank(searchCriteria.getOrgCode())) {
            joins.append(orgBdgtJoins);
        }

        if (StringUtils.isNotBlank(searchCriteria.getVendorName())) {
//            joins.append(vendorJoins);
            joins.append(" ");
        }

        if (StringUtils.isNotBlank(searchCriteria.getIcnbr())) {
            joins.append(stockItemJoins);
        }

        return joins;
    }

    private StringBuilder buildPoNmbrWhereClause() {
        if (StringUtils.isNotBlank(searchCriteria.getPoNo())) {
            return new StringBuilder(SQLUtils.buildWhereClauseUsingAnd("order_.po_number", searchCriteria.getPoNo(), false));
        }
        return new StringBuilder();
    }

    private StringBuilder buildOPRNmbrWhereClause() {
        if (StringUtils.isNotBlank(searchCriteria.getOprNo())) {
            return new StringBuilder(SQLUtils.buildWhereClauseUsingAnd("order_.order_id", searchCriteria.getOprNo(), false));
        }
        return new StringBuilder();
    }

    private StringBuilder buildRequestorWhereClause() {
        if (StringUtils.isNotBlank(searchCriteria.getRequestorId())) {
            return new StringBuilder(" and ( 0=1 ")
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingOr("rliRequest.requestor_person_id", searchCriteria.getRequestorId(), false)))
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingOr("oliRliRequest.requestor_person_id", searchCriteria.getRequestorId(), false)))
                    .append(") ");
        }
        return new StringBuilder();
    }

    private StringBuilder buildTrackingNumberWhereClause() {
        if (StringUtils.isNotBlank(searchCriteria.getRequestTrackingNo())) {
            return new StringBuilder(" and ( 0=1 ")
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingOr("rliRequest.TRACKING_NUMBER", "MRQ-" + searchCriteria.getRequestTrackingNo().trim(), false)))
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingOr("oliRliRequest.TRACKING_NUMBER", "MRQ-" + searchCriteria.getRequestTrackingNo().trim(), false)))
                    .append(") ");
        }
        return new StringBuilder();
    }

    private StringBuilder buildBuyerWhereClause() {
        if (StringUtils.isNotBlank(searchCriteria.getBuyerId())) {
            return new StringBuilder(SQLUtils.buildWhereClauseUsingAnd("order_.purchaser_id", searchCriteria.getBuyerId(), false));
        }
        return new StringBuilder();
    }

    private StringBuilder buildStatusWhereClause() {
        if (StringUtils.isNotBlank(searchCriteria.getStatusId())) {
            return new StringBuilder(" and ( 0=1 ")
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingOr("oli.status_id", searchCriteria.getStatusId(), false)))
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingOr("rli.status_id", searchCriteria.getStatusId(), false)))
                    .append(new StringBuilder(SQLUtils.buildWhereClauseUsingOr("oliRli.status_id", searchCriteria.getStatusId(), false)))
                    .append(") ");
        }
        return new StringBuilder();
    }

    private StringBuilder buildShippingAddressWhereClause() {
        if (StringUtils.isNotBlank(searchCriteria.getShippingAddressId())) {
            return new StringBuilder(SQLUtils.buildWhereClauseUsingAnd("order_.SHIP_TO_ADDR_ID", searchCriteria.getShippingAddressId(), false));
        }
        return new StringBuilder();
    }

    private StringBuilder buildSuspenseDateWhereClause(Notification notification) {
        return super.buildSuspenseDateWhereClause(notification,
                new DateWhereClauseInfoValueObject(SUSPENSE_DATE_ALIAS,
                        searchCriteria.getSuspenseDateFrom(), "order.suspenseDateFrom", "Invalid Suspense Date From:" + searchCriteria.getSuspenseDateFrom(),
                        searchCriteria.getSuspenseDateTo(), "order.suspenseDateTo", "Invalid Suspense Date To:" + searchCriteria.getSuspenseDateTo()));
    }

    private StringBuilder buildOrderedBetweenWhereClause(Notification notification) {
        return super.buildSuspenseDateWhereClause(notification,
                new DateWhereClauseInfoValueObject(ORDER_DATE_ALIAS,
                        searchCriteria.getOrderedFrom(), "order.orderedFrom", "Invalid Ordered Date From:" + searchCriteria.getOrderedFrom(),
                        searchCriteria.getOrderedTo(), "order.orderedTo", "Invalid Ordered Date To:" + searchCriteria.getOrderedTo()));
    }

}
