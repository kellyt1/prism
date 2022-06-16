package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.purchasing.OrderFormulaMaintainanceFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class OrderFormulaMaintainanceFormDirector {
    OrderFormulaMaintainanceFormBuilder builder;

    public OrderFormulaMaintainanceFormDirector(OrderFormulaMaintainanceFormBuilder builder) {
        this.builder = builder;
    }

    public void construct() throws InfrastructureException {
        builder.buildCategories();
    }
}
