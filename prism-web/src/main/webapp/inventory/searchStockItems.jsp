<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemForm" %>
<html>
    <head>
        <title>Search Stock Items</title>
    </head>
    <body>
        <form action="searchStockItems.do" method="POST" accept-charset="utf-8">
            <div class="row">
                <div class="col-md-4 col-md-offset-4">
                    <label for="keywords">Keyword Search</label>
                    <div class="input-group">
                        <nested:text name="searchCatalogForm" property="query" size="50" styleClass="form-control" styleId="keywords"/>
                        <div class="input-group-btn">
                            <label for="search" style="display: none;">Search</label>
                            <button class="btn btn-default" type="submit" id="search"><i class="glyphicon glyphicon-search"></i></button>
                        </div>
                    </div>
                    <p class="text-center">
                        <label><input type="checkbox" name="ckShowInactive" value="true">Show all items regardless of status.</label><br/>
                        <a href="viewAdvancedSearchStockItems.do">Advanced Search</a>
                    </p>
                </div>
            </div>
        </form>
        <br/>
        <nested:form action="searchStockItems">
            <p class="text-center"><small>(click on column header to sort column)</small></p>
            <table class="table table-bordered table-striped sortable" id="idSort">
                <thead>
                    <tr>
                        <th scope="col" class="unsortable"></th>
                        <th scope="col">ICNBR</th>
                        <th scope="col">Unit</th>
                        <th scope="col">Item Description</th>
                        <th scope="col"><bean:message key="orgBudget"/></th>
                        <th scope="col">Primary Contact</th>
                        <th scope="col">Secondary Contact</th>
                        <th scope="col">Usage</th>
                        <th scope="col">Status</th>
                    </tr>
                </thead>
                <tbody>
                    <nested:iterate id="stockItem" name="searchCatalogForm" property="results" indexId="a">
                        <tr>
                            <td style="vertical-align: middle;">
                                <input type="button" value="Edit Item"
                                       onclick="this.form.action = '/inventory/editStockItem.do?stockItemId=<nested:write property='itemId'/>&cmd=<%=EditStockItemForm.LOAD_ITEM%>';
                                       this.form.submit(); return false;"/>
                            </td>
                            <td style="white-space: nowrap;"><nested:write property="fullIcnbr"/></td>
                            <td><nested:write property="dispenseUnit.name"/></td>
                            <td><nested:write property="description"/></td>
                            <td><nested:write property="orgBudget.orgBudgetCodeAndNameAndFY"/></td>
                            <td><nested:write property="primaryContact.firstAndLastName"/></td>
                            <td><nested:write property="secondaryContact.firstAndLastName"/></td>
                            <td class="text-right"><nested:write property="annualUsage"/></td>
                            <td class="text-center"><nested:write property="status.name"/></td>
                        </tr>
                    </nested:iterate>
                </tbody>
            </table>
        </nested:form>
    </body>
</html>