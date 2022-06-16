package us.mn.state.health.view.materialsrequest

import org.apache.struts.action.ActionErrors
import org.apache.struts.action.ActionMapping
import org.apache.struts.upload.FormFile
import org.apache.struts.validator.ValidatorForm
import us.mn.state.health.common.lang.StringUtils
import us.mn.state.health.common.lang.WrapperUtils
import us.mn.state.health.common.util.CollectionUtils
import us.mn.state.health.model.common.Category
import us.mn.state.health.model.common.Priority
import us.mn.state.health.model.common.Status
import us.mn.state.health.model.common.User
import us.mn.state.health.model.inventory.Item
import us.mn.state.health.model.inventory.StockItem
import us.mn.state.health.model.inventory.Unit
import us.mn.state.health.model.materialsrequest.MaterialsRequestEvaluation
import us.mn.state.health.model.materialsrequest.Request
import us.mn.state.health.model.materialsrequest.RequestLineItem

import javax.servlet.http.HttpServletRequest

public class RequestLineItemForm extends ValidatorForm {
    String requestLineItemId;
//    Boolean requestMakeCatalogItem;
    String statusId;
    String statusCode;
    String statusName;
    Collection<Status> statuses = new ArrayList<Status>();
    String fundingSourceKey = "";
    RequestLineItemFundingSourceForm fundingSourceForm;
    Collection<RequestLineItemFundingSourceForm> fundingSourceForms = new ArrayList<RequestLineItemFundingSourceForm>();  //this is an ArrayList because TreeSet elements must implement Comparable
    Boolean amountInDollars = true;                //true means dollars (default), false means percent
    Request request;
    Collection<RequestLineItemNoteForm> noteForms = new ArrayList<RequestLineItemNoteForm>();
    Item item;
    String itemDescription;
    String categoryId;
    Collection<Category> categories = new TreeSet<Category>();
    String quantity;
    String quantityFilled;
    String quantityPicked;
    Double estimatedCost
    String itemCost = "";
    Boolean itemHazardous = false;
    String itemJustification="";
    String endRequestStatus = "WFP";
    Collection<FormFile> purchasingInfoFiles;
    FormFile purchasingInfoFile;
    FormFile purchasingInfoFileAlternate;
    String suggestedVendorURL;
    String suggestedVendorCatalogNumber;
    String suggestedVendorName;
    User evaluator;
    Integer approved;
    RequestLineItem requestLineItem;
    String unitId;
    Collection<Unit> units = new ArrayList<Unit>();
    Boolean isForStockReorder = false;
    String textNote;
    String icnbr;                   //cheater property to display full icnbr on purchasing pages.
    ArrayList<String> vendorNames = new ArrayList<String>();          //cheater to display stock itemVendorLink vendor name
    ArrayList<String> vendorCatalogNbrs = new ArrayList<String>();    //cheater to display stock itemVendorLink vendorCatalogNbr
    ArrayList<String> vendorURLs = new ArrayList<String>();    //cheater to display stock itemVendorLink vendorCatalogNbr

