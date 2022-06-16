<%@ include file="../../include/tlds.jsp" %>

<label>Annual Usage:</label>
<nested:write property="annualUsage"/>
<label>Estimated Annual Usage:<br/>
    <small>(in dispense units)</small>
</label>
<nested:text property="estimatedAnnualUsage" size="6"/>

<label>Seasonal:</label>
Yes
<nested:radio property="stockItem.seasonal" value="true"/>
No
<nested:radio property="stockItem.seasonal" value="false"/>

