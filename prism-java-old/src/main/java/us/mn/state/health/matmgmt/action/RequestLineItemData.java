package us.mn.state.health.matmgmt.action;

import us.mn.state.health.model.materialsrequest.Request;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource;
import us.mn.state.health.common.util.CollectionUtils;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: rodent1
 * Date: Sep 8, 2009
 * Time: 4:13:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestLineItemData {
//    private Request request;
//    private RequestLineItem rli;
    private Collection<FundingSourceData> rliFundingSources  = new ArrayList(); // AllReqLnItmFundingSources();
    private Long lineItemId;
    private String trackingNumber;
    private String description;
    private Double cost;
    private Integer quantity;
    private Double extendedAmount;


    public RequestLineItemData() {
    }

    public RequestLineItemData(RequestLineItem rli) {
        Request request = rli.getRequest();
        Collection cFundingSource = rli.getFundingSources();
        for (Iterator iterator = cFundingSource.iterator(); iterator.hasNext();) {
            RequestLineItemFundingSource o =  (RequestLineItemFundingSource)iterator.next();
            FundingSourceData cFundingSourceData = new FundingSourceData();
            cFundingSourceData.addFundingSourceData(o);
            rliFundingSources.add(cFundingSourceData);
        }
        this.lineItemId = rli.getRequestLineItemId();
        this.trackingNumber = request.getTrackingNumber();
        this.description = rli.getItemDescription();
        this.cost = rli.getItemCost();
        this.quantity = rli.getQuantity();
        this.extendedAmount = this.cost * this.quantity;
    }


    public Collection<FundingSourceData> getRliFundingSources() {
    return rliFundingSources;
}

    public void setRliFundingSources(Collection<FundingSourceData> rliFundingSources) {
        this.rliFundingSources = rliFundingSources;
    }

    public Long getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(Long lineItemId) {
        this.lineItemId = lineItemId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getExtendedAmount() {
        return extendedAmount;
    }

    public void setExtendedAmount(Double extendedAmount) {
        this.extendedAmount = extendedAmount;
    }
}
