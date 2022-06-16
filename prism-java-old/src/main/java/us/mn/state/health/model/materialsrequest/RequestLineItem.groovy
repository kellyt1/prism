package us.mn.state.health.model.materialsrequest;

import groovy.util.logging.Log4j;
import org.hibernate.search.annotations.*;
import us.mn.state.health.builder.email.ValidateHelpDeskTicketAddedEmailBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateEmployeeDAO;
import us.mn.state.health.dao.HibernateUserDAO
import us.mn.state.health.dao.configuration.HibernateConfigurationItemDAO;
import us.mn.state.health.matmgmt.director.email.EmailDirector;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.*;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.purchasing.HelpDeskTicketTracking;
import us.mn.state.health.model.purchasing.OrderLineItem
import us.mn.state.health.model.util.configuration.ConfigurationItem;
import us.mn.state.health.model.util.email.EmailBean;
import us.mn.state.health.model.util.email.EmailBusinessDelegate;
import us.mn.state.health.model.util.search.EntityIndex;
import us.mn.state.health.model.util.search.RequestLineItemIndex;
import us.mn.state.health.persistence.HibernateUtil

import javax.persistence.*;
import java.util.*;

@Log4j
@Entity
@Indexed(index = "requestLineItemIndex")
public class RequestLineItem extends MaterialsLineItem implements Comparable {
    @Id
    @DocumentId
    Long requestLineItemId;
    Boolean requestMakeCatalogItem;
    Status status;
    Set<String> evaluatorGroupCodes = new HashSet<>(); //a set of group codes (strings); filled by Rules4J
    Set<String> evaluatorGroupCodesLevelTwo = new HashSet<>(); //a set of group codes (strings); filled by Rules4J
    Set<MaterialsRequestEvaluation> requestEvaluations = new HashSet<>(); //an RLI may require multiple evaluators
    Set<AttachedFileNonCat> attachedFileNonCats = new HashSet<>();
    Request request;
    Set<RequestLineItemNote> notes = new HashSet<>();
    boolean approvalRequired = true;                //set by Rules4J - might not be needed but for now it is
    Boolean isForStockReorder = false;
    OrderLineItem orderLineItem;
    Integer quantityFilled = 0;       //the qty that has been received by the requestor
    Date dateDelivered;
    Person purchaser;
    Boolean isOnOrder;
    String denialReason;
    Date lastUpdated;
    String lastUpdatedBy;
    boolean itPurchase = false;
    String swiftItemId;

    public static final String REQUEST_ITEM_TYPE_STK = "Stock Item";
    public static final String REQUEST_ITEM_TYPE_PUR = "Purchase Item";
    public static final String REQUEST_ITEM_TYPE_NONCAT = "Non Catalog Item";


    public void save() throws InfrastructureException {
        daoFactory.getRequestLineItemDAO().makePersistent(this);
    }

    public void delete() throws InfrastructureException {
        this.request.removeRequestLineItem(this);
        if (this.requestLineItemId) {
            daoFactory.getRequestLineItemDAO().makeTransient(this);
            this.request.save();
        }
    }

    public void addNote(RequestLineItemNote rliNote) throws InfrastructureException {
        this.getNotes().add(rliNote);
        rliNote.setRequestLineItem(this);
    }

    public void addFundingSource(RequestLineItemFundingSource fundingSrc) throws InfrastructureException {
        this.getFundingSources().add(fundingSrc);
        fundingSrc.setRequestLineItem(this);
    }

    public void removeFundingSource(RequestLineItemFundingSource fundingSrc) throws InfrastructureException {
        if (this.getFundingSources().contains(fundingSrc)) {
            this.getFundingSources().remove(fundingSrc);
        }
        fundingSrc.delete();
    }

    public void addRequestEvaluation(MaterialsRequestEvaluation requestEvaluation) throws InfrastructureException {
        this.getRequestEvaluations().add(requestEvaluation);
        requestEvaluation.setRequestLineItem(this);
    }

    public void removeRequestEvaluation(MaterialsRequestEvaluation requestEvaluation) throws InfrastructureException {
        if (this.getRequestEvaluations().contains(requestEvaluation)) {
            this.getRequestEvaluations().remove(requestEvaluation);
        }
        requestEvaluation.setRequestLineItem(null);
        requestEvaluation.delete();
    }

    /**
     * @return true if status is WAITING_FOR_APPROVAL, false otherwise
     */
    boolean isWaitingForApproval() {
        status?.statusCode?.equals(Status.WAITING_FOR_APPROVAL)
    }

