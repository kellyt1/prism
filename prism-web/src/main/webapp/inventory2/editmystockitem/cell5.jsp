<%@ include file="../../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemAction" %>
<%@ page import="us.mn.state.health.model.common.Status" %>

<label>Status:</label>
<nested:equal property="stockItem.status.statusCode" value="<%=Status.INACTIVE%>">
    <nested:write property="stockItem.status.name"/>
    <label>Fill Until Depleted:</label>
    <nested:equal property="fillUntilDepleted" value="true">
        YES
    </nested:equal>
    <nested:equal property="fillUntilDepleted" value="false">
        NO
    </nested:equal>
    <nested:empty property="fillUntilDepleted">
        NO
    </nested:empty>
</nested:equal>
<nested:notEqual property="stockItem.status.statusCode" value="<%=Status.INACTIVE%>">
    <nested:select property="statusId" onchange="javascript:this.form.submit(); return false;">
        <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="statuses" label="name" value="statusId"/>
    </nested:select>
    <nested:equal property="stockItem.status.statusCode" value="<%=Status.ONHOLD%>">
        <label>Hold Until Date:</label>
        <nested:text property="holdUntilDate" maxlength="10" size="10" styleClass="dateInput"/>
    </nested:equal>
    <nested:notEqual property="stockItem.status.statusCode" value="<%=Status.ACTIVE%>">
        <label>Fill Until Depleted:</label>
        Yes
        <nested:radio property="fillUntilDepleted" value="true"/>
        <br/>
        No
        <nested:radio property="fillUntilDepleted" value="false"/>
    </nested:notEqual>
</nested:notEqual>