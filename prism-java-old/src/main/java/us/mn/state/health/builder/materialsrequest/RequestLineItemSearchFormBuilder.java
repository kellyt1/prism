package us.mn.state.health.builder.materialsrequest;

import org.apache.lucene.search.BooleanQuery;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.*;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.util.search.LuceneQueryBuilder;
import us.mn.state.health.model.util.search.RequestLineItemIndex;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemSearchForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class RequestLineItemSearchFormBuilder {

    private RequestLineItemSearchForm rliSearchForm;
    private DAOFactory daoFactory;

    public RequestLineItemSearchFormBuilder(RequestLineItemSearchForm rliSearchForm,
                                            DAOFactory daoFactory) {
        this.rliSearchForm = rliSearchForm;
        this.daoFactory = daoFactory;
    }

    public void buildAdvancedSearchResults() throws InfrastructureException {
        BooleanQuery query = new BooleanQuery();
        LuceneQueryBuilder queryBuilder = new LuceneQueryBuilder(rliSearchForm, query);
        queryBuilder.addAndAny("requestTrackingNumber", RequestLineItemIndex.REQUEST_TRACKING_NUMBER);
        queryBuilder.addAndMatchPhrase("vendor.vendorId", RequestLineItemIndex.VENDOR_IDS);
        queryBuilder.addAndAny("vendorName", RequestLineItemIndex.VENDOR_NAMES);
        queryBuilder.addAndAny("statusCode", RequestLineItemIndex.RLI_STATUS_CODE);
        queryBuilder.addAndAny("category.categoryCode", RequestLineItemIndex.ITEM_CATEGORY_CODE);
        queryBuilder.addAndAny("purchaser.personId", RequestLineItemIndex.PURCHASER_ID);
        queryBuilder.addAndAny("requestor.personId", RequestLineItemIndex.REQUESTOR_ID);
        queryBuilder.addAndAny("orgBudgetCode", RequestLineItemIndex.FUNDING_SRC_ORG_BDGT_CODES);
        queryBuilder.addAndAny("itemDescription", RequestLineItemIndex.ITEM_DESCRIPTION);
        queryBuilder.addAndDateRangeInclusive("neededByFrom", "neededByTo", RequestLineItemIndex.NEED_BY_DATE);

        Collection reqLnItms = new RequestLineItemIndex().search(query);
        Collection rliForms = new ArrayList();
        for (Iterator i = reqLnItms.iterator(); i.hasNext();) {
            RequestLineItemForm rliForm = new RequestLineItemForm();
            RequestLineItem rli = (RequestLineItem) i.next();
            if (rli.getItem() != null && rli.getItem().getItemType().equals(Item.STOCK_ITEM)) {
                StockItem si = daoFactory.getStockItemDAO().getStockItemById(rli.getItem().getItemId(), false);
                rliForm.setIcnbr(si.getFullIcnbr());
                for (Iterator j = si.getItemVendors().iterator(); j.hasNext();) {
                    ItemVendor ivLink = (ItemVendor) j.next();
                    if (!ivLink.getPrimaryVendor().booleanValue()) {
                        continue;//add only the primary vendor
                    }
                    Vendor vndr = ivLink.getVendor();
                    ExternalOrgDetail eod = vndr.getExternalOrgDetail();
                    rliForm.getVendorNames().add(eod.getOrgName());
                    rliForm.getVendorCatalogNbrs().add(ivLink.getVendorCatalogNbr());
                }
            }
            rliForm.setRequestLineItem(rli);
            rliForm.setRequestLineItemId(rli.getRequestLineItemId().toString());
            rliForms.add(rliForm);
        }
        rliSearchForm.setRliForms(rliForms);
    }

    public void buildCategories() throws InfrastructureException {
        Collection categories = daoFactory.getCategoryDAO().findAll(false);
        rliSearchForm.setCategories(categories);
    }

    public void buildDefaultProperties() {
        rliSearchForm.setCategoryId(null);
        rliSearchForm.setItemDescription(null);
        rliSearchForm.setNeededByFrom(null);
        rliSearchForm.setNeededByTo(null);
        rliSearchForm.setOrgBudgetCode(null);
        rliSearchForm.setPurchaserId(null);
        rliSearchForm.setRequestId(null);
        rliSearchForm.setRequestLineItemId(null);
        rliSearchForm.setSearchRequestLineItemId(null);
        rliSearchForm.setRequestorId(null);
        rliSearchForm.setRequestTrackingNumber(null);
        rliSearchForm.setRliForms(new ArrayList());
        rliSearchForm.setVendorId(null);
    }

    public void buildPurchasers() throws InfrastructureException {
        Collection purchasers = daoFactory.getPersonDAO().findPersonsByGroupCode(Group.BUYER_CODE);
        rliSearchForm.setPurchasers(purchasers);
    }

    public void buildRequestors() throws InfrastructureException {
        Collection requestors = daoFactory.getPersonDAO().findAllMDHEmployeesAsDTO();
        rliSearchForm.setRequestors(requestors);
    }

    private void buildStatuses(String[] statusCodes, String initialStatusCode) throws InfrastructureException {
        Collection statuses = daoFactory.getStatusDAO()
                .findAllByStatusTypeAndStatusCodes(StatusType.MATERIALS_REQUEST, statusCodes);
        rliSearchForm.setStatusCode(initialStatusCode);
        rliSearchForm.setDefaultStatusCodes(StringUtils.toDelimitedString(statusCodes, " "));
        rliSearchForm.setStatuses(statuses);
    }

    public void buildPurchasingStatuses() throws InfrastructureException {
        String[] statusCodes = {Status.WAITING_FOR_APPROVAL,
                Status.WAITING_FOR_PURCHASE,
                Status.PENDING_BUILDING_ORDER,
                Status.PENDING_NEED_MORE_INFO,
                Status.PENDING_OUT_FOR_BID,
                Status.MNIT_ORDERED,
                Status.MNIT_ENCUMBERED,
                Status.ORDERED,
                Status.FAILED_INCUMBRANCE,
                Status.CANCELLED_ITEM_DISCONTINUED,
                Status.DELIVERED,
                Status.DELIVERED_PARTIAL ,
                Status.STANDING_LAB_ORDER,
                Status.COMPUTER_PURCHASE_ORDER // ??
//                Status.RECEIVED,
//                Status.RECEIVED_PARTIAL
                };
        buildStatuses(statusCodes, Status.WAITING_FOR_PURCHASE);
    }

    /**
     * Don't use this unless you have to.  We moved away from loading all the vendors
     * for a page because its way too slow.  In this case, we were just doing this for
     * building a search form.  Instead of a drop-down, we put in a text box.
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildVendors() throws InfrastructureException {
//        Collection vendors = daoFactory.getVendorDAO().findAll(false);
        Collection vendors = daoFactory.getVendorDAO().findAll();
        rliSearchForm.setVendors(vendors);
    }
}