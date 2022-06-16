<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>Add Catalog Item to Shopping Cart</title>
    </head>
    <body>
        <nested:form action="/addCatalogItemToCart" method="post" enctype="multipart/form-data">
        <nested:hidden property="cmd" value=""/>
        <input type="HIDDEN" name="shoppingListAction" value="addCatReqLnItm"/>
            <div class="row">
                <div class="col-md-12">
                    <table class="table table-striped table-bordered">
                    <thead>
                        <tr>
                            <th scope="col">Item</th>
                            <th scope="col">Quantity</th>
                            <th scope="col">Unit</th>
                            <th scope="col">Unit Cost</th>
                            <th scope="col">Funding Source</th>
                            <th scope="col">
                                <logic:notEqual name="skin" value="PRISM2">Suggested Vendor</logic:notEqual>
                                <logic:equal name="skin" value="PRISM2">Notes</logic:equal>
                            </th>
                        </tr>
                    </thead>
                    <%--<c:choose>
                        <c:when test="${requestLineItemForm.item.itemType != 'Stock Item'}">
                            <tfoot>
                            <tr>
                                <th colspan="6"><label>Attach File: <nested:file property="purchasingInfoFile" size="20" style="display: inline; font-weight: 400;"/></label></th>
                            </tr>
                            </tfoot>
                        </c:when>
                    </c:choose>--%>
                    <tbody>
                        <tr>
                        <td>
                            <nested:write property="item.description" filter="false"/>
                            <nested:equal value="Stock Item" property="item.itemType">
                                <br>(IC# - <nested:write property="item.fullIcnbr"/>)
                            </nested:equal>
                        </td>
                        <td class="text-center"><nested:text property="quantity" onblur="refreshAmount()" size="5"/></td>
                        <td class="text-center"><nested:write property="item.dispenseUnit.name"/></td>
                        <td class="text-center">
                            <nested:present property="item.dispenseUnitCost">
                                <input type="HIDDEN" id="itemCost" name="itemCost" value="<nested:write property='item.dispenseUnitCost' format='0.00'/>"/>
                                $ <nested:write property="item.dispenseUnitCost" format="#,##0.00"/>
                            </nested:present>
                            <nested:notPresent property="item.dispenseUnitCost">
                                <input type="HIDDEN" id="itemCost" name="itemCost" value="0"/>
                                n/a
                            </nested:notPresent>
                        </td>
                        <!-- funding source section -->
                        <c:choose>
                            <c:when test="${requestLineItemForm.item.dispenseUnitCost <= 0 && requestLineItemForm.item.itemType eq 'Stock Item'}">
                                <td style="text-align:center;">Not Applicable - No Charge</td>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${requestLineItemForm.item.itemType eq 'Stock Item' && requestLineItemForm.stockItem.orgBudget.orgBudgetCode eq indirect}">
                                        <!-- this is a stock item funded by indirect, so do not collect the funding source information -->
                                        <td style="text-align:center;">Not Applicable - Funded by Indirect</td>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- this is either a purchase item or a stock item NOT funded by indirect, so we WILL collect the
                                        funding source information -->
                                        <td style="padding: 0;">
                                            <table id="fundingSourcesTable_NonCatItem" class="table" style="background: inherit;">
                                                <tr>
                                                    <th scope="col" style="padding-right: 25px;">
                                                        <label for="bdgt"><bean:message key="orgBudget"/>:</label>
                                                        <a href="#" onclick="window.open('./requests/help/whyIsMyBudgetMissing.jsp', 'priorityDef', 'width=1000,height=800,status=no,resizable=yes,top=200,left=200,dependent=yes,alwaysRaised=yes')" class="pull-right" style="font-weight: bold">Why can't I find my budget?</a>
                                                    </th>
                                                    <th scope="col" class="text-center"><label for="amt">Amount:</label></th>
                                                </tr>
                                                <nested:iterate property="fundingSourceForms" id="requestLineItemFundingSourceForm">
                                                    <tr class="text-left">
                                                        <td>
                                                            <nested:select property="orgBudgetId" style="width:550px" styleClass="chosen-select" styleId="bdgt" onchange="styleSelectedExpirationDate()">
                                                                <option value=""></option>
                                                                <nested:optionsCollection property="orgBudgets" label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
                                                            </nested:select>
                                                        </td>
                                                        <td class="text-center"><nested:text property="amount" size="6" styleId="amt"/></td>
                                                    </tr>
                                                </nested:iterate>
                                                <tr>
                                                    <td colspan="2">
                                                        <button onclick="this.form.cmd.value='addFundingSource'; this.form.submit(); return false;" class="btn btn-default">Add Another Funding Source</button>
                                                    </td>
                                                    <%--<td style="white-space: nowrap;">
                                                        <label><nested:radio property="amountInDollars" value="true"/>dollars</label>
                                                        <label><nested:radio property="amountInDollars" value="false"/>percent</label>
                                                    </td>--%>
                                                </tr>
                                            </table>
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                        <!-- END funding source section -->
                        <td>
                            <!-- Vendor section only for non-stock items -->
                            <c:choose>
                                <c:when test="${requestLineItemForm.item.itemType eq 'Stock Item'}">
                                    <!-- this is a stock item, so we don't need the vendor info -->
                                    <p class="text-center">Not Applicable for Stock Items</p>
                                </c:when>
                                <c:otherwise>
                                    <logic:notEqual name="skin" value="PRISM2">
                                        <table>
                                            <tr>
                                                <td><label>Vendor Name:<br><nested:text property="suggestedVendorName" size="25" maxlength="50"/></label></td>
                                            </tr>
                                            <tr>
                                                <td><label>Vendor URL:<br><nested:textarea property="suggestedVendorURL" cols="20" rows="1"/></label></td>
                                            </tr>
                                            <tr>
                                                <td><label>Vendor Catalog #:<br><nested:text property="suggestedVendorCatalogNumber" size="25"/></label></td>
                                            </tr>
                                        </table>
                                    </logic:notEqual>
                                    <logic:equal name="skin" value="PRISM2">
                                        <p style="font-weight: 700; color:#dc143c;">***List the MDH delivery name and address.<br/></p>
                                        <nested:textarea property="textNote" cols="30" rows="6"/>
                                    </logic:equal>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <!-- END Vendor section -->
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <br/>
        <div class="text-center">
            <button onclick="this.form.action='/requests/index.jsp'; this.form.submit(); return false;" class="btn btn-default">Cancel</button>
            <button onclick="this.form.cmd.value='addToShoppingList'; this.form.submit(); return false;" class="btn btn-default">Add To Shopping List</button>
            <button onclick="this.form.cmd.value='addToCart'; this.form.submit(); return false;" class="btn btn-default">Add To Cart</button>
        </div>
        </nested:form>
    </body>
</html>