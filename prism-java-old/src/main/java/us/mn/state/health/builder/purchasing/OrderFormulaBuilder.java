package us.mn.state.health.builder.purchasing;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.inventory.OrderFormula;
import us.mn.state.health.view.purchasing.OrderFormulaForm;

public class OrderFormulaBuilder {
    OrderFormulaForm orderFormulaForm;
    OrderFormula orderFormula;
    DAOFactory daoFactory;

    public OrderFormulaBuilder(OrderFormulaForm orderFormulaForm, OrderFormula orderFormula, DAOFactory daoFactory) {
        this.orderFormulaForm = orderFormulaForm;
        this.orderFormula = orderFormula;
        this.daoFactory = daoFactory;
    }

    public void buildSimpleProperties() throws InfrastructureException {
        Category category = daoFactory.getCategoryDAO().getCategoryById(new Long(orderFormulaForm.getCategoryId()),false );
        orderFormula.setCategory(category);
        orderFormula.setLeadTimeDays(new Integer(orderFormulaForm.getLeadTimeDays()));
        orderFormula.setOrderCost(new Double(orderFormulaForm.getOrderCost()));
    }
}
