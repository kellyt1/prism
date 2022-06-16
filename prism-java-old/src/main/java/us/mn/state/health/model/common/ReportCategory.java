package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ReportCategory implements Serializable {
    private long reportCategoryId;
    private String repCatApprCode;
    private String repCatOrgBdgt;
    private String repCatName;
    private String repCatFederalYear;
    private String repCatBeginDate;
    private String repCatEndDate;
    private String repCatCode;
    private String insertedBy;
    private Date insertionDate;
    private String comments;
    private Date endDate;
    public ReportCategory() {
    }

    public long getReportCategoryId() {
        return reportCategoryId;
    }

    public void setReportCategoryId(long reportCategoryId) {
        this.reportCategoryId = reportCategoryId;
    }

    public String getRepCatApprCode() {
        return repCatApprCode;
    }

    public void setRepCatApprCode(String repCatApprCode) {
        this.repCatApprCode = repCatApprCode;
    }

    public String getRepCatOrgBdgt() {
        return repCatOrgBdgt;
    }

    public void setRepCatOrgBdgt(String repCatOrgBdgt) {
        this.repCatOrgBdgt = repCatOrgBdgt;
    }

    public String getRepCatName() {
        return repCatName;
    }

    public void setRepCatName(String repCatName) {
        this.repCatName = repCatName;
    }

    public String getRepCatFederalYear() {
        return repCatFederalYear;
    }

    public void setRepCatFederalYear(String repCatFederalYear) {
        this.repCatFederalYear = repCatFederalYear;
    }

    public String getRepCatBeginDate() {
        return repCatBeginDate;
    }

    public void setRepCatBeginDate(String repCatBeginDate) {
        this.repCatBeginDate = repCatBeginDate;
    }

    public String getRepCatEndDate() {
        return repCatEndDate;
    }

    public void setRepCatEndDate(String repCatEndDate) {
        this.repCatEndDate = repCatEndDate;
    }

    public String getRepCatCode() {
        return repCatCode;
    }

    public void setRepCatCode(String repCatCode) {
        this.repCatCode = repCatCode;
    }

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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }



}
