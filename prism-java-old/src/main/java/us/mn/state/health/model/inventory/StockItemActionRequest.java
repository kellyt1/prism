package us.mn.state.health.model.inventory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import us.mn.state.health.builder.email.EmailBuilder;
import us.mn.state.health.builder.email.StockItemHasBeenDiscontinuedEmailBuilder;
import us.mn.state.health.builder.email.StockItemHitReorderPointEmailBuilder;
import us.mn.state.health.common.exceptions.BusinessException;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.matmgmt.director.email.EmailDirector;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.util.BusinessRulesEngine;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.EmailBusinessDelegate;

public class StockItemActionRequest implements Serializable {
    private Date requestedDate;
    private String requestReason;
    private StockQtyChangeReasonRef qtyOnHandChangeReason;
    private OrgBudget qtyOnHandChangeOrgBudget;
    private Person requestor;
    private StockItem stockItem;
    private StockItem potentialStockItem;
    private String specialInstructions;
    private ActionRequestType actionRequestType;
    private Long stockItemActionRequestId;
    private Status status;
    private Vendor suggestedVendor;
    private String suggestedVendorName;
    private int version;
    private DAOFactory daoFactory = new HibernateDAOFactory();
    private boolean approvalRequired = true;
    private String insertedBy;
    private Date insertionDate = new Date();
    private String lastUpdatedBy;
    private Date lastUpdatedDate;
    private Boolean discardStock;
    private Collection<StockItemActionRequestEvaluation> requestEvaluations = new HashSet<StockItemActionRequestEvaluation>(); //an SIAR may require multiple evaluators
    private Collection<String> evaluatorGroupCodes = new HashSet<String>(); //a set of group codes (strings); filled by Rules4J
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

    public static final String REQUESTED_BY = "requestedBy";
    public static final String REQUEST_DATE = "requestDate";

    public void executeBusinessRules() throws InfrastructureException {
        BusinessRulesEngine bre = new BusinessRulesEngine();
        bre.applyStockItemModificationRules(this);
    }

    /**
     * prepare a StockItemActionRequest for evaluation. pseudocode:
     * 1) for each entry in evaluatorGroupCodes:
     * a) Create a StockItemActionRequestEvaluation object.
     * b) find the Group using the current groupCode from evaluatorGroupCodes
     * c) set the evaluatorGroup property of the StockItemActionRequestEvaluation object
     * d) set the evaluationDecision property of StockItemActionRequestEvaluation object to WFA
     * e) add the StockItemActionRequestEvaluation to the requestEvaluations collection
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void prepareForEvaluation() throws InfrastructureException {
        Status waitingForApproval = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM_ACTION_REQUEST,
                Status.WAITING_FOR_APPROVAL);
        this.setStatus(waitingForApproval);
        for (Object evaluatorGroupCode : evaluatorGroupCodes) {
            String groupCode = (String) evaluatorGroupCode;
            Group evalGroup = daoFactory.getGroupDAO().findGroupByCode(groupCode);
            if (evalGroup != null) {
                StockItemActionRequestEvaluation requestEval = new StockItemActionRequestEvaluation();
                requestEval.setEvaluatorGroup(evalGroup);
                requestEval.setEvaluationDecision(waitingForApproval);
                this.addRequestEvaluation(requestEval);
            }
        }
        evaluatorGroupCodes.clear();
        this.getPotentialStockItem().setPotentialIndicator(Boolean.TRUE);   //make sure we flag the stock item as 'not real'        
        //Set status to inactive on a New Stock Item
        if (actionRequestType.getCode().equals(ActionRequestType.NEW_STOCK_ITEM)) {
            Status inactive = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM,
                    Status.INACTIVE);
            this.getPotentialStockItem().setStatus(inactive);
        }
        this.save();
    }

    /* old version of prepareForEvaluation()
     * Prepare this request for evaluation.
     * The Rules Engine determined that this request must be approved, so we set
     * the status to "Waiting For Approval" and save it.
     *
     * @param statusCode
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
    public void prepareForEvaluation() throws InfrastructureException {
        Status waitingForApproval =
                (Status) daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM_ACTION_REQUEST,
                                                                                     Status.WAITING_FOR_APPROVAL);
        this.setStatus(waitingForApproval);
        this.getPotentialStockItem().setPotentialIndicator(Boolean.TRUE);   //make sure we flag the stock item as 'not real'
        
        //Set status as inactive on a New Stock Item
        if(actionRequestType.getCode().equals(ActionRequestType.NEW_STOCK_ITEM)) {
            Status inactive = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM, 
                                                                                          Status.INACTIVE);
            this.getPotentialStockItem().setStatus(inactive);
        }
        this.save();
    }
    */

