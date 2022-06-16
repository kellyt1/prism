<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<html>
  <head>
    <title>Evaluate Stock Action Requests</title>
  </head>
  <body>
    <html:form action="/evaluateStockItemActionRequest" method="post">

            <bean:define id="hColor"  value="cyan"/>
      <html:hidden property="cmd"/>

      <TABLE width="80%" cellspacing="1" align="center">
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <th colspan="5" class="tableheader">Open Stock Action Requests</th>
        </tr>
        <tr>
          <td colspan="4">&nbsp;</td>
        </tr>
        <tr>
          <td>Action Type: </td>
          <td>Action Requested By: </td>
          <td>Date Requested: </td>
          <td>Asst Division Director: </td>
          <nested:notEmpty property="stockItem">
            <td>Item ICNBR: </td>
          </nested:notEmpty>
        </tr>
        <tr class="tabledetail">
          <td height="20">
            <nested:write property="actionRequestType.name"/>
          </td>
          <td>
            <nested:write property="requestor.firstAndLastName"/>
          </td>
          <td>
            <nested:write property="requestedDate"/>
          </td>
          <td>
            <nested:notEmpty property="potentialStockItemForm.asstDivDirector">
              <nested:write property="potentialStockItemForm.asstDivDirector.firstAndLastName"/>
            </nested:notEmpty>
          </td>
          <nested:notEmpty property="stockItem">
              <td>
                <nested:write property="potentialStockItemForm.fullIcnbr"/>
              </td>
          </nested:notEmpty>
        </tr>

        <tr><td></td></tr>
        <tr>
          <nested:notEmpty property="stockItem">
            <td width="6">Value:</td> 
          </nested:notEmpty> 
          <td>Item Description:</td> 
          <td>Category:</td> 
          <td>Primary Contact:</td> 
          <td>Secondary Contact:</td> 
        </tr> 
        <nested:notEmpty property="stockItem" > 
          <tr class="tabledetail"> 
            <td width="6">Current:</td>
            <td>

              <nested:write  property="stockItem.description"/> 
            </td> 
            <td> 
              <nested:select property="stockItem.category.categoryId" disabled="true"> 
                <nested:optionsCollection property="potentialStockItemForm.categories" label="name" value="categoryId"/> 
              </nested:select> 
            </td> 
            <td><nested:write property="stockItem.primaryContact.firstAndLastName"/></td>
            <td><nested:write property="stockItem.secondaryContact.firstAndLastName"/></td>
          </tr> 
        </nested:notEmpty> 
        <tr class="tabledetail">
          <nested:notEmpty property="stockItem" > 
              <td>New:</td>
          </nested:notEmpty> 
          <!--<td> -->
            <logic:empty  name="evaluateStockItemActionRequestForm" property="stockItem"><td></logic:empty>
            <logic:notEmpty  name="evaluateStockItemActionRequestForm" property="stockItem">
            <logic:notEmpty  name="evaluateStockItemActionRequestForm" property="stockItem.description">
             <bean:define id="hDescription" name="evaluateStockItemActionRequestForm" property="stockItem.description"  />

                        <logic:equal name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.description" value="<%= hDescription.toString().trim() %>">
                             <TD>
                        </logic:equal>
                     <logic:notEqual name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.description" value="<%= hDescription.toString().trim() %>">
                                <TD bgcolor="<%= hColor %>">
                         </logic:notEqual>
             </logic:notEmpty>
             </logic:notEmpty>
            <nested:write property="potentialStockItemForm.description"/>

          </td>

            <logic:empty  name="evaluateStockItemActionRequestForm" property="stockItem"><td></logic:empty>
            <logic:notEmpty  name="evaluateStockItemActionRequestForm" property="stockItem">

                                <logic:notEmpty name="evaluateStockItemActionRequestForm" property="stockItem.category.categoryId">
                                 <bean:define id="hCategoryId" name="evaluateStockItemActionRequestForm" property="stockItem.category.categoryId"  />

                      <logic:equal name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.categoryId" value="<%= hCategoryId.toString().trim() %>">
                             <TD>
                        </logic:equal>
                     <logic:notEqual name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.categoryId" value="<%= hCategoryId.toString().trim() %>">
                                <TD bgcolor="<%= hColor %>">
                         </logic:notEqual>
                                  </logic:notEmpty>
          </logic:notEmpty>
            <nested:select property="potentialStockItemForm.categoryId"> 
              <nested:optionsCollection property="potentialStockItemForm.categories" label="name" value="categoryId"/> 
            </nested:select> 
          </td>
                    <logic:empty  name="evaluateStockItemActionRequestForm" property="stockItem"><td></logic:empty>
            <logic:notEmpty  name="evaluateStockItemActionRequestForm" property="stockItem">
                    <logic:notEmpty name="evaluateStockItemActionRequestForm" property="stockItem.primaryContact.firstAndLastName">
                    <bean:define id="hPrimaryContact" name="evaluateStockItemActionRequestForm" property="stockItem.primaryContact.firstAndLastName"  />
                            <logic:equal name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.primaryContactName" value="<%= hPrimaryContact.toString().trim() %>">
                                 <TD>
                            </logic:equal>
                            <logic:notEqual name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.primaryContactName" value="<%= hPrimaryContact.toString().trim() %>">
                                    <TD bgcolor="<%= hColor %>">
                             </logic:notEqual>
                     </logic:notEmpty>
                         </logic:notEmpty>
            <nested:write property="potentialStockItemForm.primaryContactName"/>
          </td>                     <logic:empty  name="evaluateStockItemActionRequestForm" property="stockItem"><td></logic:empty>
            <logic:notEmpty  name="evaluateStockItemActionRequestForm" property="stockItem">
                                    <logic:notEmpty name="evaluateStockItemActionRequestForm" property="stockItem.secondaryContact.firstAndLastName">
                                     <bean:define id="hSecondaryContact" name="evaluateStockItemActionRequestForm" property="stockItem.secondaryContact.firstAndLastName"  />

                            <logic:equal name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.secondaryContactName" value="<%= hSecondaryContact.toString().trim() %>">
                                           <TD>
                                      </logic:equal>
                                   <logic:notEqual name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.secondaryContactName" value="<%= hSecondaryContact.toString().trim() %>">
                                              <TD bgcolor="<%= hColor %>">
                                       </logic:notEqual>
                                      </logic:notEmpty>
                                     </logic:notEmpty>
            <nested:write property="potentialStockItemForm.secondaryContactName"/>
          </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <nested:notEmpty property="stockItem">
            <td width="6">Value:</td> 
          </nested:notEmpty> 
          <td >Current on-hand:</td>
          <td >Est. Annual Usage:</td>  
          <td><bean:message key="orgBudget"/>:</td>
          <td>Dispense Unit:</td>
          <nested:notEqual value="NSI" property="actionRequestType.code"> 
            <td >Status:</td> 
          </nested:notEqual> 
        </tr>
        <nested:notEmpty property="stockItem"> 
          <tr>
            <td width="6">Current:</td>
            <td><nested:write property="stockItem.qtyOnHand" /></td>
            <td><nested:write property="stockItem.estimatedAnnualUsage"/></td>
            <td><nested:write property="stockItem.orgBudget.orgBudgetCodeAndNameAndFY"/></td>
            <td><nested:write property="stockItem.dispenseUnit.name"/></td>
            <TD><nested:write property="stockItem.status.name"/></TD>
          </tr> 
        </nested:notEmpty> 
        <tr class='tabledetail'> 
          <nested:notEmpty property="stockItem"> 
            <td width="6" > 
                New: 
            </td> 
          </nested:notEmpty>
            <logic:empty  name="evaluateStockItemActionRequestForm" property="stockItem"><td></logic:empty>
            <logic:notEmpty  name="evaluateStockItemActionRequestForm" property="stockItem">
            <logic:notEmpty name="evaluateStockItemActionRequestForm" property="stockItem.qtyOnHand">
                    <bean:define id="hQtyOnHand" name="evaluateStockItemActionRequestForm" property="stockItem.qtyOnHand"  />
                      <logic:equal name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.qtyOnHand" value="<%= hQtyOnHand.toString().trim() %>">
                          <TD>
                     </logic:equal>
                  <logic:notEqual name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.qtyOnHand" value="<%= hQtyOnHand.toString().trim() %>">
                             <TD bgcolor="<%= hColor %>">
                      </logic:notEqual>
              </logic:notEmpty>
               </logic:notEmpty>
            <nested:write property="potentialStockItemForm.qtyOnHand" />
          </td>
                         <logic:empty  name="evaluateStockItemActionRequestForm" property="stockItem"><td></logic:empty>
            <logic:notEmpty  name="evaluateStockItemActionRequestForm" property="stockItem">
                             <logic:notEmpty name="evaluateStockItemActionRequestForm" property="stockItem.estimatedAnnualUsage">
                                <bean:define id="estimatedAnnualUsage" name="evaluateStockItemActionRequestForm" property="stockItem.estimatedAnnualUsage"  />

                                   <logic:equal name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.estimatedAnnualUsage" value="<%= estimatedAnnualUsage.toString().trim() %>">
                                       <TD>
                                  </logic:equal>
                                   <logic:notEqual name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.estimatedAnnualUsage" value="<%= estimatedAnnualUsage.toString().trim() %>">
                                          <TD bgcolor="<%= hColor %>">
                                   </logic:notEqual>
                              </logic:notEmpty>
                   </logic:notEmpty>

            <nested:write property="potentialStockItemForm.estimatedAnnualUsage"/>
          </td>
            <logic:empty  name="evaluateStockItemActionRequestForm" property="stockItem"><td></logic:empty>
            <logic:notEmpty  name="evaluateStockItemActionRequestForm" property="stockItem">
           <logic:notEmpty name="evaluateStockItemActionRequestForm" property="stockItem.orgBudget.orgBudgetCodeAndNameAndFY">
                     <bean:define id="hOrgCode" name="evaluateStockItemActionRequestForm" property="stockItem.orgBudget.orgBudgetCodeAndNameAndFY"  />
                    <logic:equal name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.orgBudgetForm.orgBudgetCodeAndNameAndFY" value="<%= hOrgCode.toString().trim() %>">
                        <TD>
                   </logic:equal>
                    <logic:notEqual name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.orgBudgetForm.orgBudgetCodeAndNameAndFY" value="<%= hOrgCode.toString().trim() %>">
                           <TD bgcolor="<%= hColor %>">
                    </logic:notEqual>
            </logic:notEmpty>
            </logic:notEmpty>
            <nested:write property="potentialStockItemForm.orgBudgetForm.orgBudgetCodeAndNameAndFY"/>
          </td>
                           <logic:empty  name="evaluateStockItemActionRequestForm" property="stockItem"><td></logic:empty>
            <logic:notEmpty  name="evaluateStockItemActionRequestForm" property="stockItem">
                           <logic:notEmpty name="evaluateStockItemActionRequestForm" property="stockItem.dispenseUnit.name">
                               <bean:define id="hDispenseUnit" name="evaluateStockItemActionRequestForm" property="stockItem.dispenseUnit.name"  />
                                <logic:equal name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.dispenseUnit.name" value="<%= hDispenseUnit.toString().trim() %>">
                                     <TD>
                                </logic:equal>
                             <logic:notEqual name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.dispenseUnit.name" value="<%= hDispenseUnit.toString().trim() %>">
                                        <TD bgcolor="<%= hColor %>">
                                 </logic:notEqual>
                      </logic:notEmpty>
                      </logic:notEmpty>
            <nested:write property="potentialStockItemForm.dispenseUnit.name"/>

          </td>

          <nested:notEmpty property="stockItem">
              <nested:notEqual value="ACT" property="potentialStockItemForm.status.statusCode">
                         <logic:empty  name="evaluateStockItemActionRequestForm" property="stockItem"><td></logic:empty>
            <logic:notEmpty  name="evaluateStockItemActionRequestForm" property="stockItem">
                         <logic:notEmpty name="evaluateStockItemActionRequestForm" property="stockItem.status.name">
                                  <bean:define id="hSaveStatus" name="evaluateStockItemActionRequestForm" property="stockItem.status.name"  />
                                  <logic:equal name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.status.name" value="<%= hSaveStatus.toString() %>">
                                       <TD>
                                  </logic:equal>
                                   <logic:notEqual name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.status.name" value="<%= hSaveStatus.toString() %>">
                                          <TD bgcolor="<%= hColor %>">
                                   </logic:notEqual>
                         </logic:notEmpty>
                         </logic:notEmpty>
                    <a href="" onclick="window.open('./inventory/statusDetails.jsp','','toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=no, HEIGHT=150, WIDTH=350, left=50, top=50');return false"> 
                        <nested:write property="potentialStockItemForm.status.name"/></a> 
                  </TD> 
              </nested:notEqual>
              <nested:equal value="ACT" property="potentialStockItemForm.status.statusCode">
                                  <logic:empty  name="evaluateStockItemActionRequestForm" property="stockItem"><td></logic:empty>
            <logic:notEmpty  name="evaluateStockItemActionRequestForm" property="stockItem">
                                  <logic:notEmpty name="evaluateStockItemActionRequestForm" property="stockItem.status.name">
                                          <bean:define id="hSaveStatus2" name="evaluateStockItemActionRequestForm" property="stockItem.status.name"  />
                                          <logic:equal name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.status.name" value="<%= hSaveStatus2.toString() %>">
                                               <TD>
                                          </logic:equal>
                                           <logic:notEqual name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.status.name" value="<%= hSaveStatus2.toString() %>">
                                                  <TD bgcolor="<%= hColor %>">
                                           </logic:notEqual>
                                   </logic:notEmpty>
                </logic:notEmpty>
                      <nested:write property="potentialStockItemForm.status.name"/>
                  </td>
              </nested:equal>
          </nested:notEmpty>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <nested:notEmpty property="stockItem">
            <td width="6">Value:</td>
          </nested:notEmpty> 
          <td>Hazardous?</td>
          <td>Seasonal/Cyclical Item?</td>
          <nested:notEqual value="NSI" property="actionRequestType.code">
            <td>Reorder Point:</td>
            <td>Reorder Quantity:</td>
          </nested:notEqual>
        </tr>
        <nested:notEmpty property="stockItem">
          <tr class='tabledetail'> 
            <td>Current:</td>
            <TD> 
              <nested:radio property="stockItem.hazardous" value="true" disabled="true" />Yes 
              <br/> 
              <nested:radio property="stockItem.hazardous" value="false" disabled="true"/>No 
            </TD> 
            <td> 
              <nested:radio property="stockItem.seasonal" value="true" disabled="true" />Yes 
              <br/> 
              <nested:radio property="stockItem.seasonal" value="false" disabled="true" />No
            </td> 
            <nested:notEqual value="NSI" property="actionRequestType.code">
            <td>
                  <nested:write property="stockItem.reorderPoint" />
                </td>
                    <td>
                  <nested:write property="stockItem.reorderQty" />
                </td>
            </nested:notEqual>
          </tr>
          </nested:notEmpty>
          <tr class='tabledetail'> 
            <nested:notEmpty property="stockItem"> 
              <td width="6"> 
                  New: 
              </td> 
            </nested:notEmpty>
