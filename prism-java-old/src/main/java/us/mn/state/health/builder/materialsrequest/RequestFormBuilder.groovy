package us.mn.state.health.builder.materialsrequest

import org.apache.commons.collections.CollectionUtils
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.hibernate.Hibernate
import us.mn.state.health.common.exceptions.InfrastructureException
import us.mn.state.health.common.exceptions.ReflectivePropertyException
import us.mn.state.health.common.lang.PropertyUtils
import us.mn.state.health.dao.DAOFactory
import us.mn.state.health.matmgmt.director.DeliveryDetailDirector
import us.mn.state.health.matmgmt.director.RequestLineItemFormDirector
import us.mn.state.health.matmgmt.util.Constants
import us.mn.state.health.model.common.*
import us.mn.state.health.model.inventory.Item
import us.mn.state.health.model.inventory.ItemVendor
import us.mn.state.health.model.inventory.StockItem
import us.mn.state.health.model.materialsrequest.*
import us.mn.state.health.view.materialsrequest.DeliveryDetailForm
import us.mn.state.health.view.materialsrequest.RequestForm
import us.mn.state.health.view.materialsrequest.RequestLineItemForm

public class RequestFormBuilder {
    private static Log log = LogFactory.getLog(RequestFormBuilder.class);
    private DAOFactory daoFactory;
    private Request request;
    private RequestForm requestForm;
    private User user;             //this is needed because system should only display RLI's which the user may evaluate
    //Also used for address book owner, ugly but necessary
    private int requestLineItemPurpose = -1;
    public static final int FOR_EDIT_RLIF = 0;
    public static final int FOR_EVALUATE_RLIF = 1;
    public static final int FOR_STOCK_PICK_RLIF = 2;
    public static final int FOR_PURCHASING = 3;
    public static final int FOR_VIEW_MY_REQUESTS = 4;
    public static final int FOR_IT_PURCHASES = 5;


    public RequestFormBuilder(RequestForm requestForm, DAOFactory daoFactory) {
        this.requestForm = requestForm;
        this.daoFactory = daoFactory;
    }

    public RequestFormBuilder(RequestForm requestForm, DAOFactory daoFactory, Request request) {
        this(requestForm, daoFactory);
        this.request = request;
    }

    public RequestFormBuilder(RequestForm requestForm, DAOFactory daoFactory, Request request, int requestLineItemPurpose) {
        this(requestForm, daoFactory, request);
        this.requestLineItemPurpose = requestLineItemPurpose;
    }

    public RequestFormBuilder(RequestForm requestForm, DAOFactory daoFactory, Request request, User user) {
        this(requestForm, daoFactory, request);
        this.user = user;
    }

    public void buildDefaultProperties() {
        requestForm.setAdditionalInstructions(null);
        requestForm.setCmd("");
        requestForm.setCreateReqLnItmFromCatalog(false);
        requestForm.setDateRequested(new Date());
        requestForm.setEvaluator(null);
        requestForm.setInput("purchasingRequest");
        requestForm.setNeedByDate(null);
        requestForm.setPriorityCode(Priority.NORMAL);
        requestForm.setPriorityId(null);
        requestForm.setRequestId(null);
        requestForm.setRequestLineItemForm(null);
        requestForm.setRequestLineItemForms(new ArrayList());
        requestForm.setRequestLineItemKey("");
        requestForm.setSelected(false);
        requestForm.setSelectedRequestorId(null);
        requestForm.setShowDetail(false);
        requestForm.setTrackingNumber(null);
    }

