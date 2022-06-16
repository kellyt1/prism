package us.mn.state.health.builder.materialsrequest;

import org.hibernate.Hibernate;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.*;
import us.mn.state.health.model.inventory.AttachedFile;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.materialsrequest.MaterialsRequestEvaluation;
import us.mn.state.health.model.materialsrequest.RequestLineItem;
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource;
import us.mn.state.health.model.materialsrequest.RequestLineItemNote;
import us.mn.state.health.model.util.HibernateModelDetacher;
import us.mn.state.health.model.util.ModelDetacher;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemFundingSourceForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemNoteForm;

import java.text.SimpleDateFormat;

public class RequestLineItemFormBuilder {
    private DAOFactory daoFactory;
    private RequestLineItem requestLineItem;
    private RequestLineItemForm requestLineItemForm;

    public RequestLineItemFormBuilder(RequestLineItemForm requestLineItemForm, DAOFactory daoFactory) {
        this.requestLineItemForm = requestLineItemForm;
        this.daoFactory = daoFactory;
    }

    public RequestLineItemFormBuilder(RequestLineItemForm requestLineItemForm, RequestLineItem requestLineItem, DAOFactory daoFactory) {
        this(requestLineItemForm, daoFactory);
        this.requestLineItem = requestLineItem;
    }

    public void buildCategories() throws InfrastructureException {
        Collection<Category> categories = daoFactory.getCategoryDAO().findCategoriesByUsedFor(Category.MATERIALS_FOR_REQUEST);
        requestLineItemForm.setCategories(categories);
        if (requestLineItem?.getItemCategory()) {
            requestLineItemForm.setCategoryId(requestLineItem.getItemCategory().getCategoryId().toString());
        }
    }

    // Change for PRIS-181 effects PRIS-159; need to be rewritten just coming from constructEditPurchasingRequestLineItemForm()
    public void buildCategoriesEdit() throws InfrastructureException {
        Collection<Category> categories = daoFactory.getCategoryDAO().findCategoriesByUsedForEdit(Category.MATERIALS_FOR_REQUEST);
        categories.addAll(daoFactory.getCategoryDAO().findCategoriesByUsedForEdit(Category.MATERIALS_COMPUTER_EQUIPMENT))
        requestLineItemForm.setCategories(categories);
        if (requestLineItem?.getItemCategory()) {
            requestLineItemForm.setCategoryId(requestLineItem.getItemCategory().getCategoryId().toString());
        }
    }

    public void buildCategoriesForITPurchases() throws InfrastructureException {
        Collection categories = daoFactory.getCategoryDAO().findDescendantCategoriesByParentCode(Category.MATERIALS_STANDARD_COMPUTERS, false);
        //todo - check this IT purchase category part
        categories.addAll(daoFactory.getCategoryDAO().findDescendantCategoriesByParentCode(Category.MATERIALS_STANDARD_COMPUTER_ACCESSORY, false));
        categories.addAll(daoFactory.getCategoryDAO().findDescendantCategoriesByParentCode(Category.MATERIALS_STANDARD_SOFTWARE, false));
        categories.addAll(daoFactory.getCategoryDAO().findDescendantCategoriesByParentCode(Category.MATERIALS_IT_PURCHASE_OTHER, true));
        requestLineItemForm.setCategories(categories);
        //all OTHER IT purchases will have category set to ITPUR_OTH

        if (requestLineItem != null && requestLineItem.getItemCategory() != null) {
            requestLineItemForm.setCategoryId(requestLineItem.getItemCategory().getCategoryId().toString());
        }
    }

    public void buildRequestLineItemNoteForms() throws InfrastructureException {
        if (requestLineItem) {
            Collection<RequestLineItemNote> rliNotes = requestLineItem.getNotes();
            Iterator iter = rliNotes.iterator();
            Collection<RequestLineItemNoteForm> rliNoteForms = new ArrayList();
            while (iter.hasNext()) {
                RequestLineItemNote rliNote = (RequestLineItemNote) iter.next();
                RequestLineItemNoteForm rliNoteForm = new RequestLineItemNoteForm();
                RequestLineItemNoteFormBuilder rliNoteFormBuilder =
                        new RequestLineItemNoteFormBuilder(rliNoteForm, rliNote, daoFactory);
                rliNoteFormBuilder.buildSimpleProperties();
                rliNoteForms.add(rliNoteForm);
            }
            requestLineItemForm.setNoteForms(rliNoteForms);
        }
    }

    public void addNewRequestLineItemNoteForm(String text, User author) throws InfrastructureException {
        RequestLineItemNoteForm theForm = new RequestLineItemNoteForm();
        theForm.setNoteText(text);
        theForm.setAuthorName(author.getFirstAndLastName());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        theForm.setInsertionDate(sdf.format(new Date()));
        requestLineItemForm.getNoteForms().add(theForm);
    }

