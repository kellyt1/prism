<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemAction" %>
<%@ include file="../../include/tlds.jsp" %>

<label for="dispenseUnitId">Dispense Unit:</label>
<span class="pull-right">
    <nested:select property="dispenseUnitId" style="width:255px" styleClass="chosen-select" styleId="dispenseUnitId" disabled="true">
        <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="units" label="code" value="unitId"/>
    </nested:select>
</span>

<br/><br/>

<label for="orgBudgetId">Budget Code:</label>
<span class="pull-right">
    <nested:select property="orgBudgetId" style="width:255px" styleClass="chosen-select" styleId="orgBudgetId" disabled="true">
        <option style="display: none;" value="<nested:write property="stockItem.orgBudgetId"/>">Budget is expired!</option>
        <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="orgBudgets"
                label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
    </nested:select>
</span>

<br/><br/>

<label for="primaryContactId">Primary Contact:</label>
<span class="pull-right">
    <nested:select property="primaryContactId" style="width:255px" styleClass="chosen-select" styleId="primaryContactId" disabled="true">
        <option value="">==Please select a contact==</option>
        <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="contacts" label="lastAndFirstName" value="personId"/>
    </nested:select>
</span>

<br/><br/>

<label for="secondaryContactId">Secondary Contact:</label>
<span class="pull-right">
    <nested:select property="secondaryContactId" style="width:255px" styleClass="chosen-select" styleId="secondaryContactId" disabled="true">
        <option value="">==Please select a contact==</option>
        <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="contacts" label="lastAndFirstName" value="personId"/>
    </nested:select>
</span>

<%--<br/><br/>

<nested:notEqual property="stockItem.isOnOrder" value="true">
    Not On Order!
</nested:notEqual>
<nested:equal property="stockItem.isOnOrder" value="true">
    <a href="javascript:void(0);"
       onclick="return OLgetAJAX('/inventory/getStockItemOnOrderInfo.do?stockItemId=<nested:write property='stockItem.itemId'/>&icnbr=<nested:write property="stockItem.fullIcnbr"/>',
                         displayStockItemIsOnOrderInformation, 300, 'ovfl1');">On Order Information</a>

</nested:equal>--%>




