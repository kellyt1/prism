package us.mn.state.health.builder.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lucene.SmartDayQueryParser;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.util.Form;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.util.UtilStaticMethods;
import us.mn.state.health.model.util.search.AssetIndex;
import us.mn.state.health.model.util.search.LuceneQueryBuilder;
import us.mn.state.health.view.inventory.AssetSearchForm;

public class AssetSearchFormBuilder {
    private DAOFactory factory;
    private AssetSearchForm form;
    private static Log log = LogFactory.getLog(AssetSearchFormBuilder.class);

    public AssetSearchFormBuilder(AssetSearchForm form, DAOFactory factory) {
        this.form = form;
        this.factory = factory;
    }

    public void buildContacts() throws InfrastructureException {
        Collection contacts = factory.getPersonDAO().findAllMDHEmployees();
        form.setContacts(contacts);
    }

    public void buildClassCodes() throws InfrastructureException {
        Collection classCodes = factory.getClassCodeDAO().findAll();
        form.setClassCodes(classCodes);
    }

    public void buildCategories() throws InfrastructureException {
        Collection categories = factory.getCategoryDAO().findDescendantCategoriesByParentCode("MAT", false);
        form.setCategories(categories);
    }

    public void buildManufacturers() throws InfrastructureException {
        Collection manufacturers = factory.getManufacturerDAO().findAll(false);
        form.setManufacturers(manufacturers);
    }

    public void buildFacilities() throws InfrastructureException {
        Collection facilities = factory.getFacilityDAO().findAllFacilities();
        form.setFacilities(facilities);
    }

    public void buildOrgBudgets() throws InfrastructureException {
        Collection orgBudgets = factory.getOrgBudgetDAO().findAllPurchaseOrgBudgets();
        form.setOrgBudgets(orgBudgets);
    }

    public void buildStatuses() throws InfrastructureException {
        Collection statuses = factory.getStatusDAO().findAllByStatusTypeCode(StatusType.ASSET);
        form.setStatuses(statuses);
    }

    public void buildResults() throws InfrastructureException {
        if (form.getItemTypeSearchOption().equals(Form.ONLY_FIXED_ASSETS)) {
            Collection fixedAssetResults = buildFixedAssetResults();
            form.setResults(fixedAssetResults);
        } else if (form.getItemTypeSearchOption().equals(Form.ONLY_SENSITIVE_ASSETS)) {
            //buildStockItemResults
            Collection sensitiveAssetResults = buildSensitiveAssetResults();
            form.setResults(sensitiveAssetResults);
        } else {  // the case when we want to search all the types of assets
            //buildResults
            Collection fixedAssetResults = buildFixedAssetResults();
            Collection sensitiveAssetResults = buildSensitiveAssetResults();
            Collection results = new ArrayList();
            results.addAll(fixedAssetResults);
            results.addAll(sensitiveAssetResults);
            form.setResults(results);
        }
    }

