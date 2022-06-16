package us.mn.state.health.view.inventory;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.inventory.ActionRequestType;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.view.common.StatusForm;

public class StockItemActionRequestForm extends ValidatorForm {
    private Person requestor;
    private Person evaluator;
    private String specialInstructions;
    private String actionRequestTypeId;
    private ActionRequestType actionRequestType;
    private String stockItemActionRequestId;
    private String suggestedVendorId;
    private String vendorId;
    private Collection vendors = new HashSet();
    private Collection manufacturers = new HashSet();
    private String stkQtyChangeReasonRefId;
    private Collection stkQtyChangeReasonRefs = new HashSet();
    private String stkQtyChangeOrgBdgtId;
    private String suggestedVendorName;
    private StatusForm statusId;
    private Collection statuses;
    private StockItemForm potentialStockItemForm;
    private StockItem stockItem;
    private Date requestedDate;
    private String cmd = "";
    private String evaluationReason;
    private Boolean approved = null;
    private String requestReason;
    private Boolean discardStock; //used only for On-Hold and Inactive statuses
    private Boolean readOnly = Boolean.FALSE;
    private String siId;

    private String vendorCatalogNbr;
    private Double vendorCost;

    public String getVendorCatalogNbr() {
        return vendorCatalogNbr;
    }

    public void setVendorCatalogNbr(String vendorCatalogNbr) {
        this.vendorCatalogNbr = vendorCatalogNbr;
    }

    public Double getVendorCost() {
        return vendorCost;
    }

    public void setVendorCost(Double vendorCost) {
        this.vendorCost = vendorCost;
    }

    private boolean reset = false;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (reset) {
            specialInstructions = null;
            actionRequestTypeId = null;
            statusId = null;
            statuses = null;
            cmd = "";
            stkQtyChangeReasonRefId = "";
            siId = null;
            reset = false;
            stkQtyChangeOrgBdgtId = "";
            evaluationReason = "";
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Boolean getDiscardStock() {
        return discardStock;
    }

    public void setDiscardStock(Boolean discardStock) {
        this.discardStock = discardStock;
    }

    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public Vendor getSuggestedVendor() {
        Vendor vendor = (Vendor) CollectionUtils.getObjectFromCollectionById(vendors, suggestedVendorId, "vendorId");
        return vendor;
    }

    public Vendor getVendor() {
        Vendor vendor = (Vendor) CollectionUtils.getObjectFromCollectionById(vendors, vendorId, "vendorId");
        return vendor;
    }

    public Status getStatus() {
        Status status = (Status) CollectionUtils.getObjectFromCollectionById(statuses, statusId, "statusId");
        return status;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }


    public String getSpecialInstructions() {
        return specialInstructions;
    }


    public void setStockItemActionRequestId(String stockItemActionRequestId) {
        this.stockItemActionRequestId = stockItemActionRequestId;
    }


    public String getStockItemActionRequestId() {
        return stockItemActionRequestId;
    }


    public void setSuggestedVendorId(String suggestedVendorId) {
        this.suggestedVendorId = suggestedVendorId;
    }


    public String getSuggestedVendorId() {
        return suggestedVendorId;
    }

    public void setStatusId(StatusForm statusId) {
        this.statusId = statusId;
    }


    public StatusForm getStatusId() {
        return statusId;
    }


    public void setSuggestedVendorName(String suggestedVendorName) {
        this.suggestedVendorName = suggestedVendorName;
    }


    public String getSuggestedVendorName() {
        return suggestedVendorName;
    }

    public void setPotentialStockItemForm(StockItemForm potentialStockItemForm) {
        this.potentialStockItemForm = potentialStockItemForm;
    }


    public StockItemForm getPotentialStockItemForm() {
        return potentialStockItemForm;
    }

    public void setActionRequestTypeId(String actionRequestTypeId) {
        this.actionRequestTypeId = actionRequestTypeId;
    }


    public String getActionRequestTypeId() {
        return actionRequestTypeId;
    }

    public void setStatuses(Collection statuses) {
        this.statuses = statuses;
    }


    public Collection getStatuses() {
        return statuses;
    }


    public void setRequestor(Person requestor) {
        this.requestor = requestor;
    }


    public Person getRequestor() {
        return requestor;
    }


    public void setEvaluator(Person evaluator) {
        this.evaluator = evaluator;
    }


    public Person getEvaluator() {
        return evaluator;
    }


    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }


    public String getVendorId() {
        return vendorId;
    }


    public void setVendors(Collection vendors) {
        this.vendors = vendors;
    }


    public Collection getVendors() {
        return vendors;
    }

    public String getSiId() {
        return siId;
    }

    public void setSiId(String siId) {
        this.siId = siId;
    }

    public void setActionRequestType(ActionRequestType actionRequestType) {
        this.actionRequestType = actionRequestType;
    }


    public ActionRequestType getActionRequestType() {
        return actionRequestType;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }


    public String getCmd() {
        return cmd;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }


    public Boolean getApproved() {
        return approved;
    }


    public void setEvaluationReason(String evaluationReason) {
        this.evaluationReason = evaluationReason;
    }


    public String getEvaluationReason() {
        return evaluationReason;
    }


    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }


    public Date getRequestedDate() {
        return requestedDate;
    }


    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }


    public StockItem getStockItem() {
        return stockItem;
    }


    public void setStkQtyChangeReasonRefId(String stkQtyChangeReasonRefId) {
        this.stkQtyChangeReasonRefId = stkQtyChangeReasonRefId;
    }


    public String getStkQtyChangeReasonRefId() {
        return stkQtyChangeReasonRefId;
    }


    public void setStkQtyChangeReasonRefs(Collection stkQtyChangeReasonRefs) {
        this.stkQtyChangeReasonRefs = stkQtyChangeReasonRefs;
    }


    public Collection getStkQtyChangeReasonRefs() {
        return stkQtyChangeReasonRefs;
    }


    public void setStkQtyChangeOrgBdgtId(String stkQtyChangeOrgBdgtId) {
        this.stkQtyChangeOrgBdgtId = stkQtyChangeOrgBdgtId;
    }


    public String getStkQtyChangeOrgBdgtId() {
        return stkQtyChangeOrgBdgtId;
    }


    public void setManufacturers(Collection manufacturers) {
        this.manufacturers = manufacturers;
    }


    public Collection getManufacturers() {
        return manufacturers;
    }


    public void setReset(boolean reset) {
        this.reset = reset;
    }


    public boolean isReset() {
        return reset;
    }
}