    /**
     * @return true if line item is in a book category, false otherwise
     */
    boolean isBook() {
        itemCategory?.categoryCode?.equals(Category.MATERIALS_BOOK)
    }

    /**
     * prepare a RequestLineItem for evaluation. pseudocode:
     * 1) for each entry in evaluatorGroupCodes:
     * a) Create a MaterialsRequestEvaluation object.
     * b) find the Group using the current groupCode from evaluatorGroupCodes
     * c) set the evaluatorGroup property of the MaterialsRequestEvaluation object
     * d) set the evaluationDecision property of MaterialsRequestEvaluation object to WFA
     * e) add the MaterialsRequestEvaluation to the requestEvaluations collection
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void prepareForEvaluation() throws InfrastructureException {
        Status waitingForApproval = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.WAITING_FOR_APPROVAL);
        this.setStatus(waitingForApproval);
        Iterator iter = evaluatorGroupCodes.iterator();
        while (iter.hasNext()) {
            String groupCode = (String) iter.next();
            Group evalGroup = daoFactory.getGroupDAO().findGroupByCode(groupCode);
            MaterialsRequestEvaluation requestEval = new MaterialsRequestEvaluation();
            requestEval.setEvaluatorGroup(evalGroup);
            requestEval.setEvaluationDecision(waitingForApproval);
            requestEval.setApprovalLevel(Constants.APPROVAL_LEVEL_ONE);
            this.addRequestEvaluation(requestEval);
        }
        evaluatorGroupCodes.clear();

        iter = evaluatorGroupCodesLevelTwo.iterator();
        while (iter.hasNext()) {
            String groupCode = (String) iter.next();
            Group evalGroup = daoFactory.getGroupDAO().findGroupByCode(groupCode);
            MaterialsRequestEvaluation requestEval = new MaterialsRequestEvaluation();
            requestEval.setEvaluatorGroup(evalGroup);
            requestEval.setEvaluationDecision(waitingForApproval);
            requestEval.setApprovalLevel(Constants.APPROVAL_LEVEL_TWO);
            this.addRequestEvaluation(requestEval);
        }
        evaluatorGroupCodesLevelTwo.clear();
    }

    public void approve(String approvalCode) throws InfrastructureException {
        Status waitingForDispersal = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.WAITING_FOR_DISPERSAL);
        Status approvalStatus;
        try{
            approvalStatus = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, approvalCode.toUpperCase());
        } catch (InfrastructureException e){
            approvalStatus = daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.WAITING_FOR_PURCHASE);
        }

        // PRIS-180 (too many ticket generated, just for CPO generate ticket; for non CPO, don't generate ticket)
        // In case if order changed to cpo and catalog is not set, as long as status is cpo, need ticket.
//        if (this.determineIfItPurchase() || approvalStatus.getStatusCode().equals(Status.COMPUTER_PURCHASE_ORDER) ) {     // "CPO",  62969
        if (approvalStatus.getStatusCode().equals(Status.COMPUTER_PURCHASE_ORDER) ) {     // "CPO",  62969
            Boolean onHand = this.getStatus().equals(waitingForDispersal);
            alterDeliverTo(request)
            this.setStatus(onHand ? daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.FULFILLED) : approvalStatus);
            // Move request Help Ticket to purchasing
            if (onHand) request.setHelpDeskticketId(createHelpDeskTicket(request,null, onHand));
        } else {
            this.setStatus((this.getItem() instanceof StockItem && !this.getIsForStockReorder()) ? waitingForDispersal : approvalStatus);
        }
    }

    // ?? begin test PRIS-178
    /**
     * If the RequestLineItem is for an ITPurchase, then generate a Help Desk Ticket.
     *
     * @param request
     * @param onHand  - true if already have this item(s)
     * @throws InfrastructureException
     */
    public Long createHelpDeskTicket(Request request,String purchaseOrder, Boolean onHand) throws InfrastructureException {
        Person person = request.requestor
        Date dateNeeded = request?.needByDate ?: DateUtils.addDaysToDate(new Date(), 7);
        Long ticketId
        User user = new HibernateUserDAO().findUserByUsername(person.getNdsUserId());
        Employee employee = new HibernateEmployeeDAO().getEmployeeByPersonId(person.getPersonId(), false);
        HelpDeskTicketTracking ticket = daoFactory.getHelpDeskTicketTrackingDAO().findExistingTicketUsingTrackingNumber(request.trackingNumber);
        StringBuilder ticketDesc = new StringBuilder();
        StringBuilder statusComments = new StringBuilder();

        if (ticket) { // no new ticket
            ticketId = ticket.trackingId
            if (item?.description && !ticket.description.contains(item.description)) {
                ticketDesc.append("<br><br>**********************************<br><br>");
                ticketDesc.append(onHand ? "<b>on-hand</b>, This item must be delivered."
                        : "Please <b>schedule the delivery and/or installation</b> of this item.")
                        .append("<br>")
                        .append(itemDescription ?: " ")
                        .append(item?.description ? "<br><b>Description: </b>" + item.description : "");
                ticket.status = "open"
            }
            if (itemDescription && !ticket.description.contains(itemDescription)) {
                ticketDesc.append("<br><br>**********************************<br><br>");
                ticketDesc.append(onHand ? "<b>on-hand</b>, This item must be delivered."
                        : "Please <b>schedule the delivery and/or installation</b> of this item.")
                        .append("<br>")
                        .append(itemDescription ?: " ")
                        .append(item?.description ? "<br><b>Description: </b>" + item.description : "");
                ticket.status = "open"
            }

            if (purchaseOrder && !ticket?.description?.contains("PO#: ${purchaseOrder}") ) {
                ticketDesc.append("<br>PO#: ${purchaseOrder}<br>")
            }


        } else {  // new ticket
            ticket = new HelpDeskTicketTracking()
            ticketId = ticket.trackingId

            ticketDesc.append("PARIT IT Purchase has been approved and is ");

            ticketDesc.append(onHand ? "<b>on-hand</b>, so now this item must be delivered."
                    : "now WAITING FOR PURCHASE, Please <b>schedule the delivery and/or installation</b> of this item.")
                    .append("<br><b>Tracking Number:</b> ${request.trackingNumber} ")
                    .append(itemDescription ?: " ")
                    .append(item?.description ? "<br><b>Description: </b>" + item.description : "")
                    .append("<br>PO#: ").append(purchaseOrder ?: " ")

            if(itemCategory.categoryId == 600003L || itemCategory.categoryId == 600004L){
                statusComments
                        .append("IT Purchasing notes<br>")
                        .append("Customer:<br>")
                        .append("Location:<br>")
                        .append("Division:<br>")
                        .append("CPRS#:<br>")
                        .append("Ordered date:<br>")
                        .append("Received (ITP) date:<br>")
                        .append("Handoff to SM date:<br>")
                        .append("Service Management notes:<br>")
                        .append("Description:<br>")
                        .append("Imaged:<br>")
                        .append("Communication to end user date(s):<br>")
                        .append("Arrive at GRB date (if applicable):<br>")
                        .append("Placed:")
            }

        }   // else new ticket

        ticketDesc.append(notes?.noteText?.join("<br>"))

        ticket.setCategory((item?.category?.name?.contains("Software")) ? "Desktop Software" : "Desktop Hardware");
        ticket.setSubCategory(item?.category?.name?.replace("'", "")?.replace("\"", ""));
        ticket.setPersonId(person.personId);
        ticket.setContactPhone(user?.workPhone ?: "");
        ticket.setContactEmail(person.emailAddressPrimary);
        ticket.setContactName(person.firstAndLastName);
        ticket.setDateNeeded(dateNeeded);
        ticket.setOrgDiv(employee?.division);
        ticket.setBuildingOffice(person?.primaryFacility?.facilityName?.substring(0, 3));
        if (ticket.description) {
            ticket.setDescription(ticket?.description?.concat(ticketDesc?.toString()?.trim()));
        } else {
            ticket.setDescription(ticketDesc?.toString()?.trim())
        }
        ticket.setTrackingNumber(request.trackingNumber);
        ticket.statusComments = ticket.statusComments?: statusComments.toString()
        HibernateUtil.getSession().saveOrUpdate(ticket);

        //Send Email right here for change
        sendEmail(user, ticket)

        return ticketId;
    }

