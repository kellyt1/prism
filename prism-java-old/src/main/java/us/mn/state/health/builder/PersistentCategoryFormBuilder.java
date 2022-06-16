package us.mn.state.health.builder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.view.common.CategoryForm;

public class PersistentCategoryFormBuilder  {

    private Category category;
    
    private CategoryForm categoryForm;
    
    public PersistentCategoryFormBuilder(Category category, CategoryForm categoryForm) {
        this.category = category;
        this.categoryForm = categoryForm;
    }
    
    public void buildCategoryForm() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(categoryForm, category);
            categoryForm.setChildCategories(category.getChildCategories());
            categoryForm.setParentCategory(category.getParentCategory());
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building Category Form: ", rpe);
        }
    }
}