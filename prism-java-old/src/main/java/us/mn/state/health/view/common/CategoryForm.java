package us.mn.state.health.view.common;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.model.common.Category;

public class CategoryForm extends ValidatorForm implements Comparable {
    
    private String categoryCode;
    private String name;
    private String categoryId;
    private Category parentCategory;
    private Collection childCategories = new ArrayList();

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        categoryCode = null;
        name = null;
        categoryId = null;
    }
    
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        return null;
    }
    
    public int compareTo(Object o) {
        if(o instanceof CategoryForm) {
            return this.getName().compareTo(((CategoryForm)o).getName());
        }
        return 0;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }


    public String getCategoryCode() {
        return categoryCode;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


    public String getCategoryId() {
        return categoryId;
    }


    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }


    public Category getParentCategory() {
        return parentCategory;
    }


    public void setChildCategories(Collection childCategories) {
        this.childCategories = childCategories;
    }


    public Collection getChildCategories() {
        return childCategories;
    }
    
}