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
                form.action = "removePurchasingRequestLineItem2.do";
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
                form.action = "removePurchasingRequestLineItemFundingSource2.do";
                return true;
            }
            else {
                return false;
            }
        }
    </script>
</head>

<body>
<html:form action="/savePurchasingRequest2" method="post">
<input type="HIDDEN" name="fundingSourceKey"/>
<nested:hidden property="input"/>
<table align="center" cellspacing="0" cellpadding="0" border="0" width="70%">
<tr>
    <td>&nbsp;</td>
</tr>
<tr>
    <td class="tablelabel" align="center">
        <nested:empty property="requestId">
            Enter New Request
        </nested:empty>
        <nested:notEmpty property="requestId">
            Edit Request
        </nested:notEmpty>
    </td>
</tr>
<tr>
    <td>&nbsp;</td>
</tr>
<tr>
    <th align="left" class="tableheader">&nbsp;&nbsp;Request Detail</th>
</tr>
<tr>
    <td>
        <table cellspacing="0" cellpadding="3" border="0">
            <tr>
                <td align="right" class="tablelabel">Request Tracking #:</td>
                <td>
                    <nested:empty property="trackingNumber">
                        Unassigned
                    </nested:empty>
                    <nested:notEmpty property="trackingNumber">
                        <strong>
                            <nested:write property="trackingNumber"/>
                        </strong>
                    </nested:notEmpty>
                </td>
                <td align="right" class="tablelabel">Requestor:</td>
                <td>
                    <nested:select property="selectedRequestorId">
                        <option value="">Choose a Requestor</option>
                        <nested:optionsCollection property="availableRequestors" label="lastAndFirstName"
                                                  value="personId"/>
                    </nested:select>
                </td>
                <td align="right" class="tablelabel">Needed By:</td>
                <td nowrap="nowrap">
                    <nested:text property="needByDate" size="10" styleClass="dateInput"/>
                </td>
                <td align="right" class="tablelabel">Priority:</td>
                <td>
                    <nested:select property="priorityCode">
                        <nested:optionsCollection property="priorities" label="name" value="priorityCode"/>
                    </nested:select>
                </td>
            </tr>
            <tr>
                <td align="right" class="tablelabel">Special<br>Instructions:</td>
                <td colspan="7">
                    <nested:textarea property="additionalInstructions" cols="75" rows="2"/>
                </td>
            </tr>
            <tr>
                <td align="right" class="tablelabel">Deliver To:</td>
                <td align="center" colspan="7">
                    <nested:present property="deliveryDetail">
                        <table>
                            <tr>
                                <td class="tablelabel" valign="top">Organization:</td>
                                <td class="tabledetail" valign="top">
                                    <nested:present property="deliveryDetail.organization">
                                        <nested:write property="deliveryDetail.organization.orgName"/>
                                    </nested:present>
                                    <nested:notPresent property="deliveryDetail.organization">
                                        <nested:present property="deliveryDetail.facility">
                                            MDH
                                        </nested:present>
                                    </nested:notPresent>
                                </td>
                            </tr>
                            <tr>
                                <td class="tablelabel" valign="top">Recipient:</td>
                                <td class="tabledetail">
                                    <nested:write property="deliveryDetail.recipientName"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="tablelabel" valign="top">Mailing Address:</td>
                                <td class="tabledetail" valign="top">
                                    <nested:present property="deliveryDetail.facility">
                                        <nested:write property="deliveryDetail.facility.facilityName"/>
                                        <br/>
                                    </nested:present>
                                    <nested:present property="deliveryDetail.mailingAddress">
                                        <nested:write property="deliveryDetail.mailingAddress.address1"/>
                                        <br/>
                                        <nested:present property="deliveryDetail.mailingAddress.address2">
                                            <nested:write property="deliveryDetail.mailingAddress.address2"/>
                                            <br/>
                                        </nested:present>
                                        <nested:write property="deliveryDetail.mailingAddress.city"/>
                                        ,&nbsp;
                                        <nested:write property="deliveryDetail.mailingAddress.state"/>
                                        &nbsp;
                                        <nested:write property="deliveryDetail.mailingAddress.zip"/>
                                    </nested:present>
                                </td>
                            </tr>
                            <tr>
                                <td class="tabledetail" colspan="2" align="center">
                                    <html:submit value="Change Delivery Info" disabled="true"
                                                 onclick="this.form.cmd.value='changeDeliveryInfo'; this.form.submit(); return false;"/>
                                </td>
                            </tr>
                        </table>
                    </nested:present>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td>&nbsp;</td>
</tr>
<tr>
    <td class="tablelabel">
        <table cellspacing="0" cellpadding="3" border="0" align="left">
            <tr>
                <nested:notEmpty property="requestLineItemForms">
                    <td class="tablelabel" align="right">Request Line Items (Double-click to edit):</td>
                    <td>
                        <nested:select size="4" property="requestLineItemKey"
                                       ondblclick="this.form.action='viewEditPurchasingRequestLineItem2.do'; this.form.submit(); return false;">
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
                                           onclick="this.form.action='viewCreatePurchasingRequestLineItem2.do'"/>
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
    </td>
</tr>
<tr>
    <th class="tableheader">&nbsp;</th>
</tr>
<tr>
    <td>&nbsp;</td>
</tr>
<tr>
    <td align="center">
        <input type="submit" value="Save Request"/>
        <nested:notEqual property="input" value="purchasingRequest">
            &nbsp;&nbsp;<input type="submit" value="Cancel"
            onclick="this.form.action='cancelEditPurchasingRequest2.do'"/>
        </nested:notEqual>
    </td>
</tr>
</table>
</html:form>
</body>
</html>
