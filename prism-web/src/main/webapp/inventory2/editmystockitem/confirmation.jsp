<%@ include file="../../include/tlds.jsp" %>

<html>
    <head>
        <title>Stock Item Action Request Confirmation</title>
    </head>
    <body>
        <table width="50%" align="center">
            <tr>
                <td colspan="4">&nbsp;</td>
            </tr>
            <tr>
                <th class="tableheader" colspan="2">Update Stock Item Confirmation</th>
            </tr>
            <tr>
                <td colspan="4">&nbsp;</td>
            </tr>
            <tr class="tabledetail">
                <td colspan="2">
                    The stock item <strong><%=request.getAttribute("icnbr")%>
                </strong> has been updated!<br/><br/>
                    Please click <a href="${pageContext.request.contextPath}/viewMyStockItems.do">here</a> if you want to edit another stock item!
                </td>
            </tr>
        </table>
    </body>
</html>