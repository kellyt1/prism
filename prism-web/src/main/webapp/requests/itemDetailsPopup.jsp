<%@ page import="us.mn.state.health.model.inventory.Item"%>
<%@ include file="../include/tlds.jsp" %>
<html>
	<head>
	    <title>Item Details</title>
	</head>
	
	<body  bgcolor="#dadf7b">
    <%--<jsp:include page="../headers/header.jsp" flush="true"/>--%>
        <table cellspacing="2" cellpadding="3" border="1" width="50%" align='center' bgcolor="white">
		  <tr>
		    <th class='tableheader' colspan='2'>Item Details</th>
		  </tr>
		  <tr class="tabledetail">
		    <td width="30%" >Item Description</td>
		    <td><nested:write name="itemDetailsForm" property="item.description"/></td>
		  </tr>
		  <tr class="tabledetail">
		  	<td>Category Name</td>
		  	<td>    
                <nested:present name="itemDetailsForm" property="item.category">
                    <nested:write name="itemDetailsForm" property="item.category.name"/>
                </nested:present>
            </td>
		  </tr>
		  <tr class="tabledetail">
		  	<td>Category Code</td>
		  	<td>
                <nested:present name="itemDetailsForm" property="item.category">
                    <nested:write name="itemDetailsForm" property="item.category.categoryCode"/>
                </nested:present>
            </td>
		  </tr>
		  <tr class="tabledetail">
		  	<td>Dispense Unit</td>
		  	<td>
                <nested:present name="itemDetailsForm" property="item.dispenseUnit">
                    <nested:write name="itemDetailsForm" property="item.dispenseUnit.name"/>
                </nested:present>
            </td>
		  </tr>
		  <tr class="tabledetail">
		  	<td>Manufacturer</td>
		  	<td>
                <nested:present name="itemDetailsForm" property="item.manufacturer">
                    <nested:present name="itemDetailsForm" property="item.manufacturer.externalOrgDetail">
                        <nested:write name="itemDetailsForm" property="item.manufacturer.externalOrgDetail.orgName"/>
                    </nested:present>
                </nested:present>
            </td>
		  </tr>
		  <tr class="tabledetail">
		  	<td>Hazardous</td>
		  	<td><nested:write name="itemDetailsForm" property="item.hazardous"/></td>
		  </tr>
		  <tr class="tabledetail">
		  	<td>Insertion Date</td>
		  	<td><nested:write name="itemDetailsForm" property="item.insertionDate"/></td>
		  </tr>
            <nested:notEmpty name="itemDetailsForm" property="item.itemVendors">
                    <tr><td colspan=2><table cellspacing="3">
                                <th colspan="2" align="center">Vendor information</th>
                        <tr class='tabledetail'>
                                <td><i><b>Vendor(s):</b></i></td>
                                    <nested:iterate property="item.itemVendors" name="itemDetailsForm" id="vendor">
                                  <tr><td>
                                      <nested:write name="vendor" property="vendor.externalOrgDetail.orgName"/>
                                      </td></tr><td colspan=4><table cellpadding="2"><tr><td>
                                                  <nested:present name="vendor" property="vendor.externalOrgDetail.primaryPhone.number">
                                <nested:notEmpty name="vendor" property="vendor.externalOrgDetail.primaryPhone.number">
                                <td><i><b><small>Phone:</small></b></i></td><td><nested:write name="vendor" property="vendor.externalOrgDetail.primaryPhone.number"/></td>
                                </nested:notEmpty>
                            </nested:present>
                            <nested:present name="vendor" property="vendor.externalOrgDetail.primaryFax.number">
                                <nested:notEmpty name="vendor" property="vendor.externalOrgDetail.primaryFax.number">
                                <td><i><b><small>Fax:</small></b></i></td><td><nested:write name="vendor" property="vendor.externalOrgDetail.primaryFax.number"/></td>
                                </nested:notEmpty>
                            </nested:present>

                            <nested:present name="vendor" property="vendorCatalogNbr">
                                <nested:notEmpty name="vendor" property="vendorCatalogNbr">
                                 <td><i><b><small>Catlg:</small></b></i></td><td><nested:write name="vendor" property="vendorCatalogNbr"/></td>
                                </nested:notEmpty>
                             </nested:present>
                             <nested:present name="vendor" property="buyUnitCost">
                                 <nested:notEmpty name="vendor" property="buyUnitCost">
                                 <td><i><b><small>unit$:</small></b></i></td><td><nested:write name="vendor" property="buyUnitCost"/></td>
                                  </nested:notEmpty>
                              </nested:present>
                              <nested:present name="vendor" property="vendor.vendorAccounts">
                                      <nested:notEmpty name="vendor" property="vendor.vendorAccounts">
                                     <td><i><b><small>Acct(s):</small></b></i></td><td>
                                         <nested:iterate name="vendor" property="vendor.vendorAccounts" id="vendorAccount" >
                                                    <nested:write name="vendorAccount" property="accountNbr"/>
                                         <br/>
                                         </nested:iterate>
                                    </td>
                                  </nested:notEmpty>
                              </nested:present>
                      </tr>
                                      <%--<nested:notEmpty property="vendor.externalOrgDetail.primaryPhone.number">--%>
                                      <%--<td><i><b><small>Phone:</small></b></i></td><td><nested:write name="vendor" property="vendor.externalOrgDetail.primaryPhone.number"/></td>--%>
                                      <%--</nested:notEmpty>--%>
                                      <%--<nested:notEmpty property="vendor.externalOrgDetail.primaryFax.number">--%>
                                      <%--<td><i><b><small>Fax:</small></b></i></td><td><nested:write name="vendor" property="vendor.externalOrgDetail.primaryFax.number"/></td>--%>
                                      <%--</nested:notEmpty>--%>
                                      <%--<nested:notEmpty property="vendorCatalogNbr">--%>
                                       <%--<td><i><b><small>Catlg:</small></b></i></td><td><nested:write name="vendor" property="vendorCatalogNbr"/></td>--%>
                                       <%--</nested:notEmpty>--%>
                                       <%--<nested:notEmpty property="buyUnitCost">--%>
                                       <%--<td><i><b><small>unit$:</small></b></i></td><td><nested:write name="vendor" property="buyUnitCost"/></td>--%>
                                        <%--</nested:notEmpty>--%>
                                        <%--<nested:notEmpty property="vendor.vendorAccounts">--%>
                                       <!--<td><i><b><small>Acct(s):</small></b></i></td><td>-->
                                   <%--<nested:iterate name="vendor" property="vendor.vendorAccounts" id="vendorAccount" >--%>
                                              <%--<nested:write name="vendorAccount" property="accountNbr"/>--%>
                                   <!--<br/>-->
                                   <%--</nested:iterate>--%>
                                   <!--</td>-->
                                        <%--</nested:notEmpty>--%>
                                <!--</tr>-->
                                </table>
                                  </nested:iterate>

                                </td>
                        </tr>
                                </table>
                     </td></tr>
          </nested:notEmpty>
          <!--<tr class='tabledetail'>-->
			  	<!--<td>Vendors</td>-->
			  	<!--<td>-->
			  		<%--<nested:iterate property="item.itemVendors" name="itemDetailsForm" id="vendor">--%>
                		<%--<nested:write name="vendor" property="vendor.externalOrgDetail.orgName"/><br />--%>
					<%--</nested:iterate>--%>
			  	<!--</td>-->
		  <!--</tr>-->
		  <nested:equal value="true" property="isStockItem" name="itemDetailsForm">
			  <tr class='tableheader'>
			  	<th colspan='2'>Stock Item Details</th>
			  </tr>
			  <tr class='tabledetail'>
			  	<td>ICNBR</td>
			  	<td><nested:write name="itemDetailsForm" property="item.fullIcnbr"/></td>
			  </tr>
			  <tr class='tabledetail'>
			  	<td>Status</td>
			  	<td>
                    <nested:present name="itemDetailsForm" property="item.status">
                        <nested:write name="itemDetailsForm" property="item.status.name"/>
                    </nested:present>
                </td>
			  </tr>
			  <tr class='tabledetail'>
			  	<td>Org Bugdet</td>
			  	<td>
                    <nested:present name="itemDetailsForm" property="item.orgBudget">
                        <nested:write name="itemDetailsForm" property="item.orgBudget.orgBudgetCodeAndNameAndFY"/>
                    </nested:present>
                </td>
			  </tr>
			   <tr class='tabledetail'>
			  	<td>Qty on Hand</td>
			  	<td>
                    <nested:write name="itemDetailsForm" property="item.qtyOnHand"/>
                </td>
			  </tr>
			   <tr class='tabledetail'>
			  	<td>Current Demand</td>
			  	<td>
                   <nested:write name="itemDetailsForm" property="item.currentDemand"/>
                </td>
			  </tr>
			  <tr class='tabledetail'>
			  	<td>Seasonal</td>
			  	<td>
                    <nested:write name="itemDetailsForm" property="item.seasonal"/>
                </td>
			  </tr>
			  <tr class='tabledetail'>
			  	<td>Primary Contact</td>
			  	<td>   
                    <nested:present name="itemDetailsForm" property="item.primaryContact">
                        <nested:write name="itemDetailsForm" property="item.primaryContact.firstAndLastName"/>
                    </nested:present>
                </td>
			  </tr>
			  <tr class='tabledetail'>
			  	<td>Secondary Contact</td>
			  	<td>
                    <nested:present name="itemDetailsForm" property="item.secondaryContact">
                        <nested:write name="itemDetailsForm" property="item.secondaryContact.firstAndLastName"/>
                    </nested:present> 
                </td>
			  </tr>
			  <tr class='tabledetail'>
			  	<td>Assistant Division Director</td>
			  	<td>
                    <nested:present name="itemDetailsForm" property="item.asstDivDirector">
                        <nested:write name="itemDetailsForm" property="item.asstDivDirector.firstAndLastName"/>
                    </nested:present> 
                </td>
			  </tr>
              <tr class='tabledetail'>
			  	<td>Handling Instructions</td>
			  	<td>
                    <nested:write name="itemDetailsForm" property="item.instructions"/>
                </td>
			  </tr>
		</nested:equal>
		</table>
        <a href="javascript:close()">Close</a>

	</body>
</html>