<%@ include file="../../include/tlds.jsp" %>


<jsp:useBean id="stockItemInTransactionsForm"
             class="us.mn.state.health.view.materialsrequest.reports.StockItemTransactionsForm"
             scope="session"/>
<html>
    <head>
        <title>Search Stock Item Transactions</title>
    </head>

    <body>
        <h1 class="text-center" style="color: #f00;">Information in this report is not maintained after October 23, 2014</h1>
        <html:form action="/displaystockitemintransactions.do">
            <fieldset>
                <legend>Search Stock Item IN Transactions</legend>
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
        <logic:notEmpty name="stockItemInTransactionsForm" property="stockItemTransactions">
            <!--use pagelinksClass attribute for oc4j-->
            <ajax:displayTag id="displayTagFrame" ajaxFlag="displayAjax" pagelinksClass="pagelinks">
                <% request.setAttribute("ins", stockItemInTransactionsForm.getStockItemTransactions());%>
                <display:table name="ins" class="displaytag" export="true" id="row" excludedParams="ajax" pagesize="10">
                    <display:column title="row#"><c:out value="${row_rowNum}"/></display:column>
                    <display:caption><strong>Stock Item In Transactions</strong></display:caption>
                    <display:caption media="excel"><strong>Stock Item In Transactions</strong></display:caption>
                    <display:column property="trackingNumber" title="Request Tracking #" sortable="true" maxLength="10" headerClass="sortable"/>
                    <display:column property="requestLineItemId" title="LineItemId" sortable="true" headerClass="sortable"/>
                    <display:column property="fullIcnbr" title="ICNBR" sortable="true" headerClass="sortable"/>
                    <display:column property="itemDescription" title="Description" sortable="true" headerClass="sortable" maxLength="20"/>
                    <display:column property="dateRequested" title="Date" sortable="true" headerClass="sortable"/>
                    <display:column property="qtyRequested" title="Qty Requested (BU)" sortable="true" headerClass="sortable" maxLength="5"/>
                    <display:column property="statusName" title="Status" sortable="true" headerClass="sortable" media="excel"/>
                    <display:column title="Status" sortable="true" headerClass="sortable" media="html">
                        <c:if test="${row.statusName!='Waiting For Purchase'}">
                            <a href=""
                               onclick="window.open('/showRequestLineItemDetails.do?requestLineItemId=<c:out value="${row.requestLineItemId}"/>', '','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, HEIGHT=300, WIDTH=450, left=50, top=50'); return false;">
                                <span style="color: blue;"><c:out value="${row.statusName}"/></span>
                            </a><br/>
                        </c:if>
                        <c:if test="${row.statusName=='Waiting For Purchase'}">
                            <c:out value="${row.statusName}"/>
                        </c:if>
                    </display:column>
                    <display:column property="orgCode" title="Org Bdgt" sortable="true" headerClass="sortable"/>
                    <display:column property="orgCodeDisplay" title="Alt Org" sortable="true" headerClass="sortable"/>
                    <display:column property="requestorLastFirstName" title="Requestor" sortable="true" headerClass="sortable"/>
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
        <logic:empty name="stockItemInTransactionsForm" property="stockItemTransactions">
            <strong>Stock Item IN Transactions</strong>
            <ajax:displayTag id="displayTagFrame" ajaxFlag="displayAjax">
                <% request.setAttribute("ins", stockItemInTransactionsForm.getStockItemTransactions());%>
                <display:table name="ins" class="displaytag" export="true" id="row" excludedParams="ajax" pagesize="10">
                    <display:column title="row#">
                        <c:out value="${row_rowNum}"/>
                    </display:column>
                </display:table>
            </ajax:displayTag>
        </logic:empty>
    </body>
    <IFRAME id="Defib" src="../../include/Defibrillator.jsp" frameBorder=no width=0 height=0></IFRAME>
</html>