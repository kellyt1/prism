<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemForm" %>
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
                <nested:hidden property="page" value="3"/>
                <nested:hidden property="nextPage"/>
                <nested:hidden property="cmd" value="<%=EditStockItemForm.UPDATE_ITEM%>"/>

                <div>
                    <ul class="nav nav-tabs" role="tablist">
                        <li><a href="javascript:void(0);" onclick="submitParameter('editStockItemForm',0,'/inventory/editStockItem.do','nextPage',0);">General Info</a></li>
                        <li><a href="javascript:void(0);" onclick="submitParameter('editStockItemForm',0,'/inventory/editStockItem.do','nextPage',1);">Current Info</a></li>
                        <li><a href="javascript:void(0);" onclick="submitParameter('editStockItemForm',0,'/inventory/editStockItem.do','nextPage',2);">Vendor Info</a></li>
                        <li class="active"><a href="javascript:submitForm('editStockItemForm');">History</a></li>
                    </ul>
                </div>
                <div>
                    <ul>
                        <li>
                            <a href="javascript:void(0);"
                               onclick="return OLgetAJAX('/inventory/getStockItemHistory.do?stockItemId=<nested:write property='stockItem.itemId'/>&icnbr=<nested:write property="stockItem.fullIcnbr"/>',
                                                     displayStockItemHistory, 300, 'ovfl1');">Stock Item History</a>
                        </li>
                        <li>
                            <a href="javascript:void(0);"
                               onclick="return OLgetAJAX('/inventory/getStockItemQtyAdjustmentHistory.do?stockItemId=<nested:write property='stockItem.itemId'/>&icnbr=<nested:write property="stockItem.fullIcnbr"/>',
                                                     displayStockQtyAdjustmentHistory, 300, 'ovfl1');">Stock Quantity Adjustment History</a>
                        </li>
                        <li>
                            <a href="javascript:void(0);"
                               onclick="return OLgetAJAX('/inventory/getStockItemLocationHistory.do?stockItemId=<nested:write property='stockItem.itemId'/>&icnbr=<nested:write property="stockItem.fullIcnbr"/>',
                                                     displayStockItemLocationHistory, 300, 'ovfl1');">Stock Item Location History</a>
                        </li>
                        <li>
                            <a href="javascript:void(0);"
                               onclick="return OLgetAJAX('/inventory/getItemVendorLinkHistory.do?stockItemId=<nested:write property='stockItem.itemId'/>&icnbr=<nested:write property="stockItem.fullIcnbr"/>',
                                                     displayItemVendorLinkHistory, 300, 'ovfl1');">Item Vendor Link History</a>
                        </li>
                    </ul>
                </div>
                <%@ include file="editStockItemFooter.jsp" %>
            </nested:form>
        </fieldset>
    </body>
</html>