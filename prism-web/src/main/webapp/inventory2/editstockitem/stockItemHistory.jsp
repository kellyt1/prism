<%@ include file="../../include/tlds.jsp" %>
<html>
    <head>
        <title>History for xxx-xxxx</title>
    </head>
    <body>
        <display:table export="false" name="requestScope.stockItemHistory" class="table table-bordered table-striped" id="parent">
            <display:column title="">
                <c:out value="${parent_rowNum}"/>
            </display:column>
            <display:column title="Description" property="description" />
            <display:column title="Category" property="category" maxLength="20"/>
            <display:column title="ICNBR" property="icnbr" maxLength="20"/>
            <display:column title="Hazard." property="hazardous" maxLength="20"/>
            <display:column title="ROP" property="reorderPoint" maxLength="20"/>
            <display:column title="ROQ" property="reorderQty" maxLength="20"/>
            <display:column title="Safety Stock" property="safetyStock" maxLength="20"/>
            <display:column title="Cyc. Count Priority" property="cycleCountPriority" maxLength="20"/>
            <display:column title="Est. Annual Usage" property="estimatedAnnualUsage" maxLength="20"/>
            <display:column title="Review Date" property="reviewDate" maxLength="10" format="{0,date,MM/dd/yyyy}"/>
            <display:column title="Seasonal" property="seasonal" maxLength="20"/>
            <display:column title="Dispense Unit" property="dispenseUnit" maxLength="20"/>
            <display:column title="Org. Budget" property="orgBudget" maxLength="20"/>
            <display:column title="Primary Contact" property="primaryContact.firstAndLastName" maxLength="20"/>
            <display:column title="Secondary Contact" property="secondaryContact.firstAndLastName" maxLength="20"/>
            <display:column title="Status" property="status" maxLength="20"/>
            <display:column title="Hold Until Date" property="holdUntilDate" maxLength="10" format="{0,date,MM/dd/yyyy}"/>
            <display:column title="Fill Until Depleted" property="fillUntilDepleted" maxLength="20"/>
            <display:column title="History Comments" property="historyComments"/>
            <display:column title="Inserted By" property="insertedBy" maxLength="20"/>
            <display:column title="Date" property="insertionDate" maxLength="10" format="{0,date,MM/dd/yyyy}"/>
        </display:table>
    </body>
</html>