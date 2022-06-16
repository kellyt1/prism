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
            <nested:form action="inventory/editStockItem.do" method="post" enctype="multipart/form-data">
                <nested:hidden property="page" value="0"/>
                <nested:hidden property="nextPage"/>
                <nested:hidden property="cmd" value="<%=EditStockItemForm.UPDATE_ITEM%>"/>

                <div>
                    <ul class="nav nav-tabs" role="tablist">
                        <li class="active"><a href="javascript:submitForm('editStockItemForm');">General Info</a></li>
                        <li><a href="javascript:void(0);" onclick="submitParameter('editStockItemForm',0,'/inventory/editStockItem.do','nextPage',1);">Current Info</a></li>
                        <li><a href="javascript:void(0);" onclick="submitParameter('editStockItemForm',0,'/inventory/editStockItem.do','nextPage',2);">Vendor Info</a></li>
                        <li><a href="javascript:void(0);" onclick="submitParameter('editStockItemForm',0,'/inventory/editStockItem.do','nextPage',3);">History</a></li>
                    </ul>
                </div>
                <div>
                    <table class="table table-bordered">
                        <tr>
                            <td><%@ include file="cell1.jsp" %></td>
                            <td><%@ include file="cell2.jsp" %></td>
                            <td><%@ include file="cell3.jsp" %></td>
                            <td><%@ include file="cell4.jsp" %></td>
                            <td><%@ include file="cell5.jsp" %></td>
                        </tr>
                    </table>
                </div>
                <%@ include file="editStockItemFooter.jsp" %>
            </nested:form>
        </fieldset>
    </body>
</html>