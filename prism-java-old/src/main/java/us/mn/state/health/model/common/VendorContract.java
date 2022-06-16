package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class VendorContract implements Serializable, Comparable {
    private String comments;
    private Date endDate;
    private Long vendorContractId;
    private Date insertionDate;
    private String insertedBy;
    private Collection itemVendors = new HashSet();
    private String contractNumber;
    private Date startDate;
    private Vendor vendor;
    private int version;
    private String deliveryTerms;

    public String getDeliveryTerms() {
        return deliveryTerms;
    }

    public void setDeliveryTerms(String deliveryTerms) {
        this.deliveryTerms = deliveryTerms;
    }

    public String getComments() { return comments; }

    public void setComments(String comments) { this.comments = comments; }

    public Date getEndDate() { return endDate; }

    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public Long getVendorContractId() { return vendorContractId; }

    public void setVendorContractId(Long vendorContractId) { this.vendorContractId = vendorContractId; }

    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public Date getInsertionDate() { return insertionDate; }

    public void setInsertionDate(Date insertionDate) { this.insertionDate = insertionDate; }

    public String getInsertedBy() { return insertedBy; }

    public void setInsertedBy(String insertedBy) { this.insertedBy = insertedBy; }

    public Collection getItemVendors() { return itemVendors; }

    public void setItemVendors(Collection itemVendors) { this.itemVendors = itemVendors; }

    public String getContractNumber() { return contractNumber; }

    public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }

    public Date getStartDate() { return startDate; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Vendor getVendor() { return vendor; }

    // ********************** Common Methods ********************** //
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VendorContract)) {
            return false;
        }
        final VendorContract vendorContract = (VendorContract)o;
        if (contractNumber != null ? !contractNumber.equals(vendorContract.contractNumber) :
            vendorContract.contractNumber != null) {
                return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        result = (contractNumber != null ? contractNumber.hashCode() : 0);
        result = 29 * result + (contractNumber != null ? contractNumber.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "VendorContract: '" + getContractNumber() + "',\n " + "Vendor: '" + getVendor().getExternalOrgDetail().getOrgName() + "'\n" +
            "startDate: '" + getStartDate();
    }

    public int compareTo(Object o) {
        if (o instanceof VendorContract) {
            return this.getContractNumber().compareTo(((VendorContract)o).getContractNumber());
        }
        return 0;
    }


    public void setVersion(int version) {
        this.version = version;
    }


    public int getVersion() {
        return version;
    }
}
