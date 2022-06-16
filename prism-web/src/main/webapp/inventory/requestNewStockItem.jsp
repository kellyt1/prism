<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>

<html>
<head>
    <title>Add New Stock Item</title>
</head>
<body>
<nested:form action="/requestNewStockItem" method="post" enctype="multipart/form-data">
<table border="0" style="width:80%;" cellspacing="1" align="center">
    <tr><th colspan="4">Request New Stock Item</th></tr>
    <tr><td colspan="4">&nbsp;</td></tr>
    <tr><td colspan="4">Item Description:</td></tr>
    <tr>
        <td colspan="4">
            <nested:nest property="potentialStockItemForm">
                <nested:text property="description" size="100"/>
            </nested:nest>
        </td>
    </tr>
    <tr><td colspan="4">&nbsp;</td></tr>
    <tr>
        <td>Manufacturer:</td>
        <td>Model:</td>
        <td>Category:</td>
    </tr>
    <tr>
        <td>
            <nested:nest property="potentialStockItemForm">
                <nested:select property="manufacturerId">
                    <option label="" value="">&nbsp;</option>
                    <nested:optionsCollection property="manufacturers" label="externalOrgDetail.orgName" value="manufacturerId"/>
                </nested:select>
            </nested:nest>
        </td>
        <td>
            <nested:nest property="potentialStockItemForm">
                <nested:text property="model"/>
            </nested:nest>
        </td>
        <td>
            <!-- Type Changed to Category -->
            <nested:nest property="potentialStockItemForm">
                <nested:select property="categoryId">
                    <option label="" value="">&nbsp;</option>
                    <nested:optionsCollection property="categories" label="codeAndName" value="categoryId"/>
                </nested:select>
            </nested:nest>
        </td>
    </tr>
    <tr><td colspan="4">&nbsp;</td></tr>
    <tr>
        <td>Primary Contact:</td>
        <td>Secondary Contact:</td>
        <td>Asst. Division Director:</td>
    </tr>
    <tr>
        <td>
            <nested:nest property="potentialStockItemForm">
                <nested:select property="primaryContactId">
                    <option value="">&nbsp;</option>
                    <nested:optionsCollection property="contacts" label="lastAndFirstName" value="personId"/>
                </nested:select>
            </nested:nest>
        </td>
        <td>
            <nested:nest property="potentialStockItemForm">
                <nested:select property="secondaryContactId">
                    <option value="">&nbsp;</option>
                    <nested:optionsCollection property="contacts" label="lastAndFirstName" value="personId"/>
                </nested:select>
            </nested:nest>
        </td>
        <td>
            <nested:select property="potentialStockItemForm.asstDivDirId">
                <option value="">&nbsp;</option>
                <nested:optionsCollection property="potentialStockItemForm.asstDivDirectors" label="lastAndFirstName" value="personId"/>
            </nested:select>
        </td>
    </tr>
    <tr><td colspan="4">&nbsp;</td></tr>
    <tr>
        <td>Est. Annual Usage:</td>
        <td>Dispense Unit:</td>
        <td><bean:message key="orgBudget"/>:</td>
    </tr>
    <tr>
        <td>
            <nested:nest property="potentialStockItemForm">
                <nested:text property="estimatedAnnualUsage"/>
            </nested:nest>
        </td>
        <td>
            <nested:nest property="potentialStockItemForm">
                <nested:select property="dispenseUnitId">
                    <option value="">&nbsp;</option>
                    <nested:optionsCollection property="units" label="name" value="unitId"/>
                </nested:select>
            </nested:nest>
        </td>
        <td>
            <nested:nest property="potentialStockItemForm">
                <nested:select property="orgBudgetId" style="width:600px" styleClass="chosen-select">
                    <option value="">&nbsp;</option>
                    <nested:optionsCollection property="orgBudgets" label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
                </nested:select>
            </nested:nest>
        </td>
    </tr>
    <tr><td colspan="4">&nbsp;</td></tr>
    <tr>
        <td>Suggested Vendor:</td>
        <td>Vendor Catalog#:</td>
        <td>Buy Unit Cost:</td>
    </tr>
    <tr>
        <td><html:text name="stockItemActionRequestForm" property="suggestedVendorName" size="30" maxlength="50"/></td>
        <td><html:text name="stockItemActionRequestForm" property="vendorCatalogNbr" size="15" maxlength="50"/></td>
        <td><html:text name="stockItemActionRequestForm" property="vendorCost" size="10" maxlength="10"/></td>
    </tr>
    <tr>
        <td><bean:message key="changeRequestComments"/>:</td>
        <td>Handling Instructions:</td>
    </tr>
    <tr>
        <td><html:textarea property="specialInstructions" cols="30" name="stockItemActionRequestForm" rows="3"/></td>
        <td><html:textarea property="potentialStockItemForm.instructions" cols="30" name="stockItemActionRequestForm" rows="3"/></td>
    </tr>
</table>
<br/>
<table align="center" width="50%">
        <%--<tr>--%>
            <%--<td>&nbsp;</td>--%>
            <%--<td align="left" nowrap="nowrap">--%>
                <%--If this is a printing item, attach the file(doc,pdf,...) for review if available.--%>
            <%--</td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td align="right" nowrap="nowrap">--%>
                <%--File Name:--%>
            <%--</td>--%>
            <%--<td align="left">--%>
                <%--<nested:file name="stockItemActionRequestForm" property="potentialStockItemForm.printSpecFile" size="75"/>--%>
            <%--</td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td>&nbsp;</td>--%>
            <%--<td>&nbsp;</td>--%>
        <%--</tr>--%>
    <tr>
        <td align="left" colspan="2">
            By submitting this form, you are are only requesting an item be given an Inventory Control Number.<br/>
            Before an item is given an Inventory Control Number, Purchasing and Communications will review the item
            to make sure it meets the agency standards.<br/>
            You are responsible for placing the Inventory Control Number on your print items once you receive it.
        </td>
    </tr>
</table>
<br/>
<table width="10%" border="0" align="center">
    <tr>
        <td><input type="submit" name="submit" value="Submit"/></td>
        <td><input type="reset" name="reset" value="Clear Form"/></td>
        <td><button onclick="this.form.action='/inventory/index.jsp';this.form.submit(); return false;">Cancel</button></td>
    </tr>
</table>
</nested:form>
</body>
</html>