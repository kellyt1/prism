package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Date;

public class BusinessRulesORCL implements Serializable {

    private Long businessRulesId;
    private String objectValue;
    private String objectType;
    private Double minimumAmount;
    private String primaryEvaluator;
    private String evaluateField;
    private Date endDate;
    private Date lastUpdated;
    private String lastUpdatedBy;
    private Integer approvalLevel;
    private Long orgBudgetId;

    public String toString() {
        return "BusinessRulesORCL ('" + businessRulesId + "'), " + "value: '"
                + objectValue + "'";
    }
    public Long getBusinessRulesId() {
        return businessRulesId;
    }

    public void setBusinessRulesId(Long businessRulesId) {
        this.businessRulesId = businessRulesId;
    }

    public String getObjectValue() {
        return objectValue;
    }

    public void setObjectValue(String objectValue) {
        this.objectValue = objectValue;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Double getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(Double minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public String getPrimaryEvaluator() {
        return primaryEvaluator;
    }

    public void setPrimaryEvaluator(String primaryEvaluator) {
        this.primaryEvaluator = primaryEvaluator;
    }

    public String getEvaluateField() {
        return evaluateField;
    }

    public void setEvaluateField(String evaluateField) {
        this.evaluateField = evaluateField;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Integer getApprovalLevel() {
        return approvalLevel;
    }

    public void setApprovalLevel(Integer approvalLevel) {
        this.approvalLevel = approvalLevel;
    }

    public Long getOrgBudgetId() {
        return orgBudgetId;
    }

    public void setOrgBudgetId(Long orgBudgetId) {
        this.orgBudgetId = orgBudgetId;
    }
}
