<%@ include file="../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.matmgmt.util.Form"%>
<%@ page import="us.mn.state.health.view.inventory.Command"%>

<%
    String showDetails;
    String hideDetails;
    int requestFormIndex = 0;
%>

<html>
<head>
    <title>Search All Requests</title>
    <script type="text/javascript">
        function clearForm() {
            var form = window.document.forms[0];
            var currentTime = new Date();
            var month = currentTime.getMonth() + 1;
            var day = currentTime.getDate();
            var year = currentTime.getFullYear();
            var currentdate = month + "/" + day + "/" + year;
            var myDate = new Date();
            myDate.setDate(myDate.getDate()-90);
            month = myDate.getMonth() + 1;
            day = myDate.getDate();
            year = myDate.getFullYear();
            var fromDateHold = month + "/" + day + "/" + year;

            //alert("BEGIN");
            form.requestTrackingNumber.value = "";
            form.neededByFrom.value = "";
            form.dateRequestedForm.value = fromDateHold;
            form.icnbr.value = "";
            form.neededByTo.value = "";
            form.dateRequestedTo.value = currentdate;
            form.orgBudgetCode.value = "";
            form.vendorName.value = "";
            form.itemDescription.value = "";
            form.itemModel.value = "";
            form.requestorId.selectedIndex = 0;
            form.statusCode.selectedIndex = 0;
            form.categoryCode.selectedIndex = 0;
            //alert("END");
        }
    </script>
</head>
<body>
<html:form action="/advancedSearchMyRequests" method="post">
    <fieldset>
        <legend>Search Requests</legend>
        <table style="width: 100%;">
            <tr>
                <td><label for="mrq">Request Tracking #: </label>
                    MRQ-<nested:text property="requestTrackingNumber" size="10" styleId="mrq"/>
                </td>
                <td><label>Icnbr: <nested:text property="icnbr" size="8" maxlength="8"/></label></td>
                <td><label><bean:message key="orgBudget"/> Code(s): <nested:text property="orgBudgetCode" size="35" /></label></td>
                <td><label>Item Description: <nested:text property="itemDescription" size="35"/></label></td>
            </tr>
            <tr>
                <td colspan="2">
                    <p style="font-weight: 600;">"Need By" date</p>
                    <label>From: <nested:text property="neededByFrom" size="10" styleClass="dateInput"/></label>
                    <label>To: <nested:text property="neededByTo" size="10" styleClass="dateInput"/></label>
                </td>
                <td><label>Status:
                    <nested:select property="statusCode" styleClass="chosen-select">
                        <option value="">Any</option>
                        <nested:optionsCollection property="statuses" label="name" value="statusCode"/>
                    </nested:select></label>
                </td>
                <td>
                    <label>Item Model: <nested:text property="itemModel" size="35"/></label>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <p style="font-weight: 600;">Requested date</p>
                    <label>From: <nested:text property="dateRequestedForm" size="10" styleClass="dateInput"/></label>
                    <label>To: <nested:text property="dateRequestedTo" size="10" styleClass="dateInput"/></label>
                </td>
                <td><label>Vendor Name: <nested:text property="vendorName" size="35" /></label></td>
                <td>
                    <label>Item Category:
                        <nested:select property="categoryCode" styleClass="chosen-select">
                            <option value="">Any</option>
                            <nested:optionsCollection property="categories" label="name" value="categoryCode"/>
                        </nested:select>
                    </label>
                </td>
            </tr>
        </table><br/>
        <div class="col-md-12 text-center">
            <button type="submit" value="Search" class="btn btn-default">Submit</button>
            <button onclick="clearForm();" class="btn btn-default">Clear</button>
            <button onclick="this.form.action='/advancedSearchMyRequests.do';this.form.submit(); return false;" class="btn btn-default">Cancel</button>
        </div>
    </fieldset>
