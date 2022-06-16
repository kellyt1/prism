package us.mn.state.health.model.legacySystem.fixedAssets;

import java.sql.Timestamp;

/**
 * This class is a pseudo-mapping of the Fixed Asset DBF file
 */
public class FixedAssetDbf {
    public static final String assetNumber_column = "AN";
    public static final String classCode_column = "CC";
    public static final String idnbr_column = "IDNBR";
    public static final String type_column = "TYPE";
    public static final String mnid_column = "MNID";
    public static final String description_column = "DESC";
    public static final String manufacturer_column = "MANUF";
    public static final String model_column = "MODEL";
    public static final String cost_column = "COST";
    public static final String serialNumber_column = "SN";
    public static final String building_column = "BLDG";
    public static final String room_column = "ROOM";
    public static final String workstation_column = "WS";
    public static final String dateReceived_column = "DATEREC";
    public static final String document_column = "DOCUMENT";
    public static final String bnbr_column = "BNBR";
    public static final String org_column = "ORG";
    public static final String fundingSource_column = "FUND";
    public static final String estimatedLifeCycle_column = "ESTLIFE";
    public static final String serviceContract_column = "SVC";
    public static final String maintainanceAgreement_column = "MAINT";
    public static final String maintainancePONumber_column = "MAINTPO";
    public static final String vidnbr_column = "VIDNBR";
    public static final String contractNumber_column = "CONT";
    public static final String contractExpirationDate_column = "CEXPDATE";
    public static final String warantyExpirationDate_column = "WEXPDATE";
    public static final String IExpirationDate_column = "IEXPDATE";

    private Integer assetNumber;
    private String classCode;
    private Integer idnbr;
    private Integer type;
    private Integer mnid;
    private String description;
    private String manufacturer;
    private String model;
    private Double cost;
    private String serialNumber;
    private String building;
    private String room;
    private String workstation;
    private Timestamp dateReceived;
    private String document;
    private String bnbr;
    private String org;
    private String fundingSource;
    private Integer estimatedLifeCycle;
    private Integer serviceContract;
    private Boolean maintainanceAgreement;
    private String maintainancePONumber;
    private String vidnbr;
    private String contractNumber;
    private Timestamp contractExpirationDate;
    private Timestamp warantyExpirationDate;
    private Timestamp IExpirationDate;


