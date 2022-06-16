<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>

<html:form action="viewAddNewAddressStep3.do">
    <input type="hidden" name="module" value='<nested:write property="module" name="deliveryAddressForm" />' />
    <h1>Add New Delivery Address - Step 2</h1>
    <table align="center" >
        
        <logic:notEqual property="newOrOld" value="neither" name="deliveryAddressForm">
            <tr>
                <td colspan="3">Contact Persons:</td>
            </tr>
            <tr>
                <td>Available People:</td>
                <td>&nbsp;</td>
                <td>Current Contacts:</td>
            </tr>
            <tr>
                <td>
                    <html:select property="selectedNonContactPersonIds"  multiple="true" size="10">
                       <html:optionsCollection property="nonContactPersons" label="lastAndFirstName" value="personId" />
                    </html:select>
                </td>
                <td>
                    &nbsp;<input type="submit" onclick="this.form.action='addContacts.do';this.form.submit(); return false;" value=" > "  />&nbsp;
                </td>
                <td>
                    <html:select property="selectedContactPersonIds" multiple="true" size="10">
                       <html:optionsCollection property="contactPersons" label="lastAndFirstName" value="personId" />
                    </html:select>
                </td>
            </tr>
        </logic:notEqual>
        <logic:equal property="newOrOld" value="neither" name="deliveryAddressForm">
            <tr>
                <td colspan="3">
                    <label for="personId">Current list of non-MDH employees:</label><br/>
                    <html:select property="selectedPrivateCitizenPersonId" styleClass="chosen-select" styleId="personId">
                        <html:optionsCollection property="privateCitizenPersons" label="lastAndFirstName" value="personId" />
                    </html:select>
                </td>
            </tr>
        </logic:equal>
        <tr>
            <td colspan="3">
                <div class="btn-group">
                    <html:submit value="Next" styleClass="btn btn-default"/>
                    <input type="SUBMIT" onclick="this.form.action='viewAddPerson.do';this.form.submit(); return false;" value="Add New Person" class="btn btn-default"/>
                </div>
            </td>
        </tr>
    </table>
</html:form>