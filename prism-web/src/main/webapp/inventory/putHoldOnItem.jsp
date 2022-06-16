<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<html>
  <head>
    <title>Deactivate Item - Discard or Deplete</title>
  </head>
  <body>
    <h1>Page #: M16</h1>
    <p align="center">You are requesting that this item be placed on-hold.
      <br/>Please provide the following additional information:
    </p>
    <html:form action="updateStockItemInfo.jsp">
      <table align="center">
        <tr>
          <td align="right">
            <strong>Hold Until (mm/dd/yy):</strong>
          </td>
          <td>
            <nested:text property="potentialStockItemForm.holdUntilDate"/>
          </td>
        </tr>
        <tr>
          <td align="right">
            <strong>Discard Current Stock?</strong>
          </td>
          <td>
            <label><input type="RADIO" name="discard" id="discard" value="Yes"/>Yes</label>
              <label><input type="RADIO" name="discard" id="discard" value="No"/>No</label>
          </td>
        </tr>
        <tr>
          <td align="right">
            <strong>Reason for Hold:</strong>
          </td>
          <td>
            <nested:textarea property="requestReason" cols="30" rows="1"/>
          </td>
        </tr>
        <tr>
          <td colspan="2" align="center">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" align="center">
            <input type="BUTTON" value="Cancel" onclick="history.go(-1);"/>
            <input type="SUBMIT" value="Submit Hold Request"/>
          </td>
        </tr>
      </table>
    </html:form>
  </body>
</html>
