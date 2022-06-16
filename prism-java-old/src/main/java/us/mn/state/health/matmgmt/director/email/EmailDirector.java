package us.mn.state.health.matmgmt.director.email;

import us.mn.state.health.builder.email.EmailBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class EmailDirector {
    EmailBuilder builder;

    public EmailDirector(EmailBuilder builder) {
        this.builder = builder;
    }

    public void construct() throws InfrastructureException {
        builder.buildFrom();
        builder.buildTo();
        builder.buildCc();
        builder.buildBcc();
        builder.buildSubject();
        builder.buildMessage();
    }
    
}
