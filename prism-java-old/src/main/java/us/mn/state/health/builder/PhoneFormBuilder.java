package us.mn.state.health.builder;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.common.Phone;
import us.mn.state.health.view.common.PhoneForm;

public class PhoneFormBuilder {
    Phone phone;
    PhoneForm phoneForm;

    public PhoneFormBuilder(Phone phone, PhoneForm phoneForm) {
        this.phone = phone;
        this.phoneForm = phoneForm;
    }

    public void buildDefaultProperties() {

    }

    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(phoneForm,phone);
        } catch (ReflectivePropertyException e) {
            throw new InfrastructureException(e);
        }
    }
}
