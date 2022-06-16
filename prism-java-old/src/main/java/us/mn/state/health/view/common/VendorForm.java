package us.mn.state.health.view.common;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class VendorForm extends ValidatorForm {

    private String vendorId;
    private String accountNbr;
    private ExternalOrgDetailForm externalOrgDetailForm = new ExternalOrgDetailForm();
    private Set vendorContracts = new HashSet(); // Collection of VendorContractForm's
    private Set vendorAccounts = new HashSet();


    public Set getVendorAccounts() {
        return vendorAccounts;
    }

    public void setVendorAccounts(Set vendorAccounts) {
        this.vendorAccounts = vendorAccounts;
    }

    public Set getVendorContracts() {
        return vendorContracts;
    }

    public void setVendorContracts(Set vendorContracts) {
        this.vendorContracts = vendorContracts;
    }

    public ExternalOrgDetailForm getExternalOrgDetailForm() {
        return externalOrgDetailForm;
    }

    public void setExternalOrgDetailForm(ExternalOrgDetailForm externalOrgDetailForm) {
        this.externalOrgDetailForm = externalOrgDetailForm;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }


    public String getVendorId() {
        return vendorId;
    }


    public void setAccountNbr(String accountNbr) {
        this.accountNbr = accountNbr;
    }


    public String getAccountNbr() {
        return accountNbr;
    }


    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }
}