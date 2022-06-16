<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>Advanced Search All Requests</title>
    </head>
    <body>
        <nested:form action="requests/advancedsearchmaterialsrequests">
            <fieldset>
                <legend>Search Requests</legend>
                <table>
                    <tr>
                        <td>
                            <label>Request Tracking #</label>
                            <nested:text property="requestSearchCriteria.trackingNumber"/>
                            <label>Date Requested From</label>
                            <nested:text property="requestSearchCriteria.dateRequestedFrom"/>
                            <label>Date Requested To</label>
                            <nested:text property="requestSearchCriteria.dateRequestedTo"/>
                            <label>Need By Date From</label>
                            <nested:text property="requestSearchCriteria.needByDateFrom"/>
                            <label>Need By Date Requested To</label>
                            <nested:text property="requestSearchCriteria.needByDateTo"/>
                            <label>Vendor Name</label>
                            <nested:text property="requestSearchCriteria.vendorName"/>
                        </td>
                        <td width="20%"></td>
                        <td valign="top">
                            <label>Item Description</label>
                            <nested:text property="requestSearchCriteria.itemDescription"/>

                            <label>Item Model</label>
                            <nested:text property="requestSearchCriteria.itemModel"/>

                            <label>Requestor</label>
                            <nested:select property="requestSearchCriteria.requestorId">
                                <nested:optionsCollection name="users" property="users" label="lastAndFirstName" value="personId"/>
                            </nested:select>
                            <label>Status</label>
                            <nested:select property="requestSearchCriteria.statusId">
                                <option></option>
                                <nested:optionsCollection name="statuses" property="statuses" label="name" value="statusId"/>
                            </nested:select>

                            <label>Category</label>
                            <nested:select property="requestSearchCriteria.categoryId">
                                <nested:optionsCollection name="categories" property="categories" label="name" value="categoryId"/>
                            </nested:select>
                        </td>
                    </tr>
                </table>


            </fieldset>
            <html:submit/>
        </nested:form>
        <display:table name="requests" class="displaytag" export="false" id="row" excludedParams="ajax">
            <display:column title="row#"><c:out value="${row_rowNum}"/></display:column>
            <display:caption><strong>Requests</strong></display:caption>
            <display:column property="deliverToInfoAsString" title="Delivery Detail" sortable="false" headerClass="sortable"/>
            <display:column property="trackingNumber" title="Tracking#" sortable="false"/>
            <display:column property="dateRequested" title="Date Requested" sortable="false"/>
            <display:column property="requestor.lastAndFirstName" title="Requestor" sortable="false"/>
            <display:column property="needByDate" title="Need by Date" sortable="false"/>
            <display:column property="priority.name" title="Priority" sortable="false"/>
            <display:column property="additionalInstructions" title="Additional Instructions" sortable="false"/>
        </display:table>
    </body>
</html>