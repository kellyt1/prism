<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemForm" %>
<%@ include file="../../include/tlds.jsp" %>

<script type="text/javascript">
    function addLot() {
        setFormValue("cmd", '<%=EditStockItemForm.ADD_STOCK_ITEM_LOT%>');
        submitForm("editStockItemForm");
    }
</script>

<table class="table table-bordered table-striped">
    <caption>Lots</caption>
    <thead>
        <tr>
            <th scope="col">Code</th>
            <th scope="col">Exp. Date:</th>
            <%--<th scope="col"></th>--%>
        </tr>
    </thead>
    <nested:iterate property="stockItem.lots" id="lot">
        <tr>
            <td><nested:write name="lot" property="lotCode"/></td>
            <td><nested:write name="lot" property="expirationDate" format="MM/dd/yyyy"/></td>
            <%--<td>
                <a href="?cmd=<%=EditStockItemForm.REMOVE_STOCK_ITEM_LOT%>&amp;lotCode=<nested:write name="lot" property='lotCode'/>&amp;lotExpDate=<nested:write name="lot" property="expirationDate" format="MM/dd/yyyy"/>&amp;page=1&amp;nextPage=1">Remove</a>
            </td>--%>
        </tr>
    </nested:iterate>

    <%--<tr>
        <td><nested:text property="lotCode" size="15"/></td>
        <td><nested:text property="lotExpDate" size="10" styleClass="dateInput"/></td>
        <td class="text-center"><a href="javascript:void(0);" onclick="addLot();">Add</a></td>
    </tr>--%>
</table>
<br/>
<label for="instructions">Handling Instructions:</label><br/>
<nested:textarea property="stockItem.instructions" rows="5" styleClass="form-control" styleId="instructions" disabled="true" readonly="true"/>

