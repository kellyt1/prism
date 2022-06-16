<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>

<html>
    <head>
        <title>Stock Item Action Request Confirmation</title>
    </head>
    <body>
        <table style="width: 50%; margin: 0 auto;">
            <tr>
                <td colspan="4">&nbsp;</td>
            </tr>
            <tr>
                <th colspan="2">Stock Item Action Request Confirmation</th>
            </tr>
            <tr>
                <td colspan="4">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">
                    Your request has been submitted.  Your confirmation number is <strong><bean:write name="stockItemActionRequest" property="stockItemActionRequestId"/></strong>
                    Please note the following:
                    <ul>
                        <li>If this request requires approval, none of the changes will take effect until this request has been approved.</li>
                        <li>If this request does not require approval, your changes will take effect immediately. </li>
                        <li>Requests for new stock items must always be approved.</li>
                        <li>Certain stock item status changes currently require approval, such as holds or reactivation.</li>
                        <li>If the request must be approved, the system will notify the people who must approve it via email.</li>
                    </ul>
                </td>
            </tr>
        </table>
    </body>
</html>