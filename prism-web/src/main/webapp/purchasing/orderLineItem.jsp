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
<div class="container-fluid">
    <html:form action="/saveOrderLineItem.do" method="post">
            <nested:hidden property="key" />
            <nested:hidden property="totalCost"/>
            <input type="HIDDEN" name="requestLineItemId" value=""/>
            <input type="HIDDEN" name="input" value="orderLineItem"/>
            <h2>
                <caption>
                    <nested:empty property="orderLineItemId">Create Order Line Item</nested:empty>
                    <nested:notEmpty property="orderLineItemId">Edit Order Line Item</nested:notEmpty>
                </caption>
            </h2>
        <div class="panel panel-body">
            <div class="row form-inline">
                <div class="form-group col-md-2">
                    <label>Line Item #:</label>
                    <div class="form-control-static">
                        <nested:notEmpty property="orderLineItemId">
                            OLI<nested:write property="orderLineItemId"/>
                        </nested:notEmpty>
                        <nested:empty property="orderLineItemId">
                            Unassigned
                        </nested:empty>
                    </div>
                </div>
                <div class="form-group col-md-5">
                    <label>Item Description:</label>
                    <nested:textarea property="itemDescription" cols="40" rows="3" styleClass="form-control"/>
                </div>
                <div class="form-group col-md-3">
                    <label>Status:</label>
                    <nested:select property="statusCode" styleClass="chosen-select form-control">
                        <nested:optionsCollection property="statuses" label="name" value="statusCode"/>
                    </nested:select>
                </div>
                <div class="form-group col-md-2">
                    <label>Commodity Code: </label>
                    <nested:text property="commodityCode" size="10" styleClass="form-control"/>
                </div>
            </div>
            <hr>
            <div class="row form-inline">
                <div class="form-group col-md-2">
                    <label>Quantity:</label>
                    <nested:text property="quantity" size="5" onkeyup="updateTotalCost();" onblur="updateTotalCost();"
                                 styleClass="form-control"/>
                </div>
                <div class="col-md-4">
                    <div class="row">
                        <div class="form-group col-md-5">
                            <label>Buy Unit: </label>
                            <nested:select property="buyUnitId" styleClass="chosen-select form-control">
                                <option value="">Select Buy Unit</option>
                                <nested:optionsCollection property="units" label="name" value="unitId"/>
                            </nested:select>
                        </div>
                            <nested:present property="item">
                                <div class="col-md-4">
                                    <a href="" onclick="window.open('/selectVendorPurchasingInfo.do', '','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, HEIGHT=300, WIDTH=600, left=50, top=50'); return false;">SelectVendor Purchasing Info</a>
                                </div>
                                <div class="col-md-3">
                                    <a href="" onclick="window.open('/viewEditItemVendorInfo.do?itemId=<nested:write property="item.itemId"/>', '','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, HEIGHT=300, WIDTH=600, left=50, top=50'); return false;">Edit Vendor Purchasing Info</a>
                                </div>
                            </nested:present>
                    </div>
                    <nested:notEmpty property="item">
                        <div class="row">
                            <div class="form-group col-md-12">
                                <label>Item Dispense Unit:</label>
                                <div class="form-control-static">
                                    <nested:write property="item.dispenseUnit.name"/>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group col-md-12">
                                <label>Dispense Units/Buy Unit:</label>
                                <nested:text styleClass="form-control" property="dispenseUnitsPerBuyUnit" size="10"
                                             value="1" onkeyup="updateTotalCost();" onblur="updateTotalCost();"/>
                            </div>
                        </div>
                    </nested:notEmpty>
                </div>
                <div class="col-md-2 form-group">
                    <label>Buy Unit Cost:</label>
                    $<nested:text property="buyUnitCost" size="15" onblur="updateTotalCost();"
                                  onkeyup="updateTotalCost();" styleClass="blurOnLoad form-control"/>
                </div>
                <div class="col-md-2 form-group">
                    <label>Discount %:</label>
                    <nested:text styleClass="form-control" property="discountPercent" size="3"
                                 onkeyup="updateTotalCost();" onblur="updateTotalCost();"/>
                </div>
                <div class="col-md-2 form-group">
                    <label>Total Cost:</label>
                    $<span id="totalCost" class="form-control-static"></span>
                </div>
            </div>
            <hr>
            <div class="row form-inline">
                <div class="col-md-6 form-group">
                    <label>Vendor Catalog #:</label>
                    <nested:text styleClass="form-control" property="vendorCatalogNbr" size="15"/>
                </div>
                <div class="col-md-6 form-group">
                    <div class="row">
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
                    </div>
                    <div class="row">
                        <logic:equal value="SENSITIVE" name="orderLineItemForm" property="assetsType">
                            Obsolete Asset Type is currently applied as <strong>SENSITIVE</strong>
                        </logic:equal>
                        <logic:equal value="FIXED" name="orderLineItemForm" property="assetsType">
                            Obsolete Asset Type is currently applied as <strong>FIXED</strong>
                        </logic:equal>
                    </div>
                </div>
            </div>
            <hr>
            <div class="row">
                <div class="col-md-offset-5 col-md-2 text-center">
                    <button type="submit" class="btn btn-default">Submit</button>
                    <nested:notPresent property="item">
                        <button type="submit" onclick="this.form.action='viewAdvancedSearchItemsPurchasing.do'" class="btn btn-default">Convert to Catalog Item</button>
                    </nested:notPresent>
                </div>
            </div>
        </div>
            </div>
            <br/>
            <table class="table table-bordered table-striped">
                <caption>Request Line Items</caption>
                <thead>
                <tr>
                    <th style="white-space: nowrap;">Request<br />Line #</th>
                    <th style="white-space: nowrap;">Description</th>
                    <th style="white-space: nowrap;"></th>
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
                <nested:iterate property="requestLineItemForms" >
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
                            <td>
                                <nested:define property="requestLineItem" id="rliForm0"/>
                                <c:choose>
                                    <c:when test="${rliForm0.suggestedVendorCatalogNumber == 'STAFFAUG' || rliForm0.suggestedVendorCatalogNumber == 'MNITCONTRACT' || rliForm0.suggestedVendorCatalogNumber == 'WAN/Computing Services'}">
                                        AC2 Code: <nested:write property="requestLineItem.swiftItemId"/>
                                    </c:when>
                                    <c:otherwise>
                                        Vendor Catalog#:<nested:write property="requestLineItem.suggestedVendorCatalogNumber"/>
                                    </c:otherwise>
                                </c:choose>

                            </td>
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
                                <button type="submit" onclick="this.form.action='viewEditPurchasingRequestFromOrderLineItem.do';this.form.requestLineItemId.value='<nested:write property='requestLineItem.requestLineItemId'/>'" class="btn btn-default">Edit</button>
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
    </div>
    </body>
</html>