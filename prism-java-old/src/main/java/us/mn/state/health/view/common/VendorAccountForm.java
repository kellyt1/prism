package us.mn.state.health.view.common;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;

import us.mn.state.health.model.common.Vendor;

public class VendorAccountForm {
    private Long vendorAccountId;
     private Vendor vendor;
     private String accountNbr;



    public void reset(ActionMapping mapping, HttpServletRequest request) {

    }


    public Long getVendorAccountId() {
        return vendorAccountId;
    }

    public void setVendorAccountId(Long vendorAccountId) {
        this.vendorAccountId = vendorAccountId;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public String getAccountNbr() {
        return accountNbr;
    }

    public void setAccountNbr(String accountNbr) {
        this.accountNbr = accountNbr;
    }

    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        return null;
    }

    public String getKey(){
        return this.toString();
    }
}
