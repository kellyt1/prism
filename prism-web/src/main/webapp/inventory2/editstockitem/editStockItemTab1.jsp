<%@ include file="../../include/tlds.jsp" %>

<html>
    <head>
        <title>Edit Stock Item
            <nested:write name="editStockItemForm" property="stockItem.fullIcnbr"/>
            - <nested:write name="editStockItemForm" property="stockItem.description"/>
            - Created: <nested:write name="editStockItemForm" property="stockItem.insertionDate" format="MM/dd/yyyy"/>
        </title>
        <script type="text/javascript" src="${pageContext.request.contextPath}js/overlibmws/overlibmws.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_scroll.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/ajaxcontentmws.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/prismCommands.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_overtwo.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_shadow.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_draggable.js"></script>
        <script type="text/javascript">
            function addLocation() {
                setFormValue("cmd", '<%=EditStockItemForm.ADD_STOCK_ITEM_LOCATION%>');
                submitForm("editStockItemForm");
            }
            function addLot() {
                setFormValue("cmd", '<%=EditStockItemForm.ADD_STOCK_ITEM_LOT%>');
                submitForm("editStockItemForm");
            }
        </script>
    </head>
    <body>
        <br/>
        <fieldset>
            <legend>Edit Stock Item
                <nested:write name="editStockItemForm" property="stockItem.fullIcnbr"/>
                - <nested:write name="editStockItemForm" property="stockItem.description"/>
                - Created: <nested:write name="editStockItemForm" property="stockItem.insertionDate" format="MM/dd/yyyy"/>
            </legend>
            <nested:form action="inventory/editStockItem.do" method="post">
                <nested:hidden property="page" value="1"/>
                <nested:hidden property="nextPage"/>
                <nested:hidden property="cmd" value="<%=EditStockItemForm.UPDATE_ITEM%>"/>

                <div>
                    <ul class="nav nav-tabs" role="tablist">
                        <li><a href="javascript:void(0);" onclick="submitParameter('editStockItemForm',0,'/inventory/editStockItem.do','nextPage',0);">General Info</a></li>
                        <li class="active"><a href="javascript:submitForm('editStockItemForm');">Current Info</a></li>
                        <li><a href="javascript:void(0);" onclick="submitParameter('editStockItemForm',0,'/inventory/editStockItem.do','nextPage',2);">Vendor Info</a></li>
                        <li><a href="javascript:void(0);" onclick="submitParameter('editStockItemForm',0,'/inventory/editStockItem.do','nextPage',3);">History</a></li>
                    </ul>
                </div>
                <div>
                    <table class="table table-bordered">
                        <tr>
                            <td><%@ include file="cell6.jsp" %></td>
                            <td><%@ include file="cell7.jsp" %></td>
                            <td><%@ include file="cell8.jsp" %></td>
                        </tr>
                    </table>
                </div>
                <%@ include file="editStockItemFooter.jsp" %>
            </nested:form>
        </fieldset>
    </body>
</html>