<logic:empty  name="evaluateStockItemActionRequestForm" property="stockItem"><td></logic:empty>
            <logic:notEmpty  name="evaluateStockItemActionRequestForm" property="stockItem">
              <logic:notEmpty name="evaluateStockItemActionRequestForm" property="stockItem.hazardous">
              <bean:define id="hHazardous" name="evaluateStockItemActionRequestForm" property="stockItem.hazardous"  />
                              <logic:equal name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.hazardous" value="<%= hHazardous.toString().trim() %>">
                                       <TD>
                                  </logic:equal>
                               <logic:notEqual name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.hazardous" value="<%= hHazardous.toString().trim() %>">
                                          <TD bgcolor="<%= hColor %>">
                                   </logic:notEqual>
              </logic:notEmpty>
</logic:notEmpty>
              <nested:radio property="potentialStockItemForm.hazardous" value="true"/>Yes 
              <br/> 
              <nested:radio property="potentialStockItemForm.hazardous" value="false"/>No 
            </TD>
<logic:empty  name="evaluateStockItemActionRequestForm" property="stockItem"><td></logic:empty>
            <logic:notEmpty  name="evaluateStockItemActionRequestForm" property="stockItem">
                                          <logic:notEmpty name="evaluateStockItemActionRequestForm" property="stockItem.seasonal">
                                          <bean:define id="hSeasonal" name="evaluateStockItemActionRequestForm" property="stockItem.seasonal"  />

                                      <logic:equal name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.seasonal" value="<%= hSeasonal.toString().trim() %>">
                                                               <TD>
                                                          </logic:equal>
                                                       <logic:notEqual name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.seasonal" value="<%= hSeasonal.toString().trim() %>">
                                                                  <TD bgcolor="<%= hColor %>">
                                                           </logic:notEqual>
                                           </logic:notEmpty>
