<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<%@ page import="us.mn.state.health.matmgmt.util.Form"%>

<html>
    <head>
        <title>Add New External Org Mailing Address</title>
    </head>
    <body>
        <html:form action="addNewMailingAddress" method="post">
            <input type="hidden" name="module" value='<nested:write property="module" name="deliveryAddressForm" />' />
            <html:hidden property="cmd"/>
            <h1>Add New External Org Mailing Address</h1>
            <div class="row">
                <div class="col-md-12">
                    <fieldset>
                        <legend>New Mailing Address</legend>
                        <div class="row">
                            <div class="col-md-6 text-right"><label for="addr1">Address 1:</label></div>
                            <div class="col-md-6"><nested:text property="mailingAddressForm.address1" size="40" styleId="addr1"/></div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 text-right"><label for="addr2">Address 2:</label></div>
                            <div class="col-md-6"><nested:text property="mailingAddressForm.address2" size="40" styleId="addr2"/></div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 text-right"><label for="city">City:</label></div>
                            <div class="col-md-6"><nested:text property="mailingAddressForm.city" size="40" styleId="city"/></div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 text-right"><label for="state">State:</label></div>
                            <div class="col-md-6">
                                <nested:select property="mailingAddressForm.state" name="deliveryAddressForm" styleClass="chosen-select" styleId="state">
                                <option value="AL">AL - Alabama</option>
                                <option value="AK">AK - Alaska</option>
                                <option value="AP">AP - Armed Forces Pacific</option>
                                <option value="AE">AE - Armed Forces Europe</option>
                                <option value="AA">AA - Armed Forces America</option>
                                <option value="AZ">AZ - Arizona</option>
                                <option value="AR">AR - Arkansas</option>
                                <option value="CA">CA - California</option>
                                <option value="CO">CO - Colorado</option>
                                <option value="CT">CT - Connecticut</option>
                                <option value="DC">DC - Washington D.C.</option>
                                <option value="DE">DE - Delaware</option>
                                <option value="FL">FL - Florida</option>
                                <option value="GA">GA - Georgia</option>
                                <option value="GU">GU - Guam</option>
                                <option value="HI">HI - Hawaii</option>
                                <option value="ID">ID - Idaho</option>
                                <option value="IL">IL - Illinois</option>
                                <option value="IN">IN - Indiana</option>
                                <option value="IA">IA - Iowa</option>
                                <option value="KS">KS - Kansas</option>
                                <option value="KY">KY - Kentucky</option>
                                <option value="LA">LA - Louisiana</option>
                                <option value="ME">ME - Maine</option>
                                <option value="MD">MD - Maryland</option>
                                <option value="MA">MA - Massachusetts</option>
                                <option value="MI">MI - Michigan</option>
                                <option value="MN" selected="selected">MN - Minnesota</option>
                                <option value="MS">MS - Mississippi</option>
                                <option value="MO">MO - Missouri</option>
                                <option value="MT">MT - Montana</option>
                                <option value="NE">NE - Nebraska</option>
                                <option value="NV">NV - Nevada</option>
                                <option value="NH">NH - New Hampshire</option>
                                <option value="NJ">NJ - New Jersey</option>
                                <option value="NM">NM - New Mexico</option>
                                <option value="NY">NY - New York</option>
                                <option value="NC">NC - North Carolina</option>
                                <option value="ND">ND - North Dakota</option>
                                <option value="OH">OH - Ohio</option>
                                <option value="OK">OK - Oklahoma</option>
                                <option value="OR">OR - Oregon</option>
                                <option value="PA">PA - Pennsylvania</option>
                                <option value="PR">PR - Puerto Rico</option>
                                <option value="RI">RI - Rhode Island</option>
                                <option value="SC">SC - South Carolina</option>
                                <option value="SD">SD - South Dakota</option>
                                <option value="TN">TN - Tennessee</option>
                                <option value="TX">TX - Texas</option>
                                <option value="UT">UT - Utah</option>
                                <option value="VT">VT - Vermont</option>
                                <option value="VA">VA - Virginia</option>
                                <option value="VI">VI - Virgin Islands</option>
                                <option value="WA">WA - Washington</option>
                                <option value="WV">WV - West Virginia</option>
                                <option value="WI">WI - Wisconsin</option>
                                <option value="WY">WY - Wyoming</option>
                                    </nested:select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 text-right"><label for="zip">Zip:</label></div>
                            <div class="col-md-6"><nested:text property="mailingAddressForm.zip" size="5" maxlength="5" styleId="zip"/></div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 text-center">
                                <div class="btn-group">
                                    <button onclick="history.go(-1)" class="btn btn-default">Cancel</button>
                                    <button onclick="this.form.cmd.value='<%=Form.ADD_NEW_ADDRESS%>'; this.form.submit(); return false;" class="btn btn-default">Submit</button>
                                </div>
                            </div>
                        </div>
                    </fieldset>
                </div>
            </div>
        </html:form>
    </body>
</html>
