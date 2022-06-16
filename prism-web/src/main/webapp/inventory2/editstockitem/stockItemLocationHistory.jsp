<%@ include file="../../include/tlds.jsp" %>
<html>
<head><title>History for xxx-xxxx</title></head>
<body>
<display:table export="false" name="requestScope.stockItemLocationHistory" class="table table-bordered table-striped" id="parent">
    <display:column title="">
        <c:out value="${parent_rowNum}"/>
    </display:column>
    <display:column title="Location Code" property="locationCode" maxLength="20"/>
    <display:column title="Primary" property="isPrimary" maxLength="20"/>
    <%--<display:column title="Stock Item" property="stockItem.description" maxLength="50"/>--%>
    <display:column title="Facility" property="facility.facilityName" maxLength="20"/>
    <display:column title="Update Date" property="updateDate" maxLength="10" format="{0,date,MM/dd/yyyy}"/>

</display:table>

</body>
</html>