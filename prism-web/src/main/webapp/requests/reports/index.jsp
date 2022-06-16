<%@ include file="../../include/tlds.jsp" %>

<html>
    <head><title>Reports</title></head>
    <body>
        <table align="center" width="75%">
            <tr>
                <td>
                    <ul>
                        <li><a href="stockitemouttransactions.jsp">View stock item OUT transactions (for items NOT funded by the Indirect funding source ONLY)</a></li>
                        <li><a href="stockitemouttransactionswithoutfs.jsp">View stock item OUT transactions (for items with NO cost or funded by the Indirect funding source ONLY)</a></li>
                        <li><a href="stockitemintransactions.jsp">View stock item IN transactions</a></li>
                        <li><a href="purchaseitemtransactions.jsp">View purchase item transactions</a></li>
                        <li><a href="fishconsumption.jsp">Fish Consumption Guildelines Requestors</a></li>
                    </ul>
                </td>
            </tr>
        </table>
    </body>
</html>
<%session.removeAttribute("stockItemOutTransactionsForm");%>
<%session.removeAttribute("stockItemInTransactionsForm");%>
<%session.removeAttribute("purchaseItemTransactionsForm");%>
<%session.removeAttribute("stockItemOutTransactionsWithoutFSForm");%>