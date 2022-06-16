<%@ include file="../include/tlds.jsp" %>

<html>
<head>
    <title>Purchasing Information</title>
</head>

<body>
<table cellspacing="2" cellpadding="3" border="1" width="100%" align='center'>
<tr>
    <th class='tableheader' colspan='2'>Order Details</th>
</tr>
<tr class="tabledetail">
    <td width="30%">OPR#:</td>
    <td><nested:write name="requestLineItemPurchaseDetailsForm"
                      property="order.orderId"/>
    </td>
</tr>
<tr class="tabledetail">
    <td width="30%">PO#:</td>
    <td><nested:write name="requestLineItemPurchaseDetailsForm"
                      property="order.purchaseOrderNumber"/>
    </td>
</tr>
<tr class="tabledetail">
    <td width="30%">PO# Type:</td>
    <td><nested:write name="requestLineItemPurchaseDetailsForm"
                      property="order.purchaseOrderNumberType"/>
    </td>
</tr>
<tr class="tabledetail">
    <td width="30%">Vendor Name:</td>
    <td><nested:write name="requestLineItemPurchaseDetailsForm"
                      property="order.vendor.externalOrgDetail.orgName"/>
    </td>
</tr>
<tr class="tabledetail">
    <td width="30%">Vendor Contract:</td>
    <td>
        <nested:notEmpty name="requestLineItemPurchaseDetailsForm" property="order.vendorContract">
            <nested:write name="requestLineItemPurchaseDetailsForm"
                      property="order.vendorContract.contractNumber"/>
        </nested:notEmpty>
        <nested:empty name="requestLineItemPurchaseDetailsForm" property="order.vendorContract">
            N/A
        </nested:empty>

    </td>
</tr>
<tr class="tabledetail">
    <td width="30%">Date Created:</td>
    <td><nested:write name="requestLineItemPurchaseDetailsForm"
                      property="order.insertionDate"/>
    </td>
</tr>
<tr class="tabledetail">
    <td width="30%">Buyer:</td>
    <td><nested:write name="requestLineItemPurchaseDetailsForm"
                      property="order.purchaser.firstAndLastName"/>
    </td>
</tr>
<tr class="tabledetail">
    <td width="30%">Ship To:</td>
    <td>
        <nested:notEmpty name="requestLineItemPurchaseDetailsForm" property="order.shipToAddress">
            <nested:write name="requestLineItemPurchaseDetailsForm"
                          property="order.shipToAddress.shortSummary"/>
        </nested:notEmpty>
        <nested:empty name="requestLineItemPurchaseDetailsForm" property="order.shipToAddress">N/A</nested:empty>
    </td>
</tr>
<tr class="tabledetail">
    <td width="30%">Buyer/Receiver's Notes:</td>
    <td>
        <table width="100%">
            <tr>
            <th width="60%">Message</th>
            <th width="20%">Author</th>
            <th width="20%">Date</th>
            </tr>
        <nested:iterate name="requestLineItemPurchaseDetailsForm"
                      property="order.orderNotes" id="on">
            <tr>
                <td><nested:write name="on" property="note"/></td>
                <td><nested:write name="on" property="authorName"/></td>
                <td><nested:write name="on" property="insertionDate"/></td>
            </tr>
        </nested:iterate>
        </table>
    </td>
</tr>
<tr>
    <th colspan="2">
        Order Line Item Details <nested:empty name="requestLineItemPurchaseDetailsForm" property="requestLineItem.orderLineItem">- N/A</nested:empty>
    </th>
    <nested:notEmpty name="requestLineItemPurchaseDetailsForm" property="requestLineItem.orderLineItem">
        <tr>
            <td>Item Description</td>
            <td><nested:write name="requestLineItemPurchaseDetailsForm" property="requestLineItem.orderLineItem.itemDescription"/> </td>
        </tr>
        <tr>
            <td>Buy Unit Cost</td>
            <td><nested:write name="requestLineItemPurchaseDetailsForm" property="requestLineItem.orderLineItem.buyUnitCost"/> </td>
        </tr>
        <tr>
            <td>Quantity</td>
            <td><nested:write name="requestLineItemPurchaseDetailsForm" property="requestLineItem.orderLineItem.quantity"/> </td>
        </tr>
        <tr>
            <td>Total Cost</td>
            <td><nested:write name="requestLineItemPurchaseDetailsForm" property="requestLineItem.orderLineItem.buyUnitTotalCost"/> </td>
        </tr>
        <tr>
            <td>Vendor Catalog #</td>
            <td><nested:write name="requestLineItemPurchaseDetailsForm" property="requestLineItem.orderLineItem.vendorCatalogNbr"/> </td>
        </tr>
        <tr>
            <td>Status</td>
            <td><nested:write name="requestLineItemPurchaseDetailsForm" property="requestLineItem.orderLineItem.status.name"/> </td>
        </tr>
        <nested:equal value="true" name="requestLineItemPurchaseDetailsForm" property="requestLineItem.orderLineItem.received">
            <tr>
                <td>Receiving Details</td>
                <td>
                    <table width="100%">
                        <tr>
                            <th>Qty</th>
                            <th>Date</th>
                            <th>Facility</th>
                            <th>assetNumber</th>
                            <th>Rec'd By</th>
                        </tr>
                        <nested:iterate name="requestLineItemPurchaseDetailsForm" property="requestLineItem.orderLineItem.receivingDetails" id="rd">
                            <tr>
                                <td><nested:write name="rd" property="quantityReceived"/></td>
                                <td><nested:write name="rd" property="dateReceived"/></td>
                                <td><nested:write name="rd" property="facility.facilityName"/></td>
                                <td><nested:write name="rd" property="assetNumber"/></td>
                                <td><nested:write name="rd" property="receivedBy.firstAndLastName"/></td>
                            </tr>
                        </nested:iterate>
                    </table>
                </td>
            </tr>
        </nested:equal>

    </nested:notEmpty>
</tr>

</table>
<div align="center">
    <a href="javascript:window.close();">Close window</a>
</div>
</body>
</html>