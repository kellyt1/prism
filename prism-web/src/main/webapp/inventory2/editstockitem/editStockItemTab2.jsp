<%@ include file="../../include/tlds.jsp" %>

<html>
    <head>
        <title>Edit Stock Item
            <nested:write name="editStockItemForm" property="stockItem.fullIcnbr"/>
            - <nested:write name="editStockItemForm" property="stockItem.description"/>
            - Created: <nested:write name="editStockItemForm" property="stockItem.insertionDate" format="MM/dd/yyyy"/>
        </title>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_scroll.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/ajaxcontentmws.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/prismCommands.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_overtwo.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_shadow.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_draggable.js"></script>
        <script type="text/javascript">
            function addVendor() {
                setFormValue("cmd", '<%=EditStockItemForm.ADD_VENDOR%>');
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
                <nested:hidden property="page" value="2"/>
                <nested:hidden property="nextPage"/>
                <nested:hidden property="cmd" value="<%=EditStockItemForm.UPDATE_ITEM%>"/>

                <div>
                    <ul class="nav nav-tabs" role="tablist">
                        <li><a href="javascript:void(0);" onclick="submitParameter('editStockItemForm',0,'/inventory/editStockItem.do','nextPage',0);">General Info</a></li>
                        <li><a href="javascript:void(0);" onclick="submitParameter('editStockItemForm',0,'/inventory/editStockItem.do','nextPage',1);">Current Info</a></li>
                        <li class="active"><a href="javascript:submitForm('editStockItemForm');">Vendor Info</a></li>
                        <li><a href="javascript:void(0);" onclick="submitParameter('editStockItemForm',0,'/inventory/editStockItem.do','nextPage',3);">History</a></li>
                    </ul>
                </div>
                <div>
                    <table class="table table-bordered">
                        <tr><td><%@ include file="cell9.jsp" %></td></tr>
                    </table>
                </div>
                <%@ include file="editStockItemFooter.jsp" %>
            </nested:form>
        </fieldset>
    </body>
</html>