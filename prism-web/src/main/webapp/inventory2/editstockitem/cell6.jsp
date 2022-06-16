<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemAction" %>
<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemForm" %>
<%@ page import="us.mn.state.health.model.inventory.StockItemLocation" %>
<%@ include file="../../include/tlds.jsp" %>

<script type="text/javascript">
    function addLocation() {
        setFormValue("cmd", '<%=EditStockItemForm.ADD_STOCK_ITEM_LOCATION%>');
        submitForm("editStockItemForm");
    }
</script>

<table class="table table-bordered table-striped">
    <caption>Locations</caption>
    <thead>
        <tr>
            <th scope="col">Facility</th>
            <th scope="col">Location Code</th>
            <th scope="col">Primary</th>
            <%--<th scope="col"></th>--%>
        </tr>
    </thead>
    <nested:iterate property="stockItem.locations" id="stockItemLocation">
        <tr>
            <td><nested:write name="stockItemLocation" property="facility.facilityName"/></td>
            <td><nested:write name="stockItemLocation" property="locationCode"/></td>
            <td class="text-center"><nested:radio name="editStockItemForm" property="primaryLocationKey" value="<%=((StockItemLocation)stockItemLocation).getFullBinLocation()%>" disabled="true"/></td>
            <%--<td class="text-center"><a href="?cmd=<%=EditStockItemForm.REMOVE_STOCK_ITEM_LOCATION%>&amp;facilityId=<nested:write name='stockItemLocation' property='facility.facilityId'/>&amp;locationCode=<nested:write name='stockItemLocation' property='locationCode'/>&amp;page=1&amp;nextPage=1">Remove</a></td>--%>
        </tr>
    </nested:iterate>
    <%--<tr>
        <td>
            <nested:select property="facilityId" styleClass="chosen-select">
                <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="stockItemFacilities" label="facilityName" value="facilityId"/>
            </nested:select>
        </td>
        <td><nested:text property="locationCode" size="10"/></td>
        <td></td>
        <td class="text-center"><a href="javascript:void(0);" onclick="addLocation();">Add</a></td>
    </tr>--%>
</table>
