package us.mn.state.health.builder;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.view.common.MailingAddressForm;

/**
 * Class that builds a MailingAddressForm from a MailingAddress
 * @author Shawn Flahave, Lucian Ochian, Jason Stull
 */
public class MailingAddressFormBuilder {

    /** MailingAddressForm to be built */
    private MailingAddressForm mailingAddressForm;

    /** MailingAddress used to build MailingAddressForm */
    private MailingAddress mailingAddress;

    /**
     * MailingAddressFormBuilder Constructor
     * @param mailingAddressForm the MailingAddressForm to be built
     * @param mailingAddress the MailingAddress used to build MailingAddressForm
     */
    public MailingAddressFormBuilder(MailingAddressForm mailingAddressForm, MailingAddress mailingAddress) {
        this.mailingAddressForm = mailingAddressForm;
        this.mailingAddress = mailingAddress;
    }

    /**
     * MailingAddressFormBuilder Constructor
     * @param mailingAddressForm the MailingAddressForm to be built
     * @param mailingAddress the MailingAddress used to build MailingAddressForm
     * @param daoFactory factory for accessing DAO's used to build form elements
     */
    public MailingAddressFormBuilder(MailingAddressForm mailingAddressForm, MailingAddress mailingAddress,
    DAOFactory daoFactory) {
        this(mailingAddressForm, mailingAddress);
    }

    /** Build default MailingAddressForm properties */
    public void buildDefaultProperties() {

    }

    /**
     * Build simple mailingAddressForm properties MailingAddressForm the MailingAddress
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(mailingAddressForm, mailingAddress);
        }
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
}