    public void buildDeliveryDetail() throws InfrastructureException {
        //check if a catalog item with a default delivery detail has been defined, and set form delivery detail if so
        DeliveryDetail defaultItemDeliveryDetail = requestLineItemDefaultDeliveryDetail(request)
        if (defaultItemDeliveryDetail) {
            requestForm.setDeliveryDetail(defaultItemDeliveryDetail)
            requestForm.setDefaultItemDeliveryDetail(true)
        } else {
            requestForm.setDefaultItemDeliveryDetail(false)
            if (request != null && request.getDeliveryDetail() != null) {
                requestForm.setDeliveryDetail(request.getDeliveryDetail());
            } else if ((request == null || (request.getRequestId() == null)) && requestForm.getDeliveryDetail() == null) {
                if (user != null) {
                    //if the request is either null or has not yet been persisted (and therefore has no ID),
                    //set up a default deliver To based on the logged in user.
                    log.debug("buildDeliveryDetail() - new request, so setting up a default DeliveryDetail based on the logged in user.");
                    DeliveryDetailForm deliveryDetailForm = new DeliveryDetailForm();
                    DeliveryDetail deliveryDetail = new DeliveryDetail();
                    DeliveryDetailBuilder builder = new DeliveryDetailBuilder(deliveryDetailForm, deliveryDetail, user, daoFactory);
                    DeliveryDetailDirector director = new DeliveryDetailDirector(builder);
                    if (hasSwiftItems())
                        director.constructStock();
                    else
                        director.constructDefault();
                    requestForm.setDeliveryDetail(deliveryDetail);
                } else {
                    log.debug("buildDeliveryDetail() - tried to set up a default DeliveryDetail, but user is null");
                }
            }
        }
    }

