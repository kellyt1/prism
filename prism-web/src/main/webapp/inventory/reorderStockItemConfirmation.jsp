<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<html>
    <head>
        <title>Re-Order Stock Item Confirmation</title>
    </head>
    <body>
        <nested:form action="/reorderStockItem" method="post">
            <p align="center">
                Your request has been submitted.  Your request tracking number is: <nested:write property="trackingNumber" />
                <br />
                Please keep this number for future reference.
            </p>
        </nested:form>
    </body>
</html>