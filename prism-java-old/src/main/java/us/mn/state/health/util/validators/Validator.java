package us.mn.state.health.util.validators;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.FieldChecks;
import org.apache.struts.validator.Resources;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.model.common.Group;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.StockItem;
import us.mn.state.health.model.materialsrequest.AttachedFileNonCat;
import us.mn.state.health.security.ApplicationResources;
import us.mn.state.health.view.inventory.StockItemActionRequestForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemForm;
import us.mn.state.health.view.materialsrequest.RequestLineItemFundingSourceForm;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

public class Validator extends FieldChecks implements Serializable {
    private static final Log log = LogFactory.getLog(Validator.class);
    
    /**
     * Ensures that the user specified a Qty On-Hand Change reason when/if they modified the Qty On Hand.
     * @return boolean
     * @param bean      Bean validation is being performed on.
     * @param va        ValidatorAction being performed.
     * @param field     Field object being validated.
     * @param errors    The errors objects to add an ActionError to if the validation fails.
     * @param request   Current request object.
     */
    public boolean validateQtyOnHandChangeReasonRequired(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        String stkQtyChangeReasonRefId = field.getProperty().trim();
        StockItemActionRequestForm siarForm = (StockItemActionRequestForm)bean;
        String changeReasonIdValue = siarForm.getStkQtyChangeReasonRefId();
        String newQtyOnHand = siarForm.getPotentialStockItemForm().getQtyOnHand().trim();
        ActionMessage _message = Resources.getActionMessage(request, va, field);
        
        if(StringUtils.nullOrBlank(newQtyOnHand) || !StringUtils.isNumeric(newQtyOnHand)) {
            //if there is no 'new' qoh or if its not numeric, that's an error (on the user's part) 
            //it shouldn't be blank though - we have another validation for this - its a required field
            ActionMessage actionMessage = new ActionMessage(field.getKey());
            errors.add(stkQtyChangeReasonRefId, actionMessage);
        }      
        else {
            Integer newQOH_Int = new Integer(newQtyOnHand);
            if(!newQOH_Int.equals(siarForm.getStockItem().getQtyOnHand()) && 
               StringUtils.nullOrBlank(changeReasonIdValue)) {
               //if the qoh value was changed, but no reason was entered, that's an error
               ActionMessage actionMessage = new ActionMessage(_message.getKey(), stkQtyChangeReasonRefId);
               errors.add(stkQtyChangeReasonRefId, actionMessage);
            }
        }
        return (errors.isEmpty());
    }

