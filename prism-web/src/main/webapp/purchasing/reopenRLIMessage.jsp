<%@ include file="../include/tlds.jsp" %>


<html>
    <head>
        <title>Reopen Request Line Item</title>
    </head>

    <body>
        <html:form action="/reopenselectedRLIs" method="post">
            <label title="Message to send with reopened Request Lines" >Message For Reopened Request Line(s)
             <nested:iterate property="requestLineItemForms">
                <nested:write property="requestLineItem.request.trackingNumber"/>,
            </nested:iterate>
            </label>
            <br/><nested:textarea rows="10" cols="70" property="noteFormKey" />
            <br/>

            <br/>
            <button onclick="this.form.submit(); return false;" class="btn btn-default">Re-Open selected RLIs and send email to requestor.</button>
        </html:form>
    </body>
</html>