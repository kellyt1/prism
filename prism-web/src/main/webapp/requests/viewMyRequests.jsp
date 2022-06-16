<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ page import="us.mn.state.health.matmgmt.util.Form" %>
<%@ page import="us.mn.state.health.view.inventory.Command" %>

<%
    String showDetails;
    String hideDetails;
    int requestFormIndex = 0;
%>

<html>
    <head>
        <title>View My Requests</title>
    </head>
    <body>
        <form action="searchMyRequests.do" method="POST" accept-charset="utf-8">
            <div class="row">
                <div class="col-md-4 col-md-offset-4">
                    <label for="keywords">Keyword Search</label>
                    <div class="input-group">
                        <nested:text name="searchMaterialRequestsForm" property="query" size="50" styleClass="form-control" styleId="keywords"/>
                        <div class="input-group-btn">
                            <button class="btn btn-default" type="submit" id="search" aria-label="Search"><i class="glyphicon glyphicon-search"></i></button>
                        </div>
                    </div>
                    <p class="text-center"><a href="viewAdvancedSearchMyRequests.do">Advanced Search</a></p>
                </div>
            </div>
        </form>
        <br>
        <nested:form action="/viewMyRequests" method="post">
            <nested:hidden property="cmd" value=""/>
            <nested:hidden property="requestFormIndex" value=""/>
            <nested:hidden property="rliId" value=""/>
            <nested:hidden property="paginationDirection" value=""/>
            <input type="HIDDEN" name="requestLineItemId"/>
            <div class="row">
                <div class="col-md-12 text-right">
                    <nested:greaterThan name="searchMaterialRequestsForm" property="firstResult" value="0">
                        <a href='viewMyRequests.do?paginationDirection=<%=Form.PREVIOUS%>'>&lt; Prev</a> &nbsp;
                    </nested:greaterThan>
                    <nested:equal name="searchMaterialRequestsForm" property="displayNextLink" value="true">
                        <a href="viewMyRequests.do?paginationDirection=<%=Form.NEXT%>">Next &gt;</a>
                    </nested:equal>
                </div>
            </div><br/>
            <table class="table table-bordered table-striped">
                <caption>My Requests</caption>
                <thead>
                    <tr>
                        <th scope="col" class="col-md-1">&nbsp;</th>
                        <th scope="col" class="col-md-2">Deliver To:</th>
                        <th scope="col" class="col-md-1">Request #:</th>
                        <th scope="col" class="col-md-1">Date Requested:</th>
                        <th scope="col" class="col-md-1">Requested By:</th>
                        <th scope="col" class="col-md-1">Need-by Date:</th>
                        <th scope="col" class="col-md-1">Priority:</th>
                        <th scope="col" class="col-md-3">Addt'l Instructions:</th>
                    </tr>
                </thead>
                <nested:iterate id="materialsRequest" name="searchMaterialRequestsForm" property="requestForms" indexId="a">
                    <%
                        showDetails = "this.form.cmd.value='" + Command.HIDE_DETAIL
                                + "'; this.form.requestFormIndex.value='" + requestFormIndex + "';"
                                + "this.form.action='/viewMyRequests.do#" + a + "'; this.form.submit(); return false;";
                        hideDetails = "this.form.cmd.value='" + Command.SHOW_DETAIL
                                + "'; this.form.requestFormIndex.value='" + requestFormIndex + "';" +
                                "" + "this.form.action='/viewMyRequests.do#" + a + "';  this.form.submit(); return false;";
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
                        <td><nested:write property="trackingNumber"/></td>
                        <td><nested:write property="dateRequested" format="MM/dd/yyyy"/></td>
                        <td>
                            <nested:present property="requestor">
                                <nested:write property="requestor.lastAndFirstName"/>
                            </nested:present>
                        </td>
                        <td><nested:write property="needByDate" format="MM/dd/yyyy"/></td>
                        <td><nested:write property="priority.name"/></td>
                        <td><nested:write property="additionalInstructions"/></td>
                    </tr>
                    <nested:equal property="showDetail" value="true">
                        <tr>
                            <td>&nbsp;</td>
                            <td colspan="7" style="padding: 0;">
                                <nested:iterate id="rliForm" property="requestLineItemForms">

                                <table class="table table-bordered">
                                    <tr>
                                        <th scope="col">&nbsp;</th>
                                        <th scope="col">Item Description</th>
                                        <th scope="col">Item Type</th>
                                        <nested:equal value="IT Services and Contracts" property="category.name">
                                            <th scope="col"> Category</th>
                                        </nested:equal>
                                        <nested:equal value="IT Staff" property="category.name">
                                            <th scope="col"> Category</th>
                                        </nested:equal>
                                        <nested:equal value="WAN/Computing Services" property="category.name">
                                            <th scope="col"> Category</th>
                                        </nested:equal>
                                        <th scope="col">Quantity</th>
                                        <th scope="col">Unit</th>
                                        <th scope="col">Unit Cost</th>
                                        <th scope="col">Estimated Cost</th>
                                        <th scope="col">Status</th>
                                        <th scope="col">Funding Sources</th>
                                    </tr>
                                        <tr>
                                            <td>
                                                <nested:equal property="showNotes" value="false">
                                                    <a href="${pageContext.request.contextPath}/viewMyRequests.do?requestFormIndex=<%=requestFormIndex%>&cmd=<%=Command.SHOW_NOTES%>&rliId=<nested:write property='requestLineItemId'/>#<bean:write name='a'/>">View
                                                        Notes</a>
                                                </nested:equal>
                                                <nested:equal property="showNotes" value="true">
                                                    <a href="${pageContext.request.contextPath}/viewMyRequests.do?requestFormIndex=<%=requestFormIndex%>&cmd=<%=Command.HIDE_NOTES%>&rliId=<nested:write property='requestLineItemId'/>#<bean:write name='a'/>">Hide
                                                        Notes</a>
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
                                                    <nested:write property="itemDescription"/>
                                                    <nested:notEmpty property="swiftItemId">
                                                        <br>(SWIFT Item ID#/AC2/Account# - <nested:write property="swiftItemId"/>)
                                                    </nested:notEmpty>
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
                                                    <a href=""
                                                       onclick="window.open('/showRequestLineItemDetails.do?requestLineItemId=<nested:write property='requestLineItemId'/>', '','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, HEIGHT=300, WIDTH=450, left=50, top=50'); return false;">
                                                        <span style="color: blue; ">(Purchasing Info)</span>
                                                    </a><br/>
                                                </nested:equal>
                                                <c:if test="${rliForm.statusCode =='WFA' || rliForm.statusCode=='DEN'}">
                                                    <%--<nested:equal value="<%=Status.WAITING_FOR_APPROVAL%>" property="statusCode">--%>
                                                    <br/>
                                                    <button onclick="window.open('/viewApprovalStatus?requestLineItemId=<nested:write property='requestLineItemId'/>&trackingNumber=<nested:write property="request.trackingNumber"/>&showIndicator=true', '','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, HEIGHT=600, WIDTH=1000, left=50, top=50'); return false;" class="btn btn-default">View Current Approval Status</button>
                                                    <br/>
                                                    <%--</nested:equal>--%>
                                                </c:if>
                                            </td>
                                            <td>
                                                <nested:present property="item">
                                                    <nested:write property="item.itemType"/>
                                                </nested:present>
                                                <nested:notPresent property="item">
                                                    Non-Catalog
                                                </nested:notPresent>
                                            </td>
                                            <nested:equal property="category.name" value="IT Services and Contracts">
                                                <td>
                                                    <nested:write property="category.name"/>
                                                </td>
                                            </nested:equal>
                                            <nested:equal property="category.name" value="IT Staff">
                                                <td>
                                                    <nested:write property="category.name"/>
                                                </td>
                                            </nested:equal>
                                            <nested:equal property="category.name" value="WAN/Computing Services">
                                                <td>
                                                    <nested:write property="category.name"/>
                                                </td>
                                            </nested:equal>
                                            <td>
                                                <nested:write property="quantity"/>
                                            </td>
                                            <td>
                                                <nested:present property="item">
                                                    <nested:present property="item.dispenseUnit">
                                                        <nested:write property="item.dispenseUnit.name"/>
                                                    </nested:present>
                                                </nested:present>
                                            </td>
                                            <td>
                                                <nested:present property="item">
                                                    <%--<nested:write property="item.dispenseUnitCost"/>--%>
                                                    <nested:write property="itemCost" format="#,##0.00"/>
                                                </nested:present>
                                            </td>
                                            <td>
                                                <nested:present property="item">
                                                    <nested:write property="estimatedCostD" format="#,##0.00"/>
                                                </nested:present>
                                                <nested:notPresent property="item">
                                                    <nested:write property="estimatedCost" format="#,##0.00"/>
                                                </nested:notPresent>
                                            </td>
                                            <td>
                                                <nested:write property="statusName"/>
                                            </td>
                                            <!-- funding sources -->
                                            <c:choose>
                                                <c:when test="${not empty rliForm.fundingSourceForms}">
                                                    <td>
                                                        <table>
                                                            <tr>
                                                                <td style="padding: 0;">
                                                                    <table class="table">
                                                                        <tr>
                                                                            <td><bean:message key="orgBudget"/>:</td>
                                                                            <td>Amount:</td>
                                                                        </tr>
                                                                        <nested:iterate property="fundingSourceForms" id="requestLineItemFundingSourceForm">
                                                                            <tr>
                                                                                <td>
                                                                                    <nested:select disabled="true" property="orgBudgetId" styleClass="chosen-select" styleId="bdgt">
                                                                                        <option value=""></option>
                                                                                        <nested:optionsCollection property="orgBudgets" label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
                                                                                    </nested:select>
                                                                                </td>
                                                                                <td><nested:write property="amount"/></td>
                                                                            </tr>
                                                                        </nested:iterate>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td>No funding source provided</td>
                                                </c:otherwise>
                                            </c:choose>
                                            <!-- end of funding sources -->
                                                <%--<% String status = ((RequestLineItemForm) rliForm).getStatusCode();--%>
                                                <%--if (status.equals(Status.WAITING_FOR_APPROVAL)--%>
                                                <%--|| status.equals(Status.WAITING_FOR_DISPERSAL)--%>
                                                <%--|| status.equals(Status.WAITING_FOR_PURCHASE)) {%>--%>
                                            <td class="text-center" style="vertical-align: middle;">
                                                <c:if test="${rliForm.statusCode =='WFA' || rliForm.statusCode =='WFD' || rliForm.statusCode =='WFP'}">
                                                    <button onclick="this.form.action='/viewMyRequests.do#<bean:write name='a'/>';
                                                            this.form.cmd.value='<%=Command.CLOSE_RLI%>';
                                                            this.form.rliId.value='<nested:write property='requestLineItemId' />';
                                                            this.form.requestFormIndex.value=<%=requestFormIndex%>;
                                                            if ( window.confirm('Are you sure you want to cancel?')) {
                                                            this.form.submit(); return false;}" class="btn btn-danger">Cancel</button>
                                                </c:if>
                                                <c:if test="${rliForm.statusCode =='PMI' || rliForm.statusCode =='DEN'}">
                                                    <br>
                                                    <button type="SUBMIT" class="btn btn-default" onclick="this.form.action='viewEditRequestLineItem.do';this.form.requestLineItemId.value='<nested:write property='requestLineItemId'/>'">Edit</button>
                                                </c:if>
                                            </td>
                                                <%--<%}%>--%>
                                        </tr>
                                        <nested:equal property="showNotes" value="true">
                                            <tr>
                                                <td colspan="10" style="padding: 0;">
                                                    <table class="table">
                                                        <tr>
                                                            <th scope="col">Note</th>
                                                            <th scope="col">Author</th>
                                                            <th scope="col">Date</th>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                <!--<textarea name="text" cols="50" rows="1"></textarea>-->
                                                                <nested:textarea property="textNote" cols="50" rows="1"/>
                                                            </td>
                                                            <td>
                                                                <button onclick="this.form.action='/viewMyRequests.do#<bean:write name='a'/>';
                                                                        this.form.cmd.value='<%=Command.ADD_NOTE%>';
                                                                        this.form.rliId.value='<nested:write property='requestLineItemId' />';
                                                                        this.form.requestFormIndex.value=<%=requestFormIndex%> ;
                                                                        this.form.submit(); return false;" class="btn btn-default">Save Note</button>
                                                            </td>
                                                            <td>&nbsp;</td>
                                                        </tr>
                                                        <nested:iterate id="noteForm" name="rliForm" property="noteForms">
                                                            <tr>
                                                                <td><nested:write property="noteText"/></td>
                                                                <td><nested:write property="authorName"/></td>
                                                                <td><nested:write property="insertionDate"/></td>
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
            </table><br/>
            <div class="row">
                <div class="col-md-12 text-right">
                    <nested:greaterThan name="searchMaterialRequestsForm" property="firstResult" value="0">
                        <a href='viewMyRequests.do?paginationDirection=<%=Form.PREVIOUS%>'>&lt; Prev</a> &nbsp;
                    </nested:greaterThan>
                    <nested:equal name="searchMaterialRequestsForm" property="displayNextLink" value="true">
                        <a href="viewMyRequests.do?paginationDirection=<%=Form.NEXT%>">Next &gt;</a>
                    </nested:equal>
                </div>
            </div>
        </nested:form>
    </body>
</html>