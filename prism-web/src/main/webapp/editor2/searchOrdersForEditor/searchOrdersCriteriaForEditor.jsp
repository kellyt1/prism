<%@ include file="../../include/tlds.jsp" %>
<script type="text/javascript">
    function clearForm() {
        var form = window.document.forms[0];
        form.elements['searchCriteria.oprNo'].value = "";
        form.elements['searchCriteria.poNo'].value = "";
        form.elements['searchCriteria.buyerId'].value = "";
        form.elements['searchCriteria.statusId'].value = "";
        form.elements['searchCriteria.requestorId'].value = "";
        form.elements['searchCriteria.itemDescription'].value = "";
        form.elements['searchCriteria.vendorName'].value = "";
        form.elements['searchCriteria.orderedTo'].value = "";
        form.elements['searchCriteria.orderedFrom'].value = "";
        form.elements['searchCriteria.suspenseDateFrom'].value = "";
        form.elements['searchCriteria.suspenseDateTo'].value = "";
        form.elements['searchCriteria.requestTrackingNo'].value = "";
        form.elements['searchCriteria.icnbr'].value = "";
        form.elements['searchCriteria.itemModel'].value = "";
        form.elements['searchCriteria.orgCode'].value = "";
    }
</script>
<table class="table">
    <tfoot>
    <tr>
        <td colspan="6" align="center">
            <%--<nested:submit onclick="javascript:search();"/>--%>
            <button onclick="search();" class="btn btn-default">Search</button>
            <button onclick="clearForm();" class="btn btn-default">Clear</button>
        </td>
    </tr>
    </tfoot>
<tr>
<td>
    <table>

        <tr>
            <td align="right">OPR#:</td>
            <td><nested:text property="searchCriteria.oprNo"/></td>
        </tr>
        <tr>
            <td align="right">PO#:</td>
            <td><nested:text property="searchCriteria.poNo"/></td>
        </tr>
        <tr>
            <td align="right">Buyer:</td>
            <td>
                <nested:select property="searchCriteria.buyerId" styleClass="chosen-select">
                    <option value="">All</option>
                    <nested:optionsCollection name="lists" property="buyers" label="lastAndFirstName" value="personId"/>
                </nested:select>
            </td>
        </tr>
        <tr>
            <td align="right">Status:</td>
            <td>
                <nested:select property="searchCriteria.statusId" style="width:160px" styleClass="chosen-select">
                    <option value="">All</option>
                    <nested:optionsCollection name="lists" property="statuses" label="name" value="statusId"/>
                </nested:select>
            </td>
        </tr>
        <tr>
            <td align="right">Requestor:</td>
            <td>
                <nested:select property="searchCriteria.requestorId" style="width:160px" styleClass="chosen-select">
                    <option value="">All</option>
                    <nested:optionsCollection name="lists" property="requestors" label="lastAndFirstName" value="personId"/>
                </nested:select>
            </td>
        </tr>
    </table>
</td>
<td>
    <table>
        <tr>
            <td>Item Description:</td>
            <td>
                <nested:text property="searchCriteria.itemDescription" size="30"/>
            </td>
        </tr>
        <tr>
            <td>Vendor Name:</td>
            <td>
                <nested:text property="searchCriteria.vendorName" size="30"/>
            </td>
        </tr>
        <tr>
            <td>Ordered Between:</td>
            <td>
                <nested:text property="searchCriteria.orderedFrom" maxlength="10" size="10" styleClass="dateInput"/> and
                <nested:text property="searchCriteria.orderedTo" maxlength="10" size="10" styleClass="dateInput"/>
            </td>
        </tr>
        <tr>
            <td>Suspense Date Between:</td>
            <td>
                <nested:text property="searchCriteria.suspenseDateFrom" maxlength="10" size="10" styleClass="dateInput"/> and
                <nested:text property="searchCriteria.suspenseDateTo" maxlength="10" size="10" styleClass="dateInput"/>
            </td>
        </tr>
    </table>
</td>
<td>
    <table>
        <tr>
            <td align="right"> MRQ#:</td>
            <td>MRQ-
                <nested:text property="searchCriteria.requestTrackingNo" size="6"/>
            </td>
        </tr>
        <tr>
            <td align="right">ICNBR:</td>
            <td>
                <nested:text property="searchCriteria.icnbr" size="11"/>
            </td>
        </tr>
        <tr>
            <td align="right">Catalog #:</td>
            <td>
                <nested:text property="searchCriteria.itemModel" size="11"/>
            </td>
        </tr>
        <tr>
            <td align="right">Budget Code:</td>
            <td>
                <nested:text property="searchCriteria.orgCode" size="11"/>
            </td>
        </tr>
    </table>
</td>
</tr>
</table>