</html:form><br>
<nested:form action="advancedSearchMyRequests" method="post">
<nested:hidden property="cmd" value=""  />
<nested:hidden property="requestFormIndex" value=""  />
<nested:hidden property="rliId" value=""  />
<nested:notEmpty name="searchMaterialRequestsForm" property="requestForms">
<nested:hidden property="paginationDirection" value=""  />
<nested:hidden property="pageNo"/>
<div class="row">
    <div class="col-xs-6">Your search returned <nested:write property="resultsNo"/> results!</div>
    <div class="col-xs-6 text-right">
        <nested:greaterThan name="searchMaterialRequestsForm" property="firstResult" value="0">
            <a href='advancedSearchMyRequests.do?paginationDirection=<%=Form.PREVIOUS%>'>&lt; Prev</a> &nbsp;
        </nested:greaterThan>
        &nbsp;<label>Page #:
        <select>
            <nested:iterate property="pages" indexId="a">
                <nested:equal value="<%=a.toString()%>" name="searchMaterialRequestsForm" property="pageNo">
                    <option value="<%=a%>" selected=""><%=a+1%></option>
                </nested:equal>
                <nested:notEqual value="<%=a.toString()%>" name="searchMaterialRequestsForm" property="pageNo">
                    <option value="<%=a%>" onclick="this.form.pageNo.value='<%=a.toString()%>';this.form.submit(); return false;">
                        <%=a+1%>
                    </option>
                </nested:notEqual>
            </nested:iterate>
        </select></label>&nbsp;
        <nested:equal name="searchMaterialRequestsForm" property="displayNextLink" value="true">
            <a href="advancedSearchMyRequests.do?paginationDirection=<%=Form.NEXT%>">Next &gt;</a>
        </nested:equal>
    </div>
</div><br/>
<table class="table table-bordered table-striped">
<thead>
<tr>
    <th scope="col"></th>
    <th scope="col">Deliver To:</th>
    <th scope="col">Request #:</th>
    <th scope="col">Date Requested:</th>
    <th scope="col">Requested By:</th>
    <th scope="col">Need-by Date:</th>
    <th scope="col">Priority:</th>
    <th scope="col">Additional Instructions:</th>
