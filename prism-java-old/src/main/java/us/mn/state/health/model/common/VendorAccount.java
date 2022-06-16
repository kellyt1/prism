package us.mn.state.health.model.common;

import java.io.Serializable;

public class VendorAccount implements Serializable, Comparable {
    private Long vendorAccountId;
    private Vendor vendor;
    private String accountNbr;

    // ********************** Common Methods ********************** //
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof VendorAccount)) {
            return false;
        }
        final VendorAccount vendorAccount = (VendorAccount)o;
        if(accountNbr != null ? !accountNbr.equals(vendorAccount.accountNbr) : vendorAccount.accountNbr != null) {
                return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        result = (accountNbr != null ? accountNbr.hashCode() : 0);
        result = 29 * result + (accountNbr != null ? accountNbr.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "VendorAccount: '" + getAccountNbr() + "',\n " + 
               "Vendor: '" + getVendor().getExternalOrgDetail().getOrgName() + "'\n";
    }

    public int compareTo(Object o) {
        if (o instanceof VendorAccount) {
            return this.getAccountNbr().compareTo(((VendorAccount)o).getAccountNbr());
        }
        return 0;
    }

    public void setVendorAccountId(Long vendorAccountId) {
        this.vendorAccountId = vendorAccountId;
    }


    public Long getVendorAccountId() {
        return vendorAccountId;
    }


    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }


    public Vendor getVendor() {
        return vendor;
    }


    public void setAccountNbr(String accountNbr) {
        this.accountNbr = accountNbr;
    }


    public String getAccountNbr() {
        return accountNbr;
    }
}