    /**
     * Build the delivery detail form for stock reorder.  All we want is a list of MDH facilities
     * of type BUILDING.  We're assuming the organization is MDH.  we are not interested in the
     * recipient because its going to the stockroom.  the mailing address will be determined from the
     * selected facility.
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildDeliveryDetailFormForStockReorder() throws InfrastructureException {
        DeliveryDetailForm deliveryDetailForm = new DeliveryDetailForm();
        DeliveryDetailFormBuilder builder = new DeliveryDetailFormBuilder(deliveryDetailForm, daoFactory);
        builder.buildFacilities(Facility.TYPE_BUILDING);
        requestForm.setDeliveryDetailForm(deliveryDetailForm);
    }

    public void buildPriorities() throws InfrastructureException {
        Collection priorities = daoFactory.getPriorityDAO().findAll();
        requestForm.setPriorities(priorities);
        String requestFormPriorityCode = requestForm.getPriorityCode();
        if (request) {
            if (request?.getPriority()) {
                requestForm.setPriorityId(request.getPriority().getPriorityId().toString());
            } else {
                Priority normal = daoFactory.getPriorityDAO().findByPriorityCode(Priority.NORMAL);
                requestForm.setPriorityId(normal.getPriorityId().toString());
                requestForm.setPriorityCode(normal.getPriorityCode());
            }
        }

        requestForm.setPriorityCode(requestFormPriorityCode ?: Priority.NORMAL);
    }

    /**
     * Build all the RequestLineItemForm objects.
     * If forEvaluation is true, that means we're building the RequestForm for an user and so we only want them to evaluate the
     * RLI's that they are supposed to evaluate (i.e., the RLI's which have a MaterialsRequestEvaluation whose evaluatorGroup is a group the
     * user is a member of).
     * If forEvaluation is false, then include ALL the RLI's
     *
     * @param forEvaluation - if true, only the RequestLineItems that have a MaterialsRequestEvaluation whose evaluatorGroup is a
     *                      group the user is a member of.
     *                      If false, ALL RequestLineItems are included.
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildRequestLineItemForms(boolean forEvaluation) throws InfrastructureException {
        requestForm.getRequestLineItemForms().clear();                  //clear out any existing rli forms        
        Iterator rliIter = request.getRequestLineItems().iterator();
        outer:
        while (rliIter.hasNext()) {
            RequestLineItem rli = (RequestLineItem) rliIter.next();

            rli.determineIfItPurchase();

            if (!forEvaluation) { //not for evaluation, so show ALL RLI's on the form
                if (requestLineItemPurpose == FOR_PURCHASING) {
                    addRLIFormToRequestForm(rli, FOR_PURCHASING);
                } else {
                    if (rli.isItPurchase()) {
                        addRLIFormToRequestForm(rli, FOR_IT_PURCHASES);
                    } else {
                        addRLIFormToRequestForm(rli, FOR_EDIT_RLIF);
                    }
                }
            } else {//it IS for evaluation, so only show the RLI's that the user may evaluate
                //check if any LEVEL One evaluations left to be done
                Iterator requestEvalsIter = rli.getRequestEvaluations().iterator();
                while (requestEvalsIter.hasNext()) {
                    MaterialsRequestEvaluation requestEvaluation = (MaterialsRequestEvaluation) requestEvalsIter.next();

                    if (user.isInGroup(requestEvaluation.getEvaluatorGroup().getGroupCode())) {
                        addRLIFormToRequestForm(rli, FOR_EVALUATE_RLIF);
                        continue outer;     //already including this RLI, so skip to the next one
                    }
                }
            }
        }
    }

    public void buildRequestLineItemFormsForITPurchases(boolean forEvaluation) throws InfrastructureException {
        requestForm.getRequestLineItemForms().clear();                  //clear out any existing rli forms
        Iterator rliIter = request.getRequestLineItems().iterator();
        outer:
        while (rliIter.hasNext()) {
            RequestLineItem rli = (RequestLineItem) rliIter.next();

            rli.setItPurchase(rli.determineIfItPurchase());

            if (!forEvaluation) { //not for evaluation, so show ALL RLI's on the form
                if (requestLineItemPurpose == FOR_PURCHASING) {
                    addRLIFormToRequestForm(rli, FOR_PURCHASING);
                } else {
                    addRLIFormToRequestForm(rli, FOR_IT_PURCHASES);
                }
            } else {//it IS for evaluation, so only show the RLI's that the user may evaluate
                Iterator requestEvalsIter = rli.getRequestEvaluations().iterator();
                while (requestEvalsIter.hasNext()) {
                    MaterialsRequestEvaluation requestEvaluation = (MaterialsRequestEvaluation) requestEvalsIter.next();
                    Iterator personGroupLinksIter = user.getPersonGroupLinks().iterator();
                    while (personGroupLinksIter.hasNext()) {
                        PersonGroupLink personGroupLink = (PersonGroupLink) personGroupLinksIter.next();
                        Group userGroup = personGroupLink.getGroup();
                        if (userGroup.getGroupCode().equalsIgnoreCase(requestEvaluation.getEvaluatorGroup().getGroupCode())
                                && rli.getStatus().getStatusCode().equalsIgnoreCase(Status.WAITING_FOR_APPROVAL)) {
                            addRLIFormToRequestForm(rli, FOR_EVALUATE_RLIF);
                            continue outer;     //already including this RLI, so skip to the next one
                        }
                    }
                }
            }
        }
    }
    /**
     * Add only those RLI's that are approved (Waiting For Dispersal) and are for Stock items
     * to the Request Form.
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildRequestLineItemFormsForStockItems() throws InfrastructureException {
        requestForm.getRequestLineItemForms().clear();                  //clear out any existing rli forms        
        Iterator rliIter = request.getRequestLineItems().iterator();
        while (rliIter.hasNext()) {
            RequestLineItem rli = (RequestLineItem) rliIter.next();
            String statusCode = rli.getStatus().getStatusCode();
            Item item = rli.getItem();
            if (statusCode.equals(Status.WAITING_FOR_DISPERSAL) && item.getItemType().equals(Item.STOCK_ITEM)) {
                addRLIFormToRequestForm(rli, FOR_STOCK_PICK_RLIF);
            }
        }
    }

    public void buildRequestLineItemFormsForStockItemsWithAllStatuses() throws InfrastructureException {
        requestForm.getRequestLineItemForms().clear();                  //clear out any existing rli forms
        Iterator rliIter = request.getRequestLineItems().iterator();
        while (rliIter.hasNext()) {
            RequestLineItem rli = (RequestLineItem) rliIter.next();
            String statusCode = rli.getStatus().getStatusCode();
            if (rli?.getItem()?.getItemType()?.equals(Item.STOCK_ITEM)) {
                addRLIFormToRequestForm(rli, FOR_STOCK_PICK_RLIF);
            }
        }
    }

    public void buildRequestLineItemFormsForStockItems(String[] codes) throws InfrastructureException {
        requestForm.getRequestLineItemForms().clear();
        Set c = new HashSet();//clear out any existing rli forms
        CollectionUtils.addAll(c, codes);
        Iterator rliIter = request.getRequestLineItems().iterator();
        while (rliIter.hasNext()) {
            RequestLineItem rli = (RequestLineItem) rliIter.next();
            String statusCode = rli.getStatus().getStatusCode();
            if (c.contains(statusCode) && rli.getItem() instanceof StockItem) {
                addRLIFormToRequestForm(rli, FOR_STOCK_PICK_RLIF);
            }
        }
    }

    /**
     * @param rli
     * @param purpose - FOR_EDIT_RLIF(0) = for a requestor to edit, FOR_EVALUATE_RLIF(1) = for an evaluator, FOR_STOCK_PICK_RLIF(2) = for stock clerk
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    private void addRLIFormToRequestForm(RequestLineItem rli, int purpose) throws InfrastructureException {
        RequestLineItemForm rliForm = new RequestLineItemForm();

        rliForm.setRequest(request);
        RequestLineItemFormBuilder rliFormBuilder = new RequestLineItemFormBuilder(rliForm, rli, daoFactory);
        RequestLineItemFormDirector rliFormDirector = new RequestLineItemFormDirector(rliFormBuilder);
        switch (purpose) {
            case FOR_EDIT_RLIF:
                rliFormDirector.constructEditRequestLineItemForm();
                break;
            case FOR_EVALUATE_RLIF:
                rliFormDirector.constructEvaluateRequestLineItemForm(user);
                if (rliForm.isLevelOneRequestEvaluationApprovalsPending()) {
                    if (rliForm.isHasPendingLevelOneForCurrentEvaluator() && rliForm.isHasPendingLevelTwoForCurrentEvaluator()) {
                        requestForm.setEvaluatorsEvaluationStatus(Constants.LEVEL_ONE_AND_LEVEL_TWO_APPROVER);
                    } else if (rliForm.isHasPendingLevelOneForCurrentEvaluator()) {
                        requestForm.setEvaluatorsEvaluationStatus(Constants.LEVEL_ONE_APPROVER);
                    } else if (rliForm.isHasPendingLevelTwoForCurrentEvaluator()) {
                        requestForm.setEvaluatorsEvaluationStatus(Constants.LEVEL_TWO_APPROVER + Constants.DASHES_TWO + Constants.CLICK_TO_VIEW_DETAILS);
                    } else {
                        requestForm.setEvaluatorsEvaluationStatus(Constants.WAITING_FOR_OTHERS_TO_DO_LEVEL_ONE_APPROVAL + Constants.DASHES_TWO + Constants.CLICK_TO_VIEW_DETAILS);
                    }
                } else if (rliForm.isLevelTwoRequestEvaluationApprovalsPending()) {
                    if (rliForm.isHasPendingLevelTwoForCurrentEvaluator()) {
                        requestForm.setEvaluatorsEvaluationStatus(Constants.LEVEL_TWO_APPROVER);
                    } else {
                        requestForm.setEvaluatorsEvaluationStatus(Constants.NO_ACTION_REQUIRED_BY_YOU + Constants.DASHES_TWO + Constants.CLICK_TO_VIEW_DETAILS);
                    }
                }

                requestForm.getRequestLineItemForms().add(rliForm);
                break;
            case FOR_STOCK_PICK_RLIF:
                rliFormDirector.constructStockpickRequestLineItemForm();
                break;
            case FOR_PURCHASING:
                rliFormDirector.constructEditPurchasingRequestLineItemForm();
                break;
            case FOR_VIEW_MY_REQUESTS:
                rliFormDirector.constructRequestLineItemFormForViewMyRequests();
                break;
            case FOR_IT_PURCHASES:
                rliFormDirector.constructEditRequestLineItemFormForITPurchases();
                break;
            default:
                rliFormDirector.constructEditRequestLineItemForm();
        }

        if (purpose != FOR_EVALUATE_RLIF) {
            requestForm.getRequestLineItemForms().add(rliForm);
        }
    }

    private void buildItemCostAndEstimate(RequestLineItemForm rliForm, RequestLineItem rli) throws InfrastructureException {
        // If it is a stock item,  show item cost from primary vendor
        if (rliForm.getItem() != null) {
            Item tItem = rliForm.getItem();
            Collection itemVendors = daoFactory.getVendorDAO().findItemVendorsById(rliForm.getItem().getItemId());
            for (Iterator<ItemVendor> iterator = itemVendors.iterator(); iterator.hasNext();) {
                ItemVendor lVendor = iterator.next();
                if (lVendor.getPrimaryVendor()) {
                    if (rliForm.getItemCost() == null || Double.valueOf(rliForm.getItemCost()) == 0d) rliForm.setItemCost(lVendor.getBuyUnitCost().toString());
                    if (rliForm.getEstimatedCost() == 0d) {
                        if (rli.getQuantity() != null && !rli.getQuantity().equals("") && lVendor.getBuyUnit().getCode().equalsIgnoreCase("EACH")) {
                            rliForm.setEstimatedCost(lVendor.getBuyUnitCost() * rli.getQuantity());
                        } else {
                            rliForm.setEstimatedCost("0");
                        }
                    }
                }

            }
        }

    }

    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(requestForm, request);
            requestForm.setSelectedRequestorId(request?.getRequestor()?.getPersonId()?.toString());
            requestForm.setPriorityCode(request.getPriority()?.getPriorityCode());
        }
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building RequestForm: ", rpe);
        }
    }

    public void buildAvailableRequestors() throws InfrastructureException {
        Collection availableRequestors = daoFactory.getPersonDAO().findAllMDHEmployees();
        requestForm.setAvailableRequestors(availableRequestors);
    }

    public void buildRequestor() throws InfrastructureException {
        requestForm.setRequestor(request?.getRequestor() ?: user);
    }

    public void buildRequestLineItemFormsForViewMyRequests() throws InfrastructureException {
        requestForm.getRequestLineItemForms().clear();
        Iterator rliIter = request.getRequestLineItems().iterator();
        while (rliIter.hasNext()) {
            RequestLineItem rli = (RequestLineItem) rliIter.next();
            Iterator afiIter = rli.getAttachedFileNonCats().iterator();
            while (afiIter.hasNext()) {
                AttachedFileNonCat attachedFileNonCat = (AttachedFileNonCat) afiIter.next();
                Hibernate.initialize(attachedFileNonCat);
            }
            addRLIFormToRequestForm(rli, FOR_VIEW_MY_REQUESTS);
        }
    }

    public boolean hasSwiftItems() {
        for (Object o : request.getRequestLineItems()) {
            RequestLineItem rli = (RequestLineItem) o;
            if (rli?.getSwiftItemId())
                return true;
        }
        return false;
    }

    private DeliveryDetail requestLineItemDefaultDeliveryDetail(Request request) {
//        check if request has already been submitted, and don't change delivery details if it has.
        if(!request.trackingNumber){
//            find the first item with default delivery info and return it
            request?.requestLineItems?.findResult {
                def item = it.item ? daoFactory.purchaseItemDAO.getItemById(it.item.itemId) : null
                item?.deliveryDetail ? daoFactory.deliveryDetailDAO.getDeliveryDetailById(item.deliveryDetail.deliveryDetailId) : null
            }
        }
    }
}