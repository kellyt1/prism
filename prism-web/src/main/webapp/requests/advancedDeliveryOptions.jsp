<%@ include file="../include/tlds.jsp" %>

<html>
<head>
    <title>Advanced Delivery Options</title>
</head>

<body>
    <nested:form action="chooseAdvancedDeliveryOption" method="post">
        <html:hidden property="cmd" value=""/>
        <table align="center" cellpadding="5" bgcolor="#CCCCCC" width="50%" >
            <tr>
                <th>Advanced Delivery Options</th>
            </tr>
            <tr>
              <td valign="top">
                <table align="center">
                  <tr>
                    <td><nested:radio property="deliveryInfoType" value="enteredByUser" 
                            onchange="this.form.cmd.value='selectDeliveryMethod';this.form.submit(); return false;"/>
                    </td>
                    <td>Enter New Delivery Information</td>
                  </tr>
                  <tr>
                    <td><nested:radio property="deliveryInfoType" value="persAddBkPerson" 
                            onchange="this.form.cmd.value='selectDeliveryMethod';this.form.submit(); return false;"/>
                    </td>
                    <td>Use MDH Person from Personal Address Book</td>
                  </tr>
                  <tr>
                    <td><nested:radio property="deliveryInfoType" value="persAddBkExtLctn" 
                            onchange="this.form.cmd.value='selectDeliveryMethod';this.form.submit(); return false;"/>
                    </td>
                    <td>Use External Mailing Address from Personal Address Book</td>
                  </tr>
                  <tr>
                    <td><nested:radio property="deliveryInfoType" value="glbAddBkPersonAdvncd" 
                            onchange="this.form.cmd.value='selectDeliveryMethod';this.form.submit(); return false;"/>
                    </td>
                    <td>Use MDH Person from MDH Global Address Book</td>
                  </tr>
                  <tr>
                    <td><nested:radio property="deliveryInfoType" value="glbAddBkExtLctn" 
                            onchange="this.form.cmd.value='selectDeliveryMethod';this.form.submit(); return false;"/>
                    </td>
                    <td>Use External Mailing Address from MDH Global Address Book</td>
                  </tr>
                </table>
             </td>
            </tr>
            <tr valign="top">
              <td valign="top" align="left">
                <nested:equal property="deliveryInfoType" value="enteredByUser">
                 <table align="center">
                    <tr>
                        <td align="right">Organization (Optional):</td>
                        <td>
                          <nested:text property="addBkExtSrcForm.orgName" size="25"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">First Name:</td>
                        <td>
                          <nested:text property="addBkExtSrcForm.firstName" size="25"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">Last Name:</td>
                        <td>
                          <nested:text property="addBkExtSrcForm.lastName" size="25"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">Address:</td>
                        <td>
                          <nested:text property="addBkExtSrcForm.streetAddress" size="25"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">City:</td>
                        <td>
                          <nested:text property="addBkExtSrcForm.city" size="25"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">State:</td>
                        <td>
                          <nested:text property="addBkExtSrcForm.state" size="2"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">Zip Code:</td>
                        <td>
                          <nested:text property="addBkExtSrcForm.zip" maxlength="10" size="10"/>
                        </td>
                    </tr>
                  </table>
                </nested:equal>
                <nested:equal property="deliveryInfoType" value="persAddBkPerson">
                    <table align="center">
                        <nested:notEmpty property="addBkPersons">
                            <tr>
                                <td>Choose an MDH Person from your Personal Address Book</td>
                            </tr>
                            <tr>
                                <td>
                                    <nested:select property="addBkPersonId">
                                        <nested:optionsCollection property="addBkPersons" label="addressBookPerson.person.lastAndFirstName" value="personalAddBkPersonId"/>
                                    </nested:select>
                                </td>
                            </tr>
                        </nested:notEmpty>
                        <nested:empty property="addBkPersons">
                            <tr>
                                <td>No MDH People in your Personal Address Book</td>
                            </tr>
                        </nested:empty>
                    </table>
                </nested:equal>
                <nested:equal property="deliveryInfoType" value="persAddBkExtLctn">
                    <table align="center">
                        <nested:notEmpty property="addBkExtSources">
                            <tr>
                                <td>Choose an External Mailing Address from your Personal Address Book</td>
                            </tr>
                            <tr>
                                <td>
                                    <nested:select property="addressBookExternalSourceId">
                                        <nested:optionsCollection property="addBkExtSources" label="addBkExtSource.shortSummary" value="personalAddBkExtSourceId"/>
                                    </nested:select>
                                </td>
                            </tr>
                        </nested:notEmpty>
                        <nested:empty property="addBkExtSources">
                            <tr>
                                <td>No External Mailing Addresses in your Personal Address Book</td>
                            </tr>
                        </nested:empty>
                    </table>
                </nested:equal>
                <nested:equal property="deliveryInfoType" value="glbAddBkPersonAdvncd">
                    <table align="center">
                        <tr>
                            <td>Choose an MDH Person from the Global Address Book</td>
                        </tr>
                        <tr>
                            <td>
                                <nested:select property="addBkPersonId">
                                    <option></option>
                                    <nested:optionsCollection property="addBkPersons" label="person.lastAndFirstName" value="addressBookPersonId"/>
                                </nested:select>
                            </td>
                        </tr>
                    </table>
                </nested:equal>
                <nested:equal property="deliveryInfoType" value="glbAddBkExtLctn">
                    <table align="center">
                        <tr>
                            <td>Choose an External Mailing Address from the Global Address Book</td>
                        </tr>
                        <tr>
                            <td>
                                <nested:select property="addressBookExternalSourceId">
                                    <option></option>
                                    <nested:optionsCollection property="addBkExtSources" label="shortSummary" value="addressBookExternalSourceId"/>
                                </nested:select>
                            </td>
                        </tr>
                    </table>
                </nested:equal>
              </td>
            </tr>
            <nested:equal property="deliveryInfoType" value="glbAddBkExtLctn">
                <tr align="center">
                    <td colspan="2"><html:checkbox property="addToPersAddBk"/>Add to Personal Address Book</td>
                </tr>
            </nested:equal>
            <nested:equal property="deliveryInfoType" value="glbAddBkPerson">
                <tr align="center">
                    <td colspan="2"><html:checkbox property="addToPersAddBk"/>Add to Personal Address Book</td>
                </tr>
            </nested:equal>
            <nested:equal property="deliveryInfoType" value="enteredByUser">
                <tr align="center">
                    <td colspan="2"><html:checkbox property="addToPersAddBk"/>Add to Personal Address Book</td>
                </tr>
            </nested:equal>
            <tr align="center">
                <td colspan="2"><input type="submit" value="Cancel" onclick="this.form.cmd.value='cancelAdvancedDeliveryOptions'"/>&nbsp;&nbsp;<input type="SUBMIT" value="Set Delivery Option"/></td>
            </tr>
        </table>
    </nested:form>
  </body>
</html>