package us.mn.state.health.builder;

import java.util.Date;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.view.common.PersonForm;

/**
 * Class that builds a Person bean from a Person form
 * @author $Author: rodent1 $
 */
public class PersonBuilder {

    /** Person to be built */
    private Person person;

    /** PersonForm used to build Person */
    private PersonForm personForm;
    
    /** AddressBookExternalSourceForm used to build Person */
    //private AddressBookExternalSourceForm addBkExtSrForm;
    
    /** AddressBookPersonForm used to build Person */
    //private AddressBookPersonForm addBkPersonFrm;

    /** Current Application Session username */
    private String username; //This is not the new Person's username, but the current Application User


    /**
     * PersonBuilder Constructor
     * @param person Person to be built
     * @param personForm PersonForm from which Person is built
     * @param username String value for auditing purposes
     */
    public PersonBuilder(Person person, 
                         PersonForm personForm,
                         String username) {
        this(person, personForm);
        this.username = username;
    }
    
    /**
     * PersonBuilder Constructor
     * @param person Person to be built
     * @param personForm PersonForm from which Person is built
     */
    public PersonBuilder(Person person, PersonForm personForm) {
        this.person = person;
        this.personForm = personForm;
    }

    /**
     * PersonBuilder Constructor
     * @param person Person to be built
     * @param personForm PersonForm from which Person is built
     * @param daoFactory obtains DAO's used in building Person
     */
    public PersonBuilder(Person person, PersonForm personForm, DAOFactory daoFactory) {
        this(person, personForm);
    }
    
    /**
     * PersonBuilder Constructor
     * @param person Person to be built
     * @param personForm PersonForm from which Person is built
     * @param daoFactory obtains DAO's used in building Person
     */
    public PersonBuilder(Person person, PersonForm personForm, DAOFactory daoFactory, String username) {
        this(person, personForm);
        this.username = username;
    }

    /** Build default Person properties */
    public void buildDefaultProperties() {
        if(person.getPersonId() == null) {
            if(username != null && !("".equals(username))) {
                person.setInsertedBy(username);
            }
            else {
                person.setInsertedBy("prism");
            }
            person.setInsertionDate(new Date());
//            person.setPersonType(4l);   //set to foriegn key value of  "PRISM Private Citizen" from PERSON_TYPE table
            person.setPersonType("PRISM Private Citizen");   //set to foriegn key value of  "PRISM Private Citizen" from PERSON_TYPE table
        }
    }
    
    /* Builds Person's primary postion 
    public void buildPrimaryFacility() throws InfrastructureException {
        Facility facility = addBkPersonFrm.getFacility();
        Position position = person.getPrimaryPosition();
        PositionFacility.create(position, facility, username);
    }
    */
    
    /* Build Primary MailingAddress 
    public void buildPrimaryMailingAddress() throws InfrastructureException {
        MailingAddressBuilder mlngAddrBuilder = 
                new MailingAddressBuilder(person.getPrimaryMailingAddress(), addBkExtSrForm);
        mlngAddrBuilder.buildSimpleProperties(); 
    }
    */
    
    /* Build and add new MailingAddress 
    public void buildNewMailingAddress() throws InfrastructureException {
        MailingAddress mlngAddr = new MailingAddress();
        MailingAddressBuilder mlngAddrBuilder =  new MailingAddressBuilder(mlngAddr, addBkExtSrForm);
        mlngAddrBuilder.buildSimpleProperties();
        PersonMailingAddress.create(person, mlngAddr, username);
    }
    */

    /**
     * Build simple Person properties from the PersonForm
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            if(personForm != null) {
                PropertyUtils.copyProperties(person, personForm);
            }
            /*
            else {
                if(addBkExtSrForm != null) {
                    PropertyUtils.copyProperties(person, addBkExtSrForm);
                }
            }
            */
        }
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
}
