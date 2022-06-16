package us.mn.state.health.builder.materialsrequest;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.view.materialsrequest.BrowseCatalogForm;

import java.util.ArrayList;
import java.util.Collection;

public class BrowseCatalogFormBuilder {
    private DAOFactory daoFactory;
    private BrowseCatalogForm form;

//    These fields are used for internal use   
    private ArrayList categoriesPath = new ArrayList();
    private Category category;
    private ArrayList currentItems = new ArrayList();

    public BrowseCatalogFormBuilder(BrowseCatalogForm browseCatalogForm, DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.form = browseCatalogForm;
    }
    
    private void initializeCategory() throws InfrastructureException {
        long selectedCategoryId = 0;
        if(!StringUtils.nullOrBlank(form.getSelectedCategory())) {
            selectedCategoryId = Long.parseLong(form.getSelectedCategory());
            category = daoFactory.getCategoryDAO().getCategoryById(new Long(selectedCategoryId), false);
        }
        else {  //default to MATERIALS MANAGEMENT
            category = daoFactory.getCategoryDAO().findByCategoryCode("MAT");
        }
    }

    public void buildCategoriesPath() throws InfrastructureException {
        initializeCategory();
        categoriesPath.add(0, category);
        Category parent = category.getParentCategory();
        while (parent != null) {
            categoriesPath.add(0, parent);
            parent = parent.getParentCategory();
        }
        form.setCategoriesPath(categoriesPath);
    }

    public void buildCurrentSubCategories() throws InfrastructureException {
        initializeCategory();
        form.setCurrentSubCategories(new ArrayList(category.getChildCategories()));
    }

    public void buildCurrentItems() throws InfrastructureException {        
        Collection items = new ArrayList();
        
        if(form.getSearchType() == BrowseCatalogForm.CATEGORY_SEARCH_TYPE) {
            initializeCategory();
//            items = daoFactory.getItemDAO().findByCategory(category,
            if (category.getUsedFor().equalsIgnoreCase("SCOMP")) {
                items = daoFactory.getPurchaseItemDAO().findByCategory(category,
                                                           form.getStartIndex().intValue(),
                                                           form.getMaxItems().intValue(),
                                                           "asc");
            } else {
                items = daoFactory.getItemDAO().findByCategory(category,
                                                               form.getStartIndex().intValue(),
                                                               form.getMaxItems().intValue(),
                                                               "asc");
            }

            if ((items.size() < 20) && (form.getStartIndex().intValue() <= 0))
                form.setTotalNumItemsInResults(items.size());
            else
                form.setTotalNumItemsInResults(daoFactory.getCategoryDAO().getNumberOfItemsInCategory(category));
        }
        else if(form.getSearchType() == BrowseCatalogForm.KEYWORD_SEARCH_TYPE) {
            items = daoFactory.getItemDAO().findByKeywords(form.getQuery(),
                                                           form.getStartIndex().intValue(),
                                                           form.getMaxItems().intValue(),
                                                           "asc");
            Integer size = new Integer(daoFactory.getItemDAO().findByKeywords(form.getQuery()).size());
            form.setTotalNumItemsInResults(size);
        }
        form.setCurrentItems(items);
    }

    public void buildCurrentItemsUsingIds(Collection ids) throws InfrastructureException {
        Collection currentItems = daoFactory.getItemDAO().findItemsUsingIds(ids);
        form.setCurrentItems(currentItems);
    }
}
