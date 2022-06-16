<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<html>
    <head>
        <title>Update Stock Item Information</title>
    </head>
    <body>
        <nested:form action="inventory/editMyStockItem.do">
            <p class="text-center"><small>(click on column header to sort column)</small></p>
            <p class="text-center">
                <input type="button" value="Click to show all items regardless of status"
                       onclick="this.form.action = '/viewMyStockItems.do?viewAll=true'; this.form.submit(); return false;"/>
            </p>

            <table class="table table-bordered table-striped sortable" id="unique_id">
                <tr>
                    <th>ICNBR</th>
                    <th>Unit</th>
                    <th>Item Description</th>
                    <th>
                        <bean:message key="orgBudget"/>
                    </th>
                    <th>Primary Contact</th>
                    <th>Secondary Contact</th>
                    <th>Usage</th>
                    <th>Status</th>
                </tr>
                <nested:iterate id="stockItem" name="stockItemsForm" property="stockItems" indexId="a">
                    <tr>
                        <%--<td>
                            <!--<input type="BUTTON" value="Edit Item"-->
                            <!--onclick="this.form.action = 'viewRequestMyStockItemChange.do';-->
                                &lt;%&ndash;this.form.siId.value = <nested:write property='itemId'/>;&ndash;%&gt;
                            <!--this.form.submit(); " />-->
                            <input type="button" value="Edit Item"
                                   onclick="this.form.action = '/inventory/editMyStockItem.do?stockItemId=<nested:write property='itemId'/>&cmd=<%=EditStockItemForm.LOAD_ITEM%>';
                                   this.form.submit(); return false;"/>
                        </td>--%>
                        <td><nested:write property="fullIcnbr"/></td>
                        <td><nested:write property="dispenseUnit.name"/></td>
                        <td><nested:write property="description"/></td>
                        <td><nested:write property="orgBudget.orgBudgetCodeAndName"/></td>
                        <td><nested:write property="primaryContact.firstAndLastName"/></td>
                        <td><nested:write property="secondaryContact.firstAndLastName"/></td>
                        <td class="text-right"><nested:write property="annualUsage" format="#,###"/></td>
                        <td><nested:write property="status.name"/></td>
                    </tr>
                </nested:iterate>
            </table>
        </nested:form>
    </body>
</html>
