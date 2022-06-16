package us.mn.state.health.builder;
import java.util.Collection;
import java.util.Iterator;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.view.common.PersonForm;

public class PersonFormsBuilder  {

    private Collection personForms;
    private Collection persons;
    
    public PersonFormsBuilder(Collection personForms, Collection persons) {
        this.personForms = personForms;
        this.persons = persons;
    }
    
    public void buildPersonForms() throws InfrastructureException {
        try {
            for(Iterator i = persons.iterator(); i.hasNext();) {
                Person person = (Person)i.next();
                PersonForm personForm = new PersonForm();
                PropertyUtils.copyProperties(personForm, person);
                personForms.add(personForm);
            }
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building Person Forms: ", rpe);
        }
    }
}