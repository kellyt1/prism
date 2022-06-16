<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>

<html>
    <head>
        <title>Modify Category</title>
    </head>
    <body>
        <html:form action="maintainCategory" method="post">
            <table style="width: 100%; border: none; border-collapse: collapse;">
                <tr>
                    <th>Category</th>
                </tr>
                <tr>
                    <td>
                        <table style="margin: 0 auto;">
                            <tr>
                                <th>Name</th>
                                <th>Code</th>
                                <th>Parent</th>
                            </tr>
                            <tr>
                                <td>
                                    <html:text property="name" size="20"/>
                                </td>
                                <td>
                                    <html:text property="categoryCode" size="20"/>
                                </td>
                                <td>
                                    <nested:notEmpty property="parentCategory">
                                        <nested:write property="parentCategory.name"/>
                                    </nested:notEmpty>
                                </td>
                            </tr>
                            <tr>
                                <td><html:submit value="Save"/></td>
                        </table>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html>