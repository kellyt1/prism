<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>Purchase Item</title>
        <script type="text/javascript">
            jQuery(document).ready(function () {
                // Find ALL <form> tags on your page
                jQuery('form').submit(function () {
                    // On submit disable its submit button
                    jQuery('input[type=submit]', this).attr('disabled', 'disabled');
                });
            });
        </script>
    </head>
    <body>
        <nested:form action="/savePurchaseItem.do" method="post" enctype="multipart/form-data">
            <nested:hidden property="cmd" value=""  />

          <br />
          <table cellspacing="0" cellpadding="0" border="0" width="90%" align="center">
            <tr>
              <nested:notEmpty property="itemId">
                <th class="tableheader" colspan="4">Edit Purchase Item</th>
              </nested:notEmpty>
              <nested:empty property="itemId">
                <th class="tableheader"  colspan="4">Create Purchase Item</th>
              </nested:empty>
            </tr>
            <tr>
              <td>&nbsp;</td>
            </tr>

            <nested:notEmpty property="itemId">
                <tr>
                  <td>
                    <nested:checkbox property="endDatePurchaseItem" value="true"/>Remove This Purchase Item<br/>
                  </td>
                  <td colspan="3">&nbsp;</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
            </nested:notEmpty>

            <tr>
                <td align="left" class="tablelabel">Item ID #:</td>                    
                <td align="left" class="tablelabel">Description that purchasing sees:</td>
                <td align="left" class="tablelabel">Category:</td>
                <td align="left" class="tablelabel">
                    <logic:notEqual name="skin" value="PRISM2">
                    Object Code:
                    </logic:notEqual>
                </td>

            </tr>
            <tr>
                <td valign="top">
                    <nested:empty property="itemId">
                        Unnassigned
                    </nested:empty>
                    <nested:notEmpty property="itemId">
                        <strong><nested:write property="itemId"/></strong>
                    </nested:notEmpty>
                </td>
                <td>
                    <nested:textarea property="description" rows="2" cols="30"/>
                </td>
                <td valign="top">
                    <nested:select property="categoryId">
                        <option value="" selected="selected" ></option>
                        <nested:optionsCollection property="categories" label="name" value="categoryId"/>
                    </nested:select>
                </td>
                <td valign="top">
                    <logic:notEqual name="skin" value="PRISM2">
                    <nested:select property="objectCodeId" style="width:200px">
                        <option value="" selected="selected" ></option>
                        <nested:optionsCollection property="objectCodes" label="name" value="objectCodeId"/>
                    </nested:select>
                    </logic:notEqual>
                </td>
            </tr>
                  
              <tr>
                <td colspan="4">&nbsp;</td>
              </tr>
                  
              <tr>
                  <logic:notEqual name="skin" value="PRISM2">
                      <td align="left" class="tablelabel">Manufacturer:</td>
                      <td align="left" class="tablelabel">Model:</td>
                      <td align="left" class="tablelabel">Dispense Unit:</td>
                      <td align="left" class="tablelabel">Description that user sees</td>
                      <td align="left" class="tablelabel">Hazardous</td>
                  </logic:notEqual>

                  <logic:equal name="skin" value="PRISM2">
                    <td></td>

                    <td align="left" class="tablelabel">Model:</td>
                    <td align="left" class="tablelabel">Dispense Unit:</td>
                  </logic:equal>
              </tr>
              <tr>
                <td valign="top">
                    <logic:notEqual name="skin" value="PRISM2">
                    <nested:select property="manufacturerId">
                        <option value="" selected="selected" ></option>
                        <nested:optionsCollection property="manufacturers" label="externalOrgDetail.orgName" value="manufacturerId"/>
                    </nested:select>
                    </logic:notEqual>
                </td>
                <td valign="top">
                    <nested:text property="model" size="20" />
                </td>
                <td valign="top">
                    <nested:select property="dispenseUnitId">
                        <option value="" selected="selected" ></option>
                        <nested:optionsCollection property="units" label="name" value="unitId"/>
                    </nested:select>
                </td>
                <td valign="top">
