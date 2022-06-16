<%@ include file="../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.domain.repository.purchasing.OrderSearchCriteria" %>

<html>
<head>
    <title>Search Orders for Purchasing</title>
    <script type="text/javascript">
        function search() {
            setFormValue('searchCriteria.pageNo', 0);
            submitForm('searchOrdersForm2');
        }
        function orderByOprNo() {
            setFormValue('searchCriteria.pageNo', 0);
            toggleSorting('searchCriteria.sortBy', '<%=OrderSearchCriteria.SORT_BY_ORDER_ID%>', 'searchCriteria.sortMethod');
            setFormValue('searchCriteria.sortBy', <%=OrderSearchCriteria.SORT_BY_ORDER_ID%>);
            submitForm('searchOrdersForm2');
        }

        function orderByPoNo() {
            setFormValue('searchCriteria.pageNo', 0);
            toggleSorting('searchCriteria.sortBy', '<%=OrderSearchCriteria.SORT_BY_PO_NO%>', 'searchCriteria.sortMethod');
            setFormValue('searchCriteria.sortBy', <%=OrderSearchCriteria.SORT_BY_PO_NO%>);
            submitForm('searchOrdersForm2');
        }
        function orderByVendorName() {
            setFormValue('searchCriteria.pageNo', 0);
            toggleSorting('searchCriteria.sortBy', '<%=OrderSearchCriteria.SORT_BY_VENDOR%>', 'searchCriteria.sortMethod');
            setFormValue('searchCriteria.sortBy', <%=OrderSearchCriteria.SORT_BY_VENDOR%>);
            submitForm('searchOrdersForm2');
        }
        function orderByBuyerLastName() {
            setFormValue('searchCriteria.pageNo', 0);
            toggleSorting('searchCriteria.sortBy', '<%=OrderSearchCriteria.SORT_BY_BUYER%>', 'searchCriteria.sortMethod');
            setFormValue('searchCriteria.sortBy', <%=OrderSearchCriteria.SORT_BY_BUYER%>);
            submitForm('searchOrdersForm2');
        }
        function orderBySuspenseDate() {
            setFormValue('searchCriteria.pageNo', 0);
            toggleSorting('searchCriteria.sortBy', '<%=OrderSearchCriteria.SORT_BY_SUSPENSE_DATE%>', 'searchCriteria.sortMethod');
            setFormValue('searchCriteria.sortBy', <%=OrderSearchCriteria.SORT_BY_SUSPENSE_DATE%>);
            submitForm('searchOrdersForm2');
        }

        function openOrder(orderId) {
            window.location = '<%=request.getContextPath()%>/viewMNITOrder.do?orderId=' + orderId;
        }
    </script>
