<%@ include file="../include/tlds.jsp" %>

<html>
<head><title>Change Prism Notification Messages</title></head>
<body>
<nested:form action="changePrismNotificationsSave">
    <br><br>
    <fieldset>
        <legend>Change Prism Login Page Message:</legend>     
        <nested:textarea rows="10" cols="70" property="prismLoginNotification" />
    </fieldset>
<br><br>
    <fieldset>
        <legend>Change Prism Other Pages Message:</legend>
        <nested:textarea rows="10" cols="70" property="prismEveryPageNotification" />
    </fieldset>

    <br/>
    <br/>
    <html:submit value="Update Prism Notifications"/>
</nested:form>
<br/>

</body>
</html>