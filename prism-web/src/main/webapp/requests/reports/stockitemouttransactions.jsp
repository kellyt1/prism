<%@ include file="../../include/tlds.jsp" %>

<jsp:useBean id="stockItemOutTransactionsForm"
             class="us.mn.state.health.view.materialsrequest.reports.StockItemTransactionsForm"
             scope="session"/>
<html>
    <head>
        <title>Search Stock Item Transactions</title>
    </head>
    <body>
    <h1 class="text-center" style="color: #f00;">Information in this report is not maintained after October 23, 2014</h1>
    <html:form action="/displaystockitemouttransactions">
        <fieldset>
            <legend>Search Stock Item OUT Transactions</legend>
            <div class="text-center">
                <label>Org Budget #: <html:text property="orgBdgtCode" size="6" maxlength="8"/></label>&nbsp;
                <label>ICNBR: <html:text property="icnbr" size="8" maxlength="10"/></label>&nbsp;
                <label>
                    Category:&nbsp;
                    <nested:select property="categoryId">
                        <option label="All"></option>
                        <nested:optionsCollection property="categories" label="name" value="categoryId"/>
                    </nested:select>
                </label><br/>
                <label>Fiscal Year:
                    <nested:select property="fiscalYear" multiple="true" styleClass="chosen-select" style="width: 250px;">
                        <nested:optionsCollection property="fiscalyears" label="fiscalyear" value="fiscalyear"/>
                    </nested:select>
                </label><br/>
                <label>Date From: <nested:text property="dateFrom" size="10" maxlength="10" styleClass="dateInput"/></label>&nbsp;
                <label>Date To: <nested:text property="dateTo" size="10" maxlength="10" styleClass="dateInput"/></label>
            </div>
            <div class="text-center">
                <div class="btn-group">
                    <html:submit styleClass="btn btn-default"/>
                    <button onclick="this.form.action = 'index.jsp';this.form.submit(); return false;" class="btn btn-default">Cancel</button>
                </div>
            </div>
        </fieldset>
    </html:form>
    <html:errors/>
    <logic:notEmpty name="stockItemOutTransactionsForm" property="stockItemTransactions">
        <!--use pagelinksClass attribute for oc4j-->
        <ajax:displayTag id="displayTagFrame" ajaxFlag="displayAjax" pagelinksClass="pagelinks">
            <% request.setAttribute("outs", stockItemOutTransactionsForm.getStockItemTransactions());%>
            <display:table name="outs" class="displaytag" export="true" id="row" excludedParams="ajax" pagesize="10">
                <display:column title="row#"><c:out value="${row_rowNum}"/></display:column>
                <display:caption><strong>Stock Item Out Transactions</strong></display:caption>
                <display:caption media="excel"><strong>Stock Item Out Transactions</strong></display:caption>
                <display:column property="trackingNumber" title="Request Tracking #" sortable="true" maxLength="10" headerClass="sortable"/>
                <display:column property="requestLineItemId" title="LineItemId" sortable="true" headerClass="sortable"/>
                <display:column property="fullIcnbr" title="ICNBR" sortable="true" headerClass="sortable"/>
                <display:column property="itemDescription" title="Description" sortable="true" headerClass="sortable" maxLength="20"/>
                <display:column property="dispenseUnit" title="Unit" sortable="true" headerClass="sortable"/>
                <display:column property="dispUnitCost" title="Dispense Unit Cost" sortable="true" headerClass="sortable" format="$ {0,number,#,###.##}"/>
                <display:column property="dateRequested" title="Date" sortable="true" headerClass="sortable"/>
                <display:column property="qtyRequested" title="Qty Requested" sortable="true" headerClass="sortable"/>
                <display:column property="qtyFilled" title="Qty Filled" sortable="true" headerClass="sortable"/>
                <display:column property="orgCode" title="Org Bdgt" sortable="true" headerClass="sortable"/>
                <display:column property="orgCodeDisplay" title="Alt Org" sortable="true" headerClass="sortable"/>
                <display:column property="amountPaid" title="Amount Paid" sortable="true" headerClass="sortable" format="$ {0,number,#,###.##}"/>
                <display:column property="totalAmount" title="Total Amount" sortable="true" headerClass="sortable" format="$ {0,number,#,###.##}"/>
                <display:column property="requestorLastFirstName" title="Requestor" sortable="true" headerClass="sortable"/>
                <display:footer media="html">
                    <tr>
                        <td colspan="8"><strong>Total:</strong></td>
                        <td>
                            <strong>
                                <nested:write name="stockItemOutTransactionsForm" property="totalRequested"/>
                            </strong>
                        </td>
                        <td colspan="3">
                            <strong>
                                <nested:write name="stockItemOutTransactionsForm" property="totalFilled"/>
                            </strong>
                        </td>
                        <td colspan="3">
                            <strong>
                                $ <nested:write name="stockItemOutTransactionsForm" property="totalAmountPaid" format="#,###.##"/>
                            </strong>
                        </td>
                    </tr>
                </display:footer>
                <display:column property="recipientName" title="Recipient" media="excel"/>
                <display:column property="orgName" title="Organization" media="excel"/>
                <display:column property="address" title="Mailing Address" media="excel"/>
                <display:setProperty name="export.pdf" value="false"/>
                <display:setProperty name="export.rtf" value="false"/>
                <display:setProperty name="export.pdf.filename" value="example.pdf"/>
                <display:setProperty name="export.excel.filename" value="example.xls"/>
                <display:setProperty name="export.rtf.filename" value="example.rtf"/>
                <display:setProperty name="export.xml" value="false"/>
                <display:setProperty name="export.csv" value="false"/>
            </display:table>
        </ajax:displayTag>
        <p>*<i><font size="2">the excel report has the "Deliver To" information</font></i></p>
    </logic:notEmpty>
    <logic:empty name="stockItemOutTransactionsForm" property="stockItemTransactions">
        <strong>Stock Item Out Transactions</strong>
        <ajax:displayTag id="displayTagFrame" ajaxFlag="displayAjax">
            <% request.setAttribute("outs", stockItemOutTransactionsForm.getStockItemTransactions());%>
            <display:table name="outs" class="displaytag" export="true" id="row" excludedParams="ajax" pagesize="10">
                <display:column title="row#">
                    <c:out value="${row_rowNum}"/>
                </display:column>
            </display:table>
        </ajax:displayTag>
    </logic:empty>
    </body>
</html>