    /**
     * Create helpdesk tickets for PARIT orders that need pricing or quotes updated.
     *
     * @param request
     * @return
     * @throws InfrastructureException
     */
    public Long createHelpDeskTicketNeedsQuote(Request request) throws InfrastructureException {
        Person person = request.getRequestor();
        Date dateNeeded = (request.getNeedByDate() != null) ? request.getNeedByDate() : DateUtils.addDaysToDate(new Date(), 7);
        String trackingNumber = request.getTrackingNumber();
        Long ticketId
        User user = new HibernateUserDAO().findUserByUsername(person.getNdsUserId());
        Employee employee = new HibernateEmployeeDAO().getEmployeeByPersonId(person.getPersonId(), false);
        HelpDeskTicketTracking ticket = daoFactory.getHelpDeskTicketTrackingDAO().findExistingTicketUsingTrackingNumber(trackingNumber);
        StringBuilder ticketDesc = new StringBuilder();

        if (ticket) {
            ticketId = ticket.getTrackingId();

            ticketDesc.append("<br>")
                    .append((itemDescription ? "<br><b>Description:</b> ${itemDescription}" : ""))
                    .append((item?.description) ? "<br><b>Description:</b> ${item?.description}" : "")
                    .append(notes?.noteText?.join("<br>"))
        } else {
            ticket = new HelpDeskTicketTracking();
            ticketId = ticket.trackingId;

            ticketDesc.append("<b>${trackingNumber}</b><br/>")
                    .append("PARIT IT Purchase line item ${requestLineItemId} needs a quote attached. Please attach a current quote and update pricing.<br/>")
                    .append(itemDescription ?: " ")
                    .append(item?.description ? "<br><b>Description: </b>" + item.description : "")
                    .append(notes?.noteText?.join("<br>"))
        }

        ticket.setCategory((item?.category?.name?.contains("Software")) ? "Desktop Software" : "Desktop Hardware");
        ticket.setSubCategory(item?.category?.name?.replace("'", "")?.replace("\"", ""));
        ticket.setPersonId(person.personId);
        ticket.setContactPhone(user?.workPhone ?: "");
        ticket.setContactEmail(person.emailAddressPrimary);
        ticket.setContactName(person.firstAndLastName);
        ticket.setDateNeeded(dateNeeded);
        ticket.setOrgDiv(employee?.division);
        ticket.setBuildingOffice(person?.primaryFacility?.facilityName?.substring(0, 3));
        if (ticket.description) {
            ticket.setDescription(ticket?.description?.concat(ticketDesc?.toString()?.trim()))
        } else {
            ticket.setDescription(ticketDesc?.toString()?.trim())
        }
        ticket.setTrackingNumber(request.trackingNumber);

        HibernateUtil.getSession().saveOrUpdate(ticket);

        //Send Email right here for change
        sendEmail(user, ticket)

        return ticketId;
    }

