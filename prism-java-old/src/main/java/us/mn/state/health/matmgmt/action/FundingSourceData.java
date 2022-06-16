package us.mn.state.health.matmgmt.action;

import us.mn.state.health.model.purchasing.Order;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: RodenT1a
 * Date: Oct 31, 2008
 * Time: 11:43:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class FundingSourceData {
private Double amount = new Double(0);
private String orgBudget_provisions="";
private String orgBudget_rptCategoriesList="";
private String orgBudget_appropriationCode="";
private String orgBudget_orgBudgetCode="";
private String orgBudget_fundCode="";
private String orgBudget_budgetAccountCode="";
private String orgBudget_programCode="";
private String orgBudget_fiscalYear = "";
private String orgBudget_deptId = "";
private String orgBudget_projectId = "";
private String orgBudget_percentage = "";
private String orgBudget_sourceType = "";

public void addFundingSourceData(RequestLineItemFundingSource fundingSource) {
            orgBudget_fiscalYear = fundingSource.getOrgBudget().getOrgBudgetCodeAndNameAndFY();
            orgBudget_programCode = fundingSource.getOrgBudget().getProgramCode();
            orgBudget_budgetAccountCode = fundingSource.getOrgBudget().getBudgetAccountCode();
            orgBudget_fundCode = fundingSource.getOrgBudget().getFundCode();
            orgBudget_orgBudgetCode = fundingSource.getOrgBudget().getOrgBudgetCode();
            orgBudget_appropriationCode = fundingSource.getOrgBudget().getAppropriationCode();
            orgBudget_rptCategoriesList = fundingSource.getOrgBudget().getRptCategoriesList();
            orgBudget_provisions = fundingSource.getOrgBudget().getProvisions();
            amount = fundingSource.getAmount();
            orgBudget_deptId = fundingSource.getOrgBudget().getDeptId();
            orgBudget_projectId = fundingSource.getOrgBudget().getProjectId();
            orgBudget_percentage = "";
            orgBudget_sourceType = fundingSource.getOrgBudget().getSourceType();
}

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOrgBudget_provisions() {
        return orgBudget_provisions;
    }

    public void setOrgBudget_provisions(String orgBudget_provisions) {
        this.orgBudget_provisions = orgBudget_provisions;
    }

    public String getOrgBudget_rptCategoriesList() {
        return orgBudget_rptCategoriesList;
    }

    public void setOrgBudget_rptCategoriesList(String orgBudget_rptCategoriesList) {
        this.orgBudget_rptCategoriesList = orgBudget_rptCategoriesList;
    }

    public String getOrgBudget_appropriationCode() {
        return orgBudget_appropriationCode;
    }

    public void setOrgBudget_appropriationCode(String orgBudget_appropriationCode) {
        this.orgBudget_appropriationCode = orgBudget_appropriationCode;
    }

    public String getOrgBudget_orgBudgetCode() {
        return orgBudget_orgBudgetCode;
    }

    public void setOrgBudget_orgBudgetCode(String orgBudget_orgBudgetCode) {
        this.orgBudget_orgBudgetCode = orgBudget_orgBudgetCode;
    }

    public String getOrgBudget_fundCode() {
        return orgBudget_fundCode;
    }

    public void setOrgBudget_fundCode(String orgBudget_fundCode) {
        this.orgBudget_fundCode = orgBudget_fundCode;
    }

    public String getOrgBudget_budgetAccountCode() {
        return orgBudget_budgetAccountCode;
    }

    public void setOrgBudget_budgetAccountCode(String orgBudget_budgetAccountCode) {
        this.orgBudget_budgetAccountCode = orgBudget_budgetAccountCode;
    }

    public String getOrgBudget_programCode() {
        return orgBudget_programCode;
    }

    public void setOrgBudget_programCode(String orgBudget_programCode) {
        this.orgBudget_programCode = orgBudget_programCode;
    }

    public String getOrgBudget_fiscalYear() {
        return orgBudget_fiscalYear;
    }

    public void setOrgBudget_fiscalYear(String orgBudget_fiscalYear) {
        this.orgBudget_fiscalYear = orgBudget_fiscalYear;
    }

    public String getOrgBudget_deptId() {
        return orgBudget_deptId;
    }

    public void setOrgBudget_deptId(String orgBudget_deptId) {
        this.orgBudget_deptId = orgBudget_deptId;
    }

    public String getOrgBudget_projectId() {
        return orgBudget_projectId;
    }

    public void setOrgBudget_projectId(String orgBudget_projectId) {
        this.orgBudget_projectId = orgBudget_projectId;
    }

    public String getOrgBudget_percentage() {
        return orgBudget_percentage;
    }

    public void setOrgBudget_percentage(String orgBudget_percentage) {
        this.orgBudget_percentage = orgBudget_percentage;
    }

    public String getOrgBudget_sourceType() {
        return orgBudget_sourceType;
    }

    public void setOrgBudget_sourceType(String orgBudget_sourceType) {
        this.orgBudget_sourceType = orgBudget_sourceType;
    }
}