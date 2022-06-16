<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemAction" %>
<%@ include file="../../include/tlds.jsp" %>

<label for="rop">ROP:</label>
<nested:define property="stockItem.suggestedROP" id="suggestedROP"/>
<nested:text property="rop" size="6" title="<%=\"Suggested: \" + suggestedROP.toString()%>" styleId="rop" styleClass="pull-right text-right" readonly="true" disabled="true"/>

<br/><br/>

<label for="roq">ROQ:</label>
<nested:define property="stockItem.suggestedROQ" id="suggestedROQ"/>
<nested:text property="roq" size="6" title="<%=\"Suggested: \" + suggestedROQ.toString()%>" styleId="roq" styleClass="pull-right text-right" readonly="true" disabled="true"/>

<br/><br/>

<label for="safetyStock">Safety Stock:</label>
<nested:text property="safetyStock" size="6" styleClass="pull-right text-right" styleId="safetyStock" readonly="true" disabled="true"/>

<br/><br/>

<label for="cycleCountPriorityId">Cycle Count Priority:</label>
<span class="pull-right">
    <nested:select property="cycleCountPriorityId" style="min-width: 60px;" styleClass="chosen-select" styleId="cycleCountPriorityId" disabled="true">
        <option value="">N/A</option>
        <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="cycleCountPriorities" label="code" value="cycleCountPriorityId"/>
    </nested:select>
</span>