    String swiftItemId;
    String cmd = "";
    Boolean remove = false;
    Boolean showDetail = false;
    Boolean showNotes = false;
    Boolean selected = false;
    Boolean removedFromOrderLineItem = false;
    Boolean removedFromOrder = false;
    String input = "";
    String rliDenialReason = "";
    RequestForm requestForm = new RequestForm();
    String shoppingListAction = "";
    boolean itPurchase = false;
    boolean levelOneRequestEvaluationApprovalsPending = false;
    boolean levelTwoRequestEvaluationApprovalsPending = false;
    boolean hasPendingLevelOneForCurrentEvaluator;
    boolean hasPendingLevelTwoForCurrentEvaluator;
    String budgetBuilderLink;


/*
   if level one needed and evaluator has level one and their group(s) still need to do approval
   then evaluators currentEvaluationLevel = 1  (effectiveEvaluationLevel
   else if evaluator has level two
   then evaluator's currentEvaluationLevel = 2

   if NOT levelOne Evaluation Approvals Pending and evaluatorsEffectiveEvaluationLevel = 2
*/

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        cmd = "";
        itemDescription = "";
        quantity = "";
        itemCost = "";
        itemJustification="";
        suggestedVendorURL = "";
        suggestedVendorCatalogNumber = "";
        categoryId="";
        amountInDollars = true;
        selected = false;
        remove = false;
        fundingSourceKey = "";
        purchasingInfoFile = null;
        purchasingInfoFileAlternate = null;
        shoppingListAction = "";
        textNote = "";
        itPurchase = false;
        if (noteForms?.size()> 0) noteForms.clear();
        if (purchasingInfoFiles?.size() > 0)  purchasingInfoFiles.clear();
        if (requestLineItem?.getAttachedFileNonCats()?.size() > 0)  requestLineItem.getAttachedFileNonCats().clear();
    }

    /**
     * Clears the values of this object
     * reset(ActionMapping, HttpServletRequest) gets called automatically by whatever, but des not clear all fields
     * Calling this reset programmatically when fields need to be cleared from previous entries
     * See PRIS-179 for more info
     */
    public void reset() {
        reset(null, (HttpServletRequest) null)

        unitId = null
        fundingSourceForms.clear()
        suggestedVendorName = null
        swiftItemId = null
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    public Unit getUnit() {
        return (Unit)CollectionUtils.getObjectFromCollectionById(units, unitId, "unitId");
    }

    public Category getRequestLineItemCategory() {
        return requestLineItem?.getItem()?.getCategory() ?: requestLineItem.getItemCategory();
    }

    public Category getCategory() {
        return (Category)CollectionUtils.getObjectFromCollectionById(categories, categoryId, "categoryId");
    }

    public Status getStatus() {
        return (Status)CollectionUtils.getObjectFromCollectionById(statuses, statusCode, "statusCode");
    }

    public StockItem getStockItem() {
        StockItem si = null;
        if(item instanceof StockItem) {
            si = (StockItem)item;
        }
        return si;
    }

    public void setQuantity(String qty) {
        this.quantity = qty.trim() ?: ""
    }

    public String getQuantity() {
        return quantity;
    }

    public void setItemCost(String itemCost) {
        this.itemCost = itemCost;
    }

    public String getItemCost() {
        return WrapperUtils.round(itemCost, 4);
    }

    public String getItemCostRND() {
        return WrapperUtils.round(itemCost, 2);
    }

    public void setItemHazardous(Boolean itemHazardous) {
        this.itemHazardous = itemHazardous;
    }

    public Boolean getItemHazardous() {
        if(requestLineItem) {
            if(requestLineItem.getItem()) {
                return requestLineItem.getItem().getHazardous();
            }
            return requestLineItem.getItemHazardous();
        }
        return itemHazardous;
    }

    public String getDescriptionSummary() {
        if(requestLineItem?.getItem()) {
            return requestLineItem.getItem().getDescription();
        }
        else if(requestLineItem) {
            return requestLineItem.getItemDescription();
        }
        else if(item) {
            return item.getDescription();
        }
        else {
            return itemDescription;
        }
    }

    public void setEstimatedCost(String estimatedCost) {
        this.estimatedCost = new Double(estimatedCost);
    }

    public void setEstimatedCost(Double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    /**
     * Added on 10/26/2005 to help with the re-order stock items use case, and other
     * areas where we want to keep the estimated cost up-to-date...
     * hopefully it doesn't screw anything up.  Its being called by getEstimatedCost(),
     * and so it resets the estimatedCost everytime getEstimatedCost is called.
     */
    private void calculateEstimatedCost() {
        int qty = 0;
        if(this.getQuantity()) {
            try {
                qty = Integer.parseInt(this.getQuantity());
            } catch (NumberFormatException ignore) {
            }
        }

        double unitCost = 0.0;
        if(this?.getItem()?.getDispenseUnitCost()) {
            unitCost = this.getItem().getDispenseUnitCost();
        }
        else if(this.getItemCost()) {
            unitCost = Double.parseDouble(this.getItemCost());
        }

        double cost = qty * unitCost;
        this.setEstimatedCost(cost);
    }

    public Double getEstimatedCost() {
        calculateEstimatedCost();
        return estimatedCost;
    }

    public Double getEstimatedCostD() {
          return estimatedCost;
    }

    public String getKey() {
        return this.toString();
    }

    public boolean getValidVendor() {
        return (!StringUtils.nullOrBlank(getSuggestedVendorName()) && getSuggestedVendorName().length() > 0);
    }

    public String getLineTotal() {
        double lineTotal = 0d;
        try {
            if(!item) {
                lineTotal = Double.parseDouble(quantity) * Double.parseDouble(itemCost);
            }
            else {
                lineTotal = Double.parseDouble(quantity) * item.getDispenseUnitCost();
            }
        }
        catch(Exception ignore) {
        }
        return Double.toString(lineTotal);
    }

    public int getFundingSourceFormsSize() {
        return this.fundingSourceForms.size();
    }

    public boolean getItemPresent() {
        return item
    }

    /**
     * @return true if the rliForm has at least on rliFundingSourceForm enabled and false otherwise
     */
    public boolean getAtLeastOneFundingSourceEnabled() {
        return fundingSourceForms.find({ it.enabled })
    }

    public String getUrgency() {
        if (!requestLineItem?.getRequest()) {
            return "NORMAL"; //null;
        }

        Calendar calendar1 = Calendar.getInstance();
        long milliseconds2 = calendar1.getTimeInMillis();

        long milliseconds1 = Calendar.getInstance().getTime().getTime();
        if (requestLineItem.getRequest().getNeedByDate() != null) {
            milliseconds1 = requestLineItem.getRequest().getNeedByDate().getTime();
        }
        // Milliseconds difference
        long diff = milliseconds2 - milliseconds1;

        long diffDate = diff / (24 * 60 * 60 * 1000);

        if ((requestLineItem.status.statusCode.equalsIgnoreCase(Status.WAITING_FOR_PURCHASE)
                || requestLineItem.status.statusCode.equalsIgnoreCase(Status.WAITING_FOR_APPROVAL))
                && (requestLineItem.request.priority != null && requestLineItem.request.priority.priorityCode.equalsIgnoreCase(Priority.HIGH))){

            return "URGENT";
        }   else if ((requestLineItem.status.statusCode.equalsIgnoreCase(Status.WAITING_FOR_PURCHASE)
                            || requestLineItem.status.statusCode.equalsIgnoreCase(Status.WAITING_FOR_APPROVAL))
                            && (diffDate >  -3 && diffDate <= 0)) {
            return "WARNDATE";
        }   else {
            return "NORMAL";
        }
    }

    public ArrayList<MaterialsRequestEvaluation> getRequestEvaluations() {
        return new ArrayList<MaterialsRequestEvaluation>(requestLineItem.getRequestEvaluations());
    }

    void setApproved(Integer approved) {
        this.approved = approved
    }
}