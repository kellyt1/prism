package us.mn.state.health.view.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * This purpose of this Form is to back the AddNewDeliveryAddress use case.  
 * That use case is a 3-step process.  
 */
public class DeliveryAddressForm extends ValidatorForm {
    private PersonForm personForm = new PersonForm();    
    private MailingAddressForm mailingAddressForm = new MailingAddressForm();    
    private ExternalOrganizationForm extOrgForm = new ExternalOrganizationForm();
    private Collection extOrgs = new HashSet();
    /** the set of persons associated with selected ext org */
    private Collection contactPersons = new HashSet();     
    private String[] selectedContactPersonIds;
    /** the set of persons not associated with selected ext org */
    private Collection nonContactPersons = new HashSet();   
    private String[] selectedNonContactPersonIds;
    /** the set of existing MailingAddress objects associated with the selected ext org */
    private Collection mailingAddresses = new ArrayList();  
    private String selectedExtOrgId;
     /** the set of persons that represent private citizens */
    private Collection privateCitizenPersons = new HashSet();   
    private String selectedPrivateCitizenPersonId;
    
    private String module;          //the part of the system the user is in, to determine which sub-menu to display
    private String forward;         //to indicate where to forward the user, if different from the default config.
    private boolean reset = false;
    private String newOrOld = null;  //default to 'old', which signifies using an existing org, when creating new address info
    private String cmd;
    /**the following static properties define valid values for the newOrOld property */
    public final static String NEW_ORG = "new";               //signifies that the user wishes to create a new org.
    public final static String OLD_ORG = "old";               //signifies that the user wishes to work with an existing org
    public final static String PRIVATE_CITIZEN = "neither"; //signifies that user wishes to work with a private citizen; a person instead of an org

    
    private static Log log = LogFactory.getLog(DeliveryAddressForm.class);

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if(reset) {
            personForm.reset(mapping, request);
            mailingAddressForm.reset(mapping, request);
            extOrgForm.reset(mapping, request);
            selectedExtOrgId = "";
            reset = false;
            module = "";
            forward = "";
            selectedPrivateCitizenPersonId = "";
            mailingAddresses.clear();
            selectedContactPersonIds = null;
            selectedNonContactPersonIds = null;
            newOrOld = null;
        }
        cmd = null;
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping,request);
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public void setPersonForm(PersonForm personForm) {
        this.personForm = personForm;
    }


    public PersonForm getPersonForm() {
        return personForm;
    }


    public void setMailingAddressForm(MailingAddressForm mailingAddressForm) {
        this.mailingAddressForm = mailingAddressForm;
    }


    public MailingAddressForm getMailingAddressForm() {
        return mailingAddressForm;
    }


    public void setExtOrgForm(ExternalOrganizationForm extOrgForm) {
        this.extOrgForm = extOrgForm;
    }


    public ExternalOrganizationForm getExtOrgForm() {
        return extOrgForm;
    }


    public void setMailingAddresses(Collection mailingAddresses) {
        this.mailingAddresses = mailingAddresses;
    }


    public Collection getMailingAddresses() {
        return mailingAddresses;
    }


    public void setExtOrgs(Collection extOrgs) {
        this.extOrgs = extOrgs;
    }


    public Collection getExtOrgs() {
        return extOrgs;
    }


    public void setSelectedExtOrgId(String selectedExtOrgId) {
        this.selectedExtOrgId = selectedExtOrgId;
    }


    public String getSelectedExtOrgId() {
        return selectedExtOrgId;
    }


    public void setContactPersons(Collection contactPersons) {
        this.contactPersons = contactPersons;
    }


    public Collection getContactPersons() {
        return contactPersons;
    }


    public void setNonContactPersons(Collection nonContactPersons) {
        this.nonContactPersons = nonContactPersons;
    }


    public Collection getNonContactPersons() {
        return nonContactPersons;
    }


    public void setSelectedContactPersonIds(String[] selectedContactPersonIds) {
        this.selectedContactPersonIds = selectedContactPersonIds;
    }


    public String[] getSelectedContactPersonIds() {
        return selectedContactPersonIds;
    }


    public void setSelectedNonContactPersonIds(String[] selectedNonContactPersonIds) {
        this.selectedNonContactPersonIds = selectedNonContactPersonIds;
    }


    public String[] getSelectedNonContactPersonIds() {
        return selectedNonContactPersonIds;
    }


    public void setReset(boolean reset) {
        this.reset = reset;
    }


    public boolean isReset() {
        return reset;
    }


    public void setModule(String module) {
        this.module = module;
    }


    public String getModule() {
        return module;
    }


    public void setForward(String forward) {
        this.forward = forward;
    }


    public String getForward() {
        return forward;
    }


    public void setNewOrOld(String newOrOld) {
        this.newOrOld = newOrOld;
    }


    public String getNewOrOld() {
        return newOrOld;
    }


    public void setPrivateCitizenPersons(Collection privateCitizenPersons) {
        this.privateCitizenPersons = privateCitizenPersons;
    }


    public Collection getPrivateCitizenPersons() {
        return privateCitizenPersons;
    }


    public void setSelectedPrivateCitizenPersonId(String selectedPrivateCitizenPersonId) {
        this.selectedPrivateCitizenPersonId = selectedPrivateCitizenPersonId;
    }


    public String getSelectedPrivateCitizenPersonId() {
        return selectedPrivateCitizenPersonId;
    }

}