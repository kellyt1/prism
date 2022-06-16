package us.mn.state.health.builder.materialsrequest

import org.apache.struts.upload.FormFile
import us.mn.state.health.builder.LineItemBuilder
import us.mn.state.health.common.exceptions.InfrastructureException
import us.mn.state.health.common.exceptions.ReflectivePropertyException
import us.mn.state.health.common.lang.PropertyUtils
import us.mn.state.health.common.lang.StringUtils
import us.mn.state.health.common.util.CollectionUtils
import us.mn.state.health.common.util.DateUtils
import us.mn.state.health.dao.DAOFactory
import us.mn.state.health.dao.StatusDAO
import us.mn.state.health.matmgmt.util.Constants
import us.mn.state.health.model.common.*
import us.mn.state.health.model.inventory.Item
import us.mn.state.health.model.inventory.StockItem
import us.mn.state.health.model.materialsrequest.*
import us.mn.state.health.view.materialsrequest.*

public class RequestLineItemBuilder extends LineItemBuilder {
    private RequestLineItem requestLineItem;
    private RequestLineItemForm requestLineItemForm;
    private ShoppingListCatLineItemForm shppngLstCatLnItmForm;
    private ShoppingListNonCatLineItemForm shppngLstNonCatLnItmForm;
    private DAOFactory daoFactory;
    private User actor;

    public RequestLineItemBuilder(RequestLineItem requestLineItem, RequestLineItemForm requestLineItemForm, DAOFactory daoFactory, User user) {
        this.requestLineItem = requestLineItem;
        this.requestLineItemForm = requestLineItemForm;
        this.daoFactory = daoFactory;
        this.actor = user;
    }

    public RequestLineItemBuilder(RequestLineItem requestLineItem, ShoppingListCatLineItemForm shppngLstCatLnItmForm, DAOFactory daoFactory, User user) {
        this.requestLineItem = requestLineItem;
        this.shppngLstCatLnItmForm = shppngLstCatLnItmForm;
        this.daoFactory = daoFactory;
        this.actor = user;
    }

    public RequestLineItemBuilder(RequestLineItem requestLineItem, ShoppingListNonCatLineItemForm shppngLstNonCatLnItmForm, DAOFactory daoFactory, User user) {
        this.requestLineItem = requestLineItem;
        this.shppngLstNonCatLnItmForm = shppngLstNonCatLnItmForm;
        this.daoFactory = daoFactory;
        this.actor = user;
    }

    /**
     * This method sets all the 'primitive' types of the stock item from the form...
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            if(requestLineItemForm != null) {
                PropertyUtils.copyProperties(requestLineItem, requestLineItemForm);
            }
            else if(shppngLstCatLnItmForm != null) {
                PropertyUtils.copyProperties(requestLineItem, shppngLstCatLnItmForm);
            }
            else {
                PropertyUtils.copyProperties(requestLineItem, shppngLstNonCatLnItmForm);
            }
        } 
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building RequestLineItem: ", rpe);
        }
    }

    /**
     * build the RLI Funding Sources.  Clear out any existing first, and then rebuild.
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     ***************************************************************************************/
    public void buildFundingSources() throws InfrastructureException {
        if(requestLineItem != null && !requestLineItem.getFundingSources().isEmpty()) {   //delete all the existing funding sources, rebuild later
            Object[] fSrcArray = requestLineItem.getFundingSources().toArray();
            for (Object aFSrcArray : fSrcArray) {
                requestLineItem.removeFundingSource((RequestLineItemFundingSource) aFSrcArray);
            }
        }
        
        if(requestLineItemForm != null && requestLineItem != null) {
            if(requestLineItemForm.getIsForStockReorder()) {
                StockItem si = daoFactory.getStockItemDAO().getStockItemById(requestLineItemForm.getStockItem().getItemId(), false);
                OrgBudget orgBdgt = daoFactory.getOrgBudgetDAO().getOrgBudgetById(si.getOrgBudget().getOrgBudgetId(), false);            
                RequestLineItemFundingSource fndSrc = 
                    new RequestLineItemFundingSource(orgBdgt, requestLineItemForm.getEstimatedCost(), requestLineItem);
                requestLineItem.addFundingSource(fndSrc);
            }
            else {
                Iterator iter = requestLineItemForm.getFundingSourceForms().iterator();
                while(iter.hasNext()) {
                    RequestLineItemFundingSourceForm fundingSourceForm = (RequestLineItemFundingSourceForm) iter.next();
                    if(!("".equals(fundingSourceForm.getOrgBudgetId()) || "".equals(fundingSourceForm.getAmount()))) {
                        Double amt = new Double(fundingSourceForm.getAmount());
                        RequestLineItemFundingSource fundingSrc = new RequestLineItemFundingSource(fundingSourceForm.getOrgBudget(), amt, requestLineItem);
                        requestLineItem.addFundingSource(fundingSrc);
                    }
                }
            }           
        }        
    }

