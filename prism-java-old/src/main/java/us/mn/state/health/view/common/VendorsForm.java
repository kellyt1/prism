package us.mn.state.health.view.common;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class VendorsForm extends ValidatorForm {
    private Collection vendors = new ArrayList();
    private String vendorId;
    private VendorForm vendorForm = new VendorForm();
    private String mailingAddressId;
    private String vendorContractId;
    private String vendorAccountId;
    private VendorContractForm currentVendorContractForm;
    private VendorAccountForm currentVendorAccountForm;
    private MailingAddressForm currentMailingAddressForm;
    private String cmd;

    public String getVendorContractId() {
        return vendorContractId;
    }

    public void setVendorContractId(String vendorContractId) {
        this.vendorContractId = vendorContractId;
    }


    public String getVendorAccountId() {
        return vendorAccountId;
    }

    public void setVendorAccountId(String vendorAccountId) {
        this.vendorAccountId = vendorAccountId;
    }

    public VendorContractForm getCurrentVendorContractForm() {
        return currentVendorContractForm;
    }

    public void setCurrentVendorContractForm(VendorContractForm currentVendorContractForm) {
        this.currentVendorContractForm = currentVendorContractForm;
    }


    public VendorAccountForm getCurrentVendorAccountForm() {
        return currentVendorAccountForm;
    }

    public void setCurrentVendorAccountForm(VendorAccountForm currentVendorAccountForm) {
        this.currentVendorAccountForm = currentVendorAccountForm;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public MailingAddressForm getCurrentMailingAddressForm() {
        return currentMailingAddressForm;
    }

    public void setCurrentMailingAddressForm(MailingAddressForm currentMailingAddressForm) {
        this.currentMailingAddressForm = currentMailingAddressForm;
    }

    public String getMailingAddressId() {
        return mailingAddressId;
    }

    public void setMailingAddressId(String mailingAddressId) {
        this.mailingAddressId = mailingAddressId;
    }

    public Collection getVendors() {
        return vendors;
    }

    public void setVendors(Collection vendors) {
        this.vendors = vendors;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public VendorForm getVendorForm() {
        return vendorForm;
    }

    public void setVendorForm(VendorForm vendorForm) {
        this.vendorForm = vendorForm;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (currentMailingAddressForm!=null) {
            currentMailingAddressForm.reset(mapping, request);
        }
        cmd="";
    }

    public String getCurrentCity(){
        return currentMailingAddressForm==null?null:currentMailingAddressForm.getCity();
    }

    public String getCurrentState(){
        return currentMailingAddressForm==null?null:currentMailingAddressForm.getState();
    }

    public String getCurrentZip(){
        return currentMailingAddressForm==null?null:currentMailingAddressForm.getZip();
    }

    public String getCurrentContractNumber(){
        return currentVendorContractForm==null?null:currentVendorContractForm.getContractNumber();
    }
    public String getCurrentContractStartDate(){
        return currentVendorContractForm==null?null:currentVendorContractForm.getStartDate();
    }
    public String getCurrentContractEndDate(){
        return currentVendorContractForm==null?null:currentVendorContractForm.getEndDate();
    }


}