    private Collection buildFixedAssetResults() throws InfrastructureException {
        BooleanQuery mainQuery = new BooleanQuery();
        Query classNameQuery = createTermQuery(AssetIndex.CLASSNAME, "us.mn.state.health.model.inventory.FixedAsset");
        mainQuery.add(classNameQuery, BooleanClause.Occur.MUST);

        if (!form.getDescription().trim().equals("")) {
            Query descriptionQuery = createDescriptionQuery(form.getDescription(), form.getDescriptionSearchOption());
            mainQuery.add(descriptionQuery, BooleanClause.Occur.MUST);
        }
        if (!form.getCategoryCode().trim().equals("")) {
            Query query = createTermQuery(AssetIndex.CATEGORY_CODE, form.getCategoryCode());
            mainQuery.add(query, BooleanClause.Occur.MUST);
        }
        if (!form.getModel().trim().equals("")) {
            Query query = createTermQuery(AssetIndex.MODEL, form.getModel());
            mainQuery.add(query, BooleanClause.Occur.MUST);
        }
        if (!form.getSerialNumber().trim().equals("")) {
            Query query = createTermQuery(AssetIndex.SERIAL_NUMBER, form.getSerialNumber());
            mainQuery.add(query, BooleanClause.Occur.MUST);
        }
        if (!form.getAssetNumber().trim().equals("")) {
            Query query = createTermQuery(AssetIndex.ASSET_NUMBER, form.getAssetNumber());
            mainQuery.add(query, BooleanClause.Occur.MUST);
        }
        if (!form.getVendor().trim().equals("")) {
            Query query = createTermQuery(AssetIndex.VENDOR_NAME, form.getVendor());
            mainQuery.add(query, BooleanClause.Occur.MUST);
        }
        if (!form.getManufacturer().trim().equals("")) {
            Query query = createTermQuery(AssetIndex.MANUFACTURER_ID, form.getManufacturer());
            mainQuery.add(query, BooleanClause.Occur.MUST);
        }
        if (!form.getStatusCode().trim().equals("")) {
            Query query = createTermQuery(AssetIndex.STATUS_CODE, form.getStatusCode());
            mainQuery.add(query, BooleanClause.Occur.MUST);
        }
        if (!form.getClassCode().trim().equals("")) {
            Query query = createTermQuery(AssetIndex.CLASS_CODE, form.getClassCode());
            mainQuery.add(query, BooleanClause.Occur.MUST);
        }
        if (!form.getOrgBudget().trim().equals("")) {
            Query query = createTermQuery(AssetIndex.ORG_BUDGET_IDS, form.getOrgBudget());
            mainQuery.add(query, BooleanClause.Occur.MUST);
        }
        if (!form.getContact().trim().equals("")) {
            Query query = createTermQuery(AssetIndex.CONTACT_PERSON_ID, form.getContact());
            mainQuery.add(query, BooleanClause.Occur.MUST);
        }
        if (!form.getFacility().trim().equals("")) {
            Query query = createTermQuery(AssetIndex.FACILITY_ID, form.getFacility());
            mainQuery.add(query, BooleanClause.Occur.MUST);
        }
        if (!form.getDateReceivedFrom().trim().equals("") && !form.getDateReceivedTo().trim().equals("")) {
            Query query = createDateReceivedQuery(form.getDateReceivedFrom().trim(), form.getDateReceivedTo().trim());
            mainQuery.add(query, BooleanClause.Occur.MUST);
        }

        Collection results = new ArrayList();
        try {
            results = new AssetIndex().search(mainQuery);
        }
        catch (InfrastructureException e) {
            e.printStackTrace();
        }
        return results;
    }

    private Collection buildSensitiveAssetResults() {
        //TODO implement this
        BooleanQuery query = new BooleanQuery();
        Query classNameQuery = createTermQuery(AssetIndex.CLASSNAME, "us.mn.state.health.model.inventory.SensitiveAsset");
        query.add(classNameQuery, BooleanClause.Occur.MUST);
        LuceneQueryBuilder luceneQueryBuilder = new LuceneQueryBuilder(form, query);
        luceneQueryBuilder.addAndAll("description", AssetIndex.DESCRIPTION);
        luceneQueryBuilder.addAndAll("model", AssetIndex.MODEL);
        Collection results = new ArrayList();
        try {
            results = new AssetIndex().search(query);
        }
        catch (InfrastructureException e) {
            e.printStackTrace();
        }
        return results;
    }

