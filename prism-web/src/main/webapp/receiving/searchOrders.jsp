<%@ page import="us.mn.state.health.matmgmt.util.Form"%>
<%@ include file="../include/tlds.jsp" %>

<%
    String message = (String)request.getParameter("message");
%>

<html>
    <head>
        <title>Search Orders</title>
        <script type="text/javascript" src="include/calendar2.js"></script>
    </head>
    
    <body>
        <% 
            if(message != null) { 
                out.write("<p align='center'><strong>");
                out.write(message);
                out.write("</strong></p>");
            }
        %>
        <nested:form action="/searchOrdersForReceiving" method="post">
            <html:hidden property="input"/>
            <html:hidden property="selectedOrderId"/>
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
                  <table align="center" cellpadding="3" cellspacing="0" border="0" bgcolor="#ffffcc">
                    <tr>
                      <td class="tablelabel" align="right">OPR #:</td>
                      <td>
                        <nested:text property="orderId" size="20"/>
                      </td>
                      <td class="tablelabel" align="right">Item Description:</td>
                      <td class="tabledetail">
                        <nested:text property="itemDescription" size="40"/>
                      </td>
                      <td class="tablelabel" align="right">Item Model:</td>
                      <td class="tabledetail">
                        <nested:text property="itemModel" size="10"/>
                      </td>                      
                    </tr>
                    <tr>
                      <td class="tablelabel" align="right">PO #:</td>
                      <td>
                        <nested:text property="purchaseOrderNumber" size="20"/>
                      </td>
                      <td class="tablelabel" align="right">Vendor Name:</td>
                      <td>
                        <nested:text property="vendorName" size="30" />
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
                      <td class="tablelabel" align="right">Request Tracking #:</td>
                      <td>
                        <nested:text property="requestId" size="10"/>
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
                      <td colspan="4" class="tablelabel" align="right">Ordered Between
                        <nested:text property="orderedFrom" size="10"/>
                        <a href="javascript:cal1.popup();"><img src="images/cal.gif" width="16" height="16" border="0"></a>
                        &nbsp;&nbsp; and &nbsp;&nbsp;
                        <nested:text property="orderedTo" size="10"/>
                        <a href="javascript:cal2.popup();"><img src="images/cal.gif" width="16" height="16" border="0"></a>
                      </td>
                      <td class="tablelabel" align="right">Location</td>
                      <td>
                          <nested:select property="mailingAddressId">
                              <option value=""></option>
                              <nested:optionsCollection property="facilities" value="mailingAddressId" label="cityAndAddress" />
                          </nested:select>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="6" align="center">
                        <input type="submit" value="Search" onclick="this.form.action = 'searchOrdersForReceiving.do';"/>
                          &nbsp;
                        <input type="RESET" value="Clear"/>
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
            <nested:form action="searchOrdersForReceiving" method="post">
              <table align="center" cellspacing="0" cellpadding="0" border="0" width="90%">
              <nested:present property="searchResults">
              <nested:hidden property="paginationDirection" value=""  />
              <nested:hidden property="pageNo"/>
              <html:hidden property="selectedOrderId"/>
              <tr>
                  <td>
                      <table width="95%" class="text-center">
                          <tr>
                              <td class="text-left" width="100%" colspan="2"> Your search returned <nested:write
                                      property="resultsNo"/> results!</td>
                          </tr>
                          <tr>
                              <td class="text-right" width="80%">
                                  Page #:
                                  <select>
                                      <nested:iterate property="pages" indexId="a">
                                          <nested:equal value="<%=a.toString()%>" name="searchOrdersForm"
                                                        property="pageNo">
                                              <option value="<%=a%>" selected="">
                                                  <%=a.intValue() + 1%>
                                              </option>
                                          </nested:equal>
                                          <nested:notEqual value="<%=a.toString()%>"
                                                           name="searchOrdersForm" property="pageNo">
                                              <option value="<%=a%>"
                                                      onclick="this.form.pageNo.value='<%=a.toString()%>';this.form.submit(); return false;">
                                                  <%=a.intValue() + 1%>
                                              </option>
                                          </nested:notEqual>
                                      </nested:iterate>
                                  </select>
                              </td>
                              <td class="text-right">
                                  <nested:greaterThan name="searchOrdersForm" property="firstResult"
                                                      value="0">
                                      <a href='searchOrdersForReceiving.do?paginationDirection=<%=Form.PREVIOUS%>'>
                                          &lt; Prev</a> &nbsp;
                                  </nested:greaterThan>
                                  <nested:equal name="searchOrdersForm" property="displayNextLink"
                                                value="true">
                                      <a href="searchOrdersForReceiving.do?paginationDirection=<%=Form.NEXT%>">Next
                                          &gt;</a>
                                  </nested:equal>
                              </td>
                          </tr>
                      </table>
                  </td>
                  </tr>
                  <tr>
                    <td>
                      <table cellpadding="3" border="0" width="100%">
                        <tr>
                          <th>OPR #:</th>
                          <th class="text-left">PO #:</th>
                          <th class="text-left">Vendor:</th>
                          <th>Buyer:</th>
                          <th>Date Created:</th>
                          <th>Delivery Location</th>
                        </tr>
                        <nested:iterate property="searchResults">
                            <tr>
                              <td align="center">
                                <input type="SUBMIT"  
                                       value="<nested:write property='orderId' />"
                                       onclick="this.form.action='viewReceiveOrder.do'; this.form.selectedOrderId.value='<nested:write property='orderId' />' " />
                               </td>
                              <td class="text-left">
                                <nested:write property="purchaseOrderNumber" /> 
                              </td>
                              <td class="text-left">
                                <nested:write property="vendor.externalOrgDetail.orgName" />
                              </td>
                              <td align="center">
                                <nested:write property="purchaser.firstAndLastName" />
                              </td>
                              <td align="center">
                                <nested:write property="insertionDate" format="MM/dd/yyyy"/> 
                              </td>
                                <td align="center">
                                  <nested:present property="shipToAddress">
                                      <nested:write property="shipToAddress.cityAndAddress" />
                                  </nested:present>
                                </td>

                            </tr>
                        </nested:iterate>
                      </table>
                    </td>
                  </tr>
              </nested:present>
            </table>
        </nested:form>
        
        <script type="text/javascript">
			<!-- // create calendar object(s) just after form tag closed
				 // specify form element as the only parameter (document.forms['formname'].elements['inputname']);
				 // note: you can have as many calendar objects as you need for your application -->
				var cal1 = new calendar2(document.forms['searchOrdersForm'].elements['orderedFrom']);
                var cal2 = new calendar2(document.forms['searchOrdersForm'].elements['orderedTo']);
                cal1.year_scroll = true;
				cal1.time_comp = false;
				cal2.year_scroll = true;
				cal2.time_comp = false;
        </script>
    </body>
</html>
