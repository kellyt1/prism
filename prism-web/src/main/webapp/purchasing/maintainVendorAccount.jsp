<%@ include file="../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.view.purchasing.Command" %>

<html>

<head>
    <title>Maintain Account</title>
</head>

<body>
<html:form action="/editVendorAccount.do" method="post">
<table align="center" cellspacing="0" cellpadding="0" border="0" width="90%">
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <nested:notEmpty name="vendorsForm" property="vendorAccountId">
            <th class="tableheader">Edit Account</th>
        </nested:notEmpty>
        <nested:empty property="vendorAccountId">
            <th class="tableheader">New Account</th>
        </nested:empty>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr align="center">
        <td>
            <table align="center" cellspacing="0" cellpadding="3" border="0">
                <tr>
                    <td align="right" class="tablelabel">Account Number:</td>
                    <td>
                        <nested:text property="currentVendorAccountForm.accountNbr"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <html:hidden property="cmd"/>
                        <nested:notEmpty name="vendorsForm" property="vendorAccountId">
                            <input type="SUBMIT" value="Save"
                                   onclick="this.form.action='editVendorAccount.do';
                                this.form.cmd.value = 'maintainVendorAccounts';
                                this.form.submit(); return false;"/>
                        </nested:notEmpty>
                        <nested:empty name="vendorsForm" property="vendorAccountId">
                            <input type="SUBMIT" value="Save"
                                   onclick="this.form.action='addVendorAccount.do';
                                this.form.cmd.value = 'maintainVendorAccounts';
                                this.form.submit(); return false;"/>
                        </nested:empty>
                        <button onclick="this.form.action='viewEditVendor.do';
                                this.form.cmd.value='backToEditVendor';
                                document.getElementsByName('currentVendorAccountForm.accountNbr')[0].value='<nested:write property="currentVendorAccountForm.accountNbr"/>';
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
