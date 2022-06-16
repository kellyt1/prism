package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.PurchaseItem;

public class Vendor implements Serializable {
    private Collection vendorContracts = new HashSet();
    private Boolean targeted;               //indicates whether this is a targeted vendor; replaces sed attribute from old system
    private Collection itemVendors = new HashSet();
    private Collection vendorAccounts = new HashSet();
    private Long vendorId;
    private ExternalOrgDetail externalOrgDetail = new ExternalOrgDetail();
    private String legacyId;

    //system-related properties
    private Date insertionDate;
    private String insertedBy;

    public static final String UNKNOWN = "UNKWN";

    public Vendor() {
    }

    /**
     * this constructor is used only for fast queries that need to displays the vendors in the lists
     *
     * @param vendorId
     * @param vendorName
     */
    public Vendor(Long vendorId, String vendorName) {
        this.vendorId = vendorId;
        this.getExternalOrgDetail().setOrgName(vendorName);
    }

    public String getLegacyId() {
        return legacyId;
    }

    public void setLegacyId(String legacyId) {
        this.legacyId = legacyId;
    }

    public void setVendorContracts(Collection vendorContracts) {
        this.vendorContracts = vendorContracts;
    }

    public Collection getVendorContracts() {
        return vendorContracts;
    }

    public void setTargeted(Boolean targeted) {
        this.targeted = targeted;
    }

    public Boolean getTargeted() {
        return targeted;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public Date getInsertionDate() {
        return this.insertionDate;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public String getInsertedBy() {
        return this.insertedBy;
    }

    public Collection getItemVendors() {
        return itemVendors;
    }

    public void setItemVendors(Collection itemVendors) {
        this.itemVendors = itemVendors;
    }

    public void setVendorAccounts(Collection vendorAccounts) {
        this.vendorAccounts = vendorAccounts;
    }

    public Collection getVendorAccounts() {
        return vendorAccounts;
    }

    public void addItem(PurchaseItem item, String user) throws InfrastructureException {
        this.addItem(item, null, user);
    }

    public void addItem(PurchaseItem item, VendorContract contract, String user) throws InfrastructureException {
        //the createItemVendor adds the ItemVendor to this class's (and Item's) itemVendors collection
        ItemVendor.createItemVendor(this, item, contract, user);
    }

    public void addVendorContract(VendorContract contract) {
        this.getVendorContracts().add(contract);
        contract.setVendor(this);
    }

    // ********************** Common Methods ********************** //
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vendor)) {
            return false;
        }
        final Vendor vendor = (Vendor) o;

        if (this.getVendorId() != null) {
            if (this.getVendorId().equals(vendor.getVendorId())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public int hashCode() {
        int result = 13;
        result *= (getExternalOrgDetail() != null ? getExternalOrgDetail().hashCode() : 0);
        return result;
    }

    public String toString() {
        return "Vendor ('" + vendorId + "'), " + "value: '" + externalOrgDetail.getOrgName() + "'";
    }

    public int compareTo(Object o) {
        if (o instanceof Vendor) {
            return externalOrgDetail.getOrgName().compareTo(((Vendor) o).externalOrgDetail.getOrgName());
        }
        return 0;
    }


    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }


    public Long getVendorId() {
        return vendorId;
    }


    public void setExternalOrgDetail(ExternalOrgDetail externalOrgDetail) {
        this.externalOrgDetail = externalOrgDetail;
    }


    public ExternalOrgDetail getExternalOrgDetail() {
        return externalOrgDetail;
    }

    public void setVendorName(String vendorName) {
        getExternalOrgDetail().setOrgName(vendorName.toUpperCase());
    }
}