    /**
     * This method is to be used when you are loading an existing request line item.
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildRequestLineItemFundingSourceForms(User budgetManager) throws InfrastructureException {
        if (requestLineItem != null) {
            Collection rliFundingSources = requestLineItem.getFundingSources();
            Iterator iter = rliFundingSources.iterator();
            if (!requestLineItem.getFundingSources().size()) {    //there weren't any existing funding sources, so add a 'blank' one...
                addNewRequestLineItemFundingSourceForm(false);
            }
            while (iter.hasNext()) {
                RequestLineItemFundingSource fundingSrc = (RequestLineItemFundingSource) iter.next();
                RequestLineItemFundingSourceForm fundingSrcForm = new RequestLineItemFundingSourceForm();
                RequestLineItemFundingSourceFormBuilder rliFundingSourceFrmBuilder =
                        new RequestLineItemFundingSourceFormBuilder(fundingSrcForm, fundingSrc, daoFactory);
                rliFundingSourceFrmBuilder.buildOrgBudgets(budgetManager);
                rliFundingSourceFrmBuilder.buildSelectedOrgBudget();
                rliFundingSourceFrmBuilder.buildSimpleProperties();
                requestLineItemForm.getFundingSourceForms().add(fundingSrcForm);
            }
        }
    }

    public void buildRequestLineItemFundingSourceForms() throws InfrastructureException {
        buildRequestLineItemFundingSourceForms(null);
    }

    /**
     * This method is to be used when the user wishes to add another funding source to the
     * request line item.
     *
     * @param append if true, append the new RLI Funding Source form to the
     *               existing collection.  if false, clear out the existing collection before adding the new one.
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void addNewRequestLineItemFundingSourceForm(boolean append, User budgetManager) throws InfrastructureException {
        if (!append) {
            requestLineItemForm.getFundingSourceForms().clear();
        }

        RequestLineItemFundingSourceForm theForm = new RequestLineItemFundingSourceForm();
        RequestLineItemFundingSourceFormBuilder rliFundingSrcFrmBuilder =
                new RequestLineItemFundingSourceFormBuilder(theForm, daoFactory);
        rliFundingSrcFrmBuilder.buildOrgBudgets(budgetManager); //if budgetManager is null, it'll build ALL org budgets
        rliFundingSrcFrmBuilder.buildSelectedOrgBudget();

        // ??
        rliFundingSrcFrmBuilder.buildOrgApproverMissing();

        requestLineItemForm.getFundingSourceForms().add(theForm);
    }

    public void addNewRequestLineItemFundingSourceForm(boolean append) throws InfrastructureException {
        addNewRequestLineItemFundingSourceForm(append, null);
    }

    /**
     * This method is to be used when you are creating a new RLI for a catalog item
     *
     * @param itemId
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildItem(Long itemId) throws InfrastructureException {
        ModelDetacher detacher = new HibernateModelDetacher();
        Item item = daoFactory.getItemDAO().getItemById(itemId);

        for (Object o : item.getAttachedFiles()) {
            Hibernate.initialize((AttachedFile) o);
        }

        detacher.detachItem(item);
        requestLineItemForm.setItem(item);
        requestLineItemForm.setCategoryId(item.getCategory().getCategoryId().toString());
    }

    /**
     * This method is to be used when you are loading an existing request line item.
     * If the RLI is for a catalog item, then this method will put the item reference into the form.
     * If the RLI is for a non-catalog item, then the item reference will be null, which is OK.
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildItem() throws InfrastructureException {
        if (requestLineItem?.getItem()) {
            Item item = daoFactory.getItemDAO().getItemById(requestLineItem.getItem().getItemId(), false);

            for (Object o : item.getAttachedFiles()) {
                Hibernate.initialize((AttachedFile) o);
            }
            Hibernate.initialize(item.getCategory().getCategoryId());
            if (item instanceof StockItem) {
                StockItem si = (StockItem) item;
            }
            // Set helper method
            if (item) {
                Collection<ItemVendor> cItemVendor = item.getItemVendors();
                for (ItemVendor itemVendor : cItemVendor) {
                    if (itemVendor.getVendor().getExternalOrgDetail() != null) {
                        requestLineItemForm.getVendorNames().add(itemVendor.getVendor().getExternalOrgDetail().getOrgName());
                        Collection<VendorContract> vcontract = itemVendor.getVendor().getVendorContracts();
                        for (VendorContract vendorContract : vcontract) {
                            requestLineItemForm.getVendorCatalogNbrs().add(vendorContract.getContractNumber());
                        }
                        if (itemVendor.getVendor().getExternalOrgDetail().getWebAddress() != null)
                            requestLineItemForm.getVendorURLs().add(itemVendor.getVendor().getExternalOrgDetail().getWebAddress());

                    }
                }
            }
            requestLineItemForm.setItem(item);

            requestLineItemForm.setCategoryId(item.getCategory().getCategoryId().toString());
        }
    }

    public void buildRequestLineItem() {
        requestLineItemForm.setRequestLineItem(requestLineItem);
    }

    public void buildSimpleProperties() throws InfrastructureException {
        try {
            if (requestLineItem) {
                PropertyUtils.copyProperties(requestLineItemForm, requestLineItem);
                if (requestLineItem.getStatus() != null) {
                    requestLineItemForm.setStatusId(requestLineItem.getStatus().getStatusId().toString());
                    requestLineItemForm.setStatusCode(requestLineItem.getStatus().getStatusCode());
                    requestLineItemForm.setStatusName(requestLineItem.getStatus().getName());
                }

                if (requestLineItem.getUnit() != null) {
                    requestLineItemForm.setUnitId(requestLineItem.getUnit().getUnitId().toString());
                }
                requestLineItemForm.setItPurchase(requestLineItem.determineIfItPurchase());
            }
        } catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building RequestLineItemForm: ", rpe);
        }
    }

    public void buildDefaultQtyPicked() throws InfrastructureException {
        if (!requestLineItemForm?.getQuantityPicked()) {
            int qtyRequested = Integer.parseInt(requestLineItemForm.getQuantity());
            int qtyReceived = 0;
            if (requestLineItemForm?.getQuantityFilled()) {
                qtyReceived = Integer.parseInt(requestLineItemForm.getQuantityFilled());
            }
            int tempQtyPicked = qtyRequested - qtyReceived;
            int qtyOnHand = 0;
            if (requestLineItemForm.getStockItem()) {
                qtyOnHand = requestLineItemForm.getStockItem().getQtyOnHand().intValue();
            }
            int returnVal = (tempQtyPicked > qtyOnHand ? qtyOnHand : tempQtyPicked);
            requestLineItemForm.setQuantityPicked(returnVal + "");
        }
    }

    public void buildPurchasingStatuses() throws InfrastructureException {
        String[] statusCodes = null;
        String[] statusCodesOrdered = [Status.ORDERED, Status.MNIT_ORDERED];
        String[] statusCodesApproval = [Status.WAITING_FOR_APPROVAL];
        String[] statusCodesPending = [Status.PENDING_BUILDING_ORDER, Status.PENDING_NEED_MORE_INFO, Status.PENDING_OUT_FOR_BID];
        String[] statusCodesWaitingForOrder = [Status.WAITING_FOR_PURCHASE,
                Status.FAILED_INCUMBRANCE,
                Status.CANCELLED_ITEM_DISCONTINUED,
                Status.PENDING_BUILDING_ORDER,
                Status.PENDING_OUT_FOR_BID,
                Status.PENDING_NEED_MORE_INFO,
                Status.COMPUTER_PURCHASE_ORDER, // ??
                Status.STANDING_LAB_ORDER];

        String[] statusCodesDelivered = [Status.DELIVERED, Status.DELIVERED_PARTIAL];
        String[] statusCodesReceived = [Status.RECEIVED, Status.RECEIVED_PARTIAL];
        String[] statusCodeDenied = [Status.DENIED];

        if (!requestLineItem) {
            statusCodes = statusCodesWaitingForOrder;
            requestLineItemForm.setStatusCode(Status.WAITING_FOR_PURCHASE);
        } else {
            String statusCode = requestLineItem.getStatus().getStatusCode();
            if (statusCode.equals(Status.ORDERED) || statusCode.equals(Status.MNIT_ORDERED)) {
                statusCodes = statusCodesOrdered;
            } else if (statusCode.equals(Status.WAITING_FOR_APPROVAL)) {
                statusCodes = statusCodesApproval;
            } else if (statusCode.equals(Status.DELIVERED) ||
                statusCode.equals(Status.DELIVERED_PARTIAL)) {
                statusCodes = statusCodesDelivered;
            } else if (statusCode.equals(Status.RECEIVED) || statusCode.equals(Status.RECEIVED_PARTIAL)) {
                statusCodes = statusCodesReceived;
            } else if (statusCode.equals(Status.DENIED)) {
                statusCodes = statusCodeDenied;
            } else {
                statusCodes = statusCodesWaitingForOrder;
            }
        }
        Collection statuses = daoFactory.getStatusDAO().findAllByStatusTypeAndStatusCodes(StatusType.MATERIALS_REQUEST, statusCodes);
        requestLineItemForm.setStatuses(statuses);
    }

    /* For Editor Role going into the page with 3 writable fields (Status, Funding Source, Add Note):

        if SLO status, then display 4 statuses [Canceled, COMPUTER_PURCHASE_ORDER, STANDING_LAB_ORDER, WAITING_FOR_PURCHASE].
        else display status drop down as read only.

        Ability to change Funding Source up until the order is in building order status.
     */
    public void buildPurchasingStatusesEditor() throws InfrastructureException {
        String[] statusCodes = null;
        String[] statusCodesOrdered = [Status.ORDERED];
        String[] statusCodesApproval = [Status.WAITING_FOR_APPROVAL];
        String[] statusCodesPending = [Status.PENDING_BUILDING_ORDER, Status.PENDING_NEED_MORE_INFO, Status.PENDING_OUT_FOR_BID];
        String[] statusCodesWaitingForOrder = [Status.WAITING_FOR_PURCHASE, Status.CANCELED, Status.COMPUTER_PURCHASE_ORDER, Status.STANDING_LAB_ORDER];

        String[] statusCodesDelivered = [Status.DELIVERED, Status.DELIVERED_PARTIAL];
        String[] statusCodesReceived = [Status.RECEIVED, Status.RECEIVED_PARTIAL];

        if (requestLineItem == null) {
            statusCodes = statusCodesWaitingForOrder;
            requestLineItemForm.setStatusCode(Status.WAITING_FOR_PURCHASE);
        } else {
            String statusCode = requestLineItem.getStatus().getStatusCode();
            if (statusCode.equals(Status.ORDERED)) {
                statusCodes = statusCodesOrdered;
            } else if (statusCode.equals(Status.WAITING_FOR_APPROVAL)) {
                statusCodes = statusCodesApproval;
            } else if (statusCode.equals(Status.DELIVERED) || statusCode.equals(Status.DELIVERED_PARTIAL)) {
                statusCodes = statusCodesDelivered;
            } else if (statusCode.equals(Status.RECEIVED) || statusCode.equals(Status.RECEIVED_PARTIAL)) {
                statusCodes = statusCodesReceived;
            } else {
                statusCodes = statusCodesWaitingForOrder;
            }
        }
        Collection statuses = daoFactory.getStatusDAO().findAllByStatusTypeAndStatusCodes(StatusType.MATERIALS_REQUEST, statusCodes);
        requestLineItemForm.setStatuses(statuses);
    }

