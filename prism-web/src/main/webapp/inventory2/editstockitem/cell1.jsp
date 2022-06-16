<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemAction" %>
<%@ include file="../../include/tlds.jsp" %>

<label for="itemDescription">Item Description:</label><br/>
<nested:textarea property="itemDescription" rows="5" styleId="itemDescription" styleClass="form-control" disabled="true" readonly="true"/><br/>

<label for="category">Category:</label>
<span class="pull-right">
    <nested:select property="categoryId" style="width:200px; float: right;" styleId="category" styleClass="chosen-select" disabled="true">
        <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="categories" label="codeAndName" value="categoryId"/>
    </nested:select>
</span>

<br/><br/>

<label for="hazardousId">Hazardous:</label>
<span class="pull-right">
    <nested:select property="hazardousId" style="min-width: 60px; float: right;" styleClass="chosen-select" styleId="hazardousId" disabled="true">
        <option value="">N/A</option>
        <nested:optionsCollection name="<%=EditStockItemAction.LISTS_DTO%>" property="hazardouses" label="description" value="hazardousId"/>
    </nested:select>
</span><br/><br/>

<label for="printSpecFile">Attach Documents/Upload File:</label>
<nested:file name="editStockItemForm" property="printSpecFile" styleId="printSpecFile" disabled="true"/><br/>

<nested:notEmpty property="stockItem.attachedFiles" >
    <% java.util.Date dt = new java.util.Date (); %>
    <br>
    <em class="attachment">
        Download/Open File(s):<br/>
        <nested:iterate property="stockItem.attachedFiles" id="attachedFile" >
            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<a href=${pageContext.request.contextPath}/inventory/downloadFileAction.do?attachedFileId=<nested:write name="attachedFile" property='attachedFileId'/>&dt=<%=dt.getTime()%> ><nested:write name="attachedFile" property='fileName'/> </a>
            <br>
        </nested:iterate>
    </em>
</nested:notEmpty>