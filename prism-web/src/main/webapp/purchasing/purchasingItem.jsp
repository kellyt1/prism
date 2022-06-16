<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>Edit Purchase Item</title>
    </head>
    <body>
        <html:form action="savePurchasingItem" method="post">
          <input type="HIDDEN" name="input"/>
          <table cellspacing="0" cellpadding="0" border="0" width="90%" align="center">
            <tr>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <nested:notEmpty property="itemId">
                <th class="tableheader">Edit Purchase Item</th>
              </nested:notEmpty>
              <nested:empty property="itemId">
                <th class="tableheader">Create Purchase Item</th>
              </nested:empty>
            </tr>
            <tr>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>
                <table cellspacing="0" cellpadding="3" border="0" width="100%">
                  <tr>
                    <td align="right" class="tablelabel">Item ID #:</td>
                    <td>
                        <nested:empty property="itemId">
                            Unnassigned
                        </nested:empty>
                        <nested:notEmpty property="itemId">
                                <strong><nested:write property="itemId"/></strong>
                        </nested:notEmpty>
                    </td>
                    <td align="right" class="tablelabel">Description:</td>
                    <td>
                        <nested:text property="description" size="40"/>
                    </td>
                    <td align="right" class="tablelabel">Category:</td>
                    <td>
                        <nested:select property="categoryId">
                            <nested:optionsCollection property="categories" label="name" value="categoryId"/>
                        </nested:select>
                    </td>
                    <td align="right" class="tablelabel">Manufacturer:</td>
                    <td>
                        <nested:select property="manufacturerId">
                            <nested:optionsCollection property="manufacturers" label="externalOrgDetail.orgName" value="manufacturerId"/>
                        </nested:select>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="8">&nbsp;</td>
                  </tr>
                  <tr>
                    <td align="left" colspan="4">
                      <table cellspacing="3" cellpadding="0" border="0" align="left">
                        <tr>
                          <td class="tablelabel">Available Vendors:</td>
                          <td>&nbsp;</td>
                          <td class="tablelabel">Selected Vendors:</td>
                        </tr>
                        <tr>
                          <td>
                            <nested:select property="availableVendorId">
                                <nested:optionsCollection property="availableVendors" label="externalOrgDetail.orgName" value="vendorId"/>
                            </nested:select>
                          </td>
                          <td align="center" valign="middle">
                            <input type="submit" value="&nbsp;&nbsp;&#62;&nbsp;&nbsp;"/><br>
                            <nested:notEmpty property="itemVendors">
                                <input type="submit" value="&nbsp;&nbsp;&#60;&nbsp;&nbsp;"/>
                            </nested:notEmpty>
                            <nested:empty property="itemVendors">
                                <input disabled="disabled" type="submit" value="&nbsp;&nbsp;&#60;&nbsp;&nbsp;"/>
                            </nested:empty>
                          </td>
                          <td>
                            <nested:notEmpty property="itemVendors">
                                <nested:select property="availableVendorId">
                                    <nested:optionsCollection property="availableVendors" label="externalOrgDetail.orgName" value="vendorId"/>
                                </nested:select>
                            </nested:notEmpty>
                            <nested:empty property="itemVendors">
                                No Vendors assigned<br>to this Item
                            </nested:empty>
                          </td>
                        </tr>
                      </table>
                    </td>
                    <td colspan="4" align="right">
                        <table cellspacing="3" cellpadding="0" border="0" align="left">
                            <tr>
                                <td colspan="2" class="tablelabel">Contract Info for this Item and Vendor:</td>
                            </tr>
                            <nested:notEmpty property="itemVendor">
                                <tr>
                                    <td class="tablelabel">Contract #:</td>
                                    <td>
                                        <nested:text property="contractNumber" size="20"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="tablelabel">Delivery Terms:</td>
                                    <td>
                                        <nested:textarea  property="contractDeliveryTerms" rows="4" cols="30"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="tablelabel">Contract Start Date:</td>
                                    <td>
                                        <nested:text  property="contractStartDate" size="10"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="tablelabel">Contract End Date:</td>
                                    <td>
                                        <nested:text  property="contractEndDate" size="10"/>
                                    </td>
                                </tr>
                            </nested:notEmpty>
                        </table>
                    </td>
                  </tr>
                  <tr>
                    <td align="center" colspan="8">
                      <nested:equal property="input" value="orderLineItem">
                        <input type="submit" value="Save"
                             onclick="this.form.action='associateNewItemWithOrderLineItem.do'"/>
                      </nested:equal>
                      <nested:equal property="input" value="advancedSearchItems">
                        <input type="submit" value="Save"/>
                      </nested:equal>
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
