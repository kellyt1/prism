package us.mn.state.health.view.common;

import java.util.Collection;

import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.model.common.Facility;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.User;

public class AddressBookPersonForm extends ValidatorForm {

    //Personal Address Book fields (Optional)
    private User owner;
    private String personalAddBkPersonId;
    
    //Address Book Person fields
    private String addressBookPersonId;
    private Person person;
    private String facilityId;
    private Collection facilities;
    private String notes;

    public void setPerson(Person person) {
        this.person = person;
    }


    public Person getPerson() {
        return person;
    }

    public void setAddressBookPersonId(String addressBookPersonId) {
        this.addressBookPersonId = addressBookPersonId;
    }


    public String getAddressBookPersonId() {
        return addressBookPersonId;
    }


    public void setOwner(User owner) {
        this.owner = owner;
    }


    public User getOwner() {
        return owner;
    }


    public void setPersonalAddBkPersonId(String personalAddBkPersonId) {
        this.personalAddBkPersonId = personalAddBkPersonId;
    }


    public String getPersonalAddBkPersonId() {
        return personalAddBkPersonId;
    }


    public void setNotes(String notes) {
        this.notes = notes;
    }


    public String getNotes() {
        return notes;
    }

    public Facility getFacility() {
        Facility facility = (Facility)CollectionUtils.
                                    getObjectFromCollectionById(facilities, 
                                                                facilityId, 
                                                                "facilityId");
        return facility;
    }


    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }


    public String getFacilityId() {
        return facilityId;
    }


    public void setFacilities(Collection facilities) {
        this.facilities = facilities;
    }


    public Collection getFacilities() {
        return facilities;
    }
    
}