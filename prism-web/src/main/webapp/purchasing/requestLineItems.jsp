<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>Purchase Requests</title>
    </head>
    
    <body>
        <span style="color: #f00;"> <%=(request.getParameter("message") != null) ? request.getParameter("message") : ""%></span>
        <html:form action="/searchPurchasingRequestLineItems" method="post">
            <input type="HIDDEN" name="requestLineItemId"/>
            <input type="HIDDEN" name="input" value="purchasingRequestLineItems"/>
            <fieldset>
                <legend>Search Request Line Items</legend>
                <div class="row">
                    <div class="form-group col-md-4">
                        <label>Request Tracking #: <nested:text property="requestTrackingNumber" size="15"/></label>

                        <label><bean:message key="orgBudget"/> Code(s): <nested:text property="orgBudgetCode" size="35" /></label>

                        <label>Requestor:
                            <nested:select property="requestorId" styleClass="chosen-select">
                                <option value="">Any</option>
                                <nested:optionsCollection property="requestors" label="lastAndFirstName" value="personId"/>
                            </nested:select>
                        </label>
                        <br>
                        <span>
                            <label>"Need By" date between: </label>
                            <nested:text property="neededByFrom" size="10" styleClass="dateInput"/>
                            &nbsp;and&nbsp;
                            <nested:text property="neededByTo" size="10" styleClass="dateInput"/>
                        </span>
                    </div>
                    <div class="form-group col-md-4">
                        <label>Status:
                            <nested:select property="statusCode" styleClass="chosen-select">
                                <option value="<nested:write property='defaultStatusCodes' />">Any</option>
                                <nested:optionsCollection property="statuses" label="name" value="statusCode"/>
                            </nested:select>
                        </label>
                        <label>Item Description: <nested:text property="itemDescription" size="35"/></label>
                        <br>
                        <label>Item Category:
                            <nested:select property="categoryId" styleClass="chosen-select">
                                <option value="">Any</option>
                                <nested:optionsCollection property="categories" label="name" value="categoryId"/>
                            </nested:select>
                        </label>
                    </div>
                    <div class="form-group col-md-4">
                        <label>Vendor Name: <nested:text property="vendorName" size="35" /></label>

                        <label>Buyer:
                            <nested:select property="purchaserId" styleClass="chosen-select">
                                <option value="">Any</option>
                                <nested:optionsCollection property="purchasers" label="lastAndFirstName" value="personId"/>
                            </nested:select>
                        </label>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div style="clear: both; width: 100%;" class="text-center">
                        <button type="submit" class="btn btn-default">Search</button>&nbsp;
                        <button type="reset" class="btn btn-default">Clear</button>&nbsp;
                        <button onclick="this.form.action='/purchasing/index.jsp';this.form.submit(); return false;" class="btn btn-default">Cancel</button>
                    </div>
                </div>
            </fieldset>
            <br>
            <nested:notEmpty property="rliForms">
                <div class="col-md-12 text-center">
                    <button type="SUBMIT" onclick="this.form.action='viewCreateOrder.do';" class="btn btn-default">Create New Purchase Order</button>&nbsp;
                    <button type="SUBMIT" onclick="this.form.action='addReqLnItmsToExistingOrder.do'" class="btn btn-default">Add To Existing Purchase Order</button>&nbsp;
                    <button type="SUBMIT" onclick="this.form.action='changeStatusSwiftItems.do';" class="btn btn-default">MDH Inventory Ordered</button>&nbsp;
                    <button onclick="this.form.action='viewSelectedRLIsDollarAmounts.do';this.form.target='_blank'; this.form.submit(); this.form.target='_self'; return false;" class="btn btn-default">Display Total Cost of Selected RLIs</button>&nbsp;
                    <button onclick="this.form.action='reopenEnterMessageRLIs.do';this.form.submit(); return false;" class="btn btn-default">Re-Open selected RLIs</button>&nbsp;
                    <button type="SUBMIT" onclick="this.form.action='viewCreateComputerOrder.do';" class="btn btn-default">Create MNIT Order</button>&nbsp;
                    <button onclick="this.form.action='/purchasing/index.jsp';this.form.submit(); return false;" class="btn btn-default">Cancel</button>
                </div>
                <br/><br/>
            <table class="sortable table table-bordered table-striped table-condensed" id="unique_id">
                <thead>
                    <tr>
                        <th scope="col" class="unsortable"><span class="glyphicon glyphicon-ok"></span></th>
                        <th scope="col">Request Line</th>
                        <th scope="col">Tracking#</th>
                        <th scope="col" style="min-width: 200px;">Description</th>
                        <th scope="col">Vendor(s)</th>
                        <th scope="col">Catalog #</th>
                        <th scope="col">SWIFT Budget</th>
                        <th scope="col">Status</th>
                        <th scope="col">Delivery Detail</th>
                        <th scope="col">Need By</th>
                        <th scope="col">Request Date</th>
                        <%--<th scope="col">Item Type</th>--%>
                    </tr>
                </thead>
                    <tbody>
                        <nested:iterate property="rliForms" id="rliForm">
                            <nested:equal property="urgency" value="URGENT"><tr class="danger"></nested:equal>
                            <nested:equal property="urgency" value="WARNDATE"><tr class="warning"></nested:equal>
                            <nested:equal  property="urgency" value="NORMAL"><tr></nested:equal>
                                <td style="vertical-align: middle;">
                                    <nested:equal property="requestLineItem.request.allLineItemsActedOn" value="true">
                                        <nested:checkbox property="selected" disabled="false"/>
                                    </nested:equal>
                                    <%--<nested:equal property="requestLineItem.status.statusCode" value="WFP">
                                        &lt;%&ndash;<nested:notPresent property="requestLineItem.swiftItemId">&ndash;%&gt;
                                            <nested:checkbox property="selected" disabled="false"/>
                                        &lt;%&ndash;</nested:notPresent>&ndash;%&gt;
                                    </nested:equal>
                                    <nested:notEqual property="requestLineItem.status.statusCode" value="WFP">
                                        <nested:notEqual property="requestLineItem.status.statusCode" value="POB">
                                            <nested:checkbox property="selected" disabled="true"/>
                                        </nested:notEqual>
                                        <nested:equal property="requestLineItem.status.statusCode" value="POB">
                                            <nested:checkbox property="selected" disabled="false"/>
                                        </nested:equal>
                                    </nested:notEqual>--%>
                                </td>
                                <td class="text-center" style="vertical-align: middle;">
                                    <input style="width:75px" type="SUBMIT" value="<nested:write property='requestLineItem.requestLineItemId'/>"
                                          onclick="this.form.action='viewEditPurchasingRequest.do';this.form.requestLineItemId.value='<nested:write property='requestLineItem.requestLineItemId'/>'" class="btn btn-default"/>
                                    <!--Added this to give the sorttable.js a requestLineId to sort-->
                                    <span style="visibility:hidden;"><nested:write property="requestLineItem.requestLineItemId" /></span>
                                </td>
                                <td>
                                    <nested:write property="requestLineItem.request.trackingNumber"/>
                                </td>
                                <td>
                                    <nested:equal property="requestLineItem.suggestedVendorCatalogNumber" value="MNITCONTRACT" >
                                        <strong>MNIT Contracting BB</strong><br/>
                                    </nested:equal>
                                    <nested:equal property="requestLineItem.suggestedVendorCatalogNumber" value="STAFFAUG" >
                                        <strong>MNIT Staffing BB</strong><br/>
                                    </nested:equal>
                                    <nested:equal property="requestLineItem.suggestedVendorCatalogNumber" value="WAN/Computing Services" >
                                        <strong>WAN/Computing Services</strong><br/>
                                    </nested:equal>
                                    <nested:present property="icnbr">
                                        <nested:write property="icnbr" /><hr style="margin: 5px 0;">
                                    </nested:present>
                                    <nested:notEmpty property="requestLineItem.swiftItemId">
                                         <c:choose>
                                            <c:when test="${rliForm.requestLineItem.suggestedVendorCatalogNumber == 'STAFFAUG' || rliForm.requestLineItem.suggestedVendorCatalogNumber == 'MNITCONTRACT'}">
                                                <strong>AC2 Code - <nested:write property="requestLineItem.swiftItemId"/></strong><hr style="margin: 5px 0;">
                                            </c:when>
                                             <c:when test="${rliForm.requestLineItem.suggestedVendorCatalogNumber == 'WAN/Computing Services'}">
                                                 <strong>Account# - <nested:write property="requestLineItem.swiftItemId"/></strong><hr style="margin: 5px 0;">
                                             </c:when>
                                            <c:otherwise>
                                                <strong>SWIFT ID# - <nested:write property="requestLineItem.swiftItemId"/></strong><hr style="margin: 5px 0;">
                                            </c:otherwise>
                                        </c:choose>
                                    </nested:notEmpty>


                                    <nested:write property="descriptionSummary" filter="false"/>
                                    <nested:notEmpty property="requestLineItem.attachedFileNonCats">
                                        <hr style="margin: 5px 0;">
                                        <% java.util.Date dt = new java.util.Date (); %>
                                        <i class="attachment"><strong>Attachment(s):</strong>
                                        <nested:iterate property="requestLineItem.attachedFileNonCats" id="attachedFile"  >
                                            &nbsp;<a href=${pageContext.request.contextPath}/purchasing/downloadFileAction.do?attachedFileNonCatId=<nested:write name="attachedFile" property='attachedFileNonCatId'/>&dt=<%=dt.getTime()%> > <nested:write name="attachedFile" property='fileName'/></a>
                                        </nested:iterate></i>
                                    </nested:notEmpty>
                                </td>
                                <td class="text-center">
                                    <nested:present property="requestLineItem.item">
                                        <nested:iterate property="vendorNames" id="a" indexId="val" ><nested:write name="a" /><br></nested:iterate>
                                    </nested:present>
                                    <i><nested:write property="requestLineItem.suggestedVendorName"/></i>
                                </td>
                                <td class="text-center">
                                    <nested:present property="requestLineItem.item">
                                        <nested:iterate property="vendorCatalogNbrs" id="b" indexId="catNbr" >
                                            <nested:notEmpty name="b"><nested:write name="b" /></nested:notEmpty><br/>
                                        </nested:iterate>
                                    </nested:present>
                                    <nested:present property="requestLineItem.suggestedVendorCatalogNumber">
                                        <i><nested:write property="requestLineItem.suggestedVendorCatalogNumber"/></i>
                                    </nested:present>
                                </td>
                                <td>
                                    <nested:present property="requestLineItem.fundingSrcSummary">
                                        <nested:iterate property="requestLineItem.fundingSrcSummary.fundingStrings" id="fs" indexId="fsval" >
                                            <nested:write name="fs" />
                                            <br/>
                                        </nested:iterate>
                                        <%--<nested:write property="requestLineItem.fundingSrcSummary.orgBudgetCodes"/>--%>
                                        <%--<nested:write property="requestLineItem.fundingSrcSummary.fundingStrings"/>--%>
                                    </nested:present>
                                </td>
                                <td class="text-center">
                                    <nested:present property="requestLineItem.status">
                                       <nested:write property="requestLineItem.status.name"/>
                                    </nested:present>
                                </td>
                                <td class="text-nowrap">
                                    <nested:present property="requestLineItem.request.deliveryDetail">
                                        <nested:present property="requestLineItem.request.deliveryDetail.organization">
                                            <nested:write property="requestLineItem.request.deliveryDetail.organization.orgName"/><br/>
                                        </nested:present>
                                        <nested:notPresent property="requestLineItem.request.deliveryDetail.organization">
                                            <nested:present property="requestLineItem.request.deliveryDetail.facility">MDH</nested:present><br/>
                                        </nested:notPresent>
                                        <nested:write property="requestLineItem.request.deliveryDetail.recipientName"/><br/>
                                        <nested:present property="requestLineItem.request.deliveryDetail.facility">
                                            <nested:write property="requestLineItem.request.deliveryDetail.facility.facilityName"/><br/>
                                        </nested:present>
                                        <nested:present property="requestLineItem.request.deliveryDetail.mailingAddress">
                                            <nested:write property="requestLineItem.request.deliveryDetail.mailingAddress.address1"/><br/>
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
                                    <nested:notEmpty property="requestLineItem.request.needByDate">
                                        <nested:write format="MM/dd/yyyy" property="requestLineItem.request.needByDate"/>
                                    </nested:notEmpty>
                                </td>
                                <td class="text-center">
                                    <nested:present property="requestLineItem.request.dateRequested">
                                        <nested:write property="requestLineItem.request.dateRequested" format="MM/dd/yyyy kk:mm"/>
                                    </nested:present>
                                </td>
                                <%--<td class="text-center">
                                    <nested:present property="requestLineItem.item">
                                        <nested:write property="requestLineItem.item.itemType"/>
                                    </nested:present>
                                    <nested:notPresent property="requestLineItem.item">Purchase Item</nested:notPresent>
                                </td>--%>
                            </tr>
                            <bean:write name="requestLineItemSearchForm" property="flip"/>
                        </nested:iterate>
                    </tbody>
                </table>
                <br>
                <div class="col-md-12 text-center">
                    <button type="SUBMIT" onclick="this.form.action='viewCreateOrder.do';" class="btn btn-default">Create New Purchase Order</button>&nbsp;
                    <button type="SUBMIT" onclick="this.form.action='addReqLnItmsToExistingOrder.do'" class="btn btn-default">Add To Existing Purchase Order</button>&nbsp;
                    <button type="SUBMIT" onclick="this.form.action='changeStatusSwiftItems.do';" class="btn btn-default">MDH Inventory Ordered</button>&nbsp;
                    <button onclick="this.form.action='viewSelectedRLIsDollarAmounts.do';this.form.target='_blank'; this.form.submit(); this.form.target='_self'; return false;" class="btn btn-default">Display Total Cost of Selected RLIs</button>&nbsp;
                    <button onclick="this.form.action='reopenEnterMessageRLIs.do';this.form.submit(); return false;" class="btn btn-default">Re-Open selected RLIs</button>&nbsp;
                    <button type="SUBMIT" onclick="this.form.action='viewCreateComputerOrder.do';" class="btn btn-default">Create MNIT Order</button>&nbsp;
                    <button onclick="this.form.action='/purchasing/index.jsp';this.form.submit(); return false;" class="btn btn-default">Cancel</button>
                </div>
            </nested:notEmpty>
        </html:form>
    </body>
</html>