<%@ include file="../include/tlds.jsp" %>

<html>
<head>
    <title>Checkout Confirmation</title>
</head>

<body>
    <p align="center">
        Your request has been submitted.  Your request tracking number is: <nested:write property="trackingNumber" name="requestForm" />
        <br />
        Please keep this number for future reference.
    </p>
    <logic:equal name="skin" value="PRISM2">
                <p>* - After submitting request, if you have made an entry error, or need to change something, <strong>PLEASE DO NOT MAKE ANOTHER ENTRY.</strong>
                       Instead submit an <a href="https://fyi.web.health.state.mn.us/open/helpdesk/" target="_blank">MN.iT service ticket</a> with the mrq # and the change you are requesting and they will work with Purchasing to update the request for you.
                </p>
    </logic:equal>
</body>
</html>