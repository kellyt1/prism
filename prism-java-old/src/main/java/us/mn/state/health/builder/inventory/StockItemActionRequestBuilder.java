package us.mn.state.health.builder.inventory;

import java.util.Date;
import java.util.Iterator;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.StatusDAO;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.PersonGroupLink;
import us.mn.state.health.model.common.Status;
import us.mn.state.health.model.common.StatusType;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.inventory.ActionRequestType;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.inventory.StockItemActionRequest;
import us.mn.state.health.model.inventory.StockItemActionRequestEvaluation;
import us.mn.state.health.model.inventory.StockQtyChangeReasonRef;
import us.mn.state.health.view.inventory.StockItemActionRequestForm;

public class StockItemActionRequestBuilder {
    private String actionRequestTypeCode;
    private DAOFactory daoFactory;
    private User evaluator;
    private StockItem potentialStockItem;
    private User requestor;
    private StockItem stockItem;
    private StockItemActionRequest stockItemActionRequest;
    private StockItemActionRequestForm stockItemActionRequestForm;

    public StockItemActionRequestBuilder(User requestor,
                                         StockItemActionRequest stockItemActionRequest,
                                         StockItem potentialStockItem,
                                         StockItemActionRequestForm stockItemActionRequestForm,
                                         String actionRequestTypeCode,
                                         DAOFactory daoFactory) {
        //Call common constructor
        this(stockItemActionRequest, potentialStockItem, stockItemActionRequestForm, daoFactory);
        this.requestor = requestor;
        this.actionRequestTypeCode = actionRequestTypeCode;
    }

    public StockItemActionRequestBuilder(User requestor,
                                         StockItemActionRequest stockItemActionRequest,
                                         StockItem potentialStockItem,
                                         StockItem stockItem,
                                         StockItemActionRequestForm stockItemActionRequestForm,
                                         String actionRequestTypeCode,
                                         DAOFactory daoFactory) {
        //Call common constructor
        this(stockItemActionRequest, potentialStockItem, stockItemActionRequestForm, daoFactory);
        this.requestor = requestor;
        this.stockItem = stockItem;
        this.actionRequestTypeCode = actionRequestTypeCode;
    }

    public StockItemActionRequestBuilder(User evaluator,
                                         StockItemActionRequest stockItemActionRequest,
                                         StockItem potentialStockItem,
                                         StockItemActionRequestForm stockItemActionRequestForm,
                                         DAOFactory daoFactory) {
        //Call common constructor
        this(stockItemActionRequest, potentialStockItem, stockItemActionRequestForm, daoFactory);
        this.evaluator = evaluator;
    }

    public StockItemActionRequestBuilder(User evaluator,
                                         StockItemActionRequest stockItemActionRequest,
                                         StockItem potentialStockItem,
                                         StockItem stockItem,
                                         StockItemActionRequestForm stockItemActionRequestForm,
                                         DAOFactory daoFactory) {
        //Call common constructor
        this(stockItemActionRequest, potentialStockItem, stockItemActionRequestForm, daoFactory);
        this.evaluator = evaluator;
        this.stockItem = stockItem;
    }

    private StockItemActionRequestBuilder(StockItemActionRequest stockItemActionRequest,
                                          StockItem potentialStockItem,
                                          StockItemActionRequestForm stockItemActionRequestForm,
                                          DAOFactory daoFactory) {
        this.stockItemActionRequest = stockItemActionRequest;
        this.potentialStockItem = potentialStockItem;
        this.stockItemActionRequestForm = stockItemActionRequestForm;
        this.daoFactory = daoFactory;
    }

    public void buildActionRequestType() throws InfrastructureException {
        ActionRequestType actionRequestType = null;
        if(actionRequestTypeCode != null) {
            actionRequestType = daoFactory.getActionRequestTypeDAO().findByActionRequestTypeCode(actionRequestTypeCode);
        }            
        else {
            actionRequestType = stockItemActionRequestForm.getActionRequestType();
        }
        stockItemActionRequest.setActionRequestType(actionRequestType);
    }

    public void buildDAOFactory() {
        stockItemActionRequest.setDaoFactory(daoFactory);
    }

