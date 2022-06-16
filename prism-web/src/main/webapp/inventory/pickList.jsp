<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>

<html>
    <head>
        <title>Stock Item Pick List</title>
        <script type="text/javascript">
            function printAll(ilocation) {
                //  requestFormCollection;
                var currentTime = new Date();
                var sSuffix = currentTime.getTime();
                var form = document.forms[0];
                var optPicked = false;
                var lHeight = document.height * .8;
                var lWidth = document.width * .8;
                if (ilocation == 1) {
                    if (form.printpicklist.checked) {
                        optPicked = true;
                        window.open('fillStockRequests.do?cmd=printPickList&dTime=' + sSuffix, 'PickList', ' HEIGHT=' + lHeight + ', WIDTH=' + lWidth + ', left=50, top=50, resizable=1');
                    }
                    if (form.printpackinglist.checked) {
                        optPicked = true;
                        window.open('fillStockRequests.do?cmd=printPackingSlips&dTime=' + sSuffix, 'PackingList', ' HEIGHT=' + lHeight + ', WIDTH=' + lWidth + ', left=65, top=65, resizable=1');
                    }
                    if (form.printmailinglabels.checked) {
                        optPicked = true;
                        window.open('fillStockRequests.do?cmd=printMailingLabels&dTime=' + sSuffix, 'PrintMailing', ' HEIGHT=' + lHeight + ', WIDTH=' + lWidth + ' left=80, top=80, resizable=1');
                    }
                }
                if (ilocation == 2) {
                    if (form.printpicklist2.checked) {
                        optPicked = true;
                        window.open('fillStockRequests.do?cmd=printPickList&dTime=' + sSuffix, 'PickList', ' HEIGHT=' + lHeight + ', WIDTH=' + lWidth + ', left=50, top=50, resizable=1');
                    }
                    if (form.printpackinglist2.checked) {
                        optPicked = true;
                        window.open('fillStockRequests.do?cmd=printPackingSlips&dTime=' + sSuffix, 'PackingList', ' HEIGHT=' + lHeight + ', WIDTH=' + lWidth + ', left=65, top=65, resizable=1');
                    }
                    if (form.printmailinglabels2.checked) {
                        optPicked = true;
                        window.open('fillStockRequests.do?cmd=printMailingLabels&dTime=' + sSuffix, 'PrintMailing&dTime', ' HEIGHT=' + lHeight + ', WIDTH=' + lWidth + ' left=80, top=80, resizable=1');
                    }
                }
                if (!optPicked) alert('Nothing selected. Please choose something to print');

            }
        </script>
    </head>
    <% int aSize = 0; %>
    <body>

    <!--<h1>The Packing Slips Reports are disabled temporarily!</h1>-->
    <nested:form action="/fillStockRequests" method="post">
    <nested:hidden property="cmd" value=""/>
    <nested:iterate id="materialsRequest" name="requestFormCollection" property="cartRequestForms" indexId="a">
        <%--<nested:size id="size" property="cartRequestForms[0].requestLineItemForms"/>--%>
        <nested:size id="xsize" property="requestLineItemForms"/>
        <%--<nested:write name="xsize"/>--%>
        <% aSize += xsize; %>
        <%--<nested:size id="size" property="requestLineItemForms"/>--%>
    </nested:iterate>
    <%--//Count of ASIZE <%= aSize %>--%>
    <% pageContext.setAttribute("aSize", aSize); %>

    <logic:greaterThan name="aSize" value="3">
        <div align="right">
            <table cellpadding=5>
                <tr>
                    <td>
                        <table border="1" cellpadding="3">
                            <th colspan="3">Print Options</th>
                            <tr>
                                <td><label><input type="checkbox" value="Y" name="printpicklist" checked>Print Pick List</label></td>
                                <td><label><input type="checkbox" value="Y" name="printpackinglist" checked>Print Packing Slips</label></td>
                                <td><label><input type="checkbox" value="Y" name="printmailinglabels" checked>Print Mailing Labels</label></td>
                                <!--onclick="window.open('fillStockRequests.do?cmd=printPickList')" />-->
                                <!--onclick="window.open('fillStockRequests.do?cmd=printPackingSlips')" />-->
                                <!--onclick="window.open('fillStockRequests.do?cmd=printMailingLabels')" />-->
                            </tr>
                            <tr>
                                <td colspan="3" align="center"><input type="button" value="Print Selected Options" onclick="printAll(1);"></td>
                            </tr>
                        </table>
                    </td>
                    <td width="15"></td>
                </tr>
                <tr>
                    <td align="center">
                        <html:submit value="Deduct Inventory (Submit)"/>
                    </td>
                </tr>
            </table>

        </div>
    </logic:greaterThan>
    <logic:lessEqual name="aSize" value="3">
        <td><input type="hidden" value="false" name="printpicklist"></td>
        <td><input type="hidden" value="false" name="printpackinglist"></td>
        <td><input type="hidden" value="false" name="printmailinglabels"></td>
    </logic:lessEqual>

    <table class="table table-bordered table-striped sortable"  id="idSort">
    <tr>
        <td align="center">Request #:</td>
        <td align="center">Deliver To:</td>
        <td align="left">Location:</td>
        <td align="left">ICNBR:</td>
        <td align="left">Description:</td>
        <td align="center">Dispense<br/>Unit:</td>
        <td align="center">Qty<br/>On-Hand:</td>
        <td align="center">Current<br/>Demand:</td>
        <td align="center">Qty <br/>Requested:</td>
        <td align="center">Qty<br/>Shipped:</td>
        <td align="center">Qty<br/>Picked:</td>
    </tr>
    <nested:iterate id="materialsRequest" name="requestFormCollection" property="cartRequestForms" indexId="a">
        <nested:iterate id="rliForm" property="requestLineItemForms">
            <tr>
                <td align="center" nowrap="nowrap">
                    <nested:write name="materialsRequest" property="trackingNumber"/>
                </td>
                <td align="left">
                    <nested:present name="materialsRequest" property="deliveryDetail">
                        <table cellspacing="0">
                            <tr>
                                <td nowrap="nowrap">
                                    <nested:present name="materialsRequest" property="deliveryDetail.organization">
                                        <nested:write name="materialsRequest"
                                                      property="deliveryDetail.organization.orgName"/>
                                    </nested:present>
                                    <nested:notPresent name="materialsRequest" property="deliveryDetail.organization">
                                        <nested:present property="deliveryDetail.facility">
                                            MDH
                                        </nested:present>
                                    </nested:notPresent>
                                </td>
                            </tr>
                            <tr>
                                <td nowrap="nowrap">
                                    <nested:write name="materialsRequest" property="deliveryDetail.recipientName"/>
                                </td>
                            </tr>
                            <nested:present name="materialsRequest" property="deliveryDetail.facility">
                                <tr>
                                    <td nowrap="nowrap">
                                        <nested:write name="materialsRequest" property="deliveryDetail.facility.facilityName"/>
                                    </td>
                                </tr>
                            </nested:present>
                            <nested:present name="materialsRequest" property="deliveryDetail.mailingAddress">
                                <tr>
                                    <td nowrap="nowrap">
                                        <nested:write name="materialsRequest" property="deliveryDetail.mailingAddress.address1"/>
                                        <br/>
                                        <nested:present name="materialsRequest" property="deliveryDetail.mailingAddress.address2">
                                            <nested:write name="materialsRequest" property="deliveryDetail.mailingAddress.address2"/>
                                            <br/>
                                        </nested:present>
                                        <nested:write name="materialsRequest" property="deliveryDetail.mailingAddress.city"/>
                                        ,
                                        <nested:write name="materialsRequest" property="deliveryDetail.mailingAddress.state"/>
                                        &nbsp;
                                        <nested:write name="materialsRequest" property="deliveryDetail.mailingAddress.zip"/>
                                    </td>
                                </tr>
                            </nested:present>
                        </table>
                    </nested:present>
                </td>
                <td align="left">
                    <nested:iterate id="location" name="rliForm" property="stockItem.locations">
                        <nested:write name="location" property="facility.facilityCode"/>
                        -
                        <nested:write name="location" property="locationCode"/>
                        <br/>
                    </nested:iterate>
                </td>
                <td align="left" nowrap="nowrap"><nested:write property="stockItem.fullIcnbr"/></td>
                <td align="left"><nested:write property="stockItem.description"/></td>
                <td align="center" nowrap="nowrap"><nested:write property="stockItem.dispenseUnit.name"/></td>
                <td align="center" nowrap="nowrap"><nested:write property="stockItem.qtyOnHand"/></td>
                <td align="center"><nested:write property="item.currentDemand"/></td>
                <td align="center"><nested:write property="quantity"/></td>
                <td align="center"><nested:write property="quantityFilled"/></td>
                <td align="center" nowrap="nowrap"><nested:text property="quantityPicked" size="5"/></td>
            </tr>
        </nested:iterate>
    </nested:iterate>
    </table>
    <br/>

    <div align="Center">
        <table border="1">
            <th colspan="3" class="tableheader">Print Options</th>
            <tr>
                <td><label><input type="checkbox" value="Y" name="printpicklist2" checked> Print Pick List</label></td>
                <td><label><input type="checkbox" value="Y" name="printpackinglist2" checked> Print Packing Slips</label></td>
                <td><label><input type="checkbox" value="Y" name="printmailinglabels2" checked> Print Mailing Labels</label></td>
            </tr>
            <tr>
                <td colspan="3" align="center"><input type="button" value="Print Selected Options" onclick="printAll(2);">
                </td>
            </tr>
        </table>
        <p align="center">
            <html:submit value="Deduct Inventory (Submit)"/>
        </p>
    </div>
    </nested:form>
    </body>
</html>