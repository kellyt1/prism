<html>
    <head>
        <title>Assign New Stock Item IC Number</title>
    </head>
    <body>
        <%
          String showText = request.getParameter("showText");
          if(showText == null || showText.equals("")){
                showText = "N";
          }

          String action = request.getParameter("action");
          if(action == null){
                action = "";
          }
        %>

        <form name="frmMain" action="evaluateStockItemActionRequest.jsp">
        <table cellspacing="0" cellpadding="2" border="0" width="40%" align="center" >
          <tr>
            <td><label for="denyReason">Reason for denial:</label></td>
          </tr>
          <tr>
            <td>
              <input type="TEXT" name="denyReason" id="denyReason" size="30" />
              <input type="SUBMIT" value="Submit" />
            </td>
          </tr>
        </table>
        </form>
    </body>
</html>
