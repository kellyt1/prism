package us.mn.state.health.model.legacySystem.fixedAssets;

import java.util.Date;

public class FixedAssetOracle {
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

    private String assetNumber;
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
    private Date dateReceived;
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
    private Date contractExpirationDate;
    private Date warantyExpirationDate;
    private Date IExpirationDate;

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
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

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
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

    public Date getContractExpirationDate() {
        return contractExpirationDate;
    }

    public void setContractExpirationDate(Date contractExpirationDate) {
        this.contractExpirationDate = contractExpirationDate;
    }

    public Date getWarantyExpirationDate() {
        return warantyExpirationDate;
    }

    public void setWarantyExpirationDate(Date warantyExpirationDate) {
        this.warantyExpirationDate = warantyExpirationDate;
    }

    public Date getIExpirationDate() {
        return IExpirationDate;
    }

    public void setIExpirationDate(Date IExpirationDate) {
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
}