<%@ include file="../include/tlds.jsp" %>


<html>
    <head>
        <title>Add Non-Catalog Item to Shopping Cart</title>
        <script type="text/javascript">
            var swift = false;
            function validateSwift() {
                if (swift == true && document.getElementById('swiftId').value == '') {
                    alert("AC2 Code is a required field. Please enter a AC2 Code.")
                    return false;
                } else {
                    return true;
                }
            }
        </script>
    </head>
    <body>
        <nested:form action="/addStaffingToCart" method="post" enctype="multipart/form-data">
            <nested:hidden property="cmd" value=""/>
            <logic:equal name="skin" value="PRISM2"><nested:hidden property="categoryId"/></logic:equal>
            <input type="HIDDEN" name="shoppingListAction" value="addNonCatReqLnItm"/>


            <div class="row">
                <div class="col-md-12">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th scope="col" style="text-align: center;">Item Information</th>
                                <th scope="col" style="text-align: center;">Funding Source</th>
                                <%--<th scope="col" style="text-align: center;">Other Information</th>--%>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                            <td style="padding: 0;">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th><label for="itemDescription">Description<br/>
                                                <nested:equal value="STAFFAUG" property="suggestedVendorCatalogNumber">
                                                <font color="red" size="-2">Enter the Application/Project Name from the
                                                    Budget Builder. Indicate static or line item funding:
                                                    <nested:write property="budgetBuilderLink" filter="false"/>
                                                </font></label></th>
                                            </nested:equal>
                                            <nested:equal value="MNITCONTRACT" property="suggestedVendorCatalogNumber">
                                                <font color="red" size="-2">Enter the Application/Project Name from the
                                                    Budget Builder. Indicate static or line item funding:
                                                    <nested:write property="budgetBuilderLink" filter="false"/>
                                                </font></label></th>
                                            </nested:equal>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <logic:notEqual name="skin" value="PRISM2">
                                                <td colspan="3">
                                                    <nested:textarea property="itemDescription" cols="40" rows="2" styleId="itemDescription"/>
                                                </td>
                                            </logic:notEqual>
                                            <logic:equal name="skin" value="PRISM2">
                                                <td colspan="4">
                                                    <nested:textarea property="itemDescription" cols="60" rows="2" styleId="itemDescription"/>
                                                </td>
                                            </logic:equal>
                                            <logic:notEqual name="skin" value="PRISM2">
                                                <td style="text-align: center;">
                                                    <p style="font-weight: 600;">Hazardous?:</p><br>
                                                    <logic:notEqual name="skin" value="PRISM2">
                                                            <label><nested:radio property="itemHazardous" value="true"/>Yes</label>
                                                            <br/>
                                                        <label><nested:radio property="itemHazardous" value="false"/>No</label>
                                                    </logic:notEqual>
                                                </td>
                                            </logic:notEqual>
                                        </tr>
                                        <tr>
                                            <logic:notEqual name="skin" value="PRISM2">
                                                <td>
                                                    <label for="categoryId">Category:</label><br>
                                                        <nested:select property="categoryId" styleId="categoryId">
                                                            <option value="">&nbsp;</option>
                                                            <nested:optionsCollection property="categories" label="name" value="categoryId"/>
                                                        </nested:select>
                                                </td>
                                            </logic:notEqual>
                                            <td>
                                                <label for="itemQuantity">Quantify:<br/>
                                                <font style="font-size: x-small" color="red">(Enter -1 to decrease encumbrance)</font></label><br/>
                                                <nested:text property="quantity" onblur="refreshAmount()"  size="5" styleId="itemQuantity" readonly="false"/>
                                            </td>
                                            <td>
                                                <%--<label for="itemUnits">Unit:</label><br>--%>
                                                <nested:hidden property="unitId"  value="47578" />
                                            </td>
                                            <td>
                                                <label for="itemCost">Cost of work:<br/>
                                                    <nested:equal value="MNITCONTRACT" property="suggestedVendorCatalogNumber">
                                                        <font color="red" size="-2">"Enter the current FY amount only.  If this is an amendment, only enter the current FY change amount</font>
                                                    </nested:equal>
                                                </label><br>
                                                $ <nested:text property="itemCost" onblur="refreshAmount()" size="10" styleClass="currency" styleId="itemCost"/>
                                            </td>
                                        </tr>
                                        <%--<logic:notEqual name="skin" value="PRISM2">--%>
                                            <%--<tr>--%>
                                                <%--<td colspan="4">--%>
                                                    <%--<label for="itemJustification">Explanation of what is happening</label><br/>--%>
                                                    <%--<nested:textarea property="itemJustification" cols="60" rows="3" styleId="itemJustification"/>--%>
                                                <%--</td>--%>
                                            <%--</tr>--%>
                                            <tr>
                                                <td colspan="2"><label>AC2 Code: <nested:text property="swiftItemId" styleId="swiftId"/></label></td>
                                                <td colspan="2"><label for="suggestedVendorName">MDH PO Number:
                                                    <br/><font style="font-size: x-small" color="red">(Enter "NEW" if no PO exists)</font>
                                                                </label>
                                                <nested:text property="suggestedVendorName" size="25" maxlength="50" styleId="suggestedVendorName"/>
                                                </td>


                                            </tr>
                                            <script type="text/javascript">swift = true;</script>
                                    </tbody>
                                </table>
                            </td>
                                <!-- funding source section -->
                                <td style="padding: 0;">
                                    <table class="table" id="fundingSourcesTable_NonCatItem" style="background: inherit;">
                                        <tr>
                                            <th scope="col" style="padding-right: 25px;">
                                                <label for="bdgt"><bean:message key="orgBudget"/>:</label>
                                                <a href="#" onclick="window.open('./requests/help/whyIsMyBudgetMissing.jsp', 'priorityDef', 'width=1000,height=800,status=no,resizable=yes,top=200,left=200,dependent=yes,alwaysRaised=yes')" class="pull-right" style="font-weight: bold">Why can't I find my budget?</a>
                                            </th>
                                            <th scope="col" class="text-center"><label for="amt">Amount:</label></th>
                                        </tr>
                                        <nested:iterate property="fundingSourceForms" id="requestLineItemFundingSourceForm" name="requestLineItemForm" indexId="a">
                                            <tr>
                                                <td>
                                                    <nested:select property="orgBudgetId" styleClass="chosen-select" styleId="bdgt" onchange="styleSelectedExpirationDate()">
                                                        <option value="">Select an OPTION - begin By Typing Your Budget Code</option>
                                                        <nested:optionsCollection property="orgBudgets" label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
                                                    </nested:select>
                                                </td>
                                                <td style="white-space: nowrap;">
                                                    $ <nested:text property="amount" size="10" styleId="amt"/>
                                                </td>
                                            </tr>
                                        </nested:iterate>
                                        <tr>
                                            <td colspan="2">
                                                <button onclick="this.form.cmd.value='addFundingSource'; this.form.submit(); return false;" class="btn btn-default">Add Another Funding Source</button>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <%--<td style="padding: 0;">--%>
                                    <%--<table class="table">--%>
                                            <%--<tr>--%>
                                                <%--<td><label>Vendor Information:<br><nested:textarea property="suggestedVendorURL" cols="20" rows="1"/></label></td>--%>
                                            <%--</tr>--%>
                                            <%--<tr>--%>
                                                <%--<td>--%>
                                                    <%--<label for="textNote">Funding Notes:</label><br>--%>
                                                    <%--<nested:textarea property="textNote" cols="30" rows="4" styleId="textNote"/>--%>
                                                <%--</td>--%>
                                            <%--</tr>--%>
                                        <%--<tr>--%>
                                            <%--<td>--%>
                                                <%--<label>Internal Code:<br/>--%>
                                                    <nested:hidden property="suggestedVendorCatalogNumber"/>
                                                <%--</label>--%>
                                            <%--</td>--%>
                                        <%--</tr>--%>

                                    <%--</table>--%>
                                <%--</td>--%>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        <div class="row">
            <div class="col-md-12 text-center">
                <div class="btn-group">
                    <button onclick="this.form.action='/requests/index.jsp'; this.form.submit(); return false;" class="btn btn-default">Cancel</button>
                    <button onclick="this.form.cmd.value='addToShoppingList'; this.form.submit(); return false;" class="btn btn-default">Add To Shopping List</button>
                    <button onclick="this.form.cmd.value='addToCart'; if(validateSwift() == false) return false; this.form.submit(); return false;" class="btn btn-default">Add To Cart</button>
                </div>
            </div>
        </div>
        </nested:form>
    </body>
</html>