</logic:notEmpty>
              <nested:radio property="potentialStockItemForm.seasonal" value="true"/>Yes 
              <br/> 
              <nested:radio property="potentialStockItemForm.seasonal" value="false"/>No 
            </td> 
            <nested:notEqual value="NSI" property="actionRequestType.code">
                      <logic:empty  name="evaluateStockItemActionRequestForm" property="stockItem"><td></logic:empty>
            <logic:notEmpty  name="evaluateStockItemActionRequestForm" property="stockItem">
                      <logic:notEmpty name="evaluateStockItemActionRequestForm" property="stockItem.reorderPoint">
                     <bean:define id="hReorderPoint" name="evaluateStockItemActionRequestForm" property="stockItem.reorderPoint"  />

                       <logic:equal name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.reorderPoint" value="<%= hReorderPoint.toString().trim() %>">
                             <TD>
                        </logic:equal>
                     <logic:notEqual name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.reorderPoint" value="<%= hReorderPoint.toString().trim() %>">
                                <TD bgcolor="<%= hColor %>">
                         </logic:notEqual>
                     </logic:notEmpty>
                             </logic:notEmpty>
                  <nested:write property="potentialStockItemForm.reorderPoint"/>
                </td>
               <logic:empty  name="evaluateStockItemActionRequestForm" property="stockItem"><td></logic:empty>
            <logic:notEmpty  name="evaluateStockItemActionRequestForm" property="stockItem">
                <logic:notEmpty name="evaluateStockItemActionRequestForm" property="stockItem.reorderQty">
                 <bean:define id="hReorderQty" name="evaluateStockItemActionRequestForm" property="stockItem.reorderQty"  />

                <logic:equal name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.reorderQty" value="<%= hReorderQty.toString().trim() %>">
                      <TD>
                 </logic:equal>
              <logic:notEqual name="evaluateStockItemActionRequestForm" property="potentialStockItemForm.reorderQty" value="<%= hReorderQty.toString().trim() %>">
                         <TD bgcolor="<%= hColor %>">
                  </logic:notEqual>
                  </logic:notEmpty>
                      </logic:notEmpty>
                  <nested:write property="potentialStockItemForm.reorderQty"/>
                </td>
            </nested:notEqual>

