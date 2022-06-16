package us.mn.state.health.builder;

import us.mn.state.health.common.exceptions.InfrastructureException;

public abstract class LineItemBuilder  {
    public abstract void buildSimpleProperties() throws InfrastructureException;
}