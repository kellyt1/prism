<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>View Purchase Order</title>
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
    <html:form action="/saveOrder" method="post">
    <input type="HIDDEN" name="key" value=""/>
    <input type="HIDDEN" name="requestLineItemId" value=""/>
    <input type="HIDDEN" name="input" value="order"/>
    <nested:hidden property="noteFormKey"/>
    <h3>View Purchase Order</h3>
            <table class="table">
                <caption>Order Detail</caption>
            <tr>
            <td class="text-right">Vendor Info:</td>
            <td>
                <nested:present property="order">
                    <nested:present property="order.vendor">
                        <nested:present property="order.vendor.externalOrgDetail">
                            <nested:write property="order.vendor.externalOrgDetail.orgName"/><br/>
                        </nested:present>
                    </nested:present>
                </nested:present>
                <nested:present property="order">
                    <nested:present property="order.vendorContract">
                        <nested:write property="order.vendorContract.contractNumber"/><br/>
                    </nested:present>
                </nested:present>
                <nested:present property="order">
                    <nested:present property="order.vendorAddress">
                        <nested:write property="order.vendorAddress.address1"/>
                        <br/>
                        <nested:present property="order.vendorAddress.address2">
                            <nested:write property="order.vendorAddress.address2"/><br/>
                        </nested:present>
                        <nested:write property="order.vendorAddress.city"/>
                        ,&nbsp;
                        <nested:write property="order.vendorAddress.state"/>
                        &nbsp;
                        <nested:write property="order.vendorAddress.zip"/>
                    </nested:present>
                </nested:present>
            </td>
            <td colspan="2">
                <table>
                    <tr>
                        <td class="text-right">PO #:</td>
                        <td><nested:write property="order.purchaseOrderNumber"/></td>
                    </tr>
                    <tr>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="text-right">PO # Type:</td>
                        <td><nested:write property="order.purchaseOrderNumberType"/></td>
                    </tr>
                </table>
            </td>
            <td colspan="2">
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td class="text-right">OPR #: </td>
                        <td><strong><nested:write property="order.orderId"/></strong></td>
                    </tr>
                    <tr>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="text-right">Date Created: </td>
                        <td>
                            <nested:present property="order.insertionDate">
                                <nested:write property="order.insertionDate" format="MM/dd/yyyy"/>
                            </nested:present>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="text-right">Suspense Date: </td>
                        <td>
                            <nested:present property="order.suspenseDate">
                                <nested:write property="order.suspenseDate" format="MM/dd/yyyy"/>
                            </nested:present>
                        </td>
                    </tr>
                </table>
            </td>
            </tr>
            <tr>
                <td>Bill To:</td>
                <td>
                    <nested:present property="order.billToAddress">
                        <nested:present property="order.billToAddress.addressSummary">
                            <nested:notEmpty property="order.billToAddress.addressSummary.orgName">
                                <nested:write property="order.billToAddress.addressSummary.orgName"/><br/>
                            </nested:notEmpty>
                            <nested:write property="order.billToAddress.addressSummary.contactName"/><br/>
                            <nested:write property="order.billToAddress.addressSummary.address1"/><br/>
                            <nested:notEmpty property="order.billToAddress.addressSummary.address2">
                                <nested:write property="order.billToAddress.addressSummary.address2"/><br/>
                            </nested:notEmpty>
                            <nested:write property="order.billToAddress.addressSummary.city"/>
                            , &nbsp;
                            <nested:write property="order.billToAddress.addressSummary.state"/>
                            &nbsp;
                            <nested:write property="order.billToAddress.addressSummary.zip"/>
                            <br/>
                        </nested:present>
                    </nested:present>
                </td>
                <td>Other Bill To:</td>
                <td><nested:write property="order.otherBillToAddress"/></td>
                <td>Remit To:</td>
                <td><nested:write property="remitToAddress"/></td>
            </tr>
            <tr>
                <td>Ship To:</td>
                <td>
                    <nested:present property="order.shipToAddress">
                        <nested:present property="order.shipToAddress.addressSummary">
                            <nested:notEmpty property="order.shipToAddress.addressSummary.orgName">
                                <nested:write property="order.shipToAddress.addressSummary.orgName"/><br/>
                            </nested:notEmpty>
                            <nested:write property="order.shipToAddress.addressSummary.contactName"/><br/>
                            <nested:write property="order.shipToAddress.addressSummary.address1"/><br/>
                            <nested:notEmpty property="order.shipToAddress.addressSummary.address2">
                                <nested:write property="order.shipToAddress.addressSummary.address2"/><br/>
                            </nested:notEmpty>
                            <nested:write property="order.shipToAddress.addressSummary.city"/>
                            , &nbsp;
                            <nested:write property="order.shipToAddress.addressSummary.state"/>
                            &nbsp;
                            <nested:write property="order.shipToAddress.addressSummary.zip"/><br/>
                        </nested:present>
                    </nested:present>
                </td>
                <td>Other Ship To:</td>
                <td><nested:write property="otherShipToAddress"/></td>
                <td>Vendor<br>Instructions:</td>
                <td colspan="3"><nested:write property="vendorInstructions"/></td>
            </tr>
        </table>
                <br/>
        <table class="table table-bordered table-striped">
            <caption>Purchase Order Notes</caption>
            <tr>
                <th scope="col" colspan="2">Note</th>
                <th scope="col">Author</th>
                <th scope="col">Date</th>
            </tr>
            <nested:iterate id="ordNotes" property="order.orderNotes">
                <tr>
                    <td colspan="2"><nested:write property="note" name="ordNotes"/></td>
                    <td><nested:write property="authorName" name="ordNotes"/></td>
                    <td><nested:write property="insertionDate" name="ordNotes" format="MM/dd/yyyy"/></td>
                </tr>
            </nested:iterate>
        </table>
        <br/>
            <nested:notEmpty property="order.orderLineItems">
                <table class="table table-bordered">
                    <caption>Order Line Items</caption>
                    <nested:greaterThan property="orderLnItemFormCount" value="0">
                        <tfoot>
                        <tr>
                            <th scope="row" colspan="9" class="text-right"><strong>Total Cost:</strong></th>
                            <td align="left">$ <nested:write property="order.buyUnitsTotalCost" format="#,##0.00"/></td>
                        </tr>
                        </tfoot>
                    </nested:greaterThan>
                    <nested:iterate id="oli" property="order.orderLineItems">
                        <tr>
                            <td class="text-right">Line Item ID #:</td>
                            <td>
                                <nested:notEmpty property="orderLineItemId" name="oli">
                                    <strong>OLI
                                        <nested:write property="orderLineItemId" name="oli"/>
                                    </strong>
                                </nested:notEmpty>
                                <nested:empty property="orderLineItemId" name="oli">
                                    Unassigned
                                </nested:empty>
                            </td>
                            <td class="text-right">Item<br>Description:</td>
                            <td colspan="3">
                                <nested:write property="itemDescription" name="oli"/>
                            </td>

                            <td class="text-right">Vendor Catalog #:</td>
                            <td>
                                <nested:notEmpty property="vendorCatalogNbr" name="oli">
                                    <nested:write property="vendorCatalogNbr" name="oli"/>
                                </nested:notEmpty>
                            </td>

                            <td class="text-right">Status:</td>
                            <td>
                                <nested:notEmpty property="status" name="oli">
                                    <nested:write property="status.name" name="oli"/>
                                </nested:notEmpty>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right">Quantity:</td>
                            <td>
                                <nested:write property="quantity" name="oli"/>
                            </td>
                            <td class="text-right">Buy Unit:</td>
                            <td>
                                <nested:notEmpty property="buyUnit" name="oli">
                                    <nested:write property="buyUnit.name" name="oli"/>
                                </nested:notEmpty>
                                <nested:empty property="buyUnit" name="oli">
                                    Unspecified
                                </nested:empty>
                            </td>
                            <td class="text-right">Buy Unit Cost:</td>
                            <td nowrap="nowrap">
                                <nested:notEmpty property="buyUnitCost" name="oli">
                                    $
                                    <nested:write property="buyUnitCost" name="oli" format="#,##0.00"/>
                                </nested:notEmpty>
                                <nested:empty property="buyUnitCost" name="oli">
                                    Unspecified
                                </nested:empty>
                            </td>
                            <td class="text-right">Discount:</td>
                            <td>
                                <nested:notEmpty property="discountPercent" name="oli">
                                    <nested:write property="discountPercent" name="oli"/>
                                    %
                                </nested:notEmpty>
                                <nested:empty property="discountPercent" name="oli">
                                    Unspecified
                                </nested:empty>
                            </td>
                            <td class="text-right">Line Cost:</td>
                            <td>
                                <nested:notEmpty property="buyUnitTotalCost" name="oli">
                                    $
                                    <nested:write property="buyUnitTotalCost" name="oli" format="#,##0.00"/>
                                </nested:notEmpty>
                                <nested:empty property="buyUnitTotalCost" name="oli">
                                    N/A
                                </nested:empty>
                            </td>
                        </tr>
                    </nested:iterate>
                </table>
            </nested:notEmpty>
            <nested:empty property="order.orderLineItems">
                <p>No Order Line Items have been created</p>
            </nested:empty>
    <%--<br/>--%>
    <%--Report is broken. Commenting out button per JP--%>
    <%--<tr>
        <td align="center">

            <nested:present property="orderId">
                <input type="button" value="View Accounts Payable PO Report"
                       onclick="window.open('printPurchaseOrder.do?orderId=<nested:write property='orderId'/>&reportType=accountsPayablePO')"/>
            </nested:present>
        </td>
    </tr>--%>

    </html:form>
    </body>
</html>