</head>
<body>
<nested:form action="/purchasing/searchOrdersForPurchasingMNIT" method="get">
    <nested:hidden property="searchCriteria.pageNo"/>
    <nested:hidden property="searchCriteria.sortBy"/>
    <nested:hidden property="searchCriteria.sortMethod"/>
    <fieldset>
        <legend>Enter Search Criteria</legend>
        <div class="row">
            <div class="col-md-4">
                <table>
                    <tr>
                        <td>OPR#:</td>
                        <td><nested:text property="searchCriteria.oprNo"/></td>
                    </tr>
                    <tr>
                        <td>PO#:</td>
                        <td><nested:text property="searchCriteria.poNo"/></td>
                    </tr>
                    <tr>
                        <td>Buyer:</td>
                        <td>
                            <nested:select property="searchCriteria.buyerId" styleClass="chosen-select">
                                <option value="">All</option>
                                <nested:optionsCollection name="lists" property="buyers" label="lastAndFirstName" value="personId"/>
                            </nested:select>
                        </td>
                    </tr>
                    <tr>
                        <td>Status:</td>
                        <td>
                            <nested:select property="searchCriteria.statusId" style="width:160px" styleClass="chosen-select">
                                <option value="">All</option>
                                <nested:optionsCollection name="lists" property="statuses" label="name" value="statusId"/>
                            </nested:select>
                        </td>
                    </tr>
                    <tr>
                        <td>Requestor:</td>
                        <td>
                            <nested:select property="searchCriteria.requestorId" style="width:160px" styleClass="chosen-select">
                                <option value="">All</option>
                                <nested:optionsCollection name="lists" property="requestors" label="lastAndFirstName" value="personId"/>
                            </nested:select>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="col-md-4">
                <table>
                    <tr>
                        <td>Item Description:</td>
                        <td><nested:text property="searchCriteria.itemDescription" size="30"/></td>
                    </tr>
                    <tr>
                        <td >Vendor Name:</td>
                        <td><nested:text property="searchCriteria.vendorName" size="30"/></td>
                    </tr>
                    <tr>
                        <td>Ordered Between:</td>
                        <td>
                            <nested:text property="searchCriteria.orderedFrom" maxlength="10" size="10" styleClass="dateInput"/> and
                            <nested:text property="searchCriteria.orderedTo" maxlength="10" size="10" styleClass="dateInput"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Suspense Date Between:</td>
                        <td>
                            <nested:text property="searchCriteria.suspenseDateFrom" maxlength="10" size="10" styleClass="dateInput"/> and
                            <nested:text property="searchCriteria.suspenseDateTo" maxlength="10" size="10" styleClass="dateInput"/>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="col-md-4">
                <table>
                    <tr>
                        <td>MRQ#:</td>
                        <td>MRQ-<nested:text property="searchCriteria.requestTrackingNo" size="6"/></td>
                    </tr>
                    <tr>
                        <td>ICNBR:</td>
                        <td><nested:text property="searchCriteria.icnbr" size="11"/></td>
                    </tr>
                    <tr>
                        <td>Catalog #:</td>
                        <td><nested:text property="searchCriteria.itemModel" size="11"/></td>
                    </tr>
                    <tr>
                        <td class="text-right;">Budget Code:</td>
                        <td><nested:text property="searchCriteria.orgCode" size="11"/></td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="row">
            <br/>
            <div class="col-md-12 text-center">
                <div class="btn-group">
                    <button onclick="search();" class="btn btn-default">Search</button>
                    <button onclick="document.getElementById('searchCriteriaForm').reset();" class="btn btn-default">Clear</button>
                </div>
            </div>
        </div>
    </fieldset>
</nested:form>
<br/>
<ul class="pagination">
    <c:if test="${fn:length(requestScope.result.pageList)!=0}">
        <c:if test="${requestScope.result.pageNo!=0}">
            <li><a href="javascript:document.forms['searchOrdersForm2'].elements['searchCriteria.pageNo'].value=0;setActionAndSubmit('searchOrdersForm2','<%=request.getContextPath()%>/purchasing/searchOrdersForPurchasingMNIT.do');">First</a></li>
        </c:if>
        <c:forEach begin="${requestScope.result.firstLinkedPage}" end="${requestScope.result.lastLinkedPage}" var="i">
            <c:choose>
                <c:when test="${requestScope.result.pageNo!=i}">
                    <li><a href="javascript:document.forms['searchOrdersForm2'].elements['searchCriteria.pageNo'].value=<c:out value='${i}'/>;setActionAndSubmit('searchOrdersForm2','<%=request.getContextPath()%>/purchasing/searchOrdersForPurchasingMNIT.do');"><c:out value="${i+1}"/></a></li>
                </c:when>
                <c:otherwise>
                    <li class="active"><a href="#"><c:out value="${i+1}"/></a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </c:if>
    <c:if test="${requestScope.result.pageNo<requestScope.result.pageCount-1}">
        <li><a href="javascript:document.forms['searchOrdersForm2'].elements['searchCriteria.pageNo'].value=<c:out value='${requestScope.result.pageCount}'/>;setActionAndSubmit('searchOrdersForm2','<%=request.getContextPath()%>/purchasing/searchOrdersForPurchasingMNIT.do');">Last</a></li>
    </c:if>
