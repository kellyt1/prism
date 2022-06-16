package us.mn.state.health.builder.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.search.BooleanQuery;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.util.Form;
import us.mn.state.health.model.inventory.PurchaseItem;
import us.mn.state.health.model.util.search.LuceneQueryBuilder;
import us.mn.state.health.model.util.search.PurchaseItemIndex;
import us.mn.state.health.model.util.search.StockItemIndex;
import us.mn.state.health.view.inventory.ItemsAdvancedSearchForm;

public class ItemsAdvancedSearchFormBuilder {
    private DAOFactory factory;
    private ItemsAdvancedSearchForm form;
    private static Log log = LogFactory.getLog(ItemsAdvancedSearchFormBuilder.class);

    public ItemsAdvancedSearchFormBuilder(ItemsAdvancedSearchForm form, DAOFactory factory) {
        this.form = form;
        this.factory = factory;
    }

    public void buildContacts() throws InfrastructureException {
        Collection contacts = factory.getPersonDAO().findAllMDHEmployees();
        form.setContacts(contacts);
    }

    public void buildCategories() throws InfrastructureException {
        Collection categories = factory.getCategoryDAO().findDescendantCategoriesByParentCode("MAT", false);
        form.setCategories(categories);
    }

    public void buildUnits() throws InfrastructureException {
        Collection units = factory.getUnitDAO().findAll(false);
        form.setUnits(units);
    }

    public void buildManufacturers() throws InfrastructureException {
        Collection manufacturers = factory.getManufacturerDAO().findAll(false);
        form.setManufacturers(manufacturers);
    }

    public void buildLocations() throws InfrastructureException {
//        Collection locations = factory.get
    }

    public void buildOrgBudgets() throws InfrastructureException {
        Collection orgBudgets = factory.getOrgBudgetDAO().findAllPurchaseOrgBudgets();
        form.setOrgBudgets(orgBudgets);
    }

    public void buildStatuses() throws InfrastructureException {
        Collection statuses = factory.getStatusDAO().findAllByStatusTypeCode("STI");
        form.setStatuses(statuses);
    }

    public void buildCatalogResults() throws InfrastructureException {
        if (form.getItemTypeSearchOption().equals(Form.ONLY_PURCHASE_ITEMS)) {
            if (form.getIcnbr().trim().equals("") &&
                    form.getStatus().trim().equals("") &&
                    form.getOrgBudget().trim().equals("")) {
                //buildPurchaseResults
                Collection purchaseItemResults = buildPurchaseItemResults();
                form.setResults(factory.getItemDAO().findItemsUsingIds(purchaseItemResults));
            } else {
                Collection purchaseItemResults = buildPurchaseItemResults();
                form.setResults(factory.getItemDAO().findItemsUsingIds(purchaseItemResults));
            }
        } else if (form.getItemTypeSearchOption().equals(Form.ONLY_STOCK_ITEMS)) {
            //buildStockItemResults
            Collection stockItemResults = buildStockItemResults();
            form.setResults(factory.getItemDAO().findItemsUsingIds(stockItemResults));
        }
        //the case when we want to search all the types of items
        else {
            //buildResults
            Collection purchaseItemResults = buildPurchaseItemResults();
            Collection stockItemResults = buildStockItemResults();
            Collection results = new ArrayList();
            results.addAll(purchaseItemResults);
            results.addAll(stockItemResults);
            form.setResults(factory.getItemDAO().findItemsUsingIds(results));
        }
    }

    public void buildStockItemsResults() throws InfrastructureException {
        Collection stockItemResults = buildStockItemResults();
        Collection results = new ArrayList();
        results.addAll(stockItemResults);
        form.setResults(factory.getStockItemDAO().findItemsUsingIds(results));
    }

