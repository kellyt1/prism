package us.mn.state.health.view.materialsrequest;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class BrowseCatalogForm extends ValidatorForm {
    private String selectedCategory;
    private ArrayList categoriesPath = new ArrayList();
    private ArrayList currentSubCategories = new ArrayList();
    private Collection currentItems = new ArrayList();
    private Integer startIndex = new Integer(0);
    private Integer maxItems = new Integer(20);
    private String paginationDirection;
    private Integer totalNumItemsInResults = new Integer(0);
    private String query;
    private int searchType = KEYWORD_SEARCH_TYPE;
    
    public static int KEYWORD_SEARCH_TYPE = 1;
    public static int CATEGORY_SEARCH_TYPE = 2;
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        paginationDirection = "";
    }
    
    public void resetStartIndex() {
        int start = startIndex.intValue();
        int max = maxItems.intValue();
        if("prev".equals(paginationDirection)) {
            start = Math.max(0, start - max);
        }
        else if("next".equals(paginationDirection)) {
            start = Math.min(start + max, totalNumItemsInResults.intValue());
        }
        else if("first".equals(paginationDirection)) {
            start = 0;
        }
        else if("last".equals(paginationDirection)) {
            start = totalNumItemsInResults.intValue() - (max - 1);
        }
        else {
            start = 0;
        }
        startIndex = new Integer(start);
    }   
    
    public int getCurrentSubCategoriesSize() {
        return currentSubCategories.size();
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public ArrayList getCategoriesPath() {
        return categoriesPath;
    }

    public void setCategoriesPath(ArrayList categoriesPath) {
        this.categoriesPath = categoriesPath;
    }

    public Collection getCurrentSubCategories() {
        return currentSubCategories;
    }

    public void setCurrentSubCategories(ArrayList currentSubCategories) {
        this.currentSubCategories = currentSubCategories;
    }

    public Collection getCurrentItems() {
        return currentItems;
    }

    public void setCurrentItems(Collection currentItems) {
        this.currentItems = currentItems;
    }

    public String getCategoriesPathSizeMinus1() {
        if(categoriesPath != null) {
            return new String(""+(categoriesPath.size() - 1));
        }
        return new String("0");
    }


    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }


    public Integer getStartIndex() {
        return startIndex;
    }


    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }


    public Integer getMaxItems() {
        return maxItems;
    }


    public void setPaginationDirection(String paginationDirection) {
        this.paginationDirection = paginationDirection;
    }


    public String getPaginationDirection() {
        return paginationDirection;
    }


    public void setQuery(String query) {
        this.query = query;
    }


    public String getQuery() {
        return query;
    }


    public void setTotalNumItemsInResults(Integer totalNumItemsInResults) {
        this.totalNumItemsInResults = totalNumItemsInResults;
    }


    public Integer getTotalNumItemsInResults() {
        return totalNumItemsInResults;
    }


    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }


    public int getSearchType() {
        return searchType;
    }
}
