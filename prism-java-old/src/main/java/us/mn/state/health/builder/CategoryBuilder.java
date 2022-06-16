package us.mn.state.health.builder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.view.common.CategoryForm;

public class CategoryBuilder  {

    private Category category;
    private CategoryForm categoryForm;

    public CategoryBuilder(Category category, CategoryForm categoryForm) {
        this.category = category;
        this.categoryForm = categoryForm;
    }
    
    public void buildCategory() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(category, categoryForm);
            if(category.getParentCategory() == null) {
                category.setParentCategory(categoryForm.getParentCategory());
            }
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
}