package us.mn.state.health.view.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.model.common.User;

public class PersonalAddressBookForm extends ValidatorForm {

    private String personalAddBkId;
    private User owner;
    
    //AddressBokExtSources properties
    private String personalAddBkExtSourceId;
    private String availableAddbkExtSourceId;
    private Collection addBkExtSources = new ArrayList();
    private Collection availableAddBkExtSources = new ArrayList();
    private AddressBookExternalSourceForm addbkExtSourceForm;

    //AddressBookMailAddress Properties
    private String persAddBkMailAddressId;
    private String availableAddBkMailAddressId;
    private Collection addBkMailAddresses = new TreeSet();
    private Collection availableAddBkMailAddresses = new TreeSet();
    private AddressBookMailingAddressForm addBkMailAddrForm;

    //AddressBookPerson Properties
    private String persAddBkPersonId;
    private String availableAddBkPersonId;
    private Collection availableAddBkPersons = new TreeSet();
    private Collection addBkPersons = new TreeSet();
    private AddressBookPersonForm addBookPersonForm;
    
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
    }
    
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        
        return super.validate(mapping, request);
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }


    public User getOwner() {
        return owner;
    }


    public void setAddBkMailAddresses(Collection addBkMailAddresses) {
        this.addBkMailAddresses = addBkMailAddresses;
    }


    public Collection getAddBkMailAddresses() {
        return addBkMailAddresses;
    }


    public void setAddBkPersons(Collection addBkPersons) {
        this.addBkPersons = addBkPersons;
    }


    public Collection getAddBkPersons() {
        return addBkPersons;
    }


    public void setAddBkMailAddrForm(AddressBookMailingAddressForm addBkMailAddrForm) {
        this.addBkMailAddrForm = addBkMailAddrForm;
    }


    public AddressBookMailingAddressForm getAddBkMailAddrForm() {
        return addBkMailAddrForm;
    }


    public void setAvailableAddBkPersonId(String availableAddBkPersonId) {
        this.availableAddBkPersonId = availableAddBkPersonId;
    }


    public String getAvailableAddBkPersonId() {
        return availableAddBkPersonId;
    }


    public void setAvailableAddBkPersons(Collection availableAddBkPersons) {
        this.availableAddBkPersons = availableAddBkPersons;
    }


    public Collection getAvailableAddBkPersons() {
        return availableAddBkPersons;
    }


    public void setAddBookPersonForm(AddressBookPersonForm addBookPersonForm) {
        this.addBookPersonForm = addBookPersonForm;
    }


    public AddressBookPersonForm getAddBookPersonForm() {
        return addBookPersonForm;
    }


    public void setAvailableAddBkMailAddressId(String availableAddBkMailAddressId) {
        this.availableAddBkMailAddressId = availableAddBkMailAddressId;
    }


    public String getAvailableAddBkMailAddressId() {
        return availableAddBkMailAddressId;
    }


    public void setAvailableAddBkMailAddresses(Collection availableAddBkMailAddresses) {
        this.availableAddBkMailAddresses = availableAddBkMailAddresses;
    }


    public Collection getAvailableAddBkMailAddresses() {
        return availableAddBkMailAddresses;
    }


    public void setPersonalAddBkId(String personalAddBkId) {
        this.personalAddBkId = personalAddBkId;
    }


    public String getPersonalAddBkId() {
        return personalAddBkId;
    }


    public void setPersAddBkMailAddressId(String persAddBkMailAddressId) {
        this.persAddBkMailAddressId = persAddBkMailAddressId;
    }


    public String getPersAddBkMailAddressId() {
        return persAddBkMailAddressId;
    }


    public void setPersAddBkPersonId(String persAddBkPersonId) {
        this.persAddBkPersonId = persAddBkPersonId;
    }


    public String getPersAddBkPersonId() {
        return persAddBkPersonId;
    }


    public void setAddBkExtSources(Collection addBkExtSources) {
        this.addBkExtSources = addBkExtSources;
    }


    public Collection getAddBkExtSources() {
        return addBkExtSources;
    }


    public void setAvailableAddBkExtSources(Collection availableAddBkExtSources) {
        this.availableAddBkExtSources = availableAddBkExtSources;
    }


    public Collection getAvailableAddBkExtSources() {
        return availableAddBkExtSources;
    }


    public void setPersonalAddBkExtSourceId(String personalAddBkExtSourceId) {
        this.personalAddBkExtSourceId = personalAddBkExtSourceId;
    }


    public String getPersonalAddBkExtSourceId() {
        return personalAddBkExtSourceId;
    }


    public void setAvailableAddbkExtSourceId(String availableAddbkExtSourceId) {
        this.availableAddbkExtSourceId = availableAddbkExtSourceId;
    }


    public String getAvailableAddbkExtSourceId() {
        return availableAddbkExtSourceId;
    }


    public void setAddbkExtSourceForm(AddressBookExternalSourceForm addbkExtSourceForm) {
        this.addbkExtSourceForm = addbkExtSourceForm;
    }


    public AddressBookExternalSourceForm getAddbkExtSourceForm() {
        return addbkExtSourceForm;
    }

}