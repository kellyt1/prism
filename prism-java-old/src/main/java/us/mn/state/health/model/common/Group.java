package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class Group implements Serializable {
    private Long groupId;
    private String groupShortName;
    private String groupName;
    private String groupCode;
    private String groupPurpose;
    private String insertedBy;
    private Date insertionDate;
    private String terminatedBy;
    private Date terminationDate;
    private String groupType;
    private Date endDate;
    private String approvedBy;
    private Date approvalDate;
    private String changedBy;
    private Date changeDate;
    private Collection personGroupLinks = new HashSet();

    public static final String BUYER_CODE = "PRISM-BUYER";
    public static final String STOCK_PICKER_CODE = "PRISM-STOCK-PICKER";
    public static final String STOCK_CONTROLLER_CODE = "PRISM-STOCK-CONTROLLER";
    public static final String STOCK_CONTROLLER_EMAIL_NOTIFICATION_CODE = "PRISM-STOCK-CNTRL-EMAIL-NOTIFY";
    public static final String STOCK_CONTROLLER_CHEMICAL_GASSES_EMAIL_NOTIFICATION_CODE = "PRISM-STOCK-CONTROLLER-FOR-CHEMICALS-GASSES-EMAIL-NOTIFY";
    public static final String PURCHASE_COORDINATOR_CODE = "PRISM-PURCHASE-COORDINATORS";
    public static final String COMP_PRCH_EVAL = "PRISM-COMPUTER-PURCHASE-EVALUATORS";
    public static final String FIXED_ASSET_CONTROLLER = "PRISM-FIXED-ASSETS";
    public static final String PRISM_DEVELOPERS = "PRISM-DEVELOPERS";
    public static final String RADI_PRCH_EVAL = "PRISM-RADIO-PURCHASE-EVALUATORS";
    public static final String IT_BUDGET_EVALUATORS = "PRISM-IT-BUDGET-EVALUATORS";
    public static final String SCOMP = "SCOMP";
    public static final String CONFERENCE_COORDINATORS = "PRISM-CONFERENCE-COORDINATORS";
    public static final String BOOK_GROUP_CODE = "PRISM-BOOK-NOTIFICATION";


//       public static final String BUYER_CODE = "BUYER";
//    public static final String STOCK_PICKER_CODE = "STOCK_PICKER";
//    public static final String STOCK_CONTROLLER_CODE = "STOCK_CONTROLLER";
//    public static final String STOCK_CONTROLLER_EMAIL_NOTIFICATION_CODE = "STOCK_CNTRL_EMAIL_NOTIF";
//    public static final String STOCK_CONTROLLER_CHEMICAL_GASSES_EMAIL_NOTIFICATION_CODE = "STK_CTRL_C_G_EMAIL_NOTIF";
//    public static final String PURCHASE_COORDINATOR_CODE = "PURCH_COORD";
//    public static final String COMP_PRCH_EVAL = "COMP_PRCH_EVAL";
//    public static final String FIXED_ASSET_CONTROLLER = "FIXED_ASSET_CONTROLLER";
//    public static final String PRISM_DEVELOPERS = "PRISM_DEVS";
//    public static final String RADI_PRCH_EVAL = "RADI_PRCH_EVAL";
//    public static final String SCOMP = "SCOMP";

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupPurpose() {
        return groupPurpose;
    }

    public void setGroupPurpose(String groupPurpose) {
        this.groupPurpose = groupPurpose;
    }

    public String getGroupShortName() {
        return groupShortName;
    }

    public void setGroupShortName(String groupShortName) {
        this.groupShortName = groupShortName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setTerminatedBy(String terminatedBy) {
        this.terminatedBy = terminatedBy;
    }

    public String getTerminatedBy() {
        return terminatedBy;
    }


    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }


    public Date getTerminationDate() {
        return terminationDate;
    }


    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }


    public String getApprovedBy() {
        return approvedBy;
    }


    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }


    public String getChangedBy() {
        return changedBy;
    }


    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }


    public Date getChangeDate() {
        return changeDate;
    }


    public void setPersonGroupLinks(Collection personGroupLinks) {
        this.personGroupLinks = personGroupLinks;
    }


    public Collection getPersonGroupLinks() {
        return personGroupLinks;
    }


    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }


    public String getGroupCode() {
        return groupCode;
    }
}
