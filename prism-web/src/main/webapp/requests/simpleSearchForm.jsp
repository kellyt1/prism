<%--<%--%>
<%--  String category = (String)request.getParameter("searchCategory");--%>
<%--  int catNum = 0;--%>
<%--  if(category != null) {--%>
<%--   catNum = Integer.parseInt(category);--%>
<%--  }--%>
<%--%>--%>
<form name="searchForm" action="/mmPrototype/searchItems" method="POST">
      <table cellpadding="3" style="width: 40%; background: #fff; border: none; border-collapse: collapse; margin: 0 auto;">
        <tr>
          <td style="width: 5%;">Search</td>
          <td style="width: 20%;">
            <select name="searchCategory" title="searchCategory" >  
              <option value="0" selected="selected">Entire Catalog</option>
              <option value="1">Office Supplies</option>
              <option value="2">Lab Supplies</option>
              <option value="3">Computers</option>
              <option value="4">Publications</option>
              <option value="5">Category X</option>
              <option value="6">Category Y</option>
              <option value="7">Category Z</option>
            </select>
          </td>
          <td style="width: 5%; text-align: right;">
            For
          </td>
          <td style="width: 20%;">
            <input type="text" name="searchKeywords" id="searchKeywords" />
          </td>
          <td style="width: 5%; text-align: center;">
            <input type="SUBMIT" value="Go">
          </td>
          <td style="text-align: right;"><font color="Blue" size="-2"><a href="advancedSearch.jsp">Advanced Search</a></font></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
      </table>
      <table cellpadding="3" style="background: #fff; border-collapse: collapse; border: none; text-align: center;">
        <tr>
          <td><a href="searchCatalog.jsp?searchCategory=1">Office Supplies</a> |</td>
          <td><a href="searchCatalog.jsp?searchCategory=2">Lab Supplies</a> |</td>
          <td><a href="searchCatalog.jsp?searchCategory=3">Computers</a> |</td>
          <td><a href="searchCatalog.jsp?searchCategory=4">Publications</a> |</td>
          <td><a href="searchCatalog.jsp?searchCategory=5">Category X</a> |</td>
          <td><a href="searchCatalog.jsp?searchCategory=6">Category Y</a> |</td>
          <td><a href="searchCatalog.jsp?searchCategory=7">Category Z</a></td>
        </tr>
      </table>
    </form>
    
<%--   <table align="center" cellspacing="0" cellpadding="3" border="0" bgcolor="white"> --%>
<%--    --%>
<%--      <%--%>
<%--        switch(catNum){--%>
<%--          case 1:--%>
<%--            %><jsp:include page="officeSupplies.jsp" /><%--%>
<%--            break;--%>
<%--          case 2:--%>
<%--            %><jsp:include page="labSupplies.jsp" /><%--%>
<%--            break;--%>
<%--          case 3:--%>
<%--            %><jsp:include page="computers.jsp" /><%--%>
<%--            break;--%>
<%--          case 4:--%>
<%--            %><jsp:include page="publications.jsp" /><%--%>
<%--            break;--%>
<%--          case 5:--%>
<%--            %><jsp:include page="categoryX.jsp" /><%--%>
<%--            break;--%>
<%--        }--%>
<%--      %>--%>
<%--      --%>
<%--    </table>--%>