    public void buildUnits() throws InfrastructureException {
        Collection units = daoFactory.getUnitDAO().findAll(false);
        requestLineItemForm.setUnits(units);
    }

    public void buildStatuses() throws InfrastructureException {
        Collection statuses = daoFactory.getStatusDAO().findAllByStatusTypeCode(StatusType.MATERIALS_REQUEST);
        requestLineItemForm.setStatuses(statuses);
    }

    public void buildStatusNameForViewMyRequests() {
        if ((requestLineItem.getStatus().getStatusCode().equals(Status.CANCELED)) || (
                requestLineItem.getStatus().getStatusCode().equals(Status.FULFILLED) && requestLineItem.getQuantityFilled() != null && requestLineItem.getQuantityFilled().intValue() == 0)) {
            requestLineItemForm.setStatusName(Status.CANCELED_NAME);
            requestLineItemForm.setStatusCode(Status.CANCELED);
        }
    }

    public void buildRequestEvaluationApprovalsPending(User evaluator) {
        requestLineItemForm.setLevelOneRequestEvaluationApprovalsPending(false);
        requestLineItemForm.setLevelTwoRequestEvaluationApprovalsPending(false);
        requestLineItemForm.setHasPendingLevelOneForCurrentEvaluator(false);
        requestLineItemForm.setHasPendingLevelTwoForCurrentEvaluator(false);

        boolean evaluatorIsInGroup = false;
        for (MaterialsRequestEvaluation mre : requestLineItem.getRequestEvaluations()) {

            evaluatorIsInGroup = evaluator.isInGroup(mre.getEvaluatorGroup().getGroupCode());

            // ??
            if (mre.getFirstStatus()) {
                requestLineItemForm.setEndRequestStatus(mre.getFirstStatus().getStatusCode());
            }

            if (mre.getApprovalLevel() != Constants.APPROVAL_LEVEL_TWO) {  //default to level One Approval if ApprovalLevel is not set(old records)
                if (!mre.getEvaluationDate()) {
                    requestLineItemForm.setLevelOneRequestEvaluationApprovalsPending(true);
                    if (evaluatorIsInGroup) {
                        requestLineItemForm.setHasPendingLevelOneForCurrentEvaluator(true);
                    }
                }
            } else {
                if (!mre.getEvaluationDate()) {
                    requestLineItemForm.setLevelTwoRequestEvaluationApprovalsPending(true);
                    if (evaluatorIsInGroup) {
                        requestLineItemForm.setHasPendingLevelTwoForCurrentEvaluator(true);
                    }
                }
            }
        }

    }
}