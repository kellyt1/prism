package us.mn.state.health.model.util

import com.rules4j.Rules4JServer
import com.rules4j.Rules4JStatefulRuleSession
import groovy.util.logging.Log4j
import us.mn.state.health.common.exceptions.InfrastructureException
import us.mn.state.health.dao.DAOFactory
import us.mn.state.health.matmgmt.util.Constants
import us.mn.state.health.model.common.BusinessRulesORCL
import us.mn.state.health.model.common.Status
import us.mn.state.health.model.common.StatusType
import us.mn.state.health.model.inventory.*
import us.mn.state.health.model.materialsrequest.Request
import us.mn.state.health.model.materialsrequest.RequestLineItem
import us.mn.state.health.model.materialsrequest.RequestLineItemFundingSource

import javax.rules.InvalidRuleSessionException
import javax.rules.RuleRuntime
import javax.rules.RuleServiceProvider
import javax.rules.RuleServiceProviderManager

@Log4j
public class BusinessRulesEngine {
    private static Rules4JServer ruleServer = null; // this is static because we don't want every session to have its own server.
    private static Collection ruleSetNames = null;
    private String currentRuleSetName = "";
    private Rules4JStatefulRuleSession currentRuleSession = null;
    private static Class ruleServiceProviderClass = null;

    static {
        try {
            ruleSetNames = new ArrayList();
            //TODO if we want to change @ runtime the path to rules4JFilesDirectory we have to refactor this static block
            String rules4JFilesDirectory = "";

            rules4JFilesDirectory = System.getProperty("PRISM_BUSINESS_RULES") ?: System.getenv("PRISM_BUSINESS_RULES")
            String ruleServiceProvider = "com.rules4j.Rules4JServiceProvider";
            //try {
                ruleServiceProviderClass = Class.forName(ruleServiceProvider);
            /*}
            catch (ClassNotFoundException e) {
                log.error("Error in BusinessRulesEngine static block", e);
            }*/
            RuleServiceProviderManager.registerRuleServiceProvider(ruleServiceProvider, ruleServiceProviderClass);
            RuleServiceProvider serviceProvider = RuleServiceProviderManager.getRuleServiceProvider(ruleServiceProvider);
            if (ruleServer == null) {
                ruleServer = (Rules4JServer) serviceProvider.getRuleRuntime();
                if (!ruleServer.isInitialized()) { // if the ruleserver has not been initialized, initialize it.
                    log.info("Loading Rules4J Server Config from: " + rules4JFilesDirectory)
                    ruleServer.loadServerConfiguration(rules4JFilesDirectory, "rules4JServer.xml");
                }
            }
            ruleSetNames = ruleServer.getRegistrations("MDH_Business_Rules_Server");
            // gets the rulesetnames for the "MaterialsMgmtRulesServer" rulebase

        }catch(Exception e){
            log.error("Error in BusinessRulesEngine static block", e);
        }

    } //end of static block

    public BusinessRulesEngine() { }

    public void applyStockItemModificationRules(StockItemActionRequest siar) throws InfrastructureException {
        this.currentRuleSetName = "MDH_Business_Rules_Server:MaterialsMgmtRules:StockItemModificationRuleset";

        try {
            currentRuleSession = (Rules4JStatefulRuleSession)ruleServer.createRuleSession(currentRuleSetName, null, RuleRuntime.STATEFUL_SESSION);
            currentRuleSession.addObject(siar);
            currentRuleSession.loadResourceObject("Status", new Status());
            currentRuleSession.loadResourceObject("StatusType", new StatusType());
            currentRuleSession.loadResourceObject("ActionRequestType", new ActionRequestType());
            currentRuleSession.loadResourceObject("Notification", new Notification());

            currentRuleSession.executeRules();
            currentRuleSession.reset();

            if(siar.getApprovalRequired()) {
                siar.prepareForEvaluation();
            }
            else {
                siar.approve();
            }
        }
        catch(InvalidRuleSessionException irse) {
            log.error(irse);
        }
    }
    
