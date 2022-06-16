<%@ include file="../include/tlds.jsp" %>

<%@ page import="us.mn.state.health.view.inventory.Command" %>

<html>
    <head>
     <link href="styles/main.css" rel="stylesheet" type="text/css" />
        <title>Order Line Item Receiving History</title>
        <script type="text/javascript" src="/js/isdate.js"></script>
    </head>
    
    <body>
        <br />
        <br />
        <br />
        <nested:form action="createDeliveryTicket" method="post" >
            <input type="HIDDEN" name="cmd" />
            <table cellspacing="5" cellpadding="1" border="0" width="100%">
                 <tr>
                    <td class="tablelabel" align="right" width="50%">Order Line Item ID:</td>
                    <td class="tabledetail" align="left" width="50%">
                        OLI<nested:write property="orderLineItem.orderLineItemId" name="deliveryTicketForm" />
                    </td>
                 </tr>
                 <tr>
                    <td class="tablelabel" align="right">Deliver-To:</td>
                    <td class="tabledetail" align="left">
                            <nested:select property="requestLineItemId" name="deliveryTicketForm">
                                <option value=""></option>
                                <nested:optionsCollection name="deliveryTicketForm" 
                                                          property="orderLineItem.requestLineItems" 
                                                          value="requestLineItemId" 
                                                          label="request.deliverToInfoAsString" />
                            </nested:select>
                    </td>
                 </tr>
                 <tr>
                    <td class="tablelabel" align="right" width="50%">Qty To Deliver:</td>
                    <td class="tabledetail" align="left" width="50%">
                        <nested:text property="qtyDelivered" name="deliveryTicketForm" size="5" />
                    </td>
                 </tr>
                 <tr>
                    <td class="tablelabel" align="right" width="50%">Expected Delivery Date:</td>
                    <td class="tabledetail" align="left" width="50%">
                        <nested:text property="expectedDeliveryDate" name="deliveryTicketForm" size="10" maxlength="10"
                                     onblur="if (isDate(this.value)==false){setTimeout('document.deliveryTicketForm.expectedDeliveryDate.focus();',1);}" />
                    </td>
                 </tr>
                 <tr>
                    <td class="tablelabel" align="right" width="50%">Number Of Boxes:</td>
                    <td class="tabledetail" align="left" width="50%">
                        <nested:text property="numberOfBoxes" name="deliveryTicketForm" size="5" />
                    </td>
                 </tr>
                 <tr>
                    <td colspan="2">&nbsp;</td>
                 </tr>
                 <tr>
                    <td align="right" width="50%"><input type="SUBMIT" value="Save" /></td>
                    <td align="left" width="50%">
                        <input type="BUTTON" value="Print" onclick="javascript: this.form.cmd.value='<%=Command.PRINT_DELIVERY_TICKET %>'; this.form.submit(); return false;" />
                    </td>
                 </tr>
            </table>
        </nested:form>

    </body>
</html>