    private void sendEmail(User user, HelpDeskTicketTracking ticket){
        try {
            EmailBean emailBean = new EmailBean();

            ValidateHelpDeskTicketAddedEmailBuilder va = new ValidateHelpDeskTicketAddedEmailBuilder(request, emailBean, daoFactory);
            va.setEmailAddress(user.emailAddress ?: "");
            va.setTrackingID(ticket.trackingId.toString());

            EmailDirector emailDirector = new EmailDirector(va);
            emailDirector.construct();
            EmailBusinessDelegate emailBusinessDelegate = new EmailBusinessDelegate();
            emailBusinessDelegate.sendEmail(emailBean);
        } catch (Exception ignore) {
        }
    }

    /**
     * this method is a 'helper' for Rules4J stuff... since Rules4J can't instantiate objects,
     * we resort to having it fill this collection with groupCodes, and then we'll create
     * MaterialsRequestEvaluation objects, set the evaluatorGroup using the groupCodes found in this collection.
     */
    public void addEvaluatorGroup(String groupCode) {
        this.evaluatorGroupCodes.add(groupCode);
    }

    public void clearEvaluatorGroups() {
        this.evaluatorGroupCodes.clear();
    }

    /**
     * A Searchable method this class must implement.  Returns a RequestLineItemIndex
     *
     * @return EntityIndex
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public EntityIndex getEntityIndex() throws InfrastructureException {
        return new RequestLineItemIndex();
    }

    public String toString() {
        StringBuilder theString = new StringBuilder();
        theString.append("requestLineItemId: ").append(this.getRequestLineItemId()).append(" ")
                .append("requestMakeCatalogItem: ").append(this.getRequestMakeCatalogItem()).append(" ");
        if (this.status) {
            theString.append("status: ").append(this.getStatus().getName()).append(" ");
        }
        if (this.item) {
            theString.append("item: ").append(this.getItem().getItemId()).append(", ").append(this.getItem().getDescription()).append(" ");
        } else {
            theString.append("non-catalog item desc: ").append(this.getItemDescription()).append(" ")
                    .append("non-catalog itemCost: ").append(this.getItemCost()).append(" ")
                    .append("non-catalog itemHazardous: ").append(this.getItemHazardous()).append(" ");
        }
        theString.append("quantity: ").append(this.getQuantity()).append(" ")
                .append("suggestedVendorURL: ").append(this.suggestedVendorURL).append(" ")
                .append("suggestedVendorCatalogNumber: ").append(this.suggestedVendorCatalogNumber).append(" ")
                .append("suggestedVendorName: ").append(this.suggestedVendorName).append(" ");

        if (fundingSources != null && fundingSources.size() > 0) {
            Iterator iter = fundingSources.iterator();
            String amtUnit = "";
            if (this.amountInDollars != null) {
                amtUnit = this.amountInDollars ? "dollars" : "percent";
            }
            theString.append("Funding Sources (amounts are in ").append(amtUnit).append("): ");
            while (iter.hasNext()) {
                RequestLineItemFundingSource src = (RequestLineItemFundingSource) iter.next();
                theString.append("budget: ").append(src.getOrgBudget().getOrgBudgetCodeAndName()).append(", amount: ").append(src.getAmount().doubleValue()).append(" ");
            }
        }
        if (requestEvaluations != null && requestEvaluations.size() > 0) {
            Iterator iter = requestEvaluations.iterator();
            theString.append("MaterialsRequestEvaluations: ");
            while (iter.hasNext()) {
                MaterialsRequestEvaluation reqeval = (MaterialsRequestEvaluation) iter.next();
                theString.append("\t").append(reqeval.toString());
            }
        }

        return theString.toString();
    }

    public String toHtmlString() {
        StringBuilder theString = new StringBuilder();
        theString.append("requestLineItemId: ").append(this.getRequestLineItemId()).append("<br />")
                .append("requestMakeCatalogItem: ").append(this.getRequestMakeCatalogItem()).append("<br />");
        if (this.status) theString.append("status: ").append(this.getStatus().getName()).append("<br />");
        else {
            theString.append("status: null");
        }
        if (this.item)
            theString.append("item: ").append(this.getItem().getItemId()).append("<br />")
                    .append("item description: ").append(this.getItem().getDescription()).append("<br />")
                    .append("item dispenseUnitCost: ").append(this.getItem().getDispenseUnitCost()).append("<br />");
        else {
            theString.append("non-catalog item desc: ").append(this.getItemDescription()).append("<br />")
                    .append("non-catalog itemCost: ").append(this.getItemCost()).append("<br />")
                    .append("non-catalog itemHazardous: ").append(this.getItemHazardous()).append("<br />");
        }
        theString.append("quantity: ").append(this.getQuantity()).append("<br />")
                .append("estimatedCost: ").append(this.getEstimatedCost()).append("<br />")
                .append("suggestedVendorURL: ").append(this.suggestedVendorURL).append("<br />")
                .append("suggestedVendorCatalogNumber: ").append(this.suggestedVendorCatalogNumber).append("<br />")
                .append("suggestedVendorName: ").append(this.suggestedVendorName).append("<br />");

        if (fundingSources?.size() > 0) {
            Iterator iter = fundingSources.iterator();
            String amtUnit = "";
            if (this.amountInDollars) {
                amtUnit = this.amountInDollars ? "dollars" : "percent";
            }
            theString.append("Funding Sources (amounts are in ").append(amtUnit).append("):<br />");
            while (iter.hasNext()) {
                RequestLineItemFundingSource src = (RequestLineItemFundingSource) iter.next();
                theString.append("budget: ").append(src.getOrgBudget().getOrgBudgetCodeAndName()).append(", amount: ").append(src.getAmount().doubleValue()).append("<br />");
            }
        }

        return theString.toString();
    }


    /**
     * ************************** getters/setters ************************************************************
     */
    public Boolean getIsOnOrder() {
        return isOnOrder;
    }

