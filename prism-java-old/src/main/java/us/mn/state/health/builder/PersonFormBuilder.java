package us.mn.state.health.builder;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.view.common.PersonForm;

/**
 * Class that builds a PersonForm from a Person
 * @author $Author: ochial1 $
 */
public class PersonFormBuilder {

    /** PersonForm to be built */
    private PersonForm personForm;

    /** Person used to build PersonForm */
    private Person person;

    /**
     * PersonFormBuilder Constructor
     * @param personForm the PersonForm to be built
     * @param person the Person used to build PersonForm
     */
    public PersonFormBuilder(PersonForm personForm, Person person) {
        this.personForm = personForm;
        this.person = person;
    }

    /**
     * PersonFormBuilder Constructor
     * @param personForm the PersonForm to be built
     * @param person the Person used to build PersonForm
     * @param daoFactory factory for accessing DAO's used to build form elements
     */
    public PersonFormBuilder(PersonForm personForm, Person person, DAOFactory daoFactory) {
        this(personForm, person);
    }

    /** Build default PersonForm properties */
    public void buildDefaultProperties() {

    }

    /**
     * Build simple personForm properties PersonForm the Person
     * @throws us.mn.state.health.common.exceptions.InfrastrucureException
     */
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(personForm, person);
        }
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
}
