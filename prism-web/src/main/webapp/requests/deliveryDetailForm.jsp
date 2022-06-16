<%@ include file="../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.view.inventory.Command" %>
<%@ page import="us.mn.state.health.view.materialsrequest.DeliveryDetailForm" %>
<%@ page import="us.mn.state.health.builder.materialsrequest.DeliveryDetailBuilder" %>

<html>
    <head>
        <title>Change Delivery Details</title>
    </head>
    <body>
        <nested:form action="/changeDeliveryDetails" method="post">
            <nested:hidden property="cmd" value="" />
            <div class="row">
                <div class="col-md-4 col-md-offset-4">
                    <fieldset class="text-center">
                        <legend>Deliver to</legend>
                        <logic:notEqual name="requestType" value="SWIFT">
                            <label><html:radio name="requestForm" property="deliveryDetailForm.deliverToType" value="<%=DeliveryDetailForm.DELIVER_TO_MDH%>"  onchange="this.form.cmd.value='reload'; this.form.submit(); return false;"/> MDH Employee</label><br>
                            <label><html:radio name="requestForm" property="deliveryDetailForm.deliverToType" value="<%=DeliveryDetailForm.DELIVER_TO_CITIZEN%>" onchange="this.form.cmd.value='reload'; this.form.submit(); return false;" /> Private Citizen</label><br>
                            <label><html:radio name="requestForm" property="deliveryDetailForm.deliverToType" value="<%=DeliveryDetailForm.DELIVER_TO_EXT_ORG%>" onchange="this.form.cmd.value='reload'; this.form.submit(); return false;" /> External Organization</label>
                        </logic:notEqual>
                        <logic:equal name="requestType" value="SWIFT">
                            <nested:hidden property="deliveryDetailForm.deliverToType" value="<%=DeliveryDetailForm.DELIVER_TO_MDH%>"/>
                            <label><html:radio name="requestForm" property="deliveryDetailForm.facilityId" value="<%=DeliveryDetailBuilder.MINNCOR_FACILITY_ID%>"  onchange="this.form.cmd.value='reload'; this.form.submit(); return false;"/> MINNCOR Industries</label><br>
                            <label><html:radio name="requestForm" property="deliveryDetailForm.facilityId" value="<%=DeliveryDetailBuilder.LAB_FACILITY_ID%>" onchange="this.form.cmd.value='reload'; this.form.submit(); return false;" /> Lab Inventory</label><br>
                        </logic:equal>
                    </fieldset>
                </div>
            </div>
            <logic:notEqual name="requestType" value="SWIFT">
                <br><br>
                <div class="row">
                    <div class="col-md-8 col-md-offset-2">
                        <table style="width: 100%;">
                            <!-- start of the private citizen section -->
                            <logic:equal name="requestForm" property="deliveryDetailForm.deliverToType" value="<%=DeliveryDetailForm.DELIVER_TO_CITIZEN%>">
                                <tr>
                                    <td align="right">Choose a recipient:</td>
                                    <td align="left">
                                        <nested:select property="deliveryDetailForm.recipientId" onchange="this.form.cmd.value='reload'; this.form.submit(); return false;" styleClass="chosen-select">
                                            <option value=""></option>
                                            <nested:optionsCollection property="deliveryDetailForm.recipients" label="lastAndFirstName" value="personId"/>
                                        </nested:select>
                                    </td>
                                </tr>
                                <nested:present property="deliveryDetailForm.recipientId">
                                    <tr>
                                        <td colspan="2">Choose a mailing address:</td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <table class="table table-bordered table-striped">
                                                <thead>
                                                    <tr>
                                                        <th>&nbsp;</th>
                                                        <th align="center">Address 1</th>
                                                        <th align="center">Address 2</th>
                                                        <th align="center">City</th>
                                                        <th align="center">State</th>
                                                        <th align="center">Zip</th>
                                                    </tr>
                                                </thead>
                                                <nested:empty property="deliveryDetailForm.mailingAddresses">
                                                    <tr>
                                                        <td colspan="4" class="text-center">No addresses on file for this person</td>
                                                    </tr>
                                                </nested:empty>
                                                <nested:iterate name="requestForm" property="deliveryDetailForm.mailingAddresses" indexId="a" id="ddf" >
                                                    <tr>
                                                        <td align="center">&nbsp;<input type="RADIO" name="deliveryDetailForm.mailingAddressId" property="deliveryDetailForm.mailingAddressId" value="<nested:write property='mailingAddressId'/>"/>&nbsp;</td>
                                                        <td align="center">&nbsp;<nested:write name="ddf" property="address1" />&nbsp;</td>
                                                        <td align="center">&nbsp;<nested:write name="ddf" property="address2" />&nbsp;</td>
                                                        <td align="center">&nbsp;<nested:write name="ddf" property="city" />&nbsp;</td>
                                                        <td align="center">&nbsp;<nested:write name="ddf" property="state" />&nbsp;</td>
                                                        <td align="center">&nbsp;<nested:write name="ddf" property="zip" />&nbsp;</td>
                                                    </tr>
                                                </nested:iterate>
                                            </table>
                                        </td>
                                    </tr>
                                </nested:present>
                            </logic:equal>
                            <!-- end of the private citizen section -->

                            <!-- start of the external org section -->
                            <logic:equal name="requestForm" property="deliveryDetailForm.deliverToType" value="<%=DeliveryDetailForm.DELIVER_TO_EXT_ORG%>">
                                <tr>
                                    <th class="text-right" scope="row">Choose an Organization:</th>
                                    <td class="text-left">
                                        <nested:select property="deliveryDetailForm.organizationId" onchange="this.form.cmd.value='reload'; this.form.submit(); return false;" styleClass="chosen-select" style="width: 550px;">
                                            <option value=""></option>
                                            <nested:optionsCollection property="deliveryDetailForm.organizations" label="orgName" value="orgId"/>
                                        </nested:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="4">&nbsp;</td>
                                </tr>
                                <tr>
                                    <th class="text-right" scope="row">Contact Name:</th>
                                    <td><nested:text property="deliveryDetailForm.extOrgContactName" size="40" /></td>
                                </tr>
                                <tr>
                                    <td colspan="2">&nbsp;</td>
                                </tr>
                                <nested:notEmpty property="deliveryDetailForm.organizationId">
                                    <nested:hidden property="deliveryDetailForm.facilityId" value="" />
                                    <tr>
                                        <td colspan="2">Choose a mailing address:</td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <table class="table table-bordered table-striped">
                                                <tr>
                                                    <th>&nbsp;</th>
                                                    <th class="text-center">Address 1</th>
                                                    <th class="text-center">Address 2</th>
                                                    <th class="text-center">City</th>
                                                    <th class="text-center">State</th>
                                                    <th class="text-center">Zip</th>
                                                </tr>
                                                <nested:empty property="deliveryDetailForm.mailingAddresses">
                                                    <tr>
                                                        <td colspan="4" class="text-center">No addresses on file for this organization</td>
                                                    </tr>
                                                </nested:empty>
                                                <nested:iterate property="deliveryDetailForm.mailingAddresses" indexId="a" id="ddf" >
                                                    <tr>
                                                        <td class="text-center">&nbsp;<input type="radio" name="deliveryDetailForm.mailingAddressId" property="deliveryDetailForm.mailingAddressId" value="<nested:write property='mailingAddressId'/>"/>&nbsp;</td>
                                                        <td class="text-center">&nbsp;<nested:write name="ddf" property="address1" />&nbsp;</td>
                                                        <td class="text-center">&nbsp;<nested:write name="ddf" property="address2" />&nbsp;</td>
                                                        <td class="text-center">&nbsp;<nested:write name="ddf" property="city" />&nbsp;</td>
                                                        <td class="text-center">&nbsp;<nested:write name="ddf" property="state" />&nbsp;</td>
                                                        <td class="text-center">&nbsp;<nested:write name="ddf" property="zip" />&nbsp;</td>
                                                    </tr>
                                                </nested:iterate>
                                            </table>
                                        </td>
                                    </tr>
                                </nested:notEmpty>
                                <tr>
                                    <td colspan="2">&nbsp;</td>
                                </tr>
                            </logic:equal>
                            <!-- end of the external orgs section -->

                            <!-- start of the MDH section -->
                            <logic:equal name="requestForm" property="deliveryDetailForm.deliverToType" value="<%=DeliveryDetailForm.DELIVER_TO_MDH%>">
                                <tr>
                                    <td colspan="2" class="text-right">Choose a recipient:</td>
                                    <td colspan="6" class="text-left">
                                        <nested:select property="deliveryDetailForm.recipientId" styleClass="chosen-select">
                                            <option value=""></option>
                                            <nested:optionsCollection property="deliveryDetailForm.recipients" label="lastAndFirstName" value="personId"/>
                                        </nested:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" class="text-right">Choose a Facility:</td>
                                    <td colspan="2" class="text-left">
                                        <nested:select property="deliveryDetailForm.facilityId" styleClass="chosen-select">
                                            <nested:optionsCollection property="deliveryDetailForm.facilities" label="facilityName" value="facilityId"/>
                                        </nested:select>
                                    </td>
                                </tr>
                            </logic:equal>
                        </table>
                    </div>
                </div>
            </logic:notEqual>
            <br/>
            <div class="row">
                <div class="col-md-6 col-md-offset-3 text-center">
                    <input type="BUTTON" value="Cancel" onclick="history.go(-1);" class="btn btn-default"/>
                    <html:submit value="Done" styleClass="btn btn-default"/><br><br>
                    <p>Can't find the contact or address you need? <a href="viewAddNewAddressStep1.do?module=requesting&forward=/viewChangeDeliveryDetails.do">Add a new contact or address.</a></p>
                </div>
            </div>
        </nested:form>
    </body>
</html>