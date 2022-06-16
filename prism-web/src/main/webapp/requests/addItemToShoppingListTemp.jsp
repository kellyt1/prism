<%@ include file="../include/tlds.jsp" %>

<html>
  <head>
    <title>My Shopping Lists</title>
  </head>
  <body>
    <table cellspacing="2" cellpadding="3" border="1" width="100%">
      <tr>
        <td>Some&nbsp;Stock Item</td>
        <td><a href="${pageContext.request.contextPath}/viewChooseShoppingList.do?itemId=47715">Add to Shopping List</a></td>
      </tr>
      <tr>
        <td>Some Purchasable Item</td>
        <td><a href="${pageContext.request.contextPath}/viewChooseShoppingList.do?itemId=62792">Add to Shopping List</a></td>
      </tr>
    </table>
  </body>
</html>
