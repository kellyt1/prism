package us.mn.state.health.model.materialsrequest;

import java.io.Serializable;
import java.util.*;

import us.mn.state.health.model.common.ReportCategory;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import org.hibernate.LazyInitializationException;

public class ReqLnItemFundingSourcesSummary implements Serializable {

    private String names;
    private String orgBudgetCodes;
    private String budgetAccountCodes;
    private String reportCategories;
    private String appropriationCodes;
    private String fundCodes;
    private Set<String> fundingStrings = new HashSet<String>();

    public ReqLnItemFundingSourcesSummary(Collection rliFundingSources) {
        this.names = "";
        this.orgBudgetCodes = "";
        this.budgetAccountCodes = "";
        this.reportCategories = "";
        this.appropriationCodes = "";

        RequestLineItemFundingSource rliFundingSrc;
        Object[] array = rliFundingSources.toArray();
        for (int i = 0; i < array.length; i++) {
            rliFundingSrc = (RequestLineItemFundingSource) array[i];

            this.names += rliFundingSrc.getOrgBudget().getName();
            this.orgBudgetCodes += rliFundingSrc.getOrgBudget().getOrgBudgetCodeAndFY();
            this.budgetAccountCodes += rliFundingSrc.getOrgBudget().getBudgetAccountCode();
            this.fundingStrings.add(rliFundingSrc.getOrgBudget().getFundingString());
            try {
                Collection<ReportCategory>  rptCats = DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getOrgBudgetDAO().findRptCategoriesByOrg(rliFundingSrc.getOrgBudget().getOrgBudgetCode());
                if (rptCats != null) {
                    for (ReportCategory cat : rptCats) {
                        reportCategories += cat.getRepCatApprCode() + " ";
                    }
                }
                // PUT IN JMP TO catch org.hibernate.LazyInitialization.
            } catch (LazyInitializationException e) {
                reportCategories = null;
            }
             catch (InfrastructureException e) {
                reportCategories = null;
             }
            this.appropriationCodes += rliFundingSrc.getOrgBudget().getAppropriationCode();

            if (i < (array.length - 1)) {
                this.names += " ";
                this.orgBudgetCodes += " ";
                this.budgetAccountCodes += " ";
                this.budgetAccountCodes += " ";
                this.reportCategories += " ";
                this.appropriationCodes += " ";
            }
        }
    }


    public String getNames() {
        return names;
    }

    public String getOrgBudgetCodes() {
        return orgBudgetCodes;
    }

    public String getBudgetAccountCodes() {
        return budgetAccountCodes;
    }

    public String getReportCategories() {
        return reportCategories;
    }

    public String getAppropriationCodes() {
        return appropriationCodes;
    }

    public String getFundCodes() {
        return fundCodes;
    }

    public Set<String> getFundingStrings() {
        return fundingStrings;
    }

    public void setFundingStrings(Set<String> fundingStrings) {
        this.fundingStrings = fundingStrings;
    }
}