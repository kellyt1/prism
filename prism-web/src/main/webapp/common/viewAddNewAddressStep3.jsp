<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>

<nested:form action="addNewAddressFinalStep.do">
    <input type="hidden" name="module" value='<nested:write property="module" name="deliveryAddressForm" />' />
    <h1>Add New Delivery Address - Step 3</h1>
    <table class="table table-bordered table-striped">
        <caption>Mailing Addresses</caption>
        <thead>
            <tr>
                <th scope="col">Address 1</th>
                <th scope="col">Address 2</th>
                <th scope="col">City</th>
                <th scope="col">State</th>
                <th scope="col">Zip</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
                <td colspan="5" class="text-center">
                    <div class="btn-group">
                        <button onclick="this.form.action='viewAddNewMailingAddress.do';this.form.submit(); return false;" class="btn btn-default">Add New Address</button>
                        <html:submit value="Done - Save My Work" styleClass="btn btn-default"/>
                    </div>
                </td>
            </tr>
        </tfoot>
        <tbody>
            <nested:notEmpty property="mailingAddresses">
                <nested:iterate property="mailingAddresses" id="ma" name="deliveryAddressForm">
                    <tr>
                        <td><nested:write property="address1" /></td>
                        <td><nested:write property="address2" /></td>
                        <td><nested:write property="city" /></td>
                        <td><nested:write property="state" /></td>
                        <td><nested:write property="zip" /></td>
                    </tr>
                </nested:iterate>
            </nested:notEmpty>
        </tbody>
    </table>
</nested:form>