<logic:notEqual name="skin" value="PRISM2">
                    <nested:radio property="hazardous" value="true"/>Yes<br/>
                    <nested:radio property="hazardous" value="false"/>No
</logic:notEqual>
<logic:equal name="skin" value="PRISM2">
                    <nested:hidden property="hazardous" value="false"/>
</logic:equal>
                </td>
              </tr>
            <tr>
                <td colspan="1"></td>
                <td align="left" class="tablelabel">Dispense Unit Cost:</td>
                <td align="left" class="tablelabel">Dispense User Sees:</td>
            </tr>
            <tr>
                <td colspan="1"></td>
                <td valign="top">
                  <nested:text property="dispenseUnitCost" size="10" />
                </td>
                <td>
                    <nested:textarea property="descriptionForUser" rows="2" cols="30"/>
                </td>
            </tr>

            <tr>
               <td  colspan="4">
                <label>Attach Documents/Upload File:</label>
                <nested:file name="purchaseItemForm" property="printSpecFile" style="width:200px"/>
                   </td>
            </tr>

<tr>
    <td colspan="4">
        <%--<nested:present property="item.attachedFiles" >--%>
        <nested:notEmpty property="attachedFiles" >
            <% java.util.Date dt = new java.util.Date (); %>
            <br>
            <i class="attachment">Download/Open File(s): <br>
            <nested:iterate property="attachedFiles" id="attachedFile" >
                &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<a href=${pageContext.request.contextPath}/inventory/downloadFileAction.do?attachedFileId=<nested:write name="attachedFile" property='attachedFileId'/>&dt=<%=dt.getTime()%> ><nested:write name="attachedFile" property='fileName'/> </a>
                <br>
            </nested:iterate>
            </i>
        </nested:notEmpty>
        <%--</nested:present>--%>

    </td>
