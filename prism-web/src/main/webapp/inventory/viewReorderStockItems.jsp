<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<%@ page import="us.mn.state.health.model.common.Status"%>

<html>
    <head>
        <%
            String msg = (String)request.getAttribute("message");
            if(msg == null) {
                msg = "";
            }
        %>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_scroll.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/ajaxcontentmws.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/prismCommands.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_overtwo.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_shadow.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/overlibmws/overlibmws_draggable.js"></script>

        <title>Re-Order Stock Items</title>
    </head>
    <body>
        <p class="text-center">Re-Order Stock Items</p>
        <br />
        <form action="searchStockItemsForReorder.do" method="POST">
            <div class="row">
                <div class="col-md-4 col-md-offset-4">
                    <label for="keywords">Keyword Search</label>
                    <div class="input-group">
                        <nested:text name="stockItemsForm" property="query" size="50" styleClass="form-control" styleId="keywords"/>
                        <div class="input-group-btn">
                            <label for="search" style="display: none;">Search</label>
                            <button class="btn btn-default" type="submit" id="search"><i class="glyphicon glyphicon-search"></i></button>
                        </div>
                    </div>
                    <label><input type="checkbox" name="ckShowInactive" value="true"> Show all items regardless of status.</label>
                </div>
            </div>
        </form>
        <br/>
        <form action="addToReorderList.do" method="POST">
            <input type="hidden" name="stockItemId" id="stockItemId"/>
            <table class="table table-bordered table-striped">
                <thead>
                    <% if(!("".equals(msg))) { %>
                        <tr>
                            <td colspan="9" class="text-center success"><%=msg%></td>
                        </tr>
                    <% } %>
                    <tr>
                        <td colspan="7" align="right">
                            <button type="submit" onclick="this.form.action='viewStockReorderCheckout.do';this.form.submit(); return false;" class="btn btn-default">Proceed To Checkout</button>
                        </td>
                        <td colspan="2" align="right">
                            <nested:greaterThan name="stockItemsForm" property="firstResult" value="0">
                                <a href="viewReorderStockItems.do?paginationDirection=prev">&lt; Prev</a> |
                            </nested:greaterThan>
                            <nested:equal name="stockItemsForm" property="displayNextLink" value="true">
                                <a href="viewReorderStockItems.do?paginationDirection=next">Next &gt;</a>
                            </nested:equal>
                        </td>
                    </tr>
                    <tr>
                        <th scope="col">ICNBR</th>
                        <th scope="col">Unit</th>
                        <th scope="col">Item Description</th>
                        <th scope="col" class="text-right">Qty On Hand</th>
                        <th scope="col" class="text-right">Current Demand</th>
                        <th scope="col" class="text-right">ROP</th>
                        <th scope="col" class="text-right">ROQ</th>
                        <th scope="col" class="text-center">Already On-Order?</th>
                        <%--<th class="text-center"><html:submit value="Add To Re-Order List" /></th>--%>
                        <th class="text-center"><button type="submit" class="btn btn-default">Add To Re-Order List</button></th>
                    </tr>
                </thead>
                <tfoot>
                <tr>
                    <td colspan="7" align="right">
                        <button type="submit" onclick="this.form.action='viewStockReorderCheckout.do';this.form.submit(); return false;" class="btn btn-default">Proceed To Checkout</button>
                    </td>
                    <td colspan="2" align="right">
                        <nested:greaterThan name="stockItemsForm" property="firstResult" value="0">
                            <a href="viewReorderStockItems.do?paginationDirection=prev">&lt; Prev</a> |
                        </nested:greaterThan>
                        <nested:equal name="stockItemsForm" property="displayNextLink" value="true">
                            <a href="viewReorderStockItems.do?paginationDirection=next">Next &gt;</a>
                        </nested:equal>
                    </td>
                </tr>
                </tfoot>
                <nested:iterate id="stockItemForm" name="stockItemsForm" property="stockItemForms" indexId="a" >
                    <tr>
                        <td><nested:write property="stockItem.fullIcnbr" name="stockItemForm" /></td>
                        <td><nested:write property="stockItem.dispenseUnit.name" name="stockItemForm" /></td>
                        <td><nested:write property="stockItem.description" name="stockItemForm" /></td>
                        <td class="text-right"><nested:write property="stockItem.qtyOnHand" name="stockItemForm" format="#,###" /></td>
                        <td class="text-right"><nested:write property="stockItem.currentDemand" name="stockItemForm" format="#,###" /></td>
                        <td class="text-right"><nested:write property="stockItem.reorderPoint" name="stockItemForm" format="#,###" /></td>
                        <td class="text-right"><nested:write property="stockItem.reorderQty" name="stockItemForm" format="#,###" /></td>
                        <td class="text-right">
                            <nested:equal property="stockItem.isOnOrder" value="true">
                                YES
                                <a href="javascript:void(0);"
                                   onclick="return OLgetAJAX('/inventory/getStockItemIsOnOrderInformation.do?stockItemId=<nested:write property='stockItem.itemId'/>&icnbr=<nested:write property="stockItem.fullIcnbr" name="stockItemForm" />',
                                 displayStockItemIsOnOrderInformation, 300, 'ovfl1');">
                                    (Details)
                                </a>
                            </nested:equal>
                          <nested:equal property="stockItem.isOnOrder" value="false">NO</nested:equal>
                        </td>
                        <td align="center">
                            <nested:equal value="<%=Status.ACTIVE%>" property="stockItem.status.statusCode">
                                <nested:checkbox property="selected" disabled="false" /> <nested:write property="stockItem.status.name"/>
                            </nested:equal>
                            <nested:notEqual value="<%=Status.ACTIVE%>" property="stockItem.status.statusCode">
                                <nested:checkbox property="selected" disabled="true" /> <nested:write property="stockItem.status.name"/>
                            </nested:notEqual>
                        </td>
                    </tr>
                </nested:iterate>
            </table>
        </form>
    </body>
</html>