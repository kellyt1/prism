<%@ include file="../include/tlds.jsp" %>

<html>
<head>
    <title>Purchase Order Line Item</title>
    <script type="text/javascript">
        function updateTotalCost() {
            var qty = document.getElementsByName("quantity")[0].value;
            var buyUnitCost = document.getElementsByName("buyUnitCost")[0].value;
            var discountPercent = 1 - (document.getElementsByName("discountPercent")[0].value / 100);
            document.getElementById("totalCost").innerHTML = (buyUnitCost * qty * discountPercent).toFixed(2);
        }
    </script>
</head>
<body>
<html:form action="/saveOrderLineItem.do" method="post">
            <nested:hidden property="key" />
            <nested:hidden property="totalCost"/>
            <input type="HIDDEN" name="requestLineItemId" value=""/>
            <input type="HIDDEN" name="input" value="orderLineItem"/>
            <table class="table">
                <caption>
                    <nested:empty property="orderLineItemId">Create Order Line Item</nested:empty>
                    <nested:notEmpty property="orderLineItemId">Edit Order Line Item</nested:notEmpty>
                </caption>
                <tfoot>
                    <tr>
                        <td colspan="11" class="text-center">
                            <button type="submit" class="btn btn-default">Submit</button>&nbsp;
                            <nested:notPresent property="item">
                                <button type="submit" onclick="this.form.action='viewAdvancedSearchItemsPurchasing.do'" class="btn btn-default">Convert to Catalog Item</button>
                            </nested:notPresent>
                        </td>
                    </tr>
                </tfoot>
                <tr>
                    <td class="text-right" style="white-space: nowrap;"><label>Line Item #:</label></td>
                    <td>
                        <nested:notEmpty property="orderLineItemId">
                            <label>OLI<nested:write property="orderLineItemId"/></label>
                        </nested:notEmpty>
                        <nested:empty property="orderLineItemId">
                            <label>Unassigned</label>
                        </nested:empty>
                    </td>
                    <td class="text-right"><label>Item Description:</label></td>
                    <td colspan="5"><nested:textarea property="itemDescription" cols="40" rows="3"/></td>
                    <td class="text-right"><label>Status:</label></td>
                    <td>
                        <nested:select property="statusCode" styleClass="chosen-select">
                            <nested:optionsCollection property="statuses" label="name" value="statusCode"/>
                        </nested:select>
                    </td>
                    <td class="text-right"><label>Commodity Code:</label></td>
                    <td><nested:text property="commodityCode" size="10"/></td>
                </tr>
                <tr>
                    <td class="text-right"><label>Quantity:</label></td>
                    <td><nested:text property="quantity" size="5" onkeyup="updateTotalCost();" onblur="updateTotalCost();"/></td>
                    <td colspan="4">
                        <table width="100%">
                            <tr>
                                <td class="text-right"><label>Buy Unit: </label></td>
                                <td>
                                    <nested:select property="buyUnitId" styleClass="chosen-select">
                                        <option value="">Select Buy Unit</option>
                                        <nested:optionsCollection property="units" label="name" value="unitId"/>
                                    </nested:select>
                                </td>
                                <nested:present property="item">
                                    <td>
                                        <a href="" onclick="window.open('/selectVendorPurchasingInfo.do', '','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, HEIGHT=300, WIDTH=600, left=50, top=50'); return false;">Select Vendor Purchasing Info</a>
                                    </td>
                                    <td>
                                        <a href="" onclick="window.open('/viewEditItemVendorInfo.do?itemId=<nested:write property="item.itemId"/>', '','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, HEIGHT=300, WIDTH=600, left=50, top=50'); return false;">Edit Vendor Purchasing Info</a>
                                    </td>
                                </nested:present>
                            </tr>
                            <nested:notEmpty property="item">
                                <tr>
                                    <td class="text-right" style="white-space: nowrap;"><label>Item Dispense Unit:</label></td>
                                    <td><nested:write property="item.dispenseUnit.name"/></td>
                                    <nested:present property="item">
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                    </nested:present>
                                </tr>
                                <tr>
                                    <td class="text-right" style="white-space: nowrap;"><label>Dispense Units/Buy Unit:</label></td>
                                    <td><nested:text property="dispenseUnitsPerBuyUnit" size="10" value="1" onkeyup="updateTotalCost();" onblur="updateTotalCost();"/></td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                            </nested:notEmpty>
                        </table>
                    </td>
                    <td class="text-right"><label>Buy Unit Cost:</label></td>
                    <td style="white-space: nowrap;">$<nested:text property="buyUnitCost" size="15" onblur="updateTotalCost();" onkeyup="updateTotalCost();" styleClass="blurOnLoad"/></td>
                    <td class="text-right" style="white-space: nowrap;"><label>Discount %:</label></td>
                    <td><nested:text property="discountPercent" size="3" onkeyup="updateTotalCost();" onblur="updateTotalCost();"/></td>
                    <td class="text-right"><label>Total Cost:</label></td>
                    <td style="white-space: nowrap;">$<span id="totalCost"></span></td>
                </tr>
                <tr>
                    <td class="text-right"><label>Vendor Catalog #:</label></td>
                    <td colspan="2"><nested:text property="vendorCatalogNbr" size="15" /></td>
                    <td class="text-right"><label><label>Asset Type: </label></label></td>
                    <td colspan="2">
                        <label>Asset Type (required):</label>
                        <nested:select property="assetsType" styleClass="chosen-select form-control">
                            <html:option value=""/>
                            <nested:optionsCollection name="orderLineItemForm" property="assetTypes" label="label"
                                                      value="name"/>
                        </nested:select>
                        <a href=""
                           onclick="window.open('/purchasing/assetTypes.jsp', '','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, HEIGHT=600, WIDTH=1200, left=50, top=50'); return false;">
                            <span style="color: blue; ">View Details</span>
                        </a>
                        <br/>
                        <logic:equal value="SENSITIVE" name="orderLineItemForm" property="assetsType">
                            Obsolete Asset Type is currently applied as <strong>SENSITIVE</strong>
                        </logic:equal>
                        <logic:equal value="FIXED" name="orderLineItemForm" property="assetsType">
                            Obsolete Asset Type is currently applied as <strong>FIXED</strong>
                        </logic:equal>
                    </td>
                    <td colspan="7"></td>
                </tr>
            </table>
            <br/>
            <table class="table table-bordered table-striped">
                <caption>Request Line Items</caption>
                <thead>
                    <tr>
                        <th style="white-space: nowrap;">Request<br />Line #</th>
                        <th style="white-space: nowrap;">Description</th>
                        <th style="white-space: nowrap;">Vendor<br />Catalog #</th>
                        <th style="white-space: nowrap;">Quantity</th>
                        <th style="white-space: nowrap;">Unit</th>
                        <th style="white-space: nowrap;">Cost/Unit</th>
                        <th style="white-space: nowrap;">Code(s)</th>
                        <th style="white-space: nowrap;">Status</th>
                        <th style="white-space: nowrap;">Deliver To</th>
                        <th style="white-space: nowrap;"></th>
                    </tr>
                </thead>
                <tbody>
                    <nested:iterate property="requestLineItemForms">
                        <nested:equal property="removedFromOrderLineItem" value="false">
                            <tr>
                                <td>
                                    <nested:write property="requestLineItem.request.trackingNumber"/><br>
                                    (<nested:write property="requestLineItem.requestLineItemId"/>)
                                </td>
                                <td>
                                    <nested:equal value="true" property="requestLineItem.itemIsStockItem">
                                        <nested:write property="requestLineItem.fullIcnbr"/><br/>
                                    </nested:equal>
                                    <nested:write property="descriptionSummary"/>
                                </td>
                                <td><nested:write property="requestLineItem.suggestedVendorCatalogNumber"/></td>
                                <td class="text-right"><nested:write property="requestLineItem.quantity"/></td>
                                <td>
                                    <nested:notPresent property="requestLineItem.item">
                                        <nested:present property="requestLineItem.unit">
                                            <nested:write property="requestLineItem.unit.name" />
                                        </nested:present>
                                    </nested:notPresent>
                                    <nested:present property="requestLineItem.item">
                                        <nested:present property="requestLineItem.item.dispenseUnit">
                                            <nested:write property="requestLineItem.item.dispenseUnit.name"/>
                                        </nested:present>
                                    </nested:present>
                                </td>
                                <td class="text-right">$<nested:write property="requestLineItem.itemCost" format="#,##0.00"/></td>
                                <td><nested:write property="requestLineItem.fundingSrcSummary.orgBudgetCodes"/></td>
                                <td><nested:write property="requestLineItem.status.name"/></td>
                                <td style="white-space: nowrap;">
                                    <nested:present property="requestLineItem.request.deliveryDetail">
                                        <nested:present property="requestLineItem.request.deliveryDetail.organization">
                                            <nested:write property="requestLineItem.request.deliveryDetail.organization.orgName" />
                                        </nested:present>
                                        <nested:notPresent property="requestLineItem.request.deliveryDetail.organization">
                                            <nested:present property="requestLineItem.request.deliveryDetail.facility">
                                                MDH
                                            </nested:present>
                                        </nested:notPresent>
                                        <br/><nested:write property="requestLineItem.request.deliveryDetail.recipientName" />
                                        <nested:present property="requestLineItem.request.deliveryDetail.facility">
                                            <br/><nested:write property="requestLineItem.request.deliveryDetail.facility.facilityName"/>
                                        </nested:present>
                                        <nested:present property="requestLineItem.request.deliveryDetail.mailingAddress">
                                            <br/><nested:write property="requestLineItem.request.deliveryDetail.mailingAddress.address1"/><br/>
                                            <nested:present property="requestLineItem.request.deliveryDetail.mailingAddress.address2">
                                                <nested:write property="requestLineItem.request.deliveryDetail.mailingAddress.address2"/><br/>
                                            </nested:present>
                                            <nested:write property="requestLineItem.request.deliveryDetail.mailingAddress.city"/>,
                                            <nested:write property="requestLineItem.request.deliveryDetail.mailingAddress.state"/>&nbsp;
                                            <nested:write property="requestLineItem.request.deliveryDetail.mailingAddress.zip"/>
                                        </nested:present>
                                    </nested:present>
                                </td>
                                <td class="text-center">
                                    <button type="submit" onclick="this.form.action='viewEditPurchasingRequestFromOrderLineItem2.do';this.form.requestLineItemId.value='<nested:write property='requestLineItem.requestLineItemId'/>'" class="btn btn-default">Edit</button>
                                    <nested:notEqual property="requestLineItem.status.statusCode" value="RCD">
                                        <nested:notEqual property="requestLineItem.status.statusCode" value="RCP">
                                            <br/><br/><button type="submit" onclick="this.form.action='removeRequestLineItemFromOrderLineItem.do';this.form.requestLineItemId.value='<nested:write property='requestLineItem.requestLineItemId'/>'" class="btn btn-default">Remove</button>
                                            <nested:notEmpty property="requestLineItem.request.additionalInstructions">
                                                 <br/><br/><button onclick="window.open('purchasing/additionalInstructions.jsp?instructions='+encodeURIComponent('<nested:write property="requestLineItem.request.additionalInstructions"/>'),'','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=no, HEIGHT=150, WIDTH=350, left=350, top=150');return false" class="btn btn-default">Special Instructions</button>
                                            </nested:notEmpty>
                                        </nested:notEqual>
                                    </nested:notEqual>
                                </td>
                            </tr>
                            <bean:write name="orderLineItemForm" property="flip"/>
                        </nested:equal>
                     </nested:iterate>
                </tbody>
            </table>
        </html:form>
    </body>
</html>