    public void setRequestLineItemId(Long requestLineItemId) {
        this.requestLineItemId = requestLineItemId;
    }

    public Long getRequestLineItemId() {
        return requestLineItemId;
    }

    public void setRequestMakeCatalogItem(Boolean requestMakeCatalogItem) {
        this.requestMakeCatalogItem = requestMakeCatalogItem;
    }

    public Boolean getRequestMakeCatalogItem() {
        return requestMakeCatalogItem;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setEvaluatorGroupCodes(Collection evaluatorGroupCodes) {
        this.evaluatorGroupCodes = evaluatorGroupCodes;
    }

    public Collection getEvaluatorGroupCodes() {
        return evaluatorGroupCodes;
    }

    public Collection getEvaluatorGroupCodesLevelTwo() {
        return evaluatorGroupCodesLevelTwo;
    }

    public void setEvaluatorGroupCodesLevelTwo(Collection evaluatorGroupCodesLevelTwo) {
        this.evaluatorGroupCodesLevelTwo = evaluatorGroupCodesLevelTwo;
    }

    public void setRequestEvaluations(Collection<MaterialsRequestEvaluation> requestEvaluations) {
        this.requestEvaluations = requestEvaluations;
    }

    public Collection<MaterialsRequestEvaluation> getRequestEvaluations() {
        return requestEvaluations;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    @OneToOne(cascade = [CascadeType.PERSIST])
    @IndexedEmbedded
    public Request getRequest() {
        return request;
    }

    public void setNotes(Collection<RequestLineItemNote> notes) {
        this.notes = notes;
    }

    public Collection<RequestLineItemNote> getNotes() {
        return notes;
    }

    public void setApprovalRequired(boolean approvalRequired) {
        this.approvalRequired = approvalRequired;
    }

    public boolean getApprovalRequired() {
        return approvalRequired;
    }

    public Double getEstimatedCost() {
        if (!item && itemCost && quantity) {
            return this.getItemCost() * this.getQuantity();
        } else if (item != null && item.getDispenseUnitCost() != null && quantity != null) {
            return (item.getDispenseUnitCost() * this.getQuantity());
        } else {
            return 0.0;
        }
    }

    public OrderLineItem getOrderLineItem() {
        return orderLineItem;
    }

    public void setOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItem = orderLineItem;
    }

    public String getRequestItemType() {
        if (item)
            return (item instanceof StockItem) ? REQUEST_ITEM_TYPE_STK : REQUEST_ITEM_TYPE_PUR;
        else
            return REQUEST_ITEM_TYPE_NONCAT;
    }


    public void setQuantityFilled(Integer qtyFilled) {
        this.quantityFilled = (qtyFilled != null) ? qtyFilled : 0;
    }

    public Integer getQuantityFilled() {
        return quantityFilled;
    }

    public Date getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(Date dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public void setPurchaser(Person purchaser) {
        this.purchaser = purchaser;
    }

    public Person getPurchaser() {
        return purchaser;
    }

    public ReqLnItemFundingSourcesSummary getFundingSrcSummary() {
        return new ReqLnItemFundingSourcesSummary(fundingSources);
    }

    public Collection getFundingSourcesList() {
        return new ArrayList(fundingSources);
    }

    public void setIsForStockReorder(Boolean isForStockReorder) {
        this.isForStockReorder = isForStockReorder;
    }

    public Boolean getIsForStockReorder() {
        return isForStockReorder;
    }

    public boolean getItemIsStockItem() {
        return this.getItem() != null && this.getItem().getItemType().equals(Item.STOCK_ITEM);
    }

    public String getFullIcnbr() {
        Item item = this.getItem();
        try {
            if (item?.itemType?.equals(Item.STOCK_ITEM)) {
                return (DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getStockItemDAO().getStockItemById(item.getItemId(), false)).getFullIcnbr() + "\n";
            }
        } catch (InfrastructureException e) {
            log.error(e);
        }
        return "";
    }

    public ItemVendor getPrimaryItemVendor() {
        if (this.getItem() != null) {
            for (Object o : getItem().getItemVendors()) {
                if (((ItemVendor) o).getPrimaryVendor()) {
                    return (ItemVendor) o;
                }
            }
        }
        return null;
    }
//    =================================================================
//    TODO Temporary(will be refactored later) index fields

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String getConcatenatedContent() {
        String itemCat = getItemCategory()?.getCategoryCode() ?: getItem()?.getCategory()?.getCategoryCode()
        return "${status.name} ${orgBdgtNames} ${orgBdgtCodes} ${itemDescription_} ${itemCat} ${vendorNames} ${itemIcnbr} ${requestTrackingNumber} ${swiftId}";
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String getRequestTrackingNumber() {
        return getRequest().getTrackingNumber() + " ";
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String getVendorNames() {
        StringBuilder vendorNamesBuffer = new StringBuilder();
        if (item) {
            for (Object itemVendor : getItem().getItemVendors()) {
                Vendor vendor = ((ItemVendor) itemVendor).getVendor();
                try {
                    vendor = daoFactory.getVendorDAO().getVendorById(vendor.getVendorId(), false);
                    vendorNamesBuffer.append(vendor.getExternalOrgDetail().getOrgName()).append(" ");
                } catch (InfrastructureException e) {
                    log.error(e);
                }
            }
        }
        return vendorNamesBuffer.toString() + getSuggestedVendorName() + " ";
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String getVendorIDs() {
        StringBuilder vendorIDsBuffer = new StringBuilder();
        if (item) {
            for (Object itemVendor : getItem().getItemVendors()) {
                Vendor vendor = ((ItemVendor) itemVendor).getVendor();
                try {
                    vendor = daoFactory.getVendorDAO().getVendorById(vendor.getVendorId(), false);
                    vendorIDsBuffer.append(vendor.getVendorId().toString()).append(" ");
                } catch (InfrastructureException e) {
                    log.error(e);
                }
            }
        }
        return vendorIDsBuffer.toString();
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    public String getOrgBdgtCodes() {
        StringBuilder orgBdgtCodes = new StringBuilder();
        for (Object rliFS : getFundingSources()) {
            OrgBudget orgBdgt = ((RequestLineItemFundingSource) rliFS).getOrgBudget();
            try {
                orgBdgt = daoFactory.getOrgBudgetDAO().getOrgBudgetById(orgBdgt.getOrgBudgetId(), false);
                if(orgBdgt.getEffectiveDate().before(new Date(1435726800000l))){
                    orgBdgtCodes.append(orgBdgt.getOrgBudgetCode()).append(" ");
                }else{
                    orgBdgtCodes.append(orgBdgt.getDeptId().substring(orgBdgt.getDeptId().length() - 4)).append(" ");
                }
            } catch (InfrastructureException e) {
                log.error(e);
            }
        }
        return orgBdgtCodes.toString() + " ";
    }

    private String getOrgBdgtNames() {
        StringBuilder orgBdgtIds = new StringBuilder();
        for (Object rliFS : getFundingSources()) {
            OrgBudget orgBdgt = ((RequestLineItemFundingSource) rliFS).getOrgBudget();
            try {
                orgBdgt = daoFactory.getOrgBudgetDAO().getOrgBudgetById(orgBdgt.getOrgBudgetId(), false);
                orgBdgtIds.append(orgBdgt.getOrgBudgetId().toString()).append(" ");
            } catch (InfrastructureException e) {
                log.error(e);
            }
        }
        return orgBdgtIds.toString() + " ";
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String getItemModel() {
        return getItem()?.getModel()
    }

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution = Resolution.DAY)
    private Date getDateRequested() {
        return getRequest()?.getDateRequested();
    }

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution = Resolution.DAY)
    private Date getNeedByDate() {
        return getRequest()?.getNeedByDate();
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String getItemCategoryCode() {
        return getItemCategory()?.getCategoryCode() ?: getItem().getCategory().getCategoryCode()
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String getPriorityCode() {
        return getRequest()?.getPriority()?.getPriorityCode()
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String getPurchaserId() {
        return getPurchaser()?.getPersonId()?.toString()
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String getRequestorId() {
        return getRequest()?.getRequestor()?.getPersonId()?.toString()
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String getRequestId() {
        return getRequest()?.getRequestId()?.toString()
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String getRliStatusCode() {
        return getStatus()?.getStatusCode()
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String getItemIcnbr() {
        if (item && StockItem.class.isAssignableFrom(item.getClass())) {
            return ((StockItem) getItem()).getFullIcnbr()
        }
        return null
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String getItemDescription_() {
        return item?.description ?: itemDescription
    }

    @Field(index = Index.TOKENIZED, store = Store.YES)
    public String getSwiftId() {
        return getSwiftItemId() ?: ""
    }

    public String getDenialReason() {
        return denialReason;
    }

    public void setDenialReason(String denialReason) {
        this.denialReason = denialReason;
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

    @Transient
    public Collection<AttachedFileNonCat> getAttachedFileNonCats() {
        return this.attachedFileNonCats;
    }

    public void setAttachedFileNonCats(Collection<AttachedFileNonCat> attachedFileNonCats) {
        this.attachedFileNonCats = attachedFileNonCats;
    }

    public void addAttachedFileNonCat(AttachedFileNonCat attachedFileNonCat) {
        attachedFileNonCats.add(attachedFileNonCat);
    }

    public boolean levelOneRequestEvaluationsStillNeedingApproval() {
        if (this.requestEvaluations != null) {
            for (RequestEvaluation requestEvaluation : requestEvaluations) {
                if (!requestEvaluation.getEvaluationDate() && requestEvaluation.getApprovalLevel() == 1) return true;
            }
        }
        return false;
    }

    public boolean isItPurchase() {
        return itPurchase;
    }

    public void setItPurchase(boolean itPurchase) {
        this.itPurchase = itPurchase;
    }

    public boolean determineIfItPurchase() {
        itPurchase = false
        if (item?.category?.getUsedFor()?.equalsIgnoreCase(Constants.ITPurchase)){
            itPurchase = true
        } else if (itemCategory) {
            //Added OR logic to catch missed IT purchase items entered in PRISM instead of PARIT.
            if (this.getItemCategory().getUsedFor().equalsIgnoreCase(Constants.ITPurchase) || this.getItemCategory().getUsedFor().equalsIgnoreCase(Constants.ITPurchase2))
                itPurchase = true
        }
        return itPurchase
    }

    public int compareTo(Object o) {
        if (this instanceof RequestLineItem) {
            RequestLineItem that = (RequestLineItem) o;
            if (this.getRequestLineItemId() && that.getRequestLineItemId()) {
                return this.getRequestLineItemId().compareTo(that.getRequestLineItemId());
            }
            return this.toString().compareTo(that.toString());
        }
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof RequestLineItem)) return false;

        final RequestLineItem that = (RequestLineItem) o;

        if (this.getRequestLineItemId() == null) {
            if (that.getRequestLineItemId() == null) {
                //dig deeper, using comparison by value
                if (this.getRequest() != null && !this.getRequest().equals(that.getRequest())) {
                    return false;
                }
                if (!item ^ !that.getItem()) {
                    return false;
                }
                if (item && that.getItem()) {
                    if (!item.getItemId().equals(that.getItem().getItemId())) {
                        return false;
                    }
                }
                //todo tr - request line items for a new request can not have the same description because of this equals() computation
                if (itemDescription != null ? !itemDescription.equals(that.getItemDescription()) : that.getItemDescription() != null) {
                    return false;
                }

                return true;
            } else {
                //if one ID is null, and the other is not null, they can't be the same
                return false;
            }
        } else {  //we know we can't get a NullPointerException now...
            return this.getRequestLineItemId().equals(that.getRequestLineItemId());
        }
    }

    /**
     * Never use the ID's in hashCode()!
     *
     * @return hashCode
     */
    public int hashCode() {
        int result = 13;
        result += 29 * (int)(getRequest()?.hashCode() ?: 0)
        return result;
    }

    private void alterDeliverTo(Request request) throws Exception {
        HibernateConfigurationItemDAO hcidao = new HibernateConfigurationItemDAO();
        ConfigurationItem aconfig = hcidao.getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.PARIT_COMPUTER_SHIP_TO);

        if (aconfig && !request?.additionalInstructions?.contains(" Original Deliver to: ")) {
            Person person = daoFactory.getPersonDAO().getPersonById(Long.valueOf(aconfig.getValue()), false);
            Facility facility = person.getPrimaryFacility();
            MailingAddress mailingAddress = daoFactory.getFacilityDAO().getFacilityById(facility.getFacilityId(),false).getBuildingMailingAddress();
            /* IF you found a person and facility then change it */
            if (person && facility) {
                /** Take Current delivery to and put in notes */
                request.setAdditionalInstructions((request.getAdditionalInstructions() ?: "").concat(" Original Deliver to: ").concat(" ").concat(request.getDeliveryDetail().getRecipientName()));
                if (request.getDeliveryDetail().getFacility() != null) {
                    request.setAdditionalInstructions(request.getAdditionalInstructions().concat(" ").concat(request.getDeliveryDetail().getFacility().getFacilityName()));
                }
                if (request.getDeliveryDetail().getMailingAddress() != null)
                    request.setAdditionalInstructions(request.getAdditionalInstructions().concat(" ").concat(request.getDeliveryDetail().getMailingAddress().getCityAndAddress()));


                DeliveryDetail deliveryDetail = new DeliveryDetail();
                deliveryDetail.setFacility(facility);
                deliveryDetail.setRecipient(person);
                deliveryDetail.setMailingAddress(mailingAddress);

                request.setDeliveryDetail(deliveryDetail);
            }
        }
    }
}