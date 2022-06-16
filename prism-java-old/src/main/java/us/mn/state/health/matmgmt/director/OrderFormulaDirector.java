package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.purchasing.OrderFormulaBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class OrderFormulaDirector {
    OrderFormulaBuilder builder;

    public OrderFormulaDirector(OrderFormulaBuilder builder) {
        this.builder = builder;
    }

    public void construct() throws InfrastructureException {
        builder.buildSimpleProperties();
    }
}
