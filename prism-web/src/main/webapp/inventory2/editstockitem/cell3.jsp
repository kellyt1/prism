<%@ include file="../../include/tlds.jsp" %>
<label for="annualUsage">Annual Usage:</label>
<nested:text property="annualUsage" size="6" disabled="true" styleClass="pull-right text-right" styleId="annualUsage" readonly="true"/>

<br/><br/>

<label for="estimatedAnnualUsage">Estimated Annual Usage:</label>
<nested:text property="estimatedAnnualUsage" size="6" title="Quantity in dispense units" styleClass="pull-right text-right" styleId="estimatedAnnualUsage" readonly="true" disabled="true"/>

<br/><br/>

<label for="reviewDate">Review Date:</label>
<nested:text property="reviewDate" size="10" maxlength="10" styleClass="dateInput pull-right" title="Date format: mm/dd/yyyy" styleId="reviewDate" readonly="true" disabled="true"/>

<br/><br/>

<label>Seasonal:</label>
<span class="pull-right" style="white-space: nowrap;">
    <label>Yes <nested:radio property="stockItem.seasonal" value="true" disabled="true"/></label>
    <label>No <nested:radio property="stockItem.seasonal" value="false" disabled="true"/></label>
</span>

<br/><br/>

<label for="qtyOnHand">On Hand:</label>
<nested:text property="stockItem.qtyOnHand" size="6" styleClass="pull-right text-right" styleId="qtyOnHand" readonly="true" disabled="true"/>

<br/><br/>

<label for="currentDemand">Current Demand:</label>
 <nested:text property="stockItem.currentDemand" size="6" disabled="true" styleClass="pull-right text-right" styleId="currentDemand" readonly="true"/>