    /**
     * For every MaterialsRequestEvaluation in the RLI where the evaluatorGroup is a
     * group that the evaluator is a member of, set the status, evaluator, date, etc...
     * 
     * TODO: instead of loading the groups that a user belongs to, remember that MaterialsRequestEvaluation
     * has a reference to Group, which has a personGroupLinks representing the groups members.  Would it
     * be better to check that member list to see if the person is a member?  
     * @param approved
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildRequestEvaluations() throws InfrastructureException {
        StatusDAO statusDAO = daoFactory.getStatusDAO();
        Status approved = statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM_ACTION_REQUEST,
                                                                      Status.APPROVED);
        Status denied = statusDAO.findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM_ACTION_REQUEST,
                                                                    Status.DENIED);
        boolean isApproved = stockItemActionRequestForm.getApproved().booleanValue();
        
        Iterator requestEvalsIter = stockItemActionRequest.getRequestEvaluations().iterator();
      outer:
        while(requestEvalsIter.hasNext()){
            StockItemActionRequestEvaluation requestEvaluation = (StockItemActionRequestEvaluation)requestEvalsIter.next();
            Group requestEvalGroup = requestEvaluation.getEvaluatorGroup();
            Iterator requestEvalGroupMembersIter = requestEvalGroup.getPersonGroupLinks().iterator();
            while(requestEvalGroupMembersIter.hasNext()){
                PersonGroupLink pgLink = (PersonGroupLink)requestEvalGroupMembersIter.next();
                Person currentMember = pgLink.getPerson();
                if(currentMember.getPersonId().longValue() == evaluator.getPersonId().longValue()){
                    requestEvaluation.setEvaluationDate(new Date());
                    if(isApproved) { 
                        requestEvaluation.setEvaluationDecision(approved); 
                    }
                    else { 
                        requestEvaluation.setEvaluationDecision(denied); 
                    }
                    requestEvaluation.setEvaluator(evaluator);
                    continue outer;     //no need to continue iterating through the groups, so skip to next requestEval
                }
            }
        }
    }
 
    /**
     * The status of an SIAR depends on the status of all its StockItemActionRequestEvaluations.  Once 
     * ALL of them are APPROVED, the SIAR is approved and the potential stock item will be turned into 
     * a real stock item.
     * If any of them is DENIED, the SIAR is denied.  
     * If any of them are WAITING_FOR_APPROVAL the SIAR is WFA.   
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildStatus() throws InfrastructureException {    
        Iterator requestEvalsIter = stockItemActionRequest.getRequestEvaluations().iterator();
        
        if(!requestEvalsIter.hasNext()){    //there are no RequestEvaluations yet (hasn't run through BRE) , so default to WFA
            stockItemActionRequest.setStatus(daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM_ACTION_REQUEST,
                                                                                                         Status.WAITING_FOR_APPROVAL));
        }
        
        boolean allApproved = true;
        while(requestEvalsIter.hasNext()){
            StockItemActionRequestEvaluation requestEvaluation = (StockItemActionRequestEvaluation)requestEvalsIter.next();
            if(requestEvaluation.getEvaluationDecision().getStatusCode().equals(Status.WAITING_FOR_APPROVAL)){
                stockItemActionRequest.setStatus(daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM_ACTION_REQUEST,
                                                                                                      Status.WAITING_FOR_APPROVAL));
                allApproved = false;
            }
            else if(requestEvaluation.getEvaluationDecision().getStatusCode().equals(Status.DENIED)){
                stockItemActionRequest.setStatus(daoFactory.getStatusDAO().findByStatusTypeCodeAndStatusCode(StatusType.STOCK_ITEM_ACTION_REQUEST,
                                                                                                      Status.DENIED));
                allApproved = false;
                break;      //its denied, so its dead - no need to continue
            }
        }
        
        //if all the RequestEvaluations are approved, set the SIAR's status accordingly.   See the SIAR.approve() method
        if(allApproved) {
            stockItemActionRequest.approve();
        }
    }  

    public void buildMetaProperties() {
        if(stockItemActionRequest.getStockItemActionRequestId() == null) {
            stockItemActionRequest.setInsertedBy(requestor.getUsername());
        } 
        else {
            stockItemActionRequest.setLastUpdatedBy(evaluator.getUsername());
            stockItemActionRequest.setLastUpdatedDate(new Date());
        }
    }

    public void buildPotentialStockItem() throws InfrastructureException {
        stockItemActionRequest.setPotentialStockItem(potentialStockItem);
    }

    public void buildRequestedDate() {
        stockItemActionRequest.setRequestedDate(new Date());
    }

    public void buildRequestor() {
        stockItemActionRequest.setRequestor(requestor);
    }

    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(stockItemActionRequest, stockItemActionRequestForm);
        } 
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building StockItemActionRequest: ", rpe);
        }
    }

    public void buildStockItem() {
        stockItemActionRequest.setStockItem(stockItem);
    }

    public void buildSuggestedVendor() {
        Vendor vendor = stockItemActionRequestForm.getSuggestedVendor();
        stockItemActionRequest.setSuggestedVendor(vendor);
    }
    
    public void buildStockQtyChangeReasonRef() throws InfrastructureException {
        if(!StringUtils.nullOrBlank(stockItemActionRequestForm.getStkQtyChangeReasonRefId())) {
            Long reasonId = new Long(stockItemActionRequestForm.getStkQtyChangeReasonRefId());
            StockQtyChangeReasonRef reason = daoFactory.getStockQtyChangeReasonRefDAO()
                                                       .getStockQtyChangeReasonRefById(reasonId, false);
            stockItemActionRequest.setQtyOnHandChangeReason(reason);
        }
    }
    
    public void buildStockQtyChangeOrgBdgt() throws InfrastructureException {
        if(!StringUtils.nullOrBlank(stockItemActionRequestForm.getStkQtyChangeOrgBdgtId())) {
            Long orgBdgtId = new Long(stockItemActionRequestForm.getStkQtyChangeOrgBdgtId());
            OrgBudget orgBdgt = daoFactory.getOrgBudgetDAO().getOrgBudgetById(orgBdgtId, false);
            stockItemActionRequest.setQtyOnHandChangeOrgBudget(orgBdgt);
        }
    }
}