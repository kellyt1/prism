package us.mn.state.health.view.materialsrequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import us.mn.state.health.common.util.CollectionUtils;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Priority;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.materialsrequest.DeliveryDetail;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class RequestForm extends ValidatorForm {
    private String requestId;
    private Collection requestLineItemForms = new ArrayList();
    private Person requestor;
    private User evaluator;
    private Date dateRequested;
    private String needByDate;
    private String additionalInstructions;
    private String trackingNumber;
    private String priorityCode;
    private String priorityId;
    private Collection priorities = new HashSet();
    private String selectedRequestorId; //used in purchasing
    private Collection availableRequestors = new ArrayList(); //used in purchasing
    private RequestLineItemForm requestLineItemForm;
    private DeliveryDetail deliveryDetail;
    private Boolean defaultItemDeliveryDetail = false;
    private DeliveryDetailForm deliveryDetailForm;
    
    private Boolean defaultLocation = true;
    private Boolean selected = false;
    private Boolean showDetail = false;
    private String cmd = "";
    private String rliFormIndex;
    private String requestLineItemKey = "";
    private Boolean createReqLnItmFromCatalog = false;
    private String input = "";
    private String evaluatorsEvaluationStatus;

    private boolean isOwnerOfRequest = false;
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        cmd = "";
        createReqLnItmFromCatalog = false;
        defaultLocation = false;
        selected = false;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }
    
    /**
     * Sums up the estimated Costs from all the RequestLineItemForms
     * @return the sum of all rliForm.estimatedCosts
     */
    public Double getEstimatedCostTotal() {
        double sum = 0.0;
        for (Object o : getRequestLineItemForms()) {
            RequestLineItemForm rliForm = (RequestLineItemForm) o;
            sum += rliForm.getEstimatedCost();
        }
        return sum;
    }

    public Person getSelectedRequestor() {
        return (Person) CollectionUtils.getObjectFromCollectionById(availableRequestors, selectedRequestorId, "personId");
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public Priority getPriority() {
        return (Priority)CollectionUtils.getObjectFromCollectionById(priorities, priorityCode, "priorityCode");
    }
    
    public void addRequestLineItemForm(RequestLineItemForm rliForm) {
        this.requestLineItemForms.add(rliForm);
        rliForm.setRequestForm(this);
    }

    public void setRequestLineItemForms(Collection requestLineItemForms) {
        this.requestLineItemForms = requestLineItemForms;
    }

    public Collection getRequestLineItemForms() {
        return requestLineItemForms;
    }

    public void setRequestor(Person requestor) {
        this.requestor = requestor;
    }

    public Person getRequestor() {
        return requestor;
    }

    public void setDateRequested(Date dateRequested) {
        this.dateRequested = dateRequested;
    }
    
    public Date getDateRequested() {
        return dateRequested;
    }


    public void setNeedByDate(String needByDate) {
        this.needByDate = needByDate;
    }


    public String getNeedByDate() {
        return needByDate;
    }


    public void setAdditionalInstructions(String additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }


    public String getAdditionalInstructions() {
        return additionalInstructions;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }


    public void setRliFormIndex(String rliFormIndex) {
        this.rliFormIndex = rliFormIndex;
    }


    public String getRliFormIndex() {
        return rliFormIndex;
    }


    public void setPriorityId(String priorityId) {
        this.priorityId = priorityId;
    }


    public String getPriorityId() {
        return priorityId;
    }


    public void setPriorities(Collection priorities) {
        this.priorities = priorities;
    }


    public Collection getPriorities() {
        return priorities;
    }


    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }


    public String getTrackingNumber() {
        return trackingNumber;
    }


    public void setEvaluator(User evaluator) {
        this.evaluator = evaluator;
    }


    public User getEvaluator() {
        return evaluator;
    }

    public void setShowDetail(Boolean showDetail) {
        this.showDetail = showDetail;
    }


    public Boolean getShowDetail() {
        return showDetail;
    }


    public void setSelected(Boolean selected) {
        this.selected = selected;
    }


    public Boolean getSelected() {
        return selected;
    }
    
    public void setSelectedRequestorId(String selectedRequestorId) {
        this.selectedRequestorId = selectedRequestorId;
    }


    public String getSelectedRequestorId() {
        return selectedRequestorId;
    }


    public void setAvailableRequestors(Collection availableRequestors) {
        this.availableRequestors = availableRequestors;
    }


    public Collection getAvailableRequestors() {
        return availableRequestors;
    }


    public void setRequestLineItemKey(String requestLineItemKey) {
        this.requestLineItemKey = requestLineItemKey;
    }


    public String getRequestLineItemKey() {
        return requestLineItemKey;
    }


    public void setCreateReqLnItmFromCatalog(Boolean createReqLnItmFromCatalog) {
        this.createReqLnItmFromCatalog = createReqLnItmFromCatalog;
    }


    public Boolean getCreateReqLnItmFromCatalog() {
        return createReqLnItmFromCatalog;
    }


    public void setRequestLineItemForm(RequestLineItemForm requestLineItemForm) {
        this.requestLineItemForm = requestLineItemForm;
    }


    public RequestLineItemForm getRequestLineItemForm() {
        return requestLineItemForm;
    }


    public void setInput(String input) {
        this.input = input;
    }


    public String getInput() {
        return input;
    }


    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }


    public String getPriorityCode() {
        return priorityCode;
    }

    public boolean getHasItem() {
        return (requestLineItemForm != null && requestLineItemForm.getItem() != null);
    }

    public boolean getHasFundingSourceForm() {
        return (requestLineItemForm != null && requestLineItemForm.getFundingSourceForm() != null);
    }

    public boolean getHasFundingSources() {
        return requestLineItemForm == null || (requestLineItemForm.getFundingSourceForm() != null || requestLineItemForm.getFundingSourceForms().size() > 0);

    }

    public RequestLineItemFundingSourceForm getFundingSourceForm() {
        if (requestLineItemForm != null) {
            return requestLineItemForm.getFundingSourceForm();
        }
        return null;
    }

    /**
     * helper method - not really needed in this class but its here to match the Request class
     * 
     * @param val
     */
    public void setDeliverToInfoAsString(String val) {

    }

    /**
     * helper method to simplify JSPs and reports. 
     *  just returns deliveryDetail.getLongSummary() if deliveryDetail is not null.
     * otherwise returns blank string.
     * @return DeliverToInfoAsString
     */
    public String getDeliverToInfoAsString() {
        return (deliveryDetail != null) ? deliveryDetail.getLongSummary() : "";
    }


    /**
     * Helper method used by the validator to find out the number of the RLIForms for this RequestForm
     * @return RLI's Number
     */
    public int getRLIsNumber(){
        return requestLineItemForms.size();
    }


    public void setDeliveryDetail(DeliveryDetail deliveryDetail) {
        this.deliveryDetail = deliveryDetail;
    }


    public DeliveryDetail getDeliveryDetail() {
        return deliveryDetail;
    }


    public void setDeliveryDetailForm(DeliveryDetailForm deliveryDetailForm) {
        this.deliveryDetailForm = deliveryDetailForm;
    }


    public DeliveryDetailForm getDeliveryDetailForm() {
        return deliveryDetailForm;
    }

    /**
     * I build this helper method to avoid making the validator to throw unwanted
     *  Null pointer exceptions when we try to validate the mailingAddress
     * @return the mailingAddress of the deliveryDetail
     */
    public MailingAddress getMailingAddress(){
        return (deliveryDetail!=null) ? deliveryDetail.getMailingAddress() : null;
    }

    public boolean getIsOwnerOfRequest() {
        return isOwnerOfRequest;
    }

    public void setIsOwnerOfRequest(boolean ifOwnerOfRequest) {
        this.isOwnerOfRequest = ifOwnerOfRequest;
    }

    public String getEvaluatorsEvaluationStatus() {
        return evaluatorsEvaluationStatus;
    }

    public void setEvaluatorsEvaluationStatus(String evaluatorsEvaluationStatus) {
        this.evaluatorsEvaluationStatus = evaluatorsEvaluationStatus;
    }

    public Boolean getDefaultItemDeliveryDetail() {
        return defaultItemDeliveryDetail;
    }

    public void setDefaultItemDeliveryDetail(Boolean defaultItemDeliveryDetail) {
        this.defaultItemDeliveryDetail = defaultItemDeliveryDetail;
    }
}