<%@ include file="../../../include/tlds.jsp" %>

<html>
  <head>
    <title>Edit Fixed Asset</title>
  </head>
  <body>
    <div align="center">
    <nested:form action="editFixedAsset.do" method="post">
        <input type="HIDDEN" name="cmd" />
        <table class="table" align="center" border="0" bordercolor="#000000" cellspacing="0" cellpadding="5" bgcolor="#CCCCCC" width="85%">
            <tr>
                <td colspan="4" align="center" class="tableheader">Edit Fixed Asset</td>
            </tr>
            <tr>
                <td>Description:</td>
                <td>Category:</td>
                <td>Manufacturer:</td>
                <td>Model:</td>
            </tr>
            <tr>
                <td><nested:write property="fixedAsset.item.description" /></td>
                <td><nested:write property="fixedAsset.item.category.name" /></td>
                <td>
                  <nested:present property="fixedAsset.item.manufacturer">
                    <nested:write property="fixedAsset.item.manufacturer.externalOrgDetail.orgName"/>
                  </nested:present>
                </td>
                <td><nested:write property="fixedAsset.item.model" /></td>
            </tr>
            <tr>
                <td>Fixed Asset Nbr*:</td>
                <td>Serial Nbr:</td>
                <td>Maint. Agmt. Exp. Date:</td>
                <td>Maint. Agmt. PO:</td>
            </tr>
            <tr>
                <td><nested:text property="fixedAssetNumber" /></td>
                <td><nested:text property="serialNumber" /></td>
                <td>
                    <nested:text property="maintAgreementExpirationDate" size="10" styleClass="dateInput"/>
                </td>
                <td><nested:text property="maintAgreementPONumber" /></td>
            </tr>
            <tr>
                <td>Date Received:</td>
                <td>Document Nbr:</td>
                <td>Cost:</td>
                <td>Warranty Exp. Date:</td>
            </tr>
            <tr>
                <td>
                    <nested:text property="dateReceived" size="10" styleClass="dateInput"/>
                </td>
                <td><nested:text property="document" /></td>
                <td><nested:text property="cost" /></td>
                <td>
                    <nested:text property="warrantyExpirationDate" size="10" styleClass="dateInput"/>
                </td>
            </tr>
            <tr>
                <td>Vendor:</td>
                <td>Contact Person:</td>
                <td>Location:</td>
                <td>Class Code:</td>
            </tr>
            <tr>
                <td>
                    <nested:select property="vendorId" style="width: 200px">
                        <option value=""> </option>
                        <nested:optionsCollection property="vendors" label="externalOrgDetail.orgName" value="vendorId"/>
                    </nested:select>
                </td>
                <td>
                    <nested:select property="contactPersonId" style="width: 200px">
                        <option value=""> </option>
                        <nested:optionsCollection property="contactPersons" label="lastAndFirstName" value="personId"/>
                    </nested:select>
                </td>
                <td>
                    <nested:select property="facilityId">
                        <option value=""> </option>
                        <nested:optionsCollection property="facilities" label="facilityName" value="facilityId"/>
                    </nested:select>
                </td>
                <td>
                    <nested:select property="classCodeId">
                        <option value=""> </option>
                        <nested:optionsCollection property="classCodes" label="classCodeValue" value="classCodeId"/>
                    </nested:select>
                </td>
            </tr>
            <tr>
                <td><bean:message key="orgBudget"/>(s):</td>
                <td>Status:</td>
                <td>Fund:</td>
                <td>Notes:</td>
            </tr>
            <tr>
                <td valign="top">
                    <table>
                    <nested:iterate length="orgBudget" property="ownerOrgBudgets" indexId="a">                    
                        <tr>
                            <td><nested:write property="orgBudgetCodeAndNameAndFY" /></td>
                            <td>&nbsp;</td>
                            <td><a href='${pageContext.request.contextPath}/editFixedAsset.do?cmd=removeOwnerOrgBdgt&orgBdgtId=<nested:write property="orgBudgetId" />'>remove</a></td>
                        </tr>
                   </nested:iterate>
                   </table>
                    <br />
                    <nested:select property="orgBudgetId" style="width: 200px">
                        <option value=""> </option>
                        <nested:optionsCollection property="allOrgBudgetsList" label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
                    </nested:select>
                    <input type="BUTTON" onclick="this.form.cmd.value='addOwnerOrgBdgt';this.form.submit(); return false;" value="Add" />
                </td>
                <td valign="top">
                    <nested:select property="statusId" style="width: 200px" >
                        <option value=""> </option>
                        <nested:optionsCollection property="statuses" label="name" value="statusId"/>
                    </nested:select>
                </td>
                <td>   
                    <nested:text property="fund" />
                </td>
                <td>
                    <nested:textarea property="notes"/>
                </td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td colspan="4" align="center">
                    <input type="submit" value="Submit" />
                    <input type="RESET" value="Reset" />
                </td>
            </tr>
        </table>
    </nested:form>
  </body>
</html>