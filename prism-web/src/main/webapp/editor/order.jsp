<%@ include file="../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.model.purchasing.Order" %>

<html>
    <head>
        <title>Purchase Order</title>
        <script type="text/javascript">
            function updateVendorInstructions(inId) {
                if (inId == null || inId == '') {
                    document.orderForm.vendorInstructions.value = '';
                }
                else {
                    xmlhttpGet('/updateVendorInstructions.do?shipToID=' + inId );
                }

                return true;
            }
            function xmlhttpGet(strURL, elementId) {
                var xmlHttp;
                try {// Firefox, Opera 8.0+, Safari
                    xmlHttp = new XMLHttpRequest();
                }
                catch (e) {
                    // Internet Explorer
                    try {
                        xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
                    } catch (e) {
                        try {
                            xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
                        } catch (e) {
                            alert("Your browser does not support AJAX!");
                            return false;
                        }
                    }
                }
                xmlHttp.onreadystatechange = function () {
                    if (xmlHttp.readyState == 4) {
                        updatepage(xmlHttp.responseText);
                    }
                };
                xmlHttp.open("GET", strURL, true);
                xmlHttp.send(null);
            }
            function updatepage(str) {
                try {
                    //alert(str);
                    document.orderForm.vendorInstructions.value = str;
                    //document.getElementById("vendorInstructions2").innerHTML = str;
                } catch(ex) {
                    alert(ex)
                }
            }
        </script>
        <script type="text/javascript">
            function removePurchaseOrderNote(key) {
                var remove = confirm("Remove this Note from Order?");
                if (remove) {
                    window.document.orderForm.noteFormKey.value = key;
                    window.document.orderForm.action = "removePurchaseOrderNote.do";
                    return true;
                }
                else {
                    return false;
                }
            }
        </script>
    </head>

    <body>
        <html:form action="/saveOrder2" method="post">
            <input type="HIDDEN" name="cmd" value=""/>
            <input type="HIDDEN" name="key" value=""/>
            <input type="HIDDEN" name="requestLineItemId" value=""/>
            <input type="HIDDEN" name="input" value="order"/>
            <nested:hidden property="noteFormKey"/>
            <fieldset>
                <%--<legend>--%>
                    <%--<nested:empty property="orderId">Create Purchase Order</nested:empty>--%>
                    <%--<nested:notEmpty property="orderId">Edit Purchase Order</nested:notEmpty>--%>
                <%--</legend>--%>
                <table class="table">
                    <caption>Order Detail</caption>
                    <tr>
                        <th class="text-right">Vendor Info:</th>
                        <td>
                            <table>
                            <tr>
                                <td>
                                    <%--<a href="reloadVendorList2.do?start=1&end=9">1-9</a>&nbsp;&nbsp;--%>
                                    <%--<a href="reloadVendorList2.do?start=A&end=E">A-E</a>&nbsp;&nbsp;--%>
                                    <%--<a href="reloadVendorList2.do?start=F&end=J">F-J</a>&nbsp;&nbsp;--%>
                                    <%--<a href="reloadVendorList2.do?start=K&end=O">K-O</a>&nbsp;&nbsp;--%>
                                    <%--<a href="reloadVendorList2.do?start=P&end=T">P-T</a>&nbsp;&nbsp;--%>
                                    <%--<a href="reloadVendorList2.do?start=U&end=Z">U-Z</a><br>--%>
                                    <nested:select disabled="true" property="vendorId" onchange="this.form.action='chooseOrderVendor.do';this.form.submit(); return false;" styleClass="chosen-select">
                                        <option value=""></option>
                                        <nested:optionsCollection property="vendors" label="externalOrgDetail.orgName" value="vendorId"/>
                                    </nested:select>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <logic:notEmpty name="orderForm" property="vendor">
                                        <logic:notEmpty name="orderForm" property="vendor.externalOrgDetail">
                                            <logic:notEmpty name="orderForm" property="vendor.externalOrgDetail.webAddress">
                                                <br><a href='<nested:write name="orderForm" property="vendor.externalOrgDetail.webAddress"/>' target="NEW">View vendor URL</a>
                                            </logic:notEmpty>
                                        </logic:notEmpty>
                                    </logic:notEmpty>
                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td>
                                    <label>
                                        Contract:<br/>
                                        <logic:notEmpty name="orderForm" property="vendor">
                                            <logic:notEmpty name="orderForm" property="vendorContracts">
                                                <nested:select disabled="true" property="vendorContractId" styleClass="chosen-select">
                                                    <option value="">Choose a Vendor Contract</option>
                                                    <nested:optionsCollection property="vendorContracts" label="contractNumber" value="vendorContractId"/>
                                                </nested:select>
                                            </logic:notEmpty>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="orderForm" property="vendor">
                                            <logic:empty name="orderForm" property="vendorContracts">
                                                No Contracts For this Vendor
                                                <input type="hidden" name="vendorContractId" value="">
                                            </logic:empty>
                                        </logic:notEmpty>
                                    </label>
                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td>
                                    <label>
                                        Account:<br/>
                                        <logic:notEmpty name="orderForm" property="vendor">
                                            <logic:notEmpty name="orderForm" property="vendorAccounts">
                                                <nested:select disabled="true" property="vendorAccountId" styleClass="chosen-select">
                                                    <option value="">Choose a Vendor Account</option>
                                                    <nested:optionsCollection property="vendorAccounts" label="accountNbr" value="vendorAccountId"/>
                                                </nested:select>
                                            </logic:notEmpty>
                                        </logic:notEmpty>
                                        <logic:notEmpty name="orderForm" property="vendor">
                                            <logic:empty name="orderForm" property="vendorAccounts">
                                                No Accounts For this Vendor
                                                <input type="hidden" name="vendorAccountId" value="">
                                            </logic:empty>
                                        </logic:notEmpty>
                                    </label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <br/>
                                    <table class="table table-bordered">
                                        <thead>
                                            <tr>
                                                <th>Contact(s):</th>
                                                <th>Phone:</th>
                                                <th>Fax:</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>
                                                    <logic:notEmpty name="orderForm" property="vendor">
                                                        <logic:notEmpty name="orderForm" property="vendorReps">
                                                            <nested:iterate property="vendorReps" id="vr">
                                                                <%--<%=((ExtOrgDetailPhone )vp).getPhone().getNumber()%>--%>
                                                                <logic:present name="vr" property="rep.firstAndLastName">
                                                                    <nested:write name="vr" property="rep.firstAndLastName"/>
                                                                    <br/>
                                                                </logic:present>
                                                            </nested:iterate>
                                                        </logic:notEmpty>
                                                    </logic:notEmpty>
                                                    <logic:notEmpty name="orderForm" property="vendor">
                                                        <logic:empty name="orderForm" property="vendorReps">Unknown</logic:empty>
                                                    </logic:notEmpty>
                                                </td>
                                                <td>
                                                    <logic:notEmpty name="orderForm" property="vendor">
                                                        <logic:notEmpty name="orderForm" property="vendorPhones">
                                                            <nested:iterate property="vendorPhones" id="vp">
                                                                <%--<%=((ExtOrgDetailPhone )vp).getPhone().getNumber()%>--%>
                                                                <nested:write name="vp" property="phone.number"/>
                                                                <br/>
                                                            </nested:iterate>
                                                        </logic:notEmpty>
                                                    </logic:notEmpty>
                                                    <logic:notEmpty name="orderForm" property="vendor">
                                                        <logic:empty name="orderForm" property="vendorPhones">Unknown</logic:empty>
                                                    </logic:notEmpty>
                                                </td>
                                                <td>
                                                    <logic:notEmpty name="orderForm" property="vendor">
                                                        <logic:notEmpty name="orderForm" property="vendorFaxes">
                                                            <nested:iterate property="vendorFaxes" id="vf">
                                                                <%--<%=((ExtOrgDetailPhone )vp).getPhone().getNumber()%>--%>
                                                                <nested:write name="vf" property="phone.number"/>
                                                                <br/>
                                                            </nested:iterate>
                                                        </logic:notEmpty>
                                                    </logic:notEmpty>
                                                    <logic:notEmpty name="orderForm" property="vendor">
                                                        <logic:empty name="orderForm" property="vendorFaxes">Unknown</logic:empty>
                                                    </logic:notEmpty>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="3">
                                                    <logic:notEmpty name="orderForm" property="vendor">
                                                        <logic:notEmpty name="orderForm" property="vendorEmails">
                                                            <nested:iterate property="vendorEmails" id="ve">
                                                                <%--<%=((ExtOrgDetailPhone )vp).getPhone().getNumber()%>--%>
                                                                <nested:write name="ve" property="emailAddress.emailAddress"/>
                                                                <br/>
                                                            </nested:iterate>
                                                        </logic:notEmpty>
                                                    </logic:notEmpty>
                                                    <logic:notEmpty name="orderForm" property="vendor">
                                                        <logic:empty name="orderForm" property="vendorEmails">Unknown</logic:empty>
                                                    </logic:notEmpty>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td><label>Address:</label><br/>
                                    <logic:notEmpty name="orderForm" property="vendor">
                                        <logic:notEmpty name="orderForm" property="vendorAddresses">
                                            <nested:select disabled="true" property="vendorAddressId" styleClass="chosen-select">
                                                <option value=""></option>
                                                <nested:optionsCollection property="vendorAddresses" label="mailingAddress.shortSummary" value="mailingAddress.mailingAddressId"/>
                                            </nested:select>
                                        </logic:notEmpty>
                                    </logic:notEmpty>
                                    <logic:notEmpty name="orderForm" property="vendor">
                                        <logic:empty name="orderForm" property="vendorAddresses"> No Addresses for this Vendor</logic:empty>
                                    </logic:notEmpty>
                                </td>
                            </table>
                        </td>
                        <td colspan="2">
                            <table>
                                <tr >
                                    <td class="text-right"><label>PO #:</label></td>
                                    <td><nested:text property="purchaseOrderNumber" size="20" disabled="true"/></td>
                                </tr>
                                <tr>
                                    <td colspan="2">&nbsp;</td>
                                </tr>
                                <tr>
                                    <td class="text-right" style="vertical-align: middle;"><label>PO # Type:</label></td>
                                    <td>
                                        <label><nested:radio property="purchaseOrderNumberType" disabled="true" value="<%=Order.PO_NBR_TYPE_MAPS%>"/> SWIFT</label><br/>
                                        <label><nested:radio property="purchaseOrderNumberType" disabled="true" value="<%=Order.PO_NBR_TYPE_CREDIT%>"/> Credit</label><br/>
                                        <label><nested:radio property="purchaseOrderNumberType" disabled="true" value="<%=Order.PO_NBR_TYPE_NOCHARGE%>"/> No-Charge</label>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td colspan="2">
                            <table>
                                <tr>
                                    <td class="text-right"><label>OPR #:</label>&nbsp;</td>
                                    <td>
                                        <nested:empty property="orderId">Unassigned</nested:empty>
                                        <nested:notEmpty property="orderId">
                                            <nested:write property="orderId"/>
                                        </nested:notEmpty>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">&nbsp;</td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>Date Created:</label>&nbsp;</td>
                                    <td>
                                        <nested:notEmpty property="insertionDate">
                                            <nested:write property="insertionDate" format="MM/dd/yyyy"/>
                                        </nested:notEmpty>
                                        <nested:empty property="insertionDate">Unassigned</nested:empty>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">&nbsp;</td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>Suspense Date:</label>&nbsp;</td>
                                    <td><nested:text property="suspenseDate" disabled="true" size="10" styleClass="dateInput"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right"><label>Bill To:</label></td>
                        <td>
                            <nested:select property="billToAddressId" disabled="true" style="width:200px" styleClass="chosen-select">
                                <option value=""></option>
                                <nested:optionsCollection property="billToAddresses" label="shortSummary" value="mailingAddressId"/>
                            </nested:select>
                        </td>
                        <td class="text-right"><label>Other Bill To:</label></td>
                        <td>
                            <nested:textarea property="otherBillToAddress" cols="25" rows="4" disabled="true"/>
                        </td>
                        <td class="text-right"><label>Remit To:</label></td>
                        <td>
                            <nested:textarea property="remitToAddress" cols="25" rows="4" disabled="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right"><label>Ship To:</label></td>
                        <td>
                            <nested:select property="shipToAddressId" disabled="true" style="width:200px" onchange="updateVendorInstructions(this.value)" styleClass="chosen-select">
                                <option value=""></option>
                                <nested:optionsCollection property="shipToAddresses" label="shortSummary" value="mailingAddressId"/>
                            </nested:select>
                        </td>

                        <td class="text-right"><label>Other Ship To:</label></td>
                        <td>
                            <nested:textarea property="otherShipToAddress" cols="40" rows="2" disabled="true"/>
                        </td>
                        <td class="text-right"><label>Vendor<br>Instructions:</label></td>
                        <td colspan="3">
                        <span id="vendorInstructions2">
                            <nested:textarea property="vendorInstructions" cols="30" rows="2" disabled="true"/>
                         </span>
                        </td>
                    </tr>
                </table>
            </fieldset>
            <br>
            <table class="table table-bordered table-striped">
                <caption>Purchase Order Notes</caption>
                <thead>
                    <tr>
                        <th>Note <button type="submit" disabled="true" onclick="this.form.action='addPurchaseOrderNote.do'" class="btn btn-default pull-right">Add Note</button></th>
                        <th>Author</th>
                        <th>Date</th>
                        <%--<td><button type="submit" onclick="this.form.action='addPurchaseOrderNote.do'" class="btn btn-default">Add Note</button></td>--%>
                    </tr>
                </thead>
                <tbody>
                    <nested:iterate id="orderNoteForm" property="orderNoteForms">
                        <nested:equal property="removed" value="false">
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${User.firstAndLastName == orderNoteForm.authorName}">
                                            <nested:textarea property="note" cols="40" rows="2"/>
                                            <button type="submit" onclick="return removePurchaseOrderNote('<nested:write property='key'/>')" class="btn btn-default pull-right">Remove Note</button>
                                        </c:when>
                                        <c:otherwise>
                                            <nested:write property="note"/>
                                            <button type="submit" class="btn btn-default pull-right" disabled>Remove Note</button>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <nested:write property="authorName"/>
                                </td>
                                <td>
                                    <nested:write property="insertionDate" format="MM/dd/yyyy"/>
                                </td>
                                <%--<td>
                                    <c:if test="${User.firstAndLastName == orderNoteForm.authorName}">
                                        <button type="submit" onclick="return removePurchaseOrderNote('<nested:write property='key'/>')" class="btn btn-default">Remove Note</button>
                                    </c:if>
                                </td>--%>
                            </tr>
                        </nested:equal>
                    </nested:iterate>
                    <nested:empty property="orderNoteForms">
                        <tr>
                            <td colspan="4">No notes have been created</td>
                        </tr>
                    </nested:empty>
                </tbody>
            </table>
            <br>
            <table class="table table-bordered">
                <caption>Order Line Items</caption>
                <nested:notEmpty property="orderLineItemForms">
                    <tfoot>
                        <tr>
                            <nested:greaterThan property="reqLnItemFormCount" value="0">
                            <th class="text-right" colspan="10">
                                </nested:greaterThan>
                                <nested:equal property="reqLnItemFormCount" value="0">
                            <th class="text-right" colspan="9">
                                </nested:equal>
                                <strong>Total Cost:</strong></th>
                            <td>$ <nested:write property="buyUnitsTotalCost" format="#,##0.00"/></td>
                        </tr>
                    </tfoot>
                    <tbody>
                        <nested:iterate id="orderLineItemForm" property="orderLineItemForms">
                            <nested:equal property="removed" value="false">
                                <tr>
                                    <td style="font-weight: 700; font-style: italic;">
                                        <logic:notEmpty name="orderLineItemForm" property="orderLineItem.requestLineItems">
                                            <nested:iterate property="orderLineItem.requestLineItems" id="vrs">
                                                <logic:notEmpty name="vrs" property="request">
                                                    <nested:write name="vrs" property="request.trackingNumber"/>
                                                </logic:notEmpty>
                                            </nested:iterate>
                                        </logic:notEmpty>
                                    </td>
                                    <td class="text-center">
                                        <logic:greaterThan name="orderForm" property="reqLnItemFormCount" value="0">
                                            <nested:notEqual property="orderLineItem.status.statusCode" value="RCD">
                                                <nested:notEqual property="orderLineItem.status.statusCode" value="RCP">
                                                    <label><input type="RADIO" name="orderLineItemKey" value="<nested:write property='key'/>"/></label>
                                                </nested:notEqual>
                                            </nested:notEqual>
                                        </logic:greaterThan>
                                    </td>
                                    <td class="text-right">Line Item #:</td>
                                    <td>
                                        <%--<nested:notEmpty property="orderLineItemId">
                                            <strong>OLI<nested:write property="orderLineItem.orderLineItemId"/></strong>
                                        </nested:notEmpty>
                                        <nested:empty property="orderLineItem.orderLineItemId">
                                            Unassigned
                                        </nested:empty>--%>
                                        <span><nested:write property="lineItemNumber"/></span>
                                    </td>
                                    <td class="text-right">Item<br>Description:</td>
                                    <td colspan="3">
                                        <nested:equal value="true" property="orderLineItem.itemIsStockItem">
                                            <nested:write property="orderLineItem.fullIcnbr"/><br/>
                                        </nested:equal>
                                        <nested:write property="orderLineItem.itemDescription"/>
                                    </td>
                                    <td class="text-right">Vendor Catalog #:</td>
                                    <td>
                                        <nested:notEmpty property="orderLineItem.vendorCatalogNbr">
                                            <nested:write property="orderLineItem.vendorCatalogNbr"/>
                                        </nested:notEmpty>
                                    </td>
                                    <td rowspan="2" class="text-right">Status:</td>
                                    <td rowspan="2">
                                        <nested:notEmpty property="orderLineItem.status">
                                            <nested:write property="orderLineItem.status.name"/>
                                        </nested:notEmpty>
                                    </td>
                                    <td rowspan="2" class="text-center" style="vertical-align: middle;">
                                        <button type="SUBMIT" disabled="true" onclick="this.form.action='viewEditOrderLineItem2.do';this.form.key.value='<nested:write property='key'/>'" class="btn btn-default">Edit</button>
                                        <nested:notEqual property="orderLineItem.status.statusCode" value="RCD">
                                            <nested:notEqual property="orderLineItem.status.statusCode" value="RCP">
                                                &nbsp;<button type="SUBMIT" disabled="true" onclick="this.form.action='removeOrderLineItem.do';this.form.key.value='<nested:write property='key'/>'" class="btn btn-default">Remove</button>
                                            </nested:notEqual>
                                        </nested:notEqual>
                                    </td>
                                </tr>
                                <tr>
                                <logic:greaterThan name="orderForm" property="reqLnItemFormCount" value="0">
                                    <nested:notEqual property="orderLineItem.status.statusCode" value="RCD">
                                        <nested:notEqual property="orderLineItem.status.statusCode" value="RCP">
                                            <td></td>
                                        </nested:notEqual>
                                    </nested:notEqual>
                                </logic:greaterThan>
                                <td class="text-right">Quantity:</td>
                                <td>
                                    <nested:write property="orderLineItem.quantity"/>
                                </td>
                                <td class="text-right">Buy Unit:</td>
                                <td>
                                    <nested:notEmpty property="orderLineItem.buyUnit">
                                        <nested:write property="orderLineItem.buyUnit.name"/>
                                    </nested:notEmpty>
                                    <nested:empty property="orderLineItem.buyUnit">Unspecified</nested:empty>
                                </td>
                                <td class="text-right">Buy Unit Cost:</td>
                                <td nowrap="nowrap">
                                    <nested:notEmpty property="orderLineItem.buyUnitCost">
                                        $ <nested:write property="orderLineItem.buyUnitCost" format="#,##0.00"/>
                                    </nested:notEmpty>
                                    <nested:empty property="orderLineItem.buyUnitCost">Unspecified</nested:empty>
                                </td>
                                <td class="text-right">Discount:</td>
                                <td>
                                    <nested:notEmpty property="orderLineItem.discountPercent">
                                        <nested:write property="orderLineItem.discountPercent"/> %
                                    </nested:notEmpty>
                                    <nested:empty property="orderLineItem.discountPercent">Unspecified</nested:empty>
                                </td>
                                <td class="text-right">Line Cost:</td>
                                <td>
                                    <nested:notEmpty property="orderLineItem.buyUnitTotalCost">
                                        $ <nested:write property="orderLineItem.buyUnitTotalCost" format="#,##0.00"/>
                                    </nested:notEmpty>
                                    <nested:empty property="orderLineItem.buyUnitTotalCost">N/A</nested:empty>
                                </td>
                                <%--<td colspan="3"></td>--%>
                            </tr>
                            <bean:write name="orderForm" property="flip"/>
                        </nested:equal>
                    </nested:iterate>
                </tbody>
            </nested:notEmpty>
            <nested:empty property="orderLineItemForms">No Order Line Items have been created</nested:empty>
        </table>
        <br>
        <nested:greaterThan property="reqLnItemFormCount" value="0">
            <table class="table table-bordered table-striped">
                <caption>Request Line Items</caption>
                <thead>
                    <tr>
                        <th></th>
                        <th>
                            <htmlx:sortAnchor name="orderForm"
                                              style="color: inherit;"
                                              property="requestLineItemForms"
                                              value="requestLineItem.requestLineItemId"
                                              input="/editor/order.jsp">Request<br /> Line #
                            </htmlx:sortAnchor>
                        </th>
                        <th>
                            <htmlx:sortAnchor name="orderForm"
                                              style="color: inherit;"
                                              property="requestLineItemForms"
                                              value="descriptionSummary, requestLineItem.requestLineItemId"
                                              input="/editor/order.jsp">Description
                            </htmlx:sortAnchor>
                        </th>
                        <th nowrap>
                            <htmlx:sortAnchor name="orderForm"
                                              style="color: inherit;"
                                              property="requestLineItemForms"
                                              value="requestLineItem.suggestedVendorName, requestLineItem.requestLineItemId"
                                              input="/editor/order.jsp">Suggested<br />Vendor
                            </htmlx:sortAnchor>
                        </th>
                        <th>
                            <htmlx:sortAnchor name="orderForm"
                                              style="color: inherit;"
                                              property="requestLineItemForms"
                                              value="requestLineItem.suggestedVendorCatalogNumber, requestLineItem.requestLineItemId"
                                              input="/editor/order.jsp">Vendor Catalog #
                            </htmlx:sortAnchor>
                        </th>
                        <th>
                            <htmlx:sortAnchor name="orderForm"
                                              style="color: inherit;"
                                              property="requestLineItemForms"
                                              value="requestLineItem.fundingSrcSummary.orgBudgetCodes, requestLineItem.requestLineItemId"
                                              input="/editor/order.jsp">
                                <bean:message key="orgBudget"/>
                                Code(s)
                            </htmlx:sortAnchor>
                        </th>
                        <th>
                            <htmlx:sortAnchor name="orderForm"
                                              style="color: inherit;"
                                              property="requestLineItemForms"
                                              value="requestLineItem.status.name, requestLineItem.requestLineItemId"
                                              input="/editor/order.jsp">Status
                            </htmlx:sortAnchor>
                        </th>
                        <th>Deliver To</th>
                        <th>
                            <htmlx:sortAnchor name="orderForm"
                                              style="color: inherit;"
                                              property="requestLineItemForms"
                                              value="requestLineItem.status.name, requestLineItem.requestLineItemId"
                                              input="/editor/order.jsp">Need By
                            </htmlx:sortAnchor>
                        </th>
                        <th></th>
                    </tr>
                </thead>
            <nested:iterate property="requestLineItemForms">
                <nested:equal property="removedFromOrder" value="false">
                <tr>
                    <td>
                        <nested:checkbox property="selected"/>
                    </td>
                    <td>
                        <nested:write property="requestLineItem.request.trackingNumber"/>
                        <br>(<nested:write property="requestLineItem.requestLineItemId"/>)
                    </td>
                    <td>
                        <nested:present property="icnbr"><nested:write property="icnbr"/><br/></nested:present>
                        <nested:write property="descriptionSummary"/>
                    </td>
                    <td>
                        <nested:notEmpty property="requestLineItem.primaryItemVendor">
                            <nested:write property="requestLineItem.primaryItemVendor.vendor.externalOrgDetail.orgName"/>
                        </nested:notEmpty>
                        <i>

                            <nested:empty property="requestLineItem.suggestedVendorURL">
                                <nested:write property="requestLineItem.suggestedVendorName"/>
                            </nested:empty>
                            <nested:notEmpty property="requestLineItem.suggestedVendorURL">
                                <a href='<nested:write property="requestLineItem.suggestedVendorURL"/>' target="NEW"><nested:write property="requestLineItem.suggestedVendorName"/></a>
                            </nested:notEmpty>
                        </i>
                    </td>
                    <td>
                        <nested:notEmpty property="requestLineItem.primaryItemVendor">
                            <nested:write property="requestLineItem.primaryItemVendor.vendorCatalogNbr"/>
                        </nested:notEmpty>
                        <i><nested:write property="requestLineItem.suggestedVendorCatalogNumber"/></i>
                    </td>
                    <td>
                        <nested:write property="requestLineItem.fundingSrcSummary.orgBudgetCodes"/>
                    </td>
                    <td>
                        <nested:write property="requestLineItem.status.name"/>
                    </td>
                    <td>
                        <nested:present property="requestLineItem.request.deliveryDetail">
                            <table>
                                <logic:equal name="orderForm" property="odd" value="true">
                                <tr>
                                    </logic:equal>
                                    <logic:equal name="orderForm" property="odd" value="false">
                                <tr>
                                    </logic:equal>
                                    <td nowrap="nowrap">
                                        <nested:present property="requestLineItem.request.deliveryDetail.organization">
                                            <nested:write property="requestLineItem.request.deliveryDetail.organization.orgName"/>
                                        </nested:present>
                                        <nested:notPresent property="requestLineItem.request.deliveryDetail.organization">
                                            <nested:present property="requestLineItem.request.deliveryDetail.facility">MDH</nested:present>
                                        </nested:notPresent>
                                    </td>
                                </tr>
                                <logic:equal name="orderForm" property="odd" value="true">
                                <tr>
                                    </logic:equal>
                                    <logic:equal name="orderForm" property="odd" value="false">
                                <tr>
                                    </logic:equal>
                                    <td><nested:write property="requestLineItem.request.deliveryDetail.recipientName"/></td>
                                </tr>
                                <nested:present property="requestLineItem.request.deliveryDetail.facility">
                                    <tr>
                                        <td><nested:write property="requestLineItem.request.deliveryDetail.facility.facilityName"/></td>
                                    </tr>
                                </nested:present>
                                <nested:present property="requestLineItem.request.deliveryDetail.mailingAddress">
                                    <logic:equal name="orderForm" property="odd" value="true">
                                        <tr>
                                    </logic:equal>
                                    <logic:equal name="orderForm" property="odd" value="false">
                                        <tr>
                                    </logic:equal>
                                    <td nowrap="nowrap">
                                        <nested:write property="requestLineItem.request.deliveryDetail.mailingAddress.address1"/>
                                        <br/>
                                        <nested:present property="requestLineItem.request.deliveryDetail.mailingAddress.address2">
                                            <nested:write property="requestLineItem.request.deliveryDetail.mailingAddress.address2"/>
                                            <br/>
                                        </nested:present>
                                        <nested:write property="requestLineItem.request.deliveryDetail.mailingAddress.city"/>
                                        ,
                                        <nested:write property="requestLineItem.request.deliveryDetail.mailingAddress.state"/>
                                        &nbsp;
                                        <nested:write property="requestLineItem.request.deliveryDetail.mailingAddress.zip"/>
                                    </td>
                                    </tr>
                                </nested:present>
                            </table>
                        </nested:present>
                    </td>
                    <td>
                        <nested:write format="MM/dd/yyyy" property="requestLineItem.request.needByDate"/>
                    </td>
                    <td class="text-center">
                        <button type="submit" disabled="true" onclick="this.form.action='viewEditPurchasingRequestFromOrder.do';this.form.requestLineItemId.value='<nested:write property='requestLineItem.requestLineItemId'/>'"
                                class="btn btn-default">Edit</button><br><br>
                        <button type="submit" disabled="true" onclick="this.form.action='removeRequestLineItemFromOrder.do';this.form.requestLineItemId.value='<nested:write property='requestLineItem.requestLineItemId'/>'"
                                class="btn btn-default">Remove</button>

                        <nested:notEmpty property="requestLineItem.item">
                            <br><a href=""
                            <%--onclick="window.open('/viewStockItemActionRequestInfo?itemId='+escape('<nested:write property="requestLineItem.item.ItemId"/>'),'','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=no, HEIGHT=150, WIDTH=350, left=350, top=150');return false">--%>
                            <%--onclick="window.open('/viewStockItemActionRequestInfo?itemId=1067635','','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=no, HEIGHT=150, WIDTH=350, left=350, top=150');return false">--%>
                                   onclick="window.open('/viewStockItemActionRequestInfo?itemId='+encodeURIComponent('<nested:write property="requestLineItem.item.itemId"/>'),'','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=no, HEIGHT=150, WIDTH=350, left=350, top=150');return false">
                                <%--onclick="window.open('/viewStockItemActionRequestInfo?itemId='+<nested:write property="item.itemId"/>,'','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=no, HEIGHT=150, WIDTH=350, left=350, top=150');return false">--%>
                                <%--<br/><a href="additionalInstructions.do?instructions=<nested:write property="requestLineItem.request.additionalInstructions"/>" target="new">--%>
                            Suggested Vendor Info
                        </a><br/>
                        </nested:notEmpty>

                        <nested:notEmpty property="requestLineItem.request.additionalInstructions">
                            <br><a href=""
                                   onclick="window.open('purchasing/additionalInstructions.jsp?instructions='+encodeURIComponent('<nested:write property="requestLineItem.request.additionalInstructions"/>'),'','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=no, HEIGHT=150, WIDTH=350, left=350, top=150');return false">
                                <%--<br/><a href="additionalInstructions.do?instructions=<nested:write property="requestLineItem.request.additionalInstructions"/>" target="new">--%>
                            Special Instructions
                        </a><br/>
                        </nested:notEmpty>
                    </td>
                    </tr>
                    <bean:write name="orderForm" property="flip"/>
                </nested:equal>
            </nested:iterate>
            </table>
            </nested:greaterThan>
            <div class="col-md-12 text-center">
                <nested:greaterThan property="reqLnItemFormCount" value="0">
                    <button type="submit" onclick="this.form.action='viewCreateOrderLineItem.do'" class="btn btn-default">Add to New Order Line Item</button>&nbsp;
                </nested:greaterThan>
                <nested:greaterThan property="reqLnItemFormCount" value="0">
                    <nested:greaterThan property="orderLnItemFormCount" value="0">
                        <button type="submit" onclick="this.form.action='viewAddToOrderLineItem.do'; this.form.cmd.value='addToOrderLineItem';" class="btn btn-default">Add to Selected Order Line Item</button>&nbsp;
                    </nested:greaterThan>
                </nested:greaterThan>
                &nbsp;
                <%--<button type="submit" onclick="this.form.action='saveOrder.do'" class="btn btn-default">Save Order</button>--%>
                <logic:notEmpty name="orderForm" property="orderId">
                    &nbsp;
                    <button onclick="window.open('printPurchaseOrder2.do?orderId=<nested:write property='orderId'/>&reportType=internalPO')" class="btn btn-default">View Internal PO Report</button>
                </logic:notEmpty>
                <%--<logic:notEmpty name="orderForm" property="purchaseOrderNumber">--%>
                    <%--&nbsp;--%>
                    <%--<button onclick="window.open('printPurchaseOrder.do?orderId=<nested:write property='orderId'/>&reportType=vendorPO')" class="btn btn-default">View Vendor PO</button>--%>
                <%--</logic:notEmpty>--%>
            </div>
        </html:form>
    </body>
</html>