</tr>
</thead>
<nested:iterate id="materialsRequest" name="searchMaterialRequestsForm" property="requestForms" indexId="a">
    <%
        showDetails = "this.form.cmd.value='" + Command.HIDE_DETAIL
                + "'; this.form.requestFormIndex.value='" + requestFormIndex + "';"
                + "this.form.action='/advancedSearchMyRequests.do#"+ a + "'; this.form.submit(); return false;";
        hideDetails = "this.form.cmd.value='" + Command.SHOW_DETAIL
                + "'; this.form.requestFormIndex.value='" + requestFormIndex + "';" +
                ""+ "this.form.action='/advancedSearchMyRequests.do#"+ a + "'; this.form.submit(); return false;";
    %>
    <tr>
        <td class="text-center" id="<nested:write name='a'/>" style="vertical-align: middle; font-size: 1.5em;">
            <nested:equal property="showDetail" value="true" >
                <button onclick="<%=showDetails%>" class="btn btn-default glyphicon glyphicon-chevron-down"></button>
            </nested:equal>
            <nested:equal property="showDetail" value="false" >
                <button onclick="<%=hideDetails%>" class="btn btn-default glyphicon glyphicon-chevron-right"></button>
            </nested:equal>
        </td>
        <td>
            <nested:present property="deliveryDetail">
                <nested:present property="deliveryDetail.organization">
                    <nested:write property="deliveryDetail.organization.orgName"/><br>
                </nested:present>
                <nested:notPresent property="deliveryDetail.organization">
                    <nested:present property="deliveryDetail.facility">MDH</nested:present><br>
                </nested:notPresent>
                <nested:write property="deliveryDetail.recipientName"/><br>
                <nested:present property="deliveryDetail.facility">
                    <nested:write property="deliveryDetail.facility.facilityName"/><br>
                </nested:present>
                <nested:present property="deliveryDetail.mailingAddress">
                    <nested:write property="deliveryDetail.mailingAddress.address1"/><br>
                    <nested:present property="deliveryDetail.mailingAddress.address2">
                        <nested:write property="deliveryDetail.mailingAddress.address2"/><br>
                    </nested:present>
                    <nested:write property="deliveryDetail.mailingAddress.city"/>,
                    <nested:write property="deliveryDetail.mailingAddress.state"/>&nbsp;
                    <nested:write property="deliveryDetail.mailingAddress.zip"/>
                </nested:present>
            </nested:present>
        </td>
        <td class="text-center"><nested:write property="trackingNumber"/></td>
        <td class="text-center"><nested:write property="dateRequested" format="MM/dd/yyyy"/></td>
        <td class="text-center">
            <nested:present property="requestor">
                <nested:write property="requestor.lastAndFirstName" />
            </nested:present>
        </td>
        <td class="text-center"><nested:write property="needByDate" format="MM/dd/yyyy" /></td>
        <td class="text-center"><nested:write property="priority.name"/></td>
        <td><nested:write property="additionalInstructions" /></td>
    </tr>
    <nested:equal property="showDetail" value="true">
        <tr id="lines<nested:write name='a'/>">
            <td>&nbsp;</td>
            <td colspan="7" style="padding: 0;">
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th scope="col"></th>
                        <th scope="col">Item Description</th>
                        <th scope="col">Item Type</th>
                        <th scope="col">Quantity</th>
                        <th scope="col">Unit</th>
                        <th scope="col">Unit Cost</th>
                        <th scope="col">Estimated Cost</th>
                        <th scope="col">Status</th>
                        <th scope="col">Funding Sources</th>
                    </tr>
                    </thead>
                    <nested:iterate id="rliForm" property="requestLineItemForms">
                        <tr>
                            <td class="text-center">
                                <nested:equal property="showNotes" value="false">
                                    <a href="${pageContext.request.contextPath}/advancedSearchMyRequests.do?requestFormIndex=<%=requestFormIndex%>&cmd=<%=Command.SHOW_NOTES%>&rliId=<nested:write property='requestLineItemId'/>#<bean:write name='a'/>">View Notes</a>
                                </nested:equal>
                                <nested:equal  property="showNotes" value="true">
                                    <a href="${pageContext.request.contextPath}/advancedSearchMyRequests.do?requestFormIndex=<%=requestFormIndex%>&cmd=<%=Command.HIDE_NOTES%>&rliId=<nested:write property='requestLineItemId'/>#<bean:write name='a'/>">Hide Notes</a>
                                </nested:equal>
                            </td>
                            <td>
                                <nested:present property="item">
                                    <nested:write property="item.description"/>
                                    <nested:equal value="Stock Item" property="item.itemType">
                                        <br>(IC# - <nested:write property="item.fullIcnbr"/>)
                                    </nested:equal>
                                    <nested:notEmpty property="item.attachedFiles">
                                        <% java.util.Date dt = new java.util.Date (); %>
                                        <br/>
                                        <i class="attachment">attachment(s):
                                            <nested:iterate property="item.attachedFiles" id="attachedFile"  >
                                                &nbsp;<a href=${pageContext.request.contextPath}/inventory/downloadFileAction.do?attachedFileId=<nested:write name="attachedFile" property='attachedFileId'/>&dt=<%=dt.getTime()%> > <nested:write name="attachedFile" property='fileName'/> </a>
                                            </nested:iterate> </i>
                                    </nested:notEmpty>
                                </nested:present>
                                <nested:notPresent property="item">
                                    <nested:write property="itemDescription" />
                                </nested:notPresent>

                                <nested:notEmpty property="requestLineItem.attachedFileNonCats">
                                    <% java.util.Date dt = new java.util.Date (); %>
                                    <br/>
                                    <i class="attachment">attachment(s):
                                        <nested:iterate property="requestLineItem.attachedFileNonCats" id="attachedFile"  >
                                            &nbsp;<a href=${pageContext.request.contextPath}/purchasing/downloadFileAction.do?attachedFileNonCatId=<nested:write name="attachedFile" property='attachedFileNonCatId'/>&dt=<%=dt.getTime()%> > <nested:write name="attachedFile" property='fileName'/> </a>
                                        </nested:iterate> </i>
                                </nested:notEmpty>

                                <nested:equal value="true" property="requestLineItem.isOnOrder">
                                    <br/>
                                    <a href="" onclick="window.open('/showRequestLineItemDetails.do?requestLineItemId=<nested:write property='requestLineItemId'/>', '','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, HEIGHT=300, WIDTH=450, left=50, top=50'); return false;">
                                        <span style="color:blue;">(Purchasing Info)</span>
                                    </a><br>
                                </nested:equal>

                                    <%--<c:if test="${rliForm.statusCode =='<%=Status.WAITING_FOR_APPROVAL%>' || rliForm.statusCode=='<%=Status.DENIED%>'}">--%>
                                <c:if test="${rliForm.statusCode =='WFA' || rliForm.statusCode=='DEN'}">
                                    <%--<nested:equal value="<%=Status.WAITING_FOR_APPROVAL%>" property="statusCode">--%>
                                    <br>
                                    <button onclick="window.open('/viewApprovalStatus?requestLineItemId=<nested:write property='requestLineItemId'/>&trackingNumber=<nested:write property="request.trackingNumber"/>&showIndicator=true', '','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, HEIGHT=600, WIDTH=1000, left=50, top=50'); return false;" class="btn btn-default">View Current Approval Status</button>
                                    <br>
                                    <%--</nested:equal>--%>
                                </c:if>
                            </td>
                            <td>
                                <nested:present property="item"><nested:write property="item.itemType"/></nested:present>
                                <nested:notPresent property="item">Non-Catalog</nested:notPresent>
                            </td>
                            <td class="text-center"><nested:write property="quantity"/></td>
                            <td class="text-center">
                                <nested:present property="item">
                                    <nested:present property="item.dispenseUnit">
                                        <nested:write property="item.dispenseUnit.name"/>
                                    </nested:present>
                                </nested:present>
                            </td>
                            <td class="text-center">
                                <nested:present property="item">
                                    <nested:write property="itemCost" format="#,##0.00"/>
                                    <%--<nested:write property="item.dispenseUnitCost"/>--%>
                                </nested:present>
                            </td>
                            <td class="text-center">
                                    <%--<nested:write property="estimatedCost"/>--%>
                                <nested:present property="item">
                                    <nested:write property="estimatedCostD" format="#,##0.00"/>
                                </nested:present>
                                <nested:notPresent property="item">
                                    <nested:write property="estimatedCost" format="#,##0.00"/>
                                </nested:notPresent>
                            </td>
                            <td class="text-center"><nested:write property="statusName"/></td>
                            <td style="padding: 0;">
                                <table class="table">
                                    <tr>
                                        <th scope="col"><bean:message key="orgBudget"/></th>
                                        <th scope="col">Amount</th>
                                    </tr>
                                    <nested:iterate property="fundingSourceForms" id="requestLineItemFundingSourceForm">
                                        <tr>
                                            <td>
                                                <nested:select property="orgBudgetId" disabled="true" styleClass="chosen-select">
                                                    <option value=""></option>
                                                    <nested:optionsCollection property="orgBudgets" label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
                                                </nested:select>
                                            </td>
                                            <td><nested:text property="amount" disabled="true" size="5"/></td>
                                        </tr>
                                    </nested:iterate>
                                </table>
                            </td>
                        </tr>
                        <nested:equal property="showNotes" value="true">
                            <tr>
                                <td colspan="9" style="padding: 0;">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th scope="col">Note</th>
                                            <th scope="col">Author</th>
                                            <th scope="col">Date</th>
                                        </tr>
                                        </thead>
                                        <nested:iterate id="noteForm" name="rliForm" property="noteForms">
                                            <tr>
                                                <td><nested:write property="noteText" /></td>
                                                <td><nested:write property="authorName" /></td>
                                                <td><nested:write property="insertionDate" /></td>
                                            </tr>
                                        </nested:iterate>
                                    </table>
                                </td>
                            </tr>
                        </nested:equal>
                    </nested:iterate>
                </table>
            </td>
        </tr>
    </nested:equal>
    <% ++requestFormIndex; %>
</nested:iterate>
</table>
<div class="row">
    <div class="col-xs-6">Your search returned <nested:write property="resultsNo"/> results!</div>
    <div class="col-xs-6 text-right">
        <nested:greaterThan name="searchMaterialRequestsForm" property="firstResult" value="0">
            <a href='advancedSearchMyRequests.do?paginationDirection=<%=Form.PREVIOUS%>'>&lt; Prev</a> &nbsp;
        </nested:greaterThan>
        &nbsp;<label>Page #:
        <select>
            <nested:iterate property="pages" indexId="a">
                <nested:equal value="<%=a.toString()%>" name="searchMaterialRequestsForm" property="pageNo">
                    <option value="<%=a%>" selected=""><%=a+1%></option>
                </nested:equal>
                <nested:notEqual value="<%=a.toString()%>" name="searchMaterialRequestsForm" property="pageNo">
                    <option value="<%=a%>" onclick="this.form.pageNo.value='<%=a.toString()%>';this.form.submit(); return false;">
                        <%=a+1%>
                    </option>
                </nested:notEqual>
            </nested:iterate>
        </select></label>&nbsp;
        <nested:equal name="searchMaterialRequestsForm" property="displayNextLink" value="true">
            <a href="advancedSearchMyRequests.do?paginationDirection=<%=Form.NEXT%>">Next &gt;</a>
        </nested:equal>
    </div>
</div>
</nested:notEmpty><br>
</nested:form>
</body>
</html>