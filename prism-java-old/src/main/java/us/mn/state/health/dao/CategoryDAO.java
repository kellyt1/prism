package us.mn.state.health.dao;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Category;

import java.util.Collection;

public interface CategoryDAO {
    public Collection findAll(boolean onlyRootCategories) throws InfrastructureException;
    public Collection findChildCategoriesByParentCode(String parentCode) throws InfrastructureException;
    public Collection findDescendantCategoriesByParentCode(String parentCode, boolean addThisToo) throws InfrastructureException;
    public Collection findCategoriesByUsedFor(String usedFor) throws InfrastructureException;
    public Collection findCategoriesByUsedForEdit(String usedFor) throws InfrastructureException;
    public Collection findByExample(Category category) throws InfrastructureException;
    public Integer getNumberOfItemsInCategory(Category category) throws InfrastructureException;
    public Category getCategoryById(Long categoryId, boolean lock) throws InfrastructureException;
    public Category findByCategoryCode(String categoryCode) throws InfrastructureException;
    public Category findByCategoryName(String categoryName) throws InfrastructureException;
    public void makePersistent(Category category) throws InfrastructureException;
    public void makeTransient(Category category) throws InfrastructureException;
}
