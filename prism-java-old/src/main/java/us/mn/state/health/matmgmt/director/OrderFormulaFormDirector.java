package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.purchasing.OrderFormulaFormBuilder;

public class OrderFormulaFormDirector {
    OrderFormulaFormBuilder builder;

    public OrderFormulaFormDirector(OrderFormulaFormBuilder builder) {
        this.builder = builder;
    }

    public void construct(){
        builder.buildSimpleProperties();
    }
}
