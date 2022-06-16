package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Date;

public class Manufacturer implements Serializable {
    private String insertedBy;
    private Date insertionDate;
    private String manufacturerType;
    private Long manufacturerId;
    private ExternalOrgDetail externalOrgDetail;
    private String manufacturerCode;
    public static final String CODE_UNKNOWN = "UNKWN";

    public String getInsertedBy() {
        return insertedBy;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public String getManufacturerType() {
        return manufacturerType;
    }

    public void setManufacturerType(String manufacturerType) {
        this.manufacturerType = manufacturerType;
    }

    // ********************** Common Methods ********************** //
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Manufacturer)) {
            return false;
        }
        final Manufacturer manufacturer = (Manufacturer) o;
        if (getExternalOrgDetail() != null && manufacturer.getExternalOrgDetail() != null &&
                getExternalOrgDetail().getOrgId().equals(manufacturer.getExternalOrgDetail().getOrgId())) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result;
        result = (externalOrgDetail.getOrgName() != null ? externalOrgDetail.getOrgName().hashCode() : 0);
        result = 29 * result + (getInsertionDate() != null ? getInsertionDate().hashCode() : 0);
        return result;
    }

    public String toString() {
        return "\n Manufacturer ('" + externalOrgDetail.getOrgId() + "'), " + "Name: '" + externalOrgDetail.getOrgName() + "'";
    }

    public int compareTo(Object o) {
        if (o instanceof Manufacturer) {
            return externalOrgDetail.getOrgName().compareTo(((Manufacturer) o).externalOrgDetail.getOrgName());
        }
        return 0;
    }


    public void setManufacturerId(Long manufacturerId) {
        this.manufacturerId = manufacturerId;
    }


    public Long getManufacturerId() {
        return manufacturerId;
    }

    public void setExternalOrgDetail(ExternalOrgDetail externalOrgDetail) {
        this.externalOrgDetail = externalOrgDetail;
    }


    public ExternalOrgDetail getExternalOrgDetail() {
        return externalOrgDetail;
    }


    public void setManufacturerCode(String manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }


    public String getManufacturerCode() {
        return manufacturerCode;
    }
}
