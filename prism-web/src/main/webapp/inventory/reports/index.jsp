<%@ include file="../../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.view.inventory.Command" %>

<html>
<head>
    <title>Inventory Reports</title>
</head>

<body>
<%request.getSession().removeAttribute("stockItemsReportForm");%>
<table align="center" width="75%">
    <tr>
        <td>
            <ul>
                <li>
                    <a href="${pageContext.request.contextPath}/inventory/reports/viewStockItemsBelowROPNotOnOrder.do?cmd=<%=Command.RESET%>">
                        Stock items to be re-ordered (onhand <= ROP and not yet ordered)
                    </a>
                </li>
                <logic:iterate id="ex" name="ExternalReports" scope="request">
                    <li>
                        <a href="<nested:write name="ex" property="reporturl"/>" target="new"><nested:write name="ex" property="reportname"/></a>
                    </li>
                </logic:iterate>
                <!--<!--<li>-->
                <!--<a href="stockitemouttransactionswithoutfs.jsp">Stock items currently on order</a>-->
                <!--</li>-->
                <!--<li><a href="stockitemintransactions.jsp">Stock items stocked out (onhand = 0)</a></li>-->
            </ul>
        </td>
    </tr>

</table>
</body>
</html>