<%@ include file="../include/tlds.jsp" %>

<html>
<head><title>Change Budget Builder Link</title></head>
<body>
<nested:form action="changeBudgetBuilderLinkSave">
    <br><br>
    <fieldset>
        <legend>Change Budget Builder Link:</legend>
        <nested:textarea rows="10" cols="70" property="budgetBuilderLink" />
    </fieldset>

    <br/>
    <br/>
    <html:submit value="Update"/>
</nested:form>
<br/>

</body>
</html>