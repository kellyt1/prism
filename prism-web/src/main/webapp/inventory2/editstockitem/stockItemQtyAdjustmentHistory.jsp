<%@ include file="../../include/tlds.jsp" %>
<html>
    <head><title>Stock Qty Adjustment History for xxx-xxxx</title></head>
    <body>
        <display:table export="false" name="requestScope.qtyAdjustmentHistory" class="table table-bordered table-striped" id="parent">
            <display:column title="">
                <c:out value="${parent_rowNum}"/>
            </display:column>
            <display:column title="Previous Qty On hand" property="previousQtyOnHand" maxLength="20"/>
            <display:column title="New Qty On Hand" property="newQtyOnHand" maxLength="20"/>
            <display:column title="Reason" property="changeReason.reason" />
            <display:column title="Org. Budget" property="orgBudget.orgBudgetCodeAndNameAndFY" maxLength="20"/>
            <display:column title="Changed By" property="changedBy.firstAndLastName" maxLength="20"/>
            <display:column title="Date" property="changeDate" maxLength="10" format="{0,date,MM/dd/yyyy}"/>
        </display:table>
    </body>
</html>