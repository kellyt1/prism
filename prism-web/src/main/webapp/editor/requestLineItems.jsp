<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>

<html>
    <head>
        <title>Purchase Requests</title>
        <script type="text/javascript">
            function clearForm() {
                var form = window.document.forms[0];
                var currentTime = new Date();
                var month = currentTime.getMonth() + 1;
                var day = currentTime.getDate();
                var year = currentTime.getFullYear();
                var currentdate = month + "/" + day + "/" + year;

                //alert("BEGIN");
                form.requestTrackingNumber.value = "";
                form.neededByFrom.value = "";
                form.orgBudgetCode.value = "";
                form.vendorName.value = "";
                form.neededByTo.value = currentdate;
                form.itemDescription.value = "";

                form.purchaserId.selectedIndex = 0;
                form.requestorId.selectedIndex = 0;
                form.statusCode.selectedIndex = 0;
                form.categoryId.selectedIndex = 0;

                //alert("END");
            }
        </script>
    </head>

    <body>
        <span style="color: #f00;"> <%=request.getParameter("message")!=null ? request.getParameter("message") : "" %></span>
        <html:form action="/searchPurchasingRequestLineItems2" method="post">
            <input type="HIDDEN" name="requestLineItemId"/>
            <input type="HIDDEN" name="input" value="purchasingRequestLineItems"/>
            <fieldset>
                    <legend>Search Request Line Items</legend>
                <div class="form-group pull-left" style="width: 33%;">
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
                <div class="form-group pull-left" style="width: 33%;">
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
                <div class="form-group pull-left" style="width: 33%;">
                    <label>Vendor Name: <nested:text property="vendorName" size="35" /></label>

                    <label>Buyer:
                        <nested:select property="purchaserId" styleClass="chosen-select">
                            <option value="">Any</option>
                            <nested:optionsCollection property="purchasers" label="lastAndFirstName" value="personId"/>
                        </nested:select>
                    </label>
                </div>
                <br>
                <div style="clear: both; width: 100%;" class="text-center">
                    <button type="submit" class="btn btn-default">Search</button>&nbsp;
                    <button onclick="clearForm();" class="btn btn-default">Clear</button>&nbsp;
                    <button onclick="this.form.action='/editor/index.jsp';this.form.submit(); return false;" class="btn btn-default">Cancel</button>
                </div>
            </fieldset>
            <br>
            <nested:notEmpty property="rliForms">
            <table class="sortable table table-bordered table-striped" id="unique_id">
                <thead>
                <tr>
                    <th class="unsortable" scope="col"><span class="glyphicon glyphicon-ok"></span></th>
                    <th scope="col">Request Line</th>
                    <th scope="col">Tracking#</th>
                    <th scope="col">Description</th>
                    <th scope="col">Vendor(s)</th>
                    <th scope="col">Catalog #</th>
                    <th scope="col">Org(s)</th>
                    <th scope="col">Status</th>
                    <th scope="col">Need By</th>
                    <th scope="col">Request Date</th>
                    <th scope="col">Item Type</th>
                </tr>
                </thead>
                    <tbody>
                        <nested:iterate property="rliForms">
                            <nested:equal property="urgency" value="URGENT">
                                  <tr class="danger">
                            </nested:equal>
                            <nested:equal property="urgency" value="WARNDATE">
                                  <tr class="warning">
                            </nested:equal>
                            <nested:equal  property="urgency" value="NORMAL">
                                    <tr>
                            </nested:equal>
                                    <td style="vertical-align: middle;">
                                        <nested:equal property="requestLineItem.status.statusCode" value="WFP">
                                            <nested:checkbox property="selected" disabled="false"/>
                                        </nested:equal>
                                        <nested:notEqual property="requestLineItem.status.statusCode" value="WFP">
                                            <nested:notEqual property="requestLineItem.status.statusCode" value="POB">
                                                <nested:checkbox property="selected" disabled="true"/>
                                            </nested:notEqual>
                                            <nested:equal property="requestLineItem.status.statusCode" value="POB">
                                                <nested:checkbox property="selected" disabled="false"/>
                                            </nested:equal>
                                        </nested:notEqual>
                                    </td>
                                    <td class="text-center" style="vertical-align: middle;">
                                        <input style="width:75px" type="SUBMIT" value="<nested:write property='requestLineItem.requestLineItemId'/>"
                                              onclick="this.form.action='viewEditEditorRequest.do';this.form.requestLineItemId.value='<nested:write property='requestLineItem.requestLineItemId'/>'" class="btn btn-default"/>
                                        <!--Added this to give the sorttable.js a requestLineId to sort-->
                                        <span style="visibility:hidden;"><nested:write property="requestLineItem.requestLineItemId" /></span>
                                    </td>
                                    <td>
                                        <nested:write property="requestLineItem.request.trackingNumber"/>
                                    </td>
                                    <td width="28%">
                                        <nested:present property="icnbr">
                                            <nested:write property="icnbr" /><hr style="margin: 5px 0;">
                                        </nested:present>
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
                                    <td class="text-center">
                                        <nested:present property="requestLineItem.fundingSrcSummary">
                                            <nested:write property="requestLineItem.fundingSrcSummary.orgBudgetCodes"/>
                                        </nested:present>
                                    </td>
                                    <td class="text-center">
                                        <nested:present property="requestLineItem.status">
                                           <nested:write property="requestLineItem.status.name"/>
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
                                    <td class="text-center">
                                        <nested:present property="requestLineItem.item">
                                            <nested:write property="requestLineItem.item.itemType"/>
                                        </nested:present>
                                        <nested:notPresent property="requestLineItem.item">Purchase Item</nested:notPresent>
                                    </td>
                                </tr>
                                <bean:write name="requestLineItemSearchForm" property="flip"/>
                        </nested:iterate>
                    </tbody>
                </table>
                <br>
                <div class="col-md-12 text-center">
                    <fieldset>
                        <button onclick="this.form.action='/editor/index.jsp';this.form.submit(); return false;" class="btn btn-default">Cancel</button>
                    </fieldset>
                </div>
            </nested:notEmpty>
        </html:form>
    </body>
</html>