</tr>
              <nested:equal name="skin" value="PRISM2">
                  <nested:present property="deliveryDetail">
                      <tr>
                          <td>
                              <br/>
                              <div class="panel panel-default">
                                  <div class="panel-heading">Default Deliver To</div>
                                  <div class="panel-body">
                                      <div class="row" onload="console.print('Please double check Deliver To address.')">
                                          <div class="col-xs-6">Organization:</div>
                                          <div class="col-xs-6">
                                              <nested:present property="deliveryDetail.organization">
                                                  <nested:write property="deliveryDetail.organization.orgName" />
                                              </nested:present>
                                              <nested:notPresent property="deliveryDetail.organization">
                                                  <nested:present property="deliveryDetail.facility">MDH</nested:present>
                                              </nested:notPresent>
                                          </div>
                                      </div>
                                      <br/>
                                      <div class="row">
                                          <div class="col-xs-6">Recipient:</div>
                                          <div class="col-xs-6"><nested:write property="deliveryDetail.recipientName" /></div>
                                      </div>
                                      <br/>
                                      <div class="row">
                                          <div class="col-xs-6">Mailing Address:</div>
                                          <div class="col-xs-6">
                                              <nested:present property="deliveryDetail.facility">
                                                  <nested:write property="deliveryDetail.facility.facilityName" /><br />
                                              </nested:present>
                                              <nested:present property="deliveryDetail.mailingAddress">
                                                  <nested:write property="deliveryDetail.mailingAddress.address1" /><br />
                                                  <nested:present property="deliveryDetail.mailingAddress.address2">
                                                      <nested:write property="deliveryDetail.mailingAddress.address2" /><br />
                                                  </nested:present>
                                                  <nested:write property="deliveryDetail.mailingAddress.city" />,&nbsp;
                                                  <nested:write property="deliveryDetail.mailingAddress.state" />&nbsp;
                                                  <nested:write property="deliveryDetail.mailingAddress.zip" />
                                              </nested:present>
                                          </div>
                                      </div>
                                      <br/>
                                      <div class="row">
                                          <div class="col-md-6 text-center">
                                              <html:submit value="Change Delivery Info" onclick="this.form.cmd.value='changeDeliveryInfo'; this.form.submit(); return false;" styleClass="btn btn-primary"/>
                                          </div>
                                          <div class="col-md-6 text-center">
                                              <html:submit value="Remove Delivery Info" onclick="this.form.cmd.value='removeDeliveryInfo'; this.form.submit(); return false;" styleClass="btn btn-default"/>
                                          </div>
                                      </div>

                                  </div>
                              </div>
                          </td>
                      </tr>
                  </nested:present>
                  <nested:notPresent property="deliveryDetail">
                      <tr>
                          <td>
                              <br/>
                              <div class="panel panel-default">
                                  <div class="panel-heading">Default Deliver To</div>
                                  <div class="panel-body">
                                      <html:submit value="Add Default Delivery Info" onclick="this.form.cmd.value='changeDeliveryInfo'; this.form.submit(); return false;" styleClass="btn btn-primary"/>
                                  </div>
                              </div>
                          </td>
                      </tr>
                  </nested:notPresent>
              </nested:equal>

              <tr>
                <td colspan="4">&nbsp;</td>
              </tr>
              
              <tr>
                <td align="left" colspan="4">
                  <table cellspacing="0" cellpadding="5" border="0" align="left">
                    <tr>
                      <td class="tablelabel">Available Vendors:</td>
                      <td>&nbsp;</td>
                      <nested:empty property="itemVendors">
                          <td class="tablelabel">Selected Vendors:</td>
                      </nested:empty>
                      <nested:notEmpty property="itemVendors">
                          <td class="tablelabel">Selected Vendors:(Double click a selected vendor in order to set the vendor contract)</td>
                      </nested:notEmpty>
                    </tr>
                    <tr>
                      <td>
                        <nested:select size="4" property="availableVendorId">
                            <nested:optionsCollection property="availableVendors" label="externalOrgDetail.orgName" value="vendorId"/>
                        </nested:select>
                      </td>
                      <td align="center" valign="middle">
                        <input type="submit" value="&nbsp;&nbsp;&#62;&nbsp;&nbsp;"
                               onclick="this.form.action='addPurchaseItemVendor.do'"/><br>
                        <nested:notEmpty property="itemVendors">
                            <input type="submit" value="&nbsp;&nbsp;&#60;&nbsp;&nbsp;"
                                   onclick="this.form.action='removePurchaseItemVendor.do'"/>
                        </nested:notEmpty>
                        <nested:empty property="itemVendors">
                            <input disabled="disabled" type="submit" value="&nbsp;&nbsp;&#60;&nbsp;&nbsp;"/>
                        </nested:empty>
                      </td>
                      <td>
                        <nested:notEmpty property="itemVendors">
                            <nested:select size="4" property="itemVendorId" ondblclick="this.form.action='viewPurchaseItemVendorDetail.do';this.form.submit(); return false;">
                                <nested:optionsCollection property="itemVendors" label="vendor.externalOrgDetail.orgName" value="vendor.vendorId"/>
                            </nested:select>
                        </nested:notEmpty>
                        <nested:empty property="itemVendors">
                            No Vendors assigned<br>to this Item
                        </nested:empty>
                      </td>                        
                  </tr>
                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>
                  <nested:notEmpty property="itemVendorId">
                      <tr>
                        <th align="left" class="tableheader" colspan="6">Contract Information</th>
                      </tr>
                      <tr>
                        <td colspan="2" valign="top">
                            <table cellspacing="0" cellpadding="3" border="0">
                                <nested:notEmpty property="contract">
                              <tr>
                                <td align="right" class="tablelabel">Associate with Existing Contract</td>
                                <td>
                                    <nested:radio property="contractType" value="EXISTING"
                                         onchange="this.form.action='choosePurchaseItemContractType.do'; this.form.submit(); return false;"/>
                                </td>
                              </tr>
                                </nested:notEmpty>
                              <tr>
                                <td align="right" class="tablelabel">Create new Contract</td>
                                <td>
                                    <nested:radio property="contractType" value="NEW"
                                        onchange="this.form.action='choosePurchaseItemContractType.do'; this.form.submit(); return false;"/>
                                </td>
                              </tr>
                              <tr>
                                <td align="right" class="tablelabel">No Contract</td>
                                <td>
                                    <nested:radio property="contractType" value="NONE"
                                        onchange="this.form.action='choosePurchaseItemContractType.do'; this.form.submit(); return false;"/>
                                </td>
                              </tr>
                            </table>
                        </td>
                        <td colspan="4">
                           <nested:notEqual property="contractType" value="NONE">
                                <table cellspacing="0" cellpadding="3" border="0" align="left">
                                      <tr>
                                        <nested:equal property="contractType" value="NEW">
                                            <td></td>
                                            <td class="tablelabel">Enter New Contract Information for this Item and Vendor:</td>
                                        </nested:equal>
                                        <nested:equal property="contractType" value="EXISTING">
                                            <td></td>
                                            <td class="tablelabel">Choose an existing Contract:</td>
                                        </nested:equal>
                                      </tr>
                                      <nested:equal property="contractType" value="EXISTING">
                                            <tr>
                                                <td></td>
                                                <td>
                                                    <nested:notEmpty property="contracts">
                                                        <nested:select property="contractId">
                                                            <nested:optionsCollection property="contracts" label="contractNumber" value="vendorContractId"/>
                                                        </nested:select>
                                                    </nested:notEmpty>
                                                </td>
                                            </tr>
                                      </nested:equal>
                                      <tr>
                                        <td class="tablelabel" align="right">Contract #:</td>
                                        <td>
                                          <nested:text property="contractNumber" size="20"/>
                                        </td>
                                      </tr>
                                      <tr>
                                        <td class="tablelabel" align="right">Delivery Terms:</td>
                                        <td>
                                          <nested:textarea property="contractDeliveryTerms" rows="4" cols="30"/>
                                        </td>
                                      </tr>
                                      <tr>
                                        <td class="tablelabel" align="right">Contract Start Date:</td>
                                        <td>
                                          <nested:text property="contractStartDate" size="10" styleClass="dateInput"/>
                                        </td>
                                      </tr>
                                      <tr>
                                        <td class="tablelabel" align="right">Contract End Date:</td>
                                        <td>
                                          <nested:text property="contractEndDate" size="10" styleClass="dateInput"/>
                                        </td>
                                      </tr>
                                </table>
                            </nested:notEqual>
                        </td>
                      </tr>
                  </nested:notEmpty>
                  <tr>
                    <td align="center" colspan="4">
                      <nested:equal property="input" value="orderLineItem">
                        <input class="btn btn-primary" type="submit" value="Save"
                             onclick="this.form.action='associateNewItemWithOrderLineItem.do'"/>
                        <%--<button onclick="this.form.action='viewAdvancedSearchItemsPurchasing.do';this.form.submit(); return false;">Cancel</button>--%>
                        <a class="btn btn-danger" href="/viewAdvancedSearchItemsPurchasing.do">Cancel</a>
                      </nested:equal>
                      <nested:equal property="input" value="advancedSearchItems">
                        <input class="btn btn-primary" type="submit" value="Save"/>
                        <%--<button onclick="this.form.action='viewAdvancedSearchItemsPurchasing.do';this.form.submit(); return false;">Cancel</button>--%>
                          <a class="btn btn-danger" href="/viewAdvancedSearchItemsPurchasing.do">Cancel</a>
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
          </nested:form>
  </body>
</html>
