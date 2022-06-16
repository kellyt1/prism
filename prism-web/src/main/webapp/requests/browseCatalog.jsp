<%@ include file="../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.model.inventory.Item" %>

<html>
<head>
    <title>Search Catalog</title>
</head>
    <body>
        <logic:notEqual name="skin" value="PRISM2">
            <form action="searchCatalog.do" method="POST" accept-charset="utf-8">
                <div class="row">
                    <div class="col-md-4 col-md-offset-4">
                        <label for="keywords">Keyword Search</label>
                        <div class="input-group">
                            <input type="text" name="query" size="50" value="" id="keywords" class="form-control" placeholder="Search...">
                            <%--<nested:text name="browseCatalogForm" property="query" size="50" styleClass="form-control" styleId="keywords"/>--%>
                            <div class="input-group-btn">
                                <label for="search" style="display: none;">Search</label>
                                <button class="btn btn-default" type="submit" id="search"><i class="glyphicon glyphicon-search"></i></button>
                            </div>
                        </div>
                        <p class="text-center"><a href="viewAdvancedSearchCatalog.do">Advanced Search</a></p>
                    </div>
                </div>
            </form>
        </logic:notEqual>

        <%--6/1/2017 The below requested information banner has been requested and is likely to be requested again, but the stakeholders are debating verbiage--%>
        <%--<logic:equal name="skin" value="PRISM2">--%>
            <%--<div class="row">--%>
                <%--<div class="col-md-8 col-md-offset-2 text-center ">--%>
                    <%--<h2>--%>
                        <%--<p>Ordering a computer or computer accessory?</p>--%>
                        <%--<p>Contact the service desk at x5555. There may already be one available.</p>--%>
                    <%--</h2>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</logic:equal>--%>

        <br>

        <form action="" method="post">
            <input type="HIDDEN" name="shoppingListAction" value="addItm"/>
            <div class="row">
                <div class="col-md-6">
                    <logic:notEqual name="skin" value="PRISM2">
                        <strong>Number of search results: <c:out value="${browseCatalogForm.totalNumItemsInResults}" default="0"/></strong>
                    </logic:notEqual>
                </div>
                <div class="col-md-6 text-right">
                    <logic:notEqual name="skin" value="PRISM2XYX">
                            <c:if test="${browseCatalogForm.totalNumItemsInResults > 0}">
                                <c:if test="${browseCatalogForm.searchType == 2}">
                                    <a href="browseCatalog.do?paginationDirection=first">First&nbsp;
                                        <c:out value="${browseCatalogForm.maxItems}"/>
                                    </a>&nbsp;|&nbsp;
                                    <c:if test="${browseCatalogForm.startIndex > 0}">
                                        <a href="browseCatalog.do?paginationDirection=prev">Previous</a>&nbsp;|&nbsp;
                                    </c:if>
                                    <c:if test="${browseCatalogForm.startIndex < (browseCatalogForm.totalNumItemsInResults - (browseCatalogForm.maxItems - 1))}">
                                        <a href="browseCatalog.do?paginationDirection=next">Next</a>&nbsp;|&nbsp;
                                    </c:if>
                                    <a href="browseCatalog.do?paginationDirection=last">Last&nbsp;
                                        <c:out value="${browseCatalogForm.maxItems}"/>
                                    </a>
                                </c:if>

                                <c:if test="${browseCatalogForm.searchType == 1}">
                                    <a href="searchCatalog.do?paginationDirection=first">First&nbsp;
                                        <c:out value="${browseCatalogForm.maxItems}"/>
                                    </a>&nbsp;|&nbsp;
                                    <c:if test="${browseCatalogForm.startIndex > 0}">
                                        <a href="searchCatalog.do?paginationDirection=prev">Previous</a>&nbsp;|&nbsp;
                                    </c:if>
                                    <c:if test="${browseCatalogForm.startIndex < (browseCatalogForm.totalNumItemsInResults - (browseCatalogForm.maxItems - 1))}">
                                        <a href="searchCatalog.do?paginationDirection=next">Next</a>&nbsp;|&nbsp;
                                    </c:if>
                                    <a href="searchCatalog.do?paginationDirection=last">Last&nbsp;
                                        <c:out value="${browseCatalogForm.maxItems}"/>
                                    </a>
                                </c:if>
                            </c:if>
                    </logic:notEqual>
                </div>
            </div><br>

            <nested:greaterThan name="browseCatalogForm" property="currentSubCategoriesSize" value="0">
                <div class="row">
                    <div class="col-md-12"><strong>Sub-Categories</strong></div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <nested:iterate name="browseCatalogForm" property="currentSubCategories">
                            &nbsp;<a href="browseCatalog.do?selectedCategory=<nested:write property="categoryId"/>"> <nested:write property="name"/></a>
                        </nested:iterate>
                    </div>
                </div>
            </nested:greaterThan>
            <nested:notEmpty name="browseCatalogForm" property="currentItems">
                <div class="row">
                    <div class="col-md-12">
                        <table class="table table-striped table-bordered sortable" id="browsecatalog">
                            <thead>
                                <tr>
                                    <th scope="col" class="text-center" style="white-space: nowrap;">Description</th>
                                    <logic:notEqual name="skin" value="PRISM2">
                                        <th class="text-left">ICNBR</th>
                                        <th class="text-left">Manufacturer</th>
                                        <th class="text-left">Model</th>
                                    </logic:notEqual>

                                    <logic:equal name="skin" value="PRISM2">
                                        <th scope="col" class="text-left" style="white-space: nowrap;">Cost</th>
                                    </logic:equal>
                                    <th scope="col" class="text-left" style="white-space: nowrap;">Unit</th>

                                    <logic:notEqual name="skin" value="PRISM2">
                                        <th scope="col" class="text-left" style="white-space: nowrap;">Category</th>
                                        <th scope="col" class="text-left" style="white-space: nowrap;">Type</th>
                                    </logic:notEqual>
                                    <th scope="col" colspan="2">&nbsp;</th>
                                </tr>
                            </thead>

                            <nested:iterate name="browseCatalogForm" property="currentItems">
                                <tr>
                                    <td>
                                      <logic:notEqual name="skin" value="PRISM2">
                                          <a href="showItemDetails.do?itemId=<nested:write property="itemId"/>"><nested:write property="description" /></a>
                                      </logic:notEqual>
                                      <logic:equal name="skin" value="PRISM2">
                                          <a href="showItemDetails.do?itemId=<nested:write property="itemId"/>"><p style="white-space: pre-wrap"><nested:write property="descriptionForUser" filter="false" /></p></a>
                                      </logic:equal>
                                    </td>

                                    <logic:notEqual name="skin" value="PRISM2">
                                        <nested:equal value="<%=Item.STOCK_ITEM%>" property="itemType">
                                            <td><nested:write property="fullIcnbr"/></td>
                                        </nested:equal>
                                        <nested:notEqual value="<%=Item.STOCK_ITEM%>" property="itemType">
                                            <td>&nbsp;</td>
                                        </nested:notEqual>
                                        <td>
                                            <nested:present property="manufacturer">
                                                <nested:present property="manufacturer.externalOrgDetail">
                                                    <nested:write property="manufacturer.externalOrgDetail.orgName"/>
                                                </nested:present>
                                            </nested:present>
                                        </td>
                                        <td><nested:write property="model"/></td>
                                    </logic:notEqual>

                                    <logic:equal name="skin" value="PRISM2">
                                        <td><nested:write property="dispenseUnitCost"  format="#,##0.00"/></td>
                                    </logic:equal>

                                    <td><nested:present property="dispenseUnit"><nested:write property="dispenseUnit.name"/></nested:present></td>

                                    <logic:notEqual name="skin" value="PRISM2">
                                        <td><nested:present property="category"><nested:write property="category.name"/></nested:present></td>
                                        <td><nested:write property="itemType"/></td>
                                    </logic:notEqual>

                                    <td class="text-center">
                                        <nested:equal value="<%=Item.STOCK_ITEM%>" property="itemType">
                                            <nested:notEqual value="true" property="discontinued">
                                                <button type="button" class="btn btn-default" onclick="this.form.action='viewAddCatalogItemToCart.do?itemId=<nested:write property="itemId"/>'; this.form.submit(); return false;">
                                                    <span class="glyphicon glyphicon-shopping-cart"></span> Add to Cart
                                                </button>
                                                <%--<input type="image" src="/images/addtocart.gif" onclick="this.form.action='viewAddCatalogItemToCart.do?itemId=<nested:write property="itemId"/>'; this.form.submit(); return false;"/>--%>
                                            </nested:notEqual>
                                            <nested:equal value="true" property="discontinued">
                                                <span class="text-danger"><strong>Discontinued</strong></span>
                                            </nested:equal>
                                        </nested:equal>
                                        <nested:notEqual value="<%=Item.STOCK_ITEM%>" property="itemType">
                                            <button type="button" class="btn btn-default" onclick="this.form.action='viewAddCatalogItemToCart.do?itemId=<nested:write property="itemId"/>'; this.form.submit(); return false;">
                                                <span class="glyphicon glyphicon-shopping-cart"></span> Add to Cart
                                            </button>
                                            <%--<input type="image" src="/images/addtocart.gif" onclick="this.form.action='viewAddCatalogItemToCart.do?itemId=<nested:write property="itemId"/>'; this.form.submit(); return false;"/>--%>
                                        </nested:notEqual>
                                    </td>
                                    <td class="text-center">
                                        <button type="button" class="btn btn-default" onclick="this.form.action='viewChooseShoppingList.do?itemId=<nested:write property="itemId"/>'; this.form.submit(); return false;">
                                            <span class="glyphicon glyphicon-list-alt"></span> Add to List
                                        </button>
                                        <%--<input type="image" src="/images/addtolist.bmp" onclick="this.form.action='viewChooseShoppingList.do?itemId=<nested:write property="itemId"/>'"/>--%>
                                    </td>
                                </tr>
                            </nested:iterate>

                            <logic:equal name="skin" value="PRISM2">
                                <nested:notEmpty name="browseCatalogForm" property="currentItems">
                                    <logic:equal name="browseCatalogForm" property="selectedCategory" value="600014">
                                        <tr>
                                            <td colspan="5">
                                                <h4 class="text-danger">
                                                    If an item is not listed, please submit your order through PRISM. Please include as much information as possible (e.g. part number, website link). The MDH buyers will contact you if they have any questions.
                                                </h4>
                                            </td>
                                        </tr>
                                    </logic:equal>
                                    <logic:notEqual name="browseCatalogForm" property="selectedCategory" value="600014">
                                    <tr>
                                        <td colspan="5">
                                            <h4 class="text-danger">
                                                If an item is not listed, please use the Request menu at the top of the page to find the correct form for requesting a non-catalog item.
                                            </h4>
                                        </td>
                                    </tr>
                                    </logic:notEqual>
                                </nested:notEmpty>
                            </logic:equal>
                        </table>
                    </div>
                </div>
            </nested:notEmpty>

            <logic:equal name="skin" value="PRISM2">
                <div class="row">
                    <div class="col-md-12">
                        <table class="table table-striped table-bordered">
                            <logic:equal value="600002" name="browseCatalogForm" property="selectedCategory">
                                <tr>
                                    <td><a href="${pageContext.request.contextPath}/viewAddNonCatalogItemToCart.do?categoryId=600029" class="text-danger">Software- (All software subject to Review &amp; Approval).</a></td>
                                </tr>
                                <tr>
                                    <td><a href="${pageContext.request.contextPath}/viewAddNonCatalogItemToCart.do?categoryId=600030" class="text-danger">IT Services and Contracts - (subject to review and CIO approval)</a></td>
                                </tr>
                                <tr>
                                    <td><a href="http://fyi.health.state.mn.us/it/software/" target="_blank">Software - Specifications</a></td>
                                </tr>
                            </logic:equal>

                            <logic:equal value="600029" name="browseCatalogForm" property="selectedCategory">
                                <tr>
                                    <td><a href="${pageContext.request.contextPath}/viewAddNonCatalogItemToCart.do?categoryId=600029" class="text-danger">Software- (All software subject to Review &amp; Approval).</a></td>
                                </tr>
                                <tr>
                                    <td><a href="http://fyi.health.state.mn.us/it/software/" target="_blank">Software - Specifications</a></td>
                                </tr>
                            </logic:equal>

                            <logic:equal value="600030" name="browseCatalogForm" property="selectedCategory">
                                <tr>
                                    <td><a href="${pageContext.request.contextPath}/viewAddNonCatalogItemToCart.do?categoryId=600030" class="text-danger">IT Services and Contracts - (subject to review and CIO approval)</a></td>
                                </tr>
                            </logic:equal>
                        </table>
                    </div>
                </div>
            </logic:equal>
        </form>
    </body>
</html>