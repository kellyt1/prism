<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>Search Orders</title>
    </head>
    <body>
        <html:form action="/searchOrdersForFinance" method="post">
            <html:hidden property="input"/>
            <table align="center" cellspacing="0" cellpadding="0" border="0" width="90%">
              <tr>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <th class="tableheader">Search Orders</th>
              </tr>
              <tr>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td>
                  <table align="center" cellpadding="3" cellspacing="0" border="0" style="background: #ffc;">
                    <tr>
                      <td class="tablelabel" align="right">OPR #:</td>
                      <td>
                        <html:text property="orderId" size="20"/>
                      </td>
                      <td class="tablelabel" align="right">Item Description:</td>
                      <td colspan="3">
                        <html:text property="itemDescription" size="40"/>
                      </td>
                    </tr>
                    <tr>
                      <td class="tablelabel" align="right">PO #:</td>
                      <td>
                        <html:text property="purchaseOrderNumber" size="20"/>
                      </td>
                      <td class="tablelabel" align="right">Vendor Name:</td>
                      <td>
                        <html:text property="vendorName" size="30" />
                      </td>
                      <td class="tablelabel" align="right">Status:</td>
                      <td>
                        <nested:select property="statusId">
                            <option value="">Any</option>
                            <nested:optionsCollection property="statuses" value="statusId" label="name"/>
                        </nested:select>
                      </td>
                    </tr>
                    <tr>
                      <td class="tablelabel" align="right">Buyer:</td>
                      <td>
                        <nested:select property="purchaserId">
                            <option value="">Any</option>
                            <nested:optionsCollection property="buyers" value="personId" label="lastAndFirstName"/>
                        </nested:select>
                      </td>
                      <td class="tablelabel" align="right">Request #:</td>
                      <td>
                        <html:text property="requestId" size="10"/>
                      </td>
                      <td class="tablelabel" align="right">Requestor:</td>
                      <td>
                        <nested:select property="requestorId">
                            <option value="">Any</option>
                            <nested:optionsCollection property="persons" value="personId" label="lastAndFirstName"/>
                        </nested:select>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="3" class="tablelabel" align="right">Ordered Between
                        <html:text property="orderedFrom" size="10" styleClass="dateInput"/>
                        &nbsp;&nbsp; and &nbsp;&nbsp;
                        <html:text property="orderedTo" size="10" styleClass="dateInput"/>
                      </td>
                      <td colspan="3" class="tablelabel" align="right">Suspense Date Between
                        <html:text property="suspenseDateFrom" size="10" styleClass="dateInput"/>
                        &nbsp;&nbsp; and &nbsp;&nbsp;
                        <html:text property="suspenseDateTo" size="10" styleClass="dateInput"/>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="6" align="center">
                        <input type="submit" value="Search" onclick="this.form.action='searchOrdersForFinance.do';"/>&nbsp;
                        <input type="RESET" value="Clear"/>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
              </tr>
              <nested:present property="searchResults">
              <tr>
                <td>
                  <table cellspacing="0" cellpadding="3" border="0" width="100%">
                    <tr>
                      <th>OPR #:</th>
                      <th>PO #:</th>                      
                      <th>Vendor:</th>
                      <th>Purchaser:</th>
                      <th>Date Created:</th>
                    </tr>
                    <nested:iterate property="searchResults">
                        <tr>
                            <td align="center">
                                <input type="SUBMIT" 
                                       value="<nested:write property='orderId'/>"
                                       onclick="this.form.action='viewOrderReadOnly.do'; this.form.orderId.value='<nested:write property='orderId'/>'"/>
                             </td>
                             <td align="center">
                               <nested:write property="purchaseOrderNumber" /> 
                             </td>
                             <td align="center">
                               <nested:write property="vendor.externalOrgDetail.orgName" />
                             </td>
                             <td align="center">
                               <nested:write property="purchaser.firstAndLastName" />
                             </td>
                             <td align="center">
                               <nested:write property="insertionDate" format="MM/dd/yyyy"/> 
                             </td>
                            </tr>
                    </nested:iterate>
                  </table>
                </td>
              </tr>
              </nested:present>
              <tr>
                <td>&nbsp;</td>
              </tr>
            </table>
        </html:form>
    </body>
</html>