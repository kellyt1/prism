<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>Resubmit Requests</title>
    </head>
    <body>
        <html:form action="/saveReopenedRequestLineItem" method="post" enctype="multipart/form-data">
            <input type="HIDDEN" name="fundingSourceKey"/>
            <nested:hidden property="input"/>
            <div class="row">
                <div class="col-md-12">
                    <h2>
                        <nested:empty property="requestId">Enter New Request</nested:empty>
                        <nested:notEmpty property="requestId">Edit Request</nested:notEmpty>
                    </h2>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Request Details</div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-xs-6">
                                    <strong>Request Tracking #:</strong>
                                </div>
                                <div class="col-xs-6">
                                    <nested:empty property="trackingNumber">Unassigned</nested:empty>
                                    <nested:notEmpty property="trackingNumber"><nested:write property="trackingNumber"/></nested:notEmpty>
                                </div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-6"><label for="requestor">Requestor:</label></div>
                                <div class="col-xs-6">
                                    <nested:select property="selectedRequestorId" styleClass="chosen-select" styleId="requestor">
                                        <option value="">Choose a Requestor</option>
                                        <nested:optionsCollection property="availableRequestors" label="lastAndFirstName" value="personId"/>
                                    </nested:select>
                                </div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-6"><label for="neededBy">Needed By:</label></div>
                                <div class="col-xs-6"><nested:text property="needByDate" size="10" styleClass="dateInput" styleId="neededBy"/></div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-6"><label for="priority">Priority:</label></div>
                                <div class="col-xs-6">
                                    <nested:select property="priorityCode" styleClass="chosen-select" styleId="priority">
                                        <nested:optionsCollection property="priorities" label="name" value="priorityCode"/>
                                    </nested:select>
                                </div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-12">
                                    <label>Special Instructions:<br/>
                                        <nested:textarea property="additionalInstructions" cols="75" rows="4" styleClass="form-control"/>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Deliver To</div>
                        <div class="panel-body">
                            <nested:present property="deliveryDetail">
                                <div class="row">
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
                                        <html:submit value="Change Delivery Info" disabled="true" styleClass="btn btn-default"
                                                     onclick="this.form.cmd.value='changeDeliveryInfo'; this.form.submit(); return false;"/>
                                    </div>
                                </div>
                            </nested:present>
                        </div>
                    </div>
                </div>
            </div>
            <nested:notEmpty property="requestLineItemForm">
                <div class="row">
                    <div class="col-md-12">
                        <table class="table">
                            <caption>Request Line Item Detail</caption>
                            <tr>
                                <td colspan="2">
                                    Request Line #:
                                    <nested:notEmpty property="requestLineItemForm.requestLineItemId">
                                        <strong><nested:write property="requestLineItemForm.requestLineItemId"/></strong>
                                    </nested:notEmpty>
                                    <nested:empty property="requestLineItemForm.requestLineItemId">Unassigned</nested:empty>
                                </td>
                                <td colspan="3">
                                    <label>
                                        Item Description:
                                        <nested:empty property="requestLineItemForm.item">
                                            <nested:textarea property="requestLineItemForm.itemDescription" cols="35" rows="2"/>
                                        </nested:empty>
                                        <nested:notEmpty property="requestLineItemForm.item">
                                            <span style="font-weight: 400;"><nested:write property="requestLineItemForm.item.description"/></span>
                                        </nested:notEmpty>
                                    </label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <nested:equal property="requestLineItemForm.itPurchase" value="true"><nested:hidden property="requestLineItemForm.categoryId"/></nested:equal>
                                    <nested:notEqual property="requestLineItemForm.itPurchase" value="true">
                                    <label>
                                        Category:
                                        <nested:empty property="requestLineItemForm.item">
                                            <nested:select property="requestLineItemForm.categoryId" styleClass="chosen-select">
                                                <option value="">Select a Category</option>
                                                <nested:optionsCollection property="requestLineItemForm.categories" label="name" value="categoryId"/>
                                            </nested:select>
                                        </nested:empty>
                                        <nested:notEmpty property="requestLineItemForm.item">
                                            <nested:write property="requestLineItemForm.item.category.name"/>
                                        </nested:notEmpty>
                                    </label>
                                    </nested:notEqual>
                                </td>
                                <td>
                                    <label>Quantity: <nested:text property="requestLineItemForm.quantity" size="10"/></label></td>
                                <td>
                                    <label>
                                        <nested:empty property="requestLineItemForm.item">
                                            Unit:
                                            <nested:select property="requestLineItemForm.unitId" styleClass="chosen-select">
                                                <option value="">Choose a Unit</option>
                                                <nested:optionsCollection property="requestLineItemForm.units" label="name" value="unitId"/>
                                            </nested:select>
                                        </nested:empty>
                                        <nested:notEmpty property="requestLineItemForm.item">
                                            Dispense Unit:
                                            <nested:write property="requestLineItemForm.item.dispenseUnit.name"/>
                                        </nested:notEmpty>
                                    </label>
                                </td>
                                <td nowrap="nowrap">
                                    <label>
                                        <nested:empty property="requestLineItemForm.item">
                                            Cost/Unit: $<nested:text property="requestLineItemForm.itemCost" size="20"/>
                                        </nested:empty>
                                        <nested:notEmpty property="requestLineItemForm.item">
                                            Cost/Dispense Unit: $<nested:write property="requestLineItemForm.item.dispenseUnitCost" format="#,##0.00"/>
                                        </nested:notEmpty>
                                    </label>
                                </td>
                                <td nowrap="nowrap">
                                    <span style="font-weight: 600;">Item Hazardous?:</span>
                                    <nested:empty property="requestLineItemForm.item">
                                        <label><nested:radio property="requestLineItemForm.itemHazardous" value="true"/> Yes</label>&nbsp;
                                        <label><nested:radio property="requestLineItemForm.itemHazardous" value="false"/> No</label>
                                    </nested:empty>
                                    <nested:notEmpty property="requestLineItemForm.item">
                                        <nested:notEmpty property="requestLineItemForm.item.hazardous">
                                            <label><nested:equal property="requestLineItemForm.item.hazardous" value="true"> Yes</nested:equal></label>&nbsp;
                                            <label><nested:notEqual property="requestLineItemForm.item.hazardous" value="true"> No</nested:notEqual></label>
                                        </nested:notEmpty>
                                    </nested:notEmpty>
                                </td>
                            </tr>

                            <nested:define id="rliForm2" property="requestLineItemForm"/>
            <c:choose>

                <c:when test="${rliForm2.suggestedVendorCatalogNumber == 'STAFFAUG' || rliForm2.suggestedVendorCatalogNumber == 'MNITCONTRACT'|| rliForm2.suggestedVendorCatalogNumber == 'WAN/Computing Services'}">
                    <tr>
                        <td>
                            <label>AC2: <nested:text property="requestLineItemForm.swiftItemId"/></label>
                        </td>

                        <td nowrap="nowrap">
                            <label>PO#: <nested:text property="requestLineItemForm.suggestedVendorName"/></label>
                        </td>
                        <td>
                            <label>
                                Request Type#
                                <nested:text property="requestLineItemForm.suggestedVendorCatalogNumber" readonly="true"/>
                            </label>
                        </td>
                        <td colspan="2">
                        </td>
                    </tr>
                </c:when>
                    <c:otherwise>

                                            <tr>
                                <td nowrap="nowrap">
                                    <label>
                                        <nested:empty property="requestLineItemForm.item">Vendor:</nested:empty>
                                        <nested:notEmpty property="requestLineItemForm.item">Vendor(s):</nested:notEmpty>
                                        <nested:text property="requestLineItemForm.suggestedVendorName" size="30" maxlength="50"/>
                                        <nested:notEmpty property="requestLineItemForm.item">
                                            <nested:iterate property="requestLineItemForm.vendorNames" id="vname">
                                                <br/><nested:write name="vname"/>
                                            </nested:iterate>
                                        </nested:notEmpty>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        Vendor Catalog#
                                        <nested:text property="requestLineItemForm.suggestedVendorCatalogNumber"/>
                                        <nested:notEmpty property="requestLineItemForm.item">
                                            <nested:iterate property="requestLineItemForm.vendorCatalogNbrs" id="vcat">
                                                <br/><nested:write name="vcat"/>
                                            </nested:iterate>
                                        </nested:notEmpty>
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        Vendor URL:
                                        <nested:text property="requestLineItemForm.suggestedVendorURL"/>
                                        <nested:notEmpty property="requestLineItemForm.item">
                                            <nested:iterate property="requestLineItemForm.vendorURLs" id="vur2">
                                                <br/>
                                                <nested:write name="vur2"/>
                                            </nested:iterate>
                                        </nested:notEmpty>
                                    </label>
                                </td>
                                <td colspan="2">
                                    <nested:present property="requestLineItemForm.swiftItemId">
                                        <label>SWIFT Item Id or AC2: <nested:text property="requestLineItemForm.swiftItemId"/></label>
                                    </nested:present>
                                </td>
                            </tr>
                </c:otherwise>


            </c:choose>

            <tr>
                                <nested:notEmpty property="requestLineItemForm.fundingSourceForms">
                                    <td>Funding Sources<br>(Double-click to edit):</td>
                                </nested:notEmpty>
                                <td colspan="5">
                                    <nested:notEmpty property="requestLineItemForm.fundingSourceForms">
                                        <nested:select size="3" property="requestLineItemForm.fundingSourceKey"
                                                       ondblclick="this.form.action='viewEditPurchasingRequestLineItemFundingSource.do'; this.form.submit(); return false;">
                                            <nested:optionsCollection property="requestLineItemForm.fundingSourceForms" label="summary" value="key"/>
                                        </nested:select>
                                    </nested:notEmpty>
                                    <nested:empty property="requestLineItemForm.fundingSourceForm">
                                        <div style="display: inline-block;">
                                            <button type="submit" onclick="this.form.action='viewCreatePurchasingRequestLineItemFundingSource.do'" class="btn btn-default">Add New Funding Source</button>
                                            <nested:notEmpty property="requestLineItemForm.fundingSourceForms">
                                                <br/><button type="submit" onclick="return removeRequestLineItemFundingSource(this.form)" class="btn btn-default">Remove Funding Source</button>
                                            </nested:notEmpty>
                                        </div>
                                    </nested:empty>
                                </td>
                            </tr>
                            <nested:notEmpty property="requestLineItemForm.fundingSourceForm">
                                <tr>
                                    <td colspan="5">
                                        <label>
                                            <bean:message key="orgBudget"/> :
                                            <nested:select property="requestLineItemForm.fundingSourceForm.orgBudgetId" styleClass="chosen-select">
                                                <option value="">Choose a <bean:message key="orgBudget"/></option>
                                                <nested:optionsCollection property="requestLineItemForm.fundingSourceForm.orgBudgets"
                                                                          label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
                                            </nested:select>
                                        </label>
                                        &nbsp;
                                        <label>Amount: <nested:text property="requestLineItemForm.fundingSourceForm.amount" size="15"/></label>
                                        &nbsp;
                                        <button type="SUBMIT" class="btn btn-default" onclick="this.form.action='savePurchasingRequestLineItemFundingSource.do'">Done With Funding Source</button>
                                    </td>
                                </tr>
                            </nested:notEmpty>
                            <tr>
                                <td colspan="5">
                                    <label for="addFile">Attach Files</label><br/>
                                    <nested:file property="requestLineItemForm.purchasingInfoFile" size="20" onchange="this.form.cmd.value='saveCart'; this.form.submit(); return false;" styleId="addFile"/>
                                    <i class="attachment">attachment(s):
                                        <% java.util.Date dt = new java.util.Date (); %>
                                        <nested:iterate property="requestLineItemForm.requestLineItem.attachedFileNonCats" id="attachedFile">
                                            &nbsp;<a href=${pageContext.request.contextPath}/purchasing/downloadFileAction.do?attachedFileNonCatId=<nested:write name="attachedFile" property='attachedFileNonCatId'/>&dt=<%=dt.getTime()%> > <nested:write name="attachedFile" property='fileName'/> </a>
                                        </nested:iterate>
                                    </i>
                                    <nested:notEmpty property="requestLineItemForm.item">
                                        <br/>
                                        <i class="attachment">
                                            <nested:iterate property="requestLineItemForm.item.attachedFiles" id="attachedFile">
                                                &nbsp;<a href=${pageContext.request.contextPath}/inventory/downloadFileAction.do?attachedFileId=<nested:write
                                                    name="attachedFile" property='attachedFileId'/>&dt=<%=dt.getTime()%> > <nested:write
                                                    name="attachedFile" property='fileName'/> </a>
                                            </nested:iterate>
                                        </i>
                                    </nested:notEmpty>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-md-12">
                        <table class="table table-bordered table-striped">
                            <thead>
                            <tr>
                                <th scope="col">Note</th>
                                <th scope="col">Author</th>
                                <th scope="col">Date</th>
                                <td>
                                    <button type="submit" onclick="this.form.action='addPurchasingRequestLineItemNote.do'" class="btn btn-default">Add Note</button>
                                </td>
                            </tr>
                            </thead>
                            <tbody>
                            <nested:iterate id="noteForm" property="requestLineItemForm.noteForms">
                                <nested:equal property="removed" value="false">
                                    <tr>
                                        <c:choose>
                                            <c:when test="${User.firstAndLastName == noteForm.authorName}">
                                                <td><nested:textarea property="noteText" rows="2" cols="40"/></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td><nested:write property="noteText"/></td>
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
                            </tbody>
                        </table>
                    </div>
                </div>
            </nested:notEmpty>
            <br/>
            <div class="row">
                <div class="col-md-12 text-center">
                    <div class="btn-group">
                        <button type="reset" onclick="parent.location='viewMyRequests.do'" class="btn btn-default">Cancel</button>
                        <button type="submit" onclick="this.form.action='saveReopenedRequestLineItem.do'" class="btn btn-default">Resubmit Request</button>
                    </div>
                </div>
            </div>
            <script type="text/javascript">
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
                    if (jQuery("[name='requestLineItemForm.fundingSourceKey']").children().size() == 1){
                        alert("Cannot remove last funding string");
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
        </html:form>
    </body>
</html>