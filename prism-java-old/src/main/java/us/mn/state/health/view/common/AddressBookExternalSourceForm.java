package us.mn.state.health.view.common;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class AddressBookExternalSourceForm extends ValidatorForm {
    private Long personalAddBkExtSourceId;
    private Long addressBookExternalSourceId;
    
    //Mailing Address Info
    private Long mailingAddressId;
    private String streetAddress;
    private String address1;
    private String address2;
    private Boolean physicalType;
    private Boolean billToType;
    private Boolean shipToType;
    private Boolean remitToType;
    private String city;
    private String state;
    private String zip;
    
    //External Org Info
    private Long orgId;
    private String orgName;
    
    //Person Info
    private Long personId;
    private String firstName;
    private String lastName;
    
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        physicalType = Boolean.FALSE;
        billToType = Boolean.FALSE;
        shipToType = Boolean.FALSE;
        remitToType = Boolean.FALSE;
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }


    public void setAddressBookExternalSourceId(Long addressBookExternalSourceId) {
        this.addressBookExternalSourceId = addressBookExternalSourceId;
    }


    public Long getAddressBookExternalSourceId() {
        return addressBookExternalSourceId;
    }


    public void setMailingAddressId(Long mailingAddressId) {
        this.mailingAddressId = mailingAddressId;
    }


    public Long getMailingAddressId() {
        return mailingAddressId;
    }


    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }


    public String getStreetAddress() {
        return streetAddress;
    }


    public void setPhysicalType(Boolean physicalType) {
        this.physicalType = physicalType;
    }


    public Boolean getPhysicalType() {
        return physicalType;
    }


    public void setBillToType(Boolean billToType) {
        this.billToType = billToType;
    }


    public Boolean getBillToType() {
        return billToType;
    }


    public void setShipToType(Boolean shipToType) {
        this.shipToType = shipToType;
    }


    public Boolean getShipToType() {
        return shipToType;
    }


    public void setRemitToType(Boolean remitToType) {
        this.remitToType = remitToType;
    }


    public Boolean getRemitToType() {
        return remitToType;
    }


    public void setCity(String city) {
        this.city = city;
    }


    public String getCity() {
        return city;
    }


    public void setState(String state) {
        this.state = state;
    }


    public String getState() {
        return state;
    }


    public void setZip(String zip) {
        this.zip = zip;
    }


    public String getZip() {
        return zip;
    }


    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }


    public Long getOrgId() {
        return orgId;
    }


    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }


    public String getOrgName() {
        return orgName;
    }


    public void setPersonId(Long personId) {
        this.personId = personId;
    }


    public Long getPersonId() {
        return personId;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getFirstName() {
        return firstName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setPersonalAddBkExtSourceId(Long personalAddBkExtSourceId) {
        this.personalAddBkExtSourceId = personalAddBkExtSourceId;
    }


    public Long getPersonalAddBkExtSourceId() {
        return personalAddBkExtSourceId;
    }
    
    public boolean getContactOnly() {
        return this.addressBookExternalSourceId != null && (orgId == null);
    }


    public void setAddress1(String address1) {
        this.address1 = address1;
    }


    public String getAddress1() {
        return address1;
    }


    public void setAddress2(String address2) {
        this.address2 = address2;
    }


    public String getAddress2() {
        return address2;
    }
    
}