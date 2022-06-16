package us.mn.state.health.view.purchasing;

import java.util.Collection;

import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.model.common.Category;

public class OrderFormulaMaintainanceForm extends ValidatorForm{
    private Collection categories; //this is the list with categories
    private OrderFormulaForm orderFormulaForm;
//    private String categoryId;
    private Category category;
    private String cmd;

    public Collection getCategories() {
        return categories;
    }

    public void setCategories(Collection categories) {
        this.categories = categories;
    }

    public OrderFormulaForm getOrderFormulaForm() {
        return orderFormulaForm;
    }

    public void setOrderFormulaForm(OrderFormulaForm orderFormulaForm) {
        this.orderFormulaForm = orderFormulaForm;
    }

//    public String getCategoryId() {
//        return categoryId;
//    }
//
//    public void setCategoryId(String categoryId) {
//        this.categoryId = categoryId;
//    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
