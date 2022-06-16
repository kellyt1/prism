package us.mn.state.health.domain.repository.common;

import java.util.Collection;
import java.util.List;

import us.mn.state.health.model.common.Category;

public interface CategoryRepository {
    public List findAll(boolean onlyRootCategories);

    public Collection findChildCategoriesByParentCode(String parentCode);

    public Collection findDescendantCategoriesByParentCode(String parentCode, boolean addThisToo);

    //    public Collection findByExample(Category category) ;
    public Integer getNumberOfItemsInCategory(Category category);

    public Category getCategoryById(Long categoryId);

    public Category findByCategoryCode(String categoryCode);

    public void makePersistent(Category category);

    Category loadCategoryById(long categoryId);
}
