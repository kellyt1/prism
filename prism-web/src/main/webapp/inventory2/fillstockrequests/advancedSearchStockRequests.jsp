<%@ page import="us.mn.state.health.domain.repository.materialsrequest.RequestSearchCriteria" %>
<%@ page import="us.mn.state.health.util.PagedQueryResultEnhanced" %>
<%@ page import="us.mn.state.health.view.inventory.Command" %>
<%@ include file="../../include/tlds.jsp" %>

<html>
    <head>
        <title>Advanced Search Open Stock Requests</title>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js"></script>
        <script type="text/javascript">
            function search() {
                setFormValue('searchCriteria.pageNo', 0);
                submitForm('advancedSearchFillStockRequestsForm');
            }
            function orderByTrackingNumber() {
                setFormValue('searchCriteria.pageNo', 0);
                toggleSorting('searchCriteria.sortBy', '<%=RequestSearchCriteria.SORT_BY_REQUEST_ID%>', 'searchCriteria.sortMethod');
                setFormValue('searchCriteria.sortBy', <%=RequestSearchCriteria.SORT_BY_REQUEST_ID%>);
                submitForm('advancedSearchFillStockRequestsForm');
            }
            function orderByNeedByDate() {
                setFormValue('searchCriteria.pageNo', 0);
                toggleSorting('searchCriteria.sortBy', '<%=RequestSearchCriteria.SORT_BY_NEED_BY_DATE%>', 'searchCriteria.sortMethod');
                setFormValue('searchCriteria.sortBy', <%=RequestSearchCriteria.SORT_BY_NEED_BY_DATE%>);
                submitForm('advancedSearchFillStockRequestsForm');
            }

            function orderByPriority() {
                setFormValue('searchCriteria.pageNo', 0);
                toggleSorting('searchCriteria.sortBy', '<%=RequestSearchCriteria.SORT_BY_PRIORITY%>', 'searchCriteria.sortMethod');
                setFormValue('searchCriteria.sortBy', <%=RequestSearchCriteria.SORT_BY_PRIORITY%>);
                submitForm('advancedSearchFillStockRequestsForm');
            }
        </script>
    </head>
    <body>
        <nested:form action="/inventory/advancedSearchOpenStockRequests" method="get">
            <nested:hidden property="searchCriteria.pageNo"/>
            <nested:hidden property="searchCriteria.sortBy"/>
            <nested:hidden property="searchCriteria.sortMethod"/>
            <html:errors/>
            <fieldset>
                <legend>Enter Search Criteria</legend>
                <table class="table">
                    <tfoot>
                        <tr>
                            <td colspan="4" class="text-center">
                                <br/>
                                <button type="submit" onclick="search();" class="btn btn-default">Search</button>
                            </td>
                        </tr>
                    </tfoot>
                    <tr>
                        <td>
                            <label>MRQ#: MRQ-<nested:text name="advancedSearchFillStockRequestsForm" property="searchCriteria.trackingNumber" size="10"/></label><br>
                            <label>ICNBR: <nested:text name="advancedSearchFillStockRequestsForm" property="searchCriteria.fullIcnbr" size="10"/></label><br>
                            <label>Item Description: <nested:text name="advancedSearchFillStockRequestsForm" property="searchCriteria.itemDescription"/></label>
                        </td>
                        <td>
                            <label>
                                Priority:
                                <html:select name="advancedSearchFillStockRequestsForm" property="searchCriteria.priorityId" styleClass="chosen-select">
                                    <option value="">All</option>
                                    <html:optionsCollection name="lists" property="priorities" label="name" value="priorityId"/>
                                </html:select>
                            </label><br>

                            <label>
                                Status:
                                <html:select name="advancedSearchFillStockRequestsForm" property="searchCriteria.statusId" styleClass="chosen-select">
                                    <html:optionsCollection name="lists" property="statuses" label="name" value="statusId"/>
                                </html:select>
                            </label><br>

                            <label>
                                Facility:
                                <html:select name="advancedSearchFillStockRequestsForm" property="searchCriteria.facilityId" styleClass="chosen-select">
                                    <option value="">All</option>
                                    <html:optionsCollection name="lists" property="stockItemFacilities" label="facilityName" value="facilityId"/>
                                </html:select>
                            </label>
                        </td>
                        <td>
                            <label>Date Requested:</label>
                            From
                            <nested:text name="advancedSearchFillStockRequestsForm" property="searchCriteria.dateRequestedFrom" size="10" title="Date format: mm/dd/yyyy" styleClass="dateInput"/>
                             To
                            <nested:text name="advancedSearchFillStockRequestsForm" property="searchCriteria.dateRequestedTo" size="10" title="Date format: mm/dd/yyyy" styleClass="dateInput"/><br>
                            <label>Need By Date:</label>
                            From
                            <nested:text name="advancedSearchFillStockRequestsForm" property="searchCriteria.needByDateFrom" size="10" title="Date format: mm/dd/yyyy" styleClass="dateInput"/>
                             To
                            <nested:text name="advancedSearchFillStockRequestsForm" property="searchCriteria.needByDateTo" size="10" title="Date format: mm/dd/yyyy" styleClass="dateInput"/><br>
                            <label>Qty On Hand:</label>
                            Out Of Stock <nested:radio name="advancedSearchFillStockRequestsForm" property="searchCriteria.onStock" value="<%=RequestSearchCriteria.OUT_OF_STOCK%>"/>&nbsp;
                            In Stock <nested:radio name="advancedSearchFillStockRequestsForm" property="searchCriteria.onStock" value="<%=RequestSearchCriteria.IN_STOCK%>"/>
                        </td>
                        <td>
                            <label>Category:</label>
                            <html:select name="advancedSearchFillStockRequestsForm" property="searchCriteria.categoryId" styleClass="chosen-select">
                                <option value="">All</option>
                                <html:optionsCollection name="lists" property="categories" label="codeAndName" value="categoryId"/>
                            </html:select><br>
                            <label>Requestor:</label>
                            <html:select name="advancedSearchFillStockRequestsForm" property="searchCriteria.requestorId" styleClass="chosen-select">
                                <option value="">All</option>
                                <html:optionsCollection name="lists" property="requestors" label="lastAndFirstName" value="personId"/>
                            </html:select>
                        </td>
                    </tr>
                </table>
            </fieldset>
            <br/>


            <%
                int firstLinked = ((PagedQueryResultEnhanced) request.getAttribute("result")).getFirstLinkedPage();
                int lastLinked = ((PagedQueryResultEnhanced) request.getAttribute("result")).getLastLinkedPage();
                if (((PagedQueryResultEnhanced) request.getAttribute("result")).getPageList().size() != 0) {
                    if (((PagedQueryResultEnhanced) request.getAttribute("result")).getPageNo() != 0) {
            %>
            <a href="javascript:document.forms['advancedSearchFillStockRequestsForm'].elements['searchCriteria.pageNo'].value=0
            ;setActionAndSubmit('advancedSearchFillStockRequestsForm','/generatePickListForAdvancedSearchOpenStockRequestsTemp.do');">
                First Page</a> &nbsp;&nbsp;
            <% }
                for (int i = firstLinked; i < lastLinked + 1; i++) {
                    if (i != ((PagedQueryResultEnhanced) request.getAttribute("result")).getPageNo()) {
            %>
            <a href="javascript:document.forms['advancedSearchFillStockRequestsForm'].elements['searchCriteria.pageNo'].value=<%=i%>;document.forms['advancedSearchFillStockRequestsForm'].action='/generatePickListForAdvancedSearchOpenStockRequestsTemp.do';document.forms['advancedSearchFillStockRequestsForm'].submit();"><%=i + 1%>
            </a>
            <% } else %><%=i + 1%><%
                    }
                }
            %>
            <%
                if (((PagedQueryResultEnhanced) request.getAttribute("result")).getPageNo() < ((PagedQueryResultEnhanced) request.getAttribute("result")).getPageCount() - 1) {
            %>
            &nbsp;&nbsp;
            <a href="javascript:document.forms['advancedSearchFillStockRequestsForm'].elements['searchCriteria.pageNo'].value=<%=((PagedQueryResultEnhanced) request.getAttribute("result")).getPageCount()%>;setActionAndSubmit('advancedSearchFillStockRequestsForm','/generatePickListForAdvancedSearchOpenStockRequestsTemp.do');">
                Last Page</a> &nbsp;&nbsp;
            <% }
            %>
            <br/>
            Total results=<%=((PagedQueryResultEnhanced) request.getAttribute("result")).getNrOfElements()%>
            <%
                String downImgOnClick;
                String rightImgOnClick;

                int requestFormIndex = 0;
            %>
            <nested:hidden name="requestFormCollection" property="cmd" value=""/>
            <nested:hidden name="requestFormCollection" property="requestFormIndex" value=""/>
            <nested:hidden name="requestFormCollection" property="rliId" value=""/>
            <nested:hidden name="requestFormCollection" property="paginationDirection" value=""/>
            <nested:hidden name="requestFormCollection" property="pageNbr"/>
            <c:if test='${requestScope.result.nrOfElements>0}'>
                <c:set var="sortByDateRequested" scope="page"><%=RequestSearchCriteria.SORT_BY_REQUEST_ID%></c:set>
                <c:set var="sortByNeedByDate" scope="page"><%=RequestSearchCriteria.SORT_BY_NEED_BY_DATE%></c:set>
                <c:set var="sortByPriority" scope="page"><%=RequestSearchCriteria.SORT_BY_PRIORITY%></c:set>

                <div class="row">
                    <div class="col-md-12">
                        <table class="table table-bordered table-striped">
                            <caption>Open Stock Requests</caption>
                            <thead>
                                <tr>
                                    <th scope="col">&nbsp;</th>
                                    <th scope="col">Deliver To:</th>
                                    <th scope="col">
                                        <a href="javascript:orderByTrackingNumber();">Request #</a>
                                        <c:if test="${advancedSearchFillStockRequestsForm.searchCriteria.sortBy==sortByDateRequested}">
                                            <c:if test="${advancedSearchFillStockRequestsForm.searchCriteria.sortMethod=='desc'}">
                                                <img src="${pageContext.request.contextPath}/images/displaytag/arrow_down.png" title="Sorted By MRQ# Descending">
                                            </c:if>
                                            <c:if test="${advancedSearchFillStockRequestsForm.searchCriteria.sortMethod=='asc'}">
                                                <img src="${pageContext.request.contextPath}/images/displaytag/arrow_up.png" title="Sorted By MRQ# Ascending">
                                            </c:if>
                                        </c:if>
                                    </th>
                                    <th scope="col">
                                        <a href="javascript:orderByTrackingNumber();">Date Requested:</a>
                                        <c:if test="${advancedSearchFillStockRequestsForm.searchCriteria.sortBy==sortByDateRequested}">
                                            <c:if test="${advancedSearchFillStockRequestsForm.searchCriteria.sortMethod=='desc'}">
                                                <img src="${pageContext.request.contextPath}/images/displaytag/arrow_down.png" title="Sorted By Requested Date Descending">
                                            </c:if>
                                            <c:if test="${advancedSearchFillStockRequestsForm.searchCriteria.sortMethod=='asc'}">
                                                <img src="${pageContext.request.contextPath}/images/displaytag/arrow_up.png" title="Sorted By Requested Date Ascending">
                                            </c:if>
                                        </c:if>
                                    </th>
                                    <th scope="col">Requestor:</th>
                                    <th scope="col">
                                        <a href="javascript:orderByNeedByDate();">Need-by Date:</a>
                                        <c:if test="${advancedSearchFillStockRequestsForm.searchCriteria.sortBy==sortByNeedByDate}">
                                            <c:if test="${advancedSearchFillStockRequestsForm.searchCriteria.sortMethod=='desc'}">
                                                <img src="${pageContext.request.contextPath}/images/displaytag/arrow_down.png" title="Sorted By Need By Date Descending">
                                            </c:if>
                                            <c:if test="${advancedSearchFillStockRequestsForm.searchCriteria.sortMethod=='asc'}">
                                                <img src="${pageContext.request.contextPath}/images/displaytag/arrow_up.png" title="Sorted By Need By Date Ascending">
                                            </c:if>
                                        </c:if>
                                    </th>
                                    <th scope="col">
                                        <a href="javascript:orderByPriority();">Priority:</a>
                                        <c:if test="${advancedSearchFillStockRequestsForm.searchCriteria.sortBy==sortByPriority}">
                                            <c:if test="${advancedSearchFillStockRequestsForm.searchCriteria.sortMethod=='desc'}">
                                                <img src="${pageContext.request.contextPath}/images/displaytag/arrow_down.png" title="Sorted By Priority Descending">
                                            </c:if>
                                            <c:if test="${advancedSearchFillStockRequestsForm.searchCriteria.sortMethod=='asc'}">
                                                <img src="${pageContext.request.contextPath}/images/displaytag/arrow_up.png" title="Sorted By Priority Ascending">
                                            </c:if>
                                        </c:if>
                                    </th>
                                    <th scope="col">Addt'l Instructions:</th>
                                    <th scope="col" class="text-center">
                                        <nested:size id="size" name="requestFormCollection" property="requestForms"/>
                                        <label>Select All<br><input type="CHECKBOX" name="checkall" onclick="selectAll('requestForms','selected', <%=size%>);"/></label>
                                    </th>
                                </tr>
                            </thead>
                            <% int i = 0; %>
                            <nested:iterate id="materialsRequest" name="requestFormCollection" property="requestForms" indexId="a">
                            <%
                                i = a % 2;
                                downImgOnClick = "javascript:" +
                                        "this.form.action ='/generatePickListForAdvancedSearchOpenStockRequestsTemp.do#" + a + "';" +
                                        " this.form.cmd.value='" + Command.HIDE_DETAIL + "';" +
                                        " this.form.requestFormIndex.value='" + requestFormIndex + "';" +
                                        " this.form.submit(); return false;";
                                rightImgOnClick = "javascript:" +
                                        "this.form.action ='/generatePickListForAdvancedSearchOpenStockRequestsTemp.do#" + a + "';" +
                                        " this.form.cmd.value='" + Command.SHOW_DETAIL + "'; " +
                                        "this.form.requestFormIndex.value='" + requestFormIndex + "';" +
                                        "this.form.submit(); return false;";
                            %>
                            <tr>
                                <td>
                                    <a name="<nested:write name='a'/>">
                                        <nested:equal property="showDetail" value="true">
                                            <html:image src="/images/arrowplaindown.gif" onclick="<%=downImgOnClick%>" border="1"/>
                                        </nested:equal>
                                        <nested:equal property="showDetail" value="false">
                                            <html:image src="/images/arrowplainright.gif" onclick="<%= rightImgOnClick %>" border="1"/>
                                        </nested:equal>
                                    </a>
                                </td>
                                <td>
                                    <nested:present property="deliveryDetail">
                                        <table cellspacing="0">
                                            <tr>
                                                <td nowrap="nowrap">
                                                    <nested:present property="deliveryDetail.organization">
                                                        <nested:write property="deliveryDetail.organization.orgName"/>
                                                    </nested:present>
                                                    <nested:notPresent property="deliveryDetail.organization">
                                                        <nested:present property="deliveryDetail.facility">
                                                            MDH
                                                        </nested:present>
                                                    </nested:notPresent>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td nowrap="nowrap">
                                                    <nested:write property="deliveryDetail.recipientName"/>
                                                </td>
                                            </tr>
                                            <nested:present property="deliveryDetail.facility">
                                                <tr>
                                                    <td nowrap="nowrap"><nested:write property="deliveryDetail.facility.facilityName"/></td>
                                                </tr>
                                            </nested:present>
                                            <nested:present property="deliveryDetail.mailingAddress">
                                                <tr>
                                                    <td nowrap="nowrap">
                                                        <nested:write property="deliveryDetail.mailingAddress.address1"/>
                                                        <br/>
                                                        <nested:present property="deliveryDetail.mailingAddress.address2">
                                                            <nested:write property="deliveryDetail.mailingAddress.address2"/>
                                                            <br/>
                                                        </nested:present>
                                                        <nested:write property="deliveryDetail.mailingAddress.city"/>
                                                        ,
                                                        <nested:write property="deliveryDetail.mailingAddress.state"/>
                                                        &nbsp;
                                                        <nested:write property="deliveryDetail.mailingAddress.zip"/>
                                                    </td>
                                                </tr>
                                            </nested:present>
                                        </table>
                                    </nested:present>
                                </td>
                                <td class="text-center" style="white-space: nowrap;"><nested:write property="trackingNumber"/></td>
                                <td class="text-center" style="white-space: nowrap;"><nested:write property="dateRequested" format="MM/dd/yyyy HH:mm"/></td>
                                <td>
                                    <nested:present property="requestor">
                                        <nested:write property="requestor.lastAndFirstName"/>
                                    </nested:present>
                                </td>
                                <td class="text-center" style="white-space: nowrap;"><nested:write property="needByDate" format="MM/dd/yyyy"/></td>
                                <td class="text-center" style="white-space: nowrap;"><nested:write property="priority.name"/></td>
                                <td><nested:write property="additionalInstructions"/></td>
                                <td align="center"><nested:checkbox property="selected"/></td>
                            </tr>
                            <nested:equal property="showDetail" value="true">
                            <tr>
                            <td>&nbsp;</td>
                            <td colspan="8" style="padding: 0;">
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th scope="col">Notes</th>
                                            <th scope="col">Location</th>
                                            <th scope="col">ICNBR</th>
                                            <th scope="col">Description</th>
                                            <th scope="col">Dispense<br/>Unit</th>
                                            <th scope="col">Status</th>
                                            <th scope="col">Category</th>
                                            <th scope="col">Qty<br/>On-Hand</th>
                                            <th scope="col">Current<br/>Demand</th>
                                            <th scope="col">Qty <br/>Requested</th>
                                            <th scope="col">Qty<br/>Shipped</th>
                                        </tr>
                                    </thead>
                                <nested:iterate id="rliForm" property="requestLineItemForms">
                                    <tr>
                                        <td class="text-center">
                                            <nested:equal property="showNotes" value="false">
                                                <a href="${pageContext.request.contextPath}/generatePickListForAdvancedSearchOpenStockRequestsTemp.do?requestFormIndex=<%=requestFormIndex%>&cmd=<%=Command.SHOW_NOTES%>&rliId=<nested:write property='requestLineItemId'/>#<bean:write name='a'/>">View Notes</a>
                                            </nested:equal>
                                            <nested:equal property="showNotes" value="true">
                                                <a href="${pageContext.request.contextPath}/generatePickListForAdvancedSearchOpenStockRequestsTemp.do?requestFormIndex=<%=requestFormIndex%>&cmd=<%=Command.HIDE_NOTES%>&rliId=<nested:write property='requestLineItemId'/>#<bean:write name='a'/>">Hide Notes</a>
                                            </nested:equal>
                                        </td>
                                        <td>
                                            <nested:iterate id="location" name="rliForm" property="stockItem.locations" indexId="loc">
                                                <nested:write name="location" property="facility.facilityCode"/>
                                                - <nested:write name="location" property="locationCode"/><br/>
                                            </nested:iterate>
                                        </td>
                                        <td><nested:write property="stockItem.fullIcnbr"/></td>
                                        <td><nested:write property="stockItem.description"/></td>
                                        <td class="text-center"><nested:write property="stockItem.dispenseUnit.name"/></td>
                                        <td class="text-center"><nested:write property="statusName"/></td>
                                        <td><nested:write property="item.category.name"/></td>
                                        <td class="text-right"><nested:write property="stockItem.qtyOnHand"/></td>
                                        <td class="text-right"><nested:write property="item.currentDemand"/></td>
                                        <td class="text-right"><nested:write property="quantity"/></td>
                                        <td class="text-right"><nested:write property="quantityFilled"/></td>
                                        <td class="text-center">
                                            <button onclick="this.form.cmd.value='<%=Command.CLOSE_RLI%>'; this.form.rliId.value='<nested:write property='requestLineItemId' />';
                                                this.form.requestFormIndex.value=<%=requestFormIndex%>;
                                                this.form.action = '/generatePickListForAdvancedSearchOpenStockRequestsTemp.do';
                                                this.form.submit(); return false;" class="btn btn-default">Cancel
                                            </button>
                                        </td>
                                    </tr>
                                    <nested:equal property="showNotes" value="true">
                                        <tr>
                                            <td colspan="9">
                                                <table class="table">
                                                    <tr>
                                                        <td scope="col">Note</td>
                                                        <td scope="col">Author</td>
                                                        <td scope="col">Date</td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <nested:textarea property="textNote" cols="50" rows="1"/>
                                                        </td>
                                                        <td class="text-center">
                                                            <button type="submit" onclick="this.form.cmd.value='<%=Command.ADD_NOTE%>';
                                                                this.form.rliId.value='<nested:write property='requestLineItemId' />';
                                                                this.form.requestFormIndex.value=<%=requestFormIndex%>;
                                                                this.form.action = '/generatePickListForAdvancedSearchOpenStockRequestsTemp.do';
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
                        </table>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-md-12 text-center">
                        <button type="submit" onclick="this.form.action='/generatePickList.do';this.form.submit(); return false;" class="btn btn-default">Generate Pick List</button>
                    </div>
                </div>
            </c:if>
        </nested:form>
    </body>
</html>