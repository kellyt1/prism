
<%@ page import="us.mn.state.health.model.materialsrequest.RequestLineItem" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<html>
<body>
<display:table name="requestScope.openRequestLineItems" class="displaytag"
               export="false" id="parent">
    <display:caption><%=request.getAttribute("requestTrackingNumber")%></display:caption>
    <display:column title="Item Description#">
        <%if (((RequestLineItem) parent).getItem() != null) {%>
        <%=((RequestLineItem) parent).getItem().getDescription()%>
        <%} else {%>
        <%=((RequestLineItem) parent).getItemDescription()%>
        <%}%>
    </display:column>
    <display:column title="Status" property="status.name"/>
    <display:column title="OPR">
        <logic:notEmpty name="parent" property="orderLineItem">
            <bean:write name="parent" property="orderLineItem.order.orderId"/>
        </logic:notEmpty>
    </display:column>
    <display:column title="PO#">
        <logic:notEmpty name="parent" property="orderLineItem">
            <bean:write name="parent" property="orderLineItem.order.purchaseOrderNumber"/>
        </logic:notEmpty>
    </display:column>
    <display:column title="Buyer">
        <logic:notEmpty name="parent" property="orderLineItem">
            <bean:write name="parent" property="orderLineItem.order.purchaser.firstAndLastName"/>
        </logic:notEmpty>
    </display:column>
    <display:column title="Order Date">
        <logic:notEmpty name="parent" property="orderLineItem">
            <bean:write name="parent" property="orderLineItem.order.insertionDate" format="MM-dd-yyyy"/>
        </logic:notEmpty>
    </display:column>
    <display:setProperty name="export.pdf" value="false"/>
    <display:setProperty name="export.rtf" value="false"/>
    <display:setProperty name="export.excel" value="false"/>
    <display:setProperty name="export.xml" value="false"/>
    <display:setProperty name="export.csv" value="false"/>
</display:table>
</body>
</html>