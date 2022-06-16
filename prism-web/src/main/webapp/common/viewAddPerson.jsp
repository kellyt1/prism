<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<%@ page import="us.mn.state.health.matmgmt.util.Form"%>

<html:form action="addPerson.do">   
    <input type="hidden" name="module" value='<nested:write property="module" name="deliveryAddressForm" />' />
    <nested:hidden property="cmd"/>
    <h1>Add New Person</h1>
    <table align="center">
        <tr>
            <td>Title:</td>
            <td>First Name:</td>
            <td>Last Name:</td>
            <td>Suffix:</td>
        </tr>
        <tr>
            <td>
                <html:text property="personForm.namePrefix" size="20" />
            </td>
            <td>
                <html:text property="personForm.firstName" size="20" />
            </td>
            <td>
                <html:text property="personForm.lastName" size="20" />
            </td>
            <td>
                <html:text property="personForm.salutation" size="20" />
            </td>
        </tr>
        <tr>
            <td colspan="6" class="text-center">
                <div class="btn-group">
                    <input type="BUTTON" value="Cancel" onclick="history.go(-1)" class="btn btn-default"/>
                    <input type="button" value="Submit" onclick="this.form.cmd.value='<%=Form.ADD_NEW_PERSON%>'; this.form.submit(); return false;" class="btn btn-default"/>
                </div>
            </td>
        </tr>
    </table>
</html:form>