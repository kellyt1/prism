<%@ include file="../../include/tlds.jsp" %>

<html>
    <head><title>On Order Info</title></head>
    <body>
        <display:table export="false" name="requestScope.requestLineItemsOnOrder" class="displaytag"
                       id="parent">
            <display:column title="MRQ#" property="request.trackingNumber"/>
            <display:column title="Date Requested" property="request.dateRequested" format="{0,date,dd-MM-yyyy}"/>
            <display:column title="Requestor" property="request.requestor.firstAndLastName"/>
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
        </display:table>
    </body>
</html>