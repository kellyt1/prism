<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>Request Line Item Detail</title>
    </head>
    <body>
        <html:form action="/savePurchasingRequestLineItem" method="post">
            <table cellspacing="0" cellpadding="3" border="0" width="90%" align="center">
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <th>Request Line Item Detail</th>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>
                    <table cellspacing="0" cellpadding="3" border="0" width="100%">
                    <tr>
                        <td>Request Line Item #:</td>
                        <td>Item Description:</td>
                        <td>Item Type:</td>
                        <td>Request Date:</td>
                        <td>Needed By:</td>
                    </tr>
                    <tr>
                        <td>
                            <nested:write property="requestLineItem.requestLineItemId"/>
                        </td>
                        <td>
                            <nested:present property="icnbr">
                                <nested:write property="icnbr"/>
                                <br/>
                            </nested:present>
                            <nested:empty property="requestLineItem.item">
                                <nested:textarea property="itemDescription" rows="2" cols="30"/>
                            </nested:empty>
                            <nested:notEmpty property="requestLineItem.item">
                                <nested:write property="itemDescription"/>
                            </nested:notEmpty>
                        </td>
                        <td>
                            <nested:write property="requestLineItem.requestItemType"/>
                        </td>
                        <td>
                            <nested:write property="requestLineItem.request.dateRequested" format="MM/dd/yyyy"/>
                        </td>
                        <td>
                            <nested:notEmpty property="requestLineItem.request.needByDate">
                                <nested:write property="requestLineItem.request.needByDate" format="MM/dd/yyyy"/>
                            </nested:notEmpty>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="5"></td>
                    </tr>
                    <tr>
                        <td>Requestor:</td>
                        <td>Deliver To:</td>
                        <td>Evaluator(s):</td>
                        <td colspan="2">Status:</td>
                    </tr>
                    <tr>
                        <td>
                            <nested:notEqual property="requestLineItem.request.requestor.firstName" value="Unknown">
                                <nested:write property="requestLineItem.request.requestor.firstAndLastName"/>
                            </nested:notEqual>
                            <nested:equal property="requestLineItem.request.requestor.firstName" value="Unknown">
                                <nested:select property="requestForm.requestorId">
                                    <nested:optionsCollection property="requestForm.availableRequestors" label="firstAndLastName"
                                                              value="personId"/>
                                </nested:select>
                            </nested:equal>
                        </td>
                        <td>
                            <nested:present property="deliveryDetail">
                                <table cellspacing="0">
                                    <tr>
                                        <td nowrap="nowrap">
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
                                        <td nowrap="nowrap">
                                            <nested:write property="deliveryDetail.recipientName"/>
                                        </td>
                                    </tr>
                                    <nested:present property="deliveryDetail.facility">
                                        <tr>
                                            <td nowrap="nowrap">
                                                <nested:write property="deliveryDetail.facility.facilityName"/>
                                            </td>
                                        </tr>
                                    </nested:present>
                                    <nested:present property="deliveryDetail.mailingAddress">
                                        <tr>
                                            <td nowrap="nowrap">
                                                <nested:write property="deliveryDetail.mailingAddress.address1"/>
                                                <br/>
                                                <nested:present property="deliveryDetail.mailingAddress.address2">
                                                    <nested:write property="deliveryDetail.mailingAddress.address2"/>
                                                    <br/>
                                                </nested:present>
                                                <nested:write property="deliveryDetail.mailingAddress.city"/>
                                                ,
                                                <nested:write property="deliveryDetail.mailingAddress.state"/>
                                                &nbsp;
                                                <nested:write property="deliveryDetail.mailingAddress.zip"/>
                                            </td>
                                        </tr>
                                    </nested:present>
                                </table>
                            </nested:present>
                        </td>
                        <td>
                            <table>
                                <nested:iterate id="requestEvaluation" property="requestLineItem.requestEvaluations">
                                    <tr>
                                        <td>
                                            <bean:write name="requestEvaluation" property="evaluator.firstAndLastName"/>
                                        </td>
                                    </tr>
                                </nested:iterate>
                            </table>
                        </td>
                        <td colspan="2">
                            <nested:select property="statusId">
                                <nested:optionsCollection property="statuses" label="name" value="statusId"/>
                            </nested:select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="5"></td>
                    </tr>
                    <tr>
                        <td>Category:</td>
                        <td>Quantity Requested:</td>
                        <td>Hazardous?</td>
                        <td colspan="2">Suggested Vendor:</td>
                    </tr>
                    <tr>
                        <td>
                            <nested:select property="categoryId">
                                <nested:optionsCollection property="categories" label="name" value="categoryId"/>
                            </nested:select>
                        </td>
                        <td>
                            <nested:text property="requestLineItem.quantity" size="10"/>
                        </td>
                        <td>
                            <nested:equal property="requestLineItem.itemHazardous" value="true">
                                Yes
                            </nested:equal>
                            <nested:equal property="requestLineItem.itemHazardous" value="false">
                                No
                            </nested:equal>
                        </td>
                        <td colspan="2">
                            <nested:write property="suggestedVendorName"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="5"></td>
                    </tr>
                    <tr>
                        <td>Suggested Vendor URL:</td>
                        <td>Suggested Vendor Catalog #:</td>
                        <td>Suggested Vendor Price:</td>
                        <td colspan="2">Purchase Info Files:</td>
                    </tr>
                    <tr>
                        <td>
                            <nested:write property="requestLineItem.suggestedVendorURL"/>
                        </td>
                        <td>
                            <nested:write property="requestLineItem.suggestedVendorCatalogNumber"/>
                        </td>
                        <td>
                            <nested:write property="requestLineItem.itemCost"/>
                        </td>
                        <td colspan="2">Under Construction</td>
                    </tr>
                    <tr>
                        <td colspan="5"></td>
                    </tr>
                    <tr>
                        <td colspan="2">Additional Instructions:</td>
                        <td colspan="3"></td>
                    </tr>
                    <tr>
                        <td colspan="5">
                            <nested:write property="requestLineItem.request.additionalInstructions"/>
                        </td>
                    </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>Funding Sources</td>
            </tr>
            <tr>
                <td>
                    <table cellspacing="0" cellpadding="5" border="0" width="40%">
                        <tr>
                            <th align="left">
                                <bean:message key="orgBudget"/>
                            </th>
                            <th align="left">Amount</th>
                        </tr>
                        <nested:iterate id="fundingSources" property="requestLineItem.fundingSources">
                            <tr>
                                <td>
                                    <nested:write name="fundingSources" property="orgBudget.orgBudgetCodeAndNameAndFY"/>
                                </td>
                                <td>
                                    <logic:equal name="purchasingRequestLineItemForm" property="amountInDollars" value="true">
                                        $
                                        <nested:write name="fundingSources" property="amount" format="#.##"/>
                                    </logic:equal>
                                    <logic:equal name="purchasingRequestLineItemForm" property="amountInDollars" value="false">
                                        <nested:write name="fundingSources" property="amount" format="#"/>
                                        %
                                    </logic:equal>
                                </td>
                            </tr>
                        </nested:iterate>
                    </table>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>Request Line Item Notes</td>
            </tr>
            <tr>
                <td>
                    <table cellspacing="0" cellpadding="5" border="0" width="65%">
                        <tr>
                            <th>Note</th>
                            <th>Author</th>
                            <th>Date</th>
                            <td>
                                <input type="submit" value="Add Note"
                                       onclick="this.form.action='addPurchasingRequestLineItemNote.do'"/>
                            </td>
                        </tr>
                        <nested:iterate id="noteForm" property="noteForms">
                            <nested:equal property="removed" value="false">
                                <tr>
                                    <c:choose>
                                        <c:when test="${User.firstAndLastName == noteForm.authorName}">
                                            <td>
                                                <nested:textarea property="noteText" rows="2" cols="40"/>
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td width="600">
                                                <nested:write property="noteText"/>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td nowrap="nowrap">
                                        <nested:write property="authorName"/>
                                    </td>
                                    <td>
                                        <nested:write property="insertionDate" format="MM/dd/yyyy"/>
                                    </td>
                                    <td>
                                        <c:if test="${User.firstAndLastName == noteForm.authorName}">
                                            <a href="removePurchasingRequestLineItemNote.do?key=<nested:write property='key'/>">remove</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </nested:equal>
                        </nested:iterate>
                    </table>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center">
                    <input type="submit" value="Save Changes"/>
                    <input type="submit" value="Cancel" onclick="this.form.action='cancelEditPurchasingRequestLineItem.do'"/>
                </td>
            </tr>
            </table>
        </html:form>
    </body>
</html>