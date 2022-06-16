package us.mn.state.health.builder.purchasing;

import java.util.Collection;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.view.purchasing.OrderFormulaMaintainanceForm;

public class OrderFormulaMaintainanceFormBuilder {
    private DAOFactory daoFactory;
    private OrderFormulaMaintainanceForm orderFormulaMaintainanceForm;

    public OrderFormulaMaintainanceFormBuilder(DAOFactory daoFactory
                                               , OrderFormulaMaintainanceForm orderFormulaMaintainanceForm) {
        this.daoFactory = daoFactory;
        this.orderFormulaMaintainanceForm = orderFormulaMaintainanceForm;
    }

    public void buildCategories() throws InfrastructureException {
        Collection categories = daoFactory.getCategoryDAO()
                .findDescendantCategoriesByParentCode(Category.MATERIALS,false);
        orderFormulaMaintainanceForm.setCategories(categories);
    }

}
