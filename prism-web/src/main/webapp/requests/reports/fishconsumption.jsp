<%@ include file="../../include/tlds.jsp" %>

<jsp:useBean id="purchasereport"
             class="us.mn.state.health.view.materialsrequest.reports.FishConsumptionForm"
             scope="session"/>
<html>
<head>
    <title>Search Purchase Item Transactions</title>
</head>

<body>
<h1 class="text-center" style="color: #f00;">Information in this report is not maintained after October 23, 2014</h1>
<html:form action="/purchasereport">
    <fieldset>
        <legend>Search Purchase Item Transactions</legend>
        <div class="text-center">
            <label>Date From: <nested:text property="dateFrom" size="10" maxlength="10" styleClass="dateInput"/></label>&nbsp;
            <label>Date To: <nested:text property="dateTo" size="10" maxlength="10" styleClass="dateInput"/></label>
        </div>
        <div class="text-center">
            <div class="btn-group">
                <html:submit styleClass="btn btn-default"/>
                <button onclick="this.form.action = 'index.jsp';this.form.submit(); return false;" class="btn btn-default">Cancel</button>
            </div>
        </div>
    </fieldset>
</html:form>
</body>
</html>
<IFRAME id="Defib" src="../../include/Defibrillator.jsp" frameBorder=no width=0 height=0></IFRAME>