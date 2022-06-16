<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemAction" %>
<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemForm" %>
<%@ include file="../../include/tlds.jsp" %>

<script type="text/javascript">
    function addVendor() {
        setFormValue("cmd", '<%=EditStockItemForm.ADD_VENDOR%>');
        submitForm("editStockItemForm");
    }
</script>
<table class="table table-bordered table-striped">
    <caption>Vendors:</caption>
    <tr>
        <th scope="col">Vendor:*</th>
        <th scope="col">Catalog#</th>
        <th scope="col">Buy Unit (BU):*</th>
        <th scope="col">BU Cost:</th>
        <th scope="col">DU(
            <nested:write property="stockItem.dispenseUnit.name"/>
            )/BU</th>
        <th scope="col">Discount%</th>
        <th scope="col">Primary</th>
        <th scope="col">Last Updated</th>
        <%--<th scope="col"></th>--%>
    </tr>
    <c:set var="count" value="0" scope="page"/>
    <c:forEach items="${sessionScope.editStockItemForm.stockItem.itemVendorsAsArray}" var="iv">
        <tr>
            <c:set var="catalogNbr" value="vendorCatalogNbr[${count}]" scope="request"/>
            <c:set var="buyUnitId" value="buyUnitId[${count}]" scope="request"/>
            <c:set var="buyUnitCost" value="buyUnitCost[${count}]" scope="request"/>
            <c:set var="discount" value="discount[${count}]" scope="request"/>
            <c:set var="duPerBu" value="dispenseUnitsPerBuyUnits[${count}]" scope="request"/>
            <c:set var="updateInfo" value="updateInfo[${count}]" scope="request"/>
            <td><nested:write name="iv" property="vendor.externalOrgDetail.orgName"/></td>
            <td><nested:text name="editStockItemForm" property='<%=request.getAttribute("catalogNbr").toString()%>' disabled="true" readonly="true"/></td>
            <td>
                <nested:select property='<%=request.getAttribute("buyUnitId").toString()%>' disabled="true">
                    <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="units" label="name" value="unitId"/>
                </nested:select>
            </td>
            <td><strong>$</strong> <nested:text property='<%=request.getAttribute("buyUnitCost").toString()%>' size="6" disabled="true" readonly="true"/></td>
            <td><nested:text property='<%=request.getAttribute("duPerBu").toString()%>' size="4" disabled="true" readonly="true"/></td>
            <td><nested:text property='<%=request.getAttribute("discount").toString()%>' size="4" disabled="true" readonly="true"/></td>
            <td><nested:radio property="primaryVendorKey" value='<%=pageContext.getAttribute("count").toString()%>' disabled="true"/></td>
            <td><nested:write property='<%=request.getAttribute("updateInfo").toString()%>'/></td>
            <%--<td><a href="?cmd=<%=EditStockItemForm.REMOVE_VENDOR%>&amp;vendorKey=<nested:write name='iv' property='key'/>&amp;page=2&amp;nextPage=2">Remove</a></td>--%>

            <c:set var="count" value="${count+1}"/>
        </tr>
    </c:forEach>
    <%--<tr>
        <td>
            <nested:select property="newVendorId" style="width:200px" styleClass="chosen-select">
                <option value="">Please Select a Vendor</option>
                <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="vendors" label="externalOrgDetail.orgName" value="vendorId"/>
            </nested:select>
        </td>
        <td><nested:text property="newVendorCatalogNbr"/></td>
        <td>
            <nested:select property="newVendorBuyUnitId" styleClass="chosen-select">
                <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="units" label="name" value="unitId"/>
            </nested:select>
        </td>
        <td>$ <nested:text property="newVendorBuyUnitCost" size="6"/></td>
        <td><nested:text property="newVendorDispenseUnitPerBuyUnit" size="4"/></td>
        <td><nested:text property="newVendorDiscount" size="4" maxlength="4"/></td>
        <td></td>
        <td></td>
        <td><a href="javascript:void(0);" onclick="addVendor();">Add</a></td>
    </tr>--%>
</table>