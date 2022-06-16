package us.mn.state.health.builder.email;

import us.mn.state.health.common.exceptions.InfrastructureException;

public interface EmailBuilder {

    String systemEmailAddress = "prism@state.mn.us";

    void buildSubject();

    void buildMessage() throws InfrastructureException;

    void buildTo() throws InfrastructureException;

    void buildFrom() throws InfrastructureException;

    void buildCc();
//    void buildCc() throws InfrastructureException;

    void buildBcc();
}
