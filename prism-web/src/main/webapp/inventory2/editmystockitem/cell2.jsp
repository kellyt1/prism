<%@ include file="../../include/tlds.jsp" %>

<label>ROP:</label>
<nested:text property="rop" size="6"/>
<br/>
(Suggested:
<nested:write property="stockItem.suggestedROP"/>
)
<label>ROQ:</label>
<nested:text property="roq" size="6"/>
<br/>
(Suggested:
<nested:write property="stockItem.suggestedROQ"/>
)
<label>On Hand/Current Demand:</label>
<nested:write property="stockItem.qtyOnHand"/>
/
<nested:write property="stockItem.currentDemand"/>


