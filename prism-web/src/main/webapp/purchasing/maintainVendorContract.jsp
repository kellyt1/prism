<%@ include file="../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.view.purchasing.Command" %>

<html>

<head>
    <title>Maintain Contract</title>
</head>

<body>
<html:form action="/editVendorContract.do" method="post">
<table align="center" cellspacing="0" cellpadding="0" border="0" width="90%">
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <nested:notEmpty name="vendorsForm" property="vendorContractId">
            <th class="tableheader">Edit Contract</th>
        </nested:notEmpty>
        <nested:empty property="vendorContractId">
            <th class="tableheader">New Contract</th>
        </nested:empty>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr align="center">
        <td>
            <table align="center" cellspacing="0" cellpadding="3" border="0">
                <tr>
                    <td align="right" class="tablelabel">Contract Number:</td>
                    <td>
                        <nested:text property="currentVendorContractForm.contractNumber"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" class="tablelabel">Contract Start Date:</td>
                    <td>
                            <nested:text property="currentVendorContractForm.startDate" styleClass="dateInput"/>
                        </td>
                </tr>
                <tr>
                    <td align="right" class="tablelabel">Contract End Date :</td>
                    <td>
                            <nested:text property="currentVendorContractForm.endDate" styleClass="dateInput"/>
                        </td>
                </tr>
                <tr>
                    <td align="right" class="tablelabel">Contract Comments:</td>
                    <td>
                        <nested:textarea property="currentVendorContractForm.comments"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" class="tablelabel">Contract Delivery Terms:</td>
                    <td>
                        <nested:textarea property="currentVendorContractForm.deliveryTerms"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <html:hidden property="cmd"/>
                        <nested:notEmpty name="vendorsForm" property="vendorContractId">
                            <input type="SUBMIT" value="Save"
                                   onclick="this.form.action='editVendorContract.do';
                                this.form.cmd.value = '<%=Command.MAINTAIN_VENDOR_CONTRACTS%>';
                                this.form.submit(); return false;"/>
                        </nested:notEmpty>
                        <nested:empty name="vendorsForm" property="vendorContractId">
                            <input type="SUBMIT" value="Save"
                                   onclick="this.form.action='addVendorContract.do';
                                this.form.cmd.value = '<%=Command.MAINTAIN_VENDOR_CONTRACTS%>';
                                this.form.submit(); return false;"/>
                        </nested:empty>
                        <button onclick="this.form.action='viewEditVendor.do';
                                this.form.cmd.value='backToEditVendor';
                                document.getElementsByName('currentVendorContractForm.contractNumber')[0].value='<nested:write property="currentVendorContractForm.contractNumber"/>';
                                document.getElementsByName('currentVendorContractForm.startDate')[0].value='<nested:write property="currentVendorContractForm.startDate"/>';
                                document.getElementsByName('currentVendorContractForm.endDate')[0].value='<nested:write property="currentVendorContractForm.endDate"/>';
                                document.getElementsByName('currentVendorContractForm.comments')[0].value='<nested:write property="currentVendorContractForm.comments"/>';
                                document.getElementsByName('currentVendorContractForm.deliveryTerms')[0].value='<nested:write property="currentVendorContractForm.deliveryTerms"/>';
                                this.form.submit(); return false;">Cancel</button>
                    </td>

            </table>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
</table>
</html:form>
</body>
</html>
