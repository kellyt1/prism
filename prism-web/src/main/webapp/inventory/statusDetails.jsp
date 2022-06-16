<%@ include file="../include/tlds.jsp" %>
<html>
    <body>
        <table align="center" border="1" >
          <nested:equal value="ONH" name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.status.statusCode">
            <tr>
              <th>Hold Until Date</th>
              <td><nested:text name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.holdUntilDate" disabled="true"/></td>
            </tr>
          </nested:equal>
          <tr>
            <th>Discard Current Stock</th>
            <td>
              <nested:radio name="evaluateStockItemActionRequestForm" property="discardStock" value="true" disabled="true"  />Yes
              <nested:radio name="evaluateStockItemActionRequestForm" property="discardStock" value="false" disabled="true" />No
            </td>
          </tr>
          <nested:equal value="ONH" name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.status.statusCode">
              <tr>
                <th>Reason of <br> the request</th>
                <td><nested:textarea name="evaluateStockItemActionRequestForm" property="requestReason" disabled="true" /></td>
              </tr>
          </nested:equal>
        </table>
    </body>
</html>