<%--            <nested:equal value="ACT" property="potentialStockItemForm.status.statusCode">--%>
<%--                <TD> --%>
<%--                  <nested:write property="potentialStockItemForm.status.name"/> --%>
<%--                </TD> --%>
<%--            </nested:equal>--%>
          </tr>
          <tr><td>&nbsp;</td></tr>

<tr >
    <td>Suggested Vendor:</td>
    <td>Vendor Catalog#:</td>
    <td>Buy Unit Cost:</td>
</tr>
<tr>
                <td valign="top" align="left">
                  <nested:notEmpty property="suggestedVendorId">
                      <nested:write property="suggestedVendor.externalOrgDetail.orgName"/>
                  </nested:notEmpty>
                  <nested:notEmpty property="suggestedVendorName">
                      <nested:write property="suggestedVendorName"/>
                  </nested:notEmpty>
                </td>
                <td><html:text name="evaluateStockItemActionRequestForm" property="vendorCatalogNbr" size="15" maxlength="50"/></td>
                <td><html:text name="evaluateStockItemActionRequestForm" property="vendorCost" size="10" maxlength="10"/></td>
   </tr>
          <tr>
              <td><bean:message key="changeRequestComments"/>:</td>
              <td>Handling Instructions:</td>
          </tr>
          <tr>
              <td><nested:textarea property="specialInstructions" cols="20" /></td>
              <TD><html:textarea property="potentialStockItemForm.instructions" cols="20" /></TD>
          </tr>

