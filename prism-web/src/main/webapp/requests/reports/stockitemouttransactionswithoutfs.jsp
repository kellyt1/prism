<%@ include file="../../include/tlds.jsp" %>

<jsp:useBean id="stockItemOutTransactionsWithoutFSForm"
             class="us.mn.state.health.view.materialsrequest.reports.StockItemTransactionsForm"
             scope="session"/>
<html>
    <head>
        <title>Search Stock Item Transactions</title>
    </head>
    <body>
        <h1 class="text-center" style="color: #f00;">Information in this report is not maintained after October 23, 2014</h1>
        <html:form action="/displaystockitemouttransactionswithoutfs">
            <fieldset>
                <legend>Search Stock Item OUT Transactions</legend>
                <div class="text-center">
                    <label>ICNBR: <html:text property="icnbr" size="8" maxlength="10"/></label>&nbsp;
                    <label>
                        Category:&nbsp;
                        <nested:select property="categoryId">
                            <option label="All"></option>
                            <nested:optionsCollection property="categories" label="name" value="categoryId"/>
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
        <logic:notEmpty name="stockItemOutTransactionsWithoutFSForm" property="stockItemTransactions">
            <!--use pagelinksClass attribute for oc4j-->
            <ajax:displayTag id="displayTagFrame" ajaxFlag="displayAjax" pagelinksClass="pagelinks">
                <% request.setAttribute("outs", stockItemOutTransactionsWithoutFSForm.getStockItemTransactions());%>
                <display:table name="outs" class="displaytag" export="true" id="row" excludedParams="ajax" pagesize="10">
                    <display:column title="row#">
                        <c:out value="${row_rowNum}"/>
                    </display:column>
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
                    <display:column property="amountPaid" title="Amount Paid" sortable="true" headerClass="sortable" format="$ {0,number,#,###.##}"/>
                    <display:column property="totalAmount" title="Total Amount" sortable="true" headerClass="sortable" format="$ {0,number,#,###.##}"/>

                    <display:column property="requestorLastFirstName" title="Requestor" sortable="true" headerClass="sortable"/>
                    <display:footer media="html">
                        <tr>
                            <td colspan="8"><strong>Total:</strong></td>
                            <td>
                                <strong>
                                    <nested:write name="stockItemOutTransactionsWithoutFSForm" property="totalRequested"/>
                                </strong>
                            </td>
                            <td colspan="2">
                                <strong>
                                    <nested:write name="stockItemOutTransactionsWithoutFSForm" property="totalFilled"/>
                                </strong>
                            </td>
                            <td colspan="3">
                                <strong>
                                    $<nested:write name="stockItemOutTransactionsWithoutFSForm" property="totalAmountPaid" format="#,###.##"/>
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
            <p style="font-size: 125%;">*<em>the excel report has the "Deliver To" information</em></p>
        </logic:notEmpty>
        <logic:empty name="stockItemOutTransactionsWithoutFSForm" property="stockItemTransactions">
            <strong>Stock Item Out Transactions</strong>
            <ajax:displayTag id="displayTagFrame" ajaxFlag="displayAjax">
                <% request.setAttribute("outs", stockItemOutTransactionsWithoutFSForm.getStockItemTransactions());%>
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