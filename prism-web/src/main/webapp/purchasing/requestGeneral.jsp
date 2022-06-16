<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>Purchase Requests</title>
        <script type="text/javascript">
            function removeRequestLineItem(form) {
                var reqLnItmKey = form.requestLineItemKey.value;
                if (reqLnItmKey == null || reqLnItmKey == "") {
                    alert("Please Select a Request Line Item");
                    return false;
                }
                var remove = confirm("Remove Selected Request Line Item?");
                if (remove) {
                    form.action = "removePurchasingRequestLineItem.do";
                    return true;
                }
                else {
                    return false;
                }
            }
    
            function removeRequestLineItemFundingSource(form) {
                var fundingSrcKey;
                for (var i = 0; i < form.elements.length; i++) {
                    if (form.elements[i].name == "requestLineItemForm.fundingSourceKey") {
                        fundingSrcKey = form.elements[i].value;
                        break;
                    }
                }
                if (fundingSrcKey == null || fundingSrcKey == "") {
                    alert("Please Select a Request Line Item Funding Source");
                    return false;
                }
                var remove = confirm("Remove Selected Request Line Item Funding Source?");
                if (remove) {
                    form.action = "removePurchasingRequestLineItemFundingSource.do";
                    return true;
                }
                else {
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <html:form action="/savePurchasingRequest" method="post">
            <input type="HIDDEN" name="fundingSourceKey"/>
            <nested:hidden property="input"/>
            <h1 class="text-center">
                <nested:empty property="requestId">Enter New Request</nested:empty>
                <nested:notEmpty property="requestId">Edit Request</nested:notEmpty>
            </h1>
            <div class="row">
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Deliver To</div>
                        <div class="panel-body">
                            <nested:present property="deliveryDetail">
                                <div class="row" onload="console.print('Please double check Deliver To address.')">
                                    <div class="col-xs-6">Organization:</div>
                                    <div class="col-xs-6">
                                        <nested:present property="deliveryDetail.organization">
                                            <nested:write property="deliveryDetail.organization.orgName" />
                                        </nested:present>
                                        <nested:notPresent property="deliveryDetail.organization">
                                            <nested:present property="deliveryDetail.facility">MDH</nested:present>
                                        </nested:notPresent>
                                    </div>
                                </div>
                                <br/>
                                <div class="row">
                                    <div class="col-xs-6">Recipient:</div>
                                    <div class="col-xs-6"><nested:write property="deliveryDetail.recipientName" /></div>
                                </div>
                                <br/>
                                <div class="row">
                                    <div class="col-xs-6">Mailing Address:</div>
                                    <div class="col-xs-6">
                                        <nested:present property="deliveryDetail.facility">
                                            <nested:write property="deliveryDetail.facility.facilityName" /><br />
                                        </nested:present>
                                        <nested:present property="deliveryDetail.mailingAddress">
                                            <nested:write property="deliveryDetail.mailingAddress.address1" /><br />
                                            <nested:present property="deliveryDetail.mailingAddress.address2">
                                                <nested:write property="deliveryDetail.mailingAddress.address2" /><br />
                                            </nested:present>
                                            <nested:write property="deliveryDetail.mailingAddress.city" />,&nbsp;
                                            <nested:write property="deliveryDetail.mailingAddress.state" />&nbsp;
                                            <nested:write property="deliveryDetail.mailingAddress.zip" />
                                        </nested:present>
                                    </div>
                                </div>
                                <br/>
                                <div class="row">
                                    <div class="col-sm-12 text-center">
                                        <html:submit value="Change Delivery Info" onclick="this.form.cmd.value='changeDeliveryInfo'; this.form.submit(); return false;" styleClass="btn btn-default"/>
                                    </div>
                                </div>
                            </nested:present>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Additional Information</div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-xs-6"><label for="dateNeeded">Need-By Date (MM/dd/yyyy):</label></div>
                                <div class="col-xs-6"><nested:text property="needByDate" size="12" styleClass="dateInput form-control" styleId="dateNeeded"/></div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-6"><label for="priority">Priority:</label></div>
                                <div class="col-xs-6">
                                    <nested:select property="priorityCode" onchange="hideshow()" styleId="priority" styleClass="chosen-select" style="width: 100px;">
                                        <nested:optionsCollection property="priorities" label="name" value="priorityCode"/>
                                    </nested:select>&nbsp;&nbsp;
                                    <a href="#" onclick="window.open('../requests/help/priorityDefinitions.jsp', 'priorityDef', 'width=500,height=300,status=no,resizable=no,top=200,left=200,dependent=yes,alwaysRaised=yes')"> <span class="smallwarning">?</span>Definitions </a>
                                </div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-6">
                                    <label for="additionalInstructions">Additional Instructions:</label><br />
                                    <span class="text-danger small">Do not enter Deliver-To info here!</span>
                                </div>
                                <div class="col-xs-6" id="addInstructions">
                                    <nested:textarea property="additionalInstructions" cols="25" rows="2" styleId="additionalInstructions" styleClass="form-control"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <br />
            <table>
                <tr>
                    <nested:notEmpty property="requestLineItemForms">
                        <td align="right">Request Line Items (Double-click to edit):</td>
                        <td>
                            <nested:select size="4" property="requestLineItemKey"
                                           ondblclick="this.form.action='viewEditPurchasingRequestLineItem.do'; this.form.submit(); return false;">
                                <nested:optionsCollection property="requestLineItemForms" label="descriptionSummary"
                                                          value="key"/>
                            </nested:select>
                        </td>
                    </nested:notEmpty>
                    <nested:equal property="input" value="purchasingRequest">
                        <td nowrap="nowrap">
                            <table cellspacing="0" cellpadding="0">
                                <tr>
                                    <td>
                                        <input type="submit" value="Create New Request Line Item (Non-Catalog)"
                                               onclick="this.form.action='viewCreatePurchasingRequestLineItem.do'"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="SUBMIT" value="Create New Request Line Item (Catalog)"
                                               onclick="this.form.input.value='purchasingRequest';this.form.action='viewAdvancedSearchItemsFromRequest.do'"/>
                                    </td>
                                </tr>
                                <nested:notEmpty property="requestLineItemForms">
                                    <nested:empty property="requestLineItemForm">
                                        <tr>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td colspan="3">
                                                <input type="submit" value="Remove Request Line Item"
                                                       onclick="return removeRequestLineItem(this.form)"/>
                                            </td>
                                        </tr>
                                    </nested:empty>
                                </nested:notEmpty>
                            </table>
                        </td>
                    </nested:equal>
                </tr>
            </table>
            <br/>
            <button type="submit" value="" class="btn btn-default">Save Request</button>
            <nested:notEqual property="input" value="purchasingRequest">
                &nbsp;&nbsp;<button type="reset" class="btn btn-default">Cancel</button>
            </nested:notEqual>
        </html:form>
    </body>
</html>
