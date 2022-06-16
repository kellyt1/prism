<%@ include file="../../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.model.inventory.StockItem" %>

<html>
<head><title>Stock Items below ROP that need to be reordered </title></head>
<body>
<html:form action="/inventory/reports/viewStockItemsBelowROPNotOnOrder">
    <table align="center">
        <tr>
            <th colspan="6">Search Stock Item Below ROP</th>
        </tr>
        <tr>
            <td align="left" colspan="4">Category</td>
            <td align="left" colspan="4">Facility</td>
        </tr>
        <tr>
            <td colspan="4" align="right">
                <html:select property="selectedCategories" multiple="true" size="4">
                    <html:optionsCollection property="categories" label="name" value="categoryId"/>
                </html:select>
            </td>
            <td align="left">
                <html:select property="selectedFacilities" multiple="true" size="4">
                    <html:optionsCollection property="stockItemFacilities" label="facilityName" value="facilityId"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td colspan="6" align="center">Include the StockItems w/o locations
                <html:checkbox property="woLocations"/>
            </td>
        </tr>
        <tr>
            <td colspan="6" align="center">
                <html:submit value="Search" title="Search"/>
                <input type="button" onclick="this.form.action = '/viewInventoryReports.do';this.form.submit(); return false;"
                       value="Return to Menu"/>
            </td>
        </tr>
    </table>
</html:form>
<ajax:displayTag id="displayTagFrame" ajaxFlag="displayAjax" pagelinksClass="pagelinks">
    <display:table name="sessionScope.stockItemsReportForm.stockItems" class="displaytag"
                   export="true" id="parent" excludedParams="ajax"
                   pagesize="10">
        <display:column title="row#">
            <c:out value="${parent_rowNum}"/>
        </display:column>
        <display:column title="ICNBR" property="fullIcnbr"/>
        <display:column title="Description" property="description" maxLength="40"/>
        <display:column title="Qty On Hand" property="qtyOnHand"/>
        <display:column title="Reorder Point" property="reorderPoint"/>
        <display:column title="Dispense Unit" property="dispenseUnit.name"/>
        <display:column title="ROQ" property="reorderQty"/>
        <display:column title="Current Demand" property="currentDemand"/>
        <display:column title="Locations" media="html">
            <%request.setAttribute("locations", ((StockItem) parent).getLocations());%>
            <display:table name="locations"
                           id="child${parent_rowNum}" class="simple sublist">
                <display:column title="Facility" property="facility.facilityName"/>
                <display:column title="Location Code" property="locationCode"/>
            </display:table>
        </display:column>
        <display:column title="Locations" property="facilityAndLocationCodesAsString" media="excel"/>

        <display:setProperty name="export.pdf" value="false"/>
        <display:setProperty name="export.rtf" value="false"/>
        <display:setProperty name="export.pdf.filename" value="Stock_Items_Below_ROP.pdf"/>
        <display:setProperty name="export.excel.filename" value="Stock_Items_Below_ROP.xls"/>
        <display:setProperty name="export.rtf.filename" value="Stock_Items_Below_ROP.rtf"/>
        <display:setProperty name="export.xml" value="false"/>
        <display:setProperty name="export.csv" value="false"/>
    </display:table>
</ajax:displayTag>
</body>
</html>