<%@ include file="../../include/tlds.jsp" %>

<html>
    <head>
        <title>Create 'To Be Stocked' Report</title>
        <script type="text/javascript" src="../../js/common.js"></script>
    </head>
    
    <body>
        <nested:form action="printToBeStockedReport" method="post" target="_blank">
            <table width="90%" align="center">
                <tr>
                    <th colspan="4" class="tableheader" valign="middle" align="center">
                        Received Stock Items To Be Stocked - enter report parameters
                    </th>
                </tr>
                <tr>
                    <td colspan="4">&nbsp;</td>
                </tr>
                <tr>
                    <td nowrap="nowrap" colspan="1" class="tablelabel" align="right">
                        Facility: &nbsp; 
                    </td>
                    <td nowrap="nowrap" colspan="3" class="tabledetail" align="left">
                        <nested:select property="facilityId">
                            <option value=""></option>
                            <nested:optionsCollection property="facilities" value="facilityId" label="facilityName" />
                        </nested:select> 
                    </td>
                </tr>
                <tr>
                    <td colspan="4">&nbsp;</td>
                </tr>
                <tr>
                    <td nowrap="nowrap" colspan="1" class="tablelabel" align="right">
                        Date received between: &nbsp; 
                    </td>
                    <td nowrap="nowrap" colspan="2" class="tablelabel" align="left">
                        <nested:text property="startDate" name="generalReportingForm" size="10" styleClass="dateInput"/>
                         &nbsp;and&nbsp;
                        <nested:text property="endDate" name="generalReportingForm" size="10" styleClass="dateInput"/>
                      </td>
                </tr>
                <tr>
                    <td colspan="4">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="4" align="center">
                        <input type="SUBMIT" value="Create Report" />
                    </td>
                </tr>
            </table>
        </nested:form>
    </body>
</html>
