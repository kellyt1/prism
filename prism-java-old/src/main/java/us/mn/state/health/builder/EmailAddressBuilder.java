package us.mn.state.health.builder;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.common.EmailAddress;
import us.mn.state.health.view.common.EmailAddressForm;

public class EmailAddressBuilder {
    /**
     * EmailAddress to be built
     */
    private EmailAddress emailAddress;

    /**
     * EmailAddressForm used to build EmailAddress
     */
    private EmailAddressForm emailAddressForm;


    public EmailAddressBuilder(EmailAddress emailAddress, EmailAddressForm emailAddressForm) {
        this.emailAddress = emailAddress;
        this.emailAddressForm = emailAddressForm;
    }

    /**
     * Build default EmailAddress properties
     */
    public void buildDefaultProperties() {

    }

    /**
     * Build simple MailingAddress properties from a form
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            if (emailAddressForm != null) {
                PropertyUtils.copyProperties(emailAddress, emailAddressForm);
            }
        } catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
}