    private Query createDateReceivedQuery(String dateReceivedFrom, String dateReceivedTo) {
        Term dateReceivedFromTerm = null;
        Term dateReceivedToTerm = null;
        if (!dateReceivedFrom.trim().equals("")) {
            dateReceivedFromTerm = new Term(AssetIndex.DATE_RECEIVED, dateReceivedFrom);
        } else {
            dateReceivedFromTerm = new Term(AssetIndex.DATE_RECEIVED, "01/01/1900");
        }

        if (!dateReceivedTo.trim().equals("")) {
            dateReceivedToTerm = new Term(AssetIndex.DATE_RECEIVED, dateReceivedTo);
        } else {
            dateReceivedToTerm = new Term(AssetIndex.DATE_RECEIVED, "01/01/2050");
        }
        Query query = new RangeQuery(dateReceivedFromTerm, dateReceivedToTerm, true);

        SmartDayQueryParser parser = new SmartDayQueryParser(AssetIndex.DATE_RECEIVED, new StandardAnalyzer());
        parser.setLocale(Locale.US);
        Query finalQuery = null;
        try {
            finalQuery = parser.parse(query.toString(AssetIndex.DATE_RECEIVED));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return finalQuery;
    }

    private Query createCategoriesQuery(String categoryCode, String categorySearchOption) throws InfrastructureException {
        BooleanQuery categoryQuery = new BooleanQuery();
        TermQuery tempCategoryQuery = null;
        boolean onlyInThisCategory = false;
        if (categorySearchOption.equals(Form.STRICTLY_CATEGORY)) {
            onlyInThisCategory = true;
        }
        if (onlyInThisCategory) {
            tempCategoryQuery = new TermQuery(new Term(AssetIndex.CATEGORY_CODE, categoryCode));
            categoryQuery.add(tempCategoryQuery, BooleanClause.Occur.MUST);
            return categoryQuery;
        }
        Collection subCategories = factory.getCategoryDAO().findDescendantCategoriesByParentCode(categoryCode, true);
        ArrayList stringSubCategories = UtilStaticMethods.createStringEnumerationFromCategories(subCategories);
        for (Object stringSubCategory : stringSubCategories) {
            tempCategoryQuery = new TermQuery(new Term(AssetIndex.CATEGORY_CODE, (String) stringSubCategory));
            categoryQuery.add(tempCategoryQuery, BooleanClause.Occur.SHOULD);
        }
        return categoryQuery;
    }


    /**
     * @param description
     * @param option      - can be one of the three constants from AssetSearchForm class
     *                    AssetSearchForm.ALL_WORDS
     *                    AssetSearchForm.ANY_WORD
     *                    AssetSearchForm.MATCH_PHRASE
     * @return descriptionQuery
     */
    private static Query createDescriptionQuery(String description, String option) {
        BooleanQuery descriptionQuery = new BooleanQuery();
        Query tempDescriptionQuery = null;

        if (option.equals(Form.ALL_WORDS)) {
            StringTokenizer tokenizer = new StringTokenizer(description);
            while (tokenizer.hasMoreTokens()) {
                tempDescriptionQuery = new PrefixQuery(new Term(AssetIndex.DESCRIPTION, tokenizer.nextToken()));
                descriptionQuery.add(tempDescriptionQuery, BooleanClause.Occur.MUST);
            }
            return descriptionQuery;
        } else if (option.equals(Form.ANY_WORD)) {
            StringTokenizer tokenizer = new StringTokenizer(description);
            while (tokenizer.hasMoreTokens()) {
                tempDescriptionQuery = new PrefixQuery(new Term(AssetIndex.DESCRIPTION, tokenizer.nextToken()));
                descriptionQuery.add(tempDescriptionQuery, BooleanClause.Occur.SHOULD);
            }
            return descriptionQuery;
        } else if (option.equals(Form.MATCH_PHRASE)) {
            description = "\"" + description + "\"";
            tempDescriptionQuery = new TermQuery(new Term(AssetIndex.DESCRIPTION, description));
            descriptionQuery.add(tempDescriptionQuery, BooleanClause.Occur.MUST);
            return descriptionQuery;
        }

        return descriptionQuery;
    }

    private Query createTermQuery(String searchField, String searchValue) {
        searchValue = "\"" + searchValue + "\"";
        return new TermQuery(new Term(searchField, searchValue));
    }
}