    /**
     * 
     * @param request
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void applyMaterialsRequestRules(Request request, Boolean bCheck) throws InfrastructureException {
        for (Object o : request.getRequestLineItems()) {
            RequestLineItem rli = (RequestLineItem) o;
            if (rli.getItem() == null) { //handle non-catalog item requests
                PurchaseItem purchaseItem = new PurchaseItem();
                purchaseItem.setCategory(rli.getItemCategory());

                // Not looking at quotes for now. Need to resolve edit issues first.
//                if((rli.getItemCost() <= 1.00 || rli.getAttachedFileNonCats().isEmpty()) && rli.determineIfItPurchase()){
//                if(rli.getItemCost() <= 1.00 && rli.determineIfItPurchase()){
//                    DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
//                    StatusDAO statusDAO = daoFactory.getStatusDAO();
//                    rli.createHelpDeskTicketNeedsQuote(request);
//                    rli.setStatus(statusDAO.getStatusByStatusCode(Status.PENDING_NEED_MORE_INFO));
//
//                    continue;
//                } else {
                    applyRequestRulesORCL(rli, purchaseItem.getCategory().getCategoryCode().toUpperCase(), "PurchaseItem");
//                }
            } else if (rli.getItem() instanceof StockItem) {
                StockItem stockItem = (StockItem) rli.getItem();
                if (bCheck) {
                    applyRequestRulesORCL(rli, stockItem.getCategory().getCategoryCode().toUpperCase(), "StockItem");
                } else {
                    // All stock Items set to false.
                    rli.setApprovalRequired(false);
                }
            } else if (rli.getItem() instanceof PurchaseItem) {
                PurchaseItem purchaseItem = (PurchaseItem) rli.getItem();
                applyRequestRulesORCL(rli, purchaseItem.getCategory().getCategoryCode().toUpperCase(), "PurchaseItem");
            } else if (rli.getItem() instanceof Item) {
                Item purchaseItem = (Item) rli.getItem();
                applyRequestRulesORCL(rli, purchaseItem.getCategory().getCategoryCode().toUpperCase(), "PurchaseItem");
            }




            if (rli.getApprovalRequired()) {
                rli.prepareForEvaluation();
            } else {
                rli.approve("WFP");
            }
        }
    }

    /**
     * There should only be one pre-defined funding source for a stock item request.  The funding source is the 
     * StockItem's org budget.
     * @param rli
     * @param item
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    private void applyStockItemRequestRules(RequestLineItem rli, StockItem item) throws InfrastructureException {
        this.currentRuleSetName = "MDH_Business_Rules_Server:MaterialsMgmtRules:StockItemRequestRuleset"; 
        try {            
            currentRuleSession = (Rules4JStatefulRuleSession)ruleServer.createRuleSession(currentRuleSetName, null, RuleRuntime.STATEFUL_SESSION);
            currentRuleSession.addObject(rli);
            currentRuleSession.loadResourceObject("Notification", new Notification());  
            currentRuleSession.loadResourceObject("StockItem", item);
            currentRuleSession.executeRules();
            currentRuleSession.reset();                      
        }
        catch(InvalidRuleSessionException irse) {
            log.error(irse);
        } 
    }

    private void applyRequestRulesORCL(RequestLineItem rli, String catCode, String inType) throws InfrastructureException {
        try {
            // Load Business Rule Object MaterialsMgmtRules
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
            //Get the business rules for PurchaseItem
            List<BusinessRulesORCL> businessRules = daoFactory.getBusinessRulesORCLDAO().findByExample(inType);
            // For the Request Line Item see if it requires validation by category code
            boolean bRuleFound = false;

            for (Object o : businessRules) {
                BusinessRulesORCL bRules = (BusinessRulesORCL) o;

                if (catCode.equals(bRules.getObjectValue())) {
                    bRuleFound = true;
                    if (bRules.getApprovalLevel() == Constants.APPROVAL_LEVEL_ONE) {
                        rli.getEvaluatorGroupCodes().add(bRules.getPrimaryEvaluator());
                        log.info("Notify " + bRules.getPrimaryEvaluator());
                    } else if (bRules.getApprovalLevel() == Constants.APPROVAL_LEVEL_TWO) {
                        rli.getEvaluatorGroupCodesLevelTwo().add(bRules.getPrimaryEvaluator());
                        log.info("Notify " + bRules.getPrimaryEvaluator());
                    } else {     //todo tr leave this as default for now - clean up later
                        rli.getEvaluatorGroupCodes().add(bRules.getPrimaryEvaluator());
                        log.info("Notify " + bRules.getPrimaryEvaluator());
                    }
                }
            }
            rli.setApprovalRequired(bRuleFound);

            businessRules = daoFactory.getBusinessRulesORCLDAO().findByExample("ORGCODE");
            businessRules.addAll(daoFactory.getBusinessRulesORCLDAO().findByExample("ORGID"));

            for(RequestLineItemFundingSource fundingSource : rli.getFundingSources()){
                for(Object businessRule : businessRules){
                    BusinessRulesORCL rulesORCL = (BusinessRulesORCL) businessRule;
                    if (fundingSource.orgBudget.orgBudgetCode.equals(rulesORCL.getObjectValue()) || fundingSource.orgBudget.orgBudgetId.equals(rulesORCL.getOrgBudgetId())) {
                        // Changed to bypass approvals for stock items.
                        if ((rli.estimatedCost >= rulesORCL.minimumAmount || rli.estimatedCost <= 0) && !(rli.item instanceof StockItem)) {
                            bRuleFound = true;
                            rli.getEvaluatorGroupCodes().add(rulesORCL.getPrimaryEvaluator());
                            rli.setApprovalRequired(bRuleFound);
                            log.info("Notify " + rulesORCL.getPrimaryEvaluator());
                        }
                    }
                }
            }
        } catch(Exception e) {
            log.error(e);
        }
    }
}