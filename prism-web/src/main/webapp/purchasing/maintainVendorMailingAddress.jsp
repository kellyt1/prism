<%@ include file="../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.view.purchasing.Command"%>

<html>
    <head>
        <title>Maintain Vendor</title>
    </head>
    <body>
        <html:form action="/editMailingAddress.do" method="post">

          <nested:hidden property="cmd"/>
          <table align="center" cellspacing="0" cellpadding="0" border="0" width="90%">
            <tr>
              <td>&nbsp;</td>
            </tr>
            <tr>
                <nested:notEmpty name="vendorsForm" property="mailingAddressId">
                    <th class="tableheader">Edit Mailing Address</th>
                </nested:notEmpty>
                <nested:empty property="mailingAddressId">
                    <th class="tableheader">New Mailing Address</th>
                </nested:empty>
            </tr>
            <tr>
              <td>&nbsp;</td>
            </tr>
            <tr align="center">
              <td>
                <table align="center" cellspacing="0" cellpadding="3" border="0">
                  <tr>
                    <td align="right" class="tablelabel">Address 1:</td>
                    <td>
                        <nested:text property="currentMailingAddressForm.address1" size="40"/>
                    </td>
                  </tr>
                    <td align="right" class="tablelabel">Address 2:</td>
                    <td>
                        <nested:text property="currentMailingAddressForm.address2" size="40"/>
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="tablelabel">City:</td>
                    <td>
                        <nested:text property="currentMailingAddressForm.city" size="40"/>
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="tablelabel">State:</td>
                    <td>
                        <nested:select property="currentMailingAddressForm.state">
                            <option value="">Select a State... </option>
                              <nested:optionsCollection  property="currentMailingAddressForm.states" label="stateName" value="stateCode"/>
                            <!--<option value="AL">-->
                            <!--AL - Alabama </option>-->
                            <!--<option value="AK">-->
                            <!--AK - Alaska</option><option value="AP">-->
                            <!--AP - Armed Forces Pacific</option><option value="AE">-->
                            <!--AE - Armed Forces Europe</option><option value="AA">-->
                            <!--AA - Armed Forces America</option><option value="AZ">-->
                            <!--AZ - Arizona</option><option value="AR">-->
                            <!--AR - Arkansas</option><option value="CA">-->
                            <!--CA - California</option><option value="CO">-->
                            <!--CO - Colorado</option><option value="CT">-->
                            <!--CT - Connecticut</option><option value="DC">-->

                            <!--DC - Washington D.C.</option><option value="DE">-->
                            <!--DE - Delaware</option><option value="FL">-->
                            <!--FL - Florida</option><option value="GA">-->
                            <!--GA - Georgia</option><option value="GU">-->
                            <!--GU - Guam</option><option value="HI">-->
                            <!--HI - Hawaii</option><option value="ID">-->
                            <!--ID - Idaho</option><option value="IL">-->
                            <!--IL - Illinois</option><option value="IN">-->
                            <!--IN - Indiana</option><option value="IA">-->

                            <!--IA - Iowa</option><option value="KS">-->
                            <!--KS - Kansas</option><option value="KY">-->
                            <!--KY - Kentucky</option><option value="LA">-->
                            <!--LA - Louisiana</option><option value="ME">-->
                            <!--ME - Maine</option><option value="MD">-->
                            <!--MD - Maryland</option><option value="MA">-->
                            <!--MA - Massachusetts</option><option value="MI">-->
                            <!--MI - Michigan</option><option value="MN">-->
                            <!--MN - Minnesota</option><option value="MS">-->

                            <!--MS - Mississippi</option><option value="MO">-->
                            <!--MO - Missouri</option><option value="MT">-->
                            <!--MT - Montana</option><option value="NE">-->
                            <!--NE - Nebraska</option><option value="NV">-->
                            <!--NV - Nevada</option><option value="NH">-->
                            <!--NH - New Hampshire</option><option value="NJ">-->
                            <!--NJ - New Jersey</option><option value="NM">-->
                            <!--NM - New Mexico</option><option value="NY">-->
                            <!--NY - New York</option><option value="NC">-->

                            <!--NC - North Carolina</option><option value="ND">-->
                            <!--ND - North Dakota</option><option value="OH">-->
                            <!--OH - Ohio</option><option value="OK">-->
                            <!--OK - Oklahoma</option><option value="OR">-->
                            <!--OR - Oregon</option><option value="PA">-->
                            <!--PA - Pennsylvania</option><option value="PR">-->
                            <!--PR - Puerto Rico</option><option value="RI">-->
                            <!--RI - Rhode Island</option><option value="SC">-->
                            <!--SC - South Carolina</option><option value="SD">-->

                            <!--SD - South Dakota</option><option value="TN">-->
                            <!--TN - Tennessee</option><option value="TX">-->
                            <!--TX - Texas</option><option value="UT">-->
                            <!--UT - Utah</option><option value="VT">-->
                            <!--VT - Vermont</option><option value="VA">-->
                            <!--VA - Virginia</option><option value="VI">-->
                            <!--VI - Virgin Islands</option><option value="WA">-->
                            <!--WA - Washington</option><option value="WV">-->
                            <!--WV - West Virginia</option><option value="WI">-->

                            <!--WI - Wisconsin</option><option value="WY">-->
                            <!--WY - Wyoming</option>-->
                            <!--<option value="XX">XX - Outside US</option>-->
                        </nested:select>
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="tablelabel">Zip:</td>
                    <td>
                        <nested:text property="currentMailingAddressForm.zip" size="5" maxlength="5" /> <small><small>For international addresses enter 99999</small></small>
                    </td>
                  </tr>
            <tr>
              <td align="right" class="tablelabel">End Date:</td>
              <td>
                <nested:text name="vendorsForm" property="currentMailingAddressForm.endDate" styleClass="dateInput"/>
              </td>
            </tr>

                  <tr>
                    <td align="right" class="tablelabel">Shipping Type:</td>
                    <td>
                        <nested:checkbox property="currentMailingAddressForm.shipToType"/>
                    </td>
                  </tr>
                  <tr>
                    <td align="right" class="tablelabel">Billing Type:</td>
                    <td>
                        <nested:checkbox property="currentMailingAddressForm.billToType"/>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="2"></td>
                  </tr>
            <tr>
              <td colspan="2" align="Center"><big><i>-- International (below this line) --</i></big></td>
            </tr>
                      <tr>
                    <td align="right" class="tablelabel">Is International Address?</td>
                    <td>
                        <nested:checkbox property="currentMailingAddressForm.intlFlagB"/>
                    </td>
                  </tr>
            <tr>
               <td align="right" class="tablelabel">Country:</td>
               <td>
                   <nested:text property="currentMailingAddressForm.countryName" size="20" maxlength="50" />
               </td>
             </tr>
            <!--<tr>-->
               <!--<td align="right" class="tablelabel">Country Code:</td>-->
               <!--<td>-->
                   <%--<nested:text property="currentMailingAddressForm.countryCode" size="5" maxlength="3" />--%>
               <!--</td>-->
             <!--</tr>-->

                 <tr>
                    <td align="right" class="tablelabel">International Postal Code:</td>
                    <td>
                        <nested:text property="currentMailingAddressForm.internationalZip" size="14" maxlength="14" />
                    </td>
                  </tr>

                  <tr>
                    <td></td>
                    <td>
                        <nested:notEmpty name="vendorsForm" property="mailingAddressId">
                            <input type="SUBMIT" value="Save"
                                   onclick="this.form.action='editMailingAddress.do';
                                   this.form.cmd.value = '<%=Command.MAINTAIN_MAILING_ADDRESSES%>';
                                   this.form.submit(); return false;"/>
                        </nested:notEmpty>
                        <nested:empty name="vendorsForm" property="mailingAddressId">
                            <input type="SUBMIT" value="Save"
                                   onclick="this.form.action='addMailingAddress.do';
                                   this.form.cmd.value = '<%=Command.MAINTAIN_MAILING_ADDRESSES%>';
                                   this.form.submit(); return false;"/>
                        </nested:empty>
                        <button onclick="this.form.action='viewEditVendor.do';
                        this.form.cmd.value='backToEditVendor';
                        document.getElementsByName('currentMailingAddressForm.address1')[0].value='<nested:write property="currentMailingAddressForm.address1"/>';
                        document.getElementsByName('currentMailingAddressForm.address2')[0].value='<nested:write property="currentMailingAddressForm.address2"/>';
                        document.getElementsByName('currentMailingAddressForm.city')[0].value='<nested:write property="currentMailingAddressForm.city"/>';
                        document.getElementsByName('currentMailingAddressForm.state')[0].value='<nested:write property="currentMailingAddressForm.state"/>';
                        document.getElementsByName('currentMailingAddressForm.zip')[0].value='<nested:write property="currentMailingAddressForm.zip"/>';
                        document.getElementsByName('currentMailingAddressForm.shipToType')[0].checked=<nested:write property="currentMailingAddressForm.shipToType"/>;
                        document.getElementsByName('currentMailingAddressForm.billToType')[0].checked=<nested:write property="currentMailingAddressForm.billToType"/>;
                        this.form.submit(); return false;">Cancel</button>
                    </td>
                  </tr>
                </table>
              </td>
             </tr>
             <tr>
              <td>&nbsp;</td>
            </tr>
          </table>
        </html:form>
    </body>
</html>
