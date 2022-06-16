<%@ include file="../../include/tlds.jsp" %>

<html>
    <head>
        <title>Create Delivery Receipt Report</title>
    </head>
    
    <body>
        <nested:form action="printDeliveryReceiptReport" method="post" target="_blank">
            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <div class="panel panel-default">
                        <div class="panel-heading">Delivery Receipt Report - enter report parameters</div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-xs-6"><label for="facility">Facility:</label></div>
                                <div class="col-xs-6">
                                    <nested:select property="facilityId" styleClass="chosen-select" styleId="facility">
                                        <option value=""></option>
                                        <nested:optionsCollection property="facilities" value="facilityId" label="facilityName" />
                                    </nested:select>
                                </div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-6">Date received between:</div>
                                <div class="col-xs-6">
                                    <label class="hidden" for="fromDate">From Date</label>
                                    <label class="hidden" for="toDate">To Date</label>
                                    <nested:text property="startDate" name="generalReportingForm" size="10" styleClass="dateInput" styleId="fromDate"/>
                                    &nbsp;and&nbsp;
                                    <nested:text property="endDate" name="generalReportingForm" size="10" styleClass="dateInput" styleId="toDate"/>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer text-center"><input type="SUBMIT" value="Create Report" class="btn btn-default"/></div>
                    </div>
                </div>
            </div>
        </nested:form>
    </body>
</html>
