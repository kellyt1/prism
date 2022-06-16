<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>Enter Asset Info</title>
    </head>
    
    <body>
        <h3>Enter Asset Information</h3>
        <p>
            The following item(s) you are receiving have been flagged as either fixed
            or sensitive asset(s).  Please enter the serial number for any Sensitve Assets listed below, 
            and the serial number and asset number for any Fixed Assets listed.
        </p>
        <nested:form action="enterAssetInfo" method="post">
            <table align="center" cellspacing="0" cellpadding="1" border="0" width="90%">
              <nested:notEmpty property="fixedAssetForms">
                  <tr>
                    <td>
                        <table align="center" cellspacing="0" cellpadding="1" border="0" width="75%">
                            <tr>
                                <th class="tableheader" colspan="4">Fixed Assets</th>
                            </tr>
                            <tr>
                                <th>&nbsp;</th>
                                <th>Item Description:</th>
                                <th>Fixed Asset Number:</th>
                                <th>Serial Number:</th>
                            </tr>
                            <% int i=0; %>
                            <nested:iterate id="fa" property="fixedAssetForms" indexId="a" >
                                <%  i = a % 2;%>
                                <tr class="tabledetail" bgcolor="<%out.write((i==0)?"#cccccc":"white");%>">
                                    <td><%=a +1 %>. </td>
                                    <td class="tabledetail">
                                        <nested:present property="item">
                                            <nested:write property="item.description" />
                                        </nested:present>
                                    </td>
                                    <td><nested:text property="fixedAssetNumber"/></td>
                                    <td><nested:text property="serialNumber"/></td>
                                </tr>
                            </nested:iterate>
                        </table>
                    </td>
                  </tr>
                  <tr>
                    <td>&nbsp;</td>
                  </tr>
                  <tr>
                    <td>&nbsp;</td>
                  </tr>
              </nested:notEmpty>
              <nested:notEmpty property="sensitiveAssetForms">
                  <tr>
                    <td>
                        <table align="center" cellspacing="0" cellpadding="1" border="0" width="75%">
                            <tr>
                                <th class="tableheader" colspan="3">Sensitive Assets</th>
                            </tr>
                            <tr>
                                <th>&nbsp;</th>
                                <th>Item Description:</th>
                                <th>Serial Number:</th>
                            </tr>
                            <% int s=0; %>
                            <nested:iterate id="sa" property="sensitiveAssetForms" indexId="b" >
                                <%  s = b % 2;%>
                                <tr class="tabledetail" bgcolor="<%out.write((s==0)?"#cccccc":"white");%>">
                                    <td><%=b +1 %>. </td>
                                    <td class="tabledetail">
                                        <nested:write property="item.description" />
                                    </td>
                                    <td>
                                      <nested:text property="serialNumber"/>
                                    </td>
                                </tr>
                            </nested:iterate>
                        </table>
                    </td>
                  </tr>
                </nested:notEmpty>
                <tr>
                    <td>&nbsp;</td>
                  </tr>
                <tr>
                    <td align="center">
                        <input type="submit" value="Submit" />
                    </td>
                </tr>
            </table> 
        </nested:form>
    </body>
</html>
