<%@ include file="/./include/tlds.jsp" %>

<jsp:useBean id="searchMaterialRequestsForm"
             class="us.mn.state.health.view.materialsrequest.SearchMaterialRequestsForm"
             scope="session"/>
<html>
    <head>
        <title>Search MRQ detail</title>
    </head>
    <body>
    <html:form action="/displayMRQInformation">
        <table align="center" cellspacing="15" cellpadding="15" border="0">
            <tr>
<td>                    <label>Tracking #:</label></td>
                <td>

                    MRQ-<html:text name="searchMaterialRequestsForm" property="requestTrackingNumber" size="12" maxlength="12"/>
                </td>
             </tr>
             <tr>
                 <td><label>Status:</label></td>
                <td>

                    <nested:select  property="statusCode">
                        <option value="">Any</option>
                        <nested:optionsCollection   property="statuses" value="statusCode" label="name"/>
                    </nested:select>
                </td>
            </tr>
            <tr>

                    <td><label>Requestor</label></td>
                <td>
                    <nested:select property="requestorId">
                        <option value=""></option>
                        <nested:optionsCollection property="requestors" label="lastAndFirstName" value="personId"/>
                    </nested:select>

                </td>
            </tr>
             <tr>
                <td>
                    <label>Date From:</label>
                    <nested:text property="dateRequestedForm" size="10" maxlength="10" styleClass="datePicker"/>
                </td>
                <td>
                    <label>Date To</label>
                    <nested:text property="dateRequestedTo" size="10" maxlength="10" styleClass="datePicker"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <hr width="10"/>
                    <html:submit/>
                    <input type="button" onclick="this.form.action = 'index.jsp';this.form.submit(); return false;" value="Cancel"/>
                </td>
            </tr>
        </table>
    </html:form>

    <logic:notEmpty name="searchMaterialRequestsForm" property="requestForms">
        <!--use pagelinksClass attribute for oc4j-->
        <ajax:displayTag id="displayTagFrame" ajaxFlag="displayAjax" pagelinksClass="pagelinks">
            <% request.setAttribute("outs", searchMaterialRequestsForm.getRequestForms());%>
            <display:table name="outs" class="displaytag" export="true" id="row" excludedParams="ajax" pagesize="10" cellpadding="5" cellspacing="5">
                <display:column title="row#___"><c:out value="${row_rowNum}"/></display:column>
                <display:caption media="excel"><strong>Request(s)</strong></display:caption>
                <display:column property="trackingNumber" title="Request Tracking #_______________________" sortable="true" maxLength="10" headerClass="sortable"/>
                <display:column property="requestor.firstAndLastName" title="Requestor____________________" sortable="true" headerClass="sortable"/>
                <display:column property="dateRequested" title="Date Requested_____________________" sortable="true" headerClass="sortable"/>
                <%--<display:column property="evaluatorsEvaluationStatus" title="Status" sortable="true" headerClass="sortable" media="excel"/>--%>
                <%--<display:column property="requestLineItemId" title="LineItemId" sortable="true" headerClass="sortable"/>--%>
                <%--<display:column property="itemDescription" title="Description" sortable="true" headerClass="sortable" maxLength="20"/>--%>
                <%--<display:column property="buyUnit" title="Unit" sortable="true" headerClass="sortable"/>--%>
                <%--<display:column property="categoryName" title="Category" sortable="true" headerClass="sortable" maxLength="20"/>--%>
                <%--<display:column property="estAmountPaid" title="Est Amt" sortable="true" headerClass="sortable" format="$ {0,number,#,###.##}"/>--%>
                <%--<display:column property="orgCode" title="Org." sortable="true" headerClass="sortable"/>--%>
                <%--<display:column property="orgCodeDisplay" title="Alt." sortable="true" headerClass="sortable"/>--%>

                <%--<display:column property="qtyRequested" title="Qty Req." sortable="true" headerClass="sortable"/>--%>
                <%--<display:column property="qtyOrdered" title="Qty Ord." sortable="true" headerClass="sortable"/>--%>
                <%--<display:column property="buyUnitCost" title="Unit Cost" sortable="true" headerClass="sortable" format="$ {0,number,#,###.##}"/>--%>
                <%--<display:column property="totalAmountPaid" title="Total Cost" sortable="true" headerClass="sortable" format="$ {0,number,#,###.##}"/>--%>
                <%--<display:column property="vendorName" title="Vendor" sortable="true" headerClass="sortable" maxLength="20"/>--%>

                <display:column title="Details" sortable="false" headerClass="sortable" media="html">
                    <a href=""
                       onclick="window.open('/viewSelectedRequest.do?requestId=<c:out value="${row.requestId}"/>', '','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, HEIGHT=600, WIDTH=1200, left=50, top=50'); return false;">
                        <span style="color: blue; ">View Details</span>
                    </a><br/>
                </display:column>

                <%--<display:column property="requestor.firstAndLastName" title="Requestor" sortable="true" headerClass="sortable" media="excel"/>--%>
                <%--<display:footer media="html">--%>
                    <%--<tr>--%>
                        <%--<td colspan="6"><strong>Total:</strong></td>--%>
                        <%--<td>--%>
                            <%--<strong>--%>
                                <%--$<nested:write name="purchaseItemTransactionsForm" property="estimatedTotalAmount" format="#,###.##"/>--%>
                            <%--</strong>--%>
                        <%--</td>--%>
                        <%--<td colspan="7">--%>
                            <%--<strong>--%>
                                <%--$<nested:write name="purchaseItemTransactionsForm" property="total" format="#,###.##"/>--%>
                            <%--</strong>--%>
                        <%--</td>--%>
                    <%--</tr>--%>
                <%--</display:footer>--%>
                <display:setProperty name="export.pdf" value="false"/>
                <display:setProperty name="export.rtf" value="false"/>
                <display:setProperty name="export.pdf.filename" value="example.pdf"/>
                <display:setProperty name="export.excel.filename" value="example.xls"/>
                <display:setProperty name="export.rtf.filename" value="example.rtf"/>
                <display:setProperty name="export.xml" value="false"/>
                <display:setProperty name="export.csv" value="false"/>
            </display:table>
        </ajax:displayTag>

    </logic:notEmpty>
    <logic:empty name="searchMaterialRequestsForm" property="requestForms">
        <strong>Request(s)</strong>
        <ajax:displayTag id="displayTagFrame" ajaxFlag="displayAjax">
            <% request.setAttribute("outs", searchMaterialRequestsForm.getRequestForms());%>
            <display:table name="outs" class="displaytag" export="true" id="row" excludedParams="ajax" pagesize="10">
                <display:column title="row#">
                    <c:out value="${row_rowNum}"/>
                </display:column>
            </display:table>
        </ajax:displayTag>
    </logic:empty>
    </body>
    <IFRAME id="Defib" src="../../include/Defibrillator.jsp" frameBorder=no width=0 height=0></IFRAME>
</html>