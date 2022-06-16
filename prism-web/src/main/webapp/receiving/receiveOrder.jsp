<%@ include file="../include/tlds.jsp" %>

<%@ page import="us.mn.state.health.model.inventory.Item" %>
<%@ page import="us.mn.state.health.view.inventory.Command" %>

<%
    String addNoteOnClick = "this.form.cmd.value='" + Command.ADD_NOTE + "'; this.form.submit(); return false ";
%>

<html>
    <head>
        <title>Receive Order</title>
        <script type="text/javascript">
            function popUp(URL) {
                var day = new Date();
                var id = day.getTime();
                eval("page" + id + " = window.open(URL, '" + id + "', 'toolbar=0,scrollbars=1,location=0,statusbar=0,menubar=0,resizable=1,width=1200,height=700,left = 50,top = 50');");
            }

            function updateDispenseUnitsReceived(index, multiplier) {
                orderLineItemForms[index].quantityReceivedInDispenseUnits.value = orderLineItemForms[index].quantityReceived * multiplier;
            }

            function keyCheck(eventObj, obj){
                var keyCode;

                // Check For Browser Type
                if (document.all){
                    keyCode=eventObj.keyCode
                }
                else{
                    keyCode=eventObj.which
                }

                //check for keystrokes like TAB, <shift>TAB, RETURN, Backspace etc
                if (keyCode == 0 || keyCode == 13 || keyCode == 8) {
                    return true
                }

                var str = obj.value;

                if(keyCode==45){
                    if (str.indexOf("-")>0){
                        return false
                    }
                }

                if(keyCode==46){
                    if (str.indexOf(".")>0){
                        return false
                    }
                }

                if((keyCode<48 || keyCode >57) && (keyCode != 45) && (keyCode != 46) ){ // Allow only numbers and decimal points
                    alert("Only numbers are allowed");
                    return false
                }

                return true
            }
        </script>
    </head>

    <body>
        <nested:form action="receiveOrder" method="post">
            <input type="HIDDEN" name="cmd"/>
            <input type="HIDDEN" name="oliFormIndex"/>
            <table class="table table-bordered table-striped">
                <caption>Receive Order</caption>
                <thead>
                    <tr>
                        <th scope="col">OPR #</th>
                        <th scope="col">PO #</th>
                        <th scope="col">Buyer</th>
                        <th scope="col">Date Created</th>
                        <th scope="col">Vendor</th>
                        <th scope="col">Receiving Facility</th>
                    </tr>
                </thead>
                <tr>
                    <td>
                        <nested:write property="order.orderId"/>
                    </td>
                    <td>
                        <nested:write property="order.purchaseOrderNumber"/>
                    </td>
                    <td>
                        <nested:present property="order.purchaser">
                            <nested:write property="order.purchaser.firstAndLastName"/>
                        </nested:present>
                    </td>
                    <td>
                        <nested:write property="order.insertionDate" format="MM/dd/yyyy"/>
                    </td>
                    <td>
                        <nested:present property="order.vendor">
                            <nested:present property="order.vendor.externalOrgDetail">
                                <nested:write property="order.vendor.externalOrgDetail.orgName"/>
                            </nested:present>
                        </nested:present>
                    </td>
                    <td>
                        <label class="hidden" for="receivingFacility">Receiving Facility</label>
                        <nested:select property="receivingFacilityId" styleClass="chosen-select" styleId="receivingFacility">
                            <option value=""></option>
                            <nested:optionsCollection property="facilities" value="facilityId" label="facilityName"/>
                        </nested:select>
                    </td>
                </tr>
            </table>
            <br/>
            <p>
                <strong>Destination Facility: </strong>
                <nested:present property="order.shipToAddress">
                    <nested:write property="order.shipToAddress.shortSummaryInOneLine"/>
                </nested:present>
                <nested:notPresent property="order.shipToAddress">
                    <nested:write property="order.otherShipToAddress"/>
                </nested:notPresent>
                <nested:present property="order.vendorInstructions">
                    <br/><strong>Vendor Instructions: </strong>
                    <nested:write property="order.vendorInstructions"/>
                </nested:present>
            </p>
            <nested:equal property="showNotes" value="false">
                <a href="receiveOrder.do?cmd=<%=Command.SHOW_NOTES%>"> &dArr; Show Notes &dArr;</a><br/>
            </nested:equal>
            <nested:equal property="showNotes" value="true">
                <a href="receiveOrder.do?cmd=<%=Command.HIDE_NOTES%>"> &uArr; Hide Notes &uArr; </a>
                <br/>
                <table class="table table-bordered table-striped">
                    <caption>Notes</caption>
                        <thead>
                            <tr>
                                <th scope="col">Note</th>
                                <th scope="col">Author</th>
                                <th scope="col">Date</th>
                            </tr>
                        </thead>
                    <tfoot>
                        <tr>
                            <td>
                                <label class="hidden" for="newNote">Add Note</label>
                                <textarea name="note" cols="30" rows="2" id="newNote"></textarea>
                            </td>
                            <td style="vertical-align: middle;" colspan="2">
                                <html:submit value="Add Note" onclick="<%=addNoteOnClick%>" styleClass="btn btn-default"/>
                            </td>
                        </tr>
                    </tfoot>
                    <nested:iterate property="orderNoteForms" id="orderNoteForm">
                        <tr>
                            <td>
                                <nested:write property="note"/>
                            </td>
                            <td nowrap="nowrap">
                                <nested:write property="authorName"/>
                            </td>
                            <td nowrap="nowrap">
                                <nested:write property="insertionDate" format="MM/dd/yyyy"/>
                            </td>
                        </tr>
                    </nested:iterate>
                </table>
            </nested:equal>
            <br/>
            <table class="table table-bordered table-striped">
                <tr>
                    <th scope="col">Line Item Nbr</th>
                    <th scope="col">Item Description</th>
                    <th scope="col">Vendor Nbr</th>
                    <th scope="col">Buy Units Ordered</th>
                    <th scope="col">Buy Unit</th>
                    <th scope="col">Dispense Unit</th>
                    <th scope="col">Dispense Units Per Buy Unit</th>
                    <th scope="col">Buy Units Already Rec'd</th>
                    <th scope="col">Buy Units Rec'd Now</th>
                    <th scope="col">Dispense Units Rec'd Now</th>
                    <th scope="col">Asset Number</th>
                    <th scope="col">Actions</th>
                    <th scope="col">Asset Type</th>
                </tr>
                <nested:notEmpty property="orderLineItemForms">
                    <nested:iterate property="orderLineItemForms" id="orderLineItemForm" indexId="a">
                        <tr>
                            <td class="text-center">
                                <nested:write property="lineItemNumber"/>
                            </td>
                            <td>
                                <nested:present property="item">
                                    <nested:equal property="item.itemType" value="Stock Item">
                                        <nested:write property="item.fullIcnbr"/>
                                        <br/>
                                    </nested:equal>
                                     <nested:equal property="item.itemType" value="Purchase Item">
                                        <nested:write property="item.descriptionForUser"/>
                                        <br/>
                                    </nested:equal>
                                    <nested:write property="item.description"/>

                                </nested:present>
                                <nested:notPresent property="item">
                                    <nested:write property="orderLineItem.itemDescription"/>
                                </nested:notPresent>
                            </td>
                            <td>
                                <nested:write property="orderLineItem.vendorCatalogNbr"/>
                            </td>
                            <td class="text-center">
                                <nested:write property="orderLineItem.quantity"/>
                            </td>
                            <td class="text-center">
                                <nested:present property="orderLineItem.buyUnit">
                                    <nested:write property="orderLineItem.buyUnit.name"/>
                                </nested:present>
                            </td>
                            <td class="text-center">
                                <nested:present property="orderLineItem.item">
                                    <nested:present property="orderLineItem.item.dispenseUnit">
                                        <nested:write property="orderLineItem.item.dispenseUnit.name"/>
                                    </nested:present>
                                </nested:present>
                                <nested:notPresent property="orderLineItem.item">
                                    N/A
                                </nested:notPresent>
                            </td>
                            <td class="text-center">
                                <nested:write property="dispenseUnitsPerBuyUnit"/>
                            </td>
                            <td class="text-center">
                                <a title="View Receiving History" href="javascript:popUp('/viewReceivingHistory.do?oliId=<nested:write property='orderLineItem.orderLineItemId' />')">
                                    <nested:write property="orderLineItem.totalQtyReceived"/>
                                </a>
                            </td>
                            <td class="text-center">
                                <label class="hidden" for="buRecdNow${a}">Buy Units Rec'd Now</label>
                                <nested:text property="quantityReceived" size="10" onkeypress="return keyCheck(event, this)" styleId="buRecdNow${a}"/>
                            </td>
                            <td class="text-center">
                                <nested:present property="orderLineItem.item">
                                    <nested:equal property="orderLineItem.item.itemType" value="<%=Item.STOCK_ITEM%>">
                                        <nested:equal property="unitMismatch" value="true">
                                            <label class="hidden" for="duRecdNow${a}">Dispense Units Rec'd Now</label>
                                            <nested:text property="quantityReceivedInDispenseUnits" size="10" onkeypress="return keyCheck(event, this)" styleId="duRecdNow${a}"/>
                                        </nested:equal>
                                        <nested:equal property="unitMismatch" value="false">
                                            N/A
                                        </nested:equal>
                                    </nested:equal>
                                    <nested:notEqual property="orderLineItem.item.itemType" value="<%=Item.STOCK_ITEM%>">
                                        N/A
                                    </nested:notEqual>
                                </nested:present>
                                <nested:notPresent property="orderLineItem.item">
                                    N/A
                                </nested:notPresent>
                            </td>
                            <td class="text-center">
                                <label class="hidden" for="assetNumber${a}">Asset Number</label>
                                <nested:text property="assetNumber" size="10" styleId="assetNumber${a}"/>
                            </td>
                            <td class="text-center" style="vertical-align: middle;">
                                <button type="button" onclick="popUp('/viewCreateDeliveryTicket.do?oliId=<nested:write property='orderLineItem.orderLineItemId' />')" class="btn btn-default">Create Delivery Ticket</button>
                            </td>
                            <td class="text-center">
                                <nested:notEmpty property="assetTypeEnum">
                                    <nested:write property="assetTypeEnum.label"/>
                                </nested:notEmpty>
                            </td>
                        </tr>
                    </nested:iterate>
                </nested:notEmpty>
            </table>
            <br/>
            <div align="center">
                <button type="submit" onclick="this.form.cmd.value='receiveOrder';" class="btn btn-default">Submit</button>
            </div>
        </nested:form>
    </body>
</html>
