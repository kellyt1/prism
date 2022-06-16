<%@ include file="../../include/tlds.jsp" %>
<label>Item Description:</label>
<nested:textarea property="itemDescription"/>
<label>Category:</label>
<nested:write property="stockItem.category.codeAndName"/>
<label>Hazardous:</label>
<nested:empty property="stockItem.hazardousObject">
    N/A
</nested:empty>
<nested:notEmpty property="stockItem.hazardousObject">
    <nested:write property="stockItem.hazardousObject.description"/>
</nested:notEmpty>
<label>Attach Documents/Upload File:</label>
<nested:file name="editStockItemForm" property="printSpecFile" style="width:200px"/>

<nested:notEmpty property="stockItem.attachedFiles" >
    <% java.util.Date dt = new java.util.Date (); %>
    <br>
    <i class="attachment">Download/Open File(s): <br>
    <nested:iterate property="stockItem.attachedFiles" id="attachedFile" >
        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<a href=${pageContext.request.contextPath}/inventory/downloadFileAction.do?attachedFileId=<nested:write name="attachedFile" property='attachedFileId'/>&dt=<%=dt.getTime()%> ><nested:write name="attachedFile" property='fileName'/> </a>
        <br>
    </nested:iterate>
    </i>
</nested:notEmpty>