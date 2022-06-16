<%@ include file="../../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemAction" %>

<label>Dispense Unit:</label>
<nested:write property="stockItem.dispenseUnit.name"/>
<label>Budget Code:</label>
<nested:select property="orgBudgetId" style="width:255px" styleClass="chosen-select">
    <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="orgBudgets"
                              label="orgBudgetCodeAndNameAndFY"
                              value="orgBudgetId"/>
</nested:select>

<label>Primary Contact:</label>
<nested:select property="primaryContactId" style="width:255px">
    <option value="">==Please select a contact==</option>
    <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="contacts" label="lastAndFirstName"
                              value="personId"/>
</nested:select>
<label>Secondary Contact:</label>
<nested:select property="secondaryContactId" style="width:255px">
    <option value="">==Please select a contact==</option>
    <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="contacts" label="lastAndFirstName"
                              value="personId"/>
</nested:select>
<br/>
<br/>
<nested:notEqual property="stockItem.isOnOrder" value="true">
    Not On Order!
</nested:notEqual>
<nested:equal property="stockItem.isOnOrder" value="true">
    <a href="javascript:void(0);"
       onclick="return OLgetAJAX('/inventory/getStockItemOnOrderInfo.do?stockItemId=<nested:write property='stockItem.itemId'/>&icnbr=<nested:write property="stockItem.fullIcnbr"/>',
                         displayStockItemIsOnOrderInformation, 300, 'ovfl1');">On Order Information</a>

</nested:equal>




