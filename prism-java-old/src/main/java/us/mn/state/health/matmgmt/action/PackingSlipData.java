package us.mn.state.health.matmgmt.action;

import us.mn.state.health.view.materialsrequest.RequestForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource;
import java.util.Collection;
import java.util.Iterator;


/**
 * Created by IntelliJ IDEA.
 * User: RodenT1a
 * Date: Oct 13, 2008
 * Time: 12:56:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class PackingSlipData {
    private String additionalInstructions;
    private String deliverToInfoAsString;
    private String priority_name;
    private String requestId;
    private String requestor_firstAndLastName;
    private String trackingNumber;

    private String quantityFilled;
    private String quantityPicked;
    private Long request_requestId;
    private String stockItem_description;
    private String stockItem_dispenseUnit_name;
    private String stockItem_fullIcnbr;
    private Integer stockItem_icnbr;
    private String stockItem_orgBudget;

    public PackingSlipData(RequestForm rf) {
        this.additionalInstructions = rf.getAdditionalInstructions();
        this.deliverToInfoAsString = rf.getDeliverToInfoAsString();
        this.priority_name = rf.getPriorityCode();
        this.requestId = rf.getRequestId();
        this.requestor_firstAndLastName = rf.getRequestor().getFirstAndLastName();
        this.trackingNumber = rf.getTrackingNumber();

    }

  public void addRequestLineItem(RequestLineItemForm rlif) {
      this.quantityFilled = rlif.getQuantityFilled();
      this.quantityPicked = rlif.getQuantityPicked();
      this.request_requestId = rlif.getRequest().getRequestId();
      this.stockItem_description = rlif.getStockItem().getDescription();
      this.stockItem_dispenseUnit_name = rlif.getStockItem().getDispenseUnit().getName();
      this.stockItem_fullIcnbr = rlif.getStockItem().getFullIcnbr();
      this.stockItem_icnbr = rlif.getStockItem().getIcnbr();
      this.stockItem_orgBudget = "";
      Collection fs =  rlif.getRequestLineItem().getFundingSources();
      for (Iterator iterator = fs.iterator(); iterator.hasNext();) {
          Object o =  iterator.next();
          String str = ((RequestLineItemFundingSource)o).getOrgBudget().getOrgBudgetCode();
          if (str != null && str != "null") {
              this.stockItem_orgBudget += str + " ";
          }
//          this.stockItem_orgBudget += ((RequestLineItemFundingSource)o).getOrgBudget().getOrgBudgetCode() +" ";
      }
  }
/*
    public void addRequestLineItem0(RequestLineItemForm rlif) {
        RequestLineItemFormBuilder rlifb = new RequestLineItemFormBuilder(rlif, new HibernateDAOFactory());
        RequestLineItemFundingSourceForm rfs = new RequestLineItemFundingSourceForm();
        try {
            rlifb.buildRequestLineItemFundingSourceForms();
        } catch (InfrastructureException e) {
            e.printStackTrace();
        }
        this.quantityFilled = rlif.getQuantityFilled();
        this.quantityPicked = rlif.getQuantityPicked();
        this.request_requestId = rlif.getRequest().getRequestId();
//        this.request_requestId = new Long(rf.getRequestId());
        StockItemDAO siDAO = new HibernateStockItemDAO();
        StockItem si = new StockItem();
        try {
         si = siDAO.getStockItemById(rlif.getStockItem().getItemId(),false);
        } catch (InfrastructureException e ) {
            e.printStackTrace();
        }
        OrgBudget ob = si.getOrgBudget();
        this.stockItem_description = rlif.getStockItem().getDescription();
        this.stockItem_dispenseUnit_name = rlif.getStockItem().getDispenseUnit().getName();
        this.stockItem_fullIcnbr = rlif.getStockItem().getFullIcnbr();
        this.stockItem_icnbr = rlif.getStockItem().getIcnbr();
//      this.stockItem_orgBudget = rlif.getStockItem().getOrgBudget().getBudgetAccountCode();
//      this.stockItem_orgBudget = "org";//rlif.getStockItem().getOrgBudget().getBudgetAccountCode();
//      this.stockItem_orgBudget = rlif.getStockItem().getOrgBudget().getBudgetAccountCode();
//      HibernateOrgBudgetDAO OrgBudgetDAO = new HibernateOrgBudgetDAO();
//      OrgBudget ob = OrgBudgetDAO.findByOrgBudgetCode(rlif.get)
//      this.stockItem_orgBudget = ob.getOrgBudgetCode();
//      this.stockItem_orgBudget = rlif.getFundingSourceForm().getOrgBudgets().toString();
        this.stockItem_orgBudget = "";
        Collection fs =  rlif.getRequestLineItem().getFundingSources();
        for (Iterator iterator = fs.iterator(); iterator.hasNext();) {
            Object o =  iterator.next();
            this.stockItem_orgBudget += ((RequestLineItemFundingSource)o).getOrgBudget().getOrgBudgetCode() +" ";
//          this.stockItem_orgBudget += ((RequestLineItemFundingSource)o).getFundingOrgCode();//OrgBudgetCode() +" ";
        }
//      this.stockItem_orgBudget = "ll";//rlif.getRequestLineItem().getFundingSources()FundingSourceForm().getOrgBudgets().toString();
    }
*/

    public String getAdditionalInstructions() {
        return additionalInstructions;
    }

    public void setAdditionalInstructions(String additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }

    public String getDeliverToInfoAsString() {
        return deliverToInfoAsString;
    }

    public void setDeliverToInfoAsString(String deliverToInfoAsString) {
        this.deliverToInfoAsString = deliverToInfoAsString;
    }

    public String getPriority_name() {
        return priority_name;
    }

    public void setPriority_name(String priority_name) {
        this.priority_name = priority_name;
    }

    public String getQuantityFilled() {
        return quantityFilled;
    }

    public void setQuantityFilled(String quantityFilled) {
        this.quantityFilled = quantityFilled;
    }

    public String getQuantityPicked() {
        return quantityPicked;
    }

    public void setQuantityPicked(String quantityPicked) {
        this.quantityPicked = quantityPicked;
    }

    public Long getRequest_requestId() {
        return request_requestId;
    }

    public void setRequest_requestId(Long request_requestId) {
        this.request_requestId = request_requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestor_firstAndLastName() {
        return requestor_firstAndLastName;
    }

    public void setRequestor_firstAndLastName(String requestor_firstAndLastName) {
        this.requestor_firstAndLastName = requestor_firstAndLastName;
    }

    public String getStockItem_description() {
        return stockItem_description;
    }

    public void setStockItem_description(String stockItem_description) {
        this.stockItem_description = stockItem_description;
    }

    public String getStockItem_dispenseUnit_name() {
        return stockItem_dispenseUnit_name;
    }

    public void setStockItem_dispenseUnit_name(String stockItem_dispenseUnit_name) {
        this.stockItem_dispenseUnit_name = stockItem_dispenseUnit_name;
    }

    public String getStockItem_fullIcnbr() {
        return stockItem_fullIcnbr;
    }

    public void setStockItem_fullIcnbr(String stockItem_fullIcnbr) {
        this.stockItem_fullIcnbr = stockItem_fullIcnbr;
    }

    public Integer getStockItem_icnbr() {
        return stockItem_icnbr;
    }

    public void setStockItem_icnbr(Integer stockItem_icnbr) {
        this.stockItem_icnbr = stockItem_icnbr;
    }

    public String getStockItem_orgBudget() {
        return stockItem_orgBudget;
    }

    public void setStockItem_orgBudget(String stockItem_orgBudget) {
        this.stockItem_orgBudget = stockItem_orgBudget;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
}
