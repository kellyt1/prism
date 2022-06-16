<%@ include file="../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.view.inventory.ItemVendorForm"%>

<html>
<head>
    <title>Edit Vendor Purchasing Info</title>
</head>

<body>
<nested:form action="/editItemVendorInfo.do">
    <div align="center">
        <nested:write property="item.description"/>
    </div>
    <table align="center" width="90%" border="1">
        <tr class="header">
            <th>Vendor Name</th>
            <th>Buy Unit</th>
            <th>Buy Unit Cost</th>
            <th>Catalog <br/>Number</th>
            <th>Discount<br/> %</th>
            <th>Primary Vendor</th>
            <th>Last Update</th>
        </tr>
        <nested:iterate property="itemVendorForms" id="ivf">
            <tr onmouseover="this.bgColor='#E9E9E9'" onmouseout="this.bgColor='white'">
                <td><nested:write property="vendorName"/></td>
                <td>
                    <nested:select property="buyUnitId">
                        <nested:optionsCollection name="itemVendorsForm" property="units" label="name" value="unitId"/>
                    </nested:select>
                </td>
                <td><nested:text property="buyUnitCost" size="7" maxlength="10"/></td>
                <td><nested:text property="vendorCatalogNbr" size="10"/></td>
                <td><nested:text property="discount" size="5" maxlength="6"/></td>
                <td><html:radio name="itemVendorsForm" property="primaryKey" value="<%=((ItemVendorForm)ivf).getKey().toString()%>"/></td>
                <td>
                    <nested:write name="ivf" property="lastUpdatedDate"/><br/>
                    <nested:write name="ivf" property="lastUpdatedByUser"/>
                </td>
                <%--<td><nested:radio property="key" value="<%=((ItemVendorsForm)session.getAttribute("itemVendorsForm")).getPrimaryKey().toString()%>"/></td>--%>
            </tr>
        </nested:iterate>
    </table>
    <table width="90%">
        <tr>
            <td><br/><br/><br/><br/></td>
            <td align="right"><input type="submit" value="Save Item Vendor Info"/></td>
            <td align="left"><input type="submit" value="Select Item Vendor Info" onclick="this.form.action='selectVendorPurchasingInfo.do'; this.form.submit(); return false;"> </td>
        </tr>
        <tr>
            <td colspan="3" align="center"><input type="submit" value="Close Window" onclick="window.close();"></td>
        </tr>
    </table>
</nested:form>
</body>
</html>