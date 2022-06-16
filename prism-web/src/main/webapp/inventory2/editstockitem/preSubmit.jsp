<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemForm" %>

<html>
    <head>
        <title>Edit Stock Item
            <nested:write name="editStockItemForm" property="stockItem.fullIcnbr"/>
            -
            <nested:write name="editStockItemForm" property="stockItem.description"/>
        </title>
        <script type="text/javascript" src="${pageContext.request.contextPath}js/overlibmws/overlibmws.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_scroll.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/ajaxcontentmws.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/prismCommands.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_overtwo.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_shadow.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_draggable.js"></script>
    </head>
    <body>
        <fieldset>
            <legend>Edit Stock Item
                <nested:write name="editStockItemForm" property="stockItem.fullIcnbr"/>
                - <nested:write name="editStockItemForm" property="stockItem.description"/>
            </legend>
            <nested:form action="inventory/editStockItem.do" method="post">
                <nested:hidden property="page" value="0"/>
                <nested:hidden property="nextPage"/>
                <nested:hidden property="cmd" value="<%=EditStockItemForm.SUBMIT%>"/>
                <div id="content">
                    <table cellspacing="20">
                        <tr align="left" valign="top">
                            <td>
                                <label>History Comments:</label>
                                <nested:textarea property="historyComments" cols="40" rows="7"/>
                            </td>
                        </tr>
                    </table>
                </div>
            </nested:form>
        </fieldset>
    </body>
</html>