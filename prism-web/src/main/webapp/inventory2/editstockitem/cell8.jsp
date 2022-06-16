<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemAction" %>
<%@ include file="../../include/tlds.jsp" %>

<label for="newQtyOnHand">Qty On-Hand:</label>
<nested:define property="stockItem.dispenseUnit.name" id="dispenseUnit"/>
<nested:text property="newQtyOnHand" size="10" title="<%=\"Quantity on-hand in dispense units: \" + dispenseUnit.toString()%>" styleId="newQtyOnHand" styleClass="pull-right text-right" disabled="true" readonly="true"/>

<br/><br/>

<label for="adjustmentReasonId">On-Hand Change Reason:</label>
<span class="pull-right">
    <nested:select property="adjustmentReasonId" styleClass="chosen-select" styleId="adjustmentReasonId" disabled="true">
        <option value=""></option>
        <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="qtyChangeReasonRefs" label="reason" value="id"/>
    </nested:select>
</span>

<br/><br/>

<label for="adjustmentOrgBudgetId">On-Hand Change Charge/Credit:</label>
<span class="pull-right">
    <nested:select property="adjustmentOrgBudgetId" style="width:200px" styleClass="chosen-select" styleId="adjustmentOrgBudgetId" disabled="true">
        <option value=""></option>
        <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="orgBudgets" label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
    </nested:select>
</span>

<br/><br/>

<label for="orgBudget">Org. Budget:</label><br/>
<span id="orgBudget"><nested:write property="stockItem.orgBudget.orgBudgetCodeAndNameAndFY"/></span>