<%-- Left the code below commented for having it as an example when we need to make it again--%>
<%--        <tr> --%>
<%--          <td >Available Vendors</td>--%>
<%--          <td/> --%>
<%--          <td>Selected Vendors</td> --%>
<%--          <td>Buy Unit</td> --%>
<%--        </tr> --%>
<%--        <tr class='tabledetail'> --%>
<%--          <td>--%>
<%--            <nested:select multiple="multiple" property="potentialStockItemForm.availableVendorId"> --%>
<%--              <nested:optionsCollection property="potentialStockItemForm.availableVendors" label="externalOrgDetail.orgName" value="externalOrgDetail.orgId"/> --%>
<%--            </nested:select> --%>
<%--          </td> --%>
<%--          <td> --%>
<%--            <table> --%>
<%--              <tr> --%>
<%--                <td> --%>
<%--                  <input type="button" value="add" onclick="this.form.cmd.value='AddVendor'; this.form.submit(); return false;"/> --%>
<%--                </td> --%>
<%--              </tr> --%>
<%--              <tr> --%>
<%--                <td> --%>
<%--                  <input type="button" value="remove" onclick="this.form.cmd.value='RemoveVendor'; this.form.submit(); return false;"/> --%>
<%--                </td> --%>
<%--              </tr> --%>
<%--            </table> --%>
<%--          </td> --%>
<%--          <td> --%>
<%--            <nested:select size="4" property="potentialStockItemForm.itemVendorId" onchange="this.form.cmd.value='SelectVendor'; this.form.submit(); return false;"> --%>
<%--              <nested:optionsCollection property="potentialStockItemForm.itemVendors" label="vendor.externalOrgDetail.orgName" value="vendor.vendorId"/> --%>
<%--            </nested:select> --%>
<%--          </td> --%>
<%--          <td> --%>
<%--            <nested:notEmpty property="potentialStockItemForm.itemVendorId"> --%>
<%--                <nested:select property="potentialStockItemForm.buyUnitId" onchange="this.form.cmd.value='SelectBuyUnit';this.form.submit(); return false;"> --%>
<%--                    <option value="" label=""/> --%>
<%--                    <nested:optionsCollection property="potentialStockItemForm.units" label="name" value="unitId"/> --%>
<%--                </nested:select> --%>
<%--            </nested:notEmpty> --%>
<%--          </td> --%>
<%--        </tr> --%>
        <tr><td>&nbsp;</td></tr>
        <tr>
            <td colspan="3">Denial Reason</td>
        </tr>
        <tr>
            <nested:hidden property="approved"/>
            <td colspan="3"><nested:textarea property="evaluationReason" rows="2" cols="50"/></td>
        </tr>
        <tr> 
          <td> 
            <br/> 
            <html:submit value="Approve" onclick="this.form.approved.value=true;this.form.submit(); return false;" />
            <html:submit value="Deny" onclick="this.form.approved.value=false;this.form.submit(); return false;"/>
          </td>
        </tr> 
      </table>
    </html:form>
  </body>
</html>