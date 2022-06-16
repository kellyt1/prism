<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>Purchase Order</title>
    </head>
    <body>
        <html:form action="viewPurchasingAddresses" method="post">
          <table align="center" cellspacing="0" cellpadding="0" border="0" width="90%">
            <tr>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <nested:empty property="addBkExtSrcForm">
                <th class="tableheader">Purchasing Addresses</th>
              </nested:empty>
              <nested:notEmpty property="addBkExtSrcForm">
                <nested:notEmpty property="addressBookExternalSourceId">
                    <th class="tableheader">Edit Purchasing Address</th>
                </nested:notEmpty>
                <nested:empty property="addressBookExternalSourceId">
                    <th class="tableheader">New Purchasing Address</th>
                </nested:empty>
              </nested:notEmpty>
            </tr>
            <tr>
              <td>&nbsp;</td>
            </tr>
            <nested:empty property="addBkExtSrcForm">
                <tr align="center">
                  <td>
                    <table align="center" cellspacing="0" cellpadding="3" border="0">
                      <tr>
                        <td>
                            <html:radio property="addressType" value="SHIP_TO_TYPE"
                                onchange="this.form.action='viewPurchasingAddresses.do'; this.form.submit(); return false;"/>
                        </td>
                        <td>View Ship To Addresses</td>
                        <td>
                            <html:radio property="addressType" value="BILL_TO_TYPE"
                                onchange="this.form.action='viewPurchasingAddresses.do'; this.form.submit(); return false;"/>
                        </td>
                        <td>View Bill To Addresses</td>
                        <td>
                            <input type="SUBMIT" value="Create New Address"
                             onclick="this.form.action='viewCreatePurchasingAddress.do'"/>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr align="center">
                    <td>Double-click to edit an Address</td>
                </tr>
                <tr align="center">
                  <td>
                    <nested:select property="addressBookExternalSourceId" size="5"
                        ondblclick="this.form.action='viewEditPurchasingAddress.do'; this.form.submit(); return false;">
                        <nested:optionsCollection property="addBkExtSources"
                         value="addressBookExternalSourceId" label="shortSummary"/>
                    </nested:select>
                  </td>
                </tr>
                <tr align="center">
                    <td>
                        <button onclick="this.form.action='/purchasing/index.jsp';this.form.submit(); return false;">Cancel</button>
                    </td>
                </tr>
            </nested:empty>
            <nested:notEmpty property="addBkExtSrcForm">
                <tr align="center">
                  <td>
                    <table align="center" cellspacing="0" cellpadding="3" border="0">
                      <tr>
                        <td align="right" class="tablelabel">Organization Name:</td>
                        <td>
                            <nested:text property="addBkExtSrcForm.orgName" size="40"/>
                        </td>
                      </tr>
                      <tr>
                        <td align="right" class="tablelabel">Contact First Name:</td>
                        <td>
                            <nested:text property="addBkExtSrcForm.firstName" size="40"/>
                        </td>
                      </tr>
                      <tr>
                        <td align="right" class="tablelabel">Contact Last Name:</td>
                        <td>
                            <nested:text property="addBkExtSrcForm.lastName" size="40"/>
                        </td>
                      </tr>
                      <tr>
                        <td align="right" class="tablelabel">Address 1:</td>
                        <td>
                            <nested:text property="addBkExtSrcForm.address1" size="40"/>
                        </td>
                      </tr>
                      <tr>
                        <td align="right" class="tablelabel">Address 2:</td>
                        <td>
                            <nested:text property="addBkExtSrcForm.address2" size="40"/>
                        </td>
                      </tr>
                      <tr>
                        <td align="right" class="tablelabel">City:</td>
                        <td>
                            <nested:text property="addBkExtSrcForm.city" size="40"/>
                        </td>
                      </tr>
                      <tr>
                        <td align="right" class="tablelabel">State:</td>
                        <td>
                            <nested:text property="addBkExtSrcForm.state" size="2"/>
                        </td>
                      </tr>
                      <tr>
                        <td align="right" class="tablelabel">Zip:</td>
                        <td>
                            <nested:text property="addBkExtSrcForm.zip" size="10"/>
                        </td>
                      </tr>
                      <tr>
                        <td align="right" class="tablelabel">Shipping Type:</td>
                        <td>
                            <nested:checkbox property="addBkExtSrcForm.shipToType"/>
                        </td>
                      </tr>
                      <tr>
                        <td align="right" class="tablelabel">Billing Type:</td>
                        <td>
                            <nested:checkbox property="addBkExtSrcForm.billToType"/>
                        </td>
                      </tr>
                      <tr>
                        <td colspan="2"></td>
                      </tr>
                      <tr>
                        <td></td>
                        <td>
                            <input type="SUBMIT" value="Save" onclick="this.form.action='savePurchasingAddress.do'"/>
                            <button onclick="this.form.action='viewPurchasingAddresses.do';this.form.submit(); return false;">Cancel</button>
                    </table>
                  </td>
                </tr>
              </nested:notEmpty>
            <tr>
              <td>&nbsp;</td>
            </tr>
          </table>
        </html:form>
    </body>
</html>
