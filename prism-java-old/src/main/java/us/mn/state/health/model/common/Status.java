package us.mn.state.health.model.common;

import us.mn.state.health.util.ProxyUtils;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

public class Status implements Serializable {
    private StatusType statusType;
    private String statusCode;
    private String name;
    private Long statusId;
    private Date insertionDate;
    private String insertedBy;

    //Request Line Item codes
    public static final String WAITING_FOR_DISPERSAL = "WFD";
    public static final String FULFILLED = "FLD";

    //Purchasing Related Request Line Item Codes
    public static final String WAITING_FOR_PURCHASE = "WFP";
    public static final String PENDING_NEED_MORE_INFO = "PMI";
    public static final String PENDING_BUILDING_ORDER = "PBO";
    public static final String PENDING_OUT_FOR_BID = "POB";
    public static final String FAILED_INCUMBRANCE = "FIN";
    public static final String STANDING_LAB_ORDER = "SLO";

    // ?? 7297005 - Tracking Number
    public static final String COMPUTER_PURCHASE_ORDER = "CPO";

    public static final String CANCELLED_ITEM_DISCONTINUED = "CID";

    //Purchase Order Line Item Related Codes
    public static final String ORDERED_ON_BACK_ORDER = "OBO";
    public static final String PENDING_INCUMBRANCE = "PIN";
    public static final String RECEIVED = "RCD";
    public static final String RECEIVED_PARTIAL = "RCP";
    public static final String DELIVERED = "DLD";
    public static final String DELIVERED_PARTIAL = "DLP";
    public static final String SWIFT_ORDERED = "SWO";
    public static final String MNIT_ORDERED = "MNO";
    public static final String MNIT_ENCUMBERED = "MNE";

    //General Codes
    public static final String APPROVED = "APP";
    public static final String CANCELED = "CLD";
    public static final String CANCELED_NAME = "CANCELED";
    public static final String DENIED = "DEN";
    public static final String ORDERED = "ORD";
    public static final String WAITING_FOR_APPROVAL = "WFA";

    //Stock Item Codes
    public static final String INACTIVE = "INA";
    public static final String ACTIVE = "ACT";
    public static final String ONHOLD = "ONH";

    public StatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    @Transient
    public String getAPPROVED() {
        return APPROVED;
    }

    @Transient
    public String getDENIED() {
        return DENIED;
    }

    @Transient
    public String getWAITING_FOR_APPROVAL() {
        return WAITING_FOR_APPROVAL;
    }

    @Transient
    public String getINACTIVE() {
        return INACTIVE;
    }

    @Transient
    public String getACTIVE() {
        return ACTIVE;
    }

    @Transient
    public String getONHOLD() {
        return ONHOLD;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (!ProxyUtils.validateClassesForEquals(Status.class, this, o)) return false;

        Status status = (Status) o;

        if (name != null ? !name.equals(status.getName()) : status.getName() != null) return false;
        if (!statusCode.equals(status.getStatusCode())) return false;
        if (!statusId.equals(status.getStatusId())) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = statusCode.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + statusId.hashCode();
        return result;
    }
}
