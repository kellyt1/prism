<%@ include file="../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.model.inventory.Item" %>
<%@ page import="us.mn.state.health.model.inventory.StockItem" %>
<%@ page import="us.mn.state.health.view.materialsrequest.ShoppingListCatLineItemForm" %>

<html>
    <head>
        <title>All Shopping Lists</title>
    </head>
    <body>
        <html:form action="/viewMaintainShoppingListsAll" method="post">
            <div class="row">
                <div class="col-md-12">
                    <fieldset>
                        <legend>All Shopping Lists</legend>
                        <nested:notEmpty property="shoppingLists">
                            <label for="shoppingList" class="hidden">Shopping List</label>
                            <nested:select property="shoppingListId" value="" styleClass="chosen-select" styleId="shoppingList"
                                           onchange="this.form.action='viewEditShoppingListAll.do';this.form.submit()">
                                <option>Choose a Shopping List</option>
                                <nested:optionsCollection property="shoppingLists" label="name" value="shoppingListId"/>
                            </nested:select>
                        </nested:notEmpty>
                        &nbsp;
                        <a href="createNewShoppingList.do">Create New Shopping List</a><br/><br/>
                        <p>
                            <strong>Shopping List Name:</strong> <nested:write property="shoppingListForm.name"/>
                        </p>
                        <p>
                            <strong>Shopping List Owner:</strong> <nested:write property="shoppingListForm.username"/>
                            <small>(contact owner to change list)</small>
                        </p>
                        <label>Comments: <nested:textarea cols="40" rows="4" property="shoppingListForm.comment"  readonly="true"/></label>
                    </fieldset>
                </div>
            </div>
            <nested:notEmpty property="shoppingListForm">
                <div class="row">
                    <div class="col-md-12">
                        <table class="table table-bordered table-striped" id="catItems">
                            <caption>Catalog Items</caption>
                            <nested:notEmpty property="shoppingListForm.shoppingListCatLineItemForms">
                                <thead>
                                <tr>
                                    <th scope="col">Quantity</th>
                                    <th></th>
                                    <th scope="col">Item Description</th>
                                </tr>
                                </thead>
                                <nested:iterate id="catalogLineItemForm" property="shoppingListForm.shoppingListCatLineItemForms">
                                    <tr>
                                        <td class="text-center">
                                            <nested:hidden property="selected" value="false"/>
                                            <nested:write property="quantity"/>
                                        </td>
                                        <td>&nbsp;</td>
                                        <td>
                                            <nested:write property="item.description"/>
                                            <nested:equal value="Stock Item" property="item.itemType">
                                                <%
                                                    Item item = ((ShoppingListCatLineItemForm) catalogLineItemForm).getItem();
                                                    String isDisc = (((StockItem) item)).getDiscontinued().toString();
                                                    request.setAttribute("isDiscontinued", isDisc);%>
                                                <c:if test="${requestScope.isDiscontinued == 'true'}">
                                                    <span style="color: red; ">Discontinued</span>
                                                </c:if>
                                            </nested:equal>
                                        </td>
                                    </tr>
                                </nested:iterate>
                            </nested:notEmpty>
                            <nested:empty property="shoppingListForm.shoppingListCatLineItemForms">
                                <tr>
                                    <td>No Catalog Items in this Shopping List</td>
                                </tr>
                            </nested:empty>
                        </table>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <table class="table table-bordered table-striped" id="nonCatItems">
                            <caption>Non-Catalog Items</caption>
                            <nested:notEmpty property="shoppingListForm.shoppingListNonCatLineItemForms">
                                <thead>
                                <tr>
                                    <th class="text-center">Select All<br>
                                        <nested:checkbox property="shoppingListForm.selectAllNonCatItems" onchange="this.form.action='flipNonCatLineItemCheckBoxes.do#nonCatItems';this.form.submit()"/>
                                    </th>
                                    <th scope="col">Quantity</th>
                                    <th scope="col">Description</th>
                                    <th scope="col">Category</th>
                                    <th scope="col">Cost</th>
                                    <th scope="col">Hazardous?</th>
                                    <th scope="col">Vendor</th>
                                    <th scope="col">Vendor URL</th>
                                    <th scope="col">Catalog #</th>
                                </tr>
                                </thead>
                                <nested:iterate id="nonCatalogLineItemForm" property="shoppingListForm.shoppingListNonCatLineItemForms" indexId="a">
                                    <tr>
                                        <td class="text-center" style="vertical-align: middle;">
                                            <label for='<%= "selected_" + a.toString() %>' class="hidden">Selected</label>
                                            <nested:checkbox property="selected" styleId='<%= "selected_" + a.toString() %>'/>
                                        </td>
                                        <td>
                                            <label for='<%= "quantity_" + a.toString() %>' class="hidden">Selected</label>
                                            <nested:text property="quantity" size="4" styleId='<%= "quantity_" + a.toString() %>'/>
                                        </td>
                                        <td>
                                            <label for='<%= "description_" + a.toString() %>' class="hidden">Selected</label>
                                            <nested:text property="itemDescription" size="25" styleId='<%= "description_" + a.toString() %>'/>
                                        </td>
                                        <td>
                                            <label for='<%= "category_" + a.toString() %>' class="hidden">Selected</label>
                                            <nested:select property="categoryId" styleClass="chosen-select" styleId='<%= "category_" + a.toString() %>'>
                                                <option></option>
                                                <nested:optionsCollection property="categories" label="name" value="categoryId"/>
                                            </nested:select>
                                        </td>
                                        <td>
                                            <label for='<%= "itemCost_" + a.toString() %>' class="hidden">Selected</label>
                                            <nested:text property="itemCost" size="10" styleId='<%= "itemCost_" + a.toString() %>'/>
                                        </td>
                                        <td class="text-center" style="vertical-align: middle;">
                                            <label for='<%= "itemHazardous_" + a.toString() %>' class="hidden">Selected</label>
                                            <nested:checkbox property="itemHazardous" styleId='<%= "itemHazardous_" + a.toString() %>'/>
                                        </td>
                                        <td>
                                            <label for='<%= "suggestedVendorName_" + a.toString() %>' class="hidden">Selected</label>
                                            <nested:text property="suggestedVendorName" size="20" maxlength="50" styleId='<%= "suggestedVendorName_" + a.toString() %>'/>
                                        </td>
                                        <td>
                                            <label for='<%= "suggestedVendorURL_" + a.toString() %>' class="hidden">Selected</label>
                                            <nested:text size="20" property="suggestedVendorURL" styleId='<%= "suggestedVendorURL_" + a.toString() %>'/>
                                        </td>
                                        <td>
                                            <label for='<%= "suggestedVendorCatalogNumber_" + a.toString() %>' class="hidden">Selected</label>
                                            <nested:text property="suggestedVendorCatalogNumber" size="15" styleId='<%= "suggestedVendorCatalogNumber_" + a.toString() %>'/>
                                        </td>
                                    </tr>
                                </nested:iterate>
                            </nested:notEmpty>
                            <nested:empty property="shoppingListForm.shoppingListNonCatLineItemForms">
                                <tr>
                                    <td>No Non-Catalog Items in this Shopping List</td>
                                </tr>
                            </nested:empty>
                        </table>
                    </div>
                </div>
            </nested:notEmpty>
            <br/>
            <div class="row">
                <div class="col-md-12 text-center">
                    <div class="btn-group">
                        <nested:notEmpty property="shoppingListForm.shoppingListLineItemForms">
                            <button type="SUBMIT" onclick="this.form.action='addShoppingListToCart.do'" class="btn btn-default">Add Selected Items to Cart</button>
                        </nested:notEmpty>
                        <nested:notEmpty property="shoppingListForm">
                            <button onclick="this.form.action='/requests/index.jsp';this.form.submit();" class="btn btn-default">Cancel</button>
                        </nested:notEmpty>
                    </div>
                </div>
            </div>
        </html:form>
    </body>
</html>