    /**
     * For every MaterialsRequestEvaluation in the RLI where the evaluatorGroup is a
     * group that the evaluator is a member of, set the status, evaluator, date, etc...
     * <p/>
     * TODO: instead of loading the groups that a user belongs to, remember that MaterialsRequestEvaluation
     * has a reference to Group, which has a personGroupLinks representing the groups members.  Would it
     * be better to check that member list to see if the person is a member?
     *
     * @param isApproved
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildRequestEvaluations(Boolean isApproved) throws InfrastructureException {
        if(isApproved != null) {                          
            StatusDAO statusDAO = daoFactory.getStatusDAO();
            Status approved = statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.APPROVED);
            Status denied = statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.DENIED);

            Iterator requestEvalsIter = requestLineItem.getRequestEvaluations().iterator();
            outer:
              while(requestEvalsIter.hasNext()) {
                  MaterialsRequestEvaluation requestEvaluation = (MaterialsRequestEvaluation) requestEvalsIter.next();
                  // ??
                  Group requestEvalGroup = requestEvaluation.getEvaluatorGroup();
                  Iterator requestEvalGroupMembersIter = requestEvalGroup.getPersonGroupLinks().iterator();
                  while (requestEvalGroupMembersIter.hasNext()) {
                      PersonGroupLink pgLink = (PersonGroupLink)requestEvalGroupMembersIter.next();
                      Person currentMember = pgLink.getPerson();
                      if(currentMember.getPersonId().longValue() == actor.getPersonId().longValue()) {
                          requestEvaluation.setEvaluationDate(new Date());

                          if(isApproved) {
                                requestEvaluation.setEvaluationDecision(approved);
                          }
                          else {
                              requestEvaluation.setEvaluationDecision(denied);
                          }
                          requestEvaluation.setEvaluator(actor);
                          continue outer;     //no need to continue iterating through the groups, so skip to next requestEval
                      }
                  }
              }
        }     
    }

    /**
     * For every MaterialsRequestEvaluation in the RLI where the evaluatorGroup is a
     * group that the evaluator is a member of, set the status, evaluator, date, etc...
     * <p/>
     * TODO: instead of loading the groups that a user belongs to, remember that MaterialsRequestEvaluation
     * has a reference to Group, which has a personGroupLinks representing the groups members.  Would it
     * be better to check that member list to see if the person is a member?
     *
     * @param isApproved
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     * Changed the parameter from a Boolean to an Integer so that we could add a 3rd option to select.
     */
    public void buildRequestEvaluations(Integer isApproved, String firstStatus) throws InfrastructureException {
        if(isApproved != null) {
            StatusDAO statusDAO = daoFactory.getStatusDAO();
            Status approved = statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.APPROVED);
            Status denied = statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.DENIED);
            Status waitingForDispersal = statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.WAITING_FOR_DISPERSAL);

            Iterator requestEvalsIter = requestLineItem.getRequestEvaluations().iterator();
            outer:
              while(requestEvalsIter.hasNext()) {
                  MaterialsRequestEvaluation requestEvaluation = (MaterialsRequestEvaluation) requestEvalsIter.next();
                  Group requestEvalGroup = requestEvaluation.getEvaluatorGroup();
                  //do not do any level 2 approvals if there are still level 1 approvals pending
                  if (requestLineItemForm.isLevelOneRequestEvaluationApprovalsPending() && requestEvaluation.getApprovalLevel() == Constants.APPROVAL_LEVEL_TWO) continue outer;
                  Iterator requestEvalGroupMembersIter = requestEvalGroup.getPersonGroupLinks().iterator();
                  while (requestEvalGroupMembersIter.hasNext()) {
                      PersonGroupLink pgLink = (PersonGroupLink)requestEvalGroupMembersIter.next();
                      Person currentMember = pgLink.getPerson();
                      //todo tr - insert checking for approval level here?

                      // ??
                      if (!firstStatus.isEmpty()){
                          Status firstEnteredStatus = statusDAO.getStatusByStatusCode(firstStatus);
                          requestEvaluation.setFirstStatus(firstEnteredStatus);
                      }

                      if(currentMember.getPersonId().longValue() == actor.getPersonId().longValue()) {
                          requestEvaluation.setEvaluationDate(new Date());

                          if(isApproved == 1) {
                              requestEvaluation.setEvaluationDecision(approved);
                          }
                          else if (isApproved == 0) {
                              requestEvaluation.setEvaluationDecision(denied);
                          }
                          else if (isApproved == 2) {
                              requestEvaluation.setEvaluationDecision(waitingForDispersal);
                          }
                          requestEvaluation.setEvaluator(actor);
                          continue outer;     //no need to continue iterating through the groups, so skip to next requestEval
                      }
                  }
              }
        }
    }

    /**
     * Set flag if this is an ITPurchase item so that we can generate a helpdesk ticket
     */
    public void buildItPurchase() {
        requestLineItem.determineIfItPurchase();

    }

    // PRIS-159 if amount > budget, already set it to WFA and at approval doesn't take into account CPO Approve option
    public void buildItPurchase(String status) {
        if (status.equalsIgnoreCase("CPO"))
            requestLineItem.setItPurchase(true);
        else
            requestLineItem.determineIfItPurchase();
    }


    /**
     * The status of an RLI depends on the status of all its MaterialsRequestEvaluations.  If ALL of them
     * are APPROVED, the RLI is approved but its status will be "Awaiting Purchase" or "Awaiting Stock Pick"
     * depending on the type of item (if its null or a PurchaseItem, status=Awaiting Purchase).
     * If any of them is DENIED, the RLI is denied.
     * If any of them are WAITING_FOR_APPROVAL the RLI is WFA.
     *
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public void buildStatus(String skin) throws InfrastructureException {
        buildStatus(skin,"TBD");
    }
    public void buildStatus(String skin, String inDesiredStatus) throws InfrastructureException {
        Iterator requestEvalsIter = requestLineItem.getRequestEvaluations().iterator();

        boolean allApproved = true;
        if(!requestEvalsIter.hasNext()) {    //there are no RequestEvaluations yet (hasn't run through BRE) , so default to WFA
            requestLineItem.setStatus(daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.WAITING_FOR_APPROVAL));
            allApproved = false;
        }

        while(requestEvalsIter.hasNext()) {
            MaterialsRequestEvaluation requestEvaluation = (MaterialsRequestEvaluation) requestEvalsIter.next();
            Status status = requestEvaluation.getEvaluationDecision();
            if(status == null || status.getStatusCode().equals(Status.WAITING_FOR_APPROVAL)) {
                if (requestLineItem.isItPurchase()) {
                    if (!requestLineItem.getStatus().equals(daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST,
                    Status.WAITING_FOR_DISPERSAL))) {
                        requestLineItem.setStatus(daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.WAITING_FOR_APPROVAL));
                        allApproved = false;
                    }
                }
                else {
                    requestLineItem.setStatus(daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.WAITING_FOR_APPROVAL));
                    allApproved = false;

                }
            }
            else if(status.getStatusCode().equals(Status.DENIED)) {
                requestLineItem.setStatus(daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.DENIED));
                allApproved = false;
                break;      //its denied, so its dead - no need to continue
            }
            else if(status.getStatusCode().equals(Status.WAITING_FOR_DISPERSAL)) {
                requestLineItem.setStatus(daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.MATERIALS_REQUEST, Status.WAITING_FOR_DISPERSAL));
            }
        }
        
        //if all the RequestEvaluations are approved, set the RLI's status accordingly.   See the RLI.approve() method
        if(allApproved) {
            requestLineItem.approve(inDesiredStatus);
        }
    }

    public void buildStatusFromForm() {
        requestLineItem.setStatus(requestLineItemForm.getStatus());
    }

    public void addNote() throws Exception {
        RequestLineItemNoteForm rliNote = new RequestLineItemNoteForm();
        rliNote.setAuthorName(actor.getFirstAndLastName());
        rliNote.setInsertionDate(DateUtils.toString(new Date()));
        rliNote.setNoteText(requestLineItemForm.getTextNote());
        requestLineItemForm.getNoteForms().add(rliNote);
    }

    public void buildNotes() throws Exception {
        Iterator iter = requestLineItemForm.getNoteForms().iterator();
        Collection<RequestLineItemNote> notes = requestLineItem.getNotes();

        while(iter.hasNext()) {
            RequestLineItemNoteForm noteForm = (RequestLineItemNoteForm) iter.next();
            if(!StringUtils.nullOrBlank(noteForm.getNoteText()) || noteForm.getRemoved().booleanValue()) {
                RequestLineItemNote rliNote = null;
                Long noteId = noteForm.getRequestLineItemNoteId();
                //Check for existing note
                if(noteId != null) {
                    rliNote = (RequestLineItemNote) CollectionUtils.getObjectFromCollectionById(notes, noteId, "requestLineItemNoteId");
                    //remove if marked for removal
                    if(noteForm.getRemoved()) {
                        iter.remove();
                        CollectionUtils.removeMatchingItem(notes, noteId, "requestLineItemNoteId");
                    }
                    else { //else update note text. Only user logged in can update notes they created.
                        rliNote.setNoteText(noteForm.getNoteText());
                    }
                }
                else if(noteForm.getRemoved()) { //new note form that has been removed already
                    iter.remove();
                }
                else { //this is a new note
                    rliNote = new RequestLineItemNote();
                    rliNote.setAuthorName(noteForm.getAuthorName());
                    Date insertionDate = DateUtils.createDate(noteForm.getInsertionDate());
                    rliNote.setInsertionDate(insertionDate);
                    rliNote.setNoteText(noteForm.getNoteText());
                    requestLineItem.addNote(rliNote);
                }
            }
        }
    }

    public void buildPurchasingInfoFiles() throws InfrastructureException {
        if (requestLineItemForm.getPurchasingInfoFile() != null && !requestLineItemForm.getPurchasingInfoFile().getFileName().trim().equals("")) {
            handlePurchaseFile(requestLineItemForm.getPurchasingInfoFile())
            requestLineItemForm.getPurchasingInfoFile().destroy();
        }
        else if (requestLineItemForm != null
                && requestLineItemForm.getRequestLineItem() != null
                && requestLineItemForm.getRequestLineItem().getAttachedFileNonCats() != null ){
            requestLineItem.setAttachedFileNonCats(requestLineItemForm.getRequestLineItem().getAttachedFileNonCats());
        }
    }

    private void handlePurchaseFile(FormFile purchaseFile) {
        AttachedFileNonCat attachedFile = new AttachedFileNonCat();
        try {
            attachedFile.setFileContents(purchaseFile.getFileData());
            attachedFile.setFileName(purchaseFile.getFileName());
            attachedFile.setFileSize(purchaseFile.getFileSize());
        } catch (IOException ioe) {
            throw new InfrastructureException("Failed Building Print Spec File: ", ioe);
        }
        requestLineItem.addAttachedFileNonCat(attachedFile);
    }

    public void buildPurchasingInfoFilesAlternate() throws InfrastructureException {
        if (requestLineItemForm.getPurchasingInfoFileAlternate() != null && !requestLineItemForm.getPurchasingInfoFileAlternate().getFileName().trim().equals("")) {
            handlePurchaseFile(requestLineItemForm.getPurchasingInfoFileAlternate());
            requestLineItemForm.getPurchasingInfoFileAlternate().destroy();
        }
    }

//  Not used in this version
//  Will be used to support attaching of multiple files to a request line item
//
    public void buildAttachFile() throws InfrastructureException {
        AttachedFileNonCat attachedFile = new AttachedFileNonCat();
        if (requestLineItemForm.getPurchasingInfoFile() != null) {
            attachedFile.setFileName(requestLineItemForm.getPurchasingInfoFile().getFileName());
            attachedFile.setFileSize(requestLineItemForm.getPurchasingInfoFile().getFileSize());
            try {
                attachedFile.setFileContents(requestLineItemForm.getPurchasingInfoFile().getFileData());
            }catch(Exception ignore) {
            }
            requestLineItem.addAttachedFileNonCat(attachedFile);
        } else {
            requestLineItem.setAttachedFileNonCats(requestLineItemForm.getRequestLineItem().getAttachedFileNonCats());
        }
    }


//  Not used in this version
//  Will be used to support attaching of multiple files to a request line item
//
    public void buildAttachedFileNonCats() {
        if(requestLineItemForm != null)
            requestLineItem.setAttachedFileNonCats(requestLineItemForm.getRequestLineItem().getAttachedFileNonCats());
    }

    public void buildItemCategory() throws InfrastructureException {
        Category category = null;
        if(requestLineItemForm != null) {
            category = requestLineItemForm.getCategory();
        }
        else if(shppngLstNonCatLnItmForm != null) {
            category = shppngLstNonCatLnItmForm.getCategory();
        }
        requestLineItem.setItemCategory(category);
    }

    public void buildItem() throws InfrastructureException {
        Item item = null;
        if(requestLineItemForm != null) {
            item = requestLineItemForm.getItem();
        }
        else if(shppngLstCatLnItmForm != null) {
            item = shppngLstCatLnItmForm.getItem();
        }
        
        if(item != null) { //reset non-cat fields to null
            //If already catalog item  leave as catalog item
            if (requestLineItemForm?.getCategory() != null) {
                requestLineItem.setItemCategory(requestLineItemForm.getCategory());
            } else {
                requestLineItem.setItemCategory(null);
            }
            requestLineItem.setItemCost(null);
            requestLineItem.setItemDescription(null);
            requestLineItem.setItemHazardous(null);
            item = daoFactory.getItemDAO().getItemById(item.getItemId(), false);
            requestLineItem.setItem(item);
        }
        else {
            requestLineItem.setItemDescription(requestLineItemForm.getItemDescription());
            String itemCost = requestLineItemForm.getItemCost();
            if(itemCost != null && !"".equals(itemCost.trim())) {
                requestLineItem.setItemCost(Double.valueOf(itemCost));
            }
            requestLineItem.setItemHazardous(requestLineItemForm.getItemHazardous());
            requestLineItem.setItemJustification(requestLineItemForm.getItemJustification());
            requestLineItem.setItemCategory(requestLineItemForm.getCategory());
        }
    }

    public void buildUnit() {
        if(requestLineItemForm != null) {
            requestLineItem.setUnit(requestLineItemForm.getUnit());
        }        
    }
    public void buildDenialReason() {
        if (requestLineItemForm != null && requestLineItemForm.getRliDenialReason() != null && !requestLineItemForm.getRliDenialReason().equals("") && requestLineItemForm.getApproved().equals(0)) {
            requestLineItem.setDenialReason(requestLineItemForm.getRliDenialReason());
        }
    }
    public void buildMetaProperties() {
        if (actor != null) {
            requestLineItem.setLastUpdatedBy(actor.getUsername());
            requestLineItem.setLastUpdated(new Date());
        }
    }
}