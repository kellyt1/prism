package us.mn.state.health.builder;

import java.util.Date;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.User;
import us.mn.state.health.view.common.MailingAddressForm;

/**
 * Class that builds a MailingAddress bean from a MailingAddress form
 * @author Shawn Flahave, Lucian Ochian, Jason Stull
 */
public class MailingAddressBuilder {

    /** MailingAddress to be built */
    private MailingAddress mailingAddress;

    /** MailingAddressForm used to build MailingAddress */
    private MailingAddressForm mailingAddressForm;

    private User user;


    /**
     * MailingAddressBuilder Constructor
     * @param mailingAddress MailingAddress to be built
     * @param mailingAddressForm MailingAddressForm from which MailingAddress is built
     */
    public MailingAddressBuilder(MailingAddress mailingAddress, MailingAddressForm mailingAddressForm) {
        this.mailingAddress = mailingAddress;
        this.mailingAddressForm = mailingAddressForm;
    }


    public MailingAddressBuilder(MailingAddress mailingAddress,
                                 MailingAddressForm mailingAddressForm, 
                                 User user) {
        this(mailingAddress, mailingAddressForm);
        this.user = user;
    }

    /** Build default MailingAddress properties */
    public void buildDefaultProperties() {
        if(mailingAddress.getMailingAddressId() == null) {
            mailingAddress.setInsertionDate(new Date());
            if(user != null) {
                mailingAddress.setInsertedBy(user.getUsername());
            }
            else {
                mailingAddress.setInsertedBy("prism");
            }
        }
    }

    /**
     * Build simple MailingAddress properties from a form
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            if(mailingAddressForm != null) {
                PropertyUtils.copyProperties(mailingAddress, mailingAddressForm);
            }
        }
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
}
