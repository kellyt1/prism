package us.mn.state.health.view.common;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class ExternalOrgDetailForm extends ValidatorForm {    
    private String orgEndDate;
    private String orgEffectiveDate;
    private String orgDescription;
    private String comments;
    private String webAddress;
    private String orgShortName;
    private String orgName;
    private String terminationDate;
    private String terminatedBy;
    private String insertionDate;
    private String insertedBy;
    private String type;
    private Long orgId;
    private String orgCode;
    private PersonForm personForm = new PersonForm();     //we use this property as the primary representative

    private EmailAddressForm emailAddressForm = new EmailAddressForm(); //primary email addres
    private PhoneForm phoneForm = new PhoneForm(); //primary phone
    private PhoneForm faxForm = new PhoneForm(); //primary fax
    private Set mailingAddresses = new HashSet(); //Collection of MailingAddressForm objects

    public Set getMailingAddresses() {
        return mailingAddresses;
    }

    public void setMailingAddresses(Set mailingAddresses) {
        this.mailingAddresses = mailingAddresses;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }


    public EmailAddressForm getEmailAddressForm() {
        return emailAddressForm;
    }

    public void setEmailAddressForm(EmailAddressForm emailAddressForm) {
        this.emailAddressForm = emailAddressForm;
    }

    public PhoneForm getPhoneForm() {
        return phoneForm;
    }

    public void setPhoneForm(PhoneForm phoneForm) {
        this.phoneForm = phoneForm;
    }

    public void setOrgEndDate(String orgEndDate) {
        this.orgEndDate = orgEndDate;
    }


    public String getOrgEndDate() {
        return orgEndDate;
    }


    public void setOrgEffectiveDate(String orgEffectiveDate) {
        this.orgEffectiveDate = orgEffectiveDate;
    }


    public String getOrgEffectiveDate() {
        return orgEffectiveDate;
    }


    public void setOrgDescription(String orgDescription) {
        this.orgDescription = orgDescription;
    }


    public String getOrgDescription() {
        return orgDescription;
    }


    public void setComments(String comments) {
        this.comments = comments;
    }


    public String getComments() {
        return comments;
    }


    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }


    public String getWebAddress() {
        return webAddress;
    }


    public void setOrgShortName(String orgShortName) {
        this.orgShortName = orgShortName;
    }


    public String getOrgShortName() {
        return orgShortName;
    }


    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }


    public String getOrgName() {
        return orgName;
    }


    public void setTerminationDate(String terminationDate) {
        this.terminationDate = terminationDate;
    }


    public String getTerminationDate() {
        return terminationDate;
    }


    public void setTerminatedBy(String terminatedBy) {
        this.terminatedBy = terminatedBy;
    }


    public String getTerminatedBy() {
        return terminatedBy;
    }


    public void setInsertionDate(String insertionDate) {
        this.insertionDate = insertionDate;
    }


    public String getInsertionDate() {
        return insertionDate;
    }


    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }


    public String getInsertedBy() {
        return insertedBy;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getType() {
        return type;
    }


    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }


    public Long getOrgId() {
        return orgId;
    }


    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }


    public String getOrgCode() {
        return orgCode;
    }


    public void setPersonForm(PersonForm personForm) {
        this.personForm = personForm;
    }


    public PersonForm getPersonForm() {
        return personForm;
    }

    public PhoneForm getFaxForm() {
        return faxForm;
    }

    public void setFaxForm(PhoneForm faxForm) {
        this.faxForm = faxForm;
    }

}