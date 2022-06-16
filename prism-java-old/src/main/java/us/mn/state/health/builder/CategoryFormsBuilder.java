package us.mn.state.health.builder;
import java.util.Collection;
import java.util.Iterator;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.view.common.CategoryForm;

public class CategoryFormsBuilder  {
    private Collection categoryForms;
    private Collection categories;
    
    public CategoryFormsBuilder(Collection categoryForms,
        Collection categories) {
        
        this.categoryForms = categoryForms;
        this.categories = categories;
    }
    
    public void buildCategoryForms() throws InfrastructureException {
        try {
            for(Iterator i = categories.iterator(); i.hasNext();) {
                Category category = (Category)i.next();
                CategoryForm categoryForm = new CategoryForm();
                PropertyUtils.copyProperties(categoryForm, category);
                categoryForms.add(categoryForm);
            }
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building Category Forms: ", rpe);
        }
    }
}