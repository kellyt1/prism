<%@ include file="../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.model.inventory.Item"%>

<html>
    <head>
        <title>Advanced search</title>
    </head>
    <body>
        <div class="row">
            <div class="col-md-12">
                <form action="advancedSearchCatalog.do" method="post">
                    <fieldset>
                        <legend>Advanced Search</legend>
                        <table class="table">
                            <tr>
                                <td><label>Description:<br><nested:textarea name="advancedSearchCatalogForm" property="description" rows="2"/></label></td>
                                <td><label>Model:<br><nested:textarea name="advancedSearchCatalogForm" property="model" rows="2"/></label></td>
                                <td>
                                    <label>Category:<br>
                                        <nested:select property="categoryCode" name="advancedSearchCatalogForm" >
                                            <option value="">Any</option>
                                            <nested:optionsCollection name="advancedSearchCatalogForm" property="categories" label="name" value="categoryCode"/>
                                        </nested:select>
                                    </label>
                                </td>
                                <td>
                                    <label>Vendor:<br><nested:text name="advancedSearchCatalogForm" property="vendor"/></label>
                                </td>
                                <td>
                                    <label>Manufacturer:<br>
                                        <nested:select property="manufacturer" name="advancedSearchCatalogForm">
                                            <option value="">Any</option>
                                            <nested:optionsCollection name="advancedSearchCatalogForm" property="manufacturers" label="externalOrgDetail.orgName" value="manufacturerId"/>
                                        </nested:select>
                                    </label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>Dispense Unit:<br>
                                         <nested:select property="dispenseUnit" name="advancedSearchCatalogForm" >
                                            <option value="">Any</option>
                                            <nested:optionsCollection name="advancedSearchCatalogForm" property="units" label="name" value="name"/>
                                         </nested:select>
                                    </label>
                                </td>
                                <td>
                                    <label><bean:message key="orgBudget"/>*:<br>
                                        <nested:select property="orgBudget" name="advancedSearchCatalogForm" style="max-width: 200px;">
                                            <option value="">Any</option>
                                            <nested:optionsCollection name="advancedSearchCatalogForm" property="orgBudgets" label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
                                        </nested:select>
                                    </label>
                                </td>
                                <td>
                                    <label>Hazardous:</label><br>
                                    <label><html:radio name="advancedSearchCatalogForm" property="hazardous" value="">Either</html:radio></label>&nbsp;
                                    <label><html:radio name="advancedSearchCatalogForm" property="hazardous" value="true">Yes</html:radio></label>&nbsp;
                                    <label><html:radio name="advancedSearchCatalogForm" property="hazardous" value="false">No</html:radio></label>
                                </td>
                                <td>
                                    <label>Seasonal*:</label><br>
                                    <label><html:radio name="advancedSearchCatalogForm" property="seasonal" value="">Either</html:radio></label>&nbsp;
                                    <label><html:radio name="advancedSearchCatalogForm" property="seasonal" value="true">Yes</html:radio></label>&nbsp;
                                    <label><html:radio name="advancedSearchCatalogForm" property="seasonal" value="false">No</html:radio></label>
                                </td>
                                <td>
                                    <label>Status*:<br>
                                        <nested:select property="status" name="advancedSearchCatalogForm">
                                            <option value="">Any</option>
                                            <nested:optionsCollection name="advancedSearchCatalogForm" property="statuses" label="name" value="name"/>
                                        </nested:select>
                                    </label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>Item Type:</label><br>
                                    <label><html:radio name="advancedSearchCatalogForm" property="itemTypeSearchOption" value="AI">All Items</html:radio></label>&nbsp;
                                    <label><html:radio name="advancedSearchCatalogForm" property="itemTypeSearchOption" value="OPI">Only Non-Stock Items</html:radio></label>&nbsp;
                                    <label><html:radio name="advancedSearchCatalogForm" property="itemTypeSearchOption" value="OSI">Only Stock Items</html:radio></label>
                                </td>
                                <td>
                                    <label>ICNBR*:<br>
                                        <nested:present name="advancedSearchCatalogForm" property="fullIcnbr">
                                            <nested:text name="advancedSearchCatalogForm" property="fullIcnbr" size="10" />
                                        </nested:present>
                                        <nested:notPresent name="advancedSearchCatalogForm" property="fullIcnbr">NOT STOCK ITEM</nested:notPresent>
                                    </label>
                                </td>
                                <td colspan="3">* - this search field only applies to Stock Items</td>
                            </tr>
                        </table>
                    </fieldset>
                  <table class="table" align="center" >
                      <tr>
                        <td class="form-inline text-center">
                            <input type="SUBMIT" value="Search Catalog" class="form-control btn btn-default"/>
                            <button onclick="this.form.action='browseCatalog.do';this.form.submit(); return false;" class="form-control btn btn-default">Cancel</button>
                        </td>
                      </tr>
                  </table>

                <nested:empty name="advancedSearchCatalogForm" property="results">
                    <table width="90%" align="center" cellpadding="5" cellspacing="0">
                        <tr>
                            <td>No items containing all your search terms were found!<br/></td>
                        </tr>
                     </table>
                </nested:empty>
                <nested:notEmpty name="advancedSearchCatalogForm" property="results">
                  <table align="center" cellpadding="5" style="width: 90%; border-collapse: collapse;">
                    <tr>
                        <td>You have
                          <nested:write name="advancedSearchCatalogForm" property="resultsNumber"/> results:
                          <br/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <!--Description-->
                            <htmlx:sortAnchor name="advancedSearchCatalogForm"
                                    style="color: #FFFFFF;"
                                    property="results"
                                    value="description, itemId"
                                    input="/requests/advancedSearchCatalog.jsp">Description</htmlx:sortAnchor>
                        </th>
                        <th>
                            <!--Manufacturer-->
                            <htmlx:sortAnchor name="advancedSearchCatalogForm"
                                    style="color: #FFFFFF;"
                                    property="results"
                                    value="manufacturer.externalOrgDetail.orgName, itemId"
                                    input="/requests/advancedSearchCatalog.jsp">Manufacturer</htmlx:sortAnchor>
                        </th>
                        <th>
                            Model
                        </th>
                        <th>
                            <!--Dispense Unit-->
                            <htmlx:sortAnchor name="advancedSearchCatalogForm"
                                    style="color: #FFFFFF;"
                                    property="results"
                                    value="dispenseUnit.name, itemId"
                                    input="/requests/advancedSearchCatalog.jsp">Dispense Unit</htmlx:sortAnchor>
                        </th>
                        <th>
                            <!--Category-->
                            <htmlx:sortAnchor name="advancedSearchCatalogForm"
                                    style="color: #FFFFFF;"
                                    property="results"
                                    value="category.name, itemId"
                                    input="/requests/advancedSearchCatalog.jsp">Category</htmlx:sortAnchor>
                        </th>
                        <th>
                            </th>
                            <th>
                            </th>
                        </tr>
                        <nested:iterate name="advancedSearchCatalogForm" property="results">
                          <tr>
                            <td>
                              <a href="showItemDetails.do?itemId=<nested:write property="itemId"/>">
                                <nested:write property="description"/>
                              </a>
                              <br/>
                            </td>
                            <td>
                              <nested:write property="manufacturer.externalOrgDetail.orgName" />
                            </td>
                            <td>
                              <nested:write property="model" />
                            </td>
                            <td>
                              <nested:write property="dispenseUnit.name"/>
                            </td>
                            <td>
                                <nested:write property="category.name"/>
                            </td>
                            <td align="center">
                              <%--<input type="image" src="/images/addtocart.gif" onclick="this.form.action='viewAddCatalogItemToCart.do?itemId=<nested:write property="itemId"/>'; this.form.submit(); return false;"/>--%>
                              <nested:equal value="<%=Item.STOCK_ITEM%>" property="itemType">
                                    <nested:notEqual value="true" property="discontinued">
                                        <input type="image" src="${pageContext.request.contextPath}/images/addtocart.gif"
                                               onclick="this.form.action='viewAddCatalogItemToCart.do?itemId=<nested:write property="itemId"/>'; this.form.submit(); return false;"/>
                                    </nested:notEqual>
                                    <nested:equal value="true" property="discontinued">
                                        <font color="red">Discontinued</font>
                                    </nested:equal>
                                </nested:equal>
                                <nested:notEqual value="<%=Item.STOCK_ITEM%>" property="itemType">
                                        <input type="image" src="${pageContext.request.contextPath}/images/addtocart.gif"
                                               onclick="this.form.action='viewAddCatalogItemToCart.do?itemId=<nested:write property="itemId"/>'; this.form.submit(); return false;"/>
                                </nested:notEqual>

                            </td>
                            <td align="center">
                              <input type="image" src="${pageContext.request.contextPath}/images/addtolist.bmp"/>
                            </td>
                          </tr>
                        </nested:iterate>
                      </table>
                    </nested:notEmpty>
                    </form>
            </div>
        </div>
    </body>
</html>