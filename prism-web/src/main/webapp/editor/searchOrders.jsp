<%@ include file="../include/tlds.jsp" %>
<%@ page import="us.mn.state.health.matmgmt.util.Form" %>

<html>

<head>
    <title>Search Orders</title>
    <script type="text/javascript">

     function clearForm() {
         var form = window.document.forms[0];
    var currentTime = new Date();
    var month = currentTime.getMonth() + 1;
    var day = currentTime.getDate();
    var year = currentTime.getFullYear();
    var currentdate = month + "/" + day + "/" + year;

    //     alert("BEGIN");
              form.orderId.value = "";
              form.purchaseOrderNumber.value = "";

              form.orderedFrom.value = "";
              form.orderedTo.value = currentdate;
              form.itemDescription.value = "";
              form.vendorName.value = "";
                form.requestId.value = "";
                form.suspenseDateFrom.value = "";
            form.suspenseDateTo.value = "";
            form.vendorCatalogNbr.value = "";

        form.purchaserId.selectedIndex = 0;
        form.requestorId.selectedIndex = 0;
        form.statusId.selectedIndex = 0;
         form.orgBudgetCode.value = "";


      //       alert("END");
     }



    </script>
</head>

<body>
<html:form action="/searchOrdersForEditor.do" method="post">
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
                <table align="center" cellpadding="3" cellspacing="0" border="0" bgcolor="#ffffcc">
                    <tr>
                        <td class="tablelabel" align="right">OPR #:</td>
                        <td>
                            <html:text property="orderId" size="20"/>
                        </td>
                        <td class="tablelabel" align="right">Item Description:</td>
                        <td colspan="1">
                            <html:text property="itemDescription" size="40"/>
                        </td><td colspan=2> <table><tr>
                        <td class="tablelabel" align="right">Catalog #:</td>
                        <td colspan="1">
                            <html:text property="vendorCatalogNbr" size="10"/>
                        </td>
                        <td class="tablelabel" align="right">&nbsp;&nbsp;Budget Code:</td>
                        <td colspan="1">
                            <html:text property="orgBudgetCode" size="12"/>
                        </td>
                        </tr></table></td>

                    </tr>
                    <tr>
                        <td class="tablelabel" align="right">PO #:</td>
                        <td>
                            <html:text property="purchaseOrderNumber" size="20"/>
                        </td>
                        <td class="tablelabel" align="right">Vendor Name:</td>
                        <td>
                            <html:text property="vendorName" size="30"/>
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
                            <input type="submit" value="Search"
                                   onclick="this.form.action='searchOrdersForEditor.do';"/>&nbsp;
                                               <input type="BUTTON" value="Clear" onclick="clearForm();" />
                            <button onclick="this.form.action='/editor/index.jsp';this.form.submit(); return false;">
                                Cancel</button>
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
<html:form action="/searchOrdersForEditor.do" method="post">
    <table align="center" cellspacing="0" cellpadding="0" border="0" width="90%">
        <nested:present property="searchResults">
            <nested:hidden property="paginationDirection" value=""/>
            <nested:hidden property="pageNo"/>
            <nested:hidden property="orderId"/>
            <tr>
                <td>
                    <table width="95%" align="center">
                        <tr>
                            <td align="left" width="100%" colspan="2"> Your search returned <nested:write
                                    property="resultsNo"/> results!</td>
                        </tr>
                        <tr>
                            <td align="right" width="80%">
                                Page #:
                                <select>
                                    <nested:iterate property="pages" indexId="a">
                                        <nested:equal value="<%=a.toString()%>" name="searchOrdersForm"
                                                      property="pageNo">
                                            <option value="<%=a%>" selected="">
                                                <%=a + 1%>
                                            </option>
                                        </nested:equal>
                                        <nested:notEqual value="<%=a.toString()%>"
                                                         name="searchOrdersForm" property="pageNo">
                                            <option value="<%=a%>"
                                                    onclick="this.form.pageNo.value='<%=a.toString()%>';this.form.submit(); return false;">
                                                <%=a + 1%>
                                            </option>
                                        </nested:notEqual>
                                    </nested:iterate>
                                </select>
                            </td>
                            <td align="right">
                                <nested:greaterThan name="searchOrdersForm" property="firstResult"
                                                    value="0">
                                    <a href='searchOrdersForEditor.do?paginationDirection=<%=Form.PREVIOUS%>'>
                                        &lt; Prev</a> &nbsp;
                                </nested:greaterThan>
                                <nested:equal name="searchOrdersForm" property="displayNextLink"
                                              value="true">
                                    <a href="searchOrdersForEditor.do?paginationDirection=<%=Form.NEXT%>">Next
                                        &gt;</a>
                                </nested:equal>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <table cellspacing="0" cellpadding="3" border="0" width="100%">
                        <tr>
                            <th>OPR #:</th>
                            <th align="left">PO #:</th>
                            <th align="left">Vendor:</th>
                            <th>Purchaser:</th>
                            <th>Date Created:</th>
                            <th>Suspense Date:</th>
                        </tr>
                        <nested:iterate property="searchResults">
                            <tr>
                                <td align="center">
                                    <input type="SUBMIT"
                                           value="<nested:write property='orderId'/>"
                                           onclick="this.form.action='viewEditOrder2.do'; this.form.orderId.value='<nested:write property='orderId'/>'"/>
                                </td>
                                <td align="left">
                                    <nested:write property="purchaseOrderNumber"/>
                                </td>
                                <td align="left">
                                    <nested:write property="vendor.externalOrgDetail.orgName"/>
                                </td>
                                <td align="center">
                                    <nested:write property="purchaser.firstAndLastName"/>
                                </td>
                                <td align="center">
                                    <nested:write property="insertionDate" format="MM/dd/yyyy"/>
                                </td>
                                <td align="center">
                                    <nested:write property="suspenseDate" format="MM/dd/yyyy"/>
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