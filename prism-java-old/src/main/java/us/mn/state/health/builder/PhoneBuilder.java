package us.mn.state.health.builder;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Phone;
import us.mn.state.health.view.common.PhoneForm;

public class PhoneBuilder {

    private Phone phone;

    private PhoneForm phoneForm;

    public PhoneBuilder(Phone phone, PhoneForm phoneForm) {
        this.phone = phone;
        this.phoneForm = phoneForm;
    }

    public PhoneBuilder(DAOFactory daoFactory, Phone phone, PhoneForm phoneForm) {
        this.phone = phone;
        this.phoneForm = phoneForm;
    }

    public void buildDefaultProperties() {
        phone.setLocation(Phone.DEFAULT_LOCATION);
        phone.setMdhOwned(Phone.DEFAULT_MDHOWNED);
    }

    public void buildSimpleProperties() throws InfrastructureException {
        try {
            if (phoneForm != null) {
                PropertyUtils.copyProperties(phone, phoneForm);
            }
        } catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }


}
