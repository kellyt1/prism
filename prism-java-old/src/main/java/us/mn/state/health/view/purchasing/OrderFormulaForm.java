package us.mn.state.health.view.purchasing;
import us.mn.state.health.common.lang.StringUtils;

public class OrderFormulaForm {
    private String orderFormulaId;
    private String orderCost;
    private String leadTimeDays;
    private String categoryId;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getOrderFormulaId() {
        return orderFormulaId;
    }

    public void setOrderFormulaId(String orderFormulaId) {
        this.orderFormulaId = orderFormulaId;
    }

    public String getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(String orderCost) {
        this.orderCost = StringUtils.trim(orderCost);
    }

    public String getLeadTimeDays() {
        return leadTimeDays;
    }

    public void setLeadTimeDays(String leadTimeDays) {
        this.leadTimeDays = StringUtils.trim(leadTimeDays);
    }
}
