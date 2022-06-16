<%@ include file="/./include/tlds.jsp" %>

<jsp:useBean id="purchaseItemTransactionsForm"
             class="us.mn.state.health.view.materialsrequest.reports.PurchaseItemTransactionsForm"
             scope="session"/>
<html>
    <head>
        <title>Search Purchase Item Transactions</title>
    </head>
    <body>
    <html:form action="/displaypurchaseitemtransactions">
        <table width="380" align="center">
            <th colspan="6">Search Purchase Item Transactions</th>
            <tr>
                <td>
                    <label>Org Budget #:<br/>&nbsp;</label>
                    <html:text property="orgBdgtCode" size="8" maxlength="8"/>
                </td>
                <td>
                    <label>Category:<br/>&nbsp;</label>
                    <nested:select property="categoryId">
                        <option label="All"></option>
                        <nested:optionsCollection property="categories" label="name" value="categoryId"/>
                    </nested:select>
                </td>
            </tr>
            <tr>
                <td>
                    <label title="Hold control or shift to select more than 1 year">Fiscal Year:<br/>(yyyy)</label>
                    <nested:select property="fiscalYear" multiple="true" size="3">
                        <nested:optionsCollection property="fiscalyears" label="fiscalyear" value="fiscalyear"/>
                    </nested:select>
                </td>
                <td>
                    <label>Date From: <br/>&nbsp;</label>
                    <nested:text property="dateFrom" size="10" maxlength="10" styleClass="datePicker"/>
                </td>
                <td>
                    <label>Date To:<br/>&nbsp;</label>
                    <nested:text property="dateTo" size="10" maxlength="10" styleClass="datePicker"/>
                </td>
            </tr>
            <tr>
                <td colspan="4" align="center">
                    <html:submit/>
                    <input type="button" onclick="this.form.action = 'index.jsp';this.form.submit(); return false;" value="Cancel"/>
                </td>
            </tr>
        </table>
    </html:form>

    <logic:notEmpty name="purchaseItemTransactionsForm" property="orderedPurchaseItemTransactions">
        <!--use pagelinksClass attribute for oc4j-->
        <ajax:displayTag id="displayTagFrame" ajaxFlag="displayAjax" pagelinksClass="pagelinks">
            <% request.setAttribute("outs", purchaseItemTransactionsForm.getOrderedPurchaseItemTransactions());%>
            <display:table name="outs" class="displaytag" export="true" id="row" excludedParams="ajax" pagesize="10">
                <display:column title="row#"><c:out value="${row_rowNum}"/></display:column>
                <display:caption><strong>Purchase Item Transactions</strong></display:caption>
                <display:caption media="excel"><strong>Purchase Item Transactions</strong></display:caption>
                <display:column property="trackingNumber" title="Request Tracking #" sortable="true" maxLength="10" headerClass="sortable"/>
                <display:column property="requestLineItemId" title="LineItemId" sortable="true" headerClass="sortable"/>
                <display:column property="itemDescription" title="Description" sortable="true" headerClass="sortable" maxLength="20"/>
                <display:column property="buyUnit" title="Unit" sortable="true" headerClass="sortable"/>
                <display:column property="categoryName" title="Category" sortable="true" headerClass="sortable" maxLength="20"/>
                <display:column property="estAmountPaid" title="Est Amt" sortable="true" headerClass="sortable" format="$ {0,number,#,###.##}"/>
                <display:column property="orgCode" title="Org." sortable="true" headerClass="sortable"/>
                <display:column property="orgCodeDisplay" title="Alt." sortable="true" headerClass="sortable"/>
                <display:column property="dateRequested" title="Date" sortable="true" headerClass="sortable"/>
                <display:column property="qtyRequested" title="Qty Req." sortable="true" headerClass="sortable"/>
                <display:column property="qtyOrdered" title="Qty Ord." sortable="true" headerClass="sortable"/>
                <display:column property="buyUnitCost" title="Unit Cost" sortable="true" headerClass="sortable" format="$ {0,number,#,###.##}"/>
                <display:column property="totalAmountPaid" title="Total Cost" sortable="true" headerClass="sortable" format="$ {0,number,#,###.##}"/>
                <display:column property="vendorName" title="Vendor" sortable="true" headerClass="sortable" maxLength="20"/>
                <display:column property="statusName" title="Status" sortable="true" headerClass="sortable" media="excel"/>
                <display:column title="Status" sortable="true" headerClass="sortable" media="html">
                    <a href=""
                       onclick="window.open('/showRequestLineItemDetails.do?requestLineItemId=<c:out value="${row.requestLineItemId}"/>', '','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, HEIGHT=300, WIDTH=450, left=50, top=50'); return false;">
                        <span style="color: blue; "><c:out value="${row.statusName}"/></span>
                    </a><br/>
                </display:column>

                <display:column property="requestorLastFirstName" title="Requestor" sortable="true" headerClass="sortable" media="excel"/>
                <display:footer media="html">
                    <tr>
                        <td colspan="6"><strong>Total:</strong></td>
                        <td>
                            <strong>
                                $<nested:write name="purchaseItemTransactionsForm" property="estimatedTotalAmount" format="#,###.##"/>
                            </strong>
                        </td>
                        <td colspan="7">
                            <strong>
                                $<nested:write name="purchaseItemTransactionsForm" property="total" format="#,###.##"/>
                            </strong>
                        </td>
                    </tr>
                </display:footer>
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
    <logic:empty name="purchaseItemTransactionsForm" property="orderedPurchaseItemTransactions">
        <strong>Purchase Item Transactions</strong>
        <ajax:displayTag id="displayTagFrame" ajaxFlag="displayAjax">
            <% request.setAttribute("outs", purchaseItemTransactionsForm.getOrderedPurchaseItemTransactions());%>
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