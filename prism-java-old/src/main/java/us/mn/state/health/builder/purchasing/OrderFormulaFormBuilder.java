package us.mn.state.health.builder.purchasing;

import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.inventory.OrderFormula;
import us.mn.state.health.view.purchasing.OrderFormulaForm;

public class OrderFormulaFormBuilder {
    private OrderFormulaForm orderFormulaForm;
    private OrderFormula orderFormula;

    public OrderFormulaFormBuilder(DAOFactory daoFactory, OrderFormulaForm orderFormulaForm, OrderFormula orderFormula) {
        this.orderFormulaForm = orderFormulaForm;
        this.orderFormula = orderFormula;
    }

    public void buildSimpleProperties() {
        if (orderFormula != null) {
            orderFormulaForm.setLeadTimeDays(orderFormula.getLeadTimeDays().toString());
            orderFormulaForm.setOrderCost(orderFormula.getOrderCost().toString());
        }
        else {
            orderFormulaForm.setLeadTimeDays(null);
            orderFormulaForm.setOrderCost(null);
            return;
        }
    }

    public OrderFormulaForm getOrderFormulaForm() {
        return orderFormulaForm;
    }

    public void setOrderFormulaForm(OrderFormulaForm orderFormulaForm) {
        this.orderFormulaForm = orderFormulaForm;
    }

    public OrderFormula getOrderFormula() {
        return orderFormula;
    }

    public void setOrderFormula(OrderFormula orderFormula) {
        this.orderFormula = orderFormula;
    }
}
