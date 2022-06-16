<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<html>
  <head>
    <title>Evaluate Stock Action Requests</title>
    <script type="text/javascript">
    </script>
  </head>
  <body>
    <table style="width: 100%; border: none; border-collapse: collapse;">
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>
          <table cellpadding="2" style="width: 100%; border: none; border-collapse: collapse;">
            <tr>
              <th valign="top" colspan="6" class="tableheader">Open Stock Action Requests</th>
            </tr>
            <tr>
              <th><a href="?orderBy=actionType" style="color: White">Action Type:</a></th>
              <th><a href="?orderBy=desc" style="color: White">Item Description</a></th>
              <th><a href="?orderBy=icnbr" style="color: White">Item ICNBR</a></th>
              <th><a href="?orderBy=requestedBy" style="color: White">Action Requested By:</a></th>
              <th><a href="?orderBy=requestDate" style="color: White">Date Requested:</a></th>
            </tr>
            <nested:iterate id="stockItemActionRequest" name="stockItemActionRequestsForm" property="stockItemActionRequests" indexId="a">
              <tr>
                <td align="center">
                  <a href="${pageContext.request.contextPath}/viewEvaluateStockItemActionRequest.do?siarId=<nested:write property='stockItemActionRequestId'/>">
                    <nested:write property="actionRequestType.name"/>
                  </a>
                </td>
                <td height="30" style="text-align: center;">
                  <nested:write property="potentialStockItem.description"/>
                </td>
                <td style="text-align: center;">
                  <nested:write property="potentialStockItem.fullIcnbr"/>
                </td>
                <td style="text-align: center;">
                  <nested:write property="requestor.firstAndLastName"/>
                </td>
                <td style="text-align: center;">
                  <nested:write property="requestedDate" format="MM/dd/yyyy kk:mm"/>
                </td>
              </tr>
            </nested:iterate>
          </table>
        </td>
      </tr>
    </table>
  </body>
</html>
