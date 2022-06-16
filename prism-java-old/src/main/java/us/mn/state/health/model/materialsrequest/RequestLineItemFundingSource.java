package us.mn.state.health.model.materialsrequest;

import java.io.Serializable;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.OrgBudget;

import javax.persistence.Transient;

public class RequestLineItemFundingSource implements Serializable {
    private Long requestLineItemFundingSourceId;
    private OrgBudget orgBudget;
    private Double amount;                      //dollars or percent is part of requestLineItem
    private RequestLineItem requestLineItem;
    private int version;
    private DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);

    public RequestLineItemFundingSource() {
    }

    public RequestLineItemFundingSource(OrgBudget orgBudget, Double amount, RequestLineItem requestLineItem) {
        this.orgBudget = orgBudget;
        this.amount = amount;
        this.requestLineItem = requestLineItem;
    }

    public void save() throws InfrastructureException {
        daoFactory.getRequestLineItemFundingSourceDAO().makePersistent(this);
    }

    public void delete() throws InfrastructureException {
        if (this.requestLineItemFundingSourceId != null) {
            daoFactory.getRequestLineItemFundingSourceDAO().makeTransient(this);
        }
    }

    public Long getRequestLineItemFundingSourceId() {
        return requestLineItemFundingSourceId;
    }

    public void setRequestLineItemFundingSourceId(Long requestLineItemFundingSourceId) {
        this.requestLineItemFundingSourceId = requestLineItemFundingSourceId;
    }

    public OrgBudget getOrgBudget() {
        return orgBudget;
    }

    public void setOrgBudget(OrgBudget orgBudget) {
        this.orgBudget = orgBudget;
    }

    public RequestLineItem getRequestLineItem() {
        return requestLineItem;
    }

    public void setRequestLineItem(RequestLineItem requestLineItem) {
        this.requestLineItem = requestLineItem;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }


    public void setVersion(int version) {
        this.version = version;
    }


    public int getVersion() {
        return version;
    }

    /**
     * A helper method for the rules4j rules engine, so that we don't have to
     * write such long rules.  Ie., instead of having to write huge OR statements such as
     * if(RLIFundingSource.orgBudget.orgBudgetCode == '2701') || (RLIFundingSource.orgBudget.orgBudgetCode == '2702') || (...)
     * and on and on for as many actual orgBudgets there are, we can just say:
     * if(RLIFundingSource.fundingOrgCode == '27')
     * Rules4J cannot call the String substr() method on
     * this class's orgBudget property, so this method will do that for us.  It will return
     * the first 2 characters of the orgBudget.orgBudgetCode
     */
    @Transient
    public String getFundingOrgCode() {
        return this.orgBudget.getOrgBudgetCode().substring(0, 2);
    }
}
