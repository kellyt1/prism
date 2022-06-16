package us.mn.state.health.matmgmt.director.email;

import us.mn.state.health.builder.email.EmailBuilder;
import us.mn.state.health.builder.email.EmailBuilderBook;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class EmailDirectorBook{
    EmailBuilderBook builder;

    public EmailDirectorBook(EmailBuilderBook builder) {
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

    public void construct(String groupCode) throws InfrastructureException {
        builder.buildFrom();
        builder.buildTo(groupCode);
//        builder.buildTo("todd.roden@state.mn.us, joseph.pugh@state.mn.us");
        builder.buildCc();
        builder.buildBcc();
        builder.buildSubject();
        builder.buildMessage(groupCode);
    }

}
