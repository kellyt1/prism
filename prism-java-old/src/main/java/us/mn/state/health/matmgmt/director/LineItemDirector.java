package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.LineItemBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class LineItemDirector  {
    LineItemBuilder builder;
    
    public LineItemDirector(LineItemBuilder builder) {
        this.builder = builder;
    }
    
    public void construct() throws InfrastructureException {
        builder.buildSimpleProperties();
    }
}