    public Collection buildPurchaseItemResults() throws InfrastructureException {
        BooleanQuery currentSearchingItems = new BooleanQuery();

        LuceneQueryBuilder luceneQueryBuilder = new LuceneQueryBuilder(form, currentSearchingItems);

        luceneQueryBuilder.addAndAll("description", PurchaseItemIndex.DESCRIPTION);
        luceneQueryBuilder.addAndAll("model", PurchaseItemIndex.MODEL);
        luceneQueryBuilder.addAndAll("dispenseUnit", PurchaseItemIndex.DISPENSE_UNIT);
        luceneQueryBuilder.addAndAll("categoryCode", PurchaseItemIndex.CATEGORY_CODE);
        luceneQueryBuilder.addAndAll("hazardous", PurchaseItemIndex.HAZARDOUS);
        luceneQueryBuilder.addAndAll("manufacturer", PurchaseItemIndex.MANUFACTURER_ID);
        luceneQueryBuilder.addAndAll("vendor", PurchaseItemIndex.VENDOR);
        log.debug("PurchaseItemsQuery:" + currentSearchingItems.toString());
        Collection<Long> results = new ArrayList<Long>();
        if (StringUtils.nullOrBlank(currentSearchingItems.toString())) {
            return results;
        }
        try {
            results = new PurchaseItemIndex().searchIds(currentSearchingItems);
            Collection<Long> purchaseItemsEndDated = new ArrayList<Long>();
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
            for (Long id : results) {
                PurchaseItem pItem = (PurchaseItem) daoFactory.getPurchaseItemDAO().getItemById(id);
                if (pItem == null || (pItem.getEndDate() != null && pItem.getEndDate().before(new Date()))) {
                    purchaseItemsEndDated.add(id);
                }
            }
            results.removeAll(purchaseItemsEndDated);
        }
        catch (InfrastructureException e) {
            log.error("Error in ItemsAdvancedSearchFormBuilder.buildPurchaseItemResults()", e);
        }
        return results;
    }

    public Collection buildStockItemResults() throws InfrastructureException {
        BooleanQuery currentSearchingItems = new BooleanQuery();

        LuceneQueryBuilder luceneQueryBuilder = new LuceneQueryBuilder(form, currentSearchingItems);

        luceneQueryBuilder.addAndAll("description", StockItemIndex.DESCRIPTION);
        luceneQueryBuilder.addAndAll("model", StockItemIndex.MODEL);
        luceneQueryBuilder.addAndAll("dispenseUnit", StockItemIndex.DISPENSE_UNIT);
        luceneQueryBuilder.addAndAll("categoryCode", StockItemIndex.CATEGORY_CODE);
        luceneQueryBuilder.addAndAll("hazardous", StockItemIndex.HAZARDOUS);
        luceneQueryBuilder.addAndAll("manufacturer", StockItemIndex.MANUFACTURER_ID);
        luceneQueryBuilder.addAndAll("vendor", StockItemIndex.VENDOR);

        String queryField;
        if (form.getIcnbr().lastIndexOf("-") > 0) {
            queryField = StockItemIndex.FULL_ICNBR;
        } else {
            queryField = StockItemIndex.ICNBR;
        }
        luceneQueryBuilder.addAndAll("icnbr", queryField);
        luceneQueryBuilder.addAndAll("status", StockItemIndex.STATUS);
        luceneQueryBuilder.addAndAll("orgBudget", StockItemIndex.ORG_BUDGET_ID);
        luceneQueryBuilder.addAndAll("seasonal", StockItemIndex.SEASONAL);
        luceneQueryBuilder.addAndAny("contact", StockItemIndex.PRIMARY_CONTACT_ID);
        luceneQueryBuilder.addAndAny("contact", StockItemIndex.SECONDARY_CONTACT_ID);

        log.debug("StockItemsQuery:" + currentSearchingItems.toString());
        Collection results = new ArrayList();
        if (StringUtils.nullOrBlank(currentSearchingItems.toString())) {
            return results;
        }
        try {
            results = new StockItemIndex().searchIds(currentSearchingItems);
        }
        catch (InfrastructureException e) {
            log.error("Error in ItemsAdvancedSearchFormBuilder.buildStockItemResults()", e);
        }
        return results;
    }


}
