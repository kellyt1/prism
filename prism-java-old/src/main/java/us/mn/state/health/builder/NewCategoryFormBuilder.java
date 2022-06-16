package us.mn.state.health.builder;
import java.util.ArrayList;

import us.mn.state.health.model.common.Category;
import us.mn.state.health.view.common.CategoryForm;

public class NewCategoryFormBuilder  {
    
    private Category parentCategory;
    
    private CategoryForm categoryForm;
    
    public NewCategoryFormBuilder(Category parentCategory,
            CategoryForm categoryForm) {
        this.parentCategory = parentCategory;
        this.categoryForm = categoryForm;
    }
    
    public void buildCategoryForm() {
        categoryForm.setParentCategory(parentCategory);
        categoryForm.setChildCategories(new ArrayList());
    }
}