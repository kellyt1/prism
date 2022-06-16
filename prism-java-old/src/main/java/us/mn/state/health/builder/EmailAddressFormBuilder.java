package us.mn.state.health.builder;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.common.EmailAddress;
import us.mn.state.health.view.common.EmailAddressForm;

public class EmailAddressFormBuilder {

    private EmailAddress emailAddress;
    private EmailAddressForm emailAddressForm;

    public EmailAddressFormBuilder(EmailAddress emailAddress, EmailAddressForm emailAddressForm) {
        this.emailAddress = emailAddress;
        this.emailAddressForm = emailAddressForm;
    }

    public void buildDefaultProperties(){

    }

    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(emailAddressForm,emailAddress);
        } catch (ReflectivePropertyException e) {
            throw new InfrastructureException(e);
        }
    }
}