    /**
     * Deny a Stock Item Action Request.
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void deny() throws InfrastructureException {
        Status deniedStatus =
                (Status) daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM_ACTION_REQUEST,
                        Status.DENIED);
        this.setStatus(deniedStatus);
        this.save();
    }

    /**
     * Approve a Stock Item Action Request.
     * Since the specific steps involved depend on the type of request, this method delegates
     * the actual approval logic to helper methods.
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void approve() throws InfrastructureException {
        Status approvedStatus =
                (Status) daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM_ACTION_REQUEST,
                        Status.APPROVED);
        this.setStatus(approvedStatus);

        try {
            if (this.actionRequestType.getCode().equalsIgnoreCase(ActionRequestType.NEW_STOCK_ITEM)) {
                approveNewStockItemRequest();
            } else if (this.actionRequestType.getCode().equalsIgnoreCase(ActionRequestType.STOCK_ITEM_MODIFICATIONS)) {
                approveStockItemModificationRequest();
            }
        }
        catch (BusinessException be) {
            be.printStackTrace();
        }
        this.save();
    }

    /**
     * approve a request for a new stock item.
     * Steps involved include:
     * 1) set the status of the action request to approved
     * 2) set the potential stock item 'potential indicator' to N
     * 3) set the status of the potential stock item to active
     * 4) make sure the potential stock item has a category, and assign an icnbr to it
     * 3) send notification to requestor and contacts
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    private void approveNewStockItemRequest() throws BusinessException, InfrastructureException {
        Status activeStatus =
                (Status) daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM,
                        Status.ACTIVE);
        this.getPotentialStockItem().setPotentialIndicator(Boolean.FALSE);
        this.getPotentialStockItem().setStatus(activeStatus);
        this.getPotentialStockItem().assignICNBR();
    }

    /**
     * approve a request to modify an existing stock item.  apply data from potential
     * stock item to stock item.
     * Steps involved include:
     * 1) set the status of siar to approved
     * 2) apply changes to original stock item (copy properties from potential to original)
     * 3) delete the potential stock item
     * 4) notify requestor and contacts (business rules engine handles this)
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    private void approveStockItemModificationRequest() throws InfrastructureException {
        createStockQtyAdjustmentHistory();
        notifyIfStockItemHitReorderPoint();
        notifyStockControllersIfStockItemIsDiscontinued();
        this.stockItem.copyProperties(this.potentialStockItem);
        this.stockItem.setPotentialIndicator(Boolean.FALSE);

        StockItem tempPotential = this.getPotentialStockItem();
        this.setPotentialStockItem(null);
        tempPotential.delete();
    }

    private void notifyStockControllersIfStockItemIsDiscontinued() {
        if (discontinuedStockItemEmailNeeded()) {
            try {
                EmailBean emailBean = new EmailBean();
                EmailBuilder emailBuilder = new StockItemHasBeenDiscontinuedEmailBuilder(this, emailBean);
                EmailDirector emailDirector = new EmailDirector(emailBuilder);
                emailDirector.construct();
                EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
                emailBusinessDelegate.sendEmail(emailBean);
            } catch (InfrastructureException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean discontinuedStockItemEmailNeeded() {
        boolean notActive = !getPotentialStockItem().getStatus().getStatusCode().equals(Status.ACTIVE);
        boolean differentStatuses = getPotentialStockItem().getStatus() != null && !getPotentialStockItem().getStatus().getStatusCode()
                .equals(getStockItem().getStatus().getStatusCode());
        boolean differentHoldUntilDates = getPotentialStockItem().getHoldUntilDate() != null
                && !getPotentialStockItem().getHoldUntilDate().equals(getStockItem().getHoldUntilDate());
        boolean differentDiscard = getPotentialStockItem().getFillUntilDepleted() != null && !getPotentialStockItem().getFillUntilDepleted().equals(getStockItem().getFillUntilDepleted());
        return notActive &&
                (differentStatuses || differentHoldUntilDates || differentDiscard);
    }

    private void createStockQtyAdjustmentHistory() {
        int oldQty = this.getStockItem().getQtyOnHand().intValue();
        int newQty = this.getPotentialStockItem().getQtyOnHand().intValue();
        if (oldQty != newQty && qtyOnHandChangeReason != null) {
            if (this.qtyOnHandChangeReason.getId() == null) this.qtyOnHandChangeReason.setId(new Long(1));
            StockQtyAdjustmentHistory sqah = new StockQtyAdjustmentHistory();
            sqah.setChangeDate(this.requestedDate);
            sqah.setChangedBy(this.getRequestor());
            sqah.setChangeReason(this.qtyOnHandChangeReason);
            sqah.setNewQtyOnHand(potentialStockItem.getQtyOnHand());
            sqah.setPreviousQtyOnHand(stockItem.getQtyOnHand());
            sqah.setOrgBudget(this.qtyOnHandChangeOrgBudget);
            sqah.setStockItem(stockItem);
            stockItem.getSiQtyAdjustments().add(sqah);
        }
    }

    /**
     * helper method to send an email to the appropriate parties if the
     * stock item has hit its re-order point.  If the potential Stock item's
     * QOH is less than the real stock item's QOH (i.e., user decreased the
     * QOH amount manually) AND if the new QOH value is less than the newest
     * ROP value, send an email to the appropriate parties.
     */
    private void notifyIfStockItemHitReorderPoint() {
        int oldQtyOnHand = stockItem.getQtyOnHand().intValue();
        int newQtyOnHand = potentialStockItem.getQtyOnHand().intValue();
        int newROP = potentialStockItem.getReorderPoint().intValue();
        try {
            if ((newQtyOnHand < oldQtyOnHand) &&
                    newQtyOnHand <= newROP &&
                    potentialStockItem.getStatus().getStatusCode().equalsIgnoreCase(Status.ACTIVE)) {
                EmailBean emailBean = new EmailBean();
                EmailBuilder emailBuilder = new StockItemHitReorderPointEmailBuilder(potentialStockItem, emailBean);
                EmailDirector emailDirector = new EmailDirector(emailBuilder);
                emailDirector.construct();
                EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
                emailBusinessDelegate.sendEmail(emailBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() throws InfrastructureException {
        daoFactory.getStockItemActionRequestDAO().makePersistent(this);
    }

    public void delete() throws InfrastructureException {
        if (this.stockItemActionRequestId != null) {
            daoFactory.getStockItemActionRequestDAO().makeTransient(this);
        }
    }

    /**
     * *********************   Getters/Setters *************************
     */
    public Boolean getDiscardStock() {
        return discardStock;
    }

    public void setDiscardStock(Boolean discardStock) {
        this.discardStock = discardStock;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public ActionRequestType getActionRequestType() {
        return actionRequestType;
    }

    public void setActionRequestType(ActionRequestType actionRequestType) {
        this.actionRequestType = actionRequestType;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public Person getRequestor() {
        return requestor;
    }

    public void setRequestor(Person requestor) {
        this.requestor = requestor;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStockItemActionRequestId(Long stockItemActionRequestId) {
        this.stockItemActionRequestId = stockItemActionRequestId;
    }

    public Long getStockItemActionRequestId() {
        return stockItemActionRequestId;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setPotentialStockItem(StockItem potentialStockItem) {
        this.potentialStockItem = potentialStockItem;
    }

    public StockItem getPotentialStockItem() {
        return potentialStockItem;
    }

    public void setSuggestedVendor(Vendor suggestedVendor) {
        this.suggestedVendor = suggestedVendor;
    }

    public Vendor getSuggestedVendor() {
        return suggestedVendor;
    }

    public void setSuggestedVendorName(String suggestedVendorName) {
        this.suggestedVendorName = suggestedVendorName;
    }

    public String getSuggestedVendorName() {
        return suggestedVendorName;
    }

    public void setDaoFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public void setApprovalRequired(boolean approvalRequired) {
        this.approvalRequired = approvalRequired;
    }

    public boolean getApprovalRequired() {
        return approvalRequired;
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

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }


    public void setRequestEvaluations(Collection<StockItemActionRequestEvaluation> requestEvaluations) {
        this.requestEvaluations = requestEvaluations;
    }


    public Collection<StockItemActionRequestEvaluation> getRequestEvaluations() {
        return requestEvaluations;
    }

    public void addRequestEvaluation(StockItemActionRequestEvaluation requestEvaluation) throws InfrastructureException {
        this.getRequestEvaluations().add(requestEvaluation);
        requestEvaluation.setStockItemActionRequest(this);
    }


    public void setEvaluatorGroupCodes(Collection<String> evaluatorGroupCodes) {
        this.evaluatorGroupCodes = evaluatorGroupCodes;
    }


    public Collection<String> getEvaluatorGroupCodes() {
        return evaluatorGroupCodes;
    }

    /**
     * this method is a 'helper' for Rules4J stuff... since Rules4J can't instantiate objects,
     * we resort to having it fill this collection with groupCodes, and then we'll create
     * StockItemActionRequestEvaluation objects, set the evaluatorGroup using the groupCodes found in this collection.
     */
    public void addEvaluatorGroup(String groupCode) {
        this.evaluatorGroupCodes.add(groupCode);
    }

    public void clearEvaluatorGroups() {
        this.evaluatorGroupCodes.clear();
    }


    public void setQtyOnHandChangeReason(StockQtyChangeReasonRef qtyOnHandChangeReason) {
        this.qtyOnHandChangeReason = qtyOnHandChangeReason;
    }


    public StockQtyChangeReasonRef getQtyOnHandChangeReason() {
        return qtyOnHandChangeReason;
    }


    public void setQtyOnHandChangeOrgBudget(OrgBudget qtyOnHandChangeOrgBudget) {
        this.qtyOnHandChangeOrgBudget = qtyOnHandChangeOrgBudget;
    }


    public OrgBudget getQtyOnHandChangeOrgBudget() {
        return qtyOnHandChangeOrgBudget;
    }
}
