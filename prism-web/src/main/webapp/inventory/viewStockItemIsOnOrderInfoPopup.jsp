<%@ include file="../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.model.materialsrequest.Request" %>
<html>
    <body>
        <display:table name="requestScope.openRequests" class="displaytag" export="false" id="parent">
            <display:caption><%=request.getAttribute("icnbr")%></display:caption>
            <display:column title="Request#">
                <%=((Request) parent).getTrackingNumber()%>

                <a href="javascript:void(0);"
                   onclick="return OLgetAJAX('/inventory/getRequestLineItems.do?requestId=<%=((Request) parent).getRequestId()%>&requestTrackingNumber=<%=((Request) parent).getTrackingNumber()%>',
                             displayStockItemIsOnOrderInformation2, 300, 'ovfl1');">
                    Details</a>
            </display:column>
            <display:column title="Date" property="dateRequested" format="{0,date,MM-dd-yyyy}"/>
            <display:column title="Requestor" property="requestor.firstAndLastName"/>
        </display:table>
    </body>
</html>