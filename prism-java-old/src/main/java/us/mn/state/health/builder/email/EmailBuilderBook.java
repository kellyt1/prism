package us.mn.state.health.builder.email;

import us.mn.state.health.common.exceptions.InfrastructureException;

/**
 * Created by IntelliJ IDEA.                                          
 * User: RodenT1a
 * Date: Nov 19, 2008
 * Time: 11:32:09 AM
 * To change this template use File | Settings | File Templates.
 */
public interface EmailBuilderBook extends EmailBuilder{
    void buildMessage(String groupCode) throws InfrastructureException;

    void buildTo(String groupCode) throws InfrastructureException;

}
