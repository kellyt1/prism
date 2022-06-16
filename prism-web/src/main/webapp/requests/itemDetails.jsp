<%@ page import="us.mn.state.health.model.inventory.Item"%>
<%@ include file="../include/tlds.jsp" %>
<html>
	<head>
	    <title>Item Details</title>
	</head>
	
	<body>
        <div class="row">
            <div class="col-md-6 col-md-offset-3">
                <table class="table table-bordered">
                    <tr>
                        <th colspan='2'>Item Details</th>
                    </tr>
                    <tr>
                        <th width="30%" scope="row">Item Description</th>
                        <td><nested:write name="itemDetailsForm" property="item.description" filter="false"/></td>
                    </tr>
                    <tr>
                        <th scope="row">Category Name</th>
                        <td>
                            <nested:present name="itemDetailsForm" property="item.category">
                                <nested:write name="itemDetailsForm" property="item.category.name"/>
                            </nested:present>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">Category Code</th>
                        <td>
                            <nested:present name="itemDetailsForm" property="item.category">
                                <nested:write name="itemDetailsForm" property="item.category.categoryCode"/>
                            </nested:present>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">Dispense Unit</th>
                        <td>
                            <nested:present name="itemDetailsForm" property="item.dispenseUnit">
                                <nested:write name="itemDetailsForm" property="item.dispenseUnit.name"/>
                            </nested:present>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">Manufacturer</th>
                        <td>
                            <nested:present name="itemDetailsForm" property="item.manufacturer">
                                <nested:present name="itemDetailsForm" property="item.manufacturer.externalOrgDetail">
                                    <nested:write name="itemDetailsForm" property="item.manufacturer.externalOrgDetail.orgName"/>
                                </nested:present>
                            </nested:present>
                        </td>
                    </tr>
                    <logic:notEqual name="skin" value="PRISM2">
                        <tr>
                            <th scope="row">Hazardous</th>
                            <td><nested:write name="itemDetailsForm" property="item.hazardous"/></td>
                        </tr>
                    </logic:notEqual>
                    <tr>
                        <th scope="row">Insertion Date</th>
                        <td><nested:write name="itemDetailsForm" property="item.insertionDate"/></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <nested:notEmpty  name="itemDetailsForm" property="item.attachedFiles" >
                                <% java.util.Date dt = new java.util.Date (); %>
                                <%--<br>--%>
                                <i class="attachment">Download/Open File(s):
                                    <nested:iterate name="itemDetailsForm" property="item.attachedFiles" id="attachedFile" >
                                        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<a href=/inventory/downloadFileAction.do?attachedFileId=<nested:write name="attachedFile" property='attachedFileId'/>&dt=<%=dt.getTime()%> ><nested:write name="attachedFile" property='fileName'/> </a>
                                        <br>
                                    </nested:iterate>
                                </i>
                            </nested:notEmpty>
                        </td>
                    </tr>
                    <nested:notEmpty name="itemDetailsForm" property="item.itemVendors">
                        <tr>
                            <td colspan=2>
                                <table cellspacing="3">
                                    <tr>
                                        <th colspan="2" align="center">Vendor information</th>
                                    </tr>
                                    <tr>
                                        <td><i><strong>Vendor(s):</strong></i></td>
                                    </tr>
                                    <nested:iterate property="item.itemVendors" name="itemDetailsForm" id="vendor">
                                        <tr>
                                            <td><nested:write name="vendor" property="vendor.externalOrgDetail.orgName"/></td>
                                        </tr>
                                          <td colspan=4><table cellpadding="2"><tr><td>

        <logic:notEqual name="skin" value="PRISM2">
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
        </logic:notEqual>
                              </tr>

                                              <%--<td><i><b><small>Phone:</small></b></i></td><td><nested:write name="vendor" property="vendor.externalOrgDetail.primaryPhone.number"/></td>--%>
                                              <%--<td><i><b><small>Fax:</small></b></i></td><td><nested:write name="vendor" property="vendor.externalOrgDetail.primaryFax.number"/></td>--%>
                                               <%--<td><i><b><small>Catlg:</small></b></i></td><td><nested:write name="vendor" property="vendorCatalogNbr"/></td>--%>
                                               <%--<td><i><b><small>unit$:</small></b></i></td><td><nested:write name="vendor" property="buyUnitCost"/></td>--%>
                                               <!--<td><i><b><small>Acct(s):</small></b></i></td><td>-->
                                           <%--<nested:iterate name="vendor" property="vendor.vendorAccounts" id="vendorAccount" >--%>
                                                      <%--<nested:write name="vendorAccount" property="accountNbr"/>--%>
                                           <!--<br/>-->
                                           <%--</nested:iterate>--%>
                                           <!--</td>-->
                                        </table>
                                          </nested:iterate>
                                        </table>
                             </td></tr>
                    </nested:notEmpty>
                    <nested:equal value="true" property="isStockItem" name="itemDetailsForm">
                        <tr>
                            <th colspan='2'>Stock Item Details</th>
                        </tr>
                        <tr>
                            <th scope="row">ICNBR</th>
                            <td><nested:write name="itemDetailsForm" property="item.fullIcnbr"/></td>
                        </tr>
                        <tr>
                            <th scope="row">Status</th>
                            <td>
                                <nested:present name="itemDetailsForm" property="item.status">
                                    <nested:write name="itemDetailsForm" property="item.status.name"/>
                                </nested:present>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">Org Budget</th>
                            <td>
                                <nested:present name="itemDetailsForm" property="item.orgBudget">
                                    <nested:write name="itemDetailsForm" property="item.orgBudget.orgBudgetCodeAndNameAndFY"/>
                                </nested:present>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">Qty on Hand</th>
                            <td><nested:write name="itemDetailsForm" property="item.qtyOnHand"/></td>
                        </tr>
                        <tr>
                            <th scope="row">Current Demand</th>
                            <td><nested:write name="itemDetailsForm" property="item.currentDemand"/></td>
                        </tr>
                        <tr>
                            <th scope="row">Seasonal</th>
                            <td><nested:write name="itemDetailsForm" property="item.seasonal"/></td>
                        </tr>
                        <tr>
                            <th scope="row">Primary Contact</th>
                            <td>
                                <nested:present name="itemDetailsForm" property="item.primaryContact">
                                    <nested:write name="itemDetailsForm" property="item.primaryContact.firstAndLastName"/>
                                </nested:present>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">Secondary Contact</th>
                            <td>
                                <nested:present name="itemDetailsForm" property="item.secondaryContact">
                                    <nested:write name="itemDetailsForm" property="item.secondaryContact.firstAndLastName"/>
                                </nested:present>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">Assistant Division Director</th>
                            <td>
                                <nested:present name="itemDetailsForm" property="item.asstDivDirector">
                                    <nested:write name="itemDetailsForm" property="item.asstDivDirector.firstAndLastName"/>
                                </nested:present>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">Handling Instructions</th>
                            <td><nested:write name="itemDetailsForm" property="item.instructions"/></td>
                        </tr>
                    </nested:equal>
                </table>
            </div>
        </div>
        <form method='post'>
		    <div class="row">
                <div class="col-md-3 col-md-offset-3">
                    <input type="BUTTON" value="Go Back" class="btn btn-default" onclick="history.go(-1);"/>
                </div>
                <div class="col-md-3 text-right">
                    <nested:equal value="<%=Item.STOCK_ITEM%>" name="itemDetailsForm" property="item.itemType">
                        <nested:notEqual value="true" name="itemDetailsForm" property="item.discontinued">
                            <%--<input type="image" src="/images/addtocart.gif"
                                   onclick="this.form.action='viewAddCatalogItemToCart.do?itemId=<nested:write name="itemDetailsForm" property="item.itemId"/>'; this.form.submit(); return false;"/>--%>
                            <button type="button" class="btn btn-default" onclick="this.form.action='viewAddCatalogItemToCart.do?itemId=<nested:write name="itemDetailsForm" property="item.itemId"/>'; this.form.submit(); return false;">
                                <span class="glyphicon glyphicon-shopping-cart"></span> Add to Cart
                            </button>
                        </nested:notEqual>
                        <nested:equal value="true" name="itemDetailsForm" property="item.discontinued">
                            <span style="color: red; ">Discontinued</span>
                        </nested:equal>
                    </nested:equal>
                    <nested:notEqual value="<%=Item.STOCK_ITEM%>" name="itemDetailsForm" property="item.itemType">
                            <%--<input type="image" src="/images/addtocart.gif"
                                   onclick="this.form.action='viewAddCatalogItemToCart.do?itemId=<nested:write name="itemDetailsForm" property="item.itemId"/>'; this.form.submit(); return false;"/>--%>
                        <button type="button" class="btn btn-default" onclick="this.form.action='viewAddCatalogItemToCart.do?itemId=<nested:write name="itemDetailsForm" property="item.itemId"/>'; this.form.submit(); return false;">
                            <span class="glyphicon glyphicon-shopping-cart"></span> Add to Cart
                        </button>
                    </nested:notEqual>
                    <input type="HIDDEN" name="shoppingListAction" value="addItm" />
                    <%--<input type="image" src="/images/addtolist.bmp"
                           onclick="this.form.action='viewChooseShoppingList.do?itemId=<nested:write name="itemDetailsForm" property="item.itemId"/>'" />--%>
                    <button type="button" class="btn btn-default" onclick="this.form.action='viewChooseShoppingList.do?itemId=<nested:write name="itemDetailsForm" property="item.itemId"/>'" >
                        <span class="glyphicon glyphicon-list-alt"></span> Add to List
                    </button>
                </div>
            </div>
        </form>
    </body>
</html>