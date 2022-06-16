<%@ include file="../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.model.common.Status" %>
<%@ page import="us.mn.state.health.view.inventory.Command" %>
<%@ page import="us.mn.state.health.view.materialsrequest.RequestLineItemForm" %>

<%
    String downImgOnClick;
    String rightImgOnClick;

    int requestFormIndex = 0;
%>
<!--- 7/27/07 SueD fixed bug (no 1943) -- Cancelling Line Items --->
<html>
<head>
    <title>View Request</title>
    <link rel="stylesheet" href="../css/main.css" type="text/css"/>

</head>


<body>
<br><br>
<html:errors/>
<nested:form action="/viewSelectedRequest" method="post">
    <nested:hidden property="cmd" value=""/>
    <nested:hidden property="requestFormIndex" value=""/>
    <nested:hidden property="rliId" value=""/>


    <table class="table" align="center" border="1" cellspacing="0" cellpadding="5" bgcolor="#CCCCCC" width="95%">
    <%--<tr class="tableheader">--%>
    <%--<td class="tableheader" colspan="8" align="center">Request Detail:</td>--%>
    <%--</tr>--%>

    <% int i = 0; %>
    <tr>
        <th><nested:write property="trackingNumber"/></th>
    </tr>
    <tr>
        <td></td>
    </tr>
    <nested:iterate id="rliForm" property="requestLineItemForms">
        <tr>
            <th>Item Description</th>
            <td class="tabledetail" align="left" valign="top">
                <nested:present property="item">
                    <nested:write property="item.description"/>
                    <nested:notEmpty property="item.attachedFiles">
                        <% java.util.Date dt = new java.util.Date(); %>
                        <br/>
                        <i class="attachment">attachment(s):
                            <nested:iterate property="item.attachedFiles" id="attachedFile">
                                &nbsp;<a href=${pageContext.request.contextPath}/inventory/downloadFileAction.do?attachedFileId=<nested:write
                                    name="attachedFile" property='attachedFileId'/>&dt=<%=dt.getTime()%> > <nested:write
                                    name="attachedFile" property='fileName'/> </a>
                            </nested:iterate> </i>
                    </nested:notEmpty>


                </nested:present>
                <nested:notPresent property="item">
                    <nested:write property="itemDescription"/>
                </nested:notPresent>
                <nested:notEmpty property="requestLineItem.attachedFileNonCats">
                    <% java.util.Date dt = new java.util.Date(); %>
                    <br/>
                    <i class="attachment">attachment(s):
                        <nested:iterate property="requestLineItem.attachedFileNonCats" id="attachedFile">
                            &nbsp;<a href=${pageContext.request.contextPath}/purchasing/downloadFileAction.do?attachedFileNonCatId=<nested:write
                                name="attachedFile" property='attachedFileNonCatId'/>&dt=<%=dt.getTime()%> >
                            <nested:write name="attachedFile" property='fileName'/> </a>
                        </nested:iterate> </i>
                </nested:notEmpty>
                <nested:equal value="true" property="requestLineItem.isOnOrder">
                    <br/>
                    <a href=""
                       onclick="window.open('/showRequestLineItemDetails.do?requestLineItemId=<nested:write
                               property='requestLineItemId'/>', '','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, HEIGHT=300, WIDTH=450, left=50, top=50'); return false;">
                        <span style="color:blue;">(Purchasing Info)</span>
                    </a><br/>
                </nested:equal>
                <br/>
                <a href=""
                   onclick="window.open('/viewApprovalStatus?requestLineItemId=<nested:write
                           property='requestLineItemId'/>&trackingNumber=<nested:write
                           property="request.trackingNumber"/>&showIndicator=true', '','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, HEIGHT=600, WIDTH=600, left=50, top=50'); return false;">
                    <span style="color:blue;">View Approval Information</span>
                </a><br/>

            </td>

            <th>Item Type</th>
            <td class="tabledetail" align="left" valign="top">
                <nested:present property="item">
                    <nested:write property="item.itemType"/>
                </nested:present>
                <nested:notPresent property="item">
                    Non-Catalog
                </nested:notPresent>
            </td>

            <th>Quantity</th>
            <td class="tabledetail" align="center" valign="top">
                <nested:write property="quantity"/>
            </td>
        </tr>
        <tr>
            <th>Unit</th>
            <td class="tabledetail" align="center" valign="top">
                <nested:present property="item">
                    <nested:present property="item.dispenseUnit">
                        <nested:write property="item.dispenseUnit.name"/>
                    </nested:present>
                </nested:present>
            </td>
            <th>Unit Cost</th>
            <td class="tabledetail" align="center" valign="top">
                <nested:present property="item">
                    <nested:write property="item.dispenseUnitCost"/>
                    <nested:write property="itemCost"/>
                </nested:present>
            </td>
            <th>Estimated Cost</th>
            <td class="tabledetail" align="center" valign="top">
                <nested:present property="item">
                    <nested:write property="estimatedCostD"/>
                </nested:present>
                <nested:notPresent property="item">
                    <nested:write property="estimatedCost" format="#,##0.00"/>
                </nested:notPresent>
            </td>
        </tr>
        <tr>
            <th>Status</th>
            <td class="tabledetail" align="center" valign="top">
                <nested:write property="statusName"/>
            </td>

        </tr>
        <tr>
            <th>PO#</th>
            <td class="tabledetail" align="center" valign="top">
                <nested:present property="requestLineItem.orderLineItem">
                <nested:write property="requestLineItem.orderLineItem.order.purchaseOrderNumber"/>
                </nested:present>
            </td>

        </tr>
        </table>
        <table class="table" align="center" border="0" cellspacing="0" cellpadding="5" bgcolor="#CCCCCC" width="95%">
        <tr>
            <td>
                <!-- funding sources -->
                <c:choose>
                <c:when test="${not empty rliForm.fundingSourceForms}">

            <td colspan="4">
                <table border="0" cellspacing="0">
                    <tr>
                        <td class="tabledetail">
                            <table cellspacing="0" border="0" cellpadding="2" id="fundingSourcesTable_NonCatItem">
                                <tr>
                                    <th>
                                        <bean:message key="orgBudget"/>
                                        Fund/Dept/Name/Approp/Project/Activity/EndDate:
                                    </th>
                                    <td class="tabledetail">&nbsp;</td>
                                    <th>Amount in $:</th>
                                </tr>

                                <nested:iterate property="fundingSourceForms" id="requestLineItemFundingSourceForm">
                                    <tr>

                                        <td align="center">
                                            <nested:present property="orgBudget">
                                                <nested:write property="orgBudget.fundCode"/>/
                                                <nested:write property="orgBudget.deptId"/>/
                                                -<nested:write property="orgBudget.name"/>/
                                                <nested:write property="orgBudget.appropriationCode"/>/
                                                <nested:write property="orgBudget.projectId"/>/
                                                <nested:write property="orgBudget.orgBudgetCode"/>/
                                                <nested:write property="orgBudget.endDate"/>
                                            </nested:present>
                                            <nested:notPresent property="orgBudget">
                                                    Budget is no longer active: ID=
                                                      <nested:write property="orgBudgetId"/>
                                             </nested:notPresent>
                                        </td>
                                        <td class="tabledetail">&nbsp;</td>
                                        <td class="tabledetail" align="center">

                                            <nested:write property="amount"/>
                                        </td>
                                    </tr>
                                </nested:iterate>
                                <tr>
                                    <td colspan="3">
                                        <hr/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <!--
                                              <tr bgcolor="#cccccc">
                                                <td colspan="2" nowrap="nowrap"  class="tabledetail">
                                                    <html:submit value="Add Another Funding Source" onclick="this.form.cmd.value='addFundingSource'; this.form.submit(); return false;" />
                                                </td>
                                              </tr>
                                              -->
                </table>
                </c:when>
                <c:otherwise>
            <td>No funding source provided</td>
            </c:otherwise>
            </c:choose>
            <!-- end of funding sources -->

                    <% String status = ((RequestLineItemForm) rliForm).getStatusCode();
    if (status.equals(Status.WAITING_FOR_APPROVAL)
            || status.equals(Status.WAITING_FOR_DISPERSAL)
            || status.equals(Status.WAITING_FOR_PURCHASE)) {%>
            <td>


            <td>

                <logic:equal name="requestForm" property="isOwnerOfRequest" value="true">
                    <input type="button" value="cancel line"
                           onclick="this.form.action='/viewMyRequests.do#';
                                   this.form.cmd.value='<%=Command.CLOSE_RLI%>';
                                   this.form.rliId.value='<nested:write property='requestLineItemId'/>';
                                   this.form.requestFormIndex.value=<%=requestFormIndex%>;
                                   //this.form.submit();
                                   if ( window.confirm('Are you sure you want to cancel?')) {
                                   this.form.submit(); return false;}
                                   "/>
                </logic:equal>
            </td>


                <%--<!--- 7/26/07 SueD commmended this block --->--%>
                <%---          <input type="button" value="cancel"--%>
                <%--onclick="this.form.action='/viewMyRequests.do#<bean:write name='a'/>';--%>
                <%--this.form.cmd.value='<%=Command.CLOSE_RLI%>';--%>
                <%--this.form.rliId.value='<nested:write property='requestLineItemId' />';--%>
                <%--this.form.requestFormIndex.value=<%=requestFormIndex%>;--%>
                <%--this.form.submit(); return false;" />--%>
                <%--</td>     ---%>

        <tr>
            <td colspan="8">
                <hr width="100%"/>
            </td>
            <%}%>
        </tr>
        <nested:equal property="showNotes" value="true">
            <tr>
                <td class="tabledetail" colspan="9" align="center">
                    <table cellpadding="5" cellspacing="0" width="75%">
                        <tr>
                            <td class="tableshade" align="left">Note</td>
                            <td class="tableshade" align="left">Author</td>
                            <td class="tableshade" align="left">Date</td>
                        </tr>
                        <tr>
                            <td>
                                <!--<textarea name="text" cols="50" rows="1"></textarea>-->
                                <nested:textarea property="textNote" cols="50" rows="1"/>
                            </td>
                            <td align="center">
                                <input type="BUTTON" value="Save Note" onclick="
                                    <%--this.form.action='/viewMyRequests.do#<bean:write name='a'/>';--%>
                                    <%--this.form.cmd.value='<%=Command.ADD_NOTE%>';--%>
                                    <%--this.form.rliId.value='<nested:write property='requestLineItemId' />';--%>
                                    <%--this.form.requestFormIndex.value=<%=requestFormIndex%> ;--%>
                                        this.form.submit(); return false;"/>
                            </td>
                            <td>&nbsp;</td>
                        </tr>
                        <nested:iterate id="noteForm" name="rliForm" property="noteForms">
                            <tr>
                                <td align="left" valign="top">
                                    <nested:write property="noteText"/>
                                </td>
                                <td align="left" valign="top" nowrap="nowrap">
                                    <nested:write property="authorName"/>
                                </td>
                                <td align="left" valign="top" nowrap="nowrap">
                                    <nested:write property="insertionDate"/>
                                </td>
                            </tr>
                        </nested:iterate>
                    </table>
                </td>
            </tr>
        </nested:equal>
    </nested:iterate>
    <tr>
        <td colspan="9">&nbsp;</td>
    </tr>
    </table>
    </td>
    <td>&nbsp;</td>
    </tr>
    </table>
</nested:form>
</body>
</html>