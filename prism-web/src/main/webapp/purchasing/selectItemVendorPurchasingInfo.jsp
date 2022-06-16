<%@ include file="../include/tlds.jsp" %>

<html>
<head>
    <title>Select Vendor Purchasing Info</title>
    <script type="text/javascript">
        function setVendorPurchasingInfo(buyUnitId, buyUnitCost, vendorCatalogNbr, discountPercent) {
            setParameterInParentWindow("buyUnitId", buyUnitId);
            setParameterInParentWindow("buyUnitCost", buyUnitCost);
            setParameterInParentWindow("vendorCatalogNbr", vendorCatalogNbr);
            setParameterInParentWindow("discountPercent", discountPercent);
            window.opener.update();
            window.close();
            return true;
        }
    </script>
</head>

<body>
<nested:form action="/selectVendorPurchasingInfo.do">
    <nested:write property="item.description"/>
    <br/>
    <table align="center" width="90%" border="1">
        <tr class="header">
            <th></th>
            <th>Vendor Name</th>
            <th>Buy Unit</th>
            <th>Buy Unit Cost</th>
            <th>Vendor Catalog Number</th>
            <th>Primary Vendor</th>
            <th>Discount <br/> %</th>
            <th>Last Update</th>
        </tr>
        <nested:iterate property="item.itemVendors" id="iv">
            <tr onmouseover="this.bgColor='#E9E9E9'" onmouseout="this.bgColor='white'">
                <!--<td>-->
                    <%--<a href="javascript:setVendorPurchasingInfo(<nested:write name="iv" property="buyUnit.unitId"/>--%>
                    <%--,<nested:write name="iv" property="buyUnitCost"/>--%>
                    <%--,'<nested:write name="iv" property="vendorCatalogNbr"/>', <nested:write name="iv" property="discount"/> );">--%>
                        <!--Select This-->
                    <!--</a>-->
                <!--</td>-->
                <td>
                    <input type="button" value="Select This" onclick="setVendorPurchasingInfo(<nested:write name="iv" property="buyUnit.unitId"/>
                    ,<nested:write name="iv" property="buyUnitCost"/>
                    ,'<nested:write name="iv" property="vendorCatalogNbr"/>', <nested:write name="iv" property="discount"/> );"/>
                </td>
                <td align="center"><nested:write name="iv" property="vendor.externalOrgDetail.orgName"/></td>
                <td align="center"><nested:write name="iv" property="buyUnit.name"/></td>
                <td align="center"><nested:write name="iv" property="buyUnitCost"/></td>
                <td align="center"><nested:write name="iv" property="vendorCatalogNbr"/></td>
                <td align="center">
                    <nested:equal name="iv" property="primaryVendor" value="true">
                        Yes
                    </nested:equal>
                    <nested:equal name="iv" property="primaryVendor" value="false">
                        No
                    </nested:equal>
                </td>
                <td align="center"><nested:write name="iv" property="discount"/></td>
                <td align="center">
                    <nested:write name="iv" property="lastUpdatedDate"/><br/>
                    <nested:write name="iv" property="lastUpdatedByUser"/>
                </td>
            </tr>
        </nested:iterate>
    </table>
    <table width="90%">
        <tr>
            <td><br/><br/><br/><br/></td>
            <td align="right"><input type="submit" value="Edit Item Vendor Info" onclick="this.form.action='viewEditItemVendorInfo.do?itemId=<nested:write property="item.itemId" />'; this.form.submit(); return false;"> </td>
            <td align="left"><input type="submit" value="Close Window" onclick="window.close();"> </td>
        </tr>
    </table>
</nested:form>
</body>
</html>