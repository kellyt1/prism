<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemAction" %>
<%@ page import="us.mn.state.health.model.common.Status" %>
<%@ include file="../../include/tlds.jsp" %>

<label for="statusId">Status:</label>
<span class="pull-right">
    <nested:select property="statusId" onchange="javascript:this.form.submit(); return false;" styleClass="chosen-select" styleId="statusId" disabled="true">
        <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="statuses" label="name" value="statusId"/>
    </nested:select>
</span>

<br/><br/>

<nested:equal property="stockItem.status.statusCode" value="<%=Status.ONHOLD%>">
    <label for="holdUntilDate">Hold Until Date:</label>
    <nested:text property="holdUntilDate" maxlength="10" size="10" styleClass="dateInput" styleId="holdUntilDate" disabled="true"/>
</nested:equal>
<nested:notEqual property="stockItem.status.statusCode" value="<%=Status.ACTIVE%>">
    <label>Fill Until Depleted:</label>
    <span class="pull-right" style="white-space: nowrap;">
        <label>Yes <nested:radio property="fillUntilDepleted" value="true" disabled="true"/></label>&nbsp;
        <label>No <nested:radio property="fillUntilDepleted" value="false" disabled="true"/></label>
    </span>
</nested:notEqual>