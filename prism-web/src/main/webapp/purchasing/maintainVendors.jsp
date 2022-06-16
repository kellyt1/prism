<%@ include file="../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.model.common.EmailAddress" %>
<html>
    <head>
        <title>Maintain Vendors</title>
    </head>
    <body>
        <html:form action="/viewCreateNewVendor" method="post">
            <table align="center" cellspacing="0" cellpadding="0" border="0" width="90%">
                <caption>
                    <nested:empty name="vendorsForm" property="vendorForm">Vendors</nested:empty>
                    <nested:notEmpty name="vendorsForm" property="vendorForm">
                        <nested:notEmpty name="vendorsForm" property="vendorId">Edit Vendor</nested:notEmpty>
                        <nested:empty name="vendorsForm" property="vendorId">New Vendor</nested:empty>
                    </nested:notEmpty>
                </caption>
                <tfoot>
                    <tr>
                        <td colspan="2" class="text-center">
                            <div class="btn-group">
                                <button type="SUBMIT" onclick="this.form.action='saveVendor.do';this.form.submit(); return false;" class="btn btn-default">Save</button>
                                <button onclick="this.form.action='viewMaintainVendors.do';this.form.submit(); return false;" class="btn btn-default">Cancel</button>
                            </div>
                        </td>
                    </tr>
                </tfoot>
                <nested:empty name="vendorsForm" property="vendorForm">
                    <tr class="text-center">
                        <td>
                            <div class="btn-group">
                                <button type="SUBMIT" onclick="this.form.action='viewCreateNewVendor.do'" class="btn btn-default">Create New Vendor</button>
                                <button onclick="this.form.action='/generalAdmin/index.jsp';this.form.submit(); return false;" class="btn btn-default">Cancel</button>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                    <tr align="center">
                        <td>Double-click to edit a Vendor</td>
                    </tr>
                    <tr align="center">
                        <td>
                            <nested:select property="vendorId" size="15" name="vendorsForm" ondblclick="if (document.forms[0].vendorId.selectedIndex > 0) {this.form.action='viewEditVendor.do'; this.form.submit(); return false; }">
                                <option value="-1" selected="selected">--- Select a vendor -----</option>
                                <nested:optionsCollection property="vendors" name="vendorsForm" value="vendorId" label="externalOrgDetail.orgName"/>
                            </nested:select>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                    <tr class="text-center">
                        <td><button onclick="if (document.forms[0].vendorId.selectedIndex > 0) {this.form.action='viewEditVendor.do'; this.form.submit(); return false; }" class="btn btn-default">Select</button></td>
                    </tr>
                </nested:empty>
                <nested:notEmpty name="vendorsForm" property="vendorForm">
                    <tr align="center">
                        <td>
                            <table align="center" cellpadding="3" cellspacing="0" border="0">
                                <tr>
                                    <td class="text-right">Vendor Name:</td>
                                    <td><nested:text name="vendorsForm" property="vendorForm.externalOrgDetailForm.orgName"/></td>
                                </tr>
                                <tr>
                                    <td class="text-right">Vendor Description:</td>
                                    <td><nested:text name="vendorsForm" property="vendorForm.externalOrgDetailForm.orgDescription"/></td>
                                </tr>
                                <tr>
                                    <td class="text-right">Web Address:</td>
                                    <td><nested:text name="vendorsForm" property="vendorForm.externalOrgDetailForm.webAddress"/></td>
                                </tr>
                                <tr>
                                    <td class="text-right">Comments:</td>
                                    <td><nested:textarea name="vendorsForm" property="vendorForm.externalOrgDetailForm.comments"/></td>
                                </tr>
                                <tr>
                                    <td class="text-right">Effective Date:</td>
                                    <td><nested:text name="vendorsForm" property="vendorForm.externalOrgDetailForm.orgEffectiveDate" styleClass="dateInput"/></td>
                                </tr>
                                <tr>
                                    <td class="text-right">End Date:</td>
                                    <td><nested:text name="vendorsForm" property="vendorForm.externalOrgDetailForm.orgEndDate" styleClass="dateInput"/></td>
                                </tr>
                                <tr>
                                    <th width="50%" colspan="2">Mailing Addresses:</th>
                                </tr>

                                <tr align="center">
                                    <td colspan="2">Double-click to edit an Address</td>
                                </tr>
                                <nested:hidden property="cmd" name="vendorsForm"/>
                                <tr align="center">
                                    <td colspan="2">
                                        <nested:select property="mailingAddressId" name="vendorsForm" size="5"
                                                ondblclick="this.form.action='editMailingAddress.do'; this.form.cmd.value='editVendorMailingAddress' ;this.form.submit(); return false;">
                                            <nested:optionsCollection name="vendorsForm"
                                                    property="vendorForm.externalOrgDetailForm.mailingAddresses" value="key" label="shortSummary"/>
                                        </nested:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="center" colspan="2">
                                        <button type="SUBMIT" onclick="this.form.action='viewAddMailingAddress.do';this.form.submit(); return false;" class="btn btn-default">Add Mailing Address</button>
                                        <!--<input type="SUBMIT" value="Delete Mailing Address"-->
                                        <!--onclick="this.form.action='deleteMailingAddress.do';this.form.cmd.value='deleteVendorMailingAddress';this.form.submit(); return false;"/>-->
                                    </td>
                                </tr>
                                <tr>
                                    <th width="50%" colspan="2">Accounts:</th>
                                </tr>

                                <tr align="center">
                                    <td colspan="2">Double-click to edit an Account</td>
                                </tr>
                                <tr align="center">
                                    <td colspan="2">
                                        <nested:select property="vendorAccountId" name="vendorsForm" size="5" ondblclick="this.form.action='editVendorAccount.do'; this.form.cmd.value='editVendorAccount' ;this.form.submit(); return false;">
                                            <nested:optionsCollection name="vendorsForm" property="vendorForm.vendorAccounts" value="vendorAccountId" label="accountNbr"/>
                                        </nested:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="center" colspan="2">
                                        <div class="btn-group">
                                            <button type="SUBMIT" onclick="this.form.action='viewAddVendorAccount.do';this.form.submit(); return false;" class="btn btn-default">Add Account</button>
                                            <button type="SUBMIT" onclick="this.form.action='deleteVendorAccount.do';this.form.cmd.value='deleteVendorAccount';this.form.submit(); return false;" class="btn btn-default">Delete Account</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th width="50%" colspan="2">Contracts:</th>
                                </tr>

                                <tr align="center">
                                    <td colspan="2">Double-click to edit a Contract</td>
                                </tr>
                                <tr align="center">
                                    <td colspan="2">
                                        <nested:select property="vendorContractId" name="vendorsForm" size="5" ondblclick="this.form.action='editVendorContract.do'; this.form.cmd.value='editVendorContract' ;this.form.submit(); return false;">
                                            <nested:optionsCollection name="vendorsForm" property="vendorForm.vendorContracts" value="key" label="contractNumber"/>
                                        </nested:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="center" colspan="2">
                                        <div class="btn-group">
                                            <button type="SUBMIT" onclick="this.form.action='viewAddVendorContract.do';this.form.submit(); return false;" class="btn btn-default">Add Contract</button>
                                            <button type="SUBMIT" onclick="this.form.action='deleteVendorContract.do';this.form.cmd.value='deleteVendorContract';this.form.submit(); return false;" class="btn btn-default">Delete Contract</button>
                                        </div>
                                    </td>
                                </tr>

                                <tr>
                                    <th width="50%" colspan="2">Primary Representative:</th>
                                </tr>
                                <tr>
                                    <td class="text-right">First Name:</td>
                                    <td><nested:text property="vendorForm.externalOrgDetailForm.personForm.firstName" name="vendorsForm"/></td>
                                </tr>
                                <tr>
                                    <td class="text-right">Middle Name:</td>
                                    <td><nested:text property="vendorForm.externalOrgDetailForm.personForm.middleName" name="vendorsForm"/></td>
                                </tr>
                                <tr>
                                    <td class="text-right">Last Name:</td>
                                    <td><nested:text property="vendorForm.externalOrgDetailForm.personForm.lastName" name="vendorsForm"/></td>
                                </tr>

                                <tr>
                                    <th width="50%" colspan="2">Primary Email</th>
                                </tr>
                                <tr>
                                    <td class="text-right">Email Address:</td>
                                    <td><nested:text name="vendorsForm" property="vendorForm.externalOrgDetailForm.emailAddressForm.emailAddress"/></td>
                                </tr>
                                <tr>
                                    <td class="text-right">Email Type:</td>
                                    <td>
                                        <nested:select property="vendorForm.externalOrgDetailForm.emailAddressForm.emailType" name="vendorsForm">
                                            <option value="<%=EmailAddress.VENDOR_EMAIL_TYPE%>" selected="selected">Vendor</option>
                                        </nested:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right">Email Comments:</td>
                                    <td><nested:textarea name="vendorsForm" property="vendorForm.externalOrgDetailForm.emailAddressForm.comments"/></td>
                                </tr>
                                <tr>
                                    <td class="text-right">Email Start Date:</td>
                                    <td><nested:text name="vendorsForm" property="vendorForm.externalOrgDetailForm.emailAddressForm.startDate" styleClass="dateInput"/></td>
                                </tr>
                                <tr>
                                    <td class="text-right">Email End Date:</td>
                                    <td><nested:text name="vendorsForm" property="vendorForm.externalOrgDetailForm.emailAddressForm.endDate" styleClass="dateInput"/></td>
                                </tr>

                                <tr>
                                    <th width="50%" colspan="2">Primary Phone:</th>
                                </tr>
                                <tr>
                                    <td class="text-right">Phone Number:</td>
                                    <td><nested:text name="vendorsForm" property="vendorForm.externalOrgDetailForm.phoneForm.number" maxlength="20"/></td>
                                </tr>
                                <tr>
                                    <th width="50%" colspan="2">Primary Fax:</th>
                                </tr>
                                <tr>
                                    <td class="text-right">Fax Number:</td>
                                    <td><nested:text name="vendorsForm" property="vendorForm.externalOrgDetailForm.faxForm.number" maxlength="20"/></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </nested:notEmpty>
            </table>
        </html:form>
    </body>
</html>