</ul>
Total results: <c:out value="${requestScope.result.nrOfElements}"/>
<display:table name="requestScope.result.pageList" class="displaytag table table-bordered table-striped" export="false" id="parent">
    <c:set var="sortByOrderId" scope="page"><%=OrderSearchCriteria.SORT_BY_ORDER_ID%></c:set>
    <c:set var="sortByPoNo" scope="page"><%=OrderSearchCriteria.SORT_BY_PO_NO%></c:set>
    <c:set var="sortByVendorName" scope="page"><%=OrderSearchCriteria.SORT_BY_VENDOR%></c:set>
    <c:set var="sortByBuyerLastName" scope="page"><%=OrderSearchCriteria.SORT_BY_BUYER%></c:set>
    <c:set var="sortBySuspenseDate" scope="page"><%=OrderSearchCriteria.SORT_BY_SUSPENSE_DATE%></c:set>

    <c:choose>
        <c:when test="${requestScope.searchOrdersForm2.searchCriteria.sortBy==sortByOrderId && requestScope.searchOrdersForm2.searchCriteria.sortMethod=='desc'}">
            <display:column title='<a href="javascript:orderByOprNo();">OPR#</a><img src="/images/displaytag/arrow_down.png" title="Sorted By OPR# Descending">' class="text-center">
                <button type="submit" onclick="openOrder(<c:out value='${parent.orderId}'/>)" class="btn btn-default"><c:out value="${parent.orderId}"/></button>
            </display:column>
        </c:when>
        <c:when test="${requestScope.searchOrdersForm2.searchCriteria.sortBy==sortByOrderId && requestScope.searchOrdersForm2.searchCriteria.sortMethod=='asc'}">
            <display:column title='<a href="javascript:orderByOprNo();">OPR#</a><img src="/images/displaytag/arrow_up.png" title="Sorted By OPR# Ascending">' class="text-center">
                <button type="submit" onclick="openOrder(<c:out value='${parent.orderId}'/>)" class="btn btn-default"><c:out value="${parent.orderId}"/></button>
            </display:column>
        </c:when>
        <c:otherwise>
            <display:column title='<a href="javascript:orderByOprNo();">OPR#</a>' class="text-center">
                <button type="submit" onclick="openOrder(<c:out value='${parent.orderId}'/>)" class="btn btn-default"><c:out value="${parent.orderId}"/></button>
            </display:column>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${requestScope.searchOrdersForm2.searchCriteria.sortBy==sortByPoNo && requestScope.searchOrdersForm2.searchCriteria.sortMethod=='desc'}">
            <display:column title='<a href="javascript:orderByPoNo();">PO#</a><img src="/images/displaytag/arrow_down.png" title="Sorted By PO# Descending">'
                            property="purchaseOrderNumber"/>
        </c:when>
        <c:when test="${requestScope.searchOrdersForm2.searchCriteria.sortBy==sortByPoNo && requestScope.searchOrdersForm2.searchCriteria.sortMethod=='asc'}">
            <display:column title='<a href="javascript:orderByPoNo();">PO#</a><img src="/images/displaytag/arrow_up.png" title="Sorted By PO# Ascending">'
                            property="purchaseOrderNumber"/>
        </c:when>
        <c:otherwise>
            <display:column title="<a href='javascript:orderByPoNo();'>PO#</a>" property="purchaseOrderNumber"/>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${requestScope.searchOrdersForm2.searchCriteria.sortBy==sortByVendorName && requestScope.searchOrdersForm2.searchCriteria.sortMethod=='desc'}">
            <display:column title='<a href="javascript:orderByVendorName();">Vendor</a><img src="/images/displaytag/arrow_down.png" title="Sorted By Vendor Name Descending">'
                            property="vendor.externalOrgDetail.orgName"/>
        </c:when>
        <c:when test="${requestScope.searchOrdersForm2.searchCriteria.sortBy==sortByVendorName && requestScope.searchOrdersForm2.searchCriteria.sortMethod=='asc'}">
            <display:column title='<a href="javascript:orderByVendorName();">Vendor</a><img src="/images/displaytag/arrow_up.png" title="Sorted By Vendor Name Ascending">'
                            property="vendor.externalOrgDetail.orgName"/>
        </c:when>
        <c:otherwise>
            <display:column title='<a href="javascript:orderByVendorName();">Vendor</a>' property="vendor.externalOrgDetail.orgName"/>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${requestScope.searchOrdersForm2.searchCriteria.sortBy==sortByBuyerLastName && requestScope.searchOrdersForm2.searchCriteria.sortMethod=='desc'}">
            <display:column title='<a href="javascript:orderByBuyerLastName();">Buyer</a><img src="/images/displaytag/arrow_down.png" title="Sorted By Buyer Name Ascending">'
                            property="purchaser.firstAndLastName"/>
        </c:when>
        <c:when test="${requestScope.searchOrdersForm2.searchCriteria.sortBy==sortByBuyerLastName && requestScope.searchOrdersForm2.searchCriteria.sortMethod=='asc'}">
            <display:column title='<a href="javascript:orderByBuyerLastName();">Buyer</a><img src="/images/displaytag/arrow_up.png" title="Sorted By Buyer Name Ascending">'
                            property="purchaser.firstAndLastName"/>
        </c:when>
        <c:otherwise>
            <display:column title='<a href="javascript:orderByBuyerLastName();">Buyer</a>' property="purchaser.firstAndLastName"/>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${requestScope.searchOrdersForm2.searchCriteria.sortBy==sortByOrderId && requestScope.searchOrdersForm2.searchCriteria.sortMethod=='desc'}">
            <display:column title='<a href="javascript:orderByOprNo();">Date Created</a><img src="/images/displaytag/arrow_down.png" title="Sorted By Date Created Descending">'
                            property="insertionDate" format="{0,date,MM/dd/yyyy}"/>
        </c:when>
        <c:when test="${requestScope.searchOrdersForm2.searchCriteria.sortBy==sortByOrderId && requestScope.searchOrdersForm2.searchCriteria.sortMethod=='asc'}">
            <display:column title='<a href="javascript:orderByOprNo();">Date Created</a><img src="/images/displaytag/arrow_up.png" title="Sorted By Date Created Ascending">'
                            property="insertionDate" format="{0,date,MM/dd/yyyy}"/>
        </c:when>
        <c:otherwise>
            <display:column title='<a href="javascript:orderByOprNo();">Date Created</a>' property="insertionDate" format="{0,date,MM/dd/yyyy}"/>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${requestScope.searchOrdersForm2.searchCriteria.sortBy==sortBySuspenseDate && requestScope.searchOrdersForm2.searchCriteria.sortMethod=='desc'}">
            <display:column title='<a href="javascript:orderBySuspenseDate();">Suspense Date</a><img src="/images/displaytag/arrow_down.png" title="Sorted By Suspense Date Descending">'
                            property="suspenseDate" format="{0,date,MM/dd/yyyy}"/>
        </c:when>
        <c:when test="${requestScope.searchOrdersForm2.searchCriteria.sortBy==sortBySuspenseDate && requestScope.searchOrdersForm2.searchCriteria.sortMethod=='asc'}">
            <display:column title='<a href="javascript:orderBySuspenseDate();">Suspense Date</a><img src="/images/displaytag/arrow_up.png" title="Sorted By Suspense Date Ascending">'
                            property="suspenseDate" format="{0,date,MM/dd/yyyy}"/>
        </c:when>
        <c:otherwise>
            <display:column title='<a href="javascript:orderBySuspenseDate();">Suspense Date</a>' property="suspenseDate" format="{0,date,MM/dd/yyyy}"/>
        </c:otherwise>
    </c:choose>
</display:table>
</body>
</html>