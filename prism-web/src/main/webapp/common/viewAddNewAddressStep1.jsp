<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>

<html:form action="viewAddNewAddressStep2.do">
    <html:hidden property="cmd"/>
    <input type="hidden" name="module" value='<nested:write property="module" name="deliveryAddressForm" />' />
    <h1>Add New Delivery Address - Step 1</h1>
    <p>
        Choose either an existing external organization to add new delivery information
        to it, add a new external organization, or select the last option to enter
        delivery information for a private individual (w/o affiliation to any organization).
    </p>
    <fieldset>
        <label><nested:radio property="newOrOld" value="old"/> Choose existing external org:</label>
        <html:select property="selectedExtOrgId" onfocus="deliveryAddressForm.newOrOld[0].checked=true;" styleClass="chosen-select">
            <html:option value=""/>
            <html:optionsCollection property="extOrgs" label="upperOrgName" value="orgId" />
        </html:select>
        <br/>
        <label><nested:radio property="newOrOld"  value="new"/> Add NEW external org - Enter org name:</label>
        <html:text property="extOrgForm.orgName" size="50" onfocus="deliveryAddressForm.newOrOld[1].checked=true;selectedExtOrgId.value='';" />
        <br/>
        <label><nested:radio property="newOrOld" value="neither"/> No org - add private citizen data</label>
        <br/>
        <html:submit value="Next" styleClass="btn btn-default"/>
    </fieldset>
</html:form>