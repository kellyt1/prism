package us.mn.state.health.view.common;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.model.common.User;

public class AddressBookMailingAddressForm extends ValidatorForm {

    //Personal Address Book Fields (Optional)
    private User owner;
    private String persAddBkMailAddrId;

    //AddressBookMailingAddress Properties
    private String addressBookMailingAddressId;
    private String mailToName;

    //MailingAddress Properties
    private String mailingAddressId;
    private String streetAddress;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private Boolean shipping = Boolean.FALSE;
    private Boolean physical = Boolean.FALSE;
    private Boolean billing = Boolean.FALSE;
    private Boolean remit = Boolean.FALSE;
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        shipping = Boolean.FALSE;
        physical = Boolean.FALSE;
        billing = Boolean.FALSE;
        remit = Boolean.FALSE;
    }
    
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        return null;
    }


    public void setAddressBookMailingAddressId(String addressBookMailingAddressId) {
        this.addressBookMailingAddressId = addressBookMailingAddressId;
    }


    public String getAddressBookMailingAddressId() {
        return addressBookMailingAddressId;
    }


    public void setMailToName(String mailToName) {
        this.mailToName = mailToName;
    }


    public String getMailToName() {
        return mailToName;
    }


    public void setMailingAddressId(String mailingAddressId) {
        this.mailingAddressId = mailingAddressId;
    }


    public String getMailingAddressId() {
        return mailingAddressId;
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


    public void setOwner(User owner) {
        this.owner = owner;
    }


    public User getOwner() {
        return owner;
    }


    public void setPersAddBkMailAddrId(String persAddBkMailAddrId) {
        this.persAddBkMailAddrId = persAddBkMailAddrId;
    }


    public String getPersAddBkMailAddrId() {
        return persAddBkMailAddrId;
    }


    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }


    public String getStreetAddress() {
        return streetAddress;
    }


    public void setShipping(Boolean shipping) {
        this.shipping = shipping;
    }


    public Boolean getShipping() {
        return shipping;
    }


    public void setPhysical(Boolean physical) {
        this.physical = physical;
    }


    public Boolean getPhysical() {
        return physical;
    }


    public void setBilling(Boolean billing) {
        this.billing = billing;
    }


    public Boolean getBilling() {
        return billing;
    }


    public void setRemit(Boolean remit) {
        this.remit = remit;
    }


    public Boolean getRemit() {
        return remit;
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