     public boolean validateItemJustification(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {

        boolean returnValue = true;
    /**
     * This validation method will be used by the form "requestForm" of the type
     * us.mn.state.health.view.materialsrequest.RequestForm
     * to validate if each RLI has a valid quantity (an integer greater than 0)
     *
     * @param bean    - requestForm
     * @param va
     * @param field   - requestLineItemForms
     * @param errors
     * @param request
     * @return true if the condition is satisfied or false otherwise
     */

        ActionMessage _message = Resources.getActionMessage(request, va, field);
        String _property = field.getKey();
        String itemJustification = request.getParameter(_property);
        String categoryId = request.getParameter("categoryId");
        String itemCost = request.getParameter("itemCost");

         // If computer purchase or software and the cost > 1000 then a justification is required.
         if (categoryId != null && (categoryId.equals("62968") || categoryId.equals("528041")) && itemCost != null && Double.valueOf(itemCost) >= 1000 &&
                 (itemJustification == null || itemJustification.length() < 5) ) {
                 errors.add(_property, _message);
                 returnValue = false;
         }
        return returnValue;
     }
    public boolean validateNoteRequiredOnPARIT(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {

        boolean returnValue = true;
        /**
         * This validation method will be used by the form "requestForm" of the type
         * us.mn.state.health.view.materialsrequest.RequestForm
         * to validate if each RLI has a valid quantity (an integer greater than 0)
         *
         * @param bean    - requestForm
         * @param va
         * @param field   - requestLineItemForms
         * @param errors
         * @param request
         * @return true if the condition is satisfied or false otherwise
         */

        ActionMessage _message = Resources.getActionMessage(request, va, field);
        String _property = field.getKey();
        String note = request.getParameter(_property);
        String cmd = request.getParameter("cmd");
        String skin = (String)request.getSession().getAttribute("skin");
        String shoppingListAction = ((RequestLineItemForm) bean).getShoppingListAction();

        // If computer purchase or software and the cost > 1000 then a justification is required.
        if (cmd != null && cmd.equalsIgnoreCase("addToCart") && skin.equalsIgnoreCase("PRISM2") && shoppingListAction.equalsIgnoreCase("AddCatReqLnItm")) {
                if (note == null || note.length() < 5) {
                    errors.add(_property, _message);
                    returnValue = false;
                }
        }
        return returnValue;
    }

    public boolean validateAttachmentRequiredIfComputerNonCatalogRequest(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {

        boolean returnValue = true;
        String commandProperty = field.getVarValue("commandProperty").trim();
        String commandValue = field.getVarValue("commandValue").trim();

        ActionMessage _message = Resources.getActionMessage(request, va, field);
        String _property = field.getKey();
        Collection requestLineItemForms = null;
        String currentCommand = null;
        try {
            requestLineItemForms = (Collection) PropertyUtils.getProperty(bean, field.getProperty());
            currentCommand = PropertyUtils.getNestedProperty(bean, commandProperty).toString();
        } catch (Exception e) {
            log.error("Error in Validator.validateAttachments",e);
            e.printStackTrace();
        }

        if (commandValue.equalsIgnoreCase(currentCommand)) {
            for (Iterator iterator = requestLineItemForms.iterator(); iterator.hasNext(); ) {
                RequestLineItemForm requestLineItemForm = (RequestLineItemForm) iterator.next();
                if (requestLineItemForm.getCategoryId().equals("600100")) {
                    Collection<AttachedFileNonCat> attachment = requestLineItemForm.getRequestLineItem().getAttachedFileNonCats();

                    if (attachment == null || attachment.size()<= 0) {
                        errors.add(_property, _message);
                        returnValue = false;
                    }
                }
            }
        }
        return returnValue;
    }
    public boolean validatePORequiredIfContractOrStaffing(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {

        boolean returnValue = true;
        /**
         * This validation method will be used by the form "requestForm" of the type
         * us.mn.state.health.view.materialsrequest.RequestForm
         * to validate if each RLI has a valid quantity (an integer greater than 0)
         *
         * @param bean    - requestForm
         * @param va
         * @param field   - requestLineItemForms
         * @param errors
         * @param request
         * @return true if the condition is satisfied or false otherwise
         */

        ActionMessage _message = Resources.getActionMessage(request, va, field);
        String poNum = field.getVarValue("poNumber");


        String poNumber = null;
        if (poNum != null) {
            poNum = poNum.trim();
            poNumber = ValidatorUtils.getValueAsString(
                    bean,
                    poNum);
        }



        String quantity = field.getVarValue("quantity");
        String value2 = null;
        if (quantity != null) {
            quantity = quantity.trim();
             value2 = ValidatorUtils.getValueAsString(
                    bean,
                    quantity);
        }


        String type = field.getVarValue("typeOf");
        String typeof = null;
        if (type != null) {
            type = type.trim();
            typeof = ValidatorUtils.getValueAsString(
                    bean,
                    type);

        }
        // If computer purchase or software and the cost > 1000 then a justification is required.
        //int q = Integer.parseInt(value2);
        if (typeof != null && (typeof.equalsIgnoreCase("STAFFAUG") || typeof.equalsIgnoreCase("MNITCONTRACT") || typeof.equalsIgnoreCase("WAN/Computing Services"))) {
            if (poNumber == null || poNumber.equals("")) {
                errors.add(field.getProperty(), _message);
                returnValue = false;
            }
        }
        return returnValue;

    }
    public boolean validateItemJustification2(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {

       boolean returnValue = true;
   /**
    * This validation method will be used by the form "requestForm" of the type
    * us.mn.state.health.view.materialsrequest.RequestForm
    * to validate if each RLI has a valid quantity (an integer greater than 0)
    *
    * @param bean    - requestForm
    * @param va
    * @param field   - requestLineItemForms
    * @param errors
    * @param request
    * @return true if the condition is satisfied or false otherwise
    */

       ActionMessage _message = Resources.getActionMessage(request, va, field);
       String _property = field.getKey();
       String itemJustification = request.getParameter(_property);

        // If computer purchase or software and the cost > 1000 then a justification is required.
        if (itemJustification != null && itemJustification.length() > 2000) {
                errors.add(_property, _message);
                returnValue = false;
        }
       return returnValue;

    }

    public boolean validateQuantitiesRequestForm(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        boolean returnValue = true;

        ActionMessage _message = Resources.getActionMessage(request, va, field);
        String _property = field.getKey();
        Collection requestLineItemForms = null;
        try {
            requestLineItemForms = (Collection) PropertyUtils.getProperty(bean, field.getProperty());
        }
        catch (IllegalAccessException e) {
            log.error("Error in Validator.validateQuantitiesRequestForm",e);
        }
        catch (InvocationTargetException e) {
            log.error("Error in Validator.validateQuantitiesRequestForm",e);
        }
        catch (NoSuchMethodException e) {
            log.error("Error in Validator.validateQuantitiesRequestForm",e);
        }
        for (Iterator iterator = requestLineItemForms.iterator(); iterator.hasNext();) {
            RequestLineItemForm requestLineItemForm = (RequestLineItemForm) iterator.next();
            String quantity = requestLineItemForm.getQuantity().trim();
            int q;
            try {
                if(!requestLineItemForm.getRemove()) {
                    q = Integer.parseInt(quantity);
//                    if (q <= 0) {
//                        throw new NumberFormatException();
//                    }
                }
            }
            catch (NumberFormatException e) {
                String itemDescription =
                        (requestLineItemForm.getItemPresent()) ? requestLineItemForm.getItem().getDescription() : requestLineItemForm.getItemDescription();
                ActionMessage actionMessage = new ActionMessage(_message.getKey(), itemDescription);
                errors.add(_property, actionMessage);
                returnValue = false;
            }
        }
        return returnValue;
    }

    /**
     * This validation method will be used by the form "requestForm" of the type
     * us.mn.state.health.view.materialsrequest.RequestForm
     * to validate if each RLI has a valid item cost
     * for non-catalog items
     *
     * @param bean    - requestForm
     * @param va
     * @param field   - requestLineItemForms
     * @param errors
     * @param request
     * @return true if the condition is satisfied or false otherwise
     */
    public boolean validateItemCostForNonCatalogRLI(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        boolean returnValue = true;

        ActionMessage _message = Resources.getActionMessage(request, va, field);
        String _property = field.getKey();
        Collection requestLineItemForms = null;
        try {
            requestLineItemForms = (Collection) PropertyUtils.getProperty(bean, field.getProperty());
        }
        catch (IllegalAccessException e) {
            log.error("Error in Validator.validateItemCostForNonCatalogRLI",e);
        }
        catch (InvocationTargetException e) {
            log.error("Error in Validator.validateItemCostForNonCatalogRLI",e);
        }
        catch (NoSuchMethodException e) {
            log.error("Error in Validator.validateItemCostForNonCatalogRLI",e);
        }

        for (Iterator iterator = requestLineItemForms.iterator(); iterator.hasNext();) {
            RequestLineItemForm requestLineItemForm = (RequestLineItemForm) iterator.next();
            if (!requestLineItemForm.getItemPresent()) {
                String itemCost = requestLineItemForm.getItemCost();
                double ic = 0;
                try {
                    ic = Double.parseDouble(itemCost);
                    if (ic <= 0) {
                        throw new NumberFormatException();
                    }
                }
                catch (NumberFormatException e) {
                    String itemDescription =
                            (requestLineItemForm.getItemPresent()) ? requestLineItemForm.getItem().getDescription() : requestLineItemForm.getItemDescription();
                    ActionMessage actionMessage = new ActionMessage(_message.getKey(), itemDescription);
                    errors.add(_property, actionMessage);
                    returnValue = false;
                }
            }
        }
        return returnValue;
    }


    /**
     * This validation method will be used by the form "requestLineItemForm" of type
     * us.mn.state.health.view.materialsrequest.RequestLineItemForm
     * to enforce at least one funding source  if the user is in the group "Purchasing Coordinator"
     *
     * @param bean    - requestLineItemForm
     * @param va
     * @param field   - fundingSourceForms
     * @param errors
     * @param request
     * @return true if the condition is satisfied or false otherwise
     */
    public boolean validateAtLeastOneFSforPurchaseCoordinators(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        boolean returnValue = false;
        User user = (User) request.getSession().getAttribute(ApplicationResources.USER);
        if (user != null && user.isInGroup(Group.PURCHASE_COORDINATOR_CODE)) {
            try {
                Collection fundingSourceForms = (Collection) PropertyUtils.getProperty(bean, field.getProperty());
                for (Iterator iterator = fundingSourceForms.iterator(); iterator.hasNext();) {
                    RequestLineItemFundingSourceForm requestLineItemFundingSourceForm = (RequestLineItemFundingSourceForm) iterator.next();
                    String orgBudgetId = requestLineItemFundingSourceForm.getOrgBudgetId();
                    if (orgBudgetId != null && !"".equals(orgBudgetId)) {
                        returnValue = true;
                    }
                }
            }
            catch (IllegalAccessException e) {
                log.error("Error in Validator.validateAtLeastOneFSforPurchaseCoordinators",e);
            }
            catch (InvocationTargetException e) {
                log.error("Error in Validator.validateAtLeastOneFSforPurchaseCoordinators",e);
            }
            catch (NoSuchMethodException e) {
                log.error("Error in Validator.validateAtLeastOneFSforPurchaseCoordinators",e);
            }
        } else {
            returnValue = true;
        }
        if (!returnValue) {
            ActionMessage actionMessage = Resources.getActionMessage(request, va, field);
            errors.add(field.getKey(), actionMessage);
        }
        return returnValue;
    }


    /**
     * This validation method will be used by the form "requestLineItemForm" of type
     * us.mn.state.health.view.materialsrequest.RequestLineItemForm
     * to check if the property 'amountInDollars' is set to percent if a cost is not provided
     * for a non-catalog item
     *
     * @param bean    - requestLineItemForm
     * @param va
     * @param field   - the 'amountInDollars' property
     * @param errors
     * @param request
     * @return true if the condition is satisfied or false otherwise
     */
    public static boolean validateRequestLineItemForAmountInDollars(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        try {
            Object property = PropertyUtils.getProperty(bean, field.getProperty());
            if (property == null) {
                return false;
            }
            Boolean amountInDollars = (Boolean) property;
            String itemCost = (String) PropertyUtils.getProperty(bean, "itemCost");
            if (StringUtils.nullOrBlank(itemCost)) {
                if (amountInDollars) {
                    errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
                    return false;
                }
            }
        }
        catch (IllegalAccessException e) {
            log.error("Error in Validator.validateRequestLineItemForAmountInDollars",e);
        }
        catch (InvocationTargetException e) {
            log.error("Error in Validator.validateRequestLineItemForAmountInDollars",e);
        }
        catch (NoSuchMethodException e) {
            log.error("Error in Validator.validateRequestLineItemForAmountInDollars",e);
        }

        return true;
    }

    /**
     * This validation method will be used by the form "requestLineItemForm" of type
     * us.mn.state.health.view.materialsrequest.RequestLineItemForm
     * to check if each amount of each object in the field "fundingSourceForms" is a number greater than 0
     *
     * @param bean    - requestLineItemForm
     * @param va
     * @param field   - the collection "fundingSourceForms"
     * @param errors
     * @param request
     * @return true if the conditions are satisfied, false otherwise
     */
    public static boolean validateRequestLineItemFormAmounts(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        String commandProperty = field.getVarValue("commandProperty").trim();
        String commandValue = field.getVarValue("commandValue").trim();




        String _property = field.getKey();
        ActionMessage _message = Resources.getActionMessage(request, va, field);
//        RequestLineItemForm requestLineItemForm = (RequestLineItemForm) bean;

        boolean returnValue = true;

        try {
            Double itemCost = Double.valueOf(PropertyUtils.getNestedProperty(bean, "itemCost").toString());
            Double quantity = Double.valueOf(PropertyUtils.getNestedProperty(bean, "quantity").toString());
            if(commandValue.contains(PropertyUtils.getNestedProperty(bean, commandProperty).toString())) {
                Collection fundingSourceForms = (Collection) PropertyUtils.getProperty(bean, field.getProperty());
                Iterator fundingSourceFormsIterator = fundingSourceForms.iterator();
                BigDecimal amount = BigDecimal.ZERO;
                while(fundingSourceFormsIterator.hasNext()) {
                    RequestLineItemFundingSourceForm rlifsf = (RequestLineItemFundingSourceForm) fundingSourceFormsIterator.next();
                    OrgBudget orgBudget = rlifsf.getOrgBudget();
//                    if(orgBudget==null){
//                        continue;
//                    }
                    //We have to validate here if the amount property is a double
                    try {
                        //double v = new Double(rlifsf.getAmount()).doubleValue();
                        BigDecimal v = BigDecimal.valueOf(new Double(rlifsf.getAmount()).doubleValue());







                        if ((v.compareTo(BigDecimal.ZERO) > 0) && orgBudget == null) {
                            ActionMessage m = new ActionMessage(_message.getKey(), "Empty Budget");
                            errors.add(_property, m);
                            returnValue = false;
                        }
//                        else {
                           amount = amount.add(v);
//                        }
                    }
                    catch(NumberFormatException e) {
                        if(orgBudget != null) {
                            ActionMessage m = new ActionMessage(_message.getKey(), orgBudget.getName());
                            errors.add(_property, m);
                        }
                        returnValue = false;
                    }
                }


                BigDecimal bdCost = BigDecimal.valueOf(itemCost);
                BigDecimal bdQuantity = BigDecimal.valueOf(quantity);

                if (amount.compareTo(bdCost.multiply(bdQuantity))!= 0) {
                    ActionMessage m = new ActionMessage(_message.getKey(), "Budget");
                    errors.add(_property, m);
                    returnValue = false;
                }
            }

        }
        catch (IllegalAccessException e) {
            log.error("Error in Validator.validateRequestLineItemFormAmounts",e);
        }
        catch (InvocationTargetException e) {
            log.error("Error in Validator.validateRequestLineItemFormAmounts",e);
        }
        catch (NoSuchMethodException e) {
            log.error("Error in Validator.validateRequestLineItemFormAmounts",e);
        }
        return returnValue;
    }

    public static boolean validateApproverNotRequestor(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
//        String commandProperty = field.getVarValue("commandProperty");
//        String commandValue = field.getVarValue("commandValue");

//        String _property = field.getKey();
        String _property = field.getKey();
        boolean returnValue = true;
        try {
            List<RequestLineItemForm> requestLineItemForms = (List<RequestLineItemForm>) PropertyUtils.getProperty(bean, field.getProperty());
            User user = (User) request.getSession().getAttribute(ApplicationResources.USER);

            for (int i = 0; i < requestLineItemForms.size(); i++) {
                RequestLineItemForm requestLineItemForm = requestLineItemForms.get(i);
                if (requestLineItemForm.getRequest().getRequestor().getPersonId().longValue() == user.getPersonId().longValue()) {
                    ActionMessage _message = Resources.getActionMessage(request, va, field);
                    errors.add(_property, _message);
                    returnValue = false;
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            returnValue = false;
        } catch (InvocationTargetException e) {
            returnValue = false;
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            returnValue = false;
            e.printStackTrace();
        }



        return returnValue;
    }

    public static boolean validateEvaluationSelection(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        String _property = field.getKey();
        boolean returnValue = true;
        try{
            List<RequestLineItemForm> requestLineItemForms = (List<RequestLineItemForm>) PropertyUtils.getProperty(bean, field.getProperty());
            Map requestParameterMap = request.getParameterMap();

            for (int i = 0; i < requestLineItemForms.size(); i++) {
                List validSelections = Arrays.asList(null,0,1,2);
                String[] approvedParam = (String[]) requestParameterMap.get("requestLineItemForms["+i+"].approved");
                if (approvedParam == null){
                    break;
                }
                Integer selection = Integer.parseInt(approvedParam[0]);
                if (!validSelections.contains(selection)) {
                    ActionMessage _message = Resources.getActionMessage(request, va, field);
                    errors.add(_property, _message);
                    returnValue = false;
                    break;
                }
            }
        }catch(Exception e){
            ActionMessage _message = Resources.getActionMessage(request, va, field);
            errors.add(_property, _message);
            returnValue = false;
            e.printStackTrace();
        }

        return returnValue;
    }
    /**
     * This validation method will be used by the form "requestForm" of type
     * us.mn.state.health.view.materialsrequest.RequestForm
     * to check if the amount of each object in the field "requestLineItemForms" is a number greater than 0
     *
     * @param bean    - requestForm
     * @param va
     * @param field   - the collection "requestLineItemForms"
     * @param errors
     * @param request
     * @return true if the conditions are satisfied, false otherwise
     */
    public static boolean validateRequestFormAmountPositiveDoubleValue(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        String commandProperty = field.getVarValue("commandProperty").trim();
        String commandValue = field.getVarValue("commandValue").trim();

        String _property = field.getKey();
        ActionMessage _message = Resources.getActionMessage(request, va, field);

        boolean returnValue = true;

        try {
            if (commandValue.contains(PropertyUtils.getNestedProperty(bean, commandProperty).toString())) {

                Collection requestLineItemForms = (Collection) PropertyUtils.getProperty(bean, field.getProperty());

                Iterator i = requestLineItemForms.iterator();
                while (i.hasNext()) {
                    RequestLineItemForm requestLineItemForm = (RequestLineItemForm) i.next();
                    if(requestLineItemForm.getEstimatedCost() > 0) {
                        Collection fundingSourceForms = (Collection) requestLineItemForm.getFundingSourceForms();
                        Iterator fundingSourceFormsIterator = fundingSourceForms.iterator();
                        double amount = 0;
                        while (fundingSourceFormsIterator.hasNext()) {
                            RequestLineItemFundingSourceForm rlifsf = (RequestLineItemFundingSourceForm) fundingSourceFormsIterator.next();
                            //We have to validate here if the amount property is a double
                            if (!StringUtils.nullOrBlank(rlifsf.getOrgBudgetId())) {
                                String name = rlifsf.getOrgBudget().getName();
                                String description = (requestLineItemForm.getItemPresent()) ? requestLineItemForm.getItem().getDescription() : requestLineItemForm.getItemDescription();
                                try {
                                    double v = new Double(rlifsf.getAmount());
//                                    if (v <= 0) {
//                                        ActionMessage m = new ActionMessage(_message.getKey(), name, description);
//                                        errors.add(_property, m);
//                                        returnValue = false;
//                                    } else {
                                        amount += v;
//                                    }
                                }
                                catch (NumberFormatException e) {
                                    ActionMessage m = new ActionMessage(_message.getKey(), name, description);
                                    errors.add(_property, m);
                                    returnValue = false;
                                }
                            }
                        }
                        log.debug("Total Amount in validator=" + amount);
                    }
                }
            }
        }
        catch (IllegalAccessException e) {
            log.error("Error in Validator.validateRequestFormAmountPositiveDoubleValue",e);
        }
        catch (InvocationTargetException e) {
            log.error("Error in Validator.validateRequestFormAmountPositiveDoubleValue",e);
        }
        catch (NoSuchMethodException e) {
            log.error("Error in Validator.validateRequestFormAmountPositiveDoubleValue",e);
        }

        return returnValue;
    }

    /**
     *
     * @param bean - RequestForm
     * @param va
     * @param field - requestLineItemForms
     * @param errors
     * @param request
     * @return true if the each non-catalog RLI has the unit not-null or false otherwise
     */

    public static boolean validateNonCatalogRLItemsForUnit(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request){
        String commandProperty = field.getVarValue("commandProperty").trim();
        String commandValue = field.getVarValue("commandValue").trim();
        String _property = field.getKey();
        ActionMessage _message = Resources.getActionMessage(request, va, field);

        boolean returnValue = true;

        try {
            if (commandValue.contains(PropertyUtils.getNestedProperty(bean, commandProperty).toString())) {

                Collection requestLineItemForms = (Collection) PropertyUtils.getProperty(bean, field.getProperty());

                Iterator i = requestLineItemForms.iterator();
                while (i.hasNext()) {
                    RequestLineItemForm requestLineItemForm = (RequestLineItemForm) i.next();

                    if (!requestLineItemForm.getRemove()) {
                        if(!requestLineItemForm.getItemPresent()&& StringUtils.nullOrBlank(requestLineItemForm.getUnitId())){
                            returnValue = false;
                            String description = (requestLineItemForm.getItemPresent()) ? requestLineItemForm.getItem().getDescription() : requestLineItemForm.getItemDescription();
                            ActionMessage m = new ActionMessage(_message.getKey(), description);
                            errors.add(_property, m);

                        }
                    }
                }
            }
        }
        catch (IllegalAccessException e) {
            log.error("Error in Validator.validateNonCatalogRLItemsForUnit",e);
        }
        catch (InvocationTargetException e) {
            log.error("Error in Validator.validateNonCatalogRLItemsForUnit",e);
        }
        catch (NoSuchMethodException e) {
            log.error("Error in Validator.validateNonCatalogRLItemsForUnit",e);
        }

        return returnValue;
    }

    /**
     *
     * @param bean - RequestForm
     * @param va
     * @param field - requestLineItemForms
     * @param errors
     * @param request
     * @return true if the each non-catalog RLI has the category not-null or false otherwise
     */

    public static boolean validateNonCatalogRLItemsForCategory(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request){
        String commandProperty = field.getVarValue("commandProperty").trim();
        String commandValue = field.getVarValue("commandValue").trim();
        String _property = field.getKey();
        ActionMessage _message = Resources.getActionMessage(request, va, field);

        boolean returnValue = true;

        try {
            if (commandValue.contains(PropertyUtils.getNestedProperty(bean, commandProperty).toString())) {

                Collection requestLineItemForms = (Collection) PropertyUtils.getProperty(bean, field.getProperty());

                Iterator i = requestLineItemForms.iterator();
                while (i.hasNext()) {
                    RequestLineItemForm requestLineItemForm = (RequestLineItemForm) i.next();

                    if(!requestLineItemForm.getItemPresent()&& StringUtils.nullOrBlank(requestLineItemForm.getCategoryId())){
                        returnValue = false;
                        String description = (requestLineItemForm.getItemPresent()) ? requestLineItemForm.getItem().getDescription() : requestLineItemForm.getItemDescription();
                        ActionMessage m = new ActionMessage(_message.getKey(), description);
                        errors.add(_property, m);

                    }
                }
            }
        }
        catch (IllegalAccessException e) {
            log.error("Error in Validator.validateNonCatalogRLItemsForCategory",e);
        }
        catch (InvocationTargetException e) {
            log.error("Error in Validator.validateNonCatalogRLItemsForCategory",e);
        }
        catch (NoSuchMethodException e) {
            log.error("Error in Validator.validateNonCatalogRLItemsForCategory",e);
        }

        return returnValue;
    }



    /**
     * This validation method will be used by the form "evaluateRequestForm" of type
     * us.mn.state.health.view.materialsrequest.RequestForm
     * to check if the amount of each object in the field "requestLineItemForms" is a number greater than 0
     *
     * @param bean    - evaluateRequestForm
     * @param va
     * @param field   - the collection "requestLineItemForms"
     * @param errors
     * @param request
     * @return true if the conditions are satisfied, false otherwise
     */
    public static boolean validateEvaluateRequestFormAmountPositiveDoubleValue(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        String _property = field.getKey();
        ActionMessage _message = Resources.getActionMessage(request, va, field);

        boolean returnValue = true;

        try {
            Collection requestLineItemForms = (Collection) PropertyUtils.getProperty(bean, field.getProperty());

            Iterator i = requestLineItemForms.iterator();
            while (i.hasNext()) {
                RequestLineItemForm requestLineItemForm = (RequestLineItemForm) i.next();
                boolean validate = validateForMaterialRequestsEvaluation(requestLineItemForm);
                if (!validate && requestLineItemForm.getApproved() != null && requestLineItemForm.getApproved() != 0  ) {
                    continue;
                }
                
                if (requestLineItemForm.getRliDenialReason() == null || requestLineItemForm.getRliDenialReason().equals("")
                    && requestLineItemForm.getApproved() != null && requestLineItemForm.getApproved() == 0) {
                       returnValue = false;
                        String description = "Denial Reason";
                        ActionMessage m = new ActionMessage("errors.rliDenialReason");
                        errors.add(_property, m);
                }
                Collection fundingSourceForms = (Collection) requestLineItemForm.getFundingSourceForms();
                Iterator fundingSourceFormsIterator = fundingSourceForms.iterator();
                double amount = 0;
                while (fundingSourceFormsIterator.hasNext()) {
                    RequestLineItemFundingSourceForm rlifsf =
                            (RequestLineItemFundingSourceForm) fundingSourceFormsIterator.next();
                    //We have to validate here if the amount property is a double
                    if (!StringUtils.nullOrBlank(rlifsf.getOrgBudgetId())) {
                        String name = rlifsf.getOrgBudget().getName();
                        String description = (requestLineItemForm.getItemPresent()) ? requestLineItemForm.getItem().getDescription() : requestLineItemForm.getItemDescription();
                        try {
                            double v = new Double(rlifsf.getAmount());
//                            if (v <= 0) {
//                                ActionMessage m = new ActionMessage(_message.getKey(), name, description);
//                                errors.add(_property, m);
//                                returnValue = false;
//                            }
//                            else {
                                amount += v;
//                            }
                        }
                        catch (NumberFormatException e) {
                            ActionMessage m = new ActionMessage(_message.getKey(), name, description);
                            errors.add(_property, m);
                            returnValue = false;
                        }
                    }
                }
                log.debug("Total Amount in validator=" + amount);
            }
        }
        catch (IllegalAccessException e) {
            log.error("Error in Validator.validateEvaluateRequestFormAmountPositiveDoubleValue",e);
        }
        catch (InvocationTargetException e) {
            log.error("Error in Validator.validateEvaluateRequestFormAmountPositiveDoubleValue",e);
        }
        catch (NoSuchMethodException e) {
            log.error("Error in Validator.validateEvaluateRequestFormAmountPositiveDoubleValue",e);
        }

        return returnValue;
    }

    /**
     * @return true if this RLIForm must be validated when the RequestForm that contains this rliForm is validated, false otherwise
     */
    private static boolean validateForMaterialRequestsEvaluation(RequestLineItemForm rliForm) {
        Integer approved = rliForm.getApproved();
        return approved != null && approved != 0 && rliForm.getAtLeastOneFundingSourceEnabled();
    }

    public static boolean validateRequestLineItemFormsForEvaluation(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        String _property = field.getKey();
        ActionMessage _message = Resources.getActionMessage(request, va, field);

        boolean returnValue = true;
        try {
            Collection requestLineItemForms = (Collection) PropertyUtils.getProperty(bean, field.getProperty());

            Iterator requestLineItemFormsIterator = requestLineItemForms.iterator();
            while (requestLineItemFormsIterator.hasNext()) {
                RequestLineItemForm requestLineItemForm = (RequestLineItemForm) requestLineItemFormsIterator.next();
                if (validateForMaterialRequestsEvaluation(requestLineItemForm)) {
                    Boolean amountInDollars = requestLineItemForm.getAmountInDollars();
                    double amount = 0;
                    try {
                        amount = calculateTotalAmount(requestLineItemForm);
                    }
                    catch (NumberFormatException e) {
                        log.error("Error in Validator.validateRequestLineItemFormsForEvaluation",e);
                        returnValue = false;
                    }

                    String itemDescription = (requestLineItemForm.getItemPresent()) ? requestLineItemForm.getItem().getDescription() : requestLineItemForm.getItemDescription();
                    String estimatedCost = requestLineItemForm.getEstimatedCost().toString();
                    if(returnValue && calculateNumberOfFundingSourcesPerRLI(requestLineItemForm) >= 0) {
                        returnValue = validateTotalAmountForRLIForm(amountInDollars, amount, requestLineItemForm, _message, itemDescription, errors, _property);
                    }
                }
            }

        }
        catch (IllegalAccessException e) {
            log.error("Error in Validator.validateRequestLineItemFormsForEvaluation",e);
        }
        catch (InvocationTargetException e) {
            log.error("Error in Validator.validateRequestLineItemFormsForEvaluation",e);
        }
        catch (NoSuchMethodException e) {
            log.error("Error in Validator.validateRequestLineItemFormsForEvaluation",e);
        }

        return returnValue;

    }

    /**
     * This validation method will be used by the form "requestForm" of the type
     * us.mn.state.health.view.materialsrequest.RequestForm
     * to check if the field "requestLineItemForms" is valid when we click "Add To Cart/List"
     * Valid means:
     * IF a funding source is provided:
     * -either dollars or amount must be selected
     * -if dollars is selected, the sum of all amounts must be greater than or equal to the RequestLineItemForm.estimatedCost
     * -if percent is selected, the sum of all amounts must equal 100
     *
     * @param bean    - requestForm
     * @param va
     * @param field   - the collection "requestLineItemForms"
     * @param errors
     * @param request
     * @return true if the conditions are satisfied, false otherwise
     */
    public static boolean validateRequestLineItemForms(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        String commandProperty = field.getVarValue("commandProperty").trim();
        String commandValue = field.getVarValue("commandValue").trim();

        String _property = field.getKey();
        ActionMessage _message = Resources.getActionMessage(request, va, field);

        boolean returnValue = true;

        try {
            if (commandValue.contains(PropertyUtils.getNestedProperty(bean, commandProperty).toString())) {
                Collection requestLineItemForms = (Collection) PropertyUtils.getProperty(bean, field.getProperty());

                Iterator requestLineItemFormsIterator = requestLineItemForms.iterator();
                while (requestLineItemFormsIterator.hasNext()) {
                    RequestLineItemForm requestLineItemForm = (RequestLineItemForm) requestLineItemFormsIterator.next();
                    Item item = requestLineItemForm.getStockItem();
                    if (item != null) {
                        continue;
                    }
                    Boolean amountInDollars = requestLineItemForm.getAmountInDollars();
                    double amount = 0;
                    try {
                        amount = calculateTotalAmount(requestLineItemForm);
                    }
                    catch (NumberFormatException e) {
                        log.error("Error in Validator.validateRequestLineItemForms",e);
                        returnValue = false;
                    }

                    String itemDescription = (requestLineItemForm.getItemPresent()) ? requestLineItemForm.getItem().getDescription() : requestLineItemForm.getItemDescription();
                    String estimatedCost = requestLineItemForm.getEstimatedCost().toString();
                    //This is the beginning of the validation for total amount
//                    if (user != null && user.isInGroup(Group.PURCHASE_COORDINATOR_CODE)) {
//                        TODO remove this condition after the refactoring of validateAtLeastOneFundingSourcePerRLIForm
//                        returnValue = validateTotalAmountForRLIForm(amountInDollars
//                                , amount
//                                , requestLineItemForm
//                                , _message
//                                , itemDescription
//                                , estimatedCost
//                                , errors
//                                , _property
//                                , returnValue);
//                    } else {
                    if(returnValue && calculateNumberOfFundingSourcesPerRLI(requestLineItemForm) >= 1) {
                        returnValue = validateTotalAmountForRLIForm(amountInDollars,
                                                                    amount,
                                                                    requestLineItemForm,
                                                                    _message,
                                                                    itemDescription,
                                errors,
                                                                    _property);
                    }
                } //This is the end of the validation for total amount
            }
//            }
        }
        catch (IllegalAccessException e) {
            log.error("Error in Validator.validateRequestLineItemForms",e);
        }
        catch (InvocationTargetException e) {
            log.error("Error in Validator.validateRequestLineItemForms",e);
        }
        catch (NoSuchMethodException e) {
            log.error("Error in Validator.validateRequestLineItemForms",e);
        }

        return returnValue;
    }

    /**
     * This is a helper method that does the core validation for validateRequestLineItemForms(...)
     *
     * @param amountInDollars
     * @param amount
     * @param requestLineItemForm
     * @param _message
     * @param itemDescription
     * @param errors
     * @param _property
     * @return true if the condition is satisfied or false otherwise
     */
    private static boolean validateTotalAmountForRLIForm(Boolean amountInDollars,
                                                         double amount,
                                                         RequestLineItemForm requestLineItemForm,
                                                         ActionMessage _message,
                                                         String itemDescription,
                                                         ActionMessages errors,
                                                         String _property) {
        if (requestLineItemForm.getRemove()) {
            return true;
        }
        if(amountInDollars) {
            BigDecimal estimatedCostBD = new BigDecimal(requestLineItemForm.getEstimatedCost());
            BigDecimal amtDB = new BigDecimal(amount);
            amtDB = amtDB.setScale(2, BigDecimal.ROUND_HALF_UP);
            estimatedCostBD = estimatedCostBD.setScale(2, BigDecimal.ROUND_HALF_UP);
            if(amtDB.compareTo(estimatedCostBD) >= 0) {
                return true;
            }
            else {
                ActionMessage m = new ActionMessage(_message.getKey(), "dollars", itemDescription, estimatedCostBD.toString());
                errors.add(_property, m);
                return false;
            }
        }
        else {
            if (amount == 100) {
                return true;
            }
            else {
                ActionMessage m = new ActionMessage(_message.getKey(), "percents", itemDescription, "100%");
                errors.add(_property, m);
                return false;
            }
        }
    }

    /**
     * Helper method that calculates the total amount provided by the selected orgBudgets
     *
     * @param requestLineItemForm
     * @return the total amount provided by selected orgBudgets
     */
    private static double calculateTotalAmount(RequestLineItemForm requestLineItemForm) throws NumberFormatException {
        Collection fundingSourceForms = (Collection) requestLineItemForm.getFundingSourceForms();
        Iterator fundingSourceFormsIterator = fundingSourceForms.iterator();
        double amount = 0;
        while (fundingSourceFormsIterator.hasNext()) {
            RequestLineItemFundingSourceForm rlifsf = (RequestLineItemFundingSourceForm) fundingSourceFormsIterator.next();
//          skip here the funding sources without a orgBudget
            if (!StringUtils.nullOrBlank(rlifsf.getOrgBudgetId())) {
                amount += new Double(rlifsf.getAmount());
            }
        }
        return amount;
    }

    /**
     * Helper method that calculates how many funding sources are selected for a RLIForm
     *
     * @param requestLineItemForm
     * @return the number of funding sources for a RLIForm
     */
    private static int calculateNumberOfFundingSourcesPerRLI(RequestLineItemForm requestLineItemForm) {
        Collection fundingSourceForms = (Collection) requestLineItemForm.getFundingSourceForms();
        Iterator fundingSourceFormsIterator = fundingSourceForms.iterator();
        int no = 0;
        while (fundingSourceFormsIterator.hasNext()) {
            RequestLineItemFundingSourceForm rlifsf = (RequestLineItemFundingSourceForm) fundingSourceFormsIterator.next();

            //skip here the funding sources without a orgBudget
            if (!StringUtils.nullOrBlank(rlifsf.getOrgBudgetId())) {
                no++;
            }
        }
        return no;
    }

    /**
     * This validation method will be used by the form "requestForm" of the type
     * us.mn.state.health.view.materialsrequest.RequestForm
     * to check if the field "requestLineItemForms" is valid when we click "Add To Cart/List"
     * Valid means:
     * - each RequestLineItemForm must have at least one funding source (the funding source is not optional)
     * only if the user is in group PurchaseCoordinator
     *
     * @param bean    - requestForm
     * @param va
     * @param field   - the collection "requestLineItemForms"
     * @param errors
     * @param request
     * @return true if the condition is satisfied or false otherwise
     */

    public static boolean validateAtLeastOneFundingSourcePerRLIForm(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        String commandProperty = field.getVarValue("commandProperty").trim();
        String commandValue = field.getVarValue("commandValue").trim();

        String _property = field.getKey();
        ActionMessage _message = Resources.getActionMessage(request, va, field);

        boolean returnValue = true;

//        Object user = request.getSession().getAttribute(ApplicationResources.USER);
//        if (user == null || !((Person) user).isInGroup(Group.PURCHASE_COORDINATOR_CODE)) {
//            return true;
//        }

        try {
            if (commandValue.contains(PropertyUtils.getNestedProperty(bean, commandProperty).toString())) {
                Collection requestLineItemForms = (Collection)PropertyUtils.getProperty(bean, field.getProperty());

                Iterator requestLineItemFormsIterator = requestLineItemForms.iterator();
                while(requestLineItemFormsIterator.hasNext()) {
                    RequestLineItemForm requestLineItemForm = (RequestLineItemForm) requestLineItemFormsIterator.next();
                    if(!(requestLineItemForm.getEstimatedCost() > 0)) {
                        continue; //skip the checking for this RLI and go to the next one
                    }
                    Item item = requestLineItemForm.getItem();
                    if(item != null && item instanceof StockItem) {
                        StockItem si = (StockItem)item;
                        if(si.getOrgBudget().getOrgBudgetCode().equals(OrgBudget.INDIRECT) ||
                                si.getDispenseUnitCost() <= 0.0) {
                            continue;//skip the checking for this RLI and go to the next one
                        }
                    }
                    Collection fundingSourceForms = (Collection) requestLineItemForm.getFundingSourceForms();
                    Iterator fundingSourceFormsIterator = fundingSourceForms.iterator();
                    boolean fundingSourceExists = false;
                    while (fundingSourceFormsIterator.hasNext()) {
                        RequestLineItemFundingSourceForm rlifsf = (RequestLineItemFundingSourceForm) fundingSourceFormsIterator.next();
                        if (!StringUtils.nullOrBlank(rlifsf.getOrgBudgetId())) {
                            fundingSourceExists = true;
                            break;
                        }
                    }
                    if (!fundingSourceExists) {
                        String itemDescription =
                                (requestLineItemForm.getItemPresent()) ? requestLineItemForm.getItem().getDescription() : requestLineItemForm.getItemDescription();
                        ActionMessage m = new ActionMessage(_message.getKey(), itemDescription);
                        errors.add(_property, m);
                        returnValue = false;
                    }
                }
            }
        }
        catch (IllegalAccessException e) {
            log.error("Error in Validator.validateAtLeastOneFundingSourcePerRLIForm",e);
        }
        catch (InvocationTargetException e) {
            log.error("Error in Validator.validateAtLeastOneFundingSourcePerRLIForm",e);
        }
        catch (NoSuchMethodException e) {
            log.error("Error in Validator.validateAtLeastOneFundingSourcePerRLIForm",e);
        }

        return returnValue;
    }

    public static boolean validateFieldOfObjectsInCollectionKnown(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        String property = field.getVarValue("property").trim();
        String commandProperty = field.getVarValue("commandProperty").trim();
        String commandValue = field.getVarValue("commandValue").trim();
        try {
            if (PropertyUtils.getNestedProperty(bean, commandProperty).equals(commandValue)) {
                Collection collection = (Collection) PropertyUtils.getProperty(bean, field.getProperty());
                Iterator collectionIterator = collection.iterator();
                String value;

                while (collectionIterator.hasNext()) {
                    try {
                        value = (String) PropertyUtils.getNestedProperty(collectionIterator.next(), property);
                        if (value == null || value.equals("UNKWN")) {
                            errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
                            return false;
                        }
                    }
                    catch (IllegalAccessException e) {
                        log.error("Error in Validator.validateFieldOfObjectsInCollectionKnown",e);
                    }
                    catch (InvocationTargetException e) {
                        log.error("Error in Validator.validateFieldOfObjectsInCollectionKnown",e);
                    }
                    catch (NoSuchMethodException e) {
                        log.error("Error in Validator.validateFieldOfObjectsInCollectionKnown",e);
                    }
                }
            }
        }
        catch (IllegalAccessException e) {
            log.error("Error in Validator.validateFieldOfObjectsInCollectionKnown",e);
        }
        catch (InvocationTargetException e) {
            log.error("Error in Validator.validateFieldOfObjectsInCollectionKnown",e);
        }
        catch (NoSuchMethodException e) {
            log.error("Error in Validator.validateFieldOfObjectsInCollectionKnown",e);
        }

        return true;
    }

    public static Object validateDateIfPropertyEqualsWithValue(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        String property = field.getVarValue("property").trim();
        String propertyValue = field.getVarValue("value").trim();
        String fieldValue = ValidatorUtils.getValueAsString(bean, field.getProperty());
        if (!GenericValidator.isBlankOrNull(property)) {
            try {
                String obtainedPropertyValue = ValidatorUtils.getValueAsString(bean, property);
                if (obtainedPropertyValue.equals(propertyValue) && (fieldValue == null || fieldValue.trim().length() == 0)) {
                    errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
                    return Boolean.FALSE;
                }

                if (obtainedPropertyValue.equals(propertyValue) && (fieldValue != null || fieldValue.trim().length() != 0)) {
                    return FieldChecks.validateDate(bean, va, field, errors,null, request);
                }

            }
            catch (Exception e) {
                errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    public static Object validatePropertyEqualsWithValue(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        String property = field.getVarValue("property").trim();
        String propertyValue = field.getVarValue("value").trim();
        String fieldValue = ValidatorUtils.getValueAsString(bean, field.getProperty());
        if (!GenericValidator.isBlankOrNull(property)) {
            try {
                String obtainedPropertyValue = ValidatorUtils.getValueAsString(bean, property);
                if (obtainedPropertyValue.equals(propertyValue) && (fieldValue == null || fieldValue.trim().length() == 0)) {
                    errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
                    return Boolean.FALSE;
                }
            }
            catch (Exception e) {
                errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    /**
     * This validator is declared as "xor" in validator-rules.xml
     *
     * @param bean
     * @param va
     * @param field
     * @param errors
     * @param request
     * @return true if the condition is satisfied or false otherwise
     */
    public static boolean validateOnlyOneOfTwo(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        String sProperty2 = field.getVarValue("secondProperty");
        String value2 = ValidatorUtils.getValueAsString(bean, sProperty2);

        if (GenericValidator.isBlankOrNull(value) == GenericValidator.isBlankOrNull(value2)) {
            errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
            return false;
        }
        return true;
    }

    /**
     * This validator is declared as "nand" in validator-rules.xml
     *
     * @param bean
     * @param va
     * @param field
     * @param errors
     * @param request
     * @return true if the condition is satisfied or false otherwise
     */
    public static boolean validateAtMostOneOfTwo(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        String sProperty2 = field.getVarValue("secondProperty");
        String value2 = ValidatorUtils.getValueAsString(bean, sProperty2);

        boolean valueIsblankOrNull = GenericValidator.isBlankOrNull(value);

        if ((valueIsblankOrNull == GenericValidator.isBlankOrNull(value2)) && !valueIsblankOrNull) {
            errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
            return false;
        }
        return true;
    }

    public static boolean validateEmptyCollection(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        Object value = null;

        try {
            value = PropertyUtils.getProperty(bean, field.getProperty());
        }
        catch (IllegalAccessException e) {
            log.error("Error in Validator.validateEmptyCollection",e);
        }
        catch (InvocationTargetException e) {
            log.error("Error in Validator.validateEmptyCollection",e);
        }
        catch (NoSuchMethodException e) {
            log.error("Error in Validator.validateEmptyCollection",e);
        }

        if (value == null) {
            errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
            return false;
        }

        if (value instanceof Collection) {
            if (((Collection) value).isEmpty()) {
                errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
                return false;
            }
        }

        return true;
    }

    public static boolean validateAssetTypeSelection(Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request) {
        Object value = "";

        try {
            value = PropertyUtils.getProperty(bean, field.getProperty());
        }
        catch (IllegalAccessException e) {
            log.error("Error in Validator.validateAssetTypeSelection",e);
        }
        catch (InvocationTargetException e) {
            log.error("Error in Validator.validateAssetTypeSelection",e);
        }
        catch (NoSuchMethodException e) {
            log.error("Error in Validator.validateAssetTypeSelection",e);
        }

        if (value.equals("")) {
            errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
            return false;
        }
        return true;
    }
}