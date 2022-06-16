<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>Order Line Item Receiving History</title>
    </head>
    <body>
        <div class="row">
            <div class="col-md-12">
                <table class="table table-bordered table-striped">
                    <thead>
                    <tr>
                        <th scope="col">Date Received</th>
                        <th scope="col">Received By</th>
                        <th scope="col">Qty Received</th>
                        <th scope="col">Facility</th>
                        <th scope="col">Asset Number</th>
                    </tr>
                    </thead>
                    <tbody>
                    <nested:iterate id="oliReceivingHistoryBean" property="receivingDetails" name="oliReceivingHistoryBean">
                        <tr>
                            <td><nested:write name="oliReceivingHistoryBean" property="dateReceived" format="MM/dd/yyyy"/></td>
                            <td><nested:write name="oliReceivingHistoryBean" property="receivedBy.firstAndLastName"/></td>
                            <td class="text-center"><nested:write name="oliReceivingHistoryBean" property="quantityReceived"/></td>
                            <td><nested:write name="oliReceivingHistoryBean" property="facility.facilityName" /></td>
                            <td>
                                <nested:present property="assetNumber" name="oliReceivingHistoryBean">
                                    <nested:write name="oliReceivingHistoryBean" property="assetNumber" />
                                </nested:present>
                            </td>
                        </tr>
                    </nested:iterate>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