    public Integer getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(Integer assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public Integer getIdnbr() {
        return idnbr;
    }

    public void setIdnbr(Integer idnbr) {
        this.idnbr = idnbr;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMnid() {
        return mnid;
    }

    public void setMnid(Integer mnid) {
        this.mnid = mnid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    public Timestamp getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Timestamp dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getBnbr() {
        return bnbr;
    }

    public void setBnbr(String bnbr) {
        this.bnbr = bnbr;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public void setFundingSource(String fundingSource) {
        this.fundingSource = fundingSource;
    }

    public Integer getEstimatedLifeCycle() {
        return estimatedLifeCycle;
    }

    public void setEstimatedLifeCycle(Integer estimatedLifeCycle) {
        this.estimatedLifeCycle = estimatedLifeCycle;
    }

    public Integer getServiceContract() {
        return serviceContract;
    }

    public void setServiceContract(Integer serviceContract) {
        this.serviceContract = serviceContract;
    }

    public Boolean getMaintainanceAgreement() {
        return maintainanceAgreement;
    }

    public void setMaintainanceAgreement(Boolean maintainanceAgreement) {
        this.maintainanceAgreement = maintainanceAgreement;
    }

    public String getMaintainancePONumber() {
        return maintainancePONumber;
    }

    public void setMaintainancePONumber(String maintainancePONumber) {
        this.maintainancePONumber = maintainancePONumber;
    }

    public String getVidnbr() {
        return vidnbr;
    }

    public void setVidnbr(String vidnbr) {
        this.vidnbr = vidnbr;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Timestamp getContractExpirationDate() {
        return contractExpirationDate;
    }

    public void setContractExpirationDate(Timestamp contractExpirationDate) {
        this.contractExpirationDate = contractExpirationDate;
    }

    public Timestamp getWarantyExpirationDate() {
        return warantyExpirationDate;
    }

    public void setWarantyExpirationDate(Timestamp warantyExpirationDate) {
        this.warantyExpirationDate = warantyExpirationDate;
    }

    public Timestamp getIExpirationDate() {
        return IExpirationDate;
    }

    public void setIExpirationDate(Timestamp IExpirationDate) {
        this.IExpirationDate = IExpirationDate;
    }


    public String toString() {
        return "FixedAssetDbf{" +
                "assetNumber=" + assetNumber +
                ", classCode='" + classCode + "'" +
                ", idnbr=" + idnbr +
                ", type=" + type +
                ", mnid=" + mnid +
                ", description='" + description + "'" +
                ", manufacturer='" + manufacturer + "'" +
                ", model='" + model + "'" +
                ", cost=" + cost +
                ", serialNumber='" + serialNumber + "'" +
                ", building='" + building + "'" +
                ", room='" + room + "'" +
                ", workstation='" + workstation + "'" +
                ", dateReceived=" + dateReceived +
                ", document='" + document + "'" +
                ", bnbr='" + bnbr + "'" +
                ", org='" + org + "'" +
                ", fundingSource='" + fundingSource + "'" +
                ", estimatedLifeCycle=" + estimatedLifeCycle +
                ", serviceContract=" + serviceContract +
                ", maintainanceAgreement=" + maintainanceAgreement +
                ", maintainancePONumber='" + maintainancePONumber + "'" +
                ", vidnbr='" + vidnbr + "'" +
                ", contractNumber='" + contractNumber + "'" +
                ", contractExpirationDate=" + contractExpirationDate +
                ", warantyExpirationDate=" + warantyExpirationDate +
                ", IExpirationDate=" + IExpirationDate +
                "}" + "\n";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FixedAssetDbf)) return false;

        final FixedAssetDbf fixedAssetDbf = (FixedAssetDbf) o;

        if (IExpirationDate != null ? !IExpirationDate.equals(fixedAssetDbf.IExpirationDate) : fixedAssetDbf.IExpirationDate != null) return false;
        if (!assetNumber.equals(fixedAssetDbf.assetNumber)) return false;
        if (bnbr != null ? !bnbr.equals(fixedAssetDbf.bnbr) : fixedAssetDbf.bnbr != null) return false;
        if (building != null ? !building.equals(fixedAssetDbf.building) : fixedAssetDbf.building != null) return false;
        if (classCode != null ? !classCode.equals(fixedAssetDbf.classCode) : fixedAssetDbf.classCode != null) return false;
        if (contractExpirationDate != null ? !contractExpirationDate.equals(fixedAssetDbf.contractExpirationDate) : fixedAssetDbf.contractExpirationDate != null) return false;
        if (contractNumber != null ? !contractNumber.equals(fixedAssetDbf.contractNumber) : fixedAssetDbf.contractNumber != null) return false;
        if (!cost.equals(fixedAssetDbf.cost)) return false;
        if (dateReceived != null ? !dateReceived.equals(fixedAssetDbf.dateReceived) : fixedAssetDbf.dateReceived != null) return false;
        if (description != null ? !description.equals(fixedAssetDbf.description) : fixedAssetDbf.description != null) return false;
        if (document != null ? !document.equals(fixedAssetDbf.document) : fixedAssetDbf.document != null) return false;
        if (!estimatedLifeCycle.equals(fixedAssetDbf.estimatedLifeCycle)) return false;
        if (fundingSource != null ? !fundingSource.equals(fixedAssetDbf.fundingSource) : fixedAssetDbf.fundingSource != null) return false;
        if (!idnbr.equals(fixedAssetDbf.idnbr)) return false;
        if (!maintainanceAgreement.equals(fixedAssetDbf.maintainanceAgreement)) return false;
        if (maintainancePONumber != null ? !maintainancePONumber.equals(fixedAssetDbf.maintainancePONumber) : fixedAssetDbf.maintainancePONumber != null) return false;
        if (manufacturer != null ? !manufacturer.equals(fixedAssetDbf.manufacturer) : fixedAssetDbf.manufacturer != null) return false;
        if (mnid != null ? !mnid.equals(fixedAssetDbf.mnid) : fixedAssetDbf.mnid != null) return false;
        if (model != null ? !model.equals(fixedAssetDbf.model) : fixedAssetDbf.model != null) return false;
        if (org != null ? !org.equals(fixedAssetDbf.org) : fixedAssetDbf.org != null) return false;
        if (room != null ? !room.equals(fixedAssetDbf.room) : fixedAssetDbf.room != null) return false;
        if (serialNumber != null ? !serialNumber.equals(fixedAssetDbf.serialNumber) : fixedAssetDbf.serialNumber != null) return false;
        if (!serviceContract.equals(fixedAssetDbf.serviceContract)) return false;
        if (!type.equals(fixedAssetDbf.type)) return false;
        if (vidnbr != null ? !vidnbr.equals(fixedAssetDbf.vidnbr) : fixedAssetDbf.vidnbr != null) return false;
        if (warantyExpirationDate != null ? !warantyExpirationDate.equals(fixedAssetDbf.warantyExpirationDate) : fixedAssetDbf.warantyExpirationDate != null) return false;
        if (workstation != null ? !workstation.equals(fixedAssetDbf.workstation) : fixedAssetDbf.workstation != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = assetNumber.hashCode();
        result = 29 * result + (classCode != null ? classCode.hashCode() : 0);
        result = 29 * result + idnbr.hashCode();
        result = 29 * result + type.hashCode();
        result = 29 * result + (mnid != null ? mnid.hashCode() : 0);
        result = 29 * result + (description != null ? description.hashCode() : 0);
        result = 29 * result + (manufacturer != null ? manufacturer.hashCode() : 0);
        result = 29 * result + (model != null ? model.hashCode() : 0);
        result = 29 * result + cost.hashCode();
        result = 29 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
        result = 29 * result + (building != null ? building.hashCode() : 0);
        result = 29 * result + (room != null ? room.hashCode() : 0);
        result = 29 * result + (workstation != null ? workstation.hashCode() : 0);
        result = 29 * result + (dateReceived != null ? dateReceived.hashCode() : 0);
        result = 29 * result + (document != null ? document.hashCode() : 0);
        result = 29 * result + (bnbr != null ? bnbr.hashCode() : 0);
        result = 29 * result + (org != null ? org.hashCode() : 0);
        result = 29 * result + (fundingSource != null ? fundingSource.hashCode() : 0);
        result = 29 * result + estimatedLifeCycle.hashCode();
        result = 29 * result + serviceContract.hashCode();
        result = 29 * result + maintainanceAgreement.hashCode();
        result = 29 * result + (maintainancePONumber != null ? maintainancePONumber.hashCode() : 0);
        result = 29 * result + (vidnbr != null ? vidnbr.hashCode() : 0);
        result = 29 * result + (contractNumber != null ? contractNumber.hashCode() : 0);
        result = 29 * result + (contractExpirationDate != null ? contractExpirationDate.hashCode() : 0);
        result = 29 * result + (warantyExpirationDate != null ? warantyExpirationDate.hashCode() : 0);
        result = 29 * result + (IExpirationDate != null ? IExpirationDate.hashCode() : 0);
        return result;
    }
}
