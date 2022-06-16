<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>Search The Catalog</title>
    </head>
    <body>
        <form action="searchCatalog.do" method="POST">
          <table align="center" cellspacing="0" cellpadding="3" border="0" bgcolor="white">
            <tr>
              <td align="right">Keyword Search:&nbsp;</td>
              <td align="left"><nested:text name="searchCatalogForm" property="query" size="55"/></td>
              <td align="left"><input type="SUBMIT" value="Search Catalog"></td>
              <td class="small" align="left"><a class="small" href="viewAdvancedSearchCatalog.do">Advanced Search</a></td>
             </tr>

            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
          </table>
          </form>
          <form action="" method="post">
              <input type="HIDDEN" name="shoppingListAction" value="addItm" />
              <table align="center" cellspacing="0" cellpadding="3" border="0" bgcolor="white" width="90%" >
                <tr class="tableheader" align="center" >
                    <th align="center">
                        <!--Description-->
                        <htmlx:sortAnchor name="searchCatalogForm"
                            style=""
                            property="results"
                            value="description, itemId"
                            input="/requests/searchCatalog.jsp">Description</htmlx:sortAnchor>
                    </th>
                    <th align="left">
                        <!--Manufacturer-->
                        <htmlx:sortAnchor name="searchCatalogForm"
                            style=""
                            property="results"
                            value="manufacturer.externalOrgDetail.orgName, itemId"
                            input="/requests/searchCatalog.jsp">Manufacturer</htmlx:sortAnchor>
                    </th>
                    <th align="left">
                        Model
                        <%--<htmlx:sortAnchor name="searchCatalogForm"--%>
                            <!--style="color: #FFFFFF;"-->
                            <!--property="results"-->
                            <!--value="model"-->
                            <!--input="/requests/searchCatalog.jsp">Model
                            <%--</htmlx:sortAnchor>-->--%> -->
                    </th>
                    <th align="left">
                        <!--Dispense Unit-->
                        <htmlx:sortAnchor name="searchCatalogForm"
                            style=""
                            property="results"
                            value="dispenseUnit.name, itemId"
                            input="/requests/searchCatalog.jsp">Dispense Unit</htmlx:sortAnchor>
                    </th>
                    <th align="left">
                        <!--Category-->
                        <htmlx:sortAnchor name="searchCatalogForm"
                            style=""
                            property="results"
                            value="category.name, itemId"
                            input="/requests/searchCatalog.jsp">Category</htmlx:sortAnchor>
                    </th>
                    <th align="left">
                        <!--Type-->
                        <htmlx:sortAnchor name="searchCatalogForm"
                            style=""
                            property="results"
                            value="itemType, itemId"
                            input="/requests/searchCatalog.jsp">Type</htmlx:sortAnchor>
                    </th>
                    <th></th>
                    <th></th>
                </tr>
                <nested:iterate name="searchCatalogForm" property="results">
                  <tr>
                    <td>
                        <a href="showItemDetails.do?itemId=<nested:write property="itemId"/>"><nested:write property="description"/></a><br/>
                    </td>
                    <td><nested:write property="manufacturer.externalOrgDetail.orgName"/></td>
                    <td><nested:write property="model"/></td>
                    <td><nested:write property="dispenseUnit.name"/></td>
                    <td><nested:write property="category.name"/></td>
                    <td><nested:write property="itemType"/></td>
                    <td align="center">
                        <input type="image" src="/images/addtocart.gif"
                               onclick="this.form.action='viewAddCatalogItemToCart.do?itemId=<nested:write property="itemId"/>'; this.form.submit(); return false;"/>
                    </td>
                    <td align="center">
                        <input type="image" src="/images/addtolist.bmp"
                               onclick="this.form.action='viewChooseShoppingList.do?itemId=<nested:write property="itemId"/>'"/>
                    </td>
                  </tr>
              </nested:iterate>
